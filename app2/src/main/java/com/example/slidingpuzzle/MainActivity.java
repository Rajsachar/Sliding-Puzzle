package com.example.slidingpuzzle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private int emptyX=3;
    private int emptyY=3;
    private RelativeLayout playField;
    private ImageButton[][] buttons;
    private int[] tiles;
    private int[] images = { R.drawable.img1,R.drawable.img2,R.drawable.img3,R.drawable.img4,R.drawable.img5,R.drawable.img6,R.drawable.img7,
            R.drawable.img8,R.drawable.img9,R.drawable.img10,R.drawable.img11,R.drawable.img12,R.drawable.img13,R.drawable.img14,R.drawable.img15,
            R.drawable.img16};
    private TextView title;
    private TextView moveCount;
    private int moves = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        moveCount = findViewById(R.id.textCounter);
        setContentView(R.layout.activity_main);
        Button buttonStart = findViewById(R.id.buttonstart);
        Button buttonSolve = findViewById(R.id.btnSolve);
        buttonStart.setOnClickListener(view -> {
            emptyX=3;
            emptyY=3;
            loadViews();
            loadNumbers();
            generateNumbers();
            loadData();
            moves = 0;


        });
        buttonSolve.setOnClickListener(view -> {
            emptyX=3;
            emptyY=3;
            loadViews();
            loadNumbers();
            loadData();
            moves = 0;
        });

        if(savedInstanceState!=null)
        {
            tiles = savedInstanceState.getIntArray("tiles");
            moves = savedInstanceState.getInt("moves");
            emptyX= savedInstanceState.getInt("emptyX");
            emptyY = savedInstanceState.getInt("emptyY");
            {loadViews();
            loadData();}

        } else {loadViews();
        loadNumbers();
        loadData();}

    }

    private void loadData() {

        for (int i = 0; i < playField.getChildCount(); i++){
            int value = Integer.valueOf(tiles[i]);
            buttons[i/4][i%4].setBackgroundResource(images[value]);
        }

        buttons[emptyX][emptyY].setBackgroundColor(ContextCompat.getColor(this,R.color.colorEmptySpot));
    }

    private void generateNumbers() {
        int n=15;
        Random random= new Random();
        while (n>1){
            int randomNum = random.nextInt(n--);
            int temp = tiles[randomNum];
            tiles[randomNum] = tiles[n];
            tiles[n] = temp;
        }

        if (!isSolvable())
            generateNumbers();
    }

    private boolean isSolvable() {
        int countInversions = 0;
        for (int i = 0; i < 15; i++) {
            for(int j = 0; j < i; j++){
                if (tiles[j] > tiles[i])
                    countInversions++;
             }
        }
        return countInversions % 2 == 0;
    }


    private void loadNumbers() {
        tiles = new int[16];

        for (int i = 0; i < playField.getChildCount() -1; i++) {
            tiles[i] = i;
        }
    }
    private void loadViews() {
        playField = findViewById(R.id.group);
        buttons = new ImageButton[4][4];

        for (int i = 0; i < playField.getChildCount(); i++) {
            buttons[i / 4][i % 4] = (ImageButton) playField.getChildAt(i);
        }
    }
    public void buttonClick(View view)
    {
        title = findViewById(R.id.textTitle);
        moveCount = findViewById(R.id.textCounter);
        ImageButton button = (ImageButton) view;
        int x = button.getTag().toString().charAt(0)-'0';
        int y = button.getTag().toString().charAt(1)-'0';
        Drawable current = button.getBackground();
        if ((Math.abs(emptyX-x)==1&&emptyY==y) || (Math.abs(emptyY-y)==1&&emptyX==x)){
            buttons[emptyX][emptyY].setBackground(current);
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.colorEmptySpot));
            emptyX = x;
            emptyY = y;
            title.setText("");
            moves = moves +1;
            moveCount.setText(String.valueOf(moves));
            checkWin();
        } else title.setText("Invalid Move");
    }

  private void checkWin() {
        boolean isWin =false;
         title = findViewById(R.id.textTitle);
         Drawable temp;
        if (emptyX == 3 && emptyY == 3){
            for (int i =0; i < playField.getChildCount() -1; i++)
                if (buttons[i / 4][i % 4].getBackground().getConstantState().equals( getDrawable(images[i]).getConstantState())){
                    isWin=true;
                }else
                    {
                        isWin=false;
                break;
                }
        }
        if (isWin){
            title.setText("You win! You solved it in " + moves +" moves!");
            moves=0;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("tiles",tiles);
        outState.putInt("moves",moves);
        outState.putInt("emptyX",emptyX);
        outState.putInt("emptyY",emptyY);
        outState.putInt("emptyY",emptyY);
    }
}