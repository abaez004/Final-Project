package finalproject.solitaire;
import java.util.LinkedList;

public class BoardPile implements Pile{
    private LinkedList<Card> pile;
    private boolean lastRevealed;

    public BoardPile() {
        pile = new LinkedList<Card>();
        lastRevealed = false;
    }

    public BoardPile(BoardPile boardPile) {
        pile = new LinkedList<Card>();
        if(boardPile.pile.isEmpty())
            return;
        for(Card c : boardPile.pile)
            pile.add(c);
    }

    public void flip() {
        if(!pile.peekLast().isRevealed())
            pile.peekLast().invertRevealed();
    }

    public void setLastRevealed(){
        if(!pile.isEmpty())
            lastRevealed =  pile.get(pile.size() - 1).isRevealed();
        else
            lastRevealed =  false;
    }

    public boolean getLastRevealed(){
        return lastRevealed;
    }

    public Card last(){
        return pile.peekLast();
    }

    public void add(Card c){
        pile.add(c);
    }

    public void add(int index, Card c){
        pile.add(index, c);
    }

    public Card remove(int index){
        return pile.remove(index);
    }

    public LinkedList<Card> getPile(){
        return pile;
    }

    public boolean isEmpty(){
        return pile.isEmpty();
    }
}