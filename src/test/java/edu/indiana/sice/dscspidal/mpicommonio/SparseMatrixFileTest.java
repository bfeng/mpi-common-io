package edu.indiana.sice.dscspidal.mpicommonio;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SparseMatrixFileTest {

    private static SparseMatrixFile sparseMatrixFile;
    private static long numRows;
    private static long numCols;

    private static long randomIdx(long range) {
        return Math.round(Math.floor(Math.random() * range));
    }

    @BeforeEach
    void setUp() {
        try {
            long range = 1_000_000L;
            numRows = randomIdx(range);
            numCols = randomIdx(range);
            sparseMatrixFile = new SparseMatrixFile(
                    File.createTempFile("sparse-matrix-", ".test").getAbsolutePath(),
                    numRows,
                    numCols
            );
            sparseMatrixFile.open();
        } catch (IOException e) {
            fail(e);
        }
    }

    @AfterEach
    void tearDown() {
        try {
            sparseMatrixFile.close();
            sparseMatrixFile.delete();
        } catch (IOException e) {
            fail(e);
        }
    }


    @Test
    void testNumCols() {
        long result = sparseMatrixFile.numCols();
        assertEquals(numCols, result);
    }

    @Test
    void testNumRows() {
        long results = sparseMatrixFile.numRows();
        assertEquals(numRows, results);
    }

    @Test
    void testSetGet() {
        final long rowIdx = randomIdx(numRows);
        final long colIdx = randomIdx(numCols);
        final double expected = Math.random();
        sparseMatrixFile.set(rowIdx, colIdx, expected);
        double result = sparseMatrixFile.get(rowIdx, colIdx);
        assertEquals(expected, result);
    }
}