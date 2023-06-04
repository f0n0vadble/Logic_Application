package com.project.logicapp.game;

public class GameBoard {
    private boolean[][] board;
    private int x;
    private int y;

    public GameBoard(int x, int y){
        board = new boolean[x][y];
        this.x = x;
        this.y = y;
    }

    public boolean isOutOfBounds(Position position){
        if(position.x >= x || position.x < 0) return true;
        if(position.y >= y || position.y < 0) return true;
        return false;
    }

    public boolean isOutOfBounds(Position position, Figure shape){
        for(Position p : shape.getParts()){
            if(isOutOfBounds(position.add(p))){
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty(Position position){
        if(isOutOfBounds(position)) return false;
        return !board[position.x][position.y];
    }

    public boolean isFilled(Position position){
        return !isEmpty(position);
    }

    public boolean isEmpty(Position position, Figure shape){
        for(Position p : shape.getParts()){
            if(!isEmpty(position.add(p))){
                return false;
            }
        }

        return true;
    }


    public boolean isAnyFilled(Position position, Figure shape){
        for(Position p : shape.getParts()){
            if(isFilled(position.add(p))){
                return true;
            }
        }

        return false;
    }


    public void updateFigure(Position position, Figure shape, boolean state){

        for(Position p : shape.getParts()){
            Position pos = position.add(p);
            if(!isOutOfBounds(pos)) {
                board[pos.x][pos.y] = state;
            }
        }
    }

    public boolean isAllFilled(){
       for(boolean[] arr : board){
           for(boolean b : arr){
               if(!b) return false;
           }
       }
       return true;
    }


    public boolean[][] getBoard() {
        return board;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static class Position{
        private int x;
        private int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Position setX(int x) {
            this.x = x;
            return this;
        }

        public Position setY(int y) {
            this.y = y;
            return this;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Position add(int x, int y){
            return new Position(this.x + x, this.y + y);
        }

        public Position add(Position position){
            return new Position(this.x + position.x, this.y + position.y);
        }


    }
}
