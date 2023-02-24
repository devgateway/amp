package org.digijava.kernel.services.sync;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.digijava.module.common.util.DateTimeUtil;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author Octavian Ciubotaru
 */
public class JulianDayRowMapper implements RowMapper<Date> {

    public static final JulianDayRowMapper INSTANCE = new JulianDayRowMapper();

    @Override
    public Date mapRow(ResultSet rs, int rowNum) throws SQLException {
        return DateTimeUtil.fromJulianNumberToDate(rs.getString(1));
    }
}
