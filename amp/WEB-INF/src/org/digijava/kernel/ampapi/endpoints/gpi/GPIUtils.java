package org.digijava.kernel.ampapi.endpoints.gpi;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpGPINiAidOnBudget;
import org.hibernate.Session;
import org.hibernate.Query;
import org.digijava.module.aim.dbentity.AmpOrganisation;

public class GPIUtils {
	private static Logger logger = Logger.getLogger(GPIUtils.class);

	public static AmpGPINiAidOnBudget getAidOnBudgetById(Long id) {
		return (AmpGPINiAidOnBudget) PersistenceManager.getSession().get(AmpGPINiAidOnBudget.class, id);
	}

	public static List<AmpGPINiAidOnBudget> getAidOnBudgetList(Integer offset, Integer count, String orderBy,
			String sort, Integer total) {
		Integer maxResults = count == null ? GPIEPConstants.DEFAULT_RECORDS_PER_PAGE : count;
		Integer startAt = (offset == null || offset > total) ? GPIEPConstants.DEFAULT_OFFSET : offset;
		String orderByColumn = (orderBy == null || !isValidSortColumn(orderBy)) ? GPIEPConstants.DEFAULT_SORT_COLUMN
				: orderBy;
		String sortOrder = (sort == null || !isValidSortOrder(sort)) ? GPIEPConstants.ORDER_DESC : sort;

		Session dbSession = PersistenceManager.getSession();
		String queryString = "select gpi from " + AmpGPINiAidOnBudget.class.getName() + " gpi order by "
				+ GPIEPConstants.SORT_FIELDS.get(orderByColumn) + " " + sortOrder;
		Query query = dbSession.createQuery(queryString);
		query.setFirstResult(startAt);
		query.setMaxResults(maxResults);
		return query.list();

	}

	public static boolean isValidSortColumn(String columnName) {
		return GPIEPConstants.SORT_FIELDS.containsKey(columnName);
	}

	public static boolean isValidSortOrder(String sort) {
		return GPIEPConstants.ORDER_ASC.equals(sort) || GPIEPConstants.ORDER_DESC.equals(sort);
	}

	public static Integer getCount() {
		Session dbSession = PersistenceManager.getSession();
		String queryString = "select count(*) from " + AmpGPINiAidOnBudget.class.getName();
		Query query = dbSession.createQuery(queryString);
		return (Integer) query.uniqueResult();
	}

	public static AmpOrganisation getOrganisation(Long id) {
		return (AmpOrganisation) PersistenceManager.getSession().get(AmpOrganisation.class, id);
	}

	public static void saveAidOnBudget(AmpGPINiAidOnBudget aidOnBudget) {
		Session session = null;
		try {
			session = PersistenceManager.getSession();
			session.saveOrUpdate(aidOnBudget);
		} catch (Exception e) {
			logger.error("Exception from saveAidOnBudget: " + e.getMessage());
		}
	}

	public static void delete(Long id) {
		AmpGPINiAidOnBudget aidOnBudget = GPIUtils.getAidOnBudgetById(id);
		Session session = null;
		try {
			session = PersistenceManager.getSession();
			session.delete(aidOnBudget);
		} catch (Exception e) {
			logger.error("Exception from saveAidOnBudget: " + e.getMessage());
		}
	}

	public static boolean similarRecordExists(Long ampGPINiAidOnBudgetId, Long donorId, Date indicatorDate) {
		Session dbSession = PersistenceManager.getSession();
		String queryString = "select gpi from " + AmpGPINiAidOnBudget.class.getName()
				+ " gpi where gpi.donor.ampOrgId=:orgId and gpi.indicatorDate = :indicatorDate";
		if (ampGPINiAidOnBudgetId != null) {
			queryString += " and gpi.ampGPINiAidOnBudgetId != :ampGPINiAidOnBudgetId ";
		}

		Query query = dbSession.createQuery(queryString);
		query.setParameter("orgId", donorId);
		query.setParameter("indicatorDate", indicatorDate);
		if (ampGPINiAidOnBudgetId != null) {
			query.setParameter("ampGPINiAidOnBudgetId", ampGPINiAidOnBudgetId);
		}

		return query.list().size() > 0;
	}
}
