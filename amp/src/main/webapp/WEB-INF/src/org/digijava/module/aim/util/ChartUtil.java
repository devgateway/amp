package org.digijava.module.aim.util;

import java.awt.Color;
import java.text.DecimalFormat;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.digijava.module.aim.helper.Constants;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.CustomCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer3D;
import org.jfree.data.category.CategoryDataset;

public class ChartUtil {
  private static Logger logger = Logger.getLogger(ChartUtil.class);

  public static final String LATEST_GRAPH_MAP = "LAST_GRAP_MAP";

  public static final int CHART_TYPE_BAR = 1;
  public static final int CHART_TYPE_STACKED_BARS = 2;
  public static final int CHRAT_TYPE_STACKED_BARS_PERCENTAGE = 3;
  public static final Color COLOR_BLUE_START = Color.blue;
  public static final int COLOR_BLUE_DELTA = -20;
  public static final int COLOR_RED_DELTA = 1;
  public static final int COLOR_GREEN_DELTA = 1;
  public static final int CHART_WIDTH = 550;
  public static final int CHART_HEIGHT = 400;

//  private static JFreeChart setupChart(JFreeChart chart,
//                                       CategoryDataset dataset) {
//
//    /** @todo We should have chart settings and use here */
//
//
//    return chart;
//  }

  private static JFreeChart createBarChart(CategoryDataset dataset) {

    // create the chart...
    JFreeChart chart = ChartFactory.createBarChart3D(
        null, // chart title
        null, // domain axis label
        null, // range axis label
        dataset, // data
        PlotOrientation.VERTICAL, // orientation
        true, // include legend
        true, // tooltips?
        false // URLs?
        );


    // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

    // set the background color for the chart...
    chart.setBackgroundPaint(Color.WHITE);
//  Calendar year = Calendar.getInstance();
//  int ser=0;
//  while(year.get(Calendar.YEAR) != ProgramUtil.YAERS_LIST_START){
//      renderer.setSeriesPaint(ser++,color);
//      color = getNextColor(color);
//      year.roll(Calendar.YEAR,false);
//  }

    // get a reference to the plot for further customisation...
    CategoryPlot plot = chart.getCategoryPlot();

    // set the range axis to display integers only...
    final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
    rangeAxis.setRange(0D, 1D);
    rangeAxis.setNumberFormatOverride(new DecimalFormat("###%"));

    // disable bar outlines...
    BarRenderer renderer = (BarRenderer) plot.getRenderer();
    renderer.setDrawBarOutline(false);
      renderer.setBaseToolTipGenerator(new CustomCategoryToolTipGenerator());
    //renderer.setItemLabelsVisible(false);

    // set up gradient paints for series...
    renderer.setSeriesPaint(0,new Color(0,0,255));                                  
    renderer.setSeriesPaint(1,new Color(0,204,255));
    renderer.setSeriesPaint(2,new Color(204,255,255));
    renderer.setItemMargin(0);  

//  Color color=COLOR_BLUE_START;
//  Calendar year = Calendar.getInstance();
//  int ser=0;
//  while(year.get(Calendar.YEAR) != ProgramUtil.YAERS_LIST_START){
//      renderer.setSeriesPaint(ser++,color);
//      color = getNextColor(color);
//      year.roll(Calendar.YEAR,false);
//  }

//    GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue,0.0f, 0.0f, new Color(0, 0, 64));
//    GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green,0.0f, 0.0f, new Color(0, 64, 0));
//    GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red,0.0f, 0.0f, new Color(64, 0, 0));
//    renderer.setSeriesPaint(0, gp0);
//    renderer.setSeriesPaint(1, gp1);
//    renderer.setSeriesPaint(2, gp2);

    CategoryAxis domainAxis = plot.getDomainAxis();
    domainAxis.setCategoryLabelPositions(
        CategoryLabelPositions.createUpRotationLabelPositions(
            Math.PI / 6.0));
        if (dataset != null) {
            for (int i = 0; i < dataset.getColumnCount(); i++) {
                String categoryName = (String) dataset.getColumnKey(i);
                domainAxis.addCategoryLabelToolTip(categoryName, categoryName);
            }

        }
 
    // OPTIONAL CUSTOMISATION COMPLETED.

    return chart;

//    return setupChart(chart, dataset);
  }

  private static JFreeChart createStackedBarChart(CategoryDataset dataset) {

    // create the chart...
    JFreeChart chart = ChartFactory.createStackedBarChart(
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
//    final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
//    rangeAxis.setRange(0D, 1D);
//    rangeAxis.setNumberFormatOverride(new DecimalFormat("###%"));

    // disable bar outlines...
    StackedBarRenderer renderer = (StackedBarRenderer) plot.getRenderer();
    renderer.setDrawBarOutline(false);

    // set up Simple paints for series...
    renderer.setSeriesPaint(0,Constants.TARGET_VAL_CLR);                                    
    renderer.setSeriesPaint(1,Constants.ACTUAL_VAL_CLR);
//    renderer.setSeriesPaint(0, Color.blue);
//    renderer.setSeriesPaint(1, Color.green);
//    renderer.setSeriesPaint(2, Color.red);

    CategoryAxis domainAxis = plot.getDomainAxis();
    domainAxis.setCategoryLabelPositions(
        CategoryLabelPositions.createUpRotationLabelPositions(
            Math.PI / 6.0));
    // OPTIONAL CUSTOMISATION COMPLETED.

    return chart;

//    return setupChart(chart, dataset);
  }
 
  private static JFreeChart createStackedPercentBarChart(CategoryDataset dataset) {

        // create the chart...
        JFreeChart chart = ChartFactory.createStackedBarChart(
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
//      final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
//      rangeAxis.setRange(0D, 1D);
//      rangeAxis.setNumberFormatOverride(new DecimalFormat("###%"));

        StackedBarRenderer3D renderer = new StackedBarRenderer3D();//(StackedBarRenderer) plot.getRenderer();

        renderer.setSeriesPaint(1,Constants.TARGET_VAL_CLR);                                    
        renderer.setSeriesPaint(0,Constants.ACTUAL_VAL_CLR);
        
        renderer.setDrawBarOutline(false);

        plot.setRenderer(renderer);
        

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(
                Math.PI / 6.0));
        
        NumberAxis numAxis = (NumberAxis) plot.getRangeAxis();
        numAxis.setRange(0D,1D);
        numAxis.setNumberFormatOverride(new DecimalFormat("###%"));
        

        return chart;

//      return setupChart(chart, dataset);
      }
  
  public static Color getNextColor(Color oldColor){
      Color result = new Color(oldColor.getRed() + COLOR_RED_DELTA,
              oldColor.getGreen()+COLOR_GREEN_DELTA,
              oldColor.getBlue()+COLOR_BLUE_DELTA);
      return result;
  }
  
  public static JFreeChart createChart(CategoryDataset dataset, int chartType) {
    switch (chartType) {
      case CHART_TYPE_BAR:
          return createBarChart(dataset);
      case CHART_TYPE_STACKED_BARS:
          return createStackedBarChart(dataset);
      case CHRAT_TYPE_STACKED_BARS_PERCENTAGE:
          return createStackedPercentBarChart(dataset);
      default:
          return createBarChart(dataset);
    }

  }

    public static class GraphMapRecord{
        public Long timestamp;
        public String map;

        public String getMap() {
            return map;
        }

        public void setMap(String map) {
            this.map = map;
        }
    }

    /**
     * Save map in Http Session.
     * @param map http map tag definition
     * @param timestamp timestamp of ajax request from client side
     * @param session http session bean
     */
    public static void saveMap(String map, Long timestamp, HttpSession session) {
        ChartUtil.GraphMapRecord rec = null;
        synchronized (session) {
            rec = (ChartUtil.GraphMapRecord) session.getAttribute(LATEST_GRAPH_MAP);
        }
        // TODO rec can also be accessed from multiple request (thrads) so access to rec also should be synchronized
        if (rec != null && map != null && timestamp != null
                && rec.timestamp != null && rec.map != null) {
            if (timestamp.compareTo(rec.timestamp) > 0) {
                rec.timestamp = timestamp;
                rec.map = map;
                synchronized (session) {
                    session.setAttribute(LATEST_GRAPH_MAP, rec);
                }
            }
        } else {
            rec = new ChartUtil.GraphMapRecord();
            rec.timestamp = timestamp;
            rec.map = map;
            synchronized (session) {
                session.setAttribute(LATEST_GRAPH_MAP, rec);
            }
        }
    }

  
}

