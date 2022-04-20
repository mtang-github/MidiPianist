package resource;

/**
 * An {@code IResourceManager} stores resources and provides the ability to retrieve resources by string id.
 *
 * @param <T> the data type stored by the resources.
 */
public interface IResourceManager<T> {

    /**
     * Retrieves the resource associated with the given string id.
     * @param id the string id for which it is required to find the associated resource.
     * @return the resource associated with the given string id.
     */
    Resource<T> getResource(String id);

    /**
     * Adds the specified resource to this resource manager.
     *
     * @param resource the resource to add to this manager.
     */
    void loadResource(Resource<T> resource);

    /**
     * Cleans up all the data held by this manager, if required.
     */
    void cleanUp();
}