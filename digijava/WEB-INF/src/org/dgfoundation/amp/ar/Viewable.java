/**
 * Viewable.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


/**
 * Class describing a viewable behaviour. Viewable objects always have
 * a viewer name for each view modes (types).
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 23, 2006
 *
 */
public abstract class Viewable implements Cloneable {

	/**
	 * returns the viewer name for the specified view type. 
	 * @param viewType the viewer type
	 * @return the viewer name
	 */
	public String getViewerName(String viewType) {
		return null;
	}
	
	/**
	 * Returns the full name of the viewer (along with the path)
	 * @param viewType the view type for which the viewer is requested
	 * @return the full viewer path
	 */
	public String getViewerPath(String viewType) {
		String className=this.getClass().getName();
		int idx=className.lastIndexOf('.');
		for (int i = 0; i < ArConstants.prefixes.length ; i++ )
			if (ArConstants.prefixes[i].getCategory().equals(viewType)) return (String) ArConstants.prefixes[i].getValue() +
			className.substring(idx+1,className.length()) + (String) ArConstants.suffixes[i].getValue();
		return null;
	}	


	public void invokeExporter(Exporter parent) {
		//try to instantiate the Generator
		try {
			//get the exporter class for this Viewable
			String viewer=getViewerPath();
			Class c=Class.forName(viewer);
			//get the first constructor - it SHOULD be the one that receives an Exporter object
			Constructor cons=c.getConstructors()[0];
			//instantiate an exporter object with reference to the parent
			Exporter exp=(Exporter) cons.newInstance(new Object[]{parent,this});
			//invoke generate method to this object
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
