package org.digijava.module.aim.action;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.annotations.activityversioning.CompareOutput;
import org.digijava.module.aim.annotations.activityversioning.VersionableCollection;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldTextEditor;
import org.digijava.module.aim.annotations.activityversioning.VersionableFieldSimple;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.Versionable;
import org.digijava.module.aim.form.CompareActivityVersionsForm;
import org.digijava.module.aim.util.ActivityVersionUtil;
import org.digijava.module.editor.util.DbUtil;
import org.hibernate.Session;

public class CompareActivityVersions extends Action {

	private ServletContext ampContext = null;

	private static Logger logger = Logger.getLogger(EditActivity.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		CompareActivityVersionsForm vForm = (CompareActivityVersionsForm) form;
		vForm.setOutputCollection(new ArrayList<CompareOutput>());
		// Load the activities.
		Session session = PersistenceManager.getRequestDBSession();
		vForm.setActivityOne((AmpActivityVersion) session.load(AmpActivityVersion.class, vForm.getActivityOneId()));
		vForm.setActivityTwo((AmpActivityVersion) session.load(AmpActivityVersion.class, vForm.getActivityTwoId()));

		// Retrieve annotated for versioning fields.
		Field[] fields = AmpActivity.class.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			CompareOutput output = new CompareOutput();

			if (fields[i].isAnnotationPresent(VersionableFieldSimple.class)) {
				// Obtain "get" method from field.
				Method auxMethod = ActivityVersionUtil.getMethodFromFieldName(fields[i].getName(), AmpActivity.class);

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
				Method auxMethod = ActivityVersionUtil.getMethodFromFieldName(fields[i].getName(), AmpActivity.class);

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
				Method auxMethod = ActivityVersionUtil.getMethodFromFieldName(fields[i].getName(), AmpActivity.class);
				// Get values from 2 versions.
				Object auxResult1 = auxMethod.invoke(vForm.getActivityOne(), null);
				Object auxResult2 = auxMethod.invoke(vForm.getActivityTwo(), null);
				// Obtain annotation object.
				VersionableCollection auxAnnotation = (VersionableCollection) fields[i]
						.getAnnotation(VersionableCollection.class);
				// Create list of differences.
				List<CompareOutput> differences = new ArrayList<CompareOutput>();
				// Evaluate the type of objects stored in the collection.
				// TODO: Assuming everything is a Collection, later to implement
				// other types.
				Collection auxCollection1 = (Collection) auxResult1;
				Collection auxCollection2 = (Collection) auxResult2;
				if ((auxCollection1 == null && auxResult2 == null)
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
						String auxValue1 = (String) iter1.next();
						Iterator iter2 = auxCollection2.iterator();
						while (iter2.hasNext()) {
							if (auxValue1.equals((String) iter2.next())) {
								coincidence++;
							}
						}
						if (coincidence == 0) {
							CompareOutput auxOutput = new CompareOutput(auxAnnotation.fieldTitle(), new String[] {
									auxValue1, "" });
							vForm.getOutputCollection().add(auxOutput);
						}
					}
					iter1 = auxCollection2.iterator();
					while (iter1.hasNext()) {
						int coincidence = 0;
						String auxValue2 = (String) iter1.next();
						Iterator iter2 = auxCollection1.iterator();
						while (iter2.hasNext()) {
							if (auxValue2.equals((String) iter2.next())) {
								coincidence++;
							}
						}
						if (coincidence == 0) {
							CompareOutput auxOutput = new CompareOutput(auxAnnotation.fieldTitle(), new String[] { "",
									auxValue2 });
							vForm.getOutputCollection().add(auxOutput);
						}
					}
				} else if (ActivityVersionUtil.implementsVersionable(auxReturnType.getInterfaces())) {
					List<Versionable> auxList = new ArrayList<Versionable>();
					Iterator iter1 = auxCollection1.iterator();
					while (iter1.hasNext()) {
						int coincidence = 0;
						Versionable auxVersionable1 = (Versionable) iter1.next();
						Iterator iter2 = auxCollection2.iterator();
						while (iter2.hasNext()) {
							Versionable auxVersionable2 = (Versionable) iter2.next();
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
															auxVersionable2.getOutput()) });

									vForm.getOutputCollection().add(auxOutput);
									auxList.add(auxVersionable1);
									auxList.add(auxVersionable2);
								}
							}
						}
						if (coincidence == 0) {
							CompareOutput auxOutput = new CompareOutput(auxAnnotation.fieldTitle(), new String[] {
									ActivityVersionUtil.generateFormattedOutput(request, auxVersionable1.getOutput()),
									"" });
							vForm.getOutputCollection().add(auxOutput);
							auxList.add(auxVersionable1);
						}
					}
					iter1 = auxCollection2.iterator();
					while (iter1.hasNext()) {
						int coincidence = 0;
						Versionable auxVersionable2 = (Versionable) iter1.next();
						Iterator iter2 = auxCollection1.iterator();
						while (iter2.hasNext()) {
							Versionable auxVersionable1 = (Versionable) iter2.next();
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
																auxVersionable2.getOutput()) });
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
												.getOutput()) });
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
}