package edu.indiana.sice.dscspidal.mpicommonio.examples;

import edu.indiana.sice.dscspidal.mpicommonio.SparseVector;
import mpi.MPI;
import mpi.MPIException;

public class SparseVectorBroadcastExample {

    private static int rank;

    private static void testDenseBroadcast(double[] vector) throws MPIException {
        MPI.COMM_WORLD.bcast(vector, vector.length, MPI.DOUBLE, 0);
    }

    private static void testSparseBroadcast(SparseVector vector) throws MPIException {
        int[] totalLen = new int[2];
        byte[] bytes = null;
        if (rank == 0) {
            bytes = vector.toBytes();
            totalLen[0] = bytes.length;
            totalLen[1] = vector.size();
        }
        MPI.COMM_WORLD.bcast(totalLen, 2, MPI.INT, 0);
        if (rank != 0) {
            bytes = new byte[totalLen[0]];
        }
        MPI.COMM_WORLD.bcast(bytes, totalLen[0], MPI.BYTE, 0);
        if (rank != 0) {
            vector = SparseVector.fromBytes(totalLen[1], bytes);
        }
    }

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        final int LEN = Integer.parseInt(args[0]);
        final int N_TIMES = Integer.parseInt(args[1]);
        final double sparsity = Double.parseDouble(args[2]);

        rank = MPI.COMM_WORLD.getRank();

        double[] dense = new double[LEN];
        SparseVector sparse = new SparseVector(LEN);
        if (rank == 0) {
            dense = ExampleUtils.initDenseVector(LEN);
            sparse = ExampleUtils.initSparseVector(LEN, sparsity);
        }

        long start = System.currentTimeMillis();
        for (int i = 0; i < N_TIMES; i++) {
            testDenseBroadcast(dense);
            MPI.COMM_WORLD.barrier();
        }
        long end = System.currentTimeMillis();
        long denseTime = (end - start);

        start = System.currentTimeMillis();
        for (int i = 0; i < N_TIMES; i++) {
            testSparseBroadcast(sparse);
            MPI.COMM_WORLD.barrier();
        }
        end = System.currentTimeMillis();
        long sparseTime = (end - start);

        if (rank == 0) System.out.printf("%f\t%f\n", (double) denseTime / N_TIMES, (double) sparseTime / N_TIMES);

        MPI.Finalize();
    }
}
