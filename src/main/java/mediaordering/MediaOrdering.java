package mediaordering;

import filesmanager.FilesAndFoldersManager;
import tools.DisplayConsoleTools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MediaOrdering {

    List<Media> medias;

    public void findMedias(String path, MediaType mediaType) {
        FilesAndFoldersManager manager = new FilesAndFoldersManager(path);
        List<String> files = manager.getContentList("", FilesAndFoldersManager.Type.FILE, true);

        this.medias = files.stream()
                .filter(mediaType::isCompatibleType)
                .map(Media::new)
                .sorted()
                .collect(Collectors.toList());
    }

    public void orderMedias(String path, List<Media> medias) {
        verifyOrderUnicity(medias);

        FilesAndFoldersManager manager = new FilesAndFoldersManager(path);
        medias.stream()
                .filter(Media::toBeRenamed)
                .forEach(media -> {
                    manager.rename(media.getFileTitle(), media.getOrderedTitle(), null);
                });
    }

    private void verifyOrderUnicity(List<Media> medias) {
        Set<Integer> orders = new HashSet<>();
        List<Media> duplicates = medias.stream().filter(Media::isOrdered).filter(media -> !orders.add(media.getOrder())).collect(Collectors.toList());
        if(!duplicates.isEmpty()) {
            throw new RuntimeException("Les médias suivants ont des ordres identiques : " + duplicates.stream().map(Media::getOrderedTitle).collect(Collectors.joining(", ")));
        }
    }

    public void displayMedias() {
        DisplayConsoleTools.displayList(this.medias);
    }

    public List<Media> getMedias() {
        return medias;
    }
}
