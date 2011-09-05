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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.WorkerException;
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

	public static String getPortfolioRiskChartFileName(HttpSession session,PrintWriter pw,
			int chartWidth,int chartHeight,String url, HttpServletRequest request) throws WorkerException {
        String siteId=RequestUtils.getSiteDomain(request).getSite().getId().toString();
        String langCode= RequestUtils.getNavigationLanguage(request).getCode();
		Collection activityIds = new ArrayList();
		if (session.getAttribute(Constants.AMP_PROJECTS) != null) {
			ArrayList projects = (ArrayList) session.getAttribute(Constants.AMP_PROJECTS);
			for (int i = 0;i < projects.size();i ++) {
				AmpProject proj = (AmpProject) projects.get(i);
				activityIds.add(proj.getAmpActivityId());
			}
		}

		ArrayList col = (ArrayList) MEIndicatorsUtil.getPortfolioMEIndicatorRisks(activityIds);


        for (Iterator valIter = col.iterator(); valIter.hasNext(); ) {
            MEIndicatorRisk item = (MEIndicatorRisk) valIter.next();
            String value = item.getRisk();
            String key =  value.toLowerCase();
            key = key.replaceAll(" ", "");
            String msg = TranslatorWorker.translateText(key, langCode, siteId);
            item.setRisk(msg);
        }

		Collections.sort(col);
		ChartParams cp = new ChartParams();
		cp.setChartHeight(chartHeight);
		cp.setChartWidth(chartWidth);
		cp.setData(col);
		cp.setTitle("");
		cp.setSession(session);
		cp.setWriter(pw);
		cp.setUrl(url);


		return generateRiskChart(cp,request);
	}

	public static String getActivityRiskChartFileName(Long actId,
			HttpSession session,PrintWriter pw,
			int chartWidth,int chartHeight,String url, HttpServletRequest request) throws Exception{

        String siteId=RequestUtils.getSiteDomain(request).getSite().getId().toString();
        String langCode= RequestUtils.getNavigationLanguage(request).getCode();

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
                String msg = TranslatorWorker.translateText(key, langCode, siteId);
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
		return generateRiskChart(cp,request);
	}

	public static String getPortfolioPerformanceChartFileName(Long actId,Long indId,
			Integer page,HttpSession session,PrintWriter pw,
			int chartWidth,int chartHeight,String url,boolean includeBaseline, HttpServletRequest request) throws WorkerException {

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
		ArrayList temp = (ArrayList) MEIndicatorsUtil.getPortfolioMEIndicatorValues(
				activityIds,indId,includeBaseline, request);
        String siteId=RequestUtils.getSiteDomain(request).getSite().getId().toString();
        String langCode= RequestUtils.getNavigationLanguage(request).getCode();

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
        for (Iterator valIter = col.iterator(); valIter.hasNext(); ) {
            MEIndicatorValue item = (MEIndicatorValue) valIter.next();
            String value = item.getType();
            String key = value.toLowerCase();
            key = key.replaceAll(" ", "");
            String msg = TranslatorWorker.translateText(key, langCode, siteId);
            item.setType(msg);
        }

		ChartParams cp = new ChartParams();
		cp.setChartHeight(chartHeight);
		cp.setChartWidth(chartWidth);
		cp.setData(col);
		cp.setTitle("");
		cp.setSession(session);
		cp.setWriter(pw);
		cp.setUrl(url);

		return generatePerformanceChart(cp,request);
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
		


//		Set<IndicatorActivity> valuesActivity=ActivityUtil.loadActivity(actId).getIndicators();
//			if(valuesActivity!=null && valuesActivity.size()>0){
//				Iterator<IndicatorActivity> it=valuesActivity.iterator();
//				while(it.hasNext()){
//					 IndicatorActivity indActivity=it.next();
//					 values=indActivity.getValues();
//					 for(Iterator valuesIter=values.iterator();valuesIter.hasNext();){
//						AmpIndicatorValue value=(AmpIndicatorValue)valuesIter.next();
//						String val=new Integer(value.getValueType()).toString();
//						String key = KEY_PERFORMANCE_PREFIX+ val.toLowerCase();
//						key = key.replaceAll(" ", "");
//						String msg = CategoryManagerUtil.translate(key, request, val);
//						//item.setType(msg);
//
//					}
//				}
//			}



//		Collection meIndValues = MEIndicatorsUtil.getMEIndicatorValues(actId,includeBaseline);
//        for (Iterator valIter = meIndValues.iterator(); valIter.hasNext(); ) {
//            MEIndicatorValue item = (MEIndicatorValue) valIter.next();
//            String value = item.getType();
//            String key = KEY_PERFORMANCE_PREFIX+ value.toLowerCase();
//            key = key.replaceAll(" ", "");
//            String msg = CategoryManagerUtil.translate(key, request, value);
//            item.setType(msg);
//        }

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

	public static String generateRiskChart(ChartParams cp,HttpServletRequest request) {
		String fileName = null;
        String siteId=RequestUtils.getSiteDomain(request).getSite().getId().toString();
        String langCode= RequestUtils.getNavigationLanguage(request).getCode();
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

                        String msg = TranslatorWorker.translateText(riskName, langCode, siteId);
						ds.setValue(msg,count);

					}
				}




//				while (itr.hasNext()) {
//					AmpIndicatorRiskRatings risk=itr.next();
//					//MEIndicatorRisk risk = (MEIndicatorRisk) itr.next();
//
//					switch (risk.getRatingValue()) {
//					case Constants.HIGHLY_SATISFACTORY:
//						seriesColors[index++] = Constants.HIGHLY_SATISFACTORY_CLR;
//						break;
//					case Constants.VERY_SATISFACTORY:
//						seriesColors[index++] = Constants.VERY_SATISFACTORY_CLR;
//						break;
//					case Constants.SATISFACTORY:
//						seriesColors[index++] = Constants.SATISFACTORY_CLR;
//						break;
//					case Constants.UNSATISFACTORY:
//						seriesColors[index++] = Constants.UNSATISFACTORY_CLR;
//						break;
//					case Constants.VERY_UNSATISFACTORY:
//						seriesColors[index++] = Constants.VERY_UNSATISFACTORY_CLR;
//						break;
//					case Constants.HIGHLY_UNSATISFACTORY:
//						seriesColors[index++] = Constants.HIGHLY_UNSATISFACTORY_CLR;
//					}
//
//					ds.setValue("High",-1);
//					ds.setValue("hehee",0);
//					ds.setValue("Low",1);
//					if (risk.getRatingValue()<0){
//						ds.setValue(risk.getTranslatedRatingName(),risk.getRatingValue());
//					}else{
//						ds.setValue(risk.getTranslatedRatingName(),risk.getRatingValue());
//					}
//				}

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
					plot.setURLGenerator(new PieChartURLGenerator(url,"risk",langCode,siteId));
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

	public static String generatePerformanceChart(ChartParams cp,HttpServletRequest request) {
        String siteId=RequestUtils.getSiteDomain(request).getSite().getId().toString();
        String langCode= RequestUtils.getNavigationLanguage(request).getCode();

		String fileName = null;
		Double baseValue=null,actualValue=null,targetValue=null;
		String baseValueType=null,targetValueType=null,actualValueType=null;
		DefaultCategoryDataset ds = new DefaultCategoryDataset();
		try {
			Collection data=cp.getData();
			if(data!=null)
			for (Iterator iterator = data.iterator(); iterator.hasNext();) {
				IndicatorActivity connection = (IndicatorActivity) iterator.next();

				Collection<AmpIndicatorValue> col = connection.getValues();
				if (col != null && col.size() > 0) {
					String indicatorName=connection.getIndicator().getName();

					Iterator<AmpIndicatorValue> itr = col.iterator();
					boolean revisedAlreadyParsed=false;
					while (itr.hasNext()) {
						AmpIndicatorValue ampIndValue = itr.next();
						if (ampIndValue.getValueType() == AmpIndicatorValue.BASE) {
							baseValue = ampIndValue.getValue();
							baseValueType =  "base".toLowerCase();
							baseValueType = baseValueType.replaceAll(" ", "");
							String msg = TranslatorWorker.translateText(baseValueType, langCode, siteId);
							baseValueType=msg;
						} else if (ampIndValue.getValueType() == AmpIndicatorValue.ACTUAL) {
							actualValue = ampIndValue.getValue();
							actualValueType = "actual".toLowerCase();
							actualValueType = actualValueType.replaceAll(" ", "");
							  String msg = TranslatorWorker.translateText(actualValueType, langCode, siteId);
							actualValueType=msg;
						} else if (ampIndValue.getValueType() == AmpIndicatorValue.TARGET && !revisedAlreadyParsed) {
							targetValue = ampIndValue.getValue();
							targetValueType = "target".toLowerCase();
							targetValueType = targetValueType.replaceAll(" ", "");
							  String msg = TranslatorWorker.translateText(targetValueType, langCode, siteId);
							targetValueType=msg;

						} else if (ampIndValue.getValueType() == AmpIndicatorValue.REVISED) {
							targetValue = ampIndValue.getValue();
							targetValueType =  "target".toLowerCase();
							targetValueType = targetValueType.replaceAll(" ", "");
							 String msg = TranslatorWorker.translateText(targetValueType, langCode, siteId);
							targetValueType=msg;
							revisedAlreadyParsed=true;//this is used to not overwrite revised with target.
						}
					}
					if (baseValue<=actualValue&& actualValue<=targetValue){
						actualValue=actualValue-baseValue;
						targetValue=targetValue-baseValue;
						actualValue=(100f*actualValue)/targetValue;
						targetValue=100-actualValue;
					} else {
						double result = 0;
						baseValue -= targetValue;
			            actualValue -= targetValue;
			            targetValue = baseValue;
			            if (baseValue != 0 && actualValue != 0) {
			                result = actualValue / (baseValue / 100);
			                actualValue=100-result;
			                targetValue=100 - actualValue;
			            }
						//ds.addValue(baseValue,baseValueType,indicatorName);
					}
					ds.addValue(actualValue,actualValueType,indicatorName);
					ds.addValue(targetValue,targetValueType,indicatorName);

				}
			}


			JFreeChart chart = ChartFactory.createStackedBarChart(
					cp.getTitle(), // title
					null,	// X-axis label
					null,	// Y-axis label
					ds,		// dataset
					PlotOrientation.VERTICAL,	// Orientation
					true,	// show legend
					false,	// show tooltips
					false);	// show urls
			chart.setBackgroundPaint(Color.WHITE);

			CategoryPlot plot = (CategoryPlot) chart.getPlot();

			CategoryItemRenderer r1 = plot.getRenderer();   //new StackedBarRenderer();
			//r1.setSeriesPaint(0,Constants.BASE_VAL_CLR);
			r1.setSeriesPaint(0,Constants.ACTUAL_VAL_CLR);
			r1.setSeriesPaint(1,Constants.TARGET_VAL_CLR);

			/*
			if (includeBaseLine) {
				r1.setSeriesPaint(0,Constants.BASE_VAL_CLR);
				r1.setSeriesPaint(1,Constants.ACTUAL_VAL_CLR);
				r1.setSeriesPaint(2,Constants.TARGET_VAL_CLR);
			} else {
				r1.setSeriesPaint(0,Constants.ACTUAL_VAL_CLR);
				r1.setSeriesPaint(1,Constants.TARGET_VAL_CLR);
			}*/


			String url = cp.getUrl();
			if (url != null && url.trim().length() > 0) {
				StandardCategoryURLGenerator cUrl1 = new StandardCategoryURLGenerator(url,"series","ind");
//				CategoryURLGenerator cUrl1 = new CategoryUrlGen(url);
				r1.setItemURLGenerator(cUrl1);
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
			ChartUtilities.writeImageMap(cp.getWriter(),fileName,info,false);
			cp.getWriter().flush();




		} catch (Exception e) {
			logger.error("Exception from generatePerformanceChart() :" + e.getMessage());
			e.printStackTrace();
		}
 		return fileName;
	}

    public static JFreeChart generatePerformanceChart(ChartParams cp,Long siteId,String langCode) {
			JFreeChart chart =null;
			Double baseValue=null,actualValue=null,targetValue=null;
			String baseValueType=null,targetValueType=null,actualValueType=null;
			DefaultCategoryDataset ds = new DefaultCategoryDataset();
			try {
				Collection data=cp.getData();
				if(data!=null)
				for (Iterator iterator = data.iterator(); iterator.hasNext();) {
					IndicatorActivity connection = (IndicatorActivity) iterator.next();

					Collection<AmpIndicatorValue> col = connection.getValues();
					if (col != null && col.size() > 0) {
						String indicatorName=connection.getIndicator().getName();

						Iterator<AmpIndicatorValue> itr = col.iterator();
						boolean revisedAlreadyParsed=false;
						while (itr.hasNext()) {
							AmpIndicatorValue ampIndValue = itr.next();
							if (ampIndValue.getValueType() == AmpIndicatorValue.BASE) {
								baseValue = ampIndValue.getValue();
								baseValueType =  "base".toLowerCase();
								baseValueType = baseValueType.replaceAll(" ", "");
								String msg = TranslatorWorker.translateText(baseValueType, langCode, siteId);
								baseValueType=msg;
							} else if (ampIndValue.getValueType() == AmpIndicatorValue.ACTUAL) {
								actualValue = ampIndValue.getValue();
								actualValueType = "actual".toLowerCase();
								actualValueType = actualValueType.replaceAll(" ", "");
								  String msg = TranslatorWorker.translateText(actualValueType, langCode, siteId);
								actualValueType=msg;
							} else if (ampIndValue.getValueType() == AmpIndicatorValue.TARGET && !revisedAlreadyParsed) {
								targetValue = ampIndValue.getValue();
								targetValueType = "target".toLowerCase();
								targetValueType = targetValueType.replaceAll(" ", "");
								  String msg = TranslatorWorker.translateText(targetValueType, langCode, siteId);
								targetValueType=msg;

							} else if (ampIndValue.getValueType() == AmpIndicatorValue.REVISED) {
								targetValue = ampIndValue.getValue();
								targetValueType =  "target".toLowerCase();
								targetValueType = targetValueType.replaceAll(" ", "");
								 String msg = TranslatorWorker.translateText(targetValueType, langCode, siteId);
								targetValueType=msg;
								revisedAlreadyParsed=true;//this is used to not overwrite revised with target.
							}
						}
						if (baseValue<=actualValue&& actualValue<=targetValue){
							actualValue=actualValue-baseValue;
							targetValue=targetValue-baseValue;
							actualValue=(100f*actualValue)/targetValue;
							targetValue=100-actualValue;
						} else {
							double result = 0;
							baseValue -= targetValue;
				            actualValue -= targetValue;
				            targetValue = baseValue;
				            if (baseValue != 0 && actualValue != 0) {
				                result = actualValue / (baseValue / 100);
				                actualValue=100-result;
				                targetValue=100 - actualValue;
				            }
							//ds.addValue(baseValue,baseValueType,indicatorName);
						}
						ds.addValue(actualValue,actualValueType,indicatorName);
						ds.addValue(targetValue,targetValueType,indicatorName);

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

    public static JFreeChart generateRiskChart(ChartParams cp,Long siteId,String langCode) {
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
                    Integer count=riskCount.get(risk.getRatingValue());
                    if (count==null){
                        //this is first one o the type
                        count=new Integer(1);
                    }else{
                        //this is not first one so increment
                        count=new Integer(count+1);
                    }
                    riskCount.put(String.valueOf(risk.getRatingValue()), count);
                    riskValues.put(String.valueOf(risk.getRatingValue()), risk.getAmpIndRiskRatingsId().intValue());
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
