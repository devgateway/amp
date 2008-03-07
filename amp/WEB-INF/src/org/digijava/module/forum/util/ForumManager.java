/*
*   ForumManager.java
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.upload.FormFile;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.User;
import org.digijava.module.forum.dbentity.Forum;
import org.digijava.module.forum.dbentity.ForumAsset;
import org.digijava.module.forum.dbentity.ForumSection;
import org.digijava.module.forum.dbentity.ForumSubsection;
import org.digijava.module.forum.dbentity.ForumThread;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import org.digijava.module.forum.exception.ForumException;

public class ForumManager {
    private static Map forumPageTrailCallbackMap;
    private static Map adminPageTrailCallbackMap;

    public static String[] imgsTypes = {"jpg", "bmp", "gif", "png"};

    private static SimpleDateFormat dateTimeFormater =
        new SimpleDateFormat("MMMMM dd, yyyy hh:mm aaa");
    

    private static SimpleDateFormat dateFormater =
        new SimpleDateFormat("dd MMMMM, yyyy");

    static {
        forumPageTrailCallbackMap = getForumPageTrailMap();
        adminPageTrailCallbackMap = getAdminPageTrailMap();
    }

    public static Forum getForum(ModuleInstance moduleInstance) {
        Forum retVal = null;
        String siteId;
        String instanceId;
        if (moduleInstance.getRealInstance() == null) {
            siteId = moduleInstance.getSite().getSiteId();
            instanceId = moduleInstance.getInstanceName();
        }
        else {
            siteId = moduleInstance.getRealInstance().getSite().getSiteId();
            instanceId = moduleInstance.getRealInstance().getInstanceName();
        }
        if (siteId != null && siteId.length() > 0 &&
            instanceId != null && instanceId.length() > 0) {
            retVal = DbUtil.getForumItem(siteId, instanceId);
        }
        return retVal;
    }

    public static Forum getForumForTeaser(ModuleInstance moduleInstance) {
            Forum retVal = null;
            String siteId;
            String instanceId;
            if (moduleInstance.getRealInstance() == null) {
                siteId = moduleInstance.getSite().getSiteId();
                instanceId = moduleInstance.getInstanceName();
            }
            else {
                siteId = moduleInstance.getRealInstance().getSite().getSiteId();
                instanceId = moduleInstance.getRealInstance().getInstanceName();
            }
            if (siteId != null && siteId.length() > 0 &&
                instanceId != null && instanceId.length() > 0) {
                retVal = DbUtil.getForumItemForTeaser(siteId, instanceId);
            }
            return retVal;
        }

    public static String getFormatedDateTime(Date date) {
        String retVal = "";
        if (date != null) {
            try {
                retVal = dateTimeFormater.format(date);
            }
            catch (Exception ex) {
            }
        }
        return retVal;
    }

    public static String getFormatedDate(Date date) {
        String retVal = "";
        if (date != null) {
            try {
                retVal = dateFormater.format(date);
            }
            catch (Exception ex) {
            }
        }
        return retVal;
    }

    public static ForumUserSettings getForumUser(User user, long forumId)
    throws ForumException {
        ForumUserSettings fUser = null;
        if (user != null && forumId != 0) {
            fUser = DbUtil.getForumUserItem(user.getId(), new Long(forumId));
        }
        return fUser;
    }

    public static boolean isNewPostInThread(int threadId,
                                            ForumUserSettings forumUserSettings,
                                            Map visitedThreadsMap) {
        boolean retVal = false;

        try {
            ForumThread thread = DbUtil.getThreadItem(threadId);
            retVal = isNewPostInThread(thread,
                                       forumUserSettings,
                                       visitedThreadsMap);

        }
        catch (Exception ex) {
        }
        return retVal;
    }

    public static boolean isNewPostInThread(ForumThread thread,
                                            ForumUserSettings forumUserSettings,
                                            Map visitedThreadsMap) {
        boolean retVal = false;
        Date visited = (Date) visitedThreadsMap.get(new Long(thread.getId()));

        if (visited == null &&
            forumUserSettings.getLastActiveTime() != null) {
            visited = forumUserSettings.getLastActiveTime();
        }

        if (visited != null) {
            if (thread.getLastPost() != null &&
                thread.getLastPost().getAuthorUserSettings() != null) {
                if (visited.before(thread.getLastPost().getPostTime()) &&
                    thread.getLastPost().getAuthorUserSettings().getId() !=
                    forumUserSettings.getId()) {
                    retVal = true;
                }
            }
        }
        else {
            retVal = true;
        }

        return retVal;
    }

    public static boolean isNewPostInSubsection(int subsId,
                                                ForumUserSettings
                                                forumUserSettings,
                                                Map visitedThreadsMap) {
        boolean retVal = false;
        try {
            List threadList = DbUtil.getSubsectionPostIdsAfterLastVisit(subsId,
                forumUserSettings.getId(),
                forumUserSettings.getLastActiveTime());

            Iterator it = threadList.iterator();
            while (it.hasNext()) {
                Object[] vals = (Object[]) it.next();
                Long threadId = (Long) vals[0];
                Date lastPostTime = (Date) vals[1];
                Date visitedDate = (Date) visitedThreadsMap.get(threadId);
                if (visitedDate == null ||
                    visitedDate.before(lastPostTime)) {
                    retVal = true;
                    break;
                }
            }
        }
        catch (Exception ex) {
        }
        return retVal;
    }

    public static Map getForumPageTrailMap() {
        Map forumPageTrail = new HashMap();
        forumPageTrail.put(ForumThread.class,
                           new LocationTrailItemCallback() {
            public String getCaption(Object o) {
                return ( (ForumThread) o).getTitle();
            }

            public String getParameteName(Object o) {
                return "threadId";
            }

            public long getNavigationParameter(Object o) {
                return ( (ForumThread) o).getId();
            }

            public Object getParentObject(Object o) {
                return ( (ForumThread) o).getSubsection();
            }

            public String getActionName(Object o) {
                return "showThread.do";
            }
        }
        );

        forumPageTrail.put(ForumSubsection.class,
                           new LocationTrailItemCallback() {
            public String getCaption(Object o) {
                return ( (ForumSubsection) o).getTitle();
            }

            public String getParameteName(Object o) {
                return "subsectionId";
            }

            public long getNavigationParameter(Object o) {
                return ( (ForumSubsection) o).getId();
            }

            public Object getParentObject(Object o) {
                return ( (ForumSubsection) o).getSection();
            }

            public String getActionName(Object o) {
                return "showSubsection.do";
            }

        }
        );

        forumPageTrail.put(ForumSection.class,
                           new LocationTrailItemCallback() {
            public String getCaption(Object o) {
                return ( (ForumSection) o).getTitle();
            }

            public String getParameteName(Object o) {
                return "sectionId";
            }

            public long getNavigationParameter(Object o) {
                return ( (ForumSection) o).getId();
            }

            public Object getParentObject(Object o) {
                return ( (ForumSection) o).getForum();
            }

            public String getActionName(Object o) {
                return "showSection.do";
            }

        }
        );

        forumPageTrail.put(Forum.class,
                           new LocationTrailItemCallback() {
            public String getCaption(Object o) {
                return ( (Forum) o).getName();
            }

            public String getParameteName(Object o) {
                return null;
            }

            public long getNavigationParameter(Object o) {
                return ( (Forum) o).getId();
            }

            public Object getParentObject(Object o) {
                return null;
            }

            public String getActionName(Object o) {
                return "index.do";
            }
        }
        );
        return forumPageTrail;
    }

    public static Map getAdminPageTrailMap() {
            Map forumPageTrail = new HashMap();
            forumPageTrail.put(ForumThread.class,
                               new LocationTrailItemCallback() {
                public String getCaption(Object o) {
                    return ( (ForumThread) o).getTitle();
                }

                public String getParameteName(Object o) {
                    return "threadId";
                }

                public long getNavigationParameter(Object o) {
                    return ( (ForumThread) o).getId();
                }

                public Object getParentObject(Object o) {
                    return ( (ForumThread) o).getSubsection();
                }

                public String getActionName(Object o) {
                    return "adminShowThread.do";
                }
            }
            );

            forumPageTrail.put(ForumSubsection.class,
                               new LocationTrailItemCallback() {
                public String getCaption(Object o) {
                    return ( (ForumSubsection) o).getTitle();
                }

                public String getParameteName(Object o) {
                    return "subsectionId";
                }

                public long getNavigationParameter(Object o) {
                    return ( (ForumSubsection) o).getId();
                }

                public Object getParentObject(Object o) {
                    return ( (ForumSubsection) o).getSection();
                }

                public String getActionName(Object o) {
                    return "adminShowSubsection.do";
                }

            }
            );

            forumPageTrail.put(ForumSection.class,
                               new LocationTrailItemCallback() {
                public String getCaption(Object o) {
                    return ( (ForumSection) o).getTitle();
                }

                public String getParameteName(Object o) {
                    //return "sectionId";
                    return null;
                }

                public long getNavigationParameter(Object o) {
                    return ( (ForumSection) o).getId();
                }

                public Object getParentObject(Object o) {
                    return ( (ForumSection) o).getForum();
                }

                public String getActionName(Object o) {
                    //return "adminShowSection.do";
                    return null;
                }

            }
            );

            forumPageTrail.put(Forum.class,
                               new LocationTrailItemCallback() {
                public String getCaption(Object o) {
                    return ( (Forum) o).getName();
                }

                public String getParameteName(Object o) {
                    return null;
                }

                public long getNavigationParameter(Object o) {
                    return ( (Forum) o).getId();
                }

                public Object getParentObject(Object o) {
                    return null;
                }

                public String getActionName(Object o) {
                    return "adminShowAdminIndex.do";
                }
            }
            );
            return forumPageTrail;
        }


    public static List getLastPosts (long forumId, int lastCount) {
        List retVal = new ArrayList();
        try {
            List subsectionList = DbUtil.getSubsections(new Long(forumId));
            if (subsectionList.size()>0) {
                Iterator it = subsectionList.iterator();
                List threadList = new ArrayList();
                while (it.hasNext()) {
                    ForumSubsection subs = (ForumSubsection) it.next();
                    List subsTreadList = DbUtil.getThreads(new
                        Long(subs.getId()), 0, 3);
                    if (subsTreadList != null) {
                        threadList.addAll(subsTreadList);
                    }
                }

                Comparator lastPostDateComparator = new Comparator(){
                    public int compare (Object o1, Object o2) {
                        ForumThread thr1 = (ForumThread) o1;
                        ForumThread thr2 = (ForumThread) o2;

                        int retVal = 0;

                        if (thr1.getLastPost() == null) {
                            retVal = -1;
                        } else if (thr2.getLastPost() == null) {
                            retVal = 1;
                        } else {
                            retVal = thr2.getLastPost().getPostTime().
                                compareTo(thr1.getLastPost().getPostTime());
                        }

                        return retVal;
                    }

                };

                Collections.sort(threadList, lastPostDateComparator);

                for (int thrInd = 0;
                     thrInd < threadList.size() && thrInd < lastCount; thrInd ++) {
                    ForumThread thread = (ForumThread) threadList.get(thrInd);
                    if (thread.getLastPost() != null) {
                        retVal.add(thread);
                    }

                }
            }
        }
        catch (ForumException ex) {
        }
        return retVal;
    }

    public static String getUplodadedFileBBTag (FormFile ff,
                                                String relPath,
                                                ForumUserSettings forumUser,
                                                ActionErrors errors) {
      String retVal = "";
                  byte[] fileData = null;
                  try {
                      fileData = ff.getFileData();
                  }
                  catch (IOException ex4) {
                      errors.add("forumGlobalError",
                                 new ActionError("error.forum.fileUploadError"));
                  }
                  if (fileData != null && fileData.length > 0) {
                      String fileName = ff.getFileName();
                      int fileSize = ff.getFileSize();
                      String contentType = ff.getContentType();

                      ForumAsset newAsset = new ForumAsset(fileName, fileData);
                      newAsset.setContentType(contentType);
                      newAsset.setCreationDate(new Date());
                      newAsset.setFileSize(fileSize);
                      if (forumUser != null) {
                          newAsset.setForumUserId(forumUser.getId());
                      }
                      try {
                          DbUtil.createAsset(newAsset);
                      }
                      catch (ForumException ex5) {
                          errors.add("forumGlobalError",
                                 new ActionError("error.forum.pm.userNotFound"));
                      }

                      StringBuffer imgTag = new StringBuffer();
                      if (isImgType(fileName)) {
                        imgTag.append("\r[img]");
                        imgTag.append(relPath);
                        imgTag.append("/getFile.do?assetId=");
                        imgTag.append(newAsset.getId());
                        imgTag.append("[/img]");
                      } else {
                        imgTag.append("\r[url=");
                        relPath = relPath.replaceAll("/", "\\\\");
                        imgTag.append(relPath);
                        imgTag.append("\\getFile.do~");
                        imgTag.append(newAsset.getId());
                        imgTag.append("]");
                        imgTag.append(fileName);
                        imgTag.append("[/url]");

                      }
                      retVal = imgTag.toString();
                      ff.destroy();
                      ff = null;
                  }
      return retVal;
    }

    private static boolean isImgType(String fileName) {
      boolean retVal = false;
      if (fileName.indexOf('.') > -1) {
        String ext = fileName.substring(fileName.lastIndexOf('.') + 1,
                                        fileName.length());
        for (int extIndex = 0; extIndex < imgsTypes.length; extIndex++) {
          if (ext.equals(imgsTypes[extIndex])) {
            retVal = true;
            break;
          }
        }
      }
      return retVal;
    }

    public static Map getForumPageTrailCallbackMap() {
        return forumPageTrailCallbackMap;
    }

    public static Map getAdminPageTrailCallbackMap() {
        return adminPageTrailCallbackMap;
    }



    public static int getNewThreadCount(long forumId, Date from) {
        return DbUtil.getNewThreadCount(new Long(forumId), from);
    }

    public static int getTotalThreadCount(long forumId) {
        return DbUtil.getTotalThreadCount(new Long(forumId));
    }

    public static int getNewPostCount(long forumId, Date from) {
        return DbUtil.getNewPostCount(new Long(forumId), from);
    }

    public static int getTotalPostCount(long forumId) {
        return DbUtil.getTotalPostCount(new Long(forumId));
    }
}