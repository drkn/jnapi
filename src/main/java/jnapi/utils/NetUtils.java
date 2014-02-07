package jnapi.utils;

import java.io.*;
import java.net.URL;

/**
 * Various network related utilities
 *
 * @author Maciej Dragan
 */
public class NetUtils {

    public static void downloadFile(String url, OutputStream outputStream) throws IOException {
        IOUtils.copyStream(new BufferedInputStream(new URL(url).openStream()), outputStream);
    }

    public static File downloadFile(String url) throws IOException {
        File tempFile = File.createTempFile("download_" + System.currentTimeMillis(), ".tmp");
        FileOutputStream outputStream = null;
        try {
            downloadFile(url, new FileOutputStream(tempFile));
        } finally {
            IOUtils.close(outputStream);
        }
        return tempFile;
    }

}
