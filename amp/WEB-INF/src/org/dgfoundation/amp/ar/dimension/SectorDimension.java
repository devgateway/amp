/**
 * SectorDimension.java (c) 2007 Development Gateway Foundation
 */
package org.dgfoundation.amp.ar.dimension;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpSector;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        Query createQuery = session.createQuery("select sector from "+AmpSector.class.getName()+" sector where (sector.deleted is null or sector.deleted = false)");
        HashMap<Long,Long> sectorMap=new HashMap<Long, Long>();
        links.put(AmpSector.class, sectorMap);
        List list;
        list = createQuery.list();
        Iterator it=list.iterator();
        while (it.hasNext()) {
            AmpSector as= (AmpSector) it.next();
            sectorMap.put(as.getAmpSectorId(), as.getParentSectorId()==null?null:as.getParentSectorId().getAmpSectorId());
        }
    }

    @Override
    public Long getParentObject(Long parentId, Class relatedContentPersisterClass) {
        // TODO Auto-generated method stub
        Map<Long,Long> m=links.get(relatedContentPersisterClass);
        if(m!=null)
            return m.get(parentId);
        return null;
    }


}
