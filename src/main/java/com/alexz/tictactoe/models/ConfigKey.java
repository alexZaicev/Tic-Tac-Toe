package com.alexz.tictactoe.models;

public enum ConfigKey {

    TITLE("title"),
    VERSION("version"),
    WINDOW_WIDTH("window.size.width"),
    WINDOW_HEIGHT("window.size.height"),
    FONT_H1("font.h1"),
    FONT_H2("font.h2"),
    FONT_H3("font.h3"),
    FONT_H4("font.h4"),
    FONT_H5("font.h5"),
    FONT_H6("font.h6"),
    FONT_P("font.p"),
    DIFFICULTY("difficulty"),
    BOT_PAUSE_MIN("bot.pause.min"),
    BOT_PAUSE_MAX("bot.pause.max"),
    BOT_DEPTH_MODERATE("bot.depth.moderate"),
    BOT_DEPTH_HARD("bot.depth.hard"),
    BOT_SCORE_WIN("bot.score.win"),
    BOT_SCORE_LOSE("bot.score.lose"),
    BOT_SCORE_NEUTRAL("bot.score.neutral"),
    BOT_REWARD_POSITIVE("bot.reward.positive"),
    BOT_REWARD_NEGATIVE("bot.reward.negative"),
    BOT_REWARD_NEUTRAL("bot.reward.neutral"),
    BOT_Q_AGENT_EPISODES("bot.q_agent.episodes"),
    BOT_Q_AGENT_ALPHA("bot.q_agent.alpha"),
    BOT_Q_AGENT_DISCOUNT("bot.q_agent.discount"),
    BOT_Q_AGENT_EPSILON("bot.q_agent.epsilon"),
    ;

    public final String name;

    ConfigKey(final String name) {
        this.name = name;
    }

}
