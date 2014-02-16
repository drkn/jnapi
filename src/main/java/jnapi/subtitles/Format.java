package jnapi.subtitles;

/**
 * Supported subtitles formats
 *
 * @author Maciej Dragan
 */
public enum Format {

    MicroDVD(jnapi.subtitles.handlers.MicroDVD.class, "txt"),
    SubRip(jnapi.subtitles.handlers.SubRip.class, "srt"),
    MPL2(jnapi.subtitles.handlers.MPL2.class, "mpl2.txt");
    private String ext;
    private Class<? extends FormatHandler> formatHandlerClass;


    private Format(Class<? extends FormatHandler> formatHandlerClass, String ext) {
        this.formatHandlerClass = formatHandlerClass;
        this.ext = ext;
    }

    public FormatHandler getFormatHandler() {
        try {
            return formatHandlerClass.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public String getExt() {
        return ext;
    }
}
