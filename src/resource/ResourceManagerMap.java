package resource;

import java.util.HashMap;
import java.util.Map;

class ResourceManagerMap {
    //we can probably do better than this - but getting the map is not an important operation
    private final Map<IResourceType<?>, IResourceManager<?>> innerResourceManagerMap;

    public ResourceManagerMap() {
        innerResourceManagerMap = new HashMap<>();
    }
    public ResourceManagerMap(IResourceType<?>[] resourceTypeArray) {
        innerResourceManagerMap = new HashMap<>();
        for (IResourceType<?> type : resourceTypeArray) {
            addNewType(type);
        }
    }

    //why did I separate these into methods?
    public <T> void addNewType(IResourceType<T> type) {
        put(type, new HashMapResourceManager<>());
    }
    private <T> void put(IResourceType<T> type, IResourceManager<T> manager) {
        innerResourceManagerMap.put(type, manager);
    }
    @SuppressWarnings("unchecked")
    public <T> IResourceManager<T> get(IResourceType<T> type) {
        return (IResourceManager<T>) innerResourceManagerMap.get(type);
    }
    public void remove(IResourceType<?> type) {
        innerResourceManagerMap.remove(type);
    }

    void cleanUp(){
        for(Map.Entry<IResourceType<?>, IResourceManager<?>> entry : innerResourceManagerMap.entrySet()){
            if(entry.getKey().requiresCleanUp()){
                entry.getValue().cleanUp();
            }
        }
    }
}
