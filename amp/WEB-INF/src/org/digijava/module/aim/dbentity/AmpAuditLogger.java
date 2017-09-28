/*
* AMP FEATURE TEMPLATES
*/
/**
 * @author dan
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;

public class AmpAuditLogger implements Comparable, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 7004856623866175824L;
    /**
     * 
     */
    

    private Long id;
    private String teamName;
    private String authorName;
    private String editorName;
    private String authorEmail;
    private String editorEmail;
    private Timestamp loggedDate;
    private Timestamp modifyDate;
    private String sloggeddate;
    private String smodifydate;
    private String browser;
    private String ip;
    private String action;
    private String objectId;
    private String objectType;
    private String objectTypeTrimmed;
    private String objectName;
    private String detail;
    private Long userid;
    
    
    public Long getUserid() {
        return userid;
    }
    public void setUserid(Long userid) {
        this.userid = userid;
    }
    public String getObjectName() {
        /*Object o=AuditLoggerUtil.loadObject(this.objectId,this.objectType);
        if(o instanceof AmpActivity) {
            AmpActivityVersion a=(AmpActivityVersion)o;
            return a.getAmpId()+" "+a.getName();
        }
        if(o instanceof AmpReports) {
            AmpReports a=(AmpReports)o;
            return a.getName();
        }

        return "";*/
        return this.objectName;

    }
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getAuthorName() {
        return authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    public String getBrowser() {
        return browser;
    }
    public void setBrowser(String browser) {
        this.browser = browser;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public  Timestamp getLoggedDate() {
        return loggedDate;
    }
    public void setLoggedDate(Timestamp loggedDate) {
        this.loggedDate = loggedDate;
    }
    public String getObjectId() {
        return objectId;
    }
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
    public String getObjectType() {
        return objectType;
    }
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }
    public String getTeamName() {
        return teamName;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    public String getAuthorEmail() {
        return authorEmail;
    }
    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }
    public String getEditorEmail() {
        return editorEmail;
    }
    public void setEditorEmail(String editorEmail) {
        this.editorEmail = editorEmail;
    }
    public String getEditorName() {
        return editorName;
    }
    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }
    public Timestamp getModifyDate() {
        return modifyDate;
    }
    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }
    public String getObjectTypeTrimmed() {
        int i=objectType.lastIndexOf('.')+3;
        int j=objectType.length();
        return objectType.substring(i+1,j);
    }
    
    public void setObjectTypeTrimmed(String objectTypeTrimmed) {
        this.objectTypeTrimmed = objectTypeTrimmed;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }
    public String getDetail() {
        return detail;
    }

    
    public int compareTo(Object obj) {

        if (!(obj instanceof AmpAuditLogger))
            throw new ClassCastException();
        AmpAuditLogger audit = (AmpAuditLogger) obj;
        if (this.objectName != null) {
            if (audit.objectName != null) {
                return (this.objectName.trim().toLowerCase()
                        .compareTo(audit.objectName.trim().toLowerCase()));
            } else {
                return (this.objectName.trim().toLowerCase().compareTo(""));
            }
        } else {
            if (audit.objectName != null) {
                return ("".compareTo(audit.objectName.trim().toLowerCase()));
            } else {
                return 0;
            }
        }
    }
    
    public String getSloggeddate() {
        if (this.loggedDate != null) {
            String pattern = FeaturesUtil
                    .getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT);
            pattern = pattern.replace('m', 'M');
            SimpleDateFormat formater = new SimpleDateFormat(pattern);
            sloggeddate = formater.format(this.loggedDate.getTime());
            return sloggeddate;
        }
        return "";
    
    }
    public void setSloggeddate(String sloggeddate) {
        this.sloggeddate = sloggeddate;
    }
    public String getSmodifydate() {
        if (this.modifyDate != null) {
            String pattern = FeaturesUtil
                    .getGlobalSettingValue(Constants.GLOBALSETTINGS_DATEFORMAT);
            pattern = pattern.replace('m', 'M');
            SimpleDateFormat formater = new SimpleDateFormat(pattern);
            this.smodifydate = formater.format(this.modifyDate.getTime());
            return smodifydate;
        }
        return "";
    }
    public void setSmodifydate(String smodifydate) {
        this.smodifydate = smodifydate;
    }
    
    @Override
    public String toString()
    {
        return String.format("[id: %d] %s @%s", this.getId(), this.getAction(), this.getModifyDate().toString());
    }

}
