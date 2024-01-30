using System;
using System.Collections.Generic;

namespace Elektrom.Models
{
    public partial class Uzsakymai
    {
        public Uzsakymai()
        {
            UzsakymuPrekes = new HashSet<UzsakymuPrekes>();
        }

        public DateOnly Data { get; set; }
        public double Suma { get; set; }
        public string Statusas { get; set; } = null!;
        public int PrekiuKiekis { get; set; }
        public string Pristatymas { get; set; }
        public int IdUzsakymas { get; set; }
        public int? FkNaudotojasidNaudotojas { get; set; }

        public virtual Naudotojai FkNaudotojasidNaudotojasNavigation { get; set; } = null!;
        public virtual ICollection<UzsakymuPrekes> UzsakymuPrekes { get; set; }
    }
}
