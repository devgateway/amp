
package org.digijava.module.aim.action;

import org.apache.struts.actions.DispatchAction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.NationalPlaningDashboardForm;
import java.util.Comparator;
import org.digijava.kernel.util.collections.HierarchyDefinition;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.kernel.util.collections.CollectionUtils;
import java.util.Collection;
import java.util.ArrayList;
import org.digijava.module.aim.util.ProgramUtil;
import java.util.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.plot.PlotOrientation;
import java.awt.GradientPaint;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.JFreeChart;
import java.awt.Color;
import org.jfree.data.category.CategoryDataset;
import org.jfree.chart.ChartUtilities;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpThemeIndicators;

public class NationalPlaningDashboardAction
    extends DispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws
        Exception {
        NationalPlaningDashboardForm npdForm =
            (NationalPlaningDashboardForm) form;
        // load all themes
        List themes = ProgramUtil.getAllThemes();
        Collection sortedThemes = CollectionUtils.getFlatHierarchy(themes, true,
            new ProgramHierarchyDefinition(),
            new HierarchicalProgramComparator());
        Long currentThemeId = npdForm.getCurrentProgramId();
        AmpTheme currentTheme = getCurrentTheme(themes, currentThemeId);

        if(currentTheme != null) {
            npdForm.setCurrentProgramId(currentTheme.getAmpThemeId());
        }
        npdForm.setCurrentProgram(currentTheme);

        npdForm.setPrograms(new ArrayList(sortedThemes));

        return mapping.findForward("viewNPDDashboard");
    }

    public ActionForward displayChart(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws
        Exception {
        NationalPlaningDashboardForm npdForm =
            (NationalPlaningDashboardForm) form;

        Long currentThemeId = npdForm.getCurrentProgramId();
        AmpTheme currentTheme = ProgramUtil.getTheme(currentThemeId);

        CategoryDataset dataset = createDataset(currentTheme);
        JFreeChart chart = createChart(dataset);

        response.setContentType("image/png");
        ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 500, 500);

        return null;
    }


    /**
     * Iterates over all themes and finds the theme which must be current. If
     * there is no currentThemeId passed, simply returns the first theme in the
     * list. Otherwise returns theme by the given currentThemeId;
     * @param themes List
     * @param currentThemeId Long
     * @return AmpTheme
     */
    private AmpTheme getCurrentTheme(List themes,
                                     Long currentThemeId) {
        AmpTheme currentTheme = null;
        Iterator iter = themes.iterator();
        while(iter.hasNext()) {
            AmpTheme item = (AmpTheme) iter.next();
            boolean isEqual = false;

            if(currentTheme == null |
               (currentThemeId != null &&
                (isEqual = item.getAmpThemeId().equals(currentThemeId)))) {
                currentTheme = item;

                // Break the loop
                if(isEqual) {
                    break;
                }
            }
        }
        return currentTheme;
    }

    protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws
        Exception {
        NationalPlaningDashboardForm npdForm =
            (NationalPlaningDashboardForm) form;
        npdForm.setShowChart(true);
        return display(mapping, form, request, response);
    }

    private static CategoryDataset createDataset(AmpTheme currentTheme) {

        // row keys...
        String series1 = "Base";
        String series2 = "Actual";
        String series3 = "Target";

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Iterator iter = currentTheme.getIndicators().iterator();
        while(iter.hasNext()) {
            AmpThemeIndicators item = (AmpThemeIndicators) iter.next();
            String displayLabel = item.getName();

            dataset.addValue(Math.random()*100, series1, displayLabel);
            dataset.addValue(Math.random()*100, series2, displayLabel);
            dataset.addValue(Math.random()*100, series3, displayLabel);

        }

        return dataset;

    }

    /**
     * Creates a sample chart.
     *
     * @param dataset  the dataset.
     *
     * @return The chart.
     */
    private static JFreeChart createChart(CategoryDataset dataset) {

        // create the chart...
        JFreeChart chart = ChartFactory.createBarChart(
            null, // chart title
            "Indicator", // domain axis label
            "Percent", // range axis label
            dataset, // data
            PlotOrientation.VERTICAL, // orientation
            true, // include legend
            true, // tooltips?
            false // URLs?
            );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);

        // ******************************************************************
        //  More than 150 demo applications are included with the JFreeChart
        //  Developer Guide...for more information, see:
        //
        //  >   http://www.object-refinery.com/jfreechart/guide.html
        //
        // ******************************************************************

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        // set up gradient paints for series...
        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue,
                                              0.0f, 0.0f, new Color(0, 0, 64));
        GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green,
                                              0.0f, 0.0f, new Color(0, 64, 0));
        GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red,
                                              0.0f, 0.0f, new Color(64, 0, 0));
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(
                Math.PI / 6.0));
        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;

    }

}

class HierarchicalProgramComparator
    implements Comparator {
    public int compare(Object o1, Object o2) {
        AmpTheme i1 = (AmpTheme) o1;
        AmpTheme i2 = (AmpTheme) o2;

        Long sk1 = i1.getAmpThemeId();
        Long sk2 = i2.getAmpThemeId();

        return sk1.compareTo(sk2);
    }
}

class ProgramHierarchyDefinition
    implements HierarchyDefinition {
    public Object getObjectIdentity(Object object) {
        AmpTheme i = (AmpTheme) object;
        return i.getAmpThemeId();

    }

    public Object getParentIdentity(Object object) {
        AmpTheme i = (AmpTheme) object;
        if(i.getParentThemeId() == null) {
            return null;
        } else {
            return i.getParentThemeId().getAmpThemeId();
        }
    }
}
