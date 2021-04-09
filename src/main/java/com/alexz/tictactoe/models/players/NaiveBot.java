package com.alexz.tictactoe.models.players;

import com.alexz.tictactoe.models.board.Mark;
import com.alexz.tictactoe.models.board.Tile;
import com.alexz.tictactoe.services.BoardService;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.Map;

public class NaiveBot extends Bot {

  public NaiveBot(final Mark mark) {
    super(mark, Difficulty.EASY);
  }

  @Override
  public Tile getNextMove(Map<Tile, Mark> board) {
    final List<Tile> tiles = BoardService.getInstance().getAvailableMoves(board);
    return tiles.get(RandomUtils.nextInt(0, tiles.size()));
  }
}
