 <%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://digijava.org" prefix="digi"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
				