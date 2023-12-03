package org.digijava.module.aim.dbentity ;

import org.apache.log4j.Logger;
import org.digijava.module.aim.annotations.reports.Identificator;

import java.io.Serializable;
import java.util.Set;

public class AmpColumns implements Serializable, Comparable<AmpColumns>
{
    protected static final Logger logger = Logger.getLogger(AmpColumns.class);
    
    @Identificator
    private Long columnId ;
    private String columnName ;
    //private String columnNameTrimmed;
    private String aliasName;
    private Set reports;
    private String cellType;
    private String extractorView;
    private Set<AmpColumnsFilters> filters;
    private String tokenExpression;
    
    // header calculations
    private String totalExpression;
    private Boolean showRowCalculations;
    
    private String relatedContentPersisterClass;
    private String description;
    /**
     * true if the column data is needed in order for correct filtering to be applied
     * @see http://bugs.digijava.org/jira/browse/AMP-3454?focusedCommentId=39811#action_39811
     */
    private Boolean filterRetrievable;
    


    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getFilterRetrievable() {
        return filterRetrievable;
    }
    public void setFilterRetrievable(Boolean retrieveIfFiltered) {
        this.filterRetrievable = retrieveIfFiltered;
    }
    public String getRelatedContentPersisterClass() {
        return relatedContentPersisterClass;
    }
    public void setRelatedContentPersisterClass(String relatedContentPersisterClass) {
        this.relatedContentPersisterClass = relatedContentPersisterClass;
    }
    public String getAliasName() {
        return aliasName;
    }
    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
    public Long getColumnId() {
        return columnId;
    }
    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }
    public String getColumnName() {
        return columnName;
    }
    
    /**
     * @deprecated
     * DO NOT CALL IT!!! For Hibernate's use only!
     * @param columnName
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    public Set getReports() {
        return reports;
    }
    public void setReports(Set reports) {
        this.reports = reports;
    }
    /**
     * @return Returns the cellType.
     */
    public String getCellType() {
        return cellType;
    }
    /**
     * @param cellType The cellType to set.
     */
    public void setCellType(String extractorClass) {
        this.cellType = extractorClass;
    }
    /**
     * @return Returns the extractorView.
     */
    public String getExtractorView() {
        return extractorView;
    }
    /**
     * @param extractorView The extractorView to set.
     */
    public void setExtractorView(String extractorView) {
        this.extractorView = extractorView;
    }
    public String getColumnNameTrimmed() {
        return columnName.replaceAll(" ", "");
    }
    /*public void setColumnNameTrimmed(String columnNameTrimmed) {
        this.columnNameTrimmed = columnNameTrimmed;
    }*/
    
    @Override
    public int compareTo(AmpColumns oth) {
        return this.getColumnName().compareTo(oth.getColumnName());
    }
    
    public String toString(){
        return columnName;
    }
    public Set<AmpColumnsFilters> getFilters() {
        return filters;
    }
    public void setFilters(Set<AmpColumnsFilters> filters) {
        this.filters = filters;
    }
    public String getTokenExpression() {
        return tokenExpression;
    }
    public void setTokenExpression(String tokenExpression) {
        this.tokenExpression = tokenExpression;
    }

    public String getTotalExpression() {
        return totalExpression;
    }

    public void setTotalExpression(String totalExpression) {
        this.totalExpression = totalExpression;
    }

    public Boolean isShowRowCalculations() {
        return showRowCalculations;
    }

    public void setShowRowCalculations(Boolean showRowCalculations) {
        this.showRowCalculations = showRowCalculations;
    }
    
    public AmpColumns makeCopy() {
        AmpColumns clonedCol = new AmpColumns();
        clonedCol.columnName = this.getColumnName();
        clonedCol.setColumnId( this.getColumnId() );
        clonedCol.setAliasName( this.getAliasName() );
        clonedCol.setCellType( this.getCellType() );
        clonedCol.setDescription( this.getDescription() );
        clonedCol.setExtractorView( this.getExtractorView() );
        clonedCol.setFilterRetrievable( this.getFilterRetrievable() );
        clonedCol.setRelatedContentPersisterClass( this.getRelatedContentPersisterClass() );
        clonedCol.setTokenExpression( this.getTokenExpression() );
        clonedCol.setTotalExpression( this.getTotalExpression() );
        return clonedCol;
    }
    
    public boolean isMtefColumn() {
        try {return getMtefYear("mtef") != null;}
        catch(Exception e) {
            logger.error("potentially inconsistent MTEF column definition", e);
            return false;
        }
    }

    public boolean isRealMtefColumn() {
        try {return getMtefYear("realmtef") != null;}
        catch(Exception e) {
            logger.error("potentially inconsistent MTEF column definition", e);
            return false;
        }
    }
    
    public Integer getMtefYear(String prefix) {
        if (this.getAliasName() == null || !this.getAliasName().toLowerCase().startsWith(prefix))
            return null;
        return Integer.parseInt(this.getAliasName().substring(prefix.length())); // intentionally crash if input isn't good
    }
}
