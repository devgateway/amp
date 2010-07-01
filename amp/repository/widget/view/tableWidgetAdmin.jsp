<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<jsp:include page="/repository/aim/view/teamPagesHeader.jsp" flush="true" />

<script type="text/javascript">
<!--
	function askToDelete(){
		return window.confirm("Do you want to remove table? \nThis will remove its columns and data too!");
	}
//-->
</script>

<digi:instance property="gisTableWidgetCreationForm" />
<digi:form action="/adminTableWidgets?actType=list">

<table width="60%" border="0" cellpadding="15">
	<tr>
		<td>
			<span class="crumb">
              <c:set var="translation">
                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
              </c:set>
              <html:link  href="/aim/admin.do" styleClass="comment" title="${translation}" >
                <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
              </html:link>&nbsp;&gt;&nbsp;
                <digi:trn key="admin:Navigation:WidgetList">Table Widgets</digi:trn>
			</span>
		</td>
	</tr>
	<tr>
		<td>
			<span class="subtitle-blue"><digi:trn key="gis:tableWidgetList:pageTitle">Table Widget Manager</digi:trn></span>
		</td>
	</tr>
	<tr>
		<td>
			<digi:link  href="/adminTableWidgets.do?actType=create"><digi:trn key="gis:tableWidgetList:addNeLink">Add new table widget</digi:trn></digi:link>
		</td>
	</tr>
	<tr>
		<td>
		
			<table border="0" width="100%" align="center" style="font-family:verdana;font-size:11px;">
				<tr bgColor="#d7eafd">
					<td width="70%">
						<strong><digi:trn key="gis:tableWidgetList:nameCol">Name</digi:trn></strong>
					</td>
					<td width="30%">
						<strong><digi:trn key="gis:tableWidgetList:codeCol">Code</digi:trn></strong>
					</td>
					<td>
						<strong><digi:trn key="gis:tableWidgetList:opersCol">Operations</digi:trn></strong>
					</td>
				</tr>
				<c:forEach var="widgetTable" items="${gisTableWidgetCreationForm.tables}" varStatus="stat">
					<tr>
						<td>
							<html:link href="/widget/tableWidgetData.do~actType=startEdit~widgetId=${widgetTable.id}">${widgetTable.name}</html:link>
						</td>
						<td nowrap="nowrap">
						 	${widgetTable.code}
						</td>
						<td nowrap="nowrap">
							<c:set var="titleEdit"><digi:trn key="gis:titleEdit">Edit</digi:trn></c:set>
						 	<digi:link href="/adminTableWidgets.do~actType=startEdit~id=${widgetTable.id}" title="titleEdit">
						 		<img border="0" src="/repository/message/view/images/edit.gif" title="${titleEdit}">
						 	</digi:link>
						 	&nbsp;&nbsp;
							<c:set var="titleDelete"><digi:trn key="gis:titleDelete">Delete</digi:trn></c:set>
						 	<digi:link onclick="return askToDelete()" href="/adminTableWidgets.do~actType=delete~id=${widgetTable.id}">
						 		<img border="0" src='<digi:file src="images/deleteIcon.gif"/>' title="${titleDelete}">
						 	</digi:link>
						</td>
					</tr>
				</c:forEach>
			</table>
		
		
		</td>
	</tr>
</table>

</digi:form>