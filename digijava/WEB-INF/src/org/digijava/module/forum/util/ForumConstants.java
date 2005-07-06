/*
*   ForumConstants.java
*   @Author George Kvizhinadze gio@digijava.org
*   Created: Mar 15, 2004
*   CVS-ID: $Id: ForumConstants.java,v 1.1 2005-07-06 10:34:19 rahul Exp $
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

public class ForumConstants {

    public final static String VISITED_SESSION_MAP = "VISITED_SESSION_MAP";

    public static final int MODE_NEW = 0;
    public static final int MODE_EDIT = 1;

    public static final int MOVE_UP = 0;
    public static final int MOVE_DOWN = 1;

    public static final String PM_FOLDER_INBOX = "inbox";
    public static final String PM_FOLDER_SENTBOX = "sentbox";
    public static final String PM_FOLDER_OUTBOX = "outbox";
    public static final String PM_FOLDER_SAVEBOX = "savebox";

    public static final String PM_FOLDER_DEL_ITEMS = "delItems";
    public static final String PM_FOLDER_SENT = "sentMessages";


    public static int FILTER_POSTS_ALL = 0;
    public static int FILTER_POSTS_ONE_DAY = 1;
    public static int FILTER_POSTS_ONE_WEEK = 2;
    public static int FILTER_POSTS_TWO_WEEKS = 3;
    public static int FILTER_POSTS_ONE_MONTH = 4;
    public static int FILTER_POSTS_THREE_MONTH = 5;
    public static int FILTER_POSTS_SIX_MONTH = 6;
    public static int FILTER_POSTS_ONE_YEAR = 7;

    public static int SORT_ASC = 0;
    public static int SORT_DESC = 1;
}