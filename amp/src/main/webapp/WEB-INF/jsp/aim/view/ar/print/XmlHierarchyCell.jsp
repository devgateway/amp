<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<bean:define id="xmlCell" name="viewable" type="org.dgfoundation.amp.ar.cell.XmlHierarchyCell" scope="request" toScope="page" />


<div align="left" style="padding-left:<%=request.getAttribute("pading")%>" >
<c:if test="${xmlCell.rootItems != null && not empty xmlCell.rootItems }">
	<bean:define id="xmlItemList" name="xmlCell" property="rootItems" toScope="request" />
	<jsp:include page="../HierarchycalItemViewer.jsp"/>
		

</c:if>
</div>
