package cardgames.comparators;

import java.util.Comparator;

import cardgames.model.Card;

public class PointsComparator implements Comparator<Card> {

	@Override
	public int compare(Card card1, Card card2) {
		return card2.getPoints() - card1.getPoints();
	}

}
