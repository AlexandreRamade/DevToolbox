package cardgames.comparators;

import java.util.Comparator;

import cardgames.model.Card;

public class ForceComparator implements Comparator<Card> {

	@Override
	public int compare(Card card1, Card card2) {
		return card1.getForce() - card2.getForce();
	}

}
