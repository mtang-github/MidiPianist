package util.file;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.Properties;
import java.util.Scanner;

/**
 * The class {@code FileUtil} provides several utility functions for dealing with files.
 */
public final class FileUtil {
    public static final String DIRECTORY_EXTENSION = "directory";

    private FileUtil() {}

    /**
     * Returns the absolute path of the given file.
     *
     * @param file the file for which it is required to find the absolute path.
     * @return the absolute path of the given file.
     */
    public static String getFilePath(File file) {
        return file.getAbsolutePath();
    }

    /**
     * Returns the name of the given file without its file extension.
     *
     * @param file the file for which it is required to find the name without the extension.
     * @return the name of the given file without its file extension.
     */
    public static String getFileName(File file) {
        String filePath = getFilePath(file);
        int lastIndexOfPeriod = filePath.lastIndexOf('.');
        String filePathWithoutExtension;
        if (lastIndexOfPeriod > 0) {
            String extension = filePath.substring(lastIndexOfPeriod);
            if (extension.contains("\\")) { //if the extension has a backslash, it means there is no extension
                filePathWithoutExtension = filePath;
            } else {
                filePathWithoutExtension = filePath.substring(0, lastIndexOfPeriod);
            }
        } else {
            filePathWithoutExtension = filePath;
        }
        int lastIndexOfBackSlash = filePath.lastIndexOf('\\');
        if (lastIndexOfBackSlash > 0) {
            return filePathWithoutExtension.substring(lastIndexOfBackSlash + 1);
        } else {
            return filePathWithoutExtension;
        }
    }

    /**
     * Returns the extension of the given file.
     *
     * @param file the file for which it is required to find the extension.
     * @return the extension of the given file.
     */
    public static String getFileExtension(File file) {
        if (!file.isDirectory()) {
            String filePath = getFilePath(file);
            int lastIndexOfPeriod = filePath.lastIndexOf('.');
            if (lastIndexOfPeriod > 0) {
                return filePath.substring(lastIndexOfPeriod + 1);
            }
            return null;
        } else {
            return DIRECTORY_EXTENSION;
        }
    }

    /**
     * Returns a new {@code InputStream} from the given file.
     *
     * @param file the file to create an input stream of.
     * @return a new {@code InputStream} from the given file.
     *
     * @throws RuntimeException if the file cannot be found.
     */
    public static InputStream makeInputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            throw new RuntimeException("Unable to find file " + file.getAbsolutePath(), fnfe);
        }
    }

    /**
     * Returns a new {@code OutputStream} from the given file.
     *
     * @param file the file to create an output stream of.
     * @return a new {@code OutputStream} from the given file.
     *
     * @throws RuntimeException if the file cannot be found.
     */
    public static OutputStream makeOutputStream(File file){
        try{
            return new FileOutputStream(file);
        } catch (FileNotFoundException fnfe){
            fnfe.printStackTrace();
            throw new RuntimeException("Unable to make file " + file.getAbsolutePath(), fnfe);
        }
    }

    /**
     * Returns a new {@code Scanner} from the given file.
     *
     * @param file the file to create a scanner from.
     * @return a new {@code Scanner} from the given file.
     *
     * @throws RuntimeException if the file cannot be found.
     */
    public static Scanner makeScanner(File file){
        try{
            return new Scanner(file);
        }catch(FileNotFoundException fnfe){
            fnfe.printStackTrace();
            throw new RuntimeException("Unable to make file " + file.getAbsolutePath(), fnfe);
        }
    }

    /**
     * Parses a given file as an image and returns a {@code BufferedImage}.
     *
     * @param imageFile the image file to read.
     *
     * @return a {@code BufferedImage} as read by {@link ImageIO}.
     *
     * @throws UncheckedIOException if the image file cannot be read.
     */
    public static BufferedImage parseImage(File imageFile) {
        try {
            return ImageIO.read(imageFile);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new UncheckedIOException(ioe);
        }
    }

    /**
     * Parses a given file as a properties file and returns a {@code Properties}.
     *
     * @param propertiesFile the properties file to read.
     *
     * @return a {@code Properties} object created from the properties file.
     *
     * @throws UncheckedIOException if the properties file cannot be read.
     */
    public static Properties parseProperties(File propertiesFile) {
        try {
            Properties toRet = new Properties();
            InputStream inputStream = makeInputStream(propertiesFile);
            toRet.load(inputStream);
            inputStream.close();
            return toRet;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new UncheckedIOException(ioe);
        }
    }

    /**
     * Writes a {@code Properties} object to the specified file.
     *
     * @param properties the {@code Properties} object to write.
     * @param propertiesFile the properties file to write to.
     *
     * @throws UncheckedIOException if the properties file cannot be written to.
     */
    public static void writeProperties(Properties properties, File propertiesFile){
        try{
            OutputStream outputStream = makeOutputStream(propertiesFile);
            properties.store(outputStream, null);
            outputStream.close();
        } catch(IOException ioe){
            ioe.printStackTrace();
            throw new UncheckedIOException(ioe);
        }
    }
}
