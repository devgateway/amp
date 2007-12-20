package org.digijava.module.aim.action;

import org.apache.struts.action.*;
import org.digijava.module.aim.form.NewIndicatorForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.aim.helper.AllPrgIndicators;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.helper.ActivitySector;
import org.digijava.module.aim.helper.AllMEIndicators;
import org.digijava.module.aim.helper.AllActivities;
import org.digijava.module.aim.helper.AmpIndSectors;
import org.digijava.module.aim.helper.IndicatorsBean;

import java.util.HashSet;
import java.util.List;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import org.digijava.module.aim.dbentity.AmpMEIndicators;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.helper.AmpPrgIndicator;
import java.util.*;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.apache.struts.util.LabelValueBean;

public class ViewEditIndicator
    extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                             HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
    	
        NewIndicatorForm existIndForm = (NewIndicatorForm) form;

        String action=request.getParameter("action");
        if(action!=null &&
           action.equalsIgnoreCase("save") &&
           existIndForm.getName() != null &&
           existIndForm.getCode() != null){
            switch(existIndForm.getIndType()) {
                case 0: {
//                    if(existIndForm.getSelectedProgramId() == null) {
//                        return mapping.findForward("forward");
//                    }

                    AmpPrgIndicator newInd = new AmpPrgIndicator();
                    newInd.setIndicatorId(existIndForm.getId());
                    newInd.setCategory(existIndForm.getCategory());
                    newInd.setCode(existIndForm.getCode());
                    newInd.setCreationDate(existIndForm.getDate());
                    newInd.setDescription(existIndForm.getDescription());
                    newInd.setName(existIndForm.getName());
                    newInd.setType(existIndForm.getType());
                    newInd.setSector(existIndForm.getSelActivitySector());
                    newInd.setIndSectores(existIndForm.getActivitySectors());
                    existIndForm.setSelectedProgramId(new Long(1));

                    ProgramUtil.saveThemeIndicators(newInd, existIndForm.getSelectedProgramId());

                    existIndForm.reset();

                    break;
                }
                case 1: {

                    AllMEIndicators newInd = new AllMEIndicators();

					newInd.setAmpMEIndId(existIndForm.getId());
                    newInd.setCode(existIndForm.getCode());
                    newInd.setName(existIndForm.getName());

                    MEIndicatorsUtil.saveIndicator(newInd);
                    existIndForm.reset();

                    break;
                }
                case 2: {
                    if(existIndForm.getSelectedActivityId() == null) {
                        return mapping.findForward("forward");
                    }

                    AmpMEIndicators newInd = new AmpMEIndicators();

                    newInd.setAmpMEIndId(existIndForm.getId());
                    newInd.setCode(existIndForm.getCode());
                    newInd.setDescription(existIndForm.getDescription());
                    newInd.setName(existIndForm.getName());
                    MEIndicatorsUtil.updateMEIndicator(newInd);

                    existIndForm.reset();

                    break;
                }
            }
        }

        String indId=request.getParameter("id");
        String indType=request.getParameter("type");
        String parentId=request.getParameter("parentid");

        if(indId==null || indType==null){
            return mapping.findForward("forward");
        }
        if(indType.equalsIgnoreCase("program")){
        	existIndForm.reset();
            AllPrgIndicators ind=ProgramUtil.getThemeIndicator(Long.valueOf(indId));
            if(ind!=null){

            	existIndForm.setCode(ind.getCode());
            	existIndForm.setDate(ind.getCreationDate());
            	existIndForm.setDescription(ind.getDescription());
            	existIndForm.setName(ind.getName());
            	existIndForm.setType(ind.getType());
            	existIndForm.setCategory(ind.getCategory());
            	existIndForm.setId(ind.getIndicatorId());
            	existIndForm.setIndType(0);
            	
            	  Collection indSector=ind.getSector();
            	  Collection Sectors = new ArrayList();
  				if(indSector != null){
  			
  					for(Iterator sectItr=indSector.iterator();sectItr.hasNext();){
  						
  						AmpIndSectors sect=(AmpIndSectors)sectItr.next();
  						ActivitySector actsec = new ActivitySector();
  						
  							actsec.setSectorId(sect.getSectorId().getAmpSectorId());
		  					actsec.setSectorName(sect.getSectorId().getName());
		  					actsec.setSubsectorLevel1Id(new Long(-1));
		  		            		
  		          		  Sectors.add(actsec);
  		         
  						}
  					}
  			   
  			
            	existIndForm.setActivitySectors(Sectors);            	

                if(ind.getThemes() != null) {
                    Collection<LabelValueBean> prgCol=new ArrayList<LabelValueBean>();
                    for(Iterator prgIter = ind.getThemes().iterator(); prgIter.hasNext(); ) {
                        AmpTheme prg = (AmpTheme) prgIter.next();

                        String prgName = null;
                        if(prg.getName().length() > 35) {
                            prgName = prg.getName().substring(0, 32) + "...";
                        } else {
                            prgName = prg.getName();
                        }

                        LabelValueBean lvb=new LabelValueBean(prgName,prg.getAmpThemeId().toString());
                        prgCol.add(lvb);
                        existIndForm.setSelectedProgramId(prg.getAmpThemeId());
                    }
                    existIndForm.setSelectedPrograms(prgCol);
                }
            }
            return mapping.findForward("forward");
        }else if(indType.equalsIgnoreCase("project")){
        	existIndForm.reset();
            Collection<AllActivities> actIdsCol = MEIndicatorsUtil.getAllActivityIds();
            if(actIdsCol != null) {
                for(Iterator itr = actIdsCol.iterator(); itr.hasNext(); ) {
                    AllActivities act = (AllActivities) itr.next();
                    Collection<AllMEIndicators> prjIndsCol = act.getAllMEIndicators();
                    if(prjIndsCol != null) {
                        List<AllMEIndicators> prjIndsList = new ArrayList<AllMEIndicators>(prjIndsCol);
                        for(Iterator indItr = prjIndsList.iterator(); indItr.hasNext(); ) {
                            AllMEIndicators tInd = (AllMEIndicators) indItr.next();
                            if(tInd.getAmpMEIndId().equals(Long.valueOf(indId))){
                            	existIndForm.setCode(tInd.getCode());
                            	existIndForm.setName(tInd.getName());
                            	existIndForm.setId(tInd.getAmpMEIndId());
                            	existIndForm.setIndType(2);

                                Collection<LabelValueBean> actCol=new ArrayList<LabelValueBean>();
                                String actName=null;
                                if(act.getActivityName().length()>35){
                                    actName=act.getActivityName().substring(0,32)+"...";
                                }else{
                                    actName=act.getActivityName();
                                }
                                LabelValueBean lvb=new LabelValueBean(actName,act.getActivityId().toString());
                                actCol.add(lvb);
                                existIndForm.setSelectedActivities(actCol);

                                return mapping.findForward("forward");
                            }
                        }
                    }
                }
            }
            return mapping.findForward("forward");
        }else if(indType.equalsIgnoreCase("global")){
            existIndForm.reset();
            AllMEIndicators ind =MEIndicatorsUtil.getMEIndicator(Long.valueOf(indId));
            if(ind!=null){
                existIndForm.setCode(ind.getCode());
                existIndForm.setId(ind.getAmpMEIndId());
                existIndForm.setName(ind.getName());
                existIndForm.setIndType(1);
            }
            return mapping.findForward("forward");
        }

        return mapping.findForward("forward");
    }
    public ViewEditIndicator() {
    }
}
