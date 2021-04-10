package com.alexz.tictactoe.models;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class IndexValue implements Serializable, Cloneable {

  private int index;
  private double value;

  public IndexValue() {}

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

  public boolean isValid() {
    return index != -1;
  }

  public IndexValue clone() {
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

    if (!(o instanceof IndexValue)) return false;

    IndexValue that = (IndexValue) o;

    return new EqualsBuilder()
        .append(getIndex(), that.getIndex())
        .append(getValue(), that.getValue())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(getIndex()).append(getValue()).toHashCode();
  }
}
