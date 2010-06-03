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
<style>

.tableEven {
	background-color:#dbe5f1;
	font-size:8pt;
	padding:2px;
}

.tableOdd {
	background-color:#FFFFFF;
	font-size:8pt;!important
	padding:2px;
}
 
.Hovered {
	background-color:#a5bcf2;
}

</style>
<script language="javascript">
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
</script>


<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>

<c:set target="${bcparams}" property="tId" value="-1"/>

<c:set target="${bcparams}" property="dest" value="teamLead"/>			





<script type="text/javascript">



<!--





-->



</script>



<digi:instance property="aimTeamActivitiesForm" />



<table width="100%" cellSpacing=0 cellPadding=0 valign="top" align="left">

<tr><td width="100%" valign="top" align="left">

<jsp:include page="teamPagesHeader.jsp" flush="true" />

</td></tr>

<tr><td width="100%" valign="top" align="left">

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=780>

	<tr>

		<td width=14>&nbsp;</td>

		<td align=left class=r-dotted-lg vAlign=top width=750>



			<table cellPadding=5 cellSpacing=0 width="100%">

				<tr>

					<td height=33>
                    <span class=crumb>

						<c:set var="translation">

							<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>

						</c:set>

						<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >

							<digi:trn key="aim:portfolio">Portfolio</digi:trn>

						</digi:link>

						&nbsp;&gt;&nbsp;

						<c:set var="translation">

							<digi:trn key="aim:clickToViewTeamWorkspaceSetup">Click here view Team Workspace Setup</digi:trn>

						</c:set>

						<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="comment" title="${translation}" >

							<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>

						</digi:link>

						&nbsp;&gt;&nbsp;						

						<digi:trn key="aim:activityList">Activity List</digi:trn>
                      </span>

					</td>

				</tr>

				<tr>

					<td height=16 vAlign=center width=571><span class=subtitle-blue><digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></span>

					</td>

				</tr>

				<tr>

					<td noWrap width=571 vAlign="top">

						<table bgColor=#ffffff cellPadding=0 cellSpacing=0  width="100%">

							<tr >

								<td vAlign="top" width="100%">

									<c:set var="selectedTab" value="6" scope="request"/>

									<jsp:include page="teamSetupMenu.jsp" flush="true" />								

								</td>

							</tr>

							<tr bgColor=#f4f4f2>

								<td valign="top">

                                	<div class="contentbox_border" style="border-top:0px;padding: 20px 0px 20px 0px;">
									<div align="center">

									<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="98%">	

										<tr><td>

											<digi:errors />

										</td></tr>
										<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>

										<logic:notEmpty name="aimTeamActivitiesForm" property="pages">

											<tr>

												<td height="20">

														<logic:iterate name="aimTeamActivitiesForm" property="pages" id="pages" type="java.lang.Integer">
													  	<bean:define id="currPage" name="aimTeamActivitiesForm" property="currentPage" />
														<% if (currPage.equals(pages)) { %>
																<%=pages%>
														<%	} else { %>
															<c:set var="translation">
																<digi:trn key="aim:clickToViewNextPage">Click here to goto Next Page</digi:trn>
															</c:set>
															<c:set target="${urlParams1}" property="page">
																<%=pages%>
															</c:set>
															<digi:link href="/getTeamActivities.do" name="urlParams1">
																<%=pages%>
                                                            </digi:link>
														<% } %>
														|
														</logic:iterate>
												</td>

											</tr>

										</logic:notEmpty>											
										<tr>

											<td bgColor=#ffffff valign="top">

												<table border=0 cellPadding=0 cellSpacing=0 width="100%">

													<tr>

														<td align="left" width="100%" valign="center">

															<table width="100%" cellSpacing=0 cellPadding=2 vAlign="top" align="left"

															bgcolor="#ffffff">

																<tr><td valign="center" align="center" bgcolor="#999999" height="20" style="color:black">

																	<b><digi:trn key="aim:activityList">Activity List</digi:trn></b>

																</td></tr>

															</table>

														</td>

													</tr>

													<logic:empty name="aimTeamActivitiesForm" property="activities">

													<tr>

														<td align="center">

															<table width="100%" cellSpacing=2 cellPadding=3 vAlign="top" align="center"

															bgcolor="#f4f4f2">

																<tr><td bgcolor="#f4f4f2" align="center">

																	<digi:trn key="aim:noActivitiesPresent">

																		No activities present

																	</digi:trn>

																</td></tr>

															</table>														

														</td>

													</tr>	

													</logic:empty>

							

													<logic:notEmpty name="aimTeamActivitiesForm" property="activities">

													<tr>

														<td align="left" width="100%" valign="center">

															<table width="100%" cellSpacing=0 cellPadding=6 vAlign="top" align="left" bgcolor="#f4f4f2" id="dataTable">

															<logic:iterate name="aimTeamActivitiesForm" property="activities" id="activities" 

															>

																<tr bgcolor="#f4f4f2">

																<td width="7" valign="center">

																	<img src= "../ampTemplate/images/arrow_dark.gif" border=0>

																</td>

																<td>

																	<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>

																	<c:set target="${urlParams}" property="activityId">

																		<bean:write name="activities" property="ampActivityId" />

																	</c:set>

																	<digi:link href="/getActivityIndicators.do" name="urlParams">

																		<bean:write name="activities" property="name" />

																	</digi:link>

																</td></tr>

															</logic:iterate>

															</table>

														</td>

													</tr>													

													</logic:notEmpty>

												</table>

											</td>
										</tr>
                                        <tr>
	                                        <td>
                                        		<table style="float:left;" >

                                                    <logic:notEmpty name="aimTeamActivitiesForm" property="pages">
                                                        <tr>
                                                            <td>
                                                                    <logic:iterate name="aimTeamActivitiesForm" property="pages" id="pages" type="java.lang.Integer">
                                                                    <bean:define id="currPage" name="aimTeamActivitiesForm" property="currentPage" />
                                                                    <% if (currPage.equals(pages)) { %>
                                                                            <%=pages%>
                                                                    <%	} else { %>
                                                                        <c:set var="translation">
                                                                            <digi:trn key="aim:clickToViewNextPage">Click here to goto Next Page</digi:trn>
                                                                        </c:set>
                                                                        <c:set target="${urlParams1}" property="page">
                                                                            <%=pages%>
                                                                        </c:set>
                                                                        <digi:link href="/getTeamActivities.do" name="urlParams1">
                                                                            <%=pages%>
                                                                        </digi:link>
                                                                    <% } %>
                                                                    |
                                                                    </logic:iterate>
                                                            </td>
                                                        </tr>
                                                    </logic:notEmpty>											
												</table>
												<a style="float:right;cursor:pointer;" onclick="window.scrollTo(0,0); return false">
                                                <digi:trn key="aim:backtotop">Back to Top</digi:trn> <span style="font-size: 10pt; font-family: Tahoma;">&uarr;</span>
                                                </a>
                                            </td>
                                        </tr>
                                    </table>
									</div>
                                    </div>
								</td>

							</tr>

							<tr><td bgColor=#f4f4f2>&nbsp;

								

							</td></tr>

						</table>			

					</td>

				</tr>

			</table>

		</td>

	</tr>

</table>

</td></tr>

</table>







<script language="javascript">
setStripsTable("dataTable", "tableEven", "tableOdd");
setHoveredTable("dataTable", false);
</script>

