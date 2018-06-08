package org.digijava.kernel.ampapi.endpoints.activity;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;

/**
 * @author Octavian Ciubotaru
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "field_name", "id", "field_type", "field_label", "required", "importable", "dependencies",
        "id_only", "multiple_values", "percentage_constraint", "unique_constraint", "tree_collection", "translatable",
        "regex_pattern", "regex_constraint", "field_length", "size_limit" })
public class APIField {

    @JsonProperty(ActivityEPConstants.FIELD_NAME)
    private String fieldName;

    @JsonProperty(ActivityEPConstants.ID)
    private Boolean id;

    @JsonProperty(ActivityEPConstants.FIELD_TYPE)
    private String fieldType;

    @JsonProperty(ActivityEPConstants.FIELD_LABEL)
    private JsonBean fieldLabel;

    @JsonIgnore
    private String fieldNameInternal;

    @JsonProperty(ActivityEPConstants.REQUIRED)
    private String required;

    @JsonProperty(ActivityEPConstants.ID_ONLY)
    private Boolean idOnly;

    @JsonProperty(ActivityEPConstants.IMPORTABLE)
    private Boolean importable;

    @JsonProperty(ActivityEPConstants.TRANSLATABLE)
    private Boolean translatable;

    @JsonProperty(ActivityEPConstants.MULTIPLE_VALUES)
    private Boolean multipleValues;

    @JsonIgnore
    private Boolean activity;

    @JsonProperty(ActivityEPConstants.UNIQUE_CONSTRAINT)
    private String uniqueConstraint;

    @JsonProperty(ActivityEPConstants.PERCENTAGE_CONSTRAINT)
    private String percentageConstraint;

    @JsonProperty(ActivityEPConstants.TREE_COLLECTION_CONSTRAINT)
    private Boolean treeCollectionConstraint;

    @JsonProperty(ActivityEPConstants.FIELD_LENGTH)
    private Integer fieldLength;

    @JsonProperty(ActivityEPConstants.CHILDREN)
    private List<APIField> children;

    @JsonProperty(ActivityEPConstants.DEPENDENCIES)
    private List<String> dependencies;
    
    @JsonProperty(ActivityEPConstants.REGEX_PATTERN)
    private String regexPattern;
    
    @JsonProperty(ActivityEPConstants.PERCENTAGE)
    private Boolean percentage;
    
    @JsonProperty(ActivityEPConstants.SIZE_LIMIT)
    private Integer sizeLimit;

    @JsonIgnore
    private String discriminator;

    public String getFieldName() {
        return fieldName;
    }

    public JsonBean getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(JsonBean fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldNameInternal() {
        return fieldNameInternal;
    }

    public void setFieldNameInternal(String fieldNameInternal) {
        this.fieldNameInternal = fieldNameInternal;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public Boolean isId() {
        return id;
    }

    public void setId(Boolean id) {
        this.id = id;
    }

    public Boolean isIdOnly() {
        return idOnly;
    }

    public void setIdOnly(Boolean idOnly) {
        this.idOnly = idOnly;
    }

    public boolean isImportable() {
        return importable;
    }

    public void setImportable(boolean importable) {
        this.importable = importable;
    }

    public Boolean isTranslatable() {
        return translatable;
    }

    public void setTranslatable(Boolean translatable) {
        this.translatable = translatable;
    }

    public Boolean isMultipleValues() {
        return multipleValues;
    }

    public Boolean isActivity() {
        return activity;
    }

    public void setActivity(Boolean activity) {
        this.activity = activity;
    }

    public void setMultipleValues(Boolean multipleValues) {
        this.multipleValues = multipleValues;
    }

    public String getUniqueConstraint() {
        return uniqueConstraint;
    }

    public void setUniqueConstraint(String uniqueConstraint) {
        this.uniqueConstraint = uniqueConstraint;
    }

    public String getPercentageConstraint() {
        return percentageConstraint;
    }

    public void setPercentageConstraint(String percentageConstraint) {
        this.percentageConstraint = percentageConstraint;
    }

    public Boolean getTreeCollectionConstraint() {
        return treeCollectionConstraint;
    }

    public void setTreeCollectionConstraint(Boolean treeCollectionConstraint) {
        this.treeCollectionConstraint = treeCollectionConstraint;
    }

    public Integer getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(Integer fieldLength) {
        this.fieldLength = fieldLength;
    }
    
    public Integer getSizeLimit() {
        return sizeLimit;
    }

    public void setSizeLimit(Integer sizeLimit) {
        this.sizeLimit = sizeLimit;
    }

    public List<APIField> getChildren() {
        return children;
    }

    public void setChildren(List<APIField> children) {
        this.children = children;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }
    
    public String getRegexPattern() {
        return regexPattern;
    }

    public void setRegexPattern(String regexPattern) {
        this.regexPattern = regexPattern;
    }
    
    public Boolean getPercentage() {
        return percentage;
    }

    public void setPercentage(Boolean percentage) {
        this.percentage = percentage;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    @Override
    public String toString() {
        return "APIField{" + "fieldName='" + fieldName + '\'' + ", id=" + id + ", fieldType='" + fieldType + '\''
                + ", fieldLabel=" + fieldLabel + ", fieldNameInternal='" + fieldNameInternal + '\'' + ", required='"
                + required + '\'' + ", idOnly=" + idOnly + ", importable=" + importable + ", translatable="
                + translatable + ", multipleValues=" + multipleValues + ", activity=" + activity
                + ", uniqueConstraint='" + uniqueConstraint + '\'' + ", percentageConstraint='" + percentageConstraint
                + '\'' + ", treeCollectionConstraint=" + treeCollectionConstraint + ", fieldLength=" + fieldLength
                + ", children=" + children + ", dependencies=" + dependencies + ", percentage=" + percentage 
                + ", regex_pattern=" + regexPattern + "}";
    }
}
