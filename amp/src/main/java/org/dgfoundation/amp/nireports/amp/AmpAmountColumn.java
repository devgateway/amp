package org.dgfoundation.amp.nireports.amp;

import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiDimension;
import org.dgfoundation.amp.nireports.schema.NiReportColumn;
import org.digijava.kernel.translator.LocalizableLabel;

/**
 * @author Octavian Ciubotaru
 */
public abstract class AmpAmountColumn extends PsqlSourcedColumn<CategAmountCell> {

    public AmpAmountColumn(String columnName, NiDimension.LevelColumn levelColumn,
            String viewName, Behaviour<?> behaviour) {
        super(columnName, levelColumn, viewName, behaviour);
    }

    public AmpAmountColumn(String columnName, LocalizableLabel label,
            NiDimension.LevelColumn levelColumn, String viewName,
            Behaviour<?> behaviour) {
        super(columnName, label, levelColumn, viewName, behaviour);
    }

    public abstract boolean isTransactionLevelHierarchy(NiReportColumn<?> col);
}
