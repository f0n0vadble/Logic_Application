package com.project.logicapp.game;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.project.logicapp.R;
import com.project.logicapp.game.GameBoard.Position;
import com.project.logicapp.ui.BrickFrameLayout;

import java.util.*;
import java.util.function.Consumer;

public class Figure {



    List<Position> parts;
    int color;


    public Figure(){
        parts = new ArrayList<>();
    }

    public Figure(List<Position> parts){
        this.parts = parts;
    }


    public List<Position> getParts() {
        return parts;
    }


    public BrickFrameLayout getFigureLayout(Context context) {
        int cellSize = GameManager.CELL_SIZE;
        BrickFrameLayout figureLayout = new BrickFrameLayout(context,this);
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);


        float brightnessFactor = 2f;
        r = (int) (r * brightnessFactor);
        g = (int) (g * brightnessFactor);
        b = (int) (b * brightnessFactor);

        r = Math.min(255, r);
        g = Math.min(255, g);
        b = Math.min(255, b);
        color = Color.argb(255, r, g, b);
        ColorFilter colorFilter = new LightingColorFilter(color, 0);


        for (Position part : parts) {
            ImageView imageView = new ImageView(context);


            imageView.setImageResource(R.drawable.brick);
            imageView.setColorFilter(colorFilter);

            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    cellSize,
                    cellSize
            );
            layoutParams.height = GameManager.CELL_SIZE;
            layoutParams.width = GameManager.CELL_SIZE;
            layoutParams.leftMargin = part.getX() * cellSize;
            layoutParams.topMargin = part.getY() * cellSize;

            imageView.setLayoutParams(layoutParams);
            figureLayout.addView(imageView);
        }



        return figureLayout;
    }

    public int getWidth() {
        int leftmost = Integer.MAX_VALUE;
        int rightmost = Integer.MIN_VALUE;

        for (Position part : parts) {
            leftmost = Math.min(leftmost, part.getX());
            rightmost = Math.max(rightmost, part.getX());
        }

        return (rightmost - leftmost + 1) * GameManager.CELL_SIZE;
    }

    public int getHeight() {
        int topmost = Integer.MAX_VALUE;
        int bottommost = Integer.MIN_VALUE;

        for (Position part : parts) {
            topmost = Math.min(topmost, part.getY());
            bottommost = Math.max(bottommost, part.getY());
        }

        return (bottommost - topmost + 1) * GameManager.CELL_SIZE;
    }

    public void rotate() {
        List<Position> rotatedParts = new ArrayList<>();

        for (Position part : parts) {
            int rotatedX = -(part.getY() );
            int rotatedY = part.getX() ;
            rotatedParts.add(new Position(rotatedX, rotatedY));
        }

        parts = rotatedParts;



        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;


        for (Position part : parts) {
            minX = Math.min(minX, part.getX());
            minY = Math.min(minY, part.getY());
        }

        for(Position pos : parts){
            pos.setX(pos.getX() - minX);
            pos.setY(pos.getY() - minY);
        }
    }

    public void rebuildLayout(BrickFrameLayout layout) {
        layout.removeAllViews();

        ColorFilter colorFilter = new LightingColorFilter(color, 0);
        int cellSize = GameManager.CELL_SIZE;



        for (Position part : parts) {
            ImageView imageView = new ImageView(layout.getContext());

            imageView.setImageResource(R.drawable.brick);
            imageView.setColorFilter(colorFilter);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    cellSize,
                    cellSize
            );

            layoutParams.leftMargin = (part.getX()) * cellSize;
            layoutParams.topMargin = (part.getY()) * cellSize;

            imageView.setLayoutParams(layoutParams);
            layout.addView(imageView);
        }
    }
}
