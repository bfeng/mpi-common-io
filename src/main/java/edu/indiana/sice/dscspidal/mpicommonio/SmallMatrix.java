package edu.indiana.sice.dscspidal.mpicommonio;

import java.io.Serializable;

interface SmallMatrix<T> extends Serializable {

    int numCols();

    int numRows();

    T get(int rowIdx, int colIdx);

    void set(int rowIdx, int colIdx, T value);

    int size();

    T rowSum(int i);

    T colSum(int j);
}
