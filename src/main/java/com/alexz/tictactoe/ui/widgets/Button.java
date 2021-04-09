package com.alexz.tictactoe.ui.widgets;

import com.alexz.tictactoe.models.ConfigKey;
import com.alexz.tictactoe.models.ICallable;
import com.alexz.tictactoe.services.CfgProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Button extends JButton {

  private static final long serialVersionUID = 6175488897663485105L;

  public Button(final String text) {
    this(text, null);
  }

  public Button(final String text, final ICallable callable) {
    super(text.toUpperCase());
    this.setFont((Font) CfgProvider.getInstance().get(ConfigKey.FONT_H6));
    this.setSize(85, 30);
    if (callable != null) {
      this.addActionListener(e -> callable.call(null));
    }
  }

  @Override
  public void setSize(Dimension d) {
    super.setSize(d);
    this.setPreferredSize(d);
  }

  @Override
  public void setSize(int width, int height) {
    super.setSize(width, height);
    this.setPreferredSize(new Dimension(width, height));
  }

  public void setOnClick(final ICallable callable) {
    final ActionListener[] listeners = this.getActionListeners();
    if (listeners != null && listeners.length > 0) {
      for (final ActionListener l : this.getActionListeners()) {
        this.removeActionListener(l);
      }
    }
    this.addActionListener(e -> callable.call(null));
  }
}
