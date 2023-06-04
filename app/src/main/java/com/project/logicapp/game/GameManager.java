package com.project.logicapp.game;

import com.project.logicapp.game.GameBoard.Position;

import java.util.*;

public class GameManager {
    private static final GameManager gameHandler = new GameManager();
    private static final List<Position> GROW_DIRECTIONS = new ArrayList<>(Arrays.asList(new Position(1,0), new Position(0,1),new Position(-1,0), new Position(0,-1)));
    private static final float FIGURE_GROW_CHANCE = 5f;
    private static final float FIGURE_GROW_CHANCE_DIVIDE = 2f;



    public static int CELL_SIZE = 100;

    private GameBoard board;
    private float boardOffsetX;
    private float boardOffsetY;



    private GameManager(){
        board = new GameBoard(2,2);
        updateCellSize();
    }




    public List<Figure> genPieces(){


        List<Figure> shapes = new ArrayList<>();
        boolean[][] filled = new boolean[board.getX()][board.getY()];
        for (int x = 0; x < board.getX(); x++) {
            for (int y = 0; y < board.getY(); y++) {
                if(!filled[x][y]){
                    List<Position> pos = new ArrayList<>();
                    pos.add(new Position(0,0));
                    filled[x][y] = true;
                    appendShape(pos, x,y, x,y,filled,FIGURE_GROW_CHANCE, FIGURE_GROW_CHANCE_DIVIDE);


                    Figure figure = new Figure(pos);

                    //for (int i = 0; i < Math.random() * 5; i++) {figure.rotate();} rotating by random angle

                    shapes.add(figure);
                }
            }
        }

        return shapes;

    }

    private void appendShape(List<Position> set, int x0, int y0, int x, int y, boolean[][] filled,float growChance, float s) {
        Collections.shuffle(GROW_DIRECTIONS);
        for(Position growDir : GROW_DIRECTIONS){
            if(Math.random() < growChance){
                Position pos = growDir.add(x,y);
                if(pos.getX() - x0 < 0 || pos.getY() -y0 < 0) continue;
                if(!board.isOutOfBounds(pos) && !filled[pos.getX()][pos.getY()]){
                    filled[pos.getX()][pos.getY()] = true;
                    set.add(new Position(pos.getX() - x0,  pos.getY() -y0));
                    appendShape(set, x0,y0, pos.getX(), pos.getY(), filled,growChance/s,s);
                    return;
                }
            }
        }
    }


    private int cachedScreenWidth;
    private int cachedScreenHeight;

    public void updateOffset(int width, int height) {
        this.cachedScreenWidth = width;
        this.cachedScreenHeight = height;
        height/=2;

        int remainingSpace = (int) (width - getBoardWidth());
        boardOffsetX = remainingSpace / 2f;


        remainingSpace = (int) (height - getBoardHeight());
        boardOffsetY = remainingSpace / 2f;
    }

    public float getBoardOffsetX() {
        return boardOffsetX;
    }

    public float getBoardOffsetY(){
        return boardOffsetY;
    }

    public float getBoardHeight(){
        return board.getY() * CELL_SIZE;
    }

    public float getBoardWidth(){
        return board.getX() * CELL_SIZE;
    }

    public GameBoard getBoard() {
        return board;
    }

    public static GameManager getInstance() {
        return gameHandler;
    }

    public void nextLevel() {

        int x = board.getX();
        int y = board.getY();
        if(x > y) y++;
        else x++;


        board = new GameBoard(x,y);
        updateCellSize();
        updateOffset(cachedScreenWidth,cachedScreenHeight);
    }

    private void updateCellSize() {

        int maxDim = Math.max(board.getX(), board.getY());
        if(maxDim < 5){
            CELL_SIZE = 100;
        }else if(maxDim < 10){
            CELL_SIZE = 75;
        }else if(maxDim < 15){
            CELL_SIZE = 50;
        }else if(maxDim < 20){
            CELL_SIZE = 30;
        }else{
            CELL_SIZE = 20;
        }
    }
}
