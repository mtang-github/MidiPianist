package resource;

import java.io.File;

/**
 * A {@code ResourceSystem} provides the interface for loading and accessing file resources.
 */
public class ResourceSystem {
    private final ResourceManagerMap resourceManagerMap;
    private final ResourceLoader resourceLoader;

    /**
     * Constructs a {@code ResourceSystem} capable of interacting with the given set of resource types.
     *
     * @param resourceTypeArray an array representing the set of resource types this {@code ResourceSystem} can handle.
     */
    public ResourceSystem(IResourceType<?>[] resourceTypeArray){
        resourceManagerMap = new ResourceManagerMap(resourceTypeArray);
        resourceLoader = new ResourceLoader(resourceTypeArray, resourceManagerMap);
    }

    /**
     * Loads the resource held in the file represented by the given string as a resource.
     *
     * @param fileName the string name of the file to load.
     */
    public void loadFile(String fileName){
        resourceLoader.loadFile(fileName);
    }

    /**
     * Loads the resource held in the given {@code File}.
     *
     * @param file the {@code File} to load.
     */
    public void loadFile(File file){
        resourceLoader.loadFile(file);
    }

    /**
     * Retrieves the resource manager associated with the given type.
     *
     * @param type the resource type for which it is required to retrieve the associated resource manager.
     * @param <T> the data type.
     * @return the resource manager associated with the given type.
     */
    public <T> IResourceManager<T> getResourceManager(IResourceType<T> type){
        return resourceManagerMap.get(type);
    }

    /**
     * Cleans up all the resources held by this {@code ResourceSystem}.
     */
    public void cleanUp(){
        resourceManagerMap.cleanUp();
    }
}