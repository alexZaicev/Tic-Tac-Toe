package com.alexz.tictactoe.models.players;

import com.alexz.tictactoe.models.board.Mark;
import com.alexz.tictactoe.models.board.Tile;

import java.util.Map;

public abstract class Bot extends PlayerBase {

  protected final Difficulty difficulty;

  public Bot(final Mark mark, final Difficulty difficulty) {
    super(mark);
    this.difficulty = difficulty;
  }

  public abstract Tile getNextMove(final Map<Tile, Mark> board);
}
