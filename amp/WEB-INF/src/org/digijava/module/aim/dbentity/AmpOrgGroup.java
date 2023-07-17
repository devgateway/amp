package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.dgfoundation.amp.ar.dimension.ARDimensionable;
import org.dgfoundation.amp.ar.dimension.DonorGroupDimension;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.helper.donorReport.OrgProfileValue;
import org.digijava.module.aim.helper.donorReport.ValueTranslatabePair;
import org.digijava.module.aim.util.HierarchyListable;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.NameableOrIdentifiable;
import javax.persistence.*;

@Entity
@Table(name = "AMP_ORG_GROUP")
@TranslatableClass (displayName = "Organisation Group")
public class AmpOrgGroup implements Serializable, Comparable, Identifiable, ARDimensionable, HierarchyListable,OrgProfileValue, NameableOrIdentifiable
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_ORG_GROUP_seq")
    @SequenceGenerator(name = "AMP_ORG_GROUP_seq", sequenceName = "AMP_ORG_GROUP_seq", allocationSize = 1)
    @Column(name = "amp_org_grp_id")
    private Long ampOrgGrpId;

    @Column(name = "org_grp_name")
    @TranslatableField
    private String orgGrpName;

    @Column(name = "org_grp_code")
    @TranslatableField
    private String orgGrpCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_type", nullable = false)
    private AmpOrgType orgType;

    @Column(name = "org_grp_background")
    private String orgGrpBackground;

    @Column(name = "org_grp_description")
    private String orgGrpDescription;

    @Column(name = "org_grp_keyareas")
    private String orgGrpKeyAreas;

    @Column(name = "deleted")
    private Boolean deleted;
    //IATI-check: to be ignored

    @Transient
    private boolean translateable   = true;

    /**
     * @return Returns the ampOrgGrpId.
     */
    public Long getAmpOrgGrpId() {
        return ampOrgGrpId;
    }
    /**
     * @param ampOrgGrpId The ampOrgGrpId to set.
     */
    public void setAmpOrgGrpId(Long ampOrgGrpId) {
        this.ampOrgGrpId = ampOrgGrpId;
    }

    /**
     * @return Returns the orgGrpCode.
     */
    public String getOrgGrpCode() {
        return orgGrpCode;
    }
    /**
     * @param orgGrpCode The orgGrpCode to set.
     */
    public void setOrgGrpCode(String orgGrpCode) {
        this.orgGrpCode = orgGrpCode;
    }
    /**
     * @return Returns the orgGrpName.
     */
    public String getOrgGrpName() {
        return orgGrpName;
    }
    /**
     * @param orgGrpName The orgGrpName to set.
     */
    public void setOrgGrpName(String orgGrpName) {
        this.orgGrpName = orgGrpName;
    }

    /**
     * @return Returns the orgType.
     */
    public AmpOrgType getOrgType() {
        return orgType;
    }
    /**
     * @param orgType The orgType to set.
     */
    public void setOrgType(AmpOrgType orgType) {
        this.orgType = orgType;
    }
    public int compareTo(Object arg0) {
        // TODO Auto-generated method stub
        return this.orgGrpName.trim().toLowerCase().compareTo(((AmpOrgGroup)arg0).getOrgGrpName().trim().toLowerCase());
        //return 0;
    }
    public Object getIdentifier() {
        return this.getAmpOrgGrpId();
    }
    public String toString() {
        return this.getOrgGrpName();
    }
    public Class getDimensionClass() {
        return DonorGroupDimension.class;
    }
    
    @Override
    public Collection<AmpOrgGroup> getChildren() {
        return null;
    }
    @Override
    public int getCountDescendants() {
        return 1;
    }
    @Override
    public String getLabel() {
        return this.orgGrpName;
    }
    @Override
    public String getUniqueId() {
        return String.valueOf(this.ampOrgGrpId.longValue());
    }
    @Override
    public boolean getTranslateable() {
        return translateable;
    }
    @Override
    public void setTranslateable(boolean translateable) {
        this.translateable = translateable;
    }

    public String getAdditionalSearchString() {
        return this.orgGrpCode;
    }
    public List<ValueTranslatabePair> getValuesForOrgReport(){
        List<ValueTranslatabePair> values=new ArrayList<ValueTranslatabePair>();
        ValueTranslatabePair value=new ValueTranslatabePair(Arrays.asList(new String[]{getOrgGrpName()}),false);
        values.add(value);
        return values;
    }
    @Override
    public String[] getSubHeaders() {
        // TODO Auto-generated method stub
        return null;
    }
    public String getOrgGrpBackground() {
        return orgGrpBackground;
    }
    public void setOrgGrpBackground(String orgGrpBackground) {
        this.orgGrpBackground = orgGrpBackground;
    }
    public String getOrgGrpDescription() {
        return orgGrpDescription;
    }
    public void setOrgGrpDescription(String orgGrpDescription) {
        this.orgGrpDescription = orgGrpDescription;
    }
    public String getOrgGrpKeyAreas() {
        return orgGrpKeyAreas;
    }
    public void setOrgGrpKeyAreas(String orgKeyAreas) {
        this.orgGrpKeyAreas = orgKeyAreas;
    }
    
    public String getName() {
        return this.orgGrpName;
    }
    
    public void setName(String orgGrpName) {
        this.setOrgGrpName(orgGrpName);
    }
    
    public static String hqlStringForName(String idSource)
    {
        return InternationalizedModelDescription.getForProperty(AmpOrgGroup.class, "orgGrpName").getSQLFunctionCall(idSource + ".ampOrgGrpId");
    }
    public Boolean getDeleted() {
        return deleted;
    }
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
