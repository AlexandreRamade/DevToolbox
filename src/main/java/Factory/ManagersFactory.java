package Factory;

import filesmanager.FilesAndFoldersManager;
import filesmanager.FilesComparator;
import filesmanager.FilesReader;
import listtools.ComparatorListManager;
import listtools.StringListManager;

import java.util.List;

public class ManagersFactory {

    public static StringListManager getStringListManager(List<String> listeStrings) {
        return new StringListManager(listeStrings);
    }

    public static StringListManager getStringListManager(String onlyOneString) {
        return new StringListManager(onlyOneString);
    }

    public static StringListManager getStringListManager(String stringADecouper, String separateur) {
        return new StringListManager(stringADecouper, separateur);
    }

    public static ComparatorListManager getComparatorListManager(List<String> listRef, List<String> listB) {
        return new ComparatorListManager(listRef, listB);
    }

    public static FilesComparator getFilesComparator(String directory, String fileName1, String fileName2) {
        return new FilesComparator(directory, fileName1, fileName2);
    }

    public static StringListManager getStringListManagerFromFile(String path, String file) {
        List<String> content = FilesReader.readAllLinesInFile(path, file);
        return new StringListManager(content);
    }


}
