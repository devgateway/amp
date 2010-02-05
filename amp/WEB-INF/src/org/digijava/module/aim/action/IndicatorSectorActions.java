package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.form.IndicatorForm;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.util.SectorUtil;

public class IndicatorSectorActions extends DispatchAction {
	
	public ActionForward addsectorToindicator(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
		
		IndicatorForm indForm=(IndicatorForm)form;
		Long sector = indForm.getSector();
		

		Collection<ActivitySector> prevSelSectors = indForm.getSelectedSectorsForInd();
		Collection<ActivitySector> newSectors = new ArrayList<ActivitySector>();

		if (prevSelSectors != null) {
			newSectors.addAll(prevSelSectors);
		}

		ActivitySector actSect = new ActivitySector();
		actSect.setSectorId(sector);
		AmpSector sec = SectorUtil.getAmpSector(actSect.getSectorId());
		actSect.setSectorName(sec.getName());
		
		if(!newSectors.contains(actSect)){
			newSectors.add(actSect);
		}
		indForm.setShowAddInd(true);
//		boolean flag = false;
//
//		if (prevSelSectors != null) {
//			Iterator<ActivitySector> itr = prevSelSectors.iterator();
//			while (itr.hasNext()) {
//				ActivitySector temp = (ActivitySector) itr.next();
//				if (temp.equals(actSect)) {
//					flag = true;
//					break;
//				}
//			}
//		}
//
//		if (!flag && actSect.getSectorId() != null
//				&& (!(actSect.getSectorId().equals(new Long(-1))))) {
//			if (actSect.getSubsectorLevel1Id() != null
//					&& (!(actSect.getSubsectorLevel1Id().equals(new Long(-1))))) {
//				sec = SectorUtil.getAmpSector(actSect.getSubsectorLevel1Id());
//				actSect.setSubsectorLevel1Name(sec.getName());
//			}
//			if (actSect.getSubsectorLevel2Id() != null
//					&& (!(actSect.getSubsectorLevel2Id().equals(new Long(-1))))) {
//				sec = SectorUtil.getAmpSector(actSect.getSubsectorLevel2Id());
//				actSect.setSubsectorLevel2Name(sec.getName());
//			}
//
//            if(prevSelSectors==null){
//                actSect.setSectorPercentage(new Float(100));
//            }else if(prevSelSectors.size()==0){
//                    actSect.setSectorPercentage(new Float(100));
//            }
//			newSectors.add(actSect);
//		}


		indForm.setSelectedSectorsForInd(null);
		indForm.setSelectedSectorsForInd(newSectors);

		
		return mapping.findForward("backToPage");
	}
	
	public ActionForward loadSectors(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
		
		IndicatorForm indForm=(IndicatorForm)form;
		if(request.getParameter("sectorReset")!=null && request.getParameter("sectorReset").equals("true")){
			indForm.resetsector();
		}
		if(indForm.getSectorScheme()==null || indForm.getSectorScheme().equals(new Long(-1))){
			Long primaryConfigClassId=SectorUtil.getPrimaryConfigClassificationId();
			//getting scheme
			indForm.setSectorSchemes(new ArrayList<AmpSectorScheme>());
			indForm.getSectorSchemes().add(SectorUtil.getAmpSectorScheme(primaryConfigClassId));
			//getting parent sectors
			indForm.setSectorScheme(primaryConfigClassId);
			indForm.setAllSectors(SectorUtil.getAllParentSectors(indForm.getSectorScheme()));
			indForm.setSector(new Long(-1));
		}else if(indForm.getSector()==null || indForm.getSector().equals(new Long(-1))){			
			Long sectorSchemeId = indForm.getSectorScheme();
			indForm.setAllSectors(SectorUtil.getAllParentSectors(sectorSchemeId));					
			indForm.setSector(new Long(-1));
		}		
		return mapping.findForward("forward");
	}
	
	public ActionForward removeSelectedSectors(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
		
		IndicatorForm indForm=(IndicatorForm)form;
		
		Long selSectors[] = indForm.getSelActivitySector();
		Collection<ActivitySector> prevSelSectors = indForm.getSelectedSectorsForInd();
		Collection<ActivitySector> newSectors = new ArrayList<ActivitySector>();

		Iterator<ActivitySector> itr = prevSelSectors.iterator();

        boolean flag =false;

		while (itr.hasNext()) {
			ActivitySector asec = (ActivitySector) itr.next();
            flag=false;
			for (int i = 0; i < selSectors.length; i++) {
				if (asec.getSectorId().equals(selSectors[i]) || asec.getSubsectorLevel1Id().equals(selSectors[i]) 
						|| asec.getSubsectorLevel2Id().equals(selSectors[i])) {
					flag=true;
                    break;
				}
			}

            if(!flag){
                newSectors.add(asec);
            }
		}
		
		indForm.setSelectedSectorsForInd(newSectors);
		indForm.setShowAddInd(true);
		
		return mapping.findForward("backToPage");
	}
	
	public ActionForward justSubmit(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
		
		IndicatorForm indForm=(IndicatorForm)form;
		indForm.setShowAddInd(true);
		return mapping.findForward("backToPage");
	}
	
}
