package edu.indiana.sice.dscspidal.mpicommonio;

import java.io.Serializable;

interface SmallMatrix<T> extends Serializable {

    int numCols();

    int numRows();

    T get(int rowIdx, int colIdx);

    SmallVector<T> getRow(int rowIdx);

    void set(int rowIdx, int colIdx, T value);

    long size();

    T rowSum(int i);

    T colSum(int j);

    boolean contentEqual(SmallMatrix<T> that);
}
