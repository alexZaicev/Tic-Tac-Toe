package com.alexz.tictactoe.models.players;

import com.alexz.tictactoe.models.ConfigKey;
import com.alexz.tictactoe.models.board.Mark;
import com.alexz.tictactoe.models.board.Tile;
import com.alexz.tictactoe.services.BoardService;
import com.alexz.tictactoe.services.CfgProvider;

import java.util.*;

public abstract class AgentBot extends Bot {

    protected final List<AgentMove> agentMoves;
    private final double rewardPositive;
    private final double rewardNegative;
    private final double rewardNeutral;

    public AgentBot(Mark mark, Difficulty difficulty) {
        super(mark, difficulty);
        this.agentMoves = new ArrayList<>();
        this.rewardPositive = CfgProvider.getInstance().getDouble(ConfigKey.BOT_REWARD_POSITIVE);
        this.rewardNegative = CfgProvider.getInstance().getDouble(ConfigKey.BOT_REWARD_NEGATIVE);
        this.rewardNeutral = CfgProvider.getInstance().getDouble(ConfigKey.BOT_REWARD_NEUTRAL);
    }

    public abstract void updateStrategy(final Map<Tile, Mark> board);

    public void reset() {
        this.agentMoves.clear();
    }

    protected double getReward(final Mark mark) {
        if (mark == this.mark) {
            return this.rewardPositive;
        } else if (mark != Mark.NONE) {
            return this.rewardNegative;
        }
        return this.rewardNeutral;
    }

    protected int getState(final Map<Tile, Mark> board) {
        int state = 0;
        for (final Mark mark : board.values()) {
            if (mark != Mark.NONE) {
                state = state * 3 + this.getMarkValue(mark);
            }
        }
        return state;
    }

    protected Set<Integer> getPossibleActions(final Map<Tile, Mark> board) {
        final Set<Integer> actions = new HashSet<>();
        final List<Tile> tiles = BoardService.getInstance().getAvailableMoves(board);
        for (final Tile tile : tiles) {
            final int action = tile.toInteger();
            actions.add(action);
        }
        return actions;
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
}
