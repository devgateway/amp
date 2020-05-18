package org.digijava.module.aim.dbentity;

/**
 * @author Octavian Ciubotaru
 */
public class AmpIndirectTheme {

    private Long id;

    private AmpTheme oldTheme;

    private AmpTheme newTheme;

    public AmpIndirectTheme() {
    }

    public AmpIndirectTheme(AmpTheme oldTheme, AmpTheme newTheme) {
        this.oldTheme = oldTheme;
        this.newTheme = newTheme;
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
}
