package org.digijava.module.mondrian.form;

import org.apache.struts.action.ActionForm;

public class MondrianQueryForm extends ActionForm{

private String query="";

public String getQuery() {
	return query;
}

public void setQuery(String query) {
	this.query = query;
}

}
