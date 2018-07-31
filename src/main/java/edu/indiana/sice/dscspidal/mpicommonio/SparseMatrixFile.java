package edu.indiana.sice.dscspidal.mpicommonio;

import java.io.*;

public class SparseMatrixFile implements Matrix<Double> {

    private static final String MODE = "rws";

    private String pathname;
    private long rowLength;
    private long colLength;

    private File indicesFile;
    private File dataFile;
    private RandomAccessFile indices;
    private RandomAccessFile data;

    private long indicesPointer;
    private long dataPointer;

    public SparseMatrixFile(String pathname, long rowLength, long colLength) {
        this.pathname = pathname;
        this.rowLength = rowLength;
        this.colLength = colLength;
    }

    public void open() throws IOException {
        try {
            indicesFile = new File(pathname + "--indices.bin");
            indices = new RandomAccessFile(indicesFile, MODE);
        } catch (FileNotFoundException e) {
            if (indicesFile.createNewFile()) {
                indices = new RandomAccessFile(indicesFile, MODE);
            } else
                throw e;
        }
        try {
            dataFile = new File(pathname + "--data.bin");
            data = new RandomAccessFile(dataFile, MODE);
        } catch (FileNotFoundException e) {
            if (dataFile.createNewFile()) {
                data = new RandomAccessFile(dataFile, MODE);
            } else
                throw e;
        }
    }

    public void close() throws IOException {
        indices.close();
        data.close();
    }

    public boolean delete() throws IOException {
        return indicesFile.delete() && dataFile.delete();
    }

    @Override
    public long numCols() {
        return colLength;
    }

    @Override
    public long numRows() {
        return rowLength;
    }

    @Override
    public Double get(long rowIdx, long colIdx) {
        double result = 0.0;
        try {
            indices.seek(0);
            data.seek(0);
            while (true) {
                try {
                    long currentRow = indices.readLong();
                    long currentCol = indices.readLong();
                    double value = data.readDouble();
                    if (rowIdx == currentRow && colIdx == currentCol) {
                        result = value;
                        break;
                    }
                } catch (EOFException e) {
                    break;
                }
            }
            indices.seek(indicesPointer);
            data.seek(dataPointer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void set(long rowIdx, long colIdx, Double value) {
        try {
            indices.writeLong(rowIdx);
            indices.writeLong(colIdx);
            data.writeDouble(value);
            indicesPointer = indices.getFilePointer();
            dataPointer = data.getFilePointer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
