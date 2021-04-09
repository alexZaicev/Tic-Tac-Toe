package com.alexz.tictactoe.models.players;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class AgentMove implements Serializable {

  private final int oldState;
  private final int newState;
  private final int action;
  private final Set<Integer> possibleActions;
  private double reward;

  public AgentMove(int oldState, int newState, int action, double reward) {
    this.oldState = oldState;
    this.newState = newState;
    this.action = action;
    this.reward = reward;
    this.possibleActions = new HashSet<>();
  }

  public AgentMove(
          int oldState, int newState, int action, double reward, Set<Integer> possibleActions) {
    this.oldState = oldState;
    this.newState = newState;
    this.action = action;
    this.reward = reward;
    this.possibleActions = possibleActions;
  }

  public int getOldState() {
    return oldState;
  }

  public int getNewState() {
    return newState;
  }

  public int getAction() {
    return action;
  }

  public double getReward() {
    return reward;
  }

  public void setReward(double reward) {
    this.reward = reward;
  }

  public Set<Integer> getPossibleActions() {
    return possibleActions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (!(o instanceof AgentMove)) return false;

    AgentMove agentMove = (AgentMove) o;

    return new EqualsBuilder()
            .append(getOldState(), agentMove.getOldState())
            .append(getNewState(), agentMove.getNewState())
            .append(getAction(), agentMove.getAction())
            .append(getReward(), agentMove.getReward())
            .append(getPossibleActions(), agentMove.getPossibleActions())
            .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
            .append(getOldState())
            .append(getNewState())
            .append(getAction())
            .append(getReward())
            .append(getPossibleActions())
            .toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
            .append("oldState", oldState)
            .append("newState", newState)
            .append("action", action)
            .append("reward", reward)
            .append("possibleActions", possibleActions)
            .toString();
  }
}
