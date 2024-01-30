using System;
using System.Collections.Generic;

namespace Elektrom.Models
{
    public partial class Prekes
    {
        public Prekes()
        {
            Atsiliepimais = new HashSet<Atsiliepimai>();
            Kainos = new HashSet<Kainos>();
            KrepselioPrekes = new HashSet<KrepselioPrekes>();
            Nuotraukos = new HashSet<Nuotraukos>();
            UzsakymuPrekes = new HashSet<UzsakymuPrekes>();
        }

        public string Pavadinimas { get; set; } = null!;
        public int Kiekis { get; set; }
        public string Aprasymas { get; set; } = null!;
        public DateOnly AtnaujinimoData { get; set; }
        public bool Rodymas { get; set; }
        public int IdPreke { get; set; }
        public int? FkAtsiliepimuStatistikaidAtsiliepimuStatistika { get; set; }

        public virtual AtsiliepimuStatistikos? FkAtsiliepimuStatistikaidAtsiliepimuStatistikaNavigation { get; set; } = null!;
        public virtual ICollection<Atsiliepimai> Atsiliepimais { get; set; }
        public virtual ICollection<Kainos> Kainos { get; set; }
        public virtual ICollection<KrepselioPrekes> KrepselioPrekes { get; set; }
        public virtual ICollection<Nuotraukos> Nuotraukos { get; set; }
        public virtual ICollection<UzsakymuPrekes> UzsakymuPrekes { get; set; }
    }
}
