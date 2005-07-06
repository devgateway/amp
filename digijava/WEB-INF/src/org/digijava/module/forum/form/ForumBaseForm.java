/*
*   ForumBaseForm.java
*   @Author George Kvizhinadze gio@digijava.org
*   Created:
*   CVS-ID: $Id: ForumBaseForm.java,v 1.1 2005-07-06 10:34:31 rahul Exp $
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

package org.digijava.module.forum.form;

import org.digijava.module.forum.dbentity.*;
import java.util.Map;
import java.util.List;
import org.apache.struts.upload.FormFile;

public class ForumBaseForm
    extends ForumPaginationBase {
    //Common parameters
    private String[] checkboxList;

    private Forum forum;
    private ForumSection forumSection;
    private ForumSubsection forumSubsection;
    private ForumThread forumThread;
    private ForumPost forumPost;
    private ForumPrivateMessage privateMessage;
    private ForumUserSettings forumUserSettings;
    private ForumAsset forumAsset;
    private Map visitedThreadsMap;
    private List threadList;
    private List postList;
    private FormFile formFile;

    // forum object parameters
    private long forumId;
    private String forumName;
    private int topicsPerPage;
    private int postsPerPage;
    private int systemTimezone;
    private int minMessageLength;
    private int maxImageSize;
    private int maxImageWidth;
    private int maxImageHeigth;
    private int maxUploadSize;
    private int postIntervalLimit;

    //Section object parameters
    private long sectionId;
    private String sectionTitle;
    private String sectionComment;
    private int sectionOrderIndex;
    private int accessLevel;

    //Subsection object parameters
    private long subsectionId;
    private String subsectionTitle;
    private String subsectionComment;
    private int subsectionOrderIndex;
    private int threadCount;
    private int totalPosts;
    private boolean subsectionLocked;
    private boolean requiresPublishing;
    private boolean sendApproveMsg;
    private String approveMsg;
    private boolean sendDeleteMsg;
    private String deleteMsg;

    //Thread obejct parameters
    private long threadId;
    private String threadTitle;
    private String threadComment;
    private boolean threadLocked;
    private long threadAuthorUserId;
    private int threadPostCount;
    private boolean threadHasNewPost;

    //Message(post pm base class) parameters
    private long postId;
    private long messageDigiUserId;
    private String postTitle;
    private String postContent;
    private boolean enableEmotions;
    private boolean allowHtml;

    //Post
    private boolean notifyOnReply;
    private boolean published;

    //PM
    private boolean isNew;
    private long toUserId;
    private String folderName;

    //User settings parameters
    private long userSettingsId;
    private long userDigiUserId;
    private String nickName;
    private String signature;
    private int userTotalPosts;
    private String avatarUrl;
    private String userLocation;

    //Asset object parameters
    private long assetId;
    private String srcFileName;
    private long fileSize;
    private long forumUserId;
    private String contentType;

    public void setForumParams(Forum forum) {
        this.forumId = forum.getId();
        this.forumName = forum.getName();
        this.topicsPerPage = forum.getTopicsPerPage();
        this.postsPerPage = forum.getPostsPerPage();
        this.systemTimezone = forum.getSystemTimezone();
        this.minMessageLength = forum.getMinMessageLength();
        this.maxImageSize = forum.getMaxImageSize();
        this.maxImageWidth = forum.getMaxImageWidth();
        this.maxImageHeigth = forum.getMaxImageHeigth();
        this.maxUploadSize = forum.getMaxUploadSize();
        this.postIntervalLimit = forum.getPostIntervalLimit();
    }

    public void fillForum(Forum forum) {
        forum.setName(this.forumName);
        forum.setTopicsPerPage(this.topicsPerPage);
        forum.setPostsPerPage(this.postsPerPage);
        forum.setSystemTimezone(this.systemTimezone);
        forum.setMinMessageLength(this.minMessageLength);
        forum.setMaxImageSize(this.maxImageSize);
        forum.setMaxImageWidth(this.maxImageWidth);
        forum.setMaxImageHeigth(this.maxImageHeigth);
        forum.setMaxUploadSize(this.maxUploadSize);
        forum.setPostIntervalLimit(this.postIntervalLimit);
    }

    public void setSectionParams(ForumSection section) {
        this.sectionId = section.getId();
        this.sectionTitle = section.getTitle();
        this.sectionComment = section.getComment();
        this.sectionOrderIndex = section.getOrderIndex();
        this.accessLevel = section.getAccessLevel();
    }

    public void fillSection(ForumSection section) {
        section.setTitle(this.sectionTitle);
        section.setComment(this.sectionComment);
        section.setOrderIndex(this.sectionOrderIndex);
        section.setAccessLevel(this.accessLevel);
    }

    public void setSubsectionParams (ForumSubsection subsection) {
        this.subsectionId = subsection.getId();
        this.subsectionTitle = subsection.getTitle();
        this.subsectionComment = subsection.getComment();
        this.subsectionOrderIndex = subsection.getOrderIndex();
        this.threadCount = subsection.getThreadCount();
        this.totalPosts = subsection.getTotalPosts();
        this.subsectionLocked = subsection.getLocked();
        this.requiresPublishing = subsection.getRequiresPublishing();
        this.sendApproveMsg = subsection.getSendApproveMsg();
        this.approveMsg = subsection.getApproveMsg();
        this.sendDeleteMsg = subsection.getSendDeleteMsg();
        this.deleteMsg = subsection.getDeleteMsg();
    }

    public void fillSubsection (ForumSubsection subsection) {
        subsection.setTitle (this.subsectionTitle);
        subsection.setComment(this.subsectionComment);
        subsection.setOrderIndex(this.subsectionOrderIndex);
        subsection.setThreadCount(this.threadCount);
        subsection.setTotalPosts(this.totalPosts);
        subsection.setLocked(this.subsectionLocked);
        subsection.setRequiresPublishing(this.requiresPublishing);
        subsection.setSendApproveMsg(this.sendApproveMsg);
        subsection.setApproveMsg(this.approveMsg);
        subsection.setSendDeleteMsg(this.sendDeleteMsg);
        subsection.setDeleteMsg(this.deleteMsg);
    }

    public void setThreadParams (ForumThread thread) {
        this.threadId = thread.getId();
        this.threadTitle = thread.getTitle();
        this.threadComment = thread.getComment();
        this.threadLocked = thread.isLocked();
        this.threadAuthorUserId = thread.getAuthorUserId();
        this.threadPostCount = thread.getPostCount();
        this.threadHasNewPost = thread.getHasNewPost();
    }

    public void fillThread (ForumThread thread) {
        thread.setTitle(this.threadTitle);
        thread.setComment(this.threadComment);
        thread.setLocked(this.threadLocked);
        thread.setAuthorUserId(this.threadAuthorUserId);
        thread.setPostCount(this.threadPostCount);
        thread.setHasNewPost(this.threadHasNewPost);
    }

    public void setPostParams (ForumPost post) {
        this.postId = post.getId();
        this.messageDigiUserId = post.getDigiUserId();
        this.postTitle = post.getTitle();
        this.postContent = post.getContent();
        this.enableEmotions = post.getEnableEmotions();
        this.allowHtml = post.getAllowHtml();
        this.notifyOnReply = post.getNotifyOnReply();
        this.published = post.getPublished();
    }

    public void fillPost (ForumPost post) {
        post.setDigiUserId(this.messageDigiUserId);
        post.setTitle(this.postTitle);
        post.setContent(this.postContent);
        post.setEnableEmotions(this.enableEmotions);
        post.setAllowHtml(this.allowHtml);
        post.setNotifyOnReply(this.notifyOnReply);
        post.setPublished(this.published);
    }

    public void setPMParamsParams (ForumPrivateMessage pm) {
        this.postId = pm.getId();
        this.messageDigiUserId = pm.getDigiUserId();
        this.postTitle = pm.getTitle();
        this.postContent = pm.getContent();
        this.enableEmotions = pm.getEnableEmotions();
        this.allowHtml = pm.getAllowHtml();
        this.isNew = pm.getIsNew();
        this.toUserId = pm.getToUserId();
        this.folderName = pm.getFolderName();
    }

    public void fillPM (ForumPrivateMessage pm) {
        pm.setDigiUserId(this.messageDigiUserId);
        pm.setTitle(this.postTitle);
        pm.setContent(this.postContent);
        pm.setEnableEmotions(this.enableEmotions);
        pm.setAllowHtml(this.allowHtml);
        pm.setIsNew(isNew);
        pm.setToUserId(toUserId);
        pm.setFolderName(folderName);
    }

    public void setUserSettingParams (ForumUserSettings fUser) {
        this.userSettingsId = fUser.getId();
        this.userDigiUserId = fUser.getDigiUserId();
        this.nickName = fUser.getNickName();
        this.signature = fUser.getSignature();
        this.userTotalPosts = fUser.getTotalPosts();
        this.avatarUrl = fUser.getAvatarUrl();
        this.userLocation = fUser.getLocation();
    }

    public void fillUserSetting (ForumUserSettings fUser) {
        fUser.setNickName(this.nickName);
        fUser.setSignature(this.signature);
        fUser.setTotalPosts(this.userTotalPosts);
        fUser.setAvatarUrl(this.avatarUrl);
        fUser.setLocation(this.userLocation);
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public boolean isAllowHtml() {
        return allowHtml;
    }

    public void setAllowHtml(boolean allowHtml) {
        this.allowHtml = allowHtml;
    }

    public String getApproveMsg() {
        return approveMsg;
    }

    public void setApproveMsg(String approveMsg) {
        this.approveMsg = approveMsg;
    }

    public long getAssetId() {
        return assetId;
    }

    public void setAssetId(long assetId) {
        this.assetId = assetId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String[] getCheckboxList() {
        return checkboxList;
    }

    public void setCheckboxList(String[] checkboxList) {
        this.checkboxList = checkboxList;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDeleteMsg() {
        return deleteMsg;
    }

    public void setDeleteMsg(String deleteMsg) {
        this.deleteMsg = deleteMsg;
    }

    public boolean isEnableEmotions() {
        return enableEmotions;
    }

    public void setEnableEmotions(boolean enableEmotions) {
        this.enableEmotions = enableEmotions;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public FormFile getFormFile() {
        return formFile;
    }

    public void setFormFile(FormFile formFile) {
        this.formFile = formFile;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public ForumAsset getForumAsset() {
        return forumAsset;
    }

    public void setForumAsset(ForumAsset forumAsset) {
        this.forumAsset = forumAsset;
    }

    public String getForumName() {
        return forumName;
    }

    public void setForumName(String forumName) {
        this.forumName = forumName;
    }

    public ForumPost getForumPost() {
        return forumPost;
    }

    public void setForumPost(ForumPost forumPost) {
        this.forumPost = forumPost;
    }

    public ForumSection getForumSection() {
        return forumSection;
    }

    public void setForumSection(ForumSection forumSection) {
        this.forumSection = forumSection;
    }

    public ForumSubsection getForumSubsection() {
        return forumSubsection;
    }

    public void setForumSubsection(ForumSubsection forumSubsection) {
        this.forumSubsection = forumSubsection;
    }

    public ForumThread getForumThread() {
        return forumThread;
    }

    public void setForumThread(ForumThread forumThread) {
        this.forumThread = forumThread;
    }

    public long getForumUserId() {
        return forumUserId;
    }

    public void setForumUserId(long forumUserId) {
        this.forumUserId = forumUserId;
    }

    public ForumUserSettings getForumUserSettings() {
        return forumUserSettings;
    }

    public void setForumUserSettings(ForumUserSettings forumUserSettings) {
        this.forumUserSettings = forumUserSettings;
    }

    public boolean isIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public int getMaxImageHeigth() {
        return maxImageHeigth;
    }

    public void setMaxImageHeigth(int maxImageHeigth) {
        this.maxImageHeigth = maxImageHeigth;
    }

    public int getMaxImageSize() {
        return maxImageSize;
    }

    public void setMaxImageSize(int maxImageSize) {
        this.maxImageSize = maxImageSize;
    }

    public int getMaxImageWidth() {
        return maxImageWidth;
    }

    public void setMaxImageWidth(int maxImageWidth) {
        this.maxImageWidth = maxImageWidth;
    }

    public int getMaxUploadSize() {
        return maxUploadSize;
    }

    public void setMaxUploadSize(int maxUploadSize) {
        this.maxUploadSize = maxUploadSize;
    }

    public long getMessageDigiUserId() {
        return messageDigiUserId;
    }

    public void setMessageDigiUserId(long messageDigiUserId) {
        this.messageDigiUserId = messageDigiUserId;
    }

    public int getMinMessageLength() {
        return minMessageLength;
    }

    public void setMinMessageLength(int minMessageLength) {
        this.minMessageLength = minMessageLength;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isNotifyOnReply() {
        return notifyOnReply;
    }

    public void setNotifyOnReply(boolean notifyOnReply) {
        this.notifyOnReply = notifyOnReply;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public int getPostIntervalLimit() {
        return postIntervalLimit;
    }

    public void setPostIntervalLimit(int postIntervalLimit) {
        this.postIntervalLimit = postIntervalLimit;
    }

    public List getPostList() {
        return postList;
    }

    public void setPostList(List postList) {
        this.postList = postList;
    }

    public int getPostsPerPage() {
        return postsPerPage;
    }

    public void setPostsPerPage(int postsPerPage) {
        this.postsPerPage = postsPerPage;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public ForumPrivateMessage getPrivateMessage() {
        return privateMessage;
    }

    public void setPrivateMessage(ForumPrivateMessage privateMessage) {
        this.privateMessage = privateMessage;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isRequiresPublishing() {
        return requiresPublishing;
    }

    public void setRequiresPublishing(boolean requiresPublishing) {
        this.requiresPublishing = requiresPublishing;
    }

    public String getSectionComment() {
        return sectionComment;
    }

    public void setSectionComment(String sectionComment) {
        this.sectionComment = sectionComment;
    }

    public long getSectionId() {
        return sectionId;
    }

    public void setSectionId(long sectionId) {
        this.sectionId = sectionId;
    }

    public int getSectionOrderIndex() {
        return sectionOrderIndex;
    }

    public void setSectionOrderIndex(int sectionOrderIndex) {
        this.sectionOrderIndex = sectionOrderIndex;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public boolean isSendApproveMsg() {
        return sendApproveMsg;
    }

    public void setSendApproveMsg(boolean sendApproveMsg) {
        this.sendApproveMsg = sendApproveMsg;
    }

    public boolean isSendDeleteMsg() {
        return sendDeleteMsg;
    }

    public void setSendDeleteMsg(boolean sendDeleteMsg) {
        this.sendDeleteMsg = sendDeleteMsg;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSrcFileName() {
        return srcFileName;
    }

    public void setSrcFileName(String srcFileName) {
        this.srcFileName = srcFileName;
    }

    public long getSubsectionId() {
        return subsectionId;
    }

    public void setSubsectionId(long subsectionId) {
        this.subsectionId = subsectionId;
    }

    public boolean isSubsectionLocked() {
        return subsectionLocked;
    }

    public void setSubsectionLocked(boolean subsectionLocked) {
        this.subsectionLocked = subsectionLocked;
    }

    public int getSubsectionOrderIndex() {
        return subsectionOrderIndex;
    }

    public void setSubsectionOrderIndex(int subsectionOrderIndex) {
        this.subsectionOrderIndex = subsectionOrderIndex;
    }

    public int getSystemTimezone() {
        return systemTimezone;
    }

    public void setSystemTimezone(int systemTimezone) {
        this.systemTimezone = systemTimezone;
    }

    public long getThreadAuthorUserId() {
        return threadAuthorUserId;
    }

    public void setThreadAuthorUserId(long threadAuthorUserId) {
        this.threadAuthorUserId = threadAuthorUserId;
    }

    public String getThreadComment() {
        return threadComment;
    }

    public void setThreadComment(String threadComment) {
        this.threadComment = threadComment;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public boolean isThreadHasNewPost() {
        return threadHasNewPost;
    }

    public void setThreadHasNewPost(boolean threadHasNewPost) {
        this.threadHasNewPost = threadHasNewPost;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public List getThreadList() {
        return threadList;
    }

    public void setThreadList(List threadList) {
        this.threadList = threadList;
    }

    public boolean isThreadLocked() {
        return threadLocked;
    }

    public void setThreadLocked(boolean threadLocked) {
        this.threadLocked = threadLocked;
    }

    public int getThreadPostCount() {
        return threadPostCount;
    }

    public void setThreadPostCount(int threadPostCount) {
        this.threadPostCount = threadPostCount;
    }

    public String getThreadTitle() {
        return threadTitle;
    }

    public void setThreadTitle(String threadTitle) {
        this.threadTitle = threadTitle;
    }

    public int getTopicsPerPage() {
        return topicsPerPage;
    }

    public void setTopicsPerPage(int topicsPerPage) {
        this.topicsPerPage = topicsPerPage;
    }

    public int getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(int totalPosts) {
        this.totalPosts = totalPosts;
    }

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public long getUserDigiUserId() {
        return userDigiUserId;
    }

    public void setUserDigiUserId(long userDigiUserId) {
        this.userDigiUserId = userDigiUserId;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public long getUserSettingsId() {
        return userSettingsId;
    }

    public void setUserSettingsId(long userSettingsId) {
        this.userSettingsId = userSettingsId;
    }

    public int getUserTotalPosts() {
        return userTotalPosts;
    }

    public void setUserTotalPosts(int userTotalPosts) {
        this.userTotalPosts = userTotalPosts;
    }

    public Map getVisitedThreadsMap() {
        return visitedThreadsMap;
    }

    public void setVisitedThreadsMap(Map visitedThreadsMap) {
        this.visitedThreadsMap = visitedThreadsMap;
    }
  public long getForumId() {
    return forumId;
  }
  public void setForumId(long forumId) {
    this.forumId = forumId;
  }
    public String getSubsectionComment() {
        return subsectionComment;
    }
    public void setSubsectionComment(String subsectionComment) {
        this.subsectionComment = subsectionComment;
    }
    public String getSubsectionTitle() {
        return subsectionTitle;
    }
    public void setSubsectionTitle(String subsectionTitle) {
        this.subsectionTitle = subsectionTitle;
    }

}