package org.digijava.module.esrigis.dbentity;

public class AmpMapConfig {

    private Long id;
    private String mapUrl;
    private Integer mapType;
    private Integer mapSubType;
    private String countyField;
    private String districtField;
    private String geoIdField;
    private String countField;
    private byte[] legendImage;
    private String configName;
    private String legendNotes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public Integer getMapType() {
        return mapType;
    }

    public void setMapType(Integer mapType) {
        this.mapType = mapType;
    }

    public String getCountyField() {
        return countyField;
    }

    public void setCountyField(String countyField) {
        this.countyField = countyField;
    }

    public String getDistrictField() {
        return districtField;
    }

    public void setDistrictField(String districtField) {
        this.districtField = districtField;
    }

    public String getGeoIdField() {
        return geoIdField;
    }

    public void setGeoIdField(String geoIdField) {
        this.geoIdField = geoIdField;
    }

    public String getCountField() {
        return countField;
    }

    public void setCountField(String countField) {
        this.countField = countField;
    }

    public Integer getMapSubType() {
        return mapSubType;
    }

    public void setMapSubType(Integer mapSubType) {
        this.mapSubType = mapSubType;
    }

    public byte[] getLegendImage() {
        return legendImage;
    }

    public void setLegendImage(byte[] legendImage) {
        this.legendImage = legendImage;
    }
    public String getConfigName() {
        return configName;
    }
    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getLegendNotes() {
        return legendNotes;
    }

    public void setLegendNotes(String legendNotes) {
        this.legendNotes = legendNotes;
    }
}
