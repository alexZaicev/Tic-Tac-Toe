package com.alexz.tictactoe.services;

import com.alexz.tictactoe.models.ConfigKey;
import com.alexz.tictactoe.models.board.*;
import com.alexz.tictactoe.models.players.Bot;
import com.alexz.tictactoe.models.players.Difficulty;
import com.alexz.tictactoe.models.players.Player;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BoardService {

  private static final Object lock = new Object();
  private static BoardService instance;

  private final List<IBoardListener> listeners = new ArrayList<>();
  private Board board;
  private Difficulty difficulty;

  public BoardService() {
    this.difficulty = Difficulty.valueOf(CfgProvider.getInstance().getStr(ConfigKey.DIFFICULTY));
    this.board = this.buildBoard(false);
  }

  public static BoardService getInstance() {
    BoardService result = instance;
    if (result == null) {
      synchronized (lock) {
        result = instance;
        if (result == null) instance = result = new BoardService();
      }
    }
    return result;
  }

  public void registerListener(final IBoardListener listener) {
    listeners.add(listener);
  }

  public void notifyListeners() {
    // check if bot has next turn
    if (this.board.getState() == BoardState.IN_GAME && !this.board.isPlayerATurn()) {
      BoardService.getInstance().requestBotAction();
    }
    for (final IBoardListener l : listeners) {
      l.notify(this.board);
    }
  }

  public void startNewGame() {
    this.board = this.buildBoard(false);
    this.board.setState(BoardState.IN_GAME);
    this.notifyListeners();
  }

  public Difficulty getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(Difficulty difficulty) {
    this.difficulty = difficulty;
  }

  public void onPlayerAction(final Tile tile) {
    final Mark mark =
            this.board.isPlayerATurn()
                    ? this.board.getPlayerA().getMark()
                    : this.board.getPlayerB().getMark();
    this.board.getBoard().put(tile, mark);
    this.board.setPlayerATurn(!this.board.isPlayerATurn());

    // check if game is over
    final Mark winMark = this.getWinMark(this.board.getBoard());
    if (winMark != Mark.NONE) {
      if (winMark == this.board.getPlayerA().getMark()) {
        this.board.setState(BoardState.PLAYER_A_WINS);
      } else {
        this.board.setState(BoardState.PLAYER_B_WINS);
      }
    } else if (this.isDraw(this.board.getBoard())) {
      this.board.setState(BoardState.DRAW);
    }

    this.notifyListeners();
  }

  public void requestBotAction() {
    final Board copy = SerializationUtils.clone(this.board);
    final BotWorker worker = new BotWorker((Bot) copy.getPlayerB(), copy.getBoard());
    worker.execute();
  }

  public Mark getWinMark(final Map<Tile, Mark> board) {
    Mark winMark = Mark.NONE;
    for (final ImmutableTriple<Mark, Mark, Mark> combo : this.getBoardCombos(board)) {
      if (combo.getLeft() == Mark.NONE
              || combo.getMiddle() == Mark.NONE
              || combo.getRight() == Mark.NONE) {
        continue;
      }
      if (combo.getLeft() == combo.getMiddle() && combo.getLeft() == combo.getRight()) {
        winMark = combo.getLeft();
        break;
      }
    }
    return winMark;
  }

  public boolean isDraw(final Map<Tile, Mark> board) {
    int numOfMoves = 0;
    for (final Mark m : board.values()) {
      if (m == Mark.NONE) {
        numOfMoves += 1;
      }
    }
    final Mark winMark = this.getWinMark(board);
    return numOfMoves == 0 && winMark == Mark.NONE;
  }

  public Board buildBoard(final boolean empty) {
    final Board b = new Board();

    b.setState(BoardState.IN_MENU);
    b.setBoard(new TreeMap<>());
    for (final Tile t : Tile.values()) {
      b.getBoard().put(t, Mark.NONE);
    }

    final boolean playerATurn = RandomUtils.nextInt(0, 10) % 2 == 0;
    b.setPlayerATurn(playerATurn);
    if (!empty) {
      final Player pa = new Player(playerATurn ? Mark.X : Mark.O);
      final Bot pb = BotFactory.getBot(playerATurn ? Mark.O : Mark.X, this.difficulty);
      b.setPlayerA(pa);
      b.setPlayerB(pb);
    }

    return b;
  }

  public boolean isGameOver(final Map<Tile, Mark> state) {
    if (this.getAvailableMoves(state).isEmpty()) {
      return true;
    }
    final Mark winMark = BoardService.getInstance().getWinMark(state);
    return winMark != Mark.NONE;
  }

  public List<Tile> getAvailableMoves(final Map<Tile, Mark> board) {
    final List<Tile> moves = new ArrayList<>();
    for (final Tile tile : board.keySet()) {
      if (board.get(tile) == Mark.NONE) {
        moves.add(tile);
      }
    }
    return moves;
  }

  private List<ImmutableTriple<Mark, Mark, Mark>> getBoardCombos(final Map<Tile, Mark> board) {
    final List<ImmutableTriple<Mark, Mark, Mark>> combos = new ArrayList<>();
    combos.add(ImmutableTriple.of(board.get(Tile.A1), board.get(Tile.A2), board.get(Tile.A3)));
    combos.add(ImmutableTriple.of(board.get(Tile.B1), board.get(Tile.B2), board.get(Tile.B3)));
    combos.add(ImmutableTriple.of(board.get(Tile.C1), board.get(Tile.C2), board.get(Tile.C3)));
    combos.add(ImmutableTriple.of(board.get(Tile.A1), board.get(Tile.B1), board.get(Tile.C1)));
    combos.add(ImmutableTriple.of(board.get(Tile.A2), board.get(Tile.B2), board.get(Tile.C2)));
    combos.add(ImmutableTriple.of(board.get(Tile.A3), board.get(Tile.B3), board.get(Tile.C3)));
    combos.add(ImmutableTriple.of(board.get(Tile.A1), board.get(Tile.B2), board.get(Tile.C3)));
    combos.add(ImmutableTriple.of(board.get(Tile.A3), board.get(Tile.B2), board.get(Tile.C1)));
    return combos;
  }
}
