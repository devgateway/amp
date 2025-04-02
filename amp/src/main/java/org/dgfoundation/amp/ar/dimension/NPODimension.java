/**
 * 
 */
package org.dgfoundation.amp.ar.dimension;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author Alex Gartner
 */
public class NPODimension extends ARDimension  {

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.dimension.ARDimension#initialize()
     */
    @Override
    public void initialize() throws HibernateException, SQLException {
        Session session = PersistenceManager.getSession();
        Query createQuery = session.createQuery("from "+AmpTheme.class.getName());
        HashMap<Long,Long> programsMap=new HashMap<Long, Long>();
        links.put(AmpTheme.class, programsMap);
        List list;
        list = createQuery.list();
        Iterator it=list.iterator();
        while (it.hasNext()) {
            AmpTheme prog= (AmpTheme) it.next();
            programsMap.put(prog.getAmpThemeId(), prog.getParentThemeId()==null?null:prog.getParentThemeId().getAmpThemeId() );
        }
    }
    
    @Override
    public Long getParentObject(Long parentId,  Class relatedContentPersisterClass) {
        // TODO Auto-generated method stub
        Map<Long,Long> m=links.get(relatedContentPersisterClass);
        if(m!=null)
            return m.get(parentId);
        return null;
    }
}
