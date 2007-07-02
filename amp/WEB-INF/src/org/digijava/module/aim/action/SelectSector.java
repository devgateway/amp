/**
 * SelectSector.java
 * 
 * @author Priyajith
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.FeatureTemplates;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.SectorUtil;

public class SelectSector extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		SelectSectorForm ssForm = (SelectSectorForm) form;
		
		if (request.getParameter("sectorReset") != null
				&& request.getParameter("sectorReset").equals("false")) {
			ssForm.setSectorReset(false);
		} else {
			ssForm.setSectorReset(true);
			ssForm.reset(mapping, request);
		}
		
		if (ssForm.getSectorScheme() == null
				|| ssForm.getSectorScheme().equals(new Long(-1))) {
			// if sector schemes not loaded or reset, load all sector schemes
			// and reset the
			// parent sectors and child sectors.
			
			String globalSettingValue = FeaturesUtil.getGlobalSettingValue(Constants.GLOBAL_DEFAULT_SECTOR_SCHEME);
			
			Collection secSchemes = SectorUtil.getAllSectorSchemes();
			ssForm.setSectorSchemes(secSchemes);
			ssForm.setSectorScheme(new Long(globalSettingValue));
			Collection parentSectors = SectorUtil
			.getAllParentSectors(ssForm.getSectorScheme());
			ssForm.setParentSectors(parentSectors);
			ssForm.setChildSectorsLevel1(null);
			ssForm.setChildSectorsLevel2(null);
			ssForm.setSector(new Long(-1));
			ssForm.setSubsectorLevel1(new Long(-1));
			ssForm.setSubsectorLevel2(new Long(-1));			
		} else if (ssForm.getSector() == null
				|| ssForm.getSector().equals(new Long(-1))) {
			// if a sector scheme is selected and the parent sectors of that
			// scheme are not
			// loaded, load all the parent sectors and reset the child sectors.
			Long sectorSchemeId = ssForm.getSectorScheme();
			Collection parentSectors = SectorUtil
					.getAllParentSectors(sectorSchemeId);
			ssForm.setParentSectors(parentSectors);
			ssForm.setChildSectorsLevel1(null);
			ssForm.setChildSectorsLevel2(null);
			ssForm.setSector(new Long(-1));
			ssForm.setSubsectorLevel1(new Long(-1));
			ssForm.setSubsectorLevel2(new Long(-1));			
									
		} else if (ssForm.getSubsectorLevel1() == null
				|| ssForm.getSubsectorLevel1().equals(new Long(-1))) {
			// if the sector scheme and corresponding some parent sector is
			// loaded and the subsectors
			// list is not loaded, load them
			Long parSector = ssForm.getSector();
			Collection childSectors = SectorUtil.getAllChildSectors(parSector);
			Collection parentSectors = SectorUtil
			.getAllParentSectors(ssForm.getSectorScheme());
			ssForm.setParentSectors(parentSectors);
			ssForm.setChildSectorsLevel1(childSectors);
			ssForm.setChildSectorsLevel2(null);
			ssForm.setSubsectorLevel1(new Long(-1));
			ssForm.setSubsectorLevel2(new Long(-1));			
		} else if (ssForm.getSubsectorLevel2() == null
				|| ssForm.getSubsectorLevel2().equals(new Long(-1))) {
			// if the sector scheme and corresponding some parent sector is
			// loaded and the subsectors
			// list is not loaded, load them
			Long parSector = ssForm.getSubsectorLevel1();
			Collection childSectors = SectorUtil.getAllChildSectors(parSector);
			ssForm.setChildSectorsLevel2(childSectors);
			ssForm.setSubsectorLevel2(new Long(-1));			
		}
		
		if (request.getParameter("addButton") != null){
			if (ssForm.getSector().equals(new Long(-1)))
				request.setAttribute("errSector", "true");
			else{
				request.setAttribute("addButton", "true");
				HttpSession session = request.getSession();
				
				
				Long sector = ssForm.getSector();
				Long subsectorLevel1 = ssForm.getSubsectorLevel1();
				Long subsectorLevel2 = ssForm.getSubsectorLevel2();

				Collection newSectors = new ArrayList();


				ActivitySector actSect = new ActivitySector();
				actSect.setSectorId(sector);
				AmpSector sec = SectorUtil.getAmpSector(actSect.getSectorId());
				actSect.setSectorName(sec.getName());
				actSect.setSubsectorLevel1Id(subsectorLevel1);
				actSect.setSubsectorLevel2Id(subsectorLevel2);
				/*
				if (actSect.getSectorName().equalsIgnoreCase("MULTISECTOR/CROSS-CUTTING")) {
					actSect.setSubsectorLevel1Id(null);
					actSect.setSubsectorLevel2Id(null);
				} else {
					actSect.setSubsectorLevel1Id(subsectorLevel1);
					actSect.setSubsectorLevel2Id(subsectorLevel2);
				}*/

				if (subsectorLevel2 != null && (!subsectorLevel2.equals(new Long(-1)))) {
					actSect.setId(subsectorLevel2);
				} else if (subsectorLevel1 != null
						&& (!subsectorLevel1.equals(new Long(-1)))) {
					actSect.setId(subsectorLevel1);
				} else {
					actSect.setId(sector);
				}

				boolean flag = false;

				if (!flag && actSect.getSectorId() != null
						&& (!(actSect.getSectorId().equals(new Long(-1))))) {
					if (actSect.getSubsectorLevel1Id() != null
							&& (!(actSect.getSubsectorLevel1Id().equals(new Long(-1))))) {
						sec = SectorUtil.getAmpSector(actSect.getSubsectorLevel1Id());
						actSect.setSubsectorLevel1Name(sec.getName());
					}
					if (actSect.getSubsectorLevel2Id() != null
							&& (!(actSect.getSubsectorLevel2Id().equals(new Long(-1))))) {
						sec = SectorUtil.getAmpSector(actSect.getSubsectorLevel2Id());
						actSect.setSubsectorLevel2Name(sec.getName());
					}

					newSectors.add(actSect);
				}

				
				session.setAttribute("sectorSelected", actSect);
			}
			
		}
		
		return mapping.findForward("forward");
	}
}
