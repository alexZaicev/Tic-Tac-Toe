package com.alexz.tictactoe.models;

import com.alexz.tictactoe.services.DoubleUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Matrix implements Serializable {

  private final Map<Integer, Vector> rows = new HashMap<>();
  private int rowCount;
  private int columnCount;
  private double defaultValue;

  public Matrix() {}

  public Matrix(double[][] A) {
    for (int i = 0; i < A.length; ++i) {
      double[] B = A[i];
      for (int j = 0; j < B.length; ++j) {
        set(i, j, B[j]);
      }
    }
  }

  public Matrix(int rowCount, int columnCount) {
    this.rowCount = rowCount;
    this.columnCount = columnCount;
    this.defaultValue = 0;
  }

  public static Matrix identity(int dimension) {
    Matrix m = new Matrix(dimension, dimension);
    for (int i = 0; i < m.getRowCount(); ++i) {
      m.set(i, i, 1);
    }
    return m;
  }

  public Map<Integer, Vector> getRows() {
    return rows;
  }

  public int getRowCount() {
    return rowCount;
  }

  public void setRowCount(int rowCount) {
    this.rowCount = rowCount;
  }

  public int getColumnCount() {
    return columnCount;
  }

  public void setColumnCount(int columnCount) {
    this.columnCount = columnCount;
  }

  public double getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(double defaultValue) {
    this.defaultValue = defaultValue;
  }

  public void setRow(int rowIndex, Vector rowVector) {
    rowVector.setId(rowIndex);
    rows.put(rowIndex, rowVector);
  }

  public void set(int rowIndex, int columnIndex, double value) {
    Vector row = rowAt(rowIndex);
    row.set(columnIndex, value);
    if (rowIndex >= rowCount) {
      rowCount = rowIndex + 1;
    }
    if (columnIndex >= columnCount) {
      columnCount = columnIndex + 1;
    }
  }

  public Vector rowAt(int rowIndex) {
    Vector row = rows.get(rowIndex);
    if (row == null) {
      row = new Vector(columnCount);
      row.setAll(defaultValue);
      row.setId(rowIndex);
      rows.put(rowIndex, row);
    }
    return row;
  }

  public void setAll(double value) {
    defaultValue = value;
    for (Vector row : rows.values()) {
      row.setAll(value);
    }
  }

  public double get(int rowIndex, int columnIndex) {
    Vector row = rowAt(rowIndex);
    return row.get(columnIndex);
  }

  public List<Vector> columnVectors() {
    Matrix A = this;
    int n = A.getColumnCount();
    int rowCount = A.getRowCount();

    List<Vector> Acols = new ArrayList<Vector>();

    for (int c = 0; c < n; ++c) {
      Vector Acol = new Vector(rowCount);
      Acol.setAll(defaultValue);
      Acol.setId(c);

      for (int r = 0; r < rowCount; ++r) {
        Acol.set(r, A.get(r, c));
      }
      Acols.add(Acol);
    }
    return Acols;
  }

  public Matrix multiply(Matrix rhs) {
    if (this.getColumnCount() != rhs.getRowCount()) {
      System.err.println("A.columnCount must be equal to B.rowCount in multiplication");
      return null;
    }

    Vector row1;
    Vector col2;

    Matrix result = new Matrix(getRowCount(), rhs.getColumnCount());
    result.setAll(defaultValue);

    List<Vector> rhsColumns = rhs.columnVectors();

    for (Map.Entry<Integer, Vector> entry : rows.entrySet()) {
      int r1 = entry.getKey();
      row1 = entry.getValue();
      for (int c2 = 0; c2 < rhsColumns.size(); ++c2) {
        col2 = rhsColumns.get(c2);
        result.set(r1, c2, row1.multiply(col2));
      }
    }

    return result;
  }

  public boolean isSymmetric() {
    if (getRowCount() != getColumnCount()) return false;

    for (Map.Entry<Integer, Vector> rowEntry : rows.entrySet()) {
      int row = rowEntry.getKey();
      Vector rowVec = rowEntry.getValue();

      for (Integer col : rowVec.getData().keySet()) {
        if (row == col.intValue()) continue;
        if (DoubleUtils.equals(rowVec.get(col), this.get(col, row))) {
          return false;
        }
      }
    }

    return true;
  }

  public Vector multiply(Vector rhs) {
    if (this.getColumnCount() != rhs.getDimension()) {
      System.err.println("columnCount must be equal to the size of the vector for multiplication");
    }

    Vector row1;
    Vector result = new Vector(getRowCount());
    for (Map.Entry<Integer, Vector> entry : rows.entrySet()) {
      row1 = entry.getValue();
      result.set(entry.getKey(), row1.multiply(rhs));
    }
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (!(o instanceof Matrix)) return false;

    Matrix matrix = (Matrix) o;

    return new EqualsBuilder()
        .append(getRowCount(), matrix.getRowCount())
        .append(getColumnCount(), matrix.getColumnCount())
        .append(getDefaultValue(), matrix.getDefaultValue())
        .append(getRows(), matrix.getRows())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getRows())
        .append(getRowCount())
        .append(getColumnCount())
        .append(getDefaultValue())
        .toHashCode();
  }
}
