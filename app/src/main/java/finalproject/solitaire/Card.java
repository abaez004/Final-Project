package finalproject.solitaire;
public class Card{
	private int value;
	private int suit;
	private String image;
	private boolean revealed;
	private static final String unrevealedImage = "blank";
	
	//value represents the card A through King with values 1-13
	//suit represents diamonds clubs hearts and spades in that order, 1-4
	//image is the corresponding string that holds the cards image
	public Card(int value, int suit, String image) {
		this.value = value;
		this.suit = suit;
		this.image = image;
		revealed = false;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public int getSuit() {
		return this.suit;
	}
	
	public String getImage() {
		if(!revealed)
			return Card.unrevealedImage;
		return this.image;
	}

	public boolean isSameColor(Card c) {
		return suit % 2 == c.suit % 2;
	}
	
	public boolean isSameSuit(Card c) {
		return suit == c.suit;
	}
	
	public boolean isNextCard(Card c) {
		return (this.value + 1) == c.value;
	}
	
	public boolean isPreviousCard(Card c) {
		return (this.value - 1) == c.value;
	}
	
	public boolean isRevealed() {
		return this.revealed;
	}
	
	public void invertRevealed() {
		this.revealed = !this.revealed;
	}
}