<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
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
			var msg ='<digi:trn>Are you sure you want to remove the selected activities</digi:trn>';
			var flag = confirm("${msg}");
			if(flag == false)

			  return false;
			else
				return true;
		} else {
			return false;
		}

	}

	function sortMe(val) {
		<digi:context name="sel" property="context/module/moduleinstance/teamActivityList.do" />
			url = "<%= sel %>" ;

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

	function page(val) {
		<digi:context name="sel" property="context/module/moduleinstance/teamActivityList.do" />
			url = "<%= sel %>?page=" + val ;
			document.aimTeamActivitiesForm.action = url;
			document.aimTeamActivitiesForm.submit();
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
									<c:set var="selectedSubTab" value="0" scope="request"/>
										
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
										
										
									<jsp:include page="teamSetupMenu.jsp"  />
									
									<table class="inside normal" width="100%" cellpadding="0" cellspacing="0">
										<tr>
									  	<td width="5" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
									  		<input type="checkbox" id="checkAll">
									  	</td>
									    <td width="20%" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
									    	<b class="ins_title"><digi:trn key="aim:ampId">AMP ID</digi:trn></b>
									    </td>
									    <td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
									    	<b class="ins_title">
									    		<a  style="color:black" href="javascript:sortMe('activity')" title="Click here to sort by Activity Details">
														<b><digi:trn key="aim:activityListinWorkspace">List of Activities in the Workspace</digi:trn></b>
													</a>
									    	</b>
									    </td>
									    <td width="20%" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
									    	<b class="ins_title">
									    		<a  style="color:black" href="javascript:sortMe('donor')" title="Click here to sort by Donors">
														<b><digi:trn key="aim:donors">Donors</digi:trn></b>
													</a>
									    	</b>
									    </td>
										</tr>
										
										<logic:empty name="aimTeamActivitiesForm" property="activities">
											<tr>
												<td class="inside" align="center" colspan="4">
																	<digi:trn key="aim:noNonDraftActivitiesPresent">
																		No activities present. You cannot reassign draft activities.
													</digi:trn>
												</td>
											</tr>
										</logic:empty>
										
										<logic:notEmpty name="aimTeamActivitiesForm" property="activities">
											<logic:iterate name="aimTeamActivitiesForm" property="activities" id="activities">
												<tr>
													<td class="inside">
														<html:multibox property="selActivities">
															<bean:write name="activities" property="ampActivityId" />
														</html:multibox>
													</td>
													<td class="inside">
														<bean:write name="activities" property="ampId"/>
													</td>
													<td class="inside">
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
													<td class="inside">
														<bean:write name="activities" property="donors" />
													</td>
												</tr>
											</logic:iterate>	
										</logic:notEmpty>
									</table>
									
									<!-- Pagination -->
									<logic:notEmpty name="aimTeamActivitiesForm" property="pages">
										<div class="paging" style="font-size:11px;">
													<digi:trn key="aim:pages">
														Pages :
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
									<logic:notEmpty name="aimTeamActivitiesForm" property="activities">
										<div align="center">
											<html:submit  styleClass="buttonx_sm btn" property="submitButton"  onclick="return confirmDelete()">
												<digi:trn key="btn:removeSelectedActivities">Remove selected activities</digi:trn>
											</html:submit>
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
		$("input[name=selActivities]").attr("checked", $("#checkAll").attr("checked"));
	}
	);
</script>
