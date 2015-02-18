package org.dgfoundation.amp.ar.viewfetcher;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.dgfoundation.amp.ar.FilterParam;

/**
 * a generic datasource for fetching a view. Could fetch data from a real database or from a dummy source (for testcases, for example)
 * @author Dolghier Constantin
 *
 */
public interface ViewFetcher 
{
	public RsInfo fetch(ArrayList<FilterParam> params);
}
