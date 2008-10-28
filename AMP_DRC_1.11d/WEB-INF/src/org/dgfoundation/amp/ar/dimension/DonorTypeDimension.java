/**
 * 
 */
package org.dgfoundation.amp.ar.dimension;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;

/**
 * @author mihai
 *
 */
public class DonorTypeDimension extends ARDimension {

	/**
	 * 
	 */
	public DonorTypeDimension() {
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
			if(as.getOrgTypeId()==null) continue;
			typeMap.put(as.getOrgTypeId().getAmpOrgTypeId(), as.getOrgGrpId()==null?null:as.getOrgGrpId().getAmpOrgGrpId());	       
		}
		PersistenceManager.releaseSession(session);

	}
}

