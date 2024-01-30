using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace LD2_Filmai.ViewModels
{
    public class ReitingasViewModel
    {
        [DisplayName("Filmas")]
        [Required]
        public string filmas { get; set; }
        [DisplayName("Metacritic")]
        [Required]
        public int metacritic { get; set; }
        [DisplayName("Rotten Tomatoes")]
        [Required]
        public int rottenTomatoes { get; set; }
        [DisplayName("IMDb")]
        [Required]
        public double imdb { get; set; }
        [DisplayName("ID")]
        [Required]
        public int id { get; set; }
    }
}