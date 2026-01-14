<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<bean:define id="cell" name="viewable" type="org.dgfoundation.amp.ar.cell.ComputedDateCell" scope="request" toScope="page"/>
<div style="width: 100%;text-align: center;">
  <bean:write name="cell"/>
</div>