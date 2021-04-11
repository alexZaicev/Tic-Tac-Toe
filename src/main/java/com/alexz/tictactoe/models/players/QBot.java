package com.alexz.tictactoe.models.players;

import com.alexz.tictactoe.models.ConfigKey;
import com.alexz.tictactoe.models.board.Mark;
import com.alexz.tictactoe.models.board.Tile;
import com.alexz.tictactoe.services.BoardService;
import com.alexz.tictactoe.services.CfgProvider;
import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class QBot extends AgentBot {

  private static final Logger _logger = LogManager.getLogger(QBot.class);
  private final float alpha;
  private final float discount;
  private final Map<String, Map<Tile, Double>> stateValues;
  private double epsilon;

  public QBot(Mark mark) {
    super(mark, Difficulty.EXPERT);
    this.epsilon = CfgProvider.getInstance().getFloat(ConfigKey.BOT_Q_AGENT_EPSILON);
    this.alpha = CfgProvider.getInstance().getFloat(ConfigKey.BOT_Q_AGENT_ALPHA);
    this.discount = CfgProvider.getInstance().getFloat(ConfigKey.BOT_Q_AGENT_DISCOUNT);
    this.stateValues = new HashMap<>();
  }

  @Override
  public Tile getNextMove(final Map<Tile, Mark> board) {
    final String state = this.getState(board);
    final List<Tile> availableMoves = BoardService.getInstance().getAvailableMoves(board);
    final double r = RandomUtils.nextFloat(0F, 2F);
    if (r < this.epsilon) {
      return availableMoves.get(RandomUtils.nextInt(0, availableMoves.size()));
    }
    final Tile bestMove = this.getBestAction(state);
    if (bestMove == null) {
      return availableMoves.get(RandomUtils.nextInt(0, availableMoves.size()));
    }
    return bestMove;
  }

  @Override
  public void update(
      final Map<Tile, Mark> board, final Tile action, final Map<Tile, Mark> nextBoard) {
    final String state = this.getState(board);
    final String nextState = this.getState(nextBoard);
    final double reward = this.getReward(BoardService.getInstance().getWinMark(nextBoard));

    if (!this.stateValues.containsKey(state)) {
      this.stateValues.put(state, new HashMap<>());
    }
    if (!this.stateValues.get(state).containsKey(action)) {
      this.stateValues.get(state).put(action, 0.0);
    }
    double value = this.stateValues.get(state).get(action);
    final List<Double> nextStateValues = new ArrayList<>();
    if (this.stateValues.containsKey(nextState)) {
      nextStateValues.addAll(this.stateValues.get(nextState).values());
    }
    nextStateValues.sort(Collections.reverseOrder());
    final double nextValue = nextStateValues.isEmpty() ? 0.0 : nextStateValues.get(0);
    value = value + this.alpha * (reward + this.discount * nextValue - value);
    this.stateValues.get(state).put(action, value);
  }

  private Tile getBestAction(final String state) {
    if (this.stateValues.containsKey(state)) {
      final Map<Tile, Double> actions = this.stateValues.get(state);
      Map.Entry<Tile, Double> max = null;
      for (final Map.Entry<Tile, Double> e : actions.entrySet()) {
        if (max == null) {
          max = e;
          continue;
        }
        if (max.getValue() < e.getValue()) {
          max = e;
        }
      }
      return max == null ? null : max.getKey();
    }
    return null;
  }

  public double getEpsilon() {
    return epsilon;
  }

  public void setEpsilon(double epsilon) {
    this.epsilon = epsilon;
  }
}
