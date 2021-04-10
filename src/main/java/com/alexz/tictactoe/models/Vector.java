package com.alexz.tictactoe.models;

import com.alexz.tictactoe.services.DoubleUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Vector implements Serializable, Cloneable {

  private final Map<Integer, Double> data = new HashMap<Integer, Double>();
  private int dimension;
  private double defaultValue;
  private int id = -1;

  public Vector() {}

  public Vector(double[] v) {
    for (int i = 0; i < v.length; ++i) {
      set(i, v[i]);
    }
  }

  public Vector(int dimension) {
    this.dimension = dimension;
    defaultValue = 0;
  }

  public Vector(int dimension, Map<Integer, Double> data) {
    this.dimension = dimension;
    defaultValue = 0;

    for (Map.Entry<Integer, Double> entry : data.entrySet()) {
      set(entry.getKey(), entry.getValue());
    }
  }

  public Map<Integer, Double> getData() {
    return data;
  }

  public int getDimension() {
    return dimension;
  }

  public void setDimension(int dimension) {
    this.dimension = dimension;
  }

  public double getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(double defaultValue) {
    this.defaultValue = defaultValue;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Vector clone() {
    try {
      super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return SerializationUtils.clone(this);
  }

  public void set(int i, double value) {
    if (value == defaultValue) return;

    data.put(i, value);
    if (i >= dimension) {
      dimension = i + 1;
    }
  }

  public double get(int i) {
    return data.getOrDefault(i, defaultValue);
  }

  public void setAll(double value) {
    defaultValue = value;
    data.replaceAll((i, v) -> defaultValue);
  }

  public IndexValue indexWithMaxValue(Set<Integer> indices) {
    if (indices == null) {
      return indexWithMaxValue();
    } else {
      IndexValue iv = new IndexValue();
      iv.setIndex(-1);
      iv.setValue(Double.NEGATIVE_INFINITY);
      for (Integer index : indices) {
        double value = data.getOrDefault(index, Double.NEGATIVE_INFINITY);
        if (value > iv.getValue()) {
          iv.setIndex(index);
          iv.setValue(value);
        }
      }
      return iv;
    }
  }

  public IndexValue indexWithMaxValue() {
    IndexValue iv = new IndexValue();
    iv.setIndex(-1);
    iv.setValue(Double.NEGATIVE_INFINITY);

    for (Map.Entry<Integer, Double> entry : data.entrySet()) {
      if (entry.getKey() >= dimension) continue;

      double value = entry.getValue();
      if (value > iv.getValue()) {
        iv.setValue(value);
        iv.setIndex(entry.getKey());
      }
    }

    if (!iv.isValid()) {
      iv.setValue(defaultValue);
    } else {
      if (iv.getValue() < defaultValue) {
        for (int i = 0; i < dimension; ++i) {
          if (!data.containsKey(i)) {
            iv.setValue(defaultValue);
            iv.setIndex(i);
            break;
          }
        }
      }
    }

    return iv;
  }

  public Vector projectOrthogonal(Iterable<Vector> vlist) {
    Vector b = this;
    for (Vector v : vlist) {
      b = b.minus(b.projectAlong(v));
    }

    return b;
  }

  public Vector projectOrthogonal(List<Vector> vlist, Map<Integer, Double> alpha) {
    Vector b = this;
    for (int i = 0; i < vlist.size(); ++i) {
      Vector v = vlist.get(i);
      double norm_a = v.multiply(v);

      if (DoubleUtils.isZero(norm_a)) {
        return new Vector(dimension);
      }
      double sigma = multiply(v) / norm_a;
      Vector v_parallel = v.multiply(sigma);

      alpha.put(i, sigma);

      b = b.minus(v_parallel);
    }

    return b;
  }

  public Vector projectAlong(Vector rhs) {
    double norm_a = rhs.multiply(rhs);

    if (DoubleUtils.isZero(norm_a)) {
      return new Vector(dimension);
    }
    double sigma = multiply(rhs) / norm_a;
    return rhs.multiply(sigma);
  }

  public Vector multiply(double rhs) {
    Vector clone = this.clone();
    for (Integer i : data.keySet()) {
      clone.data.put(i, rhs * data.get(i));
    }
    return clone;
  }

  public double multiply(Vector rhs) {
    double productSum = 0;
    if (defaultValue == 0) {
      for (Map.Entry<Integer, Double> entry : data.entrySet()) {
        productSum += entry.getValue() * rhs.get(entry.getKey());
      }
    } else {
      for (int i = 0; i < dimension; ++i) {
        productSum += get(i) * rhs.get(i);
      }
    }

    return productSum;
  }

  public Vector pow(double scalar) {
    Vector result = new Vector(dimension);
    for (Map.Entry<Integer, Double> entry : data.entrySet()) {
      result.data.put(entry.getKey(), Math.pow(entry.getValue(), scalar));
    }
    return result;
  }

  public Vector add(Vector rhs) {
    Vector result = new Vector(dimension);
    int index;
    for (Map.Entry<Integer, Double> entry : data.entrySet()) {
      index = entry.getKey();
      result.data.put(index, entry.getValue() + rhs.data.get(index));
    }
    for (Map.Entry<Integer, Double> entry : rhs.data.entrySet()) {
      index = entry.getKey();
      if (result.data.containsKey(index)) continue;
      result.data.put(index, entry.getValue() + data.get(index));
    }

    return result;
  }

  public Vector minus(Vector rhs) {
    Vector result = new Vector(dimension);
    int index;
    for (Map.Entry<Integer, Double> entry : data.entrySet()) {
      index = entry.getKey();
      result.data.put(index, entry.getValue() - rhs.data.get(index));
    }
    for (Map.Entry<Integer, Double> entry : rhs.data.entrySet()) {
      index = entry.getKey();
      if (result.data.containsKey(index)) continue;
      result.data.put(index, data.get(index) - entry.getValue());
    }

    return result;
  }

  public double sum() {
    double sum = 0;

    for (Map.Entry<Integer, Double> entry : data.entrySet()) {
      sum += entry.getValue();
    }
    sum += defaultValue * (dimension - data.size());

    return sum;
  }

  public boolean isZero() {
    return DoubleUtils.isZero(sum());
  }

  public double norm(int level) {
    if (level == 1) {
      double sum = 0;
      for (Double val : data.values()) {
        sum += Math.abs(val);
      }
      if (!DoubleUtils.isZero(defaultValue)) {
        sum += Math.abs(defaultValue) * (dimension - data.size());
      }
      return sum;
    } else if (level == 2) {
      double sum = multiply(this);
      if (!DoubleUtils.isZero(defaultValue)) {
        sum += (dimension - data.size()) * (defaultValue * defaultValue);
      }
      return Math.sqrt(sum);
    } else {
      double sum = 0;
      for (Double val : this.data.values()) {
        sum += Math.pow(Math.abs(val), level);
      }
      if (!DoubleUtils.isZero(defaultValue)) {
        sum += Math.pow(Math.abs(defaultValue), level) * (dimension - data.size());
      }
      return Math.pow(sum, 1.0 / level);
    }
  }

  public Vector normalize() {
    double norm = norm(2); // L2 norm is the cartesian distance
    if (DoubleUtils.isZero(norm)) {
      return new Vector(dimension);
    }
    Vector clone = new Vector(dimension);
    clone.setAll(defaultValue / norm);

    for (Integer k : data.keySet()) {
      clone.data.put(k, data.get(k) / norm);
    }
    return clone;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (!(o instanceof Vector)) return false;

    Vector vector = (Vector) o;

    return new EqualsBuilder()
        .append(getDimension(), vector.getDimension())
        .append(getDefaultValue(), vector.getDefaultValue())
        .append(getId(), vector.getId())
        .append(getData(), vector.getData())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getData())
        .append(getDimension())
        .append(getDefaultValue())
        .append(getId())
        .toHashCode();
  }
}
