package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.module.aim.annotations.reports.Identificator;
import javax.persistence.*;

@Entity
@Table(name = "AMP_COLUMNS")
public class AmpColumns implements Serializable, Comparable<AmpColumns>
{
    protected static final Logger logger = Logger.getLogger(AmpColumns.class);
    @Id
    @Identificator

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_columns_seq")
    @SequenceGenerator(name = "amp_columns_seq", sequenceName = "AMP_COLUMNS_seq", allocationSize = 1)
    @Column(name = "columnId")
    private Long columnId;

    @Column(name = "columnName", unique = true)
    private String columnName;

    @Column(name = "aliasName")
    private String aliasName;

    @Column(name = "cellType")
    private String cellType;

    @Column(name = "extractorView")
    private String extractorView;

    @Column(name = "tokenExpression")
    private String tokenExpression;

    @Column(name = "totalExpression")
    private String totalExpression;

    @Column(name = "relatedContentPersisterClass")
    private String relatedContentPersisterClass;

    @Column(name = "filterRetrievable")
    private Boolean filterRetrievable;

    @Column(name = "showRowCalculations")
    private Boolean showRowCalculations;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @OneToMany(mappedBy = "column", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AmpColumnsFilters> filters;
@Transient
    private Set reports;


    


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
