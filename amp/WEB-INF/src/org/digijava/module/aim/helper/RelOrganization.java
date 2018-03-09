/*
 * RelOrganization.java
 */

package org.digijava.module.aim.helper;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.hibernate.Session;

public class RelOrganization implements Comparable{
    private String orgName;
    private String role;
    private AmpOrgType orgTypeId;
    private AmpOrgGroup orgGrpId;
    private String acronym;
    private String orgCode;
    private String budgetOrgCode;
    private Long orgId;
    private String additionalInformation;
    private Double percentage;

    
    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public RelOrganization() {}
    
    /**
     * @param orgName
     * @param role
     */
    public RelOrganization(String orgName, String role) {
        this.orgName = orgName;
        this.role = role;
    }
    /**
     * @return Returns the orgName.
     */
    public String getOrgName() {
        return orgName;
    }
    /**
     * @param orgName The orgName to set.
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
    /**
     * @return Returns the role.
     */
    public String getRole() {
        return role;
    }
    /**
     * @param role The role to set.
     */
    public void setRole(String role) {
        this.role = role;
    }
    
    public boolean equals(Object obj) {
        if (obj == null) throw new NullPointerException();
        if (!(obj instanceof RelOrganization)) throw new NullPointerException();
        
        RelOrganization relOrg = (RelOrganization) obj;
        return (relOrg.getOrgName().equals(orgName) && relOrg.getRole().equals(role));
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public AmpOrgGroup getOrgGrpId() {
        return orgGrpId;
    }

    public void setOrgGrpId(AmpOrgGroup orgGrpId) {
        this.orgGrpId = orgGrpId;
    }

    public AmpOrgType getOrgTypeId() {
        return orgTypeId;
    }

    public void setOrgTypeId(AmpOrgType orgTypeId) {
        this.orgTypeId = orgTypeId;
    }
    
    public AmpOrganisation getAmpOrganisation() {
        Session session;
        if ( this.orgId== null )
            return null;
        try{
            session                 = PersistenceManager.getRequestDBSession();
            AmpOrganisation ampOrg  = (AmpOrganisation)session.load(AmpOrganisation.class, this.orgId);
            
            if ( ampOrg!=null )
                return ampOrg;
        }
        catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
        return null;
    }

    public String getBudgetOrgCode() {
        return budgetOrgCode;
    }

    public void setBudgetOrgCode(String budgetOrgCode) {
        this.budgetOrgCode = budgetOrgCode;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }
    
    
    public int compareTo(Object obj) {
        
        if (!(obj instanceof RelOrganization)) 
            throw new ClassCastException();
        
        RelOrganization org = (RelOrganization) obj;
        if (this.orgName != null) {
            if (org.orgName != null) {
                return (this.orgName.trim().toLowerCase().
                        compareTo(org.orgName.trim().toLowerCase()));
            } else {
                return (this.orgName.trim().toLowerCase().
                        compareTo(""));
            }
        } else {
            if (org.orgName != null) {
                return ("".compareTo(org.orgName.trim().toLowerCase()));
            } else {
                return 0;
            }           
        }
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

}
