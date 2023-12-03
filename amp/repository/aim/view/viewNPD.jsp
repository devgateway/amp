<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@taglib uri="/taglib/struts-html" prefix="html"%>
<%@taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>
<%@ taglib uri="/taglib/globalsettings" prefix="gs"%>


<script language="JavaScript" type="text/javascript"
	src="<digi:file src="ampModule/aim/scripts/asynchronous.js"/>"></script>
<digi:ref
	href="/TEMPLATE/ampTemplate/js_2/yui/assets/skins/sam/treeview.css"
	type="text/css" rel="stylesheet" />
<style type="text/css">
.ygtvlabel {
	font-size: 12px;
}
.ygtvtp, .ygtvtph, .ygtvlp,.ygtvlph{
background:url(/TEMPLATE/ampTemplate/img_2/ico_plus.gif) 0 0 no-repeat;
}
.ygtvtm,.ygtvtmh,.ygtvlm,.ygtvlmh{
background:url(/TEMPLATE/ampTemplate/img_2/ico_minus.gif) 0 0 no-repeat; ;
}
.ygtvfocus, .ygtvfocus .ygtvlabel,.ygtvfocus .ygtvlabel:link,.ygtvfocus .ygtvlabel:visited,.ygtvfocus .ygtvlabel:hover{
background-color:#FFFFFF;
}
</style>


<script language="JavaScript" type="text/javascript"
	src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/treeview/treeview-min.js"/>"></script>
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="ampModule/aim/scripts/tree/jktreeview.js"/>"></script><jsp:include
	page="scripts/npdScripts/programTree.jsp" flush="true" />
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="ampModule/aim/scripts/npdScripts/npdGraph.js"/>"></script>
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="ampModule/aim/scripts/npdScripts/npdGrid.js"/>"></script>
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="ampModule/aim/scripts/npdScripts/changeOptions.js"/>"></script>
<jsp:include page="scripts/npdScripts/activityList.jsp" flush="true" />


<digi:instance property="aimNPDForm" />
<digi:form action="/exportIndicators2xsl.do">

	<c:set var="noProgSelected">
		<digi:trn key="aim:npd:noProgSelected">
			Please select a program before selecting a filter !
	</digi:trn>
	</c:set>
	<html:hidden property="defaultProgram" styleId="defaultProgram" />
	<script language="javascript" type="text/javascript">
	var ptree;
	var curProgId;
	var curProgNodeIndex;
	var curNodeId;
	var curProgramName;
	var curGraphURL = null;
	var lastTreeUpdate=0;
	var line=new Array();
	var lineIter=0;
	var openNodes=new Array();
	var treeXML=null;
	var activityXML=null;
	var p1d='?';
	var pd='&';
	var curIndicatorIDs=[];
	var curIndicatorNames=[];
	var selIndicatorIDs=[];
	var selYear =[];
	var selActStatus = null;
	var selActDonors = null;
	var selActYearTo = null;
	var selActYearFrom = null;
	var progIdHistory = [];
	var progNodeHistory = [];
    var pr;
    var lastTimeStamp;
	var strNoActivities="<digi:trn key='aim:NPD:noActivitisLabel'>No Activities</digi:trn>";
	var strTotal="<digi:trn key='aim:NPD:totalLabels'>Totals:</digi:trn>";
<gs:test name="<%=org.digijava.ampModule.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS%>" compareWith="2" onTrueEvalBody="true">
	var strThousands="<digi:trn key='aim:NPD:amountMillionsOfDollarsLabel'>All amounts are in millions (000 000) of</digi:trn> ${aimNPDForm.defCurrency}";
</gs:test>	
<gs:test name="<%=org.digijava.ampModule.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS%>" compareWith="1" onTrueEvalBody="true">
	var strThousands="<digi:trn key='aim:NPD:amountThousandsOfDollarsLabel'>All amounts are in thousands (000) of</digi:trn> ${aimNPDForm.defCurrency}";
</gs:test>
<gs:test name="<%=org.digijava.ampModule.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS%>" compareWith="0" onTrueEvalBody="true">
	var strThousands="${aimNPDForm.defCurrency}";
</gs:test>
	var strPlanned="<digi:trn key='aim:NPD:sumplanedCommitments'>Planned Commitments</digi:trn>";
	var strActual="<digi:trn key='aim:NPD:sumactualCommitments'>Actual Commitments</digi:trn>";
    var strActualDisb="<digi:trn>Actual Disbursements</digi:trn>";
	var strProposed="<digi:trn key='aim:NPD:sumproposedPrjCost'>Proposed Project Cost</digi:trn>";
	var actCurrPage=1;
	var actMaxPages=0;
	var pgNext='<digi:trn jsFriendly="true" key="aim:npd:pagination:next">Next</digi:trn>';
	var pgPrev='<digi:trn jsFriendly="true" key="aim:npd:pagination:prev">Prev</digi:trn>';
	var pgLast='<digi:trn jsFriendly="true" key="aim:npd:pagination:last">Last</digi:trn>';
	var pgFirst='<digi:trn jsFriendly="true" key="aim:npd:pagination:first">First</digi:trn>';
	var pgPagesLabel='<digi:trn jsFriendly="true" key="aim:npd:pagination:pageslabel">Pages:</digi:trn>';
	var status='<digi:trn jsFriendly="true" key="aim:npd:status">Status</digi:trn>';
	var title='<digi:trn jsFriendly="true" key="aim:npd:titl">Title</digi:trn>';
	var strDate='<digi:trn jsFriendly="true" key="aim:npd:strdate">Start Date</digi:trn>';
	var donor='<digi:trn jsFriendly="true" key="aim:npd:donor">Donor</digi:trn>';
	
	function clearChildren(node){
		while(node.firstChild!=null){	
			node.removeChild(node.firstChild);
		}	
	}
	
   	function addActionToURL(actionName){
   		var fullURL=document.URL;
   		var lastSlash=fullURL.lastIndexOf("/");
   		var partialURL=fullURL.substring(0,lastSlash);
   		if(partialURL.lastIndexOf("aim")!=(partialURL.length-3)){
   			partialURL+="/aim";
   		}
   		return partialURL+"/"+actionName;
   	}
   	
   	function setLoadingImage(parent){
   		clearChildren(parent);
   		var div=document.createElement('div');
   		var img=document.createElement('img');
   		img.src="/TEMPLATE/ampTemplate/images/amploading.gif";
   		img.alt="Loading...";
   		div.appendChild(img);
   		parent.appendChild(div);
   	}
   	
   	function resetFilter(){
   		$("#yearFrom").val("-1");
   		$("#yearTo").val("-1");
   		$("#selectedDonorsId").val("-1");
   		$("#selectedStatusesId").val("-1");
   		
   		applyFilter();
   	}
    
    window.onload=loadInitial;
    	
    </script>
	<script language="javascript" type="text/javascript">
    	<c:forEach var="theme" items="${aimNPDForm.allThemes}">
    	<c:set var="name">
    	${theme.name}
    	</c:set>
    	<c:set var="quote">'</c:set>
    	<c:set var="escapedQuote">\'</c:set>
    	<c:set var="escapedName">
    	${fn:replace(name,quote,escapedQuote)}
    	</c:set>
    		addProgramInformation(	'${theme.ampThemeId}',
    								 '${escapedName}',
    								'${theme.description}',
    								'${theme.leadAgency}',
    								'${theme.themeCode}',
    								'${theme.typeCategoryValue.value}',
    								'${theme.targetGroups}',
    								'${theme.background}',
    								'${theme.objectives}',
    								'${theme.outputs}',
    								'${theme.beneficiaries}',
    			 					'${theme.environmentConsiderations}'
    	                            );

    	</c:forEach>
</script>
	<!-- MAIN CONTENT PART START -->

	<table cellspacing="0" cellpadding="0" border="0" align="center" width="1000">
            <tr>
		<td valign="top" align="left">
		<div id="content" class="yui-skin-sam">
		<div id="demo" class="yui-navset">
		<ul class="yui-nav">
			<ampModule:display name="National Planning Dashboard"
				parentModule="NATIONAL PLAN DASHBOARD">
				<li class="selected"><a style="cursor: default">
				<div><digi:trn key="aim:nplDashboard">National Planning Dashboard</digi:trn>
				</div>
				</a></li>
			</ampModule:display>
			<feature:display name="Portfolio Dashboard" ampModule="M & E">
				<li><digi:link
					href="/viewPortfolioDashboard.do~actId=-1~indId=-1">
					<div><digi:trn key="aim:portfolioDashboard">Dashboard</digi:trn>
					</div>
				</digi:link></li>
			</feature:display>
		</ul>

		<div class="yui-content" style="border: 1px solid rgb(208, 208, 208);">
		<div id="tabs-1" class="ui-tabs-panel ui-widget-content ui-corner-bottom ui-tabs-hide">
		<fieldset style="border:none;"><legend><span class=legend_label><span
			id="graphHeader">&nbsp;</span></span></legend>
		<div>
		<div class="dashboard_tab_opt" style="visibility:hidden">
		<div class="tab_opt_cont" style="background-color:#F2F2F2;">
		<div class="show_hide_setting"><digi:link
			href="/reportWizard.do?tabs=false&reset=true" styleClass="l_sm">
			<digi:trn>Reports</digi:trn>
		</digi:link></div>
		<a class="l_sm" href="#"><img
			border="0" src="/TEMPLATE/ampTemplate/img_2/ico-excel.png"></a>&nbsp;<a
			class="l_sm" href="#" onclick="exportToExcel();return false;"><digi:trn>Export to Excel</digi:trn></a> &nbsp;|&nbsp; <a class="l_sm" href="#"><img border="0"
			src="/TEMPLATE/ampTemplate/img_2/ico-print.png"></a>&nbsp;<a
			class="l_sm" href="#" onclick="window.print(); return false;"><digi:trn>Print</digi:trn></a>
		&nbsp;|&nbsp; <a class="l_sm" id="showGridLink"
			href="javascript:showGridTable();"><digi:trn>View Table</digi:trn></a><a class="l_sm" id="hideGridLink"
			href="javascript:hideGridTable();" class="invisible-item"><digi:trn>Hide Table</digi:trn></a></div>
		</div>
		</div>

		<table width="100%">
			<tbody>
				<tr>
					<td width="50%" valign="top">
					<div class="dashboard_child_left_1">
					<div id="indicatorTable"></div>
					<div id="">
					<table>
						<tbody>
							<tr>
								<td>
								<div id="divGraphLoading"
									style="vertical-align: middle; display: none; width: ${aimNPDForm.graphWidth}px; height: ${aimNPDForm.graphHeight}px"
									align="center"><digi:img src="images/amploading.gif" /><digi:trn
									key="aim:NPD:loadingGraph">Loading...</digi:trn></div>
								<div id="divGraphImage" style="display: block"><digi:context
									name="showChart" property="/ampModule/moduleinstance/npdGraph.do" />
								<c:url var="fullShowChartUrl" scope="page" value="${showChart}">
									<c:param name="actionMethod" value="displayChart" />
									<c:param name="currentProgramId"
										value="${aimNPDForm.programId}" />
									<c:forEach var="selVal" items="${aimNPDForm.selIndicators}">
										<c:param name="selectedIndicators" value="${selVal}" />
									</c:forEach>
								</c:url> <img id="graphImage" onload="graphLoaded()" alt="chart"
									src="${fullShowChartUrl}" width="${aimNPDForm.graphWidth}"
									height="${aimNPDForm.graphHeight}" usemap="#npdChartMap"
									border="0" /></div>
								</td>
								<td>
								<div class="jtree"
									style="width: 350px; height: 350px; overflow: scroll;">
								<div id="tree" ></div>
								</div>
								</td>
							</tr>
						</tbody>
					</table>

					</div>
					</div>
					<div class="dashboard_bottom">
					<div class="dashboard_filters"><digi:trn>Filters</digi:trn>
					<table width="60%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td valign="top" align="right" style="padding-top: 5px"><digi:trn>Statuses</digi:trn>:</td>
							<td valign="top"><category:showoptions
								name="aimNPDForm" property="selectedStatuses"
								keyName="<%= org.digijava.ampModule.categorymanager.util.CategoryConstants.ACTIVITY_STATUS_KEY%>"
								multiselect="true" size="5" ordered="true"
								styleClass="inputx insidex" styleId="selectedStatusesId"/></td>
								
							<td valign="top" align="right" style="padding-top: 5px"><digi:trn>Donors</digi:trn>:</td>
							<td valign="top"><html:select multiple="true" size="5"
								property="selectedDonors" styleClass="inputx insidex" styleId="selectedDonorsId">
								<html:optionsCollection name="aimNPDForm" property="donors"
									value="value" label="label" />
							</html:select></td>
						</tr>
						<tr>
							<td valign="top" align="right" style="padding-top: 5px"><digi:trn>From Year</digi:trn>:</td>
							<td valign="top">
								<html:select property="yearFrom" styleClass="inputx insidex" styleId="yearFrom">
									<option value="-1"><digi:trn>Select</digi:trn></option>
									<html:optionsCollection name="aimNPDForm" property="years" value="value" label="label" />
								</html:select>
							</td>
							<td valign="top" align="right" style="padding-top: 5px"><digi:trn>To Year</digi:trn>:</td>
							<td valign="top">
								<html:select property="yearTo"	styleClass="inputx insidex" styleId="yearTo">
								<option value="-1"><digi:trn>Select</digi:trn></option>
								<html:optionsCollection name="aimNPDForm" property="years" value="value" label="label" />
								</html:select>
							</td>
						</tr>
						<tr>
						<td colspan="2" align="center">
						<input type="button" class="buttonx_sm btn_save" style="width: auto;overflow: visible;" value="<digi:trn>Apply Filter</digi:trn>" onclick="applyFilter()"/>
						<input type="button" class="buttonx_sm btn_save" style="width: auto;overflow: visible;" value="<digi:trn>Reset</digi:trn>" onclick="resetFilter()"/>
						</td>
						</tr>
					</table>
					
					<div class="dashboard_activities">
					<digi:trn>Activites for:</digi:trn>
					&nbsp;<span id="actListProgname">&nbsp;</span>
					<div id="activityResultsPlace"></div>
					<div id="paginationPlace"></div>
					</div>
					</div>
					<div class="dashboard_options"><digi:trn>Change Options</digi:trn>: <br />
					<span class="normal_options"><digi:trn>Indicators (Any and all)</digi:trn></span> <br />
					<div id="indicatorsResultsPlace"></div>
					<span class="normal_options"><digi:trn>Time Limit (5)</digi:trn></span>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td valign="top"><c:forEach var="year"
								items="${aimNPDForm.years}" varStatus="status">
								<html:multibox name="aimNPDForm" property="selYears" onclick="return checkYearsRules()">
									${year.value}
								</html:multibox>${year.label}
							<c:if test="${status.index!=0&&(status.index+1)%3==0}"><br/></c:if>
							</c:forEach></td>
						</tr>
					</table>
					<br />
					<input type="button" class="buttonx_sm btn_save" style="width: auto;overflow: visible;"
						value="<digi:trn>Apply Changes</digi:trn>" onclick="doChanges();"></div>
					</div>
					</td>
				</tr>
		</table>                          
		</fieldset>
		</div>
		<!-- MAIN CONTENT PART END --> <input type="hidden" id="hdYears"
			value="" /> <input type="hidden" id="hdIndicators" value="" /> <c:forEach
			var="sys" items="${aimNPDForm.selYears}">
			<html:hidden property="myYears" value="${sys}" />
		</c:forEach> <span id="graphMapPlace"> <map name="npdChartMap"
			id="npdChartMap">
		</map> </span></div>
		</div>
                </td>
                </tr>
        </table>
		</digi:form>