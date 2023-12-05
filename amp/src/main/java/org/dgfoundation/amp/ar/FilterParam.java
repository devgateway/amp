package org.dgfoundation.amp.ar;

public class FilterParam {
    Object value;
    int sqlType;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getSqlType() {
        return sqlType;
    }

    public void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }

    public FilterParam(Object value, int sqlType) {
        this.value=value;
        this.sqlType=sqlType;
    }
}
