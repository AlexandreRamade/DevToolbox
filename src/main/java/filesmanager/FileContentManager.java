package filesmanager;

import listtools.CsvContentManager;
import listtools.StringListManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileContentManager extends StringListManager {

    private String directory;
    private String fileName;
    private List<String> fileContent;

    private CsvContentManager csvContentManager;

    public FileContentManager(String directory, String fileName) {
        super("");
        this.directory = directory;
        this.fileName = fileName;
        this.fileContent = FilesReader.readAllLinesInFile(directory, fileName);
        setListe(fileContent);
    }

    /**
     * Ecrire l'une des listes du résultat de comparaison dans un fichier
     * @param fileName nom du fichier à créer
     * @return FileContentManager
     */
    public FileContentManager saveResultInFile(String fileName) {
        if(csvContentManager != null) {
            FilesWriter.writeFile(directory, fileName, csvContentManager.getListe(), FilesWriter.OVERWRITE_EXISTING_CONTENT);
            return this;
        }
        FilesWriter.writeFile(directory, fileName, getListe(), FilesWriter.OVERWRITE_EXISTING_CONTENT);
        return this;
    }

    public FileContentManager saveResultInCSVFileFromOriginalLines(String fileName) {
        FilesWriter.writeCsvFile(directory, fileName, csvContentManager.getCsvContentFromComparativeValues(), FilesWriter.OVERWRITE_EXISTING_CONTENT);
        return this;
    }

    public CsvContentManager csvFiles() {
        return csvFiles(new ArrayList<>());
    }

    public CsvContentManager csvFiles(String... columnTitles) {
        return csvFiles(Arrays.asList(columnTitles));
    }

    public CsvContentManager csvFiles(List<String> columnsToCompare) {
        csvContentManager = new CsvContentManager(FilesReader.readCsvFile(directory, fileName), columnsToCompare);
        return csvContentManager;
    }

}
