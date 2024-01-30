using Elektrom.Models;
using Elektrom.Utils;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json;

namespace Elektrom.Controllers
{
    public class ShoppingCartController : Controller
    {
        private readonly dbcontext _db;
        public ShoppingCartController(dbcontext db)
        {
            _db = db;
        }

        [Route("Cart")]
        public IActionResult Index()
        {
            List<(Prekes, Kategorijos, int)> items = new List<(Prekes, Kategorijos, int)>();
            string? prekesJson = HttpContext.Session.GetString("ShoppingCart");
            if (prekesJson != null)
            {
                List<CartItem> cartItems = JsonConvert.DeserializeObject<List<CartItem>>(prekesJson);
                List<Prekes> allItems = _db.Prekes
                    .Include(x => x.Kainos)
                    .ToList();

                foreach (CartItem cartItem in cartItems)
                {
                    int categoryID = _db.PrekesKategorijos.First(x => x.FkPrekeidPreke == cartItem.preke.IdPreke).FkKategorijaidKategorija;
                    Kategorijos category = _db.Kategorijos.First(x => x.IdKategorija == categoryID);
                    Prekes preke = allItems.First(x => x.IdPreke == cartItem.preke.IdPreke);
                    items.Add((preke, category, cartItem.kiekis));
                }
            }

            return View(items);
        }

        [Route("Cart/Add")]
        public IActionResult AddItem()
        {
            List<Prekes> prekes = _db.Prekes.ToList();
            return View(prekes);
        }

        [Route("Cart/Add")]
        [ValidateAntiForgeryToken]
        [HttpPost]
        public IActionResult AddItemAndRedirect(int id, int quantity)
        {
            string? prekesJson = HttpContext.Session.GetString("ShoppingCart");
            Prekes? preke = _db.Prekes
                    .Include(x => x.Nuotraukos)
                    .Include(x => x.Kainos)
                    .Where(x => x.IdPreke == id)
                    .FirstOrDefault();
            if (prekesJson != null)
            {
                List<CartItem> prekes = JsonConvert.DeserializeObject<List<CartItem>>(prekesJson);
                if (preke == null)
                {
                    return RedirectToAction("Index");
                }
                int cartItemIndex = prekes.FindIndex(x => x.preke.IdPreke == preke.IdPreke);
                if (cartItemIndex >= 0)
                {
                    prekes[cartItemIndex].kiekis += quantity;
                }
                else
                {
                    prekes.Add(new CartItem(preke, quantity));
                }

                HttpContext.Session.SetString("ShoppingCart", JsonConvert.SerializeObject(prekes, new JsonSerializerSettings()
                {
                    ReferenceLoopHandling = ReferenceLoopHandling.Ignore
                }));
            }
            else
            {
                List<CartItem> prekes = new List<CartItem>();
                if (preke == null)
                {
                    return RedirectToAction("Index");
                }
                prekes.Add(new CartItem(preke, quantity));

                HttpContext.Session.SetString("ShoppingCart", JsonConvert.SerializeObject(prekes, new JsonSerializerSettings()
                {
                    ReferenceLoopHandling = ReferenceLoopHandling.Ignore
                }));
            }

            return RedirectToAction("Index");
        }

        [Route("Cart/Remove")]
        [HttpGet]
        public IActionResult RemoveItem(int id)
        {
            string? prekesJson = HttpContext.Session.GetString("ShoppingCart");
            Prekes? preke = _db.Prekes
                    .Include(x => x.Nuotraukos)
                    .Include(x => x.Kainos)
                    .Where(x => x.IdPreke == id)
                    .FirstOrDefault();
            if (prekesJson != null)
            {
                List<CartItem> prekes = JsonConvert.DeserializeObject<List<CartItem>>(prekesJson);
                prekes.RemoveAll(x => x.preke.IdPreke == id);
                HttpContext.Session.SetString("ShoppingCart", JsonConvert.SerializeObject(prekes, new JsonSerializerSettings()
                {
                    ReferenceLoopHandling = ReferenceLoopHandling.Ignore
                }));
            }
            return RedirectToAction("index");
        }

        [Route("Cart/Update")]
        [HttpGet]
        public async Task<IActionResult> UpdateItem(int id, int amount)
        {
            string? prekesJson = HttpContext.Session.GetString("ShoppingCart");
            var preke = await _db.Prekes.FindAsync(id);
            if (prekesJson != null)
            {
                List<CartItem> prekes = JsonConvert.DeserializeObject<List<CartItem>>(prekesJson);
                var item = prekes.Find(x => x.preke.IdPreke == id);
                item.kiekis = amount;
                HttpContext.Session.SetString("ShoppingCart", JsonConvert.SerializeObject(prekes, new JsonSerializerSettings()
                {
                    ReferenceLoopHandling = ReferenceLoopHandling.Ignore
                }));
            }
            return Ok();
        }
    }
}
