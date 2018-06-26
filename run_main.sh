#!/usr/bin/env bash

mpirun -n 2 java -cp target/mpi-common-io-1.0-SNAPSHOT-uber.jar \
    edu.indiana.sice.dscspidal.mpicommonio.examples.ParallelReadWriteExample /tmp/matrix-example.data