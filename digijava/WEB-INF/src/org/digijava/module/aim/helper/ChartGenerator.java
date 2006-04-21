/*
 * ChartGenerator.java
 * Created : 18-APr-2006
 */

package org.digijava.module.aim.helper;

import java.awt.Color;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class ChartGenerator {
	
	private static Logger logger = Logger.getLogger(ChartGenerator.class);
	
	public static String generateActivityRiskChart(Long actId,
			HttpSession session,PrintWriter pw) {
		
		String fileName = null;
		try {
			Collection meRisks = MEIndicatorsUtil.getMEIndicatorRisks(actId);
			if (meRisks.size() > 0) {
				Iterator itr = meRisks.iterator();
				
				DefaultPieDataset ds = new DefaultPieDataset();
				while (itr.hasNext()) {
					MEIndicatorRisk risk = (MEIndicatorRisk) itr.next();
					ds.setValue(risk.getRisk(),risk.getRiskCount());
				}
				
				JFreeChart chart = ChartFactory.createPieChart(
						Constants.ACTIVITY_RISK_CHART_TITLE, // title
						ds,		// dataset
						true,	// show legend
						false,	// show tooltips
						false);	// show urls
				chart.setBackgroundPaint(Color.WHITE);
				ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
				fileName = ServletUtilities.saveChartAsPNG(chart,Constants.CHART_WIDTH,
						Constants.CHART_HEIGHT,info,session);
				ChartUtilities.writeImageMap(pw,fileName,info,false);
				pw.flush();
				
			} else {
				fileName = "chart_no_data.png";
			}

		} catch (Exception e) {
			logger.error("Exception from generateActivityRisk() :" + e.getMessage());
			e.printStackTrace(System.out);
			fileName = "chart_error.png";
		}		
		return fileName;
	}
	
	public static String generateActivityPerformanceChart(Long actId,
			HttpSession session,PrintWriter pw) {
		
		String fileName = null;
		try {
			Collection meIndValues = MEIndicatorsUtil.getMEIndicatorValues(actId);
			if (meIndValues.size() > 0) {
				Iterator itr = meIndValues.iterator();
				DefaultCategoryDataset ds = new DefaultCategoryDataset();
				while (itr.hasNext()) {
					MEIndicatorValue meIndVal = (MEIndicatorValue) itr.next();
					ds.addValue(meIndVal.getValue(),meIndVal.getType(),meIndVal.getIndicatorName());
				}
				
				JFreeChart chart = ChartFactory.createStackedBarChart(
						Constants.ACTIVITY_PERFORMANCE_CHART_TITLE, // title
						null,	// X-axis label
						null,	// Y-axis label
						ds,		// dataset
						PlotOrientation.VERTICAL,	// Orientation
						true,	// show legend
						false,	// show tooltips
						false);	// show urls
				chart.setBackgroundPaint(Color.WHITE);
				ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
				fileName = ServletUtilities.saveChartAsPNG(chart,Constants.CHART_WIDTH,
						Constants.CHART_HEIGHT,info,session);
				ChartUtilities.writeImageMap(pw,fileName,info,false);
				pw.flush();
				
			} else {
				fileName = "chart_no_data.png";
			}

		} catch (Exception e) {
			logger.error("Exception from generateActivityPerformanceChart() :" + e.getMessage());
			e.printStackTrace(System.out);
			fileName = "chart_error.png";
		}
 		return fileName;
	}
}