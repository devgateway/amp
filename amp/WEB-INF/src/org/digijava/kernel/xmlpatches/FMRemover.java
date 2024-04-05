package org.digijava.kernel.xmlpatches;


import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;

import java.util.function.Consumer;

/**
 * FM entry remover.
 *
 * FM structure:
 * 1. flexible recursive structure. modules -> modules to any depth.
 *   1a. module name of the child starts with parent module name (ex: /PARENT and /PARENT/CHILD)
 *   1b. module name of the child has nothing to do with parent's name
 * 2. rigid structure. modules -> features -> fields.
 *
 * @author Octavian Ciubotaru
 */
public final class FMRemover {

    private FMRemover() {
    }

    /**
     * Removes only the last FM element of the fm path. Supports only 1b style of FM entries.
     *
     * @param fmPath path to fm entry
     */
    public static void removeModuleOld(String fmPath) {
        String moduleIdSql = getSelectModuleIdSql(fmPath);
        String deleteModuleTemplateSql = getDeleteModuleTemplateSql(moduleIdSql);
        String deleteModuleVizSql = getDeleteModuleVizSql(moduleIdSql);

        PersistenceManager.doInTransaction(
                (Consumer<Session>) s -> s.doWork(c -> {
                    SQLUtils.executeQuery(c, deleteModuleTemplateSql);
                    SQLUtils.executeQuery(c, deleteModuleVizSql);
                }));
    }

    private static String getDeleteModuleTemplateSql(String moduleIdSql) {
        return String.format("delete from amp_modules_templates where module in (%s)",
                moduleIdSql);
    }

    private static String getDeleteModuleVizSql(String moduleIdSql) {
        return String.format("delete from amp_modules_visibility where id in (%s)",
                moduleIdSql);
    }

    private static String getSelectModuleIdSql(String fmPath) {
        int pos = fmPath.lastIndexOf("/");
        if (pos == 0) {
            return String.format(
                    "select id from amp_modules_visibility where name='%s' and parent is null",
                    fmPath.substring(1));
        } else {
            return String.format(
                    "select id from amp_modules_visibility where name='%s' and parent in (%s)",
                    fmPath.substring(pos + 1),
                    getSelectModuleIdSql(fmPath.substring(0, pos)));
        }
    }
}
