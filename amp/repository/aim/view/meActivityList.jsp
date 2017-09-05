<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>



<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${bcparams}" property="tId" value="-1"/>

<c:set target="${bcparams}" property="dest" value="teamLead"/>			








<digi:instance property="aimTeamActivitiesForm" />



<table width="100%" cellspacing="0" cellpadding="0" valign="top" align="left">

<tr><td width="100%" valign="top" align="left">

<jsp:include page="teamPagesHeader.jsp"  />

</td></tr>
<tr>
<td>

									<c:set var="selectedTab" value="6" scope="request"/>


									<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
										<tr>
											<td>
												<div class="breadcrump_cont">
													<span class="sec_name">
														<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>
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
													<span class="bread_sel"><digi:trn key="aim:activityList">Activity List</digi:trn></span>
												</div>
											</td>
										</tr>
										<tr>
											<td valign="top">
												<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
													
													
									<jsp:include page="teamSetupMenu.jsp"  />								
							
							
							
							
							
							<table class="inside" width="970" cellpadding="0" cellspacing="0">
								<tr>
							    <td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
							    	<b class="ins_title">
							    		<digi:trn key="aim:activityList">Activity List</digi:trn>
							    	</b>
							    </td>
								</tr>
								
								<logic:empty name="aimTeamActivitiesForm" property="activities">
									<tr>
										<td class="inside" align="center">
											<digi:trn key="aim:noActivitiesPresent">No activities present</digi:trn>
										</td>
									</tr>
								</logic:empty>
								<logic:notEmpty name="aimTeamActivitiesForm" property="activities">
									<logic:iterate name="aimTeamActivitiesForm" property="activities" id="activities">
										<tr>
											<td class="inside">
												<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
												<c:set target="${urlParams}" property="activityId">
													<bean:write name="activities" property="ampActivityId" />
												</c:set>
												<digi:link href="/getActivityIndicators.do" name="urlParams" styleClass="l_sm">
													<bean:write name="activities" property="name" />
												</digi:link>
											</td>
										</tr>
									</logic:iterate>
								</logic:notEmpty>
								<tr><td class="inside"><digi:errors/></td></tr>
							</table>
							
							<logic:notEmpty name="aimTeamActivitiesForm" property="pages">
								<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
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
												<c:set target="${urlParams1}" property="page">
													<%=pages%>
												</c:set>
												<digi:link href="/getTeamActivities.do" name="urlParams1" styleClass="l_sm">
													<%=pages%>
	                      </digi:link>
												<% } %>
												|
									</logic:iterate>
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

