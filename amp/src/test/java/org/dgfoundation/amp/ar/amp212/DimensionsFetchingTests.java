package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.TreeSet;

import org.dgfoundation.amp.mondrian.MondrianReportsTestCase;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import org.dgfoundation.amp.nireports.schema.DimensionSnapshot;
import org.junit.Test;

/**
 * 
 * testcases for the NiReports dimensions fetching
 * 
 * @author Constantin Dolghier
 *
 */
public class DimensionsFetchingTests extends MondrianReportsTestCase {
	
	protected final AmpReportsSchema schema = AmpReportsSchema.getInstance();
	
	public DimensionsFetchingTests() {
		super("Dimension fetching tests");
	}

	@Test
	public void testOrganisationsFetching() {
		DimensionSnapshot snapshot = schema.orgsDimension.getDimensionData();
		assertEquals("level 0, info: [(id: 38, parent: -1, children: [17, 18, 19, 20, 21])]", snapshot.data.get(0).toString());
		assertEquals("level 1, info: [(id: 17, parent: 38, children: [21378, 21698]), (id: 18, parent: 38, children: [21694]), (id: 19, parent: 38, children: [21696, 21701, 21702]), (id: 20, parent: 38, children: [21695, 21697]), (id: 21, parent: 38, children: [21699, 21700])]", snapshot.data.get(1).toString());
		assertEquals("level 2, info: [(id: 21378, parent: 17, children: []), (id: 21694, parent: 18, children: []), (id: 21695, parent: 20, children: []), (id: 21696, parent: 19, children: []), (id: 21697, parent: 20, children: []), (id: 21698, parent: 17, children: []), (id: 21699, parent: 21, children: []), (id: 21700, parent: 21, children: []), (id: 21701, parent: 19, children: []), (id: 21702, parent: 19, children: [])]", 
				snapshot.data.get(2).toString());
	}
	
	@Test
	public void testLocationsFetching() {
		// not testing level 0, because there are way too many countries there
		DimensionSnapshot snapshot = schema.locsDimension.getDimensionData();
		assertEquals("level 1, info: [(id: 9085, parent: 8977, children: [9108, 9109, 9110]), (id: 9086, parent: 8977, children: [9111, 9112, 9113]), (id: 9087, parent: 8977, children: [9120]), (id: 9088, parent: 8977, children: []), (id: 9089, parent: 8977, children: []), (id: 9090, parent: 8977, children: []), (id: 9091, parent: 8977, children: []), (id: 9092, parent: 8977, children: []), (id: 9093, parent: 8977, children: []), (id: 9094, parent: 8977, children: []), (id: 9095, parent: 8977, children: []), (id: 9096, parent: 8977, children: []), (id: 9097, parent: 8977, children: []), (id: 9098, parent: 8977, children: []), (id: 9099, parent: 8977, children: []), (id: 9100, parent: 8977, children: []), (id: 9101, parent: 8977, children: []), (id: 9102, parent: 8977, children: []), (id: 9103, parent: 8977, children: []), (id: 9104, parent: 8977, children: []), (id: 9105, parent: 8977, children: [9114, 9115, 9116]), (id: 9106, parent: 8977, children: []), (id: 9107, parent: 8977, children: [])]", snapshot.data.get(1).toString());
		assertEquals("level 2, info: [(id: 9108, parent: 9085, children: [9118]), (id: 9109, parent: 9085, children: []), (id: 9110, parent: 9085, children: []), (id: 9111, parent: 9086, children: [9117]), (id: 9112, parent: 9086, children: []), (id: 9113, parent: 9086, children: []), (id: 9114, parent: 9105, children: []), (id: 9115, parent: 9105, children: []), (id: 9116, parent: 9105, children: []), (id: 9120, parent: 9087, children: [9121])]", snapshot.data.get(2).toString());
		assertEquals("level 3, info: [(id: 9117, parent: 9111, children: []), (id: 9118, parent: 9108, children: []), (id: 9121, parent: 9120, children: [])]", snapshot.data.get(3).toString());
	}
	
	@Test
	public void testSectorsFetching() {
		DimensionSnapshot snapshot = schema.secsDimension.getDimensionData();
		assertEquals("[6237]", snapshot.getAcceptableAscendants(2, Arrays.asList(6238l, 6239l)).toString());
		assertEquals("[6237, 6460]", new TreeSet<>(snapshot.getAcceptableAscendants(2, Arrays.asList(6238l, 6239l, 6462l))).toString());
		shouldFail(() -> snapshot.getAcceptableDescendants(2, Arrays.asList(6238l)));
	}
}
