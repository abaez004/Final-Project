package finalproject.solitaire;
import java.util.ArrayList;

public class DrawPile implements Pile{
    private ArrayList<Card> pile;

    public DrawPile(){
        pile = new ArrayList<Card>();
    }

    public DrawPile(DrawPile drawPile){
        pile = new ArrayList<Card>();
        if(drawPile.pile.isEmpty())
            return;
        for(Card c : drawPile.pile)
            pile.add(c);
    }

    public Card removeLast(){
        return pile.remove(pile.size()-1);
    }

    public void dealThree(Deck deck){
        if(!pile.isEmpty())
            return;
        if(deck.size() < 3){
            for(int i = 0; i < deck.size() - 1; i++) {
                pile.add(deck.deal());
                pile.get(i).invertRevealed();
            }

        }
        else {
            for (int i = 0; i < 3; i++) {
                pile.add(deck.deal());
                pile.get(i).invertRevealed();
            }
        }
    }

    public void returnLastThree(Deck deck){
        while(!pile.isEmpty()){
            pile.get(pile.size() - 1).invertRevealed();
            deck.placeOnTop(pile.remove(pile.size() -1));
        }
    }

    public void dealLast(Deck deck, int count){
        if(!pile.isEmpty())
            return;
        for(int i = 0; i < count; i++) {
            pile.add(0, deck.dealFromBottom());
            pile.get(0).invertRevealed();
        }
    }

    public Card last(){
        return pile.get(pile.size() - 1);
    }

    public void returnToDeck(Deck deck){
        while(!pile.isEmpty()){
            pile.get(0).invertRevealed();
            deck.placeOnBottom(pile.remove(0));
        }

    }

    public void push(Card card){
        pile.add(card);
    }

    public ArrayList<Card> getPile(){
        return pile;
    }

    public boolean isEmpty(){
        return pile.isEmpty();
    }

    public int size(){
        return pile.size();
    }

}
