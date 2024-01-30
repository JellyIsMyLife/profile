using System;
using System.Collections.Generic;

namespace Elektrom.Models
{
    public partial class KrepselioPrekes
    {
        public int Kiekis { get; set; }
        public DateOnly AtnaujinimoData { get; set; }
        public int IdKrepselioPreke { get; set; }
        public int FkKrepselisidKrepselis { get; set; }
        public int FkPrekeidPreke { get; set; }

        public virtual Krepseliai FkKrepselisidKrepselisNavigation { get; set; } = null!;
        public virtual Prekes FkPrekeidPrekeNavigation { get; set; } = null!;
    }
}
