package jnapi.subtitles.handlers;

import jnapi.subtitles.FormatHandler;
import jnapi.subtitles.Line;
import jnapi.subtitles.SubtitlesException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO File description
 *
 * @author Maciej Dragan
 */
public class SubRip extends FormatHandler {

    private int currentIndex = 1;
    private long start = -1;
    private long end = -1;
    private List<String> text = null;
    private SimpleDateFormat timeFormat;
    private boolean prevLineWasBlank = true;

    public SubRip() {
        timeFormat = new SimpleDateFormat("HH:mm:ss,SSS");
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    protected Line parseLine(float fps, String line) throws SubtitlesException {
        Line subtitlesLine = null;
        Pattern pattern = Pattern.compile("(\\d{2}:\\d{2}:\\d{2},\\d{3}) --> (\\d{2}:\\d{2}:\\d{2},\\d{3})");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            prevLineWasBlank = false;
            subtitlesLine = endParse();
            text = new ArrayList<String>();
            try {
                start = (long) (timeFormat.parse(matcher.group(1)).getTime() * fps / 1000.0f);
                end = (long) (timeFormat.parse(matcher.group(2)).getTime() * fps / 1000.0f);
            } catch (ParseException e) {
                start = -1;
                end = -1;
            }
            return subtitlesLine;
        }
        if (line != null && line.trim().length() > 0 && !prevLineWasBlank) {
            text.add(line.trim());
        }
        prevLineWasBlank = line.trim().length() == 0;
        return null;
    }

    @Override
    protected void startParse() {
        prevLineWasBlank = true;
        currentIndex = 1;
        start = -1;
        end = -1;
        text = new ArrayList<String>();
    }

    @Override
    protected Line endParse() {
        if (start > -1 && end > -1 && text != null && text.size() > 0) {
            return new Line(text, start, end);
        }
        return null;
    }

    @Override
    protected void startSave() {
        currentIndex = 1;
    }

    private Date frameToTime(long frame, float fps) {
        return new Date((long) ((frame * 1000) / fps));
    }

    @Override
    protected List<String> save(Line line, float fps) {
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        List<String> lines = new ArrayList<String>();
        lines.add("" + currentIndex);
        lines.add(String.format("%s --> %s",
                timeFormat.format(frameToTime(line.getStart(), fps)),
                timeFormat.format(frameToTime(line.getEnd(), fps))
        ));
        for (String textLine : line.getText()) {
            lines.add(textLine);
        }
        lines.add("");
        currentIndex++;
        return lines;
    }


}
