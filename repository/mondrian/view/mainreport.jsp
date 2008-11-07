<%@page contentType="text/html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%--
// $Id: mainreport.jsp,v 1.1 2008-11-07 17:21:58 ddimunzio Exp $
// This software is subject to the terms of the Common Public License
// Agreement, available at the following URL:
// http://www.opensource.org/licenses/cpl.html.
// Copyright (C) 2001-2002 Kana Software, Inc.
// Copyright (C) 2002-2008 Julian Hyde and others.
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
//
// jhyde, 6 August, 2001
--%>
<html>
<head>
<meta name="description" content="Mondrian is an OLAP server written in Java. It enables you to interactively
analyze very large datasets stored in SQL databases without writing SQL.">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Mondrian OLAP Server</title>
<link rel="stylesheet" href="stylesheet.css" type="text/css" />
</head>
<body>
<script type="text/javascript">
function sendForm(value){
document.testpageform.query.value=value;
//alert(document.testpageform.query.value);
document.testpageform.submit();
	
}

</script>
<digi:form  action="/testpage.do" method="post">

<input type="hidden" name="query">
<p>Mondrian examples:</p>
<ul>
<li><a href="javascript:sendForm('cj')">Complex 5 level cross-join of dimensions - hierarchy with years+measures as columns, activity count. All amounts are converted to USD</a></li>
<li><a href="javascript:sendForm('by_donors')">By Donor dimension - 3 level horizontal hierarchy. Includes Donor Type/Donor Group/Donor Name</a></li>
<li><a href="javascript:sendForm('by_time')">By Time dimension - 3 level vertical hierarchy. Includes years, quarters, months </a></li></li>
</ul>
</digi:form>
</body>
</html>
