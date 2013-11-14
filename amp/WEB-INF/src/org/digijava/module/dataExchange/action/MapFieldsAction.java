/**
 * 
 */
package org.digijava.module.dataExchange.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
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
import org.digijava.module.dataExchange.util.SessionSourceSettingDAO;
import org.digijava.module.dataExchange.utils.Constants;
import org.digijava.module.dataExchange.utils.DataExchangeUtils;
import org.digijava.module.message.form.AmpMessageForm;

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
		String mappedId	= request.getParameter("ampId");
		String mappedValue	= request.getParameter("mappedValue");
		
		if(actType!=null){
			if("saveRecord".compareTo(actType) ==0)
				if(fieldId != null && mappedId !=null){
					{
						saveMappedField(request,fieldId, mappedId, mappedValue);
						mForm.setAllSelectedAmpValues(null);
					}
				}
			if("saveAllRecords".compareTo(actType) ==0)
			{
				for (int i = 0; i < mForm.getSelectedFields().length; i++) {
					saveMappedField(request,mForm.getSelectedFields()[i], mForm.getSelectedAmpIds()[i], mForm.getSelectedAmpValues()[i]);
					mForm.setAllSelectedAmpValues(null);
				}
			}
		}
		TreeSet<String> ampClasses = new TreeSet<String>();
		
		String ampClassTypeSelected = DataExchangeConstants.IATI_ACTIVITY;
		if( mForm.getSelectedAmpClass()!=null && "".compareTo(mForm.getSelectedAmpClass()) != 0 && "undefined".compareTo(mForm.getSelectedAmpClass()) != 0)
			ampClassTypeSelected = mForm.getSelectedAmpClass();
		
		int allDEFieldsCounter =  DataExchangeUtils.getCountDEMappingFieldsByAmpClass(ampClassTypeSelected);
		int lastPage = 1;
		if (allDEFieldsCounter > Constants.MAPPING_RECORDS_AMOUNT_PER_PAGE) {
			lastPage = allDEFieldsCounter% Constants.MAPPING_RECORDS_AMOUNT_PER_PAGE==0 ? allDEFieldsCounter / Constants.MAPPING_RECORDS_AMOUNT_PER_PAGE : allDEFieldsCounter / Constants.MAPPING_RECORDS_AMOUNT_PER_PAGE +1;
		}
		
		int startIndex = 0;
		if (mForm.getPage() != 0 ) {
			startIndex = Constants.MAPPING_RECORDS_AMOUNT_PER_PAGE *( mForm.getPage()-1);
		}
		String sortDB=null;
		String sort = (mForm.getSort() == null) ? null : mForm.getSort().trim();
	    String sortOrder = (mForm.getSortOrder() == null) ? null : mForm.getSortOrder().trim();
	    if(sort == null || "".equals(sort) || sortOrder == null || "".equals(sortOrder)) {
            mForm.setSort("iati items");
            mForm.setSortOrder("asc");
            sortDB = "iatiItems";
            sortOrder = "asc";
        } else {
            if("iati items".equals(sort)) {
            	sortDB = "iatiItems";
            } else if("iati values".equals(sort)) {
            	sortDB = "iatiValues";
            } else if("current value".equals(sort)) {
            	sortDB = "ampValues";
            } else if("status".equals(sort)) {
            	sortDB = "status";
            }
        }
		
		Collection<DEMappingFields> allAmpDEMappingFields = DataExchangeUtils.getDEMappingFieldsByAmpClass(ampClassTypeSelected,startIndex,sortDB,sortOrder);
		ArrayList<DEMappingFieldsDisplay> fieldDisplayList = new ArrayList<DEMappingFieldsDisplay>();
		
		DataExchangeUtils.getAmpClassesFromDb(ampClasses);
		populateCollections(allAmpDEMappingFields, fieldDisplayList, ampClassTypeSelected);
		
		mForm.setMappedFields(fieldDisplayList);
		mForm.setAmpClasses(ampClasses);
		
		
        mForm.setAllSelectedAmpValues(null);
		if(mForm.getPage() == 0){
			mForm.setCurrentPage(1);
		}else{
			mForm.setCurrentPage(mForm.getPage());
		}
		
		mForm.setLastPage(lastPage);
		mForm.setSelectedAmpClass(ampClassTypeSelected);
		if ( "saveMappedField".equals( mForm.getAction() ) ) {
			modeSaveField(mapping, mForm, request, response);
		}
		
		return mapping.findForward("forward");
	}
	
	private void saveMappedField(HttpServletRequest request, String fieldId, String ampId, String mappedValue) {
		DEMappingFields ampDEMappingField = DataExchangeUtils.getAmpDEMappingField(new Long(fieldId));
		Long id = new Long(ampId);
		String newMappedValue = null;
		
		//TODO
		//locations and sectors/sector scheme can not be created as new because we do not know the nested place
		//where to put the sector.
		
		if("-1".compareTo(ampId)==0){
			//create new AMP object
			//TODO
			//if org exist don't save and provide a message
			if(DataExchangeConstants.IATI_ORGANIZATION.compareTo(ampDEMappingField.getIatiPath())==0)
			{
				AmpOrganisation o = new AmpOrganisation();
				String[] values = ampDEMappingField.getIatiValues().split("\\|\\|\\|");
				o.setName(values[0]);
				o.setOrgCode(values[1]);
				DataExchangeUtils.addObjectoToAmp(o);
				id = o.getAmpOrgId();
				newMappedValue = o.getName();
			}
			if(DataExchangeConstants.IATI_ORGANIZATION_TYPE.compareTo(ampDEMappingField.getIatiPath())==0)
			{
				AmpOrgType o = new AmpOrgType();
				o.setOrgType(ampDEMappingField.getIatiValues());
				o.setOrgTypeCode(ampDEMappingField.getIatiValues());
				DataExchangeUtils.addObjectoToAmp(o);
				id = o.getAmpOrgTypeId();
				newMappedValue = o.getOrgType();
			}
			
			if(DataExchangeConstants.IATI_ACTIVITY_STATUS.compareTo(ampDEMappingField.getIatiPath())==0)
			{
				AmpCategoryValue o = addCategValueToAmp(CategoryConstants.ACTIVITY_STATUS_KEY, ampDEMappingField.getIatiValues());
				if(o!=null){
					id = o.getId();
					newMappedValue = getValueorCode(ampDEMappingField.getIatiValues());
				}
			}
			
			if(DataExchangeConstants.IATI_AID_TYPE.compareTo(ampDEMappingField.getIatiPath())==0)
			{
				AmpCategoryValue o = addCategValueToAmp(CategoryConstants.FINANCING_INSTRUMENT_KEY,ampDEMappingField.getIatiValues());
				if(o!=null){
					id = o.getId();
					newMappedValue = getValueorCode(ampDEMappingField.getIatiValues());
				}
			}

			if(DataExchangeConstants.IMPLEMENTATION_LEVEL_TYPE.compareTo(ampDEMappingField.getIatiPath())==0)
			{
				AmpCategoryValue o = addCategValueToAmp(CategoryConstants.IMPLEMENTATION_LEVEL_KEY,ampDEMappingField.getIatiValues());
				if(o!=null){
					id = o.getId();
					newMappedValue = getValueorCode(ampDEMappingField.getIatiValues());
				}
			}
			if(DataExchangeConstants.IATI_FINANCE_TYPE.compareTo(ampDEMappingField.getIatiPath())==0)
			{
				AmpCategoryValue o = addCategValueToAmp(CategoryConstants.TYPE_OF_ASSISTENCE_KEY, ampDEMappingField.getIatiValues());
				if(o!=null){
					id = o.getId();
					newMappedValue = getValueorCode(ampDEMappingField.getIatiValues());
				}
			}
			if(DataExchangeConstants.IATI_DISBURSEMENT_CHANNEL.compareTo(ampDEMappingField.getIatiPath())==0)
			{
				AmpCategoryValue o = addCategValueToAmp(CategoryConstants.MODE_OF_PAYMENT_KEY,ampDEMappingField.getIatiValues());
				if(o!=null){
					id = o.getId();
					newMappedValue = getValueorCode(ampDEMappingField.getIatiValues());
				}
			}

		}
		ampDEMappingField.setAmpId(id);
		ampDEMappingField.setAmpValues(newMappedValue==null?mappedValue:newMappedValue);
		DataExchangeUtils.addObjectoToAmp(ampDEMappingField);
	}

	private AmpCategoryValue addCategValueToAmp(String categKey,String value) {
		AmpCategoryValue o = null;
		String finalValue = getValueorCode(value);
		if(finalValue == null) return o; //null object
		try {
			o = CategoryManagerUtil.addValueToCategory(categKey, finalValue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}
	
	private String getValueorCode(String value){
		
	String finalValue = null;
	String[] values = value.split("\\|\\|\\|");
	if(isValidString(values[0]))
		finalValue = values[0];
	else if(isValidString(values[1]) && "null".compareTo(values[1])!=0)
			finalValue = values[1];
	return finalValue;
	}
	
	private void populateCollections(Collection<DEMappingFields> allAmpDEMappingFields, ArrayList<DEMappingFieldsDisplay> fieldDisplayList, String ampClassTypeSelected)	throws DgException {
		TreeMap<Long, String> allEntities 	=	getAllAmpEntitiesByClass(ampClassTypeSelected);
		
		for (Iterator<DEMappingFields> it = allAmpDEMappingFields.iterator(); it.hasNext();) {
			DEMappingFields f = (DEMappingFields) it.next();
			fieldDisplayList.add(new DEMappingFieldsDisplay(f,allEntities));
		}
	}
	
	private TreeMap<Long, String> getAllAmpEntitiesByClass(String ampClassTypeSelected) {
		// TODO Auto-generated method stub
		TreeMap<Long, String> allEntities 	=	null;
		if(DataExchangeConstants.IATI_ACTIVITY.compareTo(ampClassTypeSelected) ==0 )
			allEntities 	=	DataExchangeUtils.getNameGroupAllActivities();
		if(DataExchangeConstants.IATI_ORGANIZATION_TYPE.compareTo(ampClassTypeSelected)==0){
			String orgTypeNameHql = AmpOrgType.hqlStringForName("f");
			allEntities 	=	DataExchangeUtils.getNameIdAllEntities(String.format("select %s, f.ampOrgTypeId from " + AmpOrgType.class.getName()+ " f order by %s asc", orgTypeNameHql, orgTypeNameHql));
		}
		if(DataExchangeConstants.IATI_ORGANIZATION.compareTo(ampClassTypeSelected)==0){
			String orgNameHql = AmpOrganisation.hqlStringForName("f");
			allEntities 	=	DataExchangeUtils.getNameIdAllEntities(String.format("select %s, f.ampOrgId from " + AmpOrganisation.class.getName()+ " f where (f.deleted is null or f.deleted = false) order by %s asc", orgNameHql, orgNameHql));
		}
		if(DataExchangeConstants.IATI_LOCATION.compareTo(ampClassTypeSelected)==0){
			allEntities 	=	DataExchangeUtils.getNameIdAllLocations();
		}
		if(CategoryConstants.ACTIVITY_STATUS_NAME.compareTo(ampClassTypeSelected)==0){
			allEntities 	=	DataExchangeUtils.getNameIdAllEntitiesFromACVC(CategoryConstants.ACTIVITY_STATUS_KEY);
		}
		if(DataExchangeConstants.AMP_VOCABULARY_CODE.compareTo(ampClassTypeSelected)==0){
			String secSchemeNameHql = AmpSectorScheme.hqlStringForName("f");
			allEntities 	=	DataExchangeUtils.getNameIdAllEntities(String.format("select %s, f.ampSecSchemeId from " + AmpSectorScheme.class.getName()+ " f", secSchemeNameHql));
		}
		if(DataExchangeConstants.IATI_SECTOR.compareTo(ampClassTypeSelected)==0){
			String sectorNameHql = AmpSector.hqlStringForName("f");
			allEntities 	=	DataExchangeUtils.getNameIdAllEntities(String.format("select concat(f.sectorCodeOfficial,concat(' - ',%s)) as sname, f.ampSectorId  from " + AmpSector.class.getName()+ " f  where (f.deleted is null or f.deleted = false) order by sname", sectorNameHql));
		}
		//type of assistance
		if(CategoryConstants.TYPE_OF_ASSISTENCE_NAME.compareTo(ampClassTypeSelected)==0){
			allEntities 	=	DataExchangeUtils.getNameIdAllEntitiesFromACVC(CategoryConstants.TYPE_OF_ASSISTENCE_KEY);
		}
		//financing instrument
		if(CategoryConstants.FINANCING_INSTRUMENT_NAME.compareTo(ampClassTypeSelected)==0){
			allEntities 	=	DataExchangeUtils.getNameIdAllEntitiesFromACVC(CategoryConstants.FINANCING_INSTRUMENT_KEY);
		}
		//mode of payment
		if(CategoryConstants.MODE_OF_PAYMENT_NAME.compareTo(ampClassTypeSelected)==0){
			allEntities 	=	DataExchangeUtils.getNameIdAllEntitiesFromACVC(CategoryConstants.MODE_OF_PAYMENT_KEY);
		}
		
		if(CategoryConstants.IMPLEMENTATION_LEVEL_NAME.compareTo(ampClassTypeSelected)==0){
			allEntities 	=	DataExchangeUtils.getNameIdAllEntitiesFromACVC(CategoryConstants.IMPLEMENTATION_LEVEL_KEY);
		}
		return allEntities;
	}

	private boolean isValidString(String s ){
		if(s != null && "".compareTo(s.trim())!=0 )
			return true;
		return false;
	}
	public void modeSaveField(ActionMapping mapping, MapFieldsForm msForm, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
	}

}
