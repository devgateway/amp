/**
 * 
 */
package org.dgfoundation.amp.ar.dimension;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

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
		       typeMap.put(as.getOrgGrpId().getAmpOrgGrpId(), as.getOrgTypeId()==null?null:as.getOrgTypeId().getAmpOrgTypeId());	       
		}
		
	    }
}

