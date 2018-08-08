package edu.indiana.sice.dscspidal.mpicommonio;

/**
 * N square sparse matrix in memory
 */
public class SparseDistanceMatrix implements SmallMatrix<Double> {
    private final int n;

    private final SparseVector[] rows;

    public SparseDistanceMatrix(int num) {
        this.n = num;
        this.rows = new SparseVector[n];
        for (int i = 0; i < n; i++) {
            rows[i] = new SparseVector(n);
        }
    }

    @Override
    public int numCols() {
        return n;
    }

    @Override
    public int numRows() {
        return n;
    }

    @Override
    public Double get(int i, int j) {
        if (i < 0 || i >= n) throw new RuntimeException("Illegal index");
        if (j < 0 || j >= n) throw new RuntimeException("Illegal index");
        return rows[i].get(j);
    }

    @Override
    public void set(int i, int j, Double value) {
        if (i < 0 || i >= n) throw new RuntimeException("Illegal index");
        if (j < 0 || j >= n) throw new RuntimeException("Illegal index");
        rows[i].put(j, value);
    }
}
