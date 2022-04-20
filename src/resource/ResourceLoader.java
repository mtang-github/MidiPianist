package resource;

import util.file.FileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@code ResourceLoader} loads file resources into a {@link ResourceManagerMap}.
 */
public class ResourceLoader {
    private final FileResourceTypeChooser fileResourceTypeChooser;
    private final ResourceManagerMap resourceManagerMap;

    /**
     * Constructs a {@code ResourceLoader} capable of loading the given resource types into the given
     * {@link ResourceManagerMap}.
     *
     * @param resourceTypeArray an array of the resource types this {@code ResourceLoader} is capable of loading.
     * @param resourceManagerMap the {@code ResourceManagerMap} into which resources will be loaded.
     */
    public ResourceLoader(IResourceType<?>[] resourceTypeArray, ResourceManagerMap resourceManagerMap){
        fileResourceTypeChooser = new FileResourceTypeChooser(resourceTypeArray);
        this.resourceManagerMap = resourceManagerMap;
    }

    /**
     * Loads the resource held in the file represented by the given string as a resource.
     *
     * @param fileName the string name of the file to load.
     * @return the {@link Resource} object holding the data loaded from the specified file.
     */
    @SuppressWarnings("UnusedReturnValue")
    public Resource<?> loadFile(String fileName){
        File file = new File(fileName);
        return loadFile(file);
    }

    /**
     * Loads the resource held in the given {@code File}.
     *
     * @param file the {@code File} to load.
     * @return the {@link Resource} object holding the data loaded from the specified file.
     */
    public Resource<?> loadFile(File file){
        String fileExtension = FileUtil.getFileExtension(file);
        if(fileResourceTypeChooser.hasMatchingFileExtension(fileExtension)) {
            IResourceType<?> type = fileResourceTypeChooser.getResourceTypeFromFileExtension(fileExtension);
            return addResourceFromFile(type, file);
        }
        return null;
    }

    /**
     * Creates a new {@link Resource} according to the given {@code File} and type and puts that {@code Resource} into
     * the {@link ResourceManagerMap} associated with this {@code ResourceLoader}.
     *
     * @param type the resource type represented inside the given {@code File}.
     * @param file file the {@code File} to load.
     * @param <T> the data type.
     * @return the {@link Resource} object holding the data loaded from the specified file.
     */
    private <T> Resource<T> addResourceFromFile(IResourceType<T> type, File file){
        Resource<T> resource;
        resource = type.makeResourceFromFile(file, this);
        if(resource == null){
            return null;
        }
        resourceManagerMap.get(type).loadResource(resource);
        return resource;
    }

    /**
     * A {@code FileResourceTypeChooser} is capable of determining which resource type is represented by a given
     * file extension.
     */
    private static class FileResourceTypeChooser {
        private final Map<String, IResourceType<?>> fileExtensionToResourceTypeMap;

        /**
         * Constructs a {@code FileResourceTypeChooser} which is capable of choosing from the given set of resource
         * types.
         *
         * @param resourceTypeArray an array representing the set of resource types to choose from.
         */
        public FileResourceTypeChooser(IResourceType<?>[] resourceTypeArray){
            fileExtensionToResourceTypeMap = makeFileExtensionToResourceTypeMap(resourceTypeArray);
        }

        /**
         * Creates a file extension to resource type map from the given set of resource types.
         *
         * @param resourceTypeArray an array representing the set of resource types to choose from.
         * @return a file extension to resource type map from the given set of resource types.
         */
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

        /**
         * Returns the resource type associated with the given file extension.
         *
         * @param extension the file extension for which it is required to retrieve the associated resource type.
         * @return the resource type associated with the given file extension.
         */
        public IResourceType<?> getResourceTypeFromFileExtension(String extension){
            return fileExtensionToResourceTypeMap.get(extension);
        }

        /**
         * Returns {@code true} if there is a resource type associated with the given file extension, {@code false}
         * otherwise.
         *
         * @param extension the file extension for which it is required to check whether there is an associated
         *                  resource type.
         * @return {@code true} if there is a resource type associated with the given file extension, {@code false}
         * otherwise.
         */
        public boolean hasMatchingFileExtension(String extension){
            return fileExtensionToResourceTypeMap.containsKey(extension);
        }
    }
}