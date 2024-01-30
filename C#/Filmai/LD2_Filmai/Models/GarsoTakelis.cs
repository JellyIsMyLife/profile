using System;

namespace LD2_Filmai.Models
{
    public class GarsoTakelis
    {
        public int id { get; set; }
        public string atlikejas { get; set; }
        public string pavadinimas { get; set; }
        public int isleidimoMetai { get; set; }
        public string zanras { get; set; }
        public string albumas { get; set; }
        public int fk_filmas { get; set; }
    }
}