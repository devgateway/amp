
package org.digijava.module.aim.action;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.Color;
import java.awt.GradientPaint;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.collections.CollectionUtils;
import org.digijava.kernel.util.collections.HierarchyDefinition;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.AmpThemeIndicators;
import org.digijava.module.aim.form.NationalPlaningDashboardForm;
import org.digijava.module.aim.util.ProgramUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import java.util.Arrays;
import java.util.Set;
import java.util.*;

public class NationalPlaningDashboardAction
    extends DispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws
        Exception {
        return doDisplay(mapping, form, request, response, false);
    }

    public ActionForward displayWithFilter(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws
        Exception {
        return doDisplay(mapping, form, request, response, true);
    }


    protected ActionForward doDisplay(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response, boolean filter) throws
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

        if(currentTheme != null) {
            if(!filter) {
                long[] ids = getIndicatorIds(currentTheme);
                npdForm.setSelectedIndicators(ids);
            }

            npdForm.setActivities(getActivities(currentTheme));
        }
        return mapping.findForward("viewNPDDashboard");
    }

    private long[] getIndicatorIds(AmpTheme currentTheme) {
        long[] ids = null;
            Set indicators = currentTheme.getIndicators();
            if (indicators == null) {
            } else {
                ids = new long[indicators.size()];
                int i=0;
                Iterator iter = indicators.iterator();
                while(iter.hasNext()) {
                    AmpThemeIndicators item = (AmpThemeIndicators) iter.next();
                    ids[i++] = item.getAmpThemeIndId().longValue();
                }
            }
        return ids;
    }


    public ActionForward displayChart(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws
        Exception {
        NationalPlaningDashboardForm npdForm =
            (NationalPlaningDashboardForm) form;

        Long currentThemeId = npdForm.getCurrentProgramId();
        AmpTheme currentTheme = ProgramUtil.getTheme(currentThemeId);

        CategoryDataset dataset = createDataset(currentTheme, npdForm.getSelectedIndicators());
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

    private static CategoryDataset createDataset(AmpTheme currentTheme, long[] selectedIndicators) {

        // row keys...
        String series1 = "Base";
        String series2 = "Actual";
        String series3 = "Target";

        Arrays.sort(selectedIndicators);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Iterator iter = currentTheme.getIndicators().iterator();
        while(iter.hasNext()) {
            AmpThemeIndicators item = (AmpThemeIndicators) iter.next();

            int pos = Arrays.binarySearch(selectedIndicators,
                                          item.getAmpThemeIndId().longValue());
            if (pos >= 0) {

                String displayLabel = item.getName();

                dataset.addValue(Math.random(), series1, displayLabel);
                dataset.addValue(Math.random(), series2, displayLabel);
                dataset.addValue(Math.random(), series3, displayLabel);
            }
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
            null, // domain axis label
            null, // range axis label
            dataset, // data
            PlotOrientation.VERTICAL, // orientation
            true, // include legend
            false, // tooltips?
            false // URLs?
            );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.WHITE);

        // get a reference to the plot for further customisation...
        CategoryPlot plot = chart.getCategoryPlot();

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0D,1D);
        rangeAxis.setNumberFormatOverride(new DecimalFormat("###%"));


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

    public static List getActivities(AmpTheme theme) throws HibernateException,
        SQLException {
        Session session = null;
        Query qry = null;

        session = PersistenceManager.getSession();
        String queryString = "from " + AmpActivity.class.getName() +
            " ampAct where ampAct.themeId.ampThemeId=:ampThemeId " +
            " and ampAct.status.statusCode=:statusCode order by ampAct.name";

        qry = session.createQuery(queryString);
        qry.setParameter("ampThemeId", theme.getAmpThemeId());
        qry.setParameter("statusCode", "1"); // 1 stands for "Planned"

        return qry.list();
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
