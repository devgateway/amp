<%@page import="org.digijava.module.aim.helper.FormatHelper,org.dgfoundation.amp.ar.AmpARFilter"%>
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

<c:choose>
<c:when test="${pledgeUser}">

	<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<jsp:include page="teamPagesHeader.jsp"  />

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
/*function addPledge() {
	document.viewPledgesForm.action="/addPledge.do?reset=true";
	document.viewPledgesForm.submit();
}

function editPledge(id){
	document.viewPledgesForm.action="/addPledge.do?pledgeId="+id;
	document.viewPledgesForm.submit();
}*/

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
	if (tableElement) {
		rows = tableElement.getElementsByTagName('tr');
		for(var i = 0, n = rows.length; i < n; ++i) {
			if(i%2 == 0)
				rows[i].className = classEven;
			else
				rows[i].className = classOdd;
		}
		rows = null;
	}
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

	<logic:present name="PNOTIFY_ERROR_MESSAGE" scope="request">
		<div class="pledge-error-message"><c:out value="${PNOTIFY_ERROR_MESSAGE}" /></div>
	</logic:present>

<digi:instance property="viewPledgesForm" />

<digi:form action="/viewPledgesList.do" method="post">
<c:set var="usedCurrency"><%=AmpARFilter.getDefaultCurrency().getCurrencyCode()%></c:set>
<div class="l_sm">
 	<font color="red">
 		<jsp:include page="utils/amountUnitsUnformatted.jsp">
			<jsp:param value="* " name="amount_prefix"/>
		</jsp:include>
	</font>
  	</div>
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="1000" vAlign="top" align="center" border="0">
	
	<tr>
		<td align=left valign="top" class=r-dotted-lg>
			<table width="100%" cellSpacing="0" cellPadding="0" vAlign="top" align="left">
				<tr><td>
					<table width="100%" cellSpacing="0" cellPadding="0" vAlign="top">
						<tr>
							<td>
								<span class=crumb>
									<digi:link href="/viewMyDesktop.do" styleClass="comment">

										<digi:trn key="aim:desktop">Desktop</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
									<digi:trn key="aim:pledges">Pledges</digi:trn>
								
								</span>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr>
					<td>
					</td>
				</tr>
				<tr><td>
					</td>
				</tr>
				<logic:notEmpty name="viewPledgesForm" property="allFundingPledges">
				<tr>
					<td>
					<div class="report content-dir">
					<table width="1000px" cellspacing="0" cellpadding="0" id="dataTable" class="inside dataTable"
						   style="margin-top:15px;" border="3">
					<tr style="background-color: #C7D4DB; color: #000000; fnt-size:12px;" align="center">
							<td width="300px" align="center" class="inside">
								<b> 
									<digi:trn>Pledge Name</digi:trn>
								</b>							</td>
							<td width="170px" align="center" class="inside">
								<b> 
									<digi:trn>Organization Group</digi:trn>
								</b>							</td>
							<td width="170px" align="center" class="inside">
								<b><digi:trn>Total Pledged Amount</digi:trn></b> (${usedCurrency})
							</td>
							<td width="150px" align="center" class="inside">
								<b> 
									<digi:trn>Time frame</digi:trn>
								</b>							</td>
						<td width="150" align="center" class="inside">
							<b>
								<digi:trn>Creation date</digi:trn>
							</b>
						</td>

						<td width="60px" align="center" class="inside">
								<b> 
									<digi:trn>Action</digi:trn>
								</b>							</td>
						</tr>
                       <tbody class="yui-dt-data">
						<c:forEach var="allFundingPledges" items="${viewPledgesForm.allFundingPledges}" varStatus="index">
							<c:set var="pledgeId" value="${allFundingPledges.id}" />
							<c:set var="pledgeUsed" value="${allFundingPledges.usedInActivityFunding}" />
							
							<tr style="height: 25px">
								<td width="300px" align="center" class="inside"><a href="/viewPledge.do?id=${pledgeId}">
									<c:out value="${allFundingPledges.effectiveName }" />
								</a></td>
								<td width="170px" align="center" class="inside">
									<bean:write name="allFundingPledges" property="organizationGroup.orgGrpName" />
								</td>
								<td width="170px" align="center" class="inside">
									<span dir="ltr"><aim:formatNumber value="${allFundingPledges.getTotalPledgedAmount(usedCurrency)}" /></span>
								</td>
								<td width="150px" align="left" class="inside">
									<c:forEach var="year" items="${allFundingPledges.yearsList}" varStatus="index">
										<li> <digi:trn>${year}</digi:trn>&nbsp;</li>
									</c:forEach>
								</td>
								<td width="150px" align="center" class="inside">
									<aim:formatDate value="${allFundingPledges.createdDate}"></aim:formatDate>
									</td>
								<td width="60px" align="center" class="inside">
									<a class="itr" href="/addPledge.do?pledgeId=${pledgeId}" title="<digi:trn key="aim:ClickToEditPledge">Click on this icon to edit pledge</digi:trn>">
	                                   	<img src= "../ampTemplate/images/application_edit.png" border="0"></a>
									&nbsp;
									<a class="itr" href="javascript:removePledge('${pledgeId}','${pledgeUsed}');" title="<digi:trn key="aim:ClickToDeletePledge">Click on this icon to delete pledge</digi:trn>&nbsp;">
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

<feature:display name="Add Pledge Button" module="Pledges">
	<div style="text-align:center;padding:10px;">
			<html:button styleClass="buttonx" property="submitButton" onclick="window.location.assign('/addPledge.do?reset=true')">
	    		<digi:trn key="btn:AddPlegde">Add Pledge</digi:trn>
			</html:button>
	</div>
</feature:display>
	
<script language="javascript">

	setStripsTable("dataTable", "tableEven", "tableOdd");
	setHoveredTable("dataTable", false);
	setHoveredRow("rowHighlight");
</script> 

</digi:form>

</c:when>

<c:otherwise>
    <digi:errors/>
</c:otherwise>

</c:choose>