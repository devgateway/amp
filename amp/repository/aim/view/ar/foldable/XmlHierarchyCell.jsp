<%@ page pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/c.tld" prefix="c" %>

<bean:define id="xmlCell" name="viewable" type="org.dgfoundation.amp.ar.cell.XmlHierarchyCell" scope="request" toScope="page" />

<div align="left" style="padding-left:<%=request.getAttribute("pading")%>" >
<c:if test="${xmlCell.rootItems != null && not empty xmlCell.rootItems }">
	<bean:define id="xmlItemList" name="xmlCell" property="rootItems" toScope="request" />
	<jsp:include page="../HierarchycalItemViewer.jsp"/>
		

</c:if>
</div>
