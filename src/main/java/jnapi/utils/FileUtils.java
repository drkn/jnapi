package jnapi.utils;

import java.io.*;

/**
 * File related util methods
 *
 * @author Maciej Dragan
 */
public class FileUtils {

    /**
     * Silently delete a file
     *
     * @param file A file to delete
     */
    public static void deleteFile(File file) {
        if (file != null) {
            try {
                file.delete();
            } catch (Exception exception) {
                file.delete();
            }
        }
    }

    /**
     * Read file into byte array with read size limit
     *
     * @param file      File to read
     * @param sizeLimit Read size limit or 0 for no limit
     * @return Byte array with file contents
     * @throws java.io.IOException
     */
    public static byte[] readFile(File file, long sizeLimit) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        long bytesRead = 0;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            if (sizeLimit == 0) {
                sizeLimit = inputStream.available();
            }
            int count = 0;
            while ((count = inputStream.read(buff, 0, Math.min(buff.length, (int) (sizeLimit - bytesRead)))) > 0) {
                bytesRead += count;
                outputStream.write(buff, 0, count);
            }
        } finally {
            IOUtils.close(inputStream);
        }
        return outputStream.toByteArray();
    }

    /**
     * Read whole file into byte array
     *
     * @param file A file to read
     * @return Byte array with file contents
     * @throws IOException
     */
    public static byte[] readFile(File file) throws IOException {
        return readFile(file, 0);
    }

    /**
     * Copy a file
     *
     * @param source Source file name
     * @param target Destination file name
     * @return true if copy succeeded
     */
    public static boolean cp(String source, String target) {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(source);
            outputStream = new FileOutputStream(target);
            IOUtils.copyStream(inputStream, outputStream);
        } catch (IOException e) {
            return false;
        } finally {
            IOUtils.close(inputStream);
            IOUtils.close(outputStream);
        }
        return true;
    }

}
