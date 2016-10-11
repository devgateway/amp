<%@ page pageEncoding="UTF-8" %>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<bean:define id="dateCell" name="viewable" type="org.dgfoundation.amp.ar.cell.DateCell" scope="request" toScope="page"/>
<bean:write name="dateCell"/>&nbsp;