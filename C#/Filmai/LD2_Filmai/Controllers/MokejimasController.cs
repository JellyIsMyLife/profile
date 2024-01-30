using System;
using System.Collections.Generic;
using System.Web.Mvc;
using LD2_Filmai.Repos;
using LD2_Filmai.Models;

namespace LD2_Filmai.Controllers
{
    public class MokejimasController : Controller
    {
        MokejimasRepository mokejimasRepository = new MokejimasRepository();
        KlientasRepository klientasRepository = new KlientasRepository();
        SaskaitaRepository saskaitaRepository = new SaskaitaRepository();

        public void PopulateSelections(Mokejimas mokejimas)
        {
            var klientai = klientasRepository.getKlientai();
            var saskaitos = saskaitaRepository.getSaskaitos();

            List<SelectListItem> selectListKlientai = new List<SelectListItem>();
            List<SelectListItem> selectListSaskaitos = new List<SelectListItem>();

            foreach (var item in klientai)
            {
                selectListKlientai.Add(new SelectListItem() { Value = Convert.ToString(item.asmensKodas), Text = item.pavarde });
            }

            foreach (var item in saskaitos)
            {
                selectListSaskaitos.Add(new SelectListItem() { Value = Convert.ToString(item.nr), Text = Convert.ToString(item.data) });
            }

            mokejimas.klientaiList = selectListKlientai;
            mokejimas.saskaitosList = selectListSaskaitos;
        }

        public ActionResult Index()
        {
            return View(mokejimasRepository.getMokejimai());
        }

        public ActionResult Create()
        {
            Mokejimas mokejimas = new Mokejimas();
            PopulateSelections(mokejimas);

            return View(mokejimas);
        }

        [HttpPost]
        public ActionResult Create(Mokejimas collection)
        {
            try
            {
                //collection.id = ++Counters.mokejimasCounter;
                Mokejimas tmpMokejimas = mokejimasRepository.getMokejimas(collection.id);

                if(tmpMokejimas.id < 0)
                    ModelState.AddModelError("id", "Mokėjimas su tokiu ID jau egzistuoja duomenų bazėje.");

                if(ModelState.IsValid)
                    mokejimasRepository.addMokejimas(collection);

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
            Mokejimas mokejimas = mokejimasRepository.getMokejimas(id);
            PopulateSelections(mokejimas);
            return View(mokejimas);
        }

        [HttpPost]
        public ActionResult Edit(int id, Mokejimas collection)
        {
            try
            {
                mokejimasRepository.updateMokejimas(collection);

                return RedirectToAction("Index");
            }
            catch
            {
                PopulateSelections(collection);
                return View(collection);
            }
        }

        public ActionResult Delete(int id)
        {
            Mokejimas mokejimas = mokejimasRepository.getMokejimas(id);
            return View(mokejimas);
        }

        [HttpPost]
        public ActionResult Delete(int id, FormCollection collection)
        {
            try
            {
                mokejimasRepository.deleteMokejimas(id);

                return RedirectToAction("Index");
            }
            catch
            {
                return View();
            }
        }
    }
}