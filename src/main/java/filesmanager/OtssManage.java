package filesmanager;

public class OtssManage {

    private static final String O = "O";
    private static final String P = "P";
    private static final String X = "X";
    private static final String V = "V";
    private static final String OK = "OK";
    private static final String FILIGRANNE = "Filigranne";
    private static final String LOGO = "Logo";
    private static final String TXT_FILE = "W.txt";
    private static final String SEPARATOR = "\\";

    public static void createNewPicsFolders(String path, String folder) {
        FilesAndFoldersManager manager = new FilesAndFoldersManager();
        manager.setMainFolderPath(path);

        manager.createFolder(O, folder);
        manager.createFolder(P, folder);
        manager.createFolder(X, folder);

        manager.createFolder(V, folder.concat(SEPARATOR).concat(O));

        manager.createFolder(OK, folder);
        manager.createFolder(LOGO, folder.concat(SEPARATOR).concat(OK));
        manager.createFolder(FILIGRANNE, folder.concat(SEPARATOR).concat(OK));
        manager.createFile(TXT_FILE, folder.concat(SEPARATOR).concat(OK));


    }
}
