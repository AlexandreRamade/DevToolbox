package tools;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import listtools.ListTools;

public class RandomTools {
	
	private static final Random rand = new Random();

	/**
	 * diceRoll </br>
	 * <p>
	 * simule un lancé de dé classique à 6 faces
	 * </p>
	 * 
	 * @return un nombre aléatoire entre 1 et 6
	 */
	public static int diceRoll() {
		return rand.nextInt(6) + 1;
	}
	
	/**
	 * diceRoll </br>
	 * <p>
	 * simule un lancé de dé à X faces
	 * </p>
	 * 
	 * @param numberOfFaces nombre de faces (X)
	 * @return un nombre aléatoire entre 1 et le nombre passé en paramètre (X)
	 */
	public static int diceRoll(int numberOfFaces) {
		return rand.nextInt(numberOfFaces) + 1;
	}
	
	/**
	 * diceRolls </br>
	 * <p>
	 * simule le résultat de N lancés de dé à X faces
	 * </p>
	 * 
	 * @param numberOfFaces nombre de face (X)
	 * @param numberOfRolls nombre de lancés (N)
	 * @return une liste de taille N contenant des nombres aléatoires compris entre 1 et X
	 */
	public static List<Integer> diceRolls(int numberOfFaces, int numberOfRolls) {
		return Stream.iterate(0, roll -> ++roll)
				.limit(numberOfRolls)
				.map(roll -> rand.nextInt(numberOfFaces) + 1)
				.collect(Collectors.toList());
	}
	
	/**
	 * diceRollsCount </br>
	 * <p>
	 * simule le résultat de N lancés de dé à X faces 
	 * et comptabilise les résultats en faisant la somme du nombre de sortie de chaque numéro
	 * </p>
	 * 
	 * @param numberOfFaces nombre de face (X)
	 * @param numberOfRolls nombre de lancés (N)
	 * @return une map associant chaque numéro (X) avec le nombre de sorti de ce numéro parmis les N tirages
	 */
	public static Map<Integer, Long> diceRollsCount(int numberOfFaces, int numberOfRolls) {
		Map<Integer, Long> resultCounts = Stream.iterate(0, roll -> roll++).limit(numberOfRolls)
				.map(roll -> rand.nextInt(numberOfFaces) + 1)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		
		// complète la map avec les numéros (X) qui ne sont pas sortis lors des N tirrages si nécéssaire
		if(resultCounts.size() < numberOfFaces) {
			Stream.iterate(1, roll -> ++roll).limit(numberOfFaces)
				.filter(n -> !resultCounts.containsKey(n))
				.forEach(n -> resultCounts.put(n, 0L));
		}
		
		return resultCounts;
	}
	
	/**
	 * randomlyDrawItemsFromCollectionWithoutDuplicates </br>
	 * <p>
	 * pioche au hasard un nombre N d'éléments dans une collection sans possibilité de doublon
	 * </p>
	 * <i>
	 * ATTENTION : le nombre d'élément à piocher ne peut être plus grand que la taille de la liste
	 * </i>
	 * 
	 * @param <T>
	 * @param collection le collection d'éléments
	 * @param numberItemsToExtract nombre d'éléments à extraire au hasard
	 * @return la liste des éléments piochés au hasard
	 */
	public static <T> Collection<T> randomlyDrawItemsFromCollectionWithoutDuplicates(Collection<T> collection, int numberItemsToExtract) {
		Collections.shuffle((List<?>) collection);
		return collection.stream().limit(numberItemsToExtract).collect(Collectors.toList());
	}
	
	/**
	 * randomlyDrawItemsFromListWithPossibilityDuplicates </br>
	 * <p>
	 * pioche au hasard un nombre N d'éléments dans une liste avec possibilité d'obtenir plusieurs fois le même élément
	 * </p>
	 * 
	 * @param <T>
	 * @param list la liste d'éléments
	 * @param numberItemsToExtract le nombre d'élément à extraire au hasard
	 * @return la liste des éléments piochés au hasard
	 * 
	 * @see this{@link #diceRolls(int, int)} permet d'obtenir la liste des index des éléments à extraire
	 */
	public static <T> Collection<T> randomlyDrawItemsFromListWithPossibilityDuplicates(List<T> list, int numberItemsToExtract) {
		List<T> extractedItems = new LinkedList<T>();
		List<Integer> index = diceRolls(list.size(), numberItemsToExtract);
		index.stream().forEach(i -> extractedItems.add(list.get(i - 1)));;
		return extractedItems;
	}
	
	/**
	 * randomlyDrawItemsFromListWithPossibilityDuplicatesCount </br>
	 * <p>
	 * pioche au hasard un nombre N d'éléments dans une liste avec possibilité d'obtenir plusieurs fois le même élément
	 * et comptabilise les résultats en faisant la somme du nombre de sortie de chaque élément
	 * </p>
	 * 
	 * @param <T>
	 * @param list la liste d'éléments
	 * @param numberOfDraws le nombre de tirage
	 * @return une map associant chaque élément avec le nombre de sorti de celui-ci parmis les N tirages
	 * 
	 * @see this{@link this#diceRollsCount(int, int)
	 */
	public static <T> Map<T, Long> randomlyDrawItemsFromListWithPossibilityDuplicatesCount(List<T> list, int numberOfDraws) {
		Map<T, Long> result = new LinkedHashMap<T, Long>();
		Map<Integer, Long> counts = diceRollsCount(list.size(), numberOfDraws);
		for(int i = 0; i < list.size(); i++) {
			result.put(list.get(i), counts.get(i + 1));
		}
		return result;
	}
}
