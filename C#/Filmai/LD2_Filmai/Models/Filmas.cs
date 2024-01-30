using System;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace LD2_Filmai.Models
{
    public class Filmas
    {
        [DisplayName("ID")]
        [Required]
        public int id { get; set; }
        [DisplayName("Pavadinimas")]
        [Required]
        public string pavadinimas { get; set; }
        [DisplayName("Išleidimo metai")]
        [Required]
        public int isleidimoMetai { get; set; }
        [DisplayName("Filmo trukmė")]
        [Required]
        public TimeSpan trukme { get; set; }
        [DisplayName("Žanras")]
        [Required]
        public string zanras { get; set; }
    }
}