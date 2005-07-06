/**
 * SelectSector.java
 * 
 * @author Priyajith
 */

package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.util.DbUtil;
import java.util.Collection;

public class SelectSector extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		EditActivityForm eaForm = (EditActivityForm) form;
		
		if (request.getParameter("sectorReset") != null
				&& request.getParameter("sectorReset").equals("false")) {
			eaForm.setSectorReset(false);
		} else {
			eaForm.setSectorReset(true);
			eaForm.reset(mapping, request);
		}		

		if (eaForm.getSectorScheme() == null
				|| eaForm.getSectorScheme().equals(new Long(-1))) {
			// if sector schemes not loaded or reset, load all sector schemes
			// and reset the
			// parent sectors and child sectors.
			Collection secSchemes = DbUtil.getAllSectorSchemes();
			eaForm.setSectorSchemes(secSchemes);
			eaForm.setParentSectors(null);
			eaForm.setChildSectorsLevel1(null);
			eaForm.setChildSectorsLevel2(null);
			eaForm.setSector(new Long(-1));
			eaForm.setSubsectorLevel1(new Long(-1));
			eaForm.setSubsectorLevel2(new Long(-1));			
		} else if (eaForm.getSector() == null
				|| eaForm.getSector().equals(new Long(-1))) {
			// if a sector scheme is selected and the parent sectors of that
			// scheme are not
			// loaded, load all the parent sectors and reset the child sectors.
			Long sectorSchemeId = eaForm.getSectorScheme();
			Collection parentSectors = DbUtil
					.getAllParentSectors(sectorSchemeId);
			eaForm.setParentSectors(parentSectors);
			eaForm.setChildSectorsLevel1(null);
			eaForm.setChildSectorsLevel2(null);
			eaForm.setSector(new Long(-1));
			eaForm.setSubsectorLevel1(new Long(-1));
			eaForm.setSubsectorLevel2(new Long(-1));			
									
		} else if (eaForm.getSubsectorLevel1() == null
				|| eaForm.getSubsectorLevel1().equals(new Long(-1))) {
			// if the sector scheme and corresponding some parent sector is
			// loaded and the subsectors
			// list is not loaded, load them
			Long parSector = eaForm.getSector();
			Collection childSectors = DbUtil.getAllChildSectors(parSector);
			eaForm.setChildSectorsLevel1(childSectors);
			eaForm.setChildSectorsLevel2(null);
			eaForm.setSubsectorLevel1(new Long(-1));
			eaForm.setSubsectorLevel2(new Long(-1));			
		} else if (eaForm.getSubsectorLevel2() == null
				|| eaForm.getSubsectorLevel2().equals(new Long(-1))) {
			// if the sector scheme and corresponding some parent sector is
			// loaded and the subsectors
			// list is not loaded, load them
			Long parSector = eaForm.getSubsectorLevel1();
			Collection childSectors = DbUtil.getAllChildSectors(parSector);
			eaForm.setChildSectorsLevel2(childSectors);
			eaForm.setSubsectorLevel2(new Long(-1));			
		}

		return mapping.findForward("forward");
	}
}
