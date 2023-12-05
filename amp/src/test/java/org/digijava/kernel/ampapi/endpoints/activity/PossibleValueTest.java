package org.digijava.kernel.ampapi.endpoints.activity;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Octavian Ciubotaru
 */
public class PossibleValueTest {

    @Test
    public void testFlattenSingle() throws Exception {
        PossibleValue possibleValue = new PossibleValue(1L, "Parent")
                .withChildren(Collections.singletonList(new PossibleValue(2L, "Child")));
        List<PossibleValue> actualPossibleValues = PossibleValue.flattenPossibleValues(possibleValue);

        List<PossibleValue> expectedPossibleValues = new ArrayList<>();
        expectedPossibleValues.add(new PossibleValue(1L, "Parent"));
        expectedPossibleValues.add(new PossibleValue(2L, "Child"));

        assertEquals(actualPossibleValues, expectedPossibleValues);
    }

    @Test
    public void testFlattenList() throws Exception {
        List<PossibleValue> possibleValues = new ArrayList<>();
        possibleValues.add(new PossibleValue(1L, "Parent 1")
                .withChildren(Collections.singletonList(new PossibleValue(2L, "Child 1"))));
        possibleValues.add(new PossibleValue(3L, "Parent 2")
                .withChildren(Collections.singletonList(new PossibleValue(4L, "Child 2"))));

        List<PossibleValue> actualPossibleValues = PossibleValue.flattenPossibleValues(possibleValues);

        List<PossibleValue> expectedPossibleValues = new ArrayList<>();
        expectedPossibleValues.add(new PossibleValue(1L, "Parent 1"));
        expectedPossibleValues.add(new PossibleValue(2L, "Child 1"));
        expectedPossibleValues.add(new PossibleValue(3L, "Parent 2"));
        expectedPossibleValues.add(new PossibleValue(4L, "Child 2"));

        assertEquals(actualPossibleValues, expectedPossibleValues);
    }
}
