package jnapi.providers;

import jnapi.sevenzip.SevenZipExtractCallback;
import jnapi.subtitles.Converter;
import jnapi.subtitles.Format;
import jnapi.utils.*;
import net.sf.sevenzipjbinding.ISevenZipInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * napiprojekt.pl subtitles provider
 *
 * @author Maciej Dragan
 */
public class NapiProjekt {

    private static final String URL_FORMAT = "http://napiprojekt.pl/unit_napisy/dl.php?l=PL&f=%s&t=%s&v=other&kolejka=false&nick=&pass=&napios=Linux";
    private static final String INVALID_CONTENT = "NPc0";
    private static final String ARCHIVE_PASSWORD = "iBlm8NTigvru0Jr0";

    /**
     * Super extra cryptographic method ;)
     *
     * @param input
     * @return
     */
    private String f(String input) {
        int[] idx = new int[]{0xe, 0x3, 0x6, 0x8, 0x2};
        int[] mul = new int[]{2, 2, 5, 4, 3};
        int[] add = new int[]{0, 0xd, 0x10, 0xb, 0x5};
        StringBuffer result = new StringBuffer();
        for (int ii = 0; ii < idx.length; ii++) {
            int a = add[ii];
            int m = mul[ii];
            int i = idx[ii];
            int t = a + Integer.parseInt(input.substring(i, i + 1), 16);
            int v = Integer.parseInt(input.substring(t, Math.min(t + 2, input.length())), 16);
            String tmp = String.format("%x", v * m);
            result.append(tmp.substring(tmp.length() - 1));
        }
        return result.toString();
    }

    /**
     * Download subtitles for a file
     *
     * @param videoFileName
     * @return
     * @throws IOException
     */
    public boolean downloadSubtitles(String videoFileName) throws IOException {
        File downloaded = null;
        File unzipped = null;
        File videoFile = new File(videoFileName);
        Log.info("Downloading subtitles for " + videoFile.getName());

        // Preapre download URL
        String md5 = CryptoUtils.md5sum(
                FileUtils.readFile(videoFile, 10485760)
        );
        String url = String.format(URL_FORMAT, md5, f(md5));

        try {
            // Download subtitles
            downloaded = NetUtils.downloadFile(url);
            if (downloaded.length() == INVALID_CONTENT.length()) {
                // Check if downloaded file is invalid
                String content = new String(FileUtils.readFile(downloaded, INVALID_CONTENT.length()));
                if (content.equals(INVALID_CONTENT)) {
                    Log.info("Could not download subtitles: " + content);
                    return false;
                }
            }

            // Unzip subtitles
            unzipped = unzip(downloaded);
            if (unzipped == null) {
                return false;
            }

            // Move to dest directory
            FileNameSplit subtitlesFileName = new FileNameSplit(videoFileName);
            subtitlesFileName.setExt("txt");

            // Convert to UTF
            if (!EncodingUtils.convertEncoding(unzipped, new File(subtitlesFileName.getAbsolutePath()), null, "utf-8")) {
                return false;
            }

            // Convert subtitles format
            try {
                Converter converter = new Converter(videoFile);
                Format format = converter.loadSubtitles(subtitlesFileName.getAbsolutePath());
                if (format != null) {
                    Log.info("Downloaded subtitles format: " + format);

                    // Rename downloaded subtitles file if needed
                    if (!subtitlesFileName.getExt().equals(format.getExt())) {
                        File file = new File(subtitlesFileName.getAbsolutePath());
                        subtitlesFileName.setExt(format.getExt());
                        file.renameTo(new File(subtitlesFileName.getAbsolutePath()));
                    }

                    // Convert subtitles format
                    for (Format availableFormat : Format.values()) {
                        if (availableFormat != format) {
                            Log.info("Converting subtitles to: " + availableFormat);
                            subtitlesFileName.setExt(availableFormat.getExt());
                            converter.saveSubtitles(new File(subtitlesFileName.getAbsolutePath()), availableFormat);
                        }
                    }
                } else {
                    Log.error("Unrecognized subtitles format for " + subtitlesFileName.getNameWithExt());
                }
            } catch (Exception e) {
                Log.error("Could not convert subtitles: " + e.getMessage());
            }

            return true;
        } catch (Exception e) {
            Log.error("Could not download subtitles: " + e.getMessage());
        } finally {
            // Some cleanup
            FileUtils.deleteFile(downloaded);
            FileUtils.deleteFile(unzipped);
        }
        return false;
    }

    /**
     * Unzip downloaded subtitles
     *
     * @param file
     * @return
     * @throws IOException
     */
    private File unzip(File file) throws IOException {
        RandomAccessFile randomAccessFile = null;
        ISevenZipInArchive inArchive = null;
        try {
            randomAccessFile = new RandomAccessFile(file.getAbsolutePath(), "r");
            inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile));


            if (inArchive.getNumberOfItems() == 1) {
                SevenZipExtractCallback callback = new SevenZipExtractCallback(inArchive, file.getParent(), ARCHIVE_PASSWORD);
                inArchive.extract(new int[]{0}, false, callback); // Extract only first file

                return callback.getOutputs().size() == 1 ? callback.getOutputs().get(0).getFile() : null;
            }
        } catch (SevenZipException e) {
            Log.error("Could not unzip downloaded subtitles");
            return null;
        } finally {
            if (inArchive != null) {
                try {
                    inArchive.close();
                } catch (Exception e) {
                }
            }
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

}
