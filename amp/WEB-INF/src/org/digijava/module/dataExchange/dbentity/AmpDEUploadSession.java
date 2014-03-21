package org.digijava.module.dataExchange.dbentity;

import org.digijava.kernel.user.User;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: flyer
 * Date: 3/17/14
 * Time: 5:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class AmpDEUploadSession {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss yyyy.MM.dd");

    Long id;
    String fileSrc;
    String fileName;
    Date uploadDate;
    Date lastEditDate;
    Set<DEMappingFields> mappedFields;
    DESourceSetting settingsAssigned;
    String selCountryFilters;
    List<DELogPerItem> logItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<DELogPerItem> getLogItems() {
        return logItems;
    }

    public void setLogItems(List<DELogPerItem> logItems) {
        this.logItems = logItems;
    }

    public String getFileSrc() {
        return fileSrc;
    }

    public void setFileSrc(String fileSrc) {
        this.fileSrc = fileSrc;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Date getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(Date lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public Set<DEMappingFields> getMappedFields() {
        return mappedFields;
    }

    public void setMappedFields(Set<DEMappingFields> mappedFields) {
        this.mappedFields = mappedFields;
    }

    public DESourceSetting getSettingsAssigned() {
        return settingsAssigned;
    }

    public void setSettingsAssigned(DESourceSetting settingsAssigned) {
        this.settingsAssigned = settingsAssigned;
    }

    public String getSelCountryFilters() {
        return selCountryFilters;
    }

    public void setSelCountryFilters(String selCountryFilters) {
        this.selCountryFilters = selCountryFilters;
    }

    public String getFormatedUploadDate () {
        return dateFormat.format(getUploadDate());
    }

    public String getFormatedLastEditDate () {
        return getLastEditDate() == null ? "N/A" : dateFormat.format(getLastEditDate());
    }

    public void setSelCountries (Set<String> selCountries) {
        StringBuilder filterSerializer = new StringBuilder();
        Iterator <String> selCountryIt = selCountries.iterator();
        while (selCountryIt.hasNext()) {
            filterSerializer.append(selCountryIt.next());
            if (selCountryIt.hasNext()) filterSerializer.append("|");
        }

        this.selCountryFilters = filterSerializer.toString();
    }

    public Set<String> getSelCountries () {
        Set<String> countryISOs = new HashSet<String>();

        if (this.selCountryFilters != null && !this.selCountryFilters.trim().isEmpty()) {
            StringTokenizer countryTokenizer = new StringTokenizer(this.selCountryFilters, "|", false);
            while (countryTokenizer.hasMoreElements()) {
                String country = (String)countryTokenizer.nextElement();
                countryISOs.add(country);
            }
        }
        return countryISOs;
    }

}
