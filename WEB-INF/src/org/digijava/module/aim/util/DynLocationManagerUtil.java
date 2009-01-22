package org.digijava.module.aim.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.exception.DynLocationStructuralException;
import org.digijava.module.aim.exception.DynLocationStructureStringException;
import org.digijava.module.aim.form.DynLocationManagerForm;
import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DynLocationManagerUtil {
	private static Logger logger = Logger.getLogger(DynLocationManagerUtil.class);
	
	public static Collection<AmpCategoryValueLocations> getHighestLayerLocations(AmpCategoryClass implLoc, DynLocationManagerForm myForm, ActionErrors errors) {
		Collection<AmpCategoryValueLocations> locations			= null;
		Session dbSession										= null;
		Collection<AmpCategoryValueLocations> rootLocations		= null;
		HashSet<AmpCategoryValueLocations> badLayerLocations	= new HashSet<AmpCategoryValueLocations>();
		
		myForm.setUnorganizedLocations(badLayerLocations);
		
		try {
			dbSession			= PersistenceManager.getSession();
			String queryString 	= "select loc from "
				+ AmpCategoryValueLocations.class.getName()
				+ " loc" ;
			Query qry			= dbSession.createQuery(queryString);
			locations			= qry.list();
			if ( locations != null && locations.size() > 0 ) {
				rootLocations				= findRootLocations(locations);
				checkTree(rootLocations, badLayerLocations);
				if ( badLayerLocations.size() > 0 ) {
					String errorListStr		= collectionToString(badLayerLocations);
					errors.add("title", new ActionError("error.aim.dynRegionManager.badLayerProblem", errorListStr) );
				}
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}
		
		return rootLocations;
	}
	
	public static void deleteLocation(Long id, ActionErrors errors) {
		Session dbSession										= null;
		Transaction tx											= null;
		try {
			dbSession						= PersistenceManager.getSession();
			tx								= dbSession.beginTransaction();
			AmpCategoryValueLocations loc	= (AmpCategoryValueLocations)dbSession.load(AmpCategoryValueLocations.class, id);
			if ( loc != null )
				dbSession.delete(loc);
			dbSession.flush();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			if ( errors != null)
				errors.add("title", new ActionError("error.aim.dynRegionManager.locationIsInUse"));
			e.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}		
	}
	
	public static void saveStructure (String treeStructure, String unorganizedLocations,  AmpCategoryClass implLoc, ActionErrors errors) {
		if ( treeStructure.length() < 4 )
				return;
		Collection<AmpCategoryValueLocations> locations			= null;
		Session dbSession										= null;
		HashMap<Long, AmpCategoryValueLocations> locationsMap	= new HashMap<Long, AmpCategoryValueLocations>();
		Transaction tx											= null;
		try {
			dbSession		= PersistenceManager.getSession();
			tx				= dbSession.beginTransaction();
			String queryString 	= "select loc from "
				+ AmpCategoryValueLocations.class.getName()
				+ " loc " ;
			Query qry			= dbSession.createQuery(queryString);
			locations			= qry.list();
			
			Iterator<AmpCategoryValueLocations> iter	= locations.iterator();
			while ( iter.hasNext() ) {
				AmpCategoryValueLocations loc	= iter.next();
				locationsMap.put(loc.getId(), loc);
			}
			
			NodeInfo nodeInfo							= new NodeInfo(treeStructure);
			
			while ( nodeInfo.hasNext() ) {
					nodeInfo.nextInfo();
					Long id				= nodeInfo.getId();
					Long parentId		= nodeInfo.getParentId();
					Integer layerIndex	= nodeInfo.getLayerIndex();
					
					AmpCategoryValueLocations loc		= locationsMap.get(id);
					AmpCategoryValue layer				= implLoc.getPossibleValues().get(layerIndex);
					AmpCategoryValueLocations parent	= locationsMap.get(parentId);
					
					loc.setParentCategoryValue(layer);
					loc.setParentLocation(parent);
					if ( parent != null ) {
						if ( parent.getChildLocations() ==  null )
							parent.setChildLocations(new TreeSet<AmpCategoryValueLocations> (alphabeticalLocComp));
						parent.getChildLocations().add(loc);
					}
				
			}
			
			TreeSet<AmpCategoryValueLocations> returnLocations		= new TreeSet<AmpCategoryValueLocations>(alphabeticalLocComp);
			HashSet<AmpCategoryValueLocations> badLayerLocations	= new HashSet<AmpCategoryValueLocations>();
			
			NodeInfo unorgInfo			= new NodeInfo(unorganizedLocations);
			while ( unorgInfo.hasNext() ) {
				unorgInfo.nextInfo();
				Long id								= unorgInfo.getId();
				AmpCategoryValueLocations loc		= locationsMap.get(id);
				locations.remove( loc );
			}
			
			Collection<AmpCategoryValueLocations> rootLocations		= findRootLocations(locations);
			checkTree(rootLocations, badLayerLocations);
			if ( badLayerLocations.size() > 0 ) {
				String errorListStr		= collectionToString(badLayerLocations);
				if ( errors != null )
					errors.add("title", new ActionError("error.aim.dynRegionManager.badLayerProblem", errorListStr) );
				throw new DynLocationStructuralException("Some locations seem to have the wrong Implementation Location category associated: " 
									+ errorListStr);
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			if ( errors != null)
				errors.add("title", new ActionError("error.aim.dynRegionManager.treeSavingProblem"));
			e.printStackTrace();
		} finally {
			try {
				PersistenceManager.releaseSession(dbSession);
			} catch (Exception ex2) {
				logger.error("releaseSession() failed :" + ex2);
			}
		}		
	}
	
	private static Collection<AmpCategoryValueLocations> findRootLocations (Collection<AmpCategoryValueLocations> allLocations) {
		TreeSet<AmpCategoryValueLocations> returnLocations				= new TreeSet<AmpCategoryValueLocations>(alphabeticalLocComp);
		Iterator<AmpCategoryValueLocations> iterator					= allLocations.iterator();
		while ( iterator.hasNext() ) {
			AmpCategoryValueLocations loc		= iterator.next();
			if ( loc.getParentLocation() == null )
				returnLocations.add(loc);
		}
		return returnLocations;
	}
	
	private static void checkSiblings (Collection<AmpCategoryValueLocations> siblingLocations, 
			Collection<AmpCategoryValueLocations> badLayerLocations, Integer parentLayerIndex) {
		
		Iterator<AmpCategoryValueLocations> iter 	= siblingLocations.iterator();
		while ( iter.hasNext() ) {
			AmpCategoryValueLocations loc 			= iter.next();
			if ( loc.getParentCategoryValue().getIndex() <= parentLayerIndex ){
					loc.getParentLocation().getChildLocations().remove(loc);
					loc.setParentLocation(null);
					badLayerLocations.add(loc);
			}
			else
				if ( loc.getChildLocations() != null && loc.getChildLocations().size() > 0 ) {
					checkSiblings( loc.getChildLocations(), badLayerLocations, loc.getParentCategoryValue().getIndex() );
				}
		}
	}
	
	public static void checkTree (Collection<AmpCategoryValueLocations> rootLocations,
			Collection<AmpCategoryValueLocations> badLayerLocations) {
		
		badLayerLocations.clear();
		
		Iterator<AmpCategoryValueLocations> locIter	= rootLocations.iterator();
		while ( locIter.hasNext() ) {
			AmpCategoryValueLocations tempLoc		= locIter.next();
			if ( tempLoc.getChildLocations() != null && tempLoc.getChildLocations().size() > 0 ) {
				checkSiblings(tempLoc.getChildLocations(), badLayerLocations, tempLoc.getParentCategoryValue().getIndex() );
			}
		}
				
		
	}
	
	public static String collectionToString (Collection<? extends Object> col) throws NullPointerException {
		if ( col == null )
			throw new NullPointerException("col param cannot be null");
		if ( col.size() == 0 )
			return "";
		
		String retString					= "";
		Iterator <? extends Object> iter	= col.iterator();
		while ( true ) {
			retString				+= iter.next();
			if ( iter.hasNext() )
				retString			+= ", ";
			else
				break;
		}
		
		return retString;
	}
	
	
	public static Comparator<AmpCategoryValueLocations> alphabeticalLocComp		=
																	new Comparator<AmpCategoryValueLocations>() {
																		public int compare(AmpCategoryValueLocations o1,
																				AmpCategoryValueLocations o2) {
																			return o1.getName().compareTo(o2.getName());
																		}
																	};  
	
																	
																	
	public static class NodeInfo {
		private int i				= 0;
		private String [] pairs		= null;
		
		private Long id				= null;
		private Long parentId		= null;
		private Integer layerIndex	= null;
		public NodeInfo(String structureStr) {
			this.pairs			= structureStr.split("\\|");
		}
		public void nextInfo() throws DynLocationStructureStringException  {
			if ( i < pairs.length && pairs[i].length() > 0  ) {
				String [] ids	= pairs[i++].split("p|h");
				if ( ids.length != 3 ) {
					throw new DynLocationStructureStringException ("Exactly 3 tokens should be found in each pair. This pair is wrong: " + pairs[i]);
				}
				this.id				= Long.parseLong(ids[0]);
				this.parentId		= Long.parseLong(ids[1]);
				this.layerIndex		= Integer.parseInt(ids[2]);
			}
			else
				i++;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Long getParentId() {
			return parentId;
		}
		public void setParentId(Long parentId) {
			this.parentId = parentId;
		}
		public Integer getLayerIndex() {
			return layerIndex;
		}
		public void setLayerIndex(Integer layerIndex) {
			this.layerIndex = layerIndex;
		}
		public boolean hasNext() {
			return i < this.pairs.length;
		}
		
	}																
}

