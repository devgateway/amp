/**
 * 
 */
package org.digijava.module.xmlpatcher.core;

import java.util.Formatter;

/**
 * This is a storage for all patches defined in one path and later moved to a different one.
 * Normally we get a poluted output on different DBs with messages like:
 * "
 * [XmlPatcherUtil] old location: xmlpatches/2.3.00/
 * [XmlPatcherUtil] new location: xmlpatches/2.3.08/
 * ERROR [XmlPatcherUtil] Patch duplication detected! The name AMP-13889-enable-recreate-views.xml is used by two
 * or more patches. Remove duplicates and restart the server. You are not allowed to use one patch name twice even 
 * if the older patch has been deleted.
 * "
 * 
 * For any patches that are safe to be moved, we'll define mappings here
 * 
 * @author Nadejda Mandrescu
 */
public class XMLPatchesWrongPaths {
    /**
     * Please do not abuse this option! This is only for dirty patches.
     * Add for each entry why it is safe to move to the new location.
     */
    // structure: {patch-name, old-path, new-path-to-set}
    private static final String[][] PATCHES_PATH_FIX = new String [][]{
        // don't have <= 2.3 releases, or at least any upgrade would pass this one, especially that we have multiple recreate views requests
        {"AMP-13889-enable-recreate-views.xml", "xmlpatches/2.3.00/", "xmlpatches/2.3.08/"},
        // applicable to old charts that are no longer used in 2.10+, plus any upgrade to 2.10+ go through 2.8 patches first anyway and looks like the order here is not important
        {"AMP-17309.xml", "xmlpatches/2.7.9/", "xmlpatches/2.8.7/"},
        // any upgrade to 2.10+ go through 2.8 patches first anyway and looks like the order here is not important
        {"AMP-17302.xml", "xmlpatches/2.7.21/", "xmlpatches/2.8.6/"},
        // any upgrade to 2.10+ go through 2.8 patches first anyway and looks like the order here is not important
        {"AMP-17073-enable-recreate-views.xml", "xmlpatches/2.7.18/", "xmlpatches/2.8.4/"},
        // any upgrade to 2.10+ go through 2.8 patches first anyway and looks like the order here is not important
        {"AMP-17073.xml", "xmlpatches/2.7.18/", "xmlpatches/2.8.4/"},
        // applicable to old maps that are no longer used in 2.10+, plus any upgrade to 2.10+ go through 2.8 patches first anyway and looks like the order here is not important
        {"AMP-16976.xml", "xmlpatches/2.7.16/", "xmlpatches/2.8.3/"},
        // wrong folder created, relocated and fixing the path 
        {"AMP-24103-deffer-unique-constraint.xml", "xmlpatches/2.z12.7/", "xmlpatches/2.z12.07/"},
        // wrong folder created, relocated and fixing the path 
        {"AMP-21326-redo-etl-1.xml", "xmlpatches/2.z11.13/", "xmlpatches/2.z11.12/"},
        // any country released in 2.11.23 has this wrong path, relocated and fixing the path 
        {"AMP-22481-rename-hide-word-global-setting.xml", "xmlpatches/2.z11.23/", "xmlpatches/2.z12.01/"}
    };
    
    public static final String SQL_PATCH = getSQLPatch();
    
    private static String getSQLPatch() {
        // using = vs LIKE in this case doesn't seem to have any impact, because no locale is used  
        String update = "UPDATE amp_xml_patch SET location = '%3$s' WHERE patch_id = '%1$s' AND location = '%2$s'; ";
        StringBuilder sb = new StringBuilder((update.length() + 100) * PATCHES_PATH_FIX.length);
        Formatter formatter = new Formatter(sb);
        for (Object[] patchFix : PATCHES_PATH_FIX) {
            formatter.format(update, patchFix);
        }
        formatter.close();
        return sb.toString();
    }
}
