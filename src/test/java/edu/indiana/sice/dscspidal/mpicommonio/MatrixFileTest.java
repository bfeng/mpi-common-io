package edu.indiana.sice.dscspidal.mpicommonio;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MatrixFileTest {

    private static MatrixFile matrixFile;
    private static long rowLength;
    private static long colLength;

    private static long randomIdx(long range) {
        return Math.round(Math.floor(Math.random() * range));
    }

    @BeforeAll
    static void setUp() {
        try {
            long range = 1_000_000L;
            rowLength = randomIdx(range);
            colLength = randomIdx(range);
            matrixFile = new MatrixFile(File.createTempFile("matrix-", ".test").getAbsolutePath(), rowLength, colLength);
            matrixFile.open();
        } catch (IOException e) {
            fail(e);
        }
    }


    @AfterAll
    static void tearDown() {
        try {
            matrixFile.close();
            matrixFile.delete();
        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    void testReadWrite() {
        try {
            final int testCases = 10;
            long[][] randIds = new long[testCases][2];
            double[] values = new double[testCases];
            for (int i = 0; i < randIds.length; i++) {
                randIds[i][0] = randomIdx(rowLength);
                randIds[i][1] = randomIdx(colLength);
                values[i] = Math.random();
                matrixFile.writeTo(randIds[i][0], randIds[i][1], values[i]);
            }

            for (int i = 0; i < randIds.length; i++) {
                double result = matrixFile.readFrom(randIds[i][0], randIds[i][1]);
                assertEquals(values[i], result);
            }
        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    void testCalculatePosition() {
        long rowIdx = randomIdx(rowLength);
        long colIdx = randomIdx(colLength);

        try {
            matrixFile.seekTo(rowIdx, colIdx);
            assertEquals(rowIdx * colLength + colIdx, matrixFile.calculatePosition(rowIdx, colIdx));
        } catch (IOException e) {
            fail(e);
        }
    }
}