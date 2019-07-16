package org.digijava.kernel.ampapi.endpoints.activity.visibility;

import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.ampapi.endpoints.activity.ActivityEPConstants;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * Verifies if the associated FM path is enabled
 * 
 */
public class FMVisibility {
    
    public static final String PARENT_FM = "_PARENT_FM_";
    public static final String ANY_FM = "_ANY_FM_";
    public static final String ALWAYS_VISIBLE_FM = "_ALWAYS_VISIBLE_FM_";

    private final static Map<String, Boolean> visibilityMap = new HashMap<String, Boolean>();
    private static Date lastTreeVisibilityUpdate;

    /**
     * Checks if a given FM path is enabled 
     * @param fmPath, the String with the FM path
     * @return true if is enabled, false otherwise
     */
    public static boolean isFmPathEnabled(String fmPath, Deque<Interchangeable> intchStack) {
        // pre-process
        if (intchStack != null) {
            Iterator<Interchangeable> iter = intchStack.iterator();
            while (iter.hasNext() && fmPath.contains(PARENT_FM)) {
                Interchangeable interchangeable = iter.next();
                // skipping current field interchangeable path if it was pushed to the queue
                if (!fmPath.equals(interchangeable.fmPath())) {
                    // if it contains more parent paths, we need to add to each one the current fmPath
                    if (interchangeable.fmPath().startsWith(ANY_FM)) {
                        fmPath = updateAnyFmPaths(fmPath, interchangeable);
                    } else {
                        fmPath = fmPath.replace(PARENT_FM, interchangeable.fmPath());
                    }
                }
            }
        }
        
        if (fmPath.startsWith(ANY_FM)) {
            for(String anyFMOption : fmPath.substring(ANY_FM.length()).split("\\|")) {
                if (StringUtils.isNotBlank(anyFMOption) && isFinalFmPathEnabled(anyFMOption)) {
                    return true;
                }
            }
            return false;
        } else {
            return isFinalFmPathEnabled(fmPath);
        }
    }
    
    /**
     * 
     * Update the children visibility paths for parents having _ANY_FM_ clause
     * E.g.: root path = _ANY_FM_X|Y, field path = z
     * The children of the path should be checked for _ANY_FM_X\z|Y\z
     * 
     * @param fmPath - current field fmPath. e.g: _PARENT_FM_\z
     * @param parentInterchangeable - the parent interchangeable (_ANY_FM_X|Y)
     * @return updated path (_ANY_FM_X\z|Y\z) 
     */
    protected static String updateAnyFmPaths(String fmPath, Interchangeable parentInterchangeable) {
        String updatedPath = parentInterchangeable.fmPath();
        
        for(String anyFMOption : parentInterchangeable.fmPath().substring(ANY_FM.length()).split("\\|")) {
            if (StringUtils.isNotBlank(anyFMOption)) {
                updatedPath = updatedPath.replace(anyFMOption, anyFMOption + fmPath.replace(PARENT_FM, ""));
            }
        }
        
        return updatedPath;
    }
    
    protected static boolean isFinalFmPathEnabled(String fmPath) {
        boolean isEnabled = false;
        HttpSession session = TLSUtils.getRequest().getSession();
        ServletContext ampContext = session.getServletContext();
        AmpTreeVisibility ampTreeVisibility = FeaturesUtil.getAmpTreeVisibility(ampContext, session);
        String ancestorVerifiedPath = getLastVerifiedPath(fmPath);
        if (ancestorVerifiedPath.equals("")) {
            ancestorVerifiedPath = getChildFMPath(fmPath, ancestorVerifiedPath);
        }
        isEnabled = isVisibleInFM(ampTreeVisibility, fmPath,ancestorVerifiedPath);

        return isEnabled;
    }
    
    /**
     * Checks whether a Field is visible or not according to the Feature
     * Manager. It checks the @Interchangeable annotation for its fmPath. If the
     * the Module is enabled on the Feature Manager or there is no defined
     * Feature Manager path for the Field the it returns true Otherwise it
     * returns false
     * 
     * @param field the field to determine its visibility
     * @return true if the field is visible, false otherwise
     */
    public static boolean isVisible(String fmPath, Deque<Interchangeable> intchStack) {
        if (fmPath == null)
            return true;
        HttpSession session = TLSUtils.getRequest().getSession();
        checkTreeVisibilityUpdate(session);
        boolean isVisible = false;
        if (fmPath.equals(FMVisibility.ALWAYS_VISIBLE_FM) || fmPath.equals("")) {
            isVisible = true;
        } else {
            isVisible = isFmPathEnabled(fmPath, intchStack);
        }

        return isVisible && isFieldVisibleInPublicView(fmPath);
    }

    /**
     * Check if the field is visible in public view or not.
     * Usually all the fields are visible by default. There are few exceptions, like Contacts field from Activity
     *
     * @param fmPath
     * @return
     */
    private static boolean isFieldVisibleInPublicView(String fmPath) {
        TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
        if (tm == null && ActivityEPConstants.CONTACTS_PATH.equals(fmPath)) {
            return FeaturesUtil.isVisibleFeature("Contacts");
        }

        return true;
    }

    /**
     * Checks if there was any update under Feature Manager tree visibility.
     * If that the case all FM paths need to be rechecked again.
     * 
     * @param session, HttpSession containing the tree last modification date.
     */
    private static void checkTreeVisibilityUpdate(HttpSession session) {
        Date lastUpdate = (Date) session.getAttribute("ampTreeVisibilityModificationDate");
        if (lastTreeVisibilityUpdate!=null && lastUpdate.after(lastTreeVisibilityUpdate)) {
            visibilityMap.clear();
        }
        lastTreeVisibilityUpdate = lastUpdate;

    }

    /**
     * Checks if a FM Path is visible. In order to be visible, the FM path and all its ancestors need to be checked.
     * 
     * @param ampTreeVisibility, the AmpTreeVisibility that contains all modules visibility
     * @param fmPath, the path to check for its visibility
     * @param lastVerifiedPath, the last FM verified path. This is needed in order to avoid checking all FM path ancestors 
     * multiple times
     * @return, whether a FM path is visible or not
     */
    private static boolean isVisibleInFM(AmpTreeVisibility ampTreeVisibility, String fmPath,String lastVerifiedPath) {
        boolean isVisible = false;
        AmpModulesVisibility modulesVisibility = ampTreeVisibility.getModuleByNameFromRoot(lastVerifiedPath);
        if (modulesVisibility != null) {
            isVisible = modulesVisibility.isVisibleTemplateObj((AmpTemplatesVisibility) ampTreeVisibility.getRoot());
            
            // AMP-20597 when root FM is disabled and children are enabled, all the children will be not shown
            if ("/Activity Form".equals(lastVerifiedPath)) {
                isVisible = true;
            }
            
            visibilityMap.put(lastVerifiedPath, isVisible);
        }
        if (!fmPath.equals(lastVerifiedPath) && isVisible) {
            lastVerifiedPath = getChildFMPath(fmPath, lastVerifiedPath);
            isVisible = isVisibleInFM(ampTreeVisibility, fmPath, lastVerifiedPath);
        }
        return isVisible;
    }

    /**
     * Starting from the last FM verified Path, it returns its next child for the FM path we are searching for.
     * If lastVerifiedPath is '/Activity Form', and we are searching for the visibility of 
     * fmPath = '/Activity Form/Identification/Description' then the next child will be
     * '/Activity Form/Identification'
     * @param fmPath, the FM path to check if it is visible or not.
     * @param lastVerifiedPath, the last FM ancestor path that was already verified for its visibility
     * @return a String with the child FM path 
     */
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

    /**
     * Given a FM Path like '/Activity Form/Identification/Description' it checks which was the last verified FM path.
     * Starting from fmPath parameter and going up until the last verified is found. That is, if '/Activity Form/Identification/Description'
     * was not already verified, it checks if '/Activity Form/Identification' was and after that it checks for the root 
     *'/Activity Form'. Checking is stopped when a fm path that was already verified is found.
     * 
     * 
     * @param fmPath, the path to check if it was already verified
     * @return the last verified FM path.
     */
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

    /**
     * Verifies if a FM Path was already checked.
     * 
     * @param fmPath, the path under Feature Manager
     * @return true if it was already verified, false otherwise
     */
    private static boolean isInMap(String fmPath) {
        return visibilityMap.containsKey(fmPath);
    }
}
