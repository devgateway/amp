package org.digijava.module.esrigis.dbentity;

import static org.digijava.module.esrigis.helpers.MapConstants.mapSubTypeNames;
import static org.digijava.module.esrigis.helpers.MapConstants.mapTypeNames;
import javax.persistence.*;

@Entity
@Table(name = "AMP_MAP_CONFIG")
public class AmpMapConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_map_config_seq")
    @SequenceGenerator(name = "amp_map_config_seq", sequenceName = "amp_map_config_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "mapurl")
    private String mapUrl;

    @Column(name = "maptype")
    private Integer mapType;

    @Column(name = "county_field")
    private String countyField;

    @Column(name = "district_field")
    private String districtField;

    @Column(name = "geoid_field")
    private String geoIdField;

    @Column(name = "count_field")
    private String countField;

    @Column(name = "map_subtype")
    private Integer mapSubType;

    @Lob
    @Column(name = "legend_image", length = 500000)
    private byte[] legendImage;

    @Column(name = "config_name")
    private String configName;

    @Lob
    @Column(name = "legend_notes")
    private String legendNotes;

    @Lob
    @Column(name = "layer")
    private String layer;



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

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getMapTypeName() {
        return mapTypeNames.get(this.mapType);
    }

    public String getMapSubTypeName() {
        return mapSubTypeNames.get(this.mapSubType);
    }
}
