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

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.exception.EditorException;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
    @SuppressWarnings("unchecked")
    public static List<Editor> getEditorList(Site site, String editorKey, String language) throws EditorException {

        Session session = null;
        List<Editor> items = new ArrayList<Editor>();
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("from " +
                                          Editor.class.getName() +
                                          " e where (e.siteId=:siteId) and (e.editorKey=:editorKey) and (e.language!=:language)");

            q.setParameter("siteId", site.getSiteId(), StringType.INSTANCE);
            q.setParameter("editorKey", editorKey,StringType.INSTANCE);
            q.setParameter("language", language,StringType.INSTANCE);

            items = q.list();
        }
        catch (Exception ex) {
            logger.debug("Unable to get editor item from database ", ex);
            throw new EditorException("Unable to get editor item from database",
                                      ex);
        }

        return items;
    }

    /**
     * Returns editors of all language with specified key and siteId
     * Useful when one wants to remove or update some other bean, and its editors in all language.
     * @param editorKey
     * @param siteId
     * @return
     * @throws EditorException
     */
    @SuppressWarnings("unchecked")
    public static List<Editor> getEditorList(String editorKey, Site site) throws EditorException {

        Session session = null;
        List<Editor> items = new ArrayList<Editor>();
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("from "
                            + Editor.class.getName()
                            + " e where (e.siteId=:siteId) and (e.editorKey=:editorKey)");

            q.setParameter("siteId", site.getSiteId(),StringType.INSTANCE);
            q.setParameter("editorKey", editorKey,StringType.INSTANCE);

            items = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get editor item from database ", ex);
            throw new EditorException(
                    "Unable to get editor item from database", ex);
        }

        return items;
    }    
    
    public static List<Editor> getEditorList(String editorKey, Site site, Session session) throws EditorException {
        List<Editor> items = new ArrayList<Editor>();
        try {
            Query q = session.createQuery("from "+ Editor.class.getName()+ " e where (e.siteId=:siteId) and (e.editorKey=:editorKey)");
            q.setParameter("siteId", site.getSiteId(),StringType.INSTANCE);
            q.setParameter("editorKey", editorKey,StringType.INSTANCE);

            items = q.list();
        } catch (Exception ex) {
            logger.debug("Unable to get editor item from database ", ex);
            throw new EditorException(
                    "Unable to get editor item from database", ex);
        }

        return items;
    }    
    
    public static void deleteEditor(Editor ed) throws
        EditorException {
        Transaction tx = null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();

//beginTransaction();
            session.delete(ed);
            //tx.commit();
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
    
    public static List<Editor> getSiteEditorList(Site site) throws
        EditorException {

        Session session = null;
        List<Editor> items = new ArrayList<Editor>();
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("from " + Editor.class.getName() +
                " e where (e.siteId=:siteId) order by e.orderIndex");

            q.setParameter("siteId", site.getSiteId(),StringType.INSTANCE);

            @SuppressWarnings("unchecked")
            List<Editor> result = q.list();
            if (result != null && (result.size() != 0)) {
                items = result;
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

    public static List<Editor> getSiteEditorList(Site site, String lang,String groupName) throws EditorException {

        Session session = null;
        List<Editor> items = new ArrayList<Editor>();
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("from " +
                                          Editor.class.getName() +
                " e where e.siteId=:siteId and e.groupName=:groupName " +
                "and e.language=:language " +
                "order by e.orderIndex");

            q.setParameter("siteId", site.getSiteId(),StringType.INSTANCE);
            q.setParameter("groupName", groupName,StringType.INSTANCE);
            q.setParameter("language", lang,StringType.INSTANCE);

            @SuppressWarnings("unchecked")
            List<Editor> result = q.list();
            if (result != null && (result.size() != 0)) {
                items = result;
            }
            else {
                return null;
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get editor list from database ", ex);
            throw new EditorException("Unable to get editor item from database", ex);
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
    public static Editor getEditor(Site site, String editorKey,String language) throws EditorException {

        Session session = null;
        Editor item = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            try {
                Query q = session.createQuery("from " + Editor.class.getName() +" e where e.siteId=:siteId and e.editorKey=:editorKey");
                    q.setParameter("siteId", site.getSiteId(),StringType.INSTANCE);
                    q.setParameter("editorKey", editorKey,StringType.INSTANCE);
                    //q.setString("language", language);
                    @SuppressWarnings("unchecked")
                    Collection<Editor> edits=q.list();
                for (Editor editor : edits) {
                    if (editor.getLanguage().equalsIgnoreCase(language) && !"".equalsIgnoreCase(editor.getBody())) {
                        item = editor;
                        break;
                    }
                }
            }
            catch (ObjectNotFoundException ex1) {
                logger.error("DbUtil:getEditor:Unable to get Editor item", ex1);
            }
        }
        catch (Exception ex) {
            logger.error("Unable to get editor item from database ", ex);
            item = null;
        }
        return item;
    }
    
    public static Editor getEditorForUpdate(String siteId, String editorKey,String language) throws EditorException {

        Session session = null;
        Editor item = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            try {
                Query q = session.createQuery("from " + Editor.class.getName() +" e where e.siteId=:siteId and e.editorKey=:editorKey and e.language=:language");
                    q.setParameter("siteId", siteId,StringType.INSTANCE);
                    q.setParameter("editorKey", editorKey,StringType.INSTANCE);
                    q.setParameter("language", language,StringType.INSTANCE);
                    item = (Editor)q.uniqueResult();
            }
            catch (ObjectNotFoundException ex1) {
                logger.error("DbUtil:getEditor:Unable to get Editor item", ex1);
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get editor item from database ", ex);
            item = null;
        }
        return item;
    }


    public static Editor getEditor(Site site, int orderIndex) throws EditorException {

        Session session = null;
        Editor item = new Editor();
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("from " +
                                          Editor.class.getName() +
                                          " e where (e.siteId=:siteId) and " +
                                          "(e.orderIndex=:orderIndex)");

            q.setParameter("siteId", site.getSiteId(),StringType.INSTANCE);
            q.setParameter("orderIndex", orderIndex, IntegerType.INSTANCE);

            @SuppressWarnings("unchecked")
            List<Editor> result = q.list();
            if (result != null && (result.size() != 0)) {
                for (Editor editor : result) {
                    item = editor;
                    break;
                }
            }
            else {
                item = null;
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get editor item from database ", ex);
            throw new EditorException("Unable to get editor item from database", ex);
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
    public static Editor getEditor(String editorKey, String language) throws EditorException {

        Session session = null;
        Editor item = new Editor();
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("from " + Editor.class.getName() + 
                " e where (e.editorKey=:editorKey) and (e.language=:language)");

            q.setParameter("editorKey", editorKey,StringType.INSTANCE);
            q.setParameter("language", language,StringType.INSTANCE);

            @SuppressWarnings("unchecked")
            List<Editor> result = q.list();
            if (result != null && (result.size() != 0)) {
                item = result.get(0);
            }
            else {
                item = null;
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get editor item from database ", ex);
            throw new EditorException("Unable to get editor item from database", ex);
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
//beginTransaction();
            session.update(editor);
            //tx.commit();
        }
        catch (Exception ex) {
            logger.debug("Unable to update editor information into database", ex);

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

    public static void updateEditorList(Collection<Editor> editors) throws
        EditorException {

        Session session = null;
        Transaction tx = null;
        try {

            session = PersistenceManager.getRequestDBSession();
//beginTransaction();
            for (Editor editor : editors) {
                session.update(editor);
            }
            //tx.commit();
        }
        catch (Exception ex) {
            logger.debug("Unable to update editor information into database", ex);

            throw new EditorException(
                "Unable to update editor information into database", ex);
        }
    }


    /**
     * Save editor
     *
     * @param editor
     */
    public static void saveEditor(Editor editor) {
        PersistenceManager.getSession().save(editor);
    }

    /**
     * Creates editor in db.
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
        ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(request);
        // TODO: not sure if moduleInstance is now always false and we can simply replace with TLSUtils.getSite()  
        Site site = moduleInstance == null ? TLSUtils.getSite() : moduleInstance.getSite();

        editor.setSite(site);
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
     * Retrieves editor body text. Gives priority to chosen language, else returns English, else returns any, else returns null
     * @param siteId
     * @param editorKey
     * @param language
     * @return
     * @throws EditorException
     */
    public static String getEditorBody(Site site, String editorKey, String language) throws EditorException
    {
        String bodyEn = null; // translation in English
        String bodyOther = null; // translation in any language which is not English and is not the requested one
    
        String stat = String.format("SELECT body, language FROM dg_editor WHERE site_id = '%s' AND editor_key = '%s'", site.getSiteId(), editorKey);
        List<Object[]> res = PersistenceManager.getSession().createNativeQuery(stat).list();
        for(Object[] entry:res)
        {
            String editorBody = PersistenceManager.getString(entry[0]);
            String editorLanguage = PersistenceManager.getString(entry[1]);
                
            if ("".equals(editorBody))
                continue; // ignore this one
                
            if (editorLanguage.equalsIgnoreCase(language))
                return editorBody;
            
            if (editorLanguage.equalsIgnoreCase("en"))
                bodyEn = editorBody;
            else
                bodyOther = editorBody;
        }
        if (bodyEn != null)
            return bodyEn;
        return bodyOther;
    }
    
    /**
     * Retrieves editor body text.
     * @param siteId
     * @param editorKey
     * @param language
     * @return
     * @throws EditorException
     */
    public static String getEditorBodyEmptyInclude(Site site, String editorKey, String language) throws EditorException {

        Session session = null;
        String body = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery(
                "select e.body from " + Editor.class.getName() + " e " +
                " where (e.siteId=:siteId) and (e.editorKey=:editorKey) and e.language=:language");
          //  q.setCacheable(true);
            q.setParameter("siteId", site.getSiteId(),StringType.INSTANCE);
            q.setParameter("editorKey", editorKey,StringType.INSTANCE);
            q.setParameter("language", language,StringType.INSTANCE);
            body=(String)q.uniqueResult();
        }
        catch (Exception ex) {
            logger.debug("Unable to get editor from database", ex);
            throw new EditorException("Unable to get editor from database", ex);
        }

        return body;
    }
    
    /**
     * Returns editor body text but strips out all HTML tags.
     * Uses {@link #getEditorBody(String, String, String)} method to 
     * retrieve initial body text and then uses regexps to strip tags.
     * Note that to strip all possible HTML tags batch processing is used to 
     * avoid stack overflow problems which happens when processing too huge regexp.
     * Solution copied from AMP v2 Help+Lucene integration solution.
     * Check AMP-9328 for more details. 
     * @param siteId
     * @param editorKey
     * @param language
     * @return
     * @throws EditorException
     */
    public static String getEditorBodyFiltered(Site site, String editorKey, String language) throws EditorException {
        String body = getEditorBody(site, editorKey, language);
        return DgUtil.cleanHtmlTags(body);
    }

    /**
     * Retrieves editor title text.
     * @param siteId
     * @param editorKey
     * @param language
     * @return title text or null.
     * @throws EditorException
     */
    public static String getEditorTitle(Site site, String editorKey,String language) throws EditorException {

        Session session = null;
        String title = "";

        try {
            session = PersistenceManager.getRequestDBSession();

            Query q = session.createQuery(
                "select e.title from " +
                Editor.class.getName() + " e, " +
                " where (e.siteId=:siteId) and (e.editorKey=:editorKey) and (e.language=:language)");

            q.setCacheable(true);
            q.setParameter("siteId", site.getSiteId(),StringType.INSTANCE);
            q.setParameter("editorKey", editorKey,StringType.INSTANCE);
            q.setParameter("language", language,StringType.INSTANCE);

            @SuppressWarnings("unchecked")
            List<String> result = q.list();
            if (result != null && (result.size() != 0)) {
                for (String editorTitle : result) {
                    if (editorTitle != null){
                        title = editorTitle;
                        break;
                    }
                }
//                Iterator iter = result.iterator();
//                while (iter.hasNext()) {
//                    Object item = iter.next();
//                    if( item != null ) {
//                        title = new String( (String) item);
//                    }
//                    break;
//                }
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
