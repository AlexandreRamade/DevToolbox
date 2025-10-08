package filesmanager.mediaordering;

import filesmanager.FilesAndFoldersManager;
import filesmanager.ImageToBase64Mapper;
import filesmanager.MediaType;

import java.util.*;
import java.util.stream.Collectors;

public class MediasOrdering {

    public static List<Media> findMedias(String folderPath, MediaType mediaType) {
        FilesAndFoldersManager manager = new FilesAndFoldersManager(folderPath);
        List<String> files = manager.getContentList("", FilesAndFoldersManager.Type.FILE, true);
        Map<String, String> imgMap = ImageToBase64Mapper.readAndEncodeAllImagesInFolder(folderPath);

        return files.stream()
                .filter(mediaType::isCompatibleType)
                .map(mediaFileTitle -> new Media(mediaFileTitle, imgMap.get(mediaFileTitle)))
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

    public static void applyOrderToFilesAndDeleteUnorderedFiles(String path, List<Media> medias, int digitsNumber) {
        List<Media> orderedMedias = deleteUnorderedFiles(path, medias);
        applyOrderToFiles(path, orderedMedias, digitsNumber);
    }

    public static void applyOrderToFiles(String path, List<Media> medias, int digitsNumber) {
        verifyOrderUnicity(medias);

        FilesAndFoldersManager manager = new FilesAndFoldersManager(path);
        medias.stream()
                .filter(Media::toBeRenamed)
                .forEach(media -> {
                    manager.rename(media.getFileTitle(), media.getOrderedTitle(digitsNumber), null);
                });
    }

    public static List<Media> deleteUnorderedFiles(String path, List<Media> medias) {
        FilesAndFoldersManager manager = new FilesAndFoldersManager(path);
        medias.stream()
                .filter(m -> !m.isOrdered())
                .forEach(media -> manager.deleteFile(media.getFileTitle(), null));

        return medias.stream().filter(Media::isOrdered).collect(Collectors.toList());
    }

    private static void verifyOrderUnicity(List<Media> medias) {
        Set<Integer> orders = new HashSet<>();
        List<Media> duplicates = medias.stream().filter(Media::isOrdered).filter(media -> !orders.add(media.getOrder())).collect(Collectors.toList());
        if(!duplicates.isEmpty()) {
            throw new RuntimeException("Les médias suivants ont des ordres identiques : " + duplicates.stream().map(Media::getOrderedTitle).collect(Collectors.joining(", ")));
        }
    }

}
