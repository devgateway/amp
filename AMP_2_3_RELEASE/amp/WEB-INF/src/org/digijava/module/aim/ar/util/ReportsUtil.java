package org.digijava.module.aim.ar.util;

import java.util.*;

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
	
	@SuppressWarnings("unchecked")
	public static Collection<AmpOrganisation> getAllOrgByRoleOfPortfolio(String roleCode) {
        Session session = null;
        List<AmpOrganisation> col = null;
        try {
            session = PersistenceManager.getRequestDBSession();

            String queryString = "select distinct ao.* from amp_organisation ao " +
						"inner join amp_org_role aor on (aor.organisation = ao.amp_org_id) " +
						"inner join amp_role ar on ((ar.amp_role_id = aor.role) and (ar.role_code=:roleCode)) where (ao.deleted is null or ao.deleted = false) ";
            Query qry = session.createSQLQuery(queryString).addEntity(AmpOrganisation.class);
            qry.setCacheable(true);
            col = qry.setString("roleCode", roleCode).list();

            Collections.sort(col, new Comparator<AmpOrganisation>() {
                public int compare(AmpOrganisation o1, AmpOrganisation o2) {
                    return o1.getName().trim().compareTo(o2.getName().trim());
                }
            });

        } catch (Exception e) {
            logger.debug("Exception from getAllOrgByRoleOfPortfolio()");
            logger.debug(e.toString());
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
