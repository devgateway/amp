<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:errors/>
<digi:instance property="aimOrgTypeManagerForm" />
<digi:context name="digiContext" property="context" />
<div class="admin-content">
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000 align=center>
	<tr>
		<td align=left valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<td noWrap width="100%" vAlign="top" colspan=5>
					<table width="100%" cellpadding="1" cellspacing="1">
					<tr>
						<td noWrap width=750 vAlign="top">
							<table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="100%">
								<tr bgColor=#f4f4f2>
									<td valign="top">
										<table align="center" cellpadding="0" cellspacing="0" width="100%" border="0">	
											<tr>
												<td bgColor=#ffffff class=box-border>
													<table border="0" cellpadding="1" cellspacing="1" class=box-border width="100%">
														<tr bgColor=#dddddb>
															<!-- header -->
															<td bgColor=#c7d4db height="20" align="center" colspan="5"><B>
																<b style="font-size:12px;"><digi:trn  key="aim:orgTypeList">List of Organization Types</digi:trn></b>						
                                                              </b>						
															</td>
															<!-- end header -->
														</tr>
													<!-- Page Logic -->

														<logic:empty name="aimOrgTypeManagerForm" property="organisation">
														<tr>
															<td colspan="5">
                                                   		<b><digi:trn key="aim:noOrganizationType">No
                                                        organization type present</digi:trn>
                                                       </b>	
															</td>
														</tr>
														</logic:empty>
														<logic:notEmpty name="aimOrgTypeManagerForm" 	property="organisation">
														<tr>
															<td width="100%">	
																<table width="100%" border="0" class="inside">
																	<tr>
																		<td height="30" width="350" class="inside" bgcolor="#f2F2f2"><b>
																			<digi:trn key="aim:orgTypeName">Name</digi:trn></b>
																		</td>	
																		<td height="30" width="150" class="inside" bgcolor="#f2F2f2"><b>
																			<digi:trn key="aim:orgTypeCode">Code</digi:trn></b>
																		</td>
																	</tr>
																<logic:iterate name="aimOrgTypeManagerForm" property="organisation" id="organisation">
                                                           			<tr>
																		<td height="30" class="inside">
																		  <jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																		  <c:set target="${urlParams}" property="action" value="edit" />
																		  <c:set target="${urlParams}" property="ampOrgTypeId">
																		  	<bean:write name="organisation" property="ampOrgTypeId" />
																		  </c:set>
																		  <digi:link href="/editOrgType.do" name="urlParams">
																		  	<bean:write name="organisation" property="orgType" />
																		  </digi:link>
																		</td>
																		<td height="30" class="inside">
																			<logic:empty name="organisation" property="orgTypeCode">
																				<c:out value="-" />
																			</logic:empty>
																			<logic:notEmpty name="organisation" property="orgTypeCode">
																				<bean:write name="organisation" property="orgTypeCode" />
																			</logic:notEmpty>
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
											<logic:notEmpty name="aimOrgTypeManagerForm" property="pages">
											<tr>
											<td colspan="4">
													<digi:trn key="aim:organizationPages">
													Pages :</digi:trn>
													<c:if test="${aimOrgTypeManagerForm.currentPage > 1}">
														<jsp:useBean id="urlParamsFirst" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParamsFirst}" property="page" value="1"/>
														<c:set target="${urlParamsFirst}" property="orgSelReset" value="false"/>
														<c:set var="translation">
															<digi:trn key="aim:firstpage">First Page</digi:trn>
														</c:set>
														
														<digi:link href="/orgTypeManager.do"  style="text-decoration=none" name="urlParamsFirst" title="${translation}"  >
															&lt;&lt;
														</digi:link>
													
														<jsp:useBean id="urlParamsPrevious" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParamsPrevious}" property="page" value="${aimOrgTypeManagerForm.currentPage -1}"/>
														<c:set target="${urlParamsPrevious}" property="orgSelReset" value="false"/>
														<c:set var="translation">
															<digi:trn key="aim:previouspage">Previous Page</digi:trn>
														</c:set>
														<digi:link href="/orgTypeManager.do" name="urlParamsPrevious" style="text-decoration=none" title="${translation}" >
															&lt;
														</digi:link>
														</c:if>
														
														<logic:iterate name="aimOrgTypeManagerForm" property="pages" id="pages" type="java.lang.Integer" offset="${start}" length="${length}">	
														<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParams1}" property="page"><%=pages%>
														</c:set>
														<c:set target="${urlParams1}" property="orgSelReset" value="false"/>
														<c:if test="${aimOrgTypeManagerForm.currentPage == pages}">
															<font color="#FF0000"><%=pages%></font>
														</c:if>
														<c:if test="${aimOrgTypeManagerForm.currentPage != pages}">
															<c:set var="translation">
															<digi:trn key="aim:clickToViewNextPage">Click here to go to Next Page</digi:trn>
															</c:set>
															<digi:link href="/orgTypeManager.do" name="urlParams1" title="${translation}" >
																<%=pages%>
															</digi:link>
														</c:if>
														|&nbsp;
														</logic:iterate>
														
														<c:if test="${aimOrgTypeManagerForm.currentPage != aimOrgTypeManagerForm.pagesSize}">
															<jsp:useBean id="urlParamsNext" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParamsNext}" property="page" value="${aimOrgTypeManagerForm.currentPage+1}"/>
															<c:set target="${urlParamsNext}" property="orgSelReset" value="false"/>
															<c:set var="translation">
																<digi:trn key="aim:nextpage">Next Page</digi:trn>
															</c:set>
															<digi:link href="/orgTypeManager.do"  style="text-decoration=none" name="urlParamsNext" title="${translation}"  >
																&gt;
															</digi:link>
															<jsp:useBean id="urlParamsLast" type="java.util.Map" class="java.util.HashMap"/>
																<c:set target="${urlParamsLast}" property="page" value="${aimOrgTypeManagerForm.pagesSize}"/>
															<c:set var="translation">
															<digi:trn key="aim:lastpage">Last Page</digi:trn>
															</c:set>
															<digi:link href="/orgTypeManager.do"  style="text-decoration=none" name="urlParamsLast" title="${translation}"  >
																&gt;&gt;  
															</digi:link>
															&nbsp;&nbsp;
														</c:if>
														<c:out value="${aimOrgTypeManagerForm.currentPage}"></c:out>&nbsp;<digi:trn key="aim:of">of</digi:trn>&nbsp;<c:out value="${aimOrgTypeManagerForm.pagesSize}"></c:out>
												</td>
											</tr>
											</logic:notEmpty>
											<!-- end page logic for pagination -->
										</table>
									</td>
								</tr>
							</table>
						</td>
						<td noWrap width="100%" vAlign="top">
							<table align="center" cellpadding="0" cellspacing="0" width="90%" border="0">	
								<tr>
									<td>
										<!-- Other Links -->
										<table cellpadding="0" cellspacing="0" width="120">
											<tr>
												<td bgColor=#c9c9c7 class=box-title>
													<b style="font-size:12px; padding-left:5px;">
														<digi:trn key="aim:otherLinks">
															Other links
														</digi:trn>
													</b>												
												</td>
												<td class="header-corner" height="17" width=17></td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td bgColor=#ffffff class=box-border>
										<table cellPadding=5 cellspacing="1" width="100%" class="inside">
											<tr>
												<td class="inside">
													<digi:img src="module/aim/images/arrow-014E86.gif" styleClass="list-item-image"	width="15" height="10"/>
														<digi:link href="/editOrgType.do?action=create" >
															<digi:trn key="aim:addNewOrgType">Add Type</digi:trn></digi:link>
												</td>
											</tr>
											<tr>
												<td class="inside">
													<digi:img src="module/aim/images/arrow-014E86.gif" styleClass="list-item-image"	width="15" height="10"/>
														<digi:link href="/organisationManager.do" >
															<digi:trn key="aim:organizationManager">Organization Manager</digi:trn></digi:link>
												</td>
											</tr>
											<tr>
												<td class="inside">
													<digi:img src="module/aim/images/arrow-014E86.gif" styleClass="list-item-image" width="15" height="10"/>
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
</div>
