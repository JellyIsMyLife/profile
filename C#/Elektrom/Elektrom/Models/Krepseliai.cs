using System;
using System.Collections.Generic;

namespace Elektrom.Models
{
    public partial class Krepseliai
    {
        public Krepseliai()
        {
            KrepselioPrekes = new HashSet<KrepselioPrekes>();
        }

        public double Suma { get; set; }
        public int PrekiuKiekis { get; set; }
        public int IdKrepselis { get; set; }
        public int FkNaudotojasidNaudotojas { get; set; }

        public virtual Naudotojai FkNaudotojasidNaudotojasNavigation { get; set; } = null!;
        public virtual ICollection<KrepselioPrekes> KrepselioPrekes { get; set; }
    }
}
