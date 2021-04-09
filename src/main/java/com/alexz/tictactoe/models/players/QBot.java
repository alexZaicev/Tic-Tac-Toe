package com.alexz.tictactoe.models.players;

import com.alexz.tictactoe.models.ConfigKey;
import com.alexz.tictactoe.models.board.Mark;
import com.alexz.tictactoe.models.board.Tile;
import com.alexz.tictactoe.services.AgentService;
import com.alexz.tictactoe.services.BoardService;
import com.alexz.tictactoe.services.CfgProvider;
import com.github.chen0040.rl.learning.qlearn.QLearner;
import com.github.chen0040.rl.utils.IndexValue;
import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class QBot extends AgentBot {

  private static final Logger _logger = LogManager.getLogger(QBot.class);
  private final QLearner agent;

  public QBot(Mark mark) {
    super(mark, Difficulty.EXPERT);
    this.agent =
            AgentService.getInstance()
                    .getTrainQAgent(
                            CfgProvider.getInstance().getInt(ConfigKey.BOT_Q_AGENT_EPISODES), Difficulty.EASY);
  }

  public QBot(final Mark mark, final QLearner agent) {
    super(mark, Difficulty.EXPERT);
    this.agent = agent;
  }

  @Override
  public Tile getNextMove(final Map<Tile, Mark> board) {

    final int state = this.getState(board);
    final Set<Integer> possibleActions = this.getPossibleActions(board);

    if (possibleActions.isEmpty()) {
      return null;
    }

    final IndexValue iv = this.agent.selectAction(state, possibleActions);
    int action = iv.getIndex();
    final double val = iv.getValue();

    if (val <= 0) {
      final List<Integer> possibleActionsList = new ArrayList<>(possibleActions);
      action = possibleActionsList.get(RandomUtils.nextInt(0, possibleActionsList.size()));
    }
    final Tile tile = Tile.fromInteger(action);
    if (tile != null) {
      _logger.debug("QBot next move [" + tile + "]");
      final Map<Tile, Mark> newBoard = new HashMap<>(board);
      newBoard.put(tile, this.mark);
      final AgentMove move = new AgentMove(state, this.getState(newBoard), action, 0.0);
      this.agentMoves.add(move);
    }
    return tile;
  }

  @Override
  public void updateStrategy(final Map<Tile, Mark> board) {
    final Mark winMark = BoardService.getInstance().getWinMark(board);
    final double reward = this.getReward(winMark);

    // update agent with move history in reversed order, but skip the first move
    for (int i = this.agentMoves.size() - 1; i >= 0; --i) {
      final AgentMove move = this.agentMoves.get(i);
      if (i >= this.agentMoves.size() - 2) {
        move.setReward(reward);
      }
      this.agent.update(move.getOldState(), move.getAction(), move.getNewState(), move.getReward());
    }
  }
}
