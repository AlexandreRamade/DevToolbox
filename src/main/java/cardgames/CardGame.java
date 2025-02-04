package cardgames;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import cardgames.model.Card;
import cardgames.model.Player;

public class CardGame {
	
	private List<Card> cards;
	
	private Map<Integer, List<Card>> cardsPlayers;
	
	private List<Player> players;
	
	private Player pioche;
	
		
	public CardGame(List<Card> cards) {
		this.cards = cards;
		this.players = new LinkedList<>();
	}

	
	public CardGame shuffleCards() {
		Collections.shuffle(this.cards);
		return this;
	}
	
	public CardGame distributeCardsToPlayers(List<String> playerNames) {
		distributeCardsToPlayersAndDrawPile(playerNames, 0);
		return this;
	}
	
	public CardGame distributeCardsToPlayersAndDrawPile(List<String> playerNames, int ndDeCarteDeLaPioche) {
		distributeCards(playerNames.size(), ndDeCarteDeLaPioche);
		attributeCardsToPlayers(playerNames);
		return this;
	}
	
	public CardGame distributeCardsToPlayersAndLeavingADrawPile(List<String> playerNames, int ndDeCarteParJoueur) {
		int ndDeCarteDeLaPioche = this.cards.size() - (playerNames.size() * ndDeCarteParJoueur);
		distributeCardsToPlayersAndDrawPile(playerNames, ndDeCarteDeLaPioche);
		return this;
	}
	
	private int iterator = 0;
		
	private void distributeCards(int nbDeJoueurs, int ndDeCarteDeLaPioche) {
		this.iterator = 0;
		if(ndDeCarteDeLaPioche > 0) {
			this.pioche = new Player(-1, "Pioche", this.cards.subList(0, ndDeCarteDeLaPioche));
			this.cardsPlayers = this.cards.subList(ndDeCarteDeLaPioche, cards.size()).stream().collect(Collectors.groupingBy(c -> iterator++ % nbDeJoueurs));
		} else {
			this.cardsPlayers = this.cards.stream().collect(Collectors.groupingBy(c -> iterator++ % nbDeJoueurs));
		}
	}
	
	private void attributeCardsToPlayers(List<String> playerNames) {
		if(!this.cardsPlayers.isEmpty() && !playerNames.isEmpty()) {
			for(int i = 0; i < playerNames.size(); i++) {
				this.players.add(new Player(i, playerNames.get(i), this.cardsPlayers.get(i)));
			}
		}
	}
	
	
	public CardGame play(Function<List<Card>, Card> playStrategy, Comparator<Card> winnerComparator) {
		this.play(playStrategy, winnerComparator, null);
		return this;
	}
	
	public CardGame playWithDrawPile(Function<List<Card>, Card> playStrategy, Comparator<Card> winnerComparator, Predicate<Player> drawInTheDrawPileStrategy) {
		this.play(playStrategy, winnerComparator, drawInTheDrawPileStrategy);
		return this;
	}
	
	private void play(Function<List<Card>, Card> playStrategy, Comparator<Card> winnerComparator, Predicate<Player> drawInTheDrawPileStrategy) {
		while(players.stream().allMatch(Player::haveCardInMain)) {
			players.stream().forEach(p -> p.play(playStrategy));
			displayTable();
			Player gagnant = players.stream().max((p1, p2) -> winnerComparator.compare(p1.getTable().get(), p2.getTable().get())).get();
			System.out.println("Gagnant = " + gagnant.getName());
			gagnant.addAllToPoche(players.stream().map(Player::removeTable).collect(Collectors.toList()));
			
			// play with Draw Pile
			if(drawInTheDrawPileStrategy != null) {
				players.stream().filter(drawInTheDrawPileStrategy::test).forEach(p -> {
					if(this.pioche.haveCardInMain()) {
						p.addCardToMain(this.pioche.getFirstCard());
					}
				});
			}
		}
		players.stream().forEach(Player::countPochePointsAndAddToPlayerPoints);
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
		if(this.pioche != null) {
			System.out.println(this.pioche);
		}
	}
	

}
