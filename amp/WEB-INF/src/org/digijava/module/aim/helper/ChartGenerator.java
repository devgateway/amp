/*
 * ChartGenerator.java
 * Created : 18-APr-2006
 */

package org.digijava.module.aim.helper;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.util.IndicatorUtil;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class ChartGenerator {

    private static Logger logger = Logger.getLogger(ChartGenerator.class);

    public static final String KEY_RISK_PREFIX="aim:risk:";
    public static final String KEY_PERFORMANCE_PREFIX="aim:performance:";


    public static String getActivityRiskChartFileName(Long actId,
            HttpSession session,PrintWriter pw,
            int chartWidth,int chartHeight,String url) throws Exception{

        ArrayList<AmpIndicatorRiskRatings> risks = new ArrayList<>();
        Set<IndicatorActivity> valuesActivity = IndicatorUtil.getAllIndicatorsForActivity(actId);
        if (valuesActivity != null && valuesActivity.size() > 0) {
            for (IndicatorActivity indActivity : valuesActivity) {
                if (indActivity.getRisk() != null) {
                    risks.add(indActivity.getRisk());
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

        Set<IndicatorActivity> values = IndicatorUtil.getAllIndicatorsForActivity(actId);

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
                        ds,     // dataset
                        true,   // show legend
                        false,  // show tooltips
                        false); // show urls
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
        //  numAxis.setNumberFormatOverride(new DecimalFormat("%"));

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
                if (ampIndValue.getValue() != null) {
                    targetValue = getSmallStuff(ampIndValue, "target", siteId, langCode);
                }
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
                        null,   // X-axis label
                        null,   // Y-axis label
                        ds,     // dataset
                        PlotOrientation.VERTICAL,   // Orientation
                        true,   // show legend
                        false,  // show tooltips
                        false); // show urls
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
                        ds,     // dataset
                        true,   // show legend
                        false,  // show tooltips
                        false); // show urls
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
