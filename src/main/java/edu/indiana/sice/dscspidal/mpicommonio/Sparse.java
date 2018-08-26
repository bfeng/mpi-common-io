package edu.indiana.sice.dscspidal.mpicommonio;

public interface Sparse {

    interface Transform {
        double apply(double v);
    }

    int nnz();

    double sparsity();

    double maxPositive();

    double minPositive();

    void inPlaceTransform(Transform transform);

    double[] values();
}
