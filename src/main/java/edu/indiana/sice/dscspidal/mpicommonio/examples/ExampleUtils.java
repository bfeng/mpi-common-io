package edu.indiana.sice.dscspidal.mpicommonio.examples;

import edu.indiana.sice.dscspidal.mpicommonio.Matrix;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

class ExampleUtils {
    static long randomIdx(long range) {
        return Math.round(Math.floor(Math.random() * range));
    }

    static void debugPrint(@NotNull Matrix<Double> doubleMatrix) throws IOException {
        long rowLength = doubleMatrix.numRows();
        long colLength = doubleMatrix.numCols();
        for (long i = 0; i < rowLength; i++) {
            for (long j = 0; j < colLength; j++) {
                System.out.printf("\t%f", doubleMatrix.get(i, j));
            }
            System.out.println();
        }
    }
}
