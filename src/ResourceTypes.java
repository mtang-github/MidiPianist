import resource.AbstractResourceOrigin;
import resource.AbstractResourceType;
import resource.FileOrigin;
import resource.ManifestOrigin;
import resource.ParentResource;
import resource.Resource;
import resource.ResourceLoader;
import util.file.ConfigurableDirectoryParser;
import util.file.FileUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class ResourceTypes {
    private ResourceTypes() {}

    private static final List<AbstractResourceType<?>> resourceTypesList = new ArrayList<>();

    public static final ResourceTypeTemplate<List<Resource<?>>> DIRECTORY = new ResourceTypeTemplate<>(
            new String[]{FileUtil.DIRECTORY_EXTENSION},
            new String[]{"directory"}
    ) {
        @Override
        public List<Resource<?>> makeDataFromFile(FileOrigin origin, ResourceLoader loader) {
            return makeData(origin.getFile(), loader);
        }

        @Override
        protected List<Resource<?>> makeDataFromManifest(ManifestOrigin origin, ResourceLoader loader) {
            return makeData(new File(origin.getMetadata()[2]), loader);
        }

        private List<Resource<?>> makeData(File file, ResourceLoader loader) {
            List<Resource<?>> childList = new ArrayList<>();
            ConfigurableDirectoryParser configurableDirectoryParser = new ConfigurableDirectoryParser(
                    file1 -> {
                        Resource<?> child = loader.parseFile(file1);
                        if (child != null) {
                            childList.add(child);
                        }
                    }
            );
            configurableDirectoryParser.parseDirectory(file);
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
            new String[]{"png"},
            new String[]{"image"}
    ) {
        @Override
        public BufferedImage makeDataFromFile(FileOrigin origin) {
            return makeData(origin.getFile());
        }

        @Override
        protected BufferedImage makeDataFromManifest(ManifestOrigin origin) {
            return makeData(new File(origin.getMetadata()[2]));
        }

        private BufferedImage makeData(File file){
            return FileUtil.parseImage(file);
        }
    };

    public static AbstractResourceType<?>[] values() {
        return resourceTypesList.toArray(new AbstractResourceType<?>[0]);
    }

    private static class ResourceTypeTemplate<T> extends resource.AbstractResourceType<T> {
        private final String[] acceptableFileTypes;
        private final String[] acceptableManifestPrefixes;

        public ResourceTypeTemplate(String[] acceptableFileTypes, String[] acceptableManifestPrefixes) {
            this.acceptableFileTypes = acceptableFileTypes;
            this.acceptableManifestPrefixes = acceptableManifestPrefixes;
            resourceTypesList.add(this);
        }

        @Override
        public String[] getAcceptableFileTypes() {
            return acceptableFileTypes;
        }

        @Override
        public String[] getAcceptableManifestPrefixes() {
            return acceptableManifestPrefixes;
        }
    }
}