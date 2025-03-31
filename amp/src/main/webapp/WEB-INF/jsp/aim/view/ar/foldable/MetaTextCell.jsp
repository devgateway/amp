<%@ page pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<bean:define id="metaTextCell" name="viewable"
	type="org.dgfoundation.amp.ar.cell.MetaTextCell" scope="request"
	toScope="page" />


<%
	boolean zStarFlag = false;
	String colour = metaTextCell.getColour();
	String statusFlag = metaTextCell.getStatusFlag();
	
	if (colour.equals("GREEN"))
	{
		if (statusFlag.equals("started"))
			zStarFlag = true;
	}
	
	if (colour.equals("RED"))
	{
		zStarFlag = statusFlag.equals("started") || statusFlag.equals("startedapproved");
	}
	request.setAttribute("starFlag", zStarFlag);
//<c:set var="starFlag" value="false" target="request" scope="request" />

//<font color="${metaTextCell.colour}">
//		<c:if test="${metaTextCell.colour == 'GREEN' && metaTextCell.statusFlag == 'started'}">
//	 		<c:set var="starFlag" value="true" target="request" scope="request" />
//		</c:if>
//		<c:if test="${metaTextCell.colour == 'RED' && metaTextCell.statusFlag == 'started'}">
//	 		<c:set var="starFlag" value="true" target="request" scope="request" />
//		</c:if>
//		<c:if test="${metaTextCell.colour == 'RED' && metaTextCell.statusFlag == 'startedapproved'}">
//	 		<c:set var="starFlag" value="true" target="request" scope="request" />
//		</c:if>

%> 		
<font color="${metaTextCell.colour}">
	<%@ include file="TextCell.jsp"%>
 </font>
 