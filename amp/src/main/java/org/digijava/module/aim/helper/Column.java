
package org.digijava.module.aim.helper;

public class Column 
{

    private Long columnId;
    private String columnName;
    private String columnAlias;
          
    public void setColumnId(Long l) 
    {
        columnId = l;
    }

    public void setColumnName(String s) 
    {
        columnName = s;
    }

    public void setColumnAlias(String s) 
    {
        columnAlias = s;
    }

          
    public Long getColumnId() 
    { 
        return columnId; 
    }

    public String getColumnName() 
    { 
        return columnName; 
    }

    public String getColumnAlias() 
    { 
        return columnAlias; 
    }

          
}
