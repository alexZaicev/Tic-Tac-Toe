package com.alexz.tictactoe.services;

import com.alexz.tictactoe.models.ConfigKey;
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
        {
          final QBot bot = new QBot(null);
          final int episode = CfgProvider.getInstance().getInt(ConfigKey.BOT_Q_AGENT_EPISODES);
          AgentService.getInstance().trainQBot(bot, episode, Difficulty.EASY);
          bot.setMark(mark);
          return bot;
        }
    }
    _logger.warn("Could not construct bot for an unsupported difficulty [" + difficulty + "]");
    return new NaiveBot(mark);
  }
}
