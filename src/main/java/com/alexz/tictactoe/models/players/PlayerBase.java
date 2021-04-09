package com.alexz.tictactoe.models.players;

import com.alexz.tictactoe.models.board.Mark;

import java.io.Serializable;

public abstract class PlayerBase implements Serializable {

    protected Mark mark;

    public PlayerBase(Mark mark) {
        this.mark = mark;
    }

    public Mark getMark() {
        return mark;
    }

    public void setMark(Mark mark) {
        this.mark = mark;
    }
}
