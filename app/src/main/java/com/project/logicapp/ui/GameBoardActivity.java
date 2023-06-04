package com.project.logicapp.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.project.logicapp.R;
import com.project.logicapp.game.GameManager;

public class GameBoardActivity extends AppCompatActivity {



    private TableLayout gameBoardLayout;
    private ConstraintLayout screenLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        GameManager manager = GameManager.getInstance();
        manager.updateOffset(displayMetrics.widthPixels, displayMetrics.heightPixels);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_board_activity);



        screenLayout = findViewById(R.id.screen_layout);



        gameBoardLayout = (TableLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.game_board_layout,screenLayout,false);


        screenLayout.addView(gameBoardLayout, 0);


        updateGameBoardLayout();
    }


    public void updateGameBoardLayout(){
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) gameBoardLayout.getLayoutParams();
        GameManager manager = GameManager.getInstance();
        layoutParams.height = (int) manager.getBoardHeight();
        layoutParams.width = (int) manager.getBoardWidth();
        gameBoardLayout.setLayoutParams(layoutParams);

        gameBoardLayout.setX(manager.getBoardOffsetX());
        gameBoardLayout.setY(manager.getBoardOffsetY());

        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cell);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, GameManager.CELL_SIZE, GameManager.CELL_SIZE, true);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), scaledBitmap);
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        gameBoardLayout.setBackground(bitmapDrawable);
    }









}