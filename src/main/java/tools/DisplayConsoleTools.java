package tools;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import stringtools.StringTools;

public class DisplayConsoleTools {
	
	public static final int NOT_DISPLAY_TAG = 0;
	public static final int DISPLAY_TAG_BEFORE = 1;
	public static final int DISPLAY_TAG_AFTER = 2;
	
	private static final int MAX_BAR_SIZE_COEFFICIENT = 60;
	private static final String UNIT_SYMPBOL = "|";
	private static final String TAB = "\t";
	
	
	public static void displayList(Collection<?> c) {
        c.forEach(System.out::println);
    }

    public static void displayListInLine(Collection<?> c, String separateur) {
        System.out.println(c.stream().map(String::valueOf).collect(Collectors.joining(separateur)));
    }
    
    public static <K, V> void displayMapOfList(Map<K, List<V>> map) {
    	System.out.println("Key => (size) : [list content]");
    	for(K key : map.keySet()) {
    		List<V> liste = map.get(key);
    		System.out.println(key.toString() + "\t => (" + liste.size() + ")\t : " + liste.toString());
    	}
    }

	public static <T> void displayHistogram(Map<T, Long> map, Integer displayTag, String unitSymbol) {
		int displayTagToUse = displayTag == null ? 0 : displayTag;
		String unitSymbolToUse = StringTools.isEmptyOrBlank(unitSymbol) ? UNIT_SYMPBOL : unitSymbol;
		
		long total = map.values().stream().reduce(0L, Long::sum);
		System.out.println("Total : " + total);
		
		long max = map.values().stream().sorted(Comparator.reverseOrder()).findFirst().get();
    	for(Entry<T, Long> e : map.entrySet()) {
    		StringBuilder resultLine = new StringBuilder(String.valueOf(e.getKey().toString())).append(TAB);
    		
    		if(DISPLAY_TAG_BEFORE == displayTagToUse) {
    			resultLine.append("(").append(e.getValue()).append(")").append(TAB);
    		}
    		
    		// charge les barres pour former l'histogramme
    		long limit = Long.divideUnsigned(e.getValue() * MAX_BAR_SIZE_COEFFICIENT, max);
    		for(int i = 0; i < limit; i++) {
    			resultLine.append(unitSymbolToUse);
    		}
    		
    		if(DISPLAY_TAG_AFTER == displayTagToUse) {
    			resultLine.append(TAB).append("(").append(e.getValue()).append(")");
    		}
    		System.out.println(resultLine.toString());
    	}
	}
}
