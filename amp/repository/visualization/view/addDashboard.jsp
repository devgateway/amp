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
<%@ page import="org.digijava.module.visualization.form.DashboardForm, org.digijava.module.visualization.dbentity.AmpGraph, java.util.*" %>


<% int idx = 0; 
DashboardForm dashboardForm = (DashboardForm) session.getAttribute("dashboardform");
%>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<style type="text/css">

</style>

<script language="JavaScript" type="text/javascript">

function addGraph() {
	var ni = document.getElementById('graphsDiv');
	var divname = "graphDiv_" + numGraphs;
	var newdiv = document.createElement('div');
	newdiv.setAttribute("id",divname);
	var s = "<table width='50%' bgcolor='#FFFFFF' cellPadding=5 cellspacing='1'> <tr> <td align='center' valign='bottom' width='10%' >";
	s += "<input name='graphInputId_"+ numGraphs +"' type='hidden' id='graphInputId_"+ numGraphs +"' value=''/> <input type='checkbox' id='graphCheck_"+ numGraphs +"'/></td>";

	s += "<td align='center' valign='bottom' width='10%'><b>" + (numGraphs+1) + "</b></td>";
	
	s += "<td align='center' valign='bottom' width='80%'> <select name='graphDrDw_"+ numGraphs +"' class='inp-text' style='width: 350px;'>";
	s += "<option selected='true' value='-1'>-<digi:trn>Select from below</digi:trn>-</option>";
	<% Collection col = dashboardForm.getGraphList();
	if (col!=null){
		Iterator itr = col.iterator();
		while (itr.hasNext()) {
			AmpGraph graph = (AmpGraph) itr.next();	
			if (graph != null){ %>
				s += "<option value='<%=graph.getId()%>'><digi:trn><%=graph.getName()%></digi:trn></option>";				  			
			<% }
		 }
	 }%>
	s += "</select> </td>";
	s += "</tr> </table>";
	
	newdiv.innerHTML = s;
	ni.appendChild(newdiv);
	numGraphs++;
	tempGraphs++;
	var remBut = document.getElementById('remBut');
	remBut.style.display="block";
	
}


function removeGraph()
{
	<c:set var="confirmDelete">
	  <digi:trn key="aim:removeSelectedGraphMessage">
	 	 Remove selected graphs?
	  </digi:trn>
	</c:set>
	if (confirm("${confirmDelete}")){
		var d = document.getElementById('graphsDiv');
		var i = 0;
		var flag = false;
		while (i<=numGraphs){
			if(document.getElementById("graphCheck_"+i)!=null && document.getElementById("graphCheck_"+i).checked==true){
				var olddiv = document.getElementById("graphDiv_"+i);
				d.removeChild(olddiv);
				tempGraphs--;
				flag = true;
			}
			i++;
		}
		if (!flag){
			<c:set var="selectFirst">
				<digi:trn>
					Please, select a graph first.
			  	</digi:trn>
			</c:set>	  	
			alert ("${selectFirst}");
		}
	}
	numGraphs = tempGraphs;
	if (tempGraphs==0){
		var remBut = document.getElementById('remBut');
		remBut.style.display="none";
	}
}

function saveDashboard() {
	<c:set var="duplicatedGraph">
	  <digi:trn>
	  	Is not possible to save a dashboard with a duplicated graph.
	  </digi:trn>
	</c:set>
	document.getElementById("showInMenu2").value = document.getElementById("show_in_menu").checked;
	document.getElementById("showOnlyDataFromThisWorkspace2").value = document.getElementById("show_only_data_from_this_workspace").checked;
	if (validateData()){
		var i = 0;
		var param = "";
		var graphArray=new Array();
		var duplicated = false;
		while (i<=numGraphs){
			if(document.getElementById('graphDiv_'+i)!=null){
				var idxStr = document.getElementsByName('graphDrDw_'+i)[0].value;
				if (graphArray.indexOf(idxStr)!=-1)
					duplicated = true;
				param += idxStr+"_";
				graphArray.push(idxStr);
			}
			i++;
		}

		if (duplicated){
			alert ("${duplicatedGraph}")
			return false;
		}
		var maxYearValue = document.getElementById('maxYearFilter').value;
		var minYearValue = document.getElementById('minYearFilter').value;
		if (checkYearFilters (minYearValue,maxYearValue) == false) {
			return false;
		}
		<digi:context name="save" property="/visualization/saveDashboard.do" />
  	 	document.dashboardform.action = "<%=save%>?graphs="+param;
  	  	document.dashboardform.target = "_self";
    	document.dashboardform.submit();
	}
}

function checkYearFilters (minYearValue, maxYearValue) {
	if (maxYearValue!="" || minYearValue!="") {
		if (maxYearValue == "" || minYearValue == "") {
			errorMsgMax='<digi:trn jsFriendly="true" >Both Default Max Year Filter and Default Min Year Filter values must be set to be used as default filter</digi:trn>';
			alert(errorMsgMax);
		    return false;
		}
		if ( /^\d+$/.test(maxYearValue) == false  && maxYearValue != "") {
			errorMsgMax='<digi:trn jsFriendly="true" >Default Max Year Filter value should be a number</digi:trn>';
			alert(errorMsgMax);
		    return false;
		}
		if (/^\d+$/.test(minYearValue) == false  && minYearValue != "") {
			errorMsgMin='<digi:trn jsFriendly="true" >Default Min Year Filter value should be a number</digi:trn>';
			alert(errorMsgMin);
			return false;	
		}
		if (parseInt (minYearValue) > parseInt(maxYearValue) ) {
			errorMsgLower='<digi:trn jsFriendly="true" >Default Min Year Filter value should be lower than Default Max Year filter value</digi:trn>';
			alert(errorMsgLower);
			return false;	
		}
	}
	return true;

}
function trim (str) {
    str = str.replace(/^\s+/, '');
    for (var i = str.length - 1; i >= 0; i--) {
        if (/\S/.test(str.charAt(i))) {
            str = str.substring(0, i + 1);
            break;
        }
    }
    return str;
}

function validateData(){
	
	<c:set var="addTitle">
	  <digi:trn>
	  	Please, add a name for dashboard.
	  </digi:trn>
	</c:set>
	if (document.getElementsByName("dashboardName")[0]==null || document.getElementsByName("dashboardName")[0].value.length == 0 || trim(document.getElementsByName("dashboardName")[0].value).length==0){
		alert ("${addTitle}")
		return false;
	}

	<c:set var="selectBase">
	  <digi:trn>
	  	Please, select a based type.
	  </digi:trn>
	</c:set>
	if (document.getElementById("baseType_dropdown").value == -1){
		alert ("${selectBase}")
		return false;
	}

	<c:set var="addGraph">
	  <digi:trn>
	  	Please, add at least one graph.
	  </digi:trn>
	</c:set>
	if (numGraphs==0){
		alert ("${addGraph}")
		return false;
	}
	
	<c:set var="selectGraph">
	  <digi:trn>
	  	Please, select a graph type.
	  </digi:trn>
	</c:set>
	i = 0;
	while (i<=numGraphs){
		if (document.getElementsByName("graphDrDw_"+i)[0]!=null){
			var temp = 0;
			temp = document.getElementsByName("graphDrDw_"+i)[0].value;
			if (temp==-1){
				alert ("${selectGraph}")
				return false;
			}
		}
		i++;
	}
	
	return true;
}
</script>

<digi:instance property="dashboardform" />

<digi:form action="/addDashboard.do" method="post">

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="1000" vAlign="top" align="center" border="0">
	
	<tr>
		<td align=left valign="top" class=r-dotted-lg>
			<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top" align="left">
				<tr>
					<td width="50%" align="left" valign="middle">
						<b><digi:trn>Dashboard Name</digi:trn></b>
					<html:text property="dashboardName" size="40" styleClass="inp-text"/></td>
				</tr>
				<tr>
					<td><b><digi:trn>Select a base type</digi:trn></b>
						<html:select property="baseType" styleId="baseType_dropdown" styleClass="inp-text" style="width:250px;">
						<html:option value="-1"><digi:trn>Select from below</digi:trn></html:option>
						<html:option value="0"><digi:trn>None</digi:trn></html:option>
						<html:option value="1"><digi:trn>Organization based</digi:trn></html:option>
						<html:option value="2"><digi:trn>Region based</digi:trn></html:option>
						<html:option value="3"><digi:trn>Sector based</digi:trn></html:option>
						<html:option value="4"><digi:trn>Deal based</digi:trn></html:option>
					</html:select></td>
				</tr>
				<tr>
					<td><b><digi:trn>Select Agency type for pivot</digi:trn></b>
						<html:select property="pivot" styleId="pivot_dropdown" styleClass="inp-text" style="width:250px;">
						<html:option value="-1"><digi:trn>Select from below</digi:trn></html:option>
						<html:option value="0"><digi:trn>Donor</digi:trn></html:option>
						<html:option value="1"><digi:trn>Executing</digi:trn></html:option>
						<html:option value="2"><digi:trn>Beneficiary</digi:trn></html:option>
					</html:select></td>
				</tr>
				<tr>
					<td><b><digi:trn>Show in AMP main menu</digi:trn></b>
						<input type="checkbox" id="show_in_menu"/>
						<html:hidden property="showInMenu" styleId="showInMenu2" />
					</td>
				</tr>
				<tr>
					<td><b><digi:trn>Show Only Data From This Workspace</digi:trn></b>
						<input type="checkbox" id="show_only_data_from_this_workspace"/>
						<html:hidden property="showOnlyDataFromThisWorkspace" styleId="showOnlyDataFromThisWorkspace2" />
					</td>
				</tr>
				<tr>
					<td><b><digi:trn>Default Max Year Filter</digi:trn></b>
						<html:text property="maxYearFilter" styleId="maxYearFilter"/>
						<span><digi:trn>Both values must be set (and bigger than 0) to be used as default filter.</digi:trn></span>
					</td>
				</tr>
				<tr>
					<td><b><digi:trn>Default Min Year Filter</digi:trn></b>
						<html:text property="minYearFilter" styleId="minYearFilter" />
					</td>
				</tr>
				<tr>
					<td>
						<b><digi:trn>Default Type of Funding Filter</digi:trn></b>
						<html:select property="transactionTypeFilter">
							<html:option value="0"><digi:trn>Commitments</digi:trn></html:option>
							<html:option value="1"><digi:trn>Disbursements</digi:trn></html:option>
							<feature:display module="Funding" name="Expenditures">
								<html:option value="2"><digi:trn>Expenditures</digi:trn></html:option>
							</feature:display>
							<module:display name="/Activity Form/Funding/Funding Group/Funding Item/MTEF Projections"
		                                    parentModule="/Activity Form/Funding/Funding Group/Funding Item">
								<html:option value="3"><digi:trn>MTEF Projections</digi:trn></html:option>
							</module:display>
						</html:select>
				 	</td>
				</tr>
				<tr>
					<td colspan="2">
						<table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellSpacing=1>
							<tr>
								<td>
	                               <div>
									 <div>
										<table width='50%' bgcolor="#f2f2f2" cellPadding=5 cellSpacing=1>
											<tr>
					                            <td align="center" valign="bottom" width="10%" >
						                        </td>
												<td align="center" valign="bottom" width="10%" >
													<b><digi:trn>Position</digi:trn></b>
						                        </td>
												<td align="center" valign="bottom" width="80%">
													<b><digi:trn>Type Of Graph</digi:trn></b>
												</td>
				                        	</tr>
										</table>
									</div>
								</div>
								</td>
							</tr>
							<tr>
								<td>
	                               	<div id="graphsDiv">
	                               	<c:forEach var="dashboardGraph" items="${dashboardform.dashGraphList}" varStatus="status">
									<%  
									String divName = "graphDiv_" + idx;
									String inputIdName = "graphInputId_" + idx; 
									String checkName = "graphCheck_" + idx; 
									String drDwName = "graphDrDw_" + idx; 
									idx++;
									%>
									 <div id="<%=divName%>" >
										<table width='50%' bgcolor='#FFFFFF' cellPadding=5 cellSpacing=1>
										<tr>
				                            <td align="center" valign="bottom" width="10%" >
												<input name="<%=inputIdName%>" type="hidden" id="<%=inputIdName%>" value='${dashboardGraph.id}'/>
					                        	<input type="checkbox" name="<%=checkName%>" id="<%=checkName%>" >																	
					                        </td>
											<td align="center" valign="bottom" width="10%" >
												<b><c:out value="<%=idx%>"></c:out></b>
					                        </td>
											<td align="center" valign="bottom" width="80%">
												<select name="<%=drDwName%>" class="inp-text" style="width: 350px;">
													<option selected="selected" value="-1">-<digi:trn>Select from below</digi:trn>-</option>
													<c:forEach var="graph" items="${dashboardform.graphList}">
														<c:if test="${dashboardGraph.graph.id == graph.id}">
															<option selected="selected" value="<c:out value="${graph.id}"/>">																				
														</c:if>
														<c:if test="${dashboardGraph.graph.id != graph.id}">
															<option value="<c:out value="${graph.id}"/>">																				
														</c:if>
														<digi:trn>${graph.name}</digi:trn>
														</option>
													</c:forEach>
												</select>		
											</td>
				                        </tr>
									</table>
								</div>
								</c:forEach>
								</div>
								</td>
							</tr>
							<tr>
								<td colspan="4"> &nbsp;
									<table>
										<tr>
											<td>
                                               	<html:button styleClass="dr-menu" property="submitButton" onclick="addGraph();">
                                                    <digi:trn>Add Graph</digi:trn>
                                                </html:button>
												&nbsp;
											</td>
											<td>
												<div id="remBut" style="display:block;">
													<html:button styleClass="dr-menu" property="submitButton" onclick="return removeGraph()">
                                                       <digi:trn>Remove Graph</digi:trn>
                                                   	</html:button>
												</div>
											</td>
										</tr>
									</table>
                                 </td>
                              </tr>
                              <tr>
								<td colspan="4"> &nbsp;
									<table>
										<tr>
											<td>
                                               	<html:button styleClass="dr-menu" property="submitButton" onclick="saveDashboard();">
                                                    <digi:trn>Save Dashboard</digi:trn>
                                                </html:button>
												&nbsp;
											</td>
										</tr>
									</table>
                                 </td>
                              </tr>
                         </table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<script language="JavaScript" type="text/javascript">

init();
var numGraphs = <%=idx%>;
var tempGraphs = numGraphs;

function init(){
	if (document.getElementById("showInMenu2").value=='true') {
		document.getElementById("show_in_menu").checked = true;
	} else {
		document.getElementById("show_in_menu").checked = false;
	}
	if (document.getElementById("showOnlyDataFromThisWorkspace2").value=='true') {
		document.getElementById("show_only_data_from_this_workspace").checked = true;
	} else {
		document.getElementById("show_only_data_from_this_workspace").checked = false;
	}	
	numGraphs = <%=idx%>;
	tempGraphs = <%=idx%>;
	if (tempGraphs==0){
		var remBut = document.getElementById('remBut');
		remBut.style.display="none";
	}
}
</script>
</digi:form>