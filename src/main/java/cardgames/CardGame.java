package cardgames;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cardgames.enums.Figure;
import cardgames.enums.Forme;
import cardgames.model.Card;
import cardgames.model.Player;

public class CardGame {
	
	private List<Card> cards;
	
	private Map<Integer, List<Card>> cardsPlayers;
	
	private List<Player> players;
	
		
	public CardGame(List<Card> cards) {
		this.cards = cards;
		this.players = new LinkedList<>();
	}

	public static CardGame generateGameOf52Cards() {
		return new CardGame(generateCardsGame(Arrays.asList(Forme.values()), Arrays.asList(Figure.values())));
	}
	
	public static CardGame generateGameOf32Cards() {
		return new CardGame(generateCardsGame(Arrays.asList(Forme.values()), Arrays.asList(Figure.values()))
				.stream().sorted().limit(32).collect(Collectors.toList()));
	}
	
	public static List<Card> generateCardsGame(List<Forme> formes, List<Figure> figures) {
		return formes.stream().flatMap(forme -> generateCardsByForme(forme, figures)).collect(Collectors.toList());
	}
	
	private static Stream<Card> generateCardsByForme(Forme forme, List<Figure> figures) {
		return figures.stream().map(figure -> new Card(forme, figure));
	}
	
	public CardGame shuffleCards() {
		Collections.shuffle(this.cards);
		return this;
	}
	
	private int iterator = 0;
	
	private void distributeCards(int nbDeJoueurs) {
		this.iterator = 0;
		this.cardsPlayers = this.cards.stream().collect(Collectors.groupingBy(c -> iterator++ % nbDeJoueurs));
	}
	
	private void distributeCardsLeavingADrawPile(int nbDeJoueurs, int ndDeCarteDeLaPioche) {
		this.iterator = 0;
		this.cardsPlayers = this.cards.subList(ndDeCarteDeLaPioche, cards.size()).stream().collect(Collectors.groupingBy(c -> iterator++ % nbDeJoueurs));
		this.cardsPlayers.put(-1, this.cards.subList(0, ndDeCarteDeLaPioche));
	}
	
	private void attribuerCardsToPlayers(List<String> playerNames) {
		if(!this.cardsPlayers.isEmpty() && !playerNames.isEmpty()) {
			for(int i = 0; i < playerNames.size(); i++) {
				this.players.add(new Player(i, playerNames.get(i), this.cardsPlayers.get(i)));
			}
		}
	}
	
	public CardGame distributeCardsToPlayers(List<String> playerNames) {
		distributeCards(playerNames.size());
		attribuerCardsToPlayers(playerNames);
		return this;
	}
	
	public CardGame play(Function<List<Card>, Card> strategy) {
		while(players.stream().allMatch(Player::haveCardInMain)) {
			players.stream().forEach(p -> p.play(strategy));
			displayTable();
			Player gagnant = players.stream().max((p1, p2) -> p1.getTable().get().compareTo(p2.getTable().get())).get();
			System.out.println("Gagnant = " + gagnant.getName());
			gagnant.addAllToPoche(players.stream().map(Player::removeTable).collect(Collectors.toList()));
		}
		players.stream().forEach(Player::countPochePointsAndAddToPlayerPoints);
		return this;
	}

	public List<Card> getCards() {
		return cards;
	}

	public Map<Integer, List<Card>> getCardsPlayers() {
		return cardsPlayers;
	}

	public List<Player> getPlayers() {
		return players;
	}
	
	
	private void displayTable() {
		System.out.print("Table : ");
		this.players.stream().forEach(p -> System.out.print(p.getName() + " = " + p.getTable().get() + " | "));
	}
	
	public void displayPlayers() {
		this.players.stream().forEach(System.out::println);
	}
	

}
