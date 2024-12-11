package listtools;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MixerListManager {

	private boolean completeMixedListWithExessItems = true;

	private List<String> listA;
	private List<String> listB;

	private List<String> mixedList;

	/** ***** ***** CONSTRUCTORS ***** ***** */
	public MixerListManager() {
		this.mixedList = new ArrayList<>();
	}

	public MixerListManager(List<String> listA, List<String> listB) {
		this.listA = listA;
		this.listB = listB;
		this.mixedList = new ArrayList<>();
	}

	/** ***** ***** GETTERS AND SETTERS ***** ***** */

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

	public boolean isCompleteMixedListWithExessItems() {
		return completeMixedListWithExessItems;
	}

	public void setCompleteMixedListWithExessItems(boolean completeMixedListWithExessItems) {
		this.completeMixedListWithExessItems = completeMixedListWithExessItems;
	}

	/** ***** ***** EXPOSED METHODS ***** ***** */

	// ----- Alternate items

	/**
	 * alternateItems </br>
	 * <p>
	 * alterner chaque éléments des 2 listes
	 * </p>
	 * <p>
	 * Exemple : listA = A1, A2, A3, A4 listB = B1, B2, B3, B4 alternateItems() =>
	 * A1, B1, A2, B2, A3, B3, A4, B4
	 * </p>
	 */
	public void alternateItems() {
		this.mixedList.clear();
		this.alternateOrReplaceGroupItemsEvery(1, 1, false, false);
	}

	/**
	 * alternateItemsEvery </br>
	 * <p>
	 * insérer un élément de la liste B tous les n éléments de la liste A
	 * </p>
	 * <p>
	 * Exemple : listA = A1, A2, A3, A4, A5, A6, A7, A8, A9 listB = B1, B2, B3, B4
	 * alternateItemsEvery(3) => A1, A2, A3, B1, A4, A5, A6, B2, A7, A8, A9, B3, B4
	 * </p>
	 *
	 * @param n nombre d'éléments de la liste A
	 */
	public void alternateItemsEvery(int n) {
		this.mixedList.clear();
		this.alternateOrReplaceGroupItemsEvery(n, 1, false, false);
	}

	/**
	 * alternateGroupItemsEvery </br>
	 * <p>
	 * insérer un groupe éléments de la liste B tous les n éléments de la liste A
	 * </p>
	 * <p>
	 * Exemple : listA = A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14
	 * listB = B1, B2, B3, B4, B5, B6, B7, B8, B9 alternateItemsEvery(2, 3) => A1,
	 * A2, B1, B2, B3, A3, A4, B4, B5, B6, A5, A6, B7, B8, B9, A7, A8, A9, A10, A11,
	 * A12, A13, A14
	 * </p>
	 *
	 * @param n     nombre d'éléments de la liste A
	 * @param group nombre déléments de la liste B
	 */
	public void alternateGroupItemsEvery(int n, int group) {
		this.alternateOrReplaceGroupItemsEvery(n, group, false, false);
	}

	// ----- Replace items in list A

	/**
	 * replaceItems </br>
	 * <p>
	 * remplace un élément sur deux de la liste A par les éléments de la liste B
	 * </p>
	 * <p>
	 * Exemple : listA = A1, A2, A3, A4, A5, A6 listB = B1, B2, B3, B4
	 * replaceItems() => A1, B1, A3, B2, A5, B3, B4
	 * </p>
	 */
	public void replaceItems() {
		this.alternateOrReplaceGroupItemsEvery(1, 1, true, false);
	}

	/**
	 * replaceItemsEvery </br>
	 * <p>
	 * remplace un élément de la liste A par les éléments de la liste B tous les n
	 * éléments
	 * </p>
	 * <p>
	 * Exemple : listA = A1, A2, A3, A4, A5, A6, A7, A8 listB = B1, B2, B3, B4
	 * replaceItemsEvery(2) => A1, A2, B1, A4, A5, B2, A7, A8, B3, B4
	 * </p>
	 *
	 * @param n nombre d'éléments de la liste A
	 */
	public void replaceItemsEvery(int n) {
		this.alternateOrReplaceGroupItemsEvery(n, 1, true, false);
	}

	/**
	 * alternateOrReplaceGroupItemsEvery </br>
	 * <p>
	 * remplace un groupe d'élément de la liste A par un groupe d'éléments de la
	 * liste B
	 * </p>
	 * <p>
	 * Exemple : listA = A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12 listB =
	 * B1, B2, B3, B4, B5, B6 replaceGroupItemsEvery(2, 3) => A1, A2, B1, B2, B3,
	 * A6, A7, B4, B5, B6, A11, A12
	 * </p>
	 *
	 * @param n     nombre d'éléments de la liste A
	 * @param group nombre d'éléments de la liste B
	 */
	public void replaceGroupItemsEvery(int n, int group) {
		this.alternateOrReplaceGroupItemsEvery(n, group, true, false);
	}

	// ----- Mix the 2 lists

	/**
	 * mixLists </br>
	 * <p>
	 * mixe les 2 listes en alternant et remplaçant un élément sur deux de chaque
	 * liste
	 * </p>
	 * <p>
	 * Exemple : listA = A1, A2, A3, A4, A5, A6 listB = B1, B2, B3, B4, B5, B6
	 * mixLists() => A1, B2, A3, B4, A5, B6
	 * </p>
	 */
	public void mixLists() {
		this.alternateOrReplaceGroupItemsEvery(1, 1, false, true);
	}

	/**
	 * mixListsEvery </br>
	 * <p>
	 * parcours les 2 listes en remplacent un élément de la liste A par un élément
	 * de la liste B tous les n éléments de la liste A
	 * </p>
	 * <p>
	 * Exemple : listA = A1, A2, A3, A4, A5, A6 listB = B1, B2, B3, B4, B5, B6
	 * mixListsEvery(2) => A1, A2, B3, A4, A5, B6
	 * </p>
	 * 
	 * @param n nombre d'éléments de la liste A
	 */
	public void mixListsEvery(int n) {
		this.alternateOrReplaceGroupItemsEvery(n, 1, false, true);
	}

	/**
	 * mixListsByGroupEvery </br>
	 * <p>
	 * parcours les 2 listes en remplacent un groupe d'éléments de la liste A par un
	 * groupe d'éléments de la liste B
	 * </p>
	 * <p>
	 * Exemple : listA = A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12 listB =
	 * B1, B2, B3, B4, B5, B6, B7, B8, B9, B10, B11, B12 mixListsByGroupEvery(2, 3)
	 * => A1, A2, B3, B4, B5, A6, A7, B8, B9, B10, A11, A12
	 * </p>
	 * 
	 * @param n     nombre d'éléments de la liste A
	 * @param group nombre d'éléments de la liste B
	 */
	public void mixListsByGroupEvery(int n, int group) {
		this.alternateOrReplaceGroupItemsEvery(n, group, false, true);
	}

	// ----- Alternate items conditionally

	/**
	 * alternateItemsIf </br>
	 * <p>
	 * insere les éléments de la liste B dans la liste A uniquement s'ils vérifient
	 * la condition passée en paramètre
	 * </p>
	 * <p>
	 * Exemple : listA = A1, A2, A3, A4, A5, A6 listB = B1, B2, B3, B4, B5, B6
	 * alternateItemsIf(stringContainEvenNumber) => A1, A2, B2, A3, A4, B4, A5, A6,
	 * B6
	 * </p>
	 *
	 * @param predicate condition que doit vérifier les éléments de la liste B
	 */
	public void alternateItemsIf(Predicate<String> predicate) {
		this.alternateOrReplaceIf(predicate, false);
	}

	// ----- Replace items in list A conditionally

	/**
	 * replaceItemsIf </br>
	 * <p>
	 * remplace les éléments de la liste A par les élements de la liste B uniquement
	 * s'ils vérifient la condition passée en paramètre
	 * </p>
	 * <p>
	 * Exemple : listA = A1, A2, A3, A4, A5, A6 listB = B1, B2, B3, B4, B5, B6
	 * alternateItemsIf(stringContainEvenNumber) => A1, B2, A3, B4, A5, B6
	 * </p>
	 *
	 * @param predicate condition que doivent vérifier les éléments de la liste B
	 */
	public void replaceItemsIf(Predicate<String> predicate) {
		this.alternateOrReplaceIf(predicate, true);
	}

	// ----- Test the items in lists A and B

	/**
	 * mixListsIf </br>
	 * <p>
	 * parcours les listes A et B et ne conserve que les éléments vérifiant la
	 * condition passée en paramètre
	 * </p>
	 * <p>
	 * Exemple : listA = A1, A2, A3, A4, A5, A6 listB = B1, B2, B3, B4, B5, B6
	 * mixListsIf(stringContainEvenNumber) => A2, B2, A4, B4, A6, B6
	 * </p>
	 *
	 * @param predicate condition que doivent vérifier les éléments de la liste A et
	 *                  B
	 */
	public void mixListsIf(Predicate<String> predicate) {
		this.alternateWithTestOn2Lists(predicate, predicate);
	}

	/**
	 * mixListsIf </br>
	 * <p>
	 * parcours les listes A et B et ne conserve que les éléments vérifiant les
	 * conditions passées en paramètre
	 * </p>
	 * <p>
	 * Exemple : listA = A1, A2, A3, A4, A5, A6 listB = B1, B2, B3, B4, B5, B6
	 * mixListsIf(stringContainEvenNumber, stringContainOddEvenNumber) => B1, A2,
	 * B3, A4, B5, A6
	 * </p>
	 *
	 * @param predicateListA condition que doivent vérifier les éléments de la liste
	 *                       A
	 * @param predicateListB condition que doivent vérifier les éléments de la liste
	 *                       B
	 */
	public void mixListsIf(Predicate<String> predicateListA, Predicate<String> predicateListB) {
		this.alternateWithTestOn2Lists(predicateListA, predicateListB);
	}

	/** ***** ***** DISPLAYING THE RESULT ***** ***** */

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

	/** ***** ***** TREATMENT METHODS ***** ***** */

	/**
	 * mergeListsItems </br>
	 * <p>
	 * concatène les items des 2 listes correspondant au même index en les séparant
	 * par le séparateur passé en paramètre
	 * </p>
	 * <p>
	 * Exemple d'utilisation : listA = A1, A2, A3, A4, A5, A6 listB = B1, B2, B3, B4
	 * mergeListsItems("-") => A1-B1, A2-B2, A3-B3, A4-B4, A5, A6
	 * </p>
	 * 
	 * @param separator
	 */
	public void mergeListsItems(String separator) {
		int index = 0;
		for (; index < listA.size() && index < listB.size(); index++) {
			this.mixedList.add(new StringBuilder(this.listA.get(index)).append(separator).append(this.listB.get(index))
					.toString());
		}

		// complète avec les éventuels éléments restants de l'une des 2 listes
		if (completeMixedListWithExessItems) {
			while (index < listA.size()) {
				this.mixedList.add(listA.get(index));
				index++;
			}

			while (index < listB.size()) {
				this.mixedList.add(listB.get(index));
				index++;
			}
		}
	}
	
	/**
	 * mergeListsItemsApplyingStringFormatPattern </br>
	 * <p>
	 * remplace les valeurs 1 et 2 d'un StringFormatPattern par les éléments respectifs de chaque liste
	 * </p>
	 * <p>
	 * Exemple d'utilisation : <br>
	 * listA = A1, A2, A3, A4 <br>
	 * listB = B1, B2
	 * mergeListsItemsApplyingStringFormatPattern("%s correspond à %s dans la deuxieme liste") => </br>
	 * A1 correspond à B1 dans la deuxieme liste
	 * A2 correspond à B2 dans la deuxieme liste
	 * A3 correspond à ? dans la deuxieme liste
	 * A4 correspond à ? dans la deuxieme liste
	 * </p>
	 * 
	 * @param stringFormatPattern patterne devant contenir exactement 2 symbole %s à remplacer
	 */
	public void mergeListsItemsApplyingStringFormatPattern(String stringFormatPattern) {
		int index = 0;
		for (; index < listA.size() && index < listB.size(); index++) {
			this.mixedList.add(String.format(stringFormatPattern, listA.get(index), listB.get(index)));
		}

		// complète avec les éventuels éléments restants de l'une des 2 listes
		if (completeMixedListWithExessItems) {
			while (index < listA.size()) {
				this.mixedList.add(String.format(stringFormatPattern, listA.get(index), "?"));
				index++;
			}

			while (index < listB.size()) {
				this.mixedList.add(String.format(stringFormatPattern, "?", listB.get(index)));
				index++;
			}
		}
	}

	/**
	 * alternateOrReplaceGroupItemsEvery </br>
	 * <p>
	 * méthode permétant d'alterner ou de remplacer un groupe d'élément de la liste
	 * A par un groupe d'élément de la liste B de manière consécutives
	 * </p>
	 * <p>
	 * Exemples d'utilisation :</br>
	 * listA = A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14 </br>listB =
	 * B1, B2, B3, B4, B5, B6, B7, B8, B9 -> alterner
	 * alternateOrReplaceGroupItemsEvery(2, 3, false, false) => A1, A2, B1, B2, B3,
	 * A3, A4, B4, B5, B6, A5, A6, B7, B8, B9, A7, A8, A9, A10, A11, A12, A13, A14
	 * -> remplacer alternateOrReplaceGroupItemsEvery(2, 3, true, false) => A1, A2,
	 * B1, B2, B3, A6, A7, B4, B5, B6, A11, A12, B7, B8, B9 -> mixer
	 * alternateOrReplaceGroupItemsEvery(2, 3, false, true) => A1, A2, B3, B4, B5,
	 * A6, A7, B8, B9, A10, A11, A12, A13, A14
	 * </p>
	 *
	 * @param n                     nombre d'elements par groupe de la liste A
	 * @param group                 nombre d'éléments par groupe de la liste B
	 * @param replaceItemsFromListA true = éléments de la liste A remplacés par les
	 *                              éléments de la liste B / false = insertion des
	 *                              éléments B dans la liste A
	 * @param mixLists              pour un index donné seuls l'élément de l'une des
	 *                              2 liste est conservé
	 */
	private void alternateOrReplaceGroupItemsEvery(int n, int group, boolean replaceItemsFromListA, boolean mixLists) {
		this.mixedList.clear();
		n++; // permet que le nb passé en param corresponde au nb d'elts de la liste A
				// (cohérence avec group = nb d'elts de la liste B)

		// alterne les éléments des listes A et B jusqu'à ce que l'une des listes soit
		// entièrement consomée
		int a = 0, b = 0;
		for (int currentStep = 1; a < listA.size() && b < listB.size(); currentStep++) {
			if (currentStep % n == 0) {
				for (int g = 0; g < group && b < listB.size(); g++) {
					this.mixedList.add(listB.get(b));
					b++;
					if (replaceItemsFromListA || mixLists) {
						a++;
					}
				}
			} else {
				this.mixedList.add(listA.get(a));
				a++;
				if (mixLists) {
					b++;
				}
			}
		}

		// complète avec les éventuels éléments restants de l'une des 2 listes
		if (completeMixedListWithExessItems) {
			while (a < listA.size()) {
				this.mixedList.add(listA.get(a));
				a++;
			}

			while (b < listB.size()) {
				this.mixedList.add(listB.get(b));
				b++;
			}
		}
	}

	/**
	 * alternateOrReplaceIf </br>
	 * <p>
	 * teste chaque élément de la liste B avec le prédicat passé en paramètre.</br>
	 * Si la condition est vérifiée, l'élément de la liste B est conservé et est
	 * inséré après ou remplace celui de la liste A du même index.</br>
	 * Sinon, c'est l'élément de la liste A qui est conservé.
	 * </p>
	 *
	 * @param predicate             test spécifique aux éléments de la liste B
	 * @param replaceItemsFromListA true = éléments A remplacé par élément B si test
	 *                              vérifié / false = élément B inséré après
	 *                              l'élément A du même index
	 */
	private void alternateOrReplaceIf(Predicate<String> predicate, boolean replaceItemsFromListA) {
		this.mixedList.clear();

		// alterne les éléments des listes A et B jusqu'à ce que l'une des listes soit
		// entièrement consomée
		int a = 0, b = 0;
		for (int currentStep = 1; a < listA.size() && b < listB.size(); currentStep++) {

			if (replaceItemsFromListA) {
				if (predicate.test(listB.get(b))) {
					this.mixedList.add(listB.get(b));
				} else {
					this.mixedList.add(listA.get(a));
				}
				a++;
				b++;
			} else {
				this.mixedList.add(listA.get(a));
				a++;
				if (predicate.test(listB.get(b))) {
					this.mixedList.add(listB.get(b));
				}
				b++;
			}
		}

		// complète avec les éventuels éléments restants de l'une des 2 listes
		if (completeMixedListWithExessItems) {
			while (a < listA.size()) {
				this.mixedList.add(listA.get(a));
				a++;
			}

			while (b < listB.size()) {
				if (predicate.test(listB.get(b))) {
					this.mixedList.add(listB.get(b));
				}
				b++;
			}
		}
	}

	/**
	 * alternateWithTestOn2Lists </br>
	 * <p>
	 * Ne conserve que les éléments des listes A et B qui vérifient le test
	 * spécifique à chaque liste passé en paramètre.</br>
	 * Garde les éléments de chaque liste dans l'ordre
	 * </p>
	 *
	 * @param predicateA test spécifique aux éléments de la liste A
	 * @param predicateB test spécifique aux éléments de la liste B
	 */
	private void alternateWithTestOn2Lists(Predicate<String> predicateA, Predicate<String> predicateB) {
		this.mixedList.clear();

		// alterne les éléments des listes A et B jusqu'à ce que l'une des listes soit
		// entièrement consomée
		int a = 0, b = 0;
		for (; a < listA.size() && b < listB.size(); a++, b++) {
			if (predicateA.test(listA.get(a))) {
				this.mixedList.add(listA.get(a));
			}
			if (predicateB.test(listB.get(b))) {
				this.mixedList.add(listB.get(b));
			}
		}

		// complète avec les éventuels éléments restants de l'une des 2 listes
		if (completeMixedListWithExessItems) {
			while (a < listA.size()) {
				if (predicateA.test(listA.get(a))) {
					this.mixedList.add(listA.get(a));
				}
				a++;
			}

			while (b < listB.size()) {
				if (predicateB.test(listB.get(b))) {
					this.mixedList.add(listB.get(b));
				}
				b++;
			}
		}
	}
}
