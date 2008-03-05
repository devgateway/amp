/*
 * AddThemeIndicator.java 
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.AmpThemeIndicators;
import org.digijava.module.aim.form.ThemeForm;
import org.digijava.module.aim.helper.AllMEIndicators;
import org.digijava.module.aim.helper.AllPrgIndicators;
import org.digijava.module.aim.helper.AmpPrgIndicator;
import org.digijava.module.aim.helper.AmpPrgIndicatorValue;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.ProgramUtil;
import java.util.Collections;
import org.digijava.module.aim.helper.AllActivities;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.helper.AllThemes;
import java.util.Collection;
import java.util.Iterator;

public class AddThemeIndicator extends Action 
{
	private static Logger logger = Logger.getLogger(AddThemeIndicator.class);
	private ArrayList indValues = null;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception 
	{
		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) 
		{
			return mapping.findForward("index");
		} else 
		{
			String str = (String) session.getAttribute("ampAdmin");
			if (str.equals("no")) 
			{
				return mapping.findForward("index");
			}
		}
		
		ThemeForm themeForm = (ThemeForm) form;
		//Long id = new Long(Long.parseLong(request.getParameter("themeId")));
		//String event = request.getParameter("event");
		//themeForm.setThemeId(id);
		//if(request.getParameter("resetIndicatorId")!=null) themeForm.setIndicatorId(null);
		
		
		
		Long selInds[] =  themeForm.getIndid();
//		Long id = new Long(Long.parseLong(request.getParameter("themeId")));
		Long id = themeForm.getThemeId();
		String event = themeForm.getEvent();
		themeForm.setThemeId(id);
		boolean flag = true;
		
		
		
		//Collection indi = ProgramUtil.getAllThemesIndicators();
	
		
		//if(request.getParameter("resetIndicatorId")!=null) themeForm.setIndicatorId(null);
		
	if(id != null && selInds != null){
			
			for (int j = 0; j < selInds.length; j++) {
				
				// improvement 2217 changed 3 time :( assigne or not
				
//				for(Iterator itr=indi.iterator(); itr.hasNext();){
//					
//					AllThemes item=(AllThemes)itr.next();
//					 Collection<AllPrgIndicators> themeassignind = item.getAllPrgIndicators();
//				 	  List<AllPrgIndicators> allprgindlist = new ArrayList<AllPrgIndicators>(themeassignind);
//				 	   	for(Iterator Itrind = allprgindlist.iterator(); Itrind.hasNext();){
//				 	   		AllPrgIndicators tInd = (AllPrgIndicators) Itrind.next();
//				 	   	
//				 	   		if(tInd.getIndicatorId().equals(selInds[j])){
//						 	   	
//				 	   					flag = false;
//							   			break;
//				 	   			 }
//				 	   		  }
//				 	   	  }
								if(flag == true){
						
								ProgramUtil.assignThemeInd(selInds[j],new Long(id )); 
							}
  		  			}
		 }

	if(event!=null)
		{
			if(event.equals("save"))
			{
				String indId = request.getParameter("indicatorId");
				AllPrgIndicators allPrgInd = new AllPrgIndicators();
				if(indId != null){
				if(indId.trim().length() == 0 || indId.trim().equals("0"))
				{
					AmpPrgIndicator ampPrgInd = new AmpPrgIndicator();
					ampPrgInd.setName(themeForm.getName());
					ampPrgInd.setCode(themeForm.getCode());
					ampPrgInd.setType(themeForm.getType());
					ampPrgInd.setCreationDate(themeForm.getCreationDate()[0]);
					ampPrgInd.setCategory(themeForm.getCategory());
					ampPrgInd.setNpIndicator(themeForm.isNpIndicator());
					ampPrgInd.setDescription(themeForm.getIndicatorDescription());
					ampPrgInd.setPrgIndicatorValues(themeForm.getPrgIndValues());
					ProgramUtil.saveThemeIndicators(ampPrgInd, id);
				}
				else
				{
					Long indicatorId = new Long(Long.parseLong(indId));
					allPrgInd.setIndicatorId(indicatorId);
					allPrgInd.setName(themeForm.getName());
					allPrgInd.setCode(themeForm.getCode());
					allPrgInd.setType(themeForm.getType());
					allPrgInd.setDescription(themeForm.getIndicatorDescription());
					allPrgInd.setCreationDate(themeForm.getCreationDate()[0]);
					allPrgInd.setValueType(themeForm.getValueType()[0]);
					allPrgInd.setCategory(themeForm.getCategory());
					allPrgInd.setNpIndicator(themeForm.isNpIndicator());
					allPrgInd.setThemeIndValues(themeForm.getPrgIndValues());
					ProgramUtil.saveEditThemeIndicators(allPrgInd, id);
				}
			}
		}
			if(event.equals("indValue"))
			{
				indValues = new ArrayList();
				AmpPrgIndicatorValue prgIndVal = null;
				if(themeForm.getPrgIndValues() != null){
                    indValues = new ArrayList(themeForm.getPrgIndValues());
                }
				prgIndVal = getPrgIndicatorValue();
				indValues.add(prgIndVal);
				themeForm.setPrgIndValues(indValues);
				return mapping.findForward("forward");
			}
			
			if (event != null && event.equals("Delete"))
			{
				Long indId[] = themeForm.getIndicatorsId();
				if(indId != null)
				{
					
					for (int j = 0; j < indId.length; j++) {
						
						//ProgramUtil.deletePrgIndicator(indId[j]);
						IndicatorUtil.deleteIndtheme(indId[j]);
					}	
					
//			   	 ProgramUtil.deletePrgIndicator(indId);
//			   	 for(Iterator itr=themeForm.getPrgIndicators().iterator(); itr.hasNext();){
//			   		AmpPrgIndicator ind=(AmpPrgIndicator)itr.next();
//			   		if(ind.getIndicatorId().equals(indId)){
//			   			itr.remove();
//			   			break;
//			   }
				}
			}

			if(event.equals("edit"))
			{
				AllPrgIndicators allPrgInd = new AllPrgIndicators();
				Long indId = new Long(Long.parseLong(request.getParameter("prgIndicatorId")));
				allPrgInd = ProgramUtil.getThemeIndValues(indId);
				themeForm.setIndicatorId(allPrgInd.getIndicatorId());
				themeForm.setName(allPrgInd.getName());
				themeForm.setCode(allPrgInd.getCode());
				themeForm.setType(allPrgInd.getType());
				themeForm.setIndicatorDescription(allPrgInd.getDescription());
                String dt[]=new String[0];
                dt[0]=allPrgInd.getCreationDate();
				themeForm.setCreationDate(dt);
				themeForm.setCategory(allPrgInd.getCategory());
				themeForm.setNpIndicator(allPrgInd.isNpIndicator());
				List prgIndVal = new ArrayList(allPrgInd.getThemeIndValues());
				themeForm.setPrgIndValues(prgIndVal);
				themeForm.setPrgIndicators(ProgramUtil.getThemeIndicators(id));
				return mapping.findForward("forward");
			}
		    if(event.equals("overall")) {
                themeForm.setIndType("overall");
            }
        }
		themeForm.setEvent(null);
		themeForm.setIndicatorsId(null);
		themeForm.setCode(null);
		themeForm.setName(null);
		themeForm.setType(null);
		themeForm.setCreationDate(null);
		themeForm.setProgramTypeCategValId(new Long(0));
		themeForm.setIndicatorDescription(null);
		themeForm.setCategory(0);
		themeForm.setNpIndicator(false);
		themeForm.setPrgIndValues(null);
		themeForm.setIndid(null);
		themeForm.setFlag("");
		
		if(flag == false){
		themeForm.setFlag("error");
		}

		List prgInds = new ArrayList(IndicatorUtil.getAllIndicators(id));
        Collections.sort(prgInds, new ProgramUtil.HelperAmpIndicatorNameComparator());   
        themeForm.setPrgIndicators(prgInds);

		return mapping.findForward("forward");
	}
	
	private AmpPrgIndicatorValue getPrgIndicatorValue() 
	{
		AmpPrgIndicatorValue prgIndVal = new AmpPrgIndicatorValue();
		prgIndVal.setCreationDate(null);
		prgIndVal.setValAmount(null);
		prgIndVal.setValueType(1);
		return prgIndVal;
	}
}
