package com.alexz.tictactoe.ui.widgets;

import com.alexz.tictactoe.models.ConfigKey;
import com.alexz.tictactoe.models.ICallable;
import com.alexz.tictactoe.services.CfgProvider;

import javax.swing.*;
import java.awt.*;

public class InfoDialog extends DialogBase {

  private static final long serialVersionUID = -5722821657160125964L;
  private final String title;
  private final String message;
  private final ICallable onOK;
  private final ICallable onCancel;

  public InfoDialog(
          final JFrame frame, final String title, final String message, final ICallable onOK) {
    this(frame, title, message, onOK, null);
  }

  public InfoDialog(
          final JFrame frame,
          final String title,
          final String message,
          final ICallable onOK,
          final ICallable onCancel) {
    super(frame);
    this.title = title;
    this.message = message;
    this.onOK = onOK;
    if (onCancel == null) {
      this.onCancel = r -> this.dispose();
    } else {
      this.onCancel = onCancel;
    }
  }

  @Override
  public void build() {
    super.build();
    this.setTitle(title);

    final int width = 300;
    final int height = 110;
    this.setSize(width, height);

    final TextArea txt = new TextArea(this.message, true, false);
    txt.setLocation(10, 15);
    txt.setSize(new Dimension(width - 25, 30));
    txt.setFont((Font) CfgProvider.getInstance().get(ConfigKey.FONT_H6));
    txt.setBackground(this.getBackground());
    this.add(txt);

    Button btn =
            new Button(
                    "OK",
                    obj -> {
                      this.onOK.call(null);
                      this.dispose();
                    });
    btn.setSize(120, 25);
    btn.setLocation(width - btn.getWidth() - 15, height - 60);
    this.add(btn);

    btn = new Button("cancel", this.onCancel);
    btn.setSize(120, 25);
    btn.setLocation(10, height - 60);
    this.add(btn);
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
