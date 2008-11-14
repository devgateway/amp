package org.digijava.module.ampharvester.api;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityInternalId;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActor;
import org.digijava.module.aim.dbentity.AmpCategoryClass;
import org.digijava.module.aim.dbentity.AmpCategoryValue;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.AmpComponentFunding;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpIssues;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpRole;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.ampharvester.exception.AmpHarvesterException;
import org.digijava.module.ampharvester.jaxb10.Activities;
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
import org.digijava.module.ampharvester.util.DbUtil;
import org.digijava.module.ampharvester.util.XmlHelper;
import org.digijava.module.ampharvester.util.XmlTransformerHelper;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.editor.dbentity.Editor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ImportManager {

  private static Logger log = Logger.getLogger(ImportManager.class);

  private Activities xmlActivities = null;

  public ImportManager(byte[] data) throws AmpHarvesterException {
    xmlActivities = (Activities)XmlTransformerHelper.geterateJAXB(data);
  }

  public StringBuffer startImportHttp(String ampTeamId, AmpTeam ampTeam) throws DgException {
    Set<String> errorSet = new HashSet();
    Transaction tx = null;
    Session session = null;
    try {

      for (Object elem : xmlActivities.getActivity()) {
        session = PersistenceManager.getSession();

        ActivityType activityType = (ActivityType)elem;
        String ampId = activityType.getAmpId();
        try {
          tx = session.beginTransaction();
          AmpActivity ampActivity = DbUtil.getAmpActivityById(ampId, session);
          if (ampActivity == null) {
            ampActivity = getAmpActivityNew(activityType, ampTeamId, session, ampTeam);
            ampActivity.setAmpId(ampId);
          } else {
            editAmpActivity(ampActivity, activityType, ampTeamId, session, ampTeam);
            session.update(ampActivity);
          }
          tx.commit();
        } catch (AmpHarvesterException ex2) {
          tx.rollback();
          session.clear();
          session.flush();
          errorSet.add("ActivityID[" + ampId + "] " + ex2.getErrorMessage());
          log.info(ex2);
        } catch (Exception ex2) {
          tx.rollback();
          throw ex2;
        } finally {
          try {
            PersistenceManager.releaseSession(session);
          } catch (Exception ex1) {
            throw new DgException(ex1);
          }
        }
      }
    } catch (Exception ex) {
      throw new DgException(ex);

    }
    StringBuffer retValue = new StringBuffer();
    for (String elem : errorSet) {
      retValue.append(elem);
    }

    return retValue.length() > 0 ? retValue : null;
  }

  private AmpActivity getAmpActivityNew(ActivityType xmlActivity, String ampTeamId, Session session, AmpTeam ampTeam) throws AmpHarvesterException, DgException,
      HibernateException {
    AmpActivity retValue = initializeActivity(null, xmlActivity, ampTeam);

    session.save(retValue);

    // set status
    if (xmlActivity.getStatus() != null) {
      createStatus(xmlActivity.getStatus(), session, retValue);
    }

    // set implementation level
    if (xmlActivity.getImplementationLevel() >=0) {
      createImplementationLevel(retValue,xmlActivity,session);
    }

    // set ids
    for (Object elem : xmlActivity.getIds()) {
      ProjectIdType projectId = (ProjectIdType)elem;
      AmpActivityInternalId aaii = new AmpActivityInternalId();
      AmpOrganisation ampOrganisation = DbUtil.getAmpOrganisationByCode(projectId.getOrganization().getCode(), session);
      if (ampOrganisation == null) {
        throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_ORGANIZATION, projectId.getOrganization().getCode());
      }
      aaii.setOrganisation(ampOrganisation);
      aaii.setInternalId(projectId.getUniqID());
      retValue.getInternalIds().add(aaii);
    }

    // set description
    //"aim-desc-" + teamMember.getMemberId() + "-" + System.currentTimeMillis()
    String transKeyDescription = "aim-desc-" + ampTeamId + "-" + System.nanoTime();
    for (Object elem : xmlActivity.getDescription()) {
      FreeTextType ftt = (FreeTextType)elem;
      createEditorTrn(ampTeam, transKeyDescription, ftt.getLanguage(), ftt.getValue(), session);
    }
    retValue.setDescription(transKeyDescription);

    // set objectives
    //"aim-obj-" + teamMember.getMemberId() + "-" + System.currentTimeMillis()
    String transKeyObjective = "aim-obj-" + ampTeamId + "-" + System.nanoTime();
    for (Object elem : xmlActivity.getObjective()) {
      FreeTextType ftt = (FreeTextType)elem;
      createEditorTrn(ampTeam, transKeyObjective, ftt.getLanguage(), ftt.getValue(), session);
    }
    retValue.setObjective(transKeyObjective);

//    List<AmpLocation> locations = DbUtil.getAmpLocation(session);

    for (Object elem : xmlActivity.getLocation()) {
      LocationType location = (LocationType)elem;
      AmpLocation ampLocation = DbUtil.getAmpLocationByName(location.getCountryName().trim(),
          location.getRegion().trim(), session);
      if (ampLocation != null) {
        retValue.getLocations().add(ampLocation);
        retValue.getRegionalFundings().addAll(
            XmlHelper.getAmpRegionalFunding(location, retValue, session));
      } else {
        throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_LOCATION, location.getCountryName());
      }

    }

    //set sector
    for (Object elem : xmlActivity.getSector()) {
      ActivityType.SectorType cvt = (ActivityType.SectorType)elem;
      AmpSector ampSector = DbUtil.getAmpSector(cvt.getCode(), session);
      if (ampSector == null) {
        throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_SECTOR, cvt.getCode());
      }
      AmpActivitySector aas = new AmpActivitySector();
      aas.setActivityId(retValue);
      aas.setSectorId(ampSector);
      if (cvt.getPercent() >= 0) {
        aas.setSectorPercentage(Float.valueOf(cvt.getPercent()));
      }
      retValue.getSectors().add(aas);
    }

    //set program
    for (Object elem : xmlActivity.getProgram()) {
      CodeValueType cvt = (CodeValueType)elem;
      AmpTheme ampTheme = DbUtil.getAmpTheme(cvt.getCode(), session);
      if (ampTheme == null) {
        throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_PROGRAM, cvt.getCode());
      }
      retValue.getActivityPrograms().add(ampTheme);
    }

    //set Organization
    for (Object elem : xmlActivity.getOrganization()) {
      OrganizationType ot = (OrganizationType)elem;
      AmpOrganisation ampOrganisation = DbUtil.getAmpOrganisationByCode(ot.getCode(), session);
      if (ampOrganisation == null) {
        throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_ORGANIZATION, ot.getCode());
      }
      for (Object elemF : ot.getFounding()) {
        FundingType ft = (FundingType)elemF;
        AmpFunding ampFunding = new AmpFunding();
        ampFunding.setFundingDetails(new HashSet());

        ampFunding.setAmpDonorOrgId(ampOrganisation);
        ampFunding.setFinancingId(ft.getFinancingId());
//        AmpTermsAssist ata = DbUtil.getAmpTermsAssist(ft.getAssistance().getCode(), session);
        AmpCategoryValue acv = DbUtil.getAmpCategoryValue(CategoryConstants.TYPE_OF_ASSISTENCE_KEY, Integer.parseInt(ft.getAssistance().getCode()), session);
        if (acv == null) {
          throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_ASSIST, ft.getAssistance().getCode());
        }
//        ampFunding.setAmpTermsAssistId(ata);
        ampFunding.setTypeOfAssistance(acv);


        createFundingDetails(session, ft, ampFunding);

        ampFunding.setAmpActivityId(retValue);
        retValue.getFunding().add(ampFunding);
      }

    }

    for (Object elem : xmlActivity.getComponent()) {
      ComponentType ct = (ComponentType)elem;
      AmpComponent ampComponent = DbUtil.getAmpComponent(ct.getCode(), session);
      if (ampComponent == null) {
        throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_COMPONENT, ct.getCode());
      }
      createComponent(retValue, ampComponent, session, ct);
    }

    // set issue
    for (Object elemIssue : xmlActivity.getIssue()) {
      IssueType issue = (IssueType)elemIssue;
      AmpIssues ampIssues = new AmpIssues();
      ampIssues.setMeasures(new HashSet());
      ampIssues.setName(issue.getTitle());
      for (Object elemMeasure : issue.getMeasure()) {
        IssueType.MeasureType measure = (IssueType.MeasureType)elemMeasure;
        AmpMeasure ampMeasure = new AmpMeasure();
        ampMeasure.setActors(new HashSet());
        ampMeasure.setName(measure.getTitle());

        for (Object elemActor : measure.getActor()) {
          AmpActor ampActor = new AmpActor();
          ampActor.setName((String)elemActor);
          ampMeasure.getActors().add(ampActor);
          ampActor.setMeasure(ampMeasure);
        }
        ampIssues.getMeasures().add(ampMeasure);
        ampMeasure.setIssue(ampIssues);
      }
      ampIssues.setActivity(retValue);
      retValue.getIssues().add(ampIssues);
    }

    // set executing Agency
    AmpRole ampRoleEA = DbUtil.getAmpRole(Constants.EXECUTING_AGENCY, session);
    for (Object elem : xmlActivity.getExecutingAgency()) {
      CodeValueType cvt = (CodeValueType)elem;
      AmpOrgRole ampOrgRole = new AmpOrgRole();
      ampOrgRole.setRole(ampRoleEA);
      AmpOrganisation ampOrganisation = DbUtil.getAmpOrganisationByCode(cvt.getCode(), session);
      if (ampOrganisation == null) {
        throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_ORGANIZATION, cvt.getCode());
      }
      ampOrgRole.setOrganisation(ampOrganisation);
      retValue.getOrgrole().add(ampOrgRole);
    }

    // set implementing Agency
    AmpRole ampRoleIA = DbUtil.getAmpRole(Constants.IMPLEMENTING_AGENCY, session);
    for (Object elem : xmlActivity.getImplementingAgency()) {
      CodeValueType cvt = (CodeValueType)elem;
      AmpOrgRole ampOrgRole = new AmpOrgRole();
      ampOrgRole.setRole(ampRoleIA);
      AmpOrganisation ampOrganisation = DbUtil.getAmpOrganisationByCode(cvt.getCode(), session);
      if (ampOrganisation == null) {
        throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_ORGANIZATION, cvt.getCode());
      }
      ampOrgRole.setOrganisation(ampOrganisation);
      retValue.getOrgrole().add(ampOrgRole);
    }

    return retValue;
  }


  private void editAmpActivity(AmpActivity ampActivity, ActivityType xmlActivity, String ampTeamId, Session session, AmpTeam ampTeam) throws DgException,
      AmpHarvesterException, HibernateException {

    ampActivity = initializeActivity(ampActivity, xmlActivity, ampTeam);


    // set create date
    if (xmlActivity.getCreateDate() != null) {
      ampActivity.setCreatedDate(xmlActivity.getCreateDate().getTime());
    }

//    session.save(ampActivity);

    // set status
    if (xmlActivity.getStatus() != null) {
      createStatus(xmlActivity.getStatus(),session, ampActivity);
    }

    // set implementation level
    if (xmlActivity.getImplementationLevel() >=0) {
      createImplementationLevel(ampActivity, xmlActivity, session);
    }

    // set ids
    for (Object elem : xmlActivity.getIds()) {
      ProjectIdType projectId = (ProjectIdType)elem;
      AmpActivityInternalId aaii = new AmpActivityInternalId();
      boolean notContain = true;

      for (Object elemId : ampActivity.getInternalIds()) {
        AmpActivityInternalId aaiId = (AmpActivityInternalId)elemId;
        if (aaiId.getOrganisation().getOrgCode().equalsIgnoreCase(projectId.getOrganization().getCode().trim())) {
          notContain = false;
          break;
        }
      }

      if (notContain) {
        AmpOrganisation ampOrganisation = DbUtil.getAmpOrganisationByCode(projectId.getOrganization().getCode(), session);
        if (ampOrganisation == null) {
          throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_ORGANIZATION, projectId.getOrganization().getCode());
        }
        aaii.setOrganisation(ampOrganisation);
        aaii.setInternalId(projectId.getUniqID());
        ampActivity.getInternalIds().add(aaii);
      }
    }

    // set description
    //"aim-desc-" + teamMember.getMemberId() + "-" + System.currentTimeMillis()
    for (Object elem : xmlActivity.getDescription()) {
      FreeTextType ftt = (FreeTextType)elem;

      String transKey = ampActivity.getDescription();
      if (transKey == null) {
        transKey = "aim-desc-" + ampTeamId + "-" + System.nanoTime();
        ampActivity.setDescription(transKey);
      }
      createEditorTrn(ampTeam, transKey, ftt.getLanguage(), ftt.getValue(), session);
    }

    // set objectives
    //"aim-obj-" + teamMember.getMemberId() + "-" + System.currentTimeMillis()
    for (Object elem : xmlActivity.getObjective()) {
      FreeTextType ftt = (FreeTextType)elem;

      String transKey = ampActivity.getObjective();
      if (transKey == null) {
        transKey = "aim-obj-" + ampTeamId + "-" + System.nanoTime();
        ampActivity.setDescription(transKey);
      }
      createEditorTrn(ampTeam, transKey, ftt.getLanguage(), ftt.getValue(), session);
    }

//    List<AmpLocation> locations = DbUtil.getAmpLocation(session);

    for (Object elem : xmlActivity.getLocation()) {
      LocationType location = (LocationType)elem;
      AmpLocation ampLocation = DbUtil.getAmpLocationByName(location.getCountryName().trim(),
          location.getRegion().trim(), session);
      if (ampLocation != null) {
        ampActivity.getLocations().add(ampLocation);
        DbUtil.deleteRegionalFundings(ampLocation, ampActivity.getRegionalFundings(), session); // delete all existing RegionalFundings
        ampActivity.getRegionalFundings().addAll(
            XmlHelper.getAmpRegionalFunding(location, ampActivity, session));
      } else {
        throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_LOCATION, location.getCountryName());
      }

    }

    //set sector
    for (Object elem : xmlActivity.getSector()) {
      ActivityType.SectorType cvt = (ActivityType.SectorType)elem;
      boolean notContain = true;
      AmpActivitySector ampActivitySector = new AmpActivitySector();

      for (Object elemSect : ampActivity.getSectors()) {
        AmpActivitySector aSector = (AmpActivitySector)elemSect;
        if (aSector.getSectorId().getSectorCode().trim().equalsIgnoreCase(cvt.getCode().trim())) {
          ampActivitySector = aSector;
          notContain = false;
          break;
        }
      }

      if (notContain) {
        AmpSector ampSector = DbUtil.getAmpSector(cvt.getCode(), session);
        if (ampSector == null) {
          throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_SECTOR, cvt.getCode());
        }
        ampActivitySector.setActivityId(ampActivity);
        ampActivitySector.setSectorId(ampSector);
        ampActivity.getSectors().add(ampActivitySector);
      }
      if (cvt.getPercent() >= 0) {
        ampActivitySector.setSectorPercentage(Float.valueOf(cvt.getPercent()));
      }
    }

    //set program
    for (Object elem : xmlActivity.getProgram()) {
      CodeValueType cvt = (CodeValueType)elem;
      boolean notContain = true;

      for (Object elemSect : ampActivity.getActivityPrograms()) {
        AmpTheme ampTheme = (AmpTheme)elemSect;
        if (ampTheme.getThemeCode().trim().equalsIgnoreCase(cvt.getCode().trim())) {
          notContain = false;
          break;
        }
      }

      if (notContain) {
        AmpTheme ampTheme = DbUtil.getAmpTheme(cvt.getCode(), session);
        if (ampTheme == null) {
          throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_PROGRAM, cvt.getCode());
        }
        ampActivity.getActivityPrograms().add(ampTheme);
      }
    }

    //set Organization
    for (Object elem : xmlActivity.getOrganization()) {
      OrganizationType ot = (OrganizationType)elem;
      AmpOrganisation ampOrganisation = DbUtil.getAmpOrganisationByCode(ot.getCode(), session);
      if (ampOrganisation == null) {
        throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_ORGANIZATION, ot.getCode());
      }
      for (Object elemF : ot.getFounding()) {
        FundingType ft = (FundingType)elemF;
        AmpCategoryValue acv = DbUtil.getAmpCategoryValue(CategoryConstants.TYPE_OF_ASSISTENCE_KEY, Integer.parseInt(ft.getAssistance().getCode()), session);
//        AmpTermsAssist ata = DbUtil.getAmpTermsAssist(ft.getAssistance().getCode(), session);
        if (acv == null) {
          throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_ASSIST, ft.getAssistance().getCode());
        }

        AmpFunding ampFunding = containsFunding(ampOrganisation, acv, ft.getFinancingId(), ampActivity.getFunding());

        createFundingDetails(session, ft, ampFunding);

        ampFunding.setAmpActivityId(ampActivity);
        ampActivity.getFunding().add(ampFunding);
      }

    }

    for (Object elem : xmlActivity.getComponent()) {
      ComponentType ct = (ComponentType)elem;
      AmpComponent ampComponent = DbUtil.getAmpComponent(ct.getCode(), session);
      if (ampComponent == null) {
        throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_COMPONENT, ct.getCode());
      }
      DbUtil.deleteComponentFunding(ampComponent.getAmpComponentId(), ampActivity.getAmpActivityId(), session);
      createComponent(ampActivity, ampComponent, session, ct);
    }

    // set issue
    for (Object elemIssue : xmlActivity.getIssue()) {
      IssueType issue = (IssueType)elemIssue;
      AmpIssues ampIssues = containsIssue(issue.getTitle(), ampActivity.getIssues());
      ampIssues.setActivity(ampActivity);
      ampActivity.getIssues().add(ampIssues);

      for (Object elemMeasure : issue.getMeasure()) {
        IssueType.MeasureType measure = (IssueType.MeasureType)elemMeasure;
        AmpMeasure ampMeasure = containsMeasure(measure.getTitle(), ampIssues.getMeasures());
        ampIssues.getMeasures().add(ampMeasure);
        ampMeasure.setIssue(ampIssues);

        for (Object elemActor : measure.getActor()) {
          AmpActor ampActor = containsActor((String)elemActor, ampMeasure.getActors());
          ampMeasure.getActors().add(ampActor);
          ampActor.setMeasure(ampMeasure);
        }
      }

    }

    // set executing Agency
    AmpRole ampRoleEA = DbUtil.getAmpRole(Constants.EXECUTING_AGENCY, session);
    for (Object elem : xmlActivity.getExecutingAgency()) {
      CodeValueType cvt = (CodeValueType)elem;
      boolean notContain = true;

      for (Object elemSect : ampActivity.getOrgrole()) {
        AmpOrgRole aor = (AmpOrgRole)elemSect;
        if (aor.getOrganisation().getOrgCode().trim().equalsIgnoreCase(cvt.getCode().trim()) &&
            aor.getRole().getRoleCode().equalsIgnoreCase(Constants.EXECUTING_AGENCY)) {
          notContain = false;
          break;
        }
      }

      if (notContain) {
        AmpOrgRole ampOrgRole = new AmpOrgRole();
        ampOrgRole.setRole(ampRoleEA);
        AmpOrganisation ampOrganisation = DbUtil.getAmpOrganisationByCode(cvt.getCode(), session);
        if (ampOrganisation == null) {
          throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_ORGANIZATION, cvt.getCode());
        }
        ampOrgRole.setOrganisation(ampOrganisation);
        ampActivity.getOrgrole().add(ampOrgRole);
      }
    }

    // set implementing Agency
    AmpRole ampRoleIA = DbUtil.getAmpRole(Constants.IMPLEMENTING_AGENCY, session);
    for (Object elem : xmlActivity.getImplementingAgency()) {
      CodeValueType cvt = (CodeValueType)elem;

      boolean notContain = true;

      for (Object elemSect : ampActivity.getOrgrole()) {
        AmpOrgRole aor = (AmpOrgRole)elemSect;
        if (aor.getOrganisation().getOrgCode().trim().equalsIgnoreCase(cvt.getCode().trim()) &&
            aor.getRole().getRoleCode().equalsIgnoreCase(Constants.IMPLEMENTING_AGENCY)) {
          notContain = false;
          break;
        }
      }

      if (notContain) {
        AmpOrgRole ampOrgRole = new AmpOrgRole();
        ampOrgRole.setRole(ampRoleIA);
        AmpOrganisation ampOrganisation = DbUtil.getAmpOrganisationByCode(cvt.getCode(), session);
        if (ampOrganisation == null) {
          throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_ORGANIZATION, cvt.getCode());
        }
        ampOrgRole.setOrganisation(ampOrganisation);
        ampActivity.getOrgrole().add(ampOrgRole);
      }
    }

  }

  private void createImplementationLevel(AmpActivity ampActivity, ActivityType xmlActivity, Session session) throws HibernateException, DgException {
    AmpCategoryClass acc = DbUtil.getAmpCategoryClassByKey(CategoryConstants.IMPLEMENTATION_LEVEL_KEY, session);
    if (acc != null) {
      boolean found = false;
      for (Object elem : acc.getPossibleValues()) {
        AmpCategoryValue acv = (AmpCategoryValue)elem;
        if (acv.getIndex() == xmlActivity.getImplementationLevel()) {
          found = true;
          ampActivity.getCategories().add(acv);
          break;
        }
      }
      if (!found){
        throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_IMPLEMENTATION_LEVEL, Integer.toString(xmlActivity.getImplementationLevel()));
      }
    } else {
      throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_KEY, CategoryConstants.IMPLEMENTATION_LEVEL_KEY);
    }
  }

  private void createStatus(CodeValueType cvt, Session session, AmpActivity retValue) throws AmpHarvesterException, HibernateException, DgException {
    AmpCategoryClass acc = DbUtil.getAmpCategoryClassByKey(CategoryConstants.ACTIVITY_STATUS_KEY, session);
    if (acc != null) {
      boolean found = false;

      for (Object elem : acc.getPossibleValues()) {
        AmpCategoryValue acv = (AmpCategoryValue)elem;
        if (acv.getIndex() == Integer.parseInt(cvt.getCode())) {
          retValue.getCategories().add(acv);
          found = true;
          break;
        }
      }

      if (!found){
        throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_STATUS, cvt.getCode());
      }
    } else {
      throw new AmpHarvesterException(AmpHarvesterException.UNKNOWN_STATUS, CategoryConstants.ACTIVITY_STATUS_KEY);
    }
  }

  private void createFundingDetails(Session session, FundingType ft, AmpFunding ampFunding) throws AmpHarvesterException, DgException, HibernateException {
    if (ft.getCondition() != null){
      ampFunding.setConditions(ft.getCondition().getValue());
    }
    if (ft.getCommitment() != null) {
      for (Object elemDetail : ft.getCommitment()) {
        ampFunding.getFundingDetails().add(XmlHelper.getAmpFundingDetail((FundingDetailType)elemDetail, new Integer(Constants.COMMITMENT), ampFunding, session));
      }
    }
    if (ft.getDisbursement() != null) {
      for (Object elemDetail : ft.getDisbursement()) {
        ampFunding.getFundingDetails().add(XmlHelper.getAmpFundingDetail((FundingDetailType)elemDetail, new Integer(Constants.DISBURSEMENT), ampFunding, session));
      }
    }
    if (ft.getExpenditure() != null) {
      for (Object elemDetail : ft.getExpenditure()) {
        ampFunding.getFundingDetails().add(XmlHelper.getAmpFundingDetail((FundingDetailType)elemDetail, new Integer(Constants.EXPENDITURE), ampFunding, session));
      }
    }
  }

  private void createComponent(AmpActivity ampActivity, AmpComponent ampComponent, Session session, ComponentType ct) throws AmpHarvesterException,
      HibernateException, DgException {
    ampActivity.getComponents().add(ampComponent);

    if (ct.getCommitment() != null) {
      for (Object elemDetail : ct.getCommitment()) {
        AmpComponentFunding acf = XmlHelper.getAmpComponentFunding((FundingDetailType)elemDetail, new Integer(Constants.COMMITMENT), session);
        acf.setComponent(ampComponent);
        session.save(acf);
        acf.setActivity(ampActivity);
      }
    }
    if (ct.getDisbursement() != null) {
      for (Object elemDetail : ct.getDisbursement()) {
        AmpComponentFunding acf = XmlHelper.getAmpComponentFunding((FundingDetailType)elemDetail, new Integer(Constants.DISBURSEMENT), session);
        acf.setComponent(ampComponent);
        session.save(acf);
        acf.setActivity(ampActivity);
      }
    }
    if (ct.getExpenditure() != null) {
      for (Object elemDetail : ct.getExpenditure()) {
        AmpComponentFunding acf = XmlHelper.getAmpComponentFunding((FundingDetailType)elemDetail, new Integer(Constants.EXPENDITURE), session);
        acf.setComponent(ampComponent);
        session.save(acf);
        acf.setActivity(ampActivity);
      }
    }
  }

  private AmpActivity initializeActivity(AmpActivity activity, ActivityType xmlActivity, AmpTeam ampTeam) {

    if (activity == null) {
      activity = new AmpActivity();
    }

    activity.setTeam(ampTeam);

    if (activity.getCategories() == null) {
      activity.setCategories(new HashSet());
    }
    if (activity.getInternalIds() == null) {
      activity.setInternalIds(new HashSet());
    }
    if (activity.getLocations() == null) {
      activity.setLocations(new HashSet());
    }
    if (activity.getRegionalFundings() == null) {
      activity.setRegionalFundings(new HashSet());
    }
    if (activity.getSectors() == null) {
      activity.setSectors(new HashSet());
    }
    if (activity.getActivityPrograms() == null) {
      activity.setActivityPrograms(new HashSet());
    }
    if (activity.getFunding() == null) {
      activity.setFunding(new HashSet());
    }
    if (activity.getComponents() == null) {
      activity.setComponents(new HashSet());
    }
    if (activity.getIssues() == null) {
      activity.setIssues(new HashSet());
    }
    if (activity.getOrgrole() == null) {
      activity.setOrgrole(new HashSet());
    }

    // set AmpId
    activity.setAmpId(xmlActivity.getAmpId());

    // set Title
    if (xmlActivity.getTitle() != null) {
      activity.setName(xmlActivity.getTitle().getValue());
    }
    // set create date
    if (xmlActivity.getCreateDate() != null) {
      activity.setCreatedDate(xmlActivity.getCreateDate().getTime());
    }

    // set LineMinRank
    if (xmlActivity.getLineMinistryRank() >= 0) {
      activity.setLineMinRank(Integer.valueOf(xmlActivity.getLineMinistryRank()));
    }

    if (xmlActivity.getMinistryPlanningRank() >= 0) {
      activity.setPlanMinRank(Integer.valueOf(xmlActivity.getMinistryPlanningRank()));
    }

    if (xmlActivity.getProposedApprovalDate() != null) {
      activity.setProposedApprovalDate(xmlActivity.getProposedApprovalDate().getTime());
    }

    if (xmlActivity.getActualApprovalDate() != null) {
      activity.setActivityApprovalDate(xmlActivity.getActualApprovalDate().getTime());
    }

    if (xmlActivity.getProposedStartDate() != null) {
      activity.setProposedStartDate(xmlActivity.getProposedStartDate().getTime());
    }

    if (xmlActivity.getActualStartDate() != null) {
      activity.setActivityStartDate(xmlActivity.getActualStartDate().getTime());
    }

    if (xmlActivity.getCurrentCompletionDate() != null) {
      activity.setActualCompletionDate(xmlActivity.getCurrentCompletionDate().getTime());
    }

    if (xmlActivity.getProposedCompletionDate() != null) {
      activity.setProposedCompletionDate(xmlActivity.getProposedCompletionDate().getTime());
    }

    // set Status Reason
    if (xmlActivity.getStatusReason() != null) {
      activity.setStatusReason(xmlActivity.getStatusReason().getValue());
    }

    // set Program Description
    if (xmlActivity.getProgramDescription() != null) {
      activity.setProgramDescription(xmlActivity.getProgramDescription().getValue());
    }

    // set Contractors
    if (xmlActivity.getContractor() != null) {
      activity.setContractors(xmlActivity.getContractor().getValue());
    }

    ContactType ctDonor = xmlActivity.getDonorFundingContact();
    if (ctDonor != null) {
      activity.setContFirstName(ctDonor.getFirstName());
      activity.setContLastName(ctDonor.getLastName());
      activity.setEmail(ctDonor.getEmail());
    }

    ContactType ctMofed = xmlActivity.getMofedContact();
    if (ctMofed != null) {
      activity.setMofedCntFirstName(ctMofed.getFirstName());
      activity.setMofedCntLastName(ctMofed.getLastName());
      activity.setMofedCntEmail(ctMofed.getEmail());
    }
    return activity;
  }

  private AmpIssues containsIssue(String name, Set set) {
    AmpIssues retValue = new AmpIssues();
    for (Object elem : set) {
      AmpIssues ampObj = (AmpIssues)elem;
      if (name.trim().equalsIgnoreCase(ampObj.getName().trim())) {
        retValue = ampObj;
        break;
      }
    }
    if (retValue.getMeasures() == null) {
      retValue.setMeasures(new HashSet());
    }
    retValue.setName(name);
    return retValue;
  }

  private AmpMeasure containsMeasure(String name, Set set) {
    AmpMeasure retValue = new AmpMeasure();
    for (Object elem : set) {
      AmpMeasure ampObj = (AmpMeasure)elem;
      if (name.trim().equalsIgnoreCase(ampObj.getName().trim())) {
        retValue = ampObj;
        break;
      }
    }
    if (retValue.getActors() == null) {
      retValue.setActors(new HashSet());
    }
    retValue.setName(name);
    return retValue;
  }

  private AmpActor containsActor(String name, Set set) {
    AmpActor retValue = new AmpActor();
    for (Object elem : set) {
      AmpActor ampObj = (AmpActor)elem;
      if (name.trim().equalsIgnoreCase(ampObj.getName().trim())) {
        retValue = ampObj;
        break;
      }
    }
    retValue.setName(name);
    return retValue;
  }

  private AmpFunding containsFunding(AmpOrganisation organisation, AmpCategoryValue ampCategoryValue, String financingId, Set set) {
    AmpFunding retValue = new AmpFunding();

    for (Object elem : set) {
      AmpFunding ampObj = (AmpFunding)elem;
      if (financingId.trim().equalsIgnoreCase(ampObj.getFinancingId().trim()) &&
          organisation.equals(ampObj.getAmpDonorOrgId()) ){
        AmpCategoryValue acv = ampObj.getTypeOfAssistance();
        if (acv.getIndex() ==  ampCategoryValue.getIndex()){
//          termsAssist.getAmpTermsAssistId().equals(ampObj.getAmpTermsAssistId().getAmpTermsAssistId())) {
                retValue = ampObj;
                break;
        }
      }
    }
    retValue.setFundingDetails(new HashSet()); // removing all old details

    retValue.setAmpDonorOrgId(organisation);
    retValue.setFinancingId(financingId);
//    retValue.setAmpTermsAssistId(termsAssist);
    retValue.setTypeOfAssistance(ampCategoryValue);
    return retValue;
  }


  private void createEditorTrn(AmpTeam ampTeam, String editorKey, String language, String body, Session session)
      throws DgException, HibernateException{

    if (language==null){
      language="en";
    }

    Editor translation = DbUtil.getEditor(XmlHelper.siteId, language, editorKey, session);
    if (translation == null){
      translation= new Editor();
      translation.setSiteId(XmlHelper.siteId);
      translation.setLanguage(language);
      translation.setEditorKey(editorKey);
    }

    translation.setLastModDate(new Date());
    translation.setUser(ampTeam.getTeamLead().getUser());
    translation.setBody(body);

    session.save(translation);
  }
}


