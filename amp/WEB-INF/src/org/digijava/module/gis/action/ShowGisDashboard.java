package org.digijava.module.gis.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.dgfoundation.amp.ar.ARUtil;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.ar.util.ReportsUtil;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpClassificationConfiguration;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.HierarchyListableUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.util.filters.GroupingElement;
import org.digijava.module.aim.util.filters.HierarchyListableImplementation;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.gis.form.GisDashboardForm;
import org.digijava.module.gis.util.DbUtil;
import org.digijava.module.gis.util.GisUtil;
import org.digijava.module.widget.util.ChartWidgetUtil;

/**
 * GIS Dashboard renderer action.
 * @author Irakli Kobiashvili
 *
 */
public class ShowGisDashboard extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
        GisDashboardForm gisForm = (GisDashboardForm) form;

        
        String baseCurr = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY);
        if (baseCurr == null) {
            baseCurr = "USD";
        }

        if (gisForm.getGisDashboardMode() == null || gisForm.getGisDashboardMode().isEmpty()) {
            gisForm.setGisDashboardMode(GisUtil.GIS_MODE_FUNDINGS);
        }


        AmpCategoryClass categoryClass = CategoryManagerUtil
					.loadAmpCategoryClassByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY);

        if (categoryClass != null) {
            gisForm.setTypeOfAssistanceVals(categoryClass.getPossibleValues());
        }

        //currently we are using base currency but in the future we may use value, selected from currency breakdown.
        gisForm.setSelectedCurrency(baseCurr);

        /*
                List secData = DbUtil.getUsedSectors();

                filterUsedSecData(secData);

                List usedSectors = new ArrayList();
                Iterator it = secData.iterator();
                while (it.hasNext()) {
                    Object[] obj = (Object[])it.next();
                    SectorRefCount src = new SectorRefCount((AmpSector) obj[0], ((Integer)obj[1]).intValue());
                    usedSectors.add(src);
                }

         */
        
        
           Collection sectors = DbUtil.getPrimaryToplevelSectors();
           gisForm.setSectorCollection(sectors);
        
           Collection<LabelValueBean> allDonors = new ArrayList <LabelValueBean>() ; 
		
           List allDbDonors = DbUtil.getFundingDonors();
		
			for (Object donorItem : allDbDonors) {
				Object[] donorNameId = (Object[]) donorItem;
				LabelValueBean donorComboItem = new LabelValueBean((String)donorNameId[0], ((Long)donorNameId[1]).toString());
				allDonors.add(donorComboItem);
			}
			
		gisForm.setAllDonorOrgs(allDonors);

	    gisForm.setCurrencies(CurrencyUtil.getUsableCurrencies());
	    
	    //calendars 
	    Collection<AmpFiscalCalendar> allFisCalenders = org.digijava.module.aim.util.DbUtil.getAllFisCalenders();
	    gisForm.setCalendars(allFisCalenders);
	    
	    ServletContext ampContext = getServlet().getServletContext();
	    
	    List<AmpSector> ampSectors = SectorUtil.getAmpSectorsAndSubSectorsHierarchy(AmpClassificationConfiguration.PRIMARY_CLASSIFICATION_CONFIGURATION_NAME);		
 	 	List<AmpSector> secondaryAmpSectors = SectorUtil.getAmpSectorsAndSubSectorsHierarchy(AmpClassificationConfiguration.SECONDARY_CLASSIFICATION_CONFIGURATION_NAME);
        List<AmpSector> tertiaryAmpSectors = SectorUtil.getAmpSectorsAndSubSectorsHierarchy(AmpClassificationConfiguration.TERTIARY_CLASSIFICATION_CONFIGURATION_NAME);
 	 	
        gisForm.setSectorElements(new ArrayList<GroupingElement<HierarchyListableImplementation>>());
        gisForm.setProgramElements(new ArrayList<GroupingElement<AmpTheme>>()); 	 	
 	 	
 	 	if (FeaturesUtil.isVisibleField("Sector", ampContext)){                
	 	 	HierarchyListableImplementation rootAmpSectors  = new HierarchyListableImplementation();
	 	 	rootAmpSectors.setLabel("Primary Sectors");
	 	 	rootAmpSectors.setUniqueId(0 + "");
	 	 	rootAmpSectors.setChildren(ampSectors);
	 	 	GroupingElement<HierarchyListableImplementation> sectorsElement = new GroupingElement<HierarchyListableImplementation>("Primary Sectors", "filter_sectors_div", rootAmpSectors, "selectedSectors");
	 	 	gisForm.getSectorElements().add(sectorsElement);
	 	 	HierarchyListableUtil.changeTranslateable(sectorsElement.getRootHierarchyListable(), false);
 	 	}
 	 	
 	 	if (FeaturesUtil.isVisibleField("Secondary Sector", ampContext)){
 	 		HierarchyListableImplementation rootSecondaryAmpSectors = new HierarchyListableImplementation();
 	 		rootSecondaryAmpSectors.setLabel("Secondary Sectors");
 	 		rootSecondaryAmpSectors.setUniqueId("0");
 	 		rootSecondaryAmpSectors.setChildren(secondaryAmpSectors);
 	 		GroupingElement<HierarchyListableImplementation> secondarySectorsElement = new GroupingElement<HierarchyListableImplementation>("Secondary Sectors", "filter_secondary_sectors_div", rootSecondaryAmpSectors, "selectedSecondarySectors");
 	 		gisForm.getSectorElements().add(secondarySectorsElement);
 	 		HierarchyListableUtil.changeTranslateable(secondarySectorsElement.getRootHierarchyListable(), false);
 	 	}

        if (FeaturesUtil.isVisibleField("Tertiary Sector", ampContext)){
 	 		HierarchyListableImplementation rootTertiaryAmpSectors = new HierarchyListableImplementation();
 	 		rootTertiaryAmpSectors.setLabel("Tertiary Sector");
 	 		rootTertiaryAmpSectors.setUniqueId("0");
 	 		rootTertiaryAmpSectors.setChildren(tertiaryAmpSectors);
 	 		GroupingElement<HierarchyListableImplementation> tertiarySectorsElement = new GroupingElement<HierarchyListableImplementation>("Tertiary Sectors", "filter_tertiary_sectors_div", rootTertiaryAmpSectors, "selectedTertiarySectors");
 	 		gisForm.getSectorElements().add(tertiarySectorsElement);
 	 		HierarchyListableUtil.changeTranslateable(tertiarySectorsElement.getRootHierarchyListable(), false);
 	 	}
 	 		
 	 	
 	 	
		AmpActivityProgramSettings primaryPrgSetting = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.PRIMARY_PROGRAM);
		AmpTheme primaryProg = null;
//		List<AmpTheme> primaryPrograms;		
		if (primaryPrgSetting!=null && primaryPrgSetting.getDefaultHierarchy() != null) {
			primaryProg= ProgramUtil.getAmpThemesAndSubThemesHierarchy(primaryPrgSetting.getDefaultHierarchy());
			GroupingElement<AmpTheme> primaryProgElement = new GroupingElement<AmpTheme>("Primary Program", "filter_primary_prog_div", primaryProg, "selectedPrimaryPrograms");
			gisForm.getProgramElements().add(primaryProgElement);
		}
		
		AmpTheme secondaryProg = null;
 	 	AmpActivityProgramSettings secondaryPrg = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.SECONDARY_PROGRAM);
// 	 	List<AmpTheme> secondaryPrograms;		
		if (secondaryPrg!=null && secondaryPrg.getDefaultHierarchy() != null) {
			secondaryProg= ProgramUtil.getAmpThemesAndSubThemesHierarchy(secondaryPrg.getDefaultHierarchy());
			GroupingElement<AmpTheme> secondaryProgElement = new GroupingElement<AmpTheme>("Secondary Program", "filter_secondary_prog_div", secondaryProg, "selectedSecondaryPrograms");
			gisForm.getProgramElements().add(secondaryProgElement);
		}
 	 	
		AmpActivityProgramSettings natPlanSetting       = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.NATIONAL_PLAN_OBJECTIVE);
 	 	
//		List<AmpTheme> nationalPlanningObjectives;
 	 	if (natPlanSetting!=null && natPlanSetting.getDefaultHierarchy() != null) {
 	 		AmpTheme nationalPlanningProg                           = ProgramUtil.getAmpThemesAndSubThemesHierarchy(natPlanSetting.getDefaultHierarchy()); 	 	 	
 	 	 	GroupingElement<AmpTheme> natPlanProgElement = new GroupingElement<AmpTheme>("National Planning Objective", "filter_nat_plan_obj_div", nationalPlanningProg, "selectedNatPlanObj"); 	 	
 	 	 	gisForm.getProgramElements().add(natPlanProgElement);
		}
 	 	
 	 	Collection donorTypes = org.digijava.module.aim.util.DbUtil.getAllOrgTypesOfPortfolio();
 	 	Collection<AmpOrgGroup> donorGroups = ARUtil.filterDonorGroups(org.digijava.module.aim.util.DbUtil.getAllOrgGroupsOfPortfolio());
 	 	gisForm.setDonorElements(new ArrayList<GroupingElement<HierarchyListableImplementation>>());
 	 	
 	 	HierarchyListableImplementation rootOrgType = new HierarchyListableImplementation();
 	 	rootOrgType.setLabel("All Donor Types");
 	 	rootOrgType.setUniqueId("0");
 	 	rootOrgType.setChildren( donorTypes );
 	 	GroupingElement<HierarchyListableImplementation> donorTypeElement = new GroupingElement<HierarchyListableImplementation>("Donor Types", "filter_donor_types_div", rootOrgType, "selectedDonorTypes");
 	 	gisForm.getDonorElements().add(donorTypeElement);
 	 	HierarchyListableUtil.changeTranslateable(donorTypeElement.getRootHierarchyListable(), false);
 	 	
 	 	HierarchyListableImplementation rootOrgGroup = new HierarchyListableImplementation();
 	 	rootOrgGroup.setLabel("All Donor Groups");
 	 	rootOrgGroup.setUniqueId("0");
 	 	rootOrgGroup.setChildren( donorGroups );
 	 	GroupingElement<HierarchyListableImplementation> donorGroupElement = new GroupingElement<HierarchyListableImplementation>("Donor Groups", "filter_donor_groups_div", rootOrgGroup, "selectedDonorGroups");
 	 	gisForm.getDonorElements().add(donorGroupElement);
 	 	HierarchyListableUtil.changeTranslateable(donorGroupElement.getRootHierarchyListable(), false);
 	 	
 	 	Collection<AmpOrganisation> donors = ReportsUtil.getAllOrgByRoleOfPortfolio(Constants.ROLE_CODE_DONOR);
 	 	HierarchyListableImplementation rootDonors = new HierarchyListableImplementation();
 	 	rootDonors.setLabel("All Donors");
 	 	rootDonors.setUniqueId("0");
 	 	rootDonors.setChildren( donors );
 	 	GroupingElement<HierarchyListableImplementation> donorsElement  = new GroupingElement<HierarchyListableImplementation>("Donor Agencies", "filter_donor_agencies_div", rootDonors, "selectedDonnorAgency");
 	 	gisForm.getDonorElements().add(donorsElement);
 	 	HierarchyListableUtil.changeTranslateable(donorsElement.getRootHierarchyListable(), false);
	    
        gisForm.setAvailYears(DbUtil.getAvailIndicatorYears());
        //dropdown(on toolbar) things
		Calendar cal=Calendar.getInstance();
		Integer year=new Integer(cal.get(java.util.Calendar.YEAR));  //get current year
		gisForm.setSelectedFromYear(new Integer(year-1).toString());
		gisForm.setSelectedToYear(year.toString());
		//fill from years' drop-down
		String fiscalYear = TranslatorWorker.translateText("FY");
		gisForm.setYearsFrom(ChartWidgetUtil.getYears(true, fiscalYear));
		//fill to years' drop-down
		gisForm.setYearsTo(ChartWidgetUtil.getYears(false, fiscalYear));

		
			
		if (request.getParameter("public")!=null){
			request.getSession().setAttribute("publicuser", true);
		}
        return mapping.findForward("forward");
    }

}
