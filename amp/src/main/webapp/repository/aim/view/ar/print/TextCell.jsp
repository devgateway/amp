<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<bean:define id="textCell" name="viewable"	type="org.dgfoundation.amp.ar.cell.TextCell" scope="request"	toScope="page" />
<bean:write name="textCell" filter="false"/>


