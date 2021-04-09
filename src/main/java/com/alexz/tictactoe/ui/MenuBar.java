package com.alexz.tictactoe.ui;

import com.alexz.tictactoe.models.players.Difficulty;
import com.alexz.tictactoe.services.BoardService;
import org.apache.commons.text.CaseUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class MenuBar extends JMenuBar implements ActionListener {

  private static final Logger _logger = LogManager.getLogger(MenuBar.class);
  private final JFrame frame;
  private final List<JMenuItem> difficultyRadios = new ArrayList<>();

  public MenuBar(final JFrame frame) {
    super();
    this.frame = frame;
    this.compose();
  }

  private void compose() {
    this.setVisible(true);
    final JMenu gameMenu = new JMenu("Game");
    gameMenu.getAccessibleContext().setAccessibleName("Game menu options");
    this.add(gameMenu);

    JMenuItem menuItem = new JMenuItem("New Game");
    menuItem.setActionCommand(Event.ON_NEW_GAME.name());
    menuItem.getAccessibleContext().setAccessibleName("Start new game");
    menuItem.addActionListener(this);
    gameMenu.add(menuItem);

    // #######################

    final JMenu difficultyMenu = new JMenu("Difficulty");
    difficultyMenu.getAccessibleContext().setAccessibleName("Difficulty level of the bot");
    gameMenu.add(difficultyMenu);

    for (final Difficulty d : Difficulty.values()) {
      menuItem = new JRadioButtonMenuItem(CaseUtils.toCamelCase(d.toString(), true));
      menuItem.setActionCommand(Event.ON_DIFFICULTY_CHANGED.name());
      menuItem.addActionListener(this);
      this.difficultyRadios.add(menuItem);
      difficultyMenu.add(menuItem);
    }


    this.updateSelectedDifficulty();

    // #######################

    menuItem = new JMenuItem("Exit");
    menuItem.setActionCommand(Event.ON_EXIT.name());
    menuItem.getAccessibleContext().setAccessibleName("Exit application");
    menuItem.addActionListener(this);
    gameMenu.add(menuItem);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    _logger.debug("Pressed " + e.getActionCommand());
    final Event event = Event.valueOf(e.getActionCommand());
    switch (event) {
      case ON_NEW_GAME: {
        BoardService.getInstance().startNewGame();
        break;
      }
      case ON_EXIT: {
        frame.dispatchEvent(new WindowEvent(this.frame, WindowEvent.WINDOW_CLOSING));
        break;
      }
      case ON_DIFFICULTY_CHANGED: {
        final JRadioButtonMenuItem item = ((JRadioButtonMenuItem) e.getSource());
        final Difficulty d = Difficulty.valueOf(item.getText().toUpperCase());
        BoardService.getInstance().setDifficulty(d);
        this.updateSelectedDifficulty();
        break;
      }
    }
  }

  private void updateSelectedDifficulty() {
    for (final JMenuItem item : this.difficultyRadios) {
      final Difficulty d = Difficulty.valueOf(item.getText().toUpperCase());
      item.setSelected(BoardService.getInstance().getDifficulty() == d);
    }
  }

  enum Event {
    ON_NEW_GAME,
    ON_EXIT,
    ON_DIFFICULTY_CHANGED,
  }
}
