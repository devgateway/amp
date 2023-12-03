<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@page import="org.digijava.ampModule.aim.action.GetTeamActivities"%>
<style>
.contentbox_border{
	border: 	1px solid #666666;
	width: 		750px;
	background-color: #f4f4f2;
}
</style>

<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${bcparams}" property="tId" value="-1"/>
<c:set target="${bcparams}" property="dest" value="teamLead"/>


<script type="text/javascript">

<!--
	function validate() {
		if (document.aimTeamActivitiesForm.selActivities.checked != null) {
			if (document.aimTeamActivitiesForm.selActivities.checked == false) {
				alert("Please choose an activity to remove");
				return false;
			}
		} else {
			var length = document.aimTeamActivitiesForm.selActivities.length;
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.aimTeamActivitiesForm.selActivities[i].checked == true) {
					flag = 1;
					break;
				}
			}

			if (flag == 0) {
				alert("Please choose an activity to remove");
			return false;
			}
		}
		return true;
	}

	function confirmDelete() {
		var valid = validate();
		if (valid == true) {
                    var msg ='<digi:trn jsFriendly="true">Are you sure you want to remove the selected activities</digi:trn>';
			var flag = confirm(msg);
			if(flag == false)

			  return false;
			else
				return true;
		} else {
			return false;
		}

	}

	function sortMe(val) {
		
		<c:set var="unarchivedTab"><%=GetTeamActivities.UNARCHIVED_SUB_TAB %></c:set>
		<c:set var="archivedTab"><%=GetTeamActivities.ARCHIVED_SUB_TAB %></c:set>
		<c:if test="${selectedSubTab==unarchivedTab}">
		     var archiveselected =false ;	
	     </c:if>
	     <c:if test="${selectedSubTab==archivedTab}">
	     var archiveselected =true ;	
     </c:if>
			
		<digi:context name="sel" property="context/ampModule/moduleinstance/teamActivityList.do" />
			 url = "<%= sel %>" ;
           // url = '/aim/teamActivityList.do~showArchivedActivities=false~dest=teamLead~tId=-1~subtab=0';
           if (typeof archiveselected != 'undefined') 
               url = url +'~showArchivedActivities='+archiveselected;
             
			var sval = document.aimTeamActivitiesForm.sort.value;
			var soval = document.aimTeamActivitiesForm.sortOrder.value;

			if ( val == sval ) {
				if (soval == "asc")
					document.aimTeamActivitiesForm.sortOrder.value = "desc";
				else if (soval == "desc")
					document.aimTeamActivitiesForm.sortOrder.value = "asc";
			}
			else
				document.aimTeamActivitiesForm.sortOrder.value = "asc";

			document.aimTeamActivitiesForm.sort.value = val;
			document.aimTeamActivitiesForm.action = url;
			document.aimTeamActivitiesForm.submit();
	}
	function submitArchiveCmd(archive) {
		var checkCheckBoxes=$("input[name='selActivities']:checked").length;
		if(checkCheckBoxes<1){
			alert('<digi:trn jsFriendly="true">Please choose the activities</digi:trn>');
			return false;
		}
		else{
			var msg;
			if(archive=='archive'){
				msg='<digi:trn jsFriendly="true">Are you sure you want to archive the selected activities?</digi:trn>';
			}
			else{
				msg='<digi:trn jsFriendly="true">Are you sure you want to unarchive the selected activities?</digi:trn>';
			}
			if(confirm(msg)){
				document.aimTeamActivitiesForm.removeActivity.value = archive;
				document.aimTeamActivitiesForm.submit();
			}
		}
		
	}

	function page(val) {
		<digi:context name="sel" property="context/ampModule/moduleinstance/teamActivityList.do" />
		<c:set var="unarchivedTab"><%=GetTeamActivities.UNARCHIVED_SUB_TAB %></c:set>
		<c:set var="archivedTab"><%=GetTeamActivities.ARCHIVED_SUB_TAB %></c:set>
		<c:if test="${selectedSubTab==unarchivedTab}">
		<digi:context name="sel" property="context/ampModule/moduleinstance/teamActivityList.do~showArchivedActivities=false~dest=teamLead~tId=-1~subtab=0" />
    	</c:if>
    	<c:if test="${selectedSubTab==archivedTab}">
		<digi:context name="sel" property="context/ampModule/moduleinstance/teamActivityList.do~showArchivedActivities=true~dest=teamLead~tId=-1~subtab=0" />
    	</c:if>
			url = "<%= sel %>?page=" + val ;
			document.aimTeamActivitiesForm.action = url;
			document.aimTeamActivitiesForm.submit();
	}
	
	
	function resetSearch() {
		<digi:context name="searchOrg" property="context/ampModule/moduleinstance/teamActivityList.do~dest=teamLead~tId=-1~subtab=0"/>
		url = "<%= searchOrg %>?reset=true";
	     document.aimTeamActivitiesForm.action = url;
	     document.aimTeamActivitiesForm.submit();
		 return true;

	}

	function searchActivity(teamId) {
			 <digi:context name="searchOrg" property="context/ampModule/moduleinstance/teamActivityList.do~dest=teamLead~tId=-1~subtab=0"/>
		     url = "<%= searchOrg %>";
		     document.aimTeamActivitiesForm.action = url;
		     document.aimTeamActivitiesForm.submit();
			 return true;
	}

-->

</script>

<digi:instance property="aimTeamActivitiesForm" />
<digi:form action="/updateTeamActivity.do" method="post">

<html:hidden property="removeActivity" value="remove" />

<html:hidden property="sort" />
<html:hidden property="sortOrder" />
<html:hidden property="page" />

<table width="100%" cellspacing="0" cellpadding="0" valign="top" align="left">
<tr><td width="100%" valign="top" align="left">
<jsp:include page="teamPagesHeader.jsp"  />
</td></tr>
<tr><td>

									<c:set var="selectedTab" value="2" scope="request"/>
										
									<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
										<tr>
											<td>
												<div class="breadcrump_cont">
													<span class="sec_name">
														<digi:trn key="aim:activityListinWorkspace">List of Activities in the Workspace</digi:trn>
													</span>
													
													<span class="breadcrump_sep">|</span>
													<digi:link href="/viewMyDesktop.do" title="${translation}" styleClass="l_sm">
														<digi:trn key="aim:portfolio">Portfolio</digi:trn>
													</digi:link>
													<span class="breadcrump_sep"><b>�</b></span>
													<c:set var="translation">
														<digi:trn key="aim:clickToViewWorkspaceOverview">Click here to view Workspace Overview</digi:trn>
													</c:set>
													<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="l_sm" title="${translation}">
													<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></digi:link>
													<span class="breadcrump_sep"><b>�</b></span>
													<span class="bread_sel"><digi:trn key="aim:activityListinWorkspace">List of Activities in the Workspace</digi:trn></span>
												</div>
											</td>
										</tr>
										<tr>
											<td valign="top">
												<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">	
									<c:if test="${selectedSubTab==null}">
										<c:set var="selectedSubTab" value="0" scope="request"/>
									</c:if>	
									<jsp:include page="teamSetupMenu.jsp"  />
									
									<table class="inside normal" width="970" cellpadding="0" cellspacing="0" style="border:none;">
										<tr>
											<td>
												<table>
													<tr>
														<td nowrap="nowrap">
															<digi:trn>Keyword</digi:trn>&nbsp;
															<html:text property="keyword" styleClass="inp-text" />
														</td>
														<td nowrap="nowrap">
															<digi:trn>Results</digi:trn>&nbsp;
															<html:select property="tempNumResults" style="width:100px" styleClass="inp-text" onchange="return searchActivity('${aimTeamActivitiesForm.teamId }')">
                                                                <c:if test="${aimTeamActivitiesForm.tempNumResults!=-1}">
																    <html:option value="${aimTeamActivitiesForm.tempNumResults}"><digi:trn>Current</digi:trn>: ${aimTeamActivitiesForm.tempNumResults}</html:option>
                                                                </c:if>
																<html:option value="10">10</html:option>
																<html:option value="20">20</html:option>
																<html:option value="50">50</html:option>
																<html:option value="-1"><digi:trn>All</digi:trn></html:option>
															</html:select>
														</td>
														<td>
															<c:set var="trnResetBtn">
																<digi:trn>Reset</digi:trn>
															</c:set>
															<input type="button" value="${trnResetBtn}" class="dr-menu" onclick="return resetSearch()">
														</td>
														<td>					
															<c:set var="trnGoBtn">
																<digi:trn> GO </digi:trn>
															</c:set>
															<input type="button" value="${trnGoBtn}" class="dr-menu" onclick="return searchActivity('${aimTeamActivitiesForm.teamId }')">
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
                                        <td style="background:#ccc;">
                                        	<table cellpadding="1" cellspacing="1" border="0" width="970" style="background:none;border:none;">
                                            	<tr>
                                                <td width="5" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside"  style="border:none;width:25px;border-left:none;" >
									  		<input type="checkbox" id="checkAll">
									  	</td>
									    <td width="20%" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside"  style="border:none;">
									    	<b class="ins_title"><digi:trn key="aim:ampId">AMP ID</digi:trn></b>
									    </td>
									    <td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside" style="border:none;">
									    	<b class="ins_title">
									    		<a  style="color:black" href="javascript:sortMe('activity')" title="Click here to sort by Activity Details">
														<b><digi:trn key="aim:activityListinWorkspace">List of Activities in the Workspace</digi:trn></b>
													<c:if test="${empty aimTeamActivitiesForm.sort || aimTeamActivitiesForm.sort=='activity' && aimTeamActivitiesForm.sortOrder=='asc'}">
														<img id="activityColumnImg" src="/repository/aim/images/up.gif" />
													</c:if>
													<c:if test="${empty aimTeamActivitiesForm.sort || aimTeamActivitiesForm.sort=='activity' && aimTeamActivitiesForm.sortOrder=='desc'}">
														<img id="activityColumnImg" src="/repository/aim/images/down.gif" />
													</c:if>
												</a>												
									    	</b>
									    </td>
									    <td width="20%" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside" style="border:none;" >
									    	<b class="ins_title">
									    		<a  style="color:black" href="javascript:sortMe('donor')" title="Click here to sort by Donors">
														<b><digi:trn key="aim:donors">Donors</digi:trn></b>
														<c:if test="${empty aimTeamActivitiesForm.sort || aimTeamActivitiesForm.sort=='donor' && aimTeamActivitiesForm.sortOrder=='asc'}">
															<img id="activityColumnImg" src="/repository/aim/images/up.gif" />
														</c:if>
														<c:if test="${empty aimTeamActivitiesForm.sort || aimTeamActivitiesForm.sort=='donor' && aimTeamActivitiesForm.sortOrder=='desc'}">
															<img id="activityColumnImg" src="/repository/aim/images/down.gif" />
														</c:if>
													</a>
									    	</b>
									    </td>
										</tr>
										
										<logic:empty name="aimTeamActivitiesForm" property="activities">
											<tr>
												<td class="inside1" align="center" colspan="4">
													<digi:trn key="aim:noNonDraftActivitiesPresent">No activities present.</digi:trn>
												</td>
											</tr>
										</logic:empty>
										
										<logic:notEmpty name="aimTeamActivitiesForm" property="activities">
											<logic:iterate name="aimTeamActivitiesForm" property="activities" id="activities">
												<tr>
													<td class="inside1">
														<html:multibox property="selActivities">
															<bean:write name="activities" property="ampActivityId" />
														</html:multibox>
													</td>
													<td class="inside1">
														<bean:write name="activities" property="ampId"/>
													</td>
													<td class="inside1">
														<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams}" property="ampActivityId">
																<bean:write name="activities" property="ampActivityId" />
															</c:set>
															<c:set target="${urlParams}" property="pageId" value="3"/>
															<c:set var="translation">
																<digi:trn key="aim:clickToViewActivityDetails">
																Click here to view Activity Details</digi:trn>
															</c:set>
															<digi:link href="/viewActivityPreview.do" name="urlParams"
															title="${translation}">
																<bean:write name="activities" property="name" />
															</digi:link>
													</td>
													<td class="inside1">
														<bean:write name="activities" property="donors" />
													</td>
												</tr>
											</logic:iterate>	
											
										</logic:notEmpty>
                                                </tr>
                                            </table>
                                        </td>
									  	
									</table>
									
									<!-- Pagination -->
									<logic:notEmpty name="aimTeamActivitiesForm" property="pages">
										<div class="paging" style="font-size:11px;">
													<digi:trn key="aim:pages">
														Pages:
													</digi:trn>
														<logic:iterate name="aimTeamActivitiesForm" property="pages" id="pages" type="java.lang.Integer">
													  	<bean:define id="currPage" name="aimTeamActivitiesForm" property="currentPage" />

														<% if (currPage.equals(pages)) { %>
																<b class="paging_sel"><%=pages%></b>
														<%	} else { %>
															<c:set var="translation">
																<digi:trn key="aim:clickToViewNextPage">Click here to goto Next Page</digi:trn>
															</c:set>
															<a href="javascript:page(<%=pages%>)" title="${translation}" class="l_sm"><%=pages%></a>
														<% } %>
															|&nbsp;
														</logic:iterate>
											</div>
										</logic:notEmpty>
										<!-- end of Pagination -->
									
									
									<br>
								<logic:notEmpty name="aimTeamActivitiesForm"
									property="activities">
									<div align="center">

										<c:set var="unarchivedTab"><%=GetTeamActivities.UNARCHIVED_SUB_TAB %></c:set>
										<c:set var="archivedTab"><%=GetTeamActivities.ARCHIVED_SUB_TAB %></c:set>
										<c:choose>
											<c:when test="${selectedSubTab==unarchivedTab}">
												<c:set var="archiveActs"><%=GetTeamActivities.ARCHIVE_COMMAND %></c:set>
												<html:button onclick="submitArchiveCmd('${archiveActs}')"
													styleClass="buttonx_sm btn" property="submitButton">
													<digi:trn>Archive Activities</digi:trn>
												</html:button>
											</c:when>
											<c:when test="${selectedSubTab==archivedTab}">
												<c:set var="unArchiveActs"><%=GetTeamActivities.UNARCHIVE_COMMAND %></c:set>
												<html:button onclick="submitArchiveCmd('${unArchiveActs}')"
													styleClass="buttonx_sm btn" property="submitButton">
													<digi:trn>Unarchive Activities</digi:trn>
												</html:button>
											</c:when>
											<c:otherwise>
												<html:submit styleClass="buttonx_sm btn"
													property="submitButton" onclick="return confirmDelete()">
													<digi:trn key="btn:removeSelectedActivities">Remove selected activities</digi:trn>
												</html:submit>
											</c:otherwise>
										</c:choose>


									</div>
								</logic:notEmpty>



							</div>
										</div>											
												
											</td>
										</tr>
									</table>										
										
		</td>
	</tr>
</table>
</digi:form>

<script language="javascript">
    $("#checkAll").bind("change", function (obj){
        $("input[name=selActivities]").attr("checked", $("#checkAll").is(":checked"));
    });
</script>
