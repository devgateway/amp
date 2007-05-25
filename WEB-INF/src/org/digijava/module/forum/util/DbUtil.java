/*
 *   DbUtil.java
 *   @Author George Kvizhinadze gio@digijava.org
 *   Created: Mar 15, 2004
 *   CVS-ID: $Id$
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

package org.digijava.module.forum.util;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.dbentity.ForumPost;
import org.digijava.module.forum.dbentity.ForumSection;
import org.digijava.module.forum.dbentity.ForumSubsection;
import org.digijava.module.forum.dbentity.ForumThread;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import org.digijava.module.forum.exception.ForumException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import org.digijava.module.forum.dbentity.ForumAsset;
import org.digijava.module.forum.dbentity.ForumPrivateMessage;
import java.util.Calendar;

public class DbUtil {

    private static Logger logger = Logger.getLogger(DbUtil.class);
    private static final String CACHE_REGION =
        "org.digijava.module.forum.util.Query";

    public DbUtil() {
    }

    public static Forum getForumItem(String siteId,
                                     String instanceId) {
        Session session = null;
        Forum forum = null;
        try {
            session = PersistenceManager.getSession();
            Query q = session.createQuery("from rs in class " +
                                          Forum.class.getName() +
                " where (rs.siteId=:siteId) and (rs.instanceId=:instanceId)");
            q.setParameter("siteId", siteId, Hibernate.STRING);
            q.setParameter("instanceId", instanceId, Hibernate.STRING);
            q.setCacheable(true);
            q.setCacheRegion(CACHE_REGION);

            Iterator it = q.iterate();
            if (it.hasNext()) {
                forum = (Forum) it.next();

//Set subsection thread count
                if (forum != null &&
                    !forum.getSections().isEmpty()) {
                    Iterator secIt = forum.getSections().iterator();
                    while (secIt.hasNext()) {
                        ForumSection forumSection = (ForumSection) secIt.next();
                        if (!forumSection.getSubsections().isEmpty()) {
                            Iterator subsecIt = forumSection.getSubsections().
                                iterator();
                            while (subsecIt.hasNext()) {
                                ForumSubsection subsection = (ForumSubsection)
                                    subsecIt.
                                    next();
                                int threadCount =
                                    getCollectionSize(session,
                                    subsection.getThreads());
                                int postCount =
                                    getSubsectionTotalPosts(subsection.getId());
                                subsection.setThreadCount(threadCount);
                                subsection.setTotalPosts(postCount);
                            }
                        }
                    }
                }

            }
            else {
                logger.debug("Unable to get forum for site: ");
            }
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
        return forum;
    }

    public static Forum getForumItemForTeaser(String siteId,
                                     String instanceId) {
        Session session = null;
        Forum forum = null;
        try {
            session = PersistenceManager.getSession();
            Query q = session.createQuery("from rs in class " +
                                          Forum.class.getName() +
                " where (rs.siteId=:siteId) and (rs.instanceId=:instanceId)");
            q.setParameter("siteId", siteId, Hibernate.STRING);
            q.setParameter("instanceId", instanceId, Hibernate.STRING);
            q.setCacheable(true);
            q.setCacheRegion(CACHE_REGION);

            Iterator it = q.iterate();
            if (it.hasNext()) {
                forum = (Forum) it.next();
            }
            else {
                logger.debug("Unable to get forum for site: ");
            }
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
        return forum;
    }


    private static int getSubsectionTotalPosts(long sectionId) {
        int retVal = 0;

        Session session = null;
        java.util.List resultList = null;
        try {
            session = PersistenceManager.getSession();
            String queryString = "select fp.id from " +
                ForumPost.class.getName() +
                " fp where fp.thread.subsection.id=" +
                Long.toString(sectionId);
            Query query = session.createQuery(queryString);
            resultList = query.list();
            retVal = resultList.size();
        }
        catch (Exception e) {
        }
        finally {
            if (session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                }
                catch (Exception e) {
                }
            }
        }

        return retVal;
    }

    public static void createForum(Forum forum) throws ForumException {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = PersistenceManager.getSession();
            tx = sess.beginTransaction();

            sess.save(forum);
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
            logger.debug("Unable to create forum", ex);
            throw new ForumException(
                "Unable to create forum", ex);
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

    public static void updateForum(Forum forum) throws ForumException {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = PersistenceManager.getSession();
            tx = sess.beginTransaction();
            sess.saveOrUpdate(forum);
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
            logger.debug("Unable to update forum", ex);
            throw new ForumException(
                "Unable to update forum", ex);
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

    public static void updateSection(ForumSection forumSection) throws
        ForumException {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = PersistenceManager.getSession();
            tx = sess.beginTransaction();
            sess.saveOrUpdate(forumSection);
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
            logger.debug("Unable to update forum section", ex);
            throw new ForumException(
                "Unable to update forum section", ex);
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

    /*
        public static void swapSubsectionOrder(ForumSubsection srcSubsection,
         ForumSubsection dstSubsection) throws
            ForumException {
            int srcOrderIndex =
                new Integer(srcSubsection.getOrderIndex()).intValue();
            int dstOrderIndex =
                new Integer(dstSubsection.getOrderIndex()).intValue();
            srcSubsection.setOrderIndex(dstOrderIndex);
            dstSubsection.setOrderIndex(srcOrderIndex);
            Session sess = null;
            Transaction tx = null;
            try {
                sess = PersistenceManager.getSession();
                tx = sess.beginTransaction();
                sess.update(srcSubsection);
                sess.update(dstSubsection);
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
                logger.debug("Unable to update forum section", ex);
                throw new ForumException(
                    "Unable to update forum section", ex);
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
     */

    public static void updateSubsection(ForumSubsection forumSubsection) throws
        ForumException {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = PersistenceManager.getSession();
            tx = sess.beginTransaction();
            sess.saveOrUpdate(forumSubsection);
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
            logger.debug("Unable to update forum section", ex);
            throw new ForumException(
                "Unable to update forum section", ex);
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

    public static void deleteSubsection(ForumSubsection subsection) throws
        ForumException {
        Transaction tx = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();

            tx = session.beginTransaction();
            session.delete(subsection);
            tx.commit();
        }
        catch (Exception ex) {

            logger.debug("Unable to delete subsection form datebase", ex);
            throw new ForumException(
                "Unable to delete subsection", ex);
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

    public static void deleteSection(ForumSection section) throws
        ForumException {
        Transaction tx = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();

            tx = session.beginTransaction();
            session.delete(section);
            tx.commit();
        }
        catch (Exception ex) {

            logger.debug("Unable to delete section form datebase", ex);
            throw new ForumException(
                "Unable to delete section", ex);
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

    public static void addThreadPost(long threadId,
                                     ForumPost post) throws ForumException {

        Session sess = null;
        Transaction tx = null;
        try {
            sess = PersistenceManager.getSession();

            ForumThread thread = (ForumThread) sess.load(ForumThread.class,
                new Long(threadId));
            tx = sess.beginTransaction();
            thread.getPosts().add(post);
            sess.saveOrUpdate(thread);
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
            logger.debug("Unable to update forum section", ex);
            throw new ForumException(
                "Unable to update forum section", ex);
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

    public static void updateThread(ForumThread forumThread) throws
        ForumException {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = PersistenceManager.getSession();
            tx = sess.beginTransaction();
            sess.saveOrUpdate(forumThread);
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
            logger.debug("Unable to update forum section", ex);
            throw new ForumException(
                "Unable to update forum section", ex);
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

    public static void updateThreadList(Collection threads) throws
        ForumException {
        if (threads != null && !threads.isEmpty()) {
            Session sess = null;
            Transaction tx = null;
            try {
                sess = PersistenceManager.getSession();
                tx = sess.beginTransaction();
                Iterator it = threads.iterator();
                while (it.hasNext()) {
                    ForumThread forumThread = (ForumThread) it.next();
                    sess.saveOrUpdate(forumThread);
                }
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
                logger.debug("Unable to update forum section", ex);
                throw new ForumException(
                    "Unable to update forum section", ex);
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
    }

    public static void updateSubsectionList(Collection subsections) throws
        ForumException {
        if (subsections != null && !subsections.isEmpty()) {
            Session sess = null;
            Transaction tx = null;
            try {
                sess = PersistenceManager.getSession();
                tx = sess.beginTransaction();
                Iterator it = subsections.iterator();
                while (it.hasNext()) {
                    ForumSubsection subsection = (ForumSubsection) it.next();
                    sess.saveOrUpdate(subsection);
                }
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
                logger.debug("Unable to update forum section", ex);
                throw new ForumException(
                    "Unable to update forum section", ex);
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
    }

    public static void deleteSubsectionList(Collection subs) throws
        ForumException {
        Transaction tx = null;
        Session session = null;
        if (subs != null && !subs.isEmpty()) {
            try {
                session = PersistenceManager.getSession();
                tx = session.beginTransaction();
                Iterator it = subs.iterator();
                while (it.hasNext()) {
                    Long subsId = (Long) it.next();
                    ForumSubsection subsection = (ForumSubsection)
                        session.load(ForumSubsection.class, subsId);
                    session.delete(subsection);
                }
                tx.commit();
            }
            catch (Exception ex) {

                logger.debug("Unable to delete subsections form datebase", ex);
                throw new ForumException(
                    "Unable to delete subsections form datebase", ex);
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

    public static ForumSection getSectionItem(long id) throws ForumException {
        Session session = null;
        ForumSection forumSection = null;
        try {
            session = PersistenceManager.getSession();
            forumSection = (ForumSection) session.load(ForumSection.class,
                new Long(id));
            //Set subsection thread count
            if (forumSection != null &&
                !forumSection.getSubsections().isEmpty()) {
                Iterator it = forumSection.getSubsections().iterator();
                while (it.hasNext()) {
                    ForumSubsection subsection = (ForumSubsection) it.next();
                    int threadCount =
                        getCollectionSize(session, subsection.getThreads());
                    subsection.setThreadCount(threadCount);
                }
            }
        }
        catch (Exception ex) {
            logger.debug("Error getting section form DB ", ex);
            throw new ForumException("Error getting section form DB", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
            }
        }
        return forumSection;
    }

    public static ForumSection getSectionItem(long forumId, int orderIndex) {
        Session session = null;
        ForumSection forumSection = null;
        try {
            session = PersistenceManager.getSession();
            Query q = session.createQuery("from rs in class " +
                                          ForumSection.class.getName() +
                " where (rs.forum.id=:forumId) and (rs.orderIndex=:orderIndex)");
            q.setParameter("forumId", new Long(forumId),
                           Hibernate.LONG);
            q.setParameter("orderIndex", new Integer(orderIndex),
                           Hibernate.INTEGER);
            q.setCacheable(true);
            q.setCacheRegion(CACHE_REGION);

            Iterator it = q.iterate();
            if (it.hasNext()) {
                forumSection = (ForumSection) it.next();
            }
            else {
                logger.debug("Unable to get forum for site: ");
            }
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
        return forumSection;
    }

    public static ForumSubsection getSubsectionItem(long id) throws
        ForumException {
        Session session = null;
        ForumSubsection forumSubsection = null;
        try {
            session = PersistenceManager.getSession();
            forumSubsection = (ForumSubsection) session.load(ForumSubsection.class,
                new Long(id));
            forumSubsection.setThreadCount(getCollectionSize(session,
                forumSubsection.getThreads()));
        }
        catch (Exception ex) {
            logger.debug("Error getting subsection form DB ", ex);
            throw new ForumException("Error getting subsection form DB", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
            }
        }
        return forumSubsection;
    }

    public static ForumThread getThreadItem(long id) throws ForumException {
        Session session = null;
        ForumThread forumThread = null;
        try {
            session = PersistenceManager.getSession();
            forumThread = (ForumThread) session.load(ForumThread.class,
                new Long(id));

            int postCount =
                getCollectionSize(session,
                                  forumThread.getPosts());
            forumThread.setPostCount(postCount);
        }
        catch (Exception ex) {
            logger.debug("Error getting thread form DB ", ex);
            throw new ForumException("Error getting thread form DB", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
            }
        }
        return forumThread;
    }

    public static long getNextThreadID(ForumThread thread) {
        Session session = null;
        long prevThreadId = 0;
        if (thread.getLastPostDate() != null) {
            try {
                session = PersistenceManager.getSession();
                String queryString = "select rs.id from " +
                    ForumThread.class.getName() +
                    " rs where rs.subsection.id=:subsectionId " +
                    "and rs.lastPostDate<:lastPostDate " +
                    "order by rs.lastPostDate desc";

                Query q = session.createQuery(queryString);
                q.setMaxResults(1);
                Calendar cal = Calendar.getInstance();
                cal.setTime(thread.getLastPostDate());
                q.setParameter("lastPostDate", cal, Hibernate.CALENDAR);
                q.setParameter("subsectionId",
                               new Long(thread.getSubsection().getId()),
                               Hibernate.LONG);
                if (q.iterate().hasNext()) {
                    prevThreadId = ( (Long) q.iterate().next()).longValue();
                }

            }
            catch (Exception ex) {
                logger.debug("Unable to get prev thread id ", ex);
            }
            finally {
                try {
                    PersistenceManager.releaseSession(session);
                }
                catch (Exception ex2) {
                    logger.warn("releaseSession() failed ", ex2);
                }
            }
        }
        return prevThreadId;
    }

    public static long getPrevThreadID(ForumThread thread) {
        Session session = null;
        long nextThreadId = 0;
        if (thread.getLastPostDate() != null) {
            try {
                session = PersistenceManager.getSession();
                String queryString = "select rs.id from " +
                    ForumThread.class.getName() +
                    " rs where (rs.subsection.id = :subsectionId) " +
                    "and (rs.lastPostDate>:lastPostDate) " +
                    "order by rs.lastPostDate asc";

                Query q = session.createQuery(queryString);
                q.setMaxResults(1);
                Calendar cal = Calendar.getInstance();
                cal.setTime(thread.getLastPostDate());
                q.setParameter("lastPostDate", cal, Hibernate.CALENDAR);
                q.setParameter("subsectionId",
                               new Long(thread.getSubsection().getId()),
                               Hibernate.LONG);
                if (q.iterate().hasNext()) {
                    nextThreadId = ( (Long) q.iterate().next()).longValue();
                }

            }
            catch (Exception ex) {
                logger.debug("Unable to get next thread id ", ex);
            }
            finally {
                try {
                    PersistenceManager.releaseSession(session);
                }
                catch (Exception ex2) {
                    logger.warn("releaseSession() failed ", ex2);
                }
            }
        }
        return nextThreadId;
    }

    public static List getThreadPostIds(long threadId) throws
        ForumException {

        Session session = null;
        List posts = null;

        try {
            session = PersistenceManager.getSession();
            String queryString = "select rs.id from " +
                ForumPost.class.getName() +
                " rs where (rs.thread.id = :threadId) " +
                "order by rs.postTime asc";

            Query q = session.createQuery(queryString);
            q.setParameter("threadId", new Long(threadId),
                           Hibernate.LONG);
            posts = q.list();
        }
        catch (Exception ex) {
            logger.debug("Unable to get thread list from database ", ex);
            throw new ForumException(
                "Unable to get thread list from database ", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }

        return posts;
    }

    public static List getThreadPublishedPostIds(long threadId,
                                                 ForumUserSettings user) throws
        ForumException {
        Session session = null;
        List posts = null;
        try {
            session = PersistenceManager.getSession();
            String queryString = "";
            if (user != null) {
                queryString = "select rs.id from " +
                    ForumPost.class.getName() +
                    " rs where (rs.thread.id = :threadId) " +
                    "and (rs.authorUserSettings.id = :userId or " +
                    "rs.published = true) " +
                    "order by rs.postTime asc";
            }
            else {
                queryString = "select rs.id from " +
                    ForumPost.class.getName() +
                    " rs where (rs.thread.id = :threadId) " +
                    "and rs.published = true) " +
                    "order by rs.postTime asc";
            }
            Query q = session.createQuery(queryString);
            q.setParameter("threadId", new Long(threadId),
                           Hibernate.LONG);
            if (user != null) {
                q.setParameter("userId", new Long(user.getId()), Hibernate.LONG);
            }
            posts = q.list();
        }
        catch (Exception ex) {
            logger.debug("Unable to get thread list from database ", ex);
            throw new ForumException(
                "Unable to get thread list from database ", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }

        return posts;
    }

    public static void deleteThread(ForumThread thread) throws
        ForumException {
        Transaction tx = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();

            tx = session.beginTransaction();
            session.delete(thread);
            tx.commit();
        }
        catch (Exception ex) {

            logger.debug("Unable to delete thread form datebase", ex);
            throw new ForumException(
                "Unable to delete thread", ex);
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

    public static void deleteThreadPosts(long threadId) throws
        ForumException {
        Transaction tx = null;
        Session session = null;
        try {
          String queryString = "select from " +
              ForumPost.class.getName() +
              " rs where rs.thread.id = " + Long.toString(threadId);

          session = PersistenceManager.getSession();

          session.delete(queryString);
        }
        catch (Exception ex) {

            logger.debug("Unable to delete post form datebase", ex);
            throw new ForumException(
                "Unable to delete thread", ex);
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

    public static void deletePost(ForumPost post) throws
    ForumException {
    Transaction tx = null;
    Session session = null;
    try {
        session = PersistenceManager.getSession();

        tx = session.beginTransaction();
        session.delete(post);
        tx.commit();
    }
    catch (Exception ex) {

        logger.debug("Unable to delete post form datebase", ex);
        throw new ForumException(
            "Unable to delete thread", ex);
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


    public static void deleteThreadList(Collection threads) throws
        ForumException {
        Transaction tx = null;
        Session session = null;
        if (threads != null && !threads.isEmpty()) {
            try {
                session = PersistenceManager.getSession();
                tx = session.beginTransaction();
                Iterator it = threads.iterator();
                while (it.hasNext()) {
//                    Long threadId = (Long) it.next();
                    ForumThread thread = (ForumThread) it.next();
                    session.delete(thread);
                }
                tx.commit();
            }
            catch (Exception ex) {

                logger.debug("Unable to delete thread form datebase", ex);
                throw new ForumException(
                    "Unable to delete thread", ex);
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

    public static void lockThreadList(Collection threads, boolean lock) throws
        ForumException {
        Transaction tx = null;
        Session session = null;
        if (threads != null && !threads.isEmpty()) {
            try {
                session = PersistenceManager.getSession();
                tx = session.beginTransaction();
                Iterator it = threads.iterator();
                while (it.hasNext()) {
                    Long threadId = (Long) it.next();
                    ForumThread thread = (ForumThread)
                        session.load(ForumThread.class, threadId);
                    if (thread.getParentThread() != null) {
                        thread = thread.getParentThread();
                    }
                    if (thread != null) {
                        thread.setLocked(lock);
                    }
                    session.update(thread);
                }
                tx.commit();
            }
            catch (Exception ex) {

                logger.debug("Unable to lock/unlock threads", ex);
                throw new ForumException(
                    "Unable to lock/unlock threads", ex);
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

    public static ForumSubsection getSubsectionItem(long sectionId,
        int orderIndex) {
        Session session = null;
        ForumSubsection forumSubsection = null;
        try {
            session = PersistenceManager.getSession();
            Query q = session.createQuery("from rs in class " +
                                          ForumSubsection.class.getName() +
                " where (rs.section.id=:sectionId) and (rs.orderIndex=:orderIndex)");
            q.setParameter("sectionId", new Long(sectionId),
                           Hibernate.LONG);
            q.setParameter("orderIndex", new Integer(orderIndex),
                           Hibernate.INTEGER);
            q.setCacheable(true);
            q.setCacheRegion(CACHE_REGION);
            Iterator it = q.iterate();
            if (it.hasNext()) {
                forumSubsection = (ForumSubsection) it.next();
            }
            else {
                logger.debug("Unable to get forum for site: ");
            }
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
        return forumSubsection;
    }

    public static ForumPost getPostItem(long id) throws ForumException {
        Session session = null;
        ForumPost forumPost = null;
        try {
            session = PersistenceManager.getSession();
            forumPost = (ForumPost) session.load(ForumPost.class, new Long(id));
        }
        catch (Exception ex) {
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex1) {
                logger.debug("error getting forum post ");
                throw new ForumException("error getting forum post ", ex1);
            }
        }
        return forumPost;
    }

    public static ForumUserSettings getForumUserItem(Long userId,
        Long forumId) throws ForumException {
        Session session = null;
        ForumUserSettings forumUser = null;
        try {
            session = PersistenceManager.getSession();

            String queryString = "from rs in class " +
                ForumUserSettings.class.getName() +
                " where (rs.digiUserId=:userId) and (rs.forumId=:forumId)";

            Query q = session.createQuery(queryString);
            q.setParameter("userId", userId, Hibernate.LONG);
            q.setParameter("forumId", forumId, Hibernate.LONG);
            q.setCacheable(true);
            q.setCacheRegion(CACHE_REGION);

            if (q != null && q.iterate().hasNext()) {
                forumUser = (ForumUserSettings) q.iterate().next();
            }
            else {
                logger.debug("Unable to get forum user settings");
            }

        }
        catch (Exception ex) {
            logger.debug("Unable to get user settings from database ", ex);
            throw new ForumException(
                "Unable to get user settings from database ", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }

        return forumUser;
    }

    public static ForumUserSettings getForumUserItem(long id) {
        Session session = null;
        ForumUserSettings forumUserSettings = null;
        try {
            session = PersistenceManager.getSession();
            forumUserSettings = (ForumUserSettings) session.load(
                ForumUserSettings.class,
                new Long(id));
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
        return forumUserSettings;
    }

    public static void updateForumUser(ForumUserSettings forumUser) throws
        ForumException {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = PersistenceManager.getSession();
            tx = sess.beginTransaction();
            sess.saveOrUpdate(forumUser);
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
            logger.debug("Unable to update forum", ex);
            throw new ForumException(
                "Unable to update forum", ex);
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

    public static void createForumUser(ForumUserSettings forumUser) throws
        ForumException {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = PersistenceManager.getSession();
            tx = sess.beginTransaction();

            sess.save(forumUser);
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
            logger.debug("Unable to create forum", ex);
            throw new ForumException(
                "Unable to create forum", ex);
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

    public static void createThread(ForumThread forumThread) throws
        ForumException {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = PersistenceManager.getSession();
            tx = sess.beginTransaction();
            sess.save(forumThread);
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
            logger.debug("Unable to create thread", ex);
            throw new ForumException(
                "Unable to create thread", ex);
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

    public static void createPost(ForumPost forumPost) throws
        ForumException {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = PersistenceManager.getSession();
            tx = sess.beginTransaction();

            sess.save(forumPost);
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
            logger.debug("Unable to create post", ex);
            throw new ForumException(
                "Unable to create post", ex);
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

    public static void updatePost(ForumPost post) throws ForumException {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = PersistenceManager.getSession();
            tx = sess.beginTransaction();
            sess.saveOrUpdate(post);
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
            logger.debug("Unable to update post", ex);
            throw new ForumException(
                "Unable to update post", ex);
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

    public static void updatePostList(Collection posts) throws ForumException {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = PersistenceManager.getSession();
            tx = sess.beginTransaction();
            Iterator it = posts.iterator();
            while (it.hasNext()) {
                ForumPost post = (ForumPost) it.next();
                sess.saveOrUpdate(post);
            }
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
            logger.debug("Unable to update post", ex);
            throw new ForumException(
                "Unable to update post", ex);
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

    public static List getThreads(Long subsectionId,
                                  int firstResult,
                                  int maxResult) throws
        ForumException {

        Session session = null;
        List threads = null;

        try {
            session = PersistenceManager.getSession();

            String queryString = "from rs in class " +
                ForumThread.class.getName() +
                " where (rs.subsection.id=:subsectionId) order by rs.lastPostDate desc";

            /*
                         String queryString = "from rs in class " +
                ForumThread.class.getName() +
                " where (rs.subSection.id=:subsectionId)";*/

            Query q = session.createQuery(queryString);
            q.setParameter("subsectionId", subsectionId, Hibernate.LONG);
            q.setFirstResult(firstResult);
            q.setMaxResults(maxResult);
            q.setCacheable(true);
            q.setCacheRegion(CACHE_REGION);

            threads = q.list();

            if (threads != null && !threads.isEmpty()) {

                Iterator it = threads.iterator();

                while (it.hasNext()) {
                    ForumThread thread = (ForumThread) it.next();
                    int postCount =
                        getCollectionSize(session, thread.getPosts());
                    thread.setPostCount(postCount);
                }
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get thread list from database ", ex);
            throw new ForumException(
                "Unable to get thread list from database ", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }

        return threads;
    }

    private static int getCollectionSize(Session session, Collection coll) {
        int count = 0;
        try {
            Query qq = session.createFilter(coll, "select count(*)");
            qq.setCacheable(true);
            qq.setCacheRegion(CACHE_REGION);
            if (qq.iterate().hasNext()) {
                count = ( (Integer) qq.iterate().next()).intValue();
            }
        }
        catch (HibernateException ex) {
            logger.debug("Unable to hibernate collection size ", ex);
        }
        return count;
    }

    public static List getPosts(Long threadId,
                                int filterFrom,
                                int sortOrder,
                                int firstResult,
                                int maxResult) throws
        ForumException {

        Session session = null;
        List posts = null;
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(new Date());

        if (filterFrom == ForumConstants.FILTER_POSTS_ONE_DAY) {
            fromCalendar.add(Calendar.DATE, -1);
        }
        else if (filterFrom == ForumConstants.FILTER_POSTS_ONE_WEEK) {
            fromCalendar.add(Calendar.DATE, -7);
        }
        else if (filterFrom == ForumConstants.FILTER_POSTS_TWO_WEEKS) {
            fromCalendar.add(Calendar.DATE, -14);
        }
        else if (filterFrom == ForumConstants.FILTER_POSTS_ONE_MONTH) {
            fromCalendar.add(Calendar.MONTH, -1);
        }
        else if (filterFrom == ForumConstants.FILTER_POSTS_THREE_MONTH) {
            fromCalendar.add(Calendar.MONTH, -3);
        }
        else if (filterFrom == ForumConstants.FILTER_POSTS_SIX_MONTH) {
            fromCalendar.add(Calendar.MONTH, -6);
        }
        else if (filterFrom == ForumConstants.FILTER_POSTS_ONE_YEAR) {
            fromCalendar.add(Calendar.YEAR, -1);
        }

        try {
            session = PersistenceManager.getSession();
            StringBuffer queryString = new StringBuffer();

            queryString.append("from rs in class ");
            queryString.append(ForumPost.class.getName());
            queryString.append(" where rs.thread.id=:threadId ");
            if (filterFrom != ForumConstants.FILTER_POSTS_ALL) {
                queryString.append("and rs.postTime >= :fromTime ");
            }

            queryString.append("order by rs.postTime ");

            if (sortOrder == ForumConstants.SORT_DESC) {
                queryString.append("desc");
            }
            else {
                queryString.append("asc");
            }

            Query q = session.createQuery(queryString.toString());
            q.setParameter("threadId", threadId, Hibernate.LONG);

            if (filterFrom != ForumConstants.FILTER_POSTS_ALL) {
                q.setParameter("fromTime", fromCalendar, Hibernate.CALENDAR);
            }

            q.setFirstResult(firstResult);
            q.setMaxResults(maxResult);
            q.setCacheable(true);
            q.setCacheRegion(CACHE_REGION);

            posts = q.list();
        }
        catch (Exception ex) {
            logger.debug("Unable to get thread list from database ", ex);
            throw new ForumException(
                "Unable to get thread list from database ", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }

        return posts;
    }

    public static List getPostsAfterDate(Long threadId, Date after) throws
        ForumException {

        Session session = null;
        List posts = null;

        try {
            session = PersistenceManager.getSession();

            String queryString = "from rs in class " + ForumPost.class.getName() +
                " where rs.thread.id=:threadId and rs.postTime >= :after order by rs.postTime";

            Query q = session.createQuery(queryString);

            Calendar afterCal = Calendar.getInstance();
            afterCal.setTime(after);
            q.setCalendar("after", afterCal);
            q.setParameter("threadId", threadId, Hibernate.LONG);

            q.setCacheable(true);
            q.setCacheRegion(CACHE_REGION);

            posts = q.list();
        }
        catch (Exception ex) {
            logger.debug("Unable to get thread list from database ", ex);
            throw new ForumException(
                "Unable to get thread list from database ", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }

        return posts;
    }

    public static List getSubsections(Long forumId) throws
        ForumException {

        Session session = null;
        List subsections = null;

        try {
            session = PersistenceManager.getSession();

            String queryString = "from rs in class " +
                ForumSubsection.class.getName() +
                " where (rs.section.forum.id=:forumId) order by rs.orderIndex";

            Query q = session.createQuery(queryString);
            q.setParameter("forumId", forumId, Hibernate.LONG);
            q.setCacheable(true);
            q.setCacheRegion(CACHE_REGION);

            subsections = q.list();
        }
        catch (Exception ex) {
            logger.debug("Unable to get subsection list from database ", ex);
            throw new ForumException(
                "Unable to get subsection list from database ", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }

        return subsections;
    }

    public static List getSectionSubsections(Long sectionId) throws
        ForumException {

        Session session = null;
        List subsections = null;

        try {
            session = PersistenceManager.getSession();

            String queryString = "from rs in class " +
                ForumSubsection.class.getName() +
                " where (rs.section.id=:sectionId) order by rs.orderIndex";

            Query q = session.createQuery(queryString);
            q.setParameter("sectionId", sectionId, Hibernate.LONG);
            q.setCacheable(true);
            q.setCacheRegion(CACHE_REGION);

            subsections = q.list();
        }
        catch (Exception ex) {
            logger.debug("Unable to get subsection list from database ", ex);
            throw new ForumException(
                "Unable to get subsection list from database ", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }

        return subsections;
    }

    public static List getPublishedPosts(Long threadId,
                                         ForumUserSettings user,
                                         int filterFrom,
                                         int sortOrder,
                                         int firstResult,
                                         int maxResult) throws
        ForumException {

        Session session = null;
        List posts = null;

        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(new Date());

        if (filterFrom == ForumConstants.FILTER_POSTS_ONE_DAY) {
            fromCalendar.add(Calendar.DATE, -1);
        }
        else if (filterFrom == ForumConstants.FILTER_POSTS_ONE_WEEK) {
            fromCalendar.add(Calendar.DATE, -7);
        }
        else if (filterFrom == ForumConstants.FILTER_POSTS_TWO_WEEKS) {
            fromCalendar.add(Calendar.DATE, -14);
        }
        else if (filterFrom == ForumConstants.FILTER_POSTS_ONE_MONTH) {
            fromCalendar.add(Calendar.MONTH, -1);
        }
        else if (filterFrom == ForumConstants.FILTER_POSTS_THREE_MONTH) {
            fromCalendar.add(Calendar.MONTH, -3);
        }
        else if (filterFrom == ForumConstants.FILTER_POSTS_SIX_MONTH) {
            fromCalendar.add(Calendar.MONTH, -6);
        }
        else if (filterFrom == ForumConstants.FILTER_POSTS_ONE_YEAR) {
            fromCalendar.add(Calendar.YEAR, -1);
        }

        try {
            session = PersistenceManager.getSession();
            StringBuffer queryString = new StringBuffer();
            /*
                         if (filterFrom != ForumConstants.FILTER_POSTS_ALL) {
                 queryString.append("and rs.postTime >= :fromTime ");
                         }
             */
            if (user != null) {
                queryString.append("from rs in class ");
                queryString.append(ForumPost.class.getName());
                queryString.append(" where rs.thread.id=:threadId and ");
                if (filterFrom != ForumConstants.FILTER_POSTS_ALL) {
                    queryString.append("rs.postTime >= :fromTime and ");
                }
                queryString.append("(rs.published = true or ");
                queryString.append(
                    "rs.authorUserSettings.id = :userId) order by rs.postTime ");
            }
            else {
                queryString.append("from rs in class ");
                queryString.append(ForumPost.class.getName());
                queryString.append(" where rs.thread.id=:threadId and ");
                if (filterFrom != ForumConstants.FILTER_POSTS_ALL) {
                    queryString.append("rs.postTime >= :fromTime and ");
                }
                queryString.append("rs.published = true ");
                queryString.append("order by rs.postTime ");
            }

            if (sortOrder == ForumConstants.SORT_DESC) {
                queryString.append("desc");
            }
            else {
                queryString.append("asc");
            }

            Query q = session.createQuery(queryString.toString());
            q.setParameter("threadId", threadId, Hibernate.LONG);
            if (user != null) {
                q.setParameter("userId", new Long(user.getId()), Hibernate.LONG);
            }
            if (filterFrom != ForumConstants.FILTER_POSTS_ALL) {
                q.setParameter("fromTime", fromCalendar, Hibernate.CALENDAR);
            }
            q.setFirstResult(firstResult);
            q.setMaxResults(maxResult);
            q.setCacheable(true);
            q.setCacheRegion(CACHE_REGION);

            posts = q.list();
        }
        catch (Exception ex) {
            logger.debug("Unable to get thread list from database ", ex);
            throw new ForumException(
                "Unable to get thread list from database ", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }

        return posts;
    }

    public static List getSubsectionPostIdsAfterLastVisit(long subsectionId,
        long userId,
        Date lastVisitDate) throws
        ForumException {

        Session session = null;
        List posts = null;

        try {
            session = PersistenceManager.getSession();
            String queryString = "select rs.id, rs.lastPostDate from " +
                ForumThread.class.getName() +
                " rs where (rs.subsection.id = :subsectionId) and" +
                " (rs.lastPostDate is not null) and" +
                " (rs.lastPost.authorUserSettings.id != :userId) and" +
                " (rs.lastPostDate > :lastVisitDate)";

            Query q = session.createQuery(queryString);

            q.setParameter("lastVisitDate", lastVisitDate, Hibernate.DATE);
            q.setParameter("subsectionId", new Long(subsectionId),
                           Hibernate.LONG);
            q.setParameter("userId", new Long(userId), Hibernate.LONG);

            posts = q.list();
        }
        catch (Exception ex) {
            logger.debug("Unable to get thread list from database ", ex);
            throw new ForumException(
                "Unable to get thread list from database ", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }

        return posts;
    }

    public static ForumAsset getAssetItem(long id) {
        Session session = null;
        ForumAsset forumAsset = null;
        try {
            session = PersistenceManager.getSession();
            forumAsset = (ForumAsset) session.load(ForumAsset.class,
                new Long(id));
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
        return forumAsset;
    }

    public static void createAsset(ForumAsset asset) throws ForumException {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = PersistenceManager.getSession();
            tx = sess.beginTransaction();

            sess.save(asset);
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
            logger.debug("Unable to create asset", ex);
            throw new ForumException(
                "Unable to create asset", ex);
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

    public static void createPrivateMessage(ForumPrivateMessage pm) throws
        ForumException {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = PersistenceManager.getSession();
            tx = sess.beginTransaction();
            sess.save(pm);
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
            logger.debug("Unable to create private message", ex);
            throw new ForumException(
                "Unable to create private message", ex);
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

    public static void deletePrivateMessage(ForumPrivateMessage pm) throws
        ForumException {
        Transaction tx = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();

            tx = session.beginTransaction();
            session.delete(pm);
            tx.commit();
        }
        catch (Exception ex) {

            logger.debug("Unable to delete private message", ex);
            throw new ForumException(
                "Unable to delete private message", ex);
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

    public static void deletePrivateMessageList(Collection pms) throws
        ForumException {
        Transaction tx = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();

            tx = session.beginTransaction();
            Iterator it = pms.iterator();
            while (it.hasNext()) {
                ForumPrivateMessage pm = (ForumPrivateMessage) it.next();
                session.delete(pm);
            }
            tx.commit();
        }
        catch (Exception ex) {

            logger.debug("Unable to delete private message", ex);
            throw new ForumException(
                "Unable to delete private message", ex);
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

    public static List getPrivateMessages(Long userId,
                                          String folderName,
                                          int firstResult,
                                          int maxResult) throws
        ForumException {

        Session session = null;
        List posts = null;

        try {
            session = PersistenceManager.getSession();

            String queryString = "from rs in class " +
                ForumPrivateMessage.class.getName() +
                " where rs.toUserId=:userID and" +
                " rs.folderName =:folderName and" +
                " rs.isSentPost=false order by rs.postTime desc";

            Query q = session.createQuery(queryString);
            q.setParameter("userID", userId, Hibernate.LONG);
            q.setParameter("folderName", folderName, Hibernate.STRING);
            q.setFirstResult(firstResult);
            q.setMaxResults(maxResult);
            q.setCacheable(true);
            q.setCacheRegion(CACHE_REGION);

            posts = q.list();
        }
        catch (Exception ex) {
            logger.debug("Unable to get user pms from database ", ex);
            throw new ForumException(
                "Unable to get user pms from database ", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }

        return posts;
    }

    public static List getSentPrivateMessages(Long userId,
                                              int firstResult,
                                              int maxResult) throws
        ForumException {

        Session session = null;
        List posts = null;

        try {
            session = PersistenceManager.getSession();

            String queryString = "from rs in class " +
                ForumPrivateMessage.class.getName() +
                " where (rs.authorUserSettings.digiUserId=:userID) and" +
                " rs.isSentPost=true" +
                " order by rs.postTime desc";

            Query q = session.createQuery(queryString);
            q.setParameter("userID", userId, Hibernate.LONG);
            q.setFirstResult(firstResult);
            q.setMaxResults(maxResult);
            q.setCacheable(true);
            q.setCacheRegion(CACHE_REGION);

            posts = q.list();
        }
        catch (Exception ex) {
            logger.debug("Unable to get user pms from database ", ex);
            throw new ForumException(
                "Unable to get user pms from database ", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }

        return posts;
    }

    public static List getOutboxPrivateMessages(Long userId,
                                                int firstResult,
                                                int maxResult) throws
        ForumException {

        Session session = null;
        List posts = null;

        try {
            session = PersistenceManager.getSession();

            String queryString = "from rs in class " +
                ForumPrivateMessage.class.getName() +
                " where (rs.authorUserSettings.digiUserId=:userID) and" +
                " rs.isNew=true" +
                " order by rs.postTime desc";

            Query q = session.createQuery(queryString);
            q.setParameter("userID", userId, Hibernate.LONG);
            q.setFirstResult(firstResult);
            q.setMaxResults(maxResult);
            q.setCacheable(true);
            q.setCacheRegion(CACHE_REGION);

            posts = q.list();
        }
        catch (Exception ex) {
            logger.debug("Unable to get user pms from database ", ex);
            throw new ForumException(
                "Unable to get user pms from database ", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }

        return posts;
    }

    /**
     * Returns the count of unnotified new private messages
     * @param fUser User to check for
     * @return number of new messages
     */
    public static int getNewPrivateMsgCount(ForumUserSettings fUser) {
        int pvtMsgCount = 0;
        Session session = null;
        List posts = null;
        Query q = null;
        Date lastNotify = fUser.getLastPmNotify();
        try {
            session = PersistenceManager.getSession();
            String queryString = "";
            if (lastNotify == null) {
                queryString = "select count(pm.isNew) from " +
                    ForumPrivateMessage.class.getName() +
                    " pm, where pm.toUserId=:toUserId";
            }
            else {
                queryString = "select count(pm.isNew) from " +
                    ForumPrivateMessage.class.getName() +
                    " pm, where pm.toUserId = :toUserId and " +
                    "pm.postTime >= :lastNotify";
            }
            q = session.createQuery(queryString);
            q.setParameter("toUserId",
                           new Long(fUser.getDigiUserId()),
                           Hibernate.LONG);
            if (lastNotify != null) {

                Calendar notDate = Calendar.getInstance();
                notDate.setTime(lastNotify);
                q.setCalendar("lastNotify", notDate);
            }

            q.setCacheable(true);
            q.setCacheRegion(CACHE_REGION);
            pvtMsgCount = ( (Integer) q.uniqueResult()).intValue();
        }
        catch (Exception ex) {
            logger.debug("Unable to get new pm count ", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }

        return pvtMsgCount;
    }

    public static int getUnreadPrivateMsgCount(ForumUserSettings fUser) {
        int pvtMsgCount = 0;
        Session session = null;
        List posts = null;
        Query q = null;
        try {
            session = PersistenceManager.getSession();
            String queryString = "";
            queryString = "select count(*) from " +
                ForumPrivateMessage.class.getName() +
                " pm, where pm.toUserId=:toUserId and pm.isNew=true";
            q = session.createQuery(queryString);
            q.setParameter("toUserId",
                           new Long(fUser.getDigiUserId()),
                           Hibernate.LONG);
            q.setCacheable(true);
            q.setCacheRegion(CACHE_REGION);
            pvtMsgCount = ( (Integer) q.uniqueResult()).intValue();
        }
        catch (Exception ex) {
            logger.debug("Unable to get new pm count ", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }

        return pvtMsgCount;
    }

    public static List getMatchedUserList(long forumId, String matchPatern) throws
        ForumException {
        Session session = null;
        List retVal = null;
        try {
            session = PersistenceManager.getSession();

            String queryString = "from rs in class " +
                ForumUserSettings.class.getName() +
                " where (rs.forumId=:forumId) and lower(rs.nickName) " +
                "like lower(:matchPatern) order by rs.nickName";

            Query q = session.createQuery(queryString);
            q.setParameter("matchPatern", matchPatern, Hibernate.STRING);
            q.setParameter("forumId", new Long(forumId), Hibernate.LONG);
            q.setCacheable(true);
            q.setCacheRegion(CACHE_REGION);

            retVal = q.list();

        }
        catch (Exception ex) {
            logger.debug("Unable to get user settings from database ", ex);
            throw new ForumException(
                "Unable to get user settings from database ", ex);
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

    public static int getPrivateMsgCount(ForumUserSettings fUser,
                                         String folderName) {
        int pvtMsgCount = 0;
        Session session = null;
        List posts = null;
        Query q = null;
        try {
            session = PersistenceManager.getSession();
            String queryString = "";
            if (folderName == null || folderName.length() == 0) {
                queryString = "select count(*) from " +
                    ForumPrivateMessage.class.getName() +
                    " pm, where pm.toUserId=:toUserId";
            }
            else {
                queryString = "select count(*) from " +
                    ForumPrivateMessage.class.getName() +
                    " pm, where pm.toUserId=:toUserId " +
                    "and pm.folderName=:folderName";
            }
            q = session.createQuery(queryString);
            q.setParameter("toUserId",
                           new Long(fUser.getDigiUserId()),
                           Hibernate.LONG);

            if (folderName != null && folderName.length() > 0) {
                q.setParameter("folderName", folderName,
                               Hibernate.STRING);
            }
            q.setCacheable(true);
            q.setCacheRegion(CACHE_REGION);
            pvtMsgCount = ( (Integer) q.uniqueResult()).intValue();
        }
        catch (Exception ex) {
            logger.debug("Unable to get new pm count ", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }

        return pvtMsgCount;
    }

    public static int getSentPrivateMsgCount(ForumUserSettings fUser) {
        int pvtMsgCount = 0;
        Session session = null;
        List posts = null;
        Query q = null;
        try {
            session = PersistenceManager.getSession();
            String queryString = "";

            queryString = "select count(*) from " +
                ForumPrivateMessage.class.getName() +
                " pm, where (pm.authorUserSettings.digiUserId=:userID) and" +
                " pm.isSentPost=true";

            q = session.createQuery(queryString);
            q.setParameter("userID",
                           new Long(fUser.getDigiUserId()),
                           Hibernate.LONG);

            q.setCacheable(true);
            q.setCacheRegion(CACHE_REGION);
            pvtMsgCount = ( (Integer) q.uniqueResult()).intValue();
        }
        catch (Exception ex) {
            logger.debug("Unable to get new pm count ", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }

        return pvtMsgCount;
    }

    public static int getOutboxPrivateMsgCount(ForumUserSettings fUser) {
        int pvtMsgCount = 0;
        Session session = null;
        List posts = null;
        Query q = null;
        try {
            session = PersistenceManager.getSession();
            String queryString = "";

            queryString = "select count(*) from " +
                ForumPrivateMessage.class.getName() +
                " pm, where (pm.authorUserSettings.digiUserId=:userID) and" +
                " pm.isNew=true";

            q = session.createQuery(queryString);
            q.setParameter("toUserId",
                           new Long(fUser.getDigiUserId()),
                           Hibernate.LONG);

            q.setCacheable(true);
            q.setCacheRegion(CACHE_REGION);
            pvtMsgCount = ( (Integer) q.uniqueResult()).intValue();
        }
        catch (Exception ex) {
            logger.debug("Unable to get new pm count ", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }

        return pvtMsgCount;
    }

    public static ForumPrivateMessage getPrivateMsgItem(long id) throws
        ForumException {
        Session session = null;
        ForumPrivateMessage pm = null;
        try {
            session = PersistenceManager.getSession();
            pm = (ForumPrivateMessage) session.load(ForumPrivateMessage.class,
                new Long(id));
        }
        catch (Exception ex) {
            throw new ForumException(
                "Unable to get private message", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
            }
        }
        return pm;
    }

    public static void updatePrivateMsg(ForumPrivateMessage pm) throws
        ForumException {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = PersistenceManager.getSession();
            tx = sess.beginTransaction();
            sess.saveOrUpdate(pm);
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
            logger.debug("Unable to update pm", ex);
            throw new ForumException(
                "Unable to update pm", ex);
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

    public static void updatePrivateMsgList(List msgList) throws ForumException {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = PersistenceManager.getSession();
            tx = sess.beginTransaction();
            Iterator it = msgList.iterator();
            while (it.hasNext()) {
                ForumPrivateMessage pm = (ForumPrivateMessage) it.next();
                sess.saveOrUpdate(pm);
            }
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
            logger.debug("Unable to update pm", ex);
            throw new ForumException(
                "Unable to update pm", ex);
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

    public static int getNewThreadCount(Long forumId, Date lastActive) {
        int threadCount = 0;
        Session session = null;
        Query q = null;
        try {
            session = PersistenceManager.getSession();
            String queryString = "";
            if (lastActive == null) {
                queryString = "select count(*) from " +
                    ForumThread.class.getName() +
                    " rc, where rc.subsection.section.forum.id=:forumId";
            }
            else {
                queryString = "select count(*) from " +
                    ForumThread.class.getName() +
                    " rc, where rc.creationDate >= :lastActive " +
                    "and rc.subsection.section.forum.id=:forumId";
            }
            q = session.createQuery(queryString);

            if (lastActive != null) {
                Calendar notDate = Calendar.getInstance();
                notDate.setTime(lastActive);
                q.setCalendar("lastActive", notDate);
            }
            q.setParameter("forumId", forumId, Hibernate.LONG);
            q.setCacheable(true);
            q.setCacheRegion(CACHE_REGION);
            threadCount = ( (Integer) q.uniqueResult()).intValue();
        }
        catch (Exception ex) {
            logger.debug("Unable to get new thread count ", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }
        return threadCount;
    }

    public static int getTotalThreadCount(Long forumId) {
        return getNewThreadCount(forumId, null);
    }

    public static int getNewPostCount(Long forumId, Date lastActive) {
        int postCount = 0;
        Session session = null;
        Query q = null;
        try {
            session = PersistenceManager.getSession();
            String queryString = "";
            if (lastActive == null) {
                queryString = "select count(*) from " +
                    ForumPost.class.getName() +
                    " rc, where rc.thread.subsection.section.forum.id=:forumId";
            }
            else {
                queryString = "select count(*) from " +
                    ForumPost.class.getName() +
                    " rc, where rc.postTime >= :lastActive and " +
                    "rc.thread.subsection.section.forum.id=:forumId";
            }
            q = session.createQuery(queryString);

            if (lastActive != null) {
                Calendar notDate = Calendar.getInstance();
                notDate.setTime(lastActive);
                q.setCalendar("lastActive", notDate);
            }
            q.setParameter("forumId", forumId, Hibernate.LONG);
            q.setCacheable(true);
            q.setCacheRegion(CACHE_REGION);
            postCount = ( (Integer) q.uniqueResult()).intValue();
        }
        catch (Exception ex) {
            logger.debug("Unable to get new post count ", ex);
        }
        finally {
            try {
                PersistenceManager.releaseSession(session);
            }
            catch (Exception ex2) {
                logger.warn("releaseSession() failed ", ex2);
            }
        }
        return postCount;
    }

    public static int getTotalPostCount(Long forumId) {
        return getNewPostCount(forumId, null);
    }

}