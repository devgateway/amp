package org.digijava.module.aim.action;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.form.IndicatorForm;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.SectorUtil;

public class AddNewIndicatorTL extends Action {
	private static Logger logger = Logger.getLogger(AddNewIndicatorTL.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		IndicatorForm indForm = (IndicatorForm) form;
		AmpIndicator indicator = new AmpIndicator();
        indicator.setName(indForm.getIndicatorName());
        indicator.setDescription(indForm.getIndicatorDesc());
        indicator.setCreationDate(new Date());
        indicator.setCode(indForm.getIndicatorCode());
        indicator.setType((indForm.getAscendingInd()+"").trim());  
            if (indForm.getSelectedSectorsForInd() != null && indForm.getSelectedSectorsForInd().size() > 0) {
                indicator.setSectors(new HashSet<AmpSector>());
                for (Iterator sectorIt = indForm.getSelectedSectorsForInd().iterator(); sectorIt.hasNext();) {
                    ActivitySector actSector = (ActivitySector) sectorIt.next();
                    AmpSector sector = SectorUtil.getAmpSector(actSector.getSectorId());
                    indicator.getSectors().add(sector);
                }
            }
      
            if (!IndicatorUtil.validateIndicatorName(indicator)) {
                IndicatorUtil.saveIndicator(indicator);

            } else {
                ActionMessages errors = new ActionMessages();
                errors.add("title", new ActionMessage("error.aim.addIndicator.duplicateName"));
                saveErrors(request, errors);
            }

     	
		
//		AmpMEIndicators ampMEIndnew = null;
//		if (indForm.getIndicatorName().trim() != null
//				&& indForm.getIndicatorCode().trim() != null) {
//			ampMEIndnew = new AmpMEIndicators();
//			ampMEIndnew.setName(indForm.getIndicatorName());
//			ampMEIndnew.setCode(indForm.getIndicatorCode());
//			if (ampMEIndnew.getDescription() != null &&
//					ampMEIndnew.getDescription().length() > 0)
//				ampMEIndnew.setDescription(indForm.getIndicatorDesc());
//			else
//				ampMEIndnew.setDescription(" ");
//			
//			if (indForm.getAscendingInd() == 'A')
//				ampMEIndnew.setAscendingInd(true);
//			else
//				ampMEIndnew.setAscendingInd(false);
//			
//			ampMEIndnew.setDefaultInd(false);
//
//			MEIndicatorsUtil.saveMEIndicator(ampMEIndnew, indForm
//					.getActivityId(), false);
//		}
        
        //clean form fields
        indForm.setIndicatorName(null);
        indForm.setIndicatorCode(null);
        indForm.setIndicatorDesc(null);
        indForm.setSelectedSectorsForInd(null);
        
        Collection indicators=IndicatorUtil.getAllNonDefaultIndicators();
        indForm.setNondefaultindicators(indicators);
        indForm.setAllSectors(SectorUtil.getAllParentSectors());
        
		return mapping.findForward("toAdditionOfIndiForStep9");
	}
}