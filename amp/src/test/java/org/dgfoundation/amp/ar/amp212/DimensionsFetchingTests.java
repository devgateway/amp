package org.dgfoundation.amp.ar.amp212;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.dgfoundation.amp.newreports.AmpReportingTestCase;
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
public class DimensionsFetchingTests extends AmpReportingTestCase {
    
    protected final AmpReportsSchema schema = AmpReportsSchema.getInstance();
    
    @Test
    public void testOrganisationsFetching() {
        DimensionSnapshot snapshot = schema.orgsDimension.getDimensionData();
        assertEquals("level 0, info: [(id: 38, parent: 0, children: [17, 18, 19, 20, 21])]", snapshot.data.get(0).toString());
        assertEquals("level 1, info: [(id: 17, parent: 38, children: [21378, 21698]), (id: 18, parent: 38, children: [21694]), (id: 19, parent: 38, children: [21696, 21701, 21702]), (id: 20, parent: 38, children: [21695, 21697]), (id: 21, parent: 38, children: [21699, 21700])]", snapshot.data.get(1).toString());
        assertEquals("level 2, info: [(id: 21378, parent: 17, children: []), (id: 21694, parent: 18, children: []), (id: 21695, parent: 20, children: []), (id: 21696, parent: 19, children: []), (id: 21697, parent: 20, children: []), (id: 21698, parent: 17, children: []), (id: 21699, parent: 21, children: []), (id: 21700, parent: 21, children: []), (id: 21701, parent: 19, children: []), (id: 21702, parent: 19, children: [])]", 
                snapshot.data.get(2).toString());
    }
    
    @Test
    public void testLocationsFetching() {
        // not testing level 0, because there are way too many countries there
        DimensionSnapshot snapshot = schema.locsDimension.getDimensionData();
        assertEquals(-866015659, snapshot.data.get(1).toString().hashCode());
        assertEquals(23, snapshot.data.get(1).parents.keySet().stream().filter(z -> z > 0).count());
        assertEquals(-701710115, snapshot.data.get(2).toString().hashCode());
        assertEquals("[9108, 9109, 9110, 9111, 9112, 9113, 9114, 9115, 9116, 9120]", sortedString(snapshot.data.get(2).parents.keySet().stream().filter(z -> z > 0).collect(Collectors.toList())));
        assertEquals(-1096815164, snapshot.data.get(3).toString().hashCode());
        assertEquals("[9117, 9118, 9121]", snapshot.data.get(3).parents.keySet().stream().filter(z -> z > 0).collect(Collectors.toList()).toString());
    }
    
    @Test
    public void testSectorsFetching() {
        DimensionSnapshot snapshot = schema.secsDimension.getDimensionData();
        assertEquals("[6237]", snapshot.getAcceptableAscendants(2, Arrays.asList(6238l, 6239l)).toString());
        assertEquals("[6237, 6460]", sortedString(snapshot.getAcceptableAscendants(2, Arrays.asList(6238l, 6239l, 6462l))));
        shouldFail(() -> snapshot.getAcceptableDescendants(2, Arrays.asList(6238l)));
    }
    
    @Test
    public void testAcvDimensionFetching() {
        DimensionSnapshot snapshot = schema.catsDimension.getDimensionData();
        assertEquals("[16]", sortedString(snapshot.getAcceptableNeighbours(1, Arrays.asList(108l, 109l), 0)));
        assertEquals("[16, 20]", sortedString(snapshot.getAcceptableNeighbours(1, Arrays.asList(108l, 120l), 0)));
        assertEquals("[223, 224, 225]", sortedString(snapshot.getAcceptableNeighbours(0, Arrays.asList(38l), 1)));
        assertEquals("[223, 224, 225]", sortedString(snapshot.getAcceptableNeighbours(1, Arrays.asList(223l, 225l, 224l), 1)));
        assertEquals("[2, 3]", sortedString(snapshot.getAcceptableNeighbours(0, Arrays.asList(1l, 2l, 3l, 4l), 0)));
    }
    
    @Test
    public void testAgreementDimensionFetching() {
        DimensionSnapshot snapshot = schema.agreementsDimension.getDimensionData();
        assertEquals("depth = 1, data = [level 0, info: [(id: 1, parent: 0, children: []), (id: 2, parent: 0, children: []), (id: 3, parent: 0, children: [])]]", snapshot.toString());
        assertEquals("[2, 3]", sortedString(snapshot.getAcceptableNeighbours(0, Arrays.asList(2l, 3l, 17l), 0)));
    }
}
