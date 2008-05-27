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
 <font color="${metaTextCell.colour}">
<%@ include file="TextCell.jsp"%>
