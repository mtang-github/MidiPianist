package resource;

import util.file.FileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ResourceLoader {
    private final FileResourceTypeChooser fileResourceTypeChooser;
    private final ResourceManagerMap resourceManagerMap;

    public ResourceLoader(IResourceType<?>[] resourceTypeArray, ResourceManagerMap resourceManagerMap){
        fileResourceTypeChooser = new FileResourceTypeChooser(resourceTypeArray);
        this.resourceManagerMap = resourceManagerMap;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Resource<?> parseFile(String fileName){
        File file = new File(fileName);
        return parseFile(file);
    }
    public Resource<?> parseFile(File file){
        String fileExtension = FileUtil.getFileExtension(file);
        if(fileResourceTypeChooser.hasMatchingFileExtension(fileExtension)) {
            IResourceType<?> type = fileResourceTypeChooser.getResourceTypeFromFileExtension(fileExtension);
            return addResourceFromFile(type, file);
        }
        return null;
    }
    private <T> Resource<T> addResourceFromFile(IResourceType<T> type, File file){
        Resource<T> resource;
        resource = type.makeResourceFromFile(file, this);
        if(resource == null){
            return null;
        }
        resourceManagerMap.get(type).loadResource(resource);
        return resource;
    }

    private static class FileResourceTypeChooser {
        private final Map<String, IResourceType<?>> fileExtensionToResourceTypeMap;

        public FileResourceTypeChooser(IResourceType<?>[] resourceTypeArray){
            fileExtensionToResourceTypeMap = makeFileExtensionToResourceTypeMap(resourceTypeArray);
        }

        private Map<String, IResourceType<?>> makeFileExtensionToResourceTypeMap(
                IResourceType<?>[] resourceTypeArray
        ){
            Map<String, IResourceType<?>> fileExtensionToResourceTypeMap = new HashMap<>();
            for(IResourceType<?> type: resourceTypeArray){
                for(String extension : type.getAcceptableFileTypes()){
                    if(fileExtensionToResourceTypeMap.containsKey(extension)){
                        throw new RuntimeException("Dual resource types claiming " + extension + " extension: " +
                                fileExtensionToResourceTypeMap.get(extension).getClass().getName() + " and " +
                                type.getClass().getName());
                    }
                    fileExtensionToResourceTypeMap.put(extension, type);
                }
            }
            return fileExtensionToResourceTypeMap;
        }

        public IResourceType<?> getResourceTypeFromFileExtension(String extension){
            return fileExtensionToResourceTypeMap.get(extension);
        }
        public boolean hasMatchingFileExtension(String extension){
            return fileExtensionToResourceTypeMap.containsKey(extension);
        }
    }
}