package org.dgfoundation.amp.nireports.amp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

/**
 * @author Octavian Ciubotaru
 */
public class DoubleTextExtractor implements SimpleTextColumn.TextExtractor {

    public static final SimpleTextColumn.TextExtractor INSTANCE = new DoubleTextExtractor();

    private static ThreadLocal<DecimalFormat> formatter =
            ThreadLocal.withInitial(() -> new DecimalFormat("#.##########"));

    private DoubleTextExtractor() {
    }

    @Override
    public String extract(ResultSet rs, int index) throws SQLException {
        if (rs.getObject(index) == null) {
            return null;
        }
        return formatter.get().format(rs.getDouble(index));
    }
}
