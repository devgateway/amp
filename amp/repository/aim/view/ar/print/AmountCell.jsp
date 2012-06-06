<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<bean:define id="amountCell" name="viewable" type="org.dgfoundation.amp.ar.cell.AmountCell" scope="request" toScope="page"/>
<bean:write name="amountCell"/>
