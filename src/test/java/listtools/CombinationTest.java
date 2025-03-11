package listtools;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class CombinationTest {

//	@Test
//	void whenOnly1ElementReturnThisElement() {
//		List<String> bouchon = Arrays.asList("A");
//		List<List<String>> result = Combination.combinate(bouchon);
//		assertThat(result.size(), is(1));
//		assertThat(result.get(0).size(), is(1));
//		assertThat(result.get(0), hasItems("A"));
//	}
	
	@Test
	void when2ElementsReturn2Combinations() {
		List<String> bouchon = Arrays.asList("A", "B");
		List<List<String>> result = Combination.combinate(bouchon);
		assertThat(result.size(), is(2));
		assertThat(result.get(0).size(), is(2));
		assertThat(result.get(0), hasItems("A", "B"));
		assertThat(result.get(1), hasItems("B", "A"));
	}
	
	@Test
	void when3ElementsReturn6Combinations() {
		List<String> bouchon = Arrays.asList("A", "B", "C");
		List<List<String>> result = Combination.combinate(bouchon);
		assertThat(result.size(), is(6));
		assertThat(result.get(0).size(), is(3));
		assertThat(result.get(0), hasItems("A", "B", "C"));
		assertThat(result.get(1), hasItems("A", "C", "B"));
		assertThat(result.get(2), hasItems("B", "A", "C"));
		assertThat(result.get(3), hasItems("B", "C", "A"));
		assertThat(result.get(4), hasItems("C", "A", "B"));
		assertThat(result.get(5), hasItems("C", "B", "A"));
	}
	
	@Test
	void when4ElementsReturn24Combinations() {
		List<String> bouchon = Arrays.asList("A", "B", "C", "D");
		List<List<String>> result = Combination.combinate(bouchon);
		assertThat(result.size(), is(24));
		assertThat(result.get(0).size(), is(4));
		assertThat(result.get(0), hasItems("A", "B", "C", "D"));
		assertThat(result.get(1), hasItems("A", "B", "D", "C"));
		assertThat(result.get(2), hasItems("A", "C", "B", "D"));
		assertThat(result.get(3), hasItems("A", "C", "D", "B"));
		assertThat(result.get(4), hasItems("A", "D", "A", "B"));
		assertThat(result.get(5), hasItems("A", "D", "B", "A"));
		assertThat(result.get(6), hasItems("B", "A", "C", "D"));

	}

}
