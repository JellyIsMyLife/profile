using Elektrom.Models;

namespace Elektrom.Utils
{
    public class CartItem
    {
        public Prekes preke { get; set; }
        public int kiekis { get; set; }

        public CartItem(Prekes preke, int kiekis)
        {
            this.preke = preke;
            this.kiekis = kiekis;
        }
    }
}
