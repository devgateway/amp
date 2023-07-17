package org.digijava.module.aim.dbentity;


import javax.persistence.*;

/**
 * Program Indicator.
 * this class defines connection between indicator and program (Theme). Most fields are defined in parent class.
 * Check hibernate mapping in IndicatorConnection.hbm.xml
 * @see IndicatorConnection
 * @author Irakli Kobiashvili
 *
 */
@Entity
@DiscriminatorValue("p")
public class IndicatorTheme extends IndicatorConnection {
    
    private static final long serialVersionUID = 3L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private AmpTheme theme;

    public AmpTheme getTheme() {
        return theme;
    }
    public void setTheme(AmpTheme theme) {
        this.theme = theme;
    }
    
}
