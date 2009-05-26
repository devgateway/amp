<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<style>
.contentbox_border{
	border:1px solid #666666;
	width:100%;	
	background-color: #f4f4f2;
	padding: 20 0 20 0;
}
</style>
<style>
.link{
	text-decoration: none;
	font-size: 8pt; font-family: Tahoma;
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

<c:set var="translationBan">
	<digi:trn key="um:confirmBanMsg">Do you really want to ban the user ?</digi:trn>
</c:set>

<c:set var="translationUnban">
	<digi:trn key="um:confirmUnbanMsg">Do you really want to remove the ban ?</digi:trn>
</c:set>

<script language="JavaScript">


function banUser(txt) {
  var ban=confirm("${translationBan}");
  return ban;
  }
  
 function unbanUser(txt) {
  var ban=confirm("${translationUnban}");
  return ban;
  }
  
  
  function searchAlpha(val) {
		     document.umViewAllUsersForm.action = "/um/viewAllUsers.do?currentAlpha="+val;
		     document.umViewAllUsersForm.submit();
			 return true;		
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

</script>

<digi:instance property="umViewAllUsersForm" />
<digi:context name="digiContext" property="context" />

<!--  AMP Admin Logo -->
<!-- jsp:include page="/repositoryteamPagesHeader.jsp" flush="true" /-->
<!-- End of Logo -->

<digi:form action="/viewAllUsers.do" method="post">
  <table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=757>
    <tr>
      <td class=r-dotted-lg width=14>&nbsp;</td>
      <td align=left class=r-dotted-lg vAlign=top>
        <table cellPadding=5 cellSpacing=0 width="100%">
          <tr>
            <!-- Start Navigation -->
            <td height=33>
              <span class=crumb>
                <c:set var="translation">
                  <digi:trn key="um:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
                </c:set>
                <digi:link module="aim" href="/admin.do" styleClass="comment" title="${translation}" >
                  <digi:trn key="um:AmpAdminHome">
                  Admin Home
                  </digi:trn>
                </digi:link>&nbsp;&gt;&nbsp;

                <digi:trn key="um:users">
                Users
                </digi:trn>
              </span>
            </td>
            <!-- End navigation -->
          </tr>
          <tr>
            <td>
              <digi:errors/> 
              <span class=subtitle-blue>
                <digi:trn key="um:viewAllUsers:ListOfUsers">
                List of users
                </digi:trn>
              </span>
            </td>
          </tr>
          <tr style="width:100%;">
			<td>
			<table>
          	<c:choose>
          		<c:when test="${umViewAllUsersForm.showBanned}">
          		<td width="200">
		              <digi:trn key="um:viewAllUsers:filter">
		              Filter by:
		              </digi:trn>
		              <html:select property="type" style="font-family:verdana;font-size:11px;" disabled="true">
		                <c:set var="translation">
		                  <digi:trn key="um:viewAllUsers:all">
		                  -All-
		                  </digi:trn>
		                </c:set>
		                <html:option value="-1">${translation}</html:option>
		              </html:select>
	              </td>
          		</c:when>
          		<c:otherwise>
	            <td width="200">
		              <digi:trn key="um:viewAllUsers:filter">
		              Filter by:
		              </digi:trn>
		              <html:select property="type" style="font-family:verdana;font-size:11px;" >
		                <c:set var="translation">
		                  <digi:trn key="um:viewAllUsers:all">
		                  -All-
		                  </digi:trn>
		                </c:set>
		                <html:option value="-1">${translation}</html:option>
		
		                <c:set var="translation">
		                  <digi:trn key="um:viewAllUsers:regisetred">
		                  Registered
		                  </digi:trn>
		                </c:set>
		                <html:option value="0">${translation}</html:option>
		
		                <c:set var="translation">
		                  <digi:trn key="um:viewAllUsers:workspaceMembers">
		                  Workspace members
		                  </digi:trn>
		                </c:set>
		                <html:option value="1">${translation}</html:option>	                
		              </html:select>
	              </td>
	              </c:otherwise>
              </c:choose>
				<logic:notEmpty name="umViewAllUsersForm" property="alphaPages">
				<td width="120" align="left">
					<table>
						<tr>
						   <digi:form action="/viewAllUsers.do" method="post">
							<td align="left">
								<digi:trn>Go to</digi:trn>

								<html:select property="currentAlpha" style="font-family:verdana;font-size:11px;" onchange="document.umViewAllUsersForm.submit()">
									<html:option value="viewAll">-All-</html:option>
									<logic:iterate name="umViewAllUsersForm" property="alphaPages" id="alphaPages" type="java.lang.String">
										<c:if test="${alphaPages != null}">
											<html:option value="<%=alphaPages %>"><%=alphaPages %></html:option>
										</c:if>
									</logic:iterate>
								</html:select>
							</td>
							</digi:form>
				 	</tr>
				</table>
			</td>
			</logic:notEmpty>									
										
              <td width="200">
	              <digi:trn key="um:viewAllUsers:keyword">
	              keyword:
	              </digi:trn>
	              <html:text property="keyword" style="font-family:verdana;font-size:11px;"/>
              </td>
              
			  <td width="330">
	              <c:set var="translation">
	                <digi:trn key="um:viewAllUsers:showButton">
	                Show
	                </digi:trn>
	              </c:set>
	              <input type="submit" value="${translation}"  class="dr-menu" style="font-family:verdana;font-size:11px;" />
            </td>
			</table>
			</td>
          </tr>
          <tr>
            <td noWrap width=967 vAlign="top" colspan="7">
              <table width="100%" cellspacing=1 cellSpacing=1>
					<tr>
						<td noWrap width=700 vAlign="top">
							<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
								
								<tr bgColor=#f4f4f2>
									<td valign="top">
										<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" border=0>
											<tr>
													
														<tr>
															<td width="100%" >
															<div style="width:100%;height:auto;max-height:250px;vertical-align: center;align: center;">
															  <div style="padding:5px;font-size:12px;color:Black;background-color: #999999;align: center;">
															  	<table cellpadding="0" cellspacing="0">
																	<tr>
																			<td style="width:210px;padding:3px;color:Black;background-color: #999999;align: center;text-align:center;">
																		  	<digi:link style="color:black;align: center;" href="/viewAllUsers.do?sortBy=name&reset=false"><b>
																					<digi:trn key="um:viewAllUsers:UsersNames">Name</digi:trn></b>
																				</digi:link>
																		  </td><td style="width:210px;padding:3px;color:Black;background-color: #999999;align: center;text-align:center;">
																		  	<digi:link style="color:black;align: center;" href="/viewAllUsers.do?sortBy=email&reset=false"><b>
																					<digi:trn key="um:viewAllUsers:UsersEmails">Email</digi:trn></b>
																				</digi:link>
																		  </td><td style="width:210px;padding:3px;color:Black;background-color: #999999;align: center;text-align:center;">
																		  	<b><digi:trn key="um:viewAllUsers:UserWorkspace">Workspace</digi:trn></b>
																		  </td><td style="width:70px;padding:3px;color:Black;background-color: #999999;align: center;text-align:center;">
																		  	<b><digi:trn key="aim:viewAllUsers:action">Actions</digi:trn></b>
																		  </td>
																		</tr>
																	</table>
																</div>
															  <div style="overflow:auto;width:auto;height:auto;max-height:220px;">
															    <c:if test="${empty umViewAllUsersForm.pagedUsers}">
								                                         <tr>
																			<td colspan="5">
				                                                   				<b><digi:trn key="um:viewAllUsers:NoUsers">No users present</digi:trn>
				                                                       			</b>
																			</td>
																		</tr>
																</c:if>
															    <c:if test="${not empty umViewAllUsersForm.pagedUsers}">
															      <table width="100%" height="100%" border="0" align=center cellPadding=0 cellSpacing=0  id="dataTable">
															        <c:forEach var="us" items="${umViewAllUsersForm.pagedUsers}">
															          <tr>
															            <td style="padding:2px;width:220px;text-align:left;">
															              <div style="white-space: nowrap;">
																			${us.firstNames}&nbsp;${us.lastName}
															              </div>
															            </td>
															            <td style="padding:5px;width:115px;text-align:left;" nowrap="nowrap">
															             <div style="white-space: nowrap;">${us.email}</div> 
															            </td>
															            <td style="padding:2px;width:200px;text-align:left;">
															              <div>
								                                                  <c:if test="${!empty us.teamMembers}">
	                                                                                  <c:forEach var="member" items="${us.teamMembers}">
	                                                                                           <li> ${member.ampTeam.name}&nbsp;(${member.ampMemberRole.role})&nbsp;&nbsp;</li>
	                                                                                  </c:forEach>
                                                                             	  </c:if>
								                                                  <c:if test="${empty us.teamMembers}">
								                                            		<digi:trn key="um:viewAllUsers:UnassignedUser">Unassigned</digi:trn>
								                                                  </c:if>
								                                                </div>
															            </td>
																		<td>
															              <c:set var="translation">
								                                                  <digi:trn key="um:viewAllUsers:EditUserLink">Edit user </digi:trn>
								                                                </c:set>
								                                                <digi:link href="/viewEditUser.do?id=${us.id}">${translation}</digi:link>
																			</td>
																			
																			<td>
																				<c:choose>
								                                                  <c:when test="${us.ban}">
								                                                    <c:set var="translation">
								                                                      <digi:trn key="um:viewAllUsers:unBanUserLink">Remove ban </digi:trn>
								                                                    </c:set>
								                                                    <digi:link href="/viewEditUser.do?id=${us.id}&ban=false" onclick="return unbanUser()"  >${translation}</digi:link>
								                                                  </c:when>
								                                                  <c:otherwise>
																			  <c:if test="${!(us.buildInAdmin)}">  <%--  AMP-4598 Build in administrator can not be banned. --%>
								                                                    <c:set var="translation">
								                                                      <digi:trn key="um:viewAllUsers:banUsersLink">Ban user </digi:trn>
								                                                    </c:set>
								
								                                                    <digi:link href="/viewEditUser.do?id=${us.id}&ban=true" onclick="return banUser()">${translation}</digi:link>
								                                              </c:if>
								                                                  </c:otherwise>
								                                                </c:choose>
															            </td>
															          </tr>
															        </c:forEach>
															      </table>
															    </c:if>
															  </div>
															</div>
														</td>
													</tr>
												
												 <!-- end page logic -->
											</td>
									</tr>
									
								</table>
							</td>
						</tr>
					                    </table>
					                  </td>
									<td noWrap width=100% vAlign="top">
										<table align=center cellPadding=0 cellSpacing=0 width="90%" border=0>
											<tr>
												<td>
													<!-- Other Links -->
													<table cellPadding=0 cellSpacing=0 width=100>
														<tr>
															<td bgColor=#c9c9c7 class=box-title>
																<digi:trn key="aim:otherLinks">
																Other links
																</digi:trn>
															</td>
															<td background="module/aim/images/corner-r.gif" 	height="17" width=17>
															&nbsp;
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td bgColor=#ffffff class=box-border>
													<table cellPadding=5 cellSpacing=1 width="100%">
														<tr>
															<td>
																<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
															</td>
															<td>
																<digi:link module="aim"  href="/../um/addUser.do">
											 					<digi:trn key="aim:addNewUser">
																Add new user																</digi:trn>
																</digi:link>
															</td>
														</tr>																								
														<tr>
															<td>
																<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>
															<td>
																<digi:link module="aim"  href="/admin.do">
																<digi:trn key="aim:AmpAdminHome">
																Admin Home
																</digi:trn>
																</digi:link>
															</td>
														</tr>
														<tr>
															<td>
																<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>
															<td>
																<digi:link  module="aim" href="/workspaceManager.do~page=1">
																<digi:trn key="aim:WorkspaceManager">
																Workspace Manager
																</digi:trn>
																</digi:link>
															</td>
														</tr>
														<c:choose>
														<c:when test="${umViewAllUsersForm.showBanned}">
															<tr>
																<td>
																	<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>
																<td>
																	<digi:link  module="aim" href="/../um/viewAllUsers.do~showBanned=false">
																	<digi:trn key="aim:ViewActiveUsers">
																	View Active Users
																	</digi:trn>
																	</digi:link>
																</td>
															</tr>
														</c:when>
														<c:otherwise>
															<tr>
																<td>
																	<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>
																<td>
																	<digi:link  module="aim" href="/../um/viewAllUsers.do~showBanned=true">
																	<digi:trn key="aim:ViewBannedUsers">
																	View Banned Users
																	</digi:trn>
																	</digi:link>
																</td>
															</tr>
														</c:otherwise>
														</c:choose>
														<!-- end of other links -->
													</table>
												</td>
											</tr>
										</table>
									</td>
				                </tr>
							</table>
						</td>
					</tr>
					 <!-- page logic for pagination -->
					
						<tr style="width:50%;">
							<logic:notEmpty name="umViewAllUsersForm" property="pages">
								<td>
								<table style="padding:5px;">
									<tr>
										<td>
											<c:if test="${umViewAllUsersForm.currentPage > 1}">
												<td style="padding:3px;border:1px solid #999999">
													<jsp:useBean id="urlParamsFirst" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${urlParamsFirst}" property="page" value="1"/>
													<c:set var="translation">
														<digi:trn key="aim:firstpage">First Page</digi:trn>
													</c:set>
													<digi:link href="/userSearch.do"  style="text-decoration=none" name="urlParamsFirst" title="${translation}"  >
														&lt;&lt;
													</digi:link>
												</td>
												<td style="padding:3px;border:1px solid #999999">
													<jsp:useBean id="urlParamsPrevious" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${urlParamsPrevious}" property="page" value="${umViewAllUsersForm.currentPage -1}"/>
														<c:set var="translation">
														<digi:trn key="aim:previouspage">Previous Page</digi:trn>
														</c:set>
													<digi:link href="/userSearch.do" name="urlParamsPrevious" style="text-decoration=none" title="${translation}" >
														&lt;
													</digi:link>
												</td>
												<c:if test="${umViewAllUsersForm.offset > 0}">
													<c:if test="${umViewAllUsersForm.offset > 10}">
														<td style="padding:3px;border:1px solid #999999;" nowrap="nowrap">
														<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParams2}" property="page" value="${umViewAllUsersForm.currentPage -10}"/>
															<c:set var="translation">
															<digi:trn key="aim:previouspages">Previous pages</digi:trn>
															</c:set>
														<digi:link href="/userSearch.do" name="urlParams2" style="text-decoration=none" title="${translation}" >
															<digi:trn key="aim:previous">Previous</digi:trn> 10
														</digi:link>
														</td>
													</c:if>	
													<c:if test="${umViewAllUsersForm.offset <= 10}">
														<td style="padding:3px;border:1px solid #999999;" nowrap="nowrap">
														<c:set target="${urlParamsFirst}" property="page" value="1"/>
															<c:set var="translation">
															<digi:trn key="aim:previouspages">Previous pages</digi:trn>
															</c:set>
														<digi:link href="/userSearch.do" name="urlParamsFirst" style="text-decoration=none" title="${translation}" >
															<digi:trn key="aim:previous">Previous</digi:trn> ${umViewAllUsersForm.offset}
														</digi:link>
														</td>
													</c:if>
												</c:if>
											</c:if>
										
											<c:set var="length" value="${umViewAllUsersForm.pagesToShow}"></c:set>
											<c:set var="start" value="${umViewAllUsersForm.offset}"/>
											<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
											<logic:iterate name="umViewAllUsersForm" property="pages" id="pages" type="java.lang.Integer" offset="${start}" length="${length}">	
												<c:set target="${urlParams1}" property="page"><%=pages%>
												</c:set>
												<c:set target="${urlParams1}" property="orgSelReset" value="false"/>
												<c:if test="${umViewAllUsersForm.currentPage == pages}">
													<td style="padding:3px;border:2px solid #333333">
														<font color="#FF0000"><%=pages%></font>
													</td>
												</c:if>
												<c:if test="${umViewAllUsersForm.currentPage != pages}">
													<td style="padding:3px;border:1px solid #999999">
													<c:set var="translation">
													<digi:trn key="aim:clickToViewNextPage">Click here to go to page #</digi:trn><%=pages%>
													</c:set>
													<digi:link href="/userSearch.do" name="urlParams1" title="${translation}" >
														<%=pages%>
													</digi:link>
													</td>
												</c:if>
											</logic:iterate>	
										
											<c:if test="${umViewAllUsersForm.currentPage != umViewAllUsersForm.pagesSize}">
												<c:if test="${umViewAllUsersForm.offset + umViewAllUsersForm.pagesToShow < umViewAllUsersForm.pagesSize}">
													<c:if test="${umViewAllUsersForm.offset + umViewAllUsersForm.pagesToShow +10 <= umViewAllUsersForm.pagesSize}">
														<td style="padding:3px;border:1px solid #999999;" nowrap="nowrap">
														<c:set target="${urlParams1}" property="page" value="${umViewAllUsersForm.currentPage +10}"/>
															<c:set var="translation">
															<digi:trn key="aim:nextpages">Next pages</digi:trn>
															</c:set>
														<digi:link href="/userSearch.do" name="urlParams1" style="text-decoration=none" title="${translation}" >
															<digi:trn key="aim:next">Next</digi:trn> 10
														</digi:link>
														</td>
													</c:if>
													<c:if test="${umViewAllUsersForm.offset + umViewAllUsersForm.pagesToShow +10 > umViewAllUsersForm.pagesSize}">
														<td style="padding:3px;border:1px solid #999999;" nowrap="nowrap">
														<c:set target="${urlParams1}" property="page" value="${umViewAllUsersForm.pagesSize}"/>
															<c:set var="translation">
															<digi:trn key="aim:nextpages">Next pages</digi:trn>
															</c:set>
														<digi:link href="/userSearch.do" name="urlParams1" style="text-decoration=none" title="${translation}" >
															<digi:trn key="aim:next">Next</digi:trn> ${umViewAllUsersForm.pagesSize - (umViewAllUsersForm.offset + umViewAllUsersForm.pagesToShow)}
														</digi:link>
														</td>
													</c:if>
												</c:if>
												<td style="padding:3px;border:1px solid #999999">
													<jsp:useBean id="urlParamsNext" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${urlParamsNext}" property="page" value="${umViewAllUsersForm.currentPage+1}"/>
													<c:set target="${urlParamsNext}" property="orgSelReset" value="false"/>
														<c:set var="translation">
														<digi:trn key="aim:nextpage">Next Page</digi:trn>
														</c:set>
													<digi:link href="/userSearch.do"  style="text-decoration=none" name="urlParamsNext" title="${translation}"  >
														&gt;
													</digi:link>
												</td>
												<td style="padding:3px;border:1px solid #999999">
													<jsp:useBean id="urlParamsLast" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${urlParamsLast}" property="page" value="${umViewAllUsersForm.pagesSize}"/>
													<c:set target="${urlParamsLast}" property="orgSelReset" value="false"/>
														<c:set var="translation">
														<digi:trn key="aim:lastpage">Last Page</digi:trn>
														</c:set>
													<digi:link href="/userSearch.do"  style="text-decoration=none" name="urlParamsLast" title="${translation}">
														&gt;&gt; 
													</digi:link>
												</td>
											</c:if>
											<td style="padding:3px;border:1px solid #999999">
												&nbsp;<digi:trn key="aim:page">Page</digi:trn>&nbsp;<c:out value="${umViewAllUsersForm.currentPage}"></c:out>&nbsp;<digi:trn key="aim:of">of</digi:trn>&nbsp;<c:out value="${umViewAllUsersForm.pagesSize}"></c:out>
											</td>
											<td style="padding:3px;border:1px solid #FFFFFF">
											</td>
											<td style="padding:3px;border:1px solid #999999">
												<c:out value="${umViewAllUsersForm.numUsers}"></c:out>&nbsp;<digi:trn key="aim:records">records</digi:trn>
											</td>						
									</td>	
									<td width="200" align="right" nowrap="nowrap">
						              	<digi:trn key="aim:results">Results</digi:trn>&nbsp;
										<html:select property="tempNumResults" styleClass="inp-text">
											<html:option value="10">10</html:option>
											<html:option value="20">20</html:option>
											<html:option value="50">50</html:option>
											<html:option value="-1">-All-</html:option>
										</html:select>
					              	</td>
								</tr>	
							</table>
							</td>
						</logic:notEmpty>
					</tr>
				</table>
			</td>
		</tr>
	</table>
<script language="javascript">
	setStripsTable("dataTable", "tableEven", "tableOdd");
	setHoveredTable("dataTable", false);
</script>
</digi:form>


