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

package org.digijava.module.editor.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DbUtil {

    private static Logger logger = Logger.getLogger(DbUtil.class);

    /**
     * Get editor item by key from database
     *
     * @param key
     * @return
     * @throws EditorException
     */
    public static List getEditorList(String siteId, String editorKey,
                                     String language) throws
        EditorException {

        Session session = null;
        List items = new ArrayList();
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("from " +
                                          Editor.class.getName() +
                                          " e where (e.siteId=:siteId) and (e.editorKey=:editorKey) and (e.language!=:language)");

            q.setParameter("siteId", siteId, Hibernate.STRING);
            q.setParameter("editorKey", editorKey, Hibernate.STRING);
            q.setParameter("language", language, Hibernate.STRING);

            items = q.list();
        }
        catch (Exception ex) {
            logger.debug("Unable to get editor item from database ", ex);
            throw new EditorException("Unable to get editor item from database",
                                      ex);
        }

        return items;
    }

    public static void deleteEditor(Editor ed) throws
        EditorException {
        Transaction tx = null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();

            tx = session.beginTransaction();
            session.delete(ed);
            tx.commit();
        }
        catch (Exception ex) {

            logger.debug("Unable to delete editor", ex);
            throw new EditorException(
                "Unable to delete editor", ex);
        }
    }

    /**
     * Get editor list for specified site
     *
     * @param key
     * @return
     * @throws EditorException
     */
    
    public static List getSiteEditorList(String siteId) throws
        EditorException {

        Session session = null;
        List items = new ArrayList();
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("from " +
                                          Editor.class.getName() +
                " e where (e.siteId=:siteId) order by e.orderIndex");

            q.setParameter("siteId", siteId, Hibernate.STRING);

            List result = q.list();
            if (result != null && (result.size() != 0)) {
                items = q.list();
            }
            else {
                return null;
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get editor list from database ", ex);
            throw new EditorException("Unable to get editor item from database",
                                      ex);
        }

        return items;
    }

    public static List getSiteEditorList(String siteId,
                                         String lang,
                                         String groupName) throws
        EditorException {

        Session session = null;
        List items = new ArrayList();
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("from " +
                                          Editor.class.getName() +
                " e where e.siteId=:siteId and e.groupName=:groupName " +
                "and e.language=:language " +
                "order by e.orderIndex");

            q.setParameter("siteId", siteId, Hibernate.STRING);
            q.setParameter("groupName", groupName, Hibernate.STRING);
            q.setParameter("language", lang, Hibernate.STRING);

            List result = q.list();
            if (result != null && (result.size() != 0)) {
                items = q.list();
            }
            else {
                return null;
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get editor list from database ", ex);
            throw new EditorException("Unable to get editor item from database",
                                      ex);
        }

        return items;
    }

    /**
     * Get editor item by siteId key and language code from database
     *
     * @param key
     * @return
     * @throws EditorException
     */
    public static Editor getEditor(String siteId, String editorKey,
                                   String language) throws
        EditorException {

        Session session = null;
        Editor item = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Editor editorKeyObj = new Editor();
            editorKeyObj.setSiteId(siteId);
            editorKeyObj.setEditorKey(editorKey);
            editorKeyObj.setLanguage(language);

            try {
                   System.out.println("try");
                item = (Editor) session.load(Editor.class, editorKeyObj);
                  System.out.println("Item:"+item);
            }
            catch (ObjectNotFoundException ex1) {
                logger.error("DbUtil:getEditor:Unable to get Editor item", ex1);
                return item = null;

            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get editor item from database ", ex);
            item = null;


        }
        return item;
    }

    




    public static Editor getEditor(String siteId, int orderIndex) throws
        EditorException {

        Session session = null;
        Editor item = new Editor();
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("from " +
                                          Editor.class.getName() +
                                          " e where (e.siteId=:siteId) and " +
                                          "(e.orderIndex=:orderIndex)");

            q.setParameter("siteId", siteId, Hibernate.STRING);
            q.setParameter("orderIndex", new Integer(orderIndex),
                           Hibernate.INTEGER);

            List result = q.list();
            if (result != null && (result.size() != 0)) {
                Iterator iter = result.iterator();
                while (iter.hasNext()) {
                    item = (Editor) iter.next();
                    break;
                }
            }
            else {
                item = null;
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get editor item from database ", ex);
            throw new EditorException("Unable to get editor item from database",
                                      ex);
        }

        return item;
    }

    /**
     * Get editor item by key and language code from database
     *
     * @param key
     * @return
     * @throws EditorException
     */
    public static Editor getEditor(String editorKey, String language) throws
        EditorException {

        Session session = null;
        Editor item = new Editor();
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("from " +
                                          Editor.class.getName() +
                " e where (e.editorKey=:editorKey) and (e.language=:language)");

            q.setParameter("editorKey", editorKey, Hibernate.STRING);
            q.setParameter("language", language, Hibernate.STRING);

            List result = q.list();
            if (result != null && (result.size() != 0)) {
                Iterator iter = result.iterator();
                while (iter.hasNext()) {
                    item = (Editor) iter.next();
                    break;
                }
            }
            else {
                item = null;
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get editor item from database ", ex);
            throw new EditorException("Unable to get editor item from database",
                                      ex);
        }

        return item;
    }

   
    
    
    /**
     * Update editor
     *
     * @param editor
     * @throws EditorException
     */
    public static void updateEditor(Editor editor) throws EditorException {

        Session session = null;
        Transaction tx = null;
        try {

            session = PersistenceManager.getRequestDBSession();
            tx = session.beginTransaction();
            session.update(editor);
            tx.commit();
        }
        catch (Exception ex) {
            logger.debug("Unable to update editor information into database",
                         ex);

            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            throw new EditorException(
                "Unable to update editor information into database", ex);
        }
    }

    public static void updateEditorList(Collection editors) throws
        EditorException {

        Session session = null;
        Transaction tx = null;
        try {

            session = PersistenceManager.getRequestDBSession();
            tx = session.beginTransaction();
            Iterator it = editors.iterator();
            while (it.hasNext()) {
                Editor editor = (Editor) it.next();
                session.update(editor);
            }
            tx.commit();
        }
        catch (Exception ex) {
            logger.debug("Unable to update editor information into database",
                         ex);

            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            throw new EditorException(
                "Unable to update editor information into database", ex);
        }
    }

    /**
     * Save editor
     *
     * @param editor
     * @throws EditorException
     */
    public static void saveEditor(Editor editor) throws EditorException {

        Session session = null;
        Transaction tx = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            tx = session.beginTransaction();
            session.save(editor);
            tx.commit();
        }
        catch (Exception ex) {
            logger.debug("Unable to save editor information into database",
                         ex);

            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            throw new EditorException(
                "Unable to save editor information into database", ex);
        }
    }

    /**
     *
     * @param user
     * @param languageCode
     * @param url
     * @param editorKey
     * @param title
     * @param body
     * @param notice
     * @param request
     * @return
     */
    public static Editor createEditor(User user, String languageCode,
                                      String url, String editorKey,
                                      String title,
                                      String body, String notice,
                                      HttpServletRequest request) {

        Editor editor = new Editor();

        // get module instance
        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);

        editor.setSiteId(moduleInstance.getSite().getSiteId());
        editor.setEditorKey(editorKey);
        editor.setUrl(url);
        editor.setLanguage(languageCode);

        editor.setTitle(title);
        editor.setBody(body);
        editor.setNotice(notice);

        editor.setLastModDate(null);
        editor.setUser(user);
        editor.setCreationIp(RequestUtils.getRemoteAddress(request));

        return editor;
    }

    /**
     *
     * @param siteId
     * @param editorKey
     * @param language
     * @return
     * @throws EditorException
     */
    public static String getEditorBody(String siteId, String editorKey,
                                             String language) throws
        EditorException {

        Session session = null;
        String body = null;

        try {
            session = PersistenceManager.getRequestDBSession();

            Query q = session.createQuery(
                "select e.body from " +
                Editor.class.getName() + " e " +
                " where (e.siteId=:siteId) and (e.editorKey=:editorKey) and (e.language=:language)");

          //  q.setCacheable(true);
            q.setParameter("siteId", siteId, Hibernate.STRING);
            q.setParameter("editorKey", editorKey, Hibernate.STRING);
            q.setParameter("language", language, Hibernate.STRING);

            List result = q.list();
            if (result != null && (result.size() != 0)) {
                Iterator iter = result.iterator();
                while (iter.hasNext()) {
                    String item = (String)iter.next();
                    body =  item;
                    break;
                }
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get editor from database", ex);
            throw new EditorException(
                "Unable to get editor from database", ex);
        }

        return body;
    }

    /**
     *
     * @param siteId
     * @param editorKey
     * @param language
     * @return
     * @throws EditorException
     */
    public static String getEditorTitle(String siteId, String editorKey,
                                             String language) throws
        EditorException {

        Session session = null;
        String title = "";

        try {
            session = PersistenceManager.getRequestDBSession();

            Query q = session.createQuery(
                "select e.title from " +
                Editor.class.getName() + " e, " +
                " where (e.siteId=:siteId) and (e.editorKey=:editorKey) and (e.language=:language)");

            q.setCacheable(true);
            q.setParameter("siteId", siteId, Hibernate.STRING);
            q.setParameter("editorKey", editorKey, Hibernate.STRING);
            q.setParameter("language", language, Hibernate.STRING);

            List result = q.list();
            if (result != null && (result.size() != 0)) {
                Iterator iter = result.iterator();
                while (iter.hasNext()) {
                    Object item = iter.next();
                    if( item != null ) {
                        title = new String( (String) item);
                    }
                    break;
                }
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get editor title from database", ex);
            throw new EditorException(
                "Unable to get editor title from database", ex);
        }

        return title;
    }

}
