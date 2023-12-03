<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ page import="org.digijava.ampModule.fundingpledges.form.PledgeForm"%>
<%--
	renders a single input field guarded by Feature. Parameters:
	-> ct_nr: contact_nr (1 or 2)
	-> field_name: the FM field name which guards the output. MIGHT BE EMPTY -> no <field> will be generated in this case
	-> field: the subproperty name to be displayed (subproperty of property contactX)
	-> text: the text to display as <label> - IT WILL BE TRANSLATED
	-> classes: classes to add to the <input> element
 --%>
<c:set var="ct_nr">${param.ct_nr}</c:set>
<c:set var="feature_name">Pledge Contact ${ct_nr}</c:set>
<c:set var="contact_var">contact${ct_nr}</c:set>
<c:set var="text"><digi:trn>${param.text}</digi:trn></c:set>

<digi:instance property="pledgeForm" />

<c:set var="supposed_output">
	<div class="col-xs-6 auto-placeholder">
		<label for="${contact_var}.${param.field}_id">${text}</label>
		<html:text property="${contact_var}.${param.field}" styleId="${contact_var}.${param.field}_id" styleClass="form-control input-sm ${param.classes}" />
	</div>
</c:set>
<c:choose>
	<c:when test="${empty param.field_name }">${supposed_output}</c:when>
	<c:otherwise>
		<field:display name="${feature_name} - ${param.field_name}" feature="${feature_name}">
			${supposed_output}
		</field:display>
	</c:otherwise>
</c:choose>


	