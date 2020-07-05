package com.game.checkers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Tile extends Rectangle {

    private OneChecker checker;

    public boolean hasChecker() {
        return checker != null;
    }

    public OneChecker getChecker() {
        return checker;
    }

    public void setChecker(OneChecker checker) {
        this.checker = checker;
    }

    public Tile(boolean light, int x, int y) {
        setWidth(Checkers.TILE_SIZE);
        setHeight(Checkers.TILE_SIZE);

        relocate(x * Checkers.TILE_SIZE, y * Checkers.TILE_SIZE);
        setFill(light ? Color.WHITE : Color.BLACK);
    }
}

