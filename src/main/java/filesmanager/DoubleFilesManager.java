package filesmanager;

import listtools.DoubleStringListManager;
import listtools.StringListManager;

import java.util.List;

public class DoubleFilesManager extends DoubleStringListManager {
    private String directory;

    public DoubleFilesManager(String directory, String fileName1, String fileName2) {
        this.directory = directory;
        this.setManagerA(new StringListManager(FilesReader.readAllLinesInFile(directory, fileName1)));
        this.setManagerB(new StringListManager(FilesReader.readAllLinesInFile(directory, fileName2)));
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
