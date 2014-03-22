package org.digijava.module.fundingpledges.form;

import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDetails;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FundingPledgesDetailsShim implements UniquelyIdentifiable{
	private Long pledgeTypeId;
	private Long typeOfAssistanceId;
	private Long aidModalityId;	
	private Double amount;
	private Long currencyId;
	private Long fundingYear;
	
	private long uniqueId = PledgeForm.uniqueIds.getAndIncrement();
	
	public FundingPledgesDetailsShim(FundingPledgesDetails fpd)
	{
		this.pledgeTypeId = getIdFrom(fpd.getPledgetype());
		this.typeOfAssistanceId = getIdFrom(fpd.getTypeOfAssistance());
		this.aidModalityId = getIdFrom(fpd.getAidmodality());
		this.amount = fpd.getAmount();
		this.currencyId = getIdFrom(fpd.getCurrency());
		this.fundingYear = getLongFrom(fpd.getFundingYear());
	}
	
	public static Long getIdFrom(Identifiable id)
	{
		return id == null ? null : (Long) id.getIdentifier();
	}
	
	public static Long getLongFrom(String z)
	{
		if (z == null) return null;
		try{
			return Long.parseLong(z);
		}
		catch(Exception e) {return null;}
	}	
}
