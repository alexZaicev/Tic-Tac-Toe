package com.alexz.tictactoe.services;

import com.alexz.tictactoe.models.board.Board;
import com.alexz.tictactoe.models.board.Mark;
import com.alexz.tictactoe.models.board.Tile;
import com.alexz.tictactoe.models.players.AgentBot;
import com.alexz.tictactoe.models.players.Bot;
import com.alexz.tictactoe.models.players.Difficulty;
import com.alexz.tictactoe.models.players.QBot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.TreeMap;

public class AgentService {

  private static final Logger _logger = LogManager.getLogger(AgentService.class);
  private static final Object lock = new Object();
  private static AgentService instance;

  public static AgentService getInstance() {
    AgentService result = instance;
    if (result == null) {
      synchronized (lock) {
        result = instance;
        if (result == null) instance = result = new AgentService();
      }
    }
    return result;
  }

  public void trainQBot(
      final QBot playerA, final int episodes, final Difficulty opponentDifficulty) {
    final Bot playerB = BotFactory.getBot(null, opponentDifficulty);
    int wins = 0;
    for (int i = 0; i < episodes; ++i) {
      final Board boardGame = BoardService.getInstance().buildBoard(true);
      if (boardGame.isPlayerATurn()) {
        playerA.setMark(Mark.X);
        playerB.setMark(Mark.O);
      } else {
        playerA.setMark(Mark.O);
        playerB.setMark(Mark.X);
      }

      final Map<Tile, Mark> nextState = new TreeMap<>(boardGame.getBoard());
      while (true) {
        final Bot player = this.getPlayer(playerA, playerB, boardGame.isPlayerATurn());
        final Tile action = player.getNextMove(nextState);
        nextState.put(action, player.getMark());

        if (BoardService.getInstance().isGameOver(nextState)) {
          playerA.update(boardGame.getBoard(), action, nextState);
          break;
        }
        boardGame.setPlayerATurn(!boardGame.isPlayerATurn());
        boardGame.setBoard(nextState);
      }

      if (BoardService.getInstance().getWinMark(nextState) == playerA.getMark()) {
        wins += 1;
      }
      playerA.setEpsilon(playerA.getEpsilon() - 0.0001);
    }
    _logger.info("Games played [" + episodes + "]");
    final int successRate = (wins * 100) / episodes;
    _logger.info("Success rate of QBot [" + successRate + "]");
  }

  private Bot getPlayer(final AgentBot playerA, final Bot playerB, final boolean playerATurn) {
    return playerATurn ? playerA : playerB;
  }
}
