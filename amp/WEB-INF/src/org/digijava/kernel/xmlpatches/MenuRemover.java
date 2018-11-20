package org.digijava.kernel.xmlpatches;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;

/**
 * Remove a menu without writing a ton of sql.
 *
 * @author Octavian Ciubotaru
 */
public final class MenuRemover {

    private MenuRemover() {
    }

    /**
     * Remove the menu item identified by menu path. Parent menu items are not deleted.
     *
     * @param menuPath menu entry name path
     */
    public static void remove(String... menuPath) {
        PersistenceManager.doWorkInTransaction(c -> remove(c, menuPath));
    }

    private static void remove(Connection c, String... menuPath) {
        List<Long> menuIds = SQLUtils.fetchLongs(c, getSelectMenuIdSql(Arrays.asList(menuPath)));

        List<Long> ruleIds = SQLUtils.fetchLongs(c,
                String.format("select rule_id from amp_menu_entry_view where menu_id in (%s)",
                        Util.toCSStringForIN(menuIds)));

        execDeleteInQuery(c, "amp_menu_entry_view", "menu_id", menuIds);
        execDeleteInQuery(c, "amp_menu_entry_dg_group", "menu_id", menuIds);
        execDeleteInQuery(c, "amp_menu_entry", "id", menuIds);

        deleteVisibilityRules(c, ruleIds);
    }

    private static void execDeleteInQuery(Connection c, String table, String column, List values) {
        SQLUtils.executeQuery(c, getDeleteInQuery(table, column, values));
    }

    private static String getDeleteInQuery(String table, String column, List values) {
        return String.format("delete from %s where %s in (%s)", table, column, Util.toCSStringForIN(values));
    }

    private static String getSelectMenuIdSql(List<String> menuPath) {
        if (menuPath.size() > 1) {
            return String.format(
                    "select id from amp_menu_entry where name='%s' and parent_id in (%s)",
                    menuPath.get(menuPath.size() - 1),
                    getSelectMenuIdSql(menuPath.subList(0, menuPath.size() - 1)));
        } else {
            return String.format(
                    "select id from amp_menu_entry where name='%s' and parent_id is null",
                    menuPath.get(0));
        }
    }

    public static void removeUnusedVisibilityRules() {
        PersistenceManager.doWorkInTransaction(MenuRemover::removeUnusedVisibilityRules);
    }

    public static void removeUnusedVisibilityRules(Connection c) {
        List<Long> ruleIds = SQLUtils.fetchLongs(c,
                "select id "
                        + "from amp_visibility_rule "
                        + "where id not in (select rule_id from amp_menu_entry_view where rule_id is not null)");

        deleteVisibilityRules(c, ruleIds);
    }

    private static void deleteVisibilityRules(Connection c, List<Long> ruleIds) {
        execDeleteInQuery(c, "amp_visibility_rule_amp_fields_visibility", "rule_id", ruleIds);
        execDeleteInQuery(c, "amp_visibility_rule_amp_features_visibility", "rule_id", ruleIds);
        execDeleteInQuery(c, "amp_visibility_rule_amp_modules_visibility", "rule_id", ruleIds);
        execDeleteInQuery(c, "amp_visibility_rule", "id", ruleIds);
    }
}
