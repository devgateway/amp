package org.dgfoundation.amp.nireports.amp;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.meta.MetaInfoGenerator;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;

/**
 * Allows to extract additional information from underlying view and return it as an MetaInfoSet.
 *
 * @author Octavian Ciubotaru
 */
public interface MetaInfoProvider {

    MetaInfoProvider empty = (engine, rs, metaInfoGenerator) -> MetaInfoSet.empty();

    MetaInfoSet provide(NiReportsEngine engine, ResultSet rs, MetaInfoGenerator metaInfoGenerator) throws SQLException;
}
