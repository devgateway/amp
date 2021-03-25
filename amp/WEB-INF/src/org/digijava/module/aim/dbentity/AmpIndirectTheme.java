package org.digijava.module.aim.dbentity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author Octavian Ciubotaru
 */
public class AmpIndirectTheme {

    @JsonIgnore
    private Long id;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ampThemeId",
            resolver = EntityResolver.class, scope = AmpTheme.class)
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("src-program")
    private AmpTheme oldTheme;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ampThemeId",
            resolver = EntityResolver.class, scope = AmpTheme.class)
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("dst-program")
    private AmpTheme newTheme;

    @JsonProperty("level")
    private Integer level;
    public AmpIndirectTheme() {
    }

    public AmpIndirectTheme(AmpTheme oldTheme, AmpTheme newTheme, Integer level) {
        this.oldTheme = oldTheme;
        this.newTheme = newTheme;
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AmpTheme getOldTheme() {
        return oldTheme;
    }

    public void setOldTheme(AmpTheme oldTheme) {
        this.oldTheme = oldTheme;
    }

    public AmpTheme getNewTheme() {
        return newTheme;
    }

    public void setNewTheme(AmpTheme newTheme) {
        this.newTheme = newTheme;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
