<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<c:set var="translationBan">
	<digi:trn jsFriendly="true" key="um:confirmBanMsg">Do you really want to ban the user ?</digi:trn>
</c:set>

<c:set var="translationUnban">
	<digi:trn jsFriendly="true" key="um:confirmUnbanMsg">Do you really want to remove the ban ?</digi:trn>
</c:set>

<script language="JavaScript">


function banUser(txt) {
  var ban=confirm('${translationBan}');
  return ban;
  }
  
 function unbanUser(txt) {
  var ban=confirm('${translationUnban}');
  return ban;
  }
  
  
  function searchAlpha(val) {
		     document.umViewAllUsersForm.action = "/um/viewAllUsers.do?currentAlpha="+val;
		     document.umViewAllUsersForm.target="_self";
		     document.umViewAllUsersForm.submit();
			 return true;		
	}
  function exportXSL(){
      <digi:context name="exportUrl" property="context/module/moduleinstance/exportUserManager.do"/>;
      document.umViewAllUsersForm.action="${exportUrl}";
      document.umViewAllUsersForm.target="_blank";
      document.umViewAllUsersForm.submit();
}
 function showUsers(){
      <digi:context name="showUsr" property="context/module/moduleinstance/viewAllUsers.do"/>;
      document.umViewAllUsersForm.action="${showUsr}";
      document.umViewAllUsersForm.target="_self";
      document.umViewAllUsersForm.submit();
}

 function resetUsers() {
     document.umViewAllUsersForm.action="/um/viewAllUsers.do?currentAlpha=viewAll&reset=true";
     document.umViewAllUsersForm.target="_self";
     document.umViewAllUsersForm.submit();
}
</script>



<!--  AMP Admin Logo -->
<!-- jsp:include page="/repositoryteamPagesHeader.jsp"  /-->
<!-- End of Logo -->
<digi:instance property="umViewAllUsersForm" />
<digi:context name="digiContext" property="context" />
<digi:form action="/viewAllUsers.do" method="post" >
<h1 class="admintitle"><digi:trn>User manager</digi:trn></h1>

<table width="1000" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width=680>
					<table>
					<tr>
						<td width=700 vAlign="top" colspan="7"
							style="border-left: 1px solid #D0D0D0; border-right: 1px solid #D0D0D0; border-bottom: 1px solid #D0D0D0;">
							<table width="100%" cellspacing="1" cellspacing="1">
								<tr>
									<td noWrap width=700 vAlign="top">
									<table cellPadding=5 cellspacing="0" width="100%" style="margin-bottom:15px;">
					<!--       <tr>
            <td height=33 colspan=8>
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
          </tr> -->
					<tr>
						<td bgcolor=#c7d4db align=center colspan=8
							style="background-color: #F2F2F2; border: 1px solid #D0D0D0; padding: 5px;">
							<digi:errors /> <span style="font-size: 12px; font-weight: bold;">
								<digi:trn key="um:viewAllUsers:ListOfUsers">
                List of users 
                </digi:trn> </span></td>
					</tr>
					<tr>
						<td align="left" colspan=8>
								<jsp:include
									page="/repository/aim/view/adminXSLExportToolbar.jsp" />
						</td>
					</tr>
					<tr style="background-color: #F2F2F2; padding: 5px;">
						<c:choose>
							<c:when test="${umViewAllUsersForm.showBanned}">
								<td width="160" style="border-left: 1px solid #D0D0D0; border-top: 1px solid #D0D0D0; border-bottom: 1px solid #D0D0D0;">
									<digi:trn key="um:viewAllUsers:filter">
		              Filter by:
		              </digi:trn> <html:select property="type"
										style="font-family:verdana;font-size:11px;" disabled="true">
										<c:set var="translation">
											<digi:trn key="um:viewAllUsers:all">
		                  -All-
		                  </digi:trn>
										</c:set>
										<html:option value="-1">${translation}</html:option>
									</html:select></td>
							</c:when>
							<c:otherwise>
								<td width="50px" class="usersSelectForm"
									style="border-left: 1px solid #D0D0D0; border-top: 1px solid #D0D0D0; border-bottom: 1px solid #D0D0D0;">
									<digi:trn key="um:viewAllUsers:filter">Filter by:</digi:trn></td>

								<td width="40px" class="usersSelectForm"
									style="border-top: 1px solid #D0D0D0; border-bottom: 1px solid #D0D0D0;">
									<html:select property="type"
										style="font-family:verdana;font-size:11px;">
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
									</html:select></td>
							</c:otherwise>
						</c:choose>
						<td width="50px" class="usersSelectForm"
							style="border-top: 1px solid #D0D0D0; border-bottom: 1px solid #D0D0D0;">
							<digi:trn key="um:viewAllUsers:keyword">keyword:</digi:trn></td>
						<td width="50px"
							style="border-top: 1px solid #D0D0D0; border-bottom: 1px solid #D0D0D0;">
							<html:text property="keyword"
								style="font-family:verdana;font-size:11px;" /></td>
						<td width="40px" align="left" class="usersSelectForm"
							style="border-top: 1px solid #D0D0D0; border-bottom: 1px solid #D0D0D0;">
							<digi:trn key="aim:results">Results</digi:trn>&nbsp;</td>
						<td width="50px"
							style="border-top: 1px solid #D0D0D0; border-bottom: 1px solid #D0D0D0;">
							<html:select property="tempNumResults" styleClass="inp-text">
								<html:option value="10"><digi:easternArabicNumber>10</digi:easternArabicNumber></html:option>
								<html:option value="20"><digi:easternArabicNumber>20</digi:easternArabicNumber></html:option>
								<html:option value="50"><digi:easternArabicNumber>50</digi:easternArabicNumber></html:option>
								<html:option value="-1"><digi:trn>ALL</digi:trn></html:option>
							</html:select></td>
						<td width="300px"
							style="border-top: 1px solid #D0D0D0; border-bottom: 1px solid #D0D0D0; border-right: 1px solid #D0D0D0;">
							<c:set var="translation">
								<digi:trn key="um:viewAllUsers:showButton">Show</digi:trn>
							</c:set> <input type="button" value="${translation}" class="dr-menu"
							style="font-family: verdana; font-size: 11px;" onclick="showUsers()" />
							
							<c:set var="translation">
								<digi:trn key="um:viewAllUsers:resetButton">Reset</digi:trn>
							</c:set> <input type="button" value="${translation}" class="dr-menu"
							style="font-family: verdana; font-size: 11px;"  onclick="resetUsers()"  />
							
							</td>
							
							
					</tr>
					</table>
										<table bgColor=#ffffff cellpadding="0" cellspacing="0"
											width="100%">
											<tr bgColor=#f4f4f2>
												<td valign="top">
													<table align="center" bgColor=#f4f4f2 cellpadding="0"
														cellspacing="0" width="90%" border="0">
														<tr>
															<td bgColor=#ffffff>
																<table border="0" cellpadding="0" cellspacing="0"
																	width="100%">
																	<tr bgColor=#dddddb>
																		<!-- header -->
																		<td bgColor=#f2f2f2 height="25" align="center"
																			colspan="5" style="border: 1px solid #ebebeb;"><B>
																				<digi:trn key="um:users">Users</digi:trn> </b></td>
																		<!-- end header -->
																	</tr>
																	<tr>
																		<td width="100%" class="report">
																			<table width="734" RULES=ALL FRAME=VOID
																				id="viewAllUsers" border="0" cellspacing="1"
																				cellpadding="1">

																				<c:if test="${empty umViewAllUsersForm.pagedUsers}">
																					<tr>
																						<td colspan="5"><b><digi:trn
																									key="um:viewAllUsers:NoUsers">No users present</digi:trn>
																						</b></td>
																					</tr>
																				</c:if>
																				<c:if
																					test="${not empty umViewAllUsersForm.pagedUsers}">
																					<thead
																						background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif">
																						<tr>
																							<td height="30" width="220"><c:if
																									test="${not empty umViewAllUsersForm.sortBy && umViewAllUsersForm.sortBy!='nameAscending'}">
																									<digi:link
																										href="/viewAllUsers.do?sortBy=nameAscending&reset=false">
																										<b><digi:trn
																												key="um:viewAllUsers:UsersNames">Name</digi:trn></b>
																									</digi:link>
																								</c:if> <c:if
																									test="${empty umViewAllUsersForm.sortBy || umViewAllUsersForm.sortBy=='nameAscending'}">
																									<digi:link
																										href="/viewAllUsers.do?sortBy=nameDescending&reset=false">
																										<b><digi:trn
																												key="um:viewAllUsers:UsersNames">Name</digi:trn></b>
																									</digi:link>
																								</c:if> <c:if
																									test="${not empty umViewAllUsersForm.sortBy && umViewAllUsersForm.sortBy=='nameAscending'}">
																									<img src="/repository/aim/images/up.gif" />
																								</c:if> <c:if
																									test="${not empty umViewAllUsersForm.sortBy && umViewAllUsersForm.sortBy=='nameDescending'}">
																									<img src="/repository/aim/images/down.gif" />
																								</c:if></td>
																							<td height="30" width="220"><c:if
																									test="${not empty umViewAllUsersForm.sortBy && umViewAllUsersForm.sortBy!='emailAscending'}">
																									<digi:link
																										href="/viewAllUsers.do?sortBy=emailAscending&reset=false">
																										<b><digi:trn
																												key="um:viewAllUsers:UsersEmails">Email</digi:trn></b>
																									</digi:link>
																								</c:if> <c:if
																									test="${empty umViewAllUsersForm.sortBy || umViewAllUsersForm.sortBy=='emailAscending'}">
																									<digi:link
																										href="/viewAllUsers.do?sortBy=emailDescending&reset=false">
																										<b><digi:trn
																												key="um:viewAllUsers:UsersEmails">Email</digi:trn></b>
																									</digi:link>
																								</c:if> <c:if
																									test="${not empty umViewAllUsersForm.sortBy && umViewAllUsersForm.sortBy=='emailAscending'}">
																									<img src="/repository/aim/images/up.gif" />
																								</c:if> <c:if
																									test="${not empty umViewAllUsersForm.sortBy && umViewAllUsersForm.sortBy=='emailDescending'}">
																									<img src="/repository/aim/images/down.gif" />
																								</c:if></td>
																							<td height="30" width="220"><b> <digi:trn
																										key="um:viewAllUsers:UserWorkspace">Workspace</digi:trn>
																							</b></td>
																							<td height="30" width="150" colspan="3"
																								class="ignore"><b> <digi:trn
																										key="aim:viewAllUsers:action">Actions</digi:trn>
																							</b></td>
																						</tr>
																					</thead>

																					<tbody class="yui-dt-data">
																						<c:forEach var="us"
																							items="${umViewAllUsersForm.pagedUsers}">
																							<tr>
																								<td height="30">
																									 <c:out value="${us.firstNames}"/>&nbsp;<c:out value="${us.lastName}"/></td>
																								<td height="30"><c:out value="${us.email}"/></td>
																								<td height="30">
																									<div>
																										<c:if test="${!empty us.teamMembers}">
																											<table>
																												<c:forEach var="member"
																													items="${us.teamMembers}">
																													<tr>
																														<td nowrap><li>
                                                                                                                                                                                                        <c:out value="${member.ampTeam.name}"/>&nbsp;( <digi:trn><c:out value="${member.ampMemberRole.role}"/></digi:trn>)&nbsp;&nbsp;</li>
																														</td>

																													</tr>
																												</c:forEach>
																											</table>
																										</c:if>
																										<c:if test="${empty us.teamMembers}">
																											<digi:trn
																												key="um:viewAllUsers:UnassignedUser">Unassigned</digi:trn>
																										</c:if>
																									</div></td>
																								<td height="30" nowrap="nowrap" class="ignore">
																									<c:set var="translation">
																										<digi:trn key="um:viewAllUsers:EditUserLink">Edit user </digi:trn>
																									</c:set> <digi:link href="/viewEditUser.do?id=${us.id}">${translation}</digi:link>
																								</td>

																								<td height="30" nowrap="nowrap" class="ignore">
																									<c:choose>
																										<c:when test="${us.ban}">
																											<c:set var="translation">
																												<digi:trn
																													key="um:viewAllUsers:unBanUserLink">Remove ban </digi:trn>
																											</c:set>
																											<digi:link
																												href="/viewEditUser.do?id=${us.id}&ban=false"
																												onclick="return unbanUser()">${translation}</digi:link>
																										</c:when>
																										<c:otherwise>
																											<c:set var="translation">
																												<digi:trn key="um:viewAllUsers:banUsersLink">Ban user </digi:trn>
																											</c:set>

																											<digi:link
																												href="/viewEditUser.do?id=${us.id}&ban=true"
																												onclick="return banUser()">${translation}</digi:link>
																										</c:otherwise>
																									</c:choose></td>
																							</tr>
																						</c:forEach>
																					</tbody>
																				</c:if>
																			</table></td>
																	</tr>
																	<!-- end page logic -->

																	<!-- page logic for pagination -->
																	<logic:notEmpty name="umViewAllUsersForm"
																		property="pages">
																		<tr>
																			<td colspan="4" nowrap="nowrap"><digi:trn
																					key="um:userPages">Pages:</digi:trn> <c:if
																					test="${umViewAllUsersForm.currentPage > 1}">
																					<jsp:useBean id="urlParamsFirst"
																						type="java.util.Map" class="java.util.HashMap" />
																					<c:set target="${urlParamsFirst}" property="page"
																						value="1" />
																					<c:set var="translation">
																						<digi:trn key="aim:firstpage">First Page</digi:trn>
																					</c:set>

																					<digi:link href="/userSearch.do"
																						style="text-decoration=none" name="urlParamsFirst"
																						title="${translation}">
															&lt;&lt;
														</digi:link>

																					<jsp:useBean id="urlParamsPrevious"
																						type="java.util.Map" class="java.util.HashMap" />
																					<c:set target="${urlParamsPrevious}"
																						property="page"
																						value="${umViewAllUsersForm.currentPage -1}" />
																					<c:set var="translation">
																						<digi:trn key="aim:previouspage">Previous Page</digi:trn>
																					</c:set>
																					<digi:link href="/userSearch.do"
																						name="urlParamsPrevious"
																						style="text-decoration=none"
																						title="${translation}">
															&lt;
														</digi:link>
																				</c:if> <c:set var="length"
																					value="${umViewAllUsersForm.pagesToShow}"></c:set>
																				<c:set var="start"
																					value="${umViewAllUsersForm.offset}" /> <logic:iterate
																					name="umViewAllUsersForm" property="pages"
																					id="pages" type="java.lang.Integer"
																					offset="${start}" length="${length}">
																					<jsp:useBean id="urlParams1" type="java.util.Map"
																						class="java.util.HashMap" />
																					<c:set target="${urlParams1}" property="page"><%=pages%>
																					</c:set>
																					<c:set target="${urlParams1}"
																						property="orgSelReset" value="false" />
																					<c:if
																						test="${umViewAllUsersForm.currentPage == pages}">
																						<font color="#FF0000"><digi:easternArabicNumber><%=pages%></digi:easternArabicNumber></font>
																					</c:if>
																					<c:if
																						test="${umViewAllUsersForm.currentPage != pages}">
																						<c:set var="translation">
																							<digi:trn key="aim:clickToViewNextPage">Click here to go to Next Page</digi:trn>
																						</c:set>
																						<digi:link href="/userSearch.do" name="urlParams1"
																							title="${translation}">
																						<digi:easternArabicNumber><%=pages%></digi:easternArabicNumber>
																						</digi:link>
																					</c:if>
														|&nbsp;
														</logic:iterate> <c:if
																					test="${umViewAllUsersForm.currentPage != umViewAllUsersForm.pagesSize}">
																					<jsp:useBean id="urlParamsNext"
																						type="java.util.Map" class="java.util.HashMap" />
																					<c:set target="${urlParamsNext}" property="page"
																						value="${umViewAllUsersForm.currentPage+1}" />
																					<c:set target="${urlParamsNext}"
																						property="orgSelReset" value="false" />
																					<c:set var="translation">
																						<digi:trn key="aim:nextpage">Next Page</digi:trn>
																					</c:set>
																					<digi:link href="/userSearch.do"
																						style="text-decoration=none" name="urlParamsNext"
																						title="${translation}">
															&gt;
														</digi:link>
																					<jsp:useBean id="urlParamsLast"
																						type="java.util.Map" class="java.util.HashMap" />
																					<c:if
																						test="${umViewAllUsersForm.pagesSize > aimOrgManagerForm.pagesToShow}">
																						<c:set target="${urlParamsLast}" property="page"
																							value="${umViewAllUsersForm.pagesSize-1}" />
																					</c:if>
																					<c:if
																						test="${umViewAllUsersForm.pagesSize <umViewAllUsersForm.pagesToShow}">
																						<c:set target="${urlParamsLast}" property="page"
																							value="${umViewAllUsersForm.pagesSize}" />
																					</c:if>
																					<c:set target="${urlParamsLast}"
																						property="orgSelReset" value="false" />
																					<c:set target="${urlParamsLast}" property="page"
																						value="-1" />
																					<c:set var="translation">
																						<digi:trn key="aim:lastpage">Last Page</digi:trn>
																					</c:set>
																					<digi:link href="/userSearch.do"
																						style="text-decoration=none" name="urlParamsLast"
																						title="${translation}">
															&gt;&gt; 
														</digi:link>
																				</c:if> &nbsp; <digi:easternArabicNumber><c:out
																					value="${umViewAllUsersForm.currentPage}"></c:out></digi:easternArabicNumber>&nbsp;<digi:trn
																					key="aim:of">of</digi:trn>&nbsp;<digi:easternArabicNumber><c:out
																					value="${umViewAllUsersForm.pagesSize}"></c:out></digi:easternArabicNumber></td>
																		</tr>
																	</logic:notEmpty>

                                                                    <c:if test="${not empty umViewAllUsersForm.alphaPages || not empty umViewAllUsersForm.digitPages}">
																		<tr>
																			<td align="center" colspan="4">
																				<table width="90%">
																					<tr>
																						<td><c:if
																								test="${not empty umViewAllUsersForm.currentAlpha}">
																								<c:if
																									test="${umViewAllUsersForm.currentAlpha!='viewAll'}">
																									<c:if
																										test="${umViewAllUsersForm.currentAlpha!=''}">
																										<c:set var="trnViewAllLink">
																											<digi:trn key="aim:clickToViewAllSearchPages">Click here to view all search pages</digi:trn>
																										</c:set>
																										<a href="javascript:searchAlpha('viewAll')"
																											title="${trnViewAllLink}"> <digi:trn
																												key="aim:viewAllLink">viewAll</digi:trn>
																										</a>
																									</c:if>
																								</c:if>
																							</c:if> <logic:iterate name="umViewAllUsersForm"
																								property="alphaPages" id="alphaPages"
																								type="java.lang.String">
																								<c:if test="${alphaPages != null}">
																									<c:if
																										test="${umViewAllUsersForm.currentAlpha == alphaPages}">
																										<font color="#FF0000"><digi:easternArabicNumber><%=alphaPages %></digi:easternArabicNumber></font>
																									</c:if>
																									<c:if
																										test="${umViewAllUsersForm.currentAlpha != alphaPages}">
																										<c:set var="translation">
																											<digi:trn key="aim:clickToGoToNext">Click here to go to next page</digi:trn>
																										</c:set>
																										<a
																											href="javascript:searchAlpha('<%=alphaPages%>')"
																											title="${translation}"> <digi:easternArabicNumber><%=alphaPages %></digi:easternArabicNumber></a>
																									</c:if>
															|&nbsp;
															</c:if>
																							</logic:iterate>

                                                                                            <br />
                                                                                            <logic:iterate name="umViewAllUsersForm"
                                                                                                           property="digitPages" id="digitPages"
                                                                                                           type="java.lang.String">

                                                                                                <c:if test="${digitPages != null}">
                                                                                                    <c:if
                                                                                                            test="${aimOrgManagerForm.currentAlpha == digitPages}">
																										<font color="#FF0000"><digi:easternArabicNumber><%=digitPages %></digi:easternArabicNumber></font>
                                                                                                    </c:if>
                                                                                                    <c:if
                                                                                                            test="${aimOrgManagerForm.currentAlpha != digitPages}">
                                                                                                        <c:set var="translation">
                                                                                                            <digi:trn key="aim:clickToGoToNext">Click here to go to next page</digi:trn>
                                                                                                        </c:set>
                                                                                                        <a
                                                                                                                href="javascript:searchAlpha('<%=digitPages%>')"
                                                                                                                title="${translation}"> <digi:easternArabicNumber><%=digitPages %></digi:easternArabicNumber></a>
                                                                                                    </c:if>
                                                                                                    |&nbsp;
                                                                                                </c:if>
                                                                                            </logic:iterate>

                                                                                        </td>
																					</tr>

																				</table></td>
																		</tr>
																	</c:if>
																	<logic:notEmpty name="umViewAllUsersForm"
																		property="alphaPages">
																		<tr>
																			<td bgColor=#f4f4f2><c:if
																					test="${not empty umViewAllUsersForm.currentAlpha}">
																					<c:if
																						test="${umViewAllUsersForm.currentAlpha!='viewAll'}">
																						<c:if
																							test="${umViewAllUsersForm.currentAlpha!=''}">
																							<digi:trn key="um:UserMan:alphaFilterNote">
																	Click on viewAll to see all existing Users.
																</digi:trn>
																						</c:if>
																					</c:if>
																				</c:if></td>
																		</tr>
																	</logic:notEmpty>
																</table></td>
														</tr>

													</table></td>
											</tr>
										</table></td>
									<td noWrap width="100%" vAlign="top">&nbsp;								  </td>
								</tr>
							</table></td>
				<td width=20>&nbsp;</td>
    <td width=300 valign=top>
										<table align="center" cellpadding="0" cellspacing="0"
											width="300" border="0">
											<tr>
												<td>
													<!-- Other Links -->
													<table cellpadding="0" cellspacing="0" width="100">
														<tr>
															<td bgColor=#c9c9c7 class=box-title><digi:trn
																	key="aim:otherLinks">
																	<b style="font-weight: bold; font-size: 12px; padding-left:5px; color:#000000;"><digi:trn>Other links</digi:trn></b>
																</digi:trn></td>
															<td class="corner-right">&nbsp;</td>
														</tr>
													</table></td>
											</tr>
											<tr>
												<td bgColor=#ffffff>
													<table cellPadding=0 cellspacing="0" width="100%" class="inside">
														<tr>
															<td class="inside">
																<span class="list-item-arrow"></span>
																<digi:link module="aim" href="/admin.do">
																	<digi:trn key="aim:AmpAdminHome">
																Admin Home
																</digi:trn>
																</digi:link></td>
														</tr>
														<tr>
															<td class="inside">
																<span class="list-item-arrow"></span>
																<digi:link module="aim" href="/../um/addUser.do">
																	<digi:trn key="aim:addNewUser">
																Add new user																</digi:trn>
																</digi:link></td>
														</tr>
														<tr>
															<td class="inside">
																<span class="list-item-arrow"></span>
																<digi:link module="aim"
																	href="/workspaceManager.do~page=1">
																	<digi:trn key="aim:WorkspaceManager">
																Workspace Manager
																</digi:trn>
																</digi:link></td>
														</tr>
														<c:choose>
															<c:when test="${umViewAllUsersForm.showBanned}">
																<tr>
																	<td class="inside">
																		<span class="list-item-arrow"></span>
																		<digi:link module="aim"
																			href="/../um/viewAllUsers.do~showBanned=false">
																			<digi:trn key="aim:ViewActiveUsers">
																	View Active Users
																	</digi:trn>
																		</digi:link></td>
																</tr>
															</c:when>
															<c:otherwise>
																<tr>
																	<td class="inside">
																		<span class="list-item-arrow"></span>
																		<digi:link module="aim"
																			href="/../um/viewAllUsers.do~showBanned=true">
																			<digi:trn key="aim:ViewBannedUsers">
																	View Banned Users
																	</digi:trn>
																		</digi:link></td>
																</tr>
															</c:otherwise>
														</c:choose>
														<tr>
															<td class="inside">
																<span class="list-item-arrow"></span>
																<digi:link module="aim"
																	href="/../um/suspendLoginManager.do">
																	<digi:trn>
																		Account Suspend Manager
																	</digi:trn>
																</digi:link></td>
														</tr>
														<!-- end of other links -->
													</table></td>
											</tr>
										</table></td>
  </tr>
</table>
</td>
</tr>
</table>


</digi:form>


