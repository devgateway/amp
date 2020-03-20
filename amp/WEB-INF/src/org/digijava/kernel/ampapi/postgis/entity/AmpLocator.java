package org.digijava.kernel.ampapi.postgis.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;


@Entity (name="amp_locator")
@JsonIgnoreProperties({ "geonameId", "asciiName", "alternateNames", "featureClass", "countryIso", "featureCode",
    "cc2", "countryIso", "admin1", "admin2", "admin3", "admin4", "population", "elevation", "gtopo30", "timezone", 
    "lastModified", "theGeometry", "anglicizedName", "anglicizedKeyword", "distance" })
public class AmpLocator implements Serializable{

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @JsonProperty
    private Long id;
    
    @Basic
    @Column (name="geoname_id")
    private Long geonameId;
    
    @Basic
    @Column (name="name")
    @JsonProperty
    private String name;
    
    @Basic
    @Column (name="ascii_name")
    private String asciiName;
    
    @Basic
    @Column (name="alternate_names",columnDefinition="TEXT")
    private String alternateNames;
    
    @Basic
    @Column (name="latitude")
    @JsonProperty
    private String latitude;
    
    @Basic
    @Column (name="longitude")
    @JsonProperty
    private String longitude;
    
    // http://www.geonames.org/source-code/javadoc/org/geonames/FeatureClass.html
    @Basic
    @Column (name="feature_class",length=1)
    private Character featureClass;

    // http://www.geonames.org/export/codes.html
    @Basic
    @Column (name="feature_code")
    private String featureCode;
    
    @Basic
    @Column (name="country_iso",length=2)
    private String countryIso;// char[2]
    
    @Basic
    @Column (name="cc2")
    private String cc2;
    
    @Basic
    @Column (name="admin1")
    private String admin1;

    
    @Basic
    @Column (name="admin2") 
    private String admin2;
    
    @Basic
    @Column (name="admin3") 
    private String admin3;
    
    @Basic
    @Column (name="admin4") 
    private String admin4;

    @Basic
    @Column (name="population") 
    private Long population;
    
    @Basic
    @Column (name="elevation")  
    private Integer elevation;
    
    @Basic
    @Column (name="gtopo30")    
    private Integer gtopo30;
    
    @Basic
    @Column (name="timezone")   
    private String timezone;
    
    @Temporal (TemporalType.DATE)
    private Date lastModified;
    
    @Type(type="org.hibernate.spatial.GeometryType")
    private Geometry theGeometry;

    //internal use only
    private String anglicizedName;
    private String anglicizedKeyword;
    
    private transient int distance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGeonameId() {
        return geonameId;
    }

    public void setGeonameId(Long geonameId) {
        this.geonameId = geonameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAsciiName() {
        return asciiName;
    }

    public void setAsciiName(String asciiName) {
        this.asciiName = asciiName;
    }

    public String getAlternateNames() {
        return alternateNames;
    }

    public void setAlternateNames(String alternateNames) {
        this.alternateNames = alternateNames;
    }

    public Character getFeatureClass() {
        return featureClass;
    }

    public void setFeatureClass(Character featureClass) {
        this.featureClass = featureClass;
    }

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public String getCountryIso() {
        return countryIso;
    }

    public void setCountryIso(String countryIso) {
        this.countryIso = countryIso;
    }

    public String getCc2() {
        return cc2;
    }

    public void setCc2(String cc2) {
        this.cc2 = cc2;
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

    public String getAdmin3() {
        return admin3;
    }

    public void setAdmin3(String admin3) {
        this.admin3 = admin3;
    }

    public String getAdmin4() {
        return admin4;
    }

    public void setAdmin4(String admin4) {
        this.admin4 = admin4;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    public Integer getElevation() {
        return elevation;
    }

    public void setElevation(Integer elevation) {
        this.elevation = elevation;
    }

    public Integer getGtopo30() {
        return gtopo30;
    }

    public void setGtopo30(Integer gtopo30) {
        this.gtopo30 = gtopo30;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Geometry getTheGeometry() {
        return theGeometry;
    }

    public void setTheGeometry(Geometry theGeometry) {
        this.theGeometry = theGeometry;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getAnglicizedName() {
        return anglicizedName;
    }

    public void setAnglicizedName(String anglicizedName) {
        this.anglicizedName = anglicizedName;
    }

    public String getAnglicizedKeyword() {
        return anglicizedKeyword;
    }

    public void setAnglicizedKeyword(String anglicizedKeyword) {
        this.anglicizedKeyword = anglicizedKeyword;
    }
    
}
