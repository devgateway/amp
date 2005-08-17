package org.digijava.module.aim.helper;

import java.util.Collection;
import java.util.Vector;

import net.sf.jasperreports.engine.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.*;
import org.jfree.data.general.DefaultPieDataset;

import org.jfree.util.Rotation;

public class WebappScriptlet extends JRDefaultScriptlet
{
	static Vector v= new Vector();

	public void setV(Collection c){
		if(v!=null)
			v.clear();
		v.addAll(c);
		
		String str="";
		for(int j=0;j<v.size();j++){
			////logger.info("#######"+v.get(j).toString());
		}	
	}
/*
	public Vector getV(){
		//logger.info("++++++++++++++++++++++++"+v.size());
		return v;
	}
*/	
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
/*		dataset.setValue("DGF", new Double(23.2));
		dataset.setValue("NCST", new Double(30.0));
		dataset.setValue("MoFED", new Double(25.5));
		dataset.setValue("OECD", new Double(12.5));
		dataset.setValue("WB", new Double(20.0));
*/
/*		JFreeChart chart = 
			ChartFactory.createPieChart3D(
				"Pie Chart for Annual Team Forecasting Report (ViewProjects)",
				dataset,
				true,
				false,
				false
				);
*/
		JFreeChart chart = 
			ChartFactory.createPieChart(
				"Annual Team Forecasting Report Pie Chart (ViewProjects).",
				dataset,
				true,
				true,
				false
				);

//		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		PiePlot plot = (PiePlot) chart.getPlot();
/*		plot.setStartAngle(90);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		plot.setNoDataMessage("No data to display");
*/
		this.setVariableValue("Chart", new JCommonDrawableRenderer(chart));

	}


	public void afterGroupInit(String groupName) throws JRScriptletException
	{
		String allCities = (String)this.getVariableValue("AllCities");
		String city = (String)this.getFieldValue("City");
		StringBuffer sbuffer = new StringBuffer();
		
		if (allCities != null)
		{
			sbuffer.append(allCities);
			sbuffer.append(", ");
		}
		
		sbuffer.append(city);
		this.setVariableValue("AllCities", sbuffer.toString());
	}


}
