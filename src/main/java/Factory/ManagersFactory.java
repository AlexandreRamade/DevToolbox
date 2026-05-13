package Factory;

import filesmanager.DoubleFilesManager;
import filesmanager.FileContentManager;
import filesmanager.FilesAndFoldersManager;
import filesmanager.FilesComparator;
import filesmanager.FilesReader;
import listtools.ComparatorListManager;
import listtools.DoubleStringListManager;
import listtools.MixerListManager;
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

    public static FileContentManager getFileContentManager(String directory, String fileName) {
        return new FileContentManager(directory, fileName);
    }

    public static MixerListManager getMixerListManager(List<String> liste1, List<String> liste2) {
        return new MixerListManager(liste1, liste2);
    }

    public static MixerListManager getMixerListManagerFromFiles(String directory, String fileName1, String fileName2) {
        return new MixerListManager(FilesReader.readAllLinesInFile(directory, fileName1), FilesReader.readAllLinesInFile(directory, fileName2));
    }

    public static DoubleStringListManager getDoubleStringListManager(List<String> listeA, List<String> listeB) {
        return new DoubleStringListManager() {{
            setListeManagerA(listeA);
            setListeManagerB(listeB);
        }};
    }

    public static DoubleFilesManager getDoubleFilesManager(String directory, String fileName1, String fileName2) {
        return new DoubleFilesManager(directory, fileName1, fileName2);
    }

}
