package listtools;

import java.util.ArrayList;
import java.util.List;

public class Combination {
	
	public static <T> List<List<T>> combinate(List<T> elements) {
        List<List<T>> combinations = new ArrayList<>();
        generateCombinations(elements, new ArrayList<>(), combinations, new boolean[elements.size()]);
        System.out.println(combinations);
        return combinations;
    }

    private static <T> void generateCombinations(List<T> elements, List<T> current, List<List<T>> result, boolean[] used) {
        if (current.size() == elements.size()) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = 0; i < elements.size(); i++) {
            if (!used[i]) {
                used[i] = true;
                current.add(elements.get(i));
                generateCombinations(elements, current, result, used);
                current.remove(current.size() - 1);
                used[i] = false;
            }
        }
    }
}
