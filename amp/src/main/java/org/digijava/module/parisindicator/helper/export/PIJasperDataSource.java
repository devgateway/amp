package org.digijava.module.parisindicator.helper.export;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class PIJasperDataSource implements JRDataSource {

    private Object data[][];
    private int index;
    private int columnCount;
    private String name[];

    public PIJasperDataSource() {
        data = null;
        index = -1;
    }

    public PIJasperDataSource(Object[][] obj) {
        data = new Object[obj.length][obj[0].length];
        index = -1;
        for (int i = 0; i < obj.length; i++) {
            for (int j = 0; j < obj[0].length; j++) {
                data[i][j] = obj[i][j];
            }
        }
        columnCount = obj[0].length;
        name = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            name[i] = "m" + Integer.toString(i + 1);
        }
    }

    public boolean next() throws JRException {
        index++;
        return (index < data.length);
    }

    public Object getFieldValue(JRField field) throws JRException {
        String fieldName = field.getName();
        Object value = null;
        for (int i = 0; i < columnCount; i++) {
            if (name[i].equals(fieldName)) {
                value = data[index][i];
            }
        }
        return value;
    }

    public String[] getName() {
        return name;
    }

    public void setName(String[] name) {
        this.name = name;
    }
}
