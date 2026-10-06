<%@ page pageEncoding="UTF-8" %>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<bean:define id="dateCell" name="viewable" type="org.dgfoundation.amp.ar.cell.DateCell" scope="request" toScope="page"/>
<logic:equal name="dateCell" value="">...</logic:equal>
<bean:write name="dateCell"/>&nbsp;