package org.dgfoundation.amp.nireports.amp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.dgfoundation.amp.algo.VivificatingMap;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.ColumnConstants;
import org.dgfoundation.amp.ar.viewfetcher.DatabaseViewFetcher;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.CalendarConverter;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.IdValuePair;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.MonetaryAmount;
import org.dgfoundation.amp.nireports.NiCurrency;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.SqlSourcedColumn;
import org.dgfoundation.amp.nireports.meta.MetaInfoGenerator;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.dgfoundation.amp.nireports.schema.TrivialMeasureBehaviour;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;

import static org.dgfoundation.amp.nireports.amp.MetaConstants.*;

/**
 * the {@link NiReportColumn} which fetches the funding
 * 
 * @author Dolghier Constantin
 *
 */
public class AmpFundingColumn extends PsqlSourcedColumn<CategAmountCell> {

	public AmpFundingColumn() {
		super("Funding", null, getFundingViewFilter(), "v_ni_donor_funding", "amp_activity_id", TrivialMeasureBehaviour.getInstance());
	}

	protected static Map<String, String> getFundingViewFilter() {
		Map<String, String> res = new HashMap<>();
		res.put(ColumnConstants.TYPE_OF_ASSISTANCE, "terms_assist_id");
		res.put(ColumnConstants.FINANCING_INSTRUMENT, "financing_instrument_id");
		res.put(ColumnConstants.DONOR_AGENCY, "donor_org_id");
		res.put(ColumnConstants.MODE_OF_PAYMENT, "mode_of_payment_id");
		res.put(ColumnConstants.FUNDING_STATUS, "funding_status_id");
		res.put(ColumnConstants.DISASTER_RESPONSE_MARKER, "disaster_response_code");
		return res;
	}
		
	// columns of type long which are optional
	protected static List<ImmutablePair<MetaCategory, String>> longColumnsToFetch = Arrays.asList(
			new ImmutablePair<>(MetaCategory.TRANSACTION_TYPE, "transaction_type"),
			new ImmutablePair<>(MetaCategory.PLEDGE_ID, "pledge_id"),
			new ImmutablePair<>(MetaCategory.TERMS_OF_ASSISTANCE, "terms_assist_id"),
			new ImmutablePair<>(MetaCategory.FINANCING_INSTRUMENT, "financing_instrument_id"),
			new ImmutablePair<>(MetaCategory.DONOR_ORG, "donor_org_id"),
			new ImmutablePair<>(MetaCategory.MODE_OF_PAYMENT, "mode_of_payment_id"),
			new ImmutablePair<>(MetaCategory.FUNDING_STATUS, "funding_status_id"),
			new ImmutablePair<>(MetaCategory.AGREEMENT_ID, "agreement_id"),
			new ImmutablePair<>(MetaCategory.RECIPIENT_ORG, "recipient_org_id")
			);
	
	@Override
	public List<CategAmountCell> fetch(NiReportsEngine engine) throws Exception {
		AmpReportsScratchpad scratchpad = AmpReportsScratchpad.get(engine);

		String query = buildQuery(engine);
		
		//TODO: do not commit this uncommented
		query = query + " AND (transaction_date >= '2005-01-01')";
		Map<Long, String> adjustmentTypes = SQLUtils.collectKeyValue(scratchpad.connection, 
				String.format("select acv_id, acv_name from v_ni_category_values where acc_keyname = '%s'", CategoryConstants.ADJUSTMENT_TYPE_KEY));
		
		Map<Long, String> roles = DatabaseViewFetcher.fetchInternationalizedView("amp_role", null, "amp_role_id", "role_code");
		
		//Map<Long, String> currencyCodes = DatabaseViewFetcher.fetchInternationalizedView("amp_currency", null, "amp_currency_id", "currency_code");
		VivificatingMap<Long, AmpCurrency> currencies = new VivificatingMap<Long, AmpCurrency>(new HashMap<>(), id -> CurrencyUtil.getAmpcurrency(id));
		
		AmpReportsSchema schema = (AmpReportsSchema) engine.schema;
		AmpCurrency usedCurrency = scratchpad.getUsedCurrency();
		
		List<CategAmountCell> cells = new ArrayList<>();
		MetaInfoGenerator metaGenerator = new MetaInfoGenerator();
		CalendarConverter calendarConverter = engine.calendar;
		
//		NiCurrency baseCurrency = CurrencyUtil.getAmpcurrency(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY));
		
		try(RsInfo rs = SQLUtils.rawRunQuery(scratchpad.connection, query, null)) {
			while (rs.rs.next()) {
				MetaInfoSet metaSet = new MetaInfoSet(metaGenerator);
				long ampActivityId = rs.rs.getLong(this.mainColumn);
								
				for(ImmutablePair<MetaCategory, String> longOptionalColumn:longColumnsToFetch)
					addMetaIfLongExists(metaSet, longOptionalColumn.k, rs.rs, longOptionalColumn.v);
							
				java.sql.Date transactionMoment = rs.rs.getDate("transaction_date");
				LocalDate transactionDate = transactionMoment.toLocalDate();
				BigDecimal transactionAmount = rs.rs.getBigDecimal("transaction_amount");
				
				long currencyId = rs.rs.getLong("currency_id");
				AmpCurrency srcCurrency = currencies.getOrCreate(currencyId);
				BigDecimal fixed_exchange_rate = rs.rs.getBigDecimal("fixed_exchange_rate");
												 				
				BigDecimal capitalSpendPercent = rs.rs.getBigDecimal("capital_spend_percent");
				if (capitalSpendPercent != null)
					metaSet.add(MetaCategory.CAPITAL_SPEND_PERCENT.category, capitalSpendPercent);
								
				addMetaIfIdValueExists(metaSet, "recipient_role_id", MetaCategory.RECIPIENT_ROLE, rs.rs, roles);
				addMetaIfIdValueExists(metaSet, "source_role_id", MetaCategory.SOURCE_ROLE, rs.rs, roles);
				addMetaIfIdValueExists(metaSet, "adjustment_type", MetaCategory.ADJUSTMENT_TYPE, rs.rs, adjustmentTypes);
				
				BigDecimal usedExchangeRate = BigDecimal.valueOf(schema.currencyConvertor.getExchangeRate(srcCurrency.getCurrencyCode(), usedCurrency.getCurrencyCode(), fixed_exchange_rate == null ? null : fixed_exchange_rate.doubleValue(), transactionDate));
				MonetaryAmount amount = new MonetaryAmount(transactionAmount.multiply(usedExchangeRate), transactionAmount, srcCurrency, transactionDate, scratchpad.getPrecisionSetting());
				CategAmountCell cell = new CategAmountCell(ampActivityId, amount, metaSet, calendarConverter.translate(transactionMoment));
				cells.add(cell);
			}
		}
		ImmutablePair<Long, Long> metaCacheStats = metaGenerator.getStats();
		engine.timer.putMetaInNode("meta_cache_calls", metaCacheStats.k);
		engine.timer.putMetaInNode("meta_cache_uncached", metaCacheStats.v);
		return cells;
	}
}
