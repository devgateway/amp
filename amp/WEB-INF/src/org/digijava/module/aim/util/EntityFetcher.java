package org.digijava.module.aim.util;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface EntityFetcher<K> {
    public K fetch(ResultSet row) throws SQLException;
    public String[] getNeededColumnNames();
}
