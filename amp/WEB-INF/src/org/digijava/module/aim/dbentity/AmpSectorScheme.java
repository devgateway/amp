package org.digijava.module.aim.dbentity ;

import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.annotations.interchange.PossibleValueId;
import org.digijava.module.aim.annotations.interchange.PossibleValueValue;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.util.SectorUtil;

import java.io.Serializable;
@TranslatableClass (displayName = "Sector Scheme")
public class AmpSectorScheme implements Serializable
{
    //IATI-check: might be relevant, but obtained from possible values
    @PossibleValueId
    private Long ampSecSchemeId ;
    private String secSchemeCode ;
    @PossibleValueValue
    @TranslatableField
    private String secSchemeName ;

    private Boolean showInRMFilters;

    public Boolean getShowInRMFilters() {
        return showInRMFilters;
    }

    public void setShowInRMFilters(Boolean showInRMFilters) {
        this.showInRMFilters = showInRMFilters;
    }

    /**
     * @return
     */
    public Long getAmpSecSchemeId() {
        return ampSecSchemeId;
    }

    /**
     * @return
     */
    public String getSecSchemeCode() {
        return secSchemeCode;
    }

    /**
     * @return
     */
    public String getSecSchemeName() {
        return secSchemeName;
    }

    /**
     * @param long1
     */
    public void setAmpSecSchemeId(Long long1) {
        ampSecSchemeId = long1;
    }

    /**
     * @param string
     */
    public void setSecSchemeCode(String string) {
        secSchemeCode = string;
    }

    /**
     * @param string
     */
    public void setSecSchemeName(String string) {
        secSchemeName = string;
    }
        
       public boolean isUsed() {
        boolean used = true;
        try {
            used = SectorUtil.isSchemeUsed(ampSecSchemeId);

        } catch (DgException ex) {
            ex.printStackTrace();

        }
        return used;
    }
       
    public static String hqlStringForName(String idSource)
    {
        return InternationalizedModelDescription.getForProperty(AmpSectorScheme.class, "secSchemeName").getSQLFunctionCall(idSource + ".ampSecSchemeId");
    }

}
