package org.digijava.module.aim.uicomponents.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.uicomponents.AddOrganizationButton;
import org.digijava.module.aim.uicomponents.IPostProcessDelegate;
import org.digijava.module.aim.uicomponents.form.selectOrganizationComponentForm;
import org.digijava.module.aim.util.DbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public class selectOrganizationComponent extends Action {
    
    public static final String ROOT_TAG = "ORGANIZATIONS";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        String subAction = (request.getParameter("subAction") != null) ? request.getParameter("subAction") : "";
        selectOrganizationComponentForm oForm = (selectOrganizationComponentForm) form;
        // call the requested action
        if ("search".equalsIgnoreCase(subAction)) {
            return search(mapping, form, request, response);
        } else if ("selectPage".equalsIgnoreCase(subAction)) {
            return selectPage(mapping, form, request, response);
        } else if ("organizationSelected".equalsIgnoreCase(subAction)) {
            //fill list first
            fillAllSelectedOgs(oForm);
            // if has a delegate so call it
            if (oForm.getDelegateClass() != null && !"".equalsIgnoreCase(oForm.getDelegateClass())) {               
                //call delegate
                return executeDelegate(mapping, form, request, response);
            } else {
                // if have to add to a collection so this is a multiselector
                if (oForm.getMultiSelect()) {
                    return addOrganizationToForm(mapping, form, request, response);
                } else {
                    return setOrganizationToForm(mapping, form, request, response);
                }
            }
        }else if("validate".equalsIgnoreCase(subAction)){
             //creating xml that will be returned
            int orgsSize=oForm.getAllSelectedOrgsIds()!=null ? oForm.getAllSelectedOrgsIds().size() : 0;
            response.setContentType("text/xml");
            OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream());
            PrintWriter out = new PrintWriter(outputStream, true);
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
            xml += "<" + ROOT_TAG + ">";
            xml += "<" + "organizations amount=\"" + new Long(orgsSize).toString() + "\" />";
            xml += "</" + ROOT_TAG + ">";
            out.println(xml);
            out.close();
            // return xml
            outputStream.close();
            return null;
        }

        return prepare(mapping, form, request, response);

    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        selectOrganizationComponentForm oForm = (selectOrganizationComponentForm) form;
        HttpSession session = request.getSession();
        Object targetForm = session.getAttribute(AddOrganizationButton.PARAM_PARAM_FORM_NAME);

        oForm.setExcludedOrgIdsSeparated(request.getParameter("excludedOrgIdsSeparated"));

        if ("true".equalsIgnoreCase(request.getParameter("reset"))) {
            oForm.clearSelected();
        }

        oForm.setTargetForm(targetForm);

        String collection = request.getParameter(AddOrganizationButton.PARAM_COLLECTION_NAME);
        oForm.setTargetCollection(collection);

        String propertyName = request.getParameter(AddOrganizationButton.PARAM_PROPERY_NAME);
        oForm.setTargetProperty(propertyName);

        String callbackFunction = request.getParameter(AddOrganizationButton.PARAM_CALLBACKFUNCTION_NAME);
        if (callbackFunction != null && !"null".equalsIgnoreCase(callbackFunction)) {
            oForm.setCallbackFunction(callbackFunction);
        }
        if ("true".equalsIgnoreCase(request.getParameter(AddOrganizationButton.PARAM_REFRESH_PARENT))) {
            oForm.setRefreshParent(true);
        } else {
            oForm.setRefreshParent(false);
        }
        
        //styleClass
        String styleClassName=request.getParameter(AddOrganizationButton.STYLE_CLASS_NAME);
        if(styleClassName!=null && !"null".equalsIgnoreCase(styleClassName)){
            oForm.setStyleClass(styleClassName);
        }
        
        if ("true".equalsIgnoreCase(request.getParameter(AddOrganizationButton.PARAM_USE_CLIENT))) {
            oForm.setUseClient(true);
            oForm.setValueHoder(request.getParameter(AddOrganizationButton.PARAM_VALUE_HOLDER));
            oForm.setNameHolder(request.getParameter(AddOrganizationButton.PARAM_NAME_HOLDER));
        } else {

            oForm.setUseClient(false);
        }

        if ("true".equalsIgnoreCase(request.getParameter(AddOrganizationButton.PARAM_USE_ACRONYM))) {
            oForm.setUseAcronym(true);
        } else {
            oForm.setUseAcronym(false);
        }
        
        if (!"".equalsIgnoreCase(collection) && collection != null) {
            oForm.setMultiSelect(true);
        } else {
            oForm.setMultiSelect(false);
        }

        if (request.getParameter(AddOrganizationButton.PARAM_RESET_FORM) != null && request.getParameter(AddOrganizationButton.PARAM_RESET_FORM).equals("false")) {
            oForm.setOrgSelReset(false);
        } else {
            oForm.setOrgSelReset(true);
            oForm.reset(mapping, request);
        }
        if (request.getParameter(AddOrganizationButton.PARAM_NAME_DELEGATE_CLASS) != null && !"".equalsIgnoreCase(request.getParameter(AddOrganizationButton.PARAM_NAME_DELEGATE_CLASS))) {
            oForm.setDelegateClass(request.getParameter(AddOrganizationButton.PARAM_NAME_DELEGATE_CLASS));
        }

        oForm.getAditionalParameters().clear();
        if (request.getParameter(AddOrganizationButton.ADITIONAL_REQUEST_PARAMS) != null && !"".equalsIgnoreCase(request.getParameter(AddOrganizationButton.ADITIONAL_REQUEST_PARAMS))) 
        {

            String[] paramString = request.getParameter(AddOrganizationButton.ADITIONAL_REQUEST_PARAMS).split(",");

            String name, value;
            if (paramString.length > 0) {

                for (int i = 0; i < paramString.length; i++) {
                    name = paramString[i].split("=")[0];
                    value = paramString[i].split("=")[1];
                    oForm.getAditionalParameters().put(name, value);
                }

            } else {

                name = paramString[0].split("=")[0];
                value = paramString[1].split("=")[1];
                oForm.getAditionalParameters().put(name, value);
            }
            
        }

        Collection<AmpOrgType> types;
        if (request.getParameter(AddOrganizationButton.PARAM_DONOR_GROUP_LIST) != null
                && !request.getParameter(AddOrganizationButton.PARAM_DONOR_GROUP_LIST).equals("")) {
            oForm.setOrgTypes(this.getOrgGroupList(request.getParameter(AddOrganizationButton.PARAM_DONOR_GROUP_LIST)));
            oForm.setFilterDonorGroups(true);
        } else {
            oForm.setOrgTypes(DbUtil.getAllOrgTypes());
            oForm.setFilterDonorGroups(false);
        }
        oForm.setTempNumResults(10);
        return mapping.findForward("forward");

    }

    private static long[] getSelectedItems(selectOrganizationComponentForm form) {
        String selectedItemsParam = form.getExcludedOrgIdsSeparated();
        long[] selectedOrganizations = null;
        if (selectedItemsParam != null && selectedItemsParam.length() > 0) {
            try {
                String[] selectedOrganizationsAsStrings = selectedItemsParam.split("_");
                selectedOrganizations = new long[selectedOrganizationsAsStrings.length];
                for (int i = 0; i < selectedOrganizationsAsStrings.length; i++) {
                    selectedOrganizations[i] = Long.parseLong(selectedOrganizationsAsStrings[i]);
                }
            } catch (RuntimeException ex){
                // if something went wrong during conversion let's null all the collection to be on the safe side
                selectedOrganizations = null;
            }
        }
        return selectedOrganizations;
    }

    @SuppressWarnings("unchecked")
    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        selectOrganizationComponentForm eaForm = (selectOrganizationComponentForm) form;
        // EditActivityForm eaForm = (EditActivityForm) form;
        String alpha = request.getParameter("alpha");
        Collection<AmpOrganisation> organizationResult = null;
        Collection<AmpOrganisation> alphaResult = null;
        if (eaForm.getTempNumResults() != 0) {
            eaForm.setNumResults(eaForm.getTempNumResults());
        } else {
            eaForm.setNumResults(10);
        }

        eaForm.setPagesToShow(10);

        if (alpha == null || alpha.trim().length() == 0) {
            //in case of "viewAll" selected organizations shouldn't get lost. in other case(applying above filter), it should be reseted
            if(request.getParameter("viewAll")==null || ! request.getParameter("viewAll").equalsIgnoreCase("viewAll")){
                eaForm.setSelOrganisations(null);
                eaForm.setAllSelectedOrgsIds(null);
            }else if(request.getParameter("viewAll")!=null && request.getParameter("viewAll").equalsIgnoreCase("viewAll")){
                eaForm.setNumResults(100000);
                fillAllSelectedOgs(eaForm);
                if(eaForm.getAllSelectedOrgsIds()!=null && eaForm.getAllSelectedOrgsIds().size()>0){
                    eaForm.setSelOrganisations(eaForm.getAllSelectedOrgsIds().toArray(new Long[eaForm.getAllSelectedOrgsIds().size()]));
                }
            }
            organizationResult = new ArrayList();

            long[] selectedOrganizations = getSelectedItems(eaForm);

            if (!eaForm.getAmpOrgTypeId().equals(new Long(-1))) {
                if (eaForm.getKeyword().trim().length() != 0) {
                    // serach for organisations based on the keyword and the
                    // organisation type
                    organizationResult = DbUtil.searchForOrganisation(eaForm.getKeyword().trim(), eaForm.getAmpOrgTypeId(), selectedOrganizations);
                } else {
                    // search for organisations based on organisation type only
                    organizationResult = DbUtil.searchForOrganisationByType(eaForm.getAmpOrgTypeId(), selectedOrganizations);
                }
            } else if (eaForm.getKeyword().trim().length() != 0) {
                // search based on the given keyword only.
                organizationResult = DbUtil.searchForOrganisation(eaForm.getKeyword().trim(), selectedOrganizations);
            } else {
                // get all organisations since keyword field is blank and org
                // type field has 'ALL'.
                organizationResult = DbUtil.getAmpOrganisations(selectedOrganizations);
            }

            if (organizationResult != null && organizationResult.size() > 0) {
                List<AmpOrganisation> temp = (List<AmpOrganisation>) organizationResult;
                Collections.sort(temp);
                organizationResult = (Collection<AmpOrganisation>) temp;

                if (eaForm.getCurrentAlpha() != null) {
                    eaForm.setCurrentAlpha(null);

                }
                eaForm.setStartAlphaFlag(true);

                String[] alphaArray = new String[26];
                int i = 0;

                for (char c = 'A'; c <= 'Z'; c++) {

                    for (AmpOrganisation org : organizationResult) {
                        if (org.getName().toUpperCase().indexOf(c) == 0) {
                            alphaArray[i++] = String.valueOf(c);
                            break;
                        }
                    }

                }
                eaForm.setAlphaPages(alphaArray);
            } else {
                eaForm.setAlphaPages(null);
            }

        } else {
            //already selected orgs shouldn't get lost
            fillAllSelectedOgs(eaForm);
            if( eaForm.getKeyword()!=null && !"".equals(eaForm.getKeyword().trim()) ) organizationResult=eaForm.getAllOrganization();
            else{
                if (!eaForm.getAmpOrgTypeId().equals(new Long(-1))) {
                    if (eaForm.getKeyword()!=null && eaForm.getKeyword().trim().length()!=0){
                        organizationResult = DbUtil.searchForOrganisation(alpha, eaForm.getKeyword().trim(), eaForm.getAmpOrgTypeId());
                    } else{
                        organizationResult = DbUtil.searchForOrganisation(alpha, "", eaForm.getAmpOrgTypeId());
                    }
                } else {
                    organizationResult = DbUtil.searchForOrganisation(alpha, "");
                }
            }
            
            eaForm.setCurrentAlpha(alpha);
            if (!alpha.equals("viewAll")) {
                eaForm.setStartAlphaFlag(false);
                alphaResult = new ArrayList();
                if (organizationResult != null )
                {
                    Iterator itr = organizationResult.iterator();
                    while (itr.hasNext()) {
                        AmpOrganisation org = (AmpOrganisation) itr.next();
                        if (org.getName().toUpperCase().startsWith(alpha)) {
                            alphaResult.add(org);
                        }
                    }
                    eaForm.setColsAlpha(alphaResult);
                }
            } else
                eaForm.setStartAlphaFlag(true);
        }

        eaForm.setAllOrganization(organizationResult);

        int stIndex = 1;
        int edIndex = eaForm.getNumResults();
        Vector vect = new Vector();
        int numPages;

        if (alpha == null || alpha.trim().length() == 0 || alpha.equals("viewAll")) {
            if (edIndex > organizationResult.size()) {
                edIndex = organizationResult.size();
            }
            vect.addAll(organizationResult);
            numPages = organizationResult.size() / eaForm.getNumResults();
            numPages += (organizationResult.size() % eaForm.getNumResults() != 0) ? 1 : 0;
        } else {
            if (edIndex > alphaResult.size()) {
                edIndex = alphaResult.size();
            }
            vect.addAll(alphaResult);
            numPages = alphaResult.size() / eaForm.getNumResults();
            numPages += (alphaResult.size() % eaForm.getNumResults() != 0) ? 1 : 0;
        }

        Collection filteredResult = new ArrayList();
        for (int i = (stIndex - 1); i < edIndex; i++) {
            filteredResult.add(vect.get(i));
        }

        Collection pages = null;

        if (numPages > 1) {
            pages = new ArrayList();
            for (int i = 0; i < numPages; i++) {
                Integer pageNum = i + 1;
                pages.add(pageNum);
            }
        }
        eaForm.setOrganizations(filteredResult);
        eaForm.setPages(pages);
        eaForm.setCurrentPage(1);

        fillSelectedOrgIds(eaForm);
        return mapping.findForward("forward");

    }

    public ActionForward selectPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        selectOrganizationComponentForm eaForm = (selectOrganizationComponentForm) form;

        int page = 0;
        if (request.getParameter("page") == null) {
            page = 1;
        } else {
            page = Integer.parseInt(request.getParameter("page"));
        }

        if (eaForm.getNumResults() == 0) {
            eaForm.setTempNumResults(10);
            if (request.getParameter(AddOrganizationButton.PARAM_DONOR_GROUP_LIST) != null
                    && !request.getParameter(AddOrganizationButton.PARAM_DONOR_GROUP_LIST).equals("")) {
                eaForm.setOrgTypes(this.getOrgGroupList(request
                        .getParameter(AddOrganizationButton.PARAM_DONOR_GROUP_LIST)));
                eaForm.setFilterDonorGroups(true);
            } else {
                eaForm.setFilterDonorGroups(false);
            }
            if (eaForm.getAlphaPages() != null)
                eaForm.setAlphaPages(null);
        } else {
            int stIndex = ((page - 1) * eaForm.getNumResults()) + 1;
            int edIndex = page * eaForm.getNumResults();

            if (eaForm.isStartAlphaFlag()) {
                if (edIndex > eaForm.getAllOrganization().size()) {
                    edIndex = eaForm.getAllOrganization().size();
                }
            } else {
                if (edIndex > eaForm.getColsAlpha().size()) {
                    edIndex = eaForm.getColsAlpha().size();
                }
            }

            Vector vect = new Vector();

            if (eaForm.isStartAlphaFlag())
                vect.addAll(eaForm.getAllOrganization());
            else
                vect.addAll(eaForm.getColsAlpha());

            Collection tempCol = new ArrayList();

            for (int i = (stIndex - 1); i < edIndex; i++) {
                tempCol.add(vect.get(i));
            }
            
            fillAllSelectedOgs(eaForm);

            eaForm.setOrganizations(tempCol);
            eaForm.setCurrentPage(new Integer(page));
        }

        fillSelectedOrgIds(eaForm);
        return mapping.findForward("forward");
    }

    private void fillSelectedOrgIds(selectOrganizationComponentForm eaForm) {
        List<Long> allSelectedOrgsIds = eaForm.getAllSelectedOrgsIds();
        eaForm.setSelOrganisations(allSelectedOrgsIds == null ? new Long[0] : allSelectedOrgsIds.toArray(new Long[0]));
    }

    public ActionForward setOrganizationToForm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        selectOrganizationComponentForm eaForm = (selectOrganizationComponentForm) form;

        Long id = Long.parseLong(request.getParameter("id"));

        AmpOrganisation org = DbUtil.getOrganisation(id);

        if (!eaForm.isUseClient()) {
            Field target = eaForm.getTargetForm().getClass().getDeclaredField(eaForm.getTargetProperty());
            target.setAccessible(true);

            target.set(eaForm.getTargetForm(), org);
            eaForm.setAfterSelect(true);
        }

        return mapping.findForward("forward");

    }

    public ActionForward addOrganizationToForm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        selectOrganizationComponentForm eaForm = (selectOrganizationComponentForm) form;

        if (!eaForm.isUseClient()) {
            Field target = eaForm.getTargetForm().getClass().getDeclaredField(eaForm.getTargetCollection());
            target.setAccessible(true);
            Collection<AmpOrganisation> targetCollecion = (Collection<AmpOrganisation>) target.get(eaForm.getTargetForm());

            if (targetCollecion == null) {
                targetCollecion = new TreeSet<AmpOrganisation>(); //Don't change this! otherwise contains function will compare objects using equals,which is not overriden in AmpOrganisation
            }

            List<Long> orgIds = eaForm.getAllSelectedOrgsIds();
            if(orgIds!=null && orgIds.size()>0){
                for (Long orgId : orgIds) {
                    AmpOrganisation org = DbUtil.getOrganisation(orgId);
                    if (!targetCollecion.contains(org)) {
                        targetCollecion.add(org);
                    }
                }
            }           
            target.set(eaForm.getTargetForm(), targetCollecion);
            eaForm.setAfterSelect(true);
        }
        return mapping.findForward("forward");
    }

    public ActionForward executeDelegate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        selectOrganizationComponentForm eaForm = (selectOrganizationComponentForm) form;
        String className = eaForm.getDelegateClass();
        Class clazz = java.lang.Class.forName(className);
        Constructor constructor = clazz.getDeclaredConstructor(new Class[] {});
        constructor.setAccessible(true);
        IPostProcessDelegate processor = (IPostProcessDelegate) constructor.newInstance(new Object[] {});
        return processor.execute(mapping, form, request, response);
    }
    
    private Collection<AmpOrgType> getOrgGroupList(String donorGroups) {
        Collection<AmpOrgType> list = new ArrayList<AmpOrgType>();
        Collection<AmpOrgType> auxList = new ArrayList<AmpOrgType>();
        if (donorGroups != null && !donorGroups.equals("")) {
            String[] paramString = donorGroups.split(",");
            for (int i = 0; i < paramString.length; i++) {
                AmpOrgType type = DbUtil.getAmpOrgTypeByCode(paramString[i]);
                if (type != null) {
                    list.add(type);
                }
            }
        }
        return list;
    }

    private void fillAllSelectedOgs(selectOrganizationComponentForm oForm) {
        if(oForm.getSelOrganisations()!=null && oForm.getSelOrganisations().length > 0){
            if(oForm.getAllSelectedOrgsIds()==null){
                oForm.setAllSelectedOrgsIds(new ArrayList<Long>());
            }
            for (Long orgId : oForm.getSelOrganisations()) {
                if(! oForm.getAllSelectedOrgsIds().contains(orgId)){
                    oForm.getAllSelectedOrgsIds().add(orgId);
                }                       
            }
        }
    }
}
