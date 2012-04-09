package org.digijava.module.aim.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
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

import org.apache.log4j.Logger;
import org.apache.wicket.util.string.Strings;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.Versionable;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.hibernate.Session;

import edu.emory.mathcs.backport.java.util.Collections;

public class ActivityVersionUtil {

	private static Logger logger = Logger.getLogger(ActivityVersionUtil.class);


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
					if (auxOutput.getValue()[i]!=null){
						ret = ret + DbUtil.filter(auxOutput.getValue()[i].toString());
					}
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
				AmpActivityVersion auxA = (AmpActivityVersion) a;
				AmpActivityVersion auxB = (AmpActivityVersion) b;

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

	public static int numberOfVersions() {
		int aux = 5; // Default value after apply patch if no redeployed.
		String gsValue = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.VERSION_QUEUE_SIZE);
		if (gsValue != null) {
			try {
				aux = Integer.valueOf(gsValue).intValue();
			} catch (NumberFormatException e) {
				logger.error(e);
			}
		}
		return aux;
	}
	
	public static boolean isVersioningEnabled(){
		return (numberOfVersions() > 0);
	}

	public static AmpActivityVersion getLastActivityFromGroup(Long groupId) throws Exception {
		AmpActivityVersion auxActivity = null;
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
	 * Create a copy of the {@link AmpActivityVersion} with all Collections linked with
	 * it and ready to save.
	 * 
	 * @param in
	 * @param member
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public static AmpActivityVersion cloneActivity(AmpActivityVersion in, AmpTeamMember member) throws CloneNotSupportedException {
		AmpActivityVersion out = (AmpActivityVersion) in.clone();
		
		
		Class clazz = AmpActivityFields.class;
		
		Field[] fields = clazz.getDeclaredFields();//clazz.getFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if (Collection.class.isAssignableFrom(field.getType())){
				logger.debug("Init set: " + field.getName());
				initSet(out, field);
			}
		}
		
		
		//out.setActivityCreator(member);
		out.setAmpActivityGroup(null);
		out.setAuthor(null);
		//out.setTeam(member.getAmpTeam());
		out.setThemeId(null);
		out.setModality(null);
		out.setModifiedBy(member);
		// out.setCreatedBy(null);
		out.setChapter(null);

		//out.setActivityPrograms(null);
		//out.setActPrograms(null);
		//out.setActRankColl(null);
		// out.setApprovedBy(null);
		//out.setReferenceDocs(null);
		//out.setProgress(null);
		//out.setMember(null);
		//out.setNotes(null);
		//out.setIndicators(null);
		//out.setInternalIds(null);
		//out.setCosts(null);
		//out.setDocuments(null);
		//out.setComponentes(null);


		
		/*
		// Contacts.
		if (out.getActivityContacts() != null && out.getActivityContacts().size() > 0) {
			Set<AmpActivityContact> setAmpCont = new HashSet<AmpActivityContact>();
			Iterator<AmpActivityContact> iActCont = out.getActivityContacts().iterator();
			while (iActCont.hasNext()) {
				AmpActivityContact auxAmpCont = iActCont.next();
				//AmpActivityContact newAmpCont = (AmpActivityContact) auxAmpCont.clone();
				auxAmpCont = (AmpActivityContact) auxAmpCont.prepareMerge(out);
				setAmpCont.add(auxAmpCont);
			}
			out.setActivityContacts(setAmpCont);
		} else {
			// Delete this apparently redundant line of code and u will get
			// "org.hibernate.HibernateException: Found shared references to a collection"
			// when the PersistentSet is initialized but with 0 elements :(
			out.setActivityContacts(null);
		}

		// Activity Documents.
		if (out.getActivityDocuments() != null && out.getActivityDocuments().size() > 0) {
			Set<AmpActivityDocument> set = new HashSet<AmpActivityDocument>();
			Iterator<AmpActivityDocument> i = out.getActivityDocuments().iterator();
			while (i.hasNext()) {
				AmpActivityDocument aux = (AmpActivityDocument) i.next();
				aux = (AmpActivityDocument) aux.prepareMerge(out);
				set.add(aux);
			}
			out.setActivityDocuments(set);
		} else {
			out.setActivityDocuments(null);
		}

		initSet(out, "categories");
		/*
		if (out.getCategories() != null && out.getCategories().size() > 0) {
			HashSet<AmpCategoryValue> set = new HashSet<AmpCategoryValue>();
			Iterator<AmpCategoryValue> i = out.getCategories().iterator();
			while (i.hasNext()) {
				AmpCategoryValue aux = (AmpCategoryValue) i.next();
				aux = (AmpCategoryValue) aux.prepareMerge(out);
				set.add(aux);
			}
			out.setCategories(set);
		} else {
			out.setCategories(null);
		}
		
		

		// Closing Dates.
		if (out.getClosingDates() != null && out.getClosingDates().size() > 0) {
			Set<AmpActivityClosingDates> set = new HashSet<AmpActivityClosingDates>();
			Iterator<AmpActivityClosingDates> i = out.getClosingDates().iterator();
			while (i.hasNext()) {
				AmpActivityClosingDates aux = (AmpActivityClosingDates) i.next();
				aux = (AmpActivityClosingDates) aux.prepareMerge(out);
				set.add(aux);
			}
			out.setClosingDates(set);
		} else {
			out.setClosingDates(null);
		}


		// Component Fundings.
		if (out.getComponentFundings() != null && out.getComponentFundings().size() > 0) {
			Set<AmpComponentFunding> set = new HashSet<AmpComponentFunding>();
			Iterator<AmpComponentFunding> i = out.getComponentFundings().iterator();
			while (i.hasNext()) {
				AmpComponentFunding aux = (AmpComponentFunding) i.next();
				aux = (AmpComponentFunding) aux.prepareMerge(out);
				set.add(aux);
			}
			out.setComponentFundings(set);
		} else {
			out.setComponentFundings(null);
		}

		// Physical Progress.
		if (out.getComponentProgress() != null && out.getComponentProgress().size() > 0) {
			Set<AmpPhysicalPerformance> set = new HashSet<AmpPhysicalPerformance>();
			Iterator<AmpPhysicalPerformance> i = out.getComponentProgress().iterator();
			while (i.hasNext()) {
				AmpPhysicalPerformance aux = (AmpPhysicalPerformance) i.next();
				aux = (AmpPhysicalPerformance) aux.prepareMerge(out);
				set.add(aux);
			}
			out.setComponentProgress(set);
		} else {
			out.setComponentProgress(null);
		}

		// Components.
		if (out.getComponents() != null && out.getComponents().size() > 0) {
			Set<AmpComponent> set = new HashSet<AmpComponent>();
			Iterator<AmpComponent> i = out.getComponents().iterator();
			while (i.hasNext()) {
				AmpComponent aux = (AmpComponent) i.next();
				aux = (AmpComponent) aux.prepareMerge(out);
				set.add(aux);
			}
			out.setComponents(set);
		} else {
			out.setComponents(null);
		}

		// IPA.
		if (out.getContracts() != null && out.getContracts().size() > 0) {
			Set<IPAContract> set = new HashSet<IPAContract>();
			Iterator<IPAContract> i = out.getContracts().iterator();
			while (i.hasNext()) {
				IPAContract aux = (IPAContract) i.next();
				aux = (IPAContract) aux.prepareMerge(out);
				set.add(aux);
			}
			out.setContracts(set);
		} else {
			out.setContracts(null);
		}
		

		// Fundings.
		if (out.getFunding() != null && out.getFunding().size() > 0) {
			Set<AmpFunding> setAmpFunding = new HashSet<AmpFunding>();
			Iterator<AmpFunding> iActFunding = out.getFunding().iterator();
			while (iActFunding.hasNext()) {
				AmpFunding auxAmpFunding = iActFunding.next();
				auxAmpFunding = (AmpFunding) auxAmpFunding.prepareMerge(out);
				setAmpFunding.add(auxAmpFunding);
			}
			out.setFunding(setAmpFunding);
		} else {
			out.setFunding(null);
		}


		// Issues - Measures - Actors.
		if (out.getIssues() != null && out.getIssues().size() > 0) {
			Set<AmpIssues> set = new HashSet<AmpIssues>();
			Iterator<AmpIssues> i = out.getIssues().iterator();
			while (i.hasNext()) {
				AmpIssues aux = (AmpIssues) i.next();
				aux = (AmpIssues) aux.prepareMerge(out);
				set.add(aux);
			}
			out.setIssues(set);
		} else {
			out.setIssues(null);
		}

		// Locations.
		if (out.getLocations() != null && out.getLocations().size() > 0) {
			Set<AmpActivityLocation> set = new HashSet<AmpActivityLocation>();
			Iterator<AmpActivityLocation> i = out.getLocations().iterator();
			while (i.hasNext()) {
				AmpActivityLocation aux = (AmpActivityLocation) i.next();
				aux = (AmpActivityLocation) aux.prepareMerge(out);
				set.add(aux);
			}
			out.setLocations(set);
		} else {
			out.setLocations(null);
		}


		// Org.Roles.
		if (out.getOrgrole() != null && out.getOrgrole().size() > 0) {
			Set<AmpOrgRole> set = new HashSet<AmpOrgRole>();
			Iterator<AmpOrgRole> i = out.getOrgrole().iterator();
			while (i.hasNext()) {
				AmpOrgRole aux = (AmpOrgRole) i.next();
				aux = (AmpOrgRole) aux.prepareMerge(out);
				set.add(aux);
			}
			out.setOrgrole(set);
		} else {
			out.setOrgrole(null);
		}


		// Regional Funding.
		if (out.getRegionalFundings() != null && out.getRegionalFundings().size() > 0) {
			Set<AmpRegionalFunding> set = new HashSet<AmpRegionalFunding>();
			Iterator<AmpRegionalFunding> i = out.getRegionalFundings().iterator();
			while (i.hasNext()) {
				AmpRegionalFunding aux = (AmpRegionalFunding) i.next();
				aux = (AmpRegionalFunding) aux.prepareMerge(out);
				set.add(aux);
			}
			out.setRegionalFundings(set);
		} else {
			out.setRegionalFundings(null);
		}

		// Regional Observations.
		if (out.getRegionalObservations() != null && out.getRegionalObservations().size() > 0) {
			Set<AmpRegionalObservation> set = new HashSet<AmpRegionalObservation>();
			Iterator<AmpRegionalObservation> i = out.getRegionalObservations().iterator();
			while (i.hasNext()) {
				AmpRegionalObservation aux = (AmpRegionalObservation) i.next();
				aux = (AmpRegionalObservation) aux.prepareMerge(out);
				set.add(aux);
			}
			out.setRegionalObservations(set);
		} else {
			out.setRegionalObservations(null);
		}

		// Sectors.
		if (out.getSectors() != null && out.getSectors().size() > 0) {
			Set<AmpActivitySector> setAmpSect = new HashSet<AmpActivitySector>();
			Iterator<AmpActivitySector> iActSect = out.getSectors().iterator();
			while (iActSect.hasNext()) {
				AmpActivitySector auxAmpSect = iActSect.next();
				auxAmpSect = (AmpActivitySector) auxAmpSect.prepareMerge(out);
				setAmpSect.add(auxAmpSect);
			}
			out.setSectors(setAmpSect);
		} else {
			out.setSectors(null);
		}

		// PI Survey.
		if (out.getSurvey() != null && out.getSurvey().size() > 0) {
			Set<AmpAhsurvey> set = new HashSet<AmpAhsurvey>();
			Iterator<AmpAhsurvey> i = out.getSurvey().iterator();
			while (i.hasNext()) {
				AmpAhsurvey aux = (AmpAhsurvey) i.next();
				aux = (AmpAhsurvey) aux.prepareMerge(out);
				set.add(aux);
			}
			out.setSurvey(set);
		} else {
			out.setSurvey(null);
		}

		// Structures Survey.
		if (out.getStructures() != null && out.getStructures().size() > 0) {
			Set<AmpStructure> set = new HashSet<AmpStructure>();
			Iterator<AmpStructure> i = out.getStructures().iterator();
			while (i.hasNext()) {
				AmpStructure aux = (AmpStructure) i.next();
				aux = (AmpStructure) aux.prepareMerge(out);
				set.add(aux);
			}
			out.setStructures(set);
		} else {
			out.setStructures(null);
		}

		 */

		return out;
	}
	
	private static void initSet(AmpActivityVersion out, Field field){
		String setName = Strings.capitalize(field.getName());
		Class clazz = out.getClass();
		try {
			Method method = clazz.getMethod("get" + setName);
			Set returnSet = null;
			Set set = (Set) method.invoke(out);
			if (set != null && set.size() > 0){
				Iterator i = set.iterator();
				returnSet = new HashSet();
				while (i.hasNext()) {
					Versionable object = (Versionable) i.next();
					object = (Versionable) object.prepareMerge(out);
					returnSet.add(object);
				}
			}
			
			if (Set.class.isAssignableFrom(field.getType()))
				method = clazz.getMethod("set" + setName, Set.class);
			else
				method = clazz.getMethod("set" + setName, Collection.class);
			method.invoke(out, returnSet);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Can't init set:", e);
		}
	}

	/**
	 * Initialize all collections (up to level 2) for an {@link AmpActivityVersion}
	 * object. Use it to prevent exceptions with lazy collections.
	 * 
	 * @param act
	 * @return
	 * @throws DgException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static AmpActivityVersion initializeActivity(AmpActivityVersion act) throws DgException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		Session session = PersistenceManager.getRequestDBSession();
		Method[] methods = AmpActivityVersion.class.getDeclaredMethods();
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