package org.digijava.kernel.security;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Octavian Ciubotaru
 */
public class TestRuleHierarchy {

    @Test
    public void testEmpty() {
        RuleHierarchy<String> hierarchy = new RuleHierarchy.Builder<String>().build();
        assertCollection(Arrays.asList("1"), hierarchy.getEffectiveRules(new String[] {"1"}));
    }

    @Test
    public void testForwards() {
        RuleHierarchy<String> hierarchy = new RuleHierarchy.Builder<String>()
                .addRuleDependency("1", "2")
                .build();
        assertCollection(Arrays.asList("1", "2"), hierarchy.getEffectiveRules(new String[] {"1"}));
    }

    @Test
    public void testBackwards() {
        RuleHierarchy<String> hierarchy = new RuleHierarchy.Builder<String>()
                .addRuleDependency("1", "2")
                .build();
        assertCollection(Arrays.asList("2"), hierarchy.getEffectiveRules(new String[] {"2"}));
    }

    @Test(expected = IllegalStateException.class)
    public void testRecursive() {
        new RuleHierarchy.Builder<String>()
                .addRuleDependency("1", "2")
                .addRuleDependency("2", "1")
                .build();
    }

    @Test
    public void testTwoRules() {
        RuleHierarchy<String> hierarchy = new RuleHierarchy.Builder<String>()
                .addRuleDependency("1", "2")
                .addRuleDependency("3", "4")
                .build();
        assertCollection(Arrays.asList("1", "2", "3", "4"), hierarchy.getEffectiveRules(new String[] {"1", "3"}));
    }

    @Test
    public void testPollution() {
        RuleHierarchy<String> hierarchy = new RuleHierarchy.Builder<String>()
                .addRuleDependency("1", "2")
                .addRuleDependency("3", "4")
                .build();
        assertCollection(Arrays.asList("1", "2"), hierarchy.getEffectiveRules(new String[] {"1"}));
    }

    @Test
    public void testThreeLevel() {
        RuleHierarchy<String> hierarchy = new RuleHierarchy.Builder<String>()
                .addRuleDependency("1", "2")
                .addRuleDependency("2", "3")
                .build();
        assertCollection(Arrays.asList("1", "2", "3"), hierarchy.getEffectiveRules(new String[] {"1"}));
    }

    @Test
    public void testThreeLevelStartAtTwo() {
        RuleHierarchy<String> hierarchy = new RuleHierarchy.Builder<String>()
                .addRuleDependency("1", "2")
                .addRuleDependency("2", "3")
                .build();
        assertCollection(Arrays.asList("2", "3"), hierarchy.getEffectiveRules(new String[] {"2"}));
    }

    @Test(expected = IllegalStateException.class)
    public void testThreeLevelRecursive() {
        new RuleHierarchy.Builder<String>()
                .addRuleDependency("1", "2")
                .addRuleDependency("2", "3")
                .addRuleDependency("3", "1")
                .build();
    }

    private void assertCollection(Collection expected, Collection actual) {
        Assert.assertTrue(CollectionUtils.isEqualCollection(expected, actual));
    }
}
