package com.game.checkers;

public enum CheckerType {
    RED(1), WHITE(-1);

    final int moveDir;

    CheckerType(int moveDir) {
        this.moveDir = moveDir;
    }
}
