package util.file;

import java.io.File;

@SuppressWarnings("unused")
public class ConfigurableDirectoryParser implements IDirectoryParser {
    private IFileParser singleFileParser;
    public ConfigurableDirectoryParser(IFileParser singleFileParser){
        this.singleFileParser = singleFileParser;
    }

    public void parseDirectory(File directory){
        if(!directory.isDirectory()){
            return;
        }
        File[] children = directory.listFiles();
        if(children == null){
            throw new RuntimeException("should not occur if directory isDirectory()");
        }
        for(File child : children){
            singleFileParser.parseFile(child);
        }
    }

    public void setFileParser(IFileParser parser) {
        this.singleFileParser = parser;
    }
}
