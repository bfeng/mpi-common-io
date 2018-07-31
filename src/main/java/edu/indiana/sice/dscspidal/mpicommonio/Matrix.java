package edu.indiana.sice.dscspidal.mpicommonio;

import java.io.Serializable;

public interface Matrix<T> extends Serializable {

    long numCols();

    long numRows();

    T get(long rowIdx, long colIdx);

    void set(long rowIdx, long colIdx, T value);
}
