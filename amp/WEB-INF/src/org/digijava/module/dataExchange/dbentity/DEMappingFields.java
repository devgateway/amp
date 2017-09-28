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

package org.digijava.module.dataExchange.dbentity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * 
 * @author dan
 *
 */


public class DEMappingFields implements Serializable {
    private static Logger logger = Logger.getLogger(DEMappingFields.class);
    private Long id;
    private String iatiPath;
    private String iatiItems;
    private String iatiValues;
    private String iatiValuesForDisplay;
    private String iatiLang;
    private Long ampId;  
    private String ampValues;
    private Set<AmpDEUploadSession> uploadSessionsLinked;

    private int tmpId;
    private boolean dirty; //Modified and unsaved

    private DEMappingFields sameAsMaping;

    public String getIatiValuesForDisplay() {
        return iatiValuesForDisplay;
    }

    public void setIatiValuesForDisplay(String iatiValuesForDisplay) {
        this.iatiValuesForDisplay = iatiValuesForDisplay;
    }

    public Set<AmpDEUploadSession> getUploadSessionsLinked() {
        return uploadSessionsLinked;
    }

    public void setUploadSessionsLinked(Set<AmpDEUploadSession> uploadSessionsLinked) {
        this.uploadSessionsLinked = uploadSessionsLinked;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        DEMappingFields.logger = logger;
    }

    public int getTmpId() {
        return tmpId;
    }

    public void setTmpId(int tmpId) {
        this.tmpId = tmpId;
    }

    public String getAmpValues() {
        return ampValues;
    }

    public void setAmpValues(String ampValues) {
        this.ampValues = ampValues;
    }
    private String ampClass;
    private Long sourceId;
    private String feedFileName;
    private String status;
    private Timestamp creationDate;

    private String selectedAmpId;
    
    public String getSelectedAmpId() {
        return selectedAmpId;
    }

    public void setSelectedAmpId(String selectedAmpId) {
        this.selectedAmpId = selectedAmpId;
    }

    public DEMappingFields() {
        super();
    }

    public DEMappingFields(Long id, String iatiPath, String iatiItems,
            String iatiValues, String iatiLang, Long ampId, String ampClass,
            Long sourceId, String feedFileName, String status,
            Timestamp creationDate) {
        super();
        this.id = id;
        this.iatiPath = iatiPath;
        this.iatiItems = iatiItems;
        this.iatiValues = iatiValues;
        this.iatiLang = iatiLang;
        this.ampId = ampId;
        this.ampClass = ampClass;
        this.sourceId = sourceId;
        this.feedFileName = feedFileName;
        this.status = status;
        this.creationDate = creationDate;
    }

    public boolean compare(DEMappingFields o){
        boolean result = false;
        String lang = o.getIatiLang()==null?"en":o.getIatiLang();
        try{
            if(o.getIatiPath()!=null && o.getIatiItems()!=null && o.getIatiValues()!=null && lang!=null && o.getAmpClass()!=null )
        result  =
            this.getIatiPath().compareTo(o.getIatiPath()) == 0 &&
            this.getIatiItems().compareTo(o.getIatiItems()) == 0 &&
            this.getIatiValues().trim().compareTo(o.getIatiValues().trim())==0 &&
            this.getIatiLangSafe().compareTo(lang)==0 &&
            this.getAmpClass().compareTo(o.getAmpClass())==0
            ;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    

    public DEMappingFields(String iatiPath, String iatiItems,
            String iatiValues, String iatiLang, Long ampId, String ampClass,
            Long sourceId, String feedFileName, String status) {
        super();
        this.iatiPath = iatiPath;
        this.iatiItems = iatiItems;
        this.iatiValues = iatiValues;
        this.iatiLang = iatiLang;
        this.ampId = ampId;
        this.ampClass = ampClass;
        this.sourceId = sourceId;
        this.feedFileName = feedFileName;
        this.status = status;
    }

    public String getShortAmpClass(){
        //return this.ampClass.toString().substring(this.ampClass.toString().lastIndexOf(".")+1,this.ampClass.toString().length());
        return this.ampClass.replaceAll(" ","");
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getIatiPath() {
        return iatiPath;
    }
    public void setIatiPath(String iatiPath) {
        this.iatiPath = iatiPath;
    }
    public String getIatiItems() {
        return iatiItems;
    }
    public void setIatiItems(String iatiItems) {
        this.iatiItems = iatiItems;
    }
    public String getIatiValues() {
        return iatiValues;
    }
    public void setIatiValues(String iatiValues) {
        this.iatiValues = iatiValues;
    }
    public String getIatiLang() {
        return iatiLang;
    }
    public void setIatiLang(String iatiLang) {
        this.iatiLang = iatiLang;
    }
    public Long getAmpId() {
        return ampId;
    }
    public void setAmpId(Long ampId) {
        this.ampId = ampId;
    }
    public String getAmpClass() {
        return ampClass;
    }
    public void setAmpClass(String ampClass) {
        this.ampClass = ampClass;
    }
    public Long getSourceId() {
        return sourceId;
    }
    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }
    public String getFeedFileName() {
        return feedFileName;
    }
    public void setFeedFileName(String feedFileName) {
        this.feedFileName = feedFileName;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Timestamp getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public String getIatiLangSafe() {
     return getIatiLang()==null?"en":getIatiLang();
    }

    public DEMappingFields getSameAsMaping() {
        return sameAsMaping;
    }

    public void setSameAsMaping(DEMappingFields sameAsMaping) {
        this.sameAsMaping = sameAsMaping;
    }
}
