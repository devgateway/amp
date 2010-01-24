/**
 * 
 */
package org.dgfoundation.amp.ar.dimension;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;

/**
 * @author mihai
 *
 */
public class DonorGroupDimension extends ARDimension {

	/**
	 * 
	 */
	public DonorGroupDimension() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.dimension.ARDimension#initialize()
	 */
	@Override
	public void initialize() throws HibernateException, SQLException {
		Session session = PersistenceManager.getSession();
		Query createQuery = session.createQuery("from "+AmpOrganisation.class.getName());
		HashMap<Long,Long> typeMap=new HashMap<Long, Long>();
		links.put(AmpOrgType.class,typeMap);


		List list;
		list = createQuery.list();
		Iterator it=list.iterator();
		while (it.hasNext()) {
			AmpOrganisation as= (AmpOrganisation) it.next();
			if(as.getOrgGrpId()==null) continue;
			typeMap.put(as.getOrgGrpId().getAmpOrgGrpId(), as.getOrgGrpId().getOrgType()==null?null:as.getOrgGrpId().getOrgType().getAmpOrgTypeId());
		}
		PersistenceManager.releaseSession(session);

	}
	@Override
	public Long getParentObject(Long parentId,
			Class relatedContentPersisterClass) {
		// TODO Auto-generated method stub
		return null;
	}
}

