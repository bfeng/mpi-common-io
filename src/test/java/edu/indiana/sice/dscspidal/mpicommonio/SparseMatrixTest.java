package edu.indiana.sice.dscspidal.mpicommonio;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void testValues() {
        final int dim = 16_000_000;
        SparseMatrix sm = new SparseMatrix(dim, dim);
        double v1 = Math.random();
        double v2 = Math.random();
        double v3 = Math.random();
        double v4 = Math.random();
        sm.set(0, Math.toIntExact(Math.round(Math.floor(Math.random() * dim))), v1);
        sm.set(1, Math.toIntExact(Math.round(Math.floor(Math.random() * dim))), v2);
        sm.set(2, Math.toIntExact(Math.round(Math.floor(Math.random() * dim))), v3);
        sm.set(3, Math.toIntExact(Math.round(Math.floor(Math.random() * dim))), v4);
        assertArrayEquals(new double[]{v1, v2, v3, v4}, sm.values());
    }

    @Test
    void testSparsity() {
        final int dim = 16_000_000;
        SparseMatrix sm = new SparseMatrix(dim, dim);
        sm.set(0, 0, 1.0);
        assertEquals((double) 1 / dim / dim, sm.sparsity());
    }

    @Test
    void testDumpAndLoad() throws IOException {
        int[] tests = {10, 10000, 12345, 23456};
        for (final int dim : tests) {
            SparseMatrix sm = new SparseMatrix(dim, dim);
            for (int i = 0; i < dim; i++) {
                sm.set(Math.toIntExact(Math.round(Math.floor(Math.random() * dim))), Math.toIntExact(Math.round(Math.floor(Math.random() * dim))), Math.random());
            }
            File matrixFile = File.createTempFile("sparse-matrix-", ".test");
            SparseMatrixFile.dumpToFile(sm, matrixFile);
            SparseMatrix result = SparseMatrixFile.loadIntoMemory(matrixFile, 0, dim - 1, dim);
            assertTrue(sm.contentEqual(result));
        }
    }
}