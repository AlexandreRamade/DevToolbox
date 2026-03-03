package filesmanager;

import listtools.StringListManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileContentManager extends StringListManager {

    private String directory;
    private String fileName;
    private List<String> contentFile;

    private static final String COMPARATIVE_VALUE_KEY = "comparativeValueKey";
    private static final String SEPARATOR = ";";
    private List<String> columns = new ArrayList<>();
    private List<Map<String, String>> contentCSVFile;
    private boolean csvOption = false;
    private boolean ignoreCase = false;

    public FileContentManager(String directory, String fileName) {
        super("");
        this.directory = directory;
        this.fileName = fileName;
        this.contentFile = FilesReader.readAllLinesInFile(directory, fileName);
        setListe(contentFile);
    }

    /**
     * Ecrire l'une des listes du résultat de comparaison dans un fichier
     * @param fileName nom du fichier à créer
     * @return FileContentManager
     */
    public FileContentManager saveResultInFile(String fileName) {
        List<String> listToSave = new ArrayList<>();
        if(csvOption) {
            listToSave.add(columns.stream().collect(Collectors.joining(SEPARATOR)));
        }
        listToSave.addAll(getListe());
        FilesWriter.writeFile(directory, fileName, listToSave, FilesWriter.OVERWRITE_EXISTING_CONTENT);
        return this;
    }

    public FileContentManager saveResultInCSVFileFromOriginalLines(String fileName) {
        List<Map<String, String>> csvDataToSave = getCsvContentFromComparativeValues(getListe(), contentCSVFile);
        FilesWriter.writeCsvFile(directory, fileName, csvDataToSave, FilesWriter.OVERWRITE_EXISTING_CONTENT);
        return this;
    }

    /**
     * Active la comparaison de fichiers au format CSV
     * @param columnTitles titres des colonnes à comparer
     * @return FileContentManager
     */
    public FileContentManager csvFiles(String... columnTitles) {
        return csvFiles(Arrays.asList(columnTitles));
    }

    /**
     * Active la comparaison de fichiers au format CSV
     * @param columnsToCompare liste des titres des colonnes à comparer
     * @return FileContentManager
     */
    public FileContentManager csvFiles(List<String> columnsToCompare) {
        this.contentCSVFile = FilesReader.readCsvFile(directory, fileName);

        completeColumnsToCompareList(columnsToCompare, contentCSVFile.get(0));
        // si aucune colonne n'est précisée pour la comparaison on reste en mode classique
        if(this.columns.isEmpty()) {
            return this;
        }

        csvOption = true;
        generateComparativeValueForEachCsvLine(contentCSVFile);
        this.contentFile = extractComparativeValues(contentCSVFile);
        this.setListe(this.contentFile);

        return this;
    }

    private void completeColumnsToCompareList(List<String> columnsToCompare, Map<String, String> csvLineDatas) {
        columnsToCompare.forEach(columnTitle -> {
            if(csvLineDatas.containsKey(columnTitle)) {
                this.columns.add(columnTitle);
            } else {
                System.err.printf("ATTENTION : la colonne '%s' n'existe pas dans le fichier CSV fourni !%n", columnTitle);
            }
        });
    }

    private void generateComparativeValueForEachCsvLine(List<Map<String, String>> contentCSVFile) {
        contentCSVFile.forEach(csvLineData -> {
            StringBuilder comparativeValue = new StringBuilder();
            columns.forEach(columnTitle -> comparativeValue.append(csvLineData.get(columnTitle)).append(SEPARATOR));
            csvLineData.put(COMPARATIVE_VALUE_KEY, comparativeValue.toString());
        });
    }

    private List<String> extractComparativeValues(List<Map<String, String>> contentCSVFile) {
        return contentCSVFile.stream().map(csvLineData -> csvLineData.get(COMPARATIVE_VALUE_KEY)).collect(Collectors.toList());
    }

    private List<Map<String, String>> getCsvContentFromComparativeValues(List<String> comparativeValues, List<Map<String, String>> contentCSVFile) {
        if(ignoreCase) {
            contentCSVFile.forEach(csvLineData -> csvLineData.put(COMPARATIVE_VALUE_KEY, csvLineData.get(COMPARATIVE_VALUE_KEY).toLowerCase()));
        }
        return contentCSVFile.stream()
                .filter(csvLineData -> comparativeValues.contains(csvLineData.get(COMPARATIVE_VALUE_KEY)))
                .map(this::removeComparativeValueKey)
                .collect(Collectors.toList());
    }

    private Map<String, String> removeComparativeValueKey(Map<String, String> csvLineData) {
        csvLineData.remove(COMPARATIVE_VALUE_KEY);
        return csvLineData;
    }
}
