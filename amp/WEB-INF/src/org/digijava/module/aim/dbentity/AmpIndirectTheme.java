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

    @JsonProperty("levelSrc")
    private Integer levelSrc;

    @JsonProperty("levelDst")
    private Integer levelDst;

    public AmpIndirectTheme() {
    }

    public AmpIndirectTheme(AmpTheme oldTheme, AmpTheme newTheme, Integer levelSrc, Integer levelDst) {
        this.oldTheme = oldTheme;
        this.newTheme = newTheme;
        this.levelSrc = levelSrc;
        this.levelDst = levelDst;
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

    public Integer getLevelSrc() {
        return levelSrc;
    }

    public void setLevelSrc(Integer levelSrc) {
        this.levelSrc = levelSrc;
    }

    public Integer getLevelDst() {
        return levelDst;
    }

    public void setLevelDst(Integer levelDst) {
        this.levelDst = levelDst;
    }
}
