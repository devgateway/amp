package org.digijava.module.esrigis.helpers;

/**
 * Copyright (c) 2010 Development Gateway (www.developmentgateway.org)
 * @author Diego Dimunzio
 * Simplified location object to manage locations in JSON - keeps (locationData, locationFundingSummaryInfo) for a location
 */
public class SimpleLocation implements Comparable<SimpleLocation> {
    private String name;
    private String GeoId;
    private String commitments;
    private String disbursements;
    private String expenditures;
    private String pledges;
    private String mtef;
    private String lat;
    private String lon;
    private Boolean islocated = false;
    private String percentage;
    private Boolean exactlocation; 
    private String exactlocation_lat;
    private String exactlocation_lon;
    private String amountsCurrencyCode;
    private Long[] ids;
    
    
    public SimpleLocation(String name, String commitments, String disbursements, String mtef)
    {
        this.name = name;
        this.commitments = commitments;
        this.disbursements = disbursements;
        this.mtef = mtef;
    }

    public Long[] getIds() {
        return ids;
    }

    public void setIds(Long[] ids) {
        this.ids = ids;
    }

    //This field is used only in the view to mark the location. 
    private String isdisplayed;

    public String getName() {
        return name;
    }

    public SimpleLocation() {
        super();        
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGeoId() {
        return GeoId;
    }

    public void setGeoId(String geoId) {
        GeoId = geoId;
    }

    public String getCommitments() {
        return commitments;
    }

    public void setCommitments(String commitments) {
        this.commitments = commitments;
    }

    public String getDisbursements() {
        return disbursements;
    }

    public void setDisbursements(String disbursements) {
        this.disbursements = disbursements;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public Boolean getIslocated() {
        return this.islocated;
    }

    public void setIslocated(Boolean islocated) {
        this.islocated = islocated;
    }

    
    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getExpenditures() {
        return expenditures;
    }

    public void setExpenditures(String expenditures) {
        this.expenditures = expenditures;
    }

    public String getPledges() {
        return pledges;
    }

    public void setPledges(String pledges) {
        this.pledges = pledges;
    }

    public void setIsdisplayed(String isdisplayed) {
        this.isdisplayed = isdisplayed;
    }

    public String getIsdisplayed() {
        return isdisplayed;
    }

    public Boolean getExactlocation() {
        return exactlocation;
    }

    public void setExactlocation(Boolean exactlocation) {
        this.exactlocation = exactlocation;
    }

    public String getExactlocation_lat() {
        return exactlocation_lat;
    }

    public void setExactlocation_lat(String exactlocation_lat) {
        this.exactlocation_lat = exactlocation_lat;
    }

    public String getExactlocation_lon() {
        return exactlocation_lon;
    }

    public void setExactlocation_lon(String exactlocation_lon) {
        this.exactlocation_lon = exactlocation_lon;
    }

    public void setAmountsCurrencyCode(String amountsCurrencyCode) {
        this.amountsCurrencyCode = amountsCurrencyCode;
    }

    public String getAmountsCurrencyCode() {
        return amountsCurrencyCode;
    }
    
    /**
     * formatted numeric expression of total MTEF Projections for the location
     * @return
     */
    public String getMtef()
    {
        return mtef;
    }
    
    public void setMtef(String mtef)
    {
        this.mtef = mtef;
    }
    
    @Override
    public String toString()
    {
        return String.format("%s: (comm: %s, disb: %s, mtef: %s)", this.getName(), this.commitments, this.disbursements, this.mtef);
    }
    
    @Override
    public int compareTo(SimpleLocation other)
    {
        return this.name.compareTo(other.name);
    }
    
    @Override
    public int hashCode()
    {
        return toString().hashCode();
    }
    
    @Override
    public boolean equals(Object oth)
    {
        SimpleLocation other = (SimpleLocation) oth;
        return this.toString().equals(other.toString());
    }
}
