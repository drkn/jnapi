package jnapi;

import jnapi.providers.NapiProjekt;
import jnapi.subtitles.SubtitlesException;
import jnapi.utils.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * JNapi main method
 *
 * @author Maciej Dragan
 */
public class JNapi {

    private static final String[] VIDEO_EXTENSIONS = new String[]{"mp4", "avi", "mkv"};

    public static void main(String[] args) throws IOException, SubtitlesException {
        if (args.length == 0) {
            Log.error("Usage: jnapi [list of files or directories]");
        }

        NapiProjekt np = new NapiProjekt();

        for (String arg : args) {
            File argf = new File(arg);
            if (argf.exists()) {
                if (argf.isFile()) {
                    np.downloadSubtitles(arg);
                } else if (argf.isDirectory()) {
                    String[] fileNames = argf.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            File test = new File(dir.getAbsolutePath() + File.separator + name);
                            if (test.exists() && test.isFile()) {
                                for (String ext : VIDEO_EXTENSIONS) {
                                    if (name.toLowerCase().endsWith("." + ext)) {
                                        return true;
                                    }
                                }
                            }
                            return false;
                        }
                    });
                    for (String fileName : fileNames) {
                        np.downloadSubtitles(argf.getAbsolutePath() + File.separator + fileName);
                    }
                } else {
                    Log.error("Invalid parameter " + arg);
                }
            } else {
                Log.error(arg + " does not exist");
            }
        }
    }

}
