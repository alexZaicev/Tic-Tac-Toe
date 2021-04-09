package com.alexz.tictactoe.services;

import com.alexz.tictactoe.models.board.Mark;
import com.alexz.tictactoe.models.players.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BotFactory {

    private static final Logger _logger = LogManager.getLogger(BotFactory.class);

    public static Bot getBot(final Mark mark, final Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return new NaiveBot(mark);
            case MODERATE:
            case HARD:
                return new MinMaxBot(mark, difficulty);
            case EXPERT:
                return new QBot(mark);
        }
        _logger.warn("Could not construct bot for an unsupported difficulty [" + difficulty + "]");
        return new NaiveBot(mark);
    }
}
