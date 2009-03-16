
package org.digijava.module.orgProfile.helper;

import java.io.Serializable;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.orgProfile.form.OrgProfileFilterForm;

/**
 *
 * @author medea
 */
public class FilterHelper implements Serializable{
    private Long orgId;
    private Long currId;
    private Long year;
    private int transactionType;
    private TeamMember teamMember;
    private Long orgGroupId;
    private Long fiscalCalendarId;

    public Long getFiscalCalendarId() {
        return fiscalCalendarId;
    }

    public void setFiscalCalendarId(Long fiscalCalendarId) {
        this.fiscalCalendarId = fiscalCalendarId;
    }

    public Long getOrgGroupId() {
        return orgGroupId;
    }

    public void setOrgGroupId(Long orgGroupId) {
        this.orgGroupId = orgGroupId;
    }

    public TeamMember getTeamMember() {
        return teamMember;
    }

    public void setTeamMember(TeamMember teamMember) {
        this.teamMember = teamMember;
    }

   

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }
    
    public FilterHelper(OrgProfileFilterForm form){
        this.orgId=form.getOrgId();
        this.currId=form.getCurrencyId();
        this.year=form.getYear();
        this.transactionType=form.getTransactionType();
        this.orgGroupId=form.getOrgGroupId();
        this.fiscalCalendarId=form.getFiscalCalendarId();
    }

     public FilterHelper(OrgProfileFilterForm form,TeamMember tm){
       this(form);
       this.teamMember=tm;
    }

    public Long getCurrId() {
        return currId;
    }

    public void setCurrId(Long currId) {
        this.currId = currId;
    }


    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }
    
    public AmpOrganisation getOrganization() {
        //view entire group...
        if (orgId == null || orgId == -1) {
            AmpOrganisation org=new AmpOrganisation();
            org.setName("All");
            return org;
        }
        //view particular organization...
        return DbUtil.getOrganisation(orgId);

    }

     public AmpOrgGroup getOrgGroup(){
         AmpOrgGroup orgGroup;
        if(orgGroupId!=null&&orgGroupId!=-1){
             orgGroup=DbUtil.getAmpOrgGroup(orgGroupId);
        }
        else{
             orgGroup=new AmpOrgGroup();
             orgGroup.setOrgGrpName("All");
        }
        return orgGroup;

    }
}
