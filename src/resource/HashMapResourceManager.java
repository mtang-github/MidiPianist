package resource;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@code HashMapResourceManager} implements {@link IResourceManager} by using a backing {@link HashMap}.
 *
 * @param <T> the type of the data stored by the resources.
 */
class HashMapResourceManager<T> implements IResourceManager<T> {
    private final Map<String, Resource<T>> resourceMap;

    /**
     * Constructs an empty {@code HashMapResourceManager}.
     */
    public HashMapResourceManager(){
        resourceMap = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     * @param id the string id for which it is required to find the associated resource.
     * @return the resource associated with the given string id.
     */
    @Override
    public Resource<T> getResource(String id) {
        return resourceMap.get(id);
    }

    /**
     * {@inheritDoc}
     * @param resource the resource to add to this manager.
     */
    @Override
    public void loadResource(Resource<T> resource) {
        resourceMap.put(resource.getId(), resource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cleanUp() {
        for(Resource<T> resource : resourceMap.values()){
            resource.cleanUpData();
        }
    }
}
