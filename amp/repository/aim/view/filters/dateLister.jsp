
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
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>


<style>
 .dateInputMarker{}
</style>

<bean:define id="entityList" toScope="page" scope="request" name="reqEntityList" />


<logic:notEmpty name="entityList">
		<table style="margin: 45px;">
		<logic:iterate id="entity" name="entityList" scope="page">
					<tr>
						<td>
							<span style="font-family: Arial; font-size: 12px;">
								<c:if test="${entity.translateable}">
									<digi:trn><c:out value="${entity.label}"/></digi:trn> 
								</c:if>
								<c:if test="${!entity.translateable}">
									<c:out value="${entity.label}"/> 
								</c:if>
							</span>
							<div style="display:none">
								<c:out value="${entity.additionalSearchString}"/>
							</div>
						</td>
						<td >
							<html:text readonly="true" property="${entity.actionFormProperty}" styleId="filter_input_${entity.uniqueId }" styleClass="dateInputMarker" />
							<input type="hidden" value="<digi:trn>${element.rootHierarchyListable.label}</digi:trn>"/>							
							<a id="filter_a_${entity.uniqueId }" style="background-color: #F6FAFF;" href='javascript:pickDateById("filter_a_${entity.uniqueId }","filter_input_${entity.uniqueId }")'>
								<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0" />
							</a>
							<a id="clearDate_${entity.uniqueId }" style="background-color: #F6FAFF;" href='javascript:clearDate("filter_input_${entity.uniqueId }")' title="<digi:trn>Clear</digi:trn>">
								<img src="../ampTemplate/images/deleteIcon.gif" border="0">
							</a>
							
						</td>
					</tr>
		</logic:iterate>
		</table>
</logic:notEmpty>