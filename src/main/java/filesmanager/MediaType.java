package filesmanager;

import java.util.Arrays;
import java.util.List;

public enum MediaType {
    IMAGE(Arrays.asList(".jpg", ".jpeg", ".png", ".gif")),
    VIDEO(Arrays.asList(".mp4", ".avi", ".wmv", ".mov"));

    private final List<String> extensions;

    MediaType(List<String> extensions) {
        this.extensions = extensions;
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public boolean containsExtension(String extension) {
        return this.extensions.contains(extension);
    }

    public boolean isCompatibleType(String fileName) {
        if(fileName.contains(".")) {
            return this.extensions.contains(fileName.substring(fileName.lastIndexOf(".")).toLowerCase());
        }
        return false;
    }
}
