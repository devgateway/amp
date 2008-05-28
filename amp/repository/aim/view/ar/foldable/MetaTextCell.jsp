<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<bean:define id="metaTextCell" name="viewable"
	type="org.dgfoundation.amp.ar.cell.MetaTextCell" scope="request"
	toScope="page" />
<!-- 
<c:if test="${metaTextCell.draftFlag eq true}">
<font color="RED">
</c:if>
 -->
 <c:set var="starFlag" value="false" target="request" scope="request" />
 <font color="${metaTextCell.colour}">
 		<c:if test="${metaTextCell.colour == 'GREEN' && metaTextCell.statusFlag == 'started'}">
	 		<c:set var="starFlag" value="true" target="request" scope="request" />
 		</c:if>
 
 <%@ include file="TextCell.jsp"%>
