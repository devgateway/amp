package org.dgfoundation.amp.codegenerators;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.nireports.PercentageTextCell;
import org.digijava.kernel.persistence.PersistenceManager;


/**
 * Code generator for NiTextPercentageColumn cells. 
 * @author acartaleanu
 *
 */
public class NiPercentageTextColumnGenerator extends ColumnGenerator {


	final String name;
	final List<Entry> entries;
	
	
	class Entry {
		public final String aavname;
		public final String text;
		public final long id;
		public final BigDecimal percentage;
		
		public Entry(String aavname, String name, long id, BigDecimal percentage) {
			this.aavname = aavname;
			this.text = name;
			this.id = id;
			this.percentage = percentage;
		}
		
		@Override
		public String toString() {
			return String.format("%s: %s (%d) %.2f", aavname, name, id, percentage.doubleValue());
		}
	}
	
	public NiPercentageTextColumnGenerator(String columnName) {
		super(columnName, PercentageTextCell.class);
		this.name = columnName;
		this.entries = populateList();
	}
	
	private Map<Long, String> getActivityNames() {
		String query = "SELECT amp_activity_id, name FROM amp_activity WHERE amp_team_id IN "
				+ "(SELECT amp_team_id FROM amp_team WHERE name = 'test workspace')";
		return (Map<Long, String>) PersistenceManager.getSession().doReturningWork(connection -> SQLUtils.collectKeyValue(connection, query));
	}
	
	/**
	 * Uses the reports engine and AmpReportsSchema to fetch cells.
	 * It's not done via direct selects because of the importance of normalization (Normalized*Column.java)
	 * @return
	 */
	private List<Entry> populateList() {
		final List<Entry> entries = new ArrayList<>();
		runInEngineContext( 
				new ArrayList<String>(getActivityNames().values()), 
				eng -> {
					@SuppressWarnings("unchecked")
					List<PercentageTextCell> cells = (List<PercentageTextCell>) eng.schema.getColumns().get(name).fetch(eng);
					Map<Long, String> activityNames = getActivityNames();
					for (PercentageTextCell cell : cells) {
						entries.add(new Entry(activityNames.get(cell.activityId), cell.text, cell.entityId, cell.percentage));
					}
					entries.sort((Entry e1, Entry e2) -> {
						if (e1.aavname.equalsIgnoreCase(e2.aavname))
							return e1.text.compareToIgnoreCase(e2.text);
						return e1.aavname.compareToIgnoreCase(e2.aavname);
					});
				});
		return entries;
	}
	
	@Override
	public String generate() {
		StringBuilder strb = new StringBuilder();
		strb.append("Arrays.asList(\n");
		for (int i = 0; i < entries.size(); i++) {
			Entry ent = entries.get(i);
			strb.append("\t\t\tcell(");
			strb.append(String.format("%s, %s, %d, %s", escape(ent.aavname), escape(ent.text), ent.id, ent.percentage));
			strb.append(")");
			if (i < entries.size() - 1)
				strb.append(",\n");
		}
		strb.append(");");
		return strb.toString();
	}
}
