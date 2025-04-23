package org.digijava.kernel.ampapi.endpoints.settings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.translator.TranslatorWorker;

import java.util.List;

/**
 * Setting Field that allows us to drill down & define custom settings structure 
 *
 * @author Nadejda Mandrescu
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SettingField.IntSettingField.class, name = SettingField.INT_VALUE),
        @JsonSubTypes.Type(value = SettingField.StrSettingField.class, name = SettingField.STR_VALUE),
        @JsonSubTypes.Type(value = SettingField.BoolSettingField.class, name = SettingField.BOOL_VALUE),
        @JsonSubTypes.Type(value = SettingField.RangeSettingField.class, name = SettingField.RANGE_VALUE),
        @JsonSubTypes.Type(value = SettingField.ListSettingField.class, name = SettingField.FIELDS),
        @JsonSubTypes.Type(value = SettingField.OptionsSettingField.class, name = SettingField.OPTIONS)})
public abstract class SettingField {

    /** The actual value */
    static final String INT_VALUE = "INT_VALUE";
    static final String STR_VALUE = "STR_VALUE";
    static final String BOOL_VALUE = "BOOL_VALUE";
    static final String RANGE_VALUE = "RANGE_VALUE";
    /** A list of Options */
    static final String OPTIONS = "OPTIONS";
    /** A list of nested SettingField-s */
    static final String FIELDS = "FIELDS";

    /** Internal id */
    public final String id;
    /** The groupId the field depends on, i.e. if that id is selected */
    public final String groupId;
    /** Name to display */
    public final String name;

    /**
     * Needed only for documentation purposes. Might not be needed if we switch to Open Api Spec v3.
     */
    @ApiModelProperty(required = true)
    private String type;

    public static final class IntSettingField extends SettingField {

        private Integer value;

        private IntSettingField(String id, String groupId, String name, int value) {
            super(id, groupId, name);
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    public static final class StrSettingField extends SettingField {

        private String value;

        private StrSettingField(String id, String groupId, String name, String value) {
            super(id, groupId, name);
            this.value = value;
        }

        @JsonIgnore(false)
        public String getValue() {
            return value;
        }
    }

    public static final class BoolSettingField extends SettingField {

        private Boolean value;

        private BoolSettingField(String id, String groupId, String name, Boolean value) {
            super(id, groupId, name);
            this.value = value;
        }

        @JsonIgnore(false)
        public Boolean getValue() {
            return value;
        }
    }

    public static final class RangeSettingField extends SettingField {

        private SettingRange value;

        private RangeSettingField(String id, String groupId, String name, SettingRange value) {
            super(id, groupId, name);
            this.value = value;
        }

        @JsonIgnore(false)
        public SettingRange getValue() {
            return value;
        }
    }

    public static final class ListSettingField extends SettingField {

        private List<SettingField> value;

        private ListSettingField(String id, String groupId, String name, List<SettingField> value) {
            super(id, groupId, name);
            this.value = value;
        }

        @JsonIgnore(false)
        public List<SettingField> getValue() {
            return value;
        }
    }

    public static final class OptionsSettingField extends SettingField {

        private SettingOptions value;

        private OptionsSettingField(String id, String groupId, String name, SettingOptions value) {
            super(id, groupId, name);
            this.value = value;
        }

        @JsonIgnore(false)
        public SettingOptions getValue() {
            return value;
        }
    }

    public static StrSettingField create(String id, String groupId, String name, String value) {
        return new StrSettingField(id, groupId, name, value);
    }

    public static IntSettingField create(String id, String groupId, String name, int value) {
        return new IntSettingField(id, groupId, name, value);
    }

    public static BoolSettingField create(String id, String groupId, String name, boolean value) {
        return new BoolSettingField(id, groupId, name, value);
    }

    public static RangeSettingField create(String id, String groupId, String name, SettingRange value) {
        return new RangeSettingField(id, groupId, name, value);
    }

    /**
     * Builds a nested setting field that contains multiple other values/options
     */
    public static ListSettingField create(String id, String groupId, String name, List<SettingField> value) {
        return new ListSettingField(id, groupId, name, value);
    }

    /**
     * Setting field with multiple options
     * @param id internal field id/name
     * @param name displayed field name
     * @param value {@link SettingOptions}
     */
    public static OptionsSettingField create(String id, String groupId, String name, SettingOptions value) {
        return new OptionsSettingField(id, groupId, name, value);
    }

    private SettingField(String id, String groupId, String name) {
        this.id = id;
        this.groupId = groupId;
        this.name = TranslatorWorker.translateText(name);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override public String toString() {
        return String.format("name: %s, type: %s, id: %s, groupId: %s", name, getClass().getSimpleName(), id, groupId);
    }
}
