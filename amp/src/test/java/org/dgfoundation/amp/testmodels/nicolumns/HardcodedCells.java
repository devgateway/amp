package org.dgfoundation.amp.testmodels.nicolumns;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.codegenerators.FundingIdsMapper;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.DateCell;
import org.dgfoundation.amp.nireports.MonetaryAmount;
import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.dgfoundation.amp.nireports.TextCell;
import org.dgfoundation.amp.nireports.TranslatedDate;
import org.dgfoundation.amp.nireports.amp.AmpPrecisionSetting;
import org.dgfoundation.amp.nireports.meta.MetaInfo;
import org.dgfoundation.amp.nireports.meta.MetaInfoGenerator;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.testmodels.HardcodedFundingNames;
import org.dgfoundation.amp.testmodels.HardcodedReportsTestSchema;
import org.dgfoundation.amp.testmodels.TestModelConstants;

/**
 * Cell source for hardcoded columns (TestColumn)
 * @author acartaleanu
 *
 * @param <K> - the type of cell used for populating
 */
public abstract class HardcodedCells<K extends Cell> {

	protected final Map<String, Long> activityIds;
	protected final Map<String, Map<String, Long>> fundingIds;
	protected final List<K> cells;
	protected final Map<String, Long> entityIds;
	public final Optional<LevelColumn> levelColumn;
	
	public HardcodedCells(Map<String, Long> activityIds, Map<String, Long> entityIds, LevelColumn levelColumn) {
		this.activityIds = activityIds;
		this.fundingIds = new HardcodedFundingNames().getParams();
		this.entityIds = entityIds;
		this.levelColumn = Optional.ofNullable(levelColumn);
		this.cells = populateCells();
	}

	protected DateCell dateCell(String activityName, String date) {
			DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
			LocalDate ld = LocalDate.parse(date, formatter);
			return new DateCell(ld, activityIds.get(activityName), activityIds.get(activityName), levelColumn);
	}	
	
	public List<K> getCells() {
		return cells;
	}
	
	/**
	 * must override in child class
	 * child class contains createCell statements, depending on what kind of Cells it's made of
	 * @return
	 */
	protected abstract List<K> populateCells();
	
	/**
	 * Create percentage text cell (sectors, programs, locations...)
	 * @param activityName the activity title
	 * @param text the payload of the cell
	 * @param entityId the id of the cell
	 * @param percentage percentage in the range of 0..1
	 * @return
	 */
	protected PercentageTextCell cell(String activityName, String text, long entityId, Double percentage) {
		return new PercentageTextCell(text, activityIds.get(activityName), entityId, levelColumn, BigDecimal.valueOf(percentage));
	}

	/**
	 * Create simple text cell
	 * @param activityName the activity title
	 * @param text
	 * @param entityId the id of the cell 
	 * @return
	 */
	protected TextCell cell(String activityName, String text, long entityId) {
		return new TextCell(text, activityIds.get(activityName), entityId, levelColumn);
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
		if (value == null)
			return;
		//Map<String, LevelColumn> optionalIdsMap = new FundingIdsMapper().getOptionalIdsMap();
		Long val = fundingIds.get(categoryName).get(value);
		//LevelColumn levelColumn = optionalIdsMap.get(categoryName);
		Coordinate newVal = levelColumn.getCoordinate(val);
		coos.put(levelColumn.dimensionUsage, newVal);
	}
	
	
	protected CategAmountCell cell(String amount, String activityTitle, int year, String month, 
			String pledge, String transaction_type, String agreement, String recipient_org, 
			String recipient_role, String source_role, String adjustment_type,
			String donor_org, String funding_status, String mode_of_payment, 
			String terms_assist, String financing_instrument) {
		
		NiDimension cats = HardcodedReportsTestSchema.catsDimension;
		
		MetaInfoGenerator mig = new MetaInfoGenerator();
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
		addToCoordsIfExists(HardcodedReportsTestSchema.DONOR_DIM_USG.getLevelColumn(2), TestModelConstants.DONOR_ORG_ID, donor_org, coos);
		addToCoordsIfExists(degenerate(cats, "funding_status"), TestModelConstants.FUNDING_STATUS_ID, funding_status, coos);
		addToCoordsIfExists(degenerate(cats, "mode_of_payment"), TestModelConstants.MODE_OF_PAYMENT_ID, mode_of_payment, coos);
		addToCoordsIfExists(degenerate(cats, "type_of_assistance"), TestModelConstants.TERMS_ASSIST_ID, terms_assist, coos);
		addToCoordsIfExists(degenerate(cats, "fin_instr"), TestModelConstants.FINANCING_INSTRUMENT_ID, financing_instrument, coos);
		return new CategAmountCell(activityIds.get(activityTitle), new MonetaryAmount(new BigDecimal(amount), new AmpPrecisionSetting()), 
				mis, coos, td);
	}
	
	protected Map<NiDimensionUsage, Coordinate> coos(long entityId) {
		if (!this.levelColumn.isPresent())
			return Collections.emptyMap();
		
		Map<NiDimensionUsage, Coordinate> res = new HashMap<>();
		res.put(levelColumn.get().dimensionUsage, levelColumn.get().getCoordinate(entityId));
		return res;
	}
	
	public static LevelColumn degenerate(NiDimension dim, String name) {
		return dim.getLevelColumn(name, dim.depth - 1);
	}
}
