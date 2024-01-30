using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Web.Mvc;

namespace LD2_Filmai.ViewModels
{
    public class GarsoTakelisEditViewModel
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
        [DisplayName("Žanras")]
        [Required]
        public string zanras { get; set; }
        [DisplayName("Albumas")]
        [Required]
        public string albumas { get; set; }
        [DisplayName("Filmas")]
        [Required]
        public int fk_filmas { get; set; }

        public IList<SelectListItem> filmuList { get; set; }
    }
}