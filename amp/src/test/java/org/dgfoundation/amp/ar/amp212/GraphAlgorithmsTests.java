package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

import org.dgfoundation.amp.algo.Graph;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.junit.Test;

import com.google.common.base.Function;

/**
 * 
 * testcases for Currency Deflator (https://jira.dgfoundation.org/browse/AMP-20534)
 * @author Constantin Dolghier
 *
 */
public class GraphAlgorithmsTests extends AmpTestCase {
	
	public GraphAlgorithmsTests() {
		super("graph algorithms tests");
	}
	
	public final static Function<String, Collection<String>> NO_DEPENDENCIES = new MapDrivenDepenciesSource(null);
	
	@Test
	public void testToposortNoDependencies() {
		Graph<String> graph = new Graph<String>(Arrays.asList("one", "two", "three", "four"), NO_DEPENDENCIES);
		assertEquals("[one, two, three, four]", graph.sortTopologically().toString());
	}

	@Test
	public void testToposortEmpty() {
		Graph<String> graph = new Graph<String>(Collections.<String>emptyList(), NO_DEPENDENCIES);
		assertEquals("[]", graph.sortTopologically().toString());
	}

	
	@Test
	public void testToposortLongChain() {
		Graph<String> graph = new Graph<String>(Arrays.asList("one"), new MapDrivenDepenciesSource(
			new HashMap<String, Collection<String>>(){{
				put("one", Arrays.asList("two"));
				put("three", Arrays.asList("four"));
				put("four", Arrays.asList("five"));
				put("two", Arrays.asList("three"));
			}}));
		assertEquals("[five, four, three, two, one]", graph.sortTopologically().toString());
	}

	@Test
	public void testToposortShortCycle() {
		Graph<String> graph = new Graph<String>(Arrays.asList("one", "three"), new MapDrivenDepenciesSource(
			new HashMap<String, Collection<String>>(){{
				put("one", Arrays.asList("two"));
				put("two", Arrays.asList("one"));
			}}));
		assertEquals("[five, four, three, two, one]", graph.sortTopologically().toString());
		//shouldFail(runnable, message);
	}

	
	@Test
	public void testToposortDependsOnItself() {
		Graph<String> graph = new Graph<String>(Arrays.asList("one", "two", "three"), new MapDrivenDepenciesSource(
			new HashMap<String, Collection<String>>(){{
				put("one", Arrays.asList("two"));
				put("three", Arrays.asList("three"));
			}}));
		assertEquals("[five, four, three, two, one]", graph.sortTopologically().toString());
	}

	//TODO: move to jdk8
	static class MapDrivenDepenciesSource implements Function<String, Collection<String>> {
		final Map<String, Collection<String>> src;
		public MapDrivenDepenciesSource(Map<String, Collection<String>> src) {
			this.src = src;
		}
		
		@Override
		public Collection<String> apply(String input) {
			if (src == null || !src.containsKey(input))
				return Collections.<String>emptyList();
			return Collections.unmodifiableCollection(src.get(input));
		}
	}
}
