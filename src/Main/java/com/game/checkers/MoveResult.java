package com.game.checkers;

public class MoveResult {

    private final MoveType type;
    private final OneChecker checker;

    public MoveType getType() {
        return type;
    }

    public OneChecker getChecker() {
        return checker;
    }

    public MoveResult(MoveType type) {
        this(type, null);
    }

    public MoveResult(MoveType type, OneChecker checker) {
        this.type = type;
        this.checker = checker;
    }
}