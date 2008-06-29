package org.digijava.module.aim.helper;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class JFreeChartScriptlet extends JRDefaultScriptlet
{
	static Vector v= new Vector();
	static String chartTitle="";

	public void setV(Collection c){
		if(v!=null)
			v.clear();
		v.addAll(c);
		
		String str="";
		for(int j=0;j<v.size();j++){
			////System.out.println("####### JFREE SCRIPTLET"+v.get(j).toString());
		}	
	}
/*
	public Vector getV(){
		//logger.info("++++++++++++++++++++++++"+v.size());
		return v;
	}
*/	
	public void setChartTitle(String str){
		chartTitle = str;
		//////System.out.println("CHART TITLE----------------"+chartTitle);
	}


	public void afterReportInit() throws JRScriptletException
	{
	
	//logger.info("Inside Jfree Webapp SCRIPTLET....6");

	DefaultPieDataset dataset = new DefaultPieDataset();


// should be dynamic.
		String str="test";
		Double dbl;
		for(int j=0;j<v.size();j++){
			dbl=new Double(v.get(j).toString());
			j++;
			str=v.get(j).toString();
			//logger.info("******"+str+"******"+dbl);
			dataset.setValue(str, dbl);
		}

		//logger.info("=====================v size: "+v.size());
		JFreeChart chart = 
			ChartFactory.createPieChart(
				chartTitle+" - Pie Chart",
				dataset,
				true,
				true,
				false
				);

		this.setVariableValue("Chart", new JCommonDrawableRenderer(chart));

// Bar chart Processing.

		DefaultCategoryDataset data = new DefaultCategoryDataset(); 
	        Iterator iter = v.iterator();
//			logger.info("@@@@@@@@@@ flag:"+col.size());

/*			data.addValue(new Double(50.0), "WORLD-BANK", "");
			data.addValue(new Double(40.0), "UNICEF", "");
			data.addValue(new Double(20.0), "MOFED", "");
			data.addValue(new Double(30.0), "CDAC", "");
*/			
			String temp="";
			Double demp;

			while (iter.hasNext()) {
				demp=new Double(iter.next().toString());
				temp= (String)iter.next();
//				////System.out.println(temp+":::::"+demp);
				data.addValue(demp,temp,"comm");
				demp=new Double(iter.next().toString());
				temp= (String)iter.next();
//				////System.out.println(temp+":::::"+demp);
				data.addValue(demp,temp,"disb");
				demp=new Double(iter.next().toString());
				temp= (String)iter.next();
//				////System.out.println(temp+":::::"+demp);
				data.addValue(demp,temp,"exp");

				
	        }
			
		
		CategoryDataset categorydataset = new DefaultCategoryDataset();
		categorydataset = data;

		JFreeChart chart2 = 
//			ChartFactory.createBarChart("Bar Chart for Custom Report.",dataset,true,true,false);
			ChartFactory.createBarChart(chartTitle+" - Bar Chart", "title", "Amount(in Thousand $)", categorydataset, PlotOrientation.VERTICAL, true, true, true);

		this.setVariableValue("Chart2", new JCommonDrawableRenderer(chart2));

}

}
