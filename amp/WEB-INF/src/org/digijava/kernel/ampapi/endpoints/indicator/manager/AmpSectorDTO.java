package org.digijava.kernel.ampapi.endpoints.indicator.manager;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import org.digijava.kernel.ampapi.endpoints.common.TranslationUtil;
import org.digijava.module.aim.dbentity.AmpSector;

import java.util.Map;

@JsonPropertyOrder({"id", "name", "code"})
public class AmpSectorDTO {

    @JsonProperty("id")
    private final Long id;

    @JsonProperty("name")
    @ApiModelProperty(dataType = "org.digijava.kernel.ampapi.swagger.types.MultilingualLabelPH")
    private final Map<String, String> name;

    @JsonProperty("code")
    private final String code;

    public AmpSectorDTO(AmpSector sector) {
        this.id = sector.getAmpSectorId();
        this.name = TranslationUtil.loadTranslationsForField(AmpSector.class, "name", sector.getName(), sector.getAmpSectorId());
        this.code = sector.getSectorCode();
    }
}
