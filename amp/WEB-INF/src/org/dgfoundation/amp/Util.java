package org.dgfoundation.amp; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.ar.workers.CategAmountColWorker;
import org.dgfoundation.amp.currencyconvertor.AmpCurrencyConvertor;
import org.dgfoundation.amp.currencyconvertor.CurrencyConvertor;
import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.fiscalcalendar.BaseCalendar;
import org.digijava.module.aim.helper.fiscalcalendar.EthiopianCalendar;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.FiscalCalendarUtil;
import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.digijava.module.editor.util.DbUtil;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.type.StringType;
import org.springframework.beans.BeanWrapperImpl;

public final class Util {

    protected static Logger logger = Logger.getLogger(Util.class);

    /**
     * Used to initialize the digi.edtior popup for activity large text
     * @param textKey
     * @param initialText
     * @param request
     * @return
     * @throws EditorException
     */
    public static String initLargeTextProperty(String textKey,String initialText,HttpServletRequest request) throws EditorException
    {
    HttpSession session = request.getSession();
    TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
    String ret=null;
    if (initialText == null ||
            initialText.length() == 0) {
        ret=textKey + teamMember.getMemberId() + "-" +System.currentTimeMillis();
          User user = RequestUtils.getUser(request);
          String currentLang = RequestUtils.getNavigationLanguage(request).
              getCode();
          String refUrl = RequestUtils.getSourceURL(request);
          
          Editor ed = org.digijava.module.editor.util.DbUtil.createEditor(user,
              currentLang,
              refUrl,
              ret,
              ret,
              " ",
              null,
              request);
          ed.setLastModDate(new Date());
          ed.setGroupName(org.digijava.module.editor.util.Constants.GROUP_OTHER);
          org.digijava.module.editor.util.DbUtil.saveEditor(ed);
        }
    if(ret==null) ret=initialText;
    return ret;
    }
    
    /**
     * Returns a Collection of the same type as the source, holding 
     * the elements of the original collection inside WrapDynaBean items 
     * @param source the source collection
     * @return the target collection - a list
     * @see BeanWrapperImpl
     */
    public static Collection<BeanWrapperImpl> createBeanWrapperItemsCollection(Collection source) {
        List<BeanWrapperImpl> dest=new ArrayList<BeanWrapperImpl>();
        Iterator i=source.iterator();
        while (i.hasNext()) {
            Object element = (Object) i.next();
            BeanWrapperImpl bwi=new BeanWrapperImpl(element);

            dest.add(bwi);
        }
        return dest;

    }
        
    
        
    
    /**
     * loads using Hibernate the object of the specified type, identified by the serializable object
     * @param type the class type of the specified object
     * @param selected the  serializable id of the object to be loaded
     * @return the fetched object
     * @throws HibernateException
     * @throws SQLException
     */
    public static Object getSelectedObject(Class type, Object selected) throws HibernateException{
        if (selected == null || "-1".equals(selected.toString())) 
            return null;
        try
        {
            Set ret = getSelectedObjects(type, new Object[] {selected});
            if (ret.size() == 0)
                return null;
            return ret.iterator().next();
        }
        catch(Exception e)
        {
            throw new RuntimeException("error getting object of class " + type, e);
        }
    }
    
    /**
     * loads using Hibernate the objects of the specified type, identified by the serializable objects in the array
     * @param type the class type of the specified objects
     * @param selected the array with serializable ids of objects to be loaded
     * @return the set of fetched objects
     * @throws HibernateException
     * @throws SQLException
     */
    public static Set getSelectedObjects(Class type, Object[] selected) throws HibernateException{
        if (selected == null)
            return null;
        HashSet set = new HashSet();        
        Session session = PersistenceManager.getSession();
        for (int i = 0; i < selected.length; i++) {
            set.add(session.load(type, new Long(selected[i].toString())));
        }
        return set;
    }
    
    
    public static String toCSStringForIN(Collection<?> col) {
        if (col == null  || col.isEmpty())
            return "-32768"; // the most unlikely ID one is supposed to find in an AMP database     
        return toCSString(col);
    }
    
    /**
     * Returns comma separated list of the values in the collection
     * 
     * @param col the collection
     * @return the comma separated string
     * @author mihai 06.05.2007
     */
    public static String toCSString(Collection<?> col) {
        return toCSString(col, true);
    }
    /**
     * Returns comma separated list of the values in the collection
     * 
     * @param col the collection
     * @return the comma separated string
     * @author mihai 06.05.2007
     */
    public static String toCSString(Collection<?> col, boolean addQuoteToString) {
        if (col == null || col.size() == 0)
            return "";
        
        StringBuilder ret = new StringBuilder();
        boolean isFirst = true;
        
        for (Object element:col) {
            
            if (element == null)
                continue;
            
            if (!isFirst)
                ret.append(",");
            
            Object item = element;
            if (element instanceof Identifiable)
                item = ((Identifiable) element).getIdentifier();

            if (item instanceof String) {
                ret.append((addQuoteToString ? "'" : "") + SQLUtils.sqlEscapeStr(item.toString())
                        + (addQuoteToString ? "'" : ""));
            } else {
                if (item instanceof PropertyListable) {
                    ret.append(((PropertyListable) item).getBeanName());
                } else {
                    ret.append(item.toString());
                }
            }
            isFirst = false;
        }
        return ret.toString();
    }
    

    /**
     * Returns comma separated view string representation of the collection
     * items of toString
     * 
     * @param col
     *            the collectoin
     * @return the comma separated string
     * @author dan 08.07.2007
     */
    //AMP-3372
    public static String collectionAsString(Collection col) {
        StringBuilder ret = new StringBuilder("");
        if (col == null || col.size() == 0)
            return ret.toString();
        Iterator i = col.iterator();
        while (i.hasNext()) {
            Object element = (Object) i.next();
            if (element == null)
                continue;           
            Object item=element;
//          if(element instanceof Identifiable) item=((String)element).toString();
            
//          if (item instanceof String)
//              ret += "'" + (String) item + "'"; else
//          if (item instanceof PropertyListable)
//              ret += ((PropertyListable)item).getBeanName();
            
//          else
                ret.append(item.toString());
            if (i.hasNext())
                ret.append(", ");
        }
        return ret.toString();
    }
    

    /**
     * refactored to use the NiReports retrofitted currency exchange rates service
     */
    public static double getExchange(final String currencyCode, final java.sql.Date currencyDate) {
        CurrencyConvertor convertor = AmpCurrencyConvertor.getInstance();
        return convertor.getExchangeRate(CurrencyUtil.getBaseCurrencyCode(), currencyCode, null, currencyDate.toLocalDate()); // WARNING: Date.toLocalDate() is jdk8 code!
    }
    
    /**
     * As the name implies only the years are checked by this function. 
     * 
     * @param toBeCheckedDate 
     * @param startDate
     * @param endDate
     * @param calendarTypeId
     * @author Alex Gartner - needed for MTEF Projections filter from Financial Progress
     * @return If filterStartYear <= toBeCheckedYear && toBeCheckedYear < filterEndYear then it returns true. Otherwise false.
     */
    public static boolean checkYearFilter(Date toBeCheckedDate, Date startDate, Date endDate, Long calendarTypeId ) {
        EthiopianCalendar ec            = new EthiopianCalendar();
        GregorianCalendar currentTime   = new GregorianCalendar();
        currentTime.setTime(toBeCheckedDate);
        
        ec                              = ec.getEthiopianDate( currentTime );
        
        Integer year                    = currentTime.get(Calendar.YEAR);
        //AMP-2212
        AmpFiscalCalendar calendar=FiscalCalendarUtil.getAmpFiscalCalendar(calendarTypeId);
        
        if(calendar.getBaseCal().equalsIgnoreCase(BaseCalendar.BASE_ETHIOPIAN.getValue()))
        {
            year    =   new Integer(ec.ethYear);
        }
        
        GregorianCalendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(startDate);
        
        GregorianCalendar endCalendar   = new GregorianCalendar();
        endCalendar.setTime(endDate);
        
        Integer startYear               = startCalendar.get(Calendar.YEAR);
        Integer endYear                 = endCalendar.get(Calendar.YEAR);
        
        if ( startYear <= year && year < endYear )
                return true;
        
        return false;
    }
    
    public static int getSystemYear() {
        GregorianCalendar gc            = new GregorianCalendar();
        return gc.get(Calendar.YEAR);
    }


    public static int getCurrentFiscalYear()
    {
        String yearGS           = FeaturesUtil.getGlobalSettingValue( GlobalSettingsConstants.CURRENT_SYSTEM_YEAR );
        Integer year;
        
        if ( yearGS.equals(GlobalSettingsConstants.SYSTEM_YEAR) )
            year        = Util.getSystemYear();
        else
            year        = Integer.parseInt( yearGS );
        
        return year;
    }
    
    /**
     * returns null if the current browser is not IE<br />
     * Partial copy-paste from http://www.java2s.com/Code/Java/Servlets/Browserdetection.htm
     * @param request
     * @return
     */
    public static Integer getIEVersion(HttpServletRequest request)
    {
        String userAgent = request.getHeader("user-agent");
        int msiePos = userAgent.indexOf("MSIE ");
        if (msiePos == -1 || userAgent.contains("Opera"))
        {
            return null;
        }
        return parseNumberAtStart(userAgent.substring(msiePos + 5));
    }
    
     /**
     * We've found the start of a sequence of numbers, what is it as an int?
     */
    private static int parseNumberAtStart(String numberString)
    {
        if (numberString == null || numberString.length() == 0)
        {
            return -1;
        }

        int endOfNumbers = 0;
        while (Character.isDigit(numberString.charAt(endOfNumbers)))
        {
            endOfNumbers++;
        }

        try
        {
            return Integer.parseInt(numberString.substring(0, endOfNumbers));
        }
        catch (NumberFormatException ex)
        {
            return -1;
        }
    }


}
