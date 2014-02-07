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
 * MicroDVD format handler
 *
 * @author Maciej Dragan
 */
public class MicroDVD extends FormatHandler {

    @Override
    protected Line parseLine(float fps, String line) throws SubtitlesException {
        if (line == null || line.trim().length() == 0) {
            return null;
        }
        Pattern pattern = Pattern.compile("\\{(\\d+)\\}\\{(\\d+)\\}(.*)");
        Matcher matcher =  pattern.matcher(line);
        if (!matcher.find()) {
            throw new SubtitlesException("Invalid line: " + line);
        }
        return new Line(matcher.group(3).split("[\\|]"), Long.parseLong(matcher.group(1)), Long.parseLong(matcher.group(2)));
    }

    @Override
    protected void startParse() {}

    @Override
    protected Line endParse() {
        return null;
    }

    @Override
    protected void startSave() {}

    @Override
    protected List<String> save(Line line, float fps) {
        return Arrays.asList(new String[]{
                String.format("{%d}{%d} %s", line.getStart(), line.getEnd(), CollectionsUtils.join(line.getText(), "|"))
        });

    }


}
