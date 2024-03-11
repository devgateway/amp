<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c"%>
<bean:define id="metaTextCell" name="viewable"
	type="org.dgfoundation.amp.ar.cell.MetaTextCell" scope="request"
	toScope="page" />

<c:set var="starFlag" value="false" target="request" scope="request" />
 	 <font color="${metaTextCell.colour}">

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
</font>