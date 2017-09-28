package org.digijava.module.aim.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface EntityFetcher<K> {
    public K fetch(ResultSet row) throws SQLException;
    public String[] getNeededColumnNames();
}
