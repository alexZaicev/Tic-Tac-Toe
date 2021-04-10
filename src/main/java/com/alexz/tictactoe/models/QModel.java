package com.alexz.tictactoe.models;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.*;

public class QModel implements Serializable, Cloneable {

  /**
   * Q value for (state_id, action_id) pair Q is known as the quality of state-action combination,
   * note that it is different from utility of a state
   */
  private Matrix Q;
  /** $\alpha[s, a]$ value for learning rate: alpha(state_id, action_id) */
  private Matrix alphaMatrix;
  /** discount factor */
  private final double gamma = 0.7;

  private int stateCount;
  private int actionCount;

  public QModel(int stateCount, int actionCount, double initialQ) {
    this.stateCount = stateCount;
    this.actionCount = actionCount;
    Q = new Matrix(stateCount, actionCount);
    alphaMatrix = new Matrix(stateCount, actionCount);
    Q.setAll(initialQ);
    alphaMatrix.setAll(0.1);
  }

  public QModel(int stateCount, int actionCount) {
    this(stateCount, actionCount, 0.1);
  }

  public QModel() {}

  public double getQ(int stateId, int actionId) {
    return Q.get(stateId, actionId);
  }

  public void setQ(int stateId, int actionId, double Qij) {
    Q.set(stateId, actionId, Qij);
  }

  public double getAlpha(int stateId, int actionId) {
    return alphaMatrix.get(stateId, actionId);
  }

  public void setAlpha(double defaultAlpha) {
    this.alphaMatrix.setAll(defaultAlpha);
  }

  public IndexValue actionWithMaxQAtState(int stateId, Set<Integer> actionsAtState) {
    Vector rowVector = Q.rowAt(stateId);
    return rowVector.indexWithMaxValue(actionsAtState);
  }

  private void reset(double initialQ) {
    Q.setAll(initialQ);
  }

  public IndexValue actionWithSoftMaxQAtState(
      int stateId, Set<Integer> actionsAtState, Random random) {
    Vector rowVector = Q.rowAt(stateId);
    double sum = 0;

    if (actionsAtState == null) {
      actionsAtState = new HashSet<>();
      for (int i = 0; i < actionCount; ++i) {
        actionsAtState.add(i);
      }
    }

    List<Integer> actions = new ArrayList<>(actionsAtState);

    double[] acc = new double[actions.size()];
    for (int i = 0; i < actions.size(); ++i) {
      sum += rowVector.get(actions.get(i));
      acc[i] = sum;
    }

    double r = random.nextDouble() * sum;

    IndexValue result = new IndexValue();
    for (int i = 0; i < actions.size(); ++i) {
      if (acc[i] >= r) {
        int actionId = actions.get(i);
        result.setIndex(actionId);
        result.setValue(rowVector.get(actionId));
        break;
      }
    }

    return result;
  }

  public QModel clone() {
    try {
      super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return SerializationUtils.clone(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (!(o instanceof QModel)) return false;

    QModel qModel = (QModel) o;

    return new EqualsBuilder()
        .append(gamma, qModel.gamma)
        .append(stateCount, qModel.stateCount)
        .append(actionCount, qModel.actionCount)
        .append(Q, qModel.Q)
        .append(alphaMatrix, qModel.alphaMatrix)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(Q)
        .append(alphaMatrix)
        .append(gamma)
        .append(stateCount)
        .append(actionCount)
        .toHashCode();
  }
}
