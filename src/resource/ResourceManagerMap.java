package resource;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@code ResourceManagerMap} provides access to the resource manager associated with a given resource type.
 *
 * @see ResourceManagerMap
 */
class ResourceManagerMap {
    private final Map<IResourceType<?>, IResourceManager<?>> innerResourceManagerMap;

    /**
     * Constructs a {@code ResourceManagerMap} with a resource manager for each given resource type.
     *
     * @param resourceTypeArray an array representing the set of resource types this {@code ResourceManagerMap} needs
     *                          to provide a resource manager for.
     *
     * @see ResourceManagerMap
     */
    public ResourceManagerMap(IResourceType<?>[] resourceTypeArray) {
        innerResourceManagerMap = new HashMap<>();
        for (IResourceType<?> type : resourceTypeArray) {
            addNewType(type);
        }
    }

    /**
     * Adds a new resource manager for the given resource type to this {@code ResourceManagerMap}.
     *
     * @param type the resource type for which it is required to add a new resource manager.
     * @param <T> the data type.
     *
     * @throws RuntimeException if the given type already has an associated resource manager.
     */
    private <T> void addNewType(IResourceType<T> type) {
        if(innerResourceManagerMap.containsKey(type)){
            throw new RuntimeException("Already contains type " + type);
        }
        innerResourceManagerMap.put(type, new HashMapResourceManager<T>());
    }

    /**
     * Retrieves the resource manager associated with the given type.
     *
     * @param type the resource type for which it is required to retrieve the associated resource manager.
     * @param <T> the data type.
     * @return the resource manager associated with the given type.
     */
    @SuppressWarnings("unchecked")
    public <T> IResourceManager<T> get(IResourceType<T> type) {
        return (IResourceManager<T>) innerResourceManagerMap.get(type);
    }

    /**
     * Cleans up all the resources held by each resource manager contained in this {@code ResourceManagerMap}.
     */
    void cleanUp(){
        for(Map.Entry<IResourceType<?>, IResourceManager<?>> entry : innerResourceManagerMap.entrySet()){
            entry.getValue().cleanUp();
        }
    }
}
