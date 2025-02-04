package cardgames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cardgames.enums.Figure;
import cardgames.enums.Forme;
import cardgames.model.Card;
import cardgames.model.WeightedFigure;
import cardgames.rules.BeloteWeightedCards;

public class CardSetGenerator {

	private List<Card> cards = new ArrayList<>();

	public void generateGameOf52Cards() {
		this.cards = generateCardsGame(Arrays.asList(Forme.values()), Arrays.asList(Figure.values()));
	}

	public void generateGameOf32Cards() {
		this.cards = generateCardsGame(Arrays.asList(Forme.values()), Arrays.asList(Figure.values())).stream().sorted()
				.limit(32).collect(Collectors.toList());
	}

	public List<Card> generateCardsGame(List<Forme> formes, List<Figure> figures) {
		return formes.stream().flatMap(forme -> generateCardsByForme(forme, figures)).collect(Collectors.toList());
	}

	private Stream<Card> generateCardsByForme(Forme forme, List<Figure> figures) {
		return figures.stream().map(figure -> new Card(forme, figure));
	}

	public CardSetGenerator createCards(List<Forme> formes, List<WeightedFigure> weightedFigures) {
		for (Forme forme : formes) {
			for (WeightedFigure wf : weightedFigures) {
				this.cards.add(new Card(forme, wf.figure, wf.points, wf.force));
			}
		}
		return this;
	}

	public CardSetGenerator genererBeloteCardGame(Forme atout) {
		createCards(Arrays.asList(atout), BeloteWeightedCards.getWeightedFigures().filter(wf -> wf.atout).collect(Collectors.toList()));
		
		List<Forme> nonAtout = Arrays.asList(Forme.values()).stream().filter(f -> !f.equals(atout)).collect(Collectors.toList());
		createCards(nonAtout, BeloteWeightedCards.getWeightedFigures().filter(wf -> !wf.atout).collect(Collectors.toList()));
		return this;
	}

	public List<Card> getCards() {
		return cards;
	}
	
	
}
