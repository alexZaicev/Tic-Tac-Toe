package com.alexz.tictactoe;

import com.alexz.tictactoe.models.ConfigKey;
import com.alexz.tictactoe.models.players.Difficulty;
import com.alexz.tictactoe.services.AgentService;
import com.alexz.tictactoe.services.CfgProvider;

public class TrainBots {

    public static void main(String[] args) {
        final int episode = CfgProvider.getInstance().getInt(ConfigKey.BOT_Q_AGENT_EPISODES);
        AgentService.getInstance().getTrainQAgent(episode, Difficulty.HARD);
    }
}
