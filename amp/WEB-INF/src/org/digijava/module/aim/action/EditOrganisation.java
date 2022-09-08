package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpOrgLocation;
import org.digijava.module.aim.dbentity.AmpOrgRecipient;
import org.digijava.module.aim.dbentity.AmpOrgStaffInformation;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpOrganisationContact;
import org.digijava.module.aim.dbentity.AmpOrganisationDocument;
import org.digijava.module.aim.dbentity.AmpOrganizationBudgetInformation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.AddOrgForm;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ContactInfoUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.budget.dbentity.AmpBudgetSector;
import org.digijava.module.budget.dbentity.AmpDepartments;
import org.digijava.module.budget.helper.BudgetDbUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.action.SelectDocumentDM;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;
import org.digijava.module.translation.util.MultilingualInputFieldValues;
import org.hibernate.JDBCException;

public class EditOrganisation extends DispatchAction {

    private static final int MAX_ACTIVITIES = 100;

  private static Logger logger = Logger.getLogger(EditOrganisation.class);
  public final static String MULTILINGUAL_ORG_PREFIX = "multilingual_organisation";
  
  private boolean sessionChk(HttpServletRequest request) {
      HttpSession session = request.getSession();
      DocumentManagerUtil.setMaxFileSizeAttribute(request);
      if (session.getAttribute("ampAdmin") == null) {
          return true;
      } else {
          String str = (String) session.getAttribute("ampAdmin");
          if ("no".equals(str)) {
              return true;
          }
      }
      return false;
  }
  private boolean sessionChkForWInfo(HttpServletRequest request) {
      HttpSession session = request.getSession();
      DocumentManagerUtil.setMaxFileSizeAttribute(request);
      TeamMember tm = (TeamMember) session.getAttribute("currentMember");
      boolean plainTeamMember = tm == null || !tm.getTeamHead();
      if (session.getAttribute("ampAdmin") == null&&plainTeamMember) {
          return true;
      } else {
          String str = (String) session.getAttribute("ampAdmin");
          if ("no".equals(str)&&plainTeamMember) {
              return true;
          }
      }
      return false;
  }
  
  @Override
  protected ActionForward unspecified(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
      if (sessionChk(request)) {
          return mapping.findForward("index");
      }
      return create(mapping, form, request, response);
  }
  
  public ActionForward create(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
      if (sessionChk(request)) {
          return mapping.findForward("index");
      }
      AddOrgForm editForm = (AddOrgForm) form;
      if(request.getParameter("skipReset")==null){
          clean(editForm);
      }
      request.setAttribute(MULTILINGUAL_ORG_PREFIX + "_name", editForm.buildMultilingualNameInputInstance());
      this.putDocumentsInSession(request, new AmpOrganisation());
      return mapping.findForward("forward");
  }
  
  public ActionForward edit(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
      if (sessionChkForWInfo(request)) {
          return mapping.findForward("index");
      }
      
      AddOrgForm editForm = (AddOrgForm) form;
      Long orgId = editForm.getAmpOrgId();
      clean(editForm);
      editForm.setAmpOrgId(orgId);
      request.setAttribute(MULTILINGUAL_ORG_PREFIX + "_name", editForm.buildMultilingualNameInputInstance());

      AmpOrganisation organization = DbUtil.getOrganisation(orgId);
      editForm.setName(organization.getName());
      editForm.setAcronym(organization.getAcronym());
      editForm.setFundingorgid(organization.getFundingorgid());
      Collection orgGroups = null;
      if (organization.getCountry() != null) {
          editForm.setCountryId(organization.getCountry().getId());
      }
      AmpOrgType orgType = null;
      if (organization.getOrgGrpId() != null) {
          orgType = organization.getOrgGrpId().getOrgType();
          editForm.setOrgGroupName(organization.getOrgGrpId().getOrgGrpName());
          editForm.setOrgTypeName(orgType.getOrgType());
          Long orgTypeId = orgType.getAmpOrgTypeId();
          orgGroups = DbUtil.searchForOrganisationGroupByType(orgTypeId);
          editForm.setAmpOrgTypeId(orgType.getAmpOrgTypeId());
          editForm.setAmpOrgGrpId(organization.getOrgGrpId().getAmpOrgGrpId());
          List sortedCol = new ArrayList(orgGroups);
          Collections.sort(sortedCol, new DbUtil.HelperAmpOrgGroupNameComparator());
          editForm.setOrgGroup(sortedCol);
          editForm.setType(orgType.getClassification());
          if (orgType.getClassification() != null && orgType.getClassification().equals(Constants.ORG_TYPE_NGO)) {

              if (organization.getStaffInfos() != null) {
                  editForm.setStaff(new ArrayList(organization.getStaffInfos()));
              }
              if (organization.getOrganizationBudgetInfos() != null) {
                  editForm.setOrgInfos(new ArrayList(organization.getOrganizationBudgetInfos()));
              }
              editForm.setOrgPrimaryPurpose(organization.getPrimaryPurpose());
            
              editForm.setRegNumbMinPlan(organization.getMinPlanRegNumb());
              
              if (organization.getMinPlanRegDate() != null) {
                  editForm.setMinPlanRegDate(FormatHelper.formatDate(organization.getMinPlanRegDate()));
              }
              
              editForm.setLegalPersonNum(organization.getLegalPersonNum());
              if (organization.getLegalPersonRegDate() != null) {
                  editForm.setLegalPersonRegDate(FormatHelper.formatDate(organization.getLegalPersonRegDate()));
              }
              if (organization.getOperFuncApprDate() != null) {
                  editForm.setOperFuncApprDate(FormatHelper.formatDate(organization.getOperFuncApprDate()));
              }
              if (organization.getLineMinRegDate() != null) {
                  editForm.setLineMinRegDate(FormatHelper.formatDate(organization.getLineMinRegDate()));
             }
             editForm.setLineMinRegNumber(organization.getLineMinRegNumber());
             editForm.setReceiptLegPersonalityAct(organization.getReceiptLegPersonalityAct());
             editForm.setOtherInformation(organization.getOtherInformation());
             List<AmpOrgRecipient> recipients=new ArrayList<AmpOrgRecipient>(organization.getRecipients());
              Collections.sort(recipients, new DbUtil.HelperAmpOrgRecipientByOrgName());
              editForm.setRecipients(recipients);


              editForm.setTaxNumber(organization.getTaxNumber());

              editForm.setAddressAbroad(organization.getAddressAbroad());
              if (organization.getImplemLocationLevel() != null) {
                  editForm.setImplemLocationLevel(organization.getImplemLocationLevel().getId());
              } else {
                  editForm.setImplemLocationLevel(null);
              }
              // locations
              Collection<AmpOrgLocation> locations = organization.getLocations();
              if (locations != null) {
                  List<Location> locs = new ArrayList<Location>();
                  Iterator<AmpOrgLocation> locationIter = locations.iterator();
                  while (locationIter.hasNext()) {
                      AmpOrgLocation location = locationIter.next();
                      Location loc = new Location();
                      AmpCategoryValueLocations catValLoc = location.getLocation();
                      loc.setAmpCVLocation(catValLoc);
                      loc.setPercent(location.getPercent().toString());
                      loc.setAncestorLocationNames(DynLocationManagerUtil.getParents(catValLoc));
                      loc.setLocationName(catValLoc.getName());
                      loc.setLocId(catValLoc.getId());
                      locs.add(loc);
                  }
                  editForm.setSelectedLocs(locs);
              }


          } else {

              editForm.setDacOrgCode(organization.getDacOrgCode());
              editForm.setOrgIsoCode(organization.getOrgIsoCode());
              editForm.setOrgCode(organization.getOrgCode());
              editForm.setBudgetOrgCode(organization.getBudgetOrgCode());
              editForm.setDescription(organization.getDescription());

            


          }

      } else {
          editForm.setAmpOrgTypeId(null);
          editForm.setOrgGroup(null);
          editForm.setAmpOrgGrpId(null);
          editForm.setType(null);
      }
      if(request.getSession().getAttribute("reloadOrgDocsFromDb")!=null){
          request.getSession().removeAttribute("reloadOrgDocsFromDb");
      }else{
          this.putDocumentsInSession(request, organization);
      }
      
      editForm.setOrgUrl(organization.getOrgUrl());
      editForm.setAddress(organization.getAddress());

   // set contacts
      List<AmpOrganisationContact> orgConts=ContactInfoUtil.getOrganizationContacts(organization.getAmpOrgId());
      editForm.setOrgContacts(new ArrayList<AmpOrganisationContact>());
      if (orgConts!= null && orgConts.size() > 0) {
        for (AmpOrganisationContact orgCont : orgConts) {
                if(orgCont.getPrimaryContact()!=null && orgCont.getPrimaryContact()){
                    if(editForm.getPrimaryOrgContIds()==null){
                        editForm.setPrimaryOrgContIds(new String[1]);
                    }
                    String idToCompare=orgCont.getContact().getId()!=null ? orgCont.getContact().getId().toString() : orgCont.getContact().getTemporaryId();
                    editForm.getPrimaryOrgContIds()[0]=idToCompare;
                }                   
                editForm.getOrgContacts().add(orgCont);
            }
        //editForm.setOrgContacts(new ArrayList<AmpOrganisationContact>(organization.getOrganizationContacts()));
      }
      
      
      //set sectors
      Set<AmpSector> sectors = organization.getSectors();
      if (sectors != null && sectors.size() > 0) {
          List<ActivitySector> activitySectors = new ArrayList<ActivitySector>();
          Iterator sectItr = sectors.iterator();
          while (sectItr.hasNext()) {
              AmpSector sec = (AmpSector) sectItr.next();
              if (sec != null) {
                  AmpSector parent = null;
                  AmpSector subsectorLevel1 = null;
                  AmpSector subsectorLevel2 = null;
                  if (sec.getParentSectorId() != null) {
                      if (sec.getParentSectorId().getParentSectorId() != null) {
                          subsectorLevel2 = sec;
                          subsectorLevel1 = sec.getParentSectorId();
                          parent = sec.getParentSectorId().getParentSectorId();
                      } else {
                          subsectorLevel1 = sec;
                          parent = sec.getParentSectorId();
                      }
                  } else {
                      parent = sec;
                  }
                  ActivitySector actSect = new ActivitySector();
                  if (parent != null) {
                      actSect.setId(parent.getAmpSectorId());
                      actSect.setSectorId(parent.getAmpSectorId());
                      actSect.setSectorName(parent.getName());
                      if (subsectorLevel1 != null) {
                          actSect.setSubsectorLevel1Id(subsectorLevel1.getAmpSectorId());
                          actSect.setSubsectorLevel1Name(subsectorLevel1.getName());
                          if (subsectorLevel2 != null) {
                              actSect.setSubsectorLevel2Id(subsectorLevel2.getAmpSectorId());
                              actSect.setSubsectorLevel2Name(subsectorLevel2.getName());
                          }
                      }
                      actSect.setSectorScheme(parent.getAmpSecSchemeId().getSecSchemeName());
                  }
                  activitySectors.add(actSect);
              }

          }
          Collections.sort(activitySectors);
          editForm.setSectors(activitySectors);
      }
      
      if (organization.getAmpSecSchemeId() != null) {
          editForm.setAmpSecSchemeId(organization.getAmpSecSchemeId().getAmpSecSchemeId());
      }
      
      if (organization.getAmpFiscalCalId() != null) {
          editForm.setFiscalCalId(organization.getAmpFiscalCalId().getAmpFiscalCalId());
      }

      if (organization.getRegion() != null) {
          editForm.setRegionId(organization.getRegion().getId());
      }
      //Budget Sectors
      Collection<Long> selectedbudgetsectors = new Vector<Long>();
      for (Iterator iterator = organization.getBudgetsectors().iterator(); iterator.hasNext();) {
          AmpBudgetSector sector = (AmpBudgetSector) iterator.next();
          selectedbudgetsectors.add(sector.getIdsector());
      }
      if (selectedbudgetsectors.size()!= 0){
          editForm.setSelectedbudgetsectors(selectedbudgetsectors.toArray(new Long[selectedbudgetsectors.size()]));
      }else{
          Long[] defaultvalue = {0L}; 
          editForm.setSelectedbudgetsectors(defaultvalue);
      }
      
      //Departments
      Collection<Long> selecteddeps = new Vector<Long>();
      for (Iterator iterator = organization.getDepartments().iterator(); iterator.hasNext();) {
          AmpDepartments ampdepartment = (AmpDepartments) iterator.next();
          selecteddeps.add(ampdepartment.getId());
      }
      if (selecteddeps.size()!= 0){
          editForm.setSelecteddepartments(selecteddeps.toArray(new Long[selecteddeps.size()]));
      }else{
          Long[] defaultvalue = {0L}; 
          editForm.setSelecteddepartments(defaultvalue);
      }
        HttpSession session = request.getSession();
        TeamMember tm = (TeamMember) session.getAttribute("currentMember");
        if (tm!=null&&tm.getTeamHead()) {
            return mapping.findForward("forwardWI");
        } else {
            return mapping.findForward("forward");
        }
      
  }
  
  
  public ActionForward delete(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
      if (sessionChk(request)) {
          return mapping.findForward("index");
      }
      AddOrgForm editForm = (AddOrgForm) form;
      ActionMessages errors = new ActionMessages();

      Set<String> ampIds = getAmpIdsWithOrg(editForm.getAmpOrgId());

      if (!ampIds.isEmpty()) {
          String topAmpIds = ampIds.stream().limit(MAX_ACTIVITIES).collect(Collectors.joining(", "));
          errors.add(ActionMessages.GLOBAL_MESSAGE,
                  new ActionMessage("error.aim.organizationManager.deleteOrgActError", topAmpIds));
          saveErrors(request, errors);
          editForm.setActionFlag("edit");
          return mapping.findForward("forward");
      } else {
          AmpOrganisation org = DbUtil.getOrganisation(editForm.getAmpOrgId());
          if (org.getCalendar() != null && org.getCalendar().size() > 0) {
              errors.add(ActionMessages.GLOBAL_MESSAGE,
                      new ActionMessage("error.aim.organizationManager.deleteOrgEventError"));
              saveErrors(request, errors);
              editForm.setActionFlag("edit");
              return mapping.findForward("forward");

          }

          List<AmpTeam> releatedTeams = TeamUtil.getTeamByOrg(editForm.getAmpOrgId());
          if (releatedTeams != null && !releatedTeams.isEmpty()){
              errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.organizationManager.deleteOrgTeamError"));
              saveErrors(request, errors);
              editForm.setActionFlag("edit");
              return mapping.findForward("forward");
          }


          try {                             
              DbUtil.deleteOrg(org);
          } catch (JDBCException e) {
              // this is a quick solution
              // due to there is a integrity referencial issue
              // related to table amp_activity_internal_id,
              // which does not have a hibernate mapping.
              editForm.setFlag("activityReferences");
              editForm.setActionFlag("edit");
              return mapping.findForward("forward");
          }
          logger.debug("Organisation deleted");

          return mapping.findForward("added");

      }

  }

  private Set<String> getAmpIdsWithOrg(Long orgId) {
      Set<String> ids = new TreeSet<>();
      ids.addAll(DbUtil.getAmpIdsByOrg(orgId));
      ids.addAll(ActivityUtil.getAmpIdsByFundingOrg(orgId));
      ids.addAll(DbUtil.getAmpIdsByInternalIdOrg(orgId));
      return ids;
  }

  public ActionForward addStaffInfo(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
      if (sessionChkForWInfo(request)) {
          return mapping.findForward("index");
      }
      AddOrgForm editForm = (AddOrgForm) form;
      List<AmpOrgStaffInformation> staff = editForm.getStaff();
      if (staff == null) {
          staff = new ArrayList<AmpOrgStaffInformation>();
      }
      AmpOrgStaffInformation info = null;
      if (editForm.getStaffInfoIndex() != -1) {
          info=staff.get(editForm.getStaffInfoIndex());
      } else {
          info = new AmpOrgStaffInformation();
          long temp = new Date().getTime();
          info.setId(temp);
          info.setNewlyCreated(true);
          staff.add(info);
          editForm.setStaff(staff);
      }
      info.setYear(Long.parseLong(editForm.getSelectedYear()));
      info.setStaffNumber(Long.parseLong(editForm.getNumberOfStaff()));
      AmpCategoryValue type = CategoryManagerUtil.getAmpCategoryValueFromDb(editForm.getTypeOfStaff());
      info.setType(type);
      editForm.setSelectedYear(null);
      editForm.setNumberOfStaff(null);
      editForm.setTypeOfStaff(null);
      editForm.setStaffInfoIndex(-1);
      HttpSession session = request.getSession();
      request.setAttribute(MULTILINGUAL_ORG_PREFIX + "_name", editForm.restoreMultilingualNameInputInstance(request));
      TeamMember tm = (TeamMember) session.getAttribute("currentMember");
        if (tm!=null&&tm.getTeamHead()) {
            return mapping.findForward("forwardWI");
        } else {
            return mapping.findForward("forward");
        }
  }

  public ActionForward deleteStaffInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {
      if (sessionChkForWInfo(request)) {
          return mapping.findForward("index");
      }
      AddOrgForm editForm = (AddOrgForm) form;
      Long ids[] = null;
      Long staffId = editForm.getSelectedStaffId();
      if (staffId != null && !staffId.equals(0l)) {
          ids = new Long[]{staffId};
      } else {
          ids = editForm.getSelectedStaff();
      }
      List<AmpOrgStaffInformation> staff = editForm.getStaff();
      List<AmpOrgStaffInformation> staffToDelete = new ArrayList<AmpOrgStaffInformation>();
      for (Long id : ids) {
          Iterator<AmpOrgStaffInformation> staffIter = staff.iterator();
          while (staffIter.hasNext()) {
              AmpOrgStaffInformation info = staffIter.next();
              if (info.getId().equals(id)) {
                  staffToDelete.add(info);
                  break;
              }
          }
      }
      staff.removeAll(staffToDelete);
      editForm.setSelectedStaff(null);
      editForm.setStaff(staff);
      editForm.setSelectedStaffId(null);
      request.setAttribute(MULTILINGUAL_ORG_PREFIX + "_name", editForm.restoreMultilingualNameInputInstance(request));
        HttpSession session = request.getSession();
        TeamMember tm = (TeamMember) session.getAttribute("currentMember");
        if (tm!=null&&tm.getTeamHead()) {
            return mapping.findForward("forwardWI");
        } else {
            return mapping.findForward("forward");
        }
  
  }
  
    public ActionForward editStaffInfo(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
      if (sessionChkForWInfo(request)) {
          return mapping.findForward("index");
      }
      AddOrgForm editForm = (AddOrgForm) form;
      AmpOrgStaffInformation staff= editForm.getStaff().get(editForm.getStaffInfoIndex());
      editForm.setSelectedYear(staff.getYear().toString());
      editForm.setTypeOfStaff(staff.getType().getId());
      editForm.setNumberOfStaff(staff.getStaffNumber().toString());
      request.setAttribute(MULTILINGUAL_ORG_PREFIX + "_name", editForm.restoreMultilingualNameInputInstance(request));
    HttpSession session = request.getSession();
    TeamMember tm = (TeamMember) session.getAttribute("currentMember");
    if (tm!=null&&tm.getTeamHead()) {
        return mapping.findForward("forwardWI");
    } else {
        return mapping.findForward("forward");
    }
  
  }

  
  public ActionForward addOrgInfo(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
      if (sessionChk(request)) {
          return mapping.findForward("index");
      }
      AddOrgForm editForm = (AddOrgForm) form;
      AmpOrganizationBudgetInformation info=null;
      List<AmpOrganizationBudgetInformation> orgInfos = editForm.getOrgInfos();
      if(orgInfos==null){
          orgInfos=new ArrayList<AmpOrganizationBudgetInformation>();
      }
      if(editForm.getOrgInfoIndex()!=-1){
         info= orgInfos.get(editForm.getOrgInfoIndex());
      }else{
         info = new AmpOrganizationBudgetInformation();
         orgInfos.add(info);
         info.setNewlyCreated(true);
         long temp = new Date().getTime();
         info.setId(temp);
      }
      info.setYear(Long.parseLong(editForm.getOrgInfoSelectedYear()));
      AmpCategoryValue type = CategoryManagerUtil.getAmpCategoryValueFromDb(editForm.getOrgInfoType());
      info.setType(type);
      Set<AmpOrganisation> orgs=null;
      if(editForm.getBudgetOrgs()!=null){
         orgs=new HashSet<AmpOrganisation>(editForm.getBudgetOrgs());
      }
      info.setOrganizations(orgs);
      info.setAmount(Double.parseDouble(editForm.getOrgInfoAmount()));
      info.setCurrency(CurrencyUtil.getAmpcurrency(editForm.getOrgInfoCurrId()));
      editForm.setOrgInfos(orgInfos);
      editForm.setOrgInfoType(null);
      editForm.setOrgInfoPercent(null);
      editForm.setOrgInfoSelectedYear(null);
      editForm.setOrgInfoCurrId(null);
      editForm.setOrgInfoAmount(null);
      editForm.setBudgetOrgs(null);
      editForm.setOrgInfoIndex(-1);
      editForm.setActionFlag("reload");
      request.setAttribute(MULTILINGUAL_ORG_PREFIX + "_name", editForm.restoreMultilingualNameInputInstance(request));
      return mapping.findForward("forward");
  }

  public ActionForward deleteOrgInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
      if (sessionChk(request)) {
          return mapping.findForward("index");
      }
      AddOrgForm editForm = (AddOrgForm) form;
      Long ids[] = null;
      Long orgInfoId = editForm.getSelectedOrgInfoId();
      if (orgInfoId != null && !orgInfoId.equals(0l)) {
          ids = new Long[]{orgInfoId};
      } else {
          ids = editForm.getSelectedOrgInfoIds();
      }
      List<AmpOrganizationBudgetInformation> orgInfos = editForm.getOrgInfos();
      List<AmpOrganizationBudgetInformation> orgInfosToDelete = new ArrayList<AmpOrganizationBudgetInformation>();
      for (Long id : ids) {
          Iterator<AmpOrganizationBudgetInformation> orgInfoIter = orgInfos.iterator();
          while (orgInfoIter.hasNext()) {
              AmpOrganizationBudgetInformation info = orgInfoIter.next();
              if (info.getId().equals(id)) {
                  orgInfosToDelete.add(info);
                  break;
              }
          }
      }
      orgInfos.removeAll(orgInfosToDelete);
      editForm.setSelectedStaff(null);
      editForm.setOrgInfos(orgInfos);
      editForm.setSelectedOrgInfoId(null);
      editForm.setSelectedOrgInfoIds(null);
      request.setAttribute(MULTILINGUAL_ORG_PREFIX + "_name", editForm.restoreMultilingualNameInputInstance(request));
      return mapping.findForward("forward");
  }
  
   public ActionForward editOrgInfo(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
      if (sessionChk(request)) {
          return mapping.findForward("index");
      }
      AddOrgForm editForm = (AddOrgForm) form;
      AmpOrganizationBudgetInformation orgBudgetInfo= editForm.getOrgInfos().get(editForm.getOrgInfoIndex());
      editForm.setOrgInfoType(orgBudgetInfo.getType().getId());
      editForm.setOrgInfoSelectedYear(orgBudgetInfo.getYear().toString());
      editForm.setOrgInfoCurrId(orgBudgetInfo.getCurrency().getAmpCurrencyId());
      editForm.setOrgInfoAmount(orgBudgetInfo.getAmount().toString());
      Set<AmpOrganisation> orgs=new TreeSet<AmpOrganisation>();
      if(orgBudgetInfo.getOrganizations()!=null){
          orgs.addAll(orgBudgetInfo.getOrganizations());
          editForm.setBudgetOrgs(orgs);
      }
      request.setAttribute(MULTILINGUAL_ORG_PREFIX + "_name", editForm.restoreMultilingualNameInputInstance(request));
      return mapping.findForward("forward");
  }

  public ActionForward addSector(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
      if (sessionChkForWInfo(request)) {
          return mapping.findForward("index");
      }
      HttpSession session = request.getSession();
      AddOrgForm editForm = (AddOrgForm) form;
      Collection<ActivitySector> prevSelSectors = editForm.getSectors();
      Object searchedsector = session.getAttribute("add");
      if (searchedsector != null && searchedsector.equals("true")) {
          Collection<ActivitySector> selectedSecto = (Collection) session.getAttribute("sectorSelected");
          prevSelSectors = editForm.getSectors();
          prevSelSectors = addSectors(selectedSecto, prevSelSectors);
      } else {
          ActivitySector selectedSector = (ActivitySector) session.getAttribute("sectorSelected");
          if (selectedSector != null) {
              Collection<ActivitySector> selectedSecto = new ArrayList<ActivitySector>();
              selectedSecto.add(selectedSector);
              prevSelSectors = addSectors(selectedSecto, prevSelSectors);

          }
      }
      editForm.setSectors(prevSelSectors);
      session.removeAttribute("add");
      session.removeAttribute("sectorSelected");
    TeamMember tm = (TeamMember) session.getAttribute("currentMember");
    request.setAttribute(MULTILINGUAL_ORG_PREFIX + "_name", editForm.restoreMultilingualNameInputInstance(request));
    if (tm!=null&&tm.getTeamHead()) {
        return mapping.findForward("forwardWI");
    } else {
        return mapping.findForward("forward");
    }
  
  }

  public ActionForward removeSector(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
      if (sessionChkForWInfo(request)) {
          return mapping.findForward("index");
      }
      AddOrgForm editForm = (AddOrgForm) form;
      Long selSectors[] = editForm.getSelSectors();
      Collection<ActivitySector> prevSelSectors = editForm.getSectors();
      Collection newSectors = new ArrayList();
      Iterator<ActivitySector> itr = prevSelSectors.iterator();
      boolean flag = false;
      while (itr.hasNext()) {
          ActivitySector asec = (ActivitySector) itr.next();
          flag = false;
          for (int i = 0; i < selSectors.length; i++) {
              if (asec.getSubsectorLevel1Id() == -1 && asec.getSectorId().equals(selSectors[i])) {
                  flag = true;
                  break;
              }
              if (asec.getSubsectorLevel1Id() != -1 && asec.getSubsectorLevel2Id() == -1 && asec.getSubsectorLevel1Id().equals(selSectors[i])) {
                  flag = true;
                  break;
              }
              if (asec.getSubsectorLevel1Id() != -1 && asec.getSubsectorLevel2Id() != -1 && asec.getSubsectorLevel2Id().equals(selSectors[i])) {
                  flag = true;
                  break;
              }
          }
          if (!flag) {
              newSectors.add(asec);
          }
      }

      editForm.setSectors(newSectors);
    HttpSession session = request.getSession();
    TeamMember tm = (TeamMember) session.getAttribute("currentMember");
    request.setAttribute(MULTILINGUAL_ORG_PREFIX + "_name", editForm.restoreMultilingualNameInputInstance(request));
    if (tm!=null&&tm.getTeamHead()) {
        return mapping.findForward("forwardWI");
    } else {
        return mapping.findForward("forward");
    }
  
  }
  
  public ActionForward removeRecipient(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
          throws Exception {
      if (sessionChk(request)) {
          return mapping.findForward("index");
      }
      AddOrgForm editForm = (AddOrgForm) form;
      Long[] selRecipients = editForm.getSelRecipients();
      List<AmpOrgRecipient> recipients = editForm.getRecipients();
      for (int i = 0; i < selRecipients.length; i++) {
          Iterator<AmpOrgRecipient> iterRecipient = recipients.iterator();
          while (iterRecipient.hasNext()) {
            AmpOrgRecipient recipient = iterRecipient.next();
            if(recipient.getOrganization().getAmpOrgId().equals(selRecipients[i])){
                iterRecipient.remove();
                break;
            }
          }

      }
      
      editForm.setRecipients(recipients);
      editForm.setSelRecipients(null);
      request.setAttribute(MULTILINGUAL_ORG_PREFIX + "_name", editForm.restoreMultilingualNameInputInstance(request));
      return mapping.findForward("forward");
  }

  public ActionForward removeBudgetOrg(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
          throws Exception {
      if (sessionChk(request)) {
          return mapping.findForward("index");
      }
      AddOrgForm editForm = (AddOrgForm) form;
      Long[] selOrgs = editForm.getSelBudgetOrg();
      Set<AmpOrganisation> orgs = editForm.getBudgetOrgs();
      for (int i = 0; i < selOrgs.length; i++) {
          Iterator<AmpOrganisation> iterOrgs = orgs.iterator();
          while (iterOrgs.hasNext()) {
            AmpOrganisation org = iterOrgs.next();
            if(org.getAmpOrgId().equals(selOrgs[i])){
                iterOrgs.remove();
                break;
            }
          }

      }

      editForm.setBudgetOrgs(orgs);
      editForm.setSelBudgetOrg(null);
      request.setAttribute(MULTILINGUAL_ORG_PREFIX + "_name", editForm.restoreMultilingualNameInputInstance(request));
      return mapping.findForward("forward");
  }

  public ActionForward deleteLocation(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
      if (sessionChk(request)) {
          return mapping.findForward("index");
      }
      AddOrgForm editForm = (AddOrgForm) form;
      Long[] selLocations = editForm.getSelLocs();
      if (selLocations != null) {
          Collection<Location> oldLocs = editForm.getSelectedLocs();
          List<Location> removedLocs = new ArrayList<Location>();
          Iterator<Location> locIter = oldLocs.iterator();
          while (locIter.hasNext()) {
              Location location = locIter.next();
              for (int i = 0; i < selLocations.length; i++) {
                  if (location.getLocId().equals(selLocations[i])) {
                      removedLocs.add(location);
                      break;
                  }

              }
          }
          oldLocs.removeAll(removedLocs);
          editForm.setSelectedLocs(oldLocs);
          editForm.setSelLocs(null);
      }
      request.setAttribute(MULTILINGUAL_ORG_PREFIX + "_name", editForm.restoreMultilingualNameInputInstance(request));
      return mapping.findForward("forward");
  }
  
  public ActionForward typeChanged(ActionMapping mapping, ActionForm form,
          HttpServletRequest request, HttpServletResponse response)
          throws Exception {
      if (sessionChk(request)) {
          return mapping.findForward("index");
      }
      AddOrgForm editForm = (AddOrgForm) form;
      Long orgTypeId = editForm.getAmpOrgTypeId();
      editForm.setAmpOrgGrpId(null);

//      editForm.setOrgGroup(DbUtil.searchForOrganisationGroupByType(orgTypeId));
      List sortedCol = new ArrayList(DbUtil.searchForOrganisationGroupByType(orgTypeId));
      Collections.sort(sortedCol, new DbUtil.HelperAmpOrgGroupNameComparator());
      editForm.setOrgGroup(sortedCol);


      AmpOrgType orgType = DbUtil.getAmpOrgType(orgTypeId);
      editForm.setType(orgType.getClassification());
      if (orgType.getClassification() != null && orgType.getClassification().equals(Constants.ORG_TYPE_NGO)) {
          editForm.setDacOrgCode(null);
          editForm.setOrgIsoCode(null);
          editForm.setBudgetOrgCode(null);
          editForm.setOrgCode(null);
          editForm.setDescription(null);

      } else {
          editForm.setStaff(null);
          editForm.setOrgPrimaryPurpose(null);
          editForm.setRegNumbMinPlan(null);
          editForm.setMinPlanRegDate(null);
          editForm.setLegalPersonNum(null);
          editForm.setLegalPersonRegDate(null);
          editForm.setRecipients(null);
          editForm.setTaxNumber(null);
          editForm.setOrgInfos(null);
          editForm.setOrgInfoCurrId(null);
          editForm.setOrgInfoType(null);
          editForm.setOrgInfoSelectedYear(null);
          editForm.setOrgInfoPercent(null);
          editForm.setAddressAbroad(null);
          editForm.setSelectedLocs(null);
          if (orgType.getClassification() == null || !orgType.getClassification().equals(Constants.ORG_TYPE_REGIONAL)) {
              editForm.setRegionId(null);
          }
          editForm.setOtherInformation(null);
          editForm.setReceiptLegPersonalityAct(null);
          editForm.setLineMinRegDate(null);
          editForm.setLineMinRegNumber(null);
          editForm.setOperFuncApprDate(null);
      }
      request.setAttribute(MULTILINGUAL_ORG_PREFIX + "_name", editForm.restoreMultilingualNameInputInstance(request));
      return mapping.findForward("forward");

  }

  public ActionForward reload(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception 
  {
      AddOrgForm editForm = (AddOrgForm) form;
      HttpSession session = request.getSession();
      TeamMember tm = (TeamMember) session.getAttribute("currentMember");
      String forwardWhere=(tm!=null&&tm.getTeamHead())?"forwardWI":"forward";
      String asynchCall=request.getParameter("asynchCall");
      if(asynchCall!=null && asynchCall.equals("true")){
          forwardWhere = null;
      }else{
          // AddOrgForm editForm = (AddOrgForm) form;
          if (editForm.getRecipients()!=null)
          {
              Collections.sort(editForm.getRecipients(), new DbUtil.HelperAmpOrgRecipientByOrgName());
          }
      }
    
      if (request.getSession().getAttribute("locations") != null)
      {
          Collection<Location> locs = (Collection<Location>) request.getSession().getAttribute("locations");
          if (editForm.getSelectedLocs() == null) {
              editForm.setSelectedLocs(new ArrayList<Location>());
          }
          Collection<Location> selectedLocs = editForm.getSelectedLocs();
          for (Location location : locs) {
              if (!selectedLocs.contains(location)) {
                  location.setPercent("0");
                  selectedLocs.add(location);
              }
          }

          request.getSession().removeAttribute("locations");
      }
      request.setAttribute(MULTILINGUAL_ORG_PREFIX + "_name", editForm.restoreMultilingualNameInputInstance(request));
      return mapping.findForward(forwardWhere);
  }

  public ActionForward save(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
      boolean isAdmin=!sessionChk(request);
      HttpSession session = request.getSession();
      TeamMember tm = (TeamMember) session.getAttribute("currentMember");
      AddOrgForm editForm = (AddOrgForm) form;

      request.setAttribute(MULTILINGUAL_ORG_PREFIX + "_name", editForm.restoreMultilingualNameInputInstance(request));

        
      boolean isTeamHead=tm!=null&&tm.getTeamHead();
      if (!isAdmin) {
          if(!isTeamHead){
              return mapping.findForward("index");
          }
      }
//      AddOrgForm editForm = (AddOrgForm) form;
      AmpOrganisation organization = null;
      Long orgId = editForm.getAmpOrgId();
      String action = "create";
      boolean exist = false;
      String organisationName = StringUtils.trim(editForm.getName());
      AmpOrganisation org = DbUtil.getOrganisationByName(organisationName);
      if (orgId == null || orgId.equals(0l)) {
          organization = new AmpOrganisation();
          if (org != null) {
              exist = true;
          }
      } else {
          organization = DbUtil.getOrganisation(orgId);
          action = "edit";
          if (org != null && !organization.getName().equals(organisationName)) {
              exist = true;
          }
      }

      ActionMessages errors = new ActionMessages();
      if (exist) {
          errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.organizationManager.saveOrgNameError"));
      }
      if(isAdmin){
        if (editForm.getOrgCode() != null
                && editForm.getOrgCode().trim().length() > 0) {
            Collection orgsCol = DbUtil.getOrgByCode(action,
                    editForm.getOrgCode(), editForm.getAmpOrgId());
            if (!orgsCol.isEmpty()) { // To check for duplicate org-code
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                        "error.aim.organizationManager.saveOrgCodeError"));
            }
        }
        if (!StringUtils.isBlank(editForm.getAcronym())) {
            Collection orgsColAcronym = DbUtil.getOrgByAcronym(action, editForm.getAcronym(), editForm.getAmpOrgId());
            if (!orgsColAcronym.isEmpty()) { // To check for duplicate org-code
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                        "error.aim.organizationManager.saveOrgAcronymError"));
            }
        }
      }
       
       String[] orgContsIds=editForm.getPrimaryOrgContIds();

        if(orgContsIds!=null && orgContsIds.length>1){ //more then one primary contact is not allowed
            errors.add("invalidOrgCont",new ActionMessage("error.aim.addOrganization.invalidOrgCont", TranslatorWorker.translateText("Must Be One Primary Donor Contact")));

        }
      
      if(!errors.isEmpty()){
        saveErrors(request, errors);
        if (isTeamHead) {
            return mapping.findForward("forwardWI");
        } else {
            return mapping.findForward("forward");
        }
      
      }
      
      if(editForm.getResetPrimaryOrgContIds()!=null && editForm.getResetPrimaryOrgContIds()){
        editForm.setPrimaryOrgContIds(null);
      }
      if (isAdmin) {
          if (ContentTranslationUtil.multilingualIsEnabled()) {
              organization.setName(organisationName);
          } else {
              String langCode = TLSUtils.getSite().getDefaultLanguage().getCode();
              String name = StringUtils.trim(MultilingualInputFieldValues.readParameter(
                      "AmpOrganisation_name_" + langCode, "AmpOrganisation_name", request).getRight());
              organization.setName(name);
          }
              
          
          organization.setAcronym(editForm.getAcronym());
          organization.setFundingorgid(editForm.getFundingorgid());

          if (editForm.getFiscalCalId() != null && !editForm.getFiscalCalId().equals(-1l)) {
              organization.setAmpFiscalCalId(DbUtil.getAmpFiscalCalendar(editForm.getFiscalCalId()));
          } else {
              organization.setAmpFiscalCalId(null);
          }
          organization.setBudgetOrgCode(editForm.getBudgetOrgCode());
          if (editForm.getCountryId() != null && !editForm.getCountryId().equals(-1l)) {
              organization.setCountry(DynLocationManagerUtil.getLocation(editForm.getCountryId(), false));
          } else {
              organization.setCountry(null);
          }
          organization.setDacOrgCode(editForm.getDacOrgCode());

          if (editForm.getLegalPersonNum() != null && !editForm.getLegalPersonNum().equals("")) {
              organization.setLegalPersonNum(editForm.getLegalPersonNum());

          } else {
              organization.setLegalPersonNum(null);
          }
          if (editForm.getRegionId() != null && !editForm.getRegionId().equals(-1l)) {
              organization.setRegion(DynLocationManagerUtil.getLocation(editForm.getRegionId(), false));

          } else {
              organization.setRegion(null);
          }
          if (editForm.getLegalPersonRegDate() != null && !editForm.getLegalPersonRegDate().equals("")) {
              organization.setLegalPersonRegDate(FormatHelper.parseDate2(editForm.getLegalPersonRegDate()));
          } else {
              organization.setLegalPersonRegDate(null);
          }
          if (editForm.getMinPlanRegDate() != null && !editForm.getMinPlanRegDate().equals("")) {
              organization.setMinPlanRegDate(FormatHelper.parseDate2(editForm.getMinPlanRegDate()));
          } else {
              organization.setMinPlanRegDate(null);
          }
          if (editForm.getRegNumbMinPlan() != null && !editForm.getRegNumbMinPlan().equals("")) {
              organization.setMinPlanRegNumb(editForm.getRegNumbMinPlan());
          } else {
              organization.setMinPlanRegNumb(null);
          }
       
          if (editForm.getOperFuncApprDate() != null&& !editForm.getOperFuncApprDate().equals("")) {
              organization.setOperFuncApprDate(FormatHelper.parseDate2(editForm.getOperFuncApprDate()));
          }
          else{
              organization.setOperFuncApprDate(null);
          }
          if (editForm.getLineMinRegDate() != null&& !editForm.getLineMinRegDate().equals("")) {
              organization.setLineMinRegDate(FormatHelper.parseDate2(editForm.getLineMinRegDate()));
          }
          else{
              organization.setLineMinRegDate(null);
          }
          if (editForm.getLineMinRegNumber() != null && !editForm.getLineMinRegNumber().equals("")) {
              organization.setLineMinRegNumber(editForm.getLineMinRegNumber());

          } else {
              organization.setLineMinRegNumber(null);
          }
          organization.setReceiptLegPersonalityAct(editForm.getReceiptLegPersonalityAct());
          organization.setOtherInformation(editForm.getOtherInformation());
          organization.setOrgGrpId(DbUtil.getAmpOrgGroup(editForm.getAmpOrgGrpId()));
          organization.setOrgIsoCode(editForm.getOrgIsoCode());
          organization.setOrgCode(editForm.getOrgCode());
          if (editForm.getImplemLocationLevel() != null && editForm.getImplemLocationLevel() != 0) {
              organization.setImplemLocationLevel(CategoryManagerUtil.getAmpCategoryValueFromDb(editForm.getImplemLocationLevel()));
          } else {
              organization.setImplemLocationLevel(null);
          }

          if (editForm.getTaxNumber() != null && !editForm.getTaxNumber().equals("")) {
              organization.setTaxNumber(editForm.getTaxNumber());
          }
       

          // recipients
          if (organization.getRecipients() == null) {
              organization.setRecipients(new HashSet<AmpOrgRecipient>());
          } 
          if (editForm.getRecipients() != null) {
              Iterator<AmpOrgRecipient> recipientIter = editForm.getRecipients().iterator();
              Set<AmpOrgRecipient> recipients = new HashSet<AmpOrgRecipient>();
              Set<AmpOrgRecipient> recipientsToRetain = new HashSet<AmpOrgRecipient>();
              while (recipientIter.hasNext()) {
                  AmpOrgRecipient recipient = recipientIter.next();
                  /*if (recipient.getAmpOrgRecipientId()!=null&&recipient.getAmpOrgRecipientId()!=0) {
                      AmpOrgRecipient newRecipient = (AmpOrgRecipient) DbUtil.getObject(AmpOrgRecipient.class, recipient.getAmpOrgRecipientId());
                      newRecipient.setDescription(recipient.getDescription());
                      recipientsToRetain.add(newRecipient);
                      continue;
                  }*/
                  AmpOrgRecipient newRecipient = new AmpOrgRecipient();
                  newRecipient.setParentOrganization(organization);
                  newRecipient.setOrganization(recipient.getOrganization());
                  newRecipient.setDescription(recipient.getDescription());
                  recipients.add(newRecipient);
              }
              //organization.getRecipients().retainAll(recipientsToRetain);
              organization.getRecipients().clear();
              organization.getRecipients().addAll(recipients);
          }
          // organization information
          if (organization.getOrganizationBudgetInfos() == null) {
              organization.setOrganizationBudgetInfos(new HashSet<AmpOrganizationBudgetInformation>());
          }

          if (editForm.getOrgInfos() != null) {
              Iterator<AmpOrganizationBudgetInformation> infoIter = editForm.getOrgInfos().iterator();
              Set<AmpOrganizationBudgetInformation> infosOrg = new HashSet<AmpOrganizationBudgetInformation>();
              Set<AmpOrganizationBudgetInformation> infosOrgToRetain = new HashSet<AmpOrganizationBudgetInformation>();
              AmpOrganizationBudgetInformation newInfo = null;
              while (infoIter.hasNext()) {
                  AmpOrganizationBudgetInformation info = infoIter.next();
                 /* if (!info.isNewlyCreated()) {
                      newInfo = (AmpOrganizationBudgetInformation) DbUtil.getObject(AmpOrganizationBudgetInformation.class, info.getId());
                      newInfo.setYear(info.getYear());
                      newInfo.setType(info.getType());
                      newInfo.setCurrency(info.getCurrency());
                      newInfo.setAmount(info.getAmount());
                      if (newInfo.getOrganizations() != null) {
                          newInfo.getOrganizations().clear();
                      } else {
                          newInfo.setOrganizations(new HashSet<AmpOrganisation>());
                      }
                      if (info.getOrganizations() != null) {
                          newInfo.getOrganizations().addAll(info.getOrganizations());
                      }
                      infosOrgToRetain.add(newInfo);
                      continue;
                  } else {*/
                      newInfo = new AmpOrganizationBudgetInformation();
                      newInfo.setOrganization(organization);
                      newInfo.setYear(info.getYear());
                      newInfo.setType(info.getType());
                      newInfo.setCurrency(info.getCurrency());
                      newInfo.setAmount(info.getAmount());
                      newInfo.setOrganizations(info.getOrganizations());
                      infosOrg.add(newInfo);
                 // }
                  
                
                 // organization.getOrganizationBudgetInfos().retainAll(infosOrgToRetain);
                  organization.getOrganizationBudgetInfos().clear();
                  organization.getOrganizationBudgetInfos().addAll(infosOrg);
              }

          } else {
              organization.getOrganizationBudgetInfos().clear();
          }
          // locations
          if (organization.getLocations() == null) {
              organization.setLocations(new HashSet<AmpOrgLocation>());
          }
          Set<AmpOrgLocation> locations = new HashSet<AmpOrgLocation>();
          if (editForm.getSelectedLocs() != null) {
              Iterator<Location> locationIter = editForm.getSelectedLocs().iterator();
              while (locationIter.hasNext()) {
                  Location location = locationIter.next();
                  Iterator<AmpOrgLocation> iter = organization.getLocations().iterator();
                  AmpOrgLocation newLoc = null;
                  while (iter.hasNext()) {
                      AmpOrgLocation orgLoc = iter.next();
                      if (orgLoc.getLocation().getId().equals(location.getAmpCVLocation().getId())) {
                          orgLoc.setPercent(Double.valueOf(location.getPercent()));
                          newLoc = orgLoc;
                          locations.add(newLoc);
                          break;
                      }
                  }
                  if (newLoc == null) {
                      newLoc = new AmpOrgLocation();
                      newLoc.setOrganization(organization);
                      newLoc.setPercent(Double.valueOf(location.getPercent()));
                      newLoc.setLocation(location.getAmpCVLocation());
                      locations.add(newLoc);
                  }
                     
              }
          }
                 
                
          organization.getLocations().clear();
          organization.getLocations().addAll(locations);


          
          //Budget sectors
          Set<AmpBudgetSector> budgetsectors = new HashSet<AmpBudgetSector>();
          if(editForm.getResetBudgetSectors()!=null && editForm.getResetBudgetSectors()){
              editForm.setSelectedbudgetsectors(null) ;
          }     
          
          if (editForm.getSelectedbudgetsectors() != null) {
              Iterator<AmpBudgetSector> itr = editForm.getBudgetsectors().iterator();
              while (itr.hasNext()) {
                  AmpBudgetSector bsector = itr.next();
                  for (int i = 0; i < editForm.getSelectedbudgetsectors().length; i++) {
                      if(bsector.getIdsector().equals(editForm.getSelectedbudgetsectors()[i])){
                          budgetsectors.add(bsector);
                      }
                }
                  
              }
          }
          organization.setBudgetsectors(budgetsectors);
          
          //Departments
          Set<AmpDepartments> departments = new HashSet<AmpDepartments>();
          if(editForm.getResetDepartments()!=null && editForm.getResetDepartments()){
              editForm.setSelecteddepartments(null);
          }
          
          if (editForm.getSelecteddepartments() != null) {
              Iterator<AmpDepartments> itr = editForm.getDepartments().iterator();
              while (itr.hasNext()) {
                  AmpDepartments dep = itr.next();
                  for (int i = 0; i < editForm.getSelecteddepartments().length; i++) {
                      if(dep.getId().equals(editForm.getSelecteddepartments()[i])){
                          departments.add(dep);
                      }
                }
                  
              }
          }
          organization.setDepartments(departments);

      }
     
      organization.setAddress(editForm.getAddress());
      organization.setAddressAbroad(editForm.getAddressAbroad());
      organization.setDescription(editForm.getDescription());
      organization.setOrgUrl(editForm.getOrgUrl());
      organization.setPrimaryPurpose(editForm.getOrgPrimaryPurpose());

      Long schemeId = editForm.getAmpSecSchemeId();
      if(schemeId != null && schemeId > 0){
          organization.setAmpSecSchemeId(DbUtil.getAmpSectorSchemeById(schemeId));
      }

      // staff information
      if (organization.getStaffInfos() == null) {
          organization.setStaffInfos(new HashSet<AmpOrgStaffInformation>());
      }

      if (editForm.getStaff() != null) {
          Iterator<AmpOrgStaffInformation> infoIter = editForm.getStaff().iterator();
          Set<AmpOrgStaffInformation> infos = new HashSet<AmpOrgStaffInformation>();
          //Set<AmpOrgStaffInformation> infosToRetain = new HashSet<AmpOrgStaffInformation>();
          while (infoIter.hasNext()) {
              AmpOrgStaffInformation info = infoIter.next();
              /*if (!info.isNewlyCreated()) {
                  AmpOrgStaffInformation newInfo = (AmpOrgStaffInformation) DbUtil.getObject(AmpOrgStaffInformation.class, info.getId());
                  newInfo.setStaffNumber(info.getStaffNumber());
                  newInfo.setType(info.getType());
                  newInfo.setYear(info.getYear());
                  infosToRetain.add(newInfo);
                  continue;
              }*/
              AmpOrgStaffInformation newInfo = new AmpOrgStaffInformation();
              newInfo.setOrganization(organization);
              newInfo.setStaffNumber(info.getStaffNumber());
              newInfo.setType(info.getType());
              newInfo.setYear(info.getYear());
              infos.add(newInfo);
          }
         // organization.getStaffInfos().retainAll(infosToRetain);
          organization.getStaffInfos().clear();
          organization.getStaffInfos().addAll(infos);
      }
      else{
          organization.getStaffInfos().clear();
      }

      //Sectors
      Set<AmpSector> sectors = new HashSet<AmpSector>();
      if (editForm.getSectors() != null) {
          Iterator<ActivitySector> itr = editForm.getSectors().iterator();
          while (itr.hasNext()) {
              ActivitySector actSect = itr.next();
              Long sectorId = null;
              if (actSect.getSubsectorLevel2Id() != null && (!actSect.getSubsectorLevel2Id().equals(new Long(-1)))) {
                  sectorId = actSect.getSubsectorLevel2Id();
              } else if (actSect.getSubsectorLevel1Id() != null && (!actSect.getSubsectorLevel1Id().equals(new Long(-1)))) {
                  sectorId = actSect.getSubsectorLevel1Id();
              } else {
                  sectorId = actSect.getSectorId();
              }
              //AmpActivitySector amps = new AmpActivitySector();
              //amps.setActivityId(activity);
              AmpSector amps = null;
              if (sectorId != null && (!sectorId.equals(new Long(-1)))) {
                  amps = SectorUtil.getAmpSector(sectorId);
              }

              sectors.add(amps);
          }
      }
      organization.setSectors(sectors);
      
        /**
         * contacts
         */

        if(organization.getOrganizationContacts()==null){
            organization.setOrganizationContacts(new HashSet<AmpOrganisationContact>());
        }else{          
            organization.getOrganizationContacts().clear();
        }
        
        List<AmpOrganisationContact> allContacts=new ArrayList<AmpOrganisationContact>();
        if(editForm.getOrgContacts()!=null && editForm.getOrgContacts().size()>0){
                    allContacts.addAll(editForm.getOrgContacts());
                }
        
      if(allContacts!=null && allContacts.size()>0){
            for (AmpOrganisationContact orgContact : allContacts) {             
                fillOrganizationContactPrimaryField(editForm.getPrimaryOrgContIds(),orgContact);
            }
                organization.getOrganizationContacts().addAll(allContacts);
      }
      this.saveDocuments(request, organization);
      DbUtil.saveOrg(organization);
      if (ContentTranslationUtil.multilingualIsEnabled()) {
          MultilingualInputFieldValues.serialize(organization, "name", null, null, request);
      }
      return mapping.findForward("added");
  }
  
  public ActionForward deleteContact(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
      if (sessionChkForWInfo(request)) {
          return mapping.findForward("index");
      }
      AddOrgForm editForm = (AddOrgForm) form;
      String selContactId = editForm.getSelContactId();        
      String[] selectedContactInfoIds = editForm.getSelectedContactInfoIds(); //passed contact temp ids
      List<AmpOrganisationContact> odlOrgContacts=editForm.getOrgContacts();
      List<AmpOrganisationContact> orgContsForRemoval=new ArrayList<AmpOrganisationContact>();
      Iterator<AmpOrganisationContact> orgContIter=odlOrgContacts.iterator();
      while(orgContIter.hasNext()){
        AmpOrganisationContact orgCont=orgContIter.next();
        AmpContact contact=orgCont.getContact();
        if (selContactId != null && selContactId.length()>0) {
              if ((contact.getId() != null && contact.getId().toString().equals(selContactId))||(contact.getTemporaryId()!=null&&contact.getTemporaryId().equals(selContactId))) {
                orgContsForRemoval.add(orgCont); 
                  break;
              }
          } else {
              if (selectedContactInfoIds != null) {
                  for (String contactId : selectedContactInfoIds) {
                      if ((contact.getId() != null && !contactId.startsWith("_") && contact.getId().equals(new Long(contactId))) || (contact.getTemporaryId() != null && contact.getTemporaryId().equals(contactId))) {
                        orgContsForRemoval.add(orgCont);
                        break;
                      }
                  }

              }
          }
      }
      
      odlOrgContacts.removeAll(orgContsForRemoval);
      editForm.setOrgContacts(odlOrgContacts);
      editForm.setSelContactId(null);
      editForm.setSelectedContactInfoIds(null);
      request.setAttribute(MULTILINGUAL_ORG_PREFIX + "_name", editForm.restoreMultilingualNameInputInstance(request));
        HttpSession session = request.getSession();
        TeamMember tm = (TeamMember) session.getAttribute("currentMember");
        if (tm != null && tm.getTeamHead()) {
            return mapping.findForward("forwardWI");
        } else {
            return mapping.findForward("forward");
        }
  }
  
  public static List<LabelValueBean> getYearsBeanList() {
      String startYear = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.YEAR_RANGE_START);
      int rangeStartYear = Integer.parseInt(startYear);
      String numbYearsRange = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_OF_YEARS_IN_RANGE);
      int rangeNumber = Integer.parseInt(numbYearsRange);
      return getYearsBeanList(rangeStartYear, rangeNumber);
  }

  public static List<LabelValueBean> getYearsBeanList(int from, int range) {
      List<LabelValueBean> result = new ArrayList<LabelValueBean>();
      int end = from + range;
      for (int i = from; i <= end; i++) {
          result.add(new LabelValueBean(String.valueOf(i), String.valueOf(i)));
      }
      return result;
  }

  public static void clean(AddOrgForm form) throws DgException {
      try {
          form.setAcronym(null);
          form.setAddress(null);
          form.setAmpOrgGrpId(null);
          form.setAmpOrgId(null);
          form.setFundingorgid(null);
          form.setAmpOrgTypeId(null);
          form.setBudgetOrgCode(null);
          form.setCountryId(null);
          form.setDacOrgCode(null);
          form.setDescription(null);
          form.setFiscalCalId(null);
          form.setFlag(null);
          form.setLevel(null);
          form.setLevelFlag(null);
          form.setMode(null);
          form.setName(null);
          form.setNumberOfStaff(null);
          form.setOrgCode(null);
          form.setOrgGroup(null);
          form.setOrgIsoCode(null);
          form.setOrgPrimaryPurpose(null);
          form.setOrgTypeFlag(null);
          form.setOrgUrl(null);
          form.setRegionFlag(null);
          form.setSaveFlag(null);
          form.setSectors(null);
          form.setSelSectors(null);
          form.setSelectedStaff(null);
          form.setSelectedYear(null);
          form.setStaff(null);
          form.setRecipients(null);
          form.setSelRecipients(null);
          form.setSelLocs(null);
          form.setSelectedLocs(null);
          form.setRegion(DynLocationManagerUtil.getLocationsOfTypeRegion());
          form.setAddressAbroad(null);
          form.setImplemLocationLevel(null);
          form.setLegalPersonNum(null);
          form.setLegalPersonRegDate(null);
          form.setParentLocId(null);
          form.setMinPlanRegDate(null);
          form.setRegNumbMinPlan(null);
          form.setCountryId(null);
          form.setRegionId(null);
          form.setType(null);
          form.setTaxNumber(null);
          form.setOrgInfos(null);
          form.setOrgInfoCurrId(null);
          form.setOrgInfoType(null);
          form.setOrgInfoSelectedYear(null);
          form.setOrgInfoPercent(null);
          form.setLineMinRegNumber(null);
          Collection col = DbUtil.getAllOrgTypes();
          if (col != null) {
              List sortedCol = new ArrayList(col);
              Collections.sort(sortedCol, new DbUtil.HelperAmpOrgTypeNameComparator());
              form.setOrgType(sortedCol);
          }
          form.setOrgGroup(null);
          form.setSectorScheme(SectorUtil.getAllSectorSchemes());
          form.setFiscalCal(DbUtil.getAllFisCalenders());
          form.setCurrencies(CurrencyUtil.getActiveAmpCurrencyByName());
          Set<AmpCategoryValueLocations> countryLocations =
                  DynLocationManagerUtil.getLocationsByLayer(CategoryConstants.IMPLEMENTATION_LOCATION_ADM_LEVEL_0);
          form.setCountries(countryLocations);
          form.setYears(getYearsBeanList());
          form.setOrgInfoAmount(null);
          form.setSelectedOrgInfoIds(null);
          form.setOtherInformation(null);
          form.setLineMinRegDate(null);
          form.setOperFuncApprDate(null);
          form.setReceiptLegPersonalityAct(null);
          form.setBudgetOrgs(null);
          form.setSelecteddepartments(null);
          form.setDepartments(BudgetDbUtil.getDepartments());
          form.setSelectedbudgetsectors(null);
          form.setBudgetsectors(BudgetDbUtil.getBudgetSectors());
          form.setOrgContacts(null);
          form.setOrgInfoIndex(-1);
          form.setStaffInfoIndex(-1);
      } catch (Exception ex) {
          logger.error("error",ex);
          throw new DgException(ex);
      }
  }

  public static Collection<ActivitySector> addSectors(Collection<ActivitySector> selectedSectors, Collection<ActivitySector> prevSelSectors) {
      if (selectedSectors != null) {
          Iterator<ActivitySector> itre = selectedSectors.iterator();
          while (itre.hasNext()) {
              ActivitySector selectedSector = itre.next();
              boolean addSector = true;
              if (prevSelSectors != null) {
                  Iterator<ActivitySector> itr = prevSelSectors.iterator();
                  while (itr.hasNext()) {
                      ActivitySector asec = (ActivitySector) itr.next();
                      if (asec.getSectorName().equals(selectedSector.getSectorName())) {
                          if (selectedSector.getSubsectorLevel1Name() == null) {
                              addSector = false;
                              break;
                          }
                          if (asec.getSubsectorLevel1Name() != null) {
                              if (asec.getSubsectorLevel1Name().equals(selectedSector.getSubsectorLevel1Name())) {
                                  if (selectedSector.getSubsectorLevel2Name() == null) {
                                      addSector = false;
                                      break;
                                  }
                                  if (asec.getSubsectorLevel2Name() != null) {
                                      if (asec.getSubsectorLevel2Name().equals(selectedSector.getSubsectorLevel2Name())) {
                                          addSector = false;
                                          break;
                                      }
                                  } else {
                                      addSector = true;
                                      break;
                                  }
                              }
                          } else {
                              addSector = true;
                              break;
                          }
                      }
                  }
              } else {
                  prevSelSectors = new ArrayList<ActivitySector>();
              }
              if (addSector) {
                  prevSelSectors.add(selectedSector);
              }
          }
      }
      return prevSelSectors;
  }

  private boolean saveDocuments(HttpServletRequest request, AmpOrganisation ampOrg) {
      HashSet<String> UUIDs = SelectDocumentDM.getSelectedDocsSet(request, AmpOrganisationDocument.SESSION_NAME, false);
      if (UUIDs == null) {
          return false;
      }

      if (ampOrg.getDocuments() == null) {
          ampOrg.setDocuments(new HashSet<AmpOrganisationDocument>(UUIDs.size()));
      } else {
          ampOrg.getDocuments().clear();
      }
      Iterator<String> iter = UUIDs.iterator();

      while (iter.hasNext()) {
          AmpOrganisationDocument ampOrgDoc = new AmpOrganisationDocument();
          ampOrgDoc.setUuid(iter.next());
          ampOrgDoc.setAmpOrganisation(ampOrg);

          ampOrg.getDocuments().add(ampOrgDoc);
      }
      return true;
  }

  private void putDocumentsInSession(HttpServletRequest request, AmpOrganisation ampOrg) {
      HashSet<String> UUIDs = SelectDocumentDM.getSelectedDocsSet(request, AmpOrganisationDocument.SESSION_NAME, true);
      if (!"true".equals(request.getParameter(CrConstants.REQUEST_UPDATED_DOCUMENTS_IN_SESSION))) {
          UUIDs.clear();
      }
      if (ampOrg != null && ampOrg.getDocuments() != null && ampOrg.getDocuments().size() > 0) {
          Iterator<AmpOrganisationDocument> iter = ampOrg.getDocuments().iterator();
          while (iter.hasNext()) {
              UUIDs.add(iter.next().getUuid());
          }
      }
  }
  
  private void fillOrganizationContactPrimaryField(String[] orgContactIds,AmpOrganisationContact orgContact) {
        if(orgContactIds!=null && orgContactIds.length>0){
            for (int i = 0; i < orgContactIds.length; i++) {
                String idToCompare=orgContact.getContact().getId()!=null ? orgContact.getContact().getId().toString() : orgContact.getContact().getTemporaryId();  
                if(idToCompare.equals(orgContactIds[i])){
                    orgContact.setPrimaryContact(true);
                }else{
                    orgContact.setPrimaryContact(false);
                }
            }
        }else{
            orgContact.setPrimaryContact(false);
        }
    }
  
}
