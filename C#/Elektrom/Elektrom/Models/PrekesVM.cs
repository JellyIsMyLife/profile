namespace Elektrom.Models
{
    public class PrekesVM
    {
        public Prekes prekes { get; set; }
        public Kainos kainos {get;set;}

        public Nuotraukos? nuotraukos { get; set; }

        public IFormFile? image { get; set; }
    }
}
