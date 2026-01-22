package filesmanager;

import listtools.ComparatorListManager;
import listtools.StringListManager;
import stringtools.StringTools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FilesComparator {
    private String directory;
    private String fileName1;
    private String fileName2;
    private List<String> contentFile1;
    private List<String> contentFile2;
    private ComparatorListManager comparatorListManager;

    public FilesComparator(String directory, String fileName1, String fileName2) {
        this.directory = directory;
        this.fileName1 = fileName1;
        this.fileName2 = fileName2;
        this.contentFile1 = FilesReader.readAllLinesInFile(directory, fileName1);
        this.contentFile2 = FilesReader.readAllLinesInFile(directory, fileName2);
    }

    public FilesComparator compareFiles() {
        compareFiles(contentFile1, contentFile2);
        return this;
    }

    /**
     * ATTENTION : méthode à personnaliser
     */
    public FilesComparator compareFilesApresTraitement() {
        compareFiles(traitementPersonnalise(this.contentFile1), traitementPersonnalise(this.contentFile2));
        return this;
    }

    private List<String> traitementPersonnalise(List<String> liste) {
        StringListManager manager = new StringListManager(liste);
        // traitement personnalisé à compléter ici
        manager
                //.extraireVariableUneSeuleFoisParLigne("(^.+?;.+?;.+?;.+?);.*");
                .traitementPersonnalise(str -> str.substring(0, StringTools.indexOfNthOccurrence(";", 4, str)))
                .supprimerDoublons()
                .traitementPersonnalise(String::toLowerCase);
        //fin du traitement personnalisé
        return manager.getListe();
    }

    public void testerTraitementPersonnalise() {
        List<String> listeTraitee = traitementPersonnalise(this.contentFile1);
        System.out.println(String.format("Nombre d'éléments dans la liste avant traitement = %d", this.contentFile1.size()));
        System.out.println(String.format("Nombre d'éléments dans la liste après traitement = %d", listeTraitee.size()));
        System.out.println("\n ----------------------- ");
        System.out.println("Contenu de la liste traitée :");
        listeTraitee.forEach(System.out::println);
    }

    public FilesComparator compareFilesSansDoublons() {
        StringListManager manager1 = new StringListManager(fileName1);
        manager1.supprimerDoublons();
        StringListManager manager2 = new StringListManager(fileName2);
        manager2.supprimerDoublons();
        compareFiles(manager1.getListe(), manager2.getListe());
        return this;
    }

    public FilesComparator compareFilesIgnoringCase() {
        StringListManager manager1 = new StringListManager(fileName1);
        manager1.traitementPersonnalise(String::toLowerCase);
        StringListManager manager2 = new StringListManager(fileName2);
        manager2.traitementPersonnalise(String::toLowerCase);
        compareFiles(manager1.getListe(), manager2.getListe());
        return this;
    }

    public FilesComparator compareFilesIgnoringCaseAndSansDoublons() {
        StringListManager manager1 = new StringListManager(fileName1);
        manager1.supprimerDoublons().traitementPersonnalise(String::toLowerCase);
        StringListManager manager2 = new StringListManager(fileName2);
        manager2.supprimerDoublons().traitementPersonnalise(String::toLowerCase);
        compareFiles(manager1.getListe(), manager2.getListe());
        return this;
    }

    public void changeDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * Ecrire l'une des listes du résultat de comparaison dans un fichier
     * @param fileName nom du fichier à créer
     * @param comparisonListToSave 0 = éléments communs / 1 = spécifique fichier A / 2 = spécifique fichier B
     * @return FilesComparator
     */
    public FilesComparator saveResultInFile(String fileName, int comparisonListToSave) {
        List<String> listToSave = comparatorListManager.getList(comparisonListToSave);

        if(csvOption) {
            List<Map<String, String>> csvDataToSave = getCsvContentFromComparativeValues(
                    listToSave,
                    ComparatorListManager.SPECIFIC_LIST_B_ITEMS == comparisonListToSave ? contentCSVFile2 : contentCSVFile1);
            FilesWriter.writeCsvFile(directory, fileName, csvDataToSave, FilesWriter.OVERWRITE_EXISTING_CONTENT);
            return this;
        }

        FilesWriter.writeFile(directory, fileName, listToSave, FilesWriter.OVERWRITE_EXISTING_CONTENT);
        return this;
    }

    private void compareFiles(List<String> content1, List<String>  content2) {
        System.out.println("Comparaison des fichiers");
        System.out.println("=======================");
        System.out.println(String.format("Nombre de ligne dans le fichier 1 - %s : %d", fileName1, content1.size()));
        System.out.println(String.format("Nombre de ligne dans le fichier 2 - %s : %d", fileName2, content2.size()));
        System.out.println("=======================");

        comparatorListManager = new ComparatorListManager(content1, content2);
        comparatorListManager.compareContent();
        comparatorListManager.displayOnlySizeComparisonResultInConsole();
        System.out.println("\n ----------------------- \n");
        comparatorListManager.displayComparisonResultInConsole(false);
    }

    private static final String COMPARATIVE_VALUE_KEY = "comparativeValueKey";
    private static final String SEPARATOR = ";";
    private List<String> columnsToCompare = new ArrayList<>();
    private List<Map<String, String>> contentCSVFile1;
    private List<Map<String, String>> contentCSVFile2;
    private boolean csvOption = false;

    public FilesComparator addColumn(String columnTitle) {
        this.columnsToCompare.add(columnTitle);
        return this;
    }

    /**
     * Active la comparaison de fichiers au format CSV sans préciser les colonnes à comparer
     * Remarque : les colonnes à comparer doivent être définies au préalable avec {{@link this#addColumn(String)}}
     * @return FileComparator
     */
    public FilesComparator csvFiles() {
        return csvFiles(Collections.EMPTY_LIST);
    }

    /**
     * Active la comparaison de fichiers au format CSV en précisant les colonnes à comparer
     * @param columnsToCompare
     * @return FileComparator
     */
    public FilesComparator csvFiles(List<String> columnsToCompare) {
        completeColumnsToCompareList(columnsToCompare, contentCSVFile1.get(0));

        // si aucune colonne n'est précisée pour la comparaison on reste en mode classique
        if(this.columnsToCompare.isEmpty()) {
            return this;
        }

        csvOption = true;

        this.contentCSVFile1 = FilesReader.readCsvFile(directory, fileName1);
        this.contentCSVFile2 = FilesReader.readCsvFile(directory, fileName2);

        generateComparativeValueForEachCsvLine(contentCSVFile1);
        generateComparativeValueForEachCsvLine(contentCSVFile2);

        this.contentFile1 = extractComparativeValues(contentCSVFile1);
        this.contentFile2 = extractComparativeValues(contentCSVFile2);

        return this;
    }

    private void completeColumnsToCompareList(List<String> columnsToCompare, Map<String, String> csvLineDatas) {
        // liste fournie en param
        if(columnsToCompare != null && !columnsToCompare.isEmpty()) {
            this.columnsToCompare.addAll(columnsToCompare);
        }

        // validation des noms de colonnes (fournies via liste en param ou via addColumn)
        this.columnsToCompare.stream()
                .filter(columnTitle -> !csvLineDatas.containsKey(columnTitle))
                .forEach(columnTitle -> System.err.printf("ATTENTION : la colonne '%s' n'existe pas dans le fichier SCV fourni !%n", columnTitle));
        this.columnsToCompare = this.columnsToCompare.stream().filter(csvLineDatas::containsKey).collect(Collectors.toList());
    }

    private void generateComparativeValueForEachCsvLine(List<Map<String, String>> contentCSVFile) {
        contentCSVFile.forEach(csvLineData -> {
            StringBuilder comparativeValue = new StringBuilder();
            columnsToCompare.forEach(columnTitle -> comparativeValue.append(csvLineData.get(columnTitle)).append(SEPARATOR));
            csvLineData.put(COMPARATIVE_VALUE_KEY, comparativeValue.toString());
        });
    }

    private List<String> extractComparativeValues(List<Map<String, String>> contentCSVFile) {
        return contentCSVFile.stream().map(csvLineData -> csvLineData.get(COMPARATIVE_VALUE_KEY)).collect(Collectors.toList());
    }

    private List<Map<String, String>> getCsvContentFromComparativeValues(List<String> comparativeValues, List<Map<String, String>> contentCSVFile) {
        return contentCSVFile.stream()
                .filter(csvLineData -> comparativeValues.contains(csvLineData.get(COMPARATIVE_VALUE_KEY)))
                .collect(Collectors.toList());
    }

    public ComparatorListManager getComparatorListManager() {
        return comparatorListManager;
    }

}
