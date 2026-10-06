package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpOrgGroup;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public class OrgGroupManagerForm extends ActionForm {
   
    private Long ampOrgTypeId;
    private String orgType;
    private String keyword;
    private int numResults;
    private int tempNumResults;
    private Collection orgTypes = null;
    private Collection<AmpOrgGroup> organisation;
    private Collection pages;
    private Integer currentPage=new Integer(1);
    private String sortBy;
    private String currentAlpha;
    private String[] alphaPages = null;
    private String alpha;
    private Collection<AmpOrgGroup> orgsForCurrentAlpha;
    private boolean reset;
    private boolean orgSelReset;
    private boolean showDeleted;
    
    public OrgGroupManagerForm(){
        reset = false;
        numResults = 0;
        tempNumResults = 0;
    }
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if (reset) {
              numResults = 0;
              orgTypes = null;
              pages = null;
              alphaPages = null;
              reset = false;
              currentPage = new Integer(0);
              currentAlpha = null;
             
            }

            if (orgSelReset) {
              keyword = null;
              setOrgType("");
              setAmpOrgTypeId(null);
              setTempNumResults(10);             
            }
        
    }
    
    public boolean isOrgSelReset() {
        return orgSelReset;
      }

    public void setOrgSelReset(boolean orgSelReset) {
        this.orgSelReset = orgSelReset;
      }
    
    public Collection<AmpOrgGroup> getOrgsForCurrentAlpha() {
        return orgsForCurrentAlpha;
    }

    public void setOrgsForCurrentAlpha(Collection<AmpOrgGroup> orgsForCurrentAlpha) {
        this.orgsForCurrentAlpha = orgsForCurrentAlpha;
    }

    public Collection<AmpOrgGroup> getOrganisation() {
         return organisation;
    }

    public void setOrganisation(Collection<AmpOrgGroup> organisation) {
         this.organisation = organisation;
    }

    public Collection getPages() {
         return pages;
    }

    public void setPages(Collection pages) {
         this.pages = pages;
    }

    public Integer getCurrentPage() {
        if(currentPage==null) return new Integer(1);
        return currentPage;
    }

    public void setCurrentPage(Integer currentpage) {
        this.currentPage = currentpage;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
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

    public String getAlpha() {
        return alpha;
    }

    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }

    public Long getAmpOrgTypeId() {
        return ampOrgTypeId;
    }

    public void setAmpOrgTypeId(Long ampOrgTypeId) {
        this.ampOrgTypeId = ampOrgTypeId;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getNumResults() {
        return numResults;
    }

    public void setNumResults(int numResults) {
        this.numResults = numResults;
    }

    public int getTempNumResults() {
        return tempNumResults;
    }

    public void setTempNumResults(int tempNumResults) {
        this.tempNumResults = tempNumResults;
    }

    public Collection getOrgTypes() {
        return orgTypes;
    }

    public void setOrgTypes(Collection orgTypes) {
        this.orgTypes = orgTypes;
    }

    public boolean isShowDeleted() {
        return showDeleted;
    }

    public void setShowDeleted(boolean showDeleted) {
        this.showDeleted = showDeleted;
    }
    
}
