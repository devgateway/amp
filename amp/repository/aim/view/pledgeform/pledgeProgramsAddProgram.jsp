<%-- renders a table of the list of locations for the currently-in-form-pledge --%>
<%-- the HTML is ready to be included in the page per se, so no css/javascript includes here! --%>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ page import="org.digijava.module.fundingpledges.form.PledgeForm"%>

<!-- <form class="form-group">  -->
<digi:instance property="pledgeForm" />

<div class="container-fluid" id="pledge_add_program_area">
<%--<h4><digi:trn key="aim:selectProgram">Select Program</digi:trn></h4> --%>
	<c:forEach var="prgLevels" varStatus="varSt" items="${pledgeForm.programLevels}">
		<div class="row">
			<div class="col-xs-5 text-right">
				<c:if test="${varSt.count==1}"><digi:trn key="aim:programScheme">Program scheme</digi:trn></c:if>
				<c:if test="${varSt.count!=1}"><digi:trn key="aim:subProgramLevel">Sub program level</digi:trn>&nbsp;${varSt.count-1}</c:if>
			</div>
			<div class="col-xs-6 col-xs-offset-1 text-left">			
			<html:select property="selPrograms" onchange="reloadProgram(this)">
					<option value="-1"><digi:trn key="aim:selectProgramOpt">Select Program</digi:trn></option>
					<html:optionsCollection name="prgLevels" value="ampThemeId" label="name" />
				</html:select>
			</div>
		</div>
	</c:forEach>
	<div class="col-xs-4 col-xs-offset-2 text-right"><button type="button" class="btn btn-success btn-sm" id='pledges_add_programs_submit_button' onclick='pledges_add_programs_submit()'><digi:trn>Submit</digi:trn></button></div>
	<div class="col-xs-4 col-xs-offset-0 text-left"><button type="button" class="btn btn-success btn-sm" id='pledges_add_programs_cancel_button' onclick='pledges_add_programs_cancel()'><digi:trn>Cancel</digi:trn></button></div>
</div>

<script type="text/javascript">
	on_element_loaded();
	$(document).ready(function(){$('#pledge_add_program_area select').attr('data-live-search', 'true');}); // Struts is stupid and does not allow to inject custom attributes
</script>


