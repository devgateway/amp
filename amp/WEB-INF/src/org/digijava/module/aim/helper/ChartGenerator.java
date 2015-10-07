/*
 * ChartGenerator.java
 * Created : 18-APr-2006
 */

package org.digijava.module.aim.helper;

import java.awt.Color;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityVersion ;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpMEIndicatorValue;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;



public class ChartGenerator {

	private static Logger logger = Logger.getLogger(ChartGenerator.class);

	public static final String KEY_RISK_PREFIX="aim:risk:";
	public static final String KEY_PERFORMANCE_PREFIX="aim:performance:";


	public static String getActivityRiskChartFileName(Long actId,
			HttpSession session,PrintWriter pw,
			int chartWidth,int chartHeight,String url) throws Exception{

		ArrayList<AmpIndicatorRiskRatings> risks=new ArrayList<AmpIndicatorRiskRatings>();
		if(session.getAttribute("indsRisks")!=null){
			 risks=(ArrayList<AmpIndicatorRiskRatings>)session.getAttribute("indsRisks");
			 session.removeAttribute("indsRisks");
		}else{
			Set<IndicatorActivity> valuesActivity=IndicatorUtil.getAllIndicatorsForActivity(actId);
			if(valuesActivity!=null && valuesActivity.size()>0){
				Iterator<IndicatorActivity> it=valuesActivity.iterator();
				while(it.hasNext()){
					 IndicatorActivity indActivity=it.next();
					 Set<AmpIndicatorValue> values=indActivity.getValues();
					 for(Iterator<AmpIndicatorValue> valuesIter=values.iterator();valuesIter.hasNext();){
						 AmpIndicatorValue val=valuesIter.next();
						 if(val.getRisk()!=null){
							 risks.add(val.getRisk());
							 break;//TODO INDIC because this is stupid! all values have same risk and this risk should go to connection.
						 }
					}
				}
			}
		}
		

		//ArrayList meRisks = (ArrayList) MEIndicatorsUtil.getMEIndicatorRisks(actId);
        for (Iterator<AmpIndicatorRiskRatings> riskIter = risks.iterator(); riskIter.hasNext(); ) {
        	AmpIndicatorRiskRatings item = (AmpIndicatorRiskRatings) riskIter.next();
        	if(item!=null){
        		String value = item.getRatingName();
                String key = value.toLowerCase();
                key = key.replaceAll(" ", "");
                String msg = TranslatorWorker.translateText(key);
                item.setTranslatedRatingName(msg);
        	}            
        }

        //Collections.sort((List)risks);


		ChartParams cp = new ChartParams();
		cp.setChartHeight(chartHeight);
		cp.setChartWidth(chartWidth);
		cp.setData(risks);
		cp.setTitle("");
		cp.setSession(session);
		cp.setWriter(pw);
		cp.setUrl(url);
		return generateRiskChart(cp);
	}

	public static String getActivityPerformanceChartFileName(Long actId,HttpSession session,PrintWriter pw,
			int chartWidth,int chartHeight,String url,boolean includeBaseline, HttpServletRequest request) throws Exception{

		
		Set<IndicatorActivity> values=null;
		Collection<ActivityIndicator> actIndicators = (Collection)session.getAttribute("indsME");
		session.removeAttribute("indsME");
		if(actIndicators!=null && actIndicators.size()>0){
			for (ActivityIndicator actInd : actIndicators) {
				AmpIndicatorRiskRatings risk=null;
                
		          AmpIndicator ind=IndicatorUtil.getIndicator(actInd.getIndicatorId());
		          if(actInd.getRisk()!=null && actInd.getRisk().longValue()>0){
		        	  risk=IndicatorUtil.getRisk(actInd.getRisk());  
		          }

		          AmpCategoryValue categoryValue = null;
		          if(actInd.getIndicatorsCategory() != null && actInd.getIndicatorsCategory().getId() != null){
		        	  categoryValue = CategoryManagerUtil.getAmpCategoryValueFromDb(actInd.getIndicatorsCategory().getId());
		          }		          
		         
		          IndicatorActivity indConn=new IndicatorActivity();
		          indConn.setIndicator(ind);
		          indConn.setValues(new HashSet<AmpIndicatorValue>());
		          //create each type of value and assign to connection
		          AmpIndicatorValue indValActual = null;
		          if (actInd.getCurrentVal()!=null){
		        	  indValActual = new AmpIndicatorValue();
		        	  indValActual.setValueType(AmpIndicatorValue.ACTUAL);
		        	  indValActual.setValue(new Double(actInd.getCurrentVal()));
		        	  indValActual.setComment(actInd.getCurrentValComments());
		        	  indValActual.setValueDate(DateConversion.getDate(actInd.getCurrentValDate()));
		        	  indValActual.setRisk(risk);
		        	  indValActual.setLogFrame(categoryValue);
		        	  indValActual.setIndicatorConnection(indConn);
		        	  indConn.getValues().add(indValActual);
		          }
		          AmpIndicatorValue indValTarget = null;
		          if (actInd.getTargetVal()!=null){
		        	  indValTarget = new AmpIndicatorValue();
		        	  indValTarget.setValueType(AmpIndicatorValue.TARGET);
		        	  indValTarget.setValue(new Double(actInd.getTargetVal()));
		        	  indValTarget.setComment(actInd.getTargetValComments());
		        	  indValTarget.setValueDate(DateConversion.getDate(actInd.getTargetValDate()));
		        	  indValTarget.setRisk(risk);
		        	  indValTarget.setLogFrame(categoryValue);
		        	  indValTarget.setIndicatorConnection(indConn);
		        	  indConn.getValues().add(indValTarget);
		          }
		          AmpIndicatorValue indValBase = null;
		          if (actInd.getBaseVal()!=null){
		        	  indValBase = new AmpIndicatorValue();
		        	  indValBase.setValueType(AmpIndicatorValue.BASE);
		        	  indValBase.setValue(new Double(actInd.getBaseVal()));
		        	  indValBase.setComment(actInd.getBaseValComments());
		        	  indValBase.setValueDate(DateConversion.getDate(actInd.getBaseValDate()));
		        	  indValBase.setRisk(risk);
		        	  indValBase.setLogFrame(categoryValue);
		        	  indValBase.setIndicatorConnection(indConn);
		        	  indConn.getValues().add(indValBase);
		          }
		          AmpIndicatorValue indValRevised = null;
		          if (actInd.getRevisedTargetVal()!=null){
		        	  indValRevised = new AmpIndicatorValue();
		        	  indValRevised.setValueType(AmpIndicatorValue.REVISED);
		        	  indValRevised.setValue(new Double(actInd.getRevisedTargetVal()));
		        	  indValRevised.setComment(actInd.getRevisedTargetValComments());
		        	  indValRevised.setValueDate(DateConversion.getDate(actInd.getRevisedTargetValDate()));
		        	  indValRevised.setRisk(risk);
		        	  indValRevised.setLogFrame(categoryValue);
		        	  indValRevised.setIndicatorConnection(indConn);
		        	  indConn.getValues().add(indValRevised);
		          }
				
		          if(values==null){
		        	  values=new HashSet<IndicatorActivity>();
		          }
		          values.add(indConn);
			}			
		}else{
			values=IndicatorUtil.getAllIndicatorsForActivity(actId);
		}
		

        String retVal = null;

        if (values != null) {
            ChartParams cp = new ChartParams();
            cp.setChartHeight(chartHeight);
            cp.setChartWidth(chartWidth);
            cp.setData(values);
            cp.setTitle("");
            cp.setSession(session);
            cp.setWriter(pw);
            cp.setUrl(url);

            retVal = generatePerformanceChart(cp,request);
        }

		return retVal;
	}

	public static String generateRiskChart(ChartParams cp) {
		String fileName = null;
		Collection<AmpIndicatorRiskRatings> col = cp.getData();
		try {
			if (col != null && col.size() > 0) {
				Iterator<AmpIndicatorRiskRatings> itr = col.iterator();

				DefaultPieDataset ds = new DefaultPieDataset();

				//how many risks are for each risk name
				Map<String,Integer> riskCount=new HashMap<String, Integer> ();
				//what is value of each risk name
				Map<String,Integer> riskValues=new HashMap<String, Integer> ();

				for (AmpIndicatorRiskRatings risk : col) {
					if(risk!=null){
						Integer count=riskCount.get(risk.getRatingName());
						if (count==null){
							//this is first one o the type
							count=new Integer(1);
						}else{
							//this is not first one so increment
							count=new Integer(count+1);
						}
						riskCount.put(risk.getRatingName(), count);
						riskValues.put(risk.getRatingName(), risk.getRatingValue());
					}					
				}

				Color seriesColors[] = new Color[col.size()];
				int index = 0;
				Set<String> types=riskCount.keySet();
				if (types!=null){
					//for each name
					for (String type : types) {
						String riskName=type;
						Integer count=riskCount.get(riskName);
						Integer value=riskValues.get(riskName);

						//for each name/value (low, high) set different color
						switch (value.intValue()) {
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

						//put in datasource

                        String msg = TranslatorWorker.translateText(riskName);
						ds.setValue(msg,count);

					}
				}


				JFreeChart chart = ChartFactory.createPieChart(
						cp.getTitle(), // title
						ds,		// dataset
						true,	// show legend
						false,	// show tooltips
						false);	// show urls
				chart.setBackgroundPaint(Color.WHITE);

				PiePlot plot = (PiePlot) chart.getPlot();
				for (int i = 0;i < index;i ++) {
					plot.setSectionPaint(i,seriesColors[i]);
				}

				String url = cp.getUrl();
				if (url != null && url.trim().length() > 0) {
					plot.setURLGenerator(new PieChartURLGenerator(url,"risk", TLSUtils.getEffectiveLangCode(), TLSUtils.getSiteId()));
				}
				ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());

				fileName = ServletUtilities.saveChartAsPNG(chart,cp.getChartWidth(),
						cp.getChartHeight(),info,cp.getSession());
				ChartUtilities.writeImageMap(cp.getWriter(),fileName,info,false);

				cp.getWriter().flush();
			}
		} catch (Exception e) {
			logger.error("Exception from generateRisk() :" + e.getMessage());
			e.printStackTrace(System.out);
		}
		return fileName;
	}

	private static String generateMapTag(String mapTag, Collection data) throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, Long> indicatorNamesToIds = IndicatorUtil.getIndicatorNamesToIds((Set<IndicatorActivity>)data);
		Pattern pat = Pattern.compile("ind=.*?\"");
		
		StringBuilder bld = new StringBuilder();
		String[] splits = pat.split(mapTag);
		Matcher m = pat.matcher(mapTag);
		for (String split : splits) {
			bld.append(split);
			if (m.find()) {
				//skip the characters "ind=
				String matched = mapTag.substring(m.start() + "ind=".length(), m.end() - "\"".length());
				bld.append("ind=").append(indicatorNamesToIds.get(URLDecoder.decode(matched, "UTF-8"))).append("\"");
			}
		}
		return bld.toString();
	}
	
	public static String generatePerformanceChart(ChartParams cp,HttpServletRequest request) {
		String fileName = null;
		try {
			Site site = RequestUtils.getSite(request);
			Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
			Long siteId = site.getId();
			String locale = navigationLanguage.getCode();
			JFreeChart chart = generatePerformanceChart(cp, siteId, locale);

			CategoryPlot plot = (CategoryPlot) chart.getPlot();

			CategoryItemRenderer r1 = plot.getRenderer();   //new StackedBarRenderer();
			//r1.setSeriesPaint(0,Constants.BASE_VAL_CLR);
			r1.setSeriesPaint(0,Constants.ACTUAL_VAL_CLR);
			r1.setSeriesPaint(1,Constants.TARGET_VAL_CLR);
			String url = cp.getUrl();
			if (url != null && url.trim().length() > 0) {
				StandardCategoryURLGenerator cUrl1 = new StandardCategoryURLGenerator(url,"series","ind");
				r1.setBaseItemURLGenerator(cUrl1);
			}

			plot.setRenderer(r1);
			CategoryAxis axis = plot.getDomainAxis();
			axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
			NumberAxis numAxis = (NumberAxis) plot.getRangeAxis();
			numAxis.setRange(0D,100D);
		//	numAxis.setNumberFormatOverride(new DecimalFormat("%"));

			ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
			fileName = ServletUtilities.saveChartAsPNG(chart,cp.getChartWidth(),
					cp.getChartHeight(),info,cp.getSession());
			
			StringWriter strW = new StringWriter();
			PrintWriter prW = new PrintWriter(strW);
			ChartUtilities.writeImageMap(prW,fileName,info,false);
			String mapTag = strW.toString();
			cp.getWriter().write(generateMapTag(mapTag, cp.getData()));
			cp.getWriter().flush();
		} catch (Exception e) {
			logger.error("Exception from generatePerformanceChart() :" + e.getMessage());
			e.printStackTrace();
		}
 		return fileName;
	}
	
	static  class PairValue {
		public Double value;
		public String type; 
		public PairValue(Double value, String type) {
			this.value = value;
			this.type = type;
		}
	}
	
	static class PerformanceChartValues {
		public PairValue baseValue;
		public PairValue actualValue;
		public PairValue targetValue;
		public PerformanceChartValues(PairValue baseValue, PairValue actualValue, PairValue targetValue) {
			this.baseValue = baseValue;
			this.actualValue = actualValue;
			this.targetValue = targetValue;
		}
	}
	
	private static PairValue getSmallStuff(AmpIndicatorValue ampIndValue, String type, 
			Long siteId, String langCode) {
		String msg = TranslatorWorker.translateText(type, langCode, siteId);
		return new PairValue(ampIndValue.getValue(), msg);
		
	}
	
	private static PerformanceChartValues getValuesFromIndicatorCollection(Collection<AmpIndicatorValue> col, String indicatorName, 
			Long siteId, String langCode) {
		PairValue baseValue = null, actualValue = null, targetValue = null;
		boolean revisedAlreadyParsed=false;
		for (AmpIndicatorValue ampIndValue: col)
			switch (ampIndValue.getValueType()) {
			case AmpIndicatorValue.BASE:
				baseValue = getSmallStuff(ampIndValue, "base", siteId, langCode); 
				break;
			case AmpIndicatorValue.ACTUAL:
				actualValue = getSmallStuff(ampIndValue, "actual", siteId, langCode); 
				break;
			case AmpIndicatorValue.TARGET:
				if (!revisedAlreadyParsed)
					targetValue = getSmallStuff(ampIndValue, "target", siteId, langCode); 
				break;
			case AmpIndicatorValue.REVISED:
				targetValue = getSmallStuff(ampIndValue, "target", siteId, langCode); 
				break;
		}
		return new PerformanceChartValues(baseValue, actualValue, targetValue);
	}

    public static JFreeChart generatePerformanceChart(ChartParams cp, Long siteId, String langCode) {
			JFreeChart chart =null;
			PerformanceChartValues vals = null;
			DefaultCategoryDataset ds = new DefaultCategoryDataset();
			try {
				Collection<IndicatorActivity> data=cp.getData();
				if(data!=null)
				for (IndicatorActivity connection : data) {
					Collection<AmpIndicatorValue> col = connection.getValues();
					String indicatorName = connection.getIndicator().getName();
					if (col != null && col.size() > 0) {
						 vals = getValuesFromIndicatorCollection(col, indicatorName, siteId, langCode);
						 if (vals.baseValue.value <= vals.actualValue.value 
								&& vals.actualValue.value <= vals.targetValue.value){
							vals.actualValue.value = vals.actualValue.value - vals.baseValue.value;
							vals.targetValue.value = vals.targetValue.value - vals.baseValue.value;
							vals.actualValue.value = (100f * vals.actualValue.value) / vals.targetValue.value;
							vals.targetValue.value = 100 - vals.actualValue.value;
						} else {
							double result = 0;
							vals.baseValue.value -= vals.targetValue.value;
				            vals.actualValue.value -= vals.targetValue.value;
				            vals.targetValue.value = vals.baseValue.value;
				            if (vals.baseValue.value != 0 && vals.actualValue.value != 0) {
				                result = vals.actualValue.value / (vals.baseValue.value / 100);
				                vals.actualValue.value = 100 - result;
				                vals.targetValue.value = 100 - vals.actualValue.value;
				            }
						}
						ds.addValue(vals.actualValue.value, vals.actualValue.type, indicatorName);
						ds.addValue(vals.targetValue.value, vals.targetValue.type,indicatorName);
					}
				}
				chart = ChartFactory.createStackedBarChart(
						cp.getTitle(), // title
						null,	// X-axis label
						null,	// Y-axis label
						ds,		// dataset
						PlotOrientation.VERTICAL,	// Orientation
						true,	// show legend
						false,	// show tooltips
						false);	// show urls
				chart.setBackgroundPaint(Color.WHITE);
			} catch (Exception e) {
				logger.error("Exception from generatePerformanceChart() :" + e.getMessage());
				e.printStackTrace();
			}
	 		return chart;
	}

    public static JFreeChart generateRiskChart(ChartParams cp, Long siteId, String langCode) {
        JFreeChart chart =null;
        Collection<AmpIndicatorRiskRatings> col = cp.getData();
        try {
            if (col != null && col.size() > 0) {
                Iterator<AmpIndicatorRiskRatings> itr = col.iterator();

                DefaultPieDataset ds = new DefaultPieDataset();
                //how many risks are for each risk name
                Map<String,Integer> riskCount=new HashMap<String, Integer> ();
                //what is value of each risk name
                Map<String,Integer> riskValues=new HashMap<String, Integer> ();

                for (AmpIndicatorRiskRatings risk : col) {
                    Integer count=riskCount.get(risk.getRatingName());
                    if (count==null){
                        //this is first one o the type
                        count=new Integer(1);
                    }else{
                        //this is not first one so increment
                        count=new Integer(count+1);
                    }
                    riskCount.put(risk.getRatingName(), count);
                    riskValues.put(risk.getRatingName(), risk.getRatingValue());
                }

                Color seriesColors[] = new Color[col.size()];
                int index = 0;
                Set<String> types=riskCount.keySet();
                if (types!=null){
                    //for each name
                    for (String type : types) {
                        String riskName=type;
                        Integer count=riskCount.get(riskName);
                        Integer value=riskValues.get(riskName);

                        //for each name/value (low, high) set different color
                        switch (value.intValue()) {
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

                        //put in datasource
                        String msg = TranslatorWorker.translateText(riskName, langCode, siteId);
                        ds.setValue(msg,count);

                    }
                }

                chart = ChartFactory.createPieChart(
                        cp.getTitle(), // title
                        ds,		// dataset
                        true,	// show legend
                        false,	// show tooltips
                        false);	// show urls
                chart.setBackgroundPaint(Color.WHITE);

                PiePlot plot = (PiePlot) chart.getPlot();
                for (int i = 0;i < index;i ++) {
                    plot.setSectionPaint(i,seriesColors[i]);
                }

            }
        } catch (Exception e) {
            logger.error("Exception from generateRisk() :" + e.getMessage());
            e.printStackTrace(System.out);
        }
        return chart;
    }
    
}
