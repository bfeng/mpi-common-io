#!/usr/bin/env bash

mvn package
mpirun -n 4 java -cp target/mpi-common-io-1.0-SNAPSHOT-uber.jar \
    edu.indiana.sice.dscspidal.mpicommonio.examples.ParallelReadWriteExample /tmp/matrix-example.data

java -cp target/mpi-common-io-1.0-SNAPSHOT-uber.jar \
    edu.indiana.sice.dscspidal.mpicommonio.examples.ReadWriteSparseMatrixExample