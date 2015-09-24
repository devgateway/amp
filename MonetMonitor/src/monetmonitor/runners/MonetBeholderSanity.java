package monetmonitor.runners;

import java.sql.Connection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import monetmonitor.MonetBeholder;

public class MonetBeholderSanity extends MonetBeholder {
	
	public MonetBeholderSanity() {
		super();
		initInternalMonetTables();
	}
	
	private List<String> filterOutSystemTables(List<Object> tableNames) {
		List<String> filtered = new ArrayList<String>();
		for (Object item : tableNames) {
			String name = (String) item;
			if (!internalMonetTables.contains(name))
				filtered.add(name);
		}
		return filtered;
	}
	
	protected void runQuery(Connection conn) throws SQLException {
		String tableListQuery = "select tables.name from tables";
		
		List<Object> tableNames = runSelect(conn, tableListQuery);
		List<String> filteredTableNames = filterOutSystemTables(tableNames);
		for (String name : filteredTableNames) {
			String tableIntegrityQuery = String.format("select count(*) from  %s", name);
			runSelect(conn, tableIntegrityQuery);
		}

	}	
	static Set<String> internalMonetTables = null;	
	private static void initInternalMonetTables() {
		if (internalMonetTables == null) {
			internalMonetTables = new HashSet<String>();
			String[] items = {"schemas", "types", "functions", "args", "sequences", "dependencies", "connections", "_tables", "_columns", "keys", "idxs", "triggers", "objects", "_tables", "_columns", "keys", "idxs", "triggers", "objects", "tables", "columns", "db_user_info", "users", "user_role", "auths", "privileges", "querylog_catalog", "querylog_calls", "querylog_history", "tracelog", "sessions", "optimizers", "environment", "queue", "storage", "storagemodelinput", "storagemodel", "tablestoragemodel", "statistics", "systemfunctions"};
			internalMonetTables.addAll(Arrays.asList(items));
		}
	}




}
