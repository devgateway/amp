package org.dgfoundation.amp.nireports.amp;

import java.util.LinkedHashSet;
import java.util.Set;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.nireports.Cell;
import org.dgfoundation.amp.nireports.NiUtils;
import org.dgfoundation.amp.nireports.SqlSourcedColumn;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension;


/**
 * a {@link SqlSourcedColumn} which uses AMP's PostgreSQL infrastructure as a source of data
 * @author Dolghier Constantin
 *
 */
public abstract class PsqlSourcedColumn<K extends Cell> extends SqlSourcedColumn<K> {
	
	final LinkedHashSet<String> viewColumns;
	
	public PsqlSourcedColumn(String columnName, NiDimension.LevelColumn levelColumn, String viewName, String mainColumn, Behaviour<?> behaviour) {
		super(columnName, levelColumn, viewName, mainColumn, behaviour, AmpReportsSchema.columnDescriptions.get(columnName));
		this.viewColumns = SQLUtils.getTableColumns(viewName);
		check();
	}
	
	/**
	 * utility function to check 
	 * @param col
	 */
	protected void check() {
		NiUtils.failIf(!SQLUtils.tableExists(viewName), String.format("column %s: view %s does not exist", name, viewName));
		Set<String> columns = SQLUtils.getTableColumns(viewName, true);
		NiUtils.failIf(!columns.contains(mainColumn), String.format("column %s: view %s does not have mainColumn %s", name, viewName, mainColumn));
	}

}
