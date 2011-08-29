package org.digijava.module.aim.action;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.annotations.activityversioning.CompareOutput;
import org.digijava.module.aim.annotations.activityversioning.VersionableCollection;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldTextEditor;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldSimple;
import org.digijava.module.aim.dbentity.AmpActivityFields;
import org.digijava.module.aim.dbentity.AmpActivityGroup;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.Versionable;
import org.digijava.module.aim.form.CompareActivityVersionsForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.editor.util.DbUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CompareActivityVersions extends DispatchAction {

	private ServletContext ampContext = null;

	private static Logger logger = Logger.getLogger(EditActivity.class);

	public ActionForward compare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		CompareActivityVersionsForm vForm = (CompareActivityVersionsForm) form;
		Session session = PersistenceManager.getRequestDBSession();

		if (request.getParameter("action") != null && request.getParameter("action").equals("setVersion")
				&& request.getParameter("activityCurrentVersion") != null) {
//beginTransaction();

			Long activityId = Long.parseLong(request.getParameter("activityCurrentVersion"));
			AmpActivityVersion activity = (AmpActivityVersion) session.load(AmpActivityVersion.class, activityId);
			AmpActivityGroup group = activity.getAmpActivityGroup();
			//tx.begin();

			// Update the modified date of the selected activity to send it last
			// to the list
			activity.setModifiedDate(Calendar.getInstance().getTime());
			group.setAmpActivityLastVersion(activity);
			session.save(group);
			//tx.commit();

			return new ActionForward(mapping.findForward("reload").getPath() + "&ampActivityId=" + activityId);
		}

		vForm.setOutputCollection(new ArrayList<CompareOutput>());
		// Load the activities.
		vForm.setActivityOne((AmpActivityVersion) session.load(AmpActivityVersion.class, vForm.getActivityOneId()));
		Hibernate.initialize(vForm.getActivityOne());
		ActivityVersionUtil.initializeActivity(vForm.getActivityOne());
		vForm.setActivityTwo((AmpActivityVersion) session.load(AmpActivityVersion.class, vForm.getActivityTwoId()));
		Hibernate.initialize(vForm.getActivityTwo());
		ActivityVersionUtil.initializeActivity(vForm.getActivityTwo());

		vForm.setOldActivity(vForm.getActivityOne());

		// Retrieve annotated for versioning fields.
		Field[] fields = AmpActivityFields.class.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			//logger.info(fields[i]);
			CompareOutput output = new CompareOutput();

			if (fields[i].isAnnotationPresent(VersionableFieldSimple.class)) {
				// Obtain "get" method from field.
				Method auxMethod = ActivityVersionUtil.getMethodFromFieldName(fields[i].getName(), AmpActivityVersion.class,
						"get");

				// Compare values from 2 versions.
				Object auxResult1 = auxMethod.invoke(vForm.getActivityOne(), null);
				Object auxResult2 = auxMethod.invoke(vForm.getActivityTwo(), null);

				// Obtain annotation object.
				VersionableFieldSimple auxAnnotation = (VersionableFieldSimple) fields[i]
						.getAnnotation(VersionableFieldSimple.class);

				// Sanitize String values.
				if (auxMethod.getReturnType().getName().equals("java.lang.String")) {
					if (auxResult1 != null && auxResult1.toString().trim().equals("")) {
						auxResult1 = null;
					}
					if (auxResult2 != null && auxResult2.toString().trim().equals("")) {
						auxResult2 = null;
					}
				}

				// Compare values, if both are null then they are considered
				// equal.
				if (!(auxResult1 == null && auxResult2 == null)) {
					if ((auxResult1 != null && auxResult2 == null)
							|| (auxResult1 == null && auxResult2 != null)
							|| (!auxMethod.invoke(vForm.getActivityOne(), null).equals(
									auxMethod.invoke(vForm.getActivityTwo(), null)))) {

						logger.warn(fields[i].getName() + ": " + auxResult1 + "-" + auxResult2);
						output.setDescriptionOutput(auxAnnotation.fieldTitle());
						output.setFieldOutput(fields[i]);

						// Identity "read-value" and "mandatory" fields.
						// TODO: If needed do the same for
						// @VersionableFieldTextEditor and
						// @VersionableCollection.
						output.setBlockSingleChangeOutput(auxAnnotation.blockSingleChange());
						output.setMandatoryForSingleChangeOutput(auxAnnotation.mandatoryForSingleChange());

						// Differentiate Wrappers from Classes that
						// implements
						// Versionable.
						Class auxReturnType = auxMethod.getReturnType();
						if (auxReturnType.getName().equals("java.util.Date")
								|| auxReturnType.getName().equals("java.sql.Date")
								|| auxReturnType.getName().equals("java.lang.String")
								|| auxReturnType.getName().equals("java.lang.Double")
								|| auxReturnType.getName().equals("java.lang.Integer")
								|| auxReturnType.getName().equals("java.lang.Long")
								|| auxReturnType.getName().equals("java.lang.Short")
								|| auxReturnType.getName().equals("java.lang.Float")
								|| auxReturnType.getName().equals("java.lang.Boolean")
								|| auxReturnType.getName().equals("java.math.BigDecimal")) {
							output.setStringOutput(new String[] { auxResult1 != null ? auxResult1.toString() : "",
									auxResult2 != null ? auxResult2.toString() : "" });
							output.setOriginalValueOutput(new Object[] { auxResult1, auxResult2 });
						} else if (ActivityVersionUtil.implementsVersionable(auxReturnType.getInterfaces())) {
							Versionable auxVersionable1 = Versionable.class.cast(auxMethod.invoke(vForm
									.getActivityOne(), null));
							Versionable auxVersionable2 = Versionable.class.cast(auxMethod.invoke(vForm
									.getActivityTwo(), null));

							String output1 = (auxVersionable1 != null) ? ActivityVersionUtil.generateFormattedOutput(
									request, auxVersionable1.getOutput()) : null;
							String output2 = (auxVersionable2 != null) ? ActivityVersionUtil.generateFormattedOutput(
									request, auxVersionable2.getOutput()) : null;
							output.setStringOutput(new String[] { output1, output2 });
							output.setOriginalValueOutput(new Object[] { auxResult1, auxResult2 });
						} else {
							output.setStringOutput(new String[] { auxResult1 != null ? auxResult1.toString() : "",
									auxResult2 != null ? auxResult2.toString() : "" });
						}
						vForm.getOutputCollection().add(output);
					}
				}
			}
			if (fields[i].isAnnotationPresent(VersionableFieldTextEditor.class)) {
				// Obtain "get" method from field.
				Method auxMethod = ActivityVersionUtil.getMethodFromFieldName(fields[i].getName(), AmpActivityVersion.class,
						"get");

				// Compare values from 2 versions.
				String auxResult1 = (String) auxMethod.invoke(vForm.getActivityOne(), null);
				String auxResult2 = (String) auxMethod.invoke(vForm.getActivityTwo(), null);

				// Obtain annotation object.
				VersionableFieldTextEditor auxAnnotation = (VersionableFieldTextEditor) fields[i]
						.getAnnotation(VersionableFieldTextEditor.class);

				// Compare values, if both are null then they are considered
				// equal.
				if (!(auxResult1 == null && auxResult2 == null)) {
					if ((auxResult1 != null && auxResult2 == null) || (auxResult1 == null && auxResult2 != null)
							|| (!auxResult1.equals(auxResult2))) {

						output.setDescriptionOutput(auxAnnotation.fieldTitle());
						String site = RequestUtils.getSite(request).getSiteId();
						String lang = RequestUtils.getNavigationLanguage(request).getCode();
						String auxBody1 = DbUtil.getEditorBody(site, auxResult1, lang);
						String auxBody2 = DbUtil.getEditorBody(site, auxResult2, lang);
						auxBody1 = auxBody1 != null ? auxBody1 : "";
						auxBody2 = auxBody2 != null ? auxBody2 : "";
						if (!auxBody1.trim().equals(auxBody2.trim())) {
							output.setStringOutput(new String[] { auxBody1, auxBody2 });
							vForm.getOutputCollection().add(output);
						}
					}
				}
			}
			if (fields[i].isAnnotationPresent(VersionableCollection.class)) {
				// Obtain "get" method from field.
				Method auxMethod = ActivityVersionUtil.getMethodFromFieldName(fields[i].getName(), AmpActivityVersion.class,
						"get");
				// Get values from 2 versions.
				// Sometimes I have a problem with lazy collections and
				// apparently closed session.
				// Delete these lines if there are problems when saving: two
				// sessions problem.
				session = PersistenceManager.getRequestDBSession();
				Object auxResult1 = auxMethod.invoke(vForm.getActivityOne(), null);
				Hibernate.initialize(auxResult1);
				Object auxResult2 = auxMethod.invoke(vForm.getActivityTwo(), null);
				Hibernate.initialize(auxResult2);
				// Obtain annotation object.
				VersionableCollection auxAnnotation = (VersionableCollection) fields[i]
						.getAnnotation(VersionableCollection.class);
				// Create list of differences.
				List<CompareOutput> differences = new ArrayList<CompareOutput>();
				Collection auxCollection1 = (Collection) auxResult1;
				Collection auxCollection2 = (Collection) auxResult2;

				// Evaluate the type of objects stored in the collection.
				// TODO: Assuming everything is a Collection, later to implement
				// other types.
				if ((auxCollection1 == null && auxCollection2 == null)
						|| (auxCollection1 == null && auxCollection2.size() == 0)
						|| (auxCollection1.size() == 0 && auxCollection2 == null)
						|| (auxCollection1.size() == 0 && auxCollection2.size() == 0)) {
					// Collections are equal.
					continue;
				}
				Class auxReturnType = null;
				if (auxCollection1 != null && auxCollection1.size() != 0) {
					auxReturnType = auxCollection1.toArray()[0].getClass();
				} else {
					auxReturnType = auxCollection2.toArray()[0].getClass();
				}
				if (auxReturnType.getName().equals("java.util.Date") || auxReturnType.getName().equals("java.sql.Date")
						|| auxReturnType.getName().equals("java.lang.String")
						|| auxReturnType.getName().equals("java.lang.Double")
						|| auxReturnType.getName().equals("java.lang.Integer")
						|| auxReturnType.getName().equals("java.lang.Long")
						|| auxReturnType.getName().equals("java.lang.Short")
						|| auxReturnType.getName().equals("java.lang.Float")
						|| auxReturnType.getName().equals("java.lang.Boolean")
						|| auxReturnType.getName().equals("java.math.BigDecimal")) {

					// Wrappers don't have IDs then we can't detect "updates".
					// Iterate each collection and show as different the values
					// not present in the other.
					Iterator iter1 = auxCollection1.iterator();
					while (iter1.hasNext()) {
						int coincidence = 0;
						Object auxObject1 = iter1.next();
						String auxValue1 = auxObject1.toString();
						Iterator iter2 = auxCollection2.iterator();
						while (iter2.hasNext()) {
							if (auxValue1.equals((String) iter2.next())) {
								coincidence++;
							}
						}
						if (coincidence == 0) {
							CompareOutput auxOutput = new CompareOutput(auxAnnotation.fieldTitle(), new String[] {
									auxValue1, "" }, fields[i], new Object[] { auxObject1, null }, false, false);
							vForm.getOutputCollection().add(auxOutput);
						}
					}
					iter1 = auxCollection2.iterator();
					while (iter1.hasNext()) {
						int coincidence = 0;
						Object auxObject2 = iter1.next();
						String auxValue2 = auxObject2.toString();
						Iterator iter2 = auxCollection1.iterator();
						while (iter2.hasNext()) {
							if (auxValue2.equals((String) iter2.next())) {
								coincidence++;
							}
						}
						if (coincidence == 0) {
							CompareOutput auxOutput = new CompareOutput(auxAnnotation.fieldTitle(), new String[] { "",
									auxValue2 }, fields[i], new Object[] { null, auxObject2 }, false, false);
							vForm.getOutputCollection().add(auxOutput);
						}
					}
				} else if (ActivityVersionUtil.implementsVersionable(auxReturnType.getInterfaces())) {
					List<Versionable> auxList = new ArrayList<Versionable>();
					Iterator iter1 = auxCollection1.iterator();
					while (iter1.hasNext()) {
						int coincidence = 0;
						Object auxObject1 = iter1.next();
						Versionable auxVersionable1 = (Versionable) auxObject1;
						Iterator iter2 = auxCollection2.iterator();
						while (iter2.hasNext()) {
							Object auxObject2 = iter2.next();
							Versionable auxVersionable2 = (Versionable) auxObject2;
							if (auxVersionable1.equalsForVersioning(auxVersionable2)) {
								coincidence++;
								Object auxValue1 = auxVersionable1.getValue() != null ? auxVersionable1.getValue() : "";
								Object auxValue2 = auxVersionable2.getValue() != null ? auxVersionable2.getValue() : "";
								if (!auxValue1.equals(auxValue2)) {
									CompareOutput auxOutput = new CompareOutput(auxAnnotation.fieldTitle(),
											new String[] {
													ActivityVersionUtil.generateFormattedOutput(request,
															auxVersionable1.getOutput()),
													ActivityVersionUtil.generateFormattedOutput(request,
															auxVersionable2.getOutput()) }, fields[i], new Object[] {
													auxObject1, auxObject2 }, false, false);

									vForm.getOutputCollection().add(auxOutput);
									auxList.add(auxVersionable1);
									auxList.add(auxVersionable2);
								}
							}
						}
						if (coincidence == 0) {
							CompareOutput auxOutput = new CompareOutput(auxAnnotation.fieldTitle(), new String[] {
									ActivityVersionUtil.generateFormattedOutput(request, auxVersionable1.getOutput()),
									"" }, fields[i], new Object[] { auxObject1, null }, false, false);
							vForm.getOutputCollection().add(auxOutput);
							auxList.add(auxVersionable1);
						}
					}
					iter1 = auxCollection2.iterator();
					while (iter1.hasNext()) {
						int coincidence = 0;
						Object auxObject2 = iter1.next();
						Versionable auxVersionable2 = (Versionable) auxObject2;
						Iterator iter2 = auxCollection1.iterator();
						while (iter2.hasNext()) {
							Object auxObject1 = iter2.next();
							Versionable auxVersionable1 = (Versionable) auxObject1;
							if (auxVersionable2.equalsForVersioning(auxVersionable1)) {
								coincidence++;
								Object auxValue1 = auxVersionable1.getValue() != null ? auxVersionable1.getValue() : "";
								Object auxValue2 = auxVersionable2.getValue() != null ? auxVersionable2.getValue() : "";
								if (!auxValue2.equals(auxValue1)) {
									// Check if the object was added in the
									// previous iteration.
									if (!auxList.contains(auxVersionable2)) {
										CompareOutput auxOutput = new CompareOutput(auxAnnotation.fieldTitle(),
												new String[] {
														ActivityVersionUtil.generateFormattedOutput(request,
																auxVersionable1.getOutput()),
														ActivityVersionUtil.generateFormattedOutput(request,
																auxVersionable2.getOutput()) }, fields[i],
												new Object[] { auxObject1, auxObject2 }, false, false);
										vForm.getOutputCollection().add(auxOutput);
									}
								}
							}
						}
						if (coincidence == 0) {
							// Check if the object was added in the
							// previous iteration.
							if (!auxList.contains(auxVersionable2)) {
								CompareOutput auxOutput = new CompareOutput(auxAnnotation.fieldTitle(), new String[] {
										"",
										ActivityVersionUtil.generateFormattedOutput(request, auxVersionable2
												.getOutput()) }, fields[i], new Object[] { null, auxObject2 }, false,
										false);
								vForm.getOutputCollection().add(auxOutput);
							}
						}
					}
				} else {
					output.setStringOutput(new String[] { auxResult1 != null ? auxResult1.toString() : "",
							auxResult2 != null ? auxResult2.toString() : "" });
					output.setDescriptionOutput(auxAnnotation.fieldTitle());
					vForm.getOutputCollection().add(output);
				}
			}
		}

		return mapping.findForward("forward");
	}

	public ActionForward enableMerge(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		CompareActivityVersionsForm vForm = (CompareActivityVersionsForm) form;
		List<CompareOutput> filteredList = new ArrayList<CompareOutput>();
		Iterator<CompareOutput> iter = vForm.getOutputCollection().iterator();
		while (iter.hasNext()) {
			CompareOutput auxCompare = iter.next();
			if (!auxCompare.getBlockSingleChangeOutput()) {
				filteredList.add(auxCompare);
			}
		}
		vForm.setOutputCollection(filteredList);
		vForm.setMergedValues(new String[vForm.getOutputCollection().size()]);
		return mapping.findForward("forward");
	}

	public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return mapping.findForward("reload");
	}

	public ActionForward saveNewActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		CompareActivityVersionsForm vForm = (CompareActivityVersionsForm) form;
		// Get data from jsp.
		ActionErrors errors = new ActionErrors();
		boolean hasErrors = false;
		List<CompareOutput> auxData = new ArrayList<CompareOutput>();
		for (int i = 0; i < vForm.getMergedValues().length; i++) {
			CompareOutput auxOutput = vForm.getOutputCollection().get(i);
			CompareOutput newOutput = new CompareOutput();
			newOutput.setFieldOutput(auxOutput.getFieldOutput());
			newOutput.setMandatoryForSingleChangeOutput(auxOutput.getMandatoryForSingleChangeOutput());
			if (vForm.getMergedValues()[i].equals("L")) {
				newOutput.setOriginalValueOutput(new Object[] { auxOutput.getOriginalValueOutput()[1], null });
			} else if (vForm.getMergedValues()[i].equals("R")) {
				newOutput.setOriginalValueOutput(new Object[] { auxOutput.getOriginalValueOutput()[0], null });
			} else {
				// The user didn't select a value, then is empty (this is
				// important because the merged activity is a copy of one of the
				// compared versions).
				newOutput.setOriginalValueOutput(new Object[] { null, null });

				// Raise error if mandatory fields have no values.
				if (newOutput.getMandatoryForSingleChangeOutput()) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"error.aim.versioning.missingMandatoryValue", auxOutput.getDescriptionOutput()));
					hasErrors = true;
				}
			}
			auxData.add(newOutput);
		}
		if (hasErrors) {
			saveErrors(request, errors);
			vForm.setShowMergeColumn(true);
			vForm.setMethod("enableMerge");
			return mapping.findForward("forward");
		}

		// The main idea is: once we have the collection with fields selected by
		// the user we need to COPY one of the AmpActivity objects and call the
		// getters from these fields and push those values into the matching
		// fields
		// in the new activity. Some values will have to be auto calculated like
		// some ids, dates, etc.
		// Others are more complex like collections or our objects that
		// implement Versionable, in those cases we need to call a specific
		// method to get the value of the field in a right format for inserting
		// into a new activity, ie: set the activityid property, etc.
		Session session = null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			session.connection().setAutoCommit(false);
//beginTransaction();

			TeamMember tm = (TeamMember) request.getSession().getAttribute("currentMember");
			AmpTeamMember member = (AmpTeamMember) session.load(AmpTeamMember.class, tm.getMemberId());

			AmpActivityVersion oldActivity = (AmpActivityVersion) session.load(AmpActivityVersion.class, vForm
					.getOldActivity().getAmpActivityId());
			AmpActivityVersion auxActivity = ActivityVersionUtil.cloneActivity(oldActivity, member);
			auxActivity.setAmpActivityId(null);
			session.save(auxActivity);
			String ampId = ActivityUtil.generateAmpId(member.getUser(), auxActivity.getAmpActivityId(), session);
			auxActivity.setAmpId(ampId);
			session.update(auxActivity);

			// Code related to versioning.
			AmpActivityGroup auxActivityGroup = (AmpActivityGroup) session.load(AmpActivityGroup.class, vForm
					.getOldActivity().getAmpActivityGroup().getAmpActivityGroupId());
			auxActivityGroup.setAmpActivityLastVersion(auxActivity);
			session.save(auxActivityGroup);
			auxActivity.setAmpActivityGroup(auxActivityGroup);
			auxActivity.setModifiedDate(Calendar.getInstance().getTime());
			auxActivity.setModifiedBy(member);
			auxActivity.setAmpActivityPreviousVersion(vForm.getOldActivity());
			session.update(auxActivity);

			// Insert fields selected by user into AmpActity properties.
			Iterator<CompareOutput> iter = auxData.iterator();
			while (iter.hasNext()) {
				CompareOutput co = iter.next();
				Method auxMethod = ActivityVersionUtil.getMethodFromFieldName(co.getFieldOutput().getName(),
						AmpActivityFields.class, "set");
				// Get value as object.
				Object auxOriginalValueObject = co.getOriginalValueOutput()[0];
				// Check if implements Versionable and call prepareMerge.
				if (auxOriginalValueObject != null) {
					if (ActivityVersionUtil.implementsVersionable(auxOriginalValueObject.getClass().getInterfaces())) {
						Versionable auxVersionableValueObject = (Versionable) auxOriginalValueObject;
						auxOriginalValueObject = auxVersionableValueObject.prepareMerge(auxActivity);
					}
					Class[] params = auxMethod.getParameterTypes();
					if (params != null && params[0].getName().contains("java.util.Set")) {
						Method auxGetMethod = ActivityVersionUtil.getMethodFromFieldName(co.getFieldOutput().getName(),
								AmpActivityVersion.class, "get");
						Set auxSet = (Set) auxGetMethod.invoke(auxActivity);
						if (auxSet == null) {
							auxSet = new HashSet();
						}
						auxSet.add(auxOriginalValueObject);
						auxMethod.invoke(auxActivity, auxSet);
					} else {
						auxMethod.invoke(auxActivity, auxOriginalValueObject);
					}
					session.update(auxActivity);
				}
			}

			session.save(auxActivity);
//session.flush();
			//tx.commit();
			logger.warn("Activity Saved.");
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			tx.rollback();
		}
		return mapping.findForward("index");
	}

	// TODO: Note: ONLY FOR DEVELOPMENT PURPOSES. Delete this method when the
	// jsp is ready for merging data.
	private List<CompareOutput> generateRandomMergedData(List<CompareOutput> input) {
		List<CompareOutput> output = new ArrayList<CompareOutput>();
		Iterator<CompareOutput> iter = input.iterator();
		while (iter.hasNext()) {
			CompareOutput auxOutput = iter.next();
			CompareOutput newOutput = new CompareOutput();
			double rnd1 = Math.random();
			double rnd2 = Math.random();
			if (auxOutput.getBlockSingleChangeOutput() == false) {
				newOutput.setFieldOutput(auxOutput.getFieldOutput());
				newOutput.setMandatoryForSingleChangeOutput(auxOutput.getMandatoryForSingleChangeOutput());
				if (rnd1 >= rnd2) {
					newOutput.setOriginalValueOutput(new Object[] { auxOutput.getOriginalValueOutput()[0], null });
				} else {
					newOutput.setOriginalValueOutput(new Object[] { auxOutput.getOriginalValueOutput()[1], null });
				}
				output.add(newOutput);
			}
		}
		return output;
	}

	private Object initializeCollections(Session session, Object aux) {
		try {
			Hibernate.initialize(aux);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			try {
				// Should never enter here.
				Hibernate.initialize(aux);
			} catch (Exception e2) {
				logger.error(e2);
				e2.printStackTrace();
			}
		}
		return aux;
	}
}