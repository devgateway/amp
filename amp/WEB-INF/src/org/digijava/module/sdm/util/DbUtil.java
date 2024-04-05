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

package org.digijava.module.sdm.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.common.util.ImageInfo;
import org.digijava.module.sdm.dbentity.Sdm;
import org.digijava.module.sdm.dbentity.SdmItem;
import org.digijava.module.sdm.exception.SDMException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
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
     * create new SDM document
     *
     * @param document
     * @throws SDMException
     */
    public static Sdm createNewDocument(String title, String siteId,
                                        String instanceId) throws
        SDMException {
        Transaction tx = null;
        Session session = null;
        Sdm document = new Sdm();
        try {
            session = PersistenceManager.getRequestDBSession();
//beginTransaction();
            document.setSiteId(siteId);
            document.setInstanceId(instanceId);
            document.setName(title);

            session.save(document);
            //tx.commit();
        }
        catch (Exception ex) {
            logger.debug("Unable to create new sdm document into database", ex);

            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            throw new SDMException(
                "Unable to create new sdm document into database", ex);
        }

        return document;
    }

    /**
     * add updtae new Item in SDm document
     *
     * @param document
     * @throws SDMException
     */
    public static void addUpdateItem(Sdm document, SdmItem item) throws
        SDMException {

        if (item.getParagraphOrder() == null) {
            if (document.getItems() != null) {
                item.setParagraphOrder(new Long(getLastParagraph(document,
                    item.getParagraphOrder()) + 1));
                document.getItems().add(item);
            }
            else {
                HashSet items = new HashSet();
                item.setParagraphOrder(new Long(0));
                //item.setDocument(document);
                items.add(item);
                document.setItems(items);
            }

            updateDocument(document);
        }
        else {
            updateItem(item);
        }
    }

    /**
     * get SDM document list from database by given site and instance id
     *
     * @param siteId
     * @param instanceId
     * @return
     * @throws SDMException
     */
    public static List getDocuments(String siteId, String instanceId) throws
        SDMException {

        Session session = null;
        Query q = null;
        List list = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = null;

            queryString =
                "select new org.digijava.module.sdm.form.SdmInfo(h.id,h.name) from  " +
                Sdm.class.getName() + " h " +
                " where (h.siteId=:siteId) and (h.instanceId=:instanceId)";

            q = session.createQuery(queryString);

            q.setParameter("siteId", siteId, StringType.INSTANCE);
            q.setParameter("instanceId", instanceId, StringType.INSTANCE);

            list = q.list();
            if (list.size() == 0) {
                list = null;
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get documents list from database", ex);
            throw new SDMException(
                "Unable to get documents list from database", ex);
        }

        return list;
    }

    /**
     * move item one step up and update into database
     *
     * @param id
     * @return
     * @throws SDMException
     */
    public static boolean moveItemUp(Sdm document, Long paragraphId) throws
        SDMException {

        return swapItem(document, paragraphId,
                        new Long(getPrevParagraph(document, paragraphId)));
    }

    /**
     * move item one step down and update into database
     *
     * @param id
     * @return
     * @throws SDMException
     */
    public static boolean moveItemDown(Sdm document, Long paragraphId) throws
        SDMException {
        return swapItem(document, paragraphId,
                        new Long(getNextParagraph(document, paragraphId)));

    }

    /**
     *
     * @return
     */
    public static int getNextParagraph(Sdm document, Long paragraphId) {

        Iterator<SdmItem> iter = document.getItems().iterator();
        while (iter.hasNext()) {
            SdmItem item = iter.next();
            if (item.getParagraphOrder().longValue() == paragraphId.longValue()) {
                return ( (SdmItem) iter.next()).getParagraphOrder().intValue();
            }
        }
        return -1;
    }

    /**
     *
     * @return
     */
    public static int getPrevParagraph(Sdm document, Long paragraphId) {
        SdmItem prevItem = null;
        Iterator<SdmItem> iter = document.getItems().iterator();
        while (iter.hasNext()) {
            SdmItem item = iter.next();
            if (item.getParagraphOrder().longValue() == paragraphId.longValue()) {
                return prevItem.getParagraphOrder().intValue();
            }
            prevItem = item;
        }
        return -1;
    }

    /**
     *
     * @return
     */
    public static int getLastParagraph(Sdm document, Long paragraphId) {
        SdmItem prevItem = null;
        Iterator<SdmItem> iter = document.getItems().iterator();
        while (iter.hasNext()) {
            SdmItem item = iter.next();
            prevItem = item;
        }
        return (prevItem != null) ? prevItem.getParagraphOrder().intValue() : 0;
    }

    /**
     *
     * @param item1
     * @param item2
     * @throws SDMException
     */
    public static boolean swapItem(Sdm document, Long id1, Long id2) throws
        SDMException {
        Transaction tx = null;
        Session session = null;
        Long paragraphId = null;
        boolean swap = false;
        try {
            session = PersistenceManager.getRequestDBSession();

//beginTransaction();
            //item1 = (SdmItem) session.load(SdmItem.class, item1);
            //item2 = (SdmItem) session.load(SdmItem.class, item2);
            Iterator<SdmItem> iter = document.getItems().iterator();
            while (iter.hasNext()) {
                SdmItem item = iter.next();
                if (item.getParagraphOrder().equals(id1)) {
                    item.setParagraphOrder(id2);
                    session.update(item);
                }
                else {
                    if (item.getParagraphOrder().equals(id2)) {
                        item.setParagraphOrder(id1);
                        session.update(item);
                    }
                }
            }
            session.update(document);

            //tx.commit();

            swap = true;

        }
        catch (Exception ex) {
            logger.debug("Unable to update information into database", ex);
            throw new SDMException("Unable to update information into database",
                                   ex);
        }

        return swap;
    }

    /**
     *
     * @param item
     * @throws SDMException
     */
    public static void deleteItem(SdmItem item) throws
        SDMException {
        Transaction tx = null;
        Session session = null;
        Long paragraphId = null;
        boolean swap = false;
        try {
            session = PersistenceManager.getRequestDBSession();

//beginTransaction();

            Sdm sdm = item.getDocument();

            Iterator<SdmItem> iter = sdm.getItems().iterator();
            while (iter.hasNext()) {
                SdmItem iterItem = iter.next();
                if (iterItem.getParagraphOrder().equals(item.getParagraphOrder())) {
                    iter.remove();
                }
            }
            //item.getDocument().getItems().remove(item);
            session.delete(item);
            session.update(sdm);

            //tx.commit();
        }
        catch (Exception ex) {
            logger.debug("Unable to delete intem", ex);
            throw new SDMException("Unable to delete intem", ex);
        }
    }

    /**
     * get document by id
     *
     * @param id
     * @return
     * @throws SDMException
     */
    public static Sdm getDocument(Long id) throws SDMException {

        Sdm document = null;
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            document = (Sdm) session.load(Sdm.class, id);
        }
        catch (Exception ex) {
            logger.debug("Unable to get Document from Database", ex);
            throw new SDMException("Unable to get Document from Database", ex);
        }

        return document;
    }
    
    public static Sdm getDocument(Long id,Session session) throws SDMException {

        Sdm document = null;
        try {
            document = (Sdm) session.load(Sdm.class, id);
        }
        catch (Exception ex) {
            logger.debug("Unable to get Document from Database", ex);
            throw new SDMException("Unable to get Document from Database", ex);
        }

        return document;
    }

    /**
     * Updtae SDM  item
     *
     * @param item
     * @throws SDMException
     */
    public static void updateItem(SdmItem item) throws
        SDMException {
        Transaction tx = null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
//beginTransaction();
            session.update(item);
            //tx.commit();
        }
        catch (Exception ex) {
            logger.debug("Unable to update sdm item into database", ex);

            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            throw new SDMException(
                "Unable to update sdm item into database", ex);
        }
    }

    /**
     * Update or insert new SDM document
     *
     * @param document
     * @throws SDMException
     */
    public static void updateDocument(Sdm document) throws
        SDMException {
        Transaction tx = null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
//beginTransaction();

            // insert new
            if (document.getId() == null) {

                if (document.getItems() != null) {
                    Iterator<SdmItem> iter = document.getItems().iterator();
                    while (iter.hasNext()) {
                        SdmItem item = iter.next();
                        item.setDocument(document);
                        session.save(item);
                    }
                }
                session.save(document);
            } // update
            else {
                if (document.getItems() != null) {
                    Iterator<SdmItem> iter = document.getItems().iterator();
                    while (iter.hasNext()) {
                        SdmItem item = iter.next();
                        if (item.getDocument() == null) {
                            item.setDocument(document);
                            session.save(item);
                        }
                        else {
                            session.update(item);
                        }
                    }
                }
                session.update(document);
            }

            //tx.commit();
        }
        catch (Exception ex) {
            logger.debug("Unable to update document information into database", ex);
            throw new SDMException( "Unable to update document information into database", ex);
        }

    }
    
    public static Sdm saveOrUpdateDocument(Sdm document) throws SDMException {
        Transaction tx = null;
        Session session = null;
        //Sdm doc=null;
       // boolean newDoc=false;
        try {
            session = PersistenceManager.openNewSession();
//beginTransaction();
            tx = session.beginTransaction();
            if(document.getId()==null){
                session.save(document);
                if(document.getItems()!=null){
                    for (SdmItem oldItem : document.getItems()) {
                        oldItem.setDocument(document);
                        //save item
                        session.save(oldItem);
                    }
                }
            }
            else{
                Sdm doc=(Sdm)session.load(Sdm.class, document.getId());
                doc.setName(document.getName());
                doc.setInstanceId(document.getInstanceId());
                doc.setSiteId(document.getSiteId());
                if(document.getItems()!=null){
                    for(Object item:document.getItems()){
                        SdmItem sdmItem=(SdmItem)item;
                        if(sdmItem.getDocument()==null){
                            sdmItem.setDocument(document);
                            session.save(sdmItem);
                        }
                        else{
                            SdmItem oldItem=(SdmItem) session.load(SdmItem.class, new SdmItem(doc,sdmItem.getParagraphOrder()));
                            oldItem.setAlignment(sdmItem.getAlignment());
                            oldItem.setBold(sdmItem.getBold());
                            oldItem.setContent(sdmItem.getContent());
                            oldItem.setContentText(sdmItem.getContentText());
                            oldItem.setContentTitle(sdmItem.getContentTitle());
                            oldItem.setContentType(sdmItem.getContentType());
                            oldItem.setFont(sdmItem.getFont());
                            oldItem.setFontSize(sdmItem.getFontSize());
                            oldItem.setItalic(sdmItem.getItalic());
                            oldItem.setParagraphOrder(sdmItem.getParagraphOrder());
                            oldItem.setRealType(sdmItem.getRealType());
                            oldItem.setUnderline(sdmItem.getUnderline());
                            //update item
                            session.update(oldItem);
                        }
                    }
                }
                
                session.update(doc);
                
            }           
//      
            tx.commit();
        }
        catch (Exception ex) {
            logger.debug("Unable to update document information into database",ex);
            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            throw new SDMException("Unable to update document information into database", ex);
        }
        finally {
            PersistenceManager.closeSession(session);
        }
        return document;
    }
    
    /**
     * After this issue: AMP-18018 we use this method to always have a unique paragraph.
     * @param document
     * @return
     */
    public static Long getNewParagraphOrder(Sdm document) {
        Long order = null;
        try {
            int i = 0;
            boolean add = false;
            if (document.getItems() != null) {
                Iterator<SdmItem> iItems = document.getItems().iterator();
                while (iItems.hasNext()) {
                    SdmItem item = iItems.next();
                    if (item.getParagraphOrder().intValue() > i) {
                        i = item.getParagraphOrder().intValue();
                    }
                    add = true;
                }
            }
            if (add) {
                order = new Long(i + 1);
            } else {
                order = new Long(i);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return order;
    }

    public static List getDocuments(HttpServletRequest request) throws
        SDMException {

        // get documents List from data base
        ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(request);

        List documentsList = DbUtil.getDocuments(moduleInstance.getSite().
                                                 getSiteId(),
                                                 moduleInstance.getInstanceName());

        return documentsList;
    }

    /**
     * delete Document
     * @param document - document to delete
     * @throws SDMException
     */
    public static void deleteDocument(Sdm document) throws
        SDMException {

        Transaction tx = null;
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();

//beginTransaction();
            session.delete(document);
            //tx.commit();
        }
        catch (Exception ex) {
            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            logger.debug("Unable to update document information into database",
                         ex);
            throw new SDMException(
                "Unable to delete document", ex);
        }
    }

    /**
     * Returns image, assigned to sdm item
     * @param sdmId long
     * @param paragraphId long
     * @return ImageInfo
     * @throws SDMException
     */
    public static ImageInfo getSdmImage(long sdmId, long paragraphId) throws
        SDMException {

        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            Query query = session.createQuery("select itm.contentType, itm.content from " +
                                          SdmItem.class.getName() +
                                          " itm where itm.document.id=:docId" +
                                          " and itm.paragraphOrder=:paragraphId" +
                                          " and itm.realType=:itemType");
            query.setParameter("docId", sdmId, LongType.INSTANCE);
            query.setParameter("paragraphId", paragraphId, LongType.INSTANCE);
            query.setParameter("itemType", SdmItem.TYPE_IMG,StringType.INSTANCE);
            query.setCacheable(true);

            List result = query.list();

            if (result.size() != 0) {
                Object[] resultRow = (Object[]) result.get(0);
                return new ImageInfo( (String) (resultRow[0]),
                                          (byte[]) (resultRow[1]));
            }

       }
        catch (Exception ex) {

            logger.debug("Unable to update document information into database",
                         ex);
            throw new SDMException(
                "Unable to delete document", ex);
        }

        return null;
    }

    public static SdmItem getSdmItem(long sdmId, long paragraphId) throws SDMException {
        Session session = null;
        SdmItem result =null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Query query = session.createQuery("select itm from " +SdmItem.class.getName() +
                                          " itm where itm.document.id=:docId and itm.paragraphOrder=:paragraphId");
            query.setParameter("docId", sdmId, LongType.INSTANCE);
            query.setParameter("paragraphId", paragraphId, LongType.INSTANCE);

            result = (SdmItem)query.uniqueResult();
       }
        catch (Exception ex) {

            logger.debug("Unable to get sdmItem",ex);
            throw new SDMException("Unable to get sdmItem", ex);
        }
        return result;
    }
}
