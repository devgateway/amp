package org.dgfoundation.amp.mondrian;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.mondrian.monet.MonetConnection;

import static org.dgfoundation.amp.mondrian.MondrianTablesRepository.FACT_TABLE;
import static org.dgfoundation.amp.mondrian.MondrianETL.MONDRIAN_EXCHANGE_RATES_TABLE;

public class CalculateExchangeRatesEtlJob {
	
	protected final MonetConnection monetConn;
	protected final List<Long> currencyIds;
	
	protected static Logger logger = Logger.getLogger(MondrianETL.class);
	
	public CalculateExchangeRatesEtlJob(List<Long> currencyIds, MonetConnection monetConn) {
		this.monetConn = monetConn;
		this.currencyIds = Collections.unmodifiableList(currencyIds == null ? SQLUtils.fetchLongs(monetConn.conn, "SELECT amp_currency_id FROM amp_currency") : currencyIds);
	}
			
	public void work() throws SQLException {
		Set<String> factTableColumns = monetConn.getTableColumns(FACT_TABLE.tableName);
		for (long currId:currencyIds) {
			if (!factTableColumns.contains(getColumnName(currId)))
				monetConn.executeQuery(String.format("ALTER TABLE %s ADD %s DOUBLE", FACT_TABLE.tableName, getColumnName(currId)));
		}
		
		if (currencyIds.size() > 2) {
			// smart
			if (!factTableColumns.contains("amount_base_currency")) {
				monetConn.executeQuery(String.format("ALTER TABLE %s ADD %s DOUBLE", FACT_TABLE.tableName, "amount_base_currency"));
			}
			monetConn.executeQuery("UPDATE mondrian_fact_table SET amount_base_currency = transaction_amount * (select mer.exchange_rate from mondrian_exchange_rates mer WHERE mer.day_code = mondrian_fact_table.date_code AND mer.currency_id = mondrian_fact_table.currency_id)");
			for (long currId:currencyIds) {
				String query = String.format(
						"UPDATE %s SET %s = " +  
						"amount_base_currency / (select mer.exchange_rate from mondrian_exchange_rates mer WHERE mer.day_code = mondrian_fact_table.date_code AND mer.currency_id = %d)",
						FACT_TABLE.tableName, getColumnName(currId), currId); 
				monetConn.executeQuery(query);
			}
		} else {
			// stupid
			for (long currId:currencyIds) {
				String query = String.format(
					"UPDATE %s SET %s = transaction_amount * (select mer.exchange_rate from %s mer WHERE mer.day_code = %s.date_code AND mer.currency_id = %s.currency_id) / (select mer2.exchange_rate from %s mer2 WHERE mer2.day_code = %s.date_code AND mer2.currency_id = %s)",
					FACT_TABLE.tableName, getColumnName(currId), MONDRIAN_EXCHANGE_RATES_TABLE, FACT_TABLE.tableName, FACT_TABLE.tableName, MONDRIAN_EXCHANGE_RATES_TABLE, FACT_TABLE.tableName, currId);
				monetConn.executeQuery(query);
			}
			return;
		}
	}
	
	public String getColumnName(long currId) {
		return "transaction_exch_" + currId;
	}
}
