/*
 *   DbUtil.java
 *   @Author George Kvizhinadze gio@digijava.org
 *   Created: May 3, 2004
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

package org.digijava.module.cms.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.cms.dbentity.CMS;
import org.digijava.module.cms.dbentity.CMSCategory;
import org.digijava.module.cms.dbentity.CMSContentItem;
import org.digijava.module.cms.exception.CMSException;
import org.digijava.module.cms.form.CMSContentItemForm;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import org.digijava.module.cms.form.CMSContentItemForm;
import org.digijava.kernel.user.User;

public class DbUtil {
  private static Logger logger = Logger.getLogger(DbUtil.class);
  private static final String CACHE_REGION =
      "org.digijava.module.cms.util.Query";

  public static void createCMS(CMS cms) throws CMSException {
    Session sess = null;
    Transaction tx = null;
    try {
      sess = PersistenceManager.getSession();
      tx = sess.beginTransaction();

      sess.save(cms);
      tx.commit();
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
      logger.debug("Unable to create cms instance", ex);
      throw new CMSException(
          "Unable to create forum", ex);
    }
    finally {
      if (sess != null) {
        try {
          PersistenceManager.releaseSession(sess);
        }
        catch (Exception ex2) {
          logger.warn("releaseSession() failed", ex2);
        }
      }
    }
  }

  public static void createCategory(CMSCategory category) throws CMSException {
    Session sess = null;
    Transaction tx = null;
    try {
      sess = PersistenceManager.getSession();
      tx = sess.beginTransaction();

      sess.save(category);
      tx.commit();
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
      logger.debug("Unable to create category", ex);
      throw new CMSException(
          "Unable to create forum", ex);
    }
    finally {
      if (sess != null) {
        try {
          PersistenceManager.releaseSession(sess);
        }
        catch (Exception ex2) {
          logger.warn("releaseSession() failed", ex2);
        }
      }
    }
  }

  public static CMS getCMSItem(String siteId,
                               String instanceId) throws CMSException {
    Session session = null;
    CMS cms = null;
    try {
      session = PersistenceManager.getRequestDBSession();
      Query q = session.createQuery("from rs in class " +
                                    CMS.class.getName() +
          " where (rs.siteId=:siteId) and (rs.instanceId=:instanceId)");
      q.setParameter("siteId", siteId, Hibernate.STRING);
      q.setParameter("instanceId", instanceId, Hibernate.STRING);
      q.setCacheable(true);
      q.setCacheRegion(CACHE_REGION);
      Iterator it = q.iterate();
      if (it.hasNext()) {
        cms = (CMS) it.next();
      }
      else {
        logger.debug("Unable to get cms for instance");
      }
    }
    catch (Exception ex) {
      throw new CMSException("Unable to get CMS form datebase", ex);
    }
    return cms;
  }

  public static List getChildCategories(CMSCategory parentCategory, long cmsId) {
    List categoryList = null;

    Session session = null;
    CMS cms = null;
    try {
      session = PersistenceManager.getSession();
      String querryString = "";
      if (parentCategory != null) {
        querryString = "select elements (rs.childCategories)" +
            " from " + CMSCategory.class.getName() + " rs" +
            " where rs.id=:parentId";
      }
      else {
        querryString = "from rs in class " +
            CMSCategory.class.getName() +
            " where rs.owner=:cmsId and" +
            " rs.primaryParent is null" +
            " order";
      }

      Query q = session.createQuery(querryString);
      q.setCacheable(true);
      q.setCacheRegion(CACHE_REGION);
      if (parentCategory != null) {
        q.setParameter("parentId", new Long(parentCategory.getId()),
                       Hibernate.LONG);
      }
      else {
        q.setParameter("cmsId", new Long(cmsId), Hibernate.LONG);
      }

      categoryList = q.list();
    }
    catch (Exception ex) {
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex2) {
      }
    }
    return categoryList;
  }

  public static List getPrimaryChildCategories(CMSCategory parentCategory) {
    List categoryList = null;

    Session session = null;
    CMS cms = null;
    try {
      session = PersistenceManager.getSession();
      String querryString = "";
      querryString = "from rs in class " +
          CMSCategory.class.getName() +
          " where rs.primaryParent.id = :categoryId";

      Query q = session.createQuery(querryString);
      q.setParameter("parentId", new Long(parentCategory.getId()),
                     Hibernate.LONG);
      q.setCacheable(true);
      q.setCacheRegion(CACHE_REGION);
      categoryList = q.list();
    }
    catch (Exception ex) {
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex2) {
      }
    }
    return categoryList;
  }

  public static CMSCategory getCategoryItem(long categoryId) throws
      CMSException {
    CMSCategory category = null;

    Session session = null;
    CMS cms = null;
    try {
      session = PersistenceManager.getSession();
      Query q = session.createQuery("from rs in class " +
                                    CMSCategory.class.getName() +
                                    " where rs.id = :categoryId");
      q.setParameter("categoryId", new Long(categoryId), Hibernate.LONG);
      q.setCacheable(true);
      q.setCacheRegion(CACHE_REGION);
      if (q.iterate().hasNext()) {
        category = (CMSCategory) q.iterate().next();
      }
    }
    catch (Exception ex) {
      throw new CMSException("Exception getting category item from DB", ex);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex2) {
      }
    }
    return category;
  }

  public static void deletePrimaryChildren(long categoryId) throws
      CMSException {
    Transaction tx = null;
    Session session = null;
    try {
      String queryString = "select from " +
          CMSCategory.class.getName() +
          " rs where rs.primaryParent.id = " + Long.toString(categoryId);

      session = PersistenceManager.getSession();

      session.delete(queryString);
    }
    catch (Exception ex) {

      logger.debug("Unable to delete category form datebase", ex);
      throw new CMSException(
          "Unable to delete category form datebase", ex);
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

  public static void deleteCategory(CMSCategory category) throws
      CMSException {
    Transaction tx = null;
    Session session = null;
    try {
      session = PersistenceManager.getSession();

      tx = session.beginTransaction();
      session.delete(category);
      tx.commit();
    }
    catch (Exception ex) {

      logger.debug("Unable to delete CMS Category form datebase", ex);
      throw new CMSException(
          "Unable to delete CMS Category form datebase", ex);
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

  public static void updateCategory(CMSCategory category) throws
      CMSException {
    Session sess = null;
    Transaction tx = null;
    try {
      sess = PersistenceManager.getSession();
      tx = sess.beginTransaction();
      sess.saveOrUpdate(category);
      tx.commit();
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
      logger.debug("Unable to update CMS category", ex);
      throw new CMSException(
          "Unable to update CMS category", ex);
    }
    finally {
      if (sess != null) {
        try {
          PersistenceManager.releaseSession(sess);
        }
        catch (Exception ex1) {
          logger.warn("releaseSession() failed", ex1);
        }
      }
    }
  }

  public static void changeCategoryPrimaryParent(long categoryId,
                                                 long newParentId,
                                                 CMS owner) throws CMSException {
    Session session = null;
    Transaction tx = null;
    try {
      session = PersistenceManager.getSession();

      Query q = session.createQuery("from rs in class " +
                                    CMSCategory.class.getName() +
                                    " where rs.id = :categoryId");

      Query q1 = session.createQuery("from rs in class " +
                                     CMSCategory.class.getName() +
                                     " where rs.id = :newParentId");

      q.setParameter("categoryId", new Long(categoryId), Hibernate.LONG);
      q1.setParameter("newParentId", new Long(newParentId), Hibernate.LONG);
      q.setCacheable(true);
      q.setCacheRegion(CACHE_REGION);
      q1.setCacheable(true);
      q1.setCacheRegion(CACHE_REGION);
      CMSCategory category = null;
      CMSCategory newPrParent = null;
      if (q.iterate().hasNext()) {
        category = (CMSCategory) q.iterate().next();
      }
      if (q1.iterate().hasNext()) {
        newPrParent = (CMSCategory) q1.iterate().next();
      }
      CMSCategory formerParent = category.getPrimaryParent();
      tx = session.beginTransaction();
      if (formerParent != null) { //not a top level category
        category.getParentCategories().add(formerParent);
      }

      if (newPrParent != null) {
        CMSManager.removeCategoryFromSet(category.getParentCategories(),
                                         newPrParent);
        category.setOwner(null);
      }
      else {
        category.setOwner(owner);
      }
      category.setPrimaryParent(newPrParent);
      session.saveOrUpdate(category);
      tx.commit();
    }
    catch (Exception ex) {
      throw new CMSException("Exception changing category primary parent", ex);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex2) {
      }
    }

  }

  public static void changeCategoryPrimaryParent(CMSCategory category,
                                                 long newParentId,
                                                 CMS owner) throws CMSException {
    Session session = null;
    Transaction tx = null;
    try {
      session = PersistenceManager.getSession();

      Query q1 = session.createQuery("from rs in class " +
                                     CMSCategory.class.getName() +
                                     " where rs.id = :newParentId");

      q1.setParameter("newParentId", new Long(newParentId), Hibernate.LONG);
      q1.setCacheable(true);
      q1.setCacheRegion(CACHE_REGION);
      CMSCategory newPrParent = null;

      if (q1.iterate().hasNext()) {
        newPrParent = (CMSCategory) q1.iterate().next();
      }

      CMSCategory formerParent = category.getPrimaryParent();

      tx = session.beginTransaction();

      if (formerParent != null) { //not a top level category
        CMSManager.removeCategoryFromSet(formerParent.
                                         getPrimaryChildCategories(), category);
        formerParent.getChildCategories().add(category);
        category.getParentCategories().add(formerParent);
        session.saveOrUpdate(formerParent);
      }
      else {
        category.setOwner(null);
      }

      if (newPrParent != null) {
        CMSManager.removeCategoryFromSet(newPrParent.getChildCategories(),
                                         category);
        newPrParent.getPrimaryChildCategories().add(category);
        CMSManager.removeCategoryFromSet(category.getParentCategories(),
                                         newPrParent);
        session.saveOrUpdate(newPrParent);
      }
      else {
        category.setOwner(owner);
      }
      category.setPrimaryParent(newPrParent);

      session.saveOrUpdate(category);
      tx.commit();

    }
    catch (Exception ex) {
      throw new CMSException("Exception changing category primary parent", ex);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex2) {
      }
    }

  }

  /**
   *
   * @param item
   * @throws CMSException
   */
  public static void updateCMSContentItem(CMSContentItem item) throws
      CMSException {

    Transaction tx = null;
    Session session = null;
    try {
      session = PersistenceManager.getSession();
      tx = session.beginTransaction();

      session.update(item);
      tx.commit();
    }
    catch (Exception ex) {
      logger.debug("Unable to update CMS Content Item from database", ex);
      throw new CMSException(
          "Unable to update CMS Content Item from database", ex);
    }
    finally {
      try {
        if (session != null) {
          PersistenceManager.releaseSession(session);
        }
      }
      catch (Exception ex1) {
        logger.warn("releaseSession() failed ", ex1);
      }
    }
  }

  /**
   *
   * @param item
   * @throws CMSException
   */
  public static void createCMSContentItem(CMSContentItem item) throws
      CMSException {

    Transaction tx = null;
    Session session = null;
    try {
      session = PersistenceManager.getSession();
      tx = session.beginTransaction();

      session.save(item);
      tx.commit();
    }
    catch (Exception ex) {
      logger.debug("Unable to create CMS Content Item from database", ex);
      throw new CMSException(
          "Unable to create CMS Content Item from database", ex);
    }
    finally {
      try {
        if (session != null) {
          PersistenceManager.releaseSession(session);
        }
      }
      catch (Exception ex1) {
        logger.warn("releaseSession() failed ", ex1);
      }
    }
  }

  /**
   *
   * @param item
   * @throws CMSException
   */
  public static void deleteCMSContentItem(CMSContentItem item) throws
      CMSException {

    Transaction tx = null;
    Session session = null;
    try {
      session = PersistenceManager.getSession();
      tx = session.beginTransaction();

      session.delete(item);
      tx.commit();
    }
    catch (Exception ex) {
      logger.debug("Unable to delete CMS Content Item from database", ex);
      throw new CMSException(
          "Unable to delete CMS Content Item from database", ex);
    }
    finally {
      try {
        if (session != null) {
          PersistenceManager.releaseSession(session);
        }
      }
      catch (Exception ex1) {
        logger.warn("releaseSession() failed ", ex1);
      }
    }
  }

  /**
   *
   * @param owner
   * @return
   * @throws CMSException
   */
  public static List getContentItems(Long ownerId) throws CMSException {

    Session session = null;
    Iterator iterator = null;
    List list = new ArrayList();
    Query q = null;
    try {
      session = PersistenceManager.getSession();

      q = session.createQuery("from ci in class " +
                              CMSContentItem.class.getName() +
                              " where (ci.owner=:ownerId)");

      q.setParameter("ownerId", ownerId, Hibernate.LONG);
      q.setCacheable(true);
      q.setCacheRegion(CACHE_REGION);
      list = q.list();
    }
    catch (Exception ex) {
      logger.debug("Unable to get content items list from database ", ex);
      throw new CMSException("Unable to get content items list from database",
                             ex);
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
   *
   * @param id
   * @return
   * @throws CMSException
   */
  public static CMSContentItem getCMSContentItem(Long id) throws CMSException {

    CMSContentItem item = null;
    Session session = null;
//    Query q = null;

    try {
      session = PersistenceManager.getSession();
      item = (CMSContentItem) session.load(CMSContentItem.class, id);
    }
    catch (Exception ex) {
      logger.debug("Unable to get CMS Content Item from database", ex);
      throw new CMSException(
          "Unable to get CMS Content Item from database", ex);
    }
    finally {
      try {
        if (session != null) {
          PersistenceManager.releaseSession(session);
        }
      }
      catch (Exception ex1) {
        logger.warn("releaseSession() failed ", ex1);
      }
    }
    return item;
  }

  public static List getContentItems(long categoryId,
                                     long cmsId, int firstResult,
                                     int maxResult) throws
      CMSException {

    List contentItemList = null;
    Session session = null;
    CMS cms = null;
    try {
      session = PersistenceManager.getSession();
      String querryString = "";

      /*
           querryString = " from " + CMSContentItem.class.getName() + " rs" +
                  " where :categoryId = some elements(rs.categories) and (rs.rejected = 0 and rs.published = 1)";
       */

      querryString = " select rs from " + CMSContentItem.class.getName() +
          " rs " +
          " inner join rs.categories cat " +
          " where cat.id = :categoryId and " +
          " (rs.rejected = 0 and rs.published = 1)";

      Query q = session.createQuery(querryString);
      q.setCacheable(true);
      q.setCacheRegion(CACHE_REGION);
      q.setFirstResult(firstResult);
      q.setMaxResults(maxResult);
      q.setParameter("categoryId", new Long(categoryId),
                     Hibernate.LONG);

      contentItemList = q.list();
    }
    catch (Exception ex) {
      logger.debug("Unable to get CMS Item from database", ex);
      throw new CMSException(
          "Unable to get CMS Item from database", ex);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex2) {
      }
    }
    return contentItemList;
  }

  /**
   *
   * @param item
   * @throws CMSException
   */
  public static void updateCMSItem(CMS item) throws
      CMSException {

    Transaction tx = null;
    Session session = null;
    try {
      session = PersistenceManager.getSession();
      tx = session.beginTransaction();

      session.update(item);
      tx.commit();
    }
    catch (Exception ex) {
      logger.debug("Unable to update CMS Item from database", ex);
      throw new CMSException(
          "Unable to update CMS Item from database", ex);
    }
    finally {
      try {
        if (session != null) {
          PersistenceManager.releaseSession(session);
        }
      }
      catch (Exception ex1) {
        logger.warn("releaseSession() failed ", ex1);
      }
    }
  }

  public static List getContentItems(long cmsId, String filter, int firstResult,
                                     int maxResult) throws
      CMSException {
    return getContentItems(cmsId, filter, null, firstResult, maxResult);
  }

  public static List getContentItems(long cmsId, String filter, User user,
                                     int firstResult,
                                     int maxResult) throws
      CMSException {
    Session session = null;
    List retVal = null;
    try {
      session = PersistenceManager.getSession();
      String queryString = "";

      if (filter.equals(CMSContentItemForm.VIEW_MODE_ALL)) {
        queryString = "from rs in class " + CMSContentItem.class.getName() +
            " where rs.owner.id=:cmsId";
      }
      else if (filter.equals(CMSContentItemForm.VIEW_MODE_PUBLISHED)) {
        queryString = "from rs in class " +
            CMSContentItem.class.getName() +
            " where rs.owner.id=:cmsId and rs.published=true and rs.rejected=false";
      }
      else if (filter.equals(CMSContentItemForm.VIEW_MODE_REJECTED)) {
        queryString = "from rs in class " +
            CMSContentItem.class.getName() +
            " where rs.owner.id=:cmsId and rs.rejected=true";
      }
      else if (filter.equals(CMSContentItemForm.VIEW_MODE_PENDING)) {
        queryString = "from rs in class " +
            CMSContentItem.class.getName() +
            " where rs.owner.id=:cmsId and rs.published=false and rs.rejected=false";
      }

      if (user != null) {
        queryString += " and rs.authorUser.id = :userId";
      }
      Query q = session.createQuery(queryString);

      if (user != null) {
        q.setParameter("userId", user.getId(), Hibernate.LONG);
      }

      q.setCacheable(true);
      q.setCacheRegion(CACHE_REGION);
      q.setFirstResult(firstResult);
      q.setMaxResults(maxResult);
      q.setParameter("cmsId", new Long(cmsId), Hibernate.LONG);
      retVal = q.list();
    }
    catch (Exception ex) {
      throw new CMSException("Unable to get content items form datebase", ex);
    }
    finally {
      try {
        PersistenceManager.releaseSession(session);
      }
      catch (Exception ex2) {
        logger.warn("releaseSession() failed ", ex2);
      }
    }
    return retVal;
  }
}