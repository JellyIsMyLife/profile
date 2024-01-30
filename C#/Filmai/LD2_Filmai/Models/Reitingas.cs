namespace LD2_Filmai.Models
{
    public class Reitingas
    {
        public int id { get; set; }
        public int metacritic { get; set; }
        public int rottenTomatoes { get; set; }
        public double imbd { get; set; }
        public int rogerEbert { get; set; }
        public int theGuardian { get; set; }
        public int fk_filmas { get; set; }
    }
}