package org.digijava.kernel.ampapi.endpoints.indicator.manager;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.digijava.kernel.ampapi.endpoints.serializers.LocalizedDateDeserializer;
import org.digijava.kernel.ampapi.endpoints.serializers.LocalizedDateSerializer;
import org.digijava.module.aim.dbentity.AmpTheme;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@JsonPropertyOrder({"id", "name", "code", "type", "deleted", "children", "startDate", "endDate"})
public class ProgramDTO {

    @JsonProperty("id")
    private final Long id;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("code")
    private final String code;

    @JsonProperty("type")
    private final String type;

    @JsonProperty("deleted")
    private final boolean deleted;

    @JsonProperty("children")
    private final List<ProgramDTO> children = new ArrayList<>();

    @JsonSerialize(using = LocalizedDateSerializer.class)
    @JsonDeserialize(using = LocalizedDateDeserializer.class)
    private Date startDate;

    @JsonSerialize(using = LocalizedDateSerializer.class)
    @JsonDeserialize(using = LocalizedDateDeserializer.class)
    private Date endDate;


    public ProgramDTO(final AmpTheme program) {
        this.id = program.getAmpThemeId();
        this.name = program.getName();
        this.code = program.getThemeCode();
        this.type = program.getTypeCategoryValue().getValue();
        this.deleted = program.isSoftDeleted();
        this.children.addAll(program.getSiblings().stream().map(ProgramDTO::new).collect(Collectors.toList()));
        this.startDate = program.getStartDate();
        this.endDate = program.getEndDate();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public List<ProgramDTO> getChildren() {
        return children;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
