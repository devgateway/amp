package org.digijava.module.aim.dbentity;

/**
 * Connection between componente and Activity specified by percents.
 * This is for bolivians, using sectors table for componente.  
 * @author Irakli Kobiashvili
 *
 */
public class AmpActivityComponente {
	private Long id;
	private AmpActivity activity;
	private AmpSector sector;
	private Float percentage;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public AmpActivity getActivity() {
		return activity;
	}
	public void setActivity(AmpActivity activity) {
		this.activity = activity;
	}
	public AmpSector getSector() {
		return sector;
	}
	public void setSector(AmpSector sector) {
		this.sector = sector;
	}
	public Float getPercentage() {
		return percentage;
	}
	public void setPercentage(Float percentage) {
		this.percentage = percentage;
	}
}
