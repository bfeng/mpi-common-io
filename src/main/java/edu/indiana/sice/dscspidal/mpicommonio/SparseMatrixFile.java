package edu.indiana.sice.dscspidal.mpicommonio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SparseMatrixFile implements Matrix<Double> {

    private static final String MODE = "rws";

    private static List<DataCell> dataCache;

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

    public boolean delete() {
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
        if (dataCache == null) {
            dataCache = this.toFullData();
        }
        if (dataCache != null) {
            for (DataCell each : dataCache) {
                long currentRow = each.getRowIdx();
                long currentCol = each.getColIdx();
                double value = each.getValue();
                if (rowIdx == currentRow && colIdx == currentCol) {
                    result = value;
                    break;
                }
            }
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

    private List<DataCell> toFullData() {
        try {
            List<DataCell> result = new ArrayList<>();
            indices.seek(0);
            data.seek(0);
            while (true) {
                try {
                    long currentRow = indices.readLong();
                    long currentCol = indices.readLong();
                    double value = data.readDouble();
                    result.add(new DataCell(currentRow, currentCol, value));
                } catch (EOFException e) {
                    break;
                }
            }
            indices.seek(indicesPointer);
            data.seek(dataPointer);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
