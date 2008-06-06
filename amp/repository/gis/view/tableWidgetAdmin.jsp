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
			<span class=crumb>
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
			<span class="subtitle-blue">Table Widget Manager</span>
		</td>
	</tr>
	<tr>
		<td>
			<digi:link  href="/adminTableWidgets.do?actType=create">Add new table widget</digi:link>
		</td>
	</tr>
	<tr>
		<td>
		
			<table border="0" width="100%" align="center" style="font-family:verdana;font-size:11px;">
				<tr bgColor="#d7eafd">
					<td width="60%">
						<strong>Name</strong>
					</td>
					<td width="30%">
						<strong>Code</strong>
					</td>
					<td width="30%">
						<strong>Data</strong>
					</td>
					<td>
						<strong>Operations</strong>
					</td>
				</tr>
				<c:forEach var="widgetTable" items="${gisTableWidgetCreationForm.tables}" varStatus="stat">
					<tr>
						<td>
						 	${widgetTable.name}
						</td>
						<td nowrap="nowrap">
						 	${widgetTable.code}
						</td>
						<td>
							<html:link href="/gis/tableWidgetData.do~actType=startEdit~widgetId=${widgetTable.id}">Edit data</html:link>
						</td>
						<td nowrap="nowrap">
						 	<digi:link href="/adminTableWidgets.do~actType=startEdit~id=${widgetTable.id}">Edit</digi:link>
						 	&nbsp;&nbsp;
						 	<digi:link onclick="return askToDelete()" href="/adminTableWidgets.do~actType=delete~id=${widgetTable.id}">Delete</digi:link>
						</td>
					</tr>
				</c:forEach>
			</table>
		
		
		</td>
	</tr>
</table>

</digi:form>