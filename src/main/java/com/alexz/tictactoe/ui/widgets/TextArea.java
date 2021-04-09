package com.alexz.tictactoe.ui.widgets;

import com.alexz.tictactoe.models.ConfigKey;
import com.alexz.tictactoe.services.CfgProvider;

import javax.swing.*;
import java.awt.*;

public class TextArea extends JTextArea {

  public TextArea(final String text, final boolean wrap, final boolean editable) {
    this(text, wrap, 0, 0, editable, 0, 0);
  }

  public TextArea(
          final String text,
          final boolean wrap,
          final int x,
          final int y,
          final boolean editable,
          final int row,
          final int column) {
    super(text, row, column);
    this.setWrapStyleWord(wrap);
    this.setLineWrap(wrap);
    this.setLocation(x, y);
    this.setFont((Font) CfgProvider.getInstance().get(ConfigKey.FONT_P));
    this.setEditable(editable);
  }

  @Override
  public void setSize(int width, int height) {
    super.setSize(width, height);
    this.setPreferredSize(new Dimension(width, height));
  }

  @Override
  public void setSize(Dimension d) {
    super.setSize(d);
    this.setPreferredSize(d);
  }
}
