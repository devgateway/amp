package org.digijava.kernel.ampapi.endpoints.indicator.manager;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.common.TranslationUtil;
import org.digijava.kernel.ampapi.endpoints.gis.LocalizedDateSerializer;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpSector;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * DTO for AmpIndicator
 */
@JsonPropertyOrder({"id", "name", "description", "code", "ascending", "creationDate", "sectors"})
public class AmpIndicatorDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.MultilingualLabelPH")
    private Map<String, String> name;

    @JsonProperty("description")
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.MultilingualLabelPH")
    private Map<String, String> description;

    @JsonProperty("code")
    private String code;

    @JsonProperty("ascending")
    private boolean ascending;

    @JsonProperty("creationDate")
    @JsonSerialize(using = LocalizedDateSerializer.class)
    private Date creationDate;

    @JsonProperty("sectors")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ampSectorId")
    @JsonIdentityReference(alwaysAsId = true)
    private List<AmpSector> sectors;

    public AmpIndicatorDTO(final AmpIndicator indicator) {
        this.id = indicator.getIndicatorId();
        this.name = TranslationUtil.loadTranslationsForField(AmpIndicator.class, "name",
                indicator.getName(), indicator.getIndicatorId());
        this.description = TranslationUtil.loadTranslationsForField(AmpIndicator.class, "description",
                indicator.getDescription(), indicator.getIndicatorId());
        this.code = indicator.getCode();
        this.ascending = indicator.getType() == null || indicator.getType().equals("A");
        this.creationDate = indicator.getCreationDate();
        this.sectors = indicator.getSectors().stream().collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Map<String, String> getName() {
        return name;
    }

    public void setName(final Map<String, String> name) {
        this.name = name;
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public void setDescription(final Map<String, String> description) {
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

    public List<AmpSector> getSectors() {
        return sectors;
    }

    public void setSectors(final List<AmpSector> sectors) {
        this.sectors = sectors;
    }
}
