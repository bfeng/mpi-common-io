package edu.indiana.sice.dscspidal.mpicommonio;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.stream.Stream;

public class SparseVector implements SmallVector<Double>, Sparse {

    private final int n;

    final HashMap<Integer, Double> map;

    public SparseVector(int n) {
        this.n = n;
        this.map = new HashMap<>();
    }

    @Override
    public void put(int i, Double value) {
        if (i < 0 || i >= n) throw new RuntimeException("Illegal index");
        if (value == 0.0) map.remove(i);
        else map.put(i, value);
    }

    @Override
    public Double get(int i) {
        if (i < 0 || i >= n) throw new RuntimeException("Illegal index");
        return map.getOrDefault(i, 0.0);
    }

    @Override
    public int nnz() {
        return map.size();
    }

    @Override
    public int size() {
        return n;
    }

    @Override
    public double sparsity() {
        return 1.0 - (double) nnz() / size();
    }

    @Override
    public double maxPositive() {
        double result = 0;
        for (Double v : map.values()) {
            if (v > result)
                result = v;
        }
        return result;
    }

    @Override
    public double minPositive() {
        double result = Double.MAX_VALUE;
        for (Double v : map.values()) {
            if (v > 0 && v < result) {
                result = v;
            }
        }
        return result;
    }

    @Override
    public Double dot(SmallVector<Double> that) {
        return null;
    }

    @Override
    public Double sum() {
        Double sum = 0.0;
        for (Double v : map.values()) {
            sum += v;
        }
        return sum;
    }

    @Override
    public boolean contentEqual(SmallVector<Double> that) {
        if (this.size() != that.size())
            return false;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).compareTo(that.get(i)) != 0)
                return false;
        }
        return true;
    }

    @Override
    public void inPlaceTransform(Transform transform) {
        map.replaceAll((k, v) -> transform.apply(v));
    }

    @Override
    public double[] values() {
        Double[] tmp = map.values().toArray(new Double[nnz()]);
        return Stream.of(tmp).mapToDouble(Double::doubleValue).toArray();
    }

    @Override
    public byte[] toBytes() {
        byte[] raw = new byte[this.nnz() * (Integer.BYTES + Double.BYTES)];

        ByteBuffer buf = ByteBuffer.wrap(raw);

        for (Integer key : map.keySet()) {
            buf.putInt(key);
            buf.putDouble(map.get(key));
        }

        return raw;
    }

    public static SparseVector fromBytes(final int length, byte[] bytes) {
        SparseVector sparseVector = new SparseVector(length);

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int i = 0;
        while (i < bytes.length) {
            int key = buffer.getInt(i);
            i += Integer.BYTES;
            double val = buffer.getDouble(i);
            i += Double.BYTES;

            sparseVector.put(key, val);
        }

        return sparseVector;
    }
}
