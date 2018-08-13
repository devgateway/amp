package org.digijava.module.contentrepository.helper;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.contentrepository.jcrentity.Label;
import org.digijava.module.contentrepository.util.DocumentOrganizationManager;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

public class DocumentData implements Comparable<DocumentData>, Serializable {
    public static final Comparator<DocumentData> COMPARATOR_BY_NAME = new Comparator<DocumentData>(){
        @Override
        public int compare(DocumentData arg0, DocumentData arg1) {
            return arg0.getName().compareTo(arg1.getName());
        }};
    String name             = null;
    String uuid             = null;
    String title            = null;
    String description      = null;
    String notes            = null;
    String calendar         = null;
    String contentType      = null;
    String webLink          = null;
    // generated field, but need to keep it to be serialized by the ObjectMapper
    String generalLink      = null;
    String cmDocType        = "";
    
    String category = null;
    String index = null;
    String organisations = null;
    
    /**
     * megabytes
     */
    double fileSize         = 0;
    Date date = null;
    String iconPath         = null;
    String yearofPublication    = null;
    
    List<Label> labels      = null;
    
    Long cmDocTypeId        = null;
    Long creatorTeamId      = null;
    String creatorEmail     = null;
    
    float versionNumber;
    
    boolean hasDeleteRights         = false;
    boolean hasViewRights           = false;
    boolean hasShowVersionsRights   = false;
    boolean hasVersioningRights     = false;
    boolean hasMakePublicRights     = false;
    boolean hasDeleteRightsOnPublicVersion  = false;
    boolean hasAddParticipatingOrgRights    = false;
    
    boolean isPublic                    = false;
    boolean lastVersionIsPublic     = false;
    
    boolean showVersionHistory      = true;
    
    /**
     * is it allowed or not to share this document
     */
    boolean hasShareRights =false;
    boolean hasUnshareRights =false;
    /**
     * 
     */
    private boolean hasApproveVersionRights=false;
    /**
     * which level of share is allowed for this document
     */
    private String shareWith= null;
    
    /** 
     * shows whether this document needs approval to become shared or not
     */
    private boolean needsApproval = false;
    private boolean isShared = false; //whether this resource is already shared.
    boolean lastVersionIsShared = false;
    
    private boolean currentVersionNeedsApproval = false;
    private boolean hasAnyVersionPendingApproval; // if any version of the node needs to be approved by Tl to be visible for team-members
    private String baseNodeUUID = null; //in case documentData is just a version of some node, this property holds that main node uuid
    private String nodeVersionUUID = null; //in case documentData is just a version of some node, this property stores it's uuid(this case nodeVersionUUID.equals(uuid))
    
    private Map <String,String> translatedTitles = null;
    private Map <String,String> translatedDescriptions = null;
    private Map <String,String> translatedNotes = null;
    
    
    public DocumentData()
    {
        
    }
    
    public boolean getHasDeleteRights() {
        return hasDeleteRights;
    }
    public void setHasDeleteRights(boolean hasDeleteRights) {
        this.hasDeleteRights = hasDeleteRights;
    }
    
    public boolean getHasShowVersionsRights() {
        return hasShowVersionsRights;
    }
    public void setHasShowVersionsRights(boolean hasShowVersionsRights) {
        this.hasShowVersionsRights = hasShowVersionsRights;
    }
    public boolean getHasVersioningRights() {
        return hasVersioningRights;
    }
    public void setHasVersioningRights(boolean hasVersioningRights) {
        this.hasVersioningRights = hasVersioningRights;
    }
    public boolean getHasViewRights() {
        return hasViewRights;
    }
    public void setHasViewRights(boolean hasViewRights) {
        this.hasViewRights = hasViewRights;
    }
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    public String getCalendar() {
        return calendar;
    }
    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }
    public String getEscapedAmpDescription() {
        if (description != null) {
            String ret = description.replace("'", "\\'").replace("\"", "\\\"").replace("\r", "").replace("\n", "\\n").replace("<", "&lt;").replace(">", "&gt;");
            return ret; 
        }
        else
            return "";
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getEscapedAmpTitle() {
        return title == null ? "" : title.replace("'", "\\'");
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public boolean getHasMakePublicRights() {
        return hasMakePublicRights;
    }
    public void setHasMakePublicRights(boolean hasMakePublicRights) {
        this.hasMakePublicRights = hasMakePublicRights;
    }
    public boolean getIsPublic() {
        return isPublic;
    }
    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
            
    public boolean isLastVersionIsPublic() {
        return lastVersionIsPublic;
    }
    public void setLastVersionIsPublic(boolean lastVersionIsPublic) {
        this.lastVersionIsPublic = lastVersionIsPublic;
    }
    public boolean isHasDeleteRightsOnPublicVersion() {
        return hasDeleteRightsOnPublicVersion;
    }
    public void setHasDeleteRightsOnPublicVersion(boolean hasDeleteRightsOnPublicVersion) {
        this.hasDeleteRightsOnPublicVersion = hasDeleteRightsOnPublicVersion;
    }
    public boolean isShowVersionHistory() {
        return showVersionHistory;
    }
    public void setShowVersionHistory(boolean showVersionHistory) {
        this.showVersionHistory = showVersionHistory;
    }
    
    
    
    /**
     * @return the hasAddParticipatingOrgRights
     */
    public boolean isHasAddParticipatingOrgRights() {
        return hasAddParticipatingOrgRights;
    }
    /**
     * @param hasAddParticipatingOrgRights the hasAddParticipatingOrgRights to set
     */
    public void setHasAddParticipatingOrgRights(boolean hasAddParticipatingOrgRights) {
        this.hasAddParticipatingOrgRights = hasAddParticipatingOrgRights;
    }
    public float getVersionNumber() {
        return versionNumber;
    }
    public void setVersionNumber(float versionNumber) {
        this.versionNumber = versionNumber;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getIconPath() {
        return iconPath;
    }
    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }
        
    public Long getCmDocTypeId() {
        return cmDocTypeId;
    }
    
    public void setCmDocTypeId(Long cmDocTypeId) {
        this.cmDocTypeId = cmDocTypeId;
    }
    
    public String getCmDocType() {
        return cmDocType;
    }
    
/*  public void setCmDocType(String cmDocType) { // This is not needed
        this.cmDocType = cmDocType;
    }*/
    
    /**
     * megabytes
     * @return
     */
    public double getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }
            
    public String getWebLink() {
        return webLink;
    }
    
    public void setWebLink(String webLink) {
        this.webLink = webLink;
        if (webLink != null && !"".equals(webLink)) {
            generalLink = webLink;
        } else {
            generalLink = "/contentrepository/downloadFile.do?uuid=" + this.uuid;
        }
    }
    
    public boolean isHasShareRights() {
        return hasShareRights;
    }
    
    public void setHasShareRights(boolean hasShareRights) {
        this.hasShareRights = hasShareRights;
    }
    
    public String getShareWith() {
        return shareWith;
    }
    
    public void setShareWith(String shareWith) {
        this.shareWith = shareWith;
    }
    
    public boolean isNeedsApproval() {
        return needsApproval;
    }
    public void setNeedsApproval(boolean needsApproval) {
        this.needsApproval = needsApproval;
    }
    
    public void process() {
        if (cmDocTypeId != null) {
            AmpCategoryValue docTypeCv  = CategoryManagerUtil.getAmpCategoryValueFromDb(cmDocTypeId);
            if ( docTypeCv != null ) {
                String translation      = CategoryManagerUtil.translateAmpCategoryValue(docTypeCv);
                cmDocType               = translation;
            }
        }
        if ( webLink != null ) {
            if ( webLink.length() <= 25 )
                name    = webLink;
            else {
                name    = webLink.substring(0, 22) + "...";
            }
        }
    }
    
    public void computeIconPath( boolean forDigiImgTag ) {
        if ( name == null )  {
            iconPath = null;
            return;
        }
        String iconPath     = "";
        String extension    = null;
        if ( webLink == null ) {
            int index           = name.lastIndexOf(".");
            extension           = name.substring(index + 1, name.length()).toLowerCase();
        }
        else 
            extension           = "link";
        if ( extension != null) {
            if (forDigiImgTag) {
                iconPath = "images/icons/"
                        + extension
                        + ".gif";
            }
            else
                iconPath = "/TEMPLATE/ampTemplate/images/icons/"
                    + extension
                    + ".gif";
        }
        
        this.iconPath   = iconPath;
        
        
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    
    
    public int compareTo(DocumentData o) {
        // NULLs come first
        if (this.getDate() == null)
        {
            if (o.getDate() == null)
                return 0;
            return -1;
        }
        if (o.getDate() == null)
            return 1;
        
        return this.getDate().compareTo(o.date);
    }
    
    public boolean isLastVersionIsShared() {
        return lastVersionIsShared;
    }
    
    public void setLastVersionIsShared(boolean lastVersionIsShared) {
        this.lastVersionIsShared = lastVersionIsShared;
    }
    
    public boolean isHasUnshareRights() {
        return hasUnshareRights;
    }
    
    public void setHasUnshareRights(boolean hasUnshareRights) {
        this.hasUnshareRights = hasUnshareRights;
    }   
    
    public boolean getIsShared() {
        return isShared;
    }
    
    public void setIsShared(boolean isShared) {
        this.isShared = isShared;
    }
    
    public boolean isCurrentVersionNeedsApproval() {
        return currentVersionNeedsApproval;
    }
    
    public void setCurrentVersionNeedsApproval(boolean currentVersionNeedsApproval) {
        this.currentVersionNeedsApproval = currentVersionNeedsApproval;
    }
    
    public String getBaseNodeUUID() {
        return baseNodeUUID;
    }
    
    public void setBaseNodeUUID(String baseNodeUUID) {
        this.baseNodeUUID = baseNodeUUID;
    }
    
    public boolean isHasApproveVersionRights() {
        return hasApproveVersionRights;
    }
    
    public void setHasApproveVersionRights(boolean hasApproveVersionRights) {
        this.hasApproveVersionRights = hasApproveVersionRights;
    }
    
    public String getYearofPublication() {
        return yearofPublication;
    }
    
    public void setYearofPublication(String yearofPublication) {
        this.yearofPublication = yearofPublication;
    }
    
    public boolean isHasAnyVersionPendingApproval() {
        return hasAnyVersionPendingApproval;
    }
    
    public void setHasAnyVersionPendingApproval(boolean hasAnyVersionPendingApproval) {
        this.hasAnyVersionPendingApproval = hasAnyVersionPendingApproval;
    }
    
    public String getNodeVersionUUID() {
        return nodeVersionUUID;
    }
    
    public void setNodeVersionUUID(String nodeVersionUUID) {
        this.nodeVersionUUID = nodeVersionUUID;
    }
    
    public List<Label> getLabels() {
        return labels;
    }
    
    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }
    
    public Long getCreatorTeamId() {
        return creatorTeamId;
    }
    
    public void setCreatorTeamId(Long creatorTeamId) {
        this.creatorTeamId = creatorTeamId;
    }
    
    public String getCreatorEmail() {
        return creatorEmail;
    }
    
    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }
    
    public String getGeneralLink() {
        return generalLink;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getIndex() {
        return index;
    }
    
    public void setIndex(String index) {
        this.index = index;
    }
    
    public String getOrganisations()
    {
        return organisations;
    }
    
    public void setOrganisations(String generatedOrganisations)
    {
        // generatedOrganisations should be generated on the fly! See other parts of the code for the way to do it.
        this.organisations = generatedOrganisations;
    }
    
    /**
     * builds a DocumentData instance off a NodeWrapper. Pay special attention to the way uuid's are juggled here!
     * @param nodeWrapper - the NodeWrapper where all the main info comes
     * @param uuid - either the uuid value to be stored in the documentData instance or, if null, nodeWrapper.uuid will be stored
     * @param nodeVersionUUID - documentData.nodeVersionUUID
     * @return documentData
     */
    public static DocumentData buildFromNodeWrapper(NodeWrapper nodeWrapper, String uuid, String nodeVersionUUID) {
        DocumentData documentData = new DocumentData();
        documentData.setName(nodeWrapper.getName());
        if (uuid != null) {
            documentData.setUuid(uuid);
        } else {
            documentData.setUuid(nodeWrapper.getUuid());
        }
        if (nodeVersionUUID != null) {
            documentData.setNodeVersionUUID(nodeVersionUUID);
        }
        documentData.setNodeVersionUUID(nodeWrapper.getUuid());
        documentData.setTitle(nodeWrapper.getTitle());
        documentData.setDescription(nodeWrapper.getDescription());
        documentData.setNotes(nodeWrapper.getNotes());
        documentData.setFileSize(nodeWrapper.getFileSizeInMegabytes());
        documentData.setCalendar(nodeWrapper.getDate());
        documentData.setVersionNumber(nodeWrapper.getVersionNumber());
        documentData.setContentType(nodeWrapper.getContentType());
        documentData.setWebLink(nodeWrapper.getWebLink());
        documentData.setCmDocTypeId(nodeWrapper.getCmDocTypeId());
        documentData.setYearofPublication(nodeWrapper.getYearOfPublication());
        documentData.setLabels(nodeWrapper.getLabels());
        documentData.setCreatorTeamId(nodeWrapper.getCreatorTeam());
        documentData.setCreatorEmail(nodeWrapper.getCreator());
        documentData.setIndex(nodeWrapper.getIndex());
        documentData.setCategory(nodeWrapper.getCategory());
        documentData.process();
        documentData.computeIconPath(true);
        documentData.setOrganisations(
                DocumentOrganizationManager.getInstance().getOrganisationsAsStringByUUID(documentData.getUuid()));
        return documentData;
    }
    
    public static DocumentData buildFromNodeWrapper(NodeWrapper nodeWrapper) {
        DocumentData documentData = buildFromNodeWrapper(nodeWrapper, null, null);
        
        return documentData;
    }
    
    /**
     * Uses the JR session from HttpServletRequest. throws exception in case of any kind of error
     * @param uuid
     * @return
     */
    public static DocumentData buildFromUuid(String uuid){
        NodeWrapper nodeWrapper = DocumentManagerUtil.getReadNodeWrapper(uuid, TLSUtils.getRequest());
        return buildFromNodeWrapper(nodeWrapper);
    }
    
    /**
     * debug-only code!!! DO NOT USE IN PRODUCTION CODE
     * @param node
     * @param forcedUuid
     * @return
     */
    public static DocumentData buildFromNode(javax.jcr.Node node){
        NodeWrapper nodeWrapper = new NodeWrapper(node);
        String fileName = nodeWrapper.getName();
        if (fileName == null)
            return null;
        DocumentData documentData = DocumentData.buildFromNodeWrapper(nodeWrapper);
        return documentData;
    }
    
    @Override public String toString(){
        return String.format("file name = '%s', size = %.2f MB, uuid = %s", this.getName(), this.getFileSize(), this.getUuid());
    }

    public Map<String, String> getTranslatedTitles() {
        return translatedTitles;
    }

    public void setTranslatedTitles(Map<String, String> translatedTitles) {
        this.translatedTitles = translatedTitles;
    }

    public Map<String, String> getTranslatedDescriptions() {
        return translatedDescriptions;
    }

    public void setTranslatedDescriptions(Map<String, String> translatedDescriptions) {
        this.translatedDescriptions = translatedDescriptions;
    }

    public Map<String, String> getTranslatedNotes() {
        return translatedNotes;
    }

    public void setTranslatedNotes(Map<String, String> translatedNotes) {
        this.translatedNotes = translatedNotes;
    }
}
