package edu.indiana.sice.dscspidal.mpicommonio.examples;

import edu.indiana.sice.dscspidal.mpicommonio.SparseMatrixFile;

import java.io.IOException;

import static edu.indiana.sice.dscspidal.mpicommonio.examples.ExampleUtils.debugPrint;
import static edu.indiana.sice.dscspidal.mpicommonio.examples.ExampleUtils.randomIdx;

public class ReadWriteSparseMatrixExample {


    public static void main(String[] args) throws IOException {
        final long rowLength, colLength;
        rowLength = colLength = 10;
        System.out.println("Sparse matrix writing...");
        SparseMatrixFile sparseMatrixFile = new SparseMatrixFile("/tmp/sparse-matrix.bin", rowLength, colLength);
        sparseMatrixFile.open();
        sparseMatrixFile.set(randomIdx(rowLength), randomIdx(colLength), Math.random());
        sparseMatrixFile.set(randomIdx(rowLength), randomIdx(colLength), Math.random());
        sparseMatrixFile.set(randomIdx(rowLength), randomIdx(colLength), Math.random());
        sparseMatrixFile.set(randomIdx(rowLength), randomIdx(colLength), Math.random());
        sparseMatrixFile.set(randomIdx(rowLength), randomIdx(colLength), Math.random());
        debugPrint(sparseMatrixFile);
        sparseMatrixFile.close();

        System.out.println("Sparse matrix reading...");
        sparseMatrixFile = new SparseMatrixFile("/tmp/sparse-matrix.bin", rowLength, rowLength);
        sparseMatrixFile.open();
        debugPrint(sparseMatrixFile);
        sparseMatrixFile.close();
    }
}
