package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import org.dgfoundation.amp.ar.dimension.ARDimensionable;
import org.dgfoundation.amp.ar.dimension.DonorTypeDimension;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.util.HierarchyListable;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.NameableOrIdentifiable;

@TranslatableClass (displayName = "Organisation Type")
public class AmpOrgType implements Serializable,Comparable,Identifiable, ARDimensionable, HierarchyListable, NameableOrIdentifiable {
    //IATI-check: to not be ignored
    private Long ampOrgTypeId;
    @TranslatableField
    private String orgType;
    
    @TranslatableField
    private String orgTypeCode;
    @Deprecated
    private Boolean orgTypeIsGovernmental;
    //private Set organizations;
    
    // NGO, Governmental etc.
    private String classification;
    
    private boolean translateable   = true;
    
    private Set<AmpOrgGroup> orgGroups;
    
    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }
    
    /**
     * @return Returns the ampOrgTypeId.
     */
    public Long getAmpOrgTypeId() {
        return ampOrgTypeId;
    }
    /**
     * @param ampOrgTypeId The ampOrgTypeId to set.
     */
    public void setAmpOrgTypeId(Long ampOrgTypeId) {
        this.ampOrgTypeId = ampOrgTypeId;
    }
    /**
     * @return Returns the orgType.
     */
    public String getOrgType() {
        return orgType;
    }
    /**
     * @param orgType The orgType to set.
     */
    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }
    /**
     * @return Returns the orgTypeCode.
     */
    public String getOrgTypeCode() {
        return orgTypeCode;
    }
    /**
     * @param orgTypeCode The orgTypeCode to set.
     */
    public void setOrgTypeCode(String orgTypeCode) {
        this.orgTypeCode = orgTypeCode;
    }
    public int compareTo(Object arg0) {
        // TODO Auto-generated method stub

        return this.orgType.compareTo(((AmpOrgType)arg0).getOrgType());  
        //return 0;
    }

    public String toString() {
        if (orgType != null) {
            return orgType;
        }
        return "";
    }
    public Object getIdentifier() {
        return ampOrgTypeId;
    }
    
    public Class<?> getDimensionClass() {
        return DonorTypeDimension.class;
    }
    public Boolean getOrgTypeIsGovernmental() {
        return orgTypeIsGovernmental;
    }
    public void setOrgTypeIsGovernmental(Boolean orgTypeIsGovernmental) {
        this.orgTypeIsGovernmental = orgTypeIsGovernmental;
    }

    @Override
    public Collection<AmpOrgType> getChildren() {
        return null;
    }

    @Override
    public int getCountDescendants() {
        return 1;
    }

    @Override
    public String getLabel() {
        return this.orgType;
    }

    @Override
    public String getUniqueId() {
        return String.valueOf(this.ampOrgTypeId.longValue());
    }

    @Override
    public boolean getTranslateable() {
        return translateable;
    }

    @Override
    public void setTranslateable(boolean translateable) {
        this.translateable = translateable;
    }

    @Override
    public String getAdditionalSearchString() {
        return this.orgTypeCode;
    }
    
    public Set<AmpOrgGroup> getOrgGroups(){
        return this.orgGroups;
    }
    
    public void setOrgGroups(Set<AmpOrgGroup> aog){
        this.orgGroups = aog;
    }
    
    public String getName() {
        return this.orgType;
    }
    
    public void setName(String orgType) {
        this.setOrgType(orgType);
    }
    
    public static String hqlStringForName(String idSource)
    {
        return InternationalizedModelDescription.getForProperty(AmpOrgType.class, "orgType").getSQLFunctionCall(idSource + ".ampOrgTypeId");
    }

    
}
