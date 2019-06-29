package finalproject.solitaire;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.View.OnTouchListener;
import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    protected Deck solitaireDeck;
    protected ArrayList<AcePile> acePiles;
    protected ArrayList<BoardPile> boardPiles;
    protected Stack<Move> moveStack;
    protected DrawPile drawPile;

    private ConstraintLayout mainLayout;
    private ImageView deckImage;
    private ImageView drawPileImage;
    private RelativeLayout acePileLayout;
    private RelativeLayout boardLayout;
    private LinearLayout deckLayout;
    private ImageView quitBtn;
    private ImageView resetBtn;
    private ImageView settingsBtn;
    private ImageView undoBtn;
    private boolean dragFlag = false;
    private int xClicked;
    private int yClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = (ConstraintLayout) findViewById(R.id.mainLayout);
        drawPileImage = (ImageView) findViewById(R.id.drawPileImg);
        boardLayout = (RelativeLayout)findViewById(R.id.boardLayout);
        acePileLayout = (RelativeLayout)findViewById(R.id.acePileLayout);
        deckLayout = (LinearLayout)findViewById(R.id.deckLayout);
        quitBtn = (ImageView) findViewById(R.id.quitBtn);
        resetBtn = (ImageView) findViewById(R.id.resetBtn);
        settingsBtn = (ImageView) findViewById(R.id.settingsBtn);
        undoBtn = (ImageView) findViewById(R.id.undoBtn);
        deckImage = (ImageView) findViewById(R.id.deckImg);

        startGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void drawCards(android.view.View v){
        if(solitaireDeck.isEmpty())
            return;
        if(drawPile.isEmpty())
            moveStack.push(new Move(0,0,0,'D', 'D'));
        else
            moveStack.push(new Move(drawPile.size(),0,0,'D', 'D'));
        drawPile.returnToDeck(solitaireDeck);
        drawPile.dealThree(solitaireDeck);
        display();
    }

    public int imageResource(Pile pile){
        if(pile.isEmpty())
            return getResources().getIdentifier("empty", "drawable", getPackageName());
        return getResources().getIdentifier(pile.last().getImage(), "drawable", getPackageName());
    }

    public int imageResource(Card c){
        if(c == null)
            return getResources().getIdentifier("empty", "drawable", getPackageName());
        return getResources().getIdentifier(c.getImage(), "drawable", getPackageName());
    }

    public String imageResourceString(Card c){
        if(c == null)
            return null;
        return getResources().getString(imageResource(c));
    }

    public void display(){
        if(moveStack.empty())
            undoBtn.setImageResource(R.drawable.undobtnunselect);
        else
            undoBtn.setImageResource(R.drawable.undobtn);
        if(solitaireDeck.isEmpty())
            deckImage.setImageResource(R.drawable.empty);
        else
           deckImage.setImageResource(R.drawable.blank);
        deckLayout.removeAllViews();
        for(int i = 0; i < 4; i++){
            if(!acePiles.get(i).isEmpty()) {
                ((ImageView)acePileLayout.getChildAt(i)).setImageResource(imageResource(acePiles.get(i).last()));
            }
            else{
                ((ImageView)acePileLayout.getChildAt(i)).setImageResource(R.drawable.empty);
                ((ImageView)acePileLayout.getChildAt(i)).setOnTouchListener(dragCardsFromAce());
            }

        }
        for(Card c:drawPile.getPile()){
            ImageView img = new ImageView(deckLayout.getContext());
            img.setImageResource(imageResource(c));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,-100,0);
            img.setLayoutParams(params);
            if(c == drawPile.last())
                img.setOnTouchListener(dragCardsFromDraw());
            deckLayout.addView(img);
        }
        for(int i = 0; i < 7; i++){
            ((LinearLayout)boardLayout.getChildAt(i)).removeAllViews();
            if(!boardPiles.get(i).isEmpty())
                boardPiles.get(i).flip();
            if(boardPiles.get(i).isEmpty()){
                ImageView img = new ImageView(boardLayout.getContext());
                img.setImageResource(R.drawable.empty);
                ((LinearLayout)boardLayout.getChildAt(i)).addView(img);
            }
            for(Card c: boardPiles.get(i).getPile()){
                ImageView img = new ImageView(boardLayout.getContext());
                img.setImageResource(imageResource(c));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,0,0,-165);
                img.setLayoutParams(params);
                if(c.isRevealed())
                    img.setOnTouchListener(dragCardsFromBoard());
                ((LinearLayout)boardLayout.getChildAt(i)).addView(img);

            }
        }
        checkWin();

    }

    public void resetClicked(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(mainLayout.getContext());
        alert.setTitle("Reset Current Game");
        alert.setMessage("Are you sure you want to reset?");

        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startGame();
            }
        });

        alert.setNegativeButton(android.R.string.no, null);
        alert.show();

    }

    public void quitClicked(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(mainLayout.getContext());
        alert.setTitle("Quit Game");
        alert.setMessage("Are you sure you want to quit?");

        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        alert.setNegativeButton(android.R.string.no, null);
        alert.show();

    }

    public void checkWin(){
        for(AcePile acepile:acePiles){
            if(!acepile.isFull())
                return;
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(mainLayout.getContext());
        alert.setTitle("You won!");
        alert.setMessage("Would you like to play again?");

        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startGame();
            }
        });

        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        alert.show();
    }

    public void startGame(){
        acePiles = new ArrayList<AcePile>();
        boardPiles = new ArrayList<BoardPile>();
        solitaireDeck = new Deck();
        drawPile = new DrawPile();
        moveStack = new Stack<Move>();
        for(int i = 0; i < 4; i++){
            acePiles.add(new AcePile());
        }

        for(int i = 0; i < 7; i++){
            boardPiles.add(new BoardPile());
            for(int j = 0; j <= i; j++)
                boardPiles.get(i).add(solitaireDeck.deal());
            boardPiles.get(i).flip();
        }

        display();
    }

    private OnTouchListener dragCardsFromBoard(){
        return new OnTouchListener(){
            public boolean onTouch(View view, MotionEvent event)  {
                if(!dragFlag && event.getRawX() != 0.0){
                     dragFlag = true;
                     xClicked = (int) event.getRawX();
                     yClicked = (int) event.getRawY();
                }

                int firstBoard = (int)xClicked / 153;
                if(firstBoard >= 7)
                    firstBoard = 6;
                else if(firstBoard < 0)
                    firstBoard = 0;
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        break;

                    case MotionEvent.ACTION_MOVE:
                        break;

                    case MotionEvent.ACTION_UP:
                        if(event.getRawY() < 450){
                            int secondBoard = ((int)event.getRawX() - 400) / 150;
                            if(secondBoard >= 4)
                                secondBoard = 3;
                            else if(secondBoard < 0)
                                secondBoard = 0;
                            moveCards(boardPiles.get(firstBoard), acePiles.get(secondBoard), secondBoard, firstBoard);
                        }
                        else{
                            int secondBoard = (int)event.getRawX() / 153;
                            if(secondBoard >= 7)
                                secondBoard = 6;
                            else if(secondBoard < 0)
                                secondBoard = 0;
                            moveCards(boardPiles.get(firstBoard), boardPiles.get(secondBoard),secondBoard,firstBoard);
                        }
                        dragFlag = false;
                        display();
                        break;

                }

                return true;
            }
        };
    }

    private OnTouchListener dragCardsFromDraw(){
        return new OnTouchListener(){
            public boolean onTouch(View view, MotionEvent event)  {
                if(!dragFlag && event.getRawX() != 0.0){
                    dragFlag = !dragFlag;
                    xClicked = (int) event.getRawX();
                    yClicked = (int) event.getRawY();
                }

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        break;

                    case MotionEvent.ACTION_MOVE:
                        break;

                    case MotionEvent.ACTION_UP:
                        if(event.getRawY() < 450){
                            int secondBoard = ((int)event.getRawX() - 400) / 150;
                            if(secondBoard >= 4)
                                secondBoard = 3;
                            else if(secondBoard < 0)
                                secondBoard = 0;
                            moveCards(drawPile, acePiles.get(secondBoard), secondBoard);
                        }
                        else{
                            int secondBoard = (int)event.getRawX() / 153;
                            if(secondBoard >= 7)
                                secondBoard = 6;
                            else if(secondBoard < 0)
                                secondBoard = 0;
                            moveCards(drawPile, boardPiles.get(secondBoard), secondBoard);
                        }

                        dragFlag = !dragFlag;
                        display();
                        break;

                }
                return true;
            }
        };
    }

    private OnTouchListener dragCardsFromAce(){
        return new OnTouchListener(){
            public boolean onTouch(View view, MotionEvent event)  {
                if(!dragFlag && event.getRawX() != 0.0){
                    dragFlag = !dragFlag;
                    xClicked = (int) event.getRawX();
                    yClicked = (int) event.getRawY();
                }

                int firstBoard = ((int)xClicked - 400) / 150;
                if(firstBoard >= 4)
                    firstBoard = 3;
                else if(firstBoard < 0)
                    firstBoard = 0;
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        break;

                    case MotionEvent.ACTION_MOVE:
                        break;

                    case MotionEvent.ACTION_UP:
                        if(event.getRawY() > 450){
                            int secondBoard = (int)event.getRawX() / 153;
                            if(secondBoard >= 7)
                                secondBoard = 7;
                            else if(secondBoard < 0)
                                secondBoard = 0;
                            moveCards(acePiles.get(firstBoard), boardPiles.get(secondBoard), secondBoard, firstBoard);
                        }

                        dragFlag = !dragFlag;
                        display();
                        break;

                }
                return true;
            }
        };
    }

    public void moveCards(AcePile b1, BoardPile b2, int add, int remove){
        if(b1.isEmpty()) {
            return;
        }
        if(b1.last().isNextCard(b2.last()) && !b1.last().isSameColor(b2.last())){
            b2.add(b1.pop());
            moveStack.push(new Move(1, remove, add, 'A', 'B'));
        }
    }

    public void moveCards(BoardPile b1, BoardPile b2, int add, int remove){
        if(b1 == b2 || b1.isEmpty()) {
            return;
        }
        int count = 0;
        if(b2.isEmpty()){
            for(int i = 0; i < b1.getPile().size(); i++){
                if(b1.getPile().get(i).isRevealed() && b1.getPile().get(i).getValue() == 13){
                    while(b1.getPile().size() > i){
                        b2.add(b1.remove(i));
                        count++;
                    }
                    moveStack.push(new Move(count, remove, add, 'B', 'B'));
                }
            }
        }
        else {
            Card targetCard = b2.last();
            for (int i = 0; i < b1.getPile().size(); i++) {
                if (b1.getPile().get(i).isRevealed() && b1.getPile().get(i).isNextCard(targetCard) && !b1.getPile().get(i).isSameColor(targetCard)) {
                    while (b1.getPile().size() > i) {
                        b2.add(b1.remove(i));
                        count++;
                    }

                    moveStack.push(new Move(count, remove, add, 'B', 'B'));
                }
            }
        }
        b1.setLastRevealed();
    }

    public void moveCards(BoardPile b1, AcePile b2, int add, int remove){
        if(b1.isEmpty()) {
            return;
        }

        if(b2.isEmpty()){
            if(b1.last().getValue() == 1) {
                b2.addCard(b1.remove(b1.getPile().size() - 1));
                moveStack.push(new Move(1, remove, add, 'B', 'A'));
            }
        }

        else if(b1.last().isPreviousCard(b2.last()) && b1.last().isSameSuit(b2.last())){
            b2.addCard(b1.remove(b1.getPile().size() - 1));
            moveStack.push(new Move(1, remove, add, 'B', 'A'));
        }

        b1.setLastRevealed();
    }

    public void moveCards(DrawPile b1, AcePile b2, int add){
        if(b1.isEmpty()) {
            return;
        }

        if(b2.isEmpty()){
            if(b1.last().getValue() == 1){
                b2.addCard(b1.removeLast());
                moveStack.push(new Move(1, 0, add, 'D', 'A'));
            }
        }
        else if(b1.last().isPreviousCard(b2.last()) && b1.last().isSameSuit(b2.last())){
            b2.addCard(b1.removeLast());
            moveStack.push(new Move(1, 0, add, 'D', 'A'));
        }
    }
    public void moveCards(DrawPile b1, BoardPile b2, int add){
        if(b1.isEmpty()) {
            return;
        }

        if(b2.isEmpty()){
            if(b1.last().getValue() == 13){
                b2.add(b1.removeLast());
                moveStack.push(new Move(1, 0, add, 'D', 'B'));
            }
        }
        else if(b1.last().isNextCard(b2.last()) && !b1.last().isSameColor(b2.last())){
            b2.add(b1.removeLast());
            moveStack.push(new Move(1, 0, add, 'D', 'B'));
        }

    }

    public void undoClicked(View view){
        if(!moveStack.empty()) {
            if(moveStack.peek().getRemovedType() == 'B' && moveStack.peek().getAddedType() == 'B'){
                int initialInsertPosition = boardPiles.get(moveStack.peek().getRemovedIndex()).getPile().size();
                if(!boardPiles.get(moveStack.peek().getRemovedIndex()).isEmpty() && !boardPiles.get(moveStack.peek().getRemovedIndex()).getLastRevealed())
                    boardPiles.get(moveStack.peek().getRemovedIndex()).last().invertRevealed();
                for(int i = 0; i < moveStack.peek().getCardCount(); i++){
                    boardPiles.get(moveStack.peek().getRemovedIndex()).add(initialInsertPosition, boardPiles.get(moveStack.peek().getAddedIndex()).remove(boardPiles.get(moveStack.peek().getAddedIndex()).getPile().size() - 1));
                }

            }

            else if(moveStack.peek().getRemovedType() == 'B' && moveStack.peek().getAddedType() == 'A'){
                if(!boardPiles.get(moveStack.peek().getRemovedIndex()).isEmpty() && boardPiles.get(moveStack.peek().getRemovedIndex()).last().isRevealed())
                    boardPiles.get(moveStack.peek().getRemovedIndex()).last().invertRevealed();
                boardPiles.get(moveStack.peek().getRemovedIndex()).add(acePiles.get(moveStack.peek().getAddedIndex()).pop());
            }

            else if(moveStack.peek().getRemovedType() == 'D' && moveStack.peek().getAddedType() == 'B'){
                drawPile.push(boardPiles.get(moveStack.peek().getAddedIndex()).remove(boardPiles.get(moveStack.peek().getAddedIndex()).getPile().size() - 1));
            }

            else if(moveStack.peek().getRemovedType() == 'D' && moveStack.peek().getAddedType() == 'A'){
                drawPile.push(acePiles.get(moveStack.peek().getAddedIndex()).pop());
            }

            else if(moveStack.peek().getRemovedType() == 'D' && moveStack.peek().getAddedType() == 'D'){
                drawPile.returnLastThree(solitaireDeck);
                if(moveStack.peek().getCardCount() != 0)
                    drawPile.dealLast(solitaireDeck, moveStack.peek().getCardCount());
            }

            else if(moveStack.peek().getRemovedType() == 'A' && moveStack.peek().getAddedType() == 'B'){
                acePiles.get(moveStack.peek().getRemovedIndex()).addCard(boardPiles.get(moveStack.peek().getAddedIndex()).remove(boardPiles.get(moveStack.peek().getAddedIndex()).getPile().size() - 1));
            }
            moveStack.pop();
            display();
        }
    }
}
