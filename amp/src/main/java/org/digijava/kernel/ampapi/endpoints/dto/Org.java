package org.digijava.kernel.ampapi.endpoints.dto;

import io.swagger.annotations.ApiModelProperty;
import org.digijava.module.aim.dbentity.AmpOrganisation;

/**
 * A very brief view of {@link AmpOrganisation} class.
 *
 * @author Octavian Ciubotaru
 */
public class Org {

    @ApiModelProperty(example = "443")
    private Long id;

    @ApiModelProperty(example = "World Food Programme")
    private String name;

    @ApiModelProperty(example = "WFP")
    private String acronym;

    public Org(Long id, String name, String acronym) {
        this.id = id;
        this.name = name;
        this.acronym = acronym;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAcronym() {
        return acronym;
    }
}
