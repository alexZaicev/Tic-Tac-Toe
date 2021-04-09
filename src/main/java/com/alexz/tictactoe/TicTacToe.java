package com.alexz.tictactoe;

import com.alexz.tictactoe.ui.MainView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

public class TicTacToe {

    private static final Logger _logger = LogManager.getLogger(TicTacToe.class);

    public static void main(String[] args) {
        final String clazz = UIManager.getCrossPlatformLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(clazz);
        } catch (final ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            _logger.error("Failed to set native look & feel", ex);
        }
        new MainView();
    }

}
