package jnapi.sevenzip;

import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.SevenZipException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 7ZipBinding file save output stream handler
 *
 * @author Maciej Dragan
 */
public class SevenZipFileOutputStream implements ISequentialOutStream {

    private FileOutputStream fileOutputStream;
    private File file;

    public SevenZipFileOutputStream(File file) throws FileNotFoundException {
        this.file = file;
        fileOutputStream = new FileOutputStream(file.getAbsolutePath());
    }

    @Override
    public int write(byte[] bytes) throws SevenZipException {
        try {
            fileOutputStream.write(bytes);
        } catch (IOException e) {
            throw new SevenZipException("Could not write to file", e);
        }
        return bytes.length;
    }

    public File getFile() {
        return file;
    }

    public FileOutputStream getFileOutputStream() {
        return fileOutputStream;
    }
}
