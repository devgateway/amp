package org.dgfoundation.amp.testutils;

import org.dgfoundation.amp.ar.Column;

import java.util.Comparator;

public class ColumnComparator implements Comparator<Column>
{
    public int compare(Column a, Column b)
    {
        return a.getName().compareTo(b.getName());
    }
}

