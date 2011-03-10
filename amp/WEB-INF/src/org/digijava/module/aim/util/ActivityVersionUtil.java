package org.digijava.module.aim.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityContact;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.IPAContract;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.FormatHelper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.emory.mathcs.backport.java.util.Collections;

public class ActivityVersionUtil {

	private static Logger logger = Logger.getLogger(ActivityVersionUtil.class);

	public static final String GLOBAL_SETTINGS_VERSION_QUEUE_SIZE = "Activity Versions Queue Size";

	public static Method getMethodFromFieldName(String fieldName, Class auxClass, String prefix) throws Exception {
		String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		methodName = prefix + methodName;
		if (prefix.equals("set")) {
			for (int i = 0; i < auxClass.getDeclaredMethods().length; i++) {
				if (auxClass.getDeclaredMethods()[i].toString().contains(methodName)) {
					return auxClass.getDeclaredMethods()[i];
				}
			}
		}
		return auxClass.getMethod(methodName, null);
	}

	public static boolean implementsVersionable(Class[] array) {
		boolean ret = false;
		for (int i = 0; i < array.length; i++) {
			if (array[i].getName().equals("org.digijava.module.aim.dbentity.Versionable")) {
				ret = true;
			}
		}
		return ret;
	}

	public static String generateFormattedOutput(HttpServletRequest request, Output out) throws WorkerException {
		Site site = RequestUtils.getSite(request);
		String langCode = RequestUtils.getNavigationLanguage(request).getCode();

		String ret = "";
		if (out.getOutputs() != null) {
			// First level.
			Iterator<Output> iter = out.getOutputs().iterator();
			while (iter.hasNext()) {
				Output auxOutput = iter.next();
				for (int i = 0; i < auxOutput.getTitle().length; i++) {
					ret = ret + "<b>" + TranslatorWorker.translateText(auxOutput.getTitle()[i], langCode, site.getId())
							+ "</b>";
				}
				for (int i = 0; i < auxOutput.getValue().length; i++) {
					/*
					 * if (auxOutput.getValue()[i] instanceof Date) { String
					 * date = DateConversion.ConvertDateToString((Date)
					 * auxOutput.getValue()[i]); ret += date; } else {
					 */
					ret = ret + auxOutput.getValue()[i];
					// }
				}
				if (auxOutput.getOutputs() != null) {
					// Second level.
					String tabs = "<br/> &nbsp; &nbsp; &nbsp;";
					Iterator<Output> iter2 = auxOutput.getOutputs().iterator();
					while (iter2.hasNext()) {
						Output auxOutput2 = iter2.next();
						ret += tabs;
						for (int i = 0; i < auxOutput2.getTitle().length; i++) {
							ret = ret + "<b>"
									+ TranslatorWorker.translateText(auxOutput2.getTitle()[i], langCode, site.getId())
									+ "</b>";
						}
						for (int i = 0; i < auxOutput2.getValue().length; i++) {
							if (auxOutput2.getValue()[i] instanceof Timestamp) {
								String date = DateConversion.ConvertDateToString(new Date(((Timestamp) auxOutput2
										.getValue()[i]).getTime()));
								ret += date;
							} else if (auxOutput2.getValue()[i] instanceof BigDecimal) {
								NumberFormat formatter = FormatHelper.getDecimalFormat();
								formatter.setMaximumFractionDigits(0);
								ret += formatter.format(auxOutput2.getValue()[i]);
							} else {
								ret = ret + auxOutput2.getValue()[i];
							}
						}
					}
				}
			}
		} else {
			for (int i = 0; i < out.getTitle().length; i++) {
				ret = ret + out.getTitle()[i];
			}
			for (int i = 0; i < out.getValue().length; i++) {
				ret = ret + out.getValue()[i];
			}
		}
		return ret;
	}

	public static void deleteOldActivityVersions() throws Exception {

		Comparator VersionActivityComparator = new Comparator() {
			public int compare(Object a, Object b) {
				AmpActivity auxA = (AmpActivity) a;
				AmpActivity auxB = (AmpActivity) b;

				// Dec.
				return auxA.getAmpActivityId().compareTo(auxB.getAmpActivityId()) * -1;
			}
		};

		// Get from GS the max number of versions plus one.
		int numberOfVersions = numberOfVersions() + 1;

		// Get the list of groups and iterate through their activities sorted by
		// ID descending.
		Session session = PersistenceManager.getSession();
		Collection<AmpActivityGroup> groups = session.createQuery(
				"SELECT grp FROM " + AmpActivityGroup.class.getName() + " grp").list();
		Iterator<AmpActivityGroup> iterGroups = groups.iterator();
		while (iterGroups.hasNext()) {
			AmpActivityGroup auxGroup = iterGroups.next();
			Set<AmpActivityVersion> activitiesFromGroup = auxGroup.getActivities();
			List<AmpActivityVersion> sortedActivitiesFromGroup = new ArrayList(activitiesFromGroup);
			Collections.sort(sortedActivitiesFromGroup, VersionActivityComparator);
			for (int i = 0; i < sortedActivitiesFromGroup.size(); i++) {
				if (i < numberOfVersions) {
					logger.warn("Version not deleted: " + sortedActivitiesFromGroup.get(i).getAmpActivityId());
				} else {
					ActivityUtil.deleteActivity(sortedActivitiesFromGroup.get(i).getAmpActivityId());
					logger.warn("Version deleted: " + sortedActivitiesFromGroup.get(i).getAmpActivityId());
				}
			}
		}
	}

	public static int numberOfVersions() throws Exception {
		int ret = 0;
		int aux = 5; // Default value after apply patch if no redeployed.
		String gsValue = FeaturesUtil.getGlobalSettingValue(ActivityVersionUtil.GLOBAL_SETTINGS_VERSION_QUEUE_SIZE);
		if (gsValue != null) {
			aux = Integer.valueOf(gsValue).intValue();
		}
		if (aux >= 0) {
			ret = aux;
		}
		return ret;
	}

	public static AmpActivity getLastActivityFromGroup(Long groupId) throws Exception {
		AmpActivity auxActivity = null;
		Session session = PersistenceManager.getSession();
		auxActivity = ((AmpActivityGroup) session.load(AmpActivityGroup.class, groupId)).getAmpActivityLastVersion();
		return auxActivity;
	}

	public static void updateActivityView() {
		logger.info("Updating amp_activity view.");
		try {
			Session session = PersistenceManager.getSession();
			String query = "CREATE OR REPLACE VIEW `amp_activity` AS  "
					+ "select  amp_activity_version.*  from    "
					+ "(`amp_activity_version` join `amp_activity_group` on `amp_activity_version`.`amp_activity_group_id` = `amp_activity_group`.`amp_activity_group_id`)  "
					+ "where (`amp_activity_version`.`amp_activity_id` = `amp_activity_group`.`amp_activity_last_version_id`)";
			session.createSQLQuery(query).executeUpdate();
		} catch (Exception e) {
			logger.error("Error updating amp_activity view.", e);
			e.printStackTrace(System.out);
		}
		logger.info("Updated amp_activity view.");
	}

	/**
	 * Create a copy of the {@link AmpActivity} with all Collections linked with
	 * it and ready to save.
	 * 
	 * @param in
	 * @param member
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public static AmpActivity cloneActivity(AmpActivity in, AmpTeamMember member) throws CloneNotSupportedException {
		AmpActivity out = (AmpActivityVersion) in.clone();

		if (out.getActivityContacts() != null && out.getActivityContacts().size() > 0) {
			Set<AmpActivityContact> setAmpCont = new HashSet<AmpActivityContact>();
			Iterator<AmpActivityContact> iActCont = out.getActivityContacts().iterator();
			while (iActCont.hasNext()) {
				AmpActivityContact auxAmpCont = iActCont.next();
				AmpActivityContact newAmpCont = (AmpActivityContact) auxAmpCont.clone();
				newAmpCont.prepareMerge(out);
				setAmpCont.add(newAmpCont);
			}
			out.setActivityContacts(setAmpCont);
		} else {
			// Delete this apparently redundant line of code and u will get
			// "org.hibernate.HibernateException: Found shared references to a collection"
			// when the PersistentSet is initialized but with 0 elements :(
			out.setActivityContacts(null);
		}

		out.setActivityCreator(member);
		out.setActivityDocuments(null);
		out.setActivityPrograms(null);
		out.setActPrograms(null);
		out.setActRankColl(null);
		out.setAmpActivityGroup(null);
		out.setAmpActivityPreviousVersion(null);
		// out.setApprovedBy(null);
		out.setAuthor(null);
		out.setCategories(null);
		out.setChapter(null);
		out.setClosingDates(null);
		out.setComponentes(null);
		out.setComponentFundings(null);
		out.setComponentProgress(null);
		out.setComponents(null);
		out.setContracts(null);
		out.setCosts(null);
		// out.setCreatedBy(null);
		out.setDocuments(null);

		// Fundings.
		if (out.getFunding() != null && out.getFunding().size() > 0) {
			Set<AmpFunding> setAmpFunding = new HashSet<AmpFunding>();
			Iterator<AmpFunding> iActFunding = out.getFunding().iterator();
			while (iActFunding.hasNext()) {
				AmpFunding auxAmpFunding = iActFunding.next();
				AmpFunding newAmpFunding = (AmpFunding) auxAmpFunding.clone();
				newAmpFunding.prepareMerge(out);
				setAmpFunding.add(newAmpFunding);
			}
			out.setFunding(setAmpFunding);
		} else {
			out.setFunding(null);
		}

		out.setIndicators(null);
		out.setInternalIds(null);
		out.setIssues(null);
		out.setLocations(null);
		out.setMember(null);
		out.setModality(null);
		out.setModifiedBy(member);
		out.setNotes(null);
		out.setOrgrole(null);
		out.setProgress(null);
		out.setReferenceDocs(null);
		out.setRegionalFundings(null);
		out.setRegionalObservations(null);

		// Sectors.
		if (out.getSectors() != null && out.getSectors().size() > 0) {
			Set<AmpActivitySector> setAmpSect = new HashSet<AmpActivitySector>();
			Iterator<AmpActivitySector> iActSect = out.getSectors().iterator();
			while (iActSect.hasNext()) {
				AmpActivitySector auxAmpSect = iActSect.next();
				AmpActivitySector newAmpSect = (AmpActivitySector) auxAmpSect.clone();
				newAmpSect.prepareMerge(out);
				setAmpSect.add(newAmpSect);
			}
			out.setSectors(setAmpSect);
		} else {
			out.setSectors(null);
		}

		out.setSurvey(null);
		out.setTeam(member.getAmpTeam());
		out.setThemeId(null);

		return out;
	}

	public static IPAContract cloneIPAContract() {
		IPAContract out = new IPAContract();

		return out;
	}

	/**
	 * Initialize all collections (up to level 2) for an {@link AmpActivity}
	 * object. Use it to prevent exceptions with lazy collections.
	 * 
	 * @param act
	 * @return
	 * @throws DgException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static AmpActivity initializeActivity(AmpActivity act) throws DgException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		Session session = PersistenceManager.getRequestDBSession();
		Method[] methods = AmpActivity.class.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().contains("get") && methods[i].getReturnType().getName().contains("java.util.Set")) {
				Object methodValue = methods[i].invoke(act, null);
				Collection auxColl = (Collection) methodValue;
				if (auxColl != null) {
					auxColl.size();
					Iterator iInner = auxColl.iterator();
					while (iInner.hasNext()) {
						Object auxInnerObject = iInner.next();
						Method[] innerMethods = auxInnerObject.getClass().getDeclaredMethods();
						for (int j = 0; j < innerMethods.length; j++) {
							if (innerMethods[j].getName().contains("get")
									&& innerMethods[j].getReturnType().getName().contains("java.util.Set")) {
								Object innerMethodValue = innerMethods[j].invoke(auxInnerObject, null);
								Collection auxInnerColl = (Collection) innerMethodValue;
								if (auxInnerColl != null) {
									auxInnerColl.size();
								}
							}
						}
					}
				}
			}
		}
		return act;
	}
}