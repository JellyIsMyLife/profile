using System;
using System.Collections.Generic;

namespace Elektrom.Models
{
    public partial class Adresai
    {
        public Adresai()
        {
            Naudotojais = new HashSet<Naudotojai>();
        }

        public Adresai(string salis, string miestas, string gatve, int namoNumeris, int butoNumeris, string pastoKodas, string savivaldybe, DateOnly atnaujinimoData)
        {
            Naudotojais = new HashSet<Naudotojai>();
            Salis = salis;
            Miestas = miestas;
            Gatve = gatve;
            NamoNumeris = namoNumeris;
            ButoNumeris = butoNumeris;
            PastoKodas = pastoKodas;
            Savivaldybe = savivaldybe;
            AtnaujinimoData = atnaujinimoData;
        }

        public string Salis { get; set; } = null!;
        public string Miestas { get; set; } = null!;
        public string Gatve { get; set; } = null!;
        public int NamoNumeris { get; set; }
        public int ButoNumeris { get; set; }
        public string PastoKodas { get; set; } = null!;
        public string Savivaldybe { get; set; } = null!;
        public DateOnly AtnaujinimoData { get; set; }
        public int IdAdresas { get; set; }

        public virtual ICollection<Naudotojai> Naudotojais { get; set; }
    }
}
