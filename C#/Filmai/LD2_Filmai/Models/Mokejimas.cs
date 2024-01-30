using System;
using System.Collections.Generic;
using System.Web.Mvc;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace LD2_Filmai.Models
{
    public class Mokejimas
    {
        [DisplayName("ID")]
        public int id { get; set; }
        [DisplayName("Data")]
        [Required]
        public DateTime data { get; set; }
        [DisplayName("Suma")]
        [Required]
        public double suma { get; set; }
        [DisplayName("Klientas")]
        [Required]
        public int fk_klientas { get; set; }
        [DisplayName("Sąskaita")]
        [Required]
        public int fk_saskaita { get; set; }

        public IList<SelectListItem> klientaiList { get; set; }
        public IList<SelectListItem> saskaitosList { get; set; }
    }
}