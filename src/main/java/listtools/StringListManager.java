package listtools;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TraiterListeStrings <br>
 * <p>
 * Classe utilitaire permettant de réaliser de nombreux traitements de bases sur
 * une liste de chaines de caractères
 * </p>
 *
 * @author a-ramade
 * @since 2022
 *
 */
public class StringListManager {

    private final static String NUMBER_REGEX = "[+-]?([0-9]*[.])?[0-9]+";
    private final static String EXCTRACT_NUMBER = "(" + NUMBER_REGEX + ")";

    private Stream<String> liste;

    private List<Double> numbers;


    /** ***** ***** CONSTRUCTEURS ***** ***** */

    public StringListManager(List<String> listeStrings) {
        liste = listeStrings.stream();
    }

    public StringListManager(String onlyOneString) {
        liste = Arrays.asList(onlyOneString).stream();
    }

    public StringListManager(String stringADecouper, String separateur) {
        liste = Arrays.asList(stringADecouper.split(separateur)).stream();
    }

    
    /** ***** ***** METHODES GET ***** ***** */
    
    public Optional<String> getFirstValue() {
    	return this.liste.findAny();
    }

    public List<String> getListe() {
    	return this.liste.collect(Collectors.toList());
    }
    
    public <T> List<T> castAndGetListe(Function<String, T> function) {
    	return this.liste.map(function::apply).collect(Collectors.toList());
    }

    public List<Double> getListeNumbers() {
        return numbers;
    }


    /** ***** ***** METHODES DE TRAITEMENT ***** ***** */

    public StringListManager ajouterGuillemets() {
        liste = liste.map(str -> String.format("\"%s\"", str));
        return this;
    }

    public StringListManager ajouterMotifs(String prefixe, String sufixe) {
        liste = liste.map(str -> String.format("%s%s%s", prefixe, str, sufixe));
        return this;
    }

    public StringListManager remplacerMotif(String motifARemplacer, String elementAInserer) {
        Pattern pattern = Pattern.compile(motifARemplacer);
        liste = liste.map(str -> pattern.matcher(str).replaceAll(elementAInserer));
        return this;
    }

    public StringListManager substringStartTo(int end) {
    	liste = liste.map(str -> str.substring(0, end));
        return this;
    }

    public StringListManager substringFromToEnd(int start) {
    	liste = liste.map(str -> str.substring(start));
        return this;
    }

    public StringListManager substrings(int start, int end) {
    	liste = liste.map(str -> str.substring(start, end));
        return this;
    }

    /**
     * extraireMotif<br>
     * <p>
     * extrait un motif dans chaque élément de la liste<br>
     * Exemple d'utilisation :<br>
     * liste = DV_NUMDECL=1234, DV_NUMSINI=5678, DV_CODNATU=BDG
     * extraireMotif("DV_[A-Z]{7}") => DV_NUMDECL, DV_NUMSINI, DV_CODNATU
     * </p>
     *
     * @param motifAExtraire
     * @return l'instance de TraiterListeStrings
     */
    public StringListManager extraireMotif(String motifAExtraire) {
        Pattern pattern = Pattern.compile(motifAExtraire);
        liste = liste.map(str -> {
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()) {
                return matcher.group();
            }
            return null;
        }).filter(Objects::nonNull);
        return this;
    }

    /**
     * extraireUneSeuleVariableParLigne<br>
     * <p>
     * extrait valeur variable au sein d'un motif constant une seule fois par ligne (la première occurrence)<br>
     * Exemple d'utilisation :<br>
     * liste = action: imprimer, action: sauvegarder, action: restaurer<br>
     * extraireVariable("action: (.*)") => imprimer, sauvegarder, restaurer
     * </p>
     * <u>utiliser le motif (.*) (ou (.*?) en mode Non-Greedy) pour identifier la partie variable à
     * extraire</u>
     *
     * @param motifAvecVariableAExtraire
     * @return l'instance de TraiterListeStrings
     */
    public StringListManager extraireVariableUneSeuleFoisParLigne(String motifAvecVariableAExtraire) {
        Pattern pattern = Pattern.compile(motifAvecVariableAExtraire);
        liste = liste.map(str -> {
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()) {
                return matcher.group(1);
            }
            return null;
        }).filter(Objects::nonNull);
        return this;
    }

    /**
     * extraireVariablePlusieursFoirParLigne<br>
     * <p>
     * extrait une valeur variable au sein d'un motif constant autant de fois que le motif apparait dans la ligne (plusieurs occurrences par lignes)<br>
     * Exemple d'utilisation :<br>
     * liste = action: imprimer, action: sauvegarder, action: restaurer<br>
     * extraireVariable("action: (.*)") => imprimer, sauvegarder, restaurer
     * </p>
     * <u>utiliser le motif (.*) (ou (.*?) en mode Non-Greedy) pour identifier la partie variable à
     * extraire</u>
     *
     * @param motifAvecVariableAExtraire
     * @return l'instance de TraiterListeStrings
     */
    public StringListManager extraireVariablePlusieursFoisParLigne(String motifAvecVariableAExtraire) {
        Pattern pattern = Pattern.compile(motifAvecVariableAExtraire);
        liste = liste.flatMap(str -> pattern.matcher(str).results().map(result -> result.group(1)));
        return this;
    }

    /**
     * extraireNthOccurrenceDeLaLigne<br>
     * <p>
     * extrait valeur énième variable de la ligne au sein d'un motif constant (la énième occurrence)<br>
     * Exemple d'utilisation :<br>
     * liste = resultats : 10 20 30, résultats : 55 66 77, résultats : 105 222 359<br>
     * extraireVariable("([0-9]+)", 3) => 30, 77, 359
     * </p>
     * <u>utiliser le motif (.*) (ou (.*?) en mode Non-Greedy) pour identifier la partie variable à
     * extraire</u>
     *
     * @param motifAvecVariableAExtraire
     * @return l'instance de TraiterListeStrings
     */
    public StringListManager extraireNthOccurrenceDeLaLigne(String motifAvecVariableAExtraire, int n) {
        Pattern pattern = Pattern.compile(motifAvecVariableAExtraire);
        liste = liste.map(str -> {
            Matcher matcher = pattern.matcher(str);
            int count = 0;
            while (matcher.find()) {
                if (++count == n) {
                    return matcher.group(1);
                }
            }
            return null;
        }).filter(Objects::nonNull);
        return this;
    }

    public StringListManager filtrer(Predicate<String> filtre) {
        liste = liste.filter(filtre);
        return this;
    }

    public StringListManager filtrerSiContient(String motifAChercher) {
        String motif = String.format(".*%s.*", motifAChercher);
        liste = liste.filter(str -> str.matches(motif));
        return this;
    }

    public StringListManager removeSiContient(String motifAChercher) {
        String motif = String.format(".*%s.*", motifAChercher);
        liste = liste.filter(str -> !str.matches(motif));
        return this;
    }

    /**
     * ordonnerListe <br>
     * <p>
     * ordonne les éléments de la liste selon l'ordre par défaut des caractères de
     * la table ASCII
     * </p>
     *
     * @return l'instance de TraiterListeStrings
     */
    public StringListManager ordonnerListe() {
        liste = liste.sorted();
        return this;
    }

    /**
     * ordonnerListe <br>
     * <p>
     * ordonne les éléments de la liste avec un comparateur personnalisé à définir
     * </p>
     *
     * @param comparator
     *            comparateur personnalisé
     * @return l'instance de TraiterListeStrings
     */
    public StringListManager ordonnerListe(Comparator<String> comparator) {
        liste = liste.sorted(comparator);
        return this;
    }

    /**
     * ordonnerListe <br>
     * <p>
     * ordonne les éléments de la liste en prenant en compte une sous partie de la
     * chaine correspondant à un motif réccurent<br>
     * Exeple d'utilisation : <br>
     * liste = action A: imprimer, action B: sauvegarder, action C: restaurer<br>
     * ordonnerListe(": [a-z]+") => action A: imprimer, action C: restaurer, action
     * B: sauvegarder
     * </p>
     *
     * @param motifDeComparaison
     *            motif dans la chaine devant servir à ordonner la liste
     * @return l'instance de TraiterListeStrings
     */
    public StringListManager ordonnerListe(String motifDeComparaison) {
        Pattern pattern = Pattern.compile(motifDeComparaison);
        liste = liste.sorted(new Comparator<String>() {

            @Override
            public int compare(String str1, String str2) {
                Matcher matcher1 = pattern.matcher(str1);
                if (matcher1.find()) {
                    str1 = matcher1.group();
                }
                Matcher matcher2 = pattern.matcher(str2);
                if (matcher2.find()) {
                    str2 = matcher2.group();
                }
                return str1.compareTo(str2);
            }

        });
        return this;
    }

    private int iterator = 0;

    /**
     * grouperElementsConsecutifs <br>
     * <p>
     * concatène plusieurs éléments consécutifs de la liste en les séparant avec le
     * séparateur passé en paramètre <br>
     * Exemple d'utilisation : <br>
     * liste = A, B, C, D, E, F, G, H, I, J, K <br>
     * grouperElementsConsecutifs(3, "-") => A-B-C, D-E-F, G-H-I, J-K
     * </p>
     *
     * @param nombreElementsConsecutifsAGrouper
     * @param separateur
     * @return l'instance de TraiterListeStrings
     */
    public StringListManager grouperElementsConsecutifs(int nombreElementsConsecutifsAGrouper, String separateur) {
        iterator = 0;
        liste = liste.collect(Collectors.groupingBy(it -> iterator++ / nombreElementsConsecutifsAGrouper)).values()
                .stream().map(value -> String.join(separateur, value));
        return this;
    }
    
    /**
     * grouperElementsConsecutifsSi <br>
     * <p>
     * concatène plusieurs éléments consécutifs de la liste en les séparant avec le
     * séparateur passé en paramètre uniquement si la ligne courante ou la ligne suivante
     * valide la condition définie par le prédicat passé en paramètre <br>
     * Exemple d'utilisation : <br>
     * liste = A, b, C, D, e, f, g, H, I, J, k <br>
     * grouperElementsConsecutifs(isLowerCase, "-") => A-b-C, D-e-f-g-H, I, J-k
     * </p>
     *
     * @param predicate conditions à valider sur la ligne courante et/ou la ligne précédente
     * @param separateur
     * @return l'instance de TraiterListeStrings
     */
    public StringListManager grouperElementsConsecutifsSi(BiPredicate<String, String> predicate, String separateur) {
    	List<String> source = liste.collect(Collectors.toList());
    	List<String> result = new LinkedList<>();
    	
    	result.add(source.get(0));
    	for(int i = 0; i < source.size() -1; i++) {
    		String current = result.get(result.size() - 1);
    		String next = source.get(i + 1);
    		if(predicate.test(current, next)) {
    			result.remove(result.size() - 1);
    			result.add(current.concat(separateur).concat(next));
    		} else {
    			result.add(next);
    		}
    	}
    	
    	liste = result.stream();
    	return this;
    }
    

    public StringListManager appliquerStringFormatPattern(String stringFormatPattern) {
        int occurrence = 0;
        Matcher m = Pattern.compile("%s").matcher(stringFormatPattern);
        while (m.find()) {
            occurrence++;
        }
        if (occurrence == 1) {
            liste = liste.map(str -> String.format(stringFormatPattern, str));
        } else if (occurrence == 2) {
            liste = liste.map(str -> String.format(stringFormatPattern, str, str));
        } else if (occurrence == 3) {
            liste = liste.map(str -> String.format(stringFormatPattern, str, str, str));
        }
        return this;
    }

    public StringListManager traitementPersonnalise(Function<String, String> function) {
        liste = liste.map(function);
        return this;
    }

    public StringListManager toLowerCase() {
        liste = liste.map(String::toLowerCase);
        return this;
    }

    public StringListManager supprimerDoublons() {
        liste = liste.distinct();
        return this;
    }

    public StringListManager supprimerElementSiNull() {
        liste = liste.filter(Objects::nonNull);
        return this;
    }

    public StringListManager supprimerElementSiEmptyOrBlank() {
        liste = liste.filter(String::isBlank);
        return this;
    }

    public StringListManager supprimerEspaces() {
        liste = liste.map(str -> str.trim());
        return this;
    }

    public StringListManager supprimerGuillemets() {
        liste = liste.map(str -> str.replaceAll("\"", ""));
        return this;
    }

    public StringListManager countOcurenceInEachLine(String motif) {
        Pattern pattern = Pattern.compile(motif);
        liste = liste.map(str -> String.format("%s apparait %d fois dans : %s", motif, countMatches(pattern, str), str));
        return this;
    }

    private int countMatches(Pattern pattern, String sequence) {
        Matcher matcher = pattern.matcher(sequence);
        int occurence = 0;
        while (matcher.find()) {
            occurence++;
        }
        return occurence;
    }
    
    public StringListManager decouperChaqueElement(String separator) {
    	this.liste = this.liste.flatMap(str -> Arrays.asList(str.split(separator)).stream());
    	return this;
    }

    /** ***** ***** METHODES DE TRAITEMENT d'une liste de nombres ***** ***** */

    /**
     * extraireNombres<br>
     * <p>
     * extrait une valeur numérique au sein d'un motif constant une seule fois par ligne (la première occurrence)<br>
     * Exemple d'utilisation :<br>
     * liste = montant : +5.2 €, montant : +10 €, montant : -3.0 €<br>
     * extraireVariable("montant : NUMBER €") => 5.2, 10.0, -3.0
     * </p>
     * <u>utiliser le motif NUMBER pour identifier la localisation de la valeur à extraire</u>
     *
     * @param motifAvecNombreAExtraire
     * @return l'instance de TraiterListeStrings
     */
    public StringListManager extraireNombres(String motifAvecNombreAExtraire) {
        this.extraireVariableUneSeuleFoisParLigne(motifAvecNombreAExtraire.replaceAll("NUMBER", EXCTRACT_NUMBER));
        this.numbers = castAndGetListe(Double::parseDouble);
        return this;
    }

    /**
     * extraireNombres<br>
     * <p>
     * extrait la énième valeur numérique de la ligne (la énième occurrence)<br>
     * Exemple d'utilisation :<br>
     * liste = Prix HT +5.2€ TTC +6.5€, Prix HT +10€ TTC +12€, Prix HT -3€ TTC -4.2€<br>
     * extraireNombres(2) => 6.5, 12, -4.2
     * </p>
     *
     * @param nthOccurrence
     * @return l'instance de TraiterListeStrings
     */
    public StringListManager extraireNombres(int nthOccurrence) {
        this.extraireNthOccurrenceDeLaLigne(EXCTRACT_NUMBER, nthOccurrence);
        this.numbers = castAndGetListe(Double::parseDouble);
        return this;
    }

    public Double calculerSomme() {
        return numbers.stream().reduce(0.0, Double::sum);
    }

    public Double calculerMoyenne() {
        return calculerSomme() / numbers.size();
    }

    public void afficherListeNombreInfos() {
        List<Double> ordonnee = numbers.stream().sorted().collect(Collectors.toList());

        System.out.println("----- ----- Liste de nombres ----- -----");
        System.out.println(String.format("Nombre d'éléments dans la liste = %d", numbers.size()));
        System.out.println(String.format("Valeur la plus petite = %f", ordonnee.get(0)));
        System.out.println(String.format("Valeur la plus grande = %f", ordonnee.get(numbers.size() - 1)));
        System.out.println(String.format("Somme des valeurs = %f", calculerSomme()));
        System.out.println(String.format("Moyenne des valeurs = %f", calculerMoyenne()));
        System.out.println("----- ----- ----- ----- -----");
    }

    /** ***** ***** AFFICHAGE DU RESULTAT ***** ***** */

    public void testerEtAfficherDoublons() {
        List<String> listeCourrante = liste.collect(Collectors.toList());
        System.out.println(String.format("Nombre d'éléments dans la liste avant filtrage = %d", listeCourrante.size()));

        Map<String, Long> apparition = listeCourrante.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        if(apparition.keySet().size() != listeCourrante.size()) {
            Long nombreDeDoublons = apparition.values().stream()
                    .filter(nb -> nb > 1)
                    .reduce(0L, Long::sum);
            List<String> doublons = apparition.entrySet().stream()
                    .filter(e -> e.getValue() > 1)
                    .map(entry -> entry.getValue() + " : " + entry.getKey())
                    .collect(Collectors.toList());

            System.out.println("Nombre de doublons = " + nombreDeDoublons);
            System.out.println(String.format("Nombre d'éléments dans la liste sans doublons = %d", (listeCourrante.size() - nombreDeDoublons)));
            System.out.println("Liste des doublons : ");
            doublons.forEach(System.out::println);

        } else {
            System.out.println("Aucun doublons détecté");
        }

    }

    public StringListManager afficherTailleListePuis() {
        List<String> listeCourrante = liste.collect(Collectors.toList());
        System.out.println(String.format("Nombre d'éléments dans la liste = %d", listeCourrante.size()));
        liste = listeCourrante.stream();
        return this;
    }

    public void afficherTailleListe() {
        System.out.println(String.format("Nombre d'éléments dans la liste = %d", liste.count()));
    }

    public void afficherListe() {
        liste.forEach(System.out::println);
    }

    public void concatenerEtAfficher() {
        System.out.println(liste.collect(Collectors.joining()));
    }

    public void afficherEnLigne(String separateur) {
        System.out.println(liste.collect(Collectors.joining(separateur)));
    }
}

