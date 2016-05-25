package org.dgfoundation.amp.nireports.amp;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.algo.Memoizer;
import org.dgfoundation.amp.algo.VivificatingMap;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.diffcaching.ActivityInvalidationDetector;
import org.dgfoundation.amp.diffcaching.ExpiringCacher;
import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.IdValuePair;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.diff.CategAmountCellProto;
import org.dgfoundation.amp.nireports.amp.diff.ContextKey;
import org.dgfoundation.amp.nireports.amp.diff.DifferentialCache;
import org.dgfoundation.amp.nireports.meta.MetaInfo;
import org.dgfoundation.amp.nireports.meta.MetaInfoGenerator;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension.Coordinate;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiDimension.NiDimensionUsage;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.nireports.schema.TrivialMeasureBehaviour;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.Constants;
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

	public final static String ENTITY_DONOR_FUNDING = "Funding";
	public final static String ENTITY_PLEDGE_FUNDING = "Pledge Funding";
	public final static String ENTITY_COMPONENT_FUNDING = "Component Funding";
	
	public final static Map<String, String> FUNDING_VIEW_COLUMNS = Collections.unmodifiableMap(_buildFundingViewFilter());
		
	protected final ExpiringCacher<ContextKey<Boolean>, FundingFetcherContext> cacher;
	protected final ActivityInvalidationDetector invalidationDetector;
	protected final Object CACHE_SYNC_OBJ = new Object();
	protected final Set<String> ignoredColumns;
	
	public final static int CACHE_TTL_SECONDS = 10 * 60;
	
	public AmpFundingColumn(String columnName, String viewName) {
		this(columnName, viewName, TrivialMeasureBehaviour.getInstance());
	}

	protected AmpFundingColumn(String columnName, String viewName, Behaviour<?> behaviour) {
		super(columnName, null, viewName, behaviour);
		this.invalidationDetector = new ActivityInvalidationDetector();
		Set<String> ic = new HashSet<>();
		for(String col:FUNDING_VIEW_COLUMNS.values())
			if (!this.viewColumns.contains(col))
				ic.add(col);
		for(String col:AmpCollections.relist(longColumnsToFetch, z -> z.v))
			if (!this.viewColumns.contains(col))
				ic.add(col);
		this.ignoredColumns = Collections.unmodifiableSet(ic); // specified-generic columns minus columns which do not exist in the view
		this.cacher = new ExpiringCacher<>("funding cacher " + columnName, cacheKey -> resetCache(cacheKey.context), invalidationDetector, CACHE_TTL_SECONDS * 1000);
	}

	
	public static Map<String, String> _buildFundingViewFilter() {
		Map<String, String> res = new HashMap<>();
		res.put(ColumnConstants.TYPE_OF_ASSISTANCE, "terms_assist_id");
		res.put(ColumnConstants.FINANCING_INSTRUMENT, "financing_instrument_id");
		res.put(ColumnConstants.DONOR_AGENCY, "donor_org_id");
		res.put(ColumnConstants.MODE_OF_PAYMENT, "mode_of_payment_id");
		res.put(ColumnConstants.FUNDING_STATUS, "funding_status_id");
		res.put(ColumnConstants.DISASTER_RESPONSE_MARKER, "disaster_response_code");
		res.put(ColumnConstants.PLEDGES_AID_MODALITY, "aid_modality_id");
		res.put(ColumnConstants.RELATED_PLEDGES, "pledge_id");
		res.put(ColumnConstants.EXPENDITURE_CLASS, "expenditure_class_id");
		return res;
	}
		
	/**
	 * returns true IFF the column has been specified as being transaction-level and the current funding view has not it blacklisted.
	 * This is a hack to accommodate AMP's "multiple schemas per schema"
	 * @param col
	 * @return
	 */
	public boolean isTransactionLevelHierarchy(NiReportColumn<?> col) {
		if (!col.isTransactionLevelHierarchy())
			return false;
		String viewName = FUNDING_VIEW_COLUMNS.get(col.name);
		if (viewName == null)
			return true;
		return !ignoredColumns.contains(viewName);
	}
	
	protected Map<String, LevelColumn> buildOptionalDimensionCols(AmpReportsSchema schema) {
		Map<String, NiReportColumn<?>> cols = schema.getColumns();
		Map<String, LevelColumn> res = new HashMap<>();
		FUNDING_VIEW_COLUMNS.forEach((colName, viewColName) -> res.put(viewColName, cols.get(colName).levelColumn.get()));
		return res;
	}
	
	// columns of type long which are optional
	protected static List<ImmutablePair<MetaCategory, String>> longColumnsToFetch = Arrays.asList(
			new ImmutablePair<>(MetaCategory.TRANSACTION_TYPE, "transaction_type"),			
			new ImmutablePair<>(MetaCategory.AGREEMENT_ID, "agreement_id"),
			new ImmutablePair<>(MetaCategory.RECIPIENT_ORG, "recipient_org_id"),
			new ImmutablePair<>(MetaCategory.SOURCE_ORG, "donor_org_id"),
			new ImmutablePair<>(MetaCategory.EXPENDITURE_CLASS, "expenditure_class_id")
			
			);
	
	protected synchronized FundingFetcherContext resetCache(NiReportsEngine engine) {
		engine.timer.putMetaInNode("resetCache", true);
		Map<Long, String> adjTypeValue = SQLUtils.collectKeyValue(AmpReportsScratchpad.get(engine).connection, String.format("select acv_id, acv_name from v_ni_category_values where acc_keyname IN ('%s', '%s')", CategoryConstants.ADJUSTMENT_TYPE_KEY, CategoryConstants.SSC_ADJUSTMENT_TYPE_KEY));
		Map<Long, String> expClassValues = SQLUtils.collectKeyValue(AmpReportsScratchpad.get(engine).connection, String.format("select acv_id, acv_name from v_ni_category_values where acc_keyname = '%s'", CategoryConstants.EXPENDITURE_CLASS_KEY));
		Map<Long, String> roles = SQLUtils.collectKeyValue(AmpReportsScratchpad.get(engine).connection, String.format("SELECT amp_role_id, role_code FROM amp_role", CategoryConstants.ADJUSTMENT_TYPE_KEY));
		
		return new FundingFetcherContext(new DifferentialCache<CategAmountCellProto>(invalidationDetector.getLastProcessedFullEtl()), adjTypeValue, roles, expClassValues);
	}

	@Override
	public List<CategAmountCell> fetch(NiReportsEngine engine) {
		AmpReportsScratchpad scratchpad = AmpReportsScratchpad.get(engine);
		AmpReportsSchema schema = (AmpReportsSchema) engine.schema;
		boolean enableDiffing = schema.ENABLE_CACHING;
		AmpCurrency usedCurrency = scratchpad.getUsedCurrency();
		if (enableDiffing) {
			long start = System.currentTimeMillis();
			List<CategAmountCellProto> protos;
			synchronized(CACHE_SYNC_OBJ) {
				FundingFetcherContext cache = cacher.buildOrGetValue(new ContextKey<>(engine, true));
				Set<Long> deltas = scratchpad.differentiallyImportCells(engine.timer, mainColumn, cache.cache, ids -> fetchSkeleton(engine, ids, cache));
				protos = cache.cache.getCells(scratchpad.getMainIds(engine, this));
				long delta = System.currentTimeMillis() - start;
				engine.timer.putMetaInNode("hot_time", delta);
			}
			List<CategAmountCell> res = protos.stream().map(cacp -> cacp.materialize(usedCurrency, engine.calendar, schema.currencyConvertor, scratchpad.getPrecisionSetting())).collect(toList());
			return res;
		}
		else {
			return fetchSkeleton(engine, scratchpad.getMainIds(engine, this), resetCache(engine)).stream().map(cacp -> cacp.materialize(usedCurrency, engine.calendar, schema.currencyConvertor, scratchpad.getPrecisionSetting())).collect(toList());
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
		if (ids.isEmpty())
			return Collections.emptyList();
		AmpReportsScratchpad scratchpad = AmpReportsScratchpad.get(engine);

		String query = String.format("SELECT * FROM %s WHERE %s IN (%s) AND (%s)", this.viewName, this.mainColumn, Util.toCSStringForIN(ids), buildSupplementalCondition(engine, ids, context));
		
		//TODO: do not commit this uncommented
		//query = query + " AND (transaction_date >= '2006-05-01') AND (transaction_date <= '2007-06-01')";
						
		VivificatingMap<Long, AmpCurrency> currencies = new VivificatingMap<Long, AmpCurrency>(new HashMap<>(), CurrencyUtil::getAmpcurrency);
		
		AmpReportsSchema schema = (AmpReportsSchema) engine.schema;
		
		List<CategAmountCellProto> cells = new ArrayList<>();
		MetaInfoGenerator metaGenerator = new MetaInfoGenerator();
		Map<String, LevelColumn> optionalDimensionCols = buildOptionalDimensionCols(schema);
		BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100l);
				
		try(RsInfo rs = SQLUtils.rawRunQuery(scratchpad.connection, query, null)) {
			while (rs.rs.next()) {
				MetaInfoSet metaSet = new MetaInfoSet(metaGenerator);
				Map<NiDimensionUsage, Coordinate> coos = new HashMap<>();
				
				long ampActivityId = rs.rs.getLong(this.mainColumn);
								
				for(ImmutablePair<MetaCategory, String> longOptionalColumn:longColumnsToFetch)
					if (!ignoredColumns.contains(longOptionalColumn.v))
						addMetaIfLongExists(metaSet, longOptionalColumn.k, rs.rs, longOptionalColumn.v);
				
				for(Map.Entry<String, LevelColumn> optDim:optionalDimensionCols.entrySet())
					if (!ignoredColumns.contains(optDim.getKey()))
						addCoordinateIfLongExists(coos, rs.rs, optDim.getKey(), optDim.getValue());
				
				if (this.name.equals(ENTITY_PLEDGE_FUNDING))
				    addCoordinateIfLongExists(coos, rs.rs, "related_project_id", schema.ACT_LEVEL_COLUMN);
				
				if (this.name.equals(ENTITY_COMPONENT_FUNDING)) {
					addCoordinateIfLongExists(coos, rs.rs, "amp_component_id", schema.COMPONENT_LEVEL_COLUMN);
					metaSet.add(MetaCategory.SOURCE_ROLE.category, Constants.FUNDING_AGENCY);
				}
				
				java.sql.Date transactionMoment = rs.rs.getDate("transaction_date");
				BigDecimal transactionAmount = rs.rs.getBigDecimal("transaction_amount");
				
				long currencyId = rs.rs.getLong("currency_id");
				AmpCurrency srcCurrency = currencies.getOrCreate(currencyId);
				BigDecimal fixed_exchange_rate = getBigDecimal(rs.rs, "fixed_exchange_rate");
												 				
				BigDecimal capitalSpendPercent = getBigDecimal(rs.rs, "capital_spend_percent");
				if (capitalSpendPercent != null)
					metaSet.add(MetaCategory.CAPITAL_SPEND_PERCENT.category, capitalSpendPercent.divide(ONE_HUNDRED));
								
				addMetaIfIdValueExists(metaSet, "recipient_role_id", MetaCategory.RECIPIENT_ROLE, rs.rs, context.roles);
				addMetaIfIdValueExists(metaSet, "source_role_id", MetaCategory.SOURCE_ROLE, rs.rs, context.roles);
				addMetaIfIdValueExists(metaSet, "adjustment_type", MetaCategory.ADJUSTMENT_TYPE, rs.rs, context.adjustmentTypes);
				addMetaIfIdValueExists(metaSet, "expenditure_class_id", MetaCategory.EXPENDITURE_CLASS, rs.rs, context.expenditureClasses);
				
				if (metaSet.hasMetaInfo(MetaCategory.SOURCE_ROLE.category) && metaSet.hasMetaInfo(MetaCategory.RECIPIENT_ROLE.category)
					&& metaSet.hasMetaInfo(MetaCategory.SOURCE_ORG.category) && metaSet.hasMetaInfo(MetaCategory.RECIPIENT_ORG.category)) 
				{
						metaSet.add(MetaCategory.DIRECTED_TRANSACTION_FLOW.category,
							String.format("%s-%s",
									ArConstants.userFriendlyNameOfRole(metaSet.getMetaInfo(MetaCategory.SOURCE_ROLE.category).v.toString()), 
									ArConstants.userFriendlyNameOfRole(metaSet.getMetaInfo(MetaCategory.RECIPIENT_ROLE.category).v.toString())));
				}
				
				if (transactionMoment == null || transactionAmount == null) continue;
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
	
	@Override
	protected IdValuePair addMetaIfIdValueExists(MetaInfoSet set, String idColumnName, MetaCategory categ, ResultSet row, Map<Long, String> map) throws SQLException {
		if (viewColumns.contains(idColumnName))
			return super.addMetaIfIdValueExists(set, idColumnName, categ, row, map);
		return null;
	}

	protected BigDecimal getBigDecimal(ResultSet rs, String colName) throws SQLException {
		if (viewColumns.contains(colName))
			return rs.getBigDecimal(colName);
		
		return null;
	}
	
	@Override
	public List<ReportRenderWarning> performCheck() {
		return null;
	}
	
	static class FundingFetcherContext {
		final DifferentialCache<CategAmountCellProto> cache;
		final Map<Long, String> adjustmentTypes;
		final Map<Long, String> roles;
		final Map<Long, String> expenditureClasses;
				
		public FundingFetcherContext(DifferentialCache<CategAmountCellProto> cache, 
				Map<Long, String> adjustmentTypes,  Map<Long, String> roles, Map<Long, String> expenditureClasses) {
			this.cache = cache;
			this.adjustmentTypes = adjustmentTypes;
			this.roles = roles;
			this.expenditureClasses = expenditureClasses;
		}
	}
	
	/**
	 * extracts the CapitalMultiplier off a cell
	 * @param cell
	 * @return
	 */
	public final static BigDecimal getCapitalMultiplier(CategAmountCell cell) {
		MetaInfo mInfo = cell.getMetaInfo().getMetaInfo(MetaCategory.CAPITAL_SPEND_PERCENT.category);
		return mInfo == null ? null : (BigDecimal) mInfo.v; 
	}
	
	/**
	 * extracts the RecurrentMultiplier off a cell
	 * @param cell
	 * @return
	 */
	public final static BigDecimal getRecurrentMultiplier(CategAmountCell cell) {
		BigDecimal capMult = getCapitalMultiplier(cell);
		return capMult == null ? null : BigDecimal.ONE.subtract(capMult);
	}

}
