/**
 * 
 */
package org.digijava.module.dataExchange.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.dataExchange.dbentity.DEMappingFields;
import org.digijava.module.dataExchange.dbentity.DEMappingFieldsDisplay;
import org.digijava.module.dataExchange.form.MapFieldsForm;
import org.digijava.module.dataExchange.util.DataExchangeConstants;
import org.digijava.module.dataExchange.utils.DataExchangeUtils;

/**
 * @author Dan Mihaila
 *
 */
public class MapFieldsAction extends MultiAction {

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.utils.MultiAction#modePrepare(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modePrepare(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		return modeSelect(mapping, form, request, response);
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.utils.MultiAction#modeSelect(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward modeSelect(ActionMapping mapping, ActionForm form,	HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String htmlView	= request.getParameter("htmlView");
		MapFieldsForm mForm = (MapFieldsForm) form;
		TreeSet<String> ampClasses = new TreeSet<String>();
		Collection<DEMappingFields> allAmpDEMappingFields = DataExchangeUtils.getAllAmpDEMappingFields();
		ArrayList<DEMappingFieldsDisplay> fieldDisplayList = new ArrayList<DEMappingFieldsDisplay>();
		
		Collection<AmpOrgType> allOrgTypes = DbUtil.getAllOrgTypes();
		Collection<AmpActivity> allActivities = DbUtil.getAllActivities();
		
		
		for (Iterator<DEMappingFields> it = allAmpDEMappingFields.iterator(); it.hasNext();) {
			DEMappingFields f = (DEMappingFields) it.next();
			ampClasses.add(f.getAmpClass());
			if(DataExchangeConstants.IATI_ACTIVITY.compareTo(f.getIatiPath()) ==0 )
				fieldDisplayList.add(new DEMappingFieldsDisplay(f,allActivities));
			if(DataExchangeConstants.IATI_ORGANIZATION_TYPE.compareTo(f.getIatiPath())==0){
				fieldDisplayList.add(new DEMappingFieldsDisplay(f,allOrgTypes));
			}
		}
		
		
		
		mForm.setMappedFields(fieldDisplayList);
		mForm.setAmpClasses(ampClasses);
		
		if ( "saveMappedField".equals( mForm.getAction() ) ) {
			modeSaveField(mapping, mForm, request, response);
		}
		
		return mapping.findForward("forward");
	}
	
	public void modeSaveField(ActionMapping mapping, MapFieldsForm msForm, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
	}

}
