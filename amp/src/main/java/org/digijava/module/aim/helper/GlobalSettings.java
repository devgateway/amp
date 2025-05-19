package org.digijava.module.aim.helper;


/**
 * 
 * 
 * There is one object of this class which is initialized at context start-up
 * (AMPStartupListener). This bean is accesible from the view using the
 * following code: 
 * <p>
 * 
 * &lt;logic:equal name="globalSettings" scope="application" ...    &gt;
 * 
 * <p>
 * Also, this bean should be updated each time GlobalSettings are being updated:
 * <p>
 * <code>
 * globalSettings = (GlobalSettings)getServlet().getServletContext().getAttribute(Constants.GLOBAL_SETTINGS);
 * globalSettings.setXX(FeaturesUtil.isXXEnabled());
 * </code> 
 * <p>
 * The name of the bean is Constants.GLOBAL_SETTINGS.
 * 
 * @author Mauricio Coria - coriamauricio@gmail.com
 */
public class GlobalSettings {
    private Boolean showComponentFundingByYear;

    private static final GlobalSettings INSTANCE = new GlobalSettings();
    
    private GlobalSettings(){}
    
    public static GlobalSettings getInstance(){
        return INSTANCE;
    }

    public void setShowComponentFundingByYear(Boolean showComponentFundingByYear) {
        this.showComponentFundingByYear = showComponentFundingByYear;
    }

    public Boolean getShowComponentFundingByYear() {
        return showComponentFundingByYear;
    }
    
}
