package util.file;

import java.io.File;
import java.util.function.Consumer;

/**
 * A {@code DirectoryConsumer} is a file consumer which, when given a directory, sends all files within that directory
 * to another file consumer specified at construction.
 */
@SuppressWarnings("ClassCanBeRecord")
public class DirectoryConsumer implements Consumer<File> {
    private final Consumer<File> fileConsumer;

    /**
     * Constructs a {@code DirectoryConsumer} with the specified file consumer.
     *
     * @param fileConsumer the file consumer which will accept all files within the directories.
     */
    public DirectoryConsumer(Consumer<File> fileConsumer){
        this.fileConsumer = fileConsumer;
    }

    /**
     * Sends all files within the specified directory to the file consumer.
     *
     * @param directory the directory which will have all its files sent to the file consumer.
     *
     * @throws RuntimeException if the directory is not a directory.
     */
    @Override
    public void accept(File directory){
        if(!directory.isDirectory()){
            throw new RuntimeException("File " + directory + " is not a directory!");
        }
        File[] children = directory.listFiles();
        if(children == null){
            throw new RuntimeException("should not occur if directory isDirectory()");
        }
        for(File child : children){
            fileConsumer.accept(child);
        }
    }
}