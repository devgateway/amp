<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<bean:define id="errorCell" name="viewable" type="org.dgfoundation.amp.ar.cell.TextCell" scope="request" toScope="page"/>
<font color="red"><b>
<bean:write name="errorCell"/>
</b>
</font>