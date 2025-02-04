package cardgames.rules;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cardgames.enums.Figure;
import cardgames.model.WeightedFigure;

public enum BeloteWeightedCards {
	ATOUT_VALET(Figure.VALET, 20, 15, true),
	ATOUT_NEUF(Figure.NEUF, 14, 14, true),
	ATOUT_AS(Figure.AS, 11, 13, true),
	ATOUT_DIX(Figure.DIX, 10, 12, true),
	ATOUT_ROI(Figure.ROI, 4, 11, true),
	ATOUT_DAME(Figure.DAME, 3, 10, true),
	ATOUT_HUIT(Figure.HUIT, 0, 9, true),
	ATOUT_SEPT(Figure.SEPT, 0, 8, true),
	AS(Figure.AS, 11, 7, false),
	DIX(Figure.DIX, 10, 6, false),
	ROI(Figure.ROI, 4, 5, false),
	DAME(Figure.DAME, 3, 4, false),
	VALET(Figure.VALET, 2, 3, false),
	NEUF(Figure.NEUF, 0, 2, false),
	HUIT(Figure.HUIT, 0, 1, false),
	SEPT(Figure.SEPT, 0, 0, false);
	
	private Figure figure;
	private int points;
	private int weight;
	private boolean atout;
	
	
	private BeloteWeightedCards(Figure figure, int points, int weight, boolean atout) {
		this.figure = figure;
		this.points = points;
		this.weight = weight;
		this.atout = atout;
	}
	
	public static Stream<WeightedFigure> getWeightedFigures() {
		return Arrays.asList(BeloteWeightedCards.values()).stream()
				.map(wc -> new WeightedFigure(wc.figure, wc.points, wc.weight, wc.atout));
	}
	
}
