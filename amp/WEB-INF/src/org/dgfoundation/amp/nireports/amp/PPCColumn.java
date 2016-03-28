package org.dgfoundation.amp.nireports.amp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.dgfoundation.amp.algo.VivificatingMap;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.CalendarConverter;
import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.MonetaryAmount;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.NumericalColumnBehaviour;
import org.dgfoundation.amp.nireports.schema.TrivialMeasureBehaviour;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;

/**
 * class for fetching PPC columns
 * @author Dolghier Constantin
 *
 */
public class PPCColumn extends PsqlSourcedColumn<CategAmountCell> {

	public PPCColumn(String columnName, String extractorViewName) {
		super(columnName, null, extractorViewName, "amp_activity_id", NumericalColumnBehaviour.getInstance());
	}
		
	@Override
	public List<CategAmountCell> fetch(NiReportsEngine engine) throws Exception {
		AmpReportsScratchpad scratchpad = AmpReportsScratchpad.get(engine);
		
		AmpReportsSchema schema = (AmpReportsSchema) engine.schema;
		AmpCurrency usedCurrency = scratchpad.getUsedCurrency();
		CalendarConverter calendarConverter = engine.calendar;
		VivificatingMap<String, AmpCurrency> currencies = new VivificatingMap<String, AmpCurrency>(new HashMap<>(), CurrencyUtil::getAmpcurrency);
		
		String query = buildQuery(engine);
		
		List<CategAmountCell> cells = new ArrayList<>();
		try(RsInfo rs = SQLUtils.rawRunQuery(scratchpad.connection, query, null)) {
			while (rs.rs.next()) {
				long ampActivityId = rs.rs.getLong(this.mainColumn);
				
				java.sql.Date transactionMoment = rs.rs.getDate("transaction_date");
				LocalDate transactionDate = transactionMoment.toLocalDate();
				BigDecimal transactionAmount = rs.rs.getBigDecimal("transaction_amount");
				
				String currencyCode = rs.rs.getString("currency_code");
				AmpCurrency srcCurrency = currencies.getOrCreate(currencyCode);
				
				BigDecimal usedExchangeRate = BigDecimal.valueOf(schema.currencyConvertor.getExchangeRate(srcCurrency.getCurrencyCode(), usedCurrency.getCurrencyCode(), null, transactionDate));
				MonetaryAmount amount = new MonetaryAmount(transactionAmount.multiply(usedExchangeRate), transactionAmount, srcCurrency, transactionDate, scratchpad.getPrecisionSetting());
				CategAmountCell cell = new CategAmountCell(ampActivityId, amount, MetaInfoSet.empty(), Collections.emptyMap(), calendarConverter.translate(transactionMoment));
				cells.add(cell);
			}
		}
		return cells;
	}

	@Override
	public List<ReportRenderWarning> performCheck() {
		return null;
	}
}
