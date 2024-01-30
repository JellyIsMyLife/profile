using Elektrom.Models;
using Elektrom.Utils;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Data.SqlClient;
using Microsoft.EntityFrameworkCore;

namespace Elektrom.Controllers
{
    [Route("Catalog")]
    public class CatalogController : Controller
    {
        private readonly dbcontext _db;

        public CatalogController(dbcontext db)
        {
            _db = db;
        }
        [HttpGet]
        public IActionResult Index(string page, string sortOrder, string lower, string higher, string[] searchString)
        {
            ViewData["NameSortParm"] = string.IsNullOrEmpty(sortOrder) ? "name_desc" : "";
            ViewData["PriceSortParm"] = sortOrder == "Price" ? "price_desc" : "Price";

            if (string.IsNullOrEmpty(lower))
                lower = "0";
            if (string.IsNullOrEmpty(higher))
                higher = "9000";

            double lowerP = double.Parse(lower);
            double higherP = double.Parse(higher);

            var prekes = _db.Prekes.Include(x=>x.Kainos).ToList();
            if (string.IsNullOrEmpty(page))
            {
                page = "1";
            }
            ViewData["PageNum"] = page;

            if (searchString.Any())
            {
                foreach (var str in searchString)
                    prekes = prekes.Where(item => _db.PrekesKategorijos.Where(x => x.FkKategorijaidKategorija == int.Parse(str)).Select(x => x.FkPrekeidPreke).ToList().Contains(item.IdPreke)).ToList();
            }
            prekes = prekes.Where(item => item.Kainos.ElementAt(0).Kaina >= lowerP && item.Kainos.ElementAt(0).Kaina <= higherP).ToList();
            switch (sortOrder)
            {
                case "name_desc":
                    prekes = prekes.OrderByDescending(s => s.Pavadinimas).ToList();
                    break;
                case "Price":
                    prekes = prekes.OrderBy(s => s.Kainos.ElementAt(0).Kaina).ToList();
                    break;
                case "price_desc":
                    prekes = prekes.OrderByDescending(s => s.Kainos.ElementAt(0).Kaina).ToList();
                    break;
                default:
                    prekes = prekes.OrderBy(s => s.Pavadinimas).ToList();
                    break;
            }

            Table table = new Table(prekes);
            table.page = int.Parse(page);
            table.Categories(_db.Kategorijos.ToList());
            table.order = sortOrder;
            

            return View(table);
        }


        [Route("Catalog/Add")]
        public IActionResult Add()
        {
            return View(_db.Prekes.Where(x => x.Rodymas == false).ToList());
        }
        [Route("Catalog/Add")]
        [HttpPost]
        public IActionResult Add(string[] show)
        {
            foreach (var str in show)
            {
                int id = int.Parse(str);
                var q = _db.Prekes.FirstOrDefault(s => s.IdPreke == id);
                if (q != null)
                {
                    q.Rodymas = true;
                }

            }
            _db.SaveChanges();
            return View(_db.Prekes.Where(x => x.Rodymas == false).ToList());
        }

        [Route("Catalog/Remove")]
        public IActionResult Remove()
        {
            return View(_db.Prekes.Where(x => x.Rodymas == true).ToList());
        }
        [Route("Catalog/Remove")]
        [HttpPost]
        public IActionResult Remove(string[] show)
        {
            foreach (var str in show)
            {
                int id = int.Parse(str);
                var q = _db.Prekes.FirstOrDefault(s => s.IdPreke == id);
                if (q != null)
                {
                    q.Rodymas = false;
                }

            }
            _db.SaveChanges();
            return View(_db.Prekes.Where(x => x.Rodymas == true).ToList());
        }
    }
}
