/**
 * ARDimension.java
 * (c) 2007 Development Gateway Foundation
 */
package org.dgfoundation.amp.ar.dimension;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.cell.Cell;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public abstract class ARDimension {

    public static final Map<String,Class> COLUMN_DB_TYPE=new Hashtable<String, Class>();
    public static final Map<Class,ARDimension> DIMENSIONS=new Hashtable<Class, ARDimension>();

    protected HashMap<Class,HashMap<Long,Long>> links;
    
    public HashMap getLinksForClass(Class c) {
	return links.get(c);
    }
    
    /**
     * @deprecated
     * @throws HibernateException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void initializeColumnDBType() throws HibernateException, SQLException, ClassNotFoundException {
	Session session = PersistenceManager.getSession();
	Query createQuery = session.createQuery("from "+AmpColumns.class.getName());
	List list = createQuery.list();
	Iterator i=list.iterator();
	while (i.hasNext()) {
	    AmpColumns c = (AmpColumns) i.next();
	   if(c.getRelatedContentPersisterClass()!=null) COLUMN_DB_TYPE.put(c.getColumnName(), Class.forName(c.getRelatedContentPersisterClass()));	    
	}
	PersistenceManager.releaseSession(session);
    }
    
    public ARDimension() { 
	links=new HashMap<Class, HashMap<Long, Long>>();
	try {
	    initialize();
	} catch (HibernateException e) {
	    // TODO Auto-generated catch block
	    throw new RuntimeException( "HibernateException Exception encountered", e);
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    throw new RuntimeException( "SQLException Exception encountered", e);
	}	  
    }
    
    
    public static boolean isLinkedWith(ReportData parent, Cell childCell) {
	//we get the dimension worker    
	// requirements for translation purposes
	String siteId = parent.getReportMetadata().getSiteId();
	String locale = parent.getReportMetadata().getLocale();
	String text = ArConstants.UNALLOCATED;
	String translatedTextUnallocated = null;
	//String prefix = "aim:reportGenerator:"; not used, trn hash keys used.
	try {
		translatedTextUnallocated = TranslatorWorker.translateText(text, locale, siteId);
	} catch (WorkerException e) {
		e.printStackTrace();
	}
	if (translatedTextUnallocated.compareTo("") == 0)
		translatedTextUnallocated = text;
	
	//See AMP-9522
	if(childCell.getValue().toString().contains(translatedTextUnallocated)) return true;
	
	Class relatedContentPersisterClass = childCell.getColumn().getRelatedContentPersisterClass();
	if(relatedContentPersisterClass==null) return true; // default behavior is to accept anything we have no information about
	Class dimensionClass=childCell.getColumn().getDimensionClass();
	if (dimensionClass == null)
		return true;
	
	ARDimension d=DIMENSIONS.get(dimensionClass);
	if(d==null) {
	    Constructor dimensionCons = ARUtil.getConstrByParamNo(dimensionClass,0);
	    try {
		 d = (ARDimension) dimensionCons.newInstance();
		DIMENSIONS.put(dimensionClass, d);
	    } catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		throw new RuntimeException( "IllegalArgumentException Exception encountered", e);
	    } catch (InstantiationException e) {
		// TODO Auto-generated catch block
		throw new RuntimeException( "InstantiationException Exception encountered", e);
	    } catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		throw new RuntimeException( "IllegalAccessException Exception encountered", e);
	    } catch (InvocationTargetException e) {
		// TODO Auto-generated catch block
		throw new RuntimeException( "InvocationTargetException Exception encountered", e);
	    }	   
	}
	return d.internalIsLinkedWith(parent,childCell);	
    }
   
    public abstract void initialize() throws HibernateException, SQLException;
    
    /**
     * 
     * @param parentId
     * @param relatedContentPersisterClass
     * @return 
     * if the parent and child of the hierarchy are of the same type (like sectors or NPO) we have to get all the parents and grandparents
     * if the parent and child are different types like donor group and donor agency, we do not have to get the grandparents.
     */
    public abstract Long getParentObject(Long parentId, Class relatedContentPersisterClass);
    
    public boolean internalIsLinkedWith(ReportData parent, Cell childCell) {
	Set<Cell> s = new HashSet<Cell>();
	parent.appendAllSplitterCells(s);	
	if(s.size()==0) return true;
	Iterator i = s.iterator();
	while (i.hasNext()) {
	    Cell c = (Cell) i.next();
	    Class relatedContentPersisterClass = c.getColumn().getRelatedContentPersisterClass();
	    //this is the class of the parent object responsible for the hierarchy
	    //we check if we have a mapping for that class. the mapping will hold its children.
	    Map<Long,Long> m=links.get(relatedContentPersisterClass);
	    if(m!=null) {
		Long childId=childCell.getId();
		
		Long parentId=null;
		int ii=0;
		Long newParent = null;
		do {
		    
			parentId = m.get(childId);
			newParent = getParentObject(childId, relatedContentPersisterClass);
			if(newParent!= null) childId=newParent;
		    if(parentId!=null && parentId.equals(c.getId())) break;
		    
		}while(newParent!=null);
		
		if( !(parentId!=null && parentId.equals(c.getId()))) return false;
	    } 	    
	}
	return true;
    }

    
}
