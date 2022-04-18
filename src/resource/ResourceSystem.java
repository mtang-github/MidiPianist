package resource;

import java.io.File;

public class ResourceSystem {
    private final ResourceManagerMap resourceManagerMap;
    private final ResourceLoader resourceLoader;

    private ResourceSystem(IResourceType<?>[] resourceTypeArray){
        resourceManagerMap = new ResourceManagerMap(resourceTypeArray);
        resourceLoader = new ResourceLoader(resourceTypeArray, resourceManagerMap);
    }

    public static ResourceSystem makeResourceController(IResourceType<?>[] resourceTypeArray){
        return new ResourceSystem(resourceTypeArray);
    }

    public void loadFile(String fileName){
        resourceLoader.parseFile(fileName);
    }

    public void loadFile(File file){
        resourceLoader.parseFile(file);
    }

    public <T> IResourceManager<T> getResourceManager(IResourceType<T> type){
        return resourceManagerMap.get(type);
    }

    public void cleanUp(){
        resourceManagerMap.cleanUp();
    }
}