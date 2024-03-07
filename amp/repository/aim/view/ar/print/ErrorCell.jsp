<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html" %>
<bean:define id="errorCell" name="viewable" type="org.dgfoundation.amp.ar.cell.TextCell" scope="request" toScope="page"/>
<font color="red"><b>
<bean:write name="errorCell"/>
</b>
</font>