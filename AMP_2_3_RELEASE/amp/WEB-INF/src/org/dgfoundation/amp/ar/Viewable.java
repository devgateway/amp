/**
 * Viewable.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Class describing a viewable behaviour. Viewable objects always have a viewer
 * name for each view modes (types). A viewer is responsible for showing the contents of the cell to the end user. 
 * One type of cell can have many viewers, depending on the number of export formats that we require (html,cvs,xls,pdf,etc...)
 * 
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 23, 2006
 * 
 */
public abstract class Viewable implements Cloneable {


		public abstract ReportData getNearestReportData();
	
    	/**
    	 * required by : pagination
    	 * implemented by each subclass to return the number of rows that are displayed for this item.
    	 * example. each amount cell should display one row. a groupReportData would display more rows - the title of the hierarchy + the totals row = 2 rows
    	 * @return the number of rows actively used on the screen to render this object
    	 */
    	public abstract int getVisibleRows();
    
	//thread safe cached viewerPath
	protected static Hashtable viewerPaths=new Hashtable();
	
	/**
	 * returns the viewer name for the specified view type.
	 * 
	 * @param viewType
	 *            the viewer type
	 * @return the viewer name
	 */
	public String getViewerName(String viewType) {
		return null;
	}

	/**
	 * Returns the full name of the viewer (along with the path)
	 * 
	 * @param viewType
	 *            the view type for which the viewer is requested
	 * @return the full viewer path
	 */
	public String getViewerPath(String viewType) {
		String className = this.getClass().getName();
		String key=className+viewType;
		String value=(String) viewerPaths.get(key);
		if(value!=null) return value;
		int idx = className.lastIndexOf('.');
		for (int i = 0; i < ArConstants.prefixes.length; i++)
			if (ArConstants.prefixes[i].getCategory().equals(viewType)) {
				viewerPaths.put(key, (String) ArConstants.prefixes[i].getValue()
						+ className.substring(idx + 1, className.length())
						+ (String) ArConstants.suffixes[i].getValue() );
				return (String) viewerPaths.get(key);
			}
		return null;
	}

	/**
	 * This method is invoked by parent exporter items. It will instantiate the
	 * appropriate Exporter class for the viewable item that is the child of the
	 * exporter parent invoker. The class is dynamically instantiated as an
	 * Exporter subclass. Exporters are java based viewers.
	 * @see Exporter
	 * @param parent
	 */
	public void invokeExporter(Exporter parent) {
			invokeExporter(parent, false);
	}
	
	public void invokeExporter(Exporter parent, boolean useBudgetClasses) {
		this.invokeExporter(parent, useBudgetClasses, false);
	}
	
	public void invokeExporter(Exporter parent, boolean useBudgetClasses, boolean isPlainReport) {
		// try to instantiate the Generator
		try {
			// get the exporter class for this Viewable
			String viewer = getViewerPath();
			Class c = Class.forName(viewer);
			// get the first constructor - it SHOULD be the one that receives an
			// Exporter object (parent)
			Constructor cons	= ARUtil.getConstrByParamNo(c,2,useBudgetClasses, isPlainReport);
			// instantiate an exporter object with reference to the parent
			ARUtil.getConstrByParamNo(c,2);
			Exporter exp = (Exporter) cons.newInstance(new Object[] { parent,
					this });
			// invoke generate method to this object
			exp.generate();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getViewerPath() {
		return getViewerPath(getCurrentView());
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * @return Returns the currentView.
	 */
	public abstract String getCurrentView();


	
}
