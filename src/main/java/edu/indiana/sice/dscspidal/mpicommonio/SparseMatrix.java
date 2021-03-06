package edu.indiana.sice.dscspidal.mpicommonio;

public class SparseMatrix implements SmallMatrix<Double>, Sparse {
    private final int numRows;
    private final int numCols;

    final SparseVector[] rows;

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
    public SmallVector<Double> getRow(int i) {
        if (i < 0 || i >= numRows) throw new RuntimeException("Illegal index");
        return rows[i];
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
    public long size() {
        return (long) numRows * (long) numCols;
    }

    @Override
    public double sparsity() {
        return 1.0 - (double) nnz() / size();
    }

    @Override
    public double maxPositive() {
        double result = 0;
        for (SparseVector row : rows) {
            double v = row.maxPositive();
            if (v > result) {
                result = v;
            }
        }
        return result;
    }

    @Override
    public double minPositive() {
        double result = Double.MAX_VALUE;
        for (SparseVector row : rows) {
            double v = row.minPositive();
            if (v > 0 && v < result) {
                result = v;
            }
        }
        return result;
    }

    @Override
    public void inPlaceTransform(Transform transform) {
        for (SparseVector row : rows) {
            row.inPlaceTransform(transform);
        }
    }

    @Override
    public Double rowSum(int i) {
        return rows[i].sum();
    }

    @Override
    public Double colSum(int j) {
        Double sum = 0.0;
        for (SparseVector row : rows) {
            sum += row.get(j);
        }
        return sum;
    }

    @Override
    public boolean contentEqual(SmallMatrix<Double> that) {
        if (this.size() != that.size())
            return false;

        for (int i = 0; i < this.rows.length; i++) {
            if (!this.rows[i].contentEqual(that.getRow(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public double[] values() {
        double[] result = new double[nnz()];
        int position = 0;
        for (SparseVector row : rows) {
            if (row.nnz() > 0) {
                System.arraycopy(row.values(), 0, result, position, row.nnz());
                position += row.nnz();
            }
        }
        return result;
    }

    @Override
    public byte[] toBytes() {
        return new byte[0];
    }
}