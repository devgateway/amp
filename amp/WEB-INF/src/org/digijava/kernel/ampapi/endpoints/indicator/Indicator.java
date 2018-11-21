package org.digijava.kernel.ampapi.endpoints.indicator;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.gis.LocalizedDateSerializer;
import org.digijava.kernel.ampapi.endpoints.gis.services.AdmLevel;
import org.digijava.module.aim.dbentity.AmpIndicatorColor;

/**
 * FIXME This class need a good refactoring.
 *
 * Tasks:
 * 1. Remove gapAnalysis, it is used only when retrieving values with gap analysis enabled.
 * 2. Color ramp is read as an array of objects. However it is written as an id, ref to a predefined
 *    list of color ramps. Can we fix that?
 * 3. When building indicator object, see setColorRamp(), sometimes ramp is sorted while other it isn't.
 *    Also multiColor flag is not computed every time setColorRamp() is called.
 *    Also there is high probability that multiColor flag is computed incorrectly.
 *
 * @author Octavian Ciubotaru
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Indicator {

    private Long id;

    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.endpoints.gis.MultilingualLabelPH")
    private Map<String, String> name;

    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.endpoints.gis.MultilingualLabelPH")
    private Map<String, String> description;

    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.endpoints.gis.MultilingualLabelPH")
    private Map<String, String> unit;

    @ApiModelProperty(allowableValues = "[0-3]")
    private Long admLevelId;

    private String admLevelName;

    private AdmLevel adminLevel;

    private Long numberOfClasses;
    private Long accessTypeId;
    private Long indicatorTypeId;

    @ApiModelProperty("Are indicator values reflecting gap analysis? Computed only when retrieving indicator.")
    private Boolean gapAnalysis;

    @ApiModelProperty("Does this indicator support gap analysis? Computed only when listing indicators.")
    private Boolean canDoGapAnalysis;

    @ApiModelProperty("During update this property will be updated only if not null.")
    private Boolean zeroCategoryEnabled;

    @JsonSerialize(using = LocalizedDateSerializer.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createdOn;

    @JsonSerialize(using = LocalizedDateSerializer.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date updatedOn;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @JsonProperty(value = "isMultiColor", access = JsonProperty.Access.READ_ONLY)
    private Boolean multiColor;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<AmpIndicatorColor> colorRamp;

    @ApiModelProperty("During update this property will be updated only if not null.")
    private List<IndicatorValue> values;

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
}
