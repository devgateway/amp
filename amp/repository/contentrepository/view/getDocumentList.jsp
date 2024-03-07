 <%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/c.tld" prefix="c"%>
<%@ page import="java.util.List"%>


<digi:instance property="crDocumentManagerForm" />
<bean:define id="myForm" name="crDocumentManagerForm" toScope="page"
	type="org.digijava.module.contentrepository.form.DocumentManagerForm" />
	
	<logic:notEmpty name="crDocumentManagerForm" property="otherDocuments">
		<bean:define name="crDocumentManagerForm" property="otherDocuments" id="documentDataCollection" type="java.util.Collection" toScope="request" />
	<jsp:include  page="documentTable.jsp" />
		</div>
		<br />
	</logic:notEmpty>
	<logic:empty name="crDocumentManagerForm" property="otherDocuments">
		<table id="team_table" bgcolor="white" width="100%"></table>
	</logic:empty>
				