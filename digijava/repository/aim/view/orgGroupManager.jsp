<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:errors/>
<digi:instance property="aimOrgGroupManagerForm" />
<digi:context name="digiContext" property="context" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
						<digi:link href="/admin.do" styleClass="comment">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:orgGroupManager"> Organization Group Manager
						</digi:trn>
                      </span>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>
						<digi:trn key="aim:orgGroupManager"></span><span class=crumb>Organization Group
                      Manager
						</digi:trn>
                      </span>
					</td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1>
					<tr>
						<td noWrap width=600 vAlign="top">
							<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
								<tr bgColor=#f4f4f2>
									<td vAlign="top" width="100%">
										&nbsp;
									</td>
								</tr>
								<tr bgColor=#f4f4f2>
									<td valign="top">
										<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="90%" border=0>	
											<tr>
												<td bgColor=#ffffff class=box-border>
													<table border=0 cellPadding=1 cellSpacing=1 class=box-border width="100%">
														<tr bgColor=#dddddb>
															<!-- header -->
															<td bgColor=#dddddb height="20" 			align="center" colspan="5"><B>
																<digi:trn key="aim:orgGroupList">List
                                                                of Organization Groups</digi:trn>						
                                                              </b>						
															</td>
															<!-- end header -->
														</tr>
													<!-- Page Logic -->

														<logic:empty name="aimOrgGroupManagerForm" property="organisation">
														<tr>
															<td colspan="5">
                                                   		<b><digi:trn key="aim:noOrganizationGroup">No
                                                        organization group present</digi:trn>
                                                       </b>	
															</td>
														</tr>
														</logic:empty>
														<logic:notEmpty name="aimOrgGroupManagerForm" 	property="organisation">
														<tr>
															<td width="100%">	
																<table width="634" border=0	 bgColor=#f4f4f2>
																	<tr>
																		<td height="30" width="377"><b>
																			<digi:trn key="aim:orgGroupName">Group
                                                                          Name</digi:trn></b>
																		</td>	
																		<td height="30" width="171"><b>
																			<digi:trn key="aim:orgGroupCode">Code</digi:trn></b>
																		</td>
																		<td height="30"width="147"><b>
																			<digi:trn key="aim:orgGroupLevel">Level</digi:trn></b>
																		</td>
																	</tr>
																<logic:iterate name="aimOrgGroupManagerForm" property="organisation" id="organisation">
                                                           			<tr>
																		<td height="30" width="377">
																		  <jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																		  <c:set target="${urlParams}" property="action" value="edit" />
																		  <c:set target="${urlParams}" property="ampOrgGrpId">
																		  	<bean:write name="organisation" property="ampOrgGrpId" />
																		  </c:set>
																		  <digi:link href="/editOrgGroup.do" name="urlParams">
																		  	<bean:write name="organisation" property="orgGrpName" />
																		  </digi:link>
																		</td>
																		<td height="30" width="171">
																			<logic:empty name="organisation" property="orgGrpCode">
																				<c:out value="-" />
																			</logic:empty>
																			<logic:notEmpty name="organisation" property="orgGrpCode">
																				<bean:write name="organisation" property="orgGrpCode" />
																			</logic:notEmpty>
                                                                        </td>
																		<td height="30" width="147">
																			<logic:notEmpty name="organisation" property="levelId">
                                                              					<c:out value="${organisation.levelId.name}" />
                                                              				</logic:notEmpty>
                                                              				<logic:empty name="organisation" property="levelId">
                                                              					<c:out value="-" />
                                                              				</logic:empty>
																		</td>
                                                            		</tr>
																</logic:iterate>
																</table>	
															</td>
														</tr>
														</logic:notEmpty>
														<!-- end page logic -->
													</table>
												</td>
											</tr>
											<!-- page logic for pagination -->
											<logic:notEmpty name="aimOrgGroupManagerForm" property="pages">
											<tr>
												<td colspan="4">
													<digi:trn key="aim:organizationPages">
													Pages :</digi:trn>
													<logic:iterate name="aimOrgGroupManagerForm" 	property="pages" id="pages" type="java.lang.Integer">
													<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${urlParams1}" property="page"><%=pages%>
													</c:set>
													<digi:link href="/orgGroupManager.do" name="urlParams1">
														<%=pages%>
													</digi:link> |&nbsp; </logic:iterate>
												</td>
											</tr>
											</logic:notEmpty>
											<!-- end page logic for pagination -->
										</table>
									</td>
								</tr>
								<tr>
									<td bgColor=#f4f4f2>
										&nbsp;
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
														<digi:link href="/editOrgGroup.do?action=create" >
															<digi:trn key="aim:addNewOrgGroup">Add Group</digi:trn></digi:link>
												</td>
											</tr>
											<tr>
												<td>
													<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
														<digi:link href="/organisationManager.do" >
															<digi:trn key="aim:organizationManager">Organization Manager</digi:trn></digi:link>
												</td>
											</tr>
											<tr>
												<td>
													<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
													<digi:link href="/admin.do">
													<digi:trn key="aim:AmpAdminHome">
													Admin Home
													</digi:trn>
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
