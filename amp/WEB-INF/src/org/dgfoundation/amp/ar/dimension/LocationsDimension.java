/**
 * 
 */
package org.dgfoundation.amp.ar.dimension;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Alex Gartner
 *
 */
public class LocationsDimension extends ARDimension {

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.dimension.ARDimension#getParentObject(java.lang.Long, java.lang.Class)
     */
    @Override
    public Long getParentObject(Long parentId,
            Class relatedContentPersisterClass) {
        
        Map<Long,Long> m=links.get(relatedContentPersisterClass);
        if(m!=null)
            return m.get(parentId);
        return null;
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.dimension.ARDimension#initialize()
     */
    @Override
    public void initialize() throws HibernateException, SQLException {
        Session session = PersistenceManager.getSession();
        Query createQuery = session.createQuery("from "+AmpCategoryValueLocations.class.getName());
        HashMap<Long,Long> locationsMap=new HashMap<Long, Long>();
        links.put(AmpCategoryValueLocations.class, locationsMap);
        List<AmpCategoryValueLocations> list;
        list = createQuery.list();
        Iterator<AmpCategoryValueLocations> it=list.iterator();
        while (it.hasNext()) {
            AmpCategoryValueLocations loc=  it.next();
            locationsMap.put(loc.getId(), loc.getParentLocation()==null?null:loc.getParentLocation().getId() );
        }

    }

}
