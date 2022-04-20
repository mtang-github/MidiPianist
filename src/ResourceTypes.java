import resource.IResourceType;
import resource.Resource;
import resource.ResourceLoader;
import util.file.DirectoryConsumer;
import util.file.FileUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code ResourceTypes} class defines the types of file resources which this program can utilize.
 */
public final class ResourceTypes {
    private ResourceTypes() {}

    private static final List<IResourceType<?>> resourceTypesList = new ArrayList<>();

    /**
     * The {@code DIRECTORY} resource type represents file directories. When loading a directory, all child files
     * will be loaded and the resource associated with that directory will hold a list of references to all child
     * resources.
     */
    public static final ResourceTypeTemplate<List<Resource<?>>> DIRECTORY = new ResourceTypeTemplate<>(
            new String[]{FileUtil.DIRECTORY_EXTENSION}
    ) {
        /**
         * Given a directory file and a reference to the {@link ResourceLoader}, load all child files and create
         * a list of references to all child resources.
         *
         * @param file the directory to load.
         * @param loader the {@code ResourceLoader} which will be used to load all child files.
         * @return a list of references to all child resources.
         */
        @Override
        public List<Resource<?>> makeDataFromFile(File file, ResourceLoader loader) {
            List<Resource<?>> childList = new ArrayList<>();
            DirectoryConsumer directoryConsumer = new DirectoryConsumer(
                    file1 -> {
                        Resource<?> child = loader.loadFile(file1);
                        if (child != null) {
                            childList.add(child);
                        }
                    }
            );
            directoryConsumer.accept(file);
            return childList;
        }
    };

    /**
     * The {@code IMAGE} resource type represents 2d images.
     */
    public static final ResourceTypeTemplate<BufferedImage> IMAGE = new ResourceTypeTemplate<>(
            new String[]{"png"}
    ) {

        /**
         * Given an image file, return a {@code BufferedImage} holding the contents of that file.
         *
         * @param file the image file to load.
         * @param loader unused.
         * @return a {@code BufferedImage} holding the contents of the given image file.
         */
        @Override
        public BufferedImage makeDataFromFile(File file, ResourceLoader loader) {
            return FileUtil.parseImage(file);
        }
    };

    /**
     * Returns an array representing the set of resource types.
     * @return an array representing the set of resource types.
     */
    public static IResourceType<?>[] values() {
        return resourceTypesList.toArray(new IResourceType<?>[0]);
    }

    /**
     * A {@code ResourceTypeTemplate} provides an implementation of acceptable file type storage.
     *
     * @param <T> the data type.
     */
    private static abstract class ResourceTypeTemplate<T> implements IResourceType<T> {
        private final String[] acceptableFileTypes;

        /**
         * Constructs a {@code ResourceTypeTemplate} with the given acceptable file types.
         *
         * @param acceptableFileTypes the acceptable file types.
         */
        public ResourceTypeTemplate(String[] acceptableFileTypes) {
            this.acceptableFileTypes = acceptableFileTypes;
            resourceTypesList.add(this);
        }

        /**
         * Returns an array of strings denoting the file types this resource type accepts.
         * @return an array of strings denoting the file types this resource type accepts.
         */
        @Override
        public String[] getAcceptableFileTypes() {
            return acceptableFileTypes;
        }
    }
}