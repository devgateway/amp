/*
*   DbUtil.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id: DbUtil.java,v 1.1 2005-07-06 10:34:32 rahul Exp $
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

package org.digijava.module.poll.util;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.poll.entity.Poll;
import org.digijava.module.poll.entity.PollOption;
import org.digijava.module.poll.entity.PollVoter;
import org.digijava.module.poll.exception.PollException;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class DbUtil {

    private static Logger logger = Logger.getLogger(DbUtil.class);

//  private static SessionFactory sf;

    /**
     * update vote
     *
     * @param pollOptionId poll option unique id
     * @param ipAddress voter ip address
     * @throws ServletException
     */
    public static void updateVote(long pollOptionId, String ipAddress) throws
        PollException {

        Session sess = null;
        try {
            sess = PersistenceManager.getSession();

            Transaction tx = null;

            // update vote number
            tx = sess.beginTransaction();
            PollOption option = (PollOption) sess.load(PollOption.class,
                new Long(pollOptionId));
            option.setVotes(option.getVotes() + 1);
            sess.update(option);

            // insert voter
            PollVoter voter = new PollVoter();
            voter.setIpAddress(ipAddress);
            voter.setPoll(option.getPoll());
            sess.save(voter);
            tx.commit();

        }
        catch (Exception ex) {
            logger.debug("Unable to update information into database", ex);
            throw new PollException(
                "Unable to update information into database",
                ex);
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

    /**
     * Populate poll object
     *
     * @param key poll teaser key
     * @return Poll object
     * @throws ServletException
     */
    public static Poll getPoll(String key) throws
        PollException {

        Poll poll = null;

        // build query
        String query = "from " + Poll.class.getName() +
            " l where l.instanceKey = ?";

        Session sess = null;
        try {
            sess = PersistenceManager.getSession();

            Iterator iter = sess.iterate(query, key, Hibernate.STRING);
            while (iter.hasNext()) {
                poll = (Poll) iter.next();
                break;
            }

        }
        catch (Exception ex) {
            logger.debug("Unable to get information from database", ex);
            throw new PollException(
                "Unable to get information from database", ex);
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

        return poll;
    }

    /**
     * Check if user vote poll,
     * return true if voted otherwise false
     *
     * @param ipAddress voter ip address
     * @param pollId poll unique id
     * @return true if voted otherwise false
     * @throws ServletException
     */
    public static boolean isVoted(String ipAddress, long pollId) throws
        PollException {

        Poll poll = null;

        // build query
        String query = "from " + PollVoter.class.getName() +
            " l where l.ipAddress = ? and l.poll = " + pollId;

        Session sess = null;
        try {
            sess = PersistenceManager.getSession();

            Iterator iter = sess.iterate(query, ipAddress, Hibernate.STRING);
            if (iter.hasNext()) {
                return true;
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get information from database", ex);
            throw new PollException(
                "Unable to get information from database", ex);
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

        return false;
    }

}