<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<HTML>
    <HEAD>
	
		<TITLE><digi:trn key="gis:resultsmatrix">GIS Region Report</digi:trn></TITLE>
	
        <digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
        <digi:ref href="css/new_styles.css" type="text/css" rel="stylesheet" />
        <digi:ref href="css/gis.css" type="text/css" rel="stylesheet" />
    </HEAD>

    <BODY leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0">
	
	<digi:insert attribute="body" />
	
    </BODY>
</HTML>

