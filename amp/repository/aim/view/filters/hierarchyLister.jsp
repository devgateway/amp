
<%@ page pageEncoding="UTF-8"%>
<%@page import="org.digijava.module.aim.util.HierarchyListable"%>
<%@page import="java.lang.*" %>
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

<bean:define id="entityList" toScope="page" scope="request" name="reqEntityList" />
<bean:define id="selectedEntityIds" toScope="page" scope="request" name="reqSelectedEntityIds" />

<logic:notEmpty name="reqBeanSetterObject" property="${selectedEntityIds}">
	<bean:define id="beanSetterArray" toScope="page" scope="request" name="reqBeanSetterObject" property="${selectedEntityIds}" />
</logic:notEmpty>


<logic:notEmpty name="entityList">
	<ul style="list-style-type: none">
		<logic:iterate id="entity" name="entityList" scope="page">
			<c:set var="checked" value="" scope="page" />
			<c:set var="parentId" value="" scope="page" />
			<logic:notEmpty name="reqBeanSetterObject" property="${selectedEntityIds}">
				<c:forEach var="elInArray"  items="${beanSetterArray}">
					<c:if test="${elInArray==entity.uniqueId}">
						<c:set var="checked" scope="page" >checked='checked'</c:set>
					</c:if>
				</c:forEach>
			</logic:notEmpty>
			<%
			Long uniqueId;
			if (pageContext.getAttribute("entity")!=null) {
				uniqueId = new Long (((HierarchyListable)pageContext.getAttribute("entity")).getUniqueId());
				pageContext.setAttribute("uniqueId", uniqueId);
				}
			%>
			<logic:present name="element" property="rootHierarchyListable.parentMapping">
				<c:set var="parentId" scope="page" >parentId='${element.rootHierarchyListable.parentMapping[uniqueId]}'</c:set>
			</logic:present>
			<li class="hierarchy-list-margin">
				<table>
					<tr>
						<td width="10px" valign="top"><input onclick="toggleCheckChildren(this);if(typeof buildLabels ==
						'function') buildLabels();" type="checkbox" value="${entity.uniqueId}" name="${selectedEntityIds}"  ${parentId}  ${checked}/></td>
						<td>
							<span class="filter-item-label">
								<c:if test="${entity.translateable}">
									<digi:trn><c:out value="${entity.label}"/></digi:trn> 
								</c:if>
								<c:if test="${!entity.translateable}">
									<c:out value="${entity.label}"/> 
								</c:if>
							</span>
							<div style="display:none" class="additionalSearchStringMarker">
								<c:out value="${entity.additionalSearchString}"/>
							</div>
						</td>
					</tr>
				</table> 
				<logic:notEmpty name="entity" property="children">
					<bean:define id="reqEntityList" toScope="request" name="entity" property="children" ></bean:define>
					<jsp:include page="hierarchyLister.jsp" />
				</logic:notEmpty>
			</li>
		</logic:iterate>
	</ul>
</logic:notEmpty>