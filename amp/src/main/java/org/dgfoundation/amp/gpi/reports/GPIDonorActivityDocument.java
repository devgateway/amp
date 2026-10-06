package org.dgfoundation.amp.gpi.reports;

import java.util.List;

/**
 * Class for storing gpi supportive documents info used in endpoints (for GPI report 1)
 * 
 * @author Viorel Chihai
 *
 */
public class GPIDonorActivityDocument {

    private String donorId;
    private String activityId;
    private List<GPIDocument> documents;
    
    public GPIDonorActivityDocument() {}

    public GPIDonorActivityDocument(String donorId, String activityId, List<GPIDocument> documents) {
        super();
        this.donorId = donorId;
        this.activityId = activityId;
        this.documents = documents;
    }
    
    public GPIDonorActivityDocument(String donorId, String activityId) {
        super();
        this.donorId = donorId;
        this.activityId = activityId;
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public List<GPIDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<GPIDocument> documents) {
        this.documents = documents;
    }
}
