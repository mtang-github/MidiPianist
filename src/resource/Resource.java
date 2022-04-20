package resource;

import java.io.File;

/**
 * A {@code Resource} holds a data object which was loaded from a file, as well as a string id and the file of origin.
 *
 * @param <T> the data type.
 */
public class Resource<T> {
    protected final String id;
    protected final File fileOrigin;
    protected final T data;
    protected final IResourceType<T> type;

    /**
     * Constructs a {@code Resource<T>} with the given id, file, and data.
     *
     * @param id the string id by which this resource will be known as.
     * @param file the file from which this resource was created.
     * @param data the data as loaded from the file.
     * @param type the {@link IResourceType} of the data.
     */
    public Resource(String id, File file, T data, IResourceType<T> type) {
        this.id = id;
        this.fileOrigin = file;
        this.data = data;
        this.type = type;
    }

    /**
     * Returns the string id this resource is associated with.
     * @return the string id this resource is associated with.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the file of origin for this resource.
     * @return the file of origin for this resource.
     */
    public File getFileOrigin() {
        return fileOrigin;
    }

    /**
     * Returns the data held by this resource.
     * @return the data held by this resource.
     */
    public T getData() {
        return data;
    }

    /**
     * Cleans up the data held by this resource if necessary.
     */
    void cleanUpData(){
        if(data != null){
            type.cleanUpData(data);
        }
    }

    @Override
    public String toString() {
        return id;
    }
}