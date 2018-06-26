package edu.indiana.sice.dscspidal.mpicommonio;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MPIFileTest {

    private MPIFile file;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        file = new MPIFile("/tmp/mpi-file.test");
    }


    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        try {
            boolean result;
            result = file.delete();
            assertTrue(result);
        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    void testOpen() {
        try {
            int result = file.open();
            assertEquals(0, result);
        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    void testReadWriteDouble() {
        double expected = Math.random();
        try {
            file.open();
            file.writeDouble(expected);
            double result = file.readDoubleAt(0);
            assertEquals(expected, result);
            file.close();
        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    void testReadWriteMoreDouble() {
        try {
            file.open();

            double[] expected = new double[10];
            for (int i = 0; i < expected.length; i++) {
                expected[i] = Math.random();
                file.writeDouble(expected[i]);
            }

            for (int i = 0; i < expected.length; i++) {
                double result = file.readDoubleAt(i);
                assertEquals(expected[i], result);
            }

            file.close();
        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    void testReadWriteInLargeFile() {
        try {
            file.open();

            long start = 15_000_000L;
            file.seekTo(start);

            double[] expected = new double[1000];
            for (int i = 0; i < expected.length; i++) {
                expected[i] = Math.random();
                file.writeDouble(expected[i]);
            }

            for (int i = 0; i < expected.length; i++) {
                double result = file.readDoubleAt(start + i);
                assertEquals(expected[i], result);
            }

            file.close();
        } catch (IOException e) {
            fail(e);
        }
    }
}