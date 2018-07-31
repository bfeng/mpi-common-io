package edu.indiana.sice.dscspidal.mpicommonio.examples;

import edu.indiana.sice.dscspidal.mpicommonio.MatrixFile;
import mpi.*;

import java.io.IOException;
import java.util.Arrays;

import static edu.indiana.sice.dscspidal.mpicommonio.examples.ExampleUtils.randomIdx;

public class ParallelReadWriteExample {

    private static Win window;

    private static final Object lock = new Object();

    private static void println(String line) throws MPIException {
        window.lockAll(MPI.LOCK_EXCLUSIVE);
        synchronized (lock) {
            int myRank = MPI.COMM_WORLD.getRank();
            int size = MPI.COMM_WORLD.getSize();
            System.out.printf("Rank[%d/%d]:%s\n", myRank, size, line);
            System.out.flush();
        }
        window.unlockAll();
    }

    public static void main(String[] args) throws MPIException, IOException {
        MPI.Init(args);

        window = new Win(MPI.INFO_NULL, MPI.COMM_WORLD);
        window.fence(MPI.MODE_NOPRECEDE);

        println("Init");
        println(Arrays.toString(args));

        final long rowLength = 40;
        final long colLength = 40;

        MatrixFile matrixFile = new MatrixFile(args[0], rowLength, colLength);

        matrixFile.open();

        long rowIdx, colIdx;
        double value, result;
        for (int i = 0; i < rowLength; i++) {
            rowIdx = randomIdx(rowLength);
            colIdx = randomIdx(colLength);
            value = Math.random();
            matrixFile.writeTo(rowIdx, colIdx, value);
            result = matrixFile.readFrom(rowIdx, colIdx);
            assert Double.compare(value, result) == 0;
        }

        matrixFile.close();

        println("Finalize");

        window.free();
        MPI.COMM_WORLD.barrier();
        MPI.Finalize();
    }
}
