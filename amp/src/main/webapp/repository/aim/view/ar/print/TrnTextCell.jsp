<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html"%>
<bean:define id="trnTextCell" name="viewable"	type="org.dgfoundation.amp.ar.cell.TrnTextCell" scope="request"	toScope="page" />
<bean:write name="trnTextCell" filter="false"/>


