using System;
using System.Collections.Generic;

namespace Elektrom.Models
{
    public partial class UzsakymuPrekes
    {
        public UzsakymuPrekes(int kiekis, DateOnly atnaujinimoData, int fkUzsakymasidUzsakymas, int fkPrekeidPreke)
        {
            Kiekis = kiekis;
            AtnaujinimoData = atnaujinimoData;
            FkUzsakymasidUzsakymas = fkUzsakymasidUzsakymas;
            FkPrekeidPreke = fkPrekeidPreke;
        }

        public int Kiekis { get; set; }
        public DateOnly AtnaujinimoData { get; set; }
        public int IdUzsakymoPrekes { get; set; }
        public int FkUzsakymasidUzsakymas { get; set; }
        public int FkPrekeidPreke { get; set; }

        public virtual Prekes FkPrekeidPrekeNavigation { get; set; } = null!;
        public virtual Uzsakymai FkUzsakymasidUzsakymasNavigation { get; set; } = null!;
    }
}
