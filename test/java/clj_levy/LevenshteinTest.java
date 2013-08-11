package clj_levy;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LevenshteinTest {

	@Test
	public void thatCorrectDistanceGetsCalculated () {
		assertEquals(0, Levenshtein.distanceOf("", ""));
		assertEquals(0, Levenshtein.distanceOf("foo", "foo"));
		assertEquals(3, Levenshtein.distanceOf("foo", ""));
		assertEquals(6, Levenshtein.distanceOf("", "foobar"));
		assertEquals(1, Levenshtein.distanceOf("a", "b"));
		assertEquals(2, Levenshtein.distanceOf("ab", "bc"));
		assertEquals(2, Levenshtein.distanceOf("ab", "ba"));
		assertEquals(3, Levenshtein.distanceOf("foo", "bar"));
		assertEquals(2, Levenshtein.distanceOf("lein", "lien"));
		assertEquals(4, Levenshtein.distanceOf("levenshtein", "meilenstein"));
	}
}

