import resource.AbstractResourceOrigin;
import resource.IResourceType;
import resource.FileOrigin;
import resource.ParentResource;
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
//todo docs
public final class ResourceTypes {
    private ResourceTypes() {}

    private static final List<IResourceType<?>> resourceTypesList = new ArrayList<>();

    public static final ResourceTypeTemplate<List<Resource<?>>> DIRECTORY = new ResourceTypeTemplate<>(
            new String[]{FileUtil.DIRECTORY_EXTENSION}
    ) {
        @Override
        public List<Resource<?>> makeDataFromFile(FileOrigin origin, ResourceLoader loader) {
            return makeData(origin.getFile(), loader);
        }

        private List<Resource<?>> makeData(File file, ResourceLoader loader) {
            List<Resource<?>> childList = new ArrayList<>();
            DirectoryConsumer directoryConsumer = new DirectoryConsumer(
                    file1 -> {
                        Resource<?> child = loader.parseFile(file1);
                        if (child != null) {
                            childList.add(child);
                        }
                    }
            );
            directoryConsumer.accept(file);
            return childList;
        }

        @Override
        public Resource<List<Resource<?>>> constructResource(String id, AbstractResourceOrigin origin, List<Resource<?>> childList) {
            return new ParentResource(id, origin, childList, this);
        }

        @Override
        public boolean acceptsLoader() {
            return true;
        }
    };

    public static final ResourceTypeTemplate<BufferedImage> IMAGE = new ResourceTypeTemplate<>(
            new String[]{"png"}
    ) {
        @Override
        public BufferedImage makeDataFromFile(FileOrigin origin) {
            return makeData(origin.getFile());
        }

        private BufferedImage makeData(File file){
            return FileUtil.parseImage(file);
        }
    };

    public static IResourceType<?>[] values() {
        return resourceTypesList.toArray(new IResourceType<?>[0]);
    }

    private static abstract class ResourceTypeTemplate<T> implements IResourceType<T> {
        private final String[] acceptableFileTypes;

        public ResourceTypeTemplate(String[] acceptableFileTypes) {
            this.acceptableFileTypes = acceptableFileTypes;
            resourceTypesList.add(this);
        }

        @Override
        public String[] getAcceptableFileTypes() {
            return acceptableFileTypes;
        }
    }
}