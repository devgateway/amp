package org.dgfoundation.amp.ar.viewfetcher;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.FilterParam;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * a generic datasource for fetching a view. Could fetch data from a real database or from a dummy source (for testcases, for example)
 * @author Dolghier Constantin
 *
 */
public interface ViewFetcher {
    public RsInfo fetch(ArrayList<FilterParam> params);
    
    public default void forEach(Consumer<ResultSet> c) {
        try(RsInfo rsInfo = fetch(null)) {
            rsInfo.forEach(c);
        }
        catch(SQLException e) {
            throw AlgoUtils.translateException(e);
        }
    }
}
