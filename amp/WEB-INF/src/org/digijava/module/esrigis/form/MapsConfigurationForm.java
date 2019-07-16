package org.digijava.module.esrigis.form;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

public class MapsConfigurationForm extends ActionForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;


    private Long mapId;
    private Integer mapType;
    private Integer mapSubType;
    private String count;
    private String url;
    private String admin1;
    private String admin2;
    private String geoId;
    private FormFile legend;
    private HashMap<Integer, String> mapTypeList;
    private HashMap<Integer, String> mapSubTypeList;
    private String configName;


    private boolean reset;
    private String legendNotes;


    public FormFile getLegend() {
        return legend;
    }

    public void setLegend(FormFile legend) {
        this.legend = legend;
    }

    public String getAdmin1() {
        return admin1;
    }

    public void setAdmin1(String admin1) {
        this.admin1 = admin1;
    }

    public String getAdmin2() {
        return admin2;
    }

    public void setAdmin2(String admin2) {
        this.admin2 = admin2;
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

    public Integer getMapSubType() {
        return mapSubType;
    }

    public void setMapSubType(Integer mapSubType) {
        this.mapSubType = mapSubType;
    }

    public String getGeoId() {
        return geoId;
    }

    public void setGeoId(String geoId) {
        this.geoId = geoId;
    }

    public Long getMapId() {
        return mapId;
    }

    public void setMapId(Long mapId) {
        this.mapId = mapId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<Integer, String> getMapTypeList() {
        return mapTypeList;
    }

    public void setMapTypeList(HashMap<Integer, String> mapTypeList) {
        this.mapTypeList = mapTypeList;
    }

    public HashMap<Integer, String> getMapSubTypeList() {
        return mapSubTypeList;
    }

    public void setMapSubTypeList(HashMap<Integer, String> mapSubTypeList) {
        this.mapSubTypeList = mapSubTypeList;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if(isReset()){
            this.mapId = null;
            this.mapType = 0;
            this.mapSubType = 0;
            this.url = null;
            this.admin1 = null;
            this.admin2 = null;
            this.geoId = null;
            this.legend = null;
            this.configName = null;
            this.count=null;
            this.legendNotes = null;
        }
    }
    public void setReset(boolean reset) {
        this.reset = reset;
    }
    public boolean isReset() {
        return reset;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setLegendNotes(String legendNotes) {
        this.legendNotes = legendNotes;
    }

    public String getLegendNotes() {
        return legendNotes;
    }
}
