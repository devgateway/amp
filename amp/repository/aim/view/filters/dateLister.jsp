
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>
<%@ page import="org.dgfoundation.amp.ar.AmpARFilter"%>

<style>
 .dateInputMarker{}
</style>
<bean:define id="entityList" toScope="page" scope="request" name="reqEntityList" />

<c:set var="radioGroupName" value="optGroupDateFilter_${element.rootHierarchyListable.uniqueId}"></c:set>

<logic:notEmpty name="entityList">
		<table style="margin-left: 45px;">
		<logic:iterate id="entity" name="entityList" scope="page" indexId="idx">
		<tr>	
						<c:if test="${entity.selected}">
							<c:set var="radioState" value="checked='checked'"/>	
							<c:set var="inputState" value=""/>					
						</c:if>
						<c:if test="${not entity.selected}">
							<c:set var="inputState" value="disabled='true'"/>
							<c:set var="radioState" value=""/>
						</c:if>
						<c:choose>
						<c:when test="${fn:length(entity.children) > 0}">
						<td>
						<input type="radio" name="${radioGroupName}" value="0" onclick="changeDateFilteringGroup(this);" <c:out value="${radioState}"/>></input>
						</td>
						<td>
						<br/>
						<div id="${radioGroupName}_0" style="border: 1px solid #CCCCCC;padding:10px;">
							<table>
						<%-- from - to date fields --%>
							<logic:iterate id="subentity" name="entity" property="children">
								<bean:define id="dateentity" name="subentity" /> 
								<%@include file="filterDate.jsp" %>
							</logic:iterate>
							</table>
						</div>
						</td>
						</c:when>
						
						<c:otherwise>
						<%-- dynamic filter --%>
							<td>
							<input type="radio" name="${radioGroupName}" value="1" onclick="changeDateFilteringGroup(this);" <c:out value="${radioState}"/>></input>
							</td>
							<td>
							<div id="${radioGroupName}_1" style="border: 1px solid #CCCCCC;padding:10px;">
							<digi:trn>From Current </digi:trn>
							<html:select property="${entity.actionFormProperty}.currentPeriod" styleId="filter_input_curr_period_${entity.uniqueId }">
								<html:option value="<%= AmpARFilter.DYNAMIC_FILTER_DAY %>"><digi:trn>day</digi:trn></html:option>
								<html:option value="<%= AmpARFilter.DYNAMIC_FILTER_MONTH %>"><digi:trn>month</digi:trn></html:option>
								<html:option value="<%= AmpARFilter.DYNAMIC_FILTER_YEAR %>"><digi:trn>year</digi:trn></html:option>
							</html:select>
							<html:select property="${entity.actionFormProperty}.operator" styleId="filter_input_op_${entity.uniqueId }">
								<html:option value="<%= AmpARFilter.DYNAMIC_FILTER_ADD_OP %>">+</html:option>
								<html:option value="<%= AmpARFilter.DYNAMIC_FILTER_SUBTRACT_OP %>">-</html:option>
							</html:select>
							<html:text property="${entity.actionFormProperty}.amount" maxlength="3" size="3" styleId="filter_input_amount_${entity.uniqueId }">
							</html:text>
							<input type="hidden" value="<digi:trn>Please enter a valid integer number for </digi:trn> <digi:trn>${element.rootHierarchyListable.label}</digi:trn>" id="filter_input_amount_${entity.uniqueId }_error"/>
							<html:select property="${entity.actionFormProperty}.xPeriod" styleId="filter_input_x_period_${entity.uniqueId }">
								<html:option value="<%= AmpARFilter.DYNAMIC_FILTER_DAY %>"><digi:trn>days</digi:trn></html:option>
								<html:option value="<%= AmpARFilter.DYNAMIC_FILTER_MONTH %>"><digi:trn>months</digi:trn></html:option>
								<html:option value="<%= AmpARFilter.DYNAMIC_FILTER_YEAR %>"><digi:trn>years</digi:trn></html:option>
							</html:select>
							</div>
							</td>
						</c:otherwise>
						</c:choose>
		</tr>
		</logic:iterate>
		</table>
</logic:notEmpty>