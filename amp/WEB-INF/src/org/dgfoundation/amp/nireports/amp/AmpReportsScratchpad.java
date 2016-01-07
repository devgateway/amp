package org.dgfoundation.amp.nireports.amp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.viewfetcher.ColumnValuesCacher;
import org.dgfoundation.amp.ar.viewfetcher.PropertyDescription;
import org.dgfoundation.amp.newreports.CalendarConverter;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.SchemaSpecificScratchpad;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;

/**
 * the AMP-schema-specific scratchpad <br />
 * please see {@link NiReportsEngine#schemaSpecificScratchpad} 
 * @author Dolghier Constantin
 *
 */
public class AmpReportsScratchpad implements SchemaSpecificScratchpad {
	 
	/**
	 * caching area for i18n fetchers
	 */
	public final Map<PropertyDescription, ColumnValuesCacher> columnCachers = new HashMap<>();
	
	public final Connection connection;
	
	/**
	 * the currency used to render the report - do not write anything to it!
	 */
	protected final AmpCurrency usedCurrency;
	
	protected final NiPrecisionSetting precisionSetting = new AmpPrecisionSetting();

	public AmpReportsScratchpad(NiReportsEngine engine) {
		try {this.connection = PersistenceManager.getJdbcConnection();}
		catch(Exception e) {throw AlgoUtils.translateException(e);}
		this.usedCurrency = engine.spec.getSettings() == null || engine.spec.getSettings().getCurrencyCode() == null ? AmpARFilter.getDefaultCurrency() : 
			CurrencyUtil.getAmpcurrency(engine.spec.getSettings().getCurrencyCode());
	}
	
	public AmpCurrency getUsedCurrency() {
		return usedCurrency;
	}
		
	public static AmpReportsScratchpad get(NiReportsEngine engine) {
		return (AmpReportsScratchpad) engine.schemaSpecificScratchpad;
	}
	
	@Override
	public void close() throws Exception {
		this.connection.close();
	}

	@Override
	public NiPrecisionSetting getPrecisionSetting() {
		return precisionSetting;
	}

	@Override
	public CalendarConverter getDefaultCalendar() {
		return AmpARFilter.getDefaultCalendar();
	}
}
