package edu.indiana.sice.dscspidal.mpicommonio.examples;

import edu.indiana.sice.dscspidal.mpicommonio.SparseVector;
import mpi.MPI;
import mpi.MPIException;

public class SparseVectorAllGatherExample {

    private static void testDenseAllGather(double[] vector, int tasks) throws MPIException {
        double[] in = new double[vector.length * tasks];
        MPI.COMM_WORLD.allGather(vector, vector.length, MPI.DOUBLE, in, vector.length, MPI.DOUBLE);
    }

    private static void testSparseAllGather(SparseVector vector, int tasks) throws MPIException {
        byte[] out = vector.toBytes();
        int[] totalLen = {out.length};
        MPI.COMM_WORLD.allReduce(totalLen, 1, MPI.INT, MPI.SUM);
        System.out.println(totalLen[0]);
        byte[] in = new byte[totalLen[0]];

//        MPI.COMM_WORLD.allGather(out, out.length, MPI.BYTE, in, out.length, MPI.BYTE);
    }

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        final int LEN = Integer.parseInt(args[0]);
        final int N_TIMES = Integer.parseInt(args[1]);
        final double sparsity = Double.parseDouble(args[2]);

        int rank = MPI.COMM_WORLD.getRank();

        double[] dense = ExampleUtils.initDenseVector(LEN);
        SparseVector sparse = ExampleUtils.initSparseVector(LEN, sparsity);
        int tasks = MPI.COMM_WORLD.getSize();

        long start = System.currentTimeMillis();
        for (int i = 0; i < N_TIMES; i++) {
            testDenseAllGather(dense, tasks);
            MPI.COMM_WORLD.barrier();
        }
        long end = System.currentTimeMillis();
//        if (rank == 0) System.out.println(end - start);

        start = System.currentTimeMillis();
        for (int i = 0; i < N_TIMES; i++) {
            testSparseAllGather(sparse, tasks);
            MPI.COMM_WORLD.barrier();
        }
        end = System.currentTimeMillis();
        if (rank == 0) System.out.println(end - start);

        MPI.Finalize();
    }
}
