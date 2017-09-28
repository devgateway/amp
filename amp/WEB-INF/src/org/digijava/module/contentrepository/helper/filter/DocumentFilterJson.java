package org.digijava.module.contentrepository.helper.filter;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.KeyValue;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.dbentity.filter.DocumentFilter;
import org.digijava.module.contentrepository.jcrentity.Label;
import org.digijava.module.contentrepository.jcrentity.LabelDAO;

public class DocumentFilterJson {
    private List<Label> filterLabels;
    private List<KeyValue> filterKeywords;
    
    private List<KeyValue> filterDocTypeIds;
    private List<KeyValue> filterFileTypes;
    
    private List<KeyValue> filterTeamIds;
    private List<KeyValue> filterOwners;
    
    private List<KeyValue> filterFromDate;
    private List<KeyValue> filterToDate;
    
    public DocumentFilterJson(DocumentFilter df, HttpServletRequest request) {
        this.filterLabels   = new ArrayList<Label>();
        if (df.getFilterLabelsUUID() != null) {
            for (String uuid: df.getFilterLabelsUUID() ) {
                LabelDAO lDAO   = new LabelDAO(request, false);
                Label label     = lDAO.getLabel(uuid);
                this.filterLabels.add(label);
            }
        }
        
        this.filterKeywords = new ArrayList<KeyValue>();
        if (df.getFilterKeywords()!= null) {
            for (String kw: df.getFilterKeywords()) {
                this.filterKeywords.add(new KeyValue(kw, kw));
            }
        }
        
        this.filterDocTypeIds   = new ArrayList<KeyValue>();
        if ( df.getFilterDocTypeIds() != null ) {
            for ( Long id: df.getFilterDocTypeIds() ) {
                AmpCategoryValue cv     = CategoryManagerUtil.getAmpCategoryValueFromDb(id);
                String value            = CategoryManagerUtil.translateAmpCategoryValue(cv);
                this.filterDocTypeIds.add( new KeyValue(id+"", value) );
            }
        }
        
        this.filterFileTypes        = new ArrayList<KeyValue>();
        if ( df.getFilterFileType() != null) {
            for (String type: df.getFilterFileType() ) {
                this.filterFileTypes.add( new KeyValue(type, type) );
            }
        }
        
        this.filterTeamIds      = new ArrayList<KeyValue>();
        if ( df.getFilterTeamIds() != null ) {
            for ( Long id: df.getFilterTeamIds() ) {
                AmpTeam team    = TeamUtil.getAmpTeam(id);
                this.filterTeamIds.add( new KeyValue(id+"", team.getName()) );
            }
        }
        
        this.filterOwners       = new ArrayList<KeyValue>();
        if ( df.getFilterOwners() != null ) {
            for ( String email: df.getFilterOwners() ) {
                this.filterOwners.add( new KeyValue(email, email) );
            }
        }
        
        this.filterFromDate     = new ArrayList<KeyValue>();
        if ( df.getFilterFromDate() != null ) {
            this.filterFromDate.add( new KeyValue(df.getFilterFromDate(), df.getFilterFromDate()) );
        }
        
        this.filterToDate       = new ArrayList<KeyValue>();
        if ( df.getFilterToDate() != null ) {
            this.filterToDate.add( new KeyValue(df.getFilterToDate(), df.getFilterToDate()) );
        }
        
    }
    
    public List<Label> getFilterLabels() {
        return filterLabels;
    }
    public void setFilterLabels(List<Label> filterLabels) {
        this.filterLabels = filterLabels;
    }
    public List<KeyValue> getFilterKeywords() {
        return filterKeywords;
    }
    public void setFilterKeywords(List<KeyValue> filterKeywords) {
        this.filterKeywords = filterKeywords;
    }
    public List<KeyValue> getFilterDocTypeIds() {
        return filterDocTypeIds;
    }
    public void setFilterDocTypeIds(List<KeyValue> filterDocTypeIds) {
        this.filterDocTypeIds = filterDocTypeIds;
    }
    public List<KeyValue> getFilterFileTypes() {
        return filterFileTypes;
    }
    public void setFilterFileTypes(List<KeyValue> filterFileTypes) {
        this.filterFileTypes = filterFileTypes;
    }
    public List<KeyValue> getFilterTeamIds() {
        return filterTeamIds;
    }
    public void setFilterTeamIds(List<KeyValue> filterTeamIds) {
        this.filterTeamIds = filterTeamIds;
    }
    public List<KeyValue> getFilterOwners() {
        return filterOwners;
    }
    public void setFilterOwners(List<KeyValue> filterOwners) {
        this.filterOwners = filterOwners;
    }

    public List<KeyValue> getFilterFromDate() {
        return filterFromDate;
    }

    public void setFilterFromDate(List<KeyValue> filterFromDate) {
        this.filterFromDate = filterFromDate;
    }

    public List<KeyValue> getFilterToDate() {
        return filterToDate;
    }

    public void setFilterToDate(List<KeyValue> filterToDate) {
        this.filterToDate = filterToDate;
    }
    
    
}
