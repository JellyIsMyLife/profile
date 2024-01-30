namespace Elektrom.Utils
{
    public class Card
    {
        public long CardNumber { get; set; }
        public string CardholdersName { get; set; } = null!;
        public string Expiration { get; set; } = null!;
        public int CVV { get; set; }

        public Card()
        {

        }

        public Card(long cardNumber, string cardholdersName, string expiration, int cvv)
        {
            CardNumber = cardNumber;
            CardholdersName = cardholdersName;
            Expiration = expiration;
            CVV = cvv;
        }
    }
}
