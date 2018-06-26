package edu.indiana.sice.dscspidal.mpicommonio.examples;

import edu.indiana.sice.dscspidal.mpicommonio.MatrixFile;
import mpi.*;

import java.io.IOException;
import java.util.Arrays;

public class ParallelReadWriteExample {

    private static final Object lock = new Object();

    private static long randomIdx(long range) {
        return Math.round(Math.floor(Math.random() * range));
    }

    private static void println(String line) throws MPIException {
        synchronized (lock) {
            int myRank = MPI.COMM_WORLD.getRank();
            int size = MPI.COMM_WORLD.getSize();
            System.out.printf("Rank[%d/%d]:%s\n", myRank, size, line);
        }
    }

    public static void main(String[] args) throws MPIException, IOException {
        MPI.Init(args);

        println("Init");
        println(Arrays.toString(args));

        final long rowLength = 4;
        final long colLength = 4;

        MatrixFile matrixFile = new MatrixFile(args[0], rowLength, colLength);

        matrixFile.open();

        final long rowIdx = randomIdx(rowLength);
        final long colIdx = randomIdx(colLength);
        final double value = Math.random();
        matrixFile.writeTo(rowIdx, colIdx, value);

        matrixFile.close();

        println("Finalize");
        MPI.Finalize();
    }
}
