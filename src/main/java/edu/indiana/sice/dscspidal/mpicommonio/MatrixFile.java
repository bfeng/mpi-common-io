package edu.indiana.sice.dscspidal.mpicommonio;

import java.io.IOException;

public class MatrixFile extends MPIFile {

    private final long rowLength;
    private final long colLength;

    public MatrixFile(String pathname, long rowLength, long colLength) throws IOException {
        super(pathname);
        this.rowLength = rowLength;
        this.colLength = colLength;
        this.open();
        this.seekTo(this.rowLength * this.colLength);
        this.seekTo(0);
        this.close();
    }

    public void seekTo(long rowIdx, long colIdx) throws IOException {
        long pos = calculatePosition(rowIdx, colIdx);
        super.seekTo(pos);
    }

    public double readFrom(long rowIdx, long colIdx) throws IOException {
        long pos = calculatePosition(rowIdx, colIdx);
        return this.readDoubleAt(pos);
    }

    public void writeTo(long rowIdx, long colIdx, double value) throws IOException {
        long pos = calculatePosition(rowIdx, colIdx);
        this.writeDoubleAt(pos, value);
    }

    public long calculatePosition(long rowIdx, long colIdx) {
        return rowIdx * colLength + colIdx;
    }
}
