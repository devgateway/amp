package org.dgfoundation.amp.codegenerators;

import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tree generator used in dimensions code generation. 
 * @author acartaleanu
 *
 */
public abstract class TreeGenerator {


    protected final String table;
    protected final String idColumn;
    protected final String parentColumn;
    protected final String nameColumn;
    protected final List<TreeNode> roots;
    
    public TreeGenerator(String table, String idColumn, String parentColumn, String nameColumn) {
        this.table = table;
        this.idColumn = idColumn;
        this.parentColumn = parentColumn;
        this.nameColumn = nameColumn;
        this.roots = generateRoots();
    }
    
    public List<TreeNode> getRoots() {
        return roots;
    }
    
    /**
     * Generates nodes for one level of a dimension entity (for instance, 'get all organizations' or 'get all subsectors').
     * @param idColumn
     * @param nameColumn
     * @param parentColumn
     * @param table
     * @return
     */
    protected Map<Long, TreeNode> getLevelNodes(String idColumn, String nameColumn, String parentColumn, String table) {
        return PersistenceManager.getSession().doReturningWork(connection -> {
            Map<Long, TreeNode> nodes = new HashMap<Long, TreeNode>();
            String query = String.format("SELECT %s, %s, %s FROM %s", idColumn, nameColumn, parentColumn, table);
            try(RsInfo rsi = SQLUtils.rawRunQuery(connection, query, null)) {
                while (rsi.rs.next())
                {
                    Long id = rsi.rs.getLong(1);
                    String name = rsi.rs.getString(2);
                    Long parent = rsi.rs.getLong(3);
                    if (rsi.rs.wasNull())
                        parent = null;
                    nodes.put(id, new TreeNode(id, name, parent));
                }
            }
            catch(SQLException e) {
                throw new RuntimeException("Error fetching list of values with query " + query, e);
            }
            return nodes;
        }); 
    }
    
    /**
     * Populates the roots (afterwards used in code generation). 
     * Must override in subclass.
     */
    protected abstract List<TreeNode> generateRoots();
}
