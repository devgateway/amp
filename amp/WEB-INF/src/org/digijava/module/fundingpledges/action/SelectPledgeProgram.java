package org.digijava.module.fundingpledges.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivityProgram;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpLocation;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.EditActivityForm.Location;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesLocation;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesProgram;
import org.digijava.module.fundingpledges.form.PledgeForm;

public class SelectPledgeProgram extends Action {

	private static Logger logger = Logger.getLogger(SelectPledgeProgram.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		PledgeForm pledgeform = (PledgeForm) form;
	    List<AmpTheme> prl = getParents();
	    List<List<AmpTheme>> prLevels = new ArrayList<>();
	    String selectedThemeId = request.getParameter("themeid");
	    String opStatus = request.getParameter("op");
	    String strLevel = request.getParameter("selPrgLevel");
	    
	    AmpActivityProgramSettings parent = ProgramUtil.getAmpActivityProgramSettings(ProgramUtil.NATIONAL_PLAN_OBJECTIVE);
	    if (selectedThemeId == null && opStatus == null && strLevel == null) {

	      if (parent == null || parent.getDefaultHierarchy() == null) {
	        prLevels.add(prl);
	      }
	      else {
	        List<AmpTheme> defaultHierarchy = ProgramUtil.getSubThemes(parent.getDefaultHierarchyId());
	        prLevels.add(defaultHierarchy);
	      }
	      pledgeform.setProgramLevels(prLevels);
	      pledgeform.setSelPrograms(null);
	      return mapping.findForward("forward");
	    }

	    if (selectedThemeId == null) {
	      if (pledgeform.getProgramLevels() != null) {
	        prLevels = pledgeform.getProgramLevels();
	      }
	      if (prLevels.size() == 0) {
	        prLevels.add(prl);
	        pledgeform.setProgramLevels(prLevels);
	      }
	      return mapping.findForward("forward");
	    }
	    else if (selectedThemeId.equals("-1")) {
	      if (strLevel != null) {
	        Long level = Long.valueOf(strLevel);
	        prLevels = pledgeform.getProgramLevels();
	        int sz = prLevels.size();
	        for (int i = level.intValue(); i < sz; i++) {
	          prLevels.remove(level.intValue());
	        }
	      }
	      if (opStatus != null) {
	        return mapping.findForward("added");
	      }
	      else {
	        return mapping.findForward("forward");
	      }
	    }
	    else if (selectedThemeId.equals("")) {
	      if (opStatus != null) {
	        return mapping.findForward("added");
	      }
	      else {
	        return mapping.findForward("forward");
	      }
	    }

	    if (opStatus != null) {
	      if (opStatus.equals("add")) {
	    	  Collection<FundingPledgesProgram> fpps = pledgeform.getSelectedProgs();
	    	  if (fpps == null){
	    		  fpps = new ArrayList<FundingPledgesProgram>();
	    	  }
		      AmpTheme prg = ProgramUtil.getThemeObject(Long.valueOf(selectedThemeId));
		      FundingPledgesProgram fpp = new FundingPledgesProgram();
		      fpp.setProgram(prg);
	          //fpp.setProgramSetting(parent);

		      if(fpps.size()==0) {
	            fpp.setProgrampercentage(100f);
	          } else{
	        	  fpp.setProgrampercentage(0f);
	          }
	        	
	          FundingPledgesProgram program = null;
	          boolean exist = false; 
	          for (Iterator iterator = fpps.iterator(); iterator.hasNext();) {
				FundingPledgesProgram fundingPledgesProgram = (FundingPledgesProgram) iterator.next();
				if ((fundingPledgesProgram.getId() != null) && (fundingPledgesProgram.getId().equals(fpp.getId()))) {
					exist = true;
                }
	          }
	          if (!exist) {
	        	  fpps.add(fpp);
	          }
	          pledgeform.setSelectedProgs(fpps);         
	        }
	        

	      return mapping.findForward("added");
	    }

	    int ind = 0;
	    boolean opflag = false;
	    AmpTheme prg = null;
	    prLevels = pledgeform.getProgramLevels();
	    if (prLevels == null) {
	      prLevels.add(getParents());
	    }
	    else if (prLevels.size() == 0) {
	      prLevels.add(getParents());

	    }

	    ListIterator prItr = prLevels.listIterator();
	    ListIterator subPrgItr;
	    while (prItr.hasNext()) {
	      ArrayList subPrg = (ArrayList) prItr.next();
	      if (subPrg != null) {
	        subPrgItr = subPrg.listIterator();
	        while (subPrgItr.hasNext()) {
	          prg = (AmpTheme) subPrgItr.next();
	          if (selectedThemeId.equals(prg.getAmpThemeId().
	                                     toString())) {
	            List<AmpTheme> subPrograms = getThenmes(Long.valueOf(selectedThemeId));
	            if (subPrograms != null) {
	              ind = prItr.nextIndex();
	              if (subPrograms.size() != 0) {
	                if (prItr.hasNext()) {
	                  prItr.next();
	                  prItr.set(subPrograms);
	                  ind = prItr.nextIndex();
	                }
	                else {
	                  prLevels.add(getThenmes(Long.valueOf(selectedThemeId)));
	                  ind = 0;
	                }
	                opflag = true;
	              }
	            }
	            break;
	          }
	        }
	      }
	      if (opflag) {
	        break;
	      }
	    }

	    if (ind != 0) {
	      int sz = prLevels.size();
	      for (int i = ind; i < sz; i++) {
	        prLevels.remove(ind);
	      }
	    }
	    pledgeform.setProgramLevels(prLevels);
	    return mapping.findForward("forward");
	  }

	  private List<AmpTheme> getThenmes(Long parentid) throws DgException{
	    ArrayList<AmpTheme> prl = new ArrayList<AmpTheme>(ProgramUtil.getSubThemes(parentid));
	    return prl;
	  }

	  private List<AmpTheme> getParents() throws DgException{
	    ArrayList<AmpTheme> prl = new ArrayList<AmpTheme>(ProgramUtil.getParentThemes());
	    return prl;
	  }

	  private boolean validateData(AmpTheme theme1, AmpTheme theme2) {
	    if (theme1.getAmpThemeId().equals(theme2.getAmpThemeId())) {
	      return true;
	    }
	    return false;
	  }
	}
