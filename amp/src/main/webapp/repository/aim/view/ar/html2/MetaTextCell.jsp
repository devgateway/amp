<%@ page pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
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