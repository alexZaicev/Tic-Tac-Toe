package com.alexz.tictactoe.services;

import com.alexz.tictactoe.models.board.Board;
import com.alexz.tictactoe.models.board.Mark;
import com.alexz.tictactoe.models.board.Tile;
import com.alexz.tictactoe.models.players.Bot;
import com.alexz.tictactoe.models.players.Difficulty;
import com.alexz.tictactoe.models.players.QBot;
import com.github.chen0040.rl.learning.qlearn.QLearner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

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

  public QLearner getTrainQAgent(final int episodes, final Difficulty difficulty) {
    final int stateCount = (int) Math.pow(3, 9);
    final int actionCount = 9;

    final QLearner agent = new QLearner(stateCount, actionCount);
    final QBot playerA = new QBot(null, agent);
    final Bot playerB = BotFactory.getBot(null, difficulty);

    int wins = 0;

    for (int i = 0; i < episodes; ++i) {
      playerA.reset();
      final Board boardGame = BoardService.getInstance().buildBoard(true);
      if (boardGame.isPlayerATurn()) {
        playerA.setMark(Mark.X);
        playerB.setMark(Mark.O);
      } else {
        playerA.setMark(Mark.O);
        playerB.setMark(Mark.X);
      }
      final Map<Tile, Mark> board = boardGame.getBoard();

      while (!BoardService.getInstance().isGameOver(board)) {
        final Tile moveA = playerA.getNextMove(board);
        board.put(moveA, playerA.getMark());

        if (BoardService.getInstance().isGameOver(board)) {
          break;
        }

        final Tile moveB = playerB.getNextMove(board);
        board.put(moveB, playerB.getMark());
      }

      playerA.updateStrategy(board);
      if (BoardService.getInstance().getWinMark(board) == playerA.getMark()) {
        wins += 1;
      }
      final int gamesPlayed = i + 1;
      _logger.info("Games played [" + gamesPlayed + "]");
      final int successRate = (wins * 100) / gamesPlayed;
      _logger.info("Success rate of QBot [" + successRate + "]");
    }

    return agent;
  }
}
