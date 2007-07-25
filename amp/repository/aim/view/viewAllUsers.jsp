<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimViewAllUsersForm" />
<digi:context name="digiContext" property="context" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<digi:form action="/viewAllUsers.do" method="post">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height=33>
						<span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:members">
						Members
						</digi:trn>
						</span>
					</td>
					<!-- End navigation -->
				</tr>
				<tr style="width:50%;">
                  <td height=16 vAlign="center">
                    Filter by:
                    <html:select property="type" style="font-family:verdana;font-size:11px;">
                      <html:option value="-1">-All-</html:option>
                      <html:option value="0">Registred</html:option>
                      <html:option value="1">Team members</html:option>
                    </html:select>

                    keyword:
                    <html:text property="keyword" style="font-family:verdana;font-size:11px;"/>

                    <input type="submit" value="Go" style="font-family:verdana;font-size:11px;" />
                  </td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top" colspan="3">
					<table width="100%" cellspacing=1 cellSpacing=1>
					<tr><td noWrap width=600 vAlign="top">
						<table  bgcolor="#d7eafd" cellPadding=0 cellSpacing=1 width="100%" border=0>
							<tr bgColor=#ffffff>
								<td vAlign="top" width="100%">
									<table width="100%" cellspacing=1 cellpadding=1 valign=top align=left>
										<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
											<!-- Table title -->
											<digi:trn key="aim:users">Users</digi:trn>

											<!-- end table title -->
										</td></tr>
										<tr><td>
											<table width="100%" cellspacing=1 cellpadding=4 valign=top align=left bgcolor="#d7eafd">

                                              <c:if test="${empty aimViewAllUsersForm.users}">
                                                <tr bgcolor="#ffffff">
                                                  <td colspan="3" align="center">
                                                    <b>No users present</b>
                                                  </td>
                                                </tr>
                                              </c:if>

                                              <c:if test="${!empty aimViewAllUsersForm.users}">
                                                <tr bgcolor="#ffffff">
                                                  <td>
                                                    <table bgcolor="#ffffff">
                                                      <c:forEach var="us" items="${aimViewAllUsersForm.users}">
                                                        <tr>
                                                          <td>
                                                          ${us.email}
                                                          </td>
                                                        </tr>
                                                      </c:forEach>
                                                    </table>
                                                  </td>
                                                </tr>
                                              </c:if>
                                              <!-- end page logic -->
											</table>
										</td></tr>
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
											<td background="module/aim/images/corner-r.gif" height="17" width=17>
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
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
												<c:set var="translation">
													<digi:trn key="aim:clickToAddTeamMember">Click here to Add Team Member</digi:trn>
												</c:set>

												<c:set target="${urlParams1}" property="fromPage" value="1"/>
												<digi:link href="/showAddTeamMember.do" name="urlParams1" title="${translation}" >
													<digi:trn key="aim:addTeamMember">Add Team Member </digi:trn>
												</digi:link>
											</td>
										</tr>
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn key="aim:clickToAddRoles">Click here to Add Roles</digi:trn>
												</c:set>
												<digi:link href="/roles.do" title="${translation}" >
												<digi:trn key="aim:roles">Add Roles</digi:trn>
												</digi:link>
											</td>
										</tr>
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
												</c:set>
												<digi:link href="/admin.do" title="${translation}" >
												<digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
												</digi:link>
											</td>
										</tr>
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
            </table>
</td>
	</tr>
</table>
</digi:form>




