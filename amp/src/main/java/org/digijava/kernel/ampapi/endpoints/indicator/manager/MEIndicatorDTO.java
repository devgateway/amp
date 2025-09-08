package org.digijava.kernel.ampapi.endpoints.indicator.manager;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.dto.AmpIndicatorDisaggregationValueDto;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.validators.ValidProgramId;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.validators.ValidSectorIds;
import org.digijava.kernel.ampapi.endpoints.serializers.LocalizedDateDeserializer;
import org.digijava.kernel.ampapi.endpoints.serializers.LocalizedDateSerializer;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorGlobalValue;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static org.digijava.module.aim.dbentity.AmpIndicatorValue.BASE;
import static org.digijava.module.aim.dbentity.AmpIndicatorValue.TARGET;

/**
 * DTO for AmpIndicator
 */
@JsonPropertyOrder({"id", "name", "description", "code", "ascending", "creationDate", "sectors", "programId" })
public class MEIndicatorDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    @NotNull
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("code")
    @NotNull
    private String code;

    @JsonProperty("ascending")
    private boolean ascending;

    @JsonProperty("creationDate")
    @JsonSerialize(using = LocalizedDateSerializer.class)
    @JsonDeserialize(using = LocalizedDateDeserializer.class)
    @NotNull
    @ApiModelProperty(dataType = "java.util.Date", example = "02/02/2023")
    private Date creationDate;

    @JsonProperty("base")
    private AmpIndicatorGlobalValue baseValue;

    @JsonProperty("target")
    private AmpIndicatorGlobalValue targetValue;

    @JsonProperty("sectors")
    @ValidSectorIds
    private List<Long> sectorIds = new ArrayList<>();

    @JsonProperty("programId")
    @ValidProgramId
    private Long programId;

    @JsonProperty("indicatorsCategory")
    private Long indicatorsCategory;

    @JsonProperty("outputId")
    private Long outputId;

    @JsonProperty("outcomeId")
    private Long outcomeId;

    @JsonProperty("relevanceForClimateChange")
    private String relevanceForClimateChange;

    @JsonProperty("indicatorType")
    private Long indicatorType;

    @JsonProperty("logframeLinks")
    private Set<Long> logframeLinks = new HashSet<>();

    @JsonProperty("data")
    private String data;

    @JsonProperty("dataSource")
    private String dataSource;

    @JsonProperty("disaggregation")
    private Set<Long> disaggregation = new HashSet<>();

    @JsonProperty("unitOfMeasure")
    private Long unitOfMeasure;

    @JsonProperty("calculationMethod")
    private String calculationMethod;

    @JsonProperty("responsibleOrganizations")
    private Set<Long> responsibleOrganizations = new HashSet<>();

    @JsonProperty("frequency")
    private Long frequency;
    @JsonProperty("disaggregationValues")
    private Set<AmpIndicatorDisaggregationValueDto> disaggregationValues = new HashSet<>();

    public MEIndicatorDTO() {

    }

    public MEIndicatorDTO(final AmpIndicator indicator) {
        this.id = indicator.getIndicatorId();
        this.name = indicator.getName();
        this.description = indicator.getDescription();
        this.code = indicator.getCode();
        this.ascending = indicator.getType() == null || indicator.getType().equals("A");
        this.creationDate = indicator.getCreationDate();
        this.baseValue = indicator.getBaseValue();
        this.targetValue = indicator.getTargetValue();
        this.sectorIds = indicator.getSectors().stream().map(AmpSector::getAmpSectorId).collect(Collectors.toList());
        this.programId = indicator.getProgram() != null ? indicator.getProgram().getAmpThemeId() : null;
        this.indicatorsCategory = indicator.getIndicatorsCategory() != null ? indicator.getIndicatorsCategory().getId() : null;
        this.outputId = indicator.getOutput() != null ? indicator.getOutput().getId() : null;
        this.outcomeId = indicator.getOutcome() != null ? indicator.getOutcome().getId() : null;
        this.relevanceForClimateChange = indicator.getRelevanceForClimateChange();
        this.indicatorType = indicator.getIndicatorType()!=null ? indicator.getIndicatorType().getId() : null;
        this.logframeLinks = indicator.getLogframeLinks();
        this.data = indicator.getData();
        this.dataSource = indicator.getDataSource();
        this.disaggregation = indicator.getDisaggregation()!=null ? indicator.getDisaggregation().stream().map(AmpCategoryValue::getId).collect(Collectors.toSet()) : null;
        this.unitOfMeasure = indicator.getUnitOfMeasure()!=null ? indicator.getUnitOfMeasure().getId() : null;
        this.calculationMethod = indicator.getCalculationMethod();
        this.responsibleOrganizations = indicator.getResponsibleOrganizations()!=null ? indicator.getResponsibleOrganizations().stream().map(AmpOrganisation::getAmpOrgId).collect(Collectors.toSet()) : null;
        this.frequency = indicator.getFrequency()!=null ? indicator.getFrequency().getId() : null;
        if (indicator.getDisaggregationValues() != null) {
            this.disaggregationValues = indicator.getDisaggregationValues().stream().map(dv -> {
                AmpIndicatorDisaggregationValueDto dto = new AmpIndicatorDisaggregationValueDto();
                dto.setId(dv.getId());
                dto.setParentCategoryId(dv.getParentCategory() != null ? dv.getParentCategory().getId() : null);
                dto.setChildCategoryId(dv.getChildCategory() != null ? dv.getChildCategory().getId() : null);
                dto.setBaseValue(dv.getBaseValue());
                dto.setTargetValue(dv.getTargetValue());
                return dto;
            }).collect(Collectors.toSet());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(final boolean ascending) {
        this.ascending = ascending;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }

    public AmpIndicatorGlobalValue getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(final AmpIndicatorGlobalValue baseValue) {
        if (baseValue != null) {
            baseValue.setType(BASE);
        }

        this.baseValue = baseValue;
    }

    public AmpIndicatorGlobalValue getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(final AmpIndicatorGlobalValue targetValue) {
        if (targetValue != null) {
            targetValue.setType(TARGET);
        }
        this.targetValue = targetValue;
    }

    public List<Long> getSectorIds() {
        return sectorIds;
    }

    public void setSectorIds(final List<Long> sectorIds) {
        this.sectorIds = sectorIds;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(final Long programId) {
        this.programId = programId;
    }

    public Long getIndicatorsCategory() {
        return indicatorsCategory;
    }

    public void setIndicatorsCategory(Long indicatorsCategory) {
        this.indicatorsCategory = indicatorsCategory;
    }

    public Long getOutputId() {
        return outputId;
    }

    public void setOutputId(Long outputId) {
        this.outputId = outputId;
    }

    public Long getOutcomeId() {
        return outcomeId;
    }

    public void setOutcomeId(Long outcomeId) {
        this.outcomeId = outcomeId;
    }

    public String getRelevanceForClimateChange() { return relevanceForClimateChange; }
    public void setRelevanceForClimateChange(String relevanceForClimateChange) { this.relevanceForClimateChange = relevanceForClimateChange; }

    public Long getIndicatorType() { return indicatorType; }
    public void setIndicatorType(Long indicatorType) { this.indicatorType = indicatorType; }

    public Set<Long> getLogframeLinks() { return logframeLinks; }
    public void setLogframeLinks(Set<Long> logframeLinks) { this.logframeLinks = logframeLinks; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }

    public Set<Long> getDisaggregation() { return disaggregation; }
    public void setDisaggregation(Set<Long> disaggregation) { this.disaggregation = disaggregation; }

    public Long getUnitOfMeasure() { return unitOfMeasure; }
    public void setUnitOfMeasure(Long unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }

    public String getCalculationMethod() { return calculationMethod; }
    public void setCalculationMethod(String calculationMethod) { this.calculationMethod = calculationMethod; }

    public Set<Long> getResponsibleOrganizations() { return responsibleOrganizations; }
    public void setResponsibleOrganizations(Set<Long> responsibleOrganizations) { this.responsibleOrganizations = responsibleOrganizations; }

    public Long getFrequency() { return frequency; }
    public void setFrequency(Long frequency) { this.frequency = frequency; }

    public Set<AmpIndicatorDisaggregationValueDto> getDisaggregationValues() {
        return disaggregationValues;
    }

    public void setDisaggregationValues(Set<AmpIndicatorDisaggregationValueDto> disaggregationValues) {
        this.disaggregationValues = disaggregationValues;
    }
}
