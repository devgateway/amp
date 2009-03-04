package org.digijava.module.aim.action;

import java.text.ParseException;
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
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpAhsurvey;
import org.digijava.module.aim.dbentity.AmpAhsurveyResponse;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpOrganisationDocument;
import org.digijava.module.aim.dbentity.AmpPledge;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.form.AddOrgForm;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.Pledge;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.ParisUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.contentrepository.action.SelectDocumentDM;
import org.digijava.module.contentrepository.helper.CrConstants;
import org.hibernate.JDBCException;

public class EditOrganisation
    extends Action {

  private static Logger logger = Logger.getLogger(EditOrganisation.class);

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws java.lang.
      Exception {

    HttpSession session = request.getSession();
    if (session.getAttribute("ampAdmin") == null) {
      return mapping.findForward("index");
    }
    else {
      String str = (String) session.getAttribute("ampAdmin");
      if (str.equals("no")) {
        return mapping.findForward("index");
      }
    }

    logger.debug("In edit organisation action");

    AddOrgForm editForm = (AddOrgForm) form;
    //editForm.setSectors(null);
    // Add Pledge
    if (request.getParameter("addPledge") != null) {
      Pledge det = new Pledge();
      det.setIndexId(System.currentTimeMillis());
      if (editForm.getFundingDetails() != null) {
        ArrayList<Pledge> list = (ArrayList<Pledge>) editForm.getFundingDetails();
        list.add(det);
        editForm.setFundingDetails(list);
      }
      else {
        ArrayList<Pledge> newList = new ArrayList<Pledge>();
        newList.add(det);
        editForm.setFundingDetails(newList);
      }
      return mapping.findForward("forward");
    }
    else
    if (request.getParameter("delPledge") != null) {
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
    if (editForm.getCurrencies() == null) {
      editForm.setCurrencies(CurrencyUtil.getAmpCurrency());
    }
    //
    // Add sectors
    if (request.getParameter("addSector") != null) {
   	  Object searchedsector = session.getAttribute("add");
   	  Collection<ActivitySector> prevSelSectors = editForm.getSectors();
      if (searchedsector != null && searchedsector.equals("true")) {
    	Collection selectedSecto = (Collection) session.getAttribute("sectorSelected");
    	
   		if (selectedSecto != null) {
			Iterator<ActivitySector> itre = selectedSecto.iterator();
			while (itre.hasNext()) {
				ActivitySector selectedSector = (ActivitySector) itre.next();

				boolean addSector = true;
				if (prevSelSectors != null) {
					Iterator<ActivitySector> itr = prevSelSectors
					.iterator();
			      while (itr.hasNext()) {
				    ActivitySector sect = (ActivitySector) itr.next();
		            if (selectedSector.getSectorId().equals(sect.getSectorId())) {
		            	addSector = false;
		                break;
		            }
			      }  
				}
				if(addSector){
	              if (prevSelSectors != null) {
	                Iterator iter = prevSelSectors.iterator();
	                boolean firstSecForConfig = true;
                    while (iter.hasNext()) {
                      ActivitySector actSect = (ActivitySector) iter.next();
                      if (actSect.getSectorId().equals(selectedSector.getSectorId())) {
                        if(actSect.getSectorPercentage() != null && actSect.getSectorPercentage()==100f){
                          actSect.setSectorPercentage(0f);
                        }	
                        firstSecForConfig = false;
                        break;
                      }
                    }
                    if (firstSecForConfig) {
	                  selectedSector.setSectorPercentage(100f);
	                }
	                prevSelSectors.add(selectedSector);
	              } 
	              else {
	                selectedSector.setSectorPercentage(new Float(100));
	                prevSelSectors = new ArrayList<ActivitySector> ();
	                prevSelSectors.add(selectedSector);
	              }
               }
			   editForm.setSectors(prevSelSectors);	
			}
   		}
		session.removeAttribute("sectorSelected");
		session.removeAttribute("add");
      }
      else{
        ActivitySector sect = (ActivitySector) session.getAttribute(
          "sectorSelected");
        session.removeAttribute("sectorSelected");
        //Collection<ActivitySector> prevSelSectors = editForm.getSectors();
        if (prevSelSectors != null) {
          if (prevSelSectors.isEmpty())
            sect.setSectorPercentage(new Float(100));
          Iterator<ActivitySector> i = prevSelSectors.iterator();
          boolean ok = true;
          while (i.hasNext()) {
            ActivitySector e = i.next();
            if (e.getSectorId().equals(sect.getSectorId())) {
              ok = false;
              break;
            }
          }
          if (ok)
            prevSelSectors.add(sect);
        }
        else {
          sect.setSectorPercentage(new Float(100));
          prevSelSectors = new ArrayList<ActivitySector>();
          prevSelSectors.add(sect);
        }
        editForm.setSectors(prevSelSectors);
      }
      return mapping.findForward("forward");
    }
    // Remove sectors
    else
    if (request.getParameter("remSectors") != null) {

      Long selSectors[] = editForm.getSelSectors();
      Collection<ActivitySector> prevSelSectors = editForm.getSectors();
      Collection<ActivitySector> newSectors = new ArrayList<ActivitySector>();

      Iterator<ActivitySector> itr = prevSelSectors.iterator();

      boolean flag = false;

      while (itr.hasNext()) {
        ActivitySector asec = itr.next();
        flag = false;
        for (int i = 0; i < selSectors.length; i++) {
          if (asec.getSectorId().equals(selSectors[i])) {
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
    //

    String action = request.getParameter("actionFlag");
    logger.debug("action : " + action);
    editForm.setActionFlag(action);

    if (null == editForm.getFiscalCal() || editForm.getFiscalCal().size() < 1)
      editForm.setFiscalCal(DbUtil.getAllFisCalenders());
    if (null == editForm.getSectorScheme() ||
        editForm.getSectorScheme().size() < 1)
      editForm.setSectorScheme(SectorUtil.getAllSectorSchemes());
    if (null == editForm.getRegion() || editForm.getRegion().size() < 1) {
      // set country
      String defaultIso = FeaturesUtil.getDefaultCountryIso();
      editForm.setRegion(LocationUtil.
                         getAllRegionsUnderCountry(defaultIso
                         ));
    }

    if (null == editForm.getOrgType() || editForm.getOrgType().size() < 1) {
      Collection col = DbUtil.getAllOrgTypes();
      if (col != null) {
        List sortedCol = new ArrayList(col);
        Collections.sort(sortedCol, new DbUtil.HelperAmpOrgTypeNameComparator());
        editForm.setOrgType(sortedCol);
      }
    }
    if (null == editForm.getSectorScheme() ||
        editForm.getSectorScheme().size() < 1)
      editForm.setSectorScheme(SectorUtil.getAllSectorSchemes());
    String orgGrpAdded = request.getParameter("orgGroupAdded");
    if (null == editForm.getOrgGroupColl() ||
        editForm.getOrgGroupColl().size() < 1 || "true".equals(orgGrpAdded)) {
      Collection col = DbUtil.getAllOrgGroups();
      if (col != null) {
        List sortedCol = new ArrayList(col);
        Collections.sort(sortedCol, new DbUtil.HelperAmpOrgGroupNameComparator());
        editForm.setOrgGroupColl(sortedCol);
      }
    }
    Collection orgGroup = new ArrayList();
    Collection oldOrgGroup = editForm.getOrgGroup();
    editForm.setOrgGroup(orgGroup);

    //Collection country = DbUtil.getAllCountries();
    //Collection level = DbUtil.getAllLevels();
    //editForm.setCountry(country);
    //editForm.setLevel(level);

    String otype = editForm.getOrgTypeFlag();
    if (!editForm.getSaveFlag().equals("yes")) {
      if (otype != null && otype.trim().length() != 0) {
        if ("regional".equals(otype))
          editForm.setRegionFlag("show"); // Setting style property for showing region drop-down
        else
          editForm.setRegionFlag("hide"); // Setting style property for hiding region drop-down
        /*
                 if ("multilateral".equals(otype)) {
         Long id = null;
         Iterator itr = level.iterator();
         while (itr.hasNext()) {
          AmpLevel al = (AmpLevel) itr.next();
          if (al.getName().toLowerCase().equals(otype)) {
           id = al.getAmpLevelId();
           break;
          }
         }
         orgGroup = DbUtil.getAllOrgGroupByType(id);
         editForm.setOrgGroup(orgGroup);
         if ("edit".equals(action))
          editForm.setFlag("delete");
                 }*/ 
        
//        orgGroup = DbUtil.getAllOrgGroupByType(editForm.getAmpOrgTypeId());
//        editForm.setOrgGroup(orgGroup);
        Iterator itr = editForm.getOrgGroupColl().iterator();
        while (itr.hasNext()) {
          AmpOrgGroup og = (AmpOrgGroup) itr.next();
          if (og.getOrgType().getAmpOrgTypeId().equals(editForm.getAmpOrgTypeId()))
            editForm.getOrgGroup().add(og);
        }
        if ("edit".equals(action))
          editForm.setFlag("delete");
        return mapping.findForward("forward");
      }
      /*
       String regionFlag = editForm.getRegionFlag();
       if (regionFlag != null && "changed".equals(regionFlag)) {
        editForm.setRegionFlag("");
        if ("edit".equals(action))
         editForm.setFlag("delete");
        return mapping.findForward("forward");
       }
             }*/
    }

    if ("create".equals(action) || "edit".equals(action)) {
      AmpOrganisation ampOrg = new AmpOrganisation();
      String oldOrgName = null;
      if ("edit".equals(action)) {
        editForm.setAmpOrgId(new Long(Integer.parseInt(request.getParameter(
            "ampOrgId"))));        
        ampOrg = DbUtil.getOrganisation(editForm.getAmpOrgId());        
        oldOrgName = ampOrg.getName();
        
        if (ampOrg == null) {
          if (session.getAttribute("ampOrg") != null) {
            session.removeAttribute("ampOrg");
          }
          return mapping.findForward("added");
        }
        editForm.setFlag("delete");
      }

      if (editForm.getName() == null) {
        if ("create".equals(action)) {
          logger.debug("Inside IF [CREATE]");
          /*
                    Iterator itr = level.iterator();
                    while (itr.hasNext()) {
           AmpLevel al = (AmpLevel) itr.next();
           if (al.getName().toLowerCase().equals("regional")) {
            editForm.setLevelId(al.getAmpLevelId());
            editForm.setLevelFlag("regional");
            break;
           }
                    }*/
          editForm.setRegionFlag("hide"); // Setting style property for hiding region drop-down
          return mapping.findForward("forward");
        }

        if (ampOrg.getName() != null) {
          editForm.setName(ampOrg.getName());
        }
        else
          editForm.setName("");
        if (ampOrg.getAcronym() != null) {
          editForm.setAcronym(ampOrg.getAcronym());
        }
        else
          editForm.setAcronym("");
        if (ampOrg.getDescription() != null) {
          editForm.setDescription(ampOrg.getDescription());
        }
        else
          editForm.setDescription("");
        if (ampOrg.getDacOrgCode() != null) {
          editForm.setDacOrgCode(ampOrg.getDacOrgCode());
        }
        else
          editForm.setDacOrgCode("");
        
        if (ampOrg.getOrgCode() != null) {
          editForm.setOrgCode(ampOrg.getOrgCode());
        }
        else
          editForm.setOrgCode("");
        
        if (ampOrg.getBudgetOrgCode() != null) {
            editForm.setBudgetOrgCode(ampOrg.getBudgetOrgCode());
          }
          else
            editForm.setBudgetOrgCode("");
        
        if (ampOrg.getOrgIsoCode() != null) {
          editForm.setOrgIsoCode(ampOrg.getOrgIsoCode());
        }
        else
          editForm.setOrgIsoCode("");

        if (ampOrg.getContactPersonName() != null)
          editForm.setContactPersonName(ampOrg.getContactPersonName());
        else
          editForm.setContactPersonName("");
        if (ampOrg.getContactPersonTitle() != null)
          editForm.setContactPersonTitle(ampOrg.getContactPersonTitle());
        else
          editForm.setContactPersonTitle("");
        if (ampOrg.getAddress() != null)
          editForm.setAddress(ampOrg.getAddress());
        else
          editForm.setAddress("");
        if (ampOrg.getEmail() != null)
          editForm.setEmail(ampOrg.getEmail());
        else
          editForm.setEmail("");
        if (ampOrg.getPhone() != null)
          editForm.setPhone(ampOrg.getPhone());
        else
          editForm.setPhone("");
        if (ampOrg.getFax() != null)
          editForm.setFax(ampOrg.getFax());
        else
          editForm.setFax("");
        if (ampOrg.getOrgUrl() != null)
          editForm.setOrgUrl(ampOrg.getOrgUrl());
        else
          editForm.setOrgUrl("");

        if (ampOrg.getAmpFiscalCalId() != null) {
          editForm.setFiscalCalId(ampOrg.getAmpFiscalCalId().getAmpFiscalCalId());
        }
        else
          editForm.setFiscalCalId(new Long( -1));
        if (ampOrg.getAmpSecSchemeId() != null) {
          editForm.setAmpSecSchemeId(ampOrg.getAmpSecSchemeId().
                                     getAmpSecSchemeId());
        }
        else
          editForm.setAmpSecSchemeId(new Long( -1));
        editForm.setRegionFlag("hide"); // Setting style property for hiding region drop-down
        if (ampOrg.getOrgTypeId() != null) {
          editForm.setAmpOrgTypeId(ampOrg.getOrgTypeId().getAmpOrgTypeId());
          /*
                    if (ampOrg.getLevelId() != null)
           orgGroup = DbUtil.getAllOrgGroupByType(ampOrg.getLevelId().getAmpLevelId());
                    else
           orgGroup = DbUtil.getAllOrgGroupByType(null);
           editForm.setOrgGroup(orgGroup); */
          if (ampOrg.getOrgTypeId().getOrgType().equals("Ethiopian Government")
              || ampOrg.getOrgTypeId().getOrgType().equals("National NGO"))
            editForm.setOrgTypeFlag("national");
          else if (ampOrg.getOrgTypeId().getOrgType().equals(
              "Regional Government")) {
            editForm.setOrgTypeFlag("regional");
            editForm.setRegionFlag("show"); // Setting style property for showing region drop-down
          }
          else if (ampOrg.getOrgTypeId().getOrgType().equals("Multilateral")) {
            editForm.setOrgTypeFlag("multilateral");
            /* Long id = null;
                        Iterator itr = level.iterator();
                        while (itr.hasNext()) {
             AmpLevel al = (AmpLevel) itr.next();
             if (al.getName().toLowerCase().equals(otype)) {
              id = al.getAmpLevelId();
              break;
             }
                        }
             orgGroup = DbUtil.getAllOrgGroupByType(editForm.getAmpOrgTypeId());
                        editForm.setOrgGroup(orgGroup); */
          }
          else
            editForm.setOrgTypeFlag("others");
          //orgGroup = DbUtil.getAllOrgGroupByType(editForm.getAmpOrgTypeId());
          //editForm.setOrgGroup(orgGroup);
          Iterator itr = editForm.getOrgGroupColl().iterator();
          while (itr.hasNext()) {
            AmpOrgGroup og = (AmpOrgGroup) itr.next();
            if (og.getOrgType().getAmpOrgTypeId().equals(editForm.
                getAmpOrgTypeId()))
              editForm.getOrgGroup().add(og);
          }
          
          this.putDocumentsInSession(request, ampOrg);
        }
        else
          editForm.setAmpOrgTypeId(new Long( -1));
        if (ampOrg.getOrgGrpId() != null) {
          editForm.setAmpOrgGrpId(ampOrg.getOrgGrpId().getAmpOrgGrpId());
        }
        else
          editForm.setAmpOrgGrpId(new Long( -1));
        /*
                 if (ampOrg.getCountryId() != null) {
         editForm.setCountryId(ampOrg.getCountryId().getIso());
                 }
                 else
         editForm.setCountryId("et"); */
        if (ampOrg.getRegionId() != null) {
          editForm.setRegionId(ampOrg.getRegionId().getAmpRegionId());
        }
        else
          editForm.setRegionId(new Long( -1));
        /*
                 if (ampOrg.getLevelId() != null) {
         editForm.setLevelId(ampOrg.getLevelId().getAmpLevelId());
         editForm.setLevelFlag("others");
         Iterator itr = level.iterator();
          while (itr.hasNext()) {
           AmpLevel al = (AmpLevel) itr.next();
           if (al.getAmpLevelId().equals(editForm.getLevelId())) {
            if (al.getName().toLowerCase().equals("regional")) {
             editForm.setLevelFlag("regional");
             break;
            }
           }
          }
                 }
                 else
         editForm.setLevelId(new Long(-1)); 	*/
        //Sectors
        ArrayList<ActivitySector> convSect = new ArrayList<ActivitySector>();
        Collection sectors = ampOrg.getSectors();
        Iterator i = sectors.iterator();
        boolean first = true;
        while (i.hasNext()) {

          //AmpActivitySector ampActSect=(AmpActivitySector) i.next();
          AmpSector sec = (AmpSector) i.next();
          ActivitySector actSect = new ActivitySector();
          //actSect.setSectorPercentage(ampActSect.getSectorPercentage());
          if (first) {
            first = false;
            actSect.setSectorPercentage(new Float(100));
          }
          else
            actSect.setSectorPercentage(new Float(0));

          if (sec.getParentSectorId() == null) {
            actSect.setSectorName(sec.getName());
          }
          else if (sec.getParentSectorId().getParentSectorId() == null) {
            actSect.setSectorName(sec.getParentSectorId().getName());
            actSect.setSubsectorLevel1Name(sec.getName());
          }
          else {
            actSect.setSectorName(sec.getParentSectorId().getParentSectorId().
                                  getName());
            actSect.setSubsectorLevel1Name(sec.getParentSectorId().getName());
            actSect.setSubsectorLevel2Name(sec.getName());
          }
          actSect.setSectorId(sec.getAmpSectorId());
          convSect.add(actSect);
        }

        editForm.setSectors(convSect);
        //
        //Pledges
        Collection<AmpPledge> funding = ampOrg.getFundingDetails();
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
          if (e.getDate() != null)
            date = dz.format(e.getDate());
          fund.setDate(date);
          fundingDet.add(fund);
        }
        editForm.setFundingDetails(fundingDet);
        //
        return mapping.findForward("forward");
      }
      else {
        if (session.getAttribute("ampOrg") != null) {
          session.removeAttribute("ampOrg");
        }

        if (editForm.getName() != null || !editForm.getName().trim().equals(""))
          ampOrg.setName(editForm.getName());
        else
          ampOrg.setName(null);
        if (editForm.getAcronym() != null ||
            !editForm.getAcronym().trim().equals(""))
          ampOrg.setAcronym(editForm.getAcronym());
        else
          ampOrg.setAcronym(null);
        if (editForm.getDacOrgCode() != null ||
            !editForm.getDacOrgCode().trim().equals(""))
          ampOrg.setDacOrgCode(editForm.getDacOrgCode());
        else
          ampOrg.setDacOrgCode(null);
        if (editForm.getOrgCode() != null ||
            !editForm.getOrgCode().trim().equals("")) {
          Collection org = DbUtil.getOrgByCode(action, editForm.getOrgCode(),
                                               editForm.getAmpOrgId());
          if (org.isEmpty()) // To check for duplicate org-code
            ampOrg.setOrgCode(editForm.getOrgCode());
          else {
            editForm.setFlag("orgCodeExist");
            return mapping.findForward("forward");
          }
        }
        else
          ampOrg.setOrgCode(null);
        
        //budget organization code
        if (editForm.getBudgetOrgCode() != null ||
                !editForm.getBudgetOrgCode().trim().equals(""))
              ampOrg.setBudgetOrgCode(editForm.getBudgetOrgCode());
            else
              ampOrg.setBudgetOrgCode(null);
        
        if (editForm.getOrgIsoCode() != null ||
            !editForm.getOrgIsoCode().trim().equals(""))
          ampOrg.setOrgIsoCode(editForm.getOrgIsoCode());
        else
          ampOrg.setOrgIsoCode(null);

        if (editForm.getDescription().trim().equals("") ||
            editForm.getDescription() == null) {
          ampOrg.setDescription(" ");
        }
        else
          ampOrg.setDescription(editForm.getDescription());

        if (editForm.getContactPersonName() != null ||
            !editForm.getContactPersonName().trim().equals(""))
          ampOrg.setContactPersonName(editForm.getContactPersonName());
        else
          ampOrg.setContactPersonName(null);
        if (editForm.getContactPersonTitle() != null ||
            !editForm.getContactPersonTitle().trim().equals(""))
          ampOrg.setContactPersonTitle(editForm.getContactPersonTitle());
        else
          ampOrg.setContactPersonTitle(null);
        if (editForm.getAddress() != null ||
            !editForm.getAddress().trim().equals(""))
          ampOrg.setAddress(editForm.getAddress());
        else
          ampOrg.setAddress(null);
        if (editForm.getEmail() != null ||
            !editForm.getEmail().trim().equals(""))
          ampOrg.setEmail(editForm.getEmail());
        else
          ampOrg.setEmail(null);
        if (editForm.getPhone() != null ||
            !editForm.getPhone().trim().equals(""))
          ampOrg.setPhone(editForm.getPhone());
        else
          ampOrg.setPhone(null);
        if (editForm.getFax() != null || !editForm.getFax().trim().equals(""))
          ampOrg.setFax(editForm.getFax());
        else
          ampOrg.setFax(null);
        if (editForm.getOrgUrl() != null ||
            !editForm.getOrgUrl().trim().equals(""))
          ampOrg.setOrgUrl(editForm.getOrgUrl());
        else
          ampOrg.setOrgUrl(null);

        if (!editForm.getFiscalCalId().equals(new Long( -1))) {
          AmpFiscalCalendar fiscal = DbUtil.getAmpFiscalCalendar(editForm.
              getFiscalCalId());
          if (fiscal != null) {
            ampOrg.setAmpFiscalCalId(fiscal);
          }
        }
        else
          ampOrg.setAmpFiscalCalId(null);

        if (!editForm.getAmpSecSchemeId().equals(new Long( -1))) {
          AmpSectorScheme sector = SectorUtil.getAmpSectorScheme(editForm.
              getAmpSecSchemeId());
          if (sector != null) {
				//changed in Burkina - error when trying to delete some sectors..
				//  ampOrg.setAmpSecSchemeId(sector);
				//  ampOrg.setAmpSecSchemeId(null);
				//Changed it back to make it work
				ampOrg.setAmpSecSchemeId(sector);
          }
        }
        else
          ampOrg.setAmpSecSchemeId(null);

        if (!editForm.getAmpOrgTypeId().equals(new Long( -1))) {
          AmpOrgType ampOrgType = DbUtil.getAmpOrgType(editForm.getAmpOrgTypeId());
          if (ampOrgType != null)
            ampOrg.setOrgTypeId(ampOrgType);
        }
        else
          ampOrg.setOrgTypeId(null);

        if (editForm.getAmpOrgGrpId().equals(new Long( -1))) {
          ampOrg.setOrgGrpId(null);
        }
        else {
          AmpOrgGroup ampOgp = DbUtil.getAmpOrgGroup(editForm.getAmpOrgGrpId());
          if (ampOgp != null)
            ampOrg.setOrgGrpId(ampOgp);
        }

        String oflag = editForm.getOrgTypeFlag();
        /*
                if (oflag.equals("national") || oflag.equals("regional")) {
         //Country cntry = DbUtil.getDgCountry(editForm.getCountryId());
         if (cntry != null)
          ampOrg.setCountryId(cntry);
                }
                else
         ampOrg.setCountryId(null); */
        if (editForm.getRegionId().equals(new Long( -1)) ||
            !oflag.equals("regional")) {
          ampOrg.setRegionId(null);
        }
        else {
          AmpRegion reg = LocationUtil.getAmpRegion(editForm.getRegionId());
          if (reg != null)
            ampOrg.setRegionId(reg);
        }
        /*
         if (editForm.getAmpOrgTypeId().equals(new Long(-1)) || !oflag.equals("national")
          || !oflag.equals("regional")) {
         ampOrg.setLevelId(null);
                }
                else {
         AmpLevel lvl = null;
         AmpLevel al = new AmpLevel();
         Iterator itr = level.iterator();
         while (itr.hasNext()) {
          al = (AmpLevel) itr.next();
          if (al.getName().toLowerCase().equals(editForm.getOrgTypeFlag()))
           lvl = DbUtil.getAmpLevel(al.getAmpLevelId());
         }
         if (lvl != null)
          ampOrg.setLevelId(lvl);
                } */

        //Sectors
        Set<AmpSector> sectors = new HashSet<AmpSector>();
        if (editForm.getSectors() != null) {
          Iterator<ActivitySector> itr = editForm.getSectors().iterator();
          while (itr.hasNext()) {
            ActivitySector actSect = itr.next();
            Long sectorId = null;
            if (actSect.getSubsectorLevel2Id() != null
                && (!actSect.getSubsectorLevel2Id().equals(new Long( -1)))) {
              sectorId = actSect.getSubsectorLevel2Id();
            }
            else if (actSect.getSubsectorLevel1Id() != null
                     && (!actSect.getSubsectorLevel1Id().equals(new Long( -1)))) {
              sectorId = actSect.getSubsectorLevel1Id();
            }
            else {
              sectorId = actSect.getSectorId();
            }
            //AmpActivitySector amps = new AmpActivitySector();
            //amps.setActivityId(activity);
            AmpSector amps = null;
            if (sectorId != null && (!sectorId.equals(new Long( -1))))
              amps = SectorUtil.getAmpSector(sectorId);

            sectors.add(amps);
          }
        }
        ampOrg.setSectors(sectors);
        //
        //Pledges
        Set<AmpPledge> ampPledges = new HashSet<AmpPledge>();
        if (editForm.getFundingDetails() != null) {
          Iterator<Pledge> itr = editForm.getFundingDetails().iterator();
          //verify if all fields were completed
          boolean forw = false;
          while (itr.hasNext()) {
            Pledge e = itr.next();
            try {
              double val = Double.parseDouble(e.getAmount());
              if (val == 0)
                forw = true;
              if ( (e.getProgram() == null) || ("".equals(e.getProgram())))
                forw = true;
              Date d = new Date();
              SimpleDateFormat dz = new SimpleDateFormat(Constants.
                  CALENDAR_DATE_FORMAT);
              d = dz.parse(e.getDate());
            }
            catch (Exception ex) {
              if ( (ex instanceof ParseException) ||
                  (ex instanceof NumberFormatException))
                forw = true;
              else {
                logger.debug(ex);
                ex.printStackTrace();
              }
            }
            if (forw)
              break;
          }
          if (forw) {
    	    editForm.setOrgGroup(oldOrgGroup);
            editForm.setFlag("completePledges");
            return mapping.findForward("forward");
          }
          //
          itr = editForm.getFundingDetails().iterator();
          while (itr.hasNext()) {
            Pledge el = itr.next();
            AmpPledge pledge = new AmpPledge();
            pledge.setAdjustmentType(new Integer(el.getAdjustmentType()));
            pledge.setAmount(Double.valueOf(el.getAmount()));
            AmpCurrency c = CurrencyUtil.getCurrencyByCode(el.getCurrencyCode());
            pledge.setCurrency(c);
            pledge.setProgram(el.getProgram());
            String date = el.getDate();
            ////System.out.println(date);
            if (! ("".equals(date))) {
              Date d = new Date();
              SimpleDateFormat dz = new SimpleDateFormat(Constants.
                  CALENDAR_DATE_FORMAT);
              d = dz.parse(date);
              ////System.out.println(d.toString());
              pledge.setDate(d);
            }
            ampPledges.add(pledge);
          }
          if (ampOrg.getFundingDetails() != null)
            ampOrg.getFundingDetails().clear();
          else
            ampOrg.setFundingDetails(new HashSet());

          ampOrg.getFundingDetails().addAll(ampPledges);
          
          this.saveDocuments(request, ampOrg);
        }
          	
        //boolean exist= DbUtil.getOrganisationByName(ampOrg.getName()) != null;
       boolean exist = false;
       if ("create".equals(action) ){
    	   if (DbUtil.getOrganisationByName(ampOrg.getName())!=null){
    		   exist = true;
    	   }
       }
       if ("edit".equals(action)&& !oldOrgName.equals(ampOrg.getName())){
    	   exist= oldOrgName.equalsIgnoreCase(editForm.getName());
       }
        
        if (exist) {
		  editForm.setFlag("orgNameExist");
          return mapping.findForward("forward");
        }
        if ("create".equals(action)) {
          DbUtil.add(ampOrg);
        }
        else {
          DbUtil.updateOrg(ampOrg);
        }

        logger.debug("Organisation added");
        return mapping.findForward("added");
      }
    }
    else if ("delete".equals(action)) {

        if (DbUtil.isUsed(editForm.getAmpOrgId(), false)) {
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.aim.organizationGroupManager.deleteOrgGroup"));
            saveErrors(request, errors);
            editForm.setActionFlag("edit");
            return mapping.findForward("forward");
        }

      Collection activities = DbUtil.getAllActivities();
      Collection testFunding = ActivityUtil.getFundingByOrg(editForm.
          getAmpOrgId());
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
        Collection testOrgrole = ActivityUtil.getOrgRole(testActivity.
            getAmpActivityId());
        Iterator itr2 = testOrgrole.iterator();

        while (itr2.hasNext()) {
          AmpOrgRole test = (AmpOrgRole) itr2.next();
          if (test.getOrganisation().getAmpOrgId().equals(editForm.getAmpOrgId())) {
            flag = true;
            break;
          }
        }
      }
      if (flag) {
        editForm.setFlag("orgReferences");
        editForm.setActionFlag("edit");
        return mapping.findForward("forward");
      }
      else
      if (flag2) {
        editForm.setFlag("fundReferences");
        editForm.setActionFlag("edit");
        return mapping.findForward("forward");
      }
      else {
        if (session.getAttribute("ampOrg") != null) {
          session.removeAttribute("ampOrg");
        }
        Collection activitiesCol = DbUtil.getAllOrgActivities(editForm.
            getAmpOrgId());
        AmpOrganisation org = DbUtil.getOrganisation(editForm.getAmpOrgId());
        Iterator actItr = activitiesCol.iterator();
        while (actItr.hasNext()) {
          AmpActivity act = (AmpActivity) actItr.next();
          DbUtil.update(act);
        }
        
        for(Iterator<AmpAhsurvey> it= org.getSurvey().iterator();it.hasNext();){
        	AmpAhsurvey ahsurvey=it.next();
        	org.getSurvey().remove(ahsurvey); 
        	for (Iterator<AmpAhsurveyResponse> jt = ahsurvey.getResponses().iterator(); jt.hasNext();) {
        		AmpAhsurveyResponse ahsurveyResponse = (AmpAhsurveyResponse) jt.next();
        		ParisUtil.deleteAhResponse(ahsurveyResponse.getAmpReponseId());
			}
        	ParisUtil.deleteAhSurvey(ahsurvey.getAmpAHSurveyId());
        }
        
        //org.setSurvey(null);
        try{
        	DbUtil.delete(org);
        }catch(JDBCException e){
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
    return mapping.findForward("index");
  }
  
  private boolean saveDocuments(HttpServletRequest request, AmpOrganisation ampOrg) {
	  HashSet<String> UUIDs = SelectDocumentDM.getSelectedDocsSet(request, AmpOrganisationDocument.SESSION_NAME, false);
	  if (UUIDs == null)
		  return false;
	  
	  if (ampOrg.getDocuments() == null) {
		  ampOrg.setDocuments( new HashSet<AmpOrganisationDocument>(UUIDs.size()) );
	  }
	  else
		  ampOrg.getDocuments().clear();
	  Iterator<String> iter	= UUIDs.iterator();
	  
	  while ( iter.hasNext() ) {
		  AmpOrganisationDocument ampOrgDoc	= new AmpOrganisationDocument();
		  ampOrgDoc.setUuid(iter.next());
		  ampOrgDoc.setAmpOrganisation(ampOrg);
		  
		  ampOrg.getDocuments().add(ampOrgDoc);
	  }
	  return true;
  }
  
  private void putDocumentsInSession(HttpServletRequest request, AmpOrganisation ampOrg) {
	  HashSet<String> UUIDs		= SelectDocumentDM.getSelectedDocsSet(request, AmpOrganisationDocument.SESSION_NAME, true);
	  if ( !"true".equals( request.getParameter(CrConstants.REQUEST_UPDATED_DOCUMENTS_IN_SESSION) ) )
		  	UUIDs.clear();
	  if (ampOrg!=null && ampOrg.getDocuments()!=null && ampOrg.getDocuments().size()>0 ) {
		  Iterator<AmpOrganisationDocument> iter	= ampOrg.getDocuments().iterator();
		  while ( iter.hasNext() ) {
			  UUIDs.add( iter.next().getUuid() );
		  }
	  }
		  
  }
  
}
