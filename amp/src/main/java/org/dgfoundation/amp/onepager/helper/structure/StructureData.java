package org.dgfoundation.amp.onepager.helper.structure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.digijava.kernel.ampapi.endpoints.util.ObjectMapperUtils;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StructureData {
    
    private String latitude;
    
    private String longitude;
    
    private String shape;
    
    private String title;
    
    private List<CoordinateData> coordinates = new ArrayList<>();
    
    @JsonProperty("color-value")
    private String colorValue;
    
    private String latitudeColName;
    
    private String longitudeColName;
    
    private String selectedShape;
    
    private String noData;
    
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
    
    public String getShape() {
        return shape;
    }
    
    public void setShape(String shape) {
        this.shape = shape;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public List<CoordinateData> getCoordinates() {
        return coordinates;
    }
    
    public void setCoordinates(List<CoordinateData> coordinates) {
        this.coordinates = coordinates;
    }
    
    public String getColorValue() {
        return colorValue;
    }
    
    public void setColorValue(String colorValue) {
        this.colorValue = colorValue;
    }
    
    public String getLatitudeColName() {
        return latitudeColName;
    }
    
    public void setLatitudeColName(String latitudeColName) {
        this.latitudeColName = latitudeColName;
    }
    
    public String getLongitudeColName() {
        return longitudeColName;
    }
    
    public void setLongitudeColName(String longitudeColName) {
        this.longitudeColName = longitudeColName;
    }
    
    public String getSelectedShape() {
        return selectedShape;
    }
    
    public void setSelectedShape(String selectedShape) {
        this.selectedShape = selectedShape;
    }
    
    public String getNoData() {
        return noData;
    }
    
    public void setNoData(String noData) {
        this.noData = noData;
    }
    
    public String asJsonString() {
        return ObjectMapperUtils.valueToString(this);
    }
}
