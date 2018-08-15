package edu.indiana.sice.dscspidal.mpicommonio;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class SparseMatrixFile implements Matrix<Double> {

    private static final String MODE = "rw";

    private static final String R_ONLY = "r";

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

    private SparseMatrixFile() {
    }

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

    public double[] getRow(long rowIdx) {
        double[] result = new double[(int) colLength];
        if (dataCache == null) {
            dataCache = this.toFullData();
        }
        if (dataCache != null) {
            for (DataCell each : dataCache) {
                long currentRow = each.getRowIdx();
                long currentCol = each.getColIdx();
                double value = each.getValue();
                if (currentRow == rowIdx) {
                    result[(int) currentCol] = value;
                }
            }
        }
        return result;
    }

    public SparseMatrix toSparseMatrix() {
        SparseMatrix sparseMatrix = new SparseMatrix((int) rowLength, (int) colLength);
        if (dataCache == null) {
            dataCache = this.toFullData();
        }
        if (dataCache != null) {
            for (DataCell each : dataCache) {
                int currentRow = (int) each.getRowIdx();
                int currentCol = (int) each.getColIdx();
                double value = each.getValue();
                sparseMatrix.set(currentRow, currentCol, value);
            }
        }
        return sparseMatrix;
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

    /**
     * Square matrix only
     *
     * @param indicesPath indices
     * @param dataPath    data
     * @param startRow    inclusive
     * @param endRow      inclusive
     * @return partial sparse matrix
     */
    public static SparseMatrix loadIntoMemory(String indicesPath, String dataPath, int startRow, int endRow, int dim) {
        if (startRow < 0 || startRow > endRow || startRow > dim) {
            throw new RuntimeException("Illegal row range");
        }
        try {
            SparseMatrix sparseMatrix = new SparseMatrix(Math.abs(endRow - startRow) + 1, dim);
            SparseMatrixFile smf = new SparseMatrixFile();
            smf.rowLength = dim;
            smf.colLength = dim;
            smf.indicesFile = new File(indicesPath);
            smf.dataFile = new File(dataPath);
            BufferedInputStream indexIn = new BufferedInputStream(new FileInputStream(smf.indicesFile));
            BufferedInputStream dataIn = new BufferedInputStream(new FileInputStream(smf.dataFile));
            byte[] buf = new byte[8];
            int len = 0;
            while (indexIn.available() > 0 && dataIn.available() > 0) {
                len = indexIn.read(buf);
                long i = bytesToLong(buf);
                len = indexIn.read(buf);
                long j = bytesToLong(buf);
                len = dataIn.read(buf);
                double value = ByteBuffer.wrap(buf).getDouble();
                if (i >= startRow && i <= endRow) {
                    sparseMatrix.set((int) i - startRow, (int) j, value);
                }
            }
            indexIn.close();
            dataIn.close();
            return sparseMatrix;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static long bytesToLong(byte[] bytes) {
        return (bytes[0] & 0xFFL) << 56
                | (bytes[1] & 0xFFL) << 48
                | (bytes[2] & 0xFFL) << 40
                | (bytes[3] & 0xFFL) << 32
                | (bytes[4] & 0xFFL) << 24
                | (bytes[5] & 0xFFL) << 16
                | (bytes[6] & 0xFFL) << 8
                | (bytes[7] & 0xFFL);
    }

    private static byte[] longToBytes(long l) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (l & 0xFF);
            l >>= 8;
        }
        return result;
    }

    public static void dumpToFile(final SparseMatrix sparseMatrix, final String outputDir) {
        try {
            SparseMatrixFile sparseMatrixFile = new SparseMatrixFile(outputDir, sparseMatrix.numRows(), sparseMatrix.numCols());
            sparseMatrixFile.open();

            BufferedOutputStream indexOut = new BufferedOutputStream(new FileOutputStream(sparseMatrixFile.indicesFile));
            BufferedOutputStream dataOut = new BufferedOutputStream(new FileOutputStream(sparseMatrixFile.dataFile));

            for (int i = 0; i < sparseMatrix.numRows(); i++) {
                SparseVector row = sparseMatrix.rows[i];
                for (Integer j : row.map.keySet()) {
                    indexOut.write(longToBytes(i));
                    indexOut.write(longToBytes(j));
                    double v = row.map.get(j);
                    dataOut.write(longToBytes(Double.doubleToLongBits(v)));
                }
            }

            indexOut.close();
            dataOut.close();
            sparseMatrixFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
