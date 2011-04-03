<%@page import="org.digijava.module.aim.helper.FormatHelper"%>
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
<jsp:include page="teamPagesHeader.jsp" flush="true" />

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
function addPledge() {
	document.viewPledgesForm.action="/addPledge.do?reset=true";
	document.viewPledgesForm.submit();
}

function editPledge(id){
	document.viewPledgesForm.action="/addPledge.do?pledgeId="+id;
	document.viewPledgesForm.submit();
}

function removePledge(id,used){
	var usedMsg = "<digi:trn>This pledge is used on a funding step of an activity. Do you want to continue?</digi:trn>";
	var delMsg = "<digi:trn>This action will remove the pledge. Are you sure?</digi:trn>";
	if (used=="true") {
		if (!confirm(usedMsg)) {
			return;
		}
	}
	if (!confirm(delMsg)) {
		return;
	} else {
		document.viewPledgesForm.action="/removePledge.do?pledgeId="+id;
		document.viewPledgesForm.submit();
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

<digi:instance property="viewPledgesForm" />

<digi:form action="/viewPledgesList.do" method="post">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="960" vAlign="top" align="center" border=0>
	
	<tr>
		<td class=r-dotted-lg width="10">&nbsp;</td>
		<td align=left vAlign=top class=r-dotted-lg>
			<table width="98%" cellSpacing="3" cellPadding="1" vAlign="top" align="left">
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td>
								<span class=crumb>
									<digi:link href="/viewMyDesktop.do" styleClass="comment" >

										<digi:trn key="aim:desktop">Desktop</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
									<digi:trn key="aim:pledges">Pledges</digi:trn>
								
								</span>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td height=50 vAlign="middle" width="100%"><span class=subtitle-blue>
								<digi:trn key="aim:pledgesList">Pledges List</digi:trn>
								
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top" border=0>
						<tr>
							<td width="75%" vAlign="middle" height="40">
								<feature:display name="Add Pledge Button" module="Pledges">
								<table cellPadding=0 cellSpacing=0 width="100%" border=0>
									<html:button styleClass="dr-menu" property="submitButton" onclick="return addPledge()">
	                                       <digi:trn key="btn:AddPlegde">Add Pledge</digi:trn>
									</html:button>
								</table>
								</feature:display>
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<logic:notEmpty name="viewPledgesForm" property="allFundingPledges">
				<tr>
                   	<td class="report">
					<table width="100%" height="40" cellpadding="0" cellspacing="0" >
                      <thead>
						<tr height="25" style="background-color: #999999; color: #000000;" align="center">
							<td width="25%" align="center">
								<b> 
									<digi:trn>Pledge Name</digi:trn>
								</b>
							</td>
							<td width="25%" align="center">
								<b> 
									<digi:trn>Organization</digi:trn>
								</b>
							</td>
							<td width="25%" align="center">
								<b> 
									<digi:trn>Total Amount</digi:trn>
								</b>
							</td>
							<td width="19%" align="center">
								<b> 
									<digi:trn>Years</digi:trn>
								</b>
							</td>
							<td width="6%" align="center">
								<b> 
									<digi:trn>Action</digi:trn>
								</b>
							</td>
						</tr>
						</thead>
					</table>
					</td>
				</tr>
				<tr>
					<td>
					<div style="overflow: auto; width: 100%; height: 400px; max-height: 420px;" class="report">
					<table width="100%" cellspacing="0" cellpadding="0" id="dataTable" >
                       <tbody class="yui-dt-data">
						<c:forEach var="allFundingPledges" items="${viewPledgesForm.allFundingPledges}" varStatus="index">
							<tr style="height: 25px">
								<td width="25%" align="center">
									<bean:write name="allFundingPledges" property="key.title" />
								</td>
								<td width="25%" align="center">
									<bean:write name="allFundingPledges" property="key.organization.name" />
								</td>
								<td width="25%" align="center">
										<aim:formatNumber value="${allFundingPledges.key.totalAmount}" />
									
								</td>
								<td width="19%" align="center">
									<c:forEach var="year" items="${allFundingPledges.key.yearsList}" varStatus="index">
										<li> <digi:trn>${year}</digi:trn>&nbsp;</li>
									</c:forEach>
								</td>
								<td width="3%" align="center">
									<c:set var="pledgeId">
										<bean:write name="allFundingPledges" property="key.id" />
									</c:set>
									<a class="itr" href="javascript:editPledge('${pledgeId}');" title="<digi:trn key="aim:ClickToEditPledge">Click on this icon to edit pledge&nbsp;</digi:trn>">
	                                   	<img src= "../ampTemplate/images/application_edit.png" border=0>
									</a>
								</td>
								<td width="3%" align="center">
									<c:set var="pledgeId">
										<bean:write name="allFundingPledges" property="key.id" />
									</c:set>
									<c:set var="pledgeUsed">
										<bean:write name="allFundingPledges" property="value" />
									</c:set>
									<a class="itr" href="javascript:removePledge('${pledgeId}','${pledgeUsed}');" title="<digi:trn key="aim:ClickToDeletePledge">Click on this icon to delete pledge&nbsp;</digi:trn>">
	                                   	<img src= "../ampTemplate/images/trash_12.gif" border=0>
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
				<logic:empty name="viewPledgesForm" property="allFundingPledges">
					<tr style="background-color: #999999; color: #000000;" align="center">
						<td width="100%" align="center" height="20" >
							<b> 
								<digi:trn>No pledges found.</digi:trn>
							</b>
						</td>
					</tr>
				</logic:empty>
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