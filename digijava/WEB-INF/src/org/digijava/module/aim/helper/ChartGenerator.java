/*
 * ChartGenerator.java
 * Created : 18-APr-2006
 */

package org.digijava.module.aim.helper;

import java.awt.Color;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.chart.urls.StandardPieURLGenerator;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class ChartGenerator {
	
	private static Logger logger = Logger.getLogger(ChartGenerator.class);
	
	public static String getPortfolioRiskChartFileName(HttpSession session,PrintWriter pw,
			int chartWidth,int chartHeight,String url) {
		
		Collection activityIds = new ArrayList();
		if (session.getAttribute(Constants.AMP_PROJECTS) != null) {
			ArrayList projects = (ArrayList) session.getAttribute(Constants.AMP_PROJECTS);
			for (int i = 0;i < projects.size();i ++) {
				AmpProject proj = (AmpProject) projects.get(i);
				activityIds.add(proj.getAmpActivityId());
			}
		}
		
		ArrayList col = (ArrayList) MEIndicatorsUtil.getPortfolioMEIndicatorRisks(
				activityIds);
		Collections.sort(col);
		return generateRiskChart(col,"",
				chartWidth,chartHeight,session,pw,url);
	}
	
	public static String getActivityRiskChartFileName(Long actId,
			HttpSession session,PrintWriter pw,
			int chartWidth,int chartHeight,String url) {
		
		ArrayList meRisks = (ArrayList) MEIndicatorsUtil.getMEIndicatorRisks(actId);
		Collections.sort(meRisks);
		return generateRiskChart(meRisks,"",chartWidth,
				chartHeight,session,pw,url);
	}
	
	public static String getPortfolioPerformanceChartFileName(Long actId,Long indId,
			Integer page,HttpSession session,PrintWriter pw,
			int chartWidth,int chartHeight,String url) {
	
		Collection activityIds = new ArrayList();
		if (actId.longValue() < 0) {
			if (session.getAttribute(Constants.AMP_PROJECTS) != null) {
				ArrayList projects = (ArrayList) session.getAttribute(Constants.AMP_PROJECTS);
				for (int i = 0;i < projects.size();i ++) {
					AmpProject proj = (AmpProject) projects.get(i);
					activityIds.add(proj.getAmpActivityId());
				}
			}
		} else {
			activityIds.add(actId);
		}
		
		Collection col = new ArrayList();
		ArrayList temp = (ArrayList) MEIndicatorsUtil.getPortfolioMEIndicatorValues(activityIds,indId);
		
		if ((actId.longValue() > 0 && indId.longValue() <= 0) ||
				(actId.longValue() <= 0 && indId.longValue() > 0)) {
			int st = (page.intValue() - 1) * 30; 
			int ed = st + 30;
			ed = (ed > temp.size()) ? temp.size() : ed;
			for (int i = st; i < ed;i ++) {
				col.add(temp.get(i));
			}
		} else {
			col = temp;
		}
		return generatePerformanceChart(col,"",
				chartWidth,chartHeight,session,pw,url);
	}		
	
	public static String getActivityPerformanceChartFileName(Long actId,
			HttpSession session,PrintWriter pw,
			int chartWidth,int chartHeight,String url) {
		
		Collection meIndValues = MEIndicatorsUtil.getMEIndicatorValues(actId);
		return generatePerformanceChart(meIndValues,"",
				chartWidth,chartHeight,session,pw,url);
	}
	
	public static String generateRiskChart(Collection col,String title,
			int chartWidth,int chartHeight,HttpSession session,PrintWriter pw,
			String url) {
		String fileName = null;
		try {
			if (col != null && col.size() > 0) {
				Iterator itr = col.iterator();
				
				DefaultPieDataset ds = new DefaultPieDataset();
				
				Color seriesColors[] = new Color[col.size()];
				int index = 0;
				while (itr.hasNext()) {
					MEIndicatorRisk risk = (MEIndicatorRisk) itr.next();
					
					switch (risk.getRiskRating()) {
					case Constants.HIGHLY_SATISFACTORY:
						seriesColors[index++] = Constants.HIGHLY_SATISFACTORY_CLR;
						break;
					case Constants.VERY_SATISFACTORY:
						seriesColors[index++] = Constants.VERY_SATISFACTORY_CLR;
						break;
					case Constants.SATISFACTORY:
						seriesColors[index++] = Constants.SATISFACTORY_CLR;
						break;
					case Constants.UNSATISFACTORY:
						seriesColors[index++] = Constants.UNSATISFACTORY_CLR;
						break;
					case Constants.VERY_UNSATISFACTORY:
						seriesColors[index++] = Constants.VERY_UNSATISFACTORY_CLR;
						break;
					case Constants.HIGHLY_UNSATISFACTORY:
						seriesColors[index++] = Constants.HIGHLY_UNSATISFACTORY_CLR;
					}
					
					ds.setValue(risk.getRisk(),risk.getRiskCount());
				}
				
				JFreeChart chart = ChartFactory.createPieChart(
						title, // title
						ds,		// dataset
						true,	// show legend
						false,	// show tooltips
						false);	// show urls
				chart.setBackgroundPaint(Color.WHITE);
				
				PiePlot plot = (PiePlot) chart.getPlot();
				for (int i = 0;i < index;i ++) {
					plot.setSectionPaint(i,seriesColors[i]);
				}
				
				if (url != null && url.trim().length() > 0) {
					plot.setURLGenerator(new StandardPieURLGenerator(url,"risk"));
				}
				ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
				
				fileName = ServletUtilities.saveChartAsPNG(chart,chartWidth,
						chartHeight,info,session);
				ChartUtilities.writeImageMap(pw,fileName,info,false);
				
				pw.flush();
			}
		} catch (Exception e) {
			logger.error("Exception from generateRisk() :" + e.getMessage());
			e.printStackTrace(System.out);
		}		
		return fileName;		
	}
	
	public static String generatePerformanceChart(Collection col,String title,
			int chartWidth,int chartHeight,HttpSession session,PrintWriter pw,
			String url) {
		
		String fileName = null;
		try {
			if (col != null && col.size() > 0) {
				Iterator itr = col.iterator();
				DefaultCategoryDataset ds = new DefaultCategoryDataset();
				while (itr.hasNext()) {
					MEIndicatorValue meIndVal = (MEIndicatorValue) itr.next();
					ds.addValue(meIndVal.getValue(),meIndVal.getType(),meIndVal.getIndicatorName());
				}
				
				JFreeChart chart = ChartFactory.createStackedBarChart(
						title, // title
						null,	// X-axis label
						null,	// Y-axis label
						ds,		// dataset
						PlotOrientation.VERTICAL,	// Orientation
						true,	// show legend
						false,	// show tooltips
						false);	// show urls
				chart.setBackgroundPaint(Color.WHITE);
				
				CategoryPlot plot = (CategoryPlot) chart.getPlot();
				
				StackedBarRenderer r1 = new StackedBarRenderer();
				r1.setSeriesPaint(0,Constants.BASE_VAL_CLR);
				r1.setSeriesPaint(1,Constants.ACTUAL_VAL_CLR);
				r1.setSeriesPaint(2,Constants.TARGET_VAL_CLR);
				if (url != null && url.trim().length() > 0) {
					CategoryURLGenerator cUrl1 = new StandardCategoryURLGenerator(url,"series","ind");
					r1.setItemURLGenerator(cUrl1);
				}
				plot.setRenderer(r1);
				
				ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
				fileName = ServletUtilities.saveChartAsPNG(chart,chartWidth,
						chartHeight,info,session);
				ChartUtilities.writeImageMap(pw,fileName,info,false);
				pw.flush();
			}

		} catch (Exception e) {
			logger.error("Exception from generatePerformanceChart() :" + e.getMessage());
			e.printStackTrace(System.out);
		}
 		return fileName;		
	}
}