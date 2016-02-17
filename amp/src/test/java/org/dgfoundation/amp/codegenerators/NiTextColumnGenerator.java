package org.dgfoundation.amp.codegenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.nireports.TextCell;
import org.digijava.kernel.persistence.PersistenceManager;


/**
 * Code generator for NiTextColumn cells. 
 * @author acartaleanu
 *
 */
public class NiTextColumnGenerator extends ColumnGenerator {


	final List<Entry> entries;
	
	
	class Entry {
		public final String aavname;
		public final String text;
		
		public Entry(String aavname, String name) {
			this.aavname = aavname;
			this.text = name;
		}
	}
	
	public NiTextColumnGenerator(String columnName) {
		super(columnName, TextCell.class);
		this.entries = populateList();
	}
	
	private Map<Long, String> getActivityNames() {
		String query = "SELECT amp_activity_id, name FROM amp_activity_version WHERE amp_team_id IN "
				+ "(SELECT amp_team_id FROM amp_team WHERE name = 'test workspace')"
				+ "AND amp_activity_id IN (SELECT amp_activity_id FROM amp_activity)";
		return (Map<Long, String>) PersistenceManager.getSession().doReturningWork(connection -> SQLUtils.collectKeyValue(connection, query));
	}
	
	private List<Entry> populateList() {
		final List<Entry> entries = new ArrayList<>();
		runInEngineContext( 
				new ArrayList<String>(getActivityNames().values()), 
				eng -> {
					Map<Long, String> activityNames = getActivityNames();
					@SuppressWarnings("unchecked")
					List<TextCell> cells = (List<TextCell>) eng.schema.getColumns().get(name).fetch(eng);
					for (TextCell cell : cells) {
						entries.add(new Entry(activityNames.get(cell.activityId), cell.text));
					}
					entries.sort((Entry e1, Entry e2) -> {
						if (e1.aavname.equals(e2.aavname))
							return e1.text.compareTo(e2.text);
						return e1.aavname.compareTo(e2.aavname);
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
			strb.append(String.format("%s, %s", escape(ent.aavname), escape(ent.text)));
			strb.append(")");
			if (i < entries.size() - 1)
				strb.append(",");
			strb.append("\n");
		}
		strb.append(");");
		return strb.toString();
	}
}
