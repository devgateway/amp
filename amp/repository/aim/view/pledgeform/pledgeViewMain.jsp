<%@ page pageEncoding="UTF-8" %>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>

    <!-- Bootstrap-select http://silviomoreto.github.io/bootstrap-select/ -->
    <link href="/repository/bootstrap/bootstrap-select.min.css" rel="stylesheet" type="text/css">
    <link href="/repository/bootstrap/jquery.pnotify.default.css" media="all" rel="stylesheet" type="text/css" />
    <link href="/repository/bootstrap/jquery.pnotify.default.icons.css" media="all" rel="stylesheet" type="text/css" />
<!-- <link href="/repository/bootstrap/lightbox/ekko-lightbox.min.css" media="all" rel="stylesheet" type="text/css" />  -->        
    <link href="/repository/bootstrap/bootstrap-dialog.min.css" rel="stylesheet" type="text/css" />
    <link href="/repository/bootstrap/amp-bootstrap.css" rel="stylesheet" type="text/css" /> <!-- this should always be included last -->
    
    <!-- THESE 4 FILES SHOULD ALWAYS BE THE FIRST INCLUDED JS, IN THIS ORDER -->
	<script src="/repository/bootstrap/amp-bootstrap.js" type="text/javascript"></script>       
  <body class="main_side">
  	<logic:present name="PNOTIFY_ERROR_MESSAGE" scope="request">
  		<script type="text/javascript">
  			$(document).ready(function(){
  				show_error_message('<c:out value="${PNOTIFY_ERROR_TITLE}" />', '<c:out value="${PNOTIFY_ERROR_MESSAGE}" />', 'success', 5000);
  			});
  		 </script>
  	</logic:present>

 	<div id="pledge_form_big_div"> 
		<aim:renderFormSubsection title="Pledge Identification">
			<jsp:include page="pledgeIdentificationView.jsp"></jsp:include>
		</aim:renderFormSubsection>
		<field:display name="Pledge Sector" feature="Pledge Sector and Location">
			<aim:renderFormSubsection title="Sector" styleId="pledge_form_sectors">
				<jsp:include page="pledgeSectorsView.jsp"></jsp:include>
			</aim:renderFormSubsection>
		</field:display>
		<field:display name="Pledge Location" feature="Pledge Sector and Location">
			<aim:renderFormSubsection title="Location" styleId="pledge_form_locations">
				<jsp:include page="pledgelocationslistView.jsp"></jsp:include>
			</aim:renderFormSubsection>
		</field:display>
		<field:display name="Pledge Program" feature="Pledge Sector and Location">
			<aim:renderFormSubsection title="Program" styleId="pledge_form_programs">
				<jsp:include page="pledgeProgramsView.jsp"></jsp:include>
			</aim:renderFormSubsection>
		</field:display>
		<feature:display name="Pledge Funding" ampModule="Pledges">
			<aim:renderFormSubsection title="Pledge Information" styleId="pledge_form_funding">
				<jsp:include page="pledgeFundingView.jsp"></jsp:include>
			</aim:renderFormSubsection>
		</feature:display>
		<jsp:include page="/repository/aim/view/pledgeform/pledgeContactsView.jsp"></jsp:include>
		<jsp:include page="/repository/aim/view/pledgeform/pledgeEpilogueView.jsp"></jsp:include>
	
	</div><br/>
	<div class="big-form-buttons">
		<button type="button" onclick="parent.window.location = '/viewPledgesList.do';" class="btn btn-primary"
				id='pledgeForm_cancel'><digi:trn>Return</digi:trn></button>
	</div>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="/repository/bootstrap/bootstrap.min.js"></script>
    <script src="/repository/bootstrap/bootstrap-select.min.js" type="text/javascript"></script>
    <script src="/repository/bootstrap/jquery.pnotify.min.js" type="text/javascript"></script>
    <script src="/repository/bootstrap/bootstrap-dialog.min.js" type="text/javascript"></script>
    <script src="/repository/bootstrap/bootstrap-utils.js" type="text/javascript"></script>
 <!--   <script src="/repository/bootstrap/lightbox/ekko-lightbox.min.js" type="text/javascript"></script>  --> 
