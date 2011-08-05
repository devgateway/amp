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
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
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
		MapFieldsForm mForm = (MapFieldsForm) form;

		String fieldId	= request.getParameter("id");
		String actType	= request.getParameter("actionType");
		String mappedId	= request.getParameter("mappedId");
		String mappedValue	= request.getParameter("mappedValue");
		
		if(fieldId != null && actType != null && mappedId !=null){
			DEMappingFields ampDEMappingField = DataExchangeUtils.getAmpDEMappingField(new Long(fieldId));
			//TODO if ampId == -1 => create new field
			ampDEMappingField.setAmpId(new Long(mappedId));
			ampDEMappingField.setAmpValues(mappedValue);
			DataExchangeUtils.addObjectoToAmp(ampDEMappingField);
		}
		TreeSet<String> ampClasses = new TreeSet<String>();
		Collection<DEMappingFields> allAmpDEMappingFields = DataExchangeUtils.getAllAmpDEMappingFields();
		ArrayList<DEMappingFieldsDisplay> fieldDisplayList = new ArrayList<DEMappingFieldsDisplay>();
		
		populateCollections(ampClasses, allAmpDEMappingFields, fieldDisplayList);
		
		mForm.setMappedFields(fieldDisplayList);
		mForm.setAmpClasses(ampClasses);
		
		if ( "saveMappedField".equals( mForm.getAction() ) ) {
			modeSaveField(mapping, mForm, request, response);
		}
		
		return mapping.findForward("forward");
	}

	private void populateCollections(TreeSet<String> ampClasses,
			Collection<DEMappingFields> allAmpDEMappingFields,
			ArrayList<DEMappingFieldsDisplay> fieldDisplayList)
			throws DgException {
		Collection<AmpOrgType> allOrgTypes = DbUtil.getAllOrgTypes();
		Collection<AmpActivity> allActivities = DbUtil.getAllActivities();
		Collection<AmpOrganisation> allOrgs = DbUtil.getAllOrganisation();
		Collection<AmpCategoryValue> statusList = (Collection<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.ACTIVITY_STATUS_KEY);
		Collection<AmpCategoryValue> typeOfAssistanceList = (Collection<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY);
		Collection<AmpCategoryValue> financingInstrumentList = (Collection<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY);
		Collection<AmpCategoryValue> modeOfPaymentList = (Collection<AmpCategoryValue>) CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.MODE_OF_PAYMENT_KEY);
		Collection<AmpSectorScheme> sectorSchemeList = SectorUtil.getAllSectorSchemes();
		Collection<AmpSector> sectorList = SectorUtil.getAllSectors();
		Collection<AmpCategoryValueLocations> locationsList = LocationUtil.getAllLocations(null);
		
		
		for (Iterator<DEMappingFields> it = allAmpDEMappingFields.iterator(); it.hasNext();) {
			DEMappingFields f = (DEMappingFields) it.next();
			ampClasses.add(f.getAmpClass());
			if(DataExchangeConstants.IATI_ACTIVITY.compareTo(f.getIatiPath()) ==0 )
				fieldDisplayList.add(new DEMappingFieldsDisplay(f,allActivities));
			if(DataExchangeConstants.IATI_ORGANIZATION_TYPE.compareTo(f.getIatiPath())==0){
				fieldDisplayList.add(new DEMappingFieldsDisplay(f,allOrgTypes));
			}
			if(DataExchangeConstants.IATI_ORGANIZATION.compareTo(f.getIatiPath())==0){
				fieldDisplayList.add(new DEMappingFieldsDisplay(f,allOrgs));
			}
			if(DataExchangeConstants.IATI_LOCATION.compareTo(f.getIatiPath())==0){
				fieldDisplayList.add(new DEMappingFieldsDisplay(f,locationsList));
			}
			if(DataExchangeConstants.IATI_ACTIVITY_STATUS.compareTo(f.getIatiPath())==0){
				fieldDisplayList.add(new DEMappingFieldsDisplay(f,statusList));
			}
			if(DataExchangeConstants.IATI_VOCABULARY_CODE.compareTo(f.getIatiPath())==0){
				fieldDisplayList.add(new DEMappingFieldsDisplay(f,sectorSchemeList));
			}
			if(DataExchangeConstants.IATI_SECTOR.compareTo(f.getIatiPath())==0){
				fieldDisplayList.add(new DEMappingFieldsDisplay(f,sectorList));
			}
			//type of assistance
			if(DataExchangeConstants.IATI_FINANCE_TYPE.compareTo(f.getIatiPath())==0){
				fieldDisplayList.add(new DEMappingFieldsDisplay(f,typeOfAssistanceList));
			}
			//financing instrument
			if(DataExchangeConstants.IATI_AID_TYPE.compareTo(f.getIatiPath())==0){
				fieldDisplayList.add(new DEMappingFieldsDisplay(f,financingInstrumentList));
			}
			//mode of payment
			if(DataExchangeConstants.IATI_DISBURSEMENT_CHANNEL.compareTo(f.getIatiPath())==0){
				fieldDisplayList.add(new DEMappingFieldsDisplay(f,modeOfPaymentList));
			}
		}
	}
	
	public void modeSaveField(ActionMapping mapping, MapFieldsForm msForm, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
	}

}
