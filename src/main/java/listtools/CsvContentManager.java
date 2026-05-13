package listtools;

import filesmanager.FileContentManager;
import filesmanager.FilesReader;
import filesmanager.FilesWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvContentManager extends StringListManager {

    protected static final String COMPARATIVE_VALUE_KEY = "comparativeValueKey";
    protected static final String SEPARATOR = ";";
    protected boolean ignoreCase = false;

    protected List<String> columns = new ArrayList<>();
    protected Stream<Map<String, String>> csvContent;
    protected List<String> columnsToCompare = new ArrayList<>();

    public CsvContentManager() {
        super("");
    }

    public CsvContentManager(List<Map<String, String>> csvContent) {
        super("");
        this.csvContent = csvContent.stream();
        this.columns = csvContent.isEmpty() ? Collections.emptyList() : new ArrayList<>(csvContent.get(0).keySet());
    }

    public CsvContentManager(List<Map<String, String>> csvContent, List<String> columns) {
        super("");
        this.csvContent = csvContent.stream();
        this.columns = columns.isEmpty() ? new ArrayList<>(csvContent.get(0).keySet()) : columns;
    }

    public CsvContentManager toList() {
        return toList(Collections.emptyList());
    }

    public CsvContentManager toList(String... columnsToCompare) {
        return toList(Arrays.asList(columnsToCompare));
    }

    public CsvContentManager toList(List<String> columnsToCompare) {
        List<Map<String, String>> currentCsvContent = csvContent.toList();

        completeColumnsToCompareList(columnsToCompare, currentCsvContent.get(0));
        if(this.columnsToCompare.isEmpty()) {
            System.err.printf("ATTENTION : la liste des colonnes à prendre en compte pour le traitement CSV est vide. La liste du contenu CSV n'a pas pu être générée !");
            return this;
        }
        generateComparativeValueForEachCsvLine(currentCsvContent);
        setListe(extractComparativeValues(currentCsvContent));

        this.csvContent = currentCsvContent.stream();
        return this;
    }

    private void completeColumnsToCompareList(List<String> columnsToCompare, Map<String, String> csvLineDatas) {
        this.columnsToCompare.clear();
        if(columnsToCompare.isEmpty()) {
            this.columnsToCompare.addAll(csvLineDatas.keySet());
            return;
        }
        columnsToCompare.forEach(columnTitle -> {
            if(csvLineDatas.containsKey(columnTitle)) {
                this.columnsToCompare.add(columnTitle);
            } else {
                System.err.printf("ATTENTION : la colonne '%s' n'existe pas dans les données CSV fournies !%n", columnTitle);
            }
        });
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

    public List<Map<String, String>> getCsvContentFromComparativeValues() {
        if(ignoreCase) {
            this.csvContent.forEach(csvLineData -> csvLineData.put(COMPARATIVE_VALUE_KEY, csvLineData.get(COMPARATIVE_VALUE_KEY).toLowerCase()));
        }
        List<String> liste = getListe();
        return this.csvContent
                .filter(csvLineData -> liste.contains(csvLineData.get(COMPARATIVE_VALUE_KEY)))
                .map(this::removeComparativeValueKey)
                .collect(Collectors.toList());
    }

    private Map<String, String> removeComparativeValueKey(Map<String, String> csvLineData) {
        csvLineData.remove(COMPARATIVE_VALUE_KEY);
        return csvLineData;
    }

    public CsvContentManager filtrerSiLesValeursDUneColonneContient(String column, String value) {
        csvContent = csvContent.filter(map -> map.get(column).contains(value));
        return this;
    }

    public CsvContentManager filtrerSurLesValeursDUneColonne(String column, String value) {
        csvContent = csvContent.filter(map -> map.get(column).equals(value));
        return this;
    }

    public CsvContentManager filtrerSurLesValeursDUneColonne(String column, Predicate<String> predicate) {
        csvContent = csvContent.filter(map -> predicate.test(map.get(column)));
        return this;
    }

    public CsvContentManager filtrerSurLesValeursDe2Colonnes(String column1, String column2, BiPredicate<String, String> predicate) {
        csvContent = csvContent.filter(map -> predicate.test(map.get(column1), map.get(column2)));
        return this;
    }

    public CsvContentManager traiterLesValeurDeLaColonne(String column, Function<String, String> function) {
        csvContent = csvContent.map(map -> {
            map.put(column, function.apply(map.get(column)));
            return map;
        });
        return this;
    }

    public CsvContentManager addColumn(String columnTitle, String defaultValue) {
        columns.add(columnTitle);
        csvContent = csvContent.map(map -> {
            map.put(columnTitle, defaultValue);
            return map;
        });
        return this;
    }

    public CsvContentManager removeColumn(String columnTitle) {
        columns.remove(columnTitle);
        csvContent = csvContent.map(map -> {
            map.remove(columnTitle);
            return map;
        });
        return this;
    }

    public CsvContentManager changeColumnTitle(String oldColumnTitle, String newColumnTitle) {
        columns.remove(oldColumnTitle);
        columns.add(newColumnTitle);
        csvContent = csvContent.map(map -> {
            map.put(newColumnTitle, map.get(oldColumnTitle));
            map.remove(oldColumnTitle);
            return map;
        });
        return this;
    }

    public CsvContentManager concatColumnsValues(String targetColumn, String separator, String... columnTitles) {
        if(!columns.contains(targetColumn)) {
            addColumn(targetColumn, "");
        }
        Map<String, String> aggregation = new LinkedHashMap<>();
        csvContent = csvContent.map(map -> {
            String concatenated = Arrays.stream(columnTitles)
                    .map(map::get)
                    .collect(Collectors.joining(separator));
            map.put(targetColumn, concatenated);
            return map;
        });
        return this;
    }

    public CsvContentManager concatColumnBValuesIfColumnAValuesAreEquals(String columnA, String columnB, String separator) {
        Map<String, String> aggregation = new LinkedHashMap<>();
        csvContent.forEach(map ->
                aggregation.merge(
                        map.get(columnA),
                        map.get(columnB),
                        (existing, incoming) -> existing + separator + incoming
                )
        );
        csvContent = aggregation.entrySet().stream()
                .map(entry -> Map.of(columnA, entry.getKey(), columnB, entry.getValue()));
        return this;
    }


    public void displayResult() {
        List<Map<String, String>> result = csvContent.toList();
        System.out.println("Taille de la liste : " + result.size());
        System.out.println(FilesWriter.generateCsvContent(result, null, true));
    }

    public List<String> getListe() {
        List<String> listToSave = new ArrayList<>();
        listToSave.add(columnsToCompare.stream().collect(Collectors.joining(SEPARATOR)));
        listToSave.addAll(super.getListe());
        return listToSave;
    }

}
