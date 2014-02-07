package jnapi.utils;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Encoding tools
 *
 * @author Maciej Dragan
 */
public class EncodingUtils {

    /**
     * Detect encoding of a text file
     *
     * @param file Text file
     * @return Detected encoding
     */
    public static String detectEncoding(File file) {
        CharsetDetector detector = new CharsetDetector();
        try {
            detector.setText(FileUtils.readFile(file));
            CharsetMatch match = detector.detect();
            return match.getName();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Convert text encoding of a text file
     *
     * @param inputFile    Input text file
     * @param outputFile   Output text file
     * @param fromEncoding Source encoding
     * @param toEncoding   Destination encoding
     * @return true if conversion succeeded
     */
    public static boolean convertEncoding(File inputFile, File outputFile, String fromEncoding, String toEncoding) {
        // Detect encoding
        if (fromEncoding == null) {
            fromEncoding = detectEncoding(inputFile);
            if (fromEncoding == null) {
                return false;
            }
        }

        // Prepare charsets
        Charset fromCharset = null;
        Charset toCharset = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            fromCharset = Charset.forName(fromEncoding);
            toCharset = Charset.forName(toEncoding);

            // Read file
            byte[] fileData = FileUtils.readFile(inputFile);

            // Convert
            ByteBuffer inputBuffer = ByteBuffer.wrap(fileData);
            CharBuffer charBuffer = fromCharset.decode(inputBuffer);
            ByteBuffer outputBuffer = toCharset.encode(charBuffer);
            outputStream = new FileOutputStream(outputFile.getAbsolutePath());

            // Write BOM
            BOM bom = BOM.getByName(toEncoding);
            if (bom.getValue().length > 0) {
                outputStream.write(bom.getValue());
            }

            // Write content
            outputStream.write(outputBuffer.array());
            return true;
        } catch (Exception e) {
            // Fail silently
        } finally {
            IOUtils.close(inputStream);
            IOUtils.close(outputStream);
        }
        return false;
    }

}
