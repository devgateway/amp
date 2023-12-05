package org.dgfoundation.amp.newreports;

import java.util.Set;

/**
 * generates an SQL query which, run against a database, generates a list of IDs
 * @author Dolghier Constantin
 *
 */
public interface IdsGeneratorSource {
    public Set<Long> getIds();
}
