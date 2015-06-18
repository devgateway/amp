package org.digijava.kernel.ampapi.endpoints.activity.visibility;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.FeaturesUtil;

public class FieldVisibility {

	private final static Map<String, Boolean> visibilityMap = new HashMap<String, Boolean>();
	private static Date lastTreeVisibilityUpdate;

	/**
	 * Checks whether a Field is visible or not according to the Feature
	 * Manager. It checks the @Interchangeable annotation for its fmPath. If the
	 * the Module is enabled on the Feature Manager or there is no defined
	 * Feature Manager path for the Field the it returns true Otherwise it
	 * returns false
	 * 
	 * @param field
	 *            , the field to determine its visibility
	 * @return boolean, returns true if the field is visible, false otherwise.
	 */
	public static boolean isVisible(Field field) {
		HttpSession session = TLSUtils.getRequest().getSession();

		ServletContext ampContext = session.getServletContext();
		AmpTreeVisibility ampTreeVisibility = FeaturesUtil.getAmpTreeVisibility(ampContext, session);
		checkTreeVisibilityUpdate(session);

		boolean isVisible = false;
		Interchangeable annotation = field.getAnnotation(Interchangeable.class);
		if (annotation == null) {
			return isVisible;
		}
		String path = annotation.fmPath();
		if (path.equals("")) {
			isVisible = true;
		} else {
			String ancestorVerifiedPath = getLastVerifiedPath(path);
			if (ancestorVerifiedPath.equals("")) {
				ancestorVerifiedPath = getChildFMPath(path, ancestorVerifiedPath);
			}
			isVisible = isVisibleInFM(ampTreeVisibility, path,ancestorVerifiedPath);
		}
		return isVisible;
	}

	private static void checkTreeVisibilityUpdate(HttpSession session) {
		Date lastUpdate = (Date) session.getAttribute("ampTreeVisibilityModificationDate");
		if (lastTreeVisibilityUpdate!=null && lastUpdate.after(lastTreeVisibilityUpdate)) {
			visibilityMap.clear();
		}
		lastTreeVisibilityUpdate = lastUpdate;

	}

	private static boolean isVisibleInFM(AmpTreeVisibility ampTreeVisibility, String path,String lastVerifiedPath) {
		boolean isVisible = false;
		AmpModulesVisibility modulesVisibility = ampTreeVisibility.getModuleByNameFromRoot(lastVerifiedPath);
		if (modulesVisibility != null) {
			isVisible = modulesVisibility.isVisibleTemplateObj((AmpTemplatesVisibility) ampTreeVisibility.getRoot());
			visibilityMap.put(lastVerifiedPath, isVisible);
		}
		if (!path.equals(lastVerifiedPath) && isVisible) {
			lastVerifiedPath = getChildFMPath(path, lastVerifiedPath);
			isVisible = isVisibleInFM(ampTreeVisibility, path, lastVerifiedPath);
		}
		return isVisible;
	}

	private static String getChildFMPath(String fmPath, String lastVerifiedPath) {
		String pathDifference ;
		if (lastVerifiedPath.equals("")) {
			pathDifference = fmPath;
		}
		else {
			pathDifference = fmPath.substring(lastVerifiedPath.length(),fmPath.length());
		}
		int secondIndex = StringUtils.ordinalIndexOf(pathDifference, "/", 2);
		if (secondIndex == -1) {
			secondIndex = pathDifference.length();
		}
		lastVerifiedPath = lastVerifiedPath + pathDifference.substring(0, secondIndex);
		return lastVerifiedPath;
	}

	private static String getLastVerifiedPath(String fmPath) {
		String fmPathToCheck = fmPath;
		boolean alreadyVerified = false;
		do {
			alreadyVerified = isInMap(fmPathToCheck);
			fmPath = fmPathToCheck;
			fmPathToCheck = fmPathToCheck.substring(0, fmPathToCheck.lastIndexOf("/"));
			if (fmPathToCheck.equals("")) {
				alreadyVerified = true;
			}

		} while (!alreadyVerified);
		return fmPath;
	}

	private static boolean isInMap(String fmPath) {
		return visibilityMap.containsKey(fmPath);
	}
}
