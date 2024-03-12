<%@ page pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/c.tld" prefix="c"%>
<bean:define id="metaTextCell" name="viewable"
	type="org.dgfoundation.amp.ar.cell.MetaTextCell" scope="request"
	toScope="page" />

<c:set var="starFlag" value="false" target="request" scope="request" />
<div <c:if test="${not empty metaTextCell.colour}">style='color: ${metaTextCell.colour}'</c:if> >
 		<c:if test="${metaTextCell.colour == 'GREEN' && metaTextCell.statusFlag == 'started'}">
 			 <font color="GREEN">
	 		<c:set var="starFlag" value="true" target="request" scope="request" />
 		</c:if>
 		<c:if test="${metaTextCell.colour == 'RED' && metaTextCell.statusFlag == 'started'}">
	 		<font color="RED">
	 		<c:set var="starFlag" value="true" target="request" scope="request" />
 		</c:if>
		<c:if test="${metaTextCell.colour == 'RED' && metaTextCell.statusFlag == 'startedapproved'}">
	 		<c:set var="starFlag" value="true" target="request" scope="request" />
 		</c:if>
<%@ include file="TextCell.jsp"%>
</div>