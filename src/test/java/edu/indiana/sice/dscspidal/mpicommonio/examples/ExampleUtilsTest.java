package edu.indiana.sice.dscspidal.mpicommonio.examples;

import edu.indiana.sice.dscspidal.mpicommonio.SparseVector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExampleUtilsTest {

    @Test
    void initSparseVector() {
        double sparsity = 0.8;
        SparseVector sparseVector = ExampleUtils.initSparseVector(10000, sparsity);
        assertEquals(sparsity, sparseVector.sparsity(), 0.01);
    }
}