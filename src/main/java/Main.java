import filesmanager.FilesAndFoldersManager;
import filesmanager.OtssManage;
import listtools.StringListManager;
import stringtools.Increment;

public class Main {

    public static void main(String[] args) {

        //new StringListManager().remplacerMotif("\\|", ",").afficherListe();

        OtssManage.createNewPicsFolders("C:\\Users\\achix\\Documents\\Sp\\H4","P-AAR");
        //OtssManage.createNewPicsFolders("C:\\Users\\achix\\Documents\\Sp\\Allgt","P. Al lgt part60ter");

    }
}
