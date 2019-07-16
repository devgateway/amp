package org.dgfoundation.amp.ar.amp212;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.FilterRule.FilterType;
import org.junit.Test;

/**
 * ETL Testcases
 * @author Dolghier Constantin
 *
 */
public class FilterRuleTests {

    @Test
    public void testMergingCornerCases() {
        assertNull(FilterRule.mergeRules(null));
        assertEquals(0, FilterRule.mergeRules(new ArrayList<FilterRule>()).size() );
        assertEquals(1, FilterRule.mergeRules(Arrays.asList(new FilterRule("1", true))).size());
    }

    @Test
    public void testMergingIds() {
        List<FilterRule> in = new ArrayList<>();
        for(int i = 1; i < 10; i++)
            in.add(new FilterRule(Integer.toString(i), true));
        List<FilterRule> out = FilterRule.mergeRules(in);
        assertEquals(1, out.size());
        FilterRule outRule = out.get(0);
        assertEquals(FilterType.VALUES, outRule.filterType);
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9]", new TreeSet<>(outRule.values).toString());
    }
    
    @Test
    public void testMergingRangesAndIds() {
        List<FilterRule> in = new ArrayList<>();
        in.add(new FilterRule(Arrays.asList("1", "3", "4", "6"), true));
        in.add(new FilterRule(Arrays.asList("1", "3", "4", "5"), true));
        in.add(new FilterRule("2", true));
        List<FilterRule> out = FilterRule.mergeRules(in);
        assertEquals(1, out.size());
        FilterRule outRule = out.get(0);
        assertEquals(FilterType.VALUES, outRule.filterType);
        assertEquals("[1, 2, 3, 4, 5, 6]", new TreeSet<>(outRule.values).toString());   
    }   
}
