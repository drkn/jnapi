package jnapi.subtitles.handlers;

import jnapi.subtitles.FormatHandler;
import jnapi.subtitles.Line;
import jnapi.subtitles.SubtitlesException;
import jnapi.utils.CollectionsUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO File description
 *
 * @author Maciej Dragan
 */
public class MPL2 extends FormatHandler {

    @Override
    protected Line parseLine(float fps, String line) throws SubtitlesException {
        if (line == null || line.trim().length() == 0) {
            return null;
        }
        Pattern pattern = Pattern.compile("\\[(\\d+)\\]\\[(\\d+)\\](.*)");
        Matcher matcher = pattern.matcher(line);
        if (!matcher.find()) {
            throw new SubtitlesException("Invalid line: " + line);
        }
        return new Line(
                matcher.group(3).split("[\\|]"),
                (long) (Float.parseFloat(matcher.group(1)) / 10.0f * fps),
                (long) (Float.parseFloat(matcher.group(2)) / 10.0f * fps)
        );
    }

    @Override
    protected void startParse() {
    }

    @Override
    protected Line endParse() {
        return null;
    }

    @Override
    protected void startSave() {
    }

    @Override
    protected List<String> save(Line line, float fps) {
        return Arrays.asList(new String[]{
                String.format("[%d][%d] %s",
                        (long) (((float) line.getStart()) / fps * 10.0f),
                        (long) (((float) line.getEnd()) / fps * 10.0f),
                        CollectionsUtils.join(line.getText(), "|"))
        });
    }
}
