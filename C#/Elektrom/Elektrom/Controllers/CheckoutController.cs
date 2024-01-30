using Elektrom.Areas.Identity.Data;
using Elektrom.Models;
using Elektrom.Utils;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;

namespace Elektrom.Controllers
{
    public class CheckoutController : Controller
    {
        private readonly dbcontext _db;
        private readonly UserManager<AuthUser> _userManager;

        public CheckoutController(dbcontext db, UserManager<AuthUser> userManager)
        {
            _db = db;
            _userManager = userManager;
        }

        public async Task<IActionResult> UserInformation()
        {
            string? prekesJson = HttpContext.Session.GetString("ShoppingCart");
            if (prekesJson == null)
            {
                return RedirectToAction("Index", "ShoppingCart");
            }

            Naudotojai user = new Naudotojai();
            if (User.Identity.IsAuthenticated)
            {
                var aspnetuser = await _userManager.GetUserAsync(User);
                user = _db.Naudotojai.Find(aspnetuser.naudotojasId);
            }
            return View(user);
        }

        [HttpPost]
        public IActionResult UserInformation(string firstName, string lastName, string email, string phoneNumber)
        {
            var emailChecker = new EmailAddressAttribute();

            if (!emailChecker.IsValid(email)) return RedirectToAction("UserInformation");

            Naudotojai guest = new Naudotojai(firstName, lastName, email, "Svecias", phoneNumber);

            HttpContext.Session.SetString("guest", JsonConvert.SerializeObject(guest));

            return RedirectToAction("Delivery");
        }

        public IActionResult Delivery()
        {
            string? guest = HttpContext.Session.GetString("guest");
            if (User.Identity.IsAuthenticated == false || guest == null)
            {
                return Redirect("/");
            }
            return View();
        }

        [HttpPost]
        public async Task<IActionResult> Delivery(IFormCollection form)
        {
            Uzsakymai order = new Uzsakymai();

            if (form.ContainsKey("courierOptions"))
            {
                order.Pristatymas = form["courierOptions"];
                var atnaujinimoData = DateOnly.FromDateTime(DateTime.Now);

                var appartmentNumber = form["appartmentNumber"];
                if (appartmentNumber == "")
                {
                    appartmentNumber = "0";
                }

                if (User.Identity.IsAuthenticated)
                {
                    var user = await _userManager.GetUserAsync(User);
                    int? userId = user.naudotojasId;
                    var userInfo = _db.Naudotojai.Find(userId);
                    if (userInfo.FkAdresasidAdresas == null)
                    {
                        Adresai adress = new Adresai(form["country"], form["city"], form["street"], int.Parse(form["streetNumber"]), int.Parse(appartmentNumber), form["postalCode"], form["municipality"], atnaujinimoData);
                        _db.Add(adress);
                        _db.SaveChanges();

                        userInfo.FkAdresasidAdresas = _db.Adresai.Where(x => x.AtnaujinimoData == atnaujinimoData).Select(x => x.IdAdresas).FirstOrDefault();
                        _db.SaveChanges();
                    }
                    else
                    {
                        Adresai adress = _db.Adresai.Where(x => x.AtnaujinimoData == atnaujinimoData).FirstOrDefault();
                        adress.Salis = form["country"];
                        adress.Miestas = form["city"];
                        adress.Gatve = form["street"];
                        adress.NamoNumeris = int.Parse(form["streetNumber"]);
                        adress.ButoNumeris = int.Parse(appartmentNumber);
                        adress.PastoKodas = form["postalCode"];
                        adress.Savivaldybe = form["municipality"];
                        _db.SaveChanges();
                    }
                }
                else
                {
                    string? guestJson = HttpContext.Session.GetString("guest");
                    Naudotojai guest = JsonConvert.DeserializeObject<Naudotojai>(guestJson);

                    Adresai adress = new Adresai(form["country"], form["city"], form["street"], int.Parse(form["streetNumber"]), int.Parse(appartmentNumber), form["postalCode"], form["municipality"], atnaujinimoData);
                    _db.Add(adress);
                    _db.SaveChanges();

                    guest.FkAdresasidAdresas = _db.Adresai.Where(x => x.AtnaujinimoData == atnaujinimoData).Select(x => x.IdAdresas).FirstOrDefault();

                    HttpContext.Session.SetString("guest", JsonConvert.SerializeObject(guest));
                }
            }
            else if (form.ContainsKey("pickupOptions"))
            {
                order.Pristatymas = form["pickupOptions"];
            }
            else
            {
                return Redirect("/");
            }

            HttpContext.Session.SetString("order", JsonConvert.SerializeObject(order));

            return RedirectToAction("Payment");
        }

        public IActionResult Payment()
        {
            string? guest = HttpContext.Session.GetString("guest");
            string? order = HttpContext.Session.GetString("order");

            if (User.Identity.IsAuthenticated == false || guest == null || order == null)
            {
                return Redirect("/");
            }

            string? prekesJson = HttpContext.Session.GetString("ShoppingCart");
            List<CartItem> prekes = JsonConvert.DeserializeObject<List<CartItem>>(prekesJson);

            double sum = Math.Round(prekes.Sum(x => x.preke.Kainos.ToList()[0].Kaina * x.kiekis), 2);

            return View(sum);
        }

        [HttpPost]
        public IActionResult Payment(IFormCollection form)
        {
            if (form["cardNumber"].ToString().Any(char.IsDigit))
            {
                Card card = new Card(long.Parse(form["cardNumber"]), form["cardHolder"], form["expiration"], int.Parse(form["cvv"]));
                HttpContext.Session.SetString("card", JsonConvert.SerializeObject(card));
            }

            return RedirectToAction("Confirmation");
        }

        public async Task<IActionResult> Confirmation()
        {
            string? guestJson = HttpContext.Session.GetString("guest");
            string? orderJson = HttpContext.Session.GetString("order");

            if (User.Identity.IsAuthenticated == false && (guestJson == null || orderJson == null))
            {
                return Redirect("/");
            }

            List<(Prekes, Kategorijos, int)> items = new List<(Prekes, Kategorijos, int)>();
            string? prekesJson = HttpContext.Session.GetString("ShoppingCart");
            List<CartItem> prekes = JsonConvert.DeserializeObject<List<CartItem>>(prekesJson);
            Uzsakymai order = JsonConvert.DeserializeObject<Uzsakymai>(orderJson);

            List<CartItem> cartItems = JsonConvert.DeserializeObject<List<CartItem>>(prekesJson);
            List<Prekes> allItems = _db.Prekes
                .Include(x => x.Nuotraukos)
                .Include(x => x.Kainos)
                .ToList();

            string? cardJson = HttpContext.Session.GetString("card");

            foreach (CartItem cartItem in cartItems)
            {
                int categoryID = _db.PrekesKategorijos.First(x => x.FkPrekeidPreke == cartItem.preke.IdPreke).FkKategorijaidKategorija;
                Kategorijos category = _db.Kategorijos.First(x => x.IdKategorija == categoryID);
                Prekes preke = allItems.First(x => x.IdPreke == cartItem.preke.IdPreke);

                items.Add((preke, category, cartItem.kiekis));
            }

            if (guestJson == null)
            {
                var aspnetuser = await _userManager.GetUserAsync(User);
                int? userId = aspnetuser.naudotojasId;
                Naudotojai user = _db.Naudotojai.Find(userId);
                Adresai adress = _db.Adresai.Find(user.FkAdresasidAdresas);

                if (cardJson == null)
                {
                    return View((items, order, user, adress, new Card()));
                }

                Card card = JsonConvert.DeserializeObject<Card>(cardJson);

                return View((items, order, user, adress, card));
            }

            Naudotojai guest = JsonConvert.DeserializeObject<Naudotojai>(guestJson);
            Adresai adress2 = _db.Adresai.Find(guest.FkAdresasidAdresas);

            if (cardJson == null)
            {
                return View((items, order, guest, adress2, new Card()));
            }

            Card card2 = JsonConvert.DeserializeObject<Card>(cardJson);

            return View((items, order, guest, adress2, card2));
        }

        public IActionResult OrderComplete()
        {
            return View();
        }

        public async Task<IActionResult> OrderInvoice()
        {
            string? orderJson = HttpContext.Session.GetString("order");
            string? prekesJson = HttpContext.Session.GetString("ShoppingCart");
            Uzsakymai order = JsonConvert.DeserializeObject<Uzsakymai>(orderJson);
            List<CartItem> prekes = JsonConvert.DeserializeObject<List<CartItem>>(prekesJson);

            if (order.IdUzsakymas == null)
            {
                return Redirect("/");
            }

            List<(Prekes, int)> items = new List<(Prekes, int)>();
            List<Prekes> allItems = _db.Prekes
                .Include(x => x.Nuotraukos)
                .Include(x => x.Kainos)
                .ToList();

            foreach (CartItem cartItem in prekes)
            {
                int categoryID = _db.PrekesKategorijos.First(x => x.FkPrekeidPreke == cartItem.preke.IdPreke).FkKategorijaidKategorija;
                Prekes preke = allItems.First(x => x.IdPreke == cartItem.preke.IdPreke);

                items.Add((preke, cartItem.kiekis));
            }

            return View((items, order));
        }

        public IActionResult Clear()
        {
            HttpContext.Session.Remove("order");
            HttpContext.Session.Remove("ShoppingCart");
            HttpContext.Session.Remove("card");
            HttpContext.Session.Remove("guest");

            return RedirectToAction("Index", "Home");
        }

        [HttpPost]
        public async Task<IActionResult> CreateOrder()
        {
            string? guestJson = HttpContext.Session.GetString("guest");
            string? orderJson = HttpContext.Session.GetString("order");
            string? cardJson = HttpContext.Session.GetString("card");
            string? prekesJson = HttpContext.Session.GetString("ShoppingCart");

            List<CartItem> prekes = JsonConvert.DeserializeObject<List<CartItem>>(prekesJson);
            Uzsakymai order = JsonConvert.DeserializeObject<Uzsakymai>(orderJson);

            if (guestJson == null)
            {
                var aspnetuser = await _userManager.GetUserAsync(User);
                int? userId = aspnetuser.naudotojasId;
                //Naudotojai user = _db.Naudotojai.Find(userId);

                order.PrekiuKiekis = 0;
                order.Suma = 0;
                order.Statusas = "Kuriama";
                order.FkNaudotojasidNaudotojas = userId;
                order.Data = DateOnly.FromDateTime(DateTime.Now);


                _db.Add(order);
                _db.SaveChanges();
                Uzsakymai orderId = _db.Uzsakymai.Where(x => x.Statusas == "Kuriama").FirstOrDefault();
                orderId.Statusas = "Užsakymas sukurtas";

                foreach (var preke in prekes)
                {
                    UzsakymuPrekes uzsakymoPreke = new UzsakymuPrekes(preke.kiekis, DateOnly.FromDateTime(DateTime.Now), orderId.IdUzsakymas, preke.preke.IdPreke);
                    orderId.Suma += preke.preke.Kainos.ToList()[0].Kaina * preke.kiekis;
                    orderId.PrekiuKiekis += preke.kiekis;
                    _db.Add(uzsakymoPreke);
                }
                _db.SaveChanges();
            }
            else
            {
                Naudotojai guest = JsonConvert.DeserializeObject<Naudotojai>(guestJson);
                _db.Add(new Naudotojai(guest.Vardas, guest.Pavarde, guest.Pastas, "Svecias", guest.TelNumeris));
                _db.SaveChanges();

                int guestId = _db.Naudotojai.Where(x => x.Vardas == guest.Vardas && x.Pavarde == guest.Pavarde).Select(x => x.IdNaudotojas).FirstOrDefault();
                order.PrekiuKiekis = 0;
                order.Suma = 0;
                order.Statusas = "Kuriama";
                order.FkNaudotojasidNaudotojas = guestId;
                order.Data = DateOnly.FromDateTime(DateTime.Now);

                _db.Add(order);
                _db.SaveChanges();
                Uzsakymai orderId = _db.Uzsakymai.Where(x => x.Statusas == "Kuriama").FirstOrDefault();
                orderId.Statusas = "Užsakymas sukurtas";

                foreach (var preke in prekes)
                {
                    UzsakymuPrekes uzsakymoPreke = new UzsakymuPrekes(preke.kiekis, DateOnly.FromDateTime(DateTime.Now), orderId.IdUzsakymas, preke.preke.IdPreke);
                    orderId.Suma += preke.preke.Kainos.ToList()[0].Kaina * preke.kiekis;
                    orderId.PrekiuKiekis += preke.kiekis;
                    _db.Add(uzsakymoPreke);
                }
                _db.SaveChanges();
            }

            HttpContext.Session.SetString("order", JsonConvert.SerializeObject(order, new JsonSerializerSettings()
            {
                ReferenceLoopHandling = ReferenceLoopHandling.Ignore
            }));

            return RedirectToAction("OrderComplete");
        }
    }
}