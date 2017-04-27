package org.digijava.kernel.ampapi.endpoints.activity;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Holds one possible value for a field. Immutable.
 * @author Octavian Ciubotaru
 */
public class PossibleValue {

    private final Object id;
    private final String value;

    @JsonProperty("extra_info")
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private final Object extraInfo;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    private final List<PossibleValue> children;

    public PossibleValue(Long id, String value) {
        this(id, value, null);
    }

    public PossibleValue(Long id, String value, Object extraInfo) {
        this(id, value, extraInfo, ImmutableList.of());
    }

    public PossibleValue(String id, String value) {
        this(id, value, null, ImmutableList.of());
    }

    public PossibleValue withChildren(List<PossibleValue> children) {
        return new PossibleValue(id, value, extraInfo, children);
    }

    public PossibleValue withoutChildren() {
        return new PossibleValue(id, value, extraInfo, ImmutableList.of());
    }

    private PossibleValue(Object id, String value, Object extraInfo, List<PossibleValue> children) {
        this.id = id;
        this.value = value;
        this.children = ImmutableList.copyOf(children);
        this.extraInfo = extraInfo;
    }

    public Object getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public List<PossibleValue> getChildren() {
        return children;
    }

    public static List<PossibleValue> flattenPossibleValues(List<PossibleValue> possibleValues) {
        if (possibleValues.stream().allMatch(pv -> pv.getChildren().isEmpty())) {
            return possibleValues;
        }
        return possibleValues.stream().flatMap(pv -> flattenPossibleValues(pv).stream()).collect(toList());
    }

    public static List<PossibleValue> flattenPossibleValues(PossibleValue possibleValue) {
        List<PossibleValue> flatPossibleValues = new ArrayList<>();
        flatPossibleValues.add(possibleValue.withoutChildren());
        possibleValue.getChildren().forEach(pv -> flatPossibleValues.addAll(flattenPossibleValues(pv)));
        return flatPossibleValues;
    }
}
