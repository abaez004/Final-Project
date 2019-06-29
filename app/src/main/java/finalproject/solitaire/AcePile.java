package finalproject.solitaire;
import java.util.Stack;

public class AcePile implements Pile{
	private Stack<Card> pile;
	
	public AcePile(){
		pile = new Stack<Card>();
	}

	public AcePile(AcePile acePile){
		pile = new Stack<Card>();
        if(acePile.pile.isEmpty())
            return;
		for(Card c:acePile.pile)
			pile.add(c);
	}

	public void addCard(Card c) {
		pile.push(c);
	}

	public Card last(){
		return pile.peek();
	}

	public boolean isFull() {
		return pile.size() == 13;
	}

	public boolean isEmpty(){
		return pile.isEmpty();
	}

	public Card pop(){ return pile.pop();}
}