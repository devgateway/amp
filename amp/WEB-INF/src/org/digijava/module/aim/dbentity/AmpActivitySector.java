package org.digijava.module.aim.dbentity;

public class AmpActivitySector {

	private Long ampActivitySectorId;
	
	private AmpActivity activityId;
	
	private AmpSector sectorId;
	
	private Float sectorPercentage;
        
        private AmpClassificationConfiguration classificationConfig;

        public AmpClassificationConfiguration getClassificationConfig() {
            return classificationConfig;
        }

        public void setClassificationConfig(AmpClassificationConfiguration classificationConfig) {
            this.classificationConfig = classificationConfig;
        }


	public Long getAmpActivitySectorId() {
		return ampActivitySectorId;
	}

	public void setAmpActivitySectorId(Long ampActivitySectorId) {
		this.ampActivitySectorId = ampActivitySectorId;
	}

	public AmpActivity getActivityId() {
		return activityId;
	}

	public void setActivityId(AmpActivity activityId) {
		this.activityId = activityId;
	}

	public AmpSector getSectorId() {
		return sectorId;
	}

	public void setSectorId(AmpSector sectorId) {
		this.sectorId = sectorId;
	}

	public Float getSectorPercentage() {
		return sectorPercentage;
	}

	public void setSectorPercentage(Float sectorPercentage) {
		this.sectorPercentage = sectorPercentage;
	}
	
	public String toString() {
		return sectorId!=null?sectorId.getName():"";
	}
}
