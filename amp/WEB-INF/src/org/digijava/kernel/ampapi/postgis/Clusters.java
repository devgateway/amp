package org.digijava.kernel.ampapi.postgis;

import java.util.List;

import org.digijava.kernel.ampapi.postgis.entity.Amp_Activity_Points;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Criteria;
import org.hibernate.Session;

public class Clusters {
	
	public List getClustersByAdmin(String adm){
	    Session session = PersistenceManager.getCurrentSession();
	    Criteria testCriteria = session.createCriteria(Amp_Activity_Points.class);
	    List results = testCriteria.list();
	    return results;
	}
}
