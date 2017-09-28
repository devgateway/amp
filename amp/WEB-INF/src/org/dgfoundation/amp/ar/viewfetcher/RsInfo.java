package org.dgfoundation.amp.ar.viewfetcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;

import org.digijava.kernel.persistence.PersistenceManager;

/**
 * a class which encapsulates a ResultSet and its generated statement and which closes them both
 * @author Dolghier Constantin
 *
 */
public class RsInfo implements AutoCloseable {
    public final ResultSet rs;
    public final Statement statement;
    
    public RsInfo(ResultSet rs, Statement statement) {
        this.rs = rs;
        this.statement = statement;
    }
    
    public void forEach(Consumer<ResultSet> c) throws SQLException {
        while (rs.next()) {
            c.accept(rs);
        }
    }
    
    @Override public void close() throws SQLException {
        PersistenceManager.closeQuietly(this.statement);
        PersistenceManager.closeQuietly(this.rs);
    }
}
