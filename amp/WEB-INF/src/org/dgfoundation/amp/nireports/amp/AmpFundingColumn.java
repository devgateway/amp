package org.dgfoundation.amp.nireports.amp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.IdValuePair;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.MonetaryAmount;
import org.dgfoundation.amp.nireports.NiCurrency;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.SqlSourcedColumn;
import org.dgfoundation.amp.nireports.meta.MetaInfoGenerator;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.NiDimension.LevelColumn;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;

import static org.dgfoundation.amp.nireports.amp.MetaConstants.*;

/**
 * the {@link NiReportColumn} which fetches the funding
 * 
 * TODO: this is a stub for now
 * @author Dolghier Constantin
 *
 */
public class AmpFundingColumn extends SqlSourcedColumn<CategAmountCell> {

	public AmpFundingColumn() {
		super("Funding", null, getFundingViewFilter(), "v_ni_donor_funding", "amp_activity_id");
	}

	protected static Map<String, String> getFundingViewFilter() {
		Map<String, String> res = new HashMap<>();
		res.put(ColumnConstants.TYPE_OF_ASSISTANCE, "terms_assist_id");
		res.put(ColumnConstants.FINANCING_INSTRUMENT, "financing_instrument_id");
		res.put(ColumnConstants.DONOR_AGENCY, "donor_org_id");
		res.put(ColumnConstants.MODE_OF_PAYMENT, "mode_pf_payment_id");
		res.put(ColumnConstants.FUNDING_STATUS, "funding_status_id");
		res.put(ColumnConstants.DISASTER_RESPONSE_MARKER, "disaster_response_code");
		return res;
	}
		
	@Override
	public List<CategAmountCell> fetchColumn(NiReportsEngine engine) throws Exception {
		String query = buildQuery(engine);
		Map<Long, Optional<String>> categoryValues = fetchValues("amp_category_value", "id", "category_value");
		Map<Long, Optional<String>> roles = fetchValues("amp_role", "amp_role_id", "role_code");
		//Map<Long, String> currencyCodes = DatabaseViewFetcher.fetchInternationalizedView("amp_currency", null, "amp_currency_id", "currency_code");
		VivificatingMap<Long, NiCurrency> currencies = new VivificatingMap<Long, NiCurrency>(new HashMap<>(), id -> CurrencyUtil.getAmpcurrency(id));
		
		AmpReportsScratchpad scratchpad = AmpReportsScratchpad.get(engine);
		AmpReportsSchema schema = (AmpReportsSchema) engine.schema;
		NiCurrency usedCurrency = engine.usedCurrency;
		
		List<CategAmountCell> cells = new ArrayList<>();
		MetaInfoGenerator metaGenerator = new MetaInfoGenerator();
		
		try(RsInfo rs = SQLUtils.rawRunQuery(scratchpad.connection, query, null)) {
			while (rs.rs.next()) {
				MetaInfoSet metaSet = new MetaInfoSet(metaGenerator);
				long ampActivityId = rs.rs.getLong(this.mainColumn);
				
				int transactionTypeId = rs.rs.getInt("transaction_type");
				metaSet.add(MetaCategory.TRANSACTION_TYPE.category, transactionTypeId); // transactiontype always exists
				//addMetaIfIntExists(metaSet, MetaCategory.TRANSACTION_TYPE, rs.rs, "transaction_type");
				
				//String transactionTypeName = ArConstants.TRANSACTION_ID_TO_TYPE_NAME.get(transactionTypeId);
				//if (transactionTypeName != null)
				//IdValuePair adjustmentType = readIdValuePair(rs.rs, "adjustment_type", categoryValues);
				addMetaIfLongExists(metaSet, MetaCategory.ADJUSTMENT_TYPE, rs.rs, "adjustment_type");
				
				LocalDate transactionDate = rs.rs.getDate("transaction_date").toLocalDate();
				BigDecimal transactionAmount = rs.rs.getBigDecimal("transaction_amount");
				//long pledgeId = rs.rs.getLong("pledge_id");
				addMetaIfLongExists(metaSet, MetaCategory.PLEDGE_ID, rs.rs, "pledge_id");
				
				long currencyId = rs.rs.getLong("currency_id");
				NiCurrency srcCurrency = currencies.getOrCreate(currencyId);
				//IdValuePair termsOfAssistance = readIdValuePair(rs.rs, "terms_assist_id", categoryValues);
				addMetaIfLongExists(metaSet, MetaCategory.TERMS_OF_ASSISTANCE, rs.rs, "terms_assist_id");
				BigDecimal fixed_exchange_rate = rs.rs.getBigDecimal("fixed_exchange_rate");
				
				//IdValuePair financingInstrument = readIdValuePair(rs.rs, "financing_instrument_id", categoryValues);
				addMetaIfLongExists(metaSet, MetaCategory.FINANCING_INSTRUMENT, rs.rs, "financing_instrument_id");
				
				//long donorOrgId = rs.rs.getLong("donor_org_id");
				addMetaIfLongExists(metaSet, MetaCategory.DONOR_ORG, rs.rs, "donor_org_id");
				
				//TODO: get donor name / donor group / donor type from here 
//				IdValuePair modeOfPayment = readIdValuePair(rs.rs, "mode_of_payment_id", categoryValues);
//				IdValuePair fundingStatus = readIdValuePair(rs.rs, "funding_status_id", categoryValues);
				addMetaIfLongExists(metaSet, MetaCategory.MODE_OF_PAYMENT, rs.rs, "mode_of_payment_id");
				addMetaIfLongExists(metaSet, MetaCategory.FUNDING_STATUS, rs.rs, "funding_status_id");
				
				BigDecimal capitalSpendPercent = rs.rs.getBigDecimal("capital_spend_percent");
				if (capitalSpendPercent != null)
					metaSet.add(MetaCategory.CAPITAL_SPEND_PERCENT.category, capitalSpendPercent);
				
				//long agreement_id = rs.rs.getLong("agreement_id");
				addMetaIfLongExists(metaSet, MetaCategory.AGREEMENT_ID, rs.rs, "agreement_id");
				
				//int recipientOrgId = rs.rs.getInt("recipient_org_id");
				addMetaIfLongExists(metaSet, MetaCategory.RECIPIENT_ORG, rs.rs, "recipient_org_id");
				
				//TODO: get recipient name/group/type
				IdValuePair recipientRole = readIdValuePair(rs.rs, "recipient_role_id", roles);
				if (recipientRole != null && recipientRole.v.isPresent())
					metaSet.add(MetaCategory.RECIPIENT_ROLE.category, recipientRole.v.get());
				
				IdValuePair sourceRole = readIdValuePair(rs.rs, "source_role_id", roles);
				if (sourceRole != null && sourceRole.v.isPresent())
					metaSet.add(MetaCategory.SOURCE_ROLE.category, sourceRole.v.get());
				
				BigDecimal usedExchangeRate = (fixed_exchange_rate == null || fixed_exchange_rate.compareTo(BigDecimal.ZERO) <= 0) ? 
						engine.currencyConvertor.getRate(srcCurrency, usedCurrency, transactionDate) : 
						fixed_exchange_rate;
				MonetaryAmount amount = new MonetaryAmount(transactionAmount.multiply(usedExchangeRate), transactionAmount, srcCurrency, transactionDate);
				CategAmountCell cell = new CategAmountCell(ampActivityId, amount, metaSet);
				cells.add(cell);
			}
		}
		ImmutablePair<Long, Long> metaCacheStats = metaGenerator.getStats();
		engine.timer.putMetaInNode("meta_cache_calls", metaCacheStats.k);
		engine.timer.putMetaInNode("meta_cache_uncached", metaCacheStats.v);
		return cells;
	}

}
