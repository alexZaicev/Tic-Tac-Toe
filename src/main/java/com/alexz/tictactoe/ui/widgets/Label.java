package com.alexz.tictactoe.ui.widgets;

import com.alexz.tictactoe.models.ConfigKey;
import com.alexz.tictactoe.services.CfgProvider;

import javax.swing.*;
import java.awt.*;

public class Label extends JLabel {

  private static final long serialVersionUID = -3641364140662611153L;

  public Label(final String text, final int align) {
    this(text, 0, 0, align);
  }

  public Label(final String text, final int x, final int y) {
    this(text, x, y, LEFT);
  }

  public Label(final String text, final int x, final int y, final int align) {
    super(text, align);
    this.setFont((Font) CfgProvider.getInstance().get(ConfigKey.FONT_P));
    this.setSize(150, 20);
    this.setLocation(x, y);
  }

  public void setFont(final ConfigKey key) {
    this.setFont((Font) CfgProvider.getInstance().get(key));
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
