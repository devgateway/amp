/**
 * 
 */
package org.digijava.module.dataExchange.action;

import java.io.PrintWriter;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.dgfoundation.amp.utils.MultiAction;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.dataExchange.dbentity.DEMappingFields;
import org.digijava.module.dataExchange.dbentity.DEMappingFieldsDisplay;
import org.digijava.module.dataExchange.form.MapFieldsForm;
import org.digijava.module.dataExchange.util.DataExchangeConstants;
import org.digijava.module.dataExchange.utils.DEConstants;
import org.digijava.module.dataExchange.utils.DataExchangeUtils;
import org.digijava.module.dataExchange.utils.ValueComparator;



/**
 * @author Dan Mihaila
 *
 */
public class MapFieldsAction extends DispatchAction {
    public static final String IATI_LABELS_SORTED = "IATI_LABELS_SORTED";



    public ActionForward unspecified(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        return modeSelect(mapping, form, request, response);
    }


    public ActionForward modeSelect(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        MapFieldsForm mForm = (MapFieldsForm) form;

        String fieldId  = request.getParameter("id");
        String actType  = request.getParameter("actionType");
        String mappedId = request.getParameter("ampId");
        String mappedValue  = request.getParameter("mappedValue");
        
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
        if (allDEFieldsCounter > DEConstants.MAPPING_RECORDS_AMOUNT_PER_PAGE) {
            lastPage = allDEFieldsCounter% DEConstants.MAPPING_RECORDS_AMOUNT_PER_PAGE==0 ? allDEFieldsCounter / DEConstants.MAPPING_RECORDS_AMOUNT_PER_PAGE : allDEFieldsCounter / DEConstants.MAPPING_RECORDS_AMOUNT_PER_PAGE +1;
        }
        
        int startIndex = 0;
        if (mForm.getPage() != 0 ) {
            startIndex = DEConstants.MAPPING_RECORDS_AMOUNT_PER_PAGE *( mForm.getPage()-1);
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
        populateCollections(allAmpDEMappingFields, fieldDisplayList, ampClassTypeSelected, false);

        TreeMap<Long, String> allEntities = DataExchangeUtils.getAllAmpEntitiesByClass(ampClassTypeSelected);
        mForm.setAllEntities(allEntities);

        ValueComparator bvc =  new ValueComparator(allEntities);
        TreeMap<Long, String> sortedLabels = new TreeMap<Long,String>(bvc);
        sortedLabels.putAll(allEntities);
        mForm.setAllEntitiesSorted(sortedLabels);
        request.getSession().setAttribute(IATI_LABELS_SORTED, sortedLabels);

        
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
    
    private void populateCollections(Collection<DEMappingFields> allAmpDEMappingFields, ArrayList<DEMappingFieldsDisplay> fieldDisplayList, String ampClassTypeSelected)    throws DgException {
        populateCollections(allAmpDEMappingFields, fieldDisplayList, ampClassTypeSelected, true);
    }

    private void populateCollections(Collection<DEMappingFields> allAmpDEMappingFields, ArrayList<DEMappingFieldsDisplay> fieldDisplayList, String ampClassTypeSelected, boolean addAllEntities)    throws DgException {
        TreeMap<Long, String> allEntities   = null;
        if (addAllEntities) {
            allEntities = DataExchangeUtils.getAllAmpEntitiesByClass(ampClassTypeSelected);
        }

        for (Iterator<DEMappingFields> it = allAmpDEMappingFields.iterator(); it.hasNext();) {
            DEMappingFields f = (DEMappingFields) it.next();

            fieldDisplayList.add(new DEMappingFieldsDisplay(f,allEntities));
        }
    }
    


    private boolean isValidString(String s ){
        if(s != null && "".compareTo(s.trim())!=0 )
            return true;
        return false;
    }
    public void modeSaveField(ActionMapping mapping, MapFieldsForm msForm, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
    }

    public ActionForward getOptionsAjaxAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        String maxResultCountStr = request.getParameter("maxResultCount");
        String searchStr = request.getParameter("searchStr");
        int maxResultCount = Integer.parseInt(maxResultCountStr);

        Map<Long, String> sortedLabels = (Map<Long,String>) request.getSession().getAttribute(IATI_LABELS_SORTED);;
        JSONArray objArray = new JSONArray();
        JSONObject retObj = new JSONObject();

        JSONObject itemObj = new JSONObject();
        itemObj.accumulate("id", "-1");
        itemObj.accumulate("val", "Add new");
        objArray.add(itemObj);

        for (java.util.Map.Entry<Long, String> item : sortedLabels.entrySet()) {
            if (searchStr == null || searchStr.trim().isEmpty() || (item.getValue() != null && item.getValue().toLowerCase().contains(searchStr.toLowerCase()))) {

                objArray.add(new JSONObject().accumulate("id", item.getKey()).accumulate("val", item.getValue()));
            }
            if (maxResultCount > 0 && objArray.size() >= maxResultCount) break;
        }

        retObj.accumulate("totalCount", sortedLabels.size());
        retObj.accumulate("criteriaCount", objArray.size());
        retObj.accumulate("objects", objArray);
        PrintWriter out = response.getWriter();
        retObj.write(out);
        out.close();
        return null;
    }

}
