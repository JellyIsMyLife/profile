using System;
using System.Collections.Generic;

namespace Elektrom.Models
{
    public partial class PrekesKategorijos
    {
        public int FkKategorijaidKategorija { get; set; }
        public int FkPrekeidPreke { get; set; }

        public virtual Kategorijos FkKategorijaidKategorijaNavigation { get; set; } = null!;
    }
}
