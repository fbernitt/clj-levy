package clj_levy;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DamerauLevenshteinTest {

	@Test
	public void thatCorrectDistanceGetsCalculated() {
		assertEquals(0, DamerauLevenshtein.distanceOf("", ""));
		assertEquals(3, DamerauLevenshtein.distanceOf("foo", ""));
		assertEquals(6, DamerauLevenshtein.distanceOf("", "foobar"));
		assertEquals(1, DamerauLevenshtein.distanceOf("a", "b"));
		assertEquals(2, DamerauLevenshtein.distanceOf("ab", "bc"));
		assertEquals(1, DamerauLevenshtein.distanceOf("ab", "ba"));
		assertEquals(3, DamerauLevenshtein.distanceOf("foo", "bar"));
		assertEquals(1, DamerauLevenshtein.distanceOf("lein", "lien"));
		assertEquals(4, DamerauLevenshtein.distanceOf("levenshtein", "meilenstein"));
	}
}

