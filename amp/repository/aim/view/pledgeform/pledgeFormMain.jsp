<%@ page pageEncoding="UTF-8" %>
<%@page trimDirectiveWhitespaces="true"%>
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
<%@ taglib uri="/taglib/aim" prefix="aim" %>

<c:set var="act">${param.extraAction}</c:set>
<%--<c:set var="rll_ajax">render_locations_list</c:set>  --%>
<%
	if ("add_locations_refresh_data".equals(request.getParameter("extraAction"))) // ajax?
	{
		%><jsp:include page="pledgelocationslist.jsp"></jsp:include><%  
	} else if ("add_locations_refresh_add".equals(request.getParameter("extraAction")))
	{
		%><jsp:include page="pledgelocationsAddPledge.jsp"></jsp:include><%
    } else 
    { // not ajax: render the full bootstrap iframe 
%>
<!DOCTYPE html>
<html lang="en">
  <head>
	<!-- IFRAME SEAMLESS !!! -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Pledges Form IFrame</title>

    <!-- Bootstrap -->
    <link rel="stylesheet" href="/repository/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="/repository/bootstrap/bootstrap-theme.min.css">   
    <!-- Bootstrap-select http://silviomoreto.github.io/bootstrap-select/ -->
    <link href="/repository/bootstrap/bootstrap-select.min.css" rel="stylesheet" type="text/css">
    <link href="/repository/bootstrap/jquery.pnotify.default.css" media="all" rel="stylesheet" type="text/css" />
    <link href="/repository/bootstrap/jquery.pnotify.default.icons.css" media="all" rel="stylesheet" type="text/css" />
<!-- <link href="/repository/bootstrap/lightbox/ekko-lightbox.min.css" media="all" rel="stylesheet" type="text/css" />  -->        
    <!-- <link href="/repository/bootstrap/bootstrap-dialog.min.css" rel="stylesheet" type="text/css" />  -->
    <link href="/repository/bootstrap/amp-bootstrap.css" rel="stylesheet" type="text/css" /> <!-- this should always be included last -->
    
    <!-- THESE 4 FILES SHOULD ALWAYS BE THE FIRST INCLUDED JS, IN THIS ORDER -->
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<script src="/repository/bootstrap/amp-bootstrap.js" type="text/javascript"></script>    
	<script src="/repository/bootstrap/hacks.js" type="text/javascript"></script>    
    <script src="/repository/aim/view/pledgeform/pfscripts.js" type="text/javascript"></script>
	<script src="/repository/aim/view/bootstrap/forms.js" type="text/javascript"></script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body class="main_side">
  
	<aim:renderFormSubsection title="Pledge Identification">
		<jsp:include page="pledgeIdentification.jsp"></jsp:include>
	</aim:renderFormSubsection>	
			
	<field:display name="Pledge Sector" feature="Pledge Sector and Location">
		<aim:renderFormSubsection title="Sector" styleId="pledge_form_sectors">
			<jsp:include page="pledgeSectors.jsp"></jsp:include>
			<div class="text-center"><button type="button" onclick="sectorsController.showAdditionArea(this);" class="btn btn-success btn-sm" id='pledge_form_sectors_data_add'><digi:trn>Add Sector</digi:trn></button></div>
			<jsp:include page="pledgeSectorsAddSector.jsp">
				<jsp:param name="DISABLE_AJAX_BODIES" value="true" />
			</jsp:include>
		</aim:renderFormSubsection>
	</field:display>
	
 	<field:display name="Pledge Location" feature="Pledge Sector and Location">
		<aim:renderFormSubsection title="Location" styleId="pledge_form_locations">			
			<jsp:include page="pledgelocationslist.jsp"></jsp:include>
			<div class="text-center"><button type="button" onclick="locationsController.showAdditionArea(this);" class="btn btn-success btn-sm" id='pledge_form_locations_data_add'><digi:trn>Add Location</digi:trn></button></div>
			<jsp:include page="pledgelocationsAddPledge.jsp">
				<jsp:param name="DISABLE_AJAX_BODIES" value="true" />
			</jsp:include>
		</aim:renderFormSubsection>
	</field:display>
	
	<field:display name="Pledge Program" feature="Pledge Sector and Location">
		<aim:renderFormSubsection title="Program" styleId="pledge_form_programs">
			<jsp:include page="pledgePrograms.jsp"></jsp:include>
			<div class="text-center"><button type="button" onclick="programsController.showAdditionArea(this);" class="btn btn-success btn-sm" id='pledge_form_programs_data_add'><digi:trn>Add Program</digi:trn></button></div>
			<jsp:include page="pledgeProgramsAddProgram.jsp">
				<jsp:param name="DISABLE_AJAX_BODIES" value="true" />			
			</jsp:include>
		</aim:renderFormSubsection>
	</field:display>
	
	<feature:display name="Pledge Funding" module="Pledges">
		<aim:renderFormSubsection title="Pledge Information" styleId="pledge_form_funding">
			<jsp:include page="pledgeFunding.jsp"></jsp:include>
			<div class="text-center"><button type="button" onclick="fundingsController.addNewItem(this);" class="btn btn-success btn-sm" id="pledge_form_funding_data_add"><digi:trn>Add Pledge Funding</digi:trn></button></div>
			<div id="pledge_form_funding_change">&nbsp; <!-- our super-duper controller checks for this div to exist --></div>
		</aim:renderFormSubsection>
	</feature:display>
	
	<jsp:include page="/repository/aim/view/pledgeform/pledgeContacts.jsp"></jsp:include>
	<jsp:include page="/repository/aim/view/pledgeform/pledgeEpilogue.jsp"></jsp:include>
	
	<!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="/repository/bootstrap/bootstrap.min.js"></script>
    <script src="/repository/bootstrap/bootstrap-select.min.js" type="text/javascript"></script>
    <script src="/repository/bootstrap/jquery.pnotify.min.js" type="text/javascript"></script>
    <script src="/repository/bootstrap/bootstrap-dialog.min.js" type="text/javascript"></script>
    <script src="/repository/bootstrap/bootstrap-utils.js" type="text/javascript"></script>
 <!--   <script src="/repository/bootstrap/lightbox/ekko-lightbox.min.js" type="text/javascript"></script>  --> 
	<div id="iframe-hack-end-marker"></div>
  </body>
</html>
<%
    } // the big "otherwise"
%>
