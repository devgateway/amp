package org.digijava.kernel.ampapi.endpoints.indicator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.gis.services.AdmLevel;
import org.digijava.kernel.ampapi.endpoints.serializers.LocalizedDateSerializer;
import org.digijava.module.aim.dbentity.AmpIndicatorColor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * FIXME This class need a good refactoring.
 *
 * Tasks:
 * 1. Remove gapAnalysis, it is used only when retrieving values with gap analysis enabled.
 * 2. Color ramp is read as an array of objects. However it is written as an id, ref to a predefined
 *    list of color ramps. Can we fix that?
 *
 * @author Octavian Ciubotaru
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Indicator {

    public static class IndicatorView {
    }

    public static class LayerView extends IndicatorView {
    }

    public enum Option {
        @JsonProperty("overwrite") OVERWRITE,
        @JsonProperty("new") NEW
    }

    @JsonView(IndicatorView.class)
    private Long id;

    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.MultilingualLabelPH")
    @JsonView(IndicatorView.class)
    private Map<String, String> name;

    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.MultilingualLabelPH")
    @JsonView(IndicatorView.class)
    private Map<String, String> description;

    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.MultilingualLabelPH")
    @JsonView(IndicatorView.class)
    private Map<String, String> unit;

    @ApiModelProperty(allowableValues = "[0-3]")
    @JsonView(IndicatorView.class)
    private Long admLevelId;

    @JsonView(IndicatorView.class)
    private String admLevelName;

    @JsonView(IndicatorView.class)
    private AdmLevel adminLevel;

    @JsonView(IndicatorView.class)
    private Long numberOfClasses;

    @JsonView(IndicatorView.class)
    private Long accessTypeId;

    @JsonView(IndicatorView.class)
    private Long indicatorTypeId;

    @ApiModelProperty("Are indicator values reflecting gap analysis? Computed only when retrieving indicator.")
    @JsonView(IndicatorView.class)
    private Boolean gapAnalysis;

    @ApiModelProperty("Does this indicator support gap analysis? Computed only when listing indicators.")
    @JsonView(IndicatorView.class)
    private Boolean canDoGapAnalysis;

    @ApiModelProperty("During update this property will be updated only if not null.")
    @JsonView(IndicatorView.class)
    private Boolean zeroCategoryEnabled;

    @JsonSerialize(using = LocalizedDateSerializer.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(IndicatorView.class)
    private Date createdOn;

    @JsonSerialize(using = LocalizedDateSerializer.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(IndicatorView.class)
    private Date updatedOn;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(IndicatorView.class)
    private String createdBy;

    @JsonProperty(value = "isMultiColor", access = JsonProperty.Access.READ_ONLY)
    @JsonView(IndicatorView.class)
    private Boolean multiColor;

    @JsonProperty(value = IndicatorEPConstants.IS_POPULATION, access = JsonProperty.Access.READ_ONLY)
    @JsonView(IndicatorView.class)
    private Boolean population;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(IndicatorView.class)
    private List<AmpIndicatorColor> colorRamp;

    @ApiModelProperty("During update this property will be updated only if not null.")
    @JsonView(IndicatorView.class)
    private List<IndicatorValue> values;

    @ApiModelProperty("Color ramp id to use. See `GET /indicator/amp-color`. "
            + "During update this property will be updated only if not null.")
    @JsonView(LayerView.class)
    private Long colorRampId;

    @ApiModelProperty("The workspaces this indicator is shared with. "
            + "During update this property will be updated only if not null.")
    @JsonView(LayerView.class)
    private List<Long> sharedWorkspaces;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ApiModelProperty("Controls how indicator values are updated.")
    @JsonView(LayerView.class)
    private Indicator.Option option = Indicator.Option.OVERWRITE;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty("Number indicator values imported.")
    @JsonView(LayerView.class)
    private Integer numberOfImportedRecords;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, String> getName() {
        return name;
    }

    public void setName(Map<String, String> name) {
        this.name = name;
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public void setDescription(Map<String, String> description) {
        this.description = description;
    }

    public Map<String, String> getUnit() {
        return unit;
    }

    public void setUnit(Map<String, String> unit) {
        this.unit = unit;
    }

    public Long getAdmLevelId() {
        return admLevelId;
    }

    public void setAdmLevelId(Long admLevelId) {
        this.admLevelId = admLevelId;
    }

    public String getAdmLevelName() {
        return admLevelName;
    }

    public void setAdmLevelName(String admLevelName) {
        this.admLevelName = admLevelName;
    }

    public AdmLevel getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(AdmLevel adminLevel) {
        this.adminLevel = adminLevel;
    }

    public Long getNumberOfClasses() {
        return numberOfClasses;
    }

    public void setNumberOfClasses(Long numberOfClasses) {
        this.numberOfClasses = numberOfClasses;
    }

    public Long getAccessTypeId() {
        return accessTypeId;
    }

    public void setAccessTypeId(Long accessTypeId) {
        this.accessTypeId = accessTypeId;
    }

    public Long getIndicatorTypeId() {
        return indicatorTypeId;
    }

    public void setIndicatorTypeId(Long indicatorTypeId) {
        this.indicatorTypeId = indicatorTypeId;
    }

    public Boolean getCanDoGapAnalysis() {
        return canDoGapAnalysis;
    }

    public void setCanDoGapAnalysis(Boolean canDoGapAnalysis) {
        this.canDoGapAnalysis = canDoGapAnalysis;
    }

    public Boolean getZeroCategoryEnabled() {
        return zeroCategoryEnabled;
    }

    public void setZeroCategoryEnabled(Boolean zeroCategoryEnabled) {
        this.zeroCategoryEnabled = zeroCategoryEnabled;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getMultiColor() {
        return multiColor;
    }

    public void setMultiColor(Boolean multiColor) {
        this.multiColor = multiColor;
    }

    public List<AmpIndicatorColor> getColorRamp() {
        return colorRamp;
    }

    public void setColorRamp(List<AmpIndicatorColor> colorRamp) {
        this.colorRamp = colorRamp;
    }

    public List<IndicatorValue> getValues() {
        return values;
    }

    public void setValues(List<IndicatorValue> values) {
        this.values = values;
    }

    public Boolean getGapAnalysis() {
        return gapAnalysis;
    }

    public void setGapAnalysis(Boolean gapAnalysis) {
        this.gapAnalysis = gapAnalysis;
    }

    public Long getColorRampId() {
        return colorRampId;
    }

    public void setColorRampId(Long colorRampId) {
        this.colorRampId = colorRampId;
    }

    public List<Long> getSharedWorkspaces() {
        return sharedWorkspaces;
    }

    public void setSharedWorkspaces(List<Long> sharedWorkspaces) {
        this.sharedWorkspaces = sharedWorkspaces;
    }

    public Indicator.Option getOption() {
        return option;
    }

    public void setOption(Indicator.Option option) {
        this.option = option;
    }

    public Integer getNumberOfImportedRecords() {
        return numberOfImportedRecords;
    }

    public void setNumberOfImportedRecords(Integer numberOfImportedRecords) {
        this.numberOfImportedRecords = numberOfImportedRecords;
    }

    public Boolean getPopulation() {
        return population;
    }

    public void setPopulation(Boolean population) {
        this.population = population;
    }
}
