package org.digijava.kernel.ampapi.endpoints.indicator.manager;

import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.digijava.kernel.ampapi.endpoints.serializers.LocalizedDateDeserializer;
import org.digijava.kernel.ampapi.endpoints.serializers.LocalizedDateSerializer;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpTheme;


@JsonPropertyOrder({"ampProgramSettingsId", "name", "programName", "allowMultiple" ,"startDate", "endDate", "children"})
public class ProgramSchemeDTO {

    @JsonProperty("ampProgramSettingsId")
    private final Long ampProgramSettingsId;

    @JsonProperty("programName")
    private final String programName;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("allowMultiple")
    private final Boolean allowMultiple;

    @JsonProperty("startDate")
    @JsonSerialize(using = LocalizedDateSerializer.class)
    @JsonDeserialize(using = LocalizedDateDeserializer.class)
    private final Date startDate;

    @JsonProperty("endDate")
    @JsonSerialize(using = LocalizedDateSerializer.class)
    @JsonDeserialize(using = LocalizedDateDeserializer.class)
    private final Date endDate;

    @JsonProperty("children")
    private final Set<ProgramDTO> children = new HashSet<>();


    public ProgramSchemeDTO(final AmpActivityProgramSettings setting) {
        this.ampProgramSettingsId = setting.getAmpProgramSettingsId();
        this.name = setting.getName();
        this.programName = setting.getDefaultHierarchy().getName();
        this.allowMultiple = setting.isAllowMultiple();
        this.startDate = setting.getStartDate();
        this.endDate = setting.getEndDate();
        this.children.addAll(setting.getDefaultHierarchy().getSiblings().stream().map(ProgramDTO::new).collect(Collectors.toList()));
    }

    public Long getAmpProgramSettingsId() {
        return ampProgramSettingsId;
    }

    public String getName() {
        return name;
    }

    public String getProgramName() {
        return programName;
    }


    public Boolean getAllowMultiple() {
        return allowMultiple;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Set<ProgramDTO> getChildren() {
        return children;
    }

}
