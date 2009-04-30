/**
 * DonorDimension.java
 * (c) 2007 Development Gateway Foundation
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
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;

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
		Query createQuery = session.createQuery("from "+AmpOrganisation.class.getName());
//		HashMap<Long,Long> typeMap=new HashMap<Long, Long>();
//		links.put(AmpOrgType.class,typeMap);

		HashMap<Long,Long> groupMap=new HashMap<Long, Long>();
		links.put(AmpOrgGroup.class,groupMap);

		List<AmpOrganisation> list;
		list = createQuery.list();
		Iterator<AmpOrganisation> it=list.iterator();
		while (it.hasNext()) {
			AmpOrganisation as= (AmpOrganisation) it.next();
			//typeMap.put(as.getAmpOrgId(), as.getOrgTypeId()==null?null:as.getOrgTypeId().getAmpOrgTypeId());
			groupMap.put(as.getAmpOrgId(), as.getOrgGrpId()==null?null:as.getOrgGrpId().getAmpOrgGrpId());	       
		}

		PersistenceManager.releaseSession(session);

	}

}
