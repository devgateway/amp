package org.digijava.kernel.ampapi.endpoints.activity.field;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableList;

import org.digijava.kernel.ampapi.discriminators.DiscriminationConfigurer;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.ampapi.endpoints.activity.FieldAccessor;
import org.digijava.kernel.ampapi.endpoints.activity.PossibleValuesProvider;
import org.digijava.kernel.ampapi.endpoints.activity.TranslationSettings;
import org.digijava.kernel.ampapi.endpoints.dto.UnwrappedTranslations;
import org.digijava.kernel.validation.ConstraintDescriptors;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Octavian Ciubotaru
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "field_name", "apiType", "field_label", "required", "importable", "dependencies", "id_only",
    "multiple_values", "percentage_constraint", "unique_constraint", "tree_collection", "translatable", "regex_pattern",
    "regex_constraint", "field_length", "size_limit", "common-possible-values" })
public class APIField {

    @JsonProperty("id")
    private boolean id;

    @JsonProperty(ActivityEPConstants.FIELD_NAME)
    private String fieldName;

    @JsonUnwrapped
    private APIType apiType;

    @JsonProperty(ActivityEPConstants.FIELD_LABEL)
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.MultilingualLabelPH")
    private UnwrappedTranslations fieldLabel;

    @JsonIgnore
    private String fieldNameInternal;

    /**
     * Can be Y/N/ND. If this field has dependencies then this value is {@link #dependencyRequired}, otherwise it is
     * {@link #unconditionalRequired}.
     */
    @JsonProperty(ActivityEPConstants.REQUIRED)
    private String required;

    /**
     * Used internally. Can be Y/N/ND as for {@link #required}. Used only for fields without dependency.
     * Validation happens unconditionally.
     */
    @JsonIgnore
    private String unconditionalRequired;

    /**
     * Used internally. Can be Y/N/ND. Used only when field has a dependency.
     *
     * <p>Examples of dependencies:
     * <ul><li>field is sometimes invisible</li>
     * <li>field is always visible but it is required only when another field has a specific value</li></ul></p>
     */
    @JsonIgnore
    private String dependencyRequired;

    @JsonProperty(ActivityEPConstants.ID_ONLY)
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
    private boolean idOnly;

    @JsonProperty(ActivityEPConstants.IMPORTABLE)
    private Boolean importable;

    @JsonProperty(ActivityEPConstants.TRANSLATABLE)
    private Boolean translatable;

    @JsonProperty(ActivityEPConstants.MULTIPLE_VALUES)
    private Boolean multipleValues;

    @JsonProperty(ActivityEPConstants.UNIQUE_CONSTRAINT)
    private String uniqueConstraint;

    @JsonProperty(ActivityEPConstants.PERCENTAGE_CONSTRAINT)
    private String percentageConstraint;

    @JsonProperty(ActivityEPConstants.TREE_COLLECTION_CONSTRAINT)
    private Boolean treeCollectionConstraint;

    @JsonProperty(ActivityEPConstants.FIELD_LENGTH)
    private Integer fieldLength;

    @JsonIgnore
    private APIField idChild;

    @JsonIgnore
    private boolean independent;

    @JsonProperty(ActivityEPConstants.CHILDREN)
    private Fields children;

    @JsonProperty(ActivityEPConstants.DEPENDENCIES)
    private List<String> dependencies = new ArrayList<>();

    @JsonProperty(ActivityEPConstants.REGEX_PATTERN)
    private String regexPattern;

    @JsonProperty(ActivityEPConstants.PERCENTAGE)
    private Boolean percentage;

    @JsonProperty(ActivityEPConstants.SIZE_LIMIT)
    private Integer sizeLimit;

    @JsonIgnore
    private String discriminatorField;

    @JsonIgnore
    private String discriminatorValue;

    @JsonIgnore
    private Class<? extends DiscriminationConfigurer> discriminationConfigurer;

    @JsonIgnore
    private Class<? extends PossibleValuesProvider> possibleValuesProviderClass;

    @JsonIgnore
    private FieldAccessor fieldAccessor;

    @JsonProperty(ActivityEPConstants.COMMON_POSSIBLE_VALUES)
    private String commonPossibleValuesPath;

    @JsonIgnore
    private TranslationSettings.TranslationType translationType;

    @JsonIgnore
    private ConstraintDescriptors beanConstraints;

    @JsonIgnore
    private ConstraintDescriptors fieldConstraints;

    public ConstraintDescriptors getFieldConstraints() {
        return fieldConstraints;
    }

    public void setFieldConstraints(ConstraintDescriptors fieldConstraints) {
        this.fieldConstraints = fieldConstraints;
    }

    public ConstraintDescriptors getBeanConstraints() {
        return beanConstraints;
    }

    public void setBeanConstraints(ConstraintDescriptors beanConstraints) {
        this.beanConstraints = beanConstraints;
    }

    public APIField getField(String fieldName) {
        return children.getField(fieldName);
    }

    public List<APIField> getFieldsForFieldNameInternal(String fieldNameInternal) {
        return children.getFieldsForFieldNameInternal(fieldNameInternal);
    }

    public List<APIField> getFieldsWithDependency(String dependency) {
        return children.getFieldsWithDependency(dependency);
    }

    @JsonIgnore
    public APIField getPercentageField() {
        return children.getPercentageField();
    }

    public void setFieldAccessor(FieldAccessor fieldAccessor) {
        this.fieldAccessor = fieldAccessor;
    }

    public FieldAccessor getFieldAccessor() {
        return fieldAccessor;
    }

    public Class<? extends PossibleValuesProvider> getPossibleValuesProviderClass() {
        return possibleValuesProviderClass;
    }

    public void setPossibleValuesProviderClass(
            Class<? extends PossibleValuesProvider> possibleValuesProviderClass) {
        this.possibleValuesProviderClass = possibleValuesProviderClass;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isId() {
        return id;
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public APIField getIdChild() {
        return idChild;
    }

    public void setIdChild(APIField idChild) {
        this.idChild = idChild;
    }

    public boolean isIndependent() {
        return independent;
    }

    public void setIndependent(boolean independent) {
        this.independent = independent;
    }

    public UnwrappedTranslations getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(UnwrappedTranslations fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public String getFieldNameInternal() {
        return fieldNameInternal;
    }

    public void setFieldNameInternal(String fieldNameInternal) {
        this.fieldNameInternal = fieldNameInternal;
    }

    @JsonProperty("apiType")
    public APIType getApiType() {
        return apiType;
    }

    public void setApiType(APIType apiType) {
        this.apiType = apiType;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public boolean isIdOnly() {
        return idOnly;
    }

    public void setIdOnly(boolean idOnly) {
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
        if (children != null) {
            return children.getList();
        } else {
            return ImmutableList.of();
        }
    }

    public void setChildren(List<APIField> list) {
        children = new Fields(list);
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

    public String getDiscriminatorValue() {
        return discriminatorValue;
    }

    public void setDiscriminatorValue(String discriminatorValue) {
        this.discriminatorValue = discriminatorValue;
    }

    public String getDiscriminatorField() {
        return discriminatorField;
    }

    public void setDiscriminatorField(String discriminatorField) {
        this.discriminatorField = discriminatorField;
    }

    public Class<? extends DiscriminationConfigurer> getDiscriminationConfigurer() {
        return discriminationConfigurer;
    }

    public void setDiscriminationConfigurer(
            Class<? extends DiscriminationConfigurer> discriminationConfigurer) {
        this.discriminationConfigurer = discriminationConfigurer;
    }

    public String getCommonPossibleValuesPath() {
        return commonPossibleValuesPath;
    }

    public void setCommonPossibleValuesPath(String commonPossibleValuesPath) {
        this.commonPossibleValuesPath = commonPossibleValuesPath;
    }

    @JsonIgnore
    public TranslationSettings.TranslationType getTranslationType() {
        return translationType;
    }

    public void setTranslationType(TranslationSettings.TranslationType translationType) {
        this.translationType = translationType;
    }

    public String getDependencyRequired() {
        return dependencyRequired;
    }

    public void setDependencyRequired(String dependencyRequired) {
        this.dependencyRequired = dependencyRequired;
    }

    public String getUnconditionalRequired() {
        return unconditionalRequired;
    }

    public void setUnconditionalRequired(String unconditionalRequired) {
        this.unconditionalRequired = unconditionalRequired;
    }

    @Override
    public String toString() {
        return "APIField{" + "fieldName='" + fieldName + '\'' + ", fieldType='" + this.apiType.getFieldType() + '\''
                + ", itemType=" + this.apiType.getItemType()
                + ", fieldLabel=" + fieldLabel + ", fieldNameInternal='" + fieldNameInternal + '\'' + ", required='"
                + required + '\'' + ", idOnly=" + idOnly + ", importable=" + importable + ", translatable="
                + translatable + ", multipleValues=" + multipleValues
                + ", uniqueConstraint='" + uniqueConstraint + '\'' + ", percentageConstraint='" + percentageConstraint
                + '\'' + ", treeCollectionConstraint=" + treeCollectionConstraint + ", fieldLength=" + fieldLength
                + ", common-possible-values='" + commonPossibleValuesPath + "'"
                + ", children=" + children + ", dependencies=" + dependencies + ", percentage=" + percentage
                + ", regex_pattern=" + regexPattern + "}";
    }
}
