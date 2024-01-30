using System.Collections;
using System.Diagnostics;
using System.Drawing.Printing;
using System.IO;
using Elektrom.Areas.Identity.Data;
using Elektrom.Models;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.CodeAnalysis.Operations;
using Microsoft.EntityFrameworkCore;

namespace Elektrom.Controllers
{
    //[Route("Item")]
    public class ItemController : Controller
    {
        private readonly dbcontext _db;
        private readonly UserManager<AuthUser> _userManager;
        public ItemController(dbcontext db, UserManager<AuthUser> user)
        {
            _db = db;
            _userManager = user;
        }


        public async Task<IActionResult> Index(int? id)
        {
            int? idKlientas = 0;
            if (HttpContext.User.Identity.IsAuthenticated)
            {
                var aspnetuser = await _userManager.GetUserAsync(User);
                idKlientas = aspnetuser.naudotojasId;
            }

            ViewBag.idKlientas = idKlientas;
            
            List<Prekes> prekes = _db.Prekes.Include(x => x.Nuotraukos)
                .Include(x => x.Kainos).Where(x => x.IdPreke == id).ToList();
            if (prekes == null || prekes.Count > 1)
            {
                return RedirectToAction("Error", "Home");
            }
            Prekes preke = prekes.First();
            List<PrekesKategorijos> rysiai = _db.PrekesKategorijos
                .Where(d => d.FkPrekeidPreke.Equals(preke.IdPreke)).ToList();
            if (rysiai.Count == 0)
            {
                ViewBag.kategorija = "Nepriskirta";
            }
            else
            {
                List<Kategorijos> temp1 = _db.Kategorijos.AsEnumerable()
                    .Where(x => rysiai
                        .Any(y => y
                            .FkKategorijaidKategorija == x.FkKategorijaidKategorija)).ToList();
                String kategorijos = "";
                for(int i = 0; i < temp1.Count; i++)
                {
                    if (i == temp1.Count - 1)
                    {
                        kategorijos += temp1[i].Pavadinimas + ".";
                    }
                    else
                    {
                        kategorijos += temp1[i].Pavadinimas + "; ";
                    }
                }
                ViewBag.kategorija = kategorijos;
            }
            
            ViewBag.PrekeID = id;

            List<Nuotraukos> nuotraukos = _db.Nuotraukos.Where(x => x.FkPrekeidPreke == id).ToList();
            

            if (nuotraukos.Count == 0)
            {
                ViewBag.Nuotrauka = "test.png";
            } 
            else
            {
                ViewBag.Nuotrauka = nuotraukos.First().Pavadinimas;
            }

            // Add comments for item
            IEnumerable<Atsiliepimai> AtsiliepimaiList = _db.Atsiliepimai.Where(d => d.FkPrekeidPreke.Equals(id)).ToList();
            IEnumerable<Naudotojai> NaudotojaiList = _db.Naudotojai.ToList();
            IEnumerable<AtsiliepimaiVM> objList = from a in AtsiliepimaiList
                join n in NaudotojaiList on a.FkNaudotojasidNaudotojas equals n.IdNaudotojas
                select new AtsiliepimaiVM
                {
                    ats = a, 
                    userName = (String.Format(n.Vardas + " " + n.Pavarde))
                };
            ViewBag.Comments = objList;
            int count = AtsiliepimaiList.Count(a => a.FkNaudotojasidNaudotojas == idKlientas);
            ViewBag.CommentNumber = count;
            
            // TODO comment ratings
            // var ratings = db.ArticlesComments.Where(d => d.ArticleId.Equals(id.Value)).ToList();
            // if (ratings.Count() > 0)
            // {
            //     var ratingSum = ratings.Sum(d => d.Rating.Value);
            //     ViewBag.RatingSum = ratingSum;
            //     var ratingCount = ratings.Count();
            //     ViewBag.RatingCount = ratingCount;
            // }
            // else
            // {
            //     ViewBag.RatingSum = 0;
            //     ViewBag.RatingCount = 0;
            // }
            
            return View(preke);
        }
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }
            var preke = await _db.Prekes.FindAsync(id);
            Kainos kaina = _db.Kainos.Where(x => x.FkPrekeidPreke == id).ToList().Last();
            //var nuotrauka = _db.Nuotraukos.Where(x => x.FkPrekeidPreke == id).ToList().First();
            PrekesVM prekevm = new PrekesVM { prekes = preke, kainos = kaina/*, nuotraukos = nuotrauka*/ };
            if (preke == null)
            {
                return NotFound();
            }
            return View(prekevm);
        }

        [HttpPost, ActionName("Edit")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> EditPost(int? id, [FromForm] PrekesVM preke)
        {
            if (id == null)
            {

                return NotFound();
            }

            try
            {
                if (ModelState.IsValid)
                {
                    //preke.image
                    var today = new DateOnly(DateTime.Today.Year, DateTime.Today.Month, DateTime.Today.Day);
                    var itemToUpdate = await _db.Prekes.FirstOrDefaultAsync(s => s.IdPreke == id);
                    itemToUpdate.Pavadinimas = preke.prekes.Pavadinimas;
                    itemToUpdate.Kiekis = preke.prekes.Kiekis;
                    itemToUpdate.Aprasymas = preke.prekes.Aprasymas;
                    itemToUpdate.AtnaujinimoData = today;
                    _db.Update(itemToUpdate);
                    await _db.SaveChangesAsync();
                    preke.kainos.Data = today;
                    preke.kainos.FkPrekeidPreke = itemToUpdate.IdPreke;
                    _db.Add(preke.kainos);
                    await _db.SaveChangesAsync();
                    return RedirectToAction(nameof(Index), new { id = id });
                }
            }
            catch (DbUpdateException /* ex */)
            {
                //Log the error (uncomment ex variable name and write a log.
                ModelState.AddModelError("", "Unable to save changes. " +
                    "Try again, and if the problem persists " +
                    "see your system administrator.");
            }
            return View(preke);
        }
        public IActionResult Create()
        {
            return View();
        }

        // POST: Students/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create(
            [FromForm] PrekesVM preke)
        {

            try
            {
                if (ModelState.IsValid)
                {
                    
                    var today = new DateOnly(DateTime.Today.Year, DateTime.Today.Month, DateTime.Today.Day);
                    AtsiliepimuStatistikos atsiliepimai = new AtsiliepimuStatistikos { AtsiliepimoVidurkis = 0, Data = today, AtnaujinimoData = today, SavaitesDidziausiasVidurkis = 0, MenesioDidziausiasVidurkis = 0, MetuDidziausiasVidurkis = 0 };
                    _db.Add(atsiliepimai);
                    await _db.SaveChangesAsync();
                    Console.WriteLine(atsiliepimai.IdAtsiliepimuStatistika);
                    preke.prekes.FkAtsiliepimuStatistikaidAtsiliepimuStatistika = atsiliepimai.IdAtsiliepimuStatistika;
                    preke.prekes.AtnaujinimoData = today;
                    preke.prekes.Rodymas = true;
                    _db.Add(preke.prekes);
                    await _db.SaveChangesAsync();
                    preke.kainos.Data = today;
                    preke.kainos.FkPrekeidPreke = preke.prekes.IdPreke;
                    _db.Add(preke.kainos);
                    await _db.SaveChangesAsync();
                    

                    preke.nuotraukos = new Nuotraukos { Pavadinimas = preke.prekes.IdPreke + "_", 
                        Plotis = 0, Aukstis = 0, Dydis = preke.image.Length, Formatas = System.IO.Path.GetExtension(preke.image.FileName), FkPrekeidPreke = preke.prekes.IdPreke};
                    _db.Add(preke.nuotraukos);
                    await _db.SaveChangesAsync();
                    preke.nuotraukos.Pavadinimas = preke.nuotraukos.Pavadinimas + preke.nuotraukos.IdNuotrauka + preke.nuotraukos.Formatas;
                    _db.Update(preke.nuotraukos);
                    await _db.SaveChangesAsync();

                    string path = Path.GetFullPath(Path.Combine(Environment.CurrentDirectory, "wwwroot\\images"));
                    using (var fileStream = new FileStream(Path.Combine(path, preke.nuotraukos.Pavadinimas), FileMode.Create))
                    {
                        await preke.image.CopyToAsync(fileStream);
                    }
                    return RedirectToAction(nameof(Index), new {id = preke.prekes.IdPreke});
                }
            }
            catch (DbUpdateException /* ex */)
            {
                //Log the error (uncomment ex variable name and write a log.
                ModelState.AddModelError("", "Unable to save changes. " +
                    "Try again, and if the problem persists " +
                    "see your system administrator.");
            }
            return View(preke);
        }
        public async Task<IActionResult> Delete(int? id, bool? saveChangesError = false)
        {
            if (id == null)
            {
                return NotFound();
            }

            var preke = await _db.Prekes
                .AsNoTracking()
                .FirstOrDefaultAsync(m => m.IdPreke == id);
            if (preke == null)
            {
                return NotFound();
            }

            if (saveChangesError.GetValueOrDefault())
            {
                ViewData["ErrorMessage"] =
                    "Delete failed. Try again, and if the problem persists " +
                    "see your system administrator.";
            }

            return View(preke);
        }

        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var preke = await _db.Prekes.FindAsync(id);
            if (preke == null)
            {
                return RedirectToAction(nameof(Index));
            }

            try
            {
                List<Kainos> kainos = _db.Kainos.Where(x => x.FkPrekeidPreke == id).ToList();
                foreach(Kainos kaina in kainos)
                {
                    _db.Remove(kaina);
                    await _db.SaveChangesAsync();
                }
                List<Nuotraukos> nuotraukos = _db.Nuotraukos.Where(x => x.FkPrekeidPreke == id).ToList();
                foreach (Nuotraukos nuotrauka in nuotraukos)
                {
                    _db.Remove(nuotrauka);
                    await _db.SaveChangesAsync();
                }
                _db.Prekes.Remove(preke);
                await _db.SaveChangesAsync();
                return RedirectToAction("Index" , "Catalog", new { page = 1 });
            }
            catch (DbUpdateException /* ex */)
            {
                //Log the error (uncomment ex variable name and write a log.)
                return RedirectToAction(nameof(Delete), new { id = id, saveChangesError = true });
            }
        }
    }
}
