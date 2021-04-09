package com.alexz.tictactoe.models.board;

import com.alexz.tictactoe.models.players.PlayerBase;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Map;

public class Board implements Serializable {

  private PlayerBase playerA;
  private PlayerBase playerB;
  private boolean playerATurn;

  private BoardState state;
  private Map<Tile, Mark> board;

  public PlayerBase getPlayerA() {
    return playerA;
  }

  public void setPlayerA(PlayerBase playerA) {
    this.playerA = playerA;
  }

  public PlayerBase getPlayerB() {
    return playerB;
  }

  public void setPlayerB(PlayerBase playerB) {
    this.playerB = playerB;
  }

  public BoardState getState() {
    return state;
  }

  public void setState(BoardState state) {
    this.state = state;
  }

  public Map<Tile, Mark> getBoard() {
    return board;
  }

  public void setBoard(Map<Tile, Mark> board) {
    this.board = board;
  }

  public boolean isPlayerATurn() {
    return playerATurn;
  }

  public void setPlayerATurn(boolean playerATurn) {
    this.playerATurn = playerATurn;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (!(o instanceof Board)) return false;

    Board board1 = (Board) o;

    return new EqualsBuilder()
            .append(getPlayerA(), board1.getPlayerA())
            .append(getPlayerB(), board1.getPlayerB())
            .append(getState(), board1.getState())
            .append(getBoard(), board1.getBoard())
            .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
            .append(getPlayerA())
            .append(getPlayerB())
            .append(getState())
            .append(getBoard())
            .toHashCode();
  }
}
