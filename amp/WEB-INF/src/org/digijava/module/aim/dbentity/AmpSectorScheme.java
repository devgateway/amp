package org.digijava.module.aim.dbentity ;

import java.io.Serializable;

import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.annotations.interchange.PossibleValueId;
import org.digijava.module.aim.annotations.interchange.PossibleValueValue;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.util.SectorUtil;
import javax.persistence.*;

@Entity
@Table(name = "AMP_SECTOR_SCHEME")

@TranslatableClass (displayName = "Sector Scheme")
public class AmpSectorScheme implements Serializable
{
    //IATI-check: might be relevant, but obtained from possible values
    @Id
    @PossibleValueId
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_sector_scheme_seq_generator")
    @SequenceGenerator(name = "amp_sector_scheme_seq_generator", sequenceName = "AMP_SECTOR_SCHEME_seq", allocationSize = 1)
    @Column(name = "amp_sec_scheme_id")
    private Long ampSecSchemeId;

    @Column(name = "sec_scheme_code")
    private String secSchemeCode;

    @Column(name = "sec_scheme_name")
    @PossibleValueValue
    @TranslatableField
    private String secSchemeName;

    @Column(name = "show_in_rm")
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
