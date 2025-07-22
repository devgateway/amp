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

    <!-- Bootstrap-select http://silviomoreto.github.io/bootstrap-select/ -->
    
    <link href="/repository/bootstrap/bootstrap-select.min.css" rel="stylesheet" type="text/css">
    <link href="/repository/bootstrap/jquery.pnotify.default.css" media="all" rel="stylesheet" type="text/css" />
    <link href="/repository/bootstrap/jquery.pnotify.default.icons.css" media="all" rel="stylesheet" type="text/css" />
<!-- <link href="/repository/bootstrap/lightbox/ekko-lightbox.min.css" media="all" rel="stylesheet" type="text/css" />  -->        
    <link href="/repository/bootstrap/bootstrap-dialog.min.css" rel="stylesheet" type="text/css" />
    <link href="/repository/bootstrap/datetimepicker/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css" />
    <link href="/repository/bootstrap/upload/jquery.fileupload.css" rel="stylesheet" type="text/css" />
    <link href="/repository/bootstrap/amp-bootstrap.css" rel="stylesheet" type="text/css" /> <!-- this should always be included last -->
    
    <!-- THESE 4 FILES SHOULD ALWAYS BE THE FIRST INCLUDED JS, IN THIS ORDER -->
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="/repository/bootstrap/amp-bootstrap.js" type="text/javascript"></script>       
    <script src="/repository/aim/view/pledgeform/pfscripts.js" type="text/javascript"></script>
    <script src="/repository/aim/view/bootstrap/amp_validation.js" type="text/javascript"></script>
	<script src="/repository/aim/view/bootstrap/forms.js" type="text/javascript"></script>
	<script type="text/javascript">
		var please_enter_phone_number_message = '<digi:trn jsFriendly="true">Please enter phone</digi:trn>';
		var please_enter_email_message = '<digi:trn jsFriendly="true">Please enter email</digi:trn>';
		var please_enter_something_message = '<digi:trn jsFriendly="true">Please enter something</digi:trn>';
		var please_enter_number_message = '<digi:trn jsFriendly="true">Please enter number</digi:trn>';
		var please_enter_year_message = '<digi:trn jsFriendly="true">Please enter an year</digi:trn>';
		var please_enter_date_message = '<digi:trn jsFriendly="true">Please enter a date</digi:trn>';
		var sum_percentages_message = '<digi:trn jsFriendly="true">Sum of percentages should be either 0 or 100</digi:trn>';
		var not_valid_percentage_message = '<digi:trn jsFriendly="true">Not a valid percentage</digi:trn>';
		var start_year_end_year_message = '<digi:trn jsFriendly="true">Start Year should be before End Year</digi:trn>';
		var start_date_end_date_message = '<digi:trn jsFriendly="true">Start Date should be before End Date</digi:trn>';
	</script>
  	<logic:present name="PNOTIFY_ERROR_MESSAGE" scope="request">
  		<script type="text/javascript">
  			$(document).ready(function(){
  				show_error_message("Not allowed to edit pledge", '<c:out value="${PNOTIFY_ERROR_MESSAGE}" />');
  			});
  		 </script>
  	</logic:present>
				
<logic:notPresent name="PNOTIFY_ERROR_MESSAGE" scope="request">
 	<div id="pledge_form_big_div" class="content-dir"> 
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
	
		<div class="big-form-buttons">
			<button type="button" onclick="pledge_form_submit('#pledge_form_big_div');" class="btn btn-primary" id='pledgeForm_submit'><digi:trn>Submit</digi:trn></button>
			<button type="button" onclick="pledge_form_validate('#pledge_form_big_div');" class="btn btn-info" id='pledgeForm_validate'><digi:trn>Validate</digi:trn></button>
			<button type="button" onclick="pledge_form_cancel('#pledge_form_big_div');" class="btn btn-warning" id='pledgeForm_cancel'><digi:trn>Cancel</digi:trn></button>		
		</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function(){
  			register_heart_beat();
  		});
	</script>
</logic:notPresent>	
	<!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="/repository/bootstrap/bootstrap-select.min.js" type="text/javascript"></script>
    <script src="/repository/bootstrap/jquery.pnotify.min.js" type="text/javascript"></script>
    <script src="/repository/bootstrap/bootstrap-dialog.min.js" type="text/javascript"></script>
    <script src="/repository/bootstrap/jquery.ui.widget.js" type="text/javascript"></script>
	<script src="/repository/bootstrap/upload/jquery.iframe-transport.js" type="text/javascript"></script>
    <script src="/repository/bootstrap/upload/jquery.fileupload.js" type="text/javascript"></script>
    
    <script src="/repository/bootstrap/datetimepicker/moment-with-langs.min.js" type="text/javascript"></script>   
    <script src="/repository/bootstrap/datetimepicker/bootstrap-datetimepicker.min.js" type="text/javascript"></script>
    <script src="/repository/bootstrap/bootstrap-utils.js" type="text/javascript"></script>
 <!--   <script src="/repository/bootstrap/lightbox/ekko-lightbox.min.js" type="text/javascript"></script>  --> 

