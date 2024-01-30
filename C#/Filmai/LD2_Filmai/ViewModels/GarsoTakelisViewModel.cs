using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace LD2_Filmai.ViewModels
{
    public class GarsoTakelisViewModel
    {
        [DisplayName("ID")]
        [Required]
        public int id { get; set; }
        [DisplayName("Atlikėjas")]
        [Required]
        public string atlikejas { get; set; }
        [DisplayName("Pavadinimas")]
        [Required]
        public string pavadinimas { get; set; }
        [DisplayName("Išleidimo metai")]
        [Required]
        public int isleidimoMetai { get; set; }
        [DisplayName("Filmas")]
        [Required]
        public string filmas { get; set; }
    }
}