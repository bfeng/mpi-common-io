package edu.indiana.sice.dscspidal.mpicommonio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SparseMatrixTest {

    @Test
    void testMatrix() {
        final int dim = 16_000_000;
        final int rows = dim / 100;
        SparseMatrix sm = new SparseMatrix(rows, dim);
        for (int n = 0; n < dim; n++) {
            int i = Math.toIntExact(Math.round(Math.floor(Math.random() * rows)));
            int j = Math.toIntExact(Math.round(Math.floor(Math.random() * dim)));
            double v = Math.random();
            sm.set(i, j, v);
            double result = sm.get(i, j);
            assertEquals(v, result);
        }
    }

    @Test
    void testSquareMatrix() {
        final int dim = 16_000_000;
        SparseMatrix sm = new SparseMatrix(dim, dim);
        for (int n = 0; n < dim; n++) {
            int i = Math.toIntExact(Math.round(Math.floor(Math.random() * dim)));
            int j = Math.toIntExact(Math.round(Math.floor(Math.random() * dim)));
            double v = Math.random();
            sm.set(i, j, v);
            double result = sm.get(i, j);
            assertEquals(v, result);
        }
    }
}