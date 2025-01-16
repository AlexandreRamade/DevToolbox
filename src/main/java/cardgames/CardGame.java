package cardgames;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cardgames.enums.Figure;
import cardgames.enums.Forme;

public class CardGame {
	
	public static List<Card> generateGameOf52Cards() {
		return generateCardsGame(Arrays.asList(Forme.values()), Arrays.asList(Figure.values()));
	}
	
	public static List<Card> generateGameOf32Cards() {
		return generateCardsGame(Arrays.asList(Forme.values()), Arrays.asList(Figure.values())).stream().sorted().limit(32).collect(Collectors.toList());
	}
	
	public static List<Card> generateCardsGame(List<Forme> formes, List<Figure> figures) {
		return formes.stream().flatMap(forme -> generateCardsByForme(forme, figures)).collect(Collectors.toList());
	}
	
	private static Stream<Card> generateCardsByForme(Forme forme, List<Figure> figures) {
		return figures.stream().map(figure -> new Card(forme, figure));
	}
	
	public static List<Card> shuffleCards(List<Card> cards) {
		Collections.shuffle(cards);
		return cards;
	}
	
	static int iterator = 0;
	
	public static Map<Integer, List<Card>> distributeCards(List<Card> cards, int nbDeJoueurs) {
		iterator = 0;
		return cards.stream().collect(Collectors.groupingBy(c -> iterator++ % nbDeJoueurs));
	}
	
	public static Map<Integer, List<Card>> distributeCardsLeavingADrawPile(List<Card> cards, int nbDeJoueurs, int ndDeCarteDeLaPioche) {
		iterator = 0;
		Map<Integer, List<Card>> play = cards.subList(ndDeCarteDeLaPioche, cards.size()).stream().collect(Collectors.groupingBy(c -> iterator++ % nbDeJoueurs));
		play.put(-1, cards.subList(0, ndDeCarteDeLaPioche));
		return play;
	}
	

}
