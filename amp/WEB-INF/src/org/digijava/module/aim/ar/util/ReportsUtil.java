package org.digijava.module.aim.ar.util;

import java.sql.Connection;
import java.util.*;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.exception.reports.ReportException;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.caching.AmpCaching;
import org.hibernate.Query;
import org.hibernate.Session;

public class ReportsUtil {
	private static Logger logger	= Logger.getLogger(ReportsUtil.class);
	
//	@SuppressWarnings("unchecked")
//	public static Collection<AmpOrganisation> getAllOrgByRole(String roleCode ){
//        Session session = null;
//        Collection<AmpOrganisation> col 	= null;
//
//        try {
//            session = PersistenceManager.getRequestDBSession();
//            String queryString = "select distinct aor.organisation from " + AmpOrgRole.class.getName() + " as aor  "
//                + " inner join aor.role as rol where rol.roleCode=:roleCode order by aor.organisation.name";
//            Query qry = session.createQuery(queryString);
//            qry.setString("roleCode", roleCode);
//            col = qry.list();
//        } catch (Exception e) {
//            logger.debug("Exception from getAllOrgByRole()");
//            e.printStackTrace();
//        }
//        return col;
//    }
	
	@SuppressWarnings("unchecked")
	public static Collection<AmpOrganisation> getAllOrgByRoleOfPortfolio(String roleCode) {
		if (AmpCaching.getInstance().allOrgByRoleOfPortfolio.containsKey(roleCode))
			return new ArrayList<AmpOrganisation>(AmpCaching.getInstance().allOrgByRoleOfPortfolio.get(roleCode));
		
        Session session = null;
        List<AmpOrganisation> col = null;
        try {
            session = PersistenceManager.getRequestDBSession();

            String rewrittenColumns = SQLUtils.rewriteQuery("amp_organisation", "ao", 
            		new HashMap<String, String>(){{
            			put("name", InternationalizedModelDescription.getForProperty(AmpOrganisation.class, "name").getSQLFunctionCall("ao.amp_org_id"));
            			put("description", InternationalizedModelDescription.getForProperty(AmpOrganisation.class, "description").getSQLFunctionCall("ao.amp_org_id"));
            		}});
            
            String orgIdsSource;
            boolean roleCodeNeededInQuery;
            if (roleCode.equals(Constants.ROLE_CODE_DONOR))
            {
            	orgIdsSource = "select DISTINCT(amp_donor_org_id) FROM amp_funding";
            	roleCodeNeededInQuery = false;
            }
            else
            {
            	roleCodeNeededInQuery = true;
            	orgIdsSource = "select DISTINCT(organisation) FROM amp_org_role WHERE role = (SELECT amp_role_id FROM amp_role WHERE role_code=:roleCode)";
            }
            
            String queryString = "select distinct " + rewrittenColumns + " from amp_organisation ao " +
            		"WHERE ao.amp_org_id IN (" + orgIdsSource + ") AND " +
            		"(ao.deleted is null or ao.deleted = false) ";

            Query qry = session.createSQLQuery(queryString).addEntity(AmpOrganisation.class);
            qry.setCacheable(true);
            
            if (roleCodeNeededInQuery)
            	qry = qry.setString("roleCode", roleCode);
            
            col = qry.list();

            Collections.sort(col, new Comparator<AmpOrganisation>() {
                public int compare(AmpOrganisation o1, AmpOrganisation o2) {
                    return o1.getName().trim().compareTo(o2.getName().trim());
                }
            });

        } catch (Exception e) {
            logger.debug("Exception from getAllOrgByRoleOfPortfolio()");
            logger.debug(e.toString());
        }

        AmpCaching.getInstance().allOrgByRoleOfPortfolio.put(roleCode, new ArrayList<AmpOrganisation>(col));
        return col;

    }
	
	public static Set<AmpOrganisation> processSelectedFilters(Object[] src)
	{
		return (Set<AmpOrganisation>) processSelectedFilters(src, AmpOrganisation.class);
	}
	
	/**
	 * returns a Set of objects of a given class whose ids are given in the input array
	 * @param src
	 * @param colObjClass
	 * @return
	 */
	public static Set processSelectedFilters(Object [] src, Class clazz) {
		try{
			if (src != null && src.length > 0) {
				HashSet set = new HashSet();
				for (int i=0; i<src.length; i++) {
					Long id 		= Long.parseLong(src[i].toString());
					Object ampObj	= Util.getSelectedObject(clazz, id);
					if (ampObj != null)
						set.add(ampObj);
				}
				return set;
			}
		}
		catch(Exception E) {
			E.printStackTrace();
		}
		return null;
	}	
	
	/**
	 * throws an exception containing the error message if the database does not respect some kind of minimum sanity checks. Returns normally if everything is fine
	 */
	public static void checkDatabaseSanity(Session session) throws ReportException
	{
		String errMsg = "";
		
		logger.debug("Database sanity check - in progress...");
		List<?> res = session.createSQLQuery("select DISTINCT(amp_report_id) from amp_report_column arc WHERE " + 
				"(SELECT count(*) from amp_report_column arc2 WHERE arc2.amp_report_id = arc.amp_report_id AND arc2.columnid = arc.columnid) > 1").list();
		if (!res.isEmpty())
			errMsg += "The following reports have a column repeated at least twice each: amp_report_id IN (" + Util.toCSString(res) + ")" + System.lineSeparator();
		
		res = session.createSQLQuery("select DISTINCT(columnname) from amp_columns col WHERE " + 
				"(SELECT count(*) FROM amp_columns col2 WHERE col.columnname = col2.columnname) > 1").list();
		if (!res.isEmpty())
			errMsg += "The following column(s) are defined at least twice in amp_columns: (" + Util.toCSString(res) + ")" + System.lineSeparator();

		res = session.createQuery("select m.measureName from AmpMeasures as m group by m.measureName, m.type having count(m) >1").list(); 
		if (!res.isEmpty())
			errMsg +="Duplicate measurenames are found in AMP_MEASURES tables: (" + Util.toCSString(res) + ")" + System.lineSeparator();

		if( !errMsg.isEmpty() ) {
			logger.error("Database sanity check - FAIL: " + errMsg);
			throw new Error(errMsg);
		}else {
			logger.debug("Database sanity check - PASS");
		}
	}
}
