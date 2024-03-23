package org.dgfoundation.amp.ar;

import org.dgfoundation.amp.ar.dbentity.FilterDataSetInterface;
import org.dgfoundation.amp.error.CurrentReportContextIdException;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.ar.util.FilterUtil;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.helper.FormatHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * holds all the information (AmpReports instance + extra information) needed to generate and render a report<br />
 * extra information == anything which is not not in an AmpReports instance or accessible through 1:1 relationships with one <br />
 * the class is <b>not</b> synchronized because:<br />
 *      the "global" data it operates one is split across sessions<br />
 *      the only destructive operation is creating a RCD, and this operation happens in very few places of code which do not happen at the same time
 * @author Dolghier Constantin
 *
 */
public class ReportContextData 
{
    /**
     * the request attribute name of reportId to fetch from if request.ampReportId is missing (used for non-single-report pages, like "View Workspace")
     */
    public final static String BACKUP_REPORT_ID_KEY = "backupReportContextId";
    
    /**
     * these ID's below will get pushed into URL's, so make them ASCII strings without separators!
     */
    public final static String REPORT_ID_CURRENT_WORKSPACE = "current_workspace";
    public final static String REPORT_ID_REPORT_WIZARD = "report_wizard";
    public final static String REPORT_ID_WORKSPACE_EDITOR = "workspace_editor";
    
    public final static Long QUERY_ENGINE_REPORT_ID = -7L;
    public final static String REPORT_ID_QUERY_ENGINE = QUERY_ENGINE_REPORT_ID.toString(); // don't change this line or saving Query as Tab won't work!
            
    private final static AtomicLong idsFactory = new AtomicLong(1);
    
    /**
     * for debugging purposes mainly
     */
    public Long uniqueId = idsFactory.getAndIncrement();
    
    private AmpARFilter filter;                             // session[ArConstants.REPORTS_Z_FILTER]
    private AmpARFilter serializedFilter;                   // the currently-serialized filter (only used while editing/creating reports). Field used for resetting. session[ReportWizardAction.EXISTING_SESSION_FILTER]
    //private AmpARFilter   editedFilter;                       // the current version of the filter (only used while editing / creating reports). session[ReportWizardACtion.SESSION_FILTER]

    //private String selectedCurrency;                      // session["selectedCurrency"] - REMOVED, replaced by function call
    //private String reportCurrencyCode;                        // session["reportCurrencyCode"] - REMOVED as it was unused
    private int progressValue;                              // session["progressValue"]
    private int progressTotalRows;                          // session["progressTotalRows"]
    private Map<Long, MetaInfo<String>> reportSorters;      // session["reportSorters"]
    //private GroupReportData generatedReport;              // session["report"];
    private AmpReports reportMeta;                          // session["reportMeta"]
    private String sortBy;                                  // session["sortBy"]
    private Boolean sortAscending;                          // session[ArConstants.SORT_ASCENDING]
    private String contextId;
    private AmpCurrency selectedCurrency;                   // the report's currency
    
    /**
     * TODO-CONSTANTIN: why is this done, if "report" has this same field?
     */
    private boolean pledgeReport;
    
    public ReportContextData(String reportContextId)
    {
        this.contextId = reportContextId;
    }

    public String getContextId()
    {
        return contextId;
    }
        
    public boolean isPledgeReport()
    {
        return pledgeReport;
    }
    
    public void setPledgeReport(boolean pledgeReport)
    {
        this.pledgeReport = pledgeReport;
    }
    
    public AmpCurrency fetch_SelectedAmpCurrency()
    {
        if (getFilter() != null && getFilter().getCurrency() != null)
            return getFilter().getCurrency();
        else
            return AmpARFilter.getDefaultCurrency();
    }
    
    public AmpCurrency getSelectedAmpCurrency()
    {
        if (selectedCurrency == null)
            selectedCurrency = fetch_SelectedAmpCurrency();
        return selectedCurrency;
    }
    
    public String getSelectedCurrencyCode()
    {
        return getSelectedAmpCurrency().getCurrencyCode();
    }
    
    /**
     * computes the name of the currently-to-be-used currency
     * @return
     */
    public String getSelectedCurrencyName()
    {
        return getSelectedAmpCurrency().getCurrencyName();
    }
    
    /**
     * this function replaces the pre-AMP2.4 session.getAttribute(ArConstants.SELECTED_CURRENCY) which was sort of a hack - not a data store per se
     * @return
     */
    public String getSelectedCurrencyText()
    {       
        return "- " + getSelectedCurrencyName();
    }       
    
    /**
     * @deprecated - kept here just for the JSP's
     * reason: undescriptive name. Use {@link #getSelectedCurrencyText()} instead
     * @return
     */
    public String getSelectedCurrency()
    {
        return getSelectedCurrencyText();
    }
    
    public int getProgressValue()
    {
        return progressValue;
    }
    
    public void increaseProgressValue(int amount)
    {
        if (amount < 0)
            throw new IllegalArgumentException("illegal progress increase amount: " + amount);
        progressValue += amount;
    }
    
    public void setProgressValue(int progressValue)
    {
        this.progressValue = progressValue;
    }
    
    public int getProgressTotalRows()
    {
        return this.progressTotalRows;
    }
    
    public void setProgressTotalRows(int progressTotalRows)
    {
        this.progressTotalRows = progressTotalRows;
    }

//  public String getReportCurrencyCode() {
//      return reportCurrencyCode;
//  }
//
//  public void setReportCurrencyCode(String reportCurrencyCode) {
//      this.reportCurrencyCode = reportCurrencyCode;
//  }

    public Map<Long, MetaInfo<String>> getReportSorters() {
        return getReportSorters(false);
    }

    /**
     * creates a new ReportSorters if current value is missing
     * @param createIfMissing
     * @return
     */
    public Map<Long, MetaInfo<String>> getReportSorters(boolean createIfMissing) {
        if (reportSorters == null)
            reportSorters = new HashMap<Long, MetaInfo<String>>();
        return reportSorters;
    }

    public void setReportSorters(Map<Long, MetaInfo<String>> reportSorters) {
        this.reportSorters = reportSorters;
    }

    public AmpARFilter getFilter() {
        return filter;
    }

    public void setFilter(AmpARFilter filter) {
        this.filter = filter;
    }

    private boolean isNullGeneratedReport = true;
    
    /**
     * equivalent to calling {@link #getGeneratedReport(true)}
     * @return
     */
    public GroupReportData getGeneratedReport(){
        return getGeneratedReport(true);
    }
    
    /**
     * returns the report encapsulated in this RCD. If none exists and generateIfMissing=true, it will be regenerated
     * @param generateIfMissing
     * @return
     */
    public GroupReportData getGeneratedReport(boolean generateIfMissing) {
        if (isNullGeneratedReport)
            return null;
        return GroupReportDataCacher.staticRecall(this, generateIfMissing);
    }

    public void setGeneratedReport(GroupReportData generatedReport) {
        if (generatedReport == null)
        {
            GroupReportDataCacher.staticDelete(this);
            this.isNullGeneratedReport = true;
        }
        else
        {
            GroupReportDataCacher.staticMemorize(this, generatedReport);
            this.isNullGeneratedReport = false;
        }
    }

    public AmpReports getReportMeta() {
        return reportMeta;
    }

    public void setReportMeta(AmpReports reportMeta) {
        this.reportMeta = reportMeta;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Boolean getSortAscending() {
        return sortAscending;
    }

    public void setSortAscending(Boolean sortAscending) {
        this.sortAscending = sortAscending;
    }

    public String getAmpReportId()
    {
        if (getReportMeta() != null && getReportMeta().getAmpReportId() != null)
            return getReportMeta().getAmpReportId().toString(); // this is the normal case
        
        // now let's try to be paranoid
        if (TLSUtils.getRequest().getParameter("ampReportId") != null && TLSUtils.getRequest().getParameter("ampReportId").trim().length() > 0)
            return TLSUtils.getRequest().getParameter("ampReportId").trim();

        return "";
        //return getContextId();
    }
    
//  public static Long getProperAmpReportId()
//  {
//      String z = ReportContextData.getCurrentReportContextId(TLSUtils.getRequest(), true);
//      if (z != null)
//      {
//          try
//          {
//              return Long.parseLong(z);
//          }
//          catch(Exception e)
//          {
//              return null;
//          }
//      }
//      return null;
//  }
    
    public AmpARFilter getSerializedFilter()
    {
        return serializedFilter;
    }
    
    /**
     * resets filters and forgets everything
     */
    public void resetFilters()
    {
        this.setSerializedFilter(null);
        this.setFilter(null);
    }
    
    
    /**
     * returns a <b>guaranteed non-null</b> filter connected to a source. If none exists, creates one filled with defaults. Puts the created/retrieved filter in RCD.filter 
     * @param initFromDB if true, then tries to read filter data from source and reverts to defaults if not possible. if false, gets preexisting filter or creates none if nonexisting
     * @param source - the source to be used when initFromDB=true. Ignored when initFromDB=false
     * @return
     */
    public AmpARFilter loadOrCreateFilter(boolean initFromDB, FilterDataSetInterface source)
    {
        if (initFromDB)
        {
            initFilters(source);
            AmpARFilter result = getFilter();
            result.initWidget();
            return result;
        }
        
        AmpARFilter result = FilterUtil.getOrCreateFilter(null, source);
        result.initWidget();
        return result;
    }
    
    /**
     * inits filters off a FilterDataSetInterface source or keeps current data
     * TODO: implement copy instead of double-load
     * @param source
     */
    public void initFilters(FilterDataSetInterface source)
    {
        setSerializedFilter(FilterUtil.buildFilter(source, null));
        setFilter(FilterUtil.buildFilter(source, null));
    }
        
    public void setSerializedFilter(AmpARFilter serializedFilter)
    {
        this.serializedFilter = serializedFilter;
    }           
    
    /**
     * cleans any calculated data for a report
     * calculated data = anything which can change its values based on filters and other settings
     */
    public void cleanReportCaches()
    {
        setGeneratedReport(null);
        progressTotalRows = 0;
        progressValue = 0;
        selectedCurrency = null;
        reportSorters = null;
        if (this.getReportMeta() != null)
            if (!this.getReportMeta().currencyIsSpecified())
            {
                resetCurrency(this.getFilter());
                resetCurrency(this.getSerializedFilter());
            }
    }
    
    /**
     * resets the currency of a filter to the default one - the default one being specified off {@link AmpARFilter#getDefaultCurrency()} <br />
     * if filter is null OR this filter has been touched by applying filters / settings, nothing is done (NPE is not thrown)
     * @param filter
     */
    protected void resetCurrency(AmpARFilter filter)
    {
        if (filter == null)
            return;
        if (filter.haveSettingsBeenApplied())
            return;
        filter.setCurrency(AmpARFilter.getDefaultCurrency());
    }
    
    public static boolean contextIdExists()
    {
        String rcid = getCurrentReportContextId(TLSUtils.getRequest(), false);
        return rcid != null;
    }
    
    /**
     * gets the reportId attached to a request.
     * if none exists, then: [if exceptionOnNull = true, throws Exception] [else returns null]
     * @param request
     * @return
     */
    public static String getCurrentReportContextId(HttpServletRequest request, boolean exceptionOnNull)
    {
        String reportContextId = request.getParameter("reportContextId");
        
        if (reportContextId == null)
            reportContextId = (String) request.getAttribute(BACKUP_REPORT_ID_KEY);
        
        if (reportContextId == null)
            reportContextId = request.getParameter("ampReportId");
        
        if (reportContextId == null)
            reportContextId = (String) request.getAttribute("ampReportId");
        
        if ((reportContextId == null) && exceptionOnNull)
            //throw new RuntimeException("request does not contain mandatory parameter 'reportContextId'!");
            throw new CurrentReportContextIdException("request does not contain mandatory parameter 'reportContextId'!");
        return reportContextId;
    }
    
    /**
     * gets (creating if necessary) the session-global ReportContextMap
     * @param request
     * @return
     */
    public static Map<String, ReportContextData> getReportContextMap(HttpSession session){      
        if (session.getAttribute("reportContext") == null){
            session.setAttribute("reportContext", new HashMap<String, ReportContextData>());
        }
        Map<String, ReportContextData> map = (Map<String, ReportContextData>) session.getAttribute("reportContext");
        return map;
    }
    
    /**
     * gets a ReportContextData associated with a request.
     * @param request
     * @return
     */
    public static ReportContextData getFromRequest(HttpServletRequest request, boolean createIfNotExists)
    {
        String reportId = getCurrentReportContextId(request, true);
        
        Map<String, ReportContextData> map = getReportContextMap(request.getSession());
        if (!map.containsKey(reportId))
        {
            if (createIfNotExists)
                return createWithId(request.getSession(), reportId, false);
            else
                throw new InvalidReportContextException("nonexistant reportId: " + reportId);
        }
        
        return map.get(reportId);
    }
    
    /**
     * gets a ReportContextData from the current HttpServletRequest stored by TLSUtils. forwards the call to {@link #getFromRequest(HttpServletRequest)}
     * all the restrictions of {@link #getFromRequest(HttpServletRequest)} apply
     * @return
     */
    public static ReportContextData getFromRequest(boolean createIfNotExists)
    {
        HttpServletRequest request = TLSUtils.getRequest();
        if (request == null) {
            throw new RuntimeException("TLS Request not found!");
        }
        request.getSession().setAttribute("reportMeta", null); //VERY important for pledge reports recognisation. For porting to AMP 2.4: put null in RCD.reportMeta
        ReportContextData res = getFromRequest(request, createIfNotExists);
        return res;
    }
    
    public static boolean hasFilters()
    {
        HttpServletRequest request = TLSUtils.getRequest();
        if (request == null) {
            throw new RuntimeException("TLS Request not found!");
        }
        request.getSession().setAttribute("reportMeta", null); //VERY important for pledge reports recognisation. For porting to AMP 2.4: put null in RCD.reportMeta
        ReportContextData res = getFromRequest(request, true);
        return (res==null?false:res.filter!=null);
    }
    
    /**
     * equivalent to {@link #getFromRequest(false)}
     * @return
     */
    public static ReportContextData getFromRequest()
    {
        return getFromRequest(false);
    }
    
    /**
     * returns true iff: 1. a reportContextId is defined  2. there is a report loaded in mem for it
     * @param request
     * @return
     */
    public static boolean reportIsLoaded(HttpServletRequest request){
        String ampReportId = getCurrentReportContextId(request, false);
        if (ampReportId == null)
            return false; // no reportContextId defined
        
        ReportContextData rcd = getReportContextMap(request.getSession()).get(ampReportId);
        if (rcd == null)
            return false;
        
        if (rcd.getReportMeta() == null)
            return false;
        
        GroupReportData cachedReport = rcd.getGeneratedReport(false);
        return cachedReport != null;
    }
    
    /**
     * creates a ReportContextData with a given id, replacing the already-existing one with the same id
     * @param session - the session where to attach this ReportContextData
     * @param id - the newly-created ReportContextData id
     * @param overwriteIfExists if true, any preexisting RCD with the same id will be deleted, if false - null will be returned
     * @return newly created RCD instance or null if an another one with the same id exists and overwriteIfExists = true 
     */
    public static ReportContextData createWithId(HttpSession session, String id, boolean overwriteIfExists)
    {
        Map<String, ReportContextData> map = getReportContextMap(session);
        
        if ((!overwriteIfExists) && (map.containsKey(id)))
            return null; // exists
        
        ReportContextData result = new ReportContextData(id);
        map.put(id, result);
        return result;
    }
    
    /**
     * see {@link #createWithId(HttpSession, String, boolean)}'s description and limitations
     * @param id
     * @param overwriteIfExists
     * @return
     */
    public static ReportContextData createWithId(String id, boolean overwriteIfExists)
    {
        return createWithId(TLSUtils.getRequest().getSession(), id, overwriteIfExists);
    }
    
    /**
     * creates a fresh ReportContextData for the current reportId, as per {@link #getFromRequest()} rules. Overwrites any preexisting data
     */
    public static void cleanContextData()
    {
        HttpServletRequest req = TLSUtils.getRequest();
        String rId = getCurrentReportContextId(req, true);
        createWithId(req.getSession(), rId, true);
    }
    

    /**
     * calls {@link #cleanReportCaches()} on current report
     *  will throw exception if no current id exists
     *  @return true if managed to clean (e.g. reportContextId exists)
     */
    public static boolean cleanCurrentReportCaches()
    {
        try{
            ReportContextData rcData = getFromRequest(); // this will throw an exception iff RCD does not exist. the enclosing try/catch will swallow it
            if (rcData != null)
                rcData.cleanReportCaches();
        }
        catch(Exception e){
            String reportId = ReportContextData.getCurrentReportContextId(TLSUtils.getRequest(), false);
            if (reportId == null)
                return false;
            createWithId(reportId, true);
        }
        return true;
    }
    
    /**
     * clears the SESSION of any ReportContextData contained in it.
     */
    public static void clearSession()
    {
        try{
            TLSUtils.getRequest().getSession(true).removeAttribute("reportContext");
        }
        catch(Exception e){}
    }
    
    public static String formatNumberUsingCustomFormat(double number){
        try
        {
            return ReportContextData.getFromRequest().getFilter().getCurrentFormat().format(number);
        }
        catch(Exception e)
        {
            return FormatHelper.formatNumber(number);
        }
    }
  
    /**
     * returns a decorated name, safe to be inserted in a session. basically concatenates given "name" with the contextId taken from the Request
     * @param name
     * @return
     */
    public static String decorateName(String name)
    {
        try
        {
            String rcid = getCurrentReportContextId(TLSUtils.getRequest(), true);
            return name + "_" + rcid;
        }
        catch(Exception e)
        {
            return name;
        }
    }
}
