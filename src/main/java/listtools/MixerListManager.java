package listtools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MixerListManager {

    private List<String> listA;
    private List<String> listB;

    private List<String> mixedList;

    /** ***** ***** CONSTRUCTEURS ***** ***** */
    public MixerListManager() {
        this.mixedList = new ArrayList<>();
    }

    public MixerListManager(List<String> listA, List<String> listB) {
        this.listA = listA;
        this.listB = listB;
        this.mixedList = new ArrayList<>();
    }

    public List<String> getListA() {
        return listA;
    }

    public List<String> getListB() {
        return listB;
    }

    public List<String> getMixedList() {
        return mixedList;
    }

    public void setListA(List<String> listA) {
        this.listA = listA;
    }

    public void setListB(List<String> listB) {
        this.listB = listB;
    }

    /** ***** ***** METHODES UTILITAIRES ***** ***** */

    private static BiFunction<List<String>, Integer, String> getOrEmpty = (list, index) -> list.size() > index ? list.get(index) : "";

    private static BiFunction<List<String>, List<String>, Integer> getMaxSize = (list1, list2) -> Math.max(list1.size(), list2.size());

    private static BiFunction<List<String>, Function<Integer, Integer>, Map<Integer, String>> convertListToMap = (list, transformIndex) -> {
        HashMap<Integer, String> map = new HashMap<>();
        for(int i = 0; i < list.size(); i++) {
            map.put(transformIndex.apply(i), list.get(i));
        }
        return map;
    };

    /** ***** ***** METHODES DE TRAITEMENT ***** ***** */
    public void alternateAllElements() {
        this.mixedList.clear();
        Map<Integer, String> mapA = convertListToMap.apply(listA, (index) -> index * 2);
        Map<Integer, String> mapB = convertListToMap.apply(listB, (index) -> index * 2 + 1);
        mapA.putAll(mapB);
        this.mixedList = mapA.values().stream().toList();
    }

    public void replaceElements(int n) {
        this.mixedList.clear();
        Map<Integer, String> mixedMap = convertListToMap.apply(listA, (index) -> index + 1);
        for(int i = 0; i < listB.size(); i++) {
            mixedMap.replace(n * (i + 1), listB.get(i));
        }
        this.mixedList = mixedMap.values().stream().toList();
    }

    /** ***** ***** AFFICHAGE DU RESULTAT ***** ***** */

    public void afficherTailleListes() {
        System.out.println(String.format("Nombre d'éléments dans la liste A = %d", listA.size()));
        System.out.println(String.format("Nombre d'éléments dans la liste B = %d", listB.size()));
        System.out.println(String.format("Nombre d'éléments dans la liste mixée = %d", mixedList.size()));
    }

    public void afficherMixedList() {
        mixedList.stream().forEach(System.out::println);
    }

    public void afficherEnLigne(String separateur) {
        System.out.println(mixedList.stream().collect(Collectors.joining(separateur)));
    }
}
