using System;
using System.Collections.Generic;
using System.Web.Mvc;
using LD2_Filmai.Repos;
using LD2_Filmai.ViewModels;

namespace LD2_Filmai.Controllers
{
    public class ReitingasController : Controller
    {
        ReitingasRepository reitingasRepository = new ReitingasRepository();
        FilmasRepository filmasRepository = new FilmasRepository();

        public void PopulateSelections(ReitingasEditViewModel reitingas)
        {
            var filmai = filmasRepository.getFilmai();
            List<SelectListItem> selectListFilmai = new List<SelectListItem>();

            foreach (var item in filmai)
            {
                selectListFilmai.Add(new SelectListItem() { Value = Convert.ToString(item.id), Text = item.pavadinimas });
            }

            reitingas.filmuList = selectListFilmai;
        }

        public ActionResult Index()
        {
            return View(reitingasRepository.getReitingai());
        }

        public ActionResult Create()
        {
            ReitingasEditViewModel reitingas = new ReitingasEditViewModel();
            PopulateSelections(reitingas);

            return View(reitingas);
        }

        [HttpPost]
        public ActionResult Create(ReitingasEditViewModel collection)
        {
            try
            {

                if (ModelState.IsValid)
                    reitingasRepository.addReitingas(collection);
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
            ReitingasEditViewModel reitingas = reitingasRepository.getReitingas(id);
            PopulateSelections(reitingas);

            return View(reitingas);
        }

        [HttpPost]
        public ActionResult Edit(int id, ReitingasEditViewModel collection)
        {
            try
            {
                if (ModelState.IsValid)
                    reitingasRepository.updateReitingas(collection);

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
            ReitingasEditViewModel reitingas = reitingasRepository.getReitingas(id);
            return View(reitingas);
        }

        [HttpPost]
        public ActionResult Delete(int id, FormCollection collection)
        {
            try
            {
                reitingasRepository.deleteReitingas(id);

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