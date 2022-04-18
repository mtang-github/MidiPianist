package resource;

import util.file.FileUtil;

import java.io.File;

/**
 * An {@code IResourceType} represents a type of resource which can be loaded from a file.
 *
 * @param <T> the type of the data.
 */
public interface IResourceType<T> {

    /**
     * Returns an array of strings denoting the file types this {@code IResourceType} accepts.
     * @return an array of strings denoting the file types this {@code IResourceType} accepts.
     */
    default String[] getAcceptableFileTypes(){
        return new String[0];
    }

    /**
     * Creates a new {@code Resource<T>} holding the data parsed from the given file.
     *
     * @param file the file to load the resource from.
     * @param loader the {@link ResourceLoader} which may be used for certain resource types.
     * @return a new {@code Resource<T>} holding the data parsed from the given file.
     */
    default Resource<T> makeResourceFromFile(File file, ResourceLoader loader){
        T data = makeDataFromFile(file, loader);
        if(data == null){
            return null;
        }
        String id = FileUtil.getFileName(file);
        return constructResource(id, file, data);
    }

    /**
     * Loads and returns the data represented by the given file
     *
     * @param file the file to load the data from.
     * @param loader the {@link ResourceLoader} which may be used for certain resource types.
     * @return the data loaded from the given file.
     */
    T makeDataFromFile(File file, ResourceLoader loader);

    /**
     * Creates a new {@code Resource<T>} with the given id, file, and data.
     *
     * @param id the string id by which this resource will be known as.
     * @param file the file from which this resource was created.
     * @param data the data as loaded from the file.
     * @return a new {@code Resource<T>} with the given id, file, and data.
     */
    default Resource<T> constructResource(String id, File file, T data){
        return new Resource<>(id, file, data, this);
    }

    /**
     * Cleans up the given data object if necessary for this resource type.
     *
     * @param data the data object to clean up.
     */
    void cleanUpData(T data);
}