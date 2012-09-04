<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<style type="text/css">
.jcol {
	padding-left: 10px;
}

.jlien {
	text-decoration: none;
}

.tableEven {
	background-color: #dbe5f1;
	font-size: 8pt;
	padding: 2px;
}

.tableOdd {
	background-color: #FFFFFF;
	font-size: 8pt;
	padding: 2px;
}

.Hovered {
	background-color: #a5bcf2;
}

.notHovered {
	background-color: #FFFFFF;
}
</style>

<script language="JavaScript" type="text/javascript">
function addDashboard() {
	document.dashboardform.action="/visualization/addDashboard.do?reset=true";
	document.dashboardform.submit();
}

function editDashboard(id){
	document.dashboardform.action="/visualization/addDashboard.do?dashboardId="+id;
	document.dashboardform.submit();
}

function removeDashboard(id){
	<c:set var="confirmDelete">
	  <digi:trn>
	 	 Are you sure to remove the dashboard?
	  </digi:trn>
	</c:set>
	if (confirm("${confirmDelete}")){
		document.dashboardform.action="/visualization/removeDashboard.do?dashboardId="+id;
		document.dashboardform.submit();
	}
}

function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	rows = tableElement.getElementsByTagName('tr');
	for(var i = 0, n = rows.length; i < n; ++i) {
		if(i%2 == 0)
			rows[i].className = classEven;
		else
			rows[i].className = classOdd;
	}
	rows = null;
}
function setHoveredTable(tableId, hasHeaders) {

	var tableElement = document.getElementById(tableId);
	if(tableElement){
    	var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        rows      = tableElement.getElementsByTagName('tr');

		for(var i = 0, n = rows.length; i < n; ++i) {
			rows[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			rows[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		rows = null;
	}
}


function setHoveredRow(rowId) {

	var rowElement = document.getElementById(rowId);
	if(rowElement){
    	var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        cells      = rowElement.getElementsByTagName('td');

		for(var i = 0, n = cells.length; i < n; ++i) {
			cells[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			cells[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		cells = null;
	}
}


</script>

<digi:instance property="dashboardform" />

<digi:form action="/viewDashboardsList.do" method="post">

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="1000" vAlign="top" align="center" border="0">
	
	<tr>
		<td align=left valign="top" class=r-dotted-lg>
			<table width="100%" cellSpacing="0" cellPadding="0" vAlign="top" align="left">
				<tr><td>
					
				</td></tr>
				<tr><td>
					</td>
				</tr>
				<logic:notEmpty name="dashboardform" property="dashboardList">

				<tr>
					<td>
					<div class="report">
					<table width="100%" cellspacing="0" cellpadding="0" id="dataTable" class="inside" style="margin-top:15px;">
					<tr style="background-color: #C7D4DB; color: #000000; fnt-size:12px;" align="center">
							<td width="90%" align="center" class="inside">
								<b> 
									<digi:trn>Dashboard Name</digi:trn>
								</b>							</td>
							<td align="center" class="inside">
								<b> 
									<digi:trn>Action</digi:trn>
								</b>							</td>
						</tr>
                       <tbody class="yui-dt-data">
						<c:forEach var="dashboard" items="${dashboardform.dashboardList}">
							<tr style="height: 25px">
								<td align="center" class="inside">
									<c:out value="${dashboard.name}"/></td>
								<td width="6%" align="center" class="inside">
									<a href="javascript:editDashboard('${dashboard.id}');" title="<digi:trn>Click on this icon to edit the dashboard&nbsp;</digi:trn>">
	                                   	<img src= "../ampTemplate/images/application_edit.png" border="0">									
	                                </a>
	                                &nbsp;&nbsp;								
									<a href="javascript:removeDashboard('${dashboard.id}');" title="<digi:trn>Click on this icon to delete the dashboard&nbsp;</digi:trn>">
	                                   	<img src= "../ampTemplate/images/trash_12.gif" border="0">									
	                                </a>								
	                            </td>
							</tr>
						</c:forEach>
                        </tbody>
					</table>
                    </div>
					</td>
				</tr>
				</logic:notEmpty>
				<logic:empty name="dashboardform" property="dashboardList">
					<tr style="background-color: #999999; color: #000000;" align="center">
						<td width="100%" align="center" height="20" >
							<b> 
								<digi:trn>No dashboards found.</digi:trn>
							</b>
						</td>
					</tr>
				</logic:empty>
			</table>
		</td>
	</tr>
</table>


					<table width="1000" cellSpacing="5" cellPadding="3" vAlign="top" border="0" align=center>
						<tr>
							<td width="75%" vAlign="middle" height="40" align=center>
								<table cellpadding="0" cellspacing="0" width="100%" border="0">
									<html:button styleClass="buttonx" property="submitButton" onclick="return addDashboard()">
	                                       <digi:trn>Add Dashboard</digi:trn>
									</html:button>
								</table>
							</td>
						</tr>
					</table>
	
<script language="javascript">

	setStripsTable("dataTable", "tableEven", "tableOdd");
	setHoveredTable("dataTable", false);
	setHoveredRow("rowHighlight");
</script> 

</digi:form>