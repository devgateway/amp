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
<digi:form action="/pledgeLocationSelected.do">

<div class="container-fluid" id="pledge_add_location_area">
	<div class="row">
		<div class="col-xs-5 text-right"><h5>Implementation level</h5></div>
		<div class="col-xs-7">	
			<c:set var="translation"><digi:trn key="aim:addActivityImplLevelFirstLine">Please select from below</digi:trn></c:set>
			<category:showoptions multiselect="false" styleId="impl_level_select" firstLine="${translation}" name="pledgeForm" property="levelId" keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.IMPLEMENTATION_LEVEL_KEY %>" />
  		</div>
	</div>
	<div class="row">
		<div class="col-xs-5 text-right"><h5>Implementation location</h5></div> <%--getAllValidImplementationLocationChoices --%>
		<div class="col-xs-7">
			<html:select styleId="impl_location_select" property="implemLocationLevel">
				<c:forEach var="ch" items="${pledgeForm.allValidImplementationLocationChoices}">
					<html:option value="${ch.key}"><c:out value="${ch.value}" /></html:option> <%-- c:out does automatic escaping, unlike ${} --%>
				</c:forEach>
			</html:select>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-5 text-right"><h5>Region</h5></div>
		<div class="col-xs-7">
			<select id="location_id_select" multiple="multiple" size="5">
				<c:forEach var="ch" items="${pledgeForm.allValidLocations}">
					<option value="${ch.keyValue.key}"
						<c:if test="${not ch.enabled}">disabled="disabled"</c:if>
					>
						<c:out value="${ch.keyValue.value}" />
					</option> <%-- c:out does automatic escaping, unlike ${} --%>
				</c:forEach>
	</select></div>
</div>
<div class="text-center"><button type="button" class="btn btn-success btn-sm" onclick='pledges_hide_add_location()'>Submit</button></div>
</div>
</digi:form>
<script type="text/javascript">
	on_element_loaded();
</script>

<!-- </form>  -->
<%--</div>  --%>
