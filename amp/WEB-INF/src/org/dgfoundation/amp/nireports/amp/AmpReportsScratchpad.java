package org.dgfoundation.amp.nireports.amp;

import java.sql.Connection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.ValueWrapper;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.viewfetcher.ColumnValuesCacher;
import org.dgfoundation.amp.ar.viewfetcher.PropertyDescription;
import org.dgfoundation.amp.newreports.CalendarConverter;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.SchemaSpecificScratchpad;
import org.dgfoundation.amp.nireports.amp.PercentagesCorrector.Snapshot;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
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
	public final Map<PropertyDescription, ColumnValuesCacher> columnCachers = new ConcurrentHashMap<>();
	public final Map<PercentagesCorrector, PercentagesCorrector.Snapshot> percsCorrectors = new ConcurrentHashMap<>();
	
	public final Connection connection;
	
	public final ReportEnvironment environment;
	public final NiReportsEngine engine;
	
	/**
	 * the currency used to render the report - do not write anything to it!
	 */
	protected final AmpCurrency usedCurrency;
	
	protected final NiPrecisionSetting precisionSetting = new AmpPrecisionSetting();

	public AmpReportsScratchpad(NiReportsEngine engine) {
		this.engine = engine;
		try {this.connection = PersistenceManager.getJdbcConnection();}
		catch(Exception e) {throw AlgoUtils.translateException(e);}
		this.usedCurrency = engine.spec.getSettings() == null || engine.spec.getSettings().getCurrencyCode() == null ? AmpARFilter.getDefaultCurrency() : 
			CurrencyUtil.getAmpcurrency(engine.spec.getSettings().getCurrencyCode());
		this.environment = ReportEnvironment.buildFor(TLSUtils.getRequest());
	}
	
	public AmpCurrency getUsedCurrency() {
		return usedCurrency;
	}
		
	public PercentagesCorrector.Snapshot buildOrGetSnapshot(PercentagesCorrector corrector, Set<Long> ids) {
		return percsCorrectors.computeIfAbsent(corrector, z -> buildSnapshot(z, ids));
	}
		
	public Snapshot buildSnapshot(PercentagesCorrector corrector, Set<Long> ids) {
		final ValueWrapper<Snapshot> snapshot = new ValueWrapper<>(null);
		engine.timer.run("normalize_percentages", () -> {
			snapshot.set(corrector.buildSnapshot(this.connection, ids));
			engine.timer.putMetaInNode("denormal activities", snapshot.value.sumOfPercentages.size());
		});
		return snapshot.value;
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
