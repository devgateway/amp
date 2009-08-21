package org.digijava.module.aim.action;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpAhsurveyResponse;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpOrgLocation;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrgStaffInformation;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpOrganisationDocument;
import org.digijava.module.aim.dbentity.AmpPledge;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.AddOrgForm;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.Location;
import org.digijava.module.aim.helper.Pledge;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.ParisUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.action.SelectDocumentDM;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.hibernate.JDBCException;

public class EditOrganisation extends DispatchAction {

    private static Logger logger = Logger.getLogger(EditOrganisation.class);

    private boolean sessionChk(HttpServletRequest request) {
        HttpSession session = request.getSession();
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

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (sessionChk(request)) {
            return mapping.findForward("index");
        }
        return create(mapping, form, request, response);
    }

    public ActionForward create(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
         if (sessionChk(request)) {
            return mapping.findForward("index");
        }
        AddOrgForm editForm = (AddOrgForm) form;
        clean(editForm);
        this.putDocumentsInSession(request, new AmpOrganisation());
        return mapping.findForward("forward");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (sessionChk(request)) {
            return mapping.findForward("index");
        }
        AddOrgForm editForm = (AddOrgForm) form;
        Long orgId = editForm.getAmpOrgId();
        clean(editForm);
        editForm.setAmpOrgId(orgId);
        AmpOrganisation organization = DbUtil.getOrganisation(orgId);
        editForm.setName(organization.getName());
        editForm.setAcronym(organization.getAcronym());
        Long orgTypeId=organization.getOrgTypeId().getAmpOrgTypeId();
        Collection orgGroups = DbUtil.searchForOrganisationGroupByType(orgTypeId);
        if (orgGroups != null) {
            List sortedCol = new ArrayList(orgGroups);
            Collections.sort(sortedCol, new DbUtil.HelperAmpOrgGroupNameComparator());
            editForm.setOrgGroup(sortedCol);
        }
        if (organization.getOrgGrpId() != null) {
            editForm.setAmpOrgGrpId(organization.getOrgGrpId().getAmpOrgGrpId());
        }
        this.putDocumentsInSession(request, organization);
        AmpOrgType orgType = organization.getOrgTypeId();
        editForm.setAmpOrgTypeId(orgType.getAmpOrgTypeId());
        editForm.setOrgUrl(organization.getOrgUrl());
        editForm.setAddress(organization.getAddress());

        // set contacs
        if (organization.getContacts() != null) {
            editForm.setContacts(new ArrayList<AmpContact>(organization.getContacts()));
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
        if (organization.getAmpFiscalCalId() != null) {
            editForm.setFiscalCalId(organization.getAmpFiscalCalId().getAmpFiscalCalId());
        }
        editForm.setType(orgType.getClassification());
        if(organization.getRegion()!=null){
            editForm.setRegionId(organization.getRegion().getId());
        }
        if (orgType.getClassification() != null && orgType.getClassification().equals(Constants.ORG_TYPE_NGO)) {
           
            if (organization.getStaffInfos() != null) {
                editForm.setStaff(new ArrayList(organization.getStaffInfos()));
            }
            editForm.setOrgPrimaryPurpose(organization.getPrimaryPurpose());
            if (organization.getMinPlanRegNumb() != null) {
                editForm.setRegNumbMinPlan(organization.getMinPlanRegNumb().toString());
            }

            editForm.setMinPlanRegDate(FormatHelper.formatDate(organization.getMinPlanRegDate()));
            if (organization.getLegalPersonNum() != null) {
                editForm.setLegalPersonNum(organization.getLegalPersonNum().toString());
            }

            if (organization.getLegalPersonRegDate() != null) {
                editForm.setLegalPersonRegDate(FormatHelper.formatDate(organization.getLegalPersonRegDate()));
            }
            editForm.setRecipients(new ArrayList(organization.getRecipients()));
            if (organization.getCountry() != null) {
                editForm.setCountryId(organization.getCountry().getId());
            }
            if (organization.getTaxNumber() != null) {
                editForm.setTaxNumber(organization.getTaxNumber().toString());
            }
            if (organization.getProgramAnnualBudget() != null) {
                editForm.setProgramAnnualBudget(organization.getProgramAnnualBudget().toString());
            }

            if (organization.getAdminAnnualBudget() != null) {
                editForm.setAdminAnnualBudget(organization.getAdminAnnualBudget().toString());
            }
            if (organization.getProgramAnnualPercent() != null) {
                editForm.setProgramAnnualPercent(organization.getProgramAnnualPercent().toString());
            }

            if (organization.getAdminAnnualBudgetCurr() != null) {
                editForm.setAdminAnnualBudgetCurrId(organization.getAdminAnnualBudgetCurr().getAmpCurrencyId());
            }
            if (organization.getProgramAnnualBudgetCurr() != null) {
                editForm.setProgramAnnualBudgetCurrId(organization.getProgramAnnualBudgetCurr().getAmpCurrencyId());
            }
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
                    loc.setPercent(location.getPercent().floatValue());
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

            // Pledges
            Collection<AmpPledge> funding = organization.getFundingDetails();
            ArrayList<Pledge> fundingDet = new ArrayList<Pledge>();
            Iterator<AmpPledge> it = funding.iterator();
            while (it.hasNext()) {
                AmpPledge e = it.next();
                Pledge fund = new Pledge();
                fund.setAdjustmentType(e.getAdjustmentType().intValue());
                fund.setAmount(String.valueOf(e.getAmount()));
                fund.setCurrencyCode(e.getCurrency().getCurrencyCode());
                fund.setProgram(e.getProgram());
                // AMP-2828 by mouhamad
                String dateFormat = FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.Constants.GLOBALSETTINGS_DATEFORMAT);
                dateFormat = dateFormat.replace("m", "M");

                SimpleDateFormat dz = new SimpleDateFormat(dateFormat);
                String date = "";
                if (e.getDate() != null) {
                    date = dz.format(e.getDate());
                }
                fund.setDate(date);
                fundingDet.add(fund);
            }
            editForm.setFundingDetails(fundingDet);


        }
        return mapping.findForward("forward");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
         if (sessionChk(request)) {
            return mapping.findForward("index");
        }
        AddOrgForm editForm = (AddOrgForm) form;
        ActionErrors errors = new ActionErrors();
        if (DbUtil.isUsed(editForm.getAmpOrgId(), false)) {

            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.aim.organizationManager.deleteOrg"));
            saveErrors(request, errors);
            editForm.setActionFlag("edit");
            return mapping.findForward("forward");
        }

        Collection activities = DbUtil.getAllActivities();
        Collection testFunding = ActivityUtil.getFundingByOrg(editForm.getAmpOrgId());
        Iterator itr1 = activities.iterator();
        boolean flag = false;
        boolean flag2 = false;
        if (!testFunding.isEmpty()) {
            flag2 = true;
        }

        while (itr1.hasNext()) {
            AmpActivity testActivity;
            testActivity = (AmpActivity) itr1.next();

            //Collection testOrgrole = testActivity.getOrgrole();
            Collection testOrgrole = ActivityUtil.getOrgRole(testActivity.getAmpActivityId());
            Iterator itr2 = testOrgrole.iterator();

            while (itr2.hasNext()) {
                AmpOrgRole test = (AmpOrgRole) itr2.next();
                if (test.getOrganisation().getAmpOrgId().equals(editForm.getAmpOrgId())) {
                    flag = true;
                    break;
                }
            }
        }
        if (flag || flag2) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.aim.organizationManager.deleteOrgActError"));
            saveErrors(request, errors);
            editForm.setActionFlag("edit");
            return mapping.findForward("forward");
        } else {

            Collection activitiesCol = DbUtil.getAllOrgActivities(editForm.getAmpOrgId());
            if (activitiesCol.size() > 0) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.aim.organizationManager.deleteOrgActError"));

                saveErrors(request, errors);
                editForm.setActionFlag("edit");
                return mapping.findForward("forward");
            }
            AmpOrganisation org = DbUtil.getOrganisation(editForm.getAmpOrgId());
            if (org.getCalendar() != null && org.getCalendar().size() > 0) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.aim.organizationManager.deleteOrgEventError"));
                saveErrors(request, errors);
                editForm.setActionFlag("edit");
                return mapping.findForward("forward");

            }


            for (Iterator<AmpAhsurvey> it = org.getSurvey().iterator(); it.hasNext();) {
                AmpAhsurvey ahsurvey = it.next();
                org.getSurvey().remove(ahsurvey);
                for (Iterator<AmpAhsurveyResponse> jt = ahsurvey.getResponses().iterator(); jt.hasNext();) {
                    AmpAhsurveyResponse ahsurveyResponse = (AmpAhsurveyResponse) jt.next();
                    ParisUtil.deleteAhResponse(ahsurveyResponse.getAmpReponseId());
                }
                ParisUtil.deleteAhSurvey(ahsurvey.getAmpAHSurveyId());
            }


            //org.setSurvey(null);
            try {
                DbUtil.delete(org);
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

    public ActionForward addPledge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
         if (sessionChk(request)) {
            return mapping.findForward("index");
        }
        AddOrgForm editForm = (AddOrgForm) form;
        Pledge det = new Pledge();
        det.setIndexId(System.currentTimeMillis());
        if (editForm.getFundingDetails() != null) {
            ArrayList<Pledge> list = (ArrayList<Pledge>) editForm.getFundingDetails();
            list.add(det);
            editForm.setFundingDetails(list);
        } else {
            ArrayList<Pledge> newList = new ArrayList<Pledge>();
            newList.add(det);
            editForm.setFundingDetails(newList);
        }

        return mapping.findForward("forward");
    }

    public ActionForward deletePledge(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (sessionChk(request)) {
            return mapping.findForward("index");
        }
        AddOrgForm editForm = (AddOrgForm) form;
        long index = editForm.getTransIndexId();
        ArrayList<Pledge> list = (ArrayList<Pledge>) editForm.getFundingDetails();
        Iterator<Pledge> i = list.iterator();
        while (i.hasNext()) {
            Pledge e = i.next();
            if (e.getIndexId() == index) {
                i.remove();
                break;
            }
        }

        return mapping.findForward("forward");
    }

    public ActionForward addStaffInfo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
         if (sessionChk(request)) {
            return mapping.findForward("index");
        }
        AddOrgForm editForm = (AddOrgForm) form;
        List<AmpOrgStaffInformation> staff = editForm.getStaff();
        if (staff == null) {
            staff = new ArrayList<AmpOrgStaffInformation>();
        }
        AmpOrgStaffInformation info = new AmpOrgStaffInformation();
        info.setYear(Long.parseLong(editForm.getSelectedYear()));
        info.setStaffNumber(Long.parseLong(editForm.getNumberOfStaff()));
        AmpCategoryValue type = CategoryManagerUtil.getAmpCategoryValueFromDb(editForm.getTypeOfStaff());
        info.setType(type);
        long temp = new Date().getTime();
        info.setId(temp);
        info.setNewlyCreated(true);
        staff.add(info);
        editForm.setStaff(staff);
        editForm.setSelectedYear(null);
        editForm.setNumberOfStaff(null);
        editForm.setTypeOfStaff(null);
        return mapping.findForward("forward");
    }

    public ActionForward deleteStaffInfo(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
         if (sessionChk(request)) {
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
        return mapping.findForward("forward");
    }

    public ActionForward addSector(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
         if (sessionChk(request)) {
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
        return mapping.findForward("forward");
    }

    public ActionForward removeSector(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
         if (sessionChk(request)) {
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
        return mapping.findForward("forward");
    }

    public ActionForward removeRecipient(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
         if (sessionChk(request)) {
            return mapping.findForward("index");
        }
        AddOrgForm editForm = (AddOrgForm) form;
        Long[] selRecipients = editForm.getSelRecipients();
        List<AmpOrganisation> oldRecipients = editForm.getRecipients();
        List<AmpOrganisation> removedOrgs = new ArrayList<AmpOrganisation>();
        Iterator<AmpOrganisation> orgIter = oldRecipients.iterator();
        while (orgIter.hasNext()) {
            AmpOrganisation organization = orgIter.next();
            for (int i = 0; i < selRecipients.length; i++) {
                if (organization.getAmpOrgId().equals(selRecipients[i])) {
                    removedOrgs.add(organization);
                    break;
                }

            }
        }
        oldRecipients.removeAll(removedOrgs);
        editForm.setRecipients(oldRecipients);
        editForm.setSelRecipients(null);
        return mapping.findForward("forward");
    }

    public ActionForward deleteLocation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
         if (sessionChk(request)) {
            return mapping.findForward("index");
        }
        AddOrgForm editForm = (AddOrgForm) form;
        Long[] selLocations = editForm.getSelLocs();
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
        return mapping.findForward("forward");
    }

    public ActionForward deleteContact(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
         if (sessionChk(request)) {
            return mapping.findForward("index");
        }
        AddOrgForm editForm = (AddOrgForm) form;
        Long selContactId = editForm.getSelContactId();
        List<AmpContact> oldContacts = editForm.getContacts();
        Iterator<AmpContact> contactIter = oldContacts.iterator();
        AmpContact contact = null;
        while (contactIter.hasNext()) {
            contact = contactIter.next();
            if (contact.getId().equals(selContactId)) {
                break;
            }

        }

        oldContacts.remove(contact);
        editForm.setContacts(oldContacts);
        editForm.setSelContactId(null);

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
        editForm.setOrgGroup(DbUtil.searchForOrganisationGroupByType(orgTypeId));
        AmpOrgType orgType = DbUtil.getOrgType(orgTypeId);
        editForm.setType(orgType.getClassification());
        if (orgType.getClassification() != null && orgType.getClassification().equals(Constants.ORG_TYPE_NGO)) {
            editForm.setDacOrgCode(null);
            editForm.setOrgIsoCode(null);
            editForm.setBudgetOrgCode(null);
            editForm.setFundingDetails(null);
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
            editForm.setCountryId(null);
            editForm.setTaxNumber(null);
            editForm.setProgramAnnualBudget(null);
            editForm.setAdminAnnualBudget(null);
            editForm.setProgramAnnualPercent(null);
            editForm.setAdminAnnualBudgetCurrId(null);
            editForm.setProgramAnnualBudgetCurrId(null);
            editForm.setAddressAbroad(null);
            editForm.setSelectedLocs(null);
            if(orgType.getClassification() == null ||!orgType.getClassification().equals(Constants.ORG_TYPE_REGIONAL)){
                editForm.setRegionId(null);
            }
        }
        return mapping.findForward("forward");

    }

    public ActionForward save(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
         if (sessionChk(request)) {
            return mapping.findForward("index");
        }
        AddOrgForm editForm = (AddOrgForm) form;
        AmpOrganisation organization = null;
        Long orgId = editForm.getAmpOrgId();
        String action="create";
        boolean exist = false;
        AmpOrganisation org=DbUtil.getOrganisationByName(editForm.getName());
        if (orgId == null || orgId.equals(0l)) {
            organization = new AmpOrganisation();
            if(org!=null){
                 exist = true;
            }
        } else {
            organization = DbUtil.getOrganisation(orgId);
             action="edit";
            if(org!=null&&!organization.getName().equals(editForm.getName())){
                exist=true;
            }
        }

        ActionErrors errors = new ActionErrors();
        if (exist) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.aim.organizationManager.saveOrgNameError"));
            saveErrors(request, errors);
            return mapping.findForward("forward");
        }

         Collection orgsCol = DbUtil.getOrgByCode(action, editForm.getOrgCode(),
                editForm.getAmpOrgId());
        if (!orgsCol.isEmpty()){ // To check for duplicate org-code
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.aim.organizationManager.saveOrgCodeError"));
            saveErrors(request, errors);
            return mapping.findForward("forward");
        }
        organization.setName(editForm.getName());
        organization.setAcronym(editForm.getAcronym());
        organization.setAddress(editForm.getAddress());
        organization.setAddressAbroad(editForm.getAddressAbroad());
        if (editForm.getAdminAnnualBudget()!=null&&!editForm.getAdminAnnualBudget().equals("")) {
            organization.setAdminAnnualBudget(Double.parseDouble(editForm.getAdminAnnualBudget()));
        } else {
            organization.setAdminAnnualBudget(null);
        }

        if (editForm.getAdminAnnualBudgetCurrId() != null && !editForm.getAdminAnnualBudgetCurrId().equals(-1l)) {
            organization.setAdminAnnualBudgetCurr(CurrencyUtil.getAmpcurrency(editForm.getAdminAnnualBudgetCurrId()));
        } else {
            organization.setAdminAnnualBudgetCurr(null);
        }
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

        if (editForm.getLegalPersonNum()!=null&&!editForm.getLegalPersonNum().equals("")) {
            organization.setLegalPersonNum(editForm.getLegalPersonNum());

        } else {
            organization.setLegalPersonNum(null);
        }
        if (editForm.getRegionId()!=null&&!editForm.getRegionId().equals(-1l)) {
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
        if(editForm.getRegNumbMinPlan()!=null&&!editForm.getRegNumbMinPlan().equals("")){
            organization.setMinPlanRegNumb(editForm.getRegNumbMinPlan());
        }
        else{
             organization.setMinPlanRegNumb(null);
        }
        
        organization.setDescription(editForm.getDescription());
        organization.setOrgGrpId(DbUtil.getAmpOrgGroup(editForm.getAmpOrgGrpId()));
        organization.setOrgIsoCode(editForm.getOrgIsoCode());
        organization.setOrgCode(editForm.getOrgCode());
        organization.setOrgTypeId(DbUtil.getAmpOrgType(editForm.getAmpOrgTypeId()));
        organization.setOrgUrl(editForm.getOrgUrl());
        organization.setPrimaryPurpose(editForm.getOrgPrimaryPurpose());
        if(editForm.getProgramAnnualBudget()!=null&&!editForm.getProgramAnnualBudget().equals("")){
            organization.setProgramAnnualPercent(Double.parseDouble(editForm.getProgramAnnualPercent()));
        }
        else{
            organization.setProgramAnnualPercent(null);
        }

        if(editForm.getProgramAnnualBudget()!=null&&!editForm.getProgramAnnualBudget().equals("")){
             organization.setProgramAnnualBudget(Double.parseDouble(editForm.getProgramAnnualBudget()));
        }
        else{
            organization.setProgramAnnualBudget(null);
        }
        if (editForm.getImplemLocationLevel() != null && editForm.getImplemLocationLevel() != 0) {
            organization.setImplemLocationLevel(CategoryManagerUtil.getAmpCategoryValueFromDb(editForm.getImplemLocationLevel()));
        } else {
            organization.setImplemLocationLevel(null);
        }
        if (editForm.getProgramAnnualBudgetCurrId() != null && !editForm.getProgramAnnualBudgetCurrId().equals(-1l)) {
            organization.setProgramAnnualBudgetCurr(CurrencyUtil.getAmpcurrency(editForm.getProgramAnnualBudgetCurrId()));
        } else {
            organization.setProgramAnnualBudgetCurr(null);
        }
        if(editForm.getTaxNumber()!=null&&!editForm.getTaxNumber().equals("")){
             organization.setTaxNumber(Long.parseLong(editForm.getTaxNumber()));
        }

        // contacts
        if (organization.getContacts() == null) {
            organization.setContacts(new HashSet<AmpContact>());
        } else {
            organization.getContacts().clear();
        }
        if (editForm.getContacts() != null) {
            organization.getContacts().addAll(editForm.getContacts());
        }

        // recipients
        if (organization.getRecipients() == null) {
            organization.setRecipients(new HashSet<AmpOrganisation>());
        } else {
            organization.getRecipients().clear();
        }
        if (editForm.getRecipients() != null) {
            organization.getRecipients().addAll(editForm.getRecipients());
        }

        // staff information
        if (organization.getStaffInfos() == null) {
            organization.setStaffInfos(new HashSet<AmpOrgStaffInformation>());
        }

        if (editForm.getStaff() != null) {
            Iterator<AmpOrgStaffInformation> infoIter = editForm.getStaff().iterator();
            Set<AmpOrgStaffInformation> infos = new HashSet<AmpOrgStaffInformation>();
            Set<AmpOrgStaffInformation> infosToRetain = new HashSet<AmpOrgStaffInformation>();
            while (infoIter.hasNext()) {
                AmpOrgStaffInformation info = infoIter.next();
                if (!info.isNewlyCreated()) {
                    AmpOrgStaffInformation newInfo = (AmpOrgStaffInformation) DbUtil.get(AmpOrgStaffInformation.class, info.getId());
                    infosToRetain.add(newInfo);
                    continue;
                }
                AmpOrgStaffInformation newInfo = new AmpOrgStaffInformation();
                newInfo.setOrganization(organization);
                newInfo.setStaffNumber(info.getStaffNumber());
                newInfo.setType(info.getType());
                newInfo.setYear(info.getYear());


                infos.add(newInfo);
            }
            organization.getStaffInfos().retainAll(infosToRetain);
            organization.getStaffInfos().addAll(infos);
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
                        orgLoc.setPercent(Float.valueOf(location.getPercent()).doubleValue());
                        newLoc = orgLoc;
                        locations.add(newLoc);
                        break;
                    }
                }
                if (newLoc == null) {
                    newLoc = new AmpOrgLocation();
                }
                newLoc.setOrganization(organization);
                newLoc.setPercent(Float.valueOf(location.getPercent()).doubleValue());
                newLoc.setLocation(location.getAmpCVLocation());
                locations.add(newLoc);
            }

        }


        organization.getLocations().clear();
        organization.getLocations().addAll(locations);


        // pledges
        Set<AmpPledge> ampPledges = new HashSet<AmpPledge>();
        if (editForm.getFundingDetails() != null) {
            Iterator<Pledge> itr = editForm.getFundingDetails().iterator();
            try {
                while (itr.hasNext()) {
                    Pledge el = itr.next();
                    AmpPledge pledge = new AmpPledge();
                    pledge.setAdjustmentType(new Integer(el.getAdjustmentType()));
                    pledge.setAmount(new BigDecimal(el.getAmount()));
                    AmpCurrency c = CurrencyUtil.getCurrencyByCode(el.getCurrencyCode());
                    pledge.setCurrency(c);
                    if (el.getProgram() == null || el.getProgram().equals("")) {
                        throw new Exception();
                    }
                    pledge.setProgram(el.getProgram());
                    String date = el.getDate();
                    ////System.out.println(d.toString());
                    pledge.setDate(FormatHelper.parseDate2(date));

                    ampPledges.add(pledge);
                }

            } catch (Exception ex) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.aim.organizationManager.saveOrgPledgeError"));
                saveErrors(request, errors);
                return mapping.findForward("forward");

            }


        }

        if (organization.getFundingDetails() == null) {
            organization.setFundingDetails(new HashSet<AmpPledge>());
        } else {
            organization.getFundingDetails().clear();
        }
        if (editForm.getFundingDetails() != null) {
            organization.getFundingDetails().addAll(ampPledges);
        }

        this.saveDocuments(request, organization);
        if (orgId == null || (orgId.equals(0l))) {
            DbUtil.add(organization);
        } else {
            DbUtil.updateOrg(organization);
        }

        return mapping.findForward("added");

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
            form.setAmpOrgTypeId(null);
            form.setBudgetOrgCode(null);
            form.setContactPersonName(null);
            form.setContactPersonTitle(null);
            form.setCountryId(null);
            form.setDacOrgCode(null);
            form.setDescription(null);
            form.setEmail(null);
            form.setFax(null);
            form.setFiscalCalId(null);
            form.setFlag(null);
            form.setFundingDetails(null);
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
            form.setPhone(null);
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
            form.setAdminAnnualBudget(null);
            form.setAdminAnnualBudgetCurrId(null);
            form.setImplemLocationLevel(null);
            form.setLegalPersonNum(null);
            form.setLegalPersonRegDate(null);
            form.setProgramAnnualBudget(null);
            form.setParentLocId(null);
            form.setProgramAnnualBudgetCurrId(null);
            form.setProgramAnnualPercent(null);
            form.setMinPlanRegDate(null);
            form.setRegNumbMinPlan(null);
            form.setCountryId(null);
            form.setRegionId(null);
            form.setType(null);
            form.setTaxNumber(null);
            Collection col = DbUtil.getAllOrgTypes();
            if (col != null) {
                List sortedCol = new ArrayList(col);
                Collections.sort(sortedCol, new DbUtil.HelperAmpOrgTypeNameComparator());
                form.setOrgType(sortedCol);
            }
            form.setOrgGroup(null);
            form.setSectorScheme(SectorUtil.getAllSectorSchemes());
            form.setFiscalCal(DbUtil.getAllFisCalenders());
            form.setCurrencies(CurrencyUtil.getAmpCurrency());
            Set<AmpCategoryValueLocations> countryLocations = DynLocationManagerUtil.getLocationsByLayer(CategoryConstants.IMPLEMENTATION_LOCATION_COUNTRY);
            form.setCountries(countryLocations);
            form.setYears(getYearsBeanList());
            form.setContacts(null);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            new DgException(ex);
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
}
