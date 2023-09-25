/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.module.admin.util;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.error.AMPException;
import org.digijava.kernel.ampapi.endpoints.config.utils.ConfigHelper;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.GroupPermission;
import org.digijava.kernel.user.User;
import org.digijava.module.admin.exception.AdminException;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.helper.KeyValue;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.type.BooleanType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import java.util.*;

public class DbUtil {

    private static Logger logger = Logger.getLogger(DbUtil.class);

    public static List getAvailableLanguages() throws AdminException {

        Session session = null;
        List languages = null;
        try {
            session = PersistenceManager.getSession();
            Query q = session.createQuery("from " + Locale.class.getName() + " rs where rs.available=true");
            languages = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get language list from database", ex);
            throw new AdminException("Unable to get language list from database", ex);
        }
        return languages;
    }

    public static boolean isAvailableLanguage(String code) throws AdminException {
        boolean available = false;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session
                    .createQuery("from " + Locale.class.getName() + " l where l.available=true and l.code=:code");
            q.setParameter("code", code, StringType.INSTANCE);
            if (q.uniqueResult() != null) {
                available = true;
            }
        } catch (Exception ex) {
            logger.debug("Unable to get language from database", ex);
            throw new AdminException("Unable to get language list from database", ex);
        }

        return available;
    }

    public static void createSite(Site site) throws AdminException {
        Session sess = null;
        try {
            sess = PersistenceManager.getSession();
            // beginTransaction();

            sess.save(site);
            // tx.commit();
        } catch (Exception ex) {
            logger.debug("Unable to create site", ex);
            throw new AdminException("Unable to create site", ex);
        }
    }

    public static void editSite(Site site) throws AdminException {
        Session sess = null;
        try {
            sess = PersistenceManager.getSession();
            // beginTransaction();

            Iterator iter = site.getModuleInstances().iterator();
            while (iter.hasNext()) {
                ModuleInstance item = (ModuleInstance) iter.next();
                if (item.getSite() == null) {
                    iter.remove();
                    sess.delete(item);
                }
            }
            iter = site.getSiteDomains().iterator();
            while (iter.hasNext()) {
                SiteDomain item = (SiteDomain) iter.next();
                if (item.getSite() == null) {
                    iter.remove();
                    sess.delete(item);
                }
            }

            iter = site.getGroups().iterator();
            while (iter.hasNext()) {
                Group item = (Group) iter.next();
                if (item.getSite() == null) {
                    iter.remove();
                    sess.delete(item);
                }
            }

            sess.saveOrUpdate(site);
            // tx.commit();
        } catch (Exception ex) {

            logger.debug("Unable to modify site", ex);

            throw new AdminException("Unable to modify site", ex);
        }
    }

    public static Site getSite(Long id) throws AdminException {
        Site site = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            site = (Site) session.load(Site.class, id);
        } catch (Exception ex) {
            logger.debug("Unable to get Site ", ex);
            throw new AdminException("Unable to get Site ", ex);
        }
        return site;
    }

    /**
     * Returns group, read from database
     *
     * @param id
     *            group identity
     * @return group, read from database
     * @throws AdminException
     *             if any error occurs
     */
    public static Group getGroup(Long id) throws AdminException {
        Group group = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            group = (Group) session.load(Group.class, id);

        } catch (Exception ex) {
            logger.debug("Unable to get Group ", ex);
            throw new AdminException("Unable to get Group ", ex);
        }
        return group;
    }
    /**
     * returns the group that matches the key
     * @param key - group key e.g MEM, TRN, the keys are defined as static fields in Group.java
     * @return group
     * @throws AdminException
     */
    public static Group getGroupByKey(String key) throws
    AdminException {
        Group group = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            Iterator iter = null;
            String queryString = "select g from " + Group.class.getName() + " g where g.key=:key";
            Query query = session.createQuery(queryString);
            query.setParameter("key", key,StringType.INSTANCE);
            iter = query.iterate();
            while (iter.hasNext()) {
                group = (Group) iter.next();
                break;
            }
        } catch (Exception ex) {
            logger.debug("Unable to get group from database ", ex);
            throw new AdminException("Unable to get group from database ", ex);
        }
        return group;   
    }
    public static void editGroup(Group group) throws AdminException {
        Session sess = null;
        try {
            sess = PersistenceManager.getSession();
            // beginTransaction();

            Iterator iter = group.getPermissions().iterator();
            while (iter.hasNext()) {
                GroupPermission item = (GroupPermission) iter.next();
                if (item.getGroup() == null) {
                    iter.remove();
                    sess.delete(item);
                }
            }

            sess.saveOrUpdate(group);
            // tx.commit();
        } catch (Exception ex) {
            logger.debug("Unable to modify Group ", ex);
            throw new AdminException("Unable to modify Group", ex);
        }
    }

    public static List getSites() throws AdminException {
        List sites = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            Query query = session.createQuery("from " + Site.class.getName() + " s order by s.name");
            sites = query.list();
        } catch (Exception ex) {
            logger.debug("Unable to get sites list from database ", ex);
            throw new AdminException("Unable to get sites list from database ", ex);
        }
        return sites;
    }

    public static List getTopLevelSites() throws AdminException {
        List sites = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            Query query = session
                    .createQuery("from " + Site.class.getName() + " s where s.parentId is null order by s.name");
            sites = query.list();
        } catch (Exception ex) {
            logger.debug("Unable to get top level sites list from database ", ex);
            throw new AdminException("Unable to get top level sites list from database ", ex);
        }
        return sites;
    }

    public static List getGroupUsers(Long id) throws AdminException {
        ArrayList users = new ArrayList();
        ;
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            Group group = (Group) session.load(Group.class, id);
            Iterator iter = group.getUsers().iterator();
            while (iter.hasNext()) {
                User user = (User) iter.next();
                users.add(user);
            }

        } catch (Exception ex) {
            logger.debug("Unable to get Users group ", ex);
            throw new AdminException("Unable to get Users group ", ex);
        }
        return users;
    }

    public static void removeUserFromGroup(Long groupId, Long userId) throws AdminException {
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            // beginTransaction();
            Group group = (Group) session.load(Group.class, groupId);
            User user = (User) session.load(User.class, userId);
            user.getGroups().remove(group);
            // tx.commit();
        } catch (Exception ex) {
            logger.debug("Unable to remove User from group ", ex);

            throw new AdminException("Unable to remove User from group ", ex);
        }
    }

    public static void addUsersToGroup(Long groupId, Long[] userIds) throws AdminException {
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            // beginTransaction();
            Group group = session.load(Group.class, groupId);
            for (Long userId : userIds) {
                User user = session.load(User.class, userId);
                user.getGroups().add(group);
                session.save(user);
            }
            session.flush();
        } catch (Exception ex) {
            logger.debug("Unable to add Users to group ", ex);
            throw new AdminException("Unable to add Users to group ", ex);
        }
    }

    public static List searchSite(String siteKey) throws AdminException {

        Session session = null;
        List siteList = new ArrayList();

        try {
            session = PersistenceManager.getSession();

            siteKey = siteKey.toLowerCase();

            StringTokenizer st = new StringTokenizer(siteKey);
            String domainUrl = new String("");
            while (st.hasMoreTokens()) {
                domainUrl += st.nextToken();
            }

            siteKey = "%" + siteKey + "%";
            domainUrl = "%" + domainUrl + "%";

            String queryString = "select distinct s from " + SiteDomain.class.getName() + " d, " + Site.class.getName()
                    + " s where (d.site.id = s.id) "
                    + " and (lower(s.name) like :siteKey or lower(s.siteId) like :siteKey "
                    + " or lower(d.siteDbDomain) like :siteKey or lower(d.sitePath) like :siteKey "
                    + " or lower(d.siteDbDomain || d.sitePath) like :domainUrl)";
            Query query = session.createQuery(queryString);
            query.setParameter("siteKey", siteKey,StringType.INSTANCE);
            query.setParameter("domainUrl", domainUrl,StringType.INSTANCE);

            Iterator iterator = query.iterate();

            if (iterator != null && iterator.hasNext()) {
                while (iterator.hasNext()) {
                    siteList.add(iterator.next());
                }
            }

        } catch (Exception ex) {
            logger.debug("Unable to get site list from database ", ex);
            throw new AdminException("Unable to get site list from database", ex);
        }
        return siteList;
    }

    public static void deleteSite(Long id) throws AdminException {

        Session session = null;
        Transaction tx = null;
        Site site = null;

        try {
            session = PersistenceManager.getSession();
            site = (Site) session.load(Site.class, id);
            // beginTransaction();

            Iterator iterator;

            if (site.getGroups() != null) {
                iterator = site.getGroups().iterator();
                while (iterator.hasNext()) {
                    Group item = (Group) iterator.next();
                    if (item.getPermissions() != null) {
                        Iterator iter = item.getPermissions().iterator();
                        while (iter.hasNext()) {
                            GroupPermission gp = (GroupPermission) iter.next();
                            session.delete(gp);
                        }
                    }
                    session.delete(item);
                }

            }

            iterator = site.getSiteDomains().iterator();
            while (iterator.hasNext()) {
                SiteDomain item = (SiteDomain) iterator.next();
                session.delete(item);
            }

            if (site.getUserLanguages() != null) {
                iterator = site.getUserLanguages().iterator();
                while (iterator.hasNext()) {
                    Locale item = (Locale) iterator.next();
                    session.delete(item);
                }

            }

            if (site.getTranslationLanguages() != null) {
                iterator = site.getTranslationLanguages().iterator();
                while (iterator.hasNext()) {
                    Locale item = (Locale) iterator.next();
                    session.delete(item);
                }
            }

            if (site.getModuleInstances() != null) {
                iterator = site.getModuleInstances().iterator();
                while (iterator.hasNext()) {
                    ModuleInstance item = (ModuleInstance) iterator.next();
                    session.delete(item);
                }
            }

            session.delete(site);

            // tx.commit();

        } catch (Exception ex) {
            logger.debug("Unable to get site list from database ", ex);
            throw new AdminException("Unable to get site list from database", ex);
        }
    }

    public static Site getSite(String domain, String path) throws AdminException {
        Site site = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            Iterator iter = null;
            if (path == null) {
                String queryString = "select sd.site from " + SiteDomain.class.getName()
                        + " sd where sd.siteDbDomain=:siteDomain and sd.sitePath is null";
                Query query = session.createQuery(queryString);
                query.setParameter("siteDomain", domain,StringType.INSTANCE);
                iter = query.iterate();
            } else {
                String queryString = "select sd.site from " + SiteDomain.class.getName()
                        + " sd where sd.siteDbDomain=:siteDomain and sd.sitePath=:sitePath";
                Query query = session.createQuery(queryString);
                query.setParameter("siteDomain", domain,StringType.INSTANCE);
                query.setParameter("sitePath", path,StringType.INSTANCE);

                iter = query.iterate();
            }
            while (iter.hasNext()) {
                site = (Site) iter.next();
                break;
            }
        } catch (Exception ex) {
            logger.debug("Unable to get site from database ", ex);
            throw new AdminException("Unable to get site from database ", ex);
        }
        return site;
    }

    public static ModuleInstance getModuleInstance(Long id) throws AdminException {
        ModuleInstance moduleInstance = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            moduleInstance = (ModuleInstance) session.load(ModuleInstance.class, id);

        } catch (Exception ex) {
            logger.debug("Unable to get Module Instance from database ", ex);
            throw new AdminException("Unable to get Module Instance from database ", ex);
        }
        return moduleInstance;
    }

    public static List<ModuleInstance> getReferencedInstances(Long siteId) throws AdminException {
        try {
            String queryString = " from " + ModuleInstance.class.getName()
                    + " m where m.site.id != :siteId and m.realInstance is not null"
                    + " and m.realInstance.site.id = :siteId " + " order by m.site.name, m.moduleName, m.instanceName";
            Query query = PersistenceManager.getSession().createQuery(queryString);
            query.setLong("siteId", siteId);
            return query.list();
        } catch (Exception ex) {
            logger.debug("Unable to get Referenced Instances from database ", ex);
            throw new AdminException("Unable to get Referenced Instances from database ", ex);
        }
    }

    public static List getSitesToReference(Long siteId, String module) throws AdminException {
        List sites = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();

            String queryString = "select distinct mi.site from " + ModuleInstance.class.getName()
                    + " mi where mi.site.id != :siteId and mi.moduleName = :moduleName "
                    + " and mi.realInstance is null";
            Query query = session.createQuery(queryString);
            query.setParameter("siteId", siteId, LongType.INSTANCE);
            query.setParameter("moduleName", module,StringType.INSTANCE);

            sites = query.list();
        } catch (Exception ex) {
            logger.debug("Unable to get sites list from database ", ex);
            throw new AdminException("Unable to get sites list from database ", ex);
        } finally {
            try {
                if (session != null) {
                    // PersistenceManager.releaseSession(session);
                    session.close();
                }
            } catch (Exception ex1) {
                logger.warn("releaseSession() failed ", ex1);
            }
        }
        return sites;
    }

    public static void updateSiteInstance(ModuleInstance instance) throws AdminException {
        Session sess = null;
        try {
            sess = PersistenceManager.getSession();
            // beginTransaction();

            sess.update(instance);
            // tx.commit();
        } catch (Exception ex) {

            logger.debug("Unable to update ModuleInstance ", ex);
            throw new AdminException("Unable to update ModuleInstance", ex);
        }
    }

    public static void editSiteInstances(Site site, List otherInstances) throws AdminException {
        Session sess = null;
        try {
            sess = org.digijava.kernel.persistence.PersistenceManager.getSession();
            // beginTransaction();

            Iterator iter = otherInstances.iterator();
            while (iter.hasNext()) {
                ModuleInstance item = (ModuleInstance) iter.next();
                sess.update(item);
            }

            iter = site.getModuleInstances().iterator();
            while (iter.hasNext()) {
                ModuleInstance item = (ModuleInstance) iter.next();
                if (item.getSite() == null) {
                    // Remove permissions, assigned on this instance
                    /**
                     * @todo implement permission removing here
                     */
                    iter.remove();
                    sess.delete(item);
                }
            }

            sess.saveOrUpdate(site);
            // tx.commit();
        } catch (Exception ex) {
            logger.debug("Unable to modify site ", ex);
            throw new AdminException("Unable to modify site", ex);
        }
    }

    public static List getReferencedGroups(Long siteId) throws AdminException {

        Session session = null;
        List groupList = new ArrayList();

        try {
            session = PersistenceManager.getSession();
            StringBuffer buff = new StringBuffer();
            boolean passed = false;
            Site site = (Site) session.load(Site.class, siteId);

            Iterator iter = site.getModuleInstances().iterator();
            while (iter.hasNext()) {
                ModuleInstance item = (ModuleInstance) iter.next();
                if (item.getRealInstance() == null) {
                    if (passed) {
                        buff.append(",");
                    } else {
                        passed = true;
                    }
                    buff.append("'").append(item.getModuleInstanceId()).append("'");
                }
            }

            if (passed) {
                String queryString = "select distinct p.group from " + GroupPermission.class.getName()
                        + " p where p.group.site.id != :siteId and p.permissionType = :permissionType "
                        + " and p.targetName in (" + buff.toString() + ")";
                Query query = session.createQuery(queryString);
                query.setLong("siteId", siteId);
                query.setInteger("permissionType", new Integer(GroupPermission.MODULE_INSTANCE_PERMISSION));

                groupList = query.list();
            }
        } catch (Exception ex) {
            logger.debug("Unable to get group list from database ", ex);
            throw new AdminException("Unable to get group list from database", ex);
        }
        return groupList;
    }

    public static void updateUser(User user) throws AdminException {

        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();

            user.updateLastModified();

            //beginTransaction();

            session.update(user);

            // tx.commit();

        } catch (Exception ex) {
            logger.debug("Unable to update user information into database", ex);
            throw new AdminException("Unable to update user information into database", ex);
        }
    }

    public static List getInheritedPermissions(Long groupId) throws AdminException {
        Session session = null;
        List result = null;
        try {
            session = PersistenceManager.getSession();
            result = getInheritedPermissions(groupId, session);

        } catch (Exception ex) {
            logger.debug("Unable to get permissions from database", ex);
            throw new AdminException("Unable to get permissions from database", ex);
        }
        return result;
    }

    private static List getInheritedPermissions(Long groupId, Session session) throws Exception {
        List permissions = null;

        String queryString = "from " + Group.class.getName() + " gr where "
                + " gr.site.inheritSecurity = :inheritSecurity and gr.parentId = :parentId";

        Query query = session.createQuery(queryString);
        query.setParameter("inheritSecurity", Boolean.TRUE, BooleanType.INSTANCE);
        query.setParameter("parentId", groupId, LongType.INSTANCE);

        Iterator iter = query.iterate();
        while (iter.hasNext()) {
            Group group = (Group) iter.next();
            permissions.addAll(group.getPermissions());
            permissions.addAll(getInheritedPermissions(group.getId(), session));
        }

        return permissions;
    }

    public static List getLocales() throws AdminException {

        Session session = null;
        List locales = null;

        try {
            session = PersistenceManager.getSession();
            locales = session.createQuery("from " + Locale.class.getName()).list();
        } catch (Exception ex) {
            logger.debug("Unable to get locales list from database", ex);
            throw new AdminException("Unable to get locales list from database", ex);
        }
        return locales;
    }

    public static void updateLocale(Locale locale) throws AdminException {

        Session session = null;

        try {
            session = PersistenceManager.getSession();
            Locale oldLoc = (Locale) session.get(Locale.class, locale.getCode());
            oldLoc.setAvailable(locale.isAvailable());
            oldLoc.setLeftToRight(locale.getLeftToRight());

            // beginTransaction();
            if (locale.getMessageLangKey() == null) {
                locale.setMessageLangKey("ln:" + locale.getCode());
            }

            session.update(oldLoc);

            // tx.commit();

        } catch (Exception ex) {

            logger.debug("Unable to update locale information into database", ex);
            throw new AdminException("Unable to update locale information into database", ex);
        }

    }

    public static List getCommonInstances() throws AdminException {
        List commonInstances = new ArrayList();
        Session session = null;
        Query q = null;
        try {
            session = PersistenceManager.getSession();
            q = session.createQuery(
                    "from " + ModuleInstance.class.getName() + " m where m.site is null" + " order by m.moduleName");

            commonInstances = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get sites list from database ", ex);
            throw new AdminException("Unable to get sites list from database ", ex);
        }
        return commonInstances;
    }

    public static void editCommonInstances(List newInstances) throws AdminException {
        Session session = null;
        try {
            session = org.digijava.kernel.persistence.PersistenceManager.getSession();
            // beginTransaction();

            List commonInstances = getCommonInstances();

            HashMap oldInstancesMap = new HashMap();
            Iterator iter = commonInstances.iterator();
            while (iter.hasNext()) {
                ModuleInstance instance = (ModuleInstance) iter.next();
                oldInstancesMap.put(instance.getModuleInstanceId(), instance);
            }

            HashMap newInstancesMap = new HashMap();
            iter = newInstances.iterator();
            while (iter.hasNext()) {
                ModuleInstance instance = (ModuleInstance) iter.next();
                newInstancesMap.put(instance.getModuleInstanceId(), instance);
            }

            if (newInstancesMap.size() <= oldInstancesMap.size()) {
                iter = commonInstances.iterator();
                while (iter.hasNext()) {
                    ModuleInstance oldInstance = (ModuleInstance) iter.next();
                    ModuleInstance newInstance = (ModuleInstance) newInstancesMap
                            .get(oldInstance.getModuleInstanceId());

                    if (newInstance != null) {
                        session.update(newInstance);
                    } else {
                        session.delete(oldInstance);
                    }
                }
            } else {
                iter = newInstances.iterator();
                while (iter.hasNext()) {
                    ModuleInstance newInstance = (ModuleInstance) iter.next();
                    ModuleInstance oldInstance = (ModuleInstance) oldInstancesMap
                            .get(newInstance.getModuleInstanceId());

                    if (oldInstance != null) {
                        session.update(newInstance);
                    } else {
                        session.save(newInstance);
                    }
                }

            }

            // tx.commit();
        } catch (Exception ex) {
            logger.debug("Unable to modify site ", ex);
            throw new AdminException("Unable to modify site", ex);
        }
    }

    public static List<KeyValue> getPossibleValues(String tableName) {
        List<KeyValue> ret = new ArrayList<>();

        if (tableName == null || tableName.length() == 0)
            return ret;

        List<Object[]> ls = PersistenceManager.getSession().createNativeQuery("select id, value from " + tableName).list();
        for (Object[] obj : ls) {
            KeyValue keyValue = new KeyValue(PersistenceManager.getString(obj[0]),
                    PersistenceManager.getString(obj[1]));
            ret.add(keyValue);
        }
        return ret;
    }
    public static void updateGlobalSetting(String name, String value) throws AMPException {
        Session session = null;
        String qryStr = null;
        Query qry = null;
        session = PersistenceManager.getSession();
        qryStr = "select gs from " + AmpGlobalSettings.class.getName() + " gs where gs.globalSettingsName = :globalSettingsName ";
        qry = session.createQuery(qryStr);
        qry.setParameter("globalSettingsName", name,StringType.INSTANCE);
        AmpGlobalSettings ags = (AmpGlobalSettings) qry.list().get(0);
        updateGlobalSetting(ags, value);
    }
    public static void updateGlobalSetting(Long id, String value) throws AMPException {

        Session session = null;
        String qryStr = null;
        Query qry = null;
        session = PersistenceManager.getSession();
        qryStr = "select gs from " + AmpGlobalSettings.class.getName() + " gs where gs.globalId = :id ";
        qry = session.createQuery(qryStr);
        qry.setParameter("id", id, LongType.INSTANCE);

        AmpGlobalSettings ags = (AmpGlobalSettings) qry.list().get(0);

        updateGlobalSetting(ags, value);
    }

    public static void updateGlobalSetting(AmpGlobalSettings ags, String value) throws AMPException {
        String criterion = ags.getGlobalSettingsPossibleValues();
        if (criterion != null && criterion.startsWith("t_")) {
            boolean isValid = ConfigHelper.validateGlobalSetting(ags, value);
            if (!isValid) {
                throw new AMPException(ags.getGlobalSettingsName());
            }
        }

        ags.setGlobalSettingsValue(value);

    }

}
