<%@ page pageEncoding="UTF-8" %>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-html.tld" prefix="html" %>
<bean:define id="dateCell" name="viewable" type="org.dgfoundation.amp.ar.cell.DateCell" scope="request" toScope="page"/>
<bean:write name="dateCell"/>&nbsp;