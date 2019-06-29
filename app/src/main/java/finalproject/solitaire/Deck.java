package finalproject.solitaire;
import java.util.ArrayList;

public class Deck {
	ArrayList<Card> deck;
	
	public Deck() {
		deck = new ArrayList<Card>();
		int j = 0;
		char[] suits = {'d', 'c', 'h', 's'};
		for(int i = 0; i < 52; i++) {
			if(i % 13 == 0 && i != 0)
				j++;
			deck.add(new Card(i % 13 + 1, j,suits[j] + "" + (i % 13 + 1)));
		}
		shuffle();
	}
	
	public void shuffle() {
		ArrayList<Card> tempDeck = new ArrayList<Card>();
		for(int i = 0; i < 52; i++) {
			int index = (int) (Math.random() * deck.size());
			tempDeck.add(deck.remove(index));
		}
		for(int i = 0; i < 52; i++) {
			deck.add(tempDeck.get(i));
		}

	}
	
	public Card deal() {
		return deck.remove(0);
	}

	public void placeOnBottom(Card c){
		deck.add(c);
	}

	public void placeOnTop(Card c){
		deck.add(0, c);
	}

	public Card dealFromBottom() {
		return deck.remove(deck.size()-1);
	}

	public boolean isEmpty(){
		return deck.isEmpty();
	}

	public int size(){return deck.size();}

}