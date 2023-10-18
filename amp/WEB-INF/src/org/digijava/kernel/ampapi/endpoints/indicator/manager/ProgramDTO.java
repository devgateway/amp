package org.digijava.kernel.ampapi.endpoints.indicator.manager;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.digijava.module.aim.dbentity.AmpTheme;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonPropertyOrder({"id", "name", "code", "type", "deleted", "children"})
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


    public ProgramDTO(final AmpTheme program) {
        this.id = program.getAmpThemeId();
        this.name = program.getName();
        this.code = program.getThemeCode();
        this.type = program.getTypeCategoryValue().getValue();
        this.deleted = program.isSoftDeleted();
        this.children.addAll(program.getSiblings().stream().map(ProgramDTO::new).collect(Collectors.toList()));
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
}
