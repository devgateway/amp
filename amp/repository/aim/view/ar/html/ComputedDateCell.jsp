<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<bean:define id="cell" name="viewable" type="org.dgfoundation.amp.ar.cell.ComputedDateCell" scope="request" toScope="page"/>
<div style="width: 100%;text-align: center;">
  <bean:write name="cell"/>
</div>