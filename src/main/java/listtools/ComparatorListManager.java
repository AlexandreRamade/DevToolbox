package listtools;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import stringtools.Increment;

/**
 * ListesSimples <br>
 * <p>
 * Outils permettant de comparer et d'effectuer des traitements simples sur 2
 * listes de chaines de caractères
 * </p>
 * 
 * @author a-ramade
 * @since 2021
 *
 */
public class ComparatorListManager {
		
		private String LIST_REF = "A\r\n" + "B\r\n" + "C\r\n" + "B";
		private String LIST_B = "A\r\n" + "B\r\n" + "D\r\n";
		
		private String SEPARATOR = "\r\n";

		private List<String> listRef;
		private List<String> listB;

		private List<String> commonItems;
		private List<String> specificToListRef;
		private List<String> specificToListB;

		
		/** ***** ***** CONSTRUCTORS ***** ***** */
		
		public ComparatorListManager() {
			listRef = Arrays.asList(LIST_REF.split(SEPARATOR));
			listB = Arrays.asList(LIST_B.split(SEPARATOR));
		}
		
		public ComparatorListManager(String listRefFromString, String listBFromString, String separator) {
			LIST_REF = listRefFromString;
			LIST_B = listBFromString;
			SEPARATOR = separator;
			listRef = Arrays.asList(LIST_REF.split(SEPARATOR));
			listB = Arrays.asList(LIST_B.split(SEPARATOR));
		}

		public ComparatorListManager(List<String> listRef, List<String> listB) {
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

		public void compareContent() {
			this.findCommonItems();
			this.findSpecificItemsInReferenceList();
			this.findSpecificItemsInListB();
		}

        public void compareContentIgnoringCase() {
            this.listRef = listRef.stream().map(String::toLowerCase).collect(Collectors.toList());
            this.listB = listB.stream().map(String::toLowerCase).collect(Collectors.toList());
            this.findCommonItems();
            this.findSpecificItemsInReferenceList();
            this.findSpecificItemsInListB();
        }
		
		
		/** ***** ***** AFFICHAGE ***** ***** */

        public void displayOnlySizeComparisonResultInConsole() {
            System.out.println(String.format(" -> Elements communs présents dans les 2 listes : %s", this.commonItems.size()));
            System.out.println(String.format(" -> Elements dans la liste de référence et non présents dans la liste B : %s", this.specificToListRef.size()));
            System.out.println(String.format(" -> Elements en trop dans la liste B et non présents dans la liste de référence : %s", this.specificToListB.size()));
        }
		
		public void displayComparisonResultInConsole(boolean showLineNumbers) {
			displayResultInConsole(" -> Elements dans la liste de référence et non présents dans la liste B : ", this.specificToListRef, showLineNumbers);
			System.out.println("\n -------------------- ");
			displayResultInConsole(" -> Elements en trop dans la liste B et non présents dans la liste de référence : ", this.specificToListB, showLineNumbers);
			//System.out.println("\n -------------------- ");
			//displayResultInConsole(" -> Elements communs présents dans les 2 listes : ", this.commonItems, showLineNumbers);
		}
		
		private void displayResultInConsole(String comment, List<String> list, boolean showLineNumbers) {
			System.out.println(comment + list.size());
			List<String> listToDispay = showLineNumbers ? addLineNumber(list, "- ") : list;
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
		



		/** ***** ***** METHODES UTILITAIRES ***** ***** */

		private static List<String> addLineNumber(List<String> list, String separator) {
			Increment increment = new Increment("0", true, true);
			return list.stream().map(str -> increment.increment() + separator + str).collect(Collectors.toList());
		}
		
		private String replaceInChain(String chainePrincipale, String patternARemplacer, String elementAInserer) {
			return Pattern.compile(patternARemplacer).matcher(chainePrincipale).replaceAll(elementAInserer);
		}
}
