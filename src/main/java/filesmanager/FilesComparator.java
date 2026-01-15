package filesmanager;

import listtools.ComparatorListManager;

import java.util.List;

public class FilesComparator {

    public static void compareFiles(String directory, String file1, String file2) {

        List<String> contentFile1 = FilesReader.readAllLinesInFile(directory, file1);

        List<String> contentFile2 = FilesReader.readAllLinesInFile(directory, file2);

        System.out.println("Comparaison des fichiers");
        System.out.println("=======================");
        System.out.println(String.format("Nombre de ligne dans le fichier 1 - %s : %d", file1, contentFile1.size()));
        System.out.println(String.format("Nombre de ligne dans le fichier 2 - %s : %d", file2, contentFile2.size()));
        System.out.println("=======================");

        ComparatorListManager c = new ComparatorListManager(contentFile1, contentFile2);
        c.compareContent();
        c.displayOnlySizeComparisonResultInConsole();
        System.out.println("");
        System.out.println("-----------------------");
        System.out.println("");
        c.displayComparisonResultInConsole(false);
    }

}
