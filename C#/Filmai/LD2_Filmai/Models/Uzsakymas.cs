using System;

namespace LD2_Filmai.Models
{
    public class Uzsakymas
    {
        public int id { get; set; }
        public DateTime uzsakymoData { get; set; }
        public DateTime nuomosPradziosData { get; set; }
        public DateTime nuomosPabaigosData { get; set; }
        public double kaina { get; set; }
        public int fk_filmu_parduotuve { get; set; }
        public int fk_klientas { get; set; }
        public int fk_filmas { get; set; }
        public int fk_saskaita { get; set; }
    }
}