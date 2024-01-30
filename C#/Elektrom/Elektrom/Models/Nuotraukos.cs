using System;
using System.Collections.Generic;

namespace Elektrom.Models
{
    public partial class Nuotraukos
    {
        public string Pavadinimas { get; set; } = null!;
        public int Plotis { get; set; }
        public int Aukstis { get; set; }
        public decimal Dydis { get; set; }
        public string Formatas { get; set; } = null!;
        public int IdNuotrauka { get; set; }
        public int FkPrekeidPreke { get; set; }

        public virtual Prekes? FkPrekeidPrekeNavigation { get; set; } = null!;
    }
}
