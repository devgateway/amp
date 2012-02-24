<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.digijava.module.aim.form.ReportsForm"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<%@ page language="java" import="org.digijava.module.aim.helper.TeamMember" %>

<!-- this is for the nice tooltip widgets -->
<DIV id="TipLayer" style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>

<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/arFunctions.js"/>"></script>

<!-- CSS -->
<link href="/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/tabview-core.css" type="text/css" rel="stylesheet">
<link href='TEMPLATE/ampTemplate/css_2/amp.css' rel='stylesheet' type='text/css'>
<link href='TEMPLATE/ampTemplate/css_2/tabs.css' rel='stylesheet' type='text/css'>
<link href="/TEMPLATE/ampTemplate/js_2/yui/container/assets/container.css" type="text/css" rel="stylesheet">
<link href="/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/skins/sam/tabview.css" type="text/css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="/TEMPLATE/ampTemplate/css_2/yui_tabs.css">

<digi:instance property="aimTeamReportsForm" />

<c:if test="${aimTeamReportsForm.showTabs}">
	<c:set var="translation">
		<digi:trn key="aim:confirmDeleteTab">Are you sure you want to delete the selected desktop tab?</digi:trn>
	</c:set>
	<c:set var="pageTitle">
		<digi:trn key="aim:teamTabs">
			Team Tabs
		</digi:trn>
	</c:set>
	<c:set var="breadCrumb">
		<digi:trn key="aim:AllTabs">All Tabs</digi:trn>
	</c:set>
	<c:set var="titleColumn">
		<digi:trn key="aim:tabTitle">Tab Title</digi:trn>
	</c:set>
	<c:set var="generator">
		<digi:trn key="aim:tabGenerator">Tab Generator</digi:trn>
	</c:set>
	
	<jsp:include page="tabManager/tabManager.jsp" />
</c:if>
<digi:form action="/viewTeamReports.do" method="post">
<html:hidden property="action"/>

<c:if test="${!aimTeamReportsForm.showTabs}">
	<c:set var="translation">
		<digi:trn>Are you sure you want to delete the selected report?</digi:trn>
	</c:set>
	<c:set var="pageTitle">
		<digi:trn>Report Manager</digi:trn>
	</c:set>
	<c:set var="breadCrumb">
		<digi:trn>Report Manager</digi:trn>
   	</c:set>
	<c:set var="titleColumn">
		<digi:trn>Report Title</digi:trn>
	</c:set>
	<c:set var="generator">
		<digi:trn>Report Generator</digi:trn>
	</c:set>
</c:if>



<!-- BREADCRUMP START -->
<div class="breadcrump">
	<div class="centering">
		<div class="breadcrump_cont" style="visibility: hidden">
			<span class="sec_name">${pageTitle}</span>
		</div>
	</div>
</div>
<!-- BREADCRUMP END --> 

<SCRIPT TYPE="text/javascript">
<!--
function popup(mylink, windowname)
{
	if (! window.focus)return true;
	var href;
	if (typeof(mylink) == 'string')
		href=mylink;
	else
	  	href=mylink.href;
	window.open(href,windowname,'channelmode=no,directories=no,menubar=no,resizable=yes,status=no,toolbar=no,scrollbars=yes,location=yes');
	return false;
}
//-->

function confirmFunc() {
  return confirm("${translation}");
  
	
	return false;
}
function activate(id){
	var number=0;
	$.ajax({
		   type: 'GET',
		   url: '/aim/getTabPositionSize.do',
		   cache : false,
		   	success: function(data,msg){
			number=parseInt(data);
			if(number<5){
				$(".activateTab"+id).hide();
				$(".savePosition"+id).show();	
			}
			else{
				alert("Please deactivate other tabs first!");
			}
		   },
	   	   error : function(XMLHttpRequest, textStatus, errorThrown){alert('Error, cannot get tab list.');} 
	});
	
	return false;
}
function showHidePositions(id,selectedPosition){
	$.ajax({
	   	url:'/aim/getTakenTabPositions.do',
	    cache : false,
	    type: 'get',
	    success: function(data, status) {
	    	var arrayPosition = jQuery.parseJSON(data);
	    	var selects=$("select[class^='savePositionDropDow']");
	    	for(var j=0;j<selects.length;j++){
	    		var index=1;
	    		var select=selects[j];
	    		var val = select.options[select.selectedIndex].value;
	    		select.options.length=0;
	    		select.options[0]=new Option("<digi:trn>None</digi:trn>", "-1", true, false);
	    		for(var label=1;label<=5;label++){
	    			var selected=false;
	    			var skip=false;
	    			var currvalue=label-1;
	    			for (var l = 0; l<arrayPosition.length;  l++) {
	            	    if(currvalue==arrayPosition[l].position){
	            	    	if(val==arrayPosition[l].position){
	            	    		skip=false;
	            	    		selected=true;
	            	    	}
	            	    	else{
	            	    		skip=true;
	            	    	}
	            	    	break;
	            	    }
	            	 }
	    			if(!skip){
	    				select.options[index]=new Option(label, currvalue, false, selected);	
		    			index++;
	    			}
	    			
	    		}
	    	}
	    	if(id!=null){
	    		if(selectedPosition==-1){
	        		$(".activateTab"+id).show();
	        		$(".savePosition"+id).hide();
	        	}
	    	}
	    	$("select[class^='savePositionDropDow']").removeAttr('disabled');
	       },
	    error: function(xhr, desc, err) {
	       		alert("unable to perform action!");
	        }
	    });
	
}

function savePosition(id){
	var selectId="select.savePositionDropDown"+id;
	var selectedPosition=$(selectId+  " option:selected").val();
	$("select[class^='savePositionDropDow']").attr('disabled', 'disabled');
	$.ajax({
   	url:'/aim/saveTabPosition.do',
    type: 'post',
    data: {position:selectedPosition, reportId:id},
    success: function(data, status) {
    	showHidePositions(id,selectedPosition);   
        },
    error: function(xhr, desc, err) {
       		alert("unable to perform action!");
        }
    });

}

$(document).ready(function() {
	$("select[class^='savePositionDropDow']").attr('disabled', 'disabled');
	showHidePositions();
	});
	
	function submitForm(action){
		document.aimTeamReportsForm.action.value=action;
		document.aimTeamReportsForm.submit();
		
	}

</SCRIPT>

<jsp:include page="teamPagesHeader.jsp" flush="true" />

<digi:errors/>

<table align="center">
	<tr>
		<td>
		<div id="content" class="yui-skin-sam">
			<div id="demo" class="yui-navset" style="width: 1000px;">
				<ul class="yui-nav" id="MyTabs">
					<li class="selected">
			        	<a rel="Tab_Name" href="#" id="Tab-tab tertiary" onclick="return false;" style="background-color: #f2f2f2; background-repeat: no-repeat; background-image: none;">
			        		<div>${pageTitle}</div>
			        	</a>
					</li>
				</ul>
				<div id="Tab_Name" class="ui-tabs-panel ui-widget-content ui-corner-bottom" style="background-color: #F2F2F2; padding: 5px; border: 1px solid rgb(208, 208, 208);">
					<table align="center">
						<tr>
						    <td>
						    	<table>
					        		<tr>
					          			<td>
					            			<table bgcolor="#FFFFFF" style="width: 970px; border-left: 1px solid #CCCCCC; border-top: 1px solid #CCCCCC; border-bottom: 1px solid #CCCCCC; border-right: 2px solid #CCCCCC;">
					              				<tr>
         	<td>
          	<table cellpadding="6" cellspacing="6">
               <tr>
                   <td id="reportsearchform"><c:choose><c:when test="${aimTeamReportsForm.showTabs}"><digi:trn>Tab Title</digi:trn></c:when><c:otherwise><digi:trn>Report Title</digi:trn></c:otherwise></c:choose>: <html:text property="keyword"/> </td> <td id="reportsearchform1"> <input type="button"  value="<digi:trn>Search</digi:trn>" onclick="submitForm('search')"/></td> <td id="reportsearchform2"><input type="button"  value="<digi:trn>clear</digi:trn>" onclick="submitForm('clear')"/></td>
               </tr>
              </table>
             </td>
         </tr>
         <tr>
					                				<td>
					                					<c:set var="reportNumber" value="${fn:length(aimTeamReportsForm.reports)}"></c:set>
					                  					<c:if test="${reportNumber != 0}">
					                  						<table>
					                  							<logic:present name="isUserLogged" scope="session">
																	<c:if test="${!aimTeamReportsForm.showTabs}">
																		<div class="filtered"style="float: right; font-size: 11px;">
																			<span><img src= "/TEMPLATE/ampTemplate/images/bullet_grey_sq.gif" border="0" style="vertical-align: baseline;" /></span>
										                       				<span><digi:trn>Not filtered Report</digi:trn>&nbsp;
										                       				<img src= "/TEMPLATE/ampTemplate/images/bullet_green_sq.gif" border="0" style="vertical-align: baseline; margin-left: 8px;" /></span>
										                       				<span><digi:trn>Filtered Report</digi:trn>&nbsp;
										                   					<img src= "/repository/message/view/images/edit.gif" border="0" style="vertical-align: bottom; margin-left: 8px;" /></span>
										                       				<span><digi:trn>Edit Report</digi:trn>&nbsp;
										                   					<img src= "/repository/message/view/images/trash_12.gif" border="0" style="vertical-align: bottom; margin-left: 8px;" /></span>
										                       				<span><digi:trn>Delete Report</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;</span>
										                       			</div>
																	</c:if>
																	<c:if test="${aimTeamReportsForm.showTabs}">
										               					<div  class="filtered" style="float: right; font-size: 11px;">
										                   					<span><img src= "/TEMPLATE/ampTemplate/images/bullet_grey_sq.gif" border="0" style="vertical-align: baseline;" />
										                       				<digi:trn>Not filtered Tab</digi:trn>&nbsp;
										                       				<span><img src= "/TEMPLATE/ampTemplate/images/bullet_green_sq.gif" border="0" style="vertical-align: baseline; margin-left: 8px;" /></span>
										                       				<digi:trn>Filtered Tab</digi:trn>&nbsp;
										                   					<span><img src= "/repository/message/view/images/edit.gif" border="0" style="vertical-align: bottom; margin-left: 8px;" /></span>
										                       				<digi:trn>Edit Tab</digi:trn>&nbsp;
										                   					<span><img src= "/repository/message/view/images/trash_12.gif" border="0" style="vertical-align: bottom; margin-left: 8px;" /></span>
										                       				<span><digi:trn>Delete Tab</digi:trn>&nbsp;&nbsp;&nbsp;&nbsp;</span>
										                       			</div>
																	</c:if>
					           									</logic:present>
					                    						<tr style="font-size: 11px; font-family: Aria, sans-serif;">
					                      							<td>                                     
												                        <%
												                        ReportsForm aimTeamReportsForm = (ReportsForm) pageContext.getAttribute("aimTeamReportsForm");
												                        java.util.List pagelist = new java.util.ArrayList();
																		for(int i = 0; i < aimTeamReportsForm.getTotalPages(); i++)
												                        	pagelist.add(new Integer(i + 1));
												                        pageContext.setAttribute("pagelist",pagelist);
																		pageContext.setAttribute("maxpages", new Integer(aimTeamReportsForm.getTotalPages()));
												                        pageContext.setAttribute("actualPage", new Integer(aimTeamReportsForm.getPage()));
												                        %>
					                        							<jsp:useBean id="urlParamsPagination" type="java.util.Map" class="java.util.HashMap"/>
												                        <c:set target="${urlParamsPagination}" property="action" value="getPage"/>
												                        <c:if test="${aimTeamReportsForm.currentPage >0}">
					                          								<jsp:useBean id="urlParamsFirst" type="java.util.Map" class="java.util.HashMap"/>
													                        <c:set target="${urlParamsFirst}" property="page" value="0"/>
													                        <c:set target="${urlParamsFirst}" property="action" value="getPage"/>
													                        <c:if test="${aimTeamReportsForm.showTabs}">
													                        	<c:set target="${urlParamsFirst}" property="tabs" value="true"/>
													                        </c:if>
					                          								<c:set var="translation">
					                            								<digi:trn key="aim:firstpage">First Page</digi:trn>
					                          								</c:set>
													                        <digi:link href="/viewTeamReports.do"  style="text-decoration=none" name="urlParamsFirst" title="${translation}" ><digi:trn key="aim:firstpage">First Page</digi:trn></digi:link>
					                          								<jsp:useBean id="urlParamsPrevious" type="java.util.Map" class="java.util.HashMap"/>
																			<c:set target="${urlParamsPrevious}" property="page" value="${aimTeamReportsForm.currentPage -1}"/>
														                    <c:set target="${urlParamsPrevious}" property="action" value="getPage"/>
														                    <c:if test="${aimTeamReportsForm.showTabs}">
					                          									<c:set target="${urlParamsPrevious}" property="tabs" value="true"/>
					                          								</c:if>
					                          								&nbsp;|&nbsp; 
					                          								<c:set var="translation">
						                          								<digi:trn key="aim:previous">Previous</digi:trn>
					                          								</c:set>
																			<!--<digi:link href="/viewTeamReports.do" name="urlParamsPrevious" style="text-decoration=none" title="${translation}" >-->
																				<!--<digi:trn key="aim:previous">Previous</digi:trn>-->
																			<!--</digi:link>-->
																			<!--&nbsp;|&nbsp;-->
					                        							</c:if>
												                        <c:set var="length" value="${aimTeamReportsForm.pagesToShow}"></c:set>
												                        <c:set var="start" value="${aimTeamReportsForm.offset}"/>
					                        							<c:if test="${maxpages > 1}">
						                        							<logic:iterate name="pagelist" id="pageidx" type="java.lang.Integer" offset="${start}" length="${length}">
						                          								<c:set target="${urlParamsPagination}" property="page" value="${pageidx - 1}"/>                         
						                            							<c:if test="${(pageidx - 1) eq actualPage}"> 
						                                							<span style="background-color: #FF6000; margin-left:2px; margin-right:2px; color: white; padding: 1px 3px; font-weight: bold; border: 1px solid #CCCCCC;">
						                                								<bean:write name="pageidx"/>
						                                							</span>
						                            							</c:if>
						                            							<c:if test="${(pageidx - 1) ne actualPage}"> 
							                          								<c:if test="${aimTeamReportsForm.showTabs}">
							                          									<c:set target="${urlParamsPagination}" property="tabs" value="true"/>
							                          								</c:if>
						                              								<digi:link href="/viewTeamReports.do"  name="urlParamsPagination" >
						                                								<bean:write name="pageidx"/>
						                              								</digi:link>
						                            							</c:if>
						                          								<c:if test="${pageidx < maxpages}">&nbsp;|&nbsp;</c:if>
						                       								</logic:iterate>
						                       							</c:if>
					                        							<c:if test="${aimTeamReportsForm.currentPage+1 != aimTeamReportsForm.totalPages}">
					                          								<jsp:useBean id="urlParamsNext" type="java.util.Map" class="java.util.HashMap"/>
					                          								<c:if test="${aimTeamReportsForm.currentPage+1 > aimTeamReportsForm.totalPages}">
					                            								<c:set target="${urlParamsNext}" property="page" value="${aimTeamReportsForm.currentPage}"/>
					                          								</c:if>
					                          								<c:if test="${aimTeamReportsForm.currentPage+1 <= aimTeamReportsForm.totalPages}">
					                            								<c:set target="${urlParamsNext}" property="page" value="${aimTeamReportsForm.currentPage+1}"/>
					                          								</c:if>
					                         								<c:set target="${urlParamsNext}" property="action" value="getPage"/>
					                          								<c:set var="translation">
					                            								<digi:trn key="aim:nextpage">Next Page</digi:trn>
					                          								</c:set>
					                          								<c:if test="${aimTeamReportsForm.showTabs}">
					                          									<c:set target="${urlParamsNext}" property="tabs" value="true"/>
					                          								</c:if>
																			<!--<digi:link href="/viewTeamReports.do"  style="text-decoration=none" name="urlParamsNext" title="${translation}"  >-->
																				<!--<digi:trn key="aim:next">Next</digi:trn>-->
																			<!--</digi:link>-->
					                          								&nbsp;|&nbsp;
					                          								<jsp:useBean id="urlParamsLast" type="java.util.Map" class="java.util.HashMap"/>
					                          								<c:set target="${urlParamsLast}" property="page" value="${aimTeamReportsForm.totalPages-1}"/>
					                          								<c:set target="${urlParamsLast}" property="action" value="getPage"/>
					                          								<c:if test="${aimTeamReportsForm.showTabs}">
					                          									<c:set target="${urlParamsLast}" property="tabs" value="true"/>
					                          								</c:if>
					                          								<c:set var="translation">
					                            								<digi:trn key="aim:lastpage">Last Page</digi:trn>
					                          								</c:set>
					                          								<digi:link href="/viewTeamReports.do"  style="text-decoration=none" name="urlParamsLast" title="${translation}"><digi:trn key="aim:lastpage">Last Page</digi:trn></digi:link>
					                          								&nbsp;&nbsp; 
					                        							</c:if>
																		<!--<c:out value="${aimTeamReportsForm.currentPage+1}"></c:out>&nbsp;<digi:trn key="aim:of">of</digi:trn>&nbsp;<c:out value="${aimTeamReportsForm.totalPages}"></c:out>-->
					                      							</td>
					                    						</tr>
					                  						</table>
					                  					</c:if>
					                				</td>
					             				 </tr>
					              				<tr>
					                				<td valign="top">
					                  					<table>
					                    					<tr>                    
					                     						<td>
					                        						<table class="inside" style="font-size:11px; font-family: Arial,sans-serif; background-color: white;" width="950px">
					                          							<tr>
					                          								<td align="center" class="inside_header">&nbsp;
					                            								
					                            							</td>
																					<td align="center" class="inside_header"><c:if
																							test="${not empty aimTeamReportsForm.sortBy && aimTeamReportsForm.sortBy!=1}">
																							<digi:link href="/viewTeamReports.do?sortBy=1">
																								<b><digi:trn>${titleColumn}</digi:trn>
																								</b>
																							</digi:link>
																							<c:if test="${aimTeamReportsForm.sortBy==2}">
																								<img src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
																									alt="down" />
																							</c:if>
																						</c:if> <c:if
																							test="${empty aimTeamReportsForm.sortBy || aimTeamReportsForm.sortBy==1}">
																							<digi:link href="/viewTeamReports.do?sortBy=2">
																								<b><digi:trn key="aim:organizationName">${titleColumn}</digi:trn>
																								</b>
																							</digi:link>
																							<img src="/TEMPLATE/ampTemplate/images/arrow_up.gif" alt="up" />
																						</c:if></td>
																					<td align="center" class="inside_header"><b>
																							<c:if
																								test="${not empty aimTeamReportsForm.sortBy && aimTeamReportsForm.sortBy!=3}">
																								<digi:link href="/viewTeamReports.do?sortBy=3">
																									<digi:trn key="aim:reportOwnerName">
														                                          Owner
														                                      </digi:trn>
																								</digi:link>
																								<c:if test="${aimTeamReportsForm.sortBy==4}">
																									<img src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
																										alt="down" />
																								</c:if>
																							</c:if> <c:if
																								test="${empty aimTeamReportsForm.sortBy || aimTeamReportsForm.sortBy==3}">
																								<digi:link href="/viewTeamReports.do?sortBy=4">
																									<digi:trn key="aim:reportOwnerName">
														                                          Owner
														                                      </digi:trn>
																								</digi:link>
																								<img src="/TEMPLATE/ampTemplate/images/arrow_up.gif"
																									alt="up" />
																							</c:if> </b></td>
																					<td align="center" class="inside_header"><b>
																							<c:if
																								test="${not empty aimTeamReportsForm.sortBy && aimTeamReportsForm.sortBy!=5}">
																								<digi:link href="/viewTeamReports.do?sortBy=5">
																									<digi:trn key="aim:reportCreationDate">
															                                          Creation Date
															                                      </digi:trn>
																								</digi:link>
																								<c:if test="${aimTeamReportsForm.sortBy==6}">
																									<img src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
																										alt="down" />
																								</c:if>
																							</c:if> <c:if
																								test="${empty aimTeamReportsForm.sortBy || aimTeamReportsForm.sortBy==5}">
																								<digi:link href="/viewTeamReports.do?sortBy=6">
																									<digi:trn key="aim:reportCreationDate">
														                                            Creation Date
														                                        </digi:trn>
																								</digi:link>
																								<img src="/TEMPLATE/ampTemplate/images/arrow_up.gif"
																									alt="up" />
																							</c:if> </b></td>
																					<c:if test="${!aimTeamReportsForm.tabs}">
																						<td align="center" class="inside_header">
					                            								<b><digi:trn key="aim:reportType">Type</digi:trn></b>
					                            							</td>
					                            		</c:if>
					                            							<td align="center" class="inside_header">
					                              								<b><digi:trn key="aim:hierarchies">Hierarchies</digi:trn></b>
					                            							</td>
					                            							<%
					                            							String s = (String)session.getAttribute("teamLeadFlag");
					                               							TeamMember tm = (TeamMember) session.getAttribute("currentMember");
					                             							if(tm!=null) {
					                            							%>
					                              								<td align="center" class="inside_header">
																					<b><digi:trn>Fields</digi:trn></b>
																				</td>
																				<c:if test="${aimTeamReportsForm.showTabs}">
																				<td align="center" class="inside_header">
					                                								<b><digi:trn>Position</digi:trn></b>
					                              								</td>
																				</c:if>
					                              								<td align="center" class="inside_header">
					                                								<b><digi:trn key="aim:reportAction">Action</digi:trn></b>
					                              								</td>
					                            							<% } %>
					                          							</tr>                          
					                          							<c:if test="${reportNumber == 0}">
																			<c:if test="${!aimTeamReportsForm.showTabs}">
							                         							<tr>
							                            							<td colspan="4">
							                            								<digi:trn key="aim:noreportspresent">No reports present</digi:trn>
							                            							</td>
							                          							</tr>
																			</c:if>
																			<c:if test="${aimTeamReportsForm.showTabs}">
							                          							<tr>
							                            							<td colspan="4">
							                            								<digi:trn key="aim:notabspresent">No tabs present</digi:trn>
							                            							</td>
							                          							</tr>
																			</c:if>
					                          							</c:if>
					                          							<%String color = ""; %>
					                          							<logic:iterate name="aimTeamReportsForm"  property="reportsList" id="report" indexId="idx"
					                            						type="org.digijava.module.aim.dbentity.AmpReports">
					                              							<tr onmouseout="setPointer(this, <%=idx.intValue()%>, 'out', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" 
					                              							onmouseover="setPointer(this, <%=idx.intValue()%>, 'over', <%=(idx.intValue()%2==1?"\'#dbe5f1\'":"\'#ffffff\'")%>, '#a5bcf2', '#FFFF00');" >
					                              							    <%if(idx.intValue()%2==1) color = "#dbe5f1"; %>
					                              							    <%if(idx.intValue()%2!=1) color = "#ffffff"; %>
					                              								<td align="center" class="inside" style="padding-right: 10px; padding-left: 10px;" bgcolor="<%=color%>">
						                              								<logic:notEmpty name="report" property="filterDataSet">
						                                  								<img src= "/TEMPLATE/ampTemplate/images/bullet_green_sq.gif" vspace="2" border="0" align="middle" />
						                              								</logic:notEmpty>
						                              								<logic:empty name="report" property="filterDataSet">
						                                   								<img src= "/TEMPLATE/ampTemplate/images/bullet_grey_sq.gif" vspace="2" border="0" align="middle" />
						                              								</logic:empty>
					                              								</td>
					                              								<td class="inside" style="padding-right: 15px; padding-left: 15px;" bgcolor="<%=color%>">
					                              									<c:if test="${!aimTeamReportsForm.showTabs}">
	                              <digi:link href="/viewNewAdvancedReport.do?view=reset&widget=false&resetSettings=true"  paramName="report"  paramId="ampReportId" paramProperty="ampReportId" styleClass="h-box" onclick="return popup(this,'');" title="Click here to view the Report">
							                              									<b>
							                              										<p style="max-width: 400px;white-space: normal" title="${report.name}">
																									<c:if test="${fn:length(report.name) > 120}" >
																										<c:out value="${fn:substring(report.name, 0, 120)}" />...
																									</c:if>
																									<c:if test="${fn:length(report.name) < 120}" >
																										<c:out value="${report.name}" />
																									</c:if>
							                     	         									</p>  
							                              									</b>
																						</digi:link>
						                          									</c:if>
						                          									<c:if test="${aimTeamReportsForm.showTabs}">
						                          										<b>
																                            <p style="max-width: 400px;white-space: normal" title="${report.name}">
																								<c:if test="${fn:length(report.name) > 120}" >
																									<c:out value="${fn:substring(report.name, 0, 120)}" />...
																								</c:if>
																								<c:if test="${fn:length(report.name) < 120}" >
																									<c:out value="${report.name}" />
																								</c:if>
															                                </p>  
						                              									</b>
						                          									</c:if>
					                             
					                              									<logic:present name="report" property="reportDescription" >
					                                									<p style="max-width: 400px;white-space: normal" title="${report.reportDescription}">
																							<c:if test="${fn:length(report.reportDescription) > 120}" >
																								<c:out value="${fn:substring(report.reportDescription, 0, 120)}" />...
																							</c:if>
																							<c:if test="${fn:length(report.reportDescription) < 120}" >
																								<c:out value="${report.reportDescription}" />
																							</c:if>
					                                									</p>
					                              									</logic:present>
					                              								</td>
					                              								<td align="center" class="inside" style="padding-right: 15px; padding-left: 15px; font-size: 11px;" bgcolor="<%=color%>">
					                                								<p style="white-space: nowrap">
					                                									<logic:present name="report" property="ownerId">
					                                   										<i><bean:write name="report" property="ownerId.user.name" /></i>
					                                									</logic:present>
					                                								</p>
					                              								</td>
					                              								<td align="center" class="inside" style="padding-right: 15px; padding-left: 15px; font-size: 11px;" bgcolor="<%=color%>">
					                                								<p style="white-space: nowrap">
					                                  									<logic:present name="report" property="updatedDate">
					                                      									<bean:write name="report" property="formatedUpdatedDate" />
					                                  									</logic:present>
					                                								</p>
					                              								</td>
					                              								<c:if test="${!aimTeamReportsForm.tabs}">
					                              								<td class="inside" style="padding-right: 8px; padding-left: 8px; font-size: 11px;" bgcolor="<%=color%>">
					                                								<p style="white-space: nowrap">
					                                  									<li>
																							<%
														                                    if (report.getType()!=null && report.getType().equals(new Long(1))) {
														                                   	%>
														                                    	<digi:trn key="aim:donorType">donor</digi:trn>
															                                <%
														                                    }
														                                    else if (report.getType()!=null && report.getType().equals(new Long (3))){
															                                %>
														                                    	<digi:trn key="aim:regionalType">regional</digi:trn>
															                                <%
														                                    }
																							else if (report.getType()!=null && report.getType().equals(new Long(2))){
															                                %>
														                                    	<digi:trn key="aim:componentType">component</digi:trn>
																							<%
														                                    }
														                                    else if (report.getType()!=null && report.getType().equals(new Long(4))){
															                               	%>
														                                    	<digi:trn key="aim:contributionType">contribution</digi:trn>
														                                   	<%
														                                    }
														                                    else if (report.getType()!=null && report.getType().equals(new Long(5))){
															                                %>
															                                	<digi:trn>pledge</digi:trn>
															                               	<%}%>
						                              									</li>
					                                  									<logic:equal name="report" property="drilldownTab" value="true">
					                                    									<li>
					                                      										<digi:trn key="aim:typeDrilldownTab">Desktop Tab</digi:trn>
					                                    									</li>
					                                  									</logic:equal>
					                                  									<logic:equal name="report" property="publicReport" value="true">
					                                    									<li>
					                                      										<digi:trn key="aim:typePublicReport">Public Report</digi:trn>
					                                    									</li>
					                                  									</logic:equal>
					                                  									<logic:equal name="report" property="hideActivities" value="true">
					                                    									<li>
					                                      										<digi:trn key="aim:typeSummaryReport">Summary Report</digi:trn>
					                                    									</li>
					                                  									</logic:equal>                                  
					                                  									<logic:equal name="report" property="options" value="A">
					                                   	 									<li>
					                                    										<digi:trn key="aim:annualreport">Annual</digi:trn>
					                                    									</li>
					                                  									</logic:equal>
					                                  									<logic:equal name="report" property="options" value="Q">
					                                    									<li>
					                                    										<digi:trn key="aim:quarterlyreport">Quarterly</digi:trn>
					                                    									</li>
					                                  									</logic:equal>
					                                  									<logic:equal name="report" property="options" value="M">
					                                    									<li>
					                                    										<digi:trn key="aim:monthlyreport">Monthly</digi:trn>	
					                                    									</li>
					                                  									</logic:equal>
					                                								</p>
					                              								</td>
					                              								
					                              							</c:if>
					                              								
					                              								<td class="inside" style="padding-right: 10px; padding-left: 10px;  font-size: 11px; width: 20%;" bgcolor="<%=color%>">
					                                								<logic:iterate name="report" property="hierarchies" id="hierarchy" >
					                                  									<%-- <bean:write name="hierarchy" property="column.columnName"/> --%>
					                                  									<li>
						                                      								<digi:trn key="aim:report:${hierarchy.column.columnName}">
						                                        								<bean:write name="hierarchy" property="column.columnName" />
						                                      								</digi:trn>
					                                  									</li>
					                                								</logic:iterate>
					                              								</td>
					                              							<%if (tm != null) {%>
					                              								<td width="200" class="inside" style="padding-right: 10px; padding-left: 10px; font-size: 11px; width: 150px;" align="center" bgcolor="<%=color%>">  
						                                							<div style='position:relative;display:none;' id='report-<bean:write name="report" property="ampReportId"/>'> 
						                                  								<logic:iterate name="report" property="columns" id="column" indexId="index"  >
						                                    								<%if (index.intValue()%2==0){ %>
						                                      									<li>                                      
						                                      										<digi:trn key="aim:report:${column.column.columnName}">
						                                        										<bean:write name="column" property="column.columnName" />
						                                      										</digi:trn>
						                                    								<% } else {%>
						                                    								,
						                                      								<digi:trn key="aim:report:${column.column.columnName}">
						                                        								<bean:write name="column" property="column.columnName" />
						                                      								</digi:trn>
						                                      									</li>
						                                    								<%} %>
						                                  								</logic:iterate>
						                                							</div>
						                                							<span align="center" style="text-transform: capitalize;" onMouseOver="stm(['<digi:trn>Columns</digi:trn>',document.getElementById('report-<bean:write name="report" property="ampReportId"/>').innerHTML],Style[0])" onMouseOut="htm()">[ <u style="text-transform:capitalize;" ><digi:trn>Columns</digi:trn></u> ]&nbsp;</span>                               
						                                							<div style='position:relative;display:none;' id='measure-<bean:write name="report" property="ampReportId"/>'> 
						                                  								<logic:iterate name="report" property="measures" id="measure" indexId="index"  >
						                                    								<li>
						                                    									<digi:trn key="aim:reportBuilder:${measure.measure.aliasName}">${measure.measure.aliasName}</digi:trn>
						                                    								</li>
						                                  								</logic:iterate>
						                                							</div>
						                                							<span align="center" style="text-transform: capitalize;white-space: no-wrap;"  onMouseOver="stm(['<digi:trn key="aim:teamreports:measures">measures</digi:trn>',document.getElementById('measure-<bean:write name="report" property="ampReportId"/>').innerHTML],Style[1])" onMouseOut="htm()">[ <u><digi:trn key="aim:teamreports:measures">Measures</digi:trn></u> ]<br /></span>
					                                							</td>
					                                							<c:if test="${aimTeamReportsForm.showTabs}">
					                                							<td class="inside" style="padding-right: 15px; padding-left: 15px; font-size: 11px;" bgcolor="<%=color%>">
					                                						
					                                							<c:forEach var="desktopTab" items="${report.desktopTabSelections}">
					                                								<c:if test="${desktopTab.owner.ampTeamMemId==aimTeamReportsForm.currentMemberId}">
					                                									<c:set var="position">
					                                									${desktopTab.index}
					                                									</c:set>
					                                								</c:if>	                                								
					                                								</c:forEach>

					                                								<a class="activateTab${report.ampReportId}" onclick="activate(${report.ampReportId})" <c:if test="${not empty position}">style="display:none"</c:if>><digi:trn>activate</digi:trn></a>
					                                								<div class="savePosition${report.ampReportId}" <c:if test="${empty position}">style="display:none"</c:if>  >
					                                								<select class="savePositionDropDown${report.ampReportId}" 
																								onchange="savePosition(${report.ampReportId})">
																									<option value="-1"><digi:trn>none</digi:trn></option>
																									 <c:forEach var="i" begin="0" end="4">
            																							<option value="${i}" <c:if test="${position==i}">selected</c:if>><c:out value="${i+1}" /></option>
            																						</c:forEach>	
																					</select>
																					</div>

					                                								 <c:remove var="position" />
					                                								
					                                				
																				</td>
					                                							</c:if>
					                                							<td align="center" class="inside" style="padding-right: 15px; padding-left: 15px; font-size: 11px;" bgcolor="<%=color%>">
						                                							<p style="white-space: nowrap">
						                                								<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
						                                								<c:set target="${urlParams}" property="rid">
						                                  									<bean:write name="report" property="ampReportId" />
						                                								</c:set>
						                                								<c:set target="${urlParams}" property="event" value="edit" />
						                                								<logic:equal name="teamLeadFlag" scope="session" value="true"> 
					                                      									<c:set var="translation">
						                                      									<c:if test="${aimTeamReportsForm.showTabs}">
							                                        								<digi:trn key="aim:ClickEditTab">Click on this icon to edit tab&nbsp;</digi:trn>
						                                      									</c:if>
						                                      									<c:if test="${!aimTeamReportsForm.showTabs}">
						    	                                    								<digi:trn key="aim:ClickEditReport">Click on this icon to edit report&nbsp;</digi:trn>
						                                      									</c:if>
					                                        								</c:set>
						                                    								<digi:link href="/reportWizard.do?editReportId=${report.ampReportId}" title="${translation}">
						                                      									<img src= "/repository/message/view/images/edit.gif" vspace="2" border="0" align="absmiddle" />
						                                    								</digi:link> 
					                                      									&nbsp;
					                                      									<c:set var="translation">
					                                      										<c:if test="${aimTeamReportsForm.showTabs}">
						                                        									<digi:trn key="aim:ClickDeleteTab">Click on this icon to delete tab&nbsp;</digi:trn>
																									<c:set target="${urlParams}" property="isTab" value="1" />
					                                      										</c:if>
					                                      										<c:if test="${!aimTeamReportsForm.showTabs}">
						                                        									<digi:trn key="aim:ClickDeleteReport">Click on this icon to delete report&nbsp;</digi:trn>
																									<c:set target="${urlParams}" property="isTab" value="0" />
					                                      										</c:if>
					                                       									</c:set>
					                                      									<digi:link href="/deleteAllReports.do" name="urlParams" onclick="return confirmFunc()" title="${translation}">
																								<img src= "/repository/message/view/images/trash_12.gif" vspace="2" border="0" align="absmiddle" />
						                                  									</digi:link>
						                                								</logic:equal>                            
						                                								<logic:equal name="teamLeadFlag" scope="session" value="false">
						                                  									<logic:present name="report" property="ownerId">
						                                    									<logic:equal  name="report" property="ownerId.ampTeamMemId" value="${aimTeamReportsForm.currentMemberId}"> 
						                                        									<c:set var="translation">
						                                      											<c:if test="${aimTeamReportsForm.showTabs}">
							                                       											<digi:trn key="aim:ClickEditTab">Click on this icon to edit tab&nbsp;</digi:trn>
						                                      											</c:if>
						                                      											<c:if test="${!aimTeamReportsForm.showTabs}">
						    	                                    										<digi:trn key="aim:ClickEditReport">Click on this icon to edit report&nbsp;</digi:trn>
						                                      											</c:if>
					                                        										</c:set>
						                                    										<digi:link href="/reportWizard.do?editReportId=${report.ampReportId}" title="${translation}">
						                                      											<img src= "/repository/message/view/images/edit.gif" vspace="2" border="0" align="absmiddle" />
						                                    										</digi:link>
							                                    									<c:set var="translation">
						                                      											<c:if test="${aimTeamReportsForm.showTabs}">
							                                        										<digi:trn key="aim:ClickDeleteTab">Click on this icon to delete tab&nbsp;</digi:trn>
																											<c:set target="${urlParams}" property="isTab" value="1" />
						                                      											</c:if>
						                                      											<c:if test="${!aimTeamReportsForm.showTabs}">
							                                        										<digi:trn key="aim:ClickDeleteReport">Click on this icon to delete report&nbsp;</digi:trn>
																											<c:set target="${urlParams}" property="isTab" value="0" />
						                                      											</c:if>
						                                       										</c:set>
						                                       										<digi:link href="/deleteAllReports.do" name="urlParams" onclick="return confirmFunc()" title="${translation}">
																										<img src= "/repository/message/view/images/trash_12.gif" vspace="2" border="0" align="absmiddle" />
							                                  										</digi:link>
						                                    									</logic:equal>    
						                                  									</logic:present>                                                                                                
						                                								</logic:equal>                              
						                               	 							</p>
					                              								</td>
					                              							<% } %>   
					                          								</tr>
					                          							</logic:iterate>
					                        						</table>
					                      						</td>
					                    					</tr>      
					                  					</table>
					                				</td>
					              				</tr>
					              				<tr>
					              					<td valign="top">
					              						<c:if test="${reportNumber != 0}">
					                  						<table style="font-size: 11px; font-family: Aria, sans-serif;">
					                    						<tr>
					                      							<td align="center">                                     
					                        							<c:set target="${urlParamsPagination}" property="action" value="getPage"/>
					                        							<c:if test="${aimTeamReportsForm.currentPage >0}">
					                          								<c:set target="${urlParamsFirst}" property="page" value="0"/>
					                          								<c:set target="${urlParamsFirst}" property="action" value="getPage"/>
					                          								<c:if test="${aimTeamReportsForm.showTabs}">
					                          									<c:set target="${urlParamsFirst}" property="tabs" value="true"/>
					                          								</c:if>
					                          								<c:set var="translation">
					                            								<digi:trn key="aim:firstpage">First Page</digi:trn>
					                          								</c:set>
					                          								<digi:link href="/viewTeamReports.do"  style="text-decoration=none" name="urlParamsFirst" title="${translation}"><digi:trn key="aim:firstpage">First Page</digi:trn></digi:link>
					                          								<c:set target="${urlParamsPrevious}" property="page" value="${aimTeamReportsForm.currentPage -1}"/>
					                          								<c:set target="${urlParamsPrevious}" property="action" value="getPage"/>
					                          								<c:if test="${aimTeamReportsForm.showTabs}">
					                          									<c:set target="${urlParamsPrevious}" property="tabs" value="true"/>
					                          								</c:if>
					                          								&nbsp;|&nbsp; 
					                          								<c:set var="translation">
						                          								<digi:trn key="aim:previous">Previous</digi:trn>
					                          								</c:set>
<!--					                          								<digi:link href="/viewTeamReports.do" name="urlParamsPrevious" style="text-decoration=none" title="${translation}" >-->
<!--					                            								<digi:trn key="aim:previous">Previous</digi:trn>-->
<!--					                          								</digi:link>-->
<!--					                          								&nbsp;|&nbsp;-->
					                        							</c:if>
					                        							<c:set var="length" value="${aimTeamReportsForm.pagesToShow}"></c:set>
					                        							<c:set var="start" value="${aimTeamReportsForm.offset}"/>
					                        							<c:if test="${maxpages > 1}">
						                        							<logic:iterate name="pagelist" id="pageidx" type="java.lang.Integer" offset="${start}" length="${length}">
						                          								<c:set target="${urlParamsPagination}" property="page" value="${pageidx - 1}"/>                         
						                            							<c:if test="${(pageidx - 1) eq actualPage}"> 
						                                							<span style="background-color: #FF6000; margin-left:2px; margin-right:2px; color: white; padding: 1px 3px; font-weight: bold; border: 1px solid #CCCCCC;">
						                                								<bean:write name="pageidx"/>
						                                							</span>
						                            							</c:if>
						                            							<c:if test="${(pageidx - 1) ne actualPage}"> 
							                          								<c:if test="${aimTeamReportsForm.showTabs}">
							                          									<c:set target="${urlParamsPagination}" property="tabs" value="true"/>
							                          								</c:if>
						                              								<digi:link href="/viewTeamReports.do"  name="urlParamsPagination" >
						                                								<bean:write name="pageidx"/>
						                              								</digi:link>
						                            							</c:if>
						                          								<c:if test="${pageidx < maxpages}">&nbsp;|&nbsp;</c:if>
						                        							</logic:iterate>
					                        							</c:if>
					                        							<c:if test="${aimTeamReportsForm.currentPage+1 != aimTeamReportsForm.totalPages}">
					                          								<c:if test="${aimTeamReportsForm.currentPage+1 > aimTeamReportsForm.totalPages}">
					                            								<c:set target="${urlParamsNext}" property="page" value="${aimTeamReportsForm.currentPage}"/>
					                          								</c:if>
					                          								<c:if test="${aimTeamReportsForm.currentPage+1 <= aimTeamReportsForm.totalPages}">
					                            								<c:set target="${urlParamsNext}" property="page" value="${aimTeamReportsForm.currentPage+1}"/>
					                          								</c:if>
					                          								<c:if test="${aimTeamReportsForm.showTabs}">
					                          									<c:set target="${urlParamsNext}" property="tabs" value="true"/>
					                          								</c:if>
					                         								<c:set target="${urlParamsNext}" property="action" value="getPage"/>
					                          								<c:set var="translation">
					                            								<digi:trn key="aim:nextpage">Next Page</digi:trn>
					                          								</c:set>
<!--					                          								<digi:link href="/viewTeamReports.do"  style="text-decoration=none" name="urlParamsNext" title="${translation}"  >-->
<!--					                            								<digi:trn key="aim:next">Next</digi:trn>-->
<!--					                          								</digi:link>-->
					                          								&nbsp;|&nbsp;
													                       	<c:set target="${urlParamsLast}" property="page" value="${aimTeamReportsForm.totalPages-1}"/>
													                        <c:set target="${urlParamsLast}" property="action" value="getPage"/>
													                        <c:if test="${aimTeamReportsForm.showTabs}">
					                          									<c:set target="${urlParamsLast}" property="tabs" value="true"/>
					                          								</c:if>
					                          								<c:set var="translation">
					                            								<digi:trn key="aim:lastpage">Last Page</digi:trn>
					                          								</c:set>
					                          								<digi:link href="/viewTeamReports.do"  style="text-decoration=none" name="urlParamsLast" title="${translation}"><digi:trn key="aim:lastpage">Last Page</digi:trn></digi:link>
					                          								&nbsp;&nbsp; 
					                        							</c:if>
																		<!--<c:out value="${aimTeamReportsForm.currentPage+1}"></c:out>&nbsp;<digi:trn key="aim:of">of</digi:trn>&nbsp;<c:out value="${aimTeamReportsForm.totalPages}"></c:out>-->
					                      							</td>
					                    						</tr>
					                  						</table>
					                  					</c:if>
					              					</td>
					              				</tr>
					            			</table>
					          			</td>
					        		</tr>
					      		</table>
             </digi:form>
					    	</td>
					  	</tr>
					</table>
				</div>
			</div>
			</div>
		</td>
	</tr>
</table>