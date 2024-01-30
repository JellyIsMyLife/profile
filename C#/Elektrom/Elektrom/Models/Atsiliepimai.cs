using System;
using System.Collections.Generic;

namespace Elektrom.Models
{
    public partial class Atsiliepimai
    {
        public int Ivertinimas { get; set; }
        public string Komentaras { get; set; } = null!;
        public DateOnly SukurimoLaikas { get; set; }
        public DateOnly AtnaujinimoLaikas { get; set; }
        public int IdAtsiliepimas { get; set; }
        public int FkNaudotojasidNaudotojas { get; set; }
        public int FkPrekeidPreke { get; set; }

        public virtual Naudotojai FkNaudotojasidNaudotojasNavigation { get; set; } = null!;
        public virtual Prekes FkPrekeidPrekeNavigation { get; set; } = null!;
    }
}
