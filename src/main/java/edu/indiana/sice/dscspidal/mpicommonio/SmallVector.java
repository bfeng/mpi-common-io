package edu.indiana.sice.dscspidal.mpicommonio;

import java.io.Serializable;

interface SmallVector<T> extends Serializable {

    void put(int i, T value);

    T get(int i);

    int size();

    T dot(SmallVector<T> that);

    T sum();

    boolean contentEqual(SmallVector<T> that);
}
