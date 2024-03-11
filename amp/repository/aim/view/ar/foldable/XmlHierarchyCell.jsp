<%@ page pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>

<bean:define id="xmlCell" name="viewable" type="org.dgfoundation.amp.ar.cell.XmlHierarchyCell" scope="request" toScope="page" />

<div align="left" style="padding-left:<%=request.getAttribute("pading")%>" >
<c:if test="${xmlCell.rootItems != null && not empty xmlCell.rootItems }">
	<bean:define id="xmlItemList" name="xmlCell" property="rootItems" toScope="request" />
	<jsp:include page="../HierarchycalItemViewer.jsp"/>
		

</c:if>
</div>
