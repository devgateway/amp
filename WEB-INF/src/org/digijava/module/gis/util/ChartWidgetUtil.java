package org.digijava.module.gis.util;

import org.digijava.module.aim.dbentity.AmpIndicator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;

/**
 * Chart widgets util.
 * @author Irakli Kobiashvili
 *
 */
public class ChartWidgetUtil {

	public static JFreeChart getIndicatorChart(AmpIndicator indicator){
		JFreeChart result = null;
		CategoryDataset ds = getIndicatorChartDataset(indicator);
		PlotOrientation orientation = PlotOrientation.HORIZONTAL;
		String valuesAxisLabel = "Value";
		String categAxisLabel = "Time";
		String title = indicator.getName();
		boolean legend = false;
		boolean tooltips = false;
		boolean urls = false;
		result = ChartFactory.createLineChart(title, categAxisLabel, valuesAxisLabel, ds, orientation, legend, tooltips, urls);
		
		return result;
	}
	public static CategoryDataset getIndicatorChartDataset(AmpIndicator indicator){
		return null;
	}
}
