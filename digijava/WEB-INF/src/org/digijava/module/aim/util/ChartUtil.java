package org.digijava.module.aim.util;

import java.awt.Color;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.digijava.module.aim.helper.Constants;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer3D;
import org.jfree.data.category.CategoryDataset;

public class ChartUtil {
  private static Logger logger = Logger.getLogger(ChartUtil.class);

  public static final int CHART_TYPE_BAR = 1;
  public static final int CHART_TYPE_STACKED_BARS = 2;
  public static final int CHRAT_TYPE_STACKED_BARS_PERCENTAGE = 3;

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
    rangeAxis.setRange(0D, 1D);
    rangeAxis.setNumberFormatOverride(new DecimalFormat("###%"));

    // disable bar outlines...
    BarRenderer renderer = (BarRenderer) plot.getRenderer();
    renderer.setDrawBarOutline(false);

    // set up gradient paints for series...
	renderer.setSeriesPaint(0,Constants.TARGET_VAL_CLR);									
	renderer.setSeriesPaint(1,Constants.ACTUAL_VAL_CLR);
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
//	    final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
//	    rangeAxis.setRange(0D, 1D);
//	    rangeAxis.setNumberFormatOverride(new DecimalFormat("###%"));

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

//	    return setupChart(chart, dataset);
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

}

class ChartSetting {

}
