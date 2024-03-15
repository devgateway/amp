/*
 * AmpTeam.java
 * Created: 03-Sep-2004
 */

package org.digijava.module.aim.dbentity;

import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.dbentity.AmpFilterData;
import org.dgfoundation.amp.ar.dbentity.FilterDataSetInterface;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.annotations.translation.TranslatableClass;
import org.digijava.module.aim.annotations.translation.TranslatableField;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.*;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.common.util.DateTimeUtil;

import java.io.Serializable;
import java.util.*;

@TranslatableClass (displayName = "Report")
public class AmpReports implements Comparable<AmpReports>, LoggerIdentifiable, Serializable, Identifiable, Cloneable,
        FilterDataSetInterface<AmpFilterData> {

    private Long ampReportId;

    //private AmpARFilter defaultFilter;
    
    private transient ReportGenerator reportGenerator;
    @TranslatableField
    private String name;
    
    // private String description;
    @TranslatableField
    private String reportDescription;

    private String options;

    private Boolean hideActivities;

    private Boolean drilldownTab;

    private Boolean publicReport;
    
    private Boolean workspaceLinked;
    
    /**
     * see AMP-17746
     */
    private Boolean alsoShowPledges;
    
    /**
     * see AMP-17746
     */
    private Boolean onlyShowProjectsRelatedPledges;

    private Long type;

    // private AmpReportsOptions ampReportsOptions;
    private String description;

    /**
     * team reports oslt
     */
    private Set<AmpTeamMember> members;

    private Set<AmpReportColumn> columns;

    private List<AmpReportColumn> orderedColumns;

    private Set<AmpReportHierarchy> hierarchies;

    private Set<AmpReportMeasures> measures;

    private Set<AmpMeasures> reportMeasures;

    private AmpTeamMember ownerId; // the member that created the report

    private Date updatedDate; // last date when the report was modified

    /*
     *  to be set in order to get information for translation purposes in pdf and excel reports
     *  not serialized
     */
    private Long siteId;

    private String locale;

    //private AmpPages ampPage;

    private AmpCategoryValue activityLevel;

    private String user;
    
    private Set<AmpDesktopTabSelection> desktopTabSelections;
    
    private Set<AmpReportLog> logs;
    
    private Set<AmpFilterData> filterDataSet;
    
    private Boolean allowEmptyFundingColumns;
    
    private Boolean showOriginalCurrency;
    
    private Boolean budgetExporter = false;
    private Date publishedDate;
    
    private AmpCategoryValue reportCategory;

    private Boolean splitByFunding = false;

    public Boolean getSplitByFunding() {
        return splitByFunding;
    }

    public void setSplitByFunding(Boolean splitByFunding) {
        this.splitByFunding = splitByFunding;
    }

    public Set<AmpReportLog> getLogs() {
        return logs;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setLogs(Set<AmpReportLog> logs) {
        this.logs = logs;
    }

    public String getFormatedUpdatedDate() {
        String result = null;
        if (this.updatedDate != null) {
            result = DateTimeUtil.formatDate(this.updatedDate);
        }
        return result;
    }
    
    public String getFormatedPublishedDate() {
        String result = null;
        if (this.publishedDate != null) {
            result = DateTimeUtil.formatDate(this.publishedDate);
        }
        return result;
    }

    public AmpTeamMember getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(AmpTeamMember owner) {
        this.ownerId = owner;
    }

    public Set<AmpReportMeasures> getMeasures() {
        return measures;
    }

    public void setMeasures(Set<AmpReportMeasures> measures) {
        this.measures = measures;
    }

    public Long getAmpReportId() {
        return ampReportId;
    }

    public String getName() {
        return name;
    }

    /*
     * public String getDescription() { return description; }
     */

    public String getReportDescription() {
        return reportDescription;
    }

    public String getOptions() {
        return options;
    }

    /*
     * public AmpReportsOptions getAmpReportsOptions() { return
     * ampReportsOptions; }
     */

    public Set<AmpTeamMember> getMembers() {
        return members;
    }

    public void setAmpReportId(Long ampReportId) {
        this.ampReportId = ampReportId;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*
     * public void setDescription(String description) { this.description =
     * description; }
     */

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    /*
     * public void setAmpReportsOptions(AmpReportsOptions ampReportsOptions) {
     * this.ampReportsOptions = ampReportsOptions; }
     */

    public void setMembers(Set<AmpTeamMember> members) {
        this.members = members;
    }

    @Override public int compareTo(AmpReports rep) {    
        return ObjectUtil4Amp.nullSafeIgnoreCaseStringCompare(this.name, rep.getName(), true);
    }

    public Set<AmpReportColumn> getShowAblesColumns(){
        Set<AmpReportColumn> finalColumns=new HashSet<AmpReportColumn>();
        if (hideActivities){
            Long order=1l;
            for (AmpReportColumn element : columns) {
                boolean add=ARUtil.hasHierarchy(this.getHierarchies(), element.getColumn().getColumnName()) || ARUtil.hasHeaderValue(element.getColumn());
                if (add){
                    element.setOrderId(order);
                    finalColumns.add(element);
                    order ++;
                }
            }
        }else{
            finalColumns    = new TreeSet<AmpReportColumn>();
            finalColumns.addAll(columns);
        }   
        return finalColumns;
    }
    
    /**
     * @return the columns
     */
    public Set<AmpReportColumn> getColumns() {
        return columns;
    }

    /**
     * @param columns the columns to set
     */
    public void setColumns(Set<AmpReportColumn> columns) {
        this.columns = columns;
    }

    /**
     * @return the hierarchies
     */
    public Set<AmpReportHierarchy> getHierarchies() {
        return hierarchies;
    }
    /**
     * @return the hierarchies as an Array
     */
    public AmpReportHierarchy[] getHierarchiesArray() {
        return hierarchies.toArray(new AmpReportHierarchy[hierarchies.size()]);
    }

    /**
     * @param hierarchies the hierarchies to set
     */
    public void setHierarchies(Set<AmpReportHierarchy> hierarchies) {
        this.hierarchies = hierarchies;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    /**
     * @return Returns the orderedColumns.
     */
    public List<AmpReportColumn> getOrderedColumns() {
        return orderedColumns;
    }

    /**
     * @param orderedColumns
     *            The orderedColumns to set.
     */
    public void setOrderedColumns(List<AmpReportColumn> orderedColumns) {
        this.orderedColumns = orderedColumns;
    }


    /**
     * @return Returns the hideActivities.
     */
    public Boolean isHideActivities() {
        return hideActivities;
    }

    /**
     * @param hideActivities
     *            The hideActivities to set.
     */
    public void setHideActivities(Boolean hideActivities) {
        this.hideActivities = hideActivities;
    }

    /**
     * @return Returns the hideActivities.
     */
    public Boolean getHideActivities() {
        return hideActivities;
    }

    public Boolean getDrilldownTab() {
        return drilldownTab;
    }

    public void setDrilldownTab(Boolean drilldownTab) {
        this.drilldownTab = drilldownTab;
    }
    
    public boolean isTab() {
        return isTrueBoolean(drilldownTab);
    }

    public Boolean getPublicReport() {
        return publicReport;
    }

    public void setPublicReport(Boolean publicReport) {
        this.publicReport = publicReport;
    }

    public Boolean getWorkspaceLinked() {
        return workspaceLinked;
    }
    
    public void setWorkspaceLinked(Boolean workspaceLinked) 
    {
        this.workspaceLinked = workspaceLinked;
    }       
            
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    @Override public Object getObjectType() {
        return this.getClass().getName();
    }

    @Override public Object getIdentifier() {
        return this.getAmpReportId();
    }

    public Long getId() {
        return this.getAmpReportId();
    }

    @Override public String getObjectName() {
        return this.getName();
    }

    public String getNameTrn() {
        return this.name.toLowerCase().replaceAll(" ", "");
    }

    public Set<AmpMeasures> getReportMeasures() {
        return reportMeasures;
    }

    public void setReportMeasures(Set<AmpMeasures> reportMeasures) {
        this.reportMeasures = reportMeasures;
    }

    public AmpCategoryValue getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(AmpCategoryValue activityLevel) {
        this.activityLevel = activityLevel;
    }

    /**
     * @return the budgetExporter
     */
    public Boolean getBudgetExporter() {
        return budgetExporter;
    }

    /**
     * @param budgetExporter the budgetExporter to set
     */
    public void setBudgetExporter(Boolean budgetExporter) {
        this.budgetExporter = budgetExporter;
    }

    public Set<AmpDesktopTabSelection> getDesktopTabSelections() {
        return desktopTabSelections;
    }

    public void setDesktopTabSelections(
            Set<AmpDesktopTabSelection> desktopTabSelections) {
        this.desktopTabSelections = desktopTabSelections;
    }
    

    @Override
    public AmpFilterData newAmpFilterData(FilterDataSetInterface filterRelObj,
            String propertyName, String propertyClassName,
            String elementClassName, String value) {
        return new AmpFilterData(filterRelObj, propertyName, propertyClassName, elementClassName, value);
    }
    
    @Override
    public Set<AmpFilterData> getFilterDataSet() {
        return filterDataSet;
    }
    
    @Override
    public void setFilterDataSet(Set<AmpFilterData> filterDataSet) {
        this.filterDataSet = filterDataSet;
    }

    public String getUser() {
        if (ownerId!= null) {
            user = ownerId.getUser().getFirstNames();
            return user;
        }
        return "";
    }

    public int getMeasureOrder(String measurName) {
        Set<AmpReportMeasures> locMeasures = this.getMeasures();
        for (AmpReportMeasures m : locMeasures) {
            if(m.getMeasure().getMeasureName().equals(measurName)){
                return m.getOrder();
            }   
        }
        return 0;
    }
    
      public static Comparator<AmpReports> UpdatedDateComparator = new Comparator<AmpReports>() {
            public int compare(AmpReports report, AmpReports anotherReport) {
              Date firstDate = report.getUpdatedDate();
              Date secondDate = anotherReport.getUpdatedDate();
              return firstDate.compareTo(secondDate);
            }
          };
    
      public static Comparator<AmpReports> lexicographicComparator = new Comparator<AmpReports>() {
            public int compare(AmpReports report, AmpReports anotherReport) {
              return
                report.getName().compareToIgnoreCase(anotherReport.getName());
            }
          };



    public String getNametrimed() {
        return this.name.replace("'","\\'");
    }

    public Boolean getAllowEmptyFundingColumns() {
        return allowEmptyFundingColumns;
    }

    public void setAllowEmptyFundingColumns(Boolean allowEmptyFundingColumns) {
        this.allowEmptyFundingColumns = allowEmptyFundingColumns;
    }
    
    public Boolean getAlsoShowPledges() {
        return this.alsoShowPledges;
    }
    
    public void setAlsoShowPledges(Boolean alsoShowPledges) {
        this.alsoShowPledges = alsoShowPledges;
    }

    public int getNumOfHierarchies() {
        int ret = 0;
        if ( this.hierarchies != null )
            ret = this.hierarchies.size();
        return ret;
    }
    
    public String getHierarchiesPath() {
        String ret  = "";
        try{
            if ( this.getHierarchies() != null ) {
                for (AmpReportHierarchy arh: this.getHierarchies() ) {
                    String colName          = arh.getColumn().getColumnName();
                    String translColName    = TranslatorWorker.translateText(colName, this.locale, this.siteId);
                    ret                     += " / " + translColName;
                }
                ret     += " /";
                if ( ret.length() > 0 )
                    ret     = ret.substring(1);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
    
    @Override
    public String getObjectFilteredName() {
        return DbUtil.filter(getObjectName());
    }

    public AmpCategoryValue getReportCategory() {
        return reportCategory;
    }

    public void setReportCategory(AmpCategoryValue reportCategory) {
        this.reportCategory = reportCategory;
    }
        
    public ReportGenerator getReportGenerator() {
        return this.reportGenerator;
    }
    
    public void setReportGenerator(ReportGenerator generator) {
        this.reportGenerator = generator;
    }

    /**
     * returns true iff the report has some column whose computation / display would require having a "Total Commitments" column in the report
     * @return
     */
    public boolean needsTotalCommitments()
    {
        for(AmpReportMeasures meas:this.getMeasures())
            if (meas.getMeasure().getMeasureName().contains(ArConstants.TOTAL_COMMITMENTS))
                return true;
        return false;
    }
    
    public Set<String> getMeasureNames()
    {
        Set<String> res = new HashSet<String>();
        for(AmpReportMeasures measure:getMeasures())
            res.add(measure.getMeasure().getMeasureName());
        return res;
    }

    public Set<String> getColumnNames()
    {
        Set<String> res = new HashSet<String>();
        for(AmpReportColumn column:getColumns())
            res.add(column.getColumn().getColumnName());
        return res;
    }
    
    public AmpMeasures[] getOrderedMeasures() {
        AmpMeasures[] res = new AmpMeasures[getMeasures().size()];
        for (AmpReportMeasures measure:getMeasures())
            res[measure.getOrder() - 1] = measure.getMeasure();
        return res;
    }
    
    public Set<String> getHierarchyNames()
    {
        Set<String> res = new HashSet<String>();
        for(AmpReportHierarchy hier: getHierarchies())
            res.add(hier.getColumn().getColumnName());
        return res;
    }

    /**
     * returns true IFF report has only Computed measures, or it a tab report
     * @param arf
     * @return
     */
    public boolean shouldDeleteFunding(AmpARFilter arf)
    {
        // if it's a tab reports just remove funding
        if (arf.isWidget() || ("N".equals(getOptions())))
            return true;

        int nrComputedMeasures = 0;
        for (AmpReportMeasures measure:getMeasures())
        {
            if (measure.getMeasure().getExpression() != null)
                nrComputedMeasures ++;
        }

        if (nrComputedMeasures > 0 && (nrComputedMeasures == getMeasures().size()))
            return true;

        return false;
    }
    
    public boolean currencyIsSpecified()
    {
        if (this.getFilterDataSet() == null)
            return false;
        
        for(AmpFilterData filterData:this.getFilterDataSet())
        {
            if (filterData.getPropertyName().equals("currency"))
                return true;
        }
        return false;
    }
    
    /**
     * Checks if all columns in the report are added as 
     * hierarchies, if it is, then add the column "Project Title".
     * Also checks if the column "Project Title" is already added,
     * if it is, then removes it from the hierarchies list.
     * @param ampReport
     */
    public void validateColumnsAndHierarchies(){
        AdvancedReportUtil.removeDuplicatedColumns(this);
        Collection<AmpColumns> availableCols    = AdvancedReportUtil.getColumnList();
        AmpCategoryValue level1     = CategoryManagerUtil.getAmpCategoryValueFromDb( CategoryConstants.ACTIVITY_LEVEL_KEY , 0L);
        if (this.getColumns().size() == this.getHierarchies().size()) {
            for ( AmpColumns tempCol: availableCols ) {
                if ( ArConstants.COLUMN_PROJECT_TITLE.equals(tempCol.getColumnName()) ) {
                    if (!AdvancedReportUtil.isColumnAdded(this.getColumns(), ArConstants.COLUMN_PROJECT_TITLE)) {
                        AmpReportColumn titleCol= new AmpReportColumn();
                        titleCol.setLevel(level1);
                        titleCol.setOrderId((long) (this.getColumns().size() + 1));
                        titleCol.setColumn(tempCol); 
                        this.getColumns().add(titleCol);
                        break;
                    }else{
                        /*if Project Title column is already added then remove it from hierarchies list*/
                        if(!FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.PROJECT_TITLE_HIRARCHY).equalsIgnoreCase("true"))
                            AdvancedReportUtil.removeColumnFromHierarchies(this.getHierarchies(), ArConstants.COLUMN_PROJECT_TITLE);
                        break;
                    }
                }
            }
        }
    }
    
    public AmpReports buildClone() {
        try {
            PersistenceManager.getSession().evict(this);
            AmpReports res = (AmpReports) super.clone();
            res.setAmpReportId(null); // detach
            PersistenceManager.getSession().evict(res);
            return res;
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String hqlStringForName(String idSource)
    {
        return InternationalizedModelDescription.getForProperty(AmpReports.class, "name").getSQLFunctionCall(idSource + ".ampReportId");
    }
        
    public static String hqlStringForDescription(String idSource)
    {
        return InternationalizedModelDescription.getForProperty(AmpReports.class, "reportDescription").getSQLFunctionCall(idSource + ".ampReportId");
    }
    
    /**
     * is non hierarchical summary report?
     * @return
     */
    public boolean isSummaryReportNoHierachies () {
        return (this.getHideActivities() && this.getHierarchies().isEmpty());
    }
    
    public boolean shouldInjectPledgeColumnsAsProjectColumns() {
        return !isTrueBoolean(this.getDrilldownTab()) // NOT a tab
                &&
                (isTrueBoolean(this.getAlsoShowPledges()));
    }
        
    private static boolean isTrueBoolean(Boolean b) {
        return (b != null) && b;
    }
    
    @Override
    public String toString()
    {
        return String.format("AmpReports: " + this.getName());
    }

    public Boolean getShowOriginalCurrency() {
        return showOriginalCurrency;
    }

    public void setShowOriginalCurrency(Boolean showOriginalCurrency) {
        this.showOriginalCurrency = showOriginalCurrency;
    }
    
}

