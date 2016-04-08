package org.dgfoundation.amp.nireports.amp;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.algo.ValueWrapper;
import org.dgfoundation.amp.algo.timing.InclusiveTimer;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.viewfetcher.ColumnValuesCacher;
import org.dgfoundation.amp.ar.viewfetcher.PropertyDescription;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.mondrian.MondrianTableDescription;
import org.dgfoundation.amp.mondrian.MondrianTablesRepository;
import org.dgfoundation.amp.mondrian.jobs.Fingerprint;
import org.dgfoundation.amp.newreports.CalendarConverter;
import org.dgfoundation.amp.newreports.ReportEnvironment;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.ImmutablePair;
import org.dgfoundation.amp.nireports.NiPrecisionSetting;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.SchemaSpecificScratchpad;
import org.dgfoundation.amp.nireports.amp.PercentagesCorrector.Snapshot;
import org.dgfoundation.amp.nireports.amp.diff.DifferentialCache;
import org.dgfoundation.amp.nireports.runtime.CachingCalendarConverter;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
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
	//public final Map<PercentagesCorrector, PercentagesCorrector.Snapshot> percsCorrectors = new ConcurrentHashMap<>();
	
	public final Connection connection;
	
	public final ReportEnvironment environment;
	public final NiReportsEngine engine;
	public final long lastEventId;
	
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
		this.lastEventId = SQLUtils.getLong(this.connection, "SELECT COALESCE(max(event_id), -1) FROM amp_etl_changelog");
	}
	
	public AmpCurrency getUsedCurrency() {
		return usedCurrency;
	}
		
//	public PercentagesCorrector.Snapshot buildOrGetSnapshot(PercentagesCorrector corrector, Set<Long> ids) {
//		return percsCorrectors.computeIfAbsent(corrector, z -> buildSnapshot(z, ids));
//	}
		
	public Snapshot buildSnapshot(Snapshot earlyEntries, PercentagesCorrector corrector, Set<Long> ids) {
		final ValueWrapper<Snapshot> snapshot = new ValueWrapper<>(null);
		engine.timer.run("normalize_percentages", () -> {
			snapshot.set(corrector.buildSnapshot(this.connection, ids));
			engine.timer.putMetaInNode("denormal activities", snapshot.value.sumOfPercentages.size());
		});
		return earlyEntries == null ? snapshot.value : earlyEntries.mergeWith(snapshot.value);
	}
	
	public Set<Long> getChangedEntities(String mainColumn, long firstEventId) {
		String entityType = mainColumn.equals("amp_activity_id") ? "activity" : "pledge";
		Set<Long> changedEntityIds = new HashSet<>(SQLUtils.fetchLongs(this.connection, String.format("SELECT DISTINCT entity_id FROM amp_etl_changelog WHERE (entity_name='%s') AND (event_id > %d) AND (event_id <= %d)", entityType, firstEventId, lastEventId)));
		return changedEntityIds;
	}
	
	public<K extends Cell> Set<Long> differentiallyImportCells(InclusiveTimer timer, String mainColumn, DifferentialCache<K> cache, Function<Set<Long>, List<K>> fetcher) {
		Set<Long> changedEntityIds = getChangedEntities(mainColumn, cache.getLastEventId());
		Set<Long> idsToReplace = AmpCollections.minusPlus(engine.getMainIds(), cache.getCachedEntityIds(), changedEntityIds);
		List<K> fetchedCells = fetcher.apply(idsToReplace);
		cache.importCells(idsToReplace, fetchedCells, lastEventId);

		timer.putMetaInNode("lastEventId", lastEventId);
		timer.putMetaInNode("changedEntities", changedEntityIds.size());
		timer.putMetaInNode("fetchedIds", idsToReplace.size());
		timer.putMetaInNode("fetchedCells", fetchedCells.size());
		
		return idsToReplace;
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
	public CachingCalendarConverter buildCalendarConverter() {
		CalendarConverter underlyingConverter = buildUnderlyingCalendarConverter(engine.spec);
		return new CachingCalendarConverter(underlyingConverter, TranslatorWorker.translateText(underlyingConverter.getDefaultFiscalYearPrefix()));
	}

	@Override
	public CalendarConverter getDefaultCalendar() {
		return AmpARFilter.getDefaultCalendar();
	}
}
