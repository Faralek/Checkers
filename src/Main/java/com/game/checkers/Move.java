package com.game.checkers;

public class Move {
    int positionX;
    int positionY;
    int directionX;
    int directionY;

    public Move(int positionX, int positionY, int directionX, int directionY) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.directionX = directionX;
        this.directionY = directionY;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public int getDirectionX() {
        return directionX;
    }

    public int getDirectionY() {
        return directionY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move)) return false;

        Move move = (Move) o;

        if (positionX != move.positionX) return false;
        if (positionY != move.positionY) return false;
        if (directionX != move.directionX) return false;
        return directionY == move.directionY;
    }

    @Override
    public int hashCode() {
        int result = positionX;
        result = 31 * result + positionY;
        result = 31 * result + directionX;
        result = 31 * result + directionY;
        return result;
    }

    @Override
    public String toString() {
        return "Move{" +
                "positionX=" + positionX +
                ", positionY=" + positionY +
                ", directionX=" + directionX +
                ", directionY=" + directionY +
                '}';
    }
}
