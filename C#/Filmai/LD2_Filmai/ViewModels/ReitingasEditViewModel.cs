using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Web.Mvc;

namespace LD2_Filmai.ViewModels
{
    public class ReitingasEditViewModel
    {
        [DisplayName("Filmas")]
        [Required]
        public int fk_filmas { get; set; }
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
        public int id { get; set; }
        [DisplayName("Roger Ebert")]
        public int rogerEbert { get; set; }
        [DisplayName("The Guardian")]
        public int theGuardian { get; set; }

        public IList<SelectListItem> filmuList { get; set; }
    }
}