package filesmanager;

import stringtools.Increment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * FilesAndFoldersManager<br>
 * <p>
 * Classe permettant de modifier les noms et les emplacements de fichiers et de
 * dossiers
 * </p>
 *
 * @author a-ramade
 * @since 09/2022
 *
 */
public class FilesAndFoldersManager {

    /** Adresse du dossier principal */
    private static String MAIN_FOLDER_PATH = "C:\\Users\\a-ramade\\Desktop\\TEST";

    public void setMainFolderPath(String mainFolderPath) {
        MAIN_FOLDER_PATH = mainFolderPath;
    }

    /**
     * Type<br>
     * <p>
     * Défini le type d'élément à modifier
     * </p>
     * <div>
     * <ul>
     * <li>FOLDER</li>
     * <li>FILE</li>
     * </ul>
     * </div>
     *
     * @author a-ramade
     *
     */
    public enum Type {
        FOLDER, FILE;
    }

    /* ***** ***** ***** ***** CONSTRUCTEURS ***** ***** ***** ***** */
    /**
     * Constructeur par défaut
     */
    public FilesAndFoldersManager() {
        super();
    }

    /**
     * Constructeur avec adresse du dossier principal
     *
     * @param mainFolderPath
     *            adresse du dossier principal
     */
    public FilesAndFoldersManager(String mainFolderPath) {
        super();
        MAIN_FOLDER_PATH = mainFolderPath;
    }

    /* ***** ***** ***** ***** FONCTIONS UTILITAIRES ***** ***** ***** ***** */

    /**
     * isEmptyOrBlank<br>
     * <p>
     * Prédicat utilitaire renvoyant true si la chaine est nulle ou vide ou
     * contenant uniquement des caractères blancs
     * </p>
     */
    private static Predicate<String> isEmptyOrBlank = (str) -> str == null || str.trim().isEmpty();

    /**
     * getAbsolutePath<br>
     * <p>
     * Concatène l'adresse du dossier principal, l'adresse relative et le nom du
     * fichier permettant de créer le Path complet de l'élément
     * </p>
     */
    private static BiFunction<String, String, Path> getAbsolutePath = (folderOrFileName, relativePath) -> {
        StringBuilder absolutePath = new StringBuilder(MAIN_FOLDER_PATH);
        if (!isEmptyOrBlank.test(relativePath)) {
            absolutePath.append("\\").append(relativePath);
        }
        if (!isEmptyOrBlank.test(folderOrFileName)) {
            absolutePath.append("\\").append(folderOrFileName);
        }
        return Paths.get(absolutePath.toString());
    };

    /**
     * getExtension<br>
     * <p>
     * Retourne l'extention d'un fichier à partir de son nom
     * </p>
     */
    private static Function<String, String> getExtension = (pathOrFileName) -> pathOrFileName
            .substring(pathOrFileName.lastIndexOf("."));

    /**
     * relativePathExists<br>
     * <p>
     * Teste la validité d'une adresse relative
     * </p>
     */
    public static BiPredicate<String, Type> relativePathExists = (relativePath, type) -> {
        Path completePath = getAbsolutePath.apply(null, relativePath);
        return Files.exists(completePath)
                && (type == Type.FOLDER ? Files.isDirectory(completePath) : Files.isRegularFile(completePath));
    };

    /* ***** ***** ***** ***** METHODES ***** ***** ***** ***** */

    /**
     * createFolder<br>
     * <p>
     * Créé un dossier
     * </p>
     * <div>
     * <ul>
     * <li>vérifie la validité de l'emplacement cible</li>
     * <li>vérifie que le dossier n'existe pas déjà à cet emplacement</li>
     * <li>créé le nouveau dossier</li>
     * </ul>
     * </div>
     *
     * @param folderName
     *            nom du dossier à créer
     * @param relativePath
     *            adresse relative
     * @return this
     */
    public FilesAndFoldersManager createFolder(String folderName, String relativePath) {
        try {
            testRelativesPaths(relativePath, null);

            Path newFolderPath = getAbsolutePath.apply(folderName, relativePath);
            if (Files.notExists(newFolderPath)) {
                Files.createDirectory(newFolderPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public FilesAndFoldersManager createFile(String folderName, String relativePath) {
        try {
            testRelativesPaths(relativePath, null);

            Path newFilePath = getAbsolutePath.apply(folderName, relativePath);
            if (Files.notExists(newFilePath)) {
                Files.createFile(newFilePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * getContentList<br>
     * <p>
     * Retourne la liste des éléments contenue dans un dossier
     * </p>
     *
     * @param relativePathSource
     *            emplacement source relatif
     * @param type
     *            {@link Type#FILE} ou {@link Type#FOLDER}
     * @param showExtension
     *            affiche l'extension des fichier si true
     * @return la liste des éléments
     */
    public List<String> getContentList(String relativePathSource, Type type, boolean showExtension) {
        List<String> contentList = new ArrayList<>();
        try {
            testRelativesPaths(relativePathSource, null);
            Path folderPath = getAbsolutePath.apply(null, relativePathSource);

            // maxDepth = 1 => ne traverse que les éléments dans le dossier source (sans
            // récursivité)
            try (Stream<Path> paths = Files.walk(folderPath, 1)) {
                // filtre les path en fonction du type d'éléments à modifier
                contentList.addAll(paths
                        .filter(path -> Type.FOLDER == type ? (Files.isDirectory(path) && !folderPath.equals(path))
                                : Files.isRegularFile(path))
                        .map(path -> path.getFileName().toString()).collect(Collectors.toList()));

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return (Type.FILE == type && !showExtension) ? contentList.stream()
                .map(fileName -> fileName.substring(0, fileName.lastIndexOf("."))).collect(Collectors.toList())
                : contentList;
    }

    /**
     * copyAllFilesToFolder<br>
     * <p>
     * Créé une copie de tous les fichiers d'un emplacement source dans un
     * emplacement cible
     * </p>
     *
     * @param relativePathSource
     *            emplacement source relatif
     * @param relativePathTarget
     *            emplacement cible relatif
     * @param activateReplaceExistingOption
     *            détermine si l'option écraser les fichers existant doit être
     *            activée
     * @return this
     */
    public FilesAndFoldersManager copyAllFilesToFolder(String relativePathSource, String relativePathTarget,
                                                       boolean activateReplaceExistingOption) {
        try {
            testRelativesPaths(relativePathSource, relativePathTarget);

            try (Stream<Path> paths = Files.walk(getAbsolutePath.apply(null, relativePathSource), 1)) {
                paths.filter(Files::isRegularFile).forEach(filePath -> {
                    try {
                        if (activateReplaceExistingOption) {
                            Files.copy(filePath,
                                    getAbsolutePath.apply(filePath.getFileName().toString(), relativePathTarget),
                                    StandardCopyOption.REPLACE_EXISTING);
                        } else {
                            Files.copy(filePath,
                                    getAbsolutePath.apply(filePath.getFileName().toString(), relativePathTarget));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * moveAllFilesToFolder<br>
     * <p>
     * Déplace tous les fichiers d'un emplacement source vers un emplacement cible
     * </p>
     *
     * @param relativePathSource
     *            emplacement source relatif
     * @param relativePathTarget
     *            emplacement cible relatif
     * @return this
     */
    public FilesAndFoldersManager moveAllFilesToFolder(String relativePathSource, String relativePathTarget) {
        this.filesMoveTo((fileName) -> fileName, relativePathSource, relativePathTarget, Type.FILE);
        return this;
    }

    /**
     * addIncrementation<br>
     * <p>
     * Ajoute une incrémentation à tous les noms des fichiers ou des dossiers
     * </p>
     * <u>la valeur de l'itération est obligatoirement placé au début du nom de
     * l'élément</u>
     *
     * @param firstStepOfIncrement
     *            première valeur de l'itération
     * @param suffixSeparator
     *            séparateur
     * @param relativePathSource
     *            emplacement source relatif
     * @return this
     *
     * @see {@link Increment}
     */
    public FilesAndFoldersManager addIncrementation(String firstStepOfIncrement, String suffixSeparator,
                                                    String relativePathSource, Type type) {
        Increment incr = new Increment(firstStepOfIncrement);
        this.filesMoveTo((fileName) -> incr.increment("", "", suffixSeparator, fileName, ""), relativePathSource,
                relativePathSource, type);
        return this;
    }

    /**
     * renameAndIncrement<br>
     * <p>
     * Renome et ajoute une incrémentation à tous les noms des fichiers ou des
     * dossiers
     * </p>
     *
     * @param prefix
     *            préfixe
     * @param prefixSeparator
     *            séparateur entre le préfixe et la valeur de l'itération
     * @param firstStepOfIncrement
     *            première valeur de l'itération
     * @param suffixSeparator
     *            séparateur entre le sufixe et la valeur de l'itération
     * @param suffix
     *            sufixe
     * @param relativePathSource
     *            emplacement source relatif
     * @param type
     *            {@link Type#FILE} ou {@link Type#FOLDER}
     * @return this
     *
     * @see {@link Increment}
     */
    public FilesAndFoldersManager renameAndIncrement(String prefix, String prefixSeparator, String firstStepOfIncrement,
                                                     String suffixSeparator, String suffix, String relativePathSource, Type type) {
        Increment incr = new Increment(firstStepOfIncrement);
        this.filesMoveTo(
                (fileName) -> incr.increment(prefix, prefixSeparator, suffixSeparator, suffix,
                        Type.FILE == type ? getExtension.apply(fileName) : ""),
                relativePathSource, relativePathSource, type);
        return this;
    }

    /**
     * replacePatternInName<br>
     * <p>
     * Remplace ou supprime un motif dans les noms des fichiers ou des dossiers
     * </p>
     *
     * @param patternToReplace
     *            motif à remplacer
     * @param replacementValue
     *            valeur de remplacement
     * @param relativePathSource
     *            emplacement source relatif
     * @param type
     *            {@link Type#FILE} ou {@link Type#FOLDER}
     * @return this
     */
    public FilesAndFoldersManager replacePatternInName(String patternToReplace, String replacementValue,
                                                       String relativePathSource, Type type) {
        Pattern pattern = Pattern.compile(patternToReplace);
        this.filesMoveTo((fileName) -> pattern.matcher(fileName).replaceAll(replacementValue), relativePathSource,
                relativePathSource, type);
        return this;
    }

    /* ***** ***** ***** ***** METHODES METIERS ***** ***** ***** ***** */

    private void testRelativesPaths(String relativePathSource, String relativePathTarget) throws IOException {
        if (!relativePathExists.test(relativePathSource, Type.FOLDER)) {
            throw new IOException(String.format("The path '%s\\%s' doesn't exist or isn't a folder.", MAIN_FOLDER_PATH,
                    relativePathSource));
        }
        // teste la cible si != de la source (= vrai déplacement)
        if (Objects.nonNull(relativePathTarget) && !relativePathSource.equals(relativePathTarget)
                && !relativePathExists.test(relativePathTarget, Type.FOLDER)) {
            throw new IOException(String.format("The path '%s\\%s' doesn't exist or isn't a folder.", MAIN_FOLDER_PATH,
                    relativePathTarget));
        }
    }

    /**
     * filesMoveTo<br>
     * <p>
     * Méthode utilisant {@link Files#move(Path, Path, java.nio.file.CopyOption...)}
     * permettant de déplacer ou de renomer les fichier et les dossiers
     * </p>
     * <div>
     * <ul>
     * <li>teste la validité de l'emplacement source</li>
     * <li>teste la validité de l'emplacement cible si nécéssaire</li>
     * <li>récupère le path de tous les éléments dans cet emplacement
     * <li>
     * <li>filtre ces éléments en fonction du type à modifier</li>
     * <li>applique la modification (renomage ou déplacement)</li>
     * </ul>
     * </div>
     *
     * @param getNewFileName
     *            fonction de traitement a appliquer sur le nom de l'élément
     * @param relativePathSource
     *            emplacement source relatif
     * @param relativePathTarget
     *            emplacement cible relatif
     * @param type
     *            {@link Type#FILE} ou {@link Type#FOLDER}
     */
    private void filesMoveTo(Function<String, String> getNewFileName, String relativePathSource,
                             String relativePathTarget, Type type) {
        try {
            testRelativesPaths(relativePathSource, relativePathTarget);

            Path folderPath = getAbsolutePath.apply(null, relativePathSource);

            // maxDepth = 1 => ne traverse que les éléments dans le dossier source (sans
            // récursivité)
            try (Stream<Path> paths = Files.walk(folderPath, 1)) {
                // filtre les path en fonction du type d'éléments à modifier
                paths.filter(path -> Type.FOLDER == type ? (Files.isDirectory(path) && !folderPath.equals(path))
                        : Files.isRegularFile(path)).forEach(filePath -> {
                    try {
                        // applique la fonction de transformation du nom de l'élément
                        // et genère le nouveau path avec le nom transformé
                        Files.move(filePath, getAbsolutePath.apply(
                                getNewFileName.apply(filePath.getFileName().toString()), relativePathTarget));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

