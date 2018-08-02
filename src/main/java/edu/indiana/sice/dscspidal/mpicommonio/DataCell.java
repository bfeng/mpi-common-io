package edu.indiana.sice.dscspidal.mpicommonio;

class DataCell {
    private long rowIdx;
    private long colIdx;
    private double value;

    DataCell(long rowIdx, long colIdx, double value) {
        this.rowIdx = rowIdx;
        this.colIdx = colIdx;
        this.value = value;
    }

    public long getRowIdx() {
        return rowIdx;
    }

    public long getColIdx() {
        return colIdx;
    }

    public double getValue() {
        return value;
    }
}
