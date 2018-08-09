package edu.indiana.sice.dscspidal.mpicommonio;

import java.util.HashMap;

public class SparseVector implements SmallVector<Double>, Sparse {

    private final int n;

    private final HashMap<Integer, Double> map;

    public SparseVector(int n) {
        this.n = n;
        this.map = new HashMap<>();
    }

    @Override
    public void put(int i, Double value) {
        if (i < 0 || i >= n) throw new RuntimeException("Illegal index");
        if (value == 0.0) map.remove(i);
        else map.put(i, value);
    }

    @Override
    public Double get(int i) {
        if (i < 0 || i >= n) throw new RuntimeException("Illegal index");
        return map.getOrDefault(i, 0.0);
    }

    @Override
    public int nnz() {
        return map.size();
    }

    @Override
    public int size() {
        return n;
    }

    @Override
    public double sparsity() {
        return (double) nnz() / size();
    }

    @Override
    public Double dot(SmallVector<Double> that) {
        return null;
    }
}
