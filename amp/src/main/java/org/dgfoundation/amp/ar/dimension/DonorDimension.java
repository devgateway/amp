/**
 * DonorDimension.java
 * (c) 2007 Development Gateway Foundation
 */
package org.dgfoundation.amp.ar.dimension;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * DonorDimension.java
 * TODO description here
 * @author mihai
 * @package org.dgfoundation.amp.ar.dimension
 * @since 28.02.2008
 */
public class DonorDimension extends ARDimension {

    /** TODO description here
     * 
     */
    public DonorDimension() {
        super();
    }

    /** @see org.dgfoundation.amp.ar.dimension.ARDimension#initialize()
     */
    @Override
    public void initialize() throws HibernateException, SQLException {
        Session session = PersistenceManager.getSession();
        Query createQuery = session.createQuery("select org from "+AmpOrganisation.class.getName()+" org where (org.deleted is null or org.deleted = false) ");
        HashMap<Long,Long> typeMap=new HashMap<Long, Long>();
        links.put(AmpOrgType.class,typeMap);

        HashMap<Long,Long> groupMap=new HashMap<Long, Long>();
        links.put(AmpOrgGroup.class,groupMap);

        List<AmpOrganisation> list;
        list = createQuery.list();
        Iterator<AmpOrganisation> it=list.iterator();
        int i = 0;
        while (it.hasNext()) {
            i++;
            AmpOrganisation as= (AmpOrganisation) it.next();
            if(as.getAmpOrgId()==null) continue;
            AmpOrgGroup orgGrp = as.getOrgGrpId();
            if (orgGrp == null) continue;
            typeMap.put(as.getAmpOrgId(), orgGrp.getOrgType()==null?null:orgGrp.getOrgType().getAmpOrgTypeId());
            groupMap.put(as.getAmpOrgId(), as.getOrgGrpId()==null?null:as.getOrgGrpId().getAmpOrgGrpId());         
        }
    }
    @Override
    public Long getParentObject(Long parentId,  Class relatedContentPersisterClass) {
        // TODO Auto-generated method stub
        return null;
}
}
