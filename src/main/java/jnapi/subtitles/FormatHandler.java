package jnapi.subtitles;

import jnapi.utils.BOM;
import jnapi.utils.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Converter format
 *
 * @author Maciej Dragan
 */
public abstract class FormatHandler {

    public static final String NEWLINE = "\n";

    protected abstract Line parseLine(float fps, String line) throws SubtitlesException;

    protected abstract void startParse();

    protected abstract Line endParse();

    public List<Line> parse(File file, float fps) throws SubtitlesException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            List<Line> lines = new ArrayList<Line>();
            startParse();
            for (String line; (line = reader.readLine()) != null; ) {
                Line subtitleLine = parseLine(fps, line);
                if (subtitleLine != null) {
                    lines.add(subtitleLine);
                }
            }
            Line subtitleLine = endParse();
            if (subtitleLine != null) {
                lines.add(subtitleLine);
            }
            if (lines.size() == 0) {
                throw new SubtitlesException("Subtitles format not recognized");
            }
            return lines;
        } catch (IOException e) {
            throw new SubtitlesException("Could not read subtitles " + file.getName());
        } finally {
            IOUtils.close(reader);
        }
    }

    protected abstract void startSave();

    protected abstract List<String> save(Line line, float fps);

    public void save(File file, List<Line> lines, float fps) throws SubtitlesException {
        if (lines == null || lines.size() == 0) {
            throw new SubtitlesException("No lines to save");
        }
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            stream.write(BOM.UTF8.getValue()); // Write BOM. It's optional, but some devices still require it ...
            startSave();
            for (Line line : lines) {
                List<String> saved = save(line, fps);
                if (saved != null && saved.size() > 0) {
                    for (String savedLine : saved) {
                        stream.write((savedLine + NEWLINE).getBytes("UTF-8"));
                    }
                }
            }

        } catch (IOException e) {
            throw new SubtitlesException("Could not save subtitles file. " + e.getMessage());
        } finally {
            IOUtils.close(stream);
        }
    }

}
