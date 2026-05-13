package filesmanager;

import listtools.ComparatorListManager;
import listtools.DoubleStringListManager;
import listtools.MixerListManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoubleFilesManager extends DoubleStringListManager {
    private String directory;
    private FileContentManager fileContentManagerA;
    private FileContentManager fileContentManagerB;

    public DoubleFilesManager(String directory, String fileName1, String fileName2) {
        this.directory = directory;
        this.fileContentManagerA = new FileContentManager(directory, fileName1);
        this.fileContentManagerB = new FileContentManager(directory, fileName2);
    }

    public void changeDirectory(String directory) {
        this.directory = directory;
    }

    public DoubleFilesManager csvFiles() {
        return csvFiles(new ArrayList<>());
    }

    public DoubleFilesManager csvFiles(String... columnTitles) {
        return csvFiles(Arrays.asList(columnTitles));
    }

    public DoubleFilesManager csvFiles(List<String> columnsToCompare) {
        fileContentManagerA.csvFiles(columnsToCompare);
        fileContentManagerB.csvFiles(columnsToCompare);
        return this;
    }

    public ComparatorListManager compare() {
        attribuerManagers();
        return super.compare();
    }

    public MixerListManager mix() {
        attribuerManagers();
        return super.mix();
    }

    private void attribuerManagers() {
        setManagerA(fileContentManagerA.isCsv() ? fileContentManagerA.getCsvContentManager() : fileContentManagerA);
        setManagerB(fileContentManagerB.isCsv() ? fileContentManagerB.getCsvContentManager() : fileContentManagerB);
    }

    /**
     * Ecrire l'une des listes du résultat de comparaison dans un fichier
     * @param fileName nom du fichier à créer
     * @param comparisonListToSave 0 = éléments communs / 1 = spécifique fichier A / 2 = spécifique fichier B
     * @return FilesComparator
     */
    public DoubleFilesManager saveComparisonResultInFile(String fileName, int comparisonListToSave) {
        List<String> listToSave = this.getComparator().getList(comparisonListToSave);
        FilesWriter.writeFile(directory, fileName, listToSave, FilesWriter.OVERWRITE_EXISTING_CONTENT);
        return this;
    }

    public DoubleFilesManager saveMixResultInFile(String fileName) {
        FilesWriter.writeFile(directory, fileName, this.getMixerList().getMixedList(), FilesWriter.OVERWRITE_EXISTING_CONTENT);
        return this;
    }
}
