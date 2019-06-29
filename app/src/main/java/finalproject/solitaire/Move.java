package finalproject.solitaire;

public class Move {
    private int cardCount;
    private int removedIndex;
    private int addedIndex;
    private char removedType;
    private char addedType;


    public Move(int cardCount, int removedIndex, int addedIndex, char removedType, char addedType){
        this.cardCount = cardCount;
        this.removedIndex = removedIndex;
        this.addedIndex = addedIndex;
        this.removedType = removedType;
        this.addedType = addedType;
    }

    public int getCardCount(){
        return this.cardCount;
    }

    public int getRemovedIndex(){
        return this.removedIndex;
    }

    public int getAddedIndex(){
        return this.addedIndex;
    }

    public char getRemovedType(){
        return this.removedType;
    }

    public char getAddedType(){
        return this.addedType;
    }

}
