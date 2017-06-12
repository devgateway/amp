package org.digijava.kernel.ampapi.endpoints.datafreeze;


import org.digijava.module.aim.dbentity.AmpDataFreezeSettings.FreezeOptions;

public class DataFreezeEvent {
	private Long id;
	private Long cid;	
	private Boolean enabled = Boolean.FALSE;
	private Integer gracePeriod;
	private String freezingDate;
	private String openPeriodStart;
	private String openPeriodEnd;
	private Boolean sendNotification = Boolean.FALSE;
	private FreezeOptions freezeOption;	
	private String filters;
	
	public DataFreezeEvent() {		
	}
			
	public DataFreezeEvent(Long id, Boolean enabled, Integer gracePeriod, String freezingDate,
			String openPeriodStart, String openPeriodEnd, Boolean sendNotification, FreezeOptions freezeOption,
			String filters, Long cid) {
		super();
		this.id = id;
		this.enabled = enabled;
		this.gracePeriod = gracePeriod;
		this.freezingDate = freezingDate;
		this.openPeriodStart = openPeriodStart;
		this.openPeriodEnd = openPeriodEnd;
		this.sendNotification = sendNotification;
		this.freezeOption = freezeOption;
		this.filters = filters;
		this.cid = cid;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public Integer getGracePeriod() {
		return gracePeriod;
	}
	public void setGracePeriod(Integer gracePeriod) {
		this.gracePeriod = gracePeriod;
	}
	public String getFreezingDate() {
		return freezingDate;
	}
	public void setFreezingDate(String freezingDate) {
		this.freezingDate = freezingDate;
	}
	public String getOpenPeriodStart() {
		return openPeriodStart;
	}
	public void setOpenPeriodStart(String openPeriodStart) {
		this.openPeriodStart = openPeriodStart;
	}
	public String getOpenPeriodEnd() {
		return openPeriodEnd;
	}
	public void setOpenPeriodEnd(String openPeriodEnd) {
		this.openPeriodEnd = openPeriodEnd;
	}	
	public Boolean getSendNotification() {
		return sendNotification;
	}
	public void setSendNotification(Boolean sendNotification) {
		this.sendNotification = sendNotification;
	}
	public FreezeOptions getFreezeOption() {
		return freezeOption;
	}
	public void setFreezeOption(FreezeOptions freezeOption) {
		this.freezeOption = freezeOption;
	}
	public String getFilters() {
		return filters;
	}
	public void setFilters(String filters) {
		this.filters = filters;
	}
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
}
