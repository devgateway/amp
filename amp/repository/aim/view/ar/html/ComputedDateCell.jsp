<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<bean:define id="cell" name="viewable" type="org.dgfoundation.amp.ar.cell.ComputedDateCell" scope="request" toScope="page"/>
<div style="width: 100%;text-align: center;">
  <bean:write name="cell"/>
</div>