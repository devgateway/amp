package org.digijava.module.aim.dbentity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author Viorel Chihai
 */
public class AmpThemeMapping {

    @JsonIgnore
    private Long id;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ampThemeId",
            resolver = EntityResolver.class, scope = AmpTheme.class)
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("src-program")
    private AmpTheme srcTheme;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ampThemeId",
            resolver = EntityResolver.class, scope = AmpTheme.class)
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("dst-program")
    private AmpTheme dstTheme;

    public AmpThemeMapping() {
    }

    public AmpThemeMapping(AmpTheme srcTheme, AmpTheme dstTheme) {
        this.srcTheme = srcTheme;
        this.dstTheme = dstTheme;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpTheme getSrcTheme() {
        return srcTheme;
    }

    public void setSrcTheme(AmpTheme srcTheme) {
        this.srcTheme = srcTheme;
    }

    public AmpTheme getDstTheme() {
        return dstTheme;
    }

    public void setDstTheme(AmpTheme dstTheme) {
        this.dstTheme = dstTheme;
    }
}
