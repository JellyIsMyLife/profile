using Elektrom.Areas.Identity.Data;
using Microsoft.AspNetCore.Mvc;
using Elektrom.Models;
using Microsoft.AspNetCore.Identity;
using NuGet.Versioning;


namespace Elektrom.Controllers;

public class CommentsController : Controller
{
    private dbcontext _db;
    private readonly UserManager<AuthUser> _userManager;

    // GET: ArticlesComments
    public CommentsController(dbcontext db, UserManager<AuthUser> user)
    {
        _db = db;
        _userManager = user;
    }

    [HttpPost]
    [ValidateAntiForgeryToken]
    public async Task<ActionResult> Add(IFormCollection form)
    {
        var comment = form["Komentaras"].ToString();
        var prekeID = int.Parse(form["PrekeID"].ToString());
        var rating = int.Parse(form["Ivertinimas"].ToString());

        int? temp;
        if (HttpContext.User.Identity.IsAuthenticated)
        {
            var aspnetuser = await _userManager.GetUserAsync(User);
            temp = aspnetuser.naudotojasId;
        }
        else
        {
            return Redirect("/Identity/Account/Login");
        }

        int idKlientas = temp ?? default(int);
        
        Atsiliepimai ats = new Atsiliepimai()
        {
            FkPrekeidPreke = prekeID,
            FkNaudotojasidNaudotojas = idKlientas,
            Komentaras = comment,
            Ivertinimas = rating,
            SukurimoLaikas = DateOnly.FromDateTime(DateTime.Now),
            AtnaujinimoLaikas = DateOnly.FromDateTime(DateTime.Now),
        };

        _db.Atsiliepimai.Add(ats);
        _db.SaveChanges();

        return RedirectToAction("Index", "Item", new { id = prekeID });
    }

    public ActionResult Edit(int? id)
    {
        if (id == null)
        {
            System.Console.WriteLine("nėra id");
            return RedirectToAction("Error", "Home");
        }
        Atsiliepimai atsiliepimai = _db.Atsiliepimai.Find(id);
        if (atsiliepimai == null)
        {
            System.Console.WriteLine("nėra sql");
            return RedirectToAction("Error", "Home");
        }
        return View(atsiliepimai);
    }

    [HttpPost]
    [ValidateAntiForgeryToken]
    public ActionResult Edit(IFormCollection form)
    {
        var id = int.Parse(form["atsID"].ToString());
        var comment = form["Komentaras"].ToString();
        var prekeID = int.Parse(form["PrekeID"].ToString());
        var rating = int.Parse(form["Ivertinimas"].ToString());
        DateOnly sukLaikas = DateOnly.Parse(form["SukLaikas"].ToString());
        Atsiliepimai ats = new Atsiliepimai()
        {
            FkPrekeidPreke = prekeID,
            FkNaudotojasidNaudotojas = 1, //TODO session id
            Komentaras = comment,
            Ivertinimas = rating,
            SukurimoLaikas = sukLaikas,
            AtnaujinimoLaikas = DateOnly.FromDateTime(DateTime.Now),
        };
        var atsiliepimas = _db.Atsiliepimai.Find(id);

        _db.Remove(atsiliepimas);
        _db.Add(ats);
        _db.SaveChanges();
        
        return RedirectToAction("Index", "Item", new {id = prekeID});
    }
    
    public ActionResult Delete(int? id)
    {
        if (id == null)
        {
            return RedirectToAction("Error", "Home");
        }

        IEnumerable<Atsiliepimai> atsiliepimai = _db.Atsiliepimai;
        IEnumerable<Naudotojai> NaudotojaiList = _db.Naudotojai.ToList();
        IEnumerable<AtsiliepimaiVM> objList = from a in atsiliepimai
                join n in NaudotojaiList on a.FkNaudotojasidNaudotojas equals n.IdNaudotojas
                select new AtsiliepimaiVM
                {
                    ats = a, 
                    userName = (String.Format(n.Vardas + " " + n.Pavarde))
                };
        AtsiliepimaiVM ats = objList.ToList().Find(x => x.ats.IdAtsiliepimas == id);
        if (ats == null)
        {
            return RedirectToAction("Error", "Home");
        }
        
        return View(ats);
    }
    
    [HttpPost, ActionName("Delete")]
    [ValidateAntiForgeryToken]
    public ActionResult DeleteConfirmed(int id)
    {
        Atsiliepimai ats = _db.Atsiliepimai.Find(id);
        int prekeID = ats.FkPrekeidPreke;
        _db.Atsiliepimai.Remove(ats);
        _db.SaveChanges();
        return RedirectToAction("Index", "Item", new {id = prekeID});
    }
}