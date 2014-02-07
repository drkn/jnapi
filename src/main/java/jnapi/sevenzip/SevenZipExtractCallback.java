package jnapi.sevenzip;

import net.sf.sevenzipjbinding.*;
import jnapi.utils.FileUtils;
import jnapi.utils.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * SevenZipExtractCallback for 7ZipBinding with password support
 *
 * @author Maciej Dragan
 */
public class SevenZipExtractCallback implements IArchiveExtractCallback, ICryptoGetTextPassword {

    private int index;
    private String password;
    private ISevenZipInArchive inArchive;
    private String outputDirectory;
    private ArrayList<SevenZipFileOutputStream> outputs = new ArrayList<SevenZipFileOutputStream>();
    private SevenZipFileOutputStream currentOutput;


    public SevenZipExtractCallback(ISevenZipInArchive inArchive, String outputDirectory, String password) {
        this.inArchive = inArchive;
        this.password = password;
        this.outputDirectory = outputDirectory;
    }

    @Override
    public ISequentialOutStream getStream(int i, ExtractAskMode extractAskMode) throws SevenZipException {
        inArchive.getProperty(i, PropID.PATH);
        this.index = i;
        if (extractAskMode != ExtractAskMode.EXTRACT) {
            return null;
        }
        try {
            currentOutput = new SevenZipFileOutputStream(new File(outputDirectory + File.separator + inArchive.getProperty(i, PropID.PATH)));
            return currentOutput;
        } catch (FileNotFoundException e) {
            throw new SevenZipException("Could not create output file", e);
        }
    }

    @Override
    public void prepareOperation(ExtractAskMode extractAskMode) throws SevenZipException {
    }

    @Override
    public void setOperationResult(ExtractOperationResult extractOperationResult) throws SevenZipException {
        IOUtils.close(currentOutput.getFileOutputStream());
        if (extractOperationResult != ExtractOperationResult.OK) {
            FileUtils.deleteFile(currentOutput.getFile());
        } else {
            outputs.add(currentOutput);
        }
    }

    @Override
    public void setTotal(long l) throws SevenZipException {
    }

    @Override
    public void setCompleted(long l) throws SevenZipException {
    }

    @Override
    public String cryptoGetTextPassword() throws SevenZipException {
        return password;
    }

    public List<SevenZipFileOutputStream> getOutputs() {
        return outputs;
    }
}
