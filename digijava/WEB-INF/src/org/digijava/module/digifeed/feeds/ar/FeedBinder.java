/**
 * FeedBinder.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.digifeed.feeds.ar;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpMeasure;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.digifeed.core.GenericFeedBinder;
import org.digijava.module.digifeed.feeds.ar.schema.MeasureType;
import org.digijava.module.digifeed.feeds.ar.schema.ObjectFactory;
import org.digijava.module.digifeed.feeds.ar.schema.ReportType;
import org.digijava.module.digifeed.feeds.ar.schema.Reports;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 27, 2005
 * 
 */
public class FeedBinder extends GenericFeedBinder {
	public static SimpleDateFormat sdt = new SimpleDateFormat("dd-MM-yyyy");

	private static Logger logger = Logger.getLogger(FeedBinder.class);

	private ObjectFactory fact;

	/**
	 * @param source
	 */
	public FeedBinder() {
		super();
		fact = new ObjectFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.digijava.module.digifeed.core.GenericFeedBinder#createXMLBean(java.lang.Object)
	 */
	protected Object createXMLBean(Object dbb) throws JAXBException,
			ParseException {
		AmpReports dbr=(AmpReports) dbb;
		
		ReportType xmlr=fact.createReportType();
		xmlr.setName(dbr.getName());
		xmlr.setDescription(dbr.getReportDescription());
		xmlr.setHideActivities(dbr.getHideActivities()!=null?dbr.getHideActivities().booleanValue():false);
		xmlr.setOptions(dbr.getOptions());
		xmlr.setType(BigInteger.valueOf(dbr.getType().longValue()));
		
		Iterator i=dbr.getMeasures().iterator();
		while (i.hasNext()) {
			AmpMeasure element = (AmpMeasure) i.next();
			MeasureType xmlm=fact.createMeasureType();
			xmlm.setId(BigInteger.valueOf(element.getAmpMeasureId().longValue()));
			xmlm.setValue(element.getName());
			xmlr.getMeasure().add(xmlm);
		}
		
		i=dbr.getColumns().iterator();
		while (i.hasNext()) {
			AmpColumns element = (AmpColumns) i.next();
			MeasureType xmlm=fact.createMeasureType();
			xmlm.setId(BigInteger.valueOf(element.getColumnId().longValue()));
			xmlm.setValue(element.getColumnName());
			xmlr.getColumn().add(xmlm);
		}
				
		i=dbr.getHierarchies().iterator();
		while (i.hasNext()) {
			AmpReportHierarchy element = (AmpReportHierarchy) i.next();
			MeasureType xmlm=fact.createMeasureType();
			xmlm.setId(BigInteger.valueOf(element.getColumn().getColumnId().longValue()));
			xmlm.setValue(element.getColumn().getColumnName());
			xmlr.getColumn().add(xmlm);
		}

		
		
		
		return xmlr;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.digijava.module.digifeed.core.GenericFeedBinder#createDBBean(java.lang.Object)
	 */

	protected Object createDBBean(Object xmlb) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.digijava.module.digifeed.core.GenericFeedBinder#createFinalTree()
	 */
	protected void createFinalTree() throws JAXBException {
		this.finalTree = fact.createReports();
		Reports reps = (Reports) finalTree;


		Iterator i = dst.iterator();
		while (i.hasNext()) {
			Object element = (Object) i.next();
			reps.getReport().add(element);
		}
	}

}
