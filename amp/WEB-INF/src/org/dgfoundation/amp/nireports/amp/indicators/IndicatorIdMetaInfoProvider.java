package org.dgfoundation.amp.nireports.amp.indicators;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.amp.MetaCategory;
import org.dgfoundation.amp.nireports.amp.MetaInfoProvider;
import org.dgfoundation.amp.nireports.meta.MetaInfoGenerator;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;

/**
 * MetaInfoSet provider that extracts indicator_id from underlying view.
 *
 * @author Octavian Ciubotaru
 */
public class IndicatorIdMetaInfoProvider implements MetaInfoProvider {

    public static final IndicatorIdMetaInfoProvider instance = new IndicatorIdMetaInfoProvider();

    @Override
    public MetaInfoSet provide(NiReportsEngine engine, ResultSet rs, MetaInfoGenerator metaInfoGenerator)
            throws SQLException {
        MetaInfoSet metaInfo = new MetaInfoSet(metaInfoGenerator);
        metaInfo.add(MetaCategory.INDICATOR_ID.category, rs.getLong("indicator_id"));
        return metaInfo;
    }
}
