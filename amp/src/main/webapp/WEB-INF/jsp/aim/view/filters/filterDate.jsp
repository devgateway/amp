
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<tr>
	<td><span style="font-family: Arial; font-size: 12px;"> <c:if
				test="${dateentity.translateable}">
				<digi:trn>
					<c:out value="${dateentity.label}" />
				</digi:trn>
			</c:if> <c:if test="${!dateentity.translateable}">
				<c:out value="${dateentity.label}" />
			</c:if>
	</span>
		<div style="display: none">
			<c:out value="${dateentity.additionalSearchString}" />
		</div></td>

	<td><html:text readonly="true" name="reqBeanSetterObject"
			property="${dateentity.actionFormProperty}"
			styleId="filter_input_${dateentity.uniqueId }"
			styleClass="dateInputMarker" />
		<input type="hidden" value="<digi:trn>${element.rootHierarchyListable.label}</digi:trn>" />		
		 <a id="filter_a_${dateentity.uniqueId }"
		style="background-color: #F6FAFF;"
		href='javascript:pickDateById("filter_a_${dateentity.uniqueId }","filter_input_${dateentity.uniqueId }")'>
			<img src="../ampTemplate/images/show-calendar.gif"
			alt="Click to View Calendar" border="0" />
	</a> <a id="clearDate_${dateentity.uniqueId }"
		style="background-color: #F6FAFF;"
		href='javascript:clearDate("filter_input_${dateentity.uniqueId }")'
		title="<digi:trn>Clear</digi:trn>"> <img
			src="../ampTemplate/images/deleteIcon.gif" border="0">
	</a></td>

</tr>
