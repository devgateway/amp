package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.IndicatorTheme;
import org.digijava.module.aim.dbentity.NpdSettings;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.form.NpdGraphForm;
import org.digijava.module.aim.util.ChartUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.NpdUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.CustomCategoryDataset;

/**
 * NPD Indicators graph generator action.
 * Generates different (currently only one) types of graphs for specified indicators
 * Response of the Action is image generated by JFreeChart.
 *
 * @author Irakli Kobiashvili - ikobiashvili@picktek.com
 */
public class getNPDgraph extends Action {


    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response)
            throws Exception {


        NpdGraphForm npdForm = (NpdGraphForm) form;


        try {
            Long currentThemeId = npdForm.getCurrentProgramId();
            long[] selIndicators = npdForm.getSelectedIndicators();
            String[] selYears = npdForm.getSelectedYears();
            if (selYears!=null){
                Arrays.sort(selYears);
            }

            //session for storing latest map for graph
            HttpSession session = request.getSession();

            CategoryDataset dataset = null;
            if (currentThemeId != null && currentThemeId.longValue() > 0) {
                AmpTheme currentTheme = ProgramUtil.getThemeObject(currentThemeId);


                dataset = createPercentsDataset(currentTheme, selIndicators, selYears);
            }
            JFreeChart chart = ChartUtil.createChart(dataset, ChartUtil.CHART_TYPE_BAR);


            ChartRenderingInfo info = new ChartRenderingInfo();


            response.setContentType("image/png");            
           
    		Long teamId=TeamUtil.getCurrentTeam(request).getAmpTeamId();
    		NpdSettings npdSettings=NpdUtil.getCurrentSettings(teamId);            
            Double angle=null;
            
            if(npdSettings.getAngle()!=null){
        		CategoryPlot categoryplot = (CategoryPlot)chart.getPlot();
                CategoryAxis categoryaxis = categoryplot.getDomainAxis();
            	angle=npdSettings.getAngle().intValue()*3.1415926535897931D/180D;
                categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(angle));
            }
            
    		ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, npdSettings.getWidth().intValue(),
            		npdSettings.getHeight().intValue(), info);
         
            //NpdGraphTooltipGenerator ttGen = new NpdGraphTooltipGenerator();

            //generate map for this graph
            String map = ChartUtilities.getImageMap("npdChartMap", info);
            //String map = getImageMap("npdChartMap", info, new StandardToolTipTagFragmentGenerator(), new StandardURLTagFragmentGenerator());
            //System.out.println(map);

            //save map with timestamp from request for later use
            //timestemp is generated with javascript before sending ajax request.
            ChartUtil.saveMap(map, npdForm.getTimestamp(), session);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    // TODO This method should be moved to NPD or chart util.
    private CategoryDataset createPercentsDataset(AmpTheme currentTheme,
                                                  long[] selectedIndicators, String[] selectedYears)
            throws AimException {

        CustomCategoryDataset dataset = new CustomCategoryDataset();

        if (selectedIndicators != null && currentTheme.getIndicators() != null) {
            Arrays.sort(selectedIndicators);

            dataset = new CustomCategoryDataset();

            //Set sortedIndicators = new TreeSet(currentTheme.getIndicators());
            List<IndicatorTheme> sortedIndicators = new ArrayList<IndicatorTheme>(currentTheme.getIndicators());
            Collections.sort(sortedIndicators, new IndicatorUtil.IndThemeIndciatorNameComparator());
            Iterator<IndicatorTheme> iter = sortedIndicators.iterator();
            while (iter.hasNext()) {
                IndicatorTheme item = iter.next();
                AmpIndicator indicator=item.getIndicator();
                int pos = Arrays.binarySearch(selectedIndicators, indicator.getIndicatorId().longValue());

                if (pos >= 0) {
                    String displayLabel = indicator.getName();
                    try {
                        Collection<AmpIndicatorValue> indValues = item.getValues();  // ProgramUtil.getThemeIndicatorValuesDB(item.getAmpThemeIndId());
                        Map<String,AmpIndicatorValue> actualValues = new HashMap<String,AmpIndicatorValue>();
                        Double targetValue = null;
                        Double baseValue = null;

                        // Arrange all values by types
//                        for (Iterator valueIter = indValues.iterator(); valueIter.hasNext();) {
                        for (AmpIndicatorValue valueItem :indValues) {
                            //AmpThemeIndicatorValue valueItem = (AmpThemeIndicatorValue) valueIter.next();

                            // target value
                            if (valueItem.getValueType() == 0) {
                                targetValue = valueItem.getValue();
                            }
                            // actual Value
                            if (valueItem.getValueType() == 1 && isInSelectedYears(valueItem, selectedYears)) {
                                Date actualDate = valueItem.getValueDate();
                                String year = extractYearString(actualDate);
                                // for every year we should have only latest actual value
                                // so check if we already have actual value for this year
                                AmpIndicatorValue v = actualValues.get(year);
                                if (v == null) {
                                    // if not then store this actual value
                                    actualValues.put(year, valueItem);
                                } else {
                                    // if we have value, check and store latest value
                                    Date crDate = v.getValueDate();
                                    if (crDate != null && crDate.compareTo(actualDate) < 0) {
                                        actualValues.put(year, valueItem);
                                    }
                                }
                            }
                            // base value - not used
                            if (valueItem.getValueType() == 2) {
                                baseValue = valueItem.getValue();
                            }
                        }
                        if (targetValue == null) {
                            targetValue = new Double(1.0);
                        }
                        if (baseValue == null) {

                        }

                        // now put all values in the dataset
                        // they will appear on the chart in same order as added in dataset
                        // so years should be ordered (done outside of this method) because we are adding data by year. 
                        if (selectedYears != null) {
                            for (int i = 0; i < selectedYears.length; i++) {
                                AmpIndicatorValue actualValue = actualValues.get(selectedYears[i]);
                                if (actualValue != null) {
                                    Double realActual = actualValue.getValue();
                                    if (realActual != null) {
//										realActual = new Double(realActual.doubleValue() / targetValue.doubleValue());
                                        dataset.addCustomTooltipValue(new String[]{formatValue(baseValue), formatValue(realActual), formatActualDate(actualValue), formatValue(targetValue)});
                                        realActual = computePercent(indicator, targetValue, realActual, baseValue);
                                        dataset.addValue(realActual.doubleValue(), selectedYears[i], displayLabel);
                                    }
                                } else {
                                    dataset.addValue(0, selectedYears[i], displayLabel);
                                    dataset.addCustomTooltipValue(new String[]{formatValue(baseValue), "", "", formatValue(targetValue)});
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        throw new AimException("Error creating dataset for graph.", ex);
                    }
                }
            }

        }
        return dataset;

    }


    public String formatValue(Double val) {
        if (val != null) {
            return val.toString();
        }
        return "0";
    }

    public String formatActualDate(AmpIndicatorValue val) {
        if (val != null) {
        	return DateTimeUtil.formatDate(val.getValueDate());
        }
        return "";
    }

    /**
     * Calculates percent from indicator values.
     * This calculation depends on type of indicator which may be ascending or descending.
     * @param indic
     * @param _target
     * @param _actual
     * @param _base
     * @return
     */
    private static Double computePercent(AmpIndicator indic, Double _target, Double _actual, Double _base) {
        double actual = (_actual == null) ? 0 : _actual.doubleValue();
        double base = (_base == null) ? actual : _base.doubleValue();//if no base value than using actual as base
        double target = (_target == null) ? actual : _target.doubleValue();
        double result = 0;
        if (indic.getType()!=null && "D".equals(indic.getType())) {
            //descending
            base -= target;
            actual -= target;
            target = 0;
            if (base != 0 && actual != 0) {
                result = actual / (base / 100);
                result = 1 - result / 100;

            }
        } else {
            //ascending
            result = actual / target;
        }
        return new Double(result);
    }

    private static int extractYearInt(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int iYear = cal.get(Calendar.YEAR);
        return iYear;
    }

    private static String extractYearString(Date date) {
        String sYear = null;
        if (date != null) {
            int iYear = extractYearInt(date);
            sYear = String.valueOf(iYear);
        }
        return sYear;
    }

    private static boolean isInSelectedYears(AmpIndicatorValue value, String[] selYars) {
        String sYear = extractYearString(value.getValueDate());
        if (sYear != null && selYars!=null) {
        	//we can use this method because selYears are sorted by sort() method of Arrays class
        	return Arrays.binarySearch(selYars, sYear) >= 0;
//            for (int i = 0; i < selYars.length; i++) {
//                if (selYars[i].equals(sYear)) {
//                    return true;
//                }
//            }
        }
        return false;
    }


}
