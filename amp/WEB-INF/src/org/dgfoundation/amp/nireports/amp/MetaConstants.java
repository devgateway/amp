package org.dgfoundation.amp.nireports.amp;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dgfoundation.amp.nireports.meta.MetaInfoSet;

/**
 * class containing various metainfo constants pertaining to the {@link AmpReportsSchema}
 * @author simple
 *
 */
public final class MetaConstants {
	public final static int addMetaIfIntExists(MetaInfoSet set, MetaCategory categ, ResultSet row, String columnName) throws SQLException {
		int val = row.getInt(columnName);
		if (val > 0)
			set.add(categ.category, val);
		return val;
	}

	public final static long addMetaIfLongExists(MetaInfoSet set, MetaCategory categ, ResultSet row, String columnName) throws SQLException {
		long val = row.getLong(columnName);
		if (val > 0)
			set.add(categ.category, val);
		return val;
	}

	/**
	 * category keys which can be found in the AmpReportsSchema metaInfo
	 * @author simple
	 *
	 */
	public static enum MetaCategory {
		
		TRANSACTION_TYPE("transaction_type"),
		ADJUSTMENT_TYPE("adjustment_type"),
		AGREEMENT_ID("agreement_id"),
		CAPITAL_SPEND_PERCENT("capital_spend_percent"),
		DONOR_ORG("donor_org"),
		FINANCING_INSTRUMENT("financing_instrument"),
		TERMS_OF_ASSISTANCE("terms_of_assistance"),
		PLEDGE_ID("pledge_id"),
		FUNDING_STATUS("funding_status"),
		MODE_OF_PAYMENT("mode_of_payment"),
		RECIPIENT_ORG("recipient_org"),
		RECIPIENT_ROLE("recipient_role"),
		SOURCE_ROLE("source_role");
		
		public final String category;
		private MetaCategory(String category) {
			this.category = category;
		}
	}
}
