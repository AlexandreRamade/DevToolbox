package filesmanager;

import listtools.ComparatorListManager;
import listtools.StringListManager;
import stringtools.StringTools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilesComparator {

    private String fileName1;
    private String fileName2;
    private List<String> contentFile1;
    private List<String> contentFile2;

    public FilesComparator(String directory, String fileName1, String fileName2) {
        this.fileName1 = fileName1;
        this.fileName2 = fileName2;
        this.contentFile1 = FilesReader.readAllLinesInFile(directory, fileName1);
        this.contentFile2 = FilesReader.readAllLinesInFile(directory, fileName2);
    }

    public void compareFiles() {
        compareFiles(contentFile1, contentFile2);
    }

    /**
     * ATTENTION : méthode à personnaliser
     */
    public void compareFilesApresTraitement() {
        compareFiles(traitementPersonnalise(this.contentFile1), traitementPersonnalise(this.contentFile2));
    }

    private List<String> traitementPersonnalise(List<String> liste) {
        StringListManager manager = new StringListManager(liste);
        // traitement personnalisé à compléter ici
        manager
                //.extraireVariableUneSeuleFoisParLigne("(^.+?;.+?;.+?;.+?);.*");
                .traitementPersonnalise(str -> str.substring(0, StringTools.indexOfNthOccurrence(";", 4, str)))
                .supprimerDoublons()
                .traitementPersonnalise(str -> str.toLowerCase());
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

    public void compareFilesSansDoublons() {
        StringListManager manager1 = new StringListManager(fileName1);
        manager1.supprimerDoublons();
        StringListManager manager2 = new StringListManager(fileName2);
        manager2.supprimerDoublons();
        compareFiles(manager1.getListe(), manager2.getListe());
    }

    public void compareFilesIgnoringCase() {
        StringListManager manager1 = new StringListManager(fileName1);
        manager1.traitementPersonnalise(str -> str.toLowerCase());
        StringListManager manager2 = new StringListManager(fileName2);
        manager2.traitementPersonnalise(str -> str.toLowerCase());
        compareFiles(manager1.getListe(), manager2.getListe());
    }

    public void compareFilesIgnoringCaseAndSansDoublons() {
        StringListManager manager1 = new StringListManager(fileName1);
        manager1.supprimerDoublons().traitementPersonnalise(str -> str.toLowerCase());
        StringListManager manager2 = new StringListManager(fileName2);
        manager2.supprimerDoublons().traitementPersonnalise(str -> str.toLowerCase());
        compareFiles(manager1.getListe(), manager2.getListe());
    }

    private void compareFiles(List<String> content1, List<String>  content2) {
        System.out.println("Comparaison des fichiers");
        System.out.println("=======================");
        System.out.println(String.format("Nombre de ligne dans le fichier 1 - %s : %d", fileName1, content1.size()));
        System.out.println(String.format("Nombre de ligne dans le fichier 2 - %s : %d", fileName2, content2.size()));
        System.out.println("=======================");

        ComparatorListManager c = new ComparatorListManager(content1, content2);
        c.compareContent();
        c.displayOnlySizeComparisonResultInConsole();
        System.out.println("\n ----------------------- \n");
        c.displayComparisonResultInConsole(false);
    }

}
