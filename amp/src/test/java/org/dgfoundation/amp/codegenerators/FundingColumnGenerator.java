package org.dgfoundation.amp.codegenerators;


import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.amp.AmpFundingColumn;
import org.dgfoundation.amp.nireports.amp.AmpReportsSchema;
import org.dgfoundation.amp.nireports.amp.MetaCategory;
import org.dgfoundation.amp.nireports.meta.MetaInfo;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.testmodels.HardcodedReportsTestSchema;
import org.dgfoundation.amp.testmodels.TestModelConstants;
import org.dgfoundation.amp.testutils.AmpTestCase;
import org.digijava.kernel.persistence.PersistenceManager;

/**
 * Code generator for funding columns.
 * Standalone since funding columns are a very special kind of column.
 * @author acartaleanu
 *
 */
public class FundingColumnGenerator extends ColumnGenerator {


	public FundingColumnGenerator() {
		super("Funding", CategAmountCell.class);
		this.entries = populateList();
	}

	class Entry {
		public final BigDecimal amount;
		public final String activityTitle;
		public final String year;
		public final String month;
		public final String pledge;
		public final String transaction_type;
		public final String agreement;
		public final String recipient_org;
		public final String recipient_role;
		public final String source_role;
		public final String adjustment_type;
		public final String donor_org;
		public final String funding_status;
		public final String mode_of_payment;
		public final String terms_assist;
		public final String financing_instrument;
		
		@Override
		public String toString() {
			return String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s", 
					pad(escape(amount.toString()), 20), 
					pad(escape(activityTitle), 60),
					year,
					pad(escape(month), 13),
					pad(escape(pledge), 30), 
					pad(escape(transaction_type), 30), 
					pad(escape(agreement), 30), 
					pad(escape(recipient_org), 30), 
					pad(escape(recipient_role), 30),
					pad(escape(source_role), 30),
					pad(escape(adjustment_type), 30),
					pad(escape(donor_org), 30), 
					pad(escape(funding_status), 30), 
					pad(escape(mode_of_payment), 30), 
					pad(escape(terms_assist), 30), 
					pad(escape(financing_instrument), 30));
		}
		
		public Entry(BigDecimal amount, String activityTitle, String year, String month, 
					String pledge, String transaction_type, String agreement, String recipient_org, 
					String recipient_role, String source_role, String adjustment_type,
					String donor_org, String funding_status, String mode_of_payment, 
					String terms_assist, String financing_instrument){
			this.amount = amount;
			this.activityTitle = activityTitle;
			this.year = year;
			this.month = month;
			this.pledge = pledge;
			this.transaction_type = transaction_type;
			this.agreement = agreement;
			this.recipient_org = recipient_org;
			this.recipient_role = recipient_role;
			this.source_role = source_role;
			this.adjustment_type = adjustment_type;
			this.donor_org = donor_org;
			this.funding_status = funding_status;
			this.mode_of_payment = mode_of_payment;
			this.terms_assist = terms_assist;
			this.financing_instrument = financing_instrument;
		}
	}
	
	final List<Entry> entries;

	private Map<String, LevelColumn> buildOptionalDimensionCols(AmpReportsSchema schema) {
		Map<String, NiReportColumn<?>> cols = schema.getColumns();
		Map<String, LevelColumn> res = new HashMap<>();
		FundingIdsMapper.getCoosFundingViewFilter().forEach((colName, viewColName) -> res.put(viewColName, cols.get(colName).levelColumn.get()));
		return res;
	}
	
	private Map<String, LevelColumn> optionalDimensionCols = buildOptionalDimensionCols(AmpReportsSchema.getInstance());
	
	
	// columns of type long which are optional
	protected static List<ImmutablePair<MetaCategory, String>> longColumnsToFetch = Arrays.asList(
			new ImmutablePair<>(MetaCategory.PLEDGE_ID, "pledge_id"),
			new ImmutablePair<>(MetaCategory.TRANSACTION_TYPE, "transaction_type"),			
			new ImmutablePair<>(MetaCategory.AGREEMENT_ID, "agreement_id"),
			new ImmutablePair<>(MetaCategory.RECIPIENT_ORG, "recipient_org_id")
			);	
		
	private Map<Long, String> getActivityNames() {
		String query = "SELECT amp_activity_id, name FROM amp_activity_version WHERE amp_team_id IN "
				+ "(SELECT amp_team_id FROM amp_team WHERE name = 'test workspace')"
				+ "AND amp_activity_id IN (SELECT amp_activity_id FROM amp_activity)";
		return (Map<Long, String>) PersistenceManager.getSession().doReturningWork(connection -> SQLUtils.collectKeyValue(connection, query));
	}
	
	protected Map<String, LevelColumn> buildOptionalDimensionCols(HardcodedReportsTestSchema schema) {
		Map<String, NiReportColumn<?>> cols = schema.getColumns();
		Map<String, LevelColumn> res = new HashMap<>();
		AmpFundingColumn.getFundingViewFilter().forEach((colName, viewColName) -> res.put(viewColName, cols.get(colName).levelColumn.get()));
		return res;
	}
	
	private List<Entry> populateList() {
		final List<Entry> entries = new ArrayList<>();
		runInEngineContext( 
				new ArrayList<String>(getActivityNames().values()), 
				eng -> {
					List<CategAmountCell> cells = AmpTestCase.sorted((List<CategAmountCell>) eng.schema.getFundingFetcher().fetch(eng));
					Map<Long, String> activityNames = getActivityNames();
					for (CategAmountCell cell : cells) {
						
						BigDecimal amount = cell.getAmount();
						String activityTitle = activityNames.get(cell.activityId);
						String year = String.format("%s", cell.getTranslatedDate().year.getValue());
						String month = cell.getTranslatedDate().month.getValue();
						String pledge = cat(TestModelConstants.PLEDGE_ID, cell.metaInfo); //meta
						String transaction_type = cat(TestModelConstants.TRANSACTION_TYPE, cell.metaInfo); //meta
						String agreement = cat(TestModelConstants.AGREEMENT_ID, cell.metaInfo); //meta
						String recipient_org = cat(TestModelConstants.RECIPIENT_ORG, cell.metaInfo); //meta
						if (recipient_org != null)
							System.out.print("");

						//these three are saved directly as values
						String recipient_role = catValueDirectly(TestModelConstants.RECIPIENT_ROLE, cell.metaInfo);
						String source_role = catValueDirectly(TestModelConstants.SOURCE_ROLE, cell.metaInfo);
						String adjustment_type = catValueDirectly(TestModelConstants.ADJUSTMENT_TYPE, cell.metaInfo);
						
						//these are read from the coords map
						String donor_org = cat(TestModelConstants.DONOR_ORG_ID, cell.coordinates);
						String funding_status = cat(TestModelConstants.FUNDING_STATUS_ID, cell.coordinates);
						String mode_of_payment = cat(TestModelConstants.MODE_OF_PAYMENT_ID, cell.coordinates);
						String terms_assist = cat(TestModelConstants.TERMS_ASSIST_ID, cell.coordinates);
						String financing_instrument = cat(TestModelConstants.FINANCING_INSTRUMENT_ID, cell.coordinates);
						entries.add(new Entry(amount, activityTitle, year, month, pledge, transaction_type, 
								agreement, recipient_org, recipient_role, source_role, adjustment_type, 
								donor_org, funding_status, mode_of_payment, terms_assist, financing_instrument));
					}
				});
		entries.sort((Entry e1, Entry e2) -> {
			if (!e1.activityTitle.equals(e2.activityTitle))
				return e1.activityTitle.compareTo(e2.activityTitle);
			if (!e1.amount.equals(e2.amount))
				return e1.amount.compareTo(e2.amount);
			if (!e1.donor_org.equals(e2.donor_org))
				return e1.donor_org.compareTo(e2.donor_org);
			if (!e1.year.equals(e2.year))
				return e1.year.compareTo(e2.year);
			if (!e1.month.equals(e2.month))
				return e1.month.compareTo(e2.month);
			return e1.transaction_type.compareTo(e2.transaction_type);
		});
		return entries;
	}

	private String cat(String name, MetaInfoSet mis) {
		MetaInfo mi = mis.getMetaInfo(name);
		if (mi == null)
			return null;
		Long id = (Long)mi.v;
		if (id == null)
			return null;
		if (!categs.get(name).containsKey(id))
			return null;
		return categs.get(name).get(id); 
	}
	
	
	private String catValueDirectly(String name, MetaInfoSet mis){
		MetaInfo mi = mis.getMetaInfo(name);
		if (mi == null)
			return null;
		String val = (String)mi.v;
		return val;
	}
	
	private String cat(String name, Map<NiDimensionUsage, Coordinate> coos) {
		if (optionalDimensionCols.get(name) == null)
			throw new RuntimeException("no column with requested name!");
		LevelColumn lc = optionalDimensionCols.get(name);
		if (coos.get(lc.dimensionUsage) == null)
			return null;
		Map<Long, String> categ = categs.get(name);
		if (categ == null)
			return null;
		return categ.get(coos.get(lc.dimensionUsage).id);
	}
	
	Map<String, Map<Long, String>> categs = new FundingIdsMapper().getAllIds();

	public static DateTimeFormatter getDateTimeFormatter() {
		return DateTimeFormatter.ofPattern("yyyy-MM-ddd");
	}

	static String generateNames() {
		return String.format("\t\t\t%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s", 
				pad("amount", 31), 
				pad("activity title", 60),
				pad("year", 4),
				pad("month", 13),
				pad("pledge", 30), 
				pad("transaction_type", 30), 
				pad("agreement", 30), 
				pad("recipient_org", 30), 
				pad("recipient_role", 30),
				pad("source_role", 30),
				pad("adjustment_type", 30),
				pad("donor_org", 30),
				pad("funding_status", 30),
				pad("mode_of_payment", 30),
				pad("terms_assist", 30),
				pad("financing_instrument", 30));
	}
	
	@Override
	public String generate() {
		StringBuilder strb = new StringBuilder();
		strb.append("/*");
		strb.append(generateNames());
		strb.append("*/\n");
		strb.append("return Arrays.asList(\n");
		for (int i = 0; i < entries.size(); i++) {
			Entry ent = entries.get(i);
			strb.append("\t\t\tcell(");
			strb.append(ent.toString());
			strb.append(")");
			if (i < entries.size() - 1)
				strb.append(",");
			strb.append("\n");
		}
		strb.append(");");
		return strb.toString();
	}	
}
