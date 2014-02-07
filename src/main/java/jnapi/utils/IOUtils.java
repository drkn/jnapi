package jnapi.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IO utilities
 *
 * @author Maciej Dragan
 */
public class IOUtils {

    /**
     * Copy data from one stream to another. Method assumes both streams are initialized. Input stream will be closed
     * by this method
     *
     * @param inputStream
     * @param outputStream
     */
    public static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        try {
            byte buff[] = new byte[4096];
            int count = 0;
            while ((count = inputStream.read(buff, 0, buff.length)) > 0) {
                outputStream.write(buff, 0, count);
            }
        } finally {
            close(inputStream);
        }
    }

    /**
     * Silently close closeable object
     *
     * @param closeable A closeable to close
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException exception) {
                // Fail silently
            }
        }
    }


}
