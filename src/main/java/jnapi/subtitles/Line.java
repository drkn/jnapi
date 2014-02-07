package jnapi.subtitles;

import jnapi.utils.CollectionsUtils;

import java.util.*;

/**
 * TODO File description
 *
 * @author Maciej Dragan
 */
public class Line {

    private long start;
    private long end;
    private List<String> text;

    public Line(String[] text, long start, long end) {
        this(Arrays.asList(text), start, end);
    }

    public Line(List<String> text, long start, long end) {
        this.text = text;
        // Just to be sure - postprocess lines
        for (int i = 0; i < text.size(); i++) {
            text.set(i, text.get(i).replaceAll("[\n\r]", ""));
        }
        this.start = start;
        this.end = end;
    }

    public List<String> getText() {
        return text;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return String.format("{%d}{%d} %s", start, end, CollectionsUtils.join(text, "|"));
    }


}
