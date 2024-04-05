/**
 * Copyright (c) 2011 Development Gateway (www.developmentgateway.org)
 */
package org.digijava.module.aim.dbentity;

import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.util.Output;

import java.util.Date;
import java.util.Set;

/**
 * @author aartimon@dginternational.org
 * @since Apr 27, 2011
 */
@TranslatableClass (displayName = "Activity Version")
public class AmpActivityVersion extends AmpActivityFields implements Versionable{
    
    /**
     * 
     * NOTE:
     *    All new fields should be added in {@link AmpActivityFields}
     *    
     */
    protected String rejectMessage;
    private Set<AmpActivityGroup> groups;

    public Set<AmpActivityGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<AmpActivityGroup> groups) {
        this.groups = groups;
    }


    public AmpActivityVersion() {
    }

    public AmpActivityVersion(Long ampActivityId, String name, Date updatedDate, AmpTeamMember modifiedBy, String ampid) {
        this.ampActivityId=ampActivityId;
        this.name=name;
        //this.budget=budget;
        this.updatedDate=updatedDate;
        this.modifiedBy = modifiedBy;
        this.ampId=ampid;
    }

    public AmpActivityVersion(Long ampActivityId, String name, String ampid) {
        this.ampActivityId=ampActivityId;
        this.name=name;
        this.ampId=ampid;
    }

    /* Note, archived should be Boolean to support null values */
    public AmpActivityVersion(Long ampActivityId, String name, String ampid, Boolean archived) {
        this.ampActivityId=ampActivityId;
        this.name=name;
        this.ampId=ampid;
        this.archived = archived;
    }

    @Override
    public boolean equalsForVersioning(Object obj) {
        throw new AssertionError("Not implemented");
    }

    @Override
    public Object getValue() {
        throw new AssertionError("Not implemented");
    }

    @Override
    public Output getOutput() {
        throw new AssertionError("Not implemented");
    }

    @Override
    public Object prepareMerge(AmpActivityVersion newActivity) throws Exception {
        throw new AssertionError("Not implemented");
    }
    
    public static String sqlStringForName(String idSource)
    {
        return InternationalizedModelDescription.getForProperty(AmpActivityVersion.class, "name").getSQLFunctionCall(idSource);
    }

    public static String hqlStringForName(String idSource)
    {
        return InternationalizedModelDescription.getForProperty(AmpActivityVersion.class, "name").getSQLFunctionCall(idSource + ".ampActivityId");
    }
    public String getRejectMessage() {
        return rejectMessage;
    }

    public void setRejectMessage(String rejectMessage) {
        this.rejectMessage = rejectMessage;
    }

    public void addLocation(AmpActivityLocation activityLocation) {
        activityLocation.setActivity(this);
        getLocations().add(activityLocation);
    }

    @Override
    public String toString() {
        return "AmpActivityVersion{" +
                "rejectMessage='" + rejectMessage + '\'' +
                ", approvalStatus='" + approvalStatus + '\'' +
                ", groups=" + groups +
                ", projectImpact='" + projectImpact + '\'' +
                ", activitySummary='" + activitySummary + '\'' +
                ", conditionality='" + conditionality + '\'' +
                ", projectManagement='" + projectManagement + '\'' +
                ", budget=" + budget +
                ", govAgreementNumber='" + govAgreementNumber + '\'' +
                ", budgetCodeProjectID='" + budgetCodeProjectID + '\'' +
                ", budgetsector=" + budgetsector +
                ", budgetorganization=" + budgetorganization +
                ", budgetdepartment=" + budgetdepartment +
                ", budgetprogram=" + budgetprogram +
                ", ampActivityId=" + ampActivityId +
                ", ampId='" + ampId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", projectComments='" + projectComments + '\'' +
                ", lessonsLearned='" + lessonsLearned + '\'' +
                ", objective='" + objective + '\'' +
                ", purpose='" + purpose + '\'' +
                ", results='" + results + '\'' +
                ", documentSpace='" + documentSpace + '\'' +
                ", iatiIdentifier='" + iatiIdentifier + '\'' +
                ", draft=" + draft +
                ", changeType='" + changeType + '\'' +
                ", lastImportedAt=" + lastImportedAt +
                ", lastImportedBy=" + lastImportedBy +
                ", equalOpportunity='" + equalOpportunity + '\'' +
                ", environment='" + environment + '\'' +
                ", minorities='" + minorities + '\'' +
                ", language='" + language + '\'' +
                ", originalCompDate=" + originalCompDate +
                ", contractingDate=" + contractingDate +
                ", disbursmentsDate=" + disbursmentsDate +
                ", sectors=" + sectors +
                ", contracts=" + contracts +
                ", locations=" + locations +
                ", orgrole=" + orgrole +
                ", internalIds=" + internalIds +
                ", funding=" + funding +
                ", progress=" + progress +
                ", documents=" + documents +
                ", issues=" + issues +
                ", regionalObservations=" + regionalObservations +
                ", lineMinistryObservations=" + lineMinistryObservations +
                ", programDescription='" + programDescription + '\'' +
                ", team=" + team +
                ", member=" + member +
                ", contactName='" + contactName + '\'' +
                ", referenceDocs=" + referenceDocs +
                ", contFirstName='" + contFirstName + '\'' +
                ", contLastName='" + contLastName + '\'' +
                ", email='" + email + '\'' +
                ", dnrCntTitle='" + dnrCntTitle + '\'' +
                ", dnrCntOrganization='" + dnrCntOrganization + '\'' +
                ", dnrCntPhoneNumber='" + dnrCntPhoneNumber + '\'' +
                ", dnrCntFaxNumber='" + dnrCntFaxNumber + '\'' +
                ", mofedCntFirstName='" + mofedCntFirstName + '\'' +
                ", mofedCntLastName='" + mofedCntLastName + '\'' +
                ", mofedCntEmail='" + mofedCntEmail + '\'' +
                ", mfdCntTitle='" + mfdCntTitle + '\'' +
                ", mfdCntOrganization='" + mfdCntOrganization + '\'' +
                ", mfdCntPhoneNumber='" + mfdCntPhoneNumber + '\'' +
                ", mfdCntFaxNumber='" + mfdCntFaxNumber + '\'' +
                ", prjCoFirstName='" + prjCoFirstName + '\'' +
                ", prjCoLastName='" + prjCoLastName + '\'' +
                ", prjCoEmail='" + prjCoEmail + '\'' +
                ", prjCoTitle='" + prjCoTitle + '\'' +
                ", prjCoOrganization='" + prjCoOrganization + '\'' +
                ", prjCoPhoneNumber='" + prjCoPhoneNumber + '\'' +
                ", prjCoFaxNumber='" + prjCoFaxNumber + '\'' +
                ", secMiCntFirstName='" + secMiCntFirstName + '\'' +
                ", secMiCntLastName='" + secMiCntLastName + '\'' +
                ", secMiCntEmail='" + secMiCntEmail + '\'' +
                ", secMiCntTitle='" + secMiCntTitle + '\'' +
                ", secMiCntOrganization='" + secMiCntOrganization + '\'' +
                ", secMiCntPhoneNumber='" + secMiCntPhoneNumber + '\'' +
                ", secMiCntFaxNumber='" + secMiCntFaxNumber + '\'' +
                ", activityContacts=" + activityContacts +
                ", statusReason='" + statusReason + '\'' +
                ", components=" + components +
                ", structures=" + structures +
                ", proposedStartDate=" + proposedStartDate +
                ", actualStartDate=" + actualStartDate +
                ", proposedApprovalDate=" + proposedApprovalDate +
                ", actualApprovalDate=" + actualApprovalDate +
                ", actualCompletionDate=" + actualCompletionDate +
                ", proposedCompletionDate=" + proposedCompletionDate +
                ", activityCreator=" + activityCreator +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", iatiLastUpdatedDate=" + iatiLastUpdatedDate +
                ", approvedBy=" + approvedBy +
                ", approvalDate=" + approvalDate +
                ", regionalFundings=" + regionalFundings +
                ", survey=" + survey +
                ", gpiSurvey=" + gpiSurvey +
                ", lineMinRank=" + lineMinRank +
                ", actRankColl=" + actRankColl +
                ", archived=" + archived +
                ", deleted=" + deleted +
                ", selectedEffectivenessIndicatorOptions=" + selectedEffectivenessIndicatorOptions +
                ", indicators=" + indicators +
                ", activityDocuments=" + activityDocuments +
                ", categories=" + categories +
                ", statusOtherInfo='" + statusOtherInfo + '\'' +
                ", projectCategoryOtherInfo='" + projectCategoryOtherInfo + '\'' +
                ", modalitiesOtherInfo='" + modalitiesOtherInfo + '\'' +
                ", indirectOnBudget=" + indirectOnBudget +
                ", FY='" + FY + '\'' +
                ", fiscalYears=" + fiscalYears +
                ", vote='" + vote + '\'' +
                ", subVote='" + subVote + '\'' +
                ", subProgram='" + subProgram + '\'' +
                ", projectCode='" + projectCode + '\'' +
                ", ministryCode='" + ministryCode + '\'' +
                ", crisNumber='" + crisNumber + '\'' +
                ", governmentApprovalProcedures=" + governmentApprovalProcedures +
                ", jointCriteria=" + jointCriteria +
                ", multiStakeholderPartnership=" + multiStakeholderPartnership +
                ", multiStakeholderPartners='" + multiStakeholderPartners + '\'' +
                ", humanitarianAid=" + humanitarianAid +
                ", actPrograms=" + actPrograms +
                ", actBudgetStructure=" + actBudgetStructure +
                ", createdAsDraft=" + createdAsDraft +
                ", ampActivityGroup=" + ampActivityGroup +
                ", modifiedDate=" + modifiedDate +
                ", modifiedBy=" + modifiedBy +
                ", mergedActivity=" + mergedActivity +
                ", mergeSource1=" + mergeSource1 +
                ", mergeSource2=" + mergeSource2 +
                ", fundingSourcesNumber=" + fundingSourcesNumber +
                ", proposedProjectLife=" + proposedProjectLife +
                ", activityType=" + activityType +
                ", annualProjectBudgets=" + annualProjectBudgets +
                ", implementedActionsAsList=" + implementedActionsAsList +
                ", cachedPermissionMap=" + cachedPermissionMap +
                ", permissionMapCached=" + permissionMapCached +
                '}';
    }
}