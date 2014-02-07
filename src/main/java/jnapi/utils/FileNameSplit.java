package jnapi.utils;

import java.io.File;

/**
 * File name manipulation util
 *
 * @author Maciej Dragan
 */
public class FileNameSplit {

    private String directory;
    private String name;
    private String ext;

    /**
     * Create file name split from a file name
     *
     * @param filename
     */
    public FileNameSplit(String filename) {
        this(new File(filename));
    }

    /**
     * Create file name split from a file
     *
     * @param file
     */
    public FileNameSplit(File file) {
        directory = file.getParent();
        name = file.getName();
        int extIndex = file.getName().lastIndexOf('.');
        if (extIndex >= 0) {
            ext = file.getName().substring(extIndex + 1, file.getName().length());
            name = file.getName().substring(0, file.getName().length() - ext.length() - 1);
        } else {
            ext = "";
            name = file.getName();
        }
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameWithExt() {
        return name + (ext.length() > 0 ? "." + ext : "");
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getAbsolutePath() {
        return directory + File.separator + getNameWithExt();
    }

}
