package cardgames.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Player {
	private int id;
	private String name;
	private List<Card> main;
	private Optional<Card> table;
	private List<Card> poche;
	private int points = 0;
	
	
	public Player(int id, String name, List<Card> main) {
		this.id = id;
		this.name = name;
		this.main = main;
		this.poche = new ArrayList<>();
	}

	
	public Card play(Function<List<Card>, Card> playStrategy) {
		this.table = Optional.of(getCard(playStrategy));
		return this.table.get();
	}
	
	public Card getFirstCard() {
		Card card = this.main.get(0);
		this.main.remove(0);
		return card;
	}
	
	public Card getCard(Function<List<Card>, Card> playStrategy) {
		Card card = playStrategy.apply(main);
		this.main.remove(card);
		return card;
	}
	
	public void addCardToMain(Card card) {
		this.main.add(card);
	}
	
	public List<Card> addAllToPoche(List<Card> cards) {
		this.poche.addAll(cards);
		return this.poche;
	}
	
	public Card getLastCardInPoche() {
		return this.poche.get(this.poche.size() - 1);
	}
	
	public void clearMain() {
		this.main.clear();
	}
	
	public void clearPoche() {
		this.poche.clear();
	}
	
	public Card removeTable() {
		Card c = this.table.get();
		this.table = Optional.empty();
		return c;
	}
	
	public int countPochePoints() {
		return this.poche.stream().map(c -> c.getPoints()).reduce(Integer::sum).orElse(0);
	}
	
	public int countPochePointsAndAddToPlayerPoints() {
		this.points += this.countPochePoints();
		return this.points;
	}
		
	public boolean haveCardInMain() {
		return !this.main.isEmpty();
	}
	
	@Override
	public String toString() {
		return "Player [id=" + id + ", name=" + name + ", main=" + main + ", table=" + table + ", poche=" + poche
				+ ", points=" + points + "]";
	}


	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Card> getMain() {
		return main;
	}

	public void setMain(List<Card> main) {
		this.main = main;
	}

	public Optional<Card> getTable() {
		return table;
	}

	public List<Card> getPoche() {
		return poche;
	}
	
	
}
