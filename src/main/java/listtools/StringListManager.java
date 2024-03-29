package listtools;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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

    private Stream<String> liste;

    private static final String SEPARATEUR = "\n";

    private String LISTE_REF = "";


    /** ***** ***** CONSTRUCTEURS ***** ***** */

    public StringListManager() {
        extraireListe();
    }

    public StringListManager(List<String> listeStrings) {
        liste = listeStrings.stream();
    }

    public StringListManager(String onlyOneString) {
        liste = Arrays.asList(onlyOneString).stream();
    }

    public StringListManager(String stringADecouper, String separateur) {
        liste = Arrays.asList(stringADecouper.split(separateur)).stream();
    }

    private void extraireListe() {
        liste = Arrays.asList(LISTE_REF.split(SEPARATEUR)).stream();
    }

    /** ***** ***** METHODES UTILITAIRES ***** ***** */

    private static String extraireValeur(Pattern pattern, String source) {
        Matcher matcher = pattern.matcher(source);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
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
     * extraireVariable<br>
     * <p>
     * extrait une valeur variable au sein d'un motif constant<br>
     * Exemple d'utilisation :<br>
     * liste = action: imprimer, action: sauvegarder, action: restaurer<br>
     * extraireVariable("action: (.*)") => imprimer, sauvegarder, restaurer
     * </p>
     * <u>utiliser le motif (.*) ou (.*?) pour identifier la partie variable à
     * extraire</u>
     *
     * @param motifAvecVaribleAExtraire
     * @return l'instance de TraiterListeStrings
     */
    public StringListManager extraireVariable(String motifAvecVaribleAExtraire) {
        String motif = String.format(".*%s.*", motifAvecVaribleAExtraire);
        Pattern pattern = Pattern.compile(motifAvecVaribleAExtraire);
        liste = liste.filter(str -> str.matches(motif)).map(str -> {
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()) {
                return matcher.group(1);
            }
            return null;
        });
        return this;
    }

    public StringListManager filtrer(Predicate<String> filtre) {
        liste = liste.filter(filtre);
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
     * liste = A, B, C, D, E, F, G, I, J, K <br>
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

    public StringListManager supprimerDoublons() {
        liste = liste.distinct();
        return this;
    }

    public StringListManager supprimerEspaces() {
        liste = liste.map(str -> str.trim());
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

    /** ***** ***** AFFICHAGE DU RESULTAT ***** ***** */

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

    public void afficherEnLigne(String separateur) {
        System.out.println(liste.collect(Collectors.joining(separateur)));
    }
}

