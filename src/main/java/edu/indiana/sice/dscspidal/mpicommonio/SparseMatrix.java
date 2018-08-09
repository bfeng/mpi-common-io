package edu.indiana.sice.dscspidal.mpicommonio;

public class SparseMatrix implements SmallMatrix<Double>, Sparse {
    private final int numRows;
    private final int numCols;

    private final SparseVector[] rows;

    public SparseMatrix(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.rows = new SparseVector[numRows];
        for (int i = 0; i < numRows; i++) {
            rows[i] = new SparseVector(numCols);
        }
    }

    @Override
    public int numCols() {
        return numCols;
    }

    @Override
    public int numRows() {
        return numRows;
    }

    @Override
    public Double get(int i, int j) {
        if (i < 0 || i >= numRows) throw new RuntimeException("Illegal index");
        if (j < 0 || j >= numCols) throw new RuntimeException("Illegal index");
        return rows[i].get(j);
    }

    @Override
    public void set(int i, int j, Double value) {
        if (i < 0 || i >= numRows) throw new RuntimeException("Illegal index");
        if (j < 0 || j >= numCols) throw new RuntimeException("Illegal index");
        rows[i].put(j, value);
    }

    @Override
    public int nnz() {
        int n = 0;
        for (SparseVector row : rows) {
            n += row.nnz();
        }
        return n;
    }

    @Override
    public int size() {
        return numRows * numCols;
    }

    @Override
    public double sparsity() {
        return (double) nnz() / size();
    }
}
