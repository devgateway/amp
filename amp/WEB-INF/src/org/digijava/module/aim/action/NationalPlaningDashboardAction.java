package org.digijava.module.aim.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.util.collections.CollectionUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpFundingAmount;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.AmpThemeIndicatorValue;
import org.digijava.module.aim.dbentity.AmpThemeIndicators;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.form.NationalPlaningDashboardForm;
import org.digijava.module.aim.helper.AmpPrgIndicatorValue;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.IndicatorValuesBean;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ChartUtil;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class NationalPlaningDashboardAction extends DispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return doDisplay(mapping, form, request, response, false);
    }

    public ActionForward displayWithFilter(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return doDisplay(mapping, form, request, response, true);
    }

    protected ActionForward doDisplay(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response,
            boolean filter) throws Exception {
        NationalPlaningDashboardForm npdForm = (NationalPlaningDashboardForm) form;
        // load all themes
        Collection<AmpTheme> themes = ProgramUtil.getAllThemes(true);
        Collection sortedThemes = CollectionUtils.getFlatHierarchy(themes,
                true, new ProgramUtil.ProgramHierarchyDefinition(),
                new ProgramUtil.HierarchicalProgramComparator());
        npdForm.setPrograms(new ArrayList(sortedThemes));

        Long currentThemeId = npdForm.getCurrentProgramId();

        // set up tree
        Collection tree = CollectionUtils.getHierarchy(themes,
                new ProgramUtil.ProgramHierarchyDefinition());
        npdForm.setProgramTree(tree);
        // generate XML from hierarchy
        String xml = ProgramUtil.getThemesHierarchyXML(themes);
        npdForm.setXmlTree(xml);

        AmpTheme currentTheme = null;

        // according new requirments we should not display any info on initial
        // view of NPD.
        if (currentThemeId != null && currentThemeId.longValue() > 0) {
            currentTheme = getCurrentTheme(themes, currentThemeId);
            npdForm.setCurrentProgramId(currentTheme.getAmpThemeId());
            npdForm.setCurrentProgram(currentTheme);
        }

        if (currentTheme != null) {

            Long locationId = null;
            if (npdForm.getSelectedLocations() != null
                    && npdForm.getSelectedLocations()[0] > 0) {
                locationId = new Long(npdForm.getSelectedLocations()[0]);
            }

            Date fromDate = null;
            if (npdForm.getFromYear() > 0) {
                Calendar cal = Calendar.getInstance();
                cal.set(npdForm.getFromYear(), 0, 1, 0, 0, 0);
                fromDate = cal.getTime();
            }

            Date toDate = null;
            if (npdForm.getToYear() > 0) {
                Calendar cal = Calendar.getInstance();
                cal.set(npdForm.getToYear(), 0, 1, 0, 0, 0);
                toDate = cal.getTime();
            }

            // Activitie filters : from date
            Date fromDateActivities = null;
            if (npdForm.getFromyearActivities() > 0) {
                Calendar cal = Calendar.getInstance();
                cal.set(npdForm.getFromyearActivities(), 0, 1, 0, 0, 0);
                fromDateActivities = cal.getTime();
            }

            // Activitie filters : to date
            Date toDateActivities = null;
            if (npdForm.getToYearActivities() > 0) {
                Calendar cal = Calendar.getInstance();
                cal.set(npdForm.getToYearActivities(), 0, 1, 0, 0, 0);
                toDateActivities = cal.getTime();
            }

            String donorOrgId = null;
            if (npdForm.getSelectedDonors() != null
                    && npdForm.getSelectedDonors()[0] > 0) {
                donorOrgId = String.valueOf(npdForm.getSelectedDonors()[0]);
            }

            // retrive activities
            Collection activities = ActivityUtil.searchActivities(
                    currentThemeId, "1", donorOrgId, fromDateActivities,
                    toDateActivities, locationId, null,null,null);
            if (activities != null) {
                npdForm.setActivities(new ArrayList(activities));
                DecimalFormat frmt=new DecimalFormat("$,###.##");
                String sum=frmt.format(getAmountSum(new ArrayList(activities)));
                npdForm.setFundingSum(sum);
            } else {
                npdForm.setActivities(new ArrayList());
                npdForm.setFundingSum(" 0 ");
            }

            List valueBeans = getIndicatorValues(currentTheme, fromDate, toDate);
            npdForm.setValuesForSelectedIndicators(valueBeans);
            Set indis=currentTheme.getIndicators();
            if (indis!=null){
                SortedSet sortedIndicators=new TreeSet(indis);
                npdForm.setIndicators(sortedIndicators);
            }else{
                npdForm.setIndicators(indis);
            }


        }

        npdForm.setYears(ProgramUtil.getYearsBeanList());
        npdForm.setDonors(getDonorsList(30));
        npdForm.setLocations(new ArrayList<>()); // dummy value
        
        return mapping.findForward("viewNPDDashboard");
    }

    private List getIndicatorValues(AmpTheme theme, Date start, Date end) {
        List result = null;
        /** @todo This shoud be implemented correctly to work faster */
        if (theme != null && theme.getIndicators() != null) {
            for (Iterator iter = theme.getIndicators().iterator(); iter
                    .hasNext();) {
                AmpThemeIndicators indicat = (AmpThemeIndicators) iter.next();
                Collection indValues = ProgramUtil
                        .getThemeIndicatorValuesDB(indicat.getAmpThemeIndId());
                if (indValues != null && indValues.size() > 0) {
                    if (result == null) {
                        result = new ArrayList();
                    }
                    for (Iterator valIterator = indValues.iterator(); valIterator
                            .hasNext();) {
                        AmpThemeIndicatorValue dbValue = (AmpThemeIndicatorValue) valIterator
                                .next();
                        if ((start == null && end == null)
                                || ((dbValue.getCreationDate() != null) && ((start != null
                                        && end == null && dbValue
                                        .getCreationDate().after(start))
                                        || (start == null && end != null && dbValue
                                                .getCreationDate().before(end)) || (start != null
                                        && end != null
                                        && dbValue.getCreationDate().after(
                                                start) && dbValue
                                        .getCreationDate().before(end))

                                ))

                        ) {
                            IndicatorValuesBean indValueBean = new IndicatorValuesBean(
                                    dbValue);
                            result.add(indValueBean);
                        }
                    }
                }
            }
        }
        return result;
    }

    private long[] getIndicatorIds(AmpTheme currentTheme) {
        long[] ids = null;
        Set indicators = currentTheme.getIndicators();
        if (indicators == null) {
        } else {
            ids = new long[indicators.size()];
            int i = 0;
            Iterator iter = indicators.iterator();
            while (iter.hasNext()) {
                AmpThemeIndicators item = (AmpThemeIndicators) iter.next();
                ids[i++] = item.getAmpThemeIndId().longValue();
            }
        }
        return ids;
    }

    /**
     * Returns Labelvaluebean's for Donors.
     * 
     * @param nameLimit
     *            max number of chars for donor names
     * @return Collection of LabelValueBean objects
     */
    private Collection getDonorsList(int nameLimit) {
        Collection result = null;
        Collection dbDonors = DbUtil.getAllDonorOrgs();
        if (dbDonors != null) {
            result = new ArrayList();
            Iterator dbIter = dbDonors.iterator();
            while (dbIter.hasNext()) {
                AmpOrganisation donor = (AmpOrganisation) dbIter.next();
                String id = donor.getAmpOrgId().toString();
                String name = (donor.getName().length() > nameLimit) ? donor
                        .getName().substring(0, nameLimit)
                        + "..." : donor.getName();
                LabelValueBean lvBean = new LabelValueBean(name, id);
                result.add(lvBean);
            }
        }
        return result;
    }

    public ActionForward displayChart(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        NationalPlaningDashboardForm npdForm = (NationalPlaningDashboardForm) form;

        try {
            Long currentThemeId = npdForm.getCurrentProgramId();
            long[] selIndicators = npdForm.getSelectedIndicators();
            String[] selYears = npdForm.getSelectedYears();

            CategoryDataset dataset = null;
            if (currentThemeId != null && currentThemeId.longValue() > 0) {
                AmpTheme currentTheme = ProgramUtil.getThemeById(currentThemeId);

                dataset = createPercentsDataset(currentTheme, selIndicators, selYears);
            }
            JFreeChart chart = ChartUtil.createChart(dataset,ChartUtil.CHART_TYPE_BAR);

            response.setContentType("image/png");
            ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, ChartUtil.CHART_WIDTH,ChartUtil.CHART_HEIGHT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Iterates over all themes and finds the theme which must be current. If
     * there is no currentThemeId passed, simply returns the first theme in the
     * list. Otherwise returns theme by the given currentThemeId;
     * 
     * @param themes
     *            List
     * @param currentThemeId
     *            Long
     * @return AmpTheme
     */
    private AmpTheme getCurrentTheme(Collection<AmpTheme> themes, Long currentThemeId) {
        AmpTheme currentTheme = null;
        Iterator<AmpTheme> iter = themes.iterator();
        while (iter.hasNext()) {
            AmpTheme item = iter.next();
            boolean isEqual = false;

            if (currentTheme == null
                    | (currentThemeId != null && (isEqual = item
                            .getAmpThemeId().equals(currentThemeId)))) {
                currentTheme = item;

                // Break the loop
                if (isEqual) {
                    break;
                }
            }
        }
        return currentTheme;
    }

    protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        NationalPlaningDashboardForm npdForm = (NationalPlaningDashboardForm) form;
        npdForm.setShowChart(true);
        return display(mapping, form, request, response);
    }

    private static CategoryDataset createDataset(AmpTheme currentTheme,
            long[] selectedIndicators) {

        String[] series = { "Target", "Actual", "Base" };

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (selectedIndicators != null) {
            Arrays.sort(selectedIndicators);

            dataset = new DefaultCategoryDataset();

            Iterator iter = currentTheme.getIndicators().iterator();
            while (iter.hasNext()) {
                AmpThemeIndicators item = (AmpThemeIndicators) iter.next();

                int pos = Arrays.binarySearch(selectedIndicators, item
                        .getAmpThemeIndId().longValue());

                if (pos >= 0) {
                    String displayLabel = item.getName();
                    try {
                        Collection indValues = ProgramUtil
                                .getThemeIndicatorValues(item
                                        .getAmpThemeIndId());
                        for (Iterator valueIter = indValues.iterator(); valueIter
                                .hasNext();) {
                            AmpPrgIndicatorValue valueItem = (AmpPrgIndicatorValue) valueIter
                                    .next();
                            dataset.addValue(valueItem.getValAmount(),
                                    series[valueItem.getValueType()],
                                    displayLabel);
                            System.out
                                    .println((valueItem.getValAmount() + ", "
                                            + series[valueItem.getValueType()]
                                            + ", " + displayLabel));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

        }
        return dataset;

    }

    private static CategoryDataset createPercentsDataset(AmpTheme currentTheme,
            long[] selectedIndicators, String[] selectedYears) throws AimException{

//      String[] series = { "Target", "Actual", "Base" };
//      String[] years = {"2000","2001","2002","2003","2004","2005","2006","2007"};

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (selectedIndicators != null && currentTheme.getIndicators()!=null) {
            Arrays.sort(selectedIndicators);

            dataset = new DefaultCategoryDataset();

            Set sortedIndicators=new TreeSet(currentTheme.getIndicators());
            Iterator iter = sortedIndicators.iterator();
            while (iter.hasNext()) {
                AmpThemeIndicators item = (AmpThemeIndicators) iter.next();

                int pos = Arrays.binarySearch(selectedIndicators, item.getAmpThemeIndId().longValue());

                if (pos >= 0) {
                    String displayLabel = item.getName();
                    try {
                        Collection indValues = ProgramUtil.getThemeIndicatorValuesDB(item.getAmpThemeIndId());
                        Map actualValues = new HashMap();
                        Double targetValue = null;
                        Double baseValue = null;
                        
                        //arrage all values by types
                        for (Iterator valueIter = indValues.iterator(); valueIter.hasNext();) {
                            AmpThemeIndicatorValue valueItem = (AmpThemeIndicatorValue) valueIter.next();
                            
                            //target value
                            if (valueItem.getValueType() == 0) {
                                targetValue = valueItem.getValueAmount();
                            }
                            //actual Value
                            if (valueItem.getValueType() == 1 && isInSelectedYears(valueItem, selectedYears)) {
                                Date actualDate = valueItem.getCreationDate();
                                String year = extractYearString(actualDate);
                                //for every year we should have only latest actual value
                                //so check if we already have atual value fro this year
                                AmpThemeIndicatorValue v = (AmpThemeIndicatorValue) actualValues.get(year);
                                if (v == null ){
                                    //if not then store this actual value
                                    actualValues.put(year, valueItem);
                                }else{
                                    //if we have valu, chack and store latest value
                                    Date crDate = v.getCreationDate();
                                    if (crDate !=null && crDate.compareTo(actualDate) < 0){
                                        actualValues.put(year, valueItem);
                                    }
                                }
                            }
                            //base value - not used
                            if (valueItem.getValueType() == 2) {
                                baseValue = valueItem.getValueAmount();
                            }
                        }
                        if (actualValues.size() == 0) {
                            actualValues.put("2000",new Double(0.0));
                        }
                        if (targetValue == null) {
                            targetValue = new Double(1.0);
                        }
                        
                        //now put all values in the dataset
                        
                        if (selectedYears != null){
                            for (int i = 0; i < selectedYears.length; i++) {
                                AmpThemeIndicatorValue actualValue = (AmpThemeIndicatorValue) actualValues.get(selectedYears[i]);
                                if (actualValue != null){
                                    Double realActual = actualValue.getValueAmount();
                                    if (realActual != null) {
                                        realActual = new Double(realActual.doubleValue()/ targetValue.doubleValue());
                                        dataset.addValue(realActual.doubleValue(),selectedYears[i],displayLabel);
                                    }
                                }else{
                                    dataset.addValue(0, selectedYears[i], displayLabel);
                                }
                            }
                        }
//                      actualValue = new Double(actualValue.doubleValue()
//                              / targetValue.doubleValue());
//
//                      dataset.addValue(actualValue.doubleValue(), series[1],
//                              displayLabel);
//                      dataset.addValue(new Double(1.0 - actualValue
//                              .doubleValue()), series[0], displayLabel);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        throw new AimException("Error creating dataset for graph.",ex);
                    }
                }
            }

        }
        return dataset;

    }

    public static boolean isInSelectedYears(AmpThemeIndicatorValue value, String[] selYars){
        String sYear = extractYearString(value.getCreationDate());
        for (int i = 0; i < selYars.length; i++) {
            if (selYars[i].equals(sYear)){
                return true;
            }
        }       
        return false;
    }

    public static int extractYearInt(Date date){
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        int iYear=cal.get(Calendar.YEAR);
        return iYear;
    }
    
    public static String extractYearString(Date date){
        int iYear = extractYearInt(date);
        String sYear=String.valueOf(iYear);
        return sYear;
    }
    
    public static Double getAmountSum(List actList) throws AimException {
        Double mnt = null;
        if (actList != null) {
            AmpActivity act;
            Iterator actItr = actList.iterator();
            while (actItr.hasNext()) {
                act = (AmpActivity) actItr.next();
                AmpFundingAmount ppc = act.getProjectCostByType(AmpFundingAmount.FundingType.PROPOSED);
                if (act!=null && ppc != null && ppc.getFunAmount() != null) {
                    double usdVal = CurrencyWorker.convert(ppc.getFunAmount().doubleValue(), ppc.getCurrencyCode());
                    if(mnt!=null){
                        mnt = new Double(mnt.doubleValue() + usdVal);
                    }else{
                        mnt = new Double(usdVal);
                }
            }
            }
        }
            return mnt;
        }
    }
