package org.digijava.module.aim.dbentity;


/**
 * Program Indicator.
 * this class defines connection between indicator and program (Theme). Most fields are defined in parent class.
 * Check hibernate mapping in IndicatorConnection.hbm.xml
 * @see IndicatorConnection
 * @author Irakli Kobiashvili
 *
 */
public class IndicatorTheme extends IndicatorConnection {
    
    private static final long serialVersionUID = 3L;

    private AmpTheme theme;

    public AmpTheme getTheme() {
        return theme;
    }
    public void setTheme(AmpTheme theme) {
        this.theme = theme;
    }
    
}
