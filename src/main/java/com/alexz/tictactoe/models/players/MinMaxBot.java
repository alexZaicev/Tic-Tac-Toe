package com.alexz.tictactoe.models.players;

import com.alexz.tictactoe.models.ConfigKey;
import com.alexz.tictactoe.models.board.Mark;
import com.alexz.tictactoe.models.board.Tile;
import com.alexz.tictactoe.services.BoardService;
import com.alexz.tictactoe.services.CfgProvider;
import org.apache.commons.lang3.RandomUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MinMaxBot extends Bot {

    private static final Tile[] INITIAL_MOVES = new Tile[]{Tile.A1, Tile.A3, Tile.C1, Tile.C3};
    private final int maxDepth;
    private final int scoreWin;
    private final int scoreLose;
    private final int scoreNeutral;

    public MinMaxBot(Mark mark, Difficulty difficulty) {
        super(mark, difficulty);
        if (this.difficulty == Difficulty.MODERATE) {
            this.maxDepth = CfgProvider.getInstance().getInt(ConfigKey.BOT_DEPTH_MODERATE);
        } else if (this.difficulty == Difficulty.HARD) {
            this.maxDepth = CfgProvider.getInstance().getInt(ConfigKey.BOT_DEPTH_HARD);
        } else {
            this.maxDepth = 0;
        }
        this.scoreWin = CfgProvider.getInstance().getInt(ConfigKey.BOT_SCORE_WIN);
        this.scoreLose = CfgProvider.getInstance().getInt(ConfigKey.BOT_SCORE_LOSE);
        this.scoreNeutral = CfgProvider.getInstance().getInt(ConfigKey.BOT_SCORE_NEUTRAL);
    }

    @Override
    public Tile getNextMove(Map<Tile, Mark> board) {
        if (this.isEmpty(board)) {
            return this.getInitialMove();
        }
        final Map<Tile, Integer> scores = new HashMap<>();
        for (final Tile available : BoardService.getInstance().getAvailableMoves(board)) {
            final Map<Tile, Mark> newState = new HashMap<>(board);
            newState.put(available, this.mark);
            final int score = this.minimax(newState, 0, true);
            scores.put(available, score);
        }

        final List<Map.Entry<Tile, Integer>> result =
                scores.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toList());

        return result.get(result.size() - 1).getKey();
    }

    private int minimax(final Map<Tile, Mark> state, int depth, final boolean opponentTurn) {
        // break recursion if game is over or max depth is reached
        if (BoardService.getInstance().isGameOver(state) || depth >= this.maxDepth) {
            return this.getScore(state, depth);
        }
        depth += 1;
        final Map<Tile, Integer> moveScores = new HashMap<>();

        for (final Tile available : BoardService.getInstance().getAvailableMoves(state)) {
            final Map<Tile, Mark> newState = new HashMap<>(state);
            newState.put(available, this.getMark(opponentTurn));
            moveScores.put(available, this.minimax(newState, depth, !opponentTurn));
        }

        final List<Map.Entry<Tile, Integer>> result =
                moveScores.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toList());
        final int idx = opponentTurn ? 0 : result.size() - 1;
        return result.get(idx).getValue();
    }

    private int getScore(final Map<Tile, Mark> state, final int depth) {
        final Mark winMark = BoardService.getInstance().getWinMark(state);
        if (winMark == Mark.NONE) {
            return this.scoreNeutral;
        } else if (winMark == this.mark) {
            return this.scoreWin - depth;
        } else {
            if (this.scoreLose < 0) {
                return depth + this.scoreLose;
            } else {
                return depth - this.scoreLose;
            }
        }
    }

    private Mark getMark(final boolean opponentTurn) {
        final Mark opMark = this.mark == Mark.X ? Mark.O : Mark.X;
        return opponentTurn ? opMark : this.mark;
    }

    private boolean isEmpty(final Map<Tile, Mark> board) {
        for (final Mark m : board.values()) {
            if (m != Mark.NONE) {
                return false;
            }
        }
        return true;
    }

    private Tile getInitialMove() {
        if (this.difficulty == Difficulty.MODERATE) {
            final Tile[] tiles = Tile.values();
            return tiles[RandomUtils.nextInt(0, tiles.length)];
        } else {
            return INITIAL_MOVES[RandomUtils.nextInt(0, INITIAL_MOVES.length)];
        }
    }
}
