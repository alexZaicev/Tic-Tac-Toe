package com.alexz.tictactoe.ui;

import com.alexz.tictactoe.models.ConfigKey;
import com.alexz.tictactoe.services.BoardService;
import com.alexz.tictactoe.services.CfgProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainView extends JFrame implements WindowListener {

    private static final Logger _logger = LogManager.getLogger(MainView.class);

    public MainView() throws HeadlessException {
        super(
                String.format(
                        "%s (v.%s)",
                        CfgProvider.getInstance().getStr(ConfigKey.TITLE),
                        CfgProvider.getInstance().getStr(ConfigKey.VERSION)));
        this.compose();
        BoardService.getInstance().notifyListeners();
        this.pack();
    }

    private void compose() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(this);
        this.setLayout(null);
        this.setResizable(false);
        this.setVisible(true);

        final int width = CfgProvider.getInstance().getInt(ConfigKey.WINDOW_WIDTH);
        final int height = CfgProvider.getInstance().getInt(ConfigKey.WINDOW_HEIGHT);
        this.setSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2);

        this.setJMenuBar(new MenuBar(this));

        final BoardPanel boardPanel = new BoardPanel();
        BoardService.getInstance().registerListener(boardPanel);
        this.add(boardPanel);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        _logger.debug("Closing");
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
