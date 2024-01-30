using System;
using System.Collections.Generic;

namespace Elektrom.Models
{
    public partial class Naudotojai
    {
        public Naudotojai()
        {
            Atsiliepimais = new HashSet<Atsiliepimai>();
            Uzsakymais = new HashSet<Uzsakymai>();
        }

        public Naudotojai(string vardas, string pavarde, string pastas, string tipas, string tel)
        {
            Atsiliepimais = new HashSet<Atsiliepimai>();
            Uzsakymais = new HashSet<Uzsakymai>();
            Vardas = vardas;
            Pavarde = pavarde;
            Pastas = pastas;
            Tipas = tipas;
            TelNumeris = tel;
        }

        public string? Vardas { get; set; } = null!;
        public string? Pavarde { get; set; } = null!;
        public string Pastas { get; set; } = null!;
        public string Slaptazodis { get; set; } = null!;
        public string Tipas { get; set; } = null!;
        public string? TelNumeris { get; set; } = null!;
        public DateOnly RegistracijosData { get; set; }
        public DateOnly PaskutinisPrisijungimas { get; set; }
        public int IdNaudotojas { get; set; }
        public int? FkAdresasidAdresas { get; set; }

        public virtual Adresai FkAdresasidAdresasNavigation { get; set; } = null!;
        public virtual Krepseliai? Krepseliai { get; set; }
        public virtual ICollection<Atsiliepimai> Atsiliepimais { get; set; }
        public virtual ICollection<Uzsakymai> Uzsakymais { get; set; }
    }
}
