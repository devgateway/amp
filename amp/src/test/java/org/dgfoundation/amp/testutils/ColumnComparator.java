package org.dgfoundation.amp.testutils;

import java.util.Comparator;

import org.dgfoundation.amp.ar.Column;

public class ColumnComparator implements Comparator<Column>
{
	public int compare(Column a, Column b)
	{
		return a.getName().compareTo(b.getName());
	}
}

