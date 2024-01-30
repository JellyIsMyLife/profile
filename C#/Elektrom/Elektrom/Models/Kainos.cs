using System;
using System.Collections.Generic;

namespace Elektrom.Models
{
    public partial class Kainos
    {
        public DateOnly Data { get; set; }
        public double Kaina { get; set; }
        public int IdKaina { get; set; }
        public int FkPrekeidPreke { get; set; }

        public virtual Prekes? FkPrekeidPrekeNavigation { get; set; } = null!;
    }
}
