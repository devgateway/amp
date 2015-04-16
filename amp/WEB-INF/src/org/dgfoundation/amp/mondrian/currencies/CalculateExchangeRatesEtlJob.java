package org.dgfoundation.amp.mondrian.currencies;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.mondrian.EtlConfiguration;
import org.dgfoundation.amp.mondrian.MondrianTablesRepository;
import org.dgfoundation.amp.mondrian.monet.MonetConnection;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.util.CurrencyUtil;

import static org.dgfoundation.amp.mondrian.MondrianETL.MONDRIAN_EXCHANGE_RATES_TABLE;

/**
 * entry point for exchange-rates-connected ETL: exchange rates table & currency-specified amounts (CAGs)
 * @author Dolghier Constantin
 *
 */
public class CalculateExchangeRatesEtlJob {
	
	protected final Connection conn;
	protected final MonetConnection monetConn;
	protected final List<Long> currencyIds;
	
	/**
	 * the dates on which to run the incremental ETL
	 */
	protected final SortedSet<Long> usedDates;
	
	protected final EtlConfiguration etlConfig;	
	
	protected static Logger logger = Logger.getLogger(CalculateExchangeRatesEtlJob.class);
	
	public CalculateExchangeRatesEtlJob(List<Long> currencyIds, Connection conn, MonetConnection monetConn, EtlConfiguration etlConfig) {
		this.conn = conn;
		this.monetConn = monetConn;
		this.currencyIds = Collections.unmodifiableList(currencyIds == null ? SQLUtils.fetchLongs(monetConn.conn, "SELECT amp_currency_id FROM amp_currency") : currencyIds);
		this.etlConfig = etlConfig;
		this.usedDates = getCurrencyDates();
	}
			
	/**
	 * incrementally updates the exchange rates table and then incrementally updates the CAGs
	 * @throws SQLException
	 */
	public void work() throws SQLException {
		generateExchangeRatesTable();
		for(CurrencyAmountGroup cag:MondrianTablesRepository.CURRENCY_GROUPS)
			generateExchangeRatesColumns(cag);
	}

	/**
	 * incrementally updates a CAG
	 * @param cag
	 */
	private void generateExchangeRatesColumns(CurrencyAmountGroup cag) {
		Set<String> destTableColumns = monetConn.getTableColumns(cag.destinationTable);
		for (long currId:currencyIds) {
			if (!destTableColumns.contains(cag.getColumnName(currId)))
				monetConn.executeQuery(String.format("ALTER TABLE %s ADD %s DOUBLE", cag.destinationTable, cag.getColumnName(currId)));
		}
		
		/**
		 * possible configs:
		 * 	1. fullEtl -> no condition
		 *  2. incremental ETL: condition on both dates and activityids
		 */

		String condition = etlConfig.fullEtl ? "" : 
			String.format(" WHERE (%s) OR (%s.%sdate_code IN (%s))", etlConfig.entityIdsIn(cag.entityIdColumn), cag.destinationTable, cag.prefix, Util.toCSStringForIN(usedDates));
				
		if (currencyIds.size() > 2) {
			// smart
			if (!destTableColumns.contains(cag.prefix + "amount_base_currency")) {
				monetConn.executeQuery(String.format("ALTER TABLE %s ADD %s DOUBLE", cag.destinationTable, cag.prefix + "amount_base_currency"));
			}
			monetConn.executeQuery(String.format("UPDATE %s SET %samount_base_currency = "
					+ "%stransaction_amount * (select mer.exchange_rate from mondrian_exchange_rates mer WHERE mer.day_code = %s.%sdate_code AND mer.currency_id = %s.%scurrency_id) %s",
					cag.destinationTable, cag.prefix, cag.prefix, cag.destinationTable, cag.prefix, cag.destinationTable, cag.prefix,
					condition));
			for (long currId:currencyIds) {
				String query = String.format(
						"UPDATE %s SET %s = COALESCE("  
						+ "%samount_base_currency / (select mer.exchange_rate from mondrian_exchange_rates mer WHERE mer.day_code = %s.%sdate_code AND mer.currency_id = %d), %s) %s ",
						cag.destinationTable, cag.getColumnName(currId),
						cag.prefix, cag.destinationTable, cag.prefix,
						currId, MoConstants.UNDEFINED_AMOUNT_STR, condition); 
				monetConn.executeQuery(query);
				monetConn.flush();
			}
		} else {
			// stupid
			for (long currId:currencyIds) {
				String query = String.format(
					"UPDATE %s SET %s = COALESCE(%stransaction_amount * (select mer.exchange_rate from %s mer WHERE mer.day_code = %s.%sdate_code AND mer.currency_id = %s.%scurrency_id) / (select mer2.exchange_rate from %s mer2 WHERE mer2.day_code = %s.%sdate_code AND mer2.currency_id = %s) , %s) %s",
					cag.destinationTable, cag.getColumnName(currId),
					cag.prefix, MONDRIAN_EXCHANGE_RATES_TABLE, 
					cag.destinationTable, cag.prefix, 
					cag.destinationTable, cag.prefix,
					MONDRIAN_EXCHANGE_RATES_TABLE, 
					cag.destinationTable, cag.prefix,
					currId, MoConstants.UNDEFINED_AMOUNT_STR, condition);
				monetConn.executeQuery(query);
			}
			return;
		}
	}	
	
	/**
	 * generates exchange rate entries for the cartesian product (transaction date, currency)
	 * to be called every time:
	 * 1) an exchange rate changes
	 * 2) a new currency is added
	 * 3) a new transaction date appears in the database
	 */
	protected void generateExchangeRatesTable() throws SQLException {
		logger.warn("generating exchange rates ETL...");
		if (etlConfig.fullEtl) {
			monetConn.dropTable(MONDRIAN_EXCHANGE_RATES_TABLE);
			monetConn.executeQuery("CREATE TABLE " + MONDRIAN_EXCHANGE_RATES_TABLE + "(" + 
										"day_code integer NOT NULL," + 
										"currency_id bigint NOT NULL," + 
										"exchange_rate double)");
		}
		
		if (usedDates.isEmpty())
			return; // no dates to ETL
		List<Long> allCurrencies = SQLUtils.fetchLongs(monetConn.conn, "SELECT amp_currency_id FROM amp_currency");
		for (Long currency:allCurrencies) {
			generateExchangeRateEntriesForCurrency(CurrencyUtil.getAmpcurrency(currency), usedDates);
			monetConn.flush();
		}		
		logger.warn("... done generating exchange rates ETL...");
		//monetConn.copyTableFromPostgres(this.conn, MONDRIAN_EXCHANGE_RATES_TABLE);
	}
	
	/**
	 * calculates list of all dates for which one should calculate exchange rates
	 * @return
	 */
	protected SortedSet<Long> getCurrencyDates() {
		TreeSet<Long> res = new TreeSet<>();
		
		if (!etlConfig.fullEtl)
			res.addAll(etlConfig.dateCodes);
		
		for (CurrencyAmountGroup cag:MondrianTablesRepository.CURRENCY_GROUPS) {
			String where = etlConfig.fullEtl ? "" : ("WHERE " + etlConfig.entityIdsIn(cag.containingEntityIdColumn));
			String query = String.format("select distinct(%sdate_code) as day_code from %s %s", cag.prefix, cag.containingTable, where);
			res.addAll(SQLUtils.fetchLongs(monetConn.conn, query));
		}
		res.remove(0l);
		return res;
	}
	
	/**
	 * generates the exchange rates for the said currency for all the given dates
	 * @param ampCurrencyId
	 * @param allDates
	 * @throws SQLException
	 */
	protected void generateExchangeRateEntriesForCurrency(AmpCurrency currency, SortedSet<Long> usedDates) throws SQLException {
		List<List<Object>> entries = new CurrencyETL(currency, conn).work(usedDates);
		if (!etlConfig.fullEtl) {
			monetConn.executeQuery("DELETE FROM " + MONDRIAN_EXCHANGE_RATES_TABLE + " WHERE (currency_id = " + currency.getAmpCurrencyId() + ") AND (day_code IN (" + Util.toCSStringForIN(usedDates) + "))");
		}
		SQLUtils.insert(monetConn.conn, MONDRIAN_EXCHANGE_RATES_TABLE, null, null, Arrays.asList("day_code", "currency_id", "exchange_rate"), entries);
	}
}
