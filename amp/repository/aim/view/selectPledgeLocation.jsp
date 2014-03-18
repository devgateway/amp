<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<c:set var="act">${param.extraAction}</c:set>
<c:set var="rll_ajax">render_locations_list</c:set>
<%
	if ("render_locations_list".equals(request.getParameter("extraAction"))) // ajax?
	{
		%><jsp:include page="/repository/aim/view/pledgeform/pledgelocationslist.jsp"></jsp:include><%  
	} else if ("render_locations_add".equals(request.getParameter("extraAction")))
	{
		%><jsp:include page="/repository/aim/view/pledgeform/pledgelocationsAddPledge.jsp"></jsp:include><%
    } else 
    { // not ajax: render the full bootstrap iframe 
%>
<!DOCTYPE html>
<html lang="en">
  <head>
	<!-- IFRAME SEAMLESS !!! -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Pledges Location Bootstrap 3</title>

    <!-- Bootstrap -->
    <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootswatch/3.1.1/united/bootstrap.min.css">
    <!-- Bootstrap-select http://silviomoreto.github.io/bootstrap-select/ -->
    <link href="/repository/bootstrap/bootstrap-select.min.css" rel="stylesheet" type="text/css">
    <link href="/repository/bootstrap/jquery.pnotify.default.css" media="all" rel="stylesheet" type="text/css" />
    <link href="/repository/bootstrap/jquery.pnotify.default.icons.css" media="all" rel="stylesheet" type="text/css" />
    <link href="/repository/bootstrap/bootstrap-dialog.min.css" rel="stylesheet" type="text/css" />
    <link href="/repository/bootstrap/amp-bootstrap.css" rel="stylesheet" type="text/css" /> <!-- this should always be included last -->
    
    <!-- THESE 4 FILES SHOULD ALWAYS BE THE FIRST INCLUDED JS, IN THIS ORDER -->
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<script src="/repository/bootstrap/amp-bootstrap.js" type="text/javascript"></script>    
	<script src="/repository/bootstrap/hacks.js" type="text/javascript"></script>    
    <script src="/repository/aim/view/pledgeform/pfscripts.js" type="text/javascript"></script>
	    
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body style="min-height: 350px">
  	<jsp:include page="/repository/aim/view/pledgeform/pledgeIdentification.jsp"></jsp:include>
  	<field:display name="Pledge Location" feature="Pledge Sector and Location">
		<jsp:include page="/repository/aim/view/pledgeform/pledgelocationslist.jsp"></jsp:include>
		<div class="text-center"><button type="button"class="btn btn-success btn-sm" id='add_location_button'>Add Location</button></div>
		<jsp:include page="/repository/aim/view/pledgeform/pledgelocationsAddPledge.jsp"></jsp:include>
	</field:display>
	
	<jsp:include page="/repository/aim/view/pledgeform/pledgeContacts.jsp"></jsp:include>
	<jsp:include page="/repository/aim/view/pledgeform/pledgeEpilogue.jsp"></jsp:include>
	
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
    <script src="/repository/bootstrap/bootstrap-select.min.js" type="text/javascript"></script>
    <script src="/repository/bootstrap/jquery.pnotify.min.js" type="text/javascript"></script>
    <script src="/repository/bootstrap/bootstrap-dialog.min.js" type="text/javascript"></script>
    <script src="/repository/bootstrap/bootstrap-utils.js" type="text/javascript"></script>
  </body>
</html>
<%
    } // the big "otherwise"
%>
