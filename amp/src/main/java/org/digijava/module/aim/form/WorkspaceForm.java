package org.digijava.module.aim.form;

import java.util.Collection;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.form.helpers.WorkspaceDataSelection;

public class WorkspaceForm extends ActionForm {

    private Collection workspaces;

    private Collection pages;

    private int page;
    
    private String workspaceType="all";

    private List<WorkspaceDataSelection> workspaceDataSelections;
    
    private int numPerPage=-1;
    
    private String keyword;
        
        private Integer currentRow;
        private Integer currentPage;
        private Long selectedWs;


        public Integer getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(Integer currentPage) {
            this.currentPage = currentPage;
        }

        public Integer getCurrentRow() {
            return currentRow;
        }

        public void setCurrentRow(Integer currentRow) {
            this.currentRow = currentRow;
        }

        public Long getSelectedWs() {
            return selectedWs;
        }

        public void setSelectedWs(Long selectedWs) {
            this.selectedWs = selectedWs;
        }

    private Long workspaceGroup;
    
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getNumPerPage() {
        return numPerPage;
    }

    public void setNumPerPage(int numPerPage) {
        this.numPerPage = numPerPage;
    }

    public String getWorkspaceType() {
        return workspaceType;
    }

    public void setWorkspaceType(String workspaceType) {
        this.workspaceType = workspaceType;
    }

    /**
     * @return Returns the page.
     */
    public int getPage() {
        return page;
    }

    /**
     * @param page The page to set.
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * @return Returns the pages.
     */
    public Collection getPages() {
        return pages;
    }

    /**
     * @param pages The pages to set.
     */
    public void setPages(Collection pages) {
        this.pages = pages;
    }

    /**
     * @return Returns the workspaces.
     */
    public Collection getWorkspaces() {
        return workspaces;
    }

    /**
     * @param workspaces The workspaces to set.
     */
    public void setWorkspaces(Collection workspaces) {
        this.workspaces = workspaces;
    }

    public void setWorkspaceGroup(Long workspaceGroup) {
        this.workspaceGroup = workspaceGroup;
    }

    public Long getWorkspaceGroup() {
        return workspaceGroup;
        
    }

    public List<WorkspaceDataSelection> getWorkspaceDataSelections() {
        return workspaceDataSelections;
    }

    public void setWorkspaceDataSelections(List<WorkspaceDataSelection> workspaceDataSelections) {
        this.workspaceDataSelections = workspaceDataSelections;
    }
}
