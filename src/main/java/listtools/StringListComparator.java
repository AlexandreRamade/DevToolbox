package listtools;

import stringtools.Increment;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringListComparator {
    private String LIST_REF = "A\r\n" + "B\r\n" + "C\r\n" + "B";
    private String LIST_B = "A\r\n" + "B\r\n" + "D\r\n";

    private String SEPARATOR = "\r\n";

    private List<String> listRef;
    private List<String> listB;

    private List<String> commonItems;
    private List<String> specificToListRef;
    private List<String> specificToListB;


    /** ***** ***** CONSTRUCTORS ***** ***** */

    public StringListComparator() {
        listRef = Arrays.asList(LIST_REF.split(SEPARATOR));
        listB = Arrays.asList(LIST_B.split(SEPARATOR));
    }

    public StringListComparator(String listRefFromString, String listBFromString, String separator) {
        LIST_REF = listRefFromString;
        LIST_B = listBFromString;
        SEPARATOR = separator;
        listRef = Arrays.asList(LIST_REF.split(SEPARATOR));
        listB = Arrays.asList(LIST_B.split(SEPARATOR));
    }

    public StringListComparator(List<String> listRef, List<String> listB) {
        this.listRef = listRef;
        this.listB = listB;
    }

    /** ***** ***** GETTERS AND SETTERS ***** ***** */

    public String getSEPARATOR() {
        return SEPARATOR;
    }

    public void setSEPARATOR(String separator) {
        SEPARATOR = separator;
    }

    public List<String> getListRef() {
        return listRef;
    }

    public void setListRef(List<String> listRef) {
        this.listRef = listRef;
    }

    public List<String> getListB() {
        return listB;
    }

    public void setListB(List<String> listB) {
        this.listB = listB;
    }

    public List<String> getSpecificToListRef() {
        return specificToListRef;
    }

    public void setSpecificToListRef(List<String> specificToListRef) {
        this.specificToListRef = specificToListRef;
    }

    public List<String> getSpecificToListB() {
        return specificToListB;
    }

    public void setSpecificToListB(List<String> specificToListB) {
        this.specificToListB = specificToListB;
    }


    /** ***** ***** METHODES DE TRAITEMENT ***** ***** */

    public List<String> findCommonItems() {
        this.commonItems = this.listRef.stream().filter(item -> this.listB.contains(item)).collect(Collectors.toList());
        return this.commonItems;
    }

    public List<String> findSpecificItemsInReferenceList() {
        this.specificToListRef = listRef.stream().filter(item -> !listB.contains(item)).collect(Collectors.toList());
        return this.specificToListRef;
    }

    public List<String> findSpecificItemsInListB() {
        this.specificToListB = this.listB.stream().filter(code -> !listRef.contains(code)) .collect(Collectors.toList());
        return this.specificToListB;
    }

    public StringListComparator compareContent() {
        this.findCommonItems();
        this.findSpecificItemsInReferenceList();
        this.findSpecificItemsInListB();
        return this;
    }


    /** ***** ***** AFFICHAGE ***** ***** */

    public void displayComparisonResultInConsole(boolean showLineNumbers) {
        displayResultInConsole("Elements dans la liste de référence et non présents dans la liste B : ", this.specificToListRef, showLineNumbers);
        System.out.println("\n ----- ----- ----- ----- ----- \n");
        displayResultInConsole("Elements en trop dans la liste B et non présents dans la liste de référence : ", this.specificToListB, showLineNumbers);
    	System.out.println("\n ----- ----- ----- ----- ----- \n");
        displayResultInConsole("Elements communs présents dans les 2 listes : ", this.commonItems, showLineNumbers);
    }

    private void displayResultInConsole(String comment, List<String> list, boolean showLineNumbers) {
        System.err.println(comment + list.size());
        List<String> listToDispay = list;
        if(showLineNumbers) {
        	Increment increment = new Increment("0", true, true);
        	listToDispay = increment.addIncrementedSequenceToListItems(list, "- ", Increment.BEFORE);
        }
        System.out.println(listToDispay.stream().collect(Collectors.joining(SEPARATOR)));
    }

    /**
     * displayDuplicatesOfEachList <br>
     * <p>
     * Vérifie pour chacune des listes si elle contient des éléments en doublon et
     * affiche ces éléments le cas échéant
     * </p>
     */
    public void displayDuplicatesOfEachList() {
        Set<String> set = new HashSet<>();
        System.out.println(
                String.format("Elements en doublons dans la liste de référence : \n%s", listRef
                        .stream().filter(str -> !set.add(str)).collect(Collectors.joining(", "))));
        set.clear();
        System.out.println(String.format("Elements en doublons dans la liste B : \n%s",
                listB.stream().filter(str -> !set.add(str)).collect(Collectors.joining(", "))));
    }

}
