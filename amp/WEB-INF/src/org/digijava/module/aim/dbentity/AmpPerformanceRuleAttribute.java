package org.digijava.module.aim.dbentity;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * @author Viorel Chihai
 *
 */
public class AmpPerformanceRuleAttribute {

    @JsonIgnore
    private Long id;

    @JsonBackReference
    private AmpPerformanceRule rule;

    private String name;
    
    private String value;

    private PerformanceRuleAttributeType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpPerformanceRule getRule() {
        return rule;
    }

    public void setRule(AmpPerformanceRule rule) {
        this.rule = rule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PerformanceRuleAttributeType getType() {
        return type;
    }

    public void setType(PerformanceRuleAttributeType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public enum PerformanceRuleAttributeType {
        AMOUNT("AMOUNT"),
        TIME_UNIT("TIME_UNIT"),
        ACTIVITY_STATUS("ACTIVITY_STATUS"),
        ACTIVITY_DATE("ACTIVITY_DATE"),
        FUNDING_DATE("FUNDING_DATE");

        private static final Map<String, PerformanceRuleAttributeType> FORMAT_MAP = Stream
                .of(PerformanceRuleAttributeType.values())
                .collect(Collectors.toMap(s -> s.formatted, Function.identity()));

        private final String formatted;

        PerformanceRuleAttributeType(String formatted) {
            this.formatted = formatted;
        }

        @JsonCreator
        public static PerformanceRuleAttributeType fromString(String string) {
            return Optional.ofNullable(FORMAT_MAP.get(string)).orElseThrow(() -> new IllegalArgumentException(string));
        }
    }

}
