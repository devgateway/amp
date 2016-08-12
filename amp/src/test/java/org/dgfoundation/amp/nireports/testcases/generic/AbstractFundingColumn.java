package org.dgfoundation.amp.nireports.testcases.generic;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.MonetaryAmount;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.TranslatedDate;
import org.dgfoundation.amp.nireports.meta.MetaInfo;
import org.dgfoundation.amp.nireports.meta.MetaInfoGenerator;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.runtime.ColumnReportData;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.nireports.testcases.IHardcodedNames;
import org.dgfoundation.amp.nireports.testcases.TestModelConstants;
import org.dgfoundation.amp.nireports.testcases.TestcasesPrecisionSetting;

public abstract class AbstractFundingColumn extends HardcodedCells<CategAmountCell> {

	protected final NiDimension cats;
	protected final NiDimensionUsage donorDimUsg;
	protected final MetaInfoGenerator mig = new MetaInfoGenerator();
	protected final NiPrecisionSetting precisionSetting = new TestcasesPrecisionSetting();
	protected final Map<String, Map<String, Long>> fundingIds;
	
	protected AbstractFundingColumn(Map<String, Long> activityNames, NiDimension cats, NiDimensionUsage donorDimUsg, IHardcodedNames fundingIdsSrc) {
		super(activityNames, activityNames, null);
		this.cats = cats;
		this.donorDimUsg = donorDimUsg;
		this.fundingIds = fundingIdsSrc.getParams();
	}

	protected CategAmountCell cell(String amount, String activityTitle, Integer year, String month, 
			String pledge, String transaction_type, String agreement, String recipient_org, 
			String recipient_role, String source_role, String adjustment_type,
			String donor_org, String funding_status, String mode_of_payment, 
			String terms_assist, String financing_instrument, String transaction_date) {
				
		LocalDate parsedDate = LocalDate.parse(transaction_date, DateTimeFormatter.ISO_LOCAL_DATE);
		if (year == null) {
			year = parsedDate.getYear();
			month = months[parsedDate.getMonth().getValue()];
		}
		Map<NiDimensionUsage, Coordinate> coos = new HashMap<NiDimensionUsage, Coordinate>();
		MetaInfoSet mis = new MetaInfoSet(mig);
		TranslatedDate td = new GregorianTestDateGenerator(year, month).toTranslatedDate();
		addToMetaIfExists(TestModelConstants.PLEDGE_ID, pledge, mis);
		addToMetaIfExists(TestModelConstants.TRANSACTION_TYPE, transaction_type, mis);
		addToMetaIfExists(TestModelConstants.AGREEMENT_ID, agreement, mis);
		addToMetaIfExists(TestModelConstants.RECIPIENT_ORG, recipient_org, mis);
		addToMetaIfExistsDirectly(TestModelConstants.RECIPIENT_ROLE, recipient_role, mis);
		addToMetaIfExistsDirectly(TestModelConstants.SOURCE_ROLE, source_role, mis);
		addToMetaIfExistsDirectly(TestModelConstants.ADJUSTMENT_TYPE, adjustment_type, mis);
		addToCoordsIfExists(donorDimUsg.getLevelColumn(2), TestModelConstants.DONOR_ORG_ID, donor_org, coos);
		addToCoordsIfExists(degenerate(cats, "funding_status"), TestModelConstants.FUNDING_STATUS_ID, funding_status, coos);
		addToCoordsIfExists(degenerate(cats, "mode_of_payment"), TestModelConstants.MODE_OF_PAYMENT_ID, mode_of_payment, coos);
		addToCoordsIfExists(degenerate(cats, "type_of_assistance"), TestModelConstants.TERMS_ASSIST_ID, terms_assist, coos);
		addToCoordsIfExists(degenerate(cats, "fin_instr"), TestModelConstants.FINANCING_INSTRUMENT_ID, financing_instrument, coos);
		
		return new CategAmountCell(activityIds.get(activityTitle), 
				new MonetaryAmount(new BigDecimal(amount), null, null, parsedDate, precisionSetting), 
				mis, coos, td);
	}
	
	/**
	 * Adds funding parameter id if value != null.
	 * This is done for: pledge, transaction type, agreement, recipient org.
	 * Mirrors code in AmpFundingColumn.fetch().
	 * @param categoryName
	 * @param value
	 * @param mis
	 */
	private void addToMetaIfExists(String categoryName, String value, MetaInfoSet mis) {
		if (value != null)
			mis.add(new MetaInfo(categoryName, fundingIds.get(categoryName).get(value)));
	}
	
	/**
	 * Adds funding parameter as string (not as id), if != null.
	 * This is done for: recipient role, source role, adjustment type
	 * Mirrors code in AmpFundingColumn.fetch().
	 * @param categoryName
	 * @param value
	 * @param mis
	 */
	private void addToMetaIfExistsDirectly(String categoryName, String value, MetaInfoSet mis) {
		if (value != null)
			mis.add(new MetaInfo(categoryName, value));
	}
	/*			this.donor_org = donor_org;
			this.funding_status = funding_status;
			this.mode_of_payment = mode_of_payment;
			this.terms_assist = terms_assist;
			this.financing_instrument = financing_instrument;*/
	
	/**
	 * Adds funding parameter (as id) to the coordinates map, if != null.
	 * this is done for: donor org, funding status, mode of payment, terms of assistance (type of assistance in other places), financing instrument.
	 * @param categoryName
	 * @param value
	 * @param coos
	 */
	private void addToCoordsIfExists(LevelColumn levelColumn, String categoryName, String value, Map<NiDimensionUsage, Coordinate> coos) {
		//Map<String, LevelColumn> optionalIdsMap = new FundingIdsMapper().getOptionalIdsMap();
		Long val = value == null ? ColumnReportData.UNALLOCATED_ID : fundingIds.get(categoryName).get(value);
		//LevelColumn levelColumn = optionalIdsMap.get(categoryName);
		Coordinate newVal = levelColumn.getCoordinate(val);
		coos.put(levelColumn.dimensionUsage, newVal);
	}
	
	static String[] months = new String[] {"dummy", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

}
