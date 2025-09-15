package filesmanager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class ImageToBase64Mapper {

    private static Function<Path, String> getFileNameWithoutExtention = (path) -> path.getFileName().toString().substring(0, path.getFileName().toString().lastIndexOf("."));

    public static Map<String, String>  readAndEncodeAllImagesInFolder(String folder) {
        return readAndEncodeAllImagesInFolder(Path.of(folder));
    }

    public static Map<String, String>  readAndEncodeAllImagesInFolder(Path folderPath) {
        Map<String, String> imgMap = new HashMap<>();
        try {
            if (Files.exists(folderPath) && Files.isDirectory(folderPath)) {
                imgMap = Files.list(folderPath)
                        .filter(Files::isRegularFile)
                        .filter(path -> MediaType.IMAGE.isCompatibleType(path.getFileName().toString().toLowerCase()))
                        .collect(Collectors.toMap(path -> path.getFileName().toString(), ImageToBase64Mapper::encodeImageToBase64));
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return imgMap;
    }

    private static String encodeImageToBase64(Path imagePath) {
        String encodeImage = "";
        try {
            encodeImage = Base64.getEncoder().withoutPadding().encodeToString(Files.readAllBytes(imagePath));
        }catch(IOException e) {
            e.printStackTrace();
        }
        return encodeImage;
    }
}
