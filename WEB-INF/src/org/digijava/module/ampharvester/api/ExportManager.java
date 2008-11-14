package org.digijava.module.ampharvester.api;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpRegionalFunding;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.ampharvester.jaxb10.ActivityType;
import org.digijava.module.ampharvester.jaxb10.CodeValueType;
import org.digijava.module.ampharvester.jaxb10.ComponentType;
import org.digijava.module.ampharvester.jaxb10.ContactType;
import org.digijava.module.ampharvester.jaxb10.FreeTextType;
import org.digijava.module.ampharvester.jaxb10.FundingDetailType;
import org.digijava.module.ampharvester.jaxb10.FundingType;
import org.digijava.module.ampharvester.jaxb10.IssueType;
import org.digijava.module.ampharvester.jaxb10.LocationType;
import org.digijava.module.ampharvester.jaxb10.OrganizationType;
import org.digijava.module.ampharvester.jaxb10.ProjectIdType;
import org.digijava.module.ampharvester.jaxb10.impl.ActivityTypeImpl;
import org.digijava.module.ampharvester.jaxb10.impl.CodeValueTypeImpl;
import org.digijava.module.ampharvester.jaxb10.impl.ComponentTypeImpl;
import org.digijava.module.ampharvester.jaxb10.impl.ContactTypeImpl;
import org.digijava.module.ampharvester.jaxb10.impl.FreeTextTypeImpl;
import org.digijava.module.ampharvester.jaxb10.impl.FundingDetailTypeImpl;
import org.digijava.module.ampharvester.jaxb10.impl.FundingTypeImpl;
import org.digijava.module.ampharvester.jaxb10.impl.IssueTypeImpl;
import org.digijava.module.ampharvester.jaxb10.impl.LocationTypeImpl;
import org.digijava.module.ampharvester.jaxb10.impl.OrganizationTypeImpl;
import org.digijava.module.ampharvester.jaxb10.impl.ProjectIdTypeImpl;
import org.digijava.module.ampharvester.util.DbUtil;
import org.digijava.module.ampharvester.util.XmlHelper;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.editor.dbentity.Editor;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class ExportManager {

  public static ActivityType getXmlActivity(AmpActivity ampActivity, Session session) throws DgException, HibernateException {
    /** @todo need correct implemaentation */
    Set languagesSet = SiteUtils.getTransLanguages(SiteUtils.getSite(XmlHelper.siteId));

    ActivityType retValue = new ActivityTypeImpl();

    // set AmpId
    retValue.setAmpId(ampActivity.getAmpId());

    // set Title
    FreeTextType fttTitle = new FreeTextTypeImpl();
    fttTitle.setValue(ampActivity.getName().trim());
    retValue.setTitle(fttTitle);

    // set create Date
    if (ampActivity.getCreatedDate() != null) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(ampActivity.getCreatedDate());
      retValue.setCreateDate(cal);
    }

    for (Iterator iter = ampActivity.getCategories().iterator(); iter.hasNext(); ) {
      AmpCategoryValue acv = (AmpCategoryValue)iter.next();
      if (acv.getAmpCategoryClass().getKeyName().equals(CategoryConstants.ACTIVITY_STATUS_KEY)) {
        // set status
        CodeValueType cvtStatus = new CodeValueTypeImpl();
        cvtStatus.setCode(Integer.toString(acv.getIndex()));
        cvtStatus.setValue(acv.getValue());
        retValue.setStatus(cvtStatus);

      } else if (acv.getAmpCategoryClass().getKeyName().equals(CategoryConstants.IMPLEMENTATION_LEVEL_KEY)) {
        // set implementation level code
        retValue.setImplementationLevel(acv.getIndex());
      }
    }

    //set ids
    for (Iterator iter = ampActivity.getInternalIds().iterator(); iter.hasNext(); ) {
      ProjectIdType projectId = new ProjectIdTypeImpl();
      AmpActivityInternalId aaii = (AmpActivityInternalId)iter.next();
      CodeValueType cvtOrganization = new CodeValueTypeImpl();

      if (aaii.getOrganisation() == null ){
        throw new DgException("Activity["+ampActivity.getAmpId()+"] unknow Organisation ");
      }
      cvtOrganization.setCode(aaii.getOrganisation().getOrgCode());
      cvtOrganization.setValue(aaii.getOrganisation().getName());

      projectId.setUniqID(aaii.getInternalId());
      projectId.setOrganization(cvtOrganization);

      retValue.getIds().add(projectId);
    }

    // set description
    for (Iterator iter = languagesSet.iterator(); iter.hasNext(); ) {
      Locale language = (Locale)iter.next();

      Editor translation = DbUtil.getEditor(XmlHelper.siteId, language.getCode(), ampActivity.getDescription(), session);

      if (translation != null) {
        FreeTextType ftt = new FreeTextTypeImpl();
        ftt.setLanguage(translation.getLanguage());
        ftt.setValue(translation.getBody());
        retValue.getDescription().add(ftt);
      }
    }

    // set Objective
    for (Iterator iter = languagesSet.iterator(); iter.hasNext(); ) {
      Locale language = (Locale)iter.next();
      Editor translation = DbUtil.getEditor(XmlHelper.siteId, language.getCode(), ampActivity.getObjective(), session);

      if (translation != null) {
        FreeTextType ftt = new FreeTextTypeImpl();
        ftt.setLanguage(translation.getLanguage());
        ftt.setValue(translation.getBody());
        retValue.getObjective().add(ftt);
      }
    }

    if (ampActivity.getLineMinRank() != null) {
      retValue.setLineMinistryRank(ampActivity.getLineMinRank().intValue());
    }

    if (ampActivity.getPlanMinRank() != null) {
      retValue.setMinistryPlanningRank(ampActivity.getPlanMinRank().intValue());
    }

    // set ApprovalDateCalendar
    if (ampActivity.getProposedApprovalDate() != null) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(ampActivity.getProposedApprovalDate());
      retValue.setProposedApprovalDate(cal);
    }

    // set actualApprovalDate
    if (ampActivity.getActivityApprovalDate() != null) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(ampActivity.getActivityApprovalDate());
      retValue.setActualApprovalDate(cal);
    }

    // set proposedStartDate
    if (ampActivity.getProposedStartDate() != null) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(ampActivity.getProposedStartDate());
      retValue.setProposedStartDate(cal);
    }

    // set actualStartDate
    if (ampActivity.getActualStartDate() != null) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(ampActivity.getActualStartDate());
      retValue.setActualStartDate(cal);
    }

    // set currentCompletionDate
    if (ampActivity.getActualCompletionDate() != null) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(ampActivity.getActualCompletionDate());
      retValue.setCurrentCompletionDate(cal);
    }

    // set proposedCompletionDate
    if (ampActivity.getProposedCompletionDate() != null) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(ampActivity.getProposedCompletionDate());
      retValue.setProposedCompletionDate(cal);
    }

    // set StatusReason
    if (ampActivity.getStatusReason() != null && ampActivity.getStatusReason().trim().length() > 0) {
      FreeTextType ftt = new FreeTextTypeImpl();
      ftt.setValue(ampActivity.getStatusReason().trim());
      retValue.setStatusReason(ftt);
    }

    // set ProgramDescription
    if (ampActivity.getProgramDescription() != null && ampActivity.getProgramDescription().trim().length() > 0) {
      FreeTextType ftt = new FreeTextTypeImpl();
      ftt.setValue(ampActivity.getProgramDescription().trim());
      retValue.setProgramDescription(ftt);
    }

    // set Contractor
    if (ampActivity.getContractors() != null && ampActivity.getContractors().trim().length() > 0) {
      FreeTextType ftt = new FreeTextTypeImpl();
      ftt.setValue(ampActivity.getContractors().trim());
      retValue.setContractor(ftt);
    }

    // set location
    for (Iterator iter = ampActivity.getLocations().iterator(); iter.hasNext(); ) {
      AmpLocation location = (AmpLocation)iter.next();

      if (location.getCountry() != null || location.getIso3Code() != null) {
        LocationType lt = new LocationTypeImpl();
        lt.setCountryName(location.getCountry());
        lt.setIso3(location.getIso3Code());
        lt.setRegion(location.getRegion());

        // set commitments, disbursements, expenditures
        for (Iterator iterRF = ampActivity.getRegionalFundings().iterator(); iterRF.hasNext(); ) {
          AmpRegionalFunding ampRegionalFunding = (AmpRegionalFunding)iterRF.next();
          AmpRegion ampRegion = ampRegionalFunding.getRegion();

          if (lt.getCountryName().equalsIgnoreCase(ampRegion.getCountry().getCountryName()) &&
              lt.getRegion().equalsIgnoreCase(ampRegion.getName())) {
            FundingDetailType fdt = new FundingDetailTypeImpl();
            if (ampRegionalFunding.getAdjustmentType() == null ){
              throw new DgException("Activity["+ampActivity.getAmpId()+"] unknow RegionalFunding.AdjustmentType ");
            }
            fdt.setAdjustmentType(ampRegionalFunding.getAdjustmentType());

            if (ampRegionalFunding.getTransactionDate() == null ){
              throw new DgException("Activity["+ampActivity.getAmpId()+"] unknow RegionalFunding.TransactionDatee ");
            }
            Calendar calFDT = Calendar.getInstance();
            calFDT.setTime(ampRegionalFunding.getTransactionDate());
            fdt.setDate(calFDT);
            if (ampRegionalFunding.getTransactionAmount() == null ){
              throw new DgException("Activity["+ampActivity.getAmpId()+"] unknow RegionalFunding.TransactionAmount ");
            }
            fdt.setAmount(ampRegionalFunding.getTransactionAmount().longValue());
            if (ampRegionalFunding.getCurrency() == null ){
              throw new DgException("Activity["+ampActivity.getAmpId()+"] unknow RegionalFunding.Currency ");
            }
            fdt.setCurrencyCode(ampRegionalFunding.getCurrency().getCurrencyCode());

            // copy from org.digijava.module.aim.action.EditActivity[915]
            if (ampRegionalFunding.getTransactionType() != null){
              if (ampRegionalFunding.getTransactionType() == Constants.COMMITMENT) { // commitments
                lt.getCommitment().add(fdt);
              } else if (ampRegionalFunding.getTransactionType() == Constants.DISBURSEMENT) { // disbursements
                lt.getDisbursement().add(fdt);
              } else if (ampRegionalFunding.getTransactionType() == Constants.EXPENDITURE) { // expenditures
                lt.getExpenditure().add(fdt);
              }
            } else {
              throw new DgException("Activity["+ampActivity.getAmpId()+"] unknow RegionalFunding.TransactionType ");
            }
          }
        }
        retValue.getLocation().add(lt);
      }
    }

    // set sector
    for (Iterator iter = ampActivity.getSectors().iterator(); iter.hasNext(); ) {
      AmpActivitySector ampSector = (AmpActivitySector)iter.next();
      ActivityType.SectorType sector = new ActivityTypeImpl.SectorTypeImpl();
      sector.setCode(ampSector.getSectorId().getSectorCode());
      sector.setValue(ampSector.getSectorId().getName());
      if (ampSector.getSectorPercentage() != null){
        sector.setPercent(ampSector.getSectorPercentage().intValue());
      } else {
         sector.setPercent(0);
      }
      retValue.getSector().add(sector);
    }
    if (retValue.getSector().size() <=0){
      throw new DgException("Activity["+ampActivity.getAmpId()+"] unknow Sector ");
    }

    // set program
    for (Iterator iter = ampActivity.getActivityPrograms().iterator(); iter.hasNext(); ) {
      AmpTheme ampTheme = (AmpTheme)iter.next();
      CodeValueType program = new CodeValueTypeImpl();
      program.setCode(ampTheme.getThemeCode());
      program.setValue(ampTheme.getName());

      retValue.getProgram().add(program);
    }

    // set Organization
    Map<String, OrganizationType> organizationMap = new HashMap();
    for (Iterator iter = ampActivity.getFunding().iterator(); iter.hasNext(); ) {
      AmpFunding ampFunding = (AmpFunding)iter.next();
      FundingType ft = new FundingTypeImpl();

      OrganizationType organization = organizationMap.get(ampFunding.getAmpDonorOrgId().getOrgCode());
      if (organization == null) {
        organization = new OrganizationTypeImpl();
        organization.setCode(ampFunding.getAmpDonorOrgId().getOrgCode());
        organizationMap.put(organization.getCode(), organization);

        FreeTextType ftt = new FreeTextTypeImpl();
        ftt.setValue(ampFunding.getAmpDonorOrgId().getName());
        organization.setTitle(ftt);

      }
      if (ampFunding.getFinancingId() == null){
        throw new DgException("Activity["+ampActivity.getAmpId()+"] unknow Organization["+organization.getCode()+"] FinancingId");
      }
      ft.setFinancingId(ampFunding.getFinancingId());

      if (ampFunding.getTypeOfAssistance() != null) {
        CodeValueType cvtAssist = new CodeValueTypeImpl();
        cvtAssist.setCode(Integer.toString(ampFunding.getTypeOfAssistance().getIndex()));
        cvtAssist.setValue(ampFunding.getTypeOfAssistance().getValue());
        ft.setAssistance(cvtAssist);
      } else {
        throw new DgException("Activity["+ampActivity.getAmpId()+"] unknow Organization["+organization.getCode()+"] Assistance");
      }

      if (ampFunding.getFinancingInstrument() != null) {
        CodeValueType cvtFinancingInstrument = new CodeValueTypeImpl();
//        cvtFinancingInstrument.setCode(ampFunding.getModalityId().getModalityCode());
        cvtFinancingInstrument.setCode(ampFunding.getFinancingInstrument().getIndex() + "");
        cvtFinancingInstrument.setValue(ampFunding.getFinancingInstrument().getValue());
        ft.setFinancingInstrument(cvtFinancingInstrument);
      }
      if (ampFunding.getConditions() != null) {
        FreeTextType fttCondition = new FreeTextTypeImpl();
        fttCondition.setValue(ampFunding.getConditions());
        ft.setCondition(fttCondition);
      }
      // set commitments, disbursements, expenditures
      for (Iterator iterFD = ampFunding.getFundingDetails().iterator(); iterFD.hasNext(); ) {
        AmpFundingDetail ampFundingDetail = (AmpFundingDetail)iterFD.next();

        FundingDetailType fdt = new FundingDetailTypeImpl();
        if (ampFundingDetail.getAdjustmentType() == null){
          throw new DgException("Activity["+ampActivity.getAmpId()+"] unknow Organization["+organization.getCode()+"] FundingDetail.AdjustmentType");
        }
        fdt.setAdjustmentType(ampFundingDetail.getAdjustmentType());

        if (ampFundingDetail.getTransactionDate() == null){
          throw new DgException("Activity["+ampActivity.getAmpId()+"] unknow Organization["+organization.getCode()+"] FundingDetail.TransactionDate");
        }
        Calendar calFDT = Calendar.getInstance();
        calFDT.setTime(ampFundingDetail.getTransactionDate());
        fdt.setDate(calFDT);
        if (ampFundingDetail.getTransactionAmount() == null){
          throw new DgException("Activity["+ampActivity.getAmpId()+"] unknow Organization["+organization.getCode()+"] FundingDetail.TransactionAmounte");
        }
        fdt.setAmount(ampFundingDetail.getTransactionAmount().longValue());
        if (ampFundingDetail.getAmpCurrencyId() == null){
          throw new DgException("Activity["+ampActivity.getAmpId()+"] unknow Organization["+organization.getCode()+"] FundingDetail.Currency");
        }
        fdt.setCurrencyCode(ampFundingDetail.getAmpCurrencyId().getCurrencyCode());

        // copy from org.digijava.module.aim.action.EditActivity[915]
        if (ampFundingDetail.getTransactionType() != null){
          if (ampFundingDetail.getTransactionType() == Constants.COMMITMENT) { // commitments
            ft.getCommitment().add(fdt);
          } else if (ampFundingDetail.getTransactionType() == Constants.DISBURSEMENT) { // disbursements
            ft.getDisbursement().add(fdt);
          } else if (ampFundingDetail.getTransactionType() == Constants.EXPENDITURE) { // expenditures
            ft.getExpenditure().add(fdt);
          }
        } else {
          throw new DgException("Activity["+ampActivity.getAmpId()+"] unknow Organization["+organization.getCode()+"] FundingDetail.TransactionType");
        }
      }
      organization.getFounding().add(ft);
    }
    for (OrganizationType elem : organizationMap.values()) {
      retValue.getOrganization().add(elem);
    }

    // set component
    for (Iterator iter = ampActivity.getComponents().iterator(); iter.hasNext(); ) {
      AmpComponent ampComponent = (AmpComponent)iter.next();
      ComponentType componentType = new ComponentTypeImpl();
      if (ampComponent.getCode() == null){
        throw new DgException("Activity["+ampActivity.getAmpId()+"] unknow Component["+ampComponent.getTitle()+"] code ");
      }
      componentType.setCode(ampComponent.getCode());

      FreeTextType ftt = new FreeTextTypeImpl();
      ftt.setValue(ampComponent.getTitle());
      componentType.setTitle(ftt);

      Collection componentFundingCol = ComponentsUtil.getComponentFunding(ampComponent.getAmpComponentId());
      if (componentFundingCol != null) {
        for (Iterator iterCF = componentFundingCol.iterator(); iterCF.hasNext(); ) {
          AmpComponentFunding ampComponentFunding = (AmpComponentFunding)iterCF.next();

          FundingDetailType fdt = new FundingDetailTypeImpl();
          if (ampComponentFunding.getAdjustmentType() == null){
            throw new DgException("Activity["+ampActivity.getAmpId()+"] unknow Component["+componentType.getCode()+"] ComponentFunding.AdjustmentType");
          }
          fdt.setAdjustmentType(ampComponentFunding.getAdjustmentType());

          Calendar calFDT = Calendar.getInstance();
          if (ampComponentFunding.getTransactionDate() == null){
            throw new DgException("Activity["+ampActivity.getAmpId()+"] unknow  Component["+componentType.getCode()+"] ComponentFunding.TransactionDate");
          }
          calFDT.setTime(ampComponentFunding.getTransactionDate());
          fdt.setDate(calFDT);
          if (ampComponentFunding.getTransactionAmount() == null){
            throw new DgException("Activity["+ampActivity.getAmpId()+"] unknow  Component["+componentType.getCode()+"] ComponentFunding.TransactionAmount");
          }
          fdt.setAmount(ampComponentFunding.getTransactionAmount().longValue());
          if (ampComponentFunding.getCurrency() == null){
            throw new DgException("Activity["+ampActivity.getAmpId()+"] unknow  Component["+componentType.getCode()+"] ComponentFunding.Currency");
          }
          fdt.setCurrencyCode(ampComponentFunding.getCurrency().getCurrencyCode());

          // copy from org.digijava.module.aim.action.EditActivity[915]
          if (ampComponentFunding.getTransactionType() != null){
            if (ampComponentFunding.getTransactionType() == Constants.COMMITMENT) { // commitments
              componentType.getCommitment().add(fdt);
            } else if (ampComponentFunding.getTransactionType() == Constants.DISBURSEMENT) { // disbursements
              componentType.getDisbursement().add(fdt);
            } else if (ampComponentFunding.getTransactionType() == Constants.EXPENDITURE) { // expenditures
              componentType.getExpenditure().add(fdt);
            }
          } else {
            throw new DgException("Activity["+ampActivity.getAmpId()+"] unknow  Component["+componentType.getCode()+"] ComponentFunding.TransactionType");
          }
        }
      }
      retValue.getComponent().add(componentType);
    }

    // set issue
    for (Iterator iter = ampActivity.getIssues().iterator(); iter.hasNext(); ) {
      AmpIssues ampIssues = (AmpIssues)iter.next();
      IssueType issue = new IssueTypeImpl();
      if (ampIssues.getName() == null){
        throw new DgException("Activity["+ampActivity.getAmpId()+"] unknow  Issues.Name");
      }
      issue.setTitle(ampIssues.getName());

      for (Iterator iterMeasures = ampIssues.getMeasures().iterator(); iterMeasures.hasNext(); ) {
        AmpMeasure ampMeasure = (AmpMeasure)iterMeasures.next();
        IssueType.MeasureType measure = new IssueTypeImpl.MeasureTypeImpl();
        if (ampMeasure.getName() == null){
          throw new DgException("Activity["+ampActivity.getAmpId()+"] unknow  Measure.Name");
        }
        measure.setTitle(ampMeasure.getName());

        for (Iterator iterActor = ampMeasure.getActors().iterator(); iterActor.hasNext(); ) {
          AmpActor ampActor = (AmpActor)iterActor.next();
          measure.getActor().add(ampActor.getName());
        }
        issue.getMeasure().add(measure);
      }

      retValue.getIssue().add(issue);
    }

    /** @todo add releated links  */
//    ampActivity.getTeam().getTeamLead().getAmpTeamMemId()

    // set Agency
    for (Iterator iter = ampActivity.getOrgrole().iterator(); iter.hasNext(); ) {
      AmpOrgRole ampOrgRole = (AmpOrgRole)iter.next();
      CodeValueType cvt = new CodeValueTypeImpl();
      cvt.setCode(ampOrgRole.getOrganisation().getOrgCode());
      cvt.setValue(ampOrgRole.getOrganisation().getName());

      if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.EXECUTING_AGENCY)) { // Executing Agency
        retValue.getExecutingAgency().add(cvt);
      } else if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.IMPLEMENTING_AGENCY)) { // Implementing Agency
        retValue.getImplementingAgency().add(cvt);
      }
    }

    // set Donot contact
    ContactType ctDonor = new ContactTypeImpl();
    ctDonor.setFirstName(ampActivity.getContFirstName());
    ctDonor.setLastName(ampActivity.getContLastName());
    ctDonor.setEmail(ampActivity.getEmail());
//    ctDonor.setTitle(ampActivity.getDnrCntTitle());
//    ctDonor.setOrganization(ampActivity.getDnrCntOrganization());
//    ctDonor.setPhoneNumber(ampActivity.getDnrCntPhoneNumber());
//    ctDonor.setFaxNumber(ampActivity.getDnrCntFaxNumber());
    retValue.setDonorFundingContact(ctDonor);

    // set Mofed contact
    ContactType ctMofed = new ContactTypeImpl();
    ctMofed.setFirstName(ampActivity.getMofedCntFirstName());
    ctMofed.setLastName(ampActivity.getMofedCntLastName());
    ctMofed.setEmail(ampActivity.getMofedCntEmail());
//    ctMofed.setTitle(ampActivity.getMfdCntTitle());
//    ctMofed.setOrganization(ampActivity.getMfdCntOrganization());
//    ctMofed.setPhoneNumber(ampActivity.getMfdCntPhoneNumber());
//    ctMofed.setFaxNumber(ampActivity.getMfdCntFaxNumber());
    retValue.setMofedContact(ctMofed);

    return retValue;
  }

}
