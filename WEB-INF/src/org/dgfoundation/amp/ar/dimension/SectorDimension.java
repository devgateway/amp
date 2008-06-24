/**
 * SectorDimension.java (c) 2007 Development Gateway Foundation
 */
package org.dgfoundation.amp.ar.dimension;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.cell.Cell;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpSector;

/**
 * SectorDimension.java TODO description here
 * 
 * @author mihai
 * @package org.dgfoundation.amp.ar.dimension
 * @since 27.02.2008
 */
public class SectorDimension extends ARDimension {

    public SectorDimension() {
	super();
    }

    @Override
    public void initialize() throws HibernateException, SQLException {
	Session session = PersistenceManager.getSession();
	Query createQuery = session.createQuery("from "+AmpSector.class.getName());
	HashMap<Long,Long> sectorMap=new HashMap<Long, Long>();
	links.put(AmpSector.class, sectorMap);
	List list;
	    list = createQuery.list();
	Iterator it=list.iterator();
	while (it.hasNext()) {
	       AmpSector as= (AmpSector) it.next();
	       sectorMap.put(as.getAmpSectorId(), as.getParentSectorId()==null?null:as.getParentSectorId().getAmpSectorId());
	}
	
	session.close();
	
    }

   
    
}
