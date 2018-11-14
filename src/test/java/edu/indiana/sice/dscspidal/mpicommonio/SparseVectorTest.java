package edu.indiana.sice.dscspidal.mpicommonio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SparseVectorTest {

    @Test
    void testPutGet() {
        final int _100M = 100_000_000;
        final int i = Math.toIntExact(Math.round(Math.floor(Math.random() * _100M)));
        double v = Math.random();
        SparseVector sv = new SparseVector(_100M);
        sv.put(i, v);
        double result = sv.get(i);
        assertEquals(v, result);
    }

    @Test
    void nnz() {
        final int _100M = 100_000_000;
        SparseVector sv = new SparseVector(_100M);
        assertEquals(0, sv.nnz());
    }

    @Test
    void size() {
        final int _100M = 100_000_000;
        SparseVector sv = new SparseVector(_100M);
        assertEquals(_100M, sv.size());
    }

    @Test
    void testValues() {
        final int _100M = 100_000_000;
        final int i = Math.toIntExact(Math.round(Math.floor(Math.random() * _100M)));
        double v = Math.random();
        SparseVector sv = new SparseVector(_100M);
        sv.put(i, v);

        assertArrayEquals(new double[]{v}, sv.values());
    }

    @Test
    void testFromBytes() {
        final int _100M = 100_000_000;
        final int i = Math.toIntExact(Math.round(Math.floor(Math.random() * _100M)));
        double v = Math.random();
        SparseVector sv = new SparseVector(_100M);
        sv.put(i, v);

        byte[] raw = sv.toBytes();

        SparseVector v2 = SparseVector.fromBytes(_100M, raw);

        assertTrue(sv.contentEqual(v2));
    }
}