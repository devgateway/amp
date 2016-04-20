package org.dgfoundation.amp.codegenerators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;

public class ActivityTitlesGenerator extends CodeGenerator {

	public ActivityTitlesGenerator() {
		activities = getActivityNames();
		entries = populateEntries();
	}

	final private Map<Long, String> activities;
	private List<Entry> entries;
	
	private Map<Long, String> getActivityNames() {
		String query = "SELECT amp_activity_id, name FROM amp_activity_version WHERE amp_team_id IN "
				+ "(SELECT amp_team_id FROM amp_team WHERE name = 'test workspace')"
				+ "AND amp_activity_id IN (SELECT amp_activity_id FROM amp_activity)";
		return (Map<Long, String>) PersistenceManager.getSession().doReturningWork(connection -> SQLUtils.collectKeyValue(connection, query));
	}
	
	List<Entry> populateEntries() {
		List<Entry> entries = new ArrayList<Entry>();
		for (Map.Entry<Long, String> entry : activities.entrySet()) {
			entries.add(new Entry(entry.getKey(), entry.getValue()));
		}
		entries.sort(null);
		return Collections.unmodifiableList(entries);
	}
	
	
	
	class Entry implements Comparable<Entry>{
		public final long id;
		public final String text;
		public Entry(long id, String text) {
			this.id = id;
			this.text = text;
		}
		@Override
		public int compareTo(Entry o) {
			return this.text.toLowerCase().compareTo(o.text.toLowerCase());
		}
	}
	
	private String generateCode() {
		StringBuilder strb = new StringBuilder();
		strb.append("\n");
		for (int i = 0; i < entries.size(); i++) {
			Entry ent = entries.get(i);
			strb.append("\t\tactivity(");
			strb.append(String.format("%s, %d", escape(ent.text), ent.id));
			strb.append(");\n");
		}
		return strb.toString();
	}
	
	
	@Override
	public String generate() {
		StringBuilder strb = new StringBuilder();
		strb.append(generateCode());
		return strb.toString();
	}

}
