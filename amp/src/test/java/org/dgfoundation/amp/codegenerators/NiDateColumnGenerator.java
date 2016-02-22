package org.dgfoundation.amp.codegenerators;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.nireports.DateCell;
import org.digijava.kernel.persistence.PersistenceManager;

public class NiDateColumnGenerator extends ColumnGenerator {

	private class Entry {
		final String activityTitle;
		final LocalDate date;
		public Entry(String activityTitle, LocalDate date, long activityId) {
			this.date = date;
			this.activityTitle = activityTitle;
		}
		
		public String toString() {
			return String.format("%s, %s", escape(this.activityTitle), escape(this.date.format(DateTimeFormatter.ISO_LOCAL_DATE)));
		}
		
		public String toCode() {
			return String.format("%s, %s", escape(this.activityTitle), escape(this.date.format(DateTimeFormatter.ISO_LOCAL_DATE)));
		}
	}
	
	final private List<Entry> entries;
	
	private Map<Long, String> getActivityNames() {
		String query = "SELECT amp_activity_id, name FROM amp_activity WHERE amp_team_id IN "
				+ "(SELECT amp_team_id FROM amp_team WHERE name = 'test workspace')";
		return (Map<Long, String>) PersistenceManager.getSession().doReturningWork(connection -> SQLUtils.collectKeyValue(connection, query));
	}
	
	private List<Entry> populateList() {
		List<Entry> result = new ArrayList<Entry>();
		final List<Entry> entries = new ArrayList<>();
		runInEngineContext( 
				new ArrayList<String>(getActivityNames().values()), 
				eng -> {
					Map<Long, String> activityNames = getActivityNames();
					@SuppressWarnings("unchecked")
					List<DateCell> cells = (List<DateCell>) eng.schema.getColumns().get(name).fetch(eng);
					for (DateCell cell : cells) {
						entries.add(new Entry(activityNames.get(cell.activityId), cell.date, cell.activityId));
					}
					entries.sort((Entry e1, Entry e2) -> {
						if (e1.activityTitle.equalsIgnoreCase(e2.activityTitle))
							return e1.date.compareTo(e2.date);
						return e1.activityTitle.compareToIgnoreCase(e2.activityTitle);
					});
					
				});
		return entries;
	}
	
	public NiDateColumnGenerator(String name) {
		super(name, DateCell.class);
		this.entries = populateList();
	}

	@Override
	public String generate() {
		StringBuilder strb = new StringBuilder();
		strb.append("Arrays.asList(\n");
		for (int i = 0; i < entries.size(); i++) {
			Entry ent = entries.get(i);
			strb.append("\t\tdateCell(");
			strb.append(ent.toCode());
			strb.append(")");
			if (i < entries.size() - 1)
				strb.append(",");
			strb.append("\n");
		}
		strb.append(");");
		return strb.toString();
	}

}
