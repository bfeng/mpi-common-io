package edu.indiana.sice.dscspidal.mpicommonio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MPIFile {

    private static final String NOT_IMPL = "MPI-IO not fully implemented.";

    private File localFile;
    private RandomAccessFile randomFile;
    private boolean isLocal;

    public MPIFile(String pathname) {
        this(pathname, true);
    }

    private MPIFile(String pathname, boolean isLocal) {
        this.isLocal = isLocal;
        this.localFile = new File(pathname);
    }

    public int open() throws IOException {
        if (isLocal) {
            try {
                randomFile = new RandomAccessFile(localFile, "rws");
            } catch (FileNotFoundException e) {
                if (localFile.createNewFile()) {
                    randomFile = new RandomAccessFile(localFile, "rws");
                } else
                    throw e;
            }
            return 0;
        } else {
            throw new IOException(NOT_IMPL);
        }
    }

    public boolean delete() throws IOException {
        if (isLocal) {
            if (localFile.exists()) return localFile.delete();
            return true;
        } else {
            throw new IOException(NOT_IMPL);
        }
    }

    public long getFilePointerAt() throws IOException {
        return randomFile.getFilePointer() / Byte.SIZE;
    }

    public double readDoubleAt() throws IOException {
        return randomFile.readDouble();
    }

    public double readDoubleAt(long pos) throws IOException {
        seekTo(pos);
        return readDoubleAt();
    }

    public void writeDouble(double value) throws IOException {
        randomFile.writeDouble(value);
    }

    public void writeDoubleAt(long pos, double value) throws IOException {
        seekTo(pos);
        randomFile.writeDouble(value);
    }

    public void seekTo(long pos) throws IOException {
        randomFile.seek(pos * Byte.SIZE);
    }

    public void close() throws IOException {
        randomFile.close();
    }
}
