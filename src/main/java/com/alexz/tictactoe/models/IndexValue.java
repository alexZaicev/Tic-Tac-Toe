package com.alexz.tictactoe.models;

import java.io.Serializable;

public class IndexValue implements Serializable, Cloneable {

  private int index;
  private double value;

  public IndexValue() {
  }

  public IndexValue(int index, double value) {
    this.index = index;
    this.value = value;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  public IndexValue clone() {
    try {
      IndexValue clone = (IndexValue) super.clone();
      clone.setValue(value);
      clone.setIndex(index);
      return clone;
    } catch (CloneNotSupportedException e) {
      return null;
    }
  }

  public boolean isValid() {
    return index != -1;
  }
}
