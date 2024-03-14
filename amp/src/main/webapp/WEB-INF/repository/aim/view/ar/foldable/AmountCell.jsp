<%@ page pageEncoding="UTF-8" %>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@page trimDirectiveWhitespaces="true"%>
<bean:define id="amountCell" name="viewable" type="org.dgfoundation.amp.ar.cell.AmountCell" scope="request" toScope="page"/>

<div class="desktop_project_count_sel">
	<bean:write name="amountCell"/>
</div>