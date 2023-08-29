package org.dgfoundation.amp.ar.amp212;

import org.dgfoundation.amp.nireports.schema.ConstantNiDimension;
import org.dgfoundation.amp.nireports.schema.DimensionSnapshot;
import org.dgfoundation.amp.nireports.schema.IdsAcceptor;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 
 * testcases for the NiReports DimensionSnapshot class
 * 
 * @author Constantin Dolghier
 *
 */
public class DimensionSnapshotTests extends AmpTestCase {
    
    public final DimensionSnapshot UNIFORM_3_LEVEL = getSnapshot("uniform", 3, Arrays.asList(
            Arrays.asList(100l, 110l, 112l),
            Arrays.asList(100l, 110l, 113l),
            Arrays.asList(100l, 120l, 121l),
            Arrays.asList(200l, 210l, 211l)));
    
    @Test
    public void testCornerCases() {
        DimensionSnapshot snapshot = getSnapshot("empty", 3, Collections.emptyList());
        assertEquals("depth = 3, data = [level 0, info: [], level 1, info: [], level 2, info: []]", snapshot.toString());
        assertEquals("[]", snapshot.getAcceptableAscendants(2, Arrays.asList(1l)).toString());
    }
    
    @Test
    public void testCrashesOnInvalidData() {
        shouldFail(() -> getSnapshot("length 3 + length 2", 3, Arrays.asList(Arrays.asList(1l, 2l, 3l), Arrays.asList(1l, 2l)))); 
        shouldFail(() -> getSnapshot("length 2 @depth 3", 3, Arrays.asList(Arrays.asList(1l, 2l), Arrays.asList(1l, 2l)))); 
        shouldFail(() -> getSnapshot("null followed by nonnull", 4, Arrays.asList(Arrays.asList(1l, 2l, 3l, 4l), Arrays.asList(1l, 2l, null, 4l)))); 
        shouldFail(() -> getSnapshot("multiple parents", 3, Arrays.asList(Arrays.asList(100l, 110l, 111l), Arrays.asList(200l, 110l, 211l))));
        shouldFail(() -> getSnapshot("leading null", 3, Arrays.asList(Arrays.asList(null, 1l, 2l))));
        shouldFail(() -> getSnapshot("trailing null", 3, Arrays.asList(Arrays.asList(3l, 1l, null))));
    }
    
    @Test
    public void testUniformDimension() {
        DimensionSnapshot snapshot = UNIFORM_3_LEVEL;
        assertEquals("depth = 3, data = [level 0, info: [(id: -999999999, parent: 0, children: [-999999999]), (id: 100, parent: 0, children: [110, 120]), (id: 200, parent: 0, children: [210])], level 1, info: [(id: -999999999, parent: -999999999, children: [-999999999]), (id: 110, parent: 100, children: [112, 113]), (id: 120, parent: 100, children: [121]), (id: 210, parent: 200, children: [211])], level 2, info: [(id: -999999999, parent: -999999999, children: []), (id: 112, parent: 110, children: []), (id: 113, parent: 110, children: []), (id: 121, parent: 120, children: []), (id: 211, parent: 210, children: [])]]", snapshot.toString());
        shouldFail(() -> snapshot.getAcceptableAscendants(0, Arrays.asList(100l)));
        shouldFail(() -> snapshot.getAcceptableDescendants(2, Arrays.asList(121l)));
        assertColEquals("[]", snapshot.getAcceptableAscendants(1, Arrays.asList(777l)));
        assertColEquals("[100]", snapshot.getAcceptableAscendants(1, Arrays.asList(777l, 110l, 120l, 120l, 120l)));
    }
    
    @Test
    public void testUniformDimensionWalking() {
        DimensionSnapshot snapshot = UNIFORM_3_LEVEL;
        
        assertColEquals("[100]", snapshot.getAcceptableNeighbours(1, Arrays.asList(110l), 0));
        assertColEquals("[110]", snapshot.getAcceptableNeighbours(1, Arrays.asList(110l), 1));
        assertColEquals("[112, 113]", snapshot.getAcceptableNeighbours(1, Arrays.asList(110l), 2));
    }
    
    @Test
    public void testUniformDimensionAcceptors() {
        DimensionSnapshot snapshot = UNIFORM_3_LEVEL;
        
        for(NiDimension.Coordinate coo1:Arrays.asList(new NiDimension.Coordinate(2, 112l), new NiDimension.Coordinate(NiDimension.LEVEL_ALL_IDS, 112l)))
            for(NiDimension.Coordinate coo2:Arrays.asList(new NiDimension.Coordinate(1, 210l), new NiDimension.Coordinate(NiDimension.LEVEL_ALL_IDS, 210l))) {
        
                IdsAcceptor acceptor = snapshot.getCachingIdsAcceptor(Arrays.asList(coo1, coo2));
                assertTrue(acceptor.isAcceptable(new NiDimension.Coordinate(2, 112l)));
                assertTrue(acceptor.isAcceptable(new NiDimension.Coordinate(NiDimension.LEVEL_ALL_IDS, 112l)));
        
                assertTrue(acceptor.isAcceptable(new NiDimension.Coordinate(1, 210l)));
                assertTrue(acceptor.isAcceptable(new NiDimension.Coordinate(NiDimension.LEVEL_ALL_IDS, 210l)));
        
                assertFalse(acceptor.isAcceptable(new NiDimension.Coordinate(1, 113l)));
                assertFalse(acceptor.isAcceptable(new NiDimension.Coordinate(NiDimension.LEVEL_ALL_IDS, 113l)));
        
                assertFalse(acceptor.isAcceptable(new NiDimension.Coordinate(1, 120l)));
                assertFalse(acceptor.isAcceptable(new NiDimension.Coordinate(NiDimension.LEVEL_ALL_IDS, 120l)));
        
                assertTrue(acceptor.isAcceptable(new NiDimension.Coordinate(0, 100l)));
                assertTrue(acceptor.isAcceptable(new NiDimension.Coordinate(NiDimension.LEVEL_ALL_IDS, 100l)));
        
                assertTrue(acceptor.isAcceptable(new NiDimension.Coordinate(0, 200l)));
                assertTrue(acceptor.isAcceptable(new NiDimension.Coordinate(NiDimension.LEVEL_ALL_IDS, 200l)));
            }
    }
    
//  @Test
//  public void testUniformDimensionUnspecifiedLevelAcceptors() {
//      DimensionSnapshot snapshot = UNIFORM_3_LEVEL;
//      IdsAcceptor acceptor = sna
//  }
    
    @Test
    public void testNegativeIds() {
        DimensionSnapshot snapshot = getSnapshot("some negative ids", 3, Arrays.asList(
            Arrays.asList(100l, 110l, 112l),
            Arrays.asList(100l, 110l, -113l),
            Arrays.asList(100l, -120l, -121l),
            Arrays.asList(-200l, 210l, 211l)));
        assertEquals("depth = 3, data = [level 0, info: [(id: -999999999, parent: 0, children: [-999999999]), (id: -200, parent: 0, children: [210]), (id: 100, parent: 0, children: [-120, 110])], level 1, info: [(id: -999999999, parent: -999999999, children: [-999999999]), (id: -120, parent: 100, children: [-121]), (id: 110, parent: 100, children: [-113, 112]), (id: 210, parent: -200, children: [211])], level 2, info: [(id: -999999999, parent: -999999999, children: []), (id: -121, parent: -120, children: []), (id: -113, parent: 110, children: []), (id: 112, parent: 110, children: []), (id: 211, parent: 210, children: [])]]", snapshot.toString());
        assertColEquals("[100]", snapshot.getAcceptableAscendants(1, Arrays.asList(-120l)));
        assertColEquals("[-200, 100]", snapshot.getAcceptableAscendants(1, Arrays.asList(-120l, 210l)));
        assertColEquals("[-121, -113, 112]", snapshot.getAcceptableNeighbours(0, Arrays.asList(100l), 2));
    }
    
//  @Test
//  public void testRaggedDimension() {
//      DimensionSnapshot snapshot = getSnapshot("ragged", 4, Arrays.asList(
//          Arrays.asList(1000l, 1100l, 1110l, 1111l),
//          Arrays.asList(1000l, 1100l, 1110l, null),
//          Arrays.asList(1000l, 1200l, 1220l, 1121l),
//          Arrays.asList(2000l, null, null, null),
//          Arrays.asList(2000l, 2100l, null, null),
//          Arrays.asList(3000l, null, null, null)
//      ));
//      assertEquals("depth = 4, data = [level 0, info: [(id: 1000, parent: -1, children: [1100, 1200]), (id: 2000, parent: -1, children: [2100]), (id: 3000, parent: -1, children: [])], level 1, info: [(id: 1100, parent: 1000, children: [1110]), (id: 1200, parent: 1000, children: [1220]), (id: 2100, parent: 2000, children: [])], level 2, info: [(id: 1110, parent: 1100, children: [1111]), (id: 1220, parent: 1200, children: [1121])], level 3, info: [(id: 1111, parent: 1110, children: []), (id: 1121, parent: 1220, children: [])]]", snapshot.toString());
//      assertColEquals("[1220]", snapshot.getAcceptableAscendants(3, Arrays.asList(1121l)));
//      //shouldFail(() -> snapshot.getAcceptableAscendants(3, Arrays.asList(1121l, null)));
//      assertColEquals("[1100, 1200]", snapshot.getAcceptableAscendants(2,  Arrays.asList(1110l, 1220l)));
//      
//      assertColEquals("[1111, 1121]", snapshot.getAcceptableNeighbours(0, Arrays.asList(1000l, 2000l, 3000l), 3));
//      assertColEquals("[1110, 1220]", snapshot.getAcceptableNeighbours(0, Arrays.asList(1000l, 2000l, 3000l), 2));
//      assertColEquals("[1100, 1200, 2100]", snapshot.getAcceptableNeighbours(0, Arrays.asList(1000l, 2000l, 3000l), 1));
//      
//      assertColEquals("[1000]", snapshot.getAcceptableNeighbours(3, Arrays.asList(1111l, 1121l), 0));
//  };
    
    @Test
    public void testIdsNotMixed() {
        DimensionSnapshot snapshot = getSnapshot("repeating ids", 4, Arrays.asList(
            Arrays.asList(1l, 1l, 1l, 1l),
            Arrays.asList(1l, 1l, 1l, 2l),
            Arrays.asList(1l, 2l, 2l, 3l)
        ));
        assertEquals("depth = 4, data = [level 0, info: [(id: -999999999, parent: 0, children: [-999999999]), (id: 1, parent: 0, children: [1, 2])], level 1, info: [(id: -999999999, parent: -999999999, children: [-999999999]), (id: 1, parent: 1, children: [1]), (id: 2, parent: 1, children: [2])], level 2, info: [(id: -999999999, parent: -999999999, children: [-999999999]), (id: 1, parent: 1, children: [1, 2]), (id: 2, parent: 2, children: [3])], level 3, info: [(id: -999999999, parent: -999999999, children: []), (id: 1, parent: 1, children: []), (id: 2, parent: 1, children: []), (id: 3, parent: 2, children: [])]]", snapshot.toString());
        assertColEquals("[1]", snapshot.getAcceptableAscendants(1, Arrays.asList(1l, 2l)));
        assertColEquals("[1, 2]", snapshot.getAcceptableDescendants(0, Arrays.asList(1l)));
        assertColEquals("[2]", snapshot.getAcceptableDescendants(1, Arrays.asList(2l)));
        assertColEquals("[3]", snapshot.getAcceptableDescendants(2, Arrays.asList(2l)));
        
        
        
        // testing downstream
        assertColEquals("[1]", snapshot.getAcceptableNeighbours(0, Arrays.asList(1l, 2l, 3l), 0));
        assertColEquals("[1, 2]", snapshot.getAcceptableNeighbours(0, Arrays.asList(1l, 2l, 3l), 1));
        assertColEquals("[1, 2]", snapshot.getAcceptableNeighbours(0, Arrays.asList(1l, 2l, 3l), 2));
        assertColEquals("[1, 2, 3]", snapshot.getAcceptableNeighbours(0, Arrays.asList(1l, 2l, 3l), 3));
        
        assertColEquals("[1]", snapshot.getAcceptableNeighbours(3, Arrays.asList(1l, 2l, 3l, 4l, 5l), 0));
        assertColEquals("[1, 2]", snapshot.getAcceptableNeighbours(3, Arrays.asList(1l, 2l, 3l, 4l, 5l), 1));
        assertColEquals("[1, 2]", snapshot.getAcceptableNeighbours(3, Arrays.asList(1l, 2l, 3l, 4l, 5l), 2));
        
        assertColEquals("[2]", snapshot.getAcceptableNeighbours(1, Arrays.asList(2l), 2));
        assertColEquals("[2]", snapshot.getAcceptableNeighbours(2, Arrays.asList(2l), 1));
        
        assertColEquals("[1, 2]", snapshot.getAcceptableNeighbours(1, Arrays.asList(1l, 2l), 2));
        assertColEquals("[1, 2]", snapshot.getAcceptableNeighbours(2, Arrays.asList(1l, 2l), 1));
    }
    
    @Test
    public void testDegenerateDimension() {
        DimensionSnapshot snapshot = getSnapshot("identity", 1, Arrays.asList(
            Arrays.asList(1l),
            Arrays.asList(2l),
            Arrays.asList(3l),
            // intentional gap
            Arrays.asList(7l)));
        
        assertEquals("depth = 1, data = [level 0, info: [(id: -999999999, parent: 0, children: []), (id: 1, parent: 0, children: []), (id: 2, parent: 0, children: []), (id: 3, parent: 0, children: []), (id: 7, parent: 0, children: [])]]", snapshot.toString());
        assertColEquals("[1]", snapshot.getAcceptableNeighbours(0, Arrays.asList(1l), 0));
        assertColEquals("[1, 2]", snapshot.getAcceptableNeighbours(0, Arrays.asList(1l, 2l), 0));
        assertColEquals("[2]", snapshot.getAcceptableNeighbours(0, Arrays.asList(2l, 5l, 6l), 0));
        assertColEquals("[1, 7]", snapshot.getAcceptableNeighbours(0, Arrays.asList(1l, 5l, 7l), 0));
    }
    
    public DimensionSnapshot getSnapshot(String name, int depth, List<List<Long>> list) {
        ConstantNiDimension niDim = new ConstantNiDimension(name, depth, list);
        return niDim.getDimensionData();
    }
}
