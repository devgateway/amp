package org.digijava.kernel.ampapi.endpoints.common.fm;

import static org.junit.Assert.*;

import java.util.Arrays;

import com.google.common.collect.ImmutableMap;
import org.dgfoundation.amp.visibility.data.FMTree;
import org.junit.Test;

/**
 * @author Octavian Ciubotaru
 */
public class FMServiceTest {

    private FMTree oneLevelTree = new FMTree(
            ImmutableMap.of(
                    "A", new FMTree(null, false),
                    "B", new FMTree(null, true)
            ),false
    );

    private FMTree twoLevelTree = new FMTree(
            ImmutableMap.of(
                    "A", new FMTree(ImmutableMap.of(
                            "1", new FMTree(null, false),
                            "2", new FMTree(null, false)
                    ), false),
                    "B", new FMTree(ImmutableMap.of(
                            "1", new FMTree(null, false),
                            "2", new FMTree(null, false)
                    ), false)
            ),false
    );

    @Test
    public void testRootNotExcluded() throws Exception {
        FMTree filteredTree = FMService.filter(oneLevelTree, Arrays.asList("/A"));
        FMTree expectedTree = new FMTree(
                ImmutableMap.of(
                        "A", new FMTree(null, false)
                ),false
        );
        assertEquals(expectedTree, filteredTree);
    }

    @Test
    public void testEmptyFilter() throws Exception {
        FMTree filteredTree = FMService.filter(oneLevelTree, Arrays.asList(""));
        FMTree expectedTree = new FMTree(null,false);
        assertEquals(expectedTree, filteredTree);
    }

    @Test
    public void testEnabled() throws Exception {
        FMTree filteredTree = FMService.filter(oneLevelTree, Arrays.asList("/B"));
        FMTree expectedTree = new FMTree(
                ImmutableMap.of(
                        "B", new FMTree(null, true)
                ),false
        );
        assertEquals(expectedTree, filteredTree);
    }

    @Test
    public void testNoneFiltered() throws Exception {
        FMTree filteredTree = FMService.filter(oneLevelTree, Arrays.asList("/A", "/B"));
        assertEquals(oneLevelTree, filteredTree);
    }

    @Test
    public void testWrongFilter() throws Exception {
        FMTree filteredTree = FMService.filter(oneLevelTree, Arrays.asList("/C"));
        FMTree expectedTree = new FMTree(null,false);
        assertEquals(expectedTree, filteredTree);
    }

    @Test
    public void testParentNotExcluded() throws Exception {
        FMTree filteredTree = FMService.filter(twoLevelTree, Arrays.asList("/A/1"));
        FMTree expectedTree = new FMTree(
                ImmutableMap.of(
                        "A", new FMTree(ImmutableMap.of(
                                "1", new FMTree(null, false)
                        ), false)
                ),false
        );
        assertEquals(expectedTree, filteredTree);
    }

    @Test
    public void testChildrenNotIncluded() throws Exception {
        FMTree filteredTree = FMService.filter(twoLevelTree, Arrays.asList("/A"));
        FMTree expectedTree = new FMTree(
                ImmutableMap.of(
                        "A", new FMTree(null, false)
                ),false
        );
        assertEquals(expectedTree, filteredTree);
    }
}
