using System.Collections.Generic;
using System.Web.Mvc;
using LD2_Filmai.Repos;
using LD2_Filmai.Models;

namespace LD2_Filmai.Controllers
{
    public class FilmasController : Controller
    {
        FilmasRepository filmasRepository = new FilmasRepository();

        public ActionResult Index()
        {
            return View(filmasRepository.getFilmai());
        }

        public ActionResult Create()
        {
            Filmas filmas = new Filmas();
            return View(filmas);
        }

        [HttpPost]
        public ActionResult Create(Filmas collection)
        {
            try
            {
                collection.id = ++Counters.filmasCounter;
                Filmas tmpFilmas = filmasRepository.getFilmas(collection.id);

                if (tmpFilmas.id < 0)
                {
                    ModelState.AddModelError("id", "Filmas su tokiu ID jau egzistuoja duomenų bazėje.");
                }

                if (ModelState.IsValid)
                    filmasRepository.addFilmas(collection);
                return RedirectToAction("Index");
            }
            catch
            {
                ViewBag.klaida = "Įvyko nenumatyta klaida.";
                return View();
            }
        }

        public ActionResult Edit(int id)
        {
            return View(filmasRepository.getFilmas(id));
        }

        [HttpPost]
        public ActionResult Edit(int id, Filmas collection)
        {
            try
            {
                if (ModelState.IsValid)
                    filmasRepository.updateFilmas(collection);

                return RedirectToAction("Index");
            }
            catch
            {
                ViewBag.klaida = "Įvyko nenumatyta klaida.";
                return View(collection);
            }
        }

        public ActionResult Delete(int id)
        {
            return View(filmasRepository.getFilmas(id));
        }

        [HttpPost]
        public ActionResult Delete(int id, FormCollection collection)
        {
            try
            {
                filmasRepository.deleteFilmas(id);

                return RedirectToAction("Index");
            }
            catch
            {
                ViewBag.klaida = "Įvyko nenumatyta klaida.";
                return View();
            }
        }
    }
}