using System;
using System.Collections.Generic;

namespace Elektrom.Models
{
    public partial class AtsiliepimuStatistikos
    {
        public double AtsiliepimoVidurkis { get; set; }
        public DateOnly Data { get; set; }
        public DateOnly AtnaujinimoData { get; set; }
        public double SavaitesDidziausiasVidurkis { get; set; }
        public double MenesioDidziausiasVidurkis { get; set; }
        public double MetuDidziausiasVidurkis { get; set; }
        public int IdAtsiliepimuStatistika { get; set; }

        public virtual Prekes? Preke { get; set; }
    }
}
