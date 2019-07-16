/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.dgfoundation.amp.menu.MenuConstants;
import org.dgfoundation.amp.menu.MenuItem;
import org.dgfoundation.amp.menu.MenuUtils;
import org.digijava.kernel.services.AmpVersionInfo;
import org.digijava.kernel.services.AmpVersionService;
import org.digijava.kernel.ampapi.endpoints.common.EPConstants;
import org.digijava.kernel.util.SpringUtil;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.endpoints.util.SecurityUtil;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Security Endpoint related services like menu, footer, user
 * 
 * @author Nadejda Mandrescu
 */
public class SecurityService {

    private static String ampVersion;
    private static String releaseDate;
    
    /**
     * @return json structure for the current view + user + state menu
     */
    public static List<JsonBean> getMenu() {
        List<MenuItem> items = MenuUtils.getCurrentRequestMenuItems();
        return convert(items);
    }
    
    /**
     * Converts menu items to JSON structure
     *  
     * @param items
     * @return JsonBean of menu items
     */
    private static List<JsonBean> convert(List<MenuItem> items) {
        List<JsonBean> jsonItems = new ArrayList<JsonBean>();
        for (MenuItem item : items) {
            JsonBean jsonItem = new JsonBean();
            // we use old menu names definition to use existing translations
            String name = TranslatorWorker.translateText(item.title);
            // AMP-20030: do top menu item All caps and all menu items underneath it capitalized
            if (item.getParent() == null || item.getParent().getParent() == null) { // we have a common root parent, that's why we check for grandparent
                name = name.toUpperCase();
            }
            jsonItem.set(EPConstants.MENU_NAME, name);
            if (item.tooltip != null) {
                jsonItem.set(EPConstants.MENU_TOOLTIP, TranslatorWorker.translateText(item.tooltip));
            }
            if (item.url != null) {
                jsonItem.set(EPConstants.MENU_URL, item.url);
            }
            if (item.isPopup) {
                jsonItem.set(EPConstants.MENU_OPEN_POPUP, true);
            }
            if (item.isTab) {
                jsonItem.set(EPConstants.MENU_OPEN_TAB, true);
            }
            if (item.isPost) {
                jsonItem.set(EPConstants.MENU_POST, true);
            }
            if (item.getChildren().size() > 0) {
                jsonItem.set(EPConstants.MENU_CHILDREN, convert(item.getChildren()));
            }
            // special case to allow GIS/Dashboards to treat language action in their custom way 
            if (MenuConstants.LANGUAGE_ITEM.equals(item.name) || MenuConstants.PUBLIC_LANGUAGE_ITEM.equals(item.name)) {
                jsonItem.set(EPConstants.MENU_LANUGAGE, true);
            }
            jsonItems.add(jsonItem);
        }
        return jsonItems;
    }
    
    /**
     * 
     * @param xmlFilePath
     * @param siteUrl
     * @param isAdmin
     * @return
     */
    public static JsonBean getFooter(String siteUrl, boolean isAdmin) {
        JsonBean jsonItem = new JsonBean();
        populateBuildValues();
        Boolean trackingEnabled = FeaturesUtil
                .getGlobalSettingValueBoolean(GlobalSettingsConstants.ENABLE_SITE_TRACKING);
        String siteId = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.TRACKING_SITE_ID);
        String trackingUrl = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.TRACKING_SITE_URL);
        jsonItem.set(EPConstants.BUILD_DATE, releaseDate);
        jsonItem.set(EPConstants.AMP_VERSION, ampVersion);
        jsonItem.set(EPConstants.TRACKING_ENABLED, trackingEnabled);
        jsonItem.set(EPConstants.SITE_ID, siteId);
        jsonItem.set(EPConstants.TRACKING_URL, trackingUrl);
        jsonItem.set(EPConstants.FOOTER_TEXT, TranslatorWorker
                .translateText("Developed in partnership with OECD, UNDP, WB, Government of Ethiopia and DG"));
        if (isAdmin) {
            List<JsonBean> links = new ArrayList<JsonBean>();
            JsonBean adminLink = new JsonBean();
            adminLink.set(EPConstants.LINK_NAME, EPConstants.ADMIN_LINK_NAME);
            adminLink.set(EPConstants.LINK_URL, siteUrl + "/admin");

            links.add(adminLink);
            jsonItem.set(EPConstants.ADMIN_LINKS, links);
        }
        return jsonItem;
    }
    
    /**
     * Obtains and populates the values for  Amp Version and Build Date
     * 
     * @param filePath the path to the xml file containing 'buildDate' and
     * 'ampVersion' values
     */
    private static void populateBuildValues() {
        if (releaseDate == null || ampVersion == null) {
            AmpVersionService ampVersionService = SpringUtil.getBean(AmpVersionService.class);
            AmpVersionInfo versionInfo = ampVersionService.getVersionInfo();

            ampVersion = versionInfo.getAmpVersion();
            releaseDate = versionInfo.getReleaseDate();

            // if buildSource is empty it shouldn't be added to the footer.
            // if buildDate is empty it shouldn't replace the release date.
            // In PROD and STG this props should be empty
            if (StringUtils.isNotEmpty(versionInfo.getBuildSource())) {
                ampVersion += versionInfo.getBuildSource();
            }

            if (StringUtils.isNotEmpty(versionInfo.getBuildDate())) {
                releaseDate = versionInfo.getBuildDate();
            }
        }
    }
    
    public static Collection<JsonBean> getWorkspaces() {

        Collection<AmpTeam> workspaces = null;

        TeamMember tm = TeamUtil.getCurrentMember();
        if (tm != null) {
            workspaces = TeamMemberUtil.getAllTeamsForUser(tm.getEmail());
        }

        Collection<JsonBean> workspacesList = new ArrayList<JsonBean>();

        if (workspaces==null) {
            workspaces = new ArrayList<AmpTeam>();
        }

        for (AmpTeam ws: workspaces){
            workspacesList.add(SecurityUtil.getTeamJsonBean(ws));
        }

        return workspacesList;
    }
}
