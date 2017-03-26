package org.dgfoundation.amp.nireports.amp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.dgfoundation.amp.newreports.ReportRenderWarning;
import org.dgfoundation.amp.nireports.DoubleCell;
import org.dgfoundation.amp.nireports.NiReportsEngine;
import org.dgfoundation.amp.nireports.meta.MetaInfoGenerator;
import org.dgfoundation.amp.nireports.meta.MetaInfoSet;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension;

/**
 * @author Octavian Ciubotaru
 */
public class DoubleColumn extends AmpDifferentialColumn<DoubleCell, Boolean> {

    private MetaInfoGenerator metaInfoGenerator;

    private MetaInfoProvider metaInfoProvider = MetaInfoProvider.empty;

    public DoubleColumn(String columnName,
            NiDimension.LevelColumn levelColumn, String viewName,
            Behaviour<?> behaviour) {
        super(columnName, levelColumn, viewName, (engine, col) -> true, behaviour);
    }

    @Override
    public synchronized List<DoubleCell> fetch(NiReportsEngine engine) {
        metaInfoGenerator = new MetaInfoGenerator();
        return super.fetch(engine);
    }

    @Override
    public List<ReportRenderWarning> performCheck() {
        return null;
    }

    @Override
    protected DoubleCell extractCell(NiReportsEngine engine, ResultSet rs) throws SQLException {
        Double value = (rs.getObject(2) == null) ? null : rs.getDouble(2);

        MetaInfoSet metaInfo = metaInfoProvider.provide(engine, rs, metaInfoGenerator);

        return new DoubleCell(value, rs.getLong(1), rs.getLong(3), metaInfo, levelColumn);
    }

    @Override
    public boolean getKeptInSummaryReports() {
        return false;
    }

    public DoubleColumn withMetaInfoProvider(MetaInfoProvider provider) {
        this.metaInfoProvider = provider;
        return this;
    }
}
