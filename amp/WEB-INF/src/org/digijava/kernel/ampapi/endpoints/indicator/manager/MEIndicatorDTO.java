package org.digijava.kernel.ampapi.endpoints.indicator.manager;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.digijava.kernel.ampapi.endpoints.indicator.manager.validators.*;
import org.digijava.kernel.ampapi.endpoints.serializers.LocalizedDateDeserializer;
import org.digijava.kernel.ampapi.endpoints.serializers.LocalizedDateSerializer;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorGlobalValue;
import org.digijava.module.aim.dbentity.AmpSector;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.digijava.module.aim.dbentity.AmpIndicatorValue.BASE;
import static org.digijava.module.aim.dbentity.AmpIndicatorValue.TARGET;

/**
 * DTO for AmpIndicator
 */
@JsonPropertyOrder({"id", "name", "description", "code", "ascending", "creationDate", "sectors", "programId"})
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
}
