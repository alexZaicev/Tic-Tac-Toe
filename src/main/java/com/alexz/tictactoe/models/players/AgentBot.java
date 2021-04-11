package com.alexz.tictactoe.models.players;

import com.alexz.tictactoe.models.ConfigKey;
import com.alexz.tictactoe.models.board.Mark;
import com.alexz.tictactoe.models.board.Tile;
import com.alexz.tictactoe.services.CfgProvider;

import java.util.Map;

public abstract class AgentBot extends Bot {

  private final double rewardPositive;
  private final double rewardNegative;
  private final double rewardNeutral;

  public AgentBot(Mark mark, Difficulty difficulty) {
    super(mark, difficulty);
    this.rewardPositive = CfgProvider.getInstance().getDouble(ConfigKey.BOT_REWARD_POSITIVE);
    this.rewardNegative = CfgProvider.getInstance().getDouble(ConfigKey.BOT_REWARD_NEGATIVE);
    this.rewardNeutral = CfgProvider.getInstance().getDouble(ConfigKey.BOT_REWARD_NEUTRAL);
  }

  public abstract void update(
      final Map<Tile, Mark> board, final Tile action, final Map<Tile, Mark> nextBoard);

  protected double getReward(final Mark mark) {
    if (mark == this.mark) {
      return this.rewardPositive;
    } else if (mark != Mark.NONE) {
      return this.rewardNegative;
    }
    return this.rewardNeutral;
  }

  protected String getState(final Map<Tile, Mark> board) {
    final StringBuilder sb = new StringBuilder();
    for (final Map.Entry<Tile, Mark> e : board.entrySet()) {
      sb.append(String.format("[%s@%s]", e.getKey(), e.getValue()));
    }
    return sb.toString();
  }

  private int getMarkValue(final Mark mark) {
    if (mark == this.mark) {
      return 1;
    } else if (this.mark == Mark.NONE) {
      return 0;
    } else {
      return 0;
    }
  }

  public double getRewardPositive() {
    return rewardPositive;
  }

  public double getRewardNegative() {
    return rewardNegative;
  }

  public double getRewardNeutral() {
    return rewardNeutral;
  }
}
