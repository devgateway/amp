/*
 *   DbUtil.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id$
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Confidential and Proprietary, Subject to the Non-Disclosure
 *   Agreement, Version 1.0, between the Development Gateway
 *   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
 *   Gateway Foundation, Inc.
 *
 *   Unauthorized Disclosure Prohibited.
 *
 *************************************************************************/
package org.digijava.module.sdm.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.sdm.dbentity.Sdm;
import org.digijava.module.sdm.dbentity.SdmItem;
import org.digijava.module.sdm.exception.SDMException;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import java.util.Set;
import java.util.Collection;
import java.util.ArrayList;
import java.util.*;

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
            session = PersistenceManager.getSession();
            tx = session.beginTransaction();
            document.setSiteId(siteId);
            document.setInstanceId(instanceId);
            document.setName(title);

            session.save(document);
            tx.commit();
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
        finally {
            if (session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                }
                catch (Exception ex1) {
                    logger.warn("releaseSession() failed", ex1);
                }
            }
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
            session = PersistenceManager.getSession();
            String queryString = null;

            queryString =
                "select new org.digijava.module.sdm.form.SdmInfo(h.id,h.name) from  " +
                Sdm.class.getName() + " h, " +
                " where (h.siteId=:siteId) and (h.instanceId=:instanceId)";

            q = session.createQuery(queryString);

            q.setParameter("siteId", siteId, Hibernate.STRING);
            q.setParameter("instanceId", instanceId, Hibernate.STRING);

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
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed", ex2);
            }
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

        Iterator iter = document.getItems().iterator();
        while (iter.hasNext()) {
            SdmItem item = (SdmItem) iter.next();
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
        Iterator iter = document.getItems().iterator();
        while (iter.hasNext()) {
            SdmItem item = (SdmItem) iter.next();
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
        Iterator iter = document.getItems().iterator();
        while (iter.hasNext()) {
            SdmItem item = (SdmItem) iter.next();
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
            session = PersistenceManager.getSession();

            tx = session.beginTransaction();
            //item1 = (SdmItem) session.load(SdmItem.class, item1);
            //item2 = (SdmItem) session.load(SdmItem.class, item2);
            Iterator iter = document.getItems().iterator();
            while (iter.hasNext()) {
                SdmItem item = (SdmItem) iter.next();
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
            /*
                        if (item1 != null && item2 != null) {
                          paragraphId = item1.getParagraphOrder();
                          item1.setParagraphOrder(item2.getParagraphOrder());
                          item2.setParagraphOrder(paragraphId);
                          session.update(item1.getDocument());
                            item1.getDocument().getItems().remove(item1);
                            session.delete(item1);
                            item2.getDocument().getItems().remove(item2);
                            session.delete(item2);
                            //
                            paragraphId = item1.getParagraphOrder();
                            item1.setParagraphOrder(item2.getParagraphOrder());
                            item2.setParagraphOrder(paragraphId);
                            session.save(item1);
                            item1.getDocument().setItems(swapedItems);
                            session.save(item2);
                            item2.getDocument().setItems(swapedItems);
             */
            tx.commit();

            swap = true;

        }
        catch (Exception ex) {
            logger.debug("Unable to update information into database", ex);
            throw new SDMException("Unable to update information into database",
                                   ex);
        }
        finally {
            try {
                if (session != null) {
                    PersistenceManager.releaseSession(session);
                }
            }
            catch (Exception ex1) {
                logger.warn("releaseSession() failed", ex1);
            }
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
            session = PersistenceManager.getSession();

            tx = session.beginTransaction();

            Sdm sdm = item.getDocument();

            Iterator iter = sdm.getItems().iterator();
            while (iter.hasNext()) {
                SdmItem iterItem = (SdmItem) iter.next();
                if (iterItem.getParagraphOrder().equals(item.getParagraphOrder())) {
                    iter.remove();
                }
            }
            //item.getDocument().getItems().remove(item);
            session.delete(item);
            session.update(sdm);

            tx.commit();
        }
        catch (Exception ex) {
            logger.debug("Unable to delete intem", ex);
            throw new SDMException("Unable to delete intem", ex);
        }
        finally {
            try {
                if (session != null) {
                    PersistenceManager.releaseSession(session);
                }
            }
            catch (Exception ex1) {
                logger.warn("releaseSession() failed", ex1);
            }
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
            session = PersistenceManager.getSession();
            document = (Sdm) session.load(Sdm.class, id);
        }
        catch (Exception ex) {
            logger.debug("Unable to get Document from Database", ex);
            throw new SDMException("Unable to get Document from Database", ex);
        }
        finally {
            try {
                if (session != null) {
                    PersistenceManager.releaseSession(session);
                }
            }
            catch (Exception ex1) {
                logger.warn("releaseSession() failed", ex1);
            }
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
            session = PersistenceManager.getSession();
            tx = session.beginTransaction();
            session.update(item);
            tx.commit();
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
        finally {
            if (session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                }
                catch (Exception ex1) {
                    logger.warn("releaseSession() failed", ex1);
                }
            }
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
            session = PersistenceManager.getSession();
            tx = session.beginTransaction();

            // insert new
            if (document.getId() == null) {

                if (document.getItems() != null) {
                    Iterator iter = document.getItems().iterator();
                    while (iter.hasNext()) {
                        SdmItem item = (SdmItem) iter.next();
                        item.setDocument(document);
                        session.save(item);
                    }
                }
                session.save(document);
            } // update
            else {
                if (document.getItems() != null) {
                    Iterator iter = document.getItems().iterator();
                    while (iter.hasNext()) {
                        SdmItem item = (SdmItem) iter.next();
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

            tx.commit();
        }
        catch (Exception ex) {

            logger.debug("Unable to update document information into database",
                         ex);

            if (tx != null) {
                try {
                    tx.rollback();
                }
                catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            throw new SDMException(
                "Unable to update document information into database", ex);
        }
        finally {
            if (session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                }
                catch (Exception ex1) {
                    logger.warn("releaseSession() failed", ex1);
                }
            }

        }
    }

    public static List getDocuments(HttpServletRequest request) throws
        SDMException {

        // get documents List from data base
        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);

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
            session = PersistenceManager.getSession();

            tx = session.beginTransaction();
            session.delete(document);
            tx.commit();
        }
        catch (Exception ex) {

            logger.debug("Unable to update document information into database",
                         ex);
            throw new SDMException(
                "Unable to delete document", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed", ex2);
            }
        }
    }

}