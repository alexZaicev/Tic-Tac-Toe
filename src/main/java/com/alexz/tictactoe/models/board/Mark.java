package com.alexz.tictactoe.models.board;

public enum Mark {
    NONE(""),
    X("X"),
    O("O"),
    ;

    public String name;

    Mark(String name) {
        this.name = name;
    }
}
