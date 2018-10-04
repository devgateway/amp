package org.digijava.kernel.ampapi.endpoints.activity;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Holds one possible value for a field. Immutable.
 * @author Octavian Ciubotaru
 */
public class PossibleValue {

    private final Object id;
    private final String value;

    @JsonProperty("translated-value")
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    private final Map<String, String> translatedValues;

    @JsonProperty("extra_info")
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private final Object extraInfo;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    private final List<PossibleValue> children;

    public PossibleValue(Long id, String value) {
        this(id, value, ImmutableMap.of());
    }

    public PossibleValue(Long id, String value, Map<String, String> translatedValues) {
        this(id, value, translatedValues, null);
    }

    public PossibleValue(Long id, String value, Map<String, String> translatedValues, Object extraInfo) {
        this(id, value, translatedValues, extraInfo, ImmutableList.of());
    }

    public PossibleValue(String id, String value, Map<String, String> translatedValues) {
        this(id, value, translatedValues, null, ImmutableList.of());
    }

    public PossibleValue withChildren(List<PossibleValue> children) {
        return new PossibleValue(id, value, translatedValues, extraInfo, children);
    }

    public PossibleValue withoutChildren() {
        return new PossibleValue(id, value, translatedValues, extraInfo, ImmutableList.of());
    }

    private PossibleValue(Object id, String value, Map<String, String> translatedValues, Object extraInfo,
            List<PossibleValue> children) {
        this.id = id;
        this.value = value;
        this.translatedValues = ImmutableMap.copyOf(translatedValues);
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
    
    public Object getExtraInfo() {
        return extraInfo;
    }

    public Map<String, String> getTranslatedValues() {
        return translatedValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PossibleValue that = (PossibleValue) o;
        return Objects.equals(id, that.id)
                && Objects.equals(value, that.value)
                && Objects.equals(extraInfo, that.extraInfo)
                && Objects.equals(children, that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, extraInfo, children);
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

    @Override
    public String toString() {
        return "PossibleValue{"
                + "id=" + id
                + ", value='" + value + '\''
                + ", hasExtraInfo=" + (extraInfo != null)
                + ", hasChildren=" + (!children.isEmpty())
                + '}';
    }
}
