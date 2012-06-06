package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.AmpCollectionUtils;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpMEIndicatorValue;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.util.MEIndicatorsUtil;


public class AddIndicatorForStepNine extends Action{
	
private static Logger logger = Logger.getLogger(AddIndicatorForStepNine.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
	
		EditActivityForm eaForm = (EditActivityForm) form;
		HttpSession session = request.getSession();
		
		
		Collection<AmpMEIndicatorValue> colampMEIndValbox = (Collection)session.getAttribute("ampMEIndValbox");
		session.setAttribute("ampMEIndValbox",null);
		String name=null,code=null;
		Collection<ActivityIndicator>  tmpActivityIndicator = 	eaForm.getIndicator().getIndicatorsME();
		if (tmpActivityIndicator==null){
			tmpActivityIndicator=new ArrayList<ActivityIndicator>();
		}

		
		if(colampMEIndValbox!=null && !colampMEIndValbox.isEmpty()){
			Map <Long,AmpMEIndicatorValue> ampMEIndMap = AmpCollectionUtils.createMap(colampMEIndValbox, 
					new AmpCollectionUtils.KeyResolver<Long, AmpMEIndicatorValue>() {
						@Override
						public Long resolveKey(AmpMEIndicatorValue element) {						
							return element.getIndicator().getIndicatorId();
						}
				
			}) ;
			
			for (ActivityIndicator actInd : (Collection<ActivityIndicator>)tmpActivityIndicator) {
				if(ampMEIndMap!=null && ampMEIndMap.get(actInd.getIndicatorId())!=null){
					ampMEIndMap.remove(actInd.getIndicatorId());
				}
			}
			
			if(!ampMEIndMap.isEmpty()){
				Iterator<AmpMEIndicatorValue> itr = ampMEIndMap.values().iterator();
				while(itr.hasNext()){
					AmpMEIndicatorValue tmpObj = (AmpMEIndicatorValue)itr.next();
					ActivityIndicator actInd = new ActivityIndicator();
					actInd.setActivityId(tmpObj.getActivityId().getAmpActivityId());
					name = tmpObj.getIndicator().getName();
					code = tmpObj.getIndicator().getCode();
					actInd.setIndicatorName(name);
					actInd.setIndicatorCode(code);
					actInd.setBaseVal(null);
					actInd.setBaseValDate(null);
					actInd.setBaseValComments("");
					actInd.setTargetVal(null);
					actInd.setTargetValDate(null);
					actInd.setTargetValComments("");
					actInd.setRevisedTargetVal(null);
					actInd.setRevisedTargetValDate(null);
					actInd.setRevisedTargetValComments("");
					actInd.setCurrentVal(null);
					actInd.setCurrentValDate("");
					actInd.setCurrentValComments("");
					actInd.setRisk(new Long(-1));
					AmpIndicator ind=tmpObj.getIndicator();
					if(ind!=null){
					actInd.setIndicatorId(ind.getIndicatorId());
					}
					tmpActivityIndicator.add(actInd);
				}
				eaForm.getIndicator().setIndicatorsME(tmpActivityIndicator);
			}			
			
		}
	
		if(!eaForm.getIndicator().getIndicatorsME().isEmpty()){
			eaForm.getIndicator().setRiskCollection(MEIndicatorsUtil.getAllIndicatorRisks());
		}
			
		eaForm.getIndicator().setIndicatorRisk(null);
		return mapping.findForward("successfull");
	}
}
