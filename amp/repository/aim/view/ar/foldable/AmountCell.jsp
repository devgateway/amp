<%@ page pageEncoding="UTF-8" %>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@page trimDirectiveWhitespaces="true"%>
<bean:define id="amountCell" name="viewable" type="org.dgfoundation.amp.ar.cell.AmountCell" scope="request" toScope="page"/>

<div class="desktop_project_count_sel">
	<bean:write name="amountCell"/>
</div>