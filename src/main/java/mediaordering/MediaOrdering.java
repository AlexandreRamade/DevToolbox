package mediaordering;

import filesmanager.FilesAndFoldersManager;
import stringtools.Increment;
import tools.DisplayConsoleTools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MediaOrdering {

    public static List<Media> findMedias(String path, MediaType mediaType) {
        FilesAndFoldersManager manager = new FilesAndFoldersManager(path);
        List<String> files = manager.getContentList("", FilesAndFoldersManager.Type.FILE, true);

        return files.stream()
                .filter(mediaType::isCompatibleType)
                .map(Media::new)
                .sorted()
                .collect(Collectors.toList());
    }

    public static void orderMedias(int firstIndex, List<Media> medias) {
        for(Media media : medias) {
            media.setOrder(firstIndex++);
        }
    }

    public static void removeOrder(List<Media> medias) {
        medias.forEach(Media::removeOrder);
        Collections.sort(medias);
    }

    public static void applyOrderToFiles(String path, List<Media> medias) {
        verifyOrderUnicity(medias);

        FilesAndFoldersManager manager = new FilesAndFoldersManager(path);
        medias.stream()
                .filter(Media::toBeRenamed)
                .forEach(media -> {
                    manager.rename(media.getFileTitle(), media.getOrderedTitle(), null);
                });
    }

    private static void verifyOrderUnicity(List<Media> medias) {
        Set<Integer> orders = new HashSet<>();
        List<Media> duplicates = medias.stream().filter(Media::isOrdered).filter(media -> !orders.add(media.getOrder())).collect(Collectors.toList());
        if(!duplicates.isEmpty()) {
            throw new RuntimeException("Les médias suivants ont des ordres identiques : " + duplicates.stream().map(Media::getOrderedTitle).collect(Collectors.joining(", ")));
        }
    }

}
