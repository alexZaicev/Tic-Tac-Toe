package com.alexz.tictactoe.ui.widgets;

import javax.swing.*;
import java.awt.*;

public class DialogBase extends JDialog {

    private static final long serialVersionUID = 2270496595170311247L;

    public DialogBase(final JFrame frame) {
        super(frame);
    }

    protected void build() {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(getParent());
        this.setVisible(true);
        this.setLayout(null);
        this.setResizable(false);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);

        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(
                (screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
    }
}
