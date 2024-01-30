using System;
using System.Collections.Generic;
using System.Web.Mvc;
using LD2_Filmai.Repos;
using LD2_Filmai.ViewModels;

namespace LD2_Filmai.Controllers
{
    public class GarsoTakelisController : Controller
    {
        GarsoTakelisRepository garsoTakelisRepository = new GarsoTakelisRepository();
        FilmasRepository filmasRepository = new FilmasRepository();

        public void PopulateSelections(GarsoTakelisEditViewModel garsoTakelis)
        {
            var filmai = filmasRepository.getFilmai();
            List<SelectListItem> selectListFilmai = new List<SelectListItem>();

            foreach (var item in filmai)
            {
                selectListFilmai.Add(new SelectListItem() { Value = Convert.ToString(item.id), Text = item.pavadinimas });
            }

            garsoTakelis.filmuList = selectListFilmai;
        }

        public ActionResult Index()
        {
            return View(garsoTakelisRepository.getGarsoTakeliai());
        }

        public ActionResult Create()
        {
            GarsoTakelisEditViewModel garsoTakelis = new GarsoTakelisEditViewModel();
            PopulateSelections(garsoTakelis);

            return View(garsoTakelis);
        }

        [HttpPost]
        public ActionResult Create(GarsoTakelisEditViewModel collection)
        {
            try
            {
                //collection.id = ++Counters.garsoTakelisCounter;
                GarsoTakelisEditViewModel tmpGarsoTakelis = garsoTakelisRepository.getGarsoTakelis(collection.id);

                if (tmpGarsoTakelis.id < 0)
                {
                    ModelState.AddModelError("id", "Garso takelis su tokiu ID jau egzistuoja duomenų bazėje.");
                }

                if (ModelState.IsValid)
                    garsoTakelisRepository.addGarsoTakelis(collection);

                return RedirectToAction("Index");
            }
            catch
            {
                ViewBag.klaida = "Įvyko nenumatyta klaida.";
                PopulateSelections(collection);
                return View(collection);
            }
        }

        public ActionResult Edit(int id)
        {
            GarsoTakelisEditViewModel garsoTakelis = garsoTakelisRepository.getGarsoTakelis(id);
            PopulateSelections(garsoTakelis);

            return View(garsoTakelis);
        }

        [HttpPost]
        public ActionResult Edit(int id, GarsoTakelisEditViewModel collection)
        {
            try
            {
                if (ModelState.IsValid)
                    garsoTakelisRepository.updateGarsoTakelis(collection);

                return RedirectToAction("Index");
            }
            catch
            {
                ViewBag.klaida = "Įvyko nenumatyta klaida.";
                PopulateSelections(collection);
                return View(collection);
            }
        }

        public ActionResult Delete(int id)
        {
            GarsoTakelisEditViewModel garsoTakelis = garsoTakelisRepository.getGarsoTakelis(id);
            return View(garsoTakelis);
        }

        [HttpPost]
        public ActionResult Delete(int id, FormCollection collection)
        {
            try
            {
                garsoTakelisRepository.deleteGarsoTakelis(id);

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