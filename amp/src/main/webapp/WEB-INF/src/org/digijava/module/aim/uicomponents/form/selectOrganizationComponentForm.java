package org.digijava.module.aim.uicomponents.form;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;

public class selectOrganizationComponentForm extends ActionForm {
    private static final long serialVersionUID = 0L;
    private Collection<AmpOrgType> orgTypes = null;
    private Long ampOrgTypeId = null;
    private int tempNumResults;
    private int numResults;
    private String keyword="";
    private Collection<AmpOrganisation> allOrganization = null;

    private Collection<AmpOrganisation> organizations = null;
    private Long ampOrgId;
    private String currentAlpha = null;
    private String[] alphaPages;
    boolean startAlphaFlag = false;
    private Collection colsAlpha;
    private Long[] selOrganisations;
    private Integer pagesToShow;
    private Integer startPage;
    Collection pages;
    private int currentPage;
    private int pagesSize;
    boolean orgSelReset;
    boolean multiSelect = true;
    boolean afterSelect = false;
    String targetCollection="";
    String targetProperty="";
    String callbackFunction="";
    Object   targetForm;
    boolean refreshParent;
    private String delegateClass;
    private HashMap<String, String> aditionalParameters=new HashMap<String, String>();
    private boolean filterDonorGroups;

    /*
     * used to hold all selected organisations (when going to another page, previously selected orgs were lost)
     */
    private List<Long> allSelectedOrgsIds;


    /*
     * List of org Ids that will be excluded from search results. IDs are separated with "_"
     */
    private String excludedOrgIdsSeparated;
    public static final String EXCLUDED_ORG_IDS_SEPARATED = "excludedOrgIdsSeparated";
    
    String styleClass="";
    
    public HashMap<String, String> getAditionalParameters() {
        return aditionalParameters;
    }

    public void setAditionalParameters(HashMap<String, String> aditionalParameters) {
        this.aditionalParameters = aditionalParameters;
    }

    boolean useClient;
    private String valueHoder="";
    private String nameHolder="";
    boolean useAcronym;
    
    public boolean isUseAcronym() {
        return useAcronym;
    }

    public void setUseAcronym(boolean useAcronym) {
        this.useAcronym = useAcronym;
    }

    public boolean isUseClient() {
        return useClient;
    }

    public void setUseClient(boolean useClient) {
        this.useClient = useClient;
    }

    public String getValueHoder() {
        return valueHoder;
    }

    public void setValueHoder(String valueHoder) {
        this.valueHoder = valueHoder;
    }

    public String getNameHolder() {
        return nameHolder;
    }

    public void setNameHolder(String nameHolder) {
        this.nameHolder = nameHolder;
    }

    public boolean isRefreshParent() {
        return refreshParent;
    }

    public void setRefreshParent(boolean refreshParent) {
        this.refreshParent = refreshParent;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        // TODO Auto-generated method stub
        super.reset(mapping, request);
        afterSelect = false;
    }

    public void clearSelected() {
        allSelectedOrgsIds = null;
        selOrganisations = null;
        this.organizations=null;
        this.alphaPages =  null;
        this.keyword="";
        this.ampOrgTypeId = null;
        delegateClass ="";
        callbackFunction = null;
    }

    private int selectedOrganisationFromPages;

    public int getSelectedOrganisationFromPages() {
        return selectedOrganisationFromPages;
    }

    public void setSelectedOrganisationFromPages(int selectedOrganisationFromPages) {
        this.selectedOrganisationFromPages = selectedOrganisationFromPages;
    }

    public Collection getColsAlpha() {
        return colsAlpha;
    }

    public void setColsAlpha(Collection colsAlpha) {
        this.colsAlpha = colsAlpha;
    }

    public Collection<AmpOrgType> getOrgTypes() {
        return orgTypes;
    }

    public void setOrgTypes(Collection<AmpOrgType> orgTypes) {
        this.orgTypes = orgTypes;
    }

    public Long getAmpOrgTypeId() {
        return ampOrgTypeId;
    }

    public void setAmpOrgTypeId(Long ampOrgTypeId) {
        this.ampOrgTypeId = ampOrgTypeId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getTempNumResults() {
        return tempNumResults;
    }

    public void setTempNumResults(int tempNumResults) {
        this.tempNumResults = tempNumResults;
    }

    public Collection<AmpOrganisation> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Collection<AmpOrganisation> organizations) {
        this.organizations = organizations;
    }

    public Long getAmpOrgId() {
        return ampOrgId;
    }

    public void setAmpOrgId(Long ampOrgId) {
        this.ampOrgId = ampOrgId;
    }

    public String getCurrentAlpha() {
        return currentAlpha;
    }

    public void setCurrentAlpha(String currentAlpha) {
        this.currentAlpha = currentAlpha;
    }

    public String[] getAlphaPages() {
        return alphaPages;
    }

    public void setAlphaPages(String[] alphaPages) {
        this.alphaPages = alphaPages;
    }

    public boolean isStartAlphaFlag() {
        return startAlphaFlag;
    }

    public void setStartAlphaFlag(boolean startAlphaFlag) {
        this.startAlphaFlag = startAlphaFlag;
    }

    public int getNumResults() {
        return numResults;
    }

    public void setNumResults(int numResults) {
        this.numResults = numResults;
    }

    public Collection getPages() {
        return pages;
    }

    public void setPages(Collection pages) {
        this.pages = pages;
        if (pages != null) {
            this.pagesSize = pages.size();
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public Long[] getSelOrganisations() {
        return selOrganisations;
    }

    public void setSelOrganisations(Long[] selOrganisations) {
        this.selOrganisations = selOrganisations;
    }

    public Integer getPagesToShow() {
        return pagesToShow;
    }

    public void setPagesToShow(Integer pagesToShow) {
        this.pagesToShow = pagesToShow;
    }

    public int getStartPage() {
        int value;
        if (getCurrentPage() > (this.getPagesToShow() / 2)) {
            value = (this.getCurrentPage() - (this.getPagesToShow() / 2)) - 1;
        } else {
            value = 0;
        }
        this.startPage = value;
        return startPage;
    }

    public void setStartPage(Integer startPage) {
        this.startPage = startPage;
    }

    public int getPagesSize() {
        return pagesSize;
    }

    public void setPagesSize(int pagesSize) {
        this.pagesSize = pagesSize;
    }

    public Collection<AmpOrganisation> getAllOrganization() {
        return allOrganization;
    }

    public void setAllOrganization(Collection<AmpOrganisation> allOrganization) {
        this.allOrganization = allOrganization;
    }

    public boolean isOrgSelReset() {
        return orgSelReset;
    }

    public void setOrgSelReset(boolean orgSelReset) {
        this.orgSelReset = orgSelReset;
    }

    public boolean getMultiSelect() {
        return multiSelect;
    }

    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    public Object getTargetForm() {
        return targetForm;
    }

    public void setTargetForm(Object targetForm) {
        this.targetForm = targetForm;
    }

    public String getCallbackFunction() {
        return callbackFunction;
    }

    public void setCallbackFunction(String callbackFunction) {
        this.callbackFunction = callbackFunction;
    }

    public String getTargetCollection() {
        return targetCollection;
    }

    public void setTargetCollection(String targetCollection) {
        this.targetCollection = targetCollection;
    }

    public String getTargetProperty() {
        return targetProperty;
    }

    public void setTargetProperty(String targetProperty) {
        this.targetProperty = targetProperty;
    }

    public boolean getAfterSelect() {
        return afterSelect;
    }

    public void setAfterSelect(boolean afterSelect) {
        this.afterSelect = afterSelect;
    }

    public String getDelegateClass() {
        return delegateClass;
    }

    public void setDelegateClass(String delegateClass) {
        this.delegateClass = delegateClass;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public List<Long> getAllSelectedOrgsIds() {
        return allSelectedOrgsIds;
    }

    public void setAllSelectedOrgsIds(List<Long> allSelectedOrgsIds) {
        this.allSelectedOrgsIds = allSelectedOrgsIds;
    }

    public boolean isFilterDonorGroups() {
        return filterDonorGroups;
    }

    public void setFilterDonorGroups(boolean filterDonorGroups) {
        this.filterDonorGroups = filterDonorGroups;
    }

    public String getExcludedOrgIdsSeparated() {
        return excludedOrgIdsSeparated;
    }

    public void setExcludedOrgIdsSeparated(String excludedOrgIdsSeparated) {
        this.excludedOrgIdsSeparated = excludedOrgIdsSeparated;
    }
    
    
}
