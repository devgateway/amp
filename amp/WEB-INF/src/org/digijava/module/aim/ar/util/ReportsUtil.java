package org.digijava.module.aim.ar.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.hibernate.Query;
import org.hibernate.Session;

public class ReportsUtil {
	private static Logger logger	= Logger.getLogger(ReportsUtil.class);
	
	@SuppressWarnings("unchecked")
	public static Collection<AmpOrganisation> getAllOrgByRole(String roleCode ){
        Session session = null;
        Collection<AmpOrganisation> col 	= null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select distinct aor.organisation from " + AmpOrgRole.class.getName() + " as aor  "
                + " inner join aor.role as rol where rol.roleCode=:roleCode order by aor.organisation.name";
            Query qry = session.createQuery(queryString);
            qry.setString("roleCode", roleCode);
            col = qry.list();
        } catch (Exception e) {
            logger.debug("Exception from getAllOrgByRole()");
            e.printStackTrace();
        }
        return col;

    }
	
	public static Set processSelectedFilters(Object [] src, Class colObjClass) {
		try{
			HashSet set	= null;
			if (src != null && src.length > 0) {
				set		= new HashSet();
				for (int i=0; i<src.length; i++) {
					Long id 		= Long.parseLong("" + src[i]);
					Object ampObj	= Util.getSelectedObject(colObjClass, id);
					if (ampObj != null)
						set.add(ampObj);
				}
			}
			return set;
		}
		catch(Exception E) {
			E.printStackTrace();
			return null;
		}
	}
}
