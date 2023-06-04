package com.project.logicapp.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.project.logicapp.R;
import com.project.logicapp.game.GameBoard;
import com.project.logicapp.game.GameBoard.Position;
import com.project.logicapp.game.GameManager;
import com.project.logicapp.game.Figure;
import com.project.logicapp.game.Utils;

import java.util.List;

public class FigureContainerView extends GridLayout {
    private int draggingIndex;
    private boolean levelCompleted;

    private static final float FIGURE_GROW_CHANCE = 3f;
    private static final float FIGURE_GROW_CHANCE_DIVIDE = 3f;


    public FigureContainerView(Context context) {
        super(context);
        init();
    }

    public FigureContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FigureContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        GridLayout.LayoutParams param =new GridLayout.LayoutParams();
        param.height = LayoutParams.WRAP_CONTENT;
        param.width = LayoutParams.WRAP_CONTENT;
        param.rightMargin = 5;
        param.topMargin = 5;
        param.setGravity(Gravity.BOTTOM);


        setOrientation(HORIZONTAL);
        populateViews();
    }


    private void populateViews() {
        removeAllViews();
        List<Figure> shapes;
        int n = 5;

        do {
            shapes= GameManager.getInstance().genPieces();
            n--;
        }
        while (n > 0 && shapes.size() == 1);

        int total = shapes.size();
        int column = 5;
        int row = total / column;
        setColumnCount(column);
        setRowCount(row + 1);



        for(int i =0, c = 0, r = 0; i < total; i++, c++)
        {
            if(c == column)
            {
                c = 0;
                r++;
            }
            BrickFrameLayout figureView = shapes.get(i).getFigureLayout(getContext());
            figureView.setTag(i);
            figureView.setOnTouchListener(new FigureTouchListener());





            figureView.setScaleX(0.4f);
            figureView.setScaleY(0.4f);

            if(r == 0) {
                GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                param.topMargin = (int) ((GameManager.getInstance().getBoardHeight() + GameManager.getInstance().getBoardOffsetY()) + 80);
                figureView.setLayoutParams(param);
            }
            addView(figureView, i);
        }

        updateMargin();

    }


    private void updateMargin(){
        float[] maxWidths = new float[getColumnCount()];
        calcMaxWidthPerColumn(maxWidths);
        updateMarginPerColumn(maxWidths);


        float[] maxHeights = new float[getRowCount()];
        calcMaxHeightPerRow(maxHeights);
        updateMarginPerRow(maxHeights);
    }

    private void calcMaxHeightPerRow(float[] maxHeights) {
        int columnCount = getColumnCount();
        for (int i = 0; i < getChildCount(); i++) {
            BrickFrameLayout layout = (BrickFrameLayout) getChildAt(i);
            int row = i / columnCount;
            float shapeHeight = layout.getShape().getHeight();
            maxHeights[row] = Math.max(maxHeights[row], shapeHeight);
        }
    }

    private void updateMarginPerRow(float[] maxHeights) {
        int columnCount = getColumnCount();
        for (int i = 0; i < getChildCount(); i++) {
            BrickFrameLayout layout = (BrickFrameLayout) getChildAt(i);
            int row = i / columnCount;
            float maxHeight = maxHeights[row];

            ViewGroup.MarginLayoutParams param = (MarginLayoutParams) layout.getLayoutParams();
            param.bottomMargin = (int) (-maxHeight + 200);
            layout.setLayoutParams(param);
        }
    }

    private void calcMaxWidthPerColumn(float[] maxWidths) {
        int columnCount = getColumnCount();
        for (int i = 0; i < getChildCount(); i++) {
            BrickFrameLayout layout = (BrickFrameLayout) getChildAt(i);
            int column = i % columnCount;
            float shapeWidth = layout.getShape().getWidth();
            maxWidths[column] = Math.max(maxWidths[column], shapeWidth);
        }
    }

    private void updateMarginPerColumn(float[] maxWidths) {
        int columnCount = getColumnCount();
        for (int i = 0; i < getChildCount(); i++) {
            BrickFrameLayout layout = (BrickFrameLayout) getChildAt(i);
            int column = i % columnCount;
            float maxWidth = maxWidths[column];

            ViewGroup.MarginLayoutParams param = (MarginLayoutParams) layout.getLayoutParams();
            param.rightMargin = (int) (-maxWidth + Utils.getDisplayMetrics(getContext()).widthPixels /5);
            layout.setLayoutParams(param);
        }
    }

    private void rotate(BrickFrameLayout layout){
        layout.getShape().rotate();
        layout.getShape().rebuildLayout(layout);
        updateMargin();
    }



    private void addNextLevelButton() {



        Context context = getContext();


        Button button = new Button(context);
        button.setText("Следующий уровень");
        Toast.makeText(context, "Вы прошли уровень! Поздравляем!", Toast.LENGTH_SHORT).show();


        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        button.setLayoutParams(layoutParams);
        button.setOnClickListener(v -> {
            levelCompleted = false;
            GameManager.getInstance().nextLevel();
            Activity activity =Utils.getCurrentActivity(getContext());
            if(activity instanceof GameBoardActivity){
                ((GameBoardActivity)activity).updateGameBoardLayout();
            }

            populateViews();

            ViewGroup rootView = ((Activity) context).findViewById(android.R.id.content);
            rootView.removeView(button);
        });


        ViewGroup rootView = ((Activity) context).findViewById(android.R.id.content);
        rootView.addView(button);


        DisplayMetrics metrics = Utils.getDisplayMetrics(getContext());

        button.setY(metrics.heightPixels/1.5f);



        button.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int width = button.getWidth();
            button.setX((metrics.widthPixels -width)/2);
        });


    }



    private class FigureTouchListener implements OnTouchListener {
        private float offsetX;
        private float offsetY;
        private float startX;
        private float startY;
        private boolean onBoard;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(levelCompleted) return false;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    boolean valid = false;

                    BrickFrameLayout layout = (BrickFrameLayout) v;

                    for (int i = 0; i < layout.getChildCount(); i++) {
                        View child = layout.getChildAt(i);
                        Rect rect = new Rect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
                        if (rect.contains((int) event.getX(), (int) event.getY())) {
                            valid = true;
                            break;
                        }
                    }

                    if(!valid) return false;
                    draggingIndex = indexOfChild(v);

                    offsetX = v.getX() - event.getRawX();
                    offsetY = v.getY() - event.getRawY();
                    v.setScaleX(1f);
                    v.setScaleY(1f);

                    if(!onBoard) {
                        startX = v.getX();
                        startY = v.getY();
                    }else{
                        Position position = calcBoardCoordinates(v);
                        GameManager.getInstance().getBoard().updateFigure(position, ((BrickFrameLayout)v).getShape(),false);
                    }



                    break;

                case MotionEvent.ACTION_MOVE:
                    float dx = event.getRawX() + offsetX ;
                    float dy = event.getRawY() + offsetY;
                    v.setX(dx);
                    v.setY(dy);
                    break;

                case MotionEvent.ACTION_UP:


                    GameBoard board = GameManager.getInstance().getBoard();
                    Position position = calcBoardCoordinates(v);


                    Figure shape = ((BrickFrameLayout)v).getShape();

                    if(!board.isEmpty(position, shape)) {
                        onBoard = false;
                        v.setX(startX);
                        v.setY(startY);
                        v.setScaleX(0.4f);
                        v.setScaleY(0.4f);
                    } else {
                        board.updateFigure(position, shape,true);
                        onBoard = true;
                        v.setX(position.getX() * GameManager.CELL_SIZE + GameManager.getInstance().getBoardOffsetX());
                        v.setY(position.getY() * GameManager.CELL_SIZE + GameManager.getInstance().getBoardOffsetY());
                        checkState();
                    }
                    break;

            }
            return true;
        }


        private Position calcBoardCoordinates(View v){



            int boardX = (int) ((v.getX() - GameManager.getInstance().getBoardOffsetX() + GameManager.CELL_SIZE/2)/GameManager.CELL_SIZE);
            int boardY = (int)((v.getY()  - GameManager.getInstance().getBoardOffsetY() + GameManager.CELL_SIZE/2)/GameManager.CELL_SIZE);

            return new Position(boardX,boardY);
        }
    }

    private void checkState() {
        if(GameManager.getInstance().getBoard().isAllFilled()) {

            levelCompleted = true;
            addNextLevelButton();

        }
    }
}