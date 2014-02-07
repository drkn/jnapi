package jnapi.subtitles;

import jnapi.mediainfo.MediaInfo;

import java.io.File;
import java.util.List;

/**
 * Subtitles converter. Handles reading and writing of several subtitles formats
 * <p/>
 * Resource: http://leksykot.top.hell.pl/lx3/B/subtitle_formats#c1
 *
 * @author Maciej Dragan
 */
public class Converter {

    List<Line> lines = null;
    private File videoFile;
    private float fps;

    public Converter(File videoFile) throws SubtitlesException {
        this.videoFile = videoFile;
        try {
            // Read subtitles framerate. This is needed for conversion
            MediaInfo mediaInfo = new MediaInfo();
            mediaInfo.open(videoFile);
            fps = Float.parseFloat(mediaInfo.get(MediaInfo.StreamKind.Video, 0, "FrameRate"));
        } catch (NumberFormatException e) {
            throw new SubtitlesException("Could not detect video file FPS");
        }
    }

    /**
     * Load and parse subtitles file
     *
     * @param fileName
     * @return Recognized format or null is none is found
     */
    public Format loadSubtitles(String fileName) {
        return loadSubtitles(new File(fileName));
    }

    public Format loadSubtitles(File subtitlesFile) {
        lines = null;
        for (Format format : Format.values()) {
            try {
                FormatHandler formatHandler = format.getFormatHandler();
                lines = formatHandler.parse(subtitlesFile, fps);
                if (lines != null && lines.size() > 0) {
                    return format;
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * Save subtitles to specified format
     *
     * @param subtitlesFiles
     * @param format
     * @throws SubtitlesException
     */
    public void saveSubtitles(File subtitlesFiles, Format format) throws SubtitlesException {
        format.getFormatHandler().save(subtitlesFiles, lines, fps);
    }


}
