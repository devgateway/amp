package org.dgfoundation.amp.nireports.amp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.VivificatingMap;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.diffcaching.ActivityInvalidationDetector;
import org.dgfoundation.amp.diffcaching.ExpiringCacher;
import org.dgfoundation.amp.newreports.CalendarConverter;
import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.diff.CategAmountCellProto;
import org.dgfoundation.amp.nireports.amp.diff.ContextKey;
import org.dgfoundation.amp.nireports.amp.diff.DifferentialCache;
import org.dgfoundation.amp.nireports.meta.MetaInfoGenerator;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.nireports.schema.TrivialMeasureBehaviour;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;

import static java.util.stream.Collectors.toList;

/**
 * the {@link NiReportColumn} which fetches a transactions' funding
 * 
 * @author Dolghier Constantin
 *
 */
public class AmpFundingColumn extends PsqlSourcedColumn<CategAmountCell> {

	protected final ExpiringCacher<ContextKey<Boolean>, FundingFetcherContext> cacher;
	protected final ActivityInvalidationDetector invalidationDetector;
	
	public final static int CACHE_TTL_SECONDS = 10 * 60;
	
	public AmpFundingColumn() {
		this("Funding", "v_ni_donor_funding", "amp_activity_id", TrivialMeasureBehaviour.getInstance());
	}

	protected AmpFundingColumn(String columnName, String viewName, String mainEntityColumn, Behaviour<?> behaviour) {
		super(columnName, null, viewName, mainEntityColumn, behaviour);
		this.invalidationDetector = new ActivityInvalidationDetector();
		this.cacher = new ExpiringCacher<>("funding cacher " + columnName, cacheKey -> resetCache(cacheKey.context), invalidationDetector, CACHE_TTL_SECONDS * 1000);
	}

	public static Map<String, String> getFundingViewFilter() {
		Map<String, String> res = new HashMap<>();
		res.put(ColumnConstants.TYPE_OF_ASSISTANCE, "terms_assist_id");
		res.put(ColumnConstants.FINANCING_INSTRUMENT, "financing_instrument_id");
		res.put(ColumnConstants.DONOR_AGENCY, "donor_org_id");
		res.put(ColumnConstants.MODE_OF_PAYMENT, "mode_of_payment_id");
		res.put(ColumnConstants.FUNDING_STATUS, "funding_status_id");
		//res.put(ColumnConstants.DISASTER_RESPONSE_MARKER, "disaster_response_code");
		return res;
	}
		
	protected Map<String, LevelColumn> buildOptionalDimensionCols(AmpReportsSchema schema) {
		Map<String, NiReportColumn<?>> cols = schema.getColumns();
		Map<String, LevelColumn> res = new HashMap<>();
		getFundingViewFilter().forEach((colName, viewColName) -> res.put(viewColName, cols.get(colName).levelColumn.get()));
		return res;
	}
	
	// columns of type long which are optional
	protected static List<ImmutablePair<MetaCategory, String>> longColumnsToFetch = Arrays.asList(
			new ImmutablePair<>(MetaCategory.PLEDGE_ID, "pledge_id"),
			new ImmutablePair<>(MetaCategory.TRANSACTION_TYPE, "transaction_type"),			
			new ImmutablePair<>(MetaCategory.AGREEMENT_ID, "agreement_id"),
			new ImmutablePair<>(MetaCategory.RECIPIENT_ORG, "recipient_org_id"),
			new ImmutablePair<>(MetaCategory.SOURCE_ORG, "donor_org_id")
			);
	
	protected synchronized FundingFetcherContext resetCache(NiReportsEngine engine) {
		engine.timer.putMetaInNode("resetCache", true);
		Map<Long, String> adjTypeValue = SQLUtils.collectKeyValue(AmpReportsScratchpad.get(engine).connection, String.format("select acv_id, acv_name from v_ni_category_values where acc_keyname = '%s'", CategoryConstants.ADJUSTMENT_TYPE_KEY));
		Map<Long, String> roles = SQLUtils.collectKeyValue(AmpReportsScratchpad.get(engine).connection, String.format("SELECT amp_role_id, role_code FROM amp_role", CategoryConstants.ADJUSTMENT_TYPE_KEY));
		return new FundingFetcherContext(new DifferentialCache<CategAmountCellProto>(invalidationDetector.getLastProcessedFullEtl()), adjTypeValue, roles);
	}

	@Override
	public synchronized List<CategAmountCell> fetch(NiReportsEngine engine) {
		boolean enableDiffing = true;
		AmpReportsScratchpad scratchpad = AmpReportsScratchpad.get(engine);
		AmpReportsSchema schema = (AmpReportsSchema) engine.schema;
		AmpCurrency usedCurrency = scratchpad.getUsedCurrency();
		if (enableDiffing) {
			FundingFetcherContext cache = cacher.buildOrGetValue(new ContextKey<>(engine, true));
			Set<Long> deltas = scratchpad.differentiallyImportCells(engine.timer, mainColumn, cache.cache, ids -> fetchSkeleton(engine, ids, cache));
			return cache.cache.getCells(engine.getMainIds()).stream().map(cacp -> cacp.materialize(usedCurrency, engine.calendar, schema.currencyConvertor, scratchpad.getPrecisionSetting())).collect(toList());
		}
		else {
			return fetchSkeleton(engine, engine.getMainIds(), resetCache(engine)).stream().map(cacp -> cacp.materialize(usedCurrency, engine.calendar, schema.currencyConvertor, scratchpad.getPrecisionSetting())).collect(toList());
		}
	}
	
	protected String buildSupplementalCondition(NiReportsEngine engine, Set<Long> ids, FundingFetcherContext context) {
		return "1=1";
	}
	
	/**
	 * independent of report options
	 * @param engine
	 * @return
	 * @throws Exception
	 */
	public List<CategAmountCellProto> fetchSkeleton(NiReportsEngine engine, Set<Long> ids, FundingFetcherContext context) {
		AmpReportsScratchpad scratchpad = AmpReportsScratchpad.get(engine);

		String query = String.format("SELECT * FROM %s WHERE %s IN (%s) AND (%s)", this.viewName, this.mainColumn, Util.toCSStringForIN(ids), buildSupplementalCondition(engine, ids, context));
		
		//TODO: do not commit this uncommented
		//query = query + " AND (transaction_date >= '2004-01-01') AND (transaction_date <= '2016-01-01')";
						
		VivificatingMap<Long, AmpCurrency> currencies = new VivificatingMap<Long, AmpCurrency>(new HashMap<>(), CurrencyUtil::getAmpcurrency);
		
		AmpReportsSchema schema = (AmpReportsSchema) engine.schema;
		AmpCurrency usedCurrency = scratchpad.getUsedCurrency();
		
		List<CategAmountCellProto> cells = new ArrayList<>();
		MetaInfoGenerator metaGenerator = new MetaInfoGenerator();
		CalendarConverter calendarConverter = engine.calendar;
		Map<String, LevelColumn> optionalDimensionCols = buildOptionalDimensionCols(schema);
		
		Set<String> ignoredColumns = getIgnoredColumns();
		
		try(RsInfo rs = SQLUtils.rawRunQuery(scratchpad.connection, query, null)) {
			while (rs.rs.next()) {
				MetaInfoSet metaSet = new MetaInfoSet(metaGenerator);
				Map<NiDimensionUsage, Coordinate> coos = new HashMap<>();
				
				long ampActivityId = rs.rs.getLong(this.mainColumn);
								
				for(ImmutablePair<MetaCategory, String> longOptionalColumn:longColumnsToFetch)
					if (!ignoredColumns.contains(longOptionalColumn.v))
						addMetaIfLongExists(metaSet, longOptionalColumn.k, rs.rs, longOptionalColumn.v);
				
				for(Map.Entry<String, LevelColumn> optDim:optionalDimensionCols.entrySet())
					addCoordinateIfLongExists(coos, rs.rs, optDim.getKey(), optDim.getValue());

				java.sql.Date transactionMoment = rs.rs.getDate("transaction_date");
				BigDecimal transactionAmount = rs.rs.getBigDecimal("transaction_amount");
				
				long currencyId = rs.rs.getLong("currency_id");
				AmpCurrency srcCurrency = currencies.getOrCreate(currencyId);
				BigDecimal fixed_exchange_rate = rs.rs.getBigDecimal("fixed_exchange_rate");
												 				
				BigDecimal capitalSpendPercent = rs.rs.getBigDecimal("capital_spend_percent");
				if (capitalSpendPercent != null)
					metaSet.add(MetaCategory.CAPITAL_SPEND_PERCENT.category, capitalSpendPercent);
								
				addMetaIfIdValueExists(metaSet, "recipient_role_id", MetaCategory.RECIPIENT_ROLE, rs.rs, context.roles);
				addMetaIfIdValueExists(metaSet, "source_role_id", MetaCategory.SOURCE_ROLE, rs.rs, context.roles);
				addMetaIfIdValueExists(metaSet, "adjustment_type", MetaCategory.ADJUSTMENT_TYPE, rs.rs, context.adjustmentTypes);
				
				if (metaSet.hasMetaInfo(MetaCategory.SOURCE_ROLE.category) && metaSet.hasMetaInfo(MetaCategory.RECIPIENT_ROLE.category)
					&& metaSet.hasMetaInfo(MetaCategory.SOURCE_ORG.category) && metaSet.hasMetaInfo(MetaCategory.RECIPIENT_ORG.category)) 
				{
						metaSet.add(MetaCategory.DIRECTED_TRANSACTION_FLOW.category,
							String.format("%s-%s",
									ArConstants.userFriendlyNameOfRole(metaSet.getMetaInfo(MetaCategory.SOURCE_ROLE.category).v.toString()), 
									ArConstants.userFriendlyNameOfRole(metaSet.getMetaInfo(MetaCategory.RECIPIENT_ROLE.category).v.toString())));
				}
				
//				BigDecimal usedExchangeRate = BigDecimal.valueOf(schema.currencyConvertor.getExchangeRate(srcCurrency.getCurrencyCode(), usedCurrency.getCurrencyCode(), fixed_exchange_rate == null ? null : fixed_exchange_rate.doubleValue(), transactionDate));
//				MonetaryAmount amount = new MonetaryAmount(transactionAmount.multiply(usedExchangeRate), transactionAmount, srcCurrency, transactionDate, scratchpad.getPrecisionSetting());
//				CategAmountCell cell = new CategAmountCell(ampActivityId, amount, metaSet, coos, calendarConverter.translate(transactionMoment));
				CategAmountCellProto cell = new CategAmountCellProto(ampActivityId, transactionAmount, srcCurrency, transactionMoment, metaSet, coos, fixed_exchange_rate);
				cells.add(cell);
			}
		}
		catch(Exception e) {
			throw AlgoUtils.translateException(e);
		}
		ImmutablePair<Long, Long> metaCacheStats = metaGenerator.getStats();
		engine.timer.putMetaInNode("meta_cache_calls", metaCacheStats.k);
		engine.timer.putMetaInNode("meta_cache_uncached", metaCacheStats.v);
		return cells;
	}
	
	/**
	 * returns a set of ColumnNames to ignore (for subclasses which only supply a subset of the data)
	 * @return
	 */
	protected Set<String> getIgnoredColumns() {
		return Collections.emptySet();
	}

	@Override
	public List<ReportRenderWarning> performCheck() {
		return null;
	}
	
	static class FundingFetcherContext {
		final DifferentialCache<CategAmountCellProto> cache;
		final Map<Long, String> adjustmentTypes;
		final Map<Long, String> roles;
				
		public FundingFetcherContext(DifferentialCache<CategAmountCellProto> cache, Map<Long, String> adjustmentTypes,  Map<Long, String> roles) {
			this.cache = cache;
			this.adjustmentTypes = adjustmentTypes;
			this.roles = roles;
		}
		
	}
}
