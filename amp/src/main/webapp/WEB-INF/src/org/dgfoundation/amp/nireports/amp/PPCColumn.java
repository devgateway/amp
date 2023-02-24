package org.dgfoundation.amp.nireports.amp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.dgfoundation.amp.algo.VivificatingMap;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.currencyconvertor.CurrencyConvertor;
import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.diff.CategAmountCellProto;
import org.dgfoundation.amp.nireports.behaviours.NumericalColumnBehaviour;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.runtime.CachingCalendarConverter;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;

/**
 * class for fetching PPC columns. Since ProposedProjectCost is an activity-level attribute, these cells lack any coordinates.
 * @author Dolghier Constantin
 *
 */
public class PPCColumn extends PsqlSourcedColumn<CategAmountCell> {

    public PPCColumn(String columnName, String extractorViewName) {
        super(columnName, null, extractorViewName, NumericalColumnBehaviour.getInstance());
    }
        
    @Override
    public List<CategAmountCell> fetch(NiReportsEngine engine) throws Exception {
        AmpReportsScratchpad scratchpad = AmpReportsScratchpad.get(engine);
        
        AmpReportsSchema schema = (AmpReportsSchema) engine.schema;
        AmpCurrency usedCurrency = scratchpad.getUsedCurrency();
        NiPrecisionSetting precisionSetting = scratchpad.getPrecisionSetting();
        CachingCalendarConverter calendar = engine.calendar;
        CurrencyConvertor currencyConvertor = schema.currencyConvertor;
        
        VivificatingMap<Long, AmpCurrency> currencies =
                new VivificatingMap<Long, AmpCurrency>(new HashMap<>(), CurrencyUtil::getAmpcurrency);
        
        String query = buildQuery(engine);
        
        List<CategAmountCell> cells = new ArrayList<>();
        try(RsInfo rs = SQLUtils.rawRunQuery(scratchpad.connection, query, null)) {
            while (rs.rs.next()) {
                long ampActivityId = rs.rs.getLong(this.mainColumn);
                
                java.sql.Date transactionMoment = rs.rs.getDate("transaction_date");
                BigDecimal transactionAmount = rs.rs.getBigDecimal("transaction_amount");
                
                Long currencyId = rs.rs.getLong("currency_id");
                AmpCurrency origCurrency = currencies.getOrCreate(currencyId);
                
                CategAmountCellProto cellProto = new CategAmountCellProto(ampActivityId, transactionAmount, 
                        origCurrency, transactionMoment, MetaInfoSet.empty(), Collections.emptyMap(), null);
                
                cells.add(cellProto.materialize(usedCurrency, calendar, currencyConvertor, precisionSetting, false));
                
                /* 
                 * AMP-27571
                 * if canSplittingStrategyBeAdded is true we need to duplicate cells with original currencies
                */
                if (engine.spec.isShowOriginalCurrency()) {
                    cells.add(cellProto.materialize(usedCurrency, calendar, currencyConvertor, precisionSetting, 
                            true));
                } 
            }
        }
        
        return cells;
    }
    
    @Override
    public List<ReportRenderWarning> performCheck() {
        return null;
    }
}
