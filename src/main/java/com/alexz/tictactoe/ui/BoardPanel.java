package com.alexz.tictactoe.ui;

import com.alexz.tictactoe.models.ConfigKey;
import com.alexz.tictactoe.models.board.*;
import com.alexz.tictactoe.services.BoardService;
import com.alexz.tictactoe.services.CfgProvider;
import com.alexz.tictactoe.ui.widgets.Button;
import com.alexz.tictactoe.ui.widgets.InfoDialog;
import com.alexz.tictactoe.ui.widgets.Label;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class BoardPanel extends JPanel implements IBoardListener {

  private static final Logger _logger = LogManager.getLogger(BoardPanel.class);
  private Board board;

  public BoardPanel() {
    super();
    this.setLayout(null);
    final int width = CfgProvider.getInstance().getInt(ConfigKey.WINDOW_WIDTH) - 25;
    final int height = CfgProvider.getInstance().getInt(ConfigKey.WINDOW_HEIGHT) - 75;
    this.setSize(new Dimension(width, height));
    this.setPreferredSize(new Dimension(width, height));
    this.setBounds(10, 10, width, height);
  }

  @Override
  public void notify(Board board) {
    if (board.equals(this.board)) {
      return;
    }
    this.board = SerializationUtils.clone(board);
    this.compose();
  }

  private void compose() {
    this.removeAll();
    switch (this.board.getState()) {
      case IN_MENU: {
        this.composeInMenu();
        break;
      }
      case IN_GAME: {
        this.composeInGame();
        break;
      }
      case PLAYER_A_WINS:
      case PLAYER_B_WINS:
      case DRAW: {
        this.composeGameOver();
        break;
      }
    }
    this.revalidate();
    this.repaint();
  }

  private void composeInMenu() {
    Label lbl = new Label("Tic-Tac-Toe", 0, 0, SwingConstants.CENTER);
    lbl.setFont(ConfigKey.FONT_H2);
    lbl.setSize(200, lbl.getFont().getSize());
    lbl.setLocation((this.getWidth() - lbl.getWidth()) / 2, 35);
    this.add(lbl);
  }

  private void composeInGame() {
    Label lbl = new Label("Player A", 0, 0);
    lbl.setFont(ConfigKey.FONT_H4);
    this.add(lbl);

    lbl = new Label("Player B", this.getWidth() - lbl.getWidth(), 0, SwingConstants.RIGHT);
    lbl.setFont(ConfigKey.FONT_H4);
    this.add(lbl);

    final String txt = this.board.isPlayerATurn() ? "Player A turn" : "Player B turn";
    lbl = new Label(txt, (this.getWidth() - lbl.getWidth()) / 2, 35, SwingConstants.CENTER);
    lbl.setFont(ConfigKey.FONT_H5);
    this.add(lbl);

    // build grid
    final JPanel grid = new JPanel(new GridLayout(3, 3));
    for (final Map.Entry<Tile, Mark> e : board.getBoard().entrySet()) {
      final Button btn = new Button(e.getValue().name);
      if (e.getValue() != Mark.NONE || this.board.getState() != BoardState.IN_GAME) {
        btn.setEnabled(false);
      }
      btn.setOnClick(
              r -> {
                if (this.board.isPlayerATurn()) {
                  _logger.debug("Grid button pressed [" + e.getKey() + "]");
                  BoardService.getInstance().onPlayerAction(e.getKey());
                }
              });
      grid.add(btn);
    }
    final int width = this.getWidth() - 100;
    final int height = this.getHeight() - 100;
    grid.setBounds((this.getWidth() - width) / 2, 85, width, height);
    grid.setSize(new Dimension(width, height));
    grid.setPreferredSize(new Dimension(width, height));
    this.add(grid);
  }

  private void composeGameOver() {
    this.composeInGame();

    final String txt;
    if (this.board.getState() == BoardState.PLAYER_A_WINS) {
      txt = "Player A have won this round!";
    } else if (this.board.getState() == BoardState.PLAYER_B_WINS) {
      txt = "Player B have won this round!";
    } else {
      txt = "It`s a Draw!";
    }
    new InfoDialog(null, "Game Finished", txt, r -> BoardService.getInstance().startNewGame())
            .build();
  }
}
