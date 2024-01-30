using System;
using System.Collections.Generic;

namespace Elektrom.Models
{
    public partial class Kategorijos
    {
        public Kategorijos()
        {
            InverseFkKategorijaidKategorijaNavigation = new HashSet<Kategorijos>();
            PrekesKategorijos = new HashSet<PrekesKategorijos>();
        }

        public string Pavadinimas { get; set; } = null!;
        public string Aprasymas { get; set; } = null!;
        public DateOnly AtnaujinimoData { get; set; }
        public int IdKategorija { get; set; }
        public int? FkKategorijaidKategorija { get; set; }

        public virtual Kategorijos? FkKategorijaidKategorijaNavigation { get; set; }
        public virtual ICollection<Kategorijos> InverseFkKategorijaidKategorijaNavigation { get; set; }
        public virtual ICollection<PrekesKategorijos> PrekesKategorijos { get; set; }
    }
}
