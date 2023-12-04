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
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<script language="JavaScript">
	function searchAlpha(val) {		 
		     aimOrgGroupManagerForm.action ="${contextPath}/aim/orgGroupManager.do?alpha="+val ;		     
		     aimOrgGroupManagerForm.submit();
			 return true;		
	}
	
	function searchOrganization() {
		if (document.aimOrgGroupManagerForm.tempNumResults.value == 0) {
			  alert ("Invalid value at 'Number of results per page'");
			  document.aimOrgGroupManagerForm.tempNumResults.focus();
			  return false;
		} else {
			
			 <digi:context name="searchOrg" property="context/module/moduleinstance/orgGroupManager.do"/>
		     url = "<%= searchOrg %>?orgSelReset=false";
		     document.aimOrgGroupManagerForm.action = url;
		     document.aimOrgGroupManagerForm.submit();
			 return true;
		}
	}

	
	function resetSearch(){
		<digi:context name="searchOrg" property="context/module/moduleinstance/orgGroupManager.do"/>
		url = "<%= searchOrg %>?orgSelReset=true";
	    document.aimOrgGroupManagerForm.action = url;
	    document.aimOrgGroupManagerForm.submit();
		return true;
	}
	
	var enterBinder	= new EnterHitBinder('searchOrgGrpBtn');
</script>


<div class="admin-content">
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->
<digi:form action="/orgGroupManager.do">
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000 align=center>
	<tr>
		<td align=left class=r-dotted-lg valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%" style="font-size:12px;">
				<tr>
					<td width="250">
						<digi:trn key="aim:orgManagerType">Type</digi:trn>&nbsp;
						<html:select property="ampOrgTypeId" styleClass="inp-text">
							<html:option value="-1"><digi:trn key="aim:all">All</digi:trn></html:option>
							<logic:notEmpty name="aimOrgGroupManagerForm" property="orgTypes">
								<html:optionsCollection name="aimOrgGroupManagerForm" property="orgTypes"
									value="ampOrgTypeId" label="orgType" />
							</logic:notEmpty>
						</html:select>
					</td>
					<td width="250">
						<digi:trn key="aim:keyword">Keyword</digi:trn>&nbsp;
						<html:text property="keyword" styleClass="inp-text" />
					</td>
					<td width="170">
						<digi:trn key="aim:results">Results</digi:trn>&nbsp;
						<html:select property="tempNumResults" styleClass="inp-text">
							<html:option value="10"><digi:trn key="aim:10">10</digi:trn></html:option>
							<html:option value="20"><digi:trn key="aim:20">20</digi:trn></html:option>
							<html:option value="50"><digi:trn key="aim:50">50</digi:trn></html:option>
							<html:option value="-1"><digi:trn key="aim:all">All</digi:trn></html:option>
						</html:select>
					</td>
					<td width="50">
                    <c:set var="trnResetBtn">
                      <digi:trn key="aim:btnReset"> Reset </digi:trn>
                    </c:set>
                    <input type="button" value="${trnResetBtn}" class="buttonx" onclick="return resetSearch()">
					</td>
					<td width="300">					
                    <c:set var="trnGoBtn">
                      <digi:trn key="aim:btnGo"> GO </digi:trn>
                    </c:set>
                    <input type="button" value="${trnGoBtn}" class="buttonx" onclick="return searchOrganization()" id="searchOrgGrpBtn">
					</td>
				</tr>
				<tr>
					<td height=16 valign="center" width=867 colspan="7">
						<digi:trn key="aim:organMan:topFilterNote">
						Select the value "ALL" in Results per page to view all results of your selection on one page.
						</digi:trn>
                     </td>
				</tr>
				
				<tr>
					<td noWrap width=867  colspan="7" vAlign="top">
					<table width="100%" cellpadding="1" cellspacing="1">
					<tr>
						<td noWrap width=750 vAlign="top">
							<table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="100%">
								<tr>
									<td valign="top">
										<table align="center" cellpadding="0" cellspacing="0" width="100%" border="0">	
											<tr>
												<td bgColor=#ffffff >
													<table border="0" cellpadding="1" cellspacing="1" class=box-border width="100%">
														<tr bgColor=#dddddb>
															<!-- header -->
															<td bgColor=#c7d4db height="25" align="center" colspan="5"><B style="font-size:12px;">
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
																<table width="100%" border="0" class="inside">
																	<tr>
																		<td height="30" width="377" class="inside" bgcolor=#F2F2f2>
																			<jsp:useBean id="urlParams4" type="java.util.Map" class="java.util.HashMap"/>
																			<c:set target="${urlParams4}" property="alpha"><bean:write name="aimOrgGroupManagerForm" property="currentAlpha"/></c:set>
																			<c:if test="${not empty aimOrgGroupManagerForm.sortBy && aimOrgGroupManagerForm.sortBy!='nameAscending'}">
																				<digi:link href="/orgGroupManager.do?sortBy=nameAscending&reset=false&orgSelReset=false" name="urlParams4">
																					<b><digi:trn key="aim:orgGroupName">Group Name</digi:trn></b>
																				</digi:link>																															
																			</c:if>
																			<c:if test="${empty aimOrgGroupManagerForm.sortBy || aimOrgGroupManagerForm.sortBy=='nameAscending'}">
																				<digi:link href="/orgGroupManager.do?sortBy=nameDescending&reset=false&orgSelReset=false" name="urlParams4">
																					<b><digi:trn key="aim:orgGroupName">Group Name</digi:trn></b>
																				</digi:link>																															
																			</c:if>
																			<c:if test="${empty aimOrgGroupManagerForm.sortBy || aimOrgGroupManagerForm.sortBy=='nameAscending'}"><img  src="/repository/aim/images/up.gif"/></c:if>
																			<c:if test="${not empty aimOrgGroupManagerForm.sortBy && aimOrgGroupManagerForm.sortBy=='nameDescending'}"><img src="/repository/aim/images/down.gif"/></c:if>
																		</td>	
																		<td height="30" width="171" class="inside" bgcolor=#F2F2f2>
																			<jsp:useBean id="urlParams5" type="java.util.Map" class="java.util.HashMap"/>
																			<c:set target="${urlParams5}" property="alpha"><bean:write name="aimOrgGroupManagerForm" property="currentAlpha"/></c:set>
																			<c:if test="${empty aimOrgGroupManagerForm.sortBy || aimOrgGroupManagerForm.sortBy!='codeAscending'}">
																				<digi:link href="/orgGroupManager.do?sortBy=codeAscending&reset=false&orgSelReset=false" name="urlParams5">
																					<b><digi:trn key="aim:orgGroupCode">Code</digi:trn></b>
																				</digi:link>																															
																			</c:if>
																			<c:if test="${not empty aimOrgGroupManagerForm.sortBy && aimOrgGroupManagerForm.sortBy=='codeAscending'}">
																				<digi:link href="/orgGroupManager.do?sortBy=codeDescending&reset=false&orgSelReset=false"  name="urlParams5">
																					<b><digi:trn key="aim:orgGroupCode">Code</digi:trn></b>
																				</digi:link>																															
																			</c:if>
																			<c:if test="${not empty aimOrgGroupManagerForm.sortBy && aimOrgGroupManagerForm.sortBy=='codeAscending'}"><img  src="/repository/aim/images/up.gif"/></c:if>
																			<c:if test="${not empty aimOrgGroupManagerForm.sortBy && aimOrgGroupManagerForm.sortBy=='codeDescending'}"><img src="/repository/aim/images/down.gif"/></c:if>
																		</td>
																		<td height="30" width="147" class="inside" bgcolor=#F2F2f2>
																			<jsp:useBean id="urlParams6" type="java.util.Map" class="java.util.HashMap"/>
																			<c:set target="${urlParams6}" property="alpha"><bean:write name="aimOrgGroupManagerForm" property="currentAlpha"/></c:set>
																			<c:if test="${empty aimOrgGroupManagerForm.sortBy || aimOrgGroupManagerForm.sortBy!='typeAscending'}">
																				<digi:link href="/orgGroupManager.do?sortBy=typeAscending&reset=false&orgSelReset=false" name="urlParams6">
																					<b><digi:trn key="aim:orgGroupType">Type</digi:trn></b>
																				</digi:link>																														
																			</c:if>
																			<c:if test="${not empty aimOrgGroupManagerForm.sortBy && aimOrgGroupManagerForm.sortBy=='typeAscending'}">
																				<digi:link href="/orgGroupManager.do?sortBy=typeDescending&reset=false&orgSelReset=false" name="urlParams6">
																					<b><digi:trn key="aim:orgGroupType">Type</digi:trn></b>
																				</digi:link>																														
																			</c:if>
																			<c:if test="${not empty aimOrgGroupManagerForm.sortBy && aimOrgGroupManagerForm.sortBy=='typeAscending'}"><img  src="/repository/aim/images/up.gif"/></c:if>
																			<c:if test="${not empty aimOrgGroupManagerForm.sortBy && aimOrgGroupManagerForm.sortBy=='typeDescending'}"><img src="/repository/aim/images/down.gif"/></c:if>
																																		
																		</td>
																	</tr>
																<logic:iterate name="aimOrgGroupManagerForm" property="organisation" id="organisation">
                                                           			<tr>
																		<td height="30" width="377" class="inside">
																		  <jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																		  <c:set target="${urlParams}" property="action" value="edit" />
																		  <c:set target="${urlParams}" property="ampOrgGrpId">
																		  	<bean:write name="organisation" property="ampOrgGrpId" />
																		  </c:set>
																		  <digi:link href="/editOrgGroup.do" name="urlParams">
																		  	<bean:write name="organisation" property="orgGrpName" />
																		  </digi:link>
																		</td>
																		<td height="30" width="171" class="inside">
																			<logic:empty name="organisation" property="orgGrpCode">
																				<c:out value="-" />
																			</logic:empty>
																			<logic:notEmpty name="organisation" property="orgGrpCode">
																				<bean:write name="organisation" property="orgGrpCode" />
																			</logic:notEmpty>
                                                                        </td>
																		<td height="30" width="147" class="inside">
																			<logic:notEmpty name="organisation" property="orgType">
                                                              					<c:out value="${organisation.orgType.orgType}" />
                                                              				</logic:notEmpty>
                                                              				<logic:empty name="organisation" property="orgType">
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
												<td colspan="4" align=center style="font-size:12px; padding-top:15px;">
													<digi:trn key="aim:organizationPages">
													Pages :</digi:trn>
													<logic:iterate name="aimOrgGroupManagerForm" 	property="pages" id="pages" type="java.lang.Integer">
													<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${urlParams1}" property="page"><%=pages%></c:set>
													<c:set target="${urlParams1}" property="sortBy">${aimOrgGroupManagerForm.sortBy}</c:set>
													<c:set target="${urlParams1}" property="orgSelReset">false</c:set>
													<c:if test="${aimOrgGroupManagerForm.currentPage == pages}">
													<font color="#FF0000"><%=pages%></font> |&nbsp;
													</c:if>
													<c:if test="${aimOrgGroupManagerForm.currentPage != pages}">
													<digi:link href="/orgGroupManager.do" name="urlParams1">
														<%=pages%>
														</digi:link> |&nbsp; 
													</c:if>
													</logic:iterate>
												</td>
											</tr>
											</logic:notEmpty>
											<!-- end page logic for pagination -->
											<logic:notEmpty name="aimOrgGroupManagerForm" property="alphaPages">
											<tr>
												<td align="center" colspan="4" style="font-size:12px;">
													<table maxWidth="90%" style="font-size:12px;">
														<tr>
														    <td>
														    <c:if test="${not empty aimOrgGroupManagerForm.currentAlpha}">
														    	<c:if test="${aimOrgGroupManagerForm.currentAlpha!='viewAll'}">
															    	<c:if test="${aimOrgGroupManagerForm.currentAlpha!=''}">														    	
																    	<c:set var="trnViewAllLink">
																			<digi:trn key="aim:clickToViewAllSearchPages">Click here to view all search pages</digi:trn>
																		</c:set>
																		<a href="javascript:searchAlpha('viewAll')" title="${trnViewAllLink}">
																				<digi:trn key="aim:viewAllLink">viewAll</digi:trn></a>
																	</c:if>
																</c:if>
														    </c:if>
														    <logic:iterate name="aimOrgGroupManagerForm"  property="alphaPages" id="alphaPages" type="java.lang.String" >
															<c:if test="${alphaPages != null}">
																<c:if test="${aimOrgGroupManagerForm.currentAlpha == alphaPages}">
																	<font color="#FF0000"><%=alphaPages%></font>
																</c:if>
																<c:if test="${aimOrgGroupManagerForm.currentAlpha != alphaPages}">
																	<c:set var="translation">
																		<digi:trn key="aim:clickToGoToNext">Click here to go to next page</digi:trn>
																	</c:set>
																	<a href="javascript:searchAlpha('<%=alphaPages%>')" title="${translation}" >
																		<%=alphaPages%></a>
																</c:if>
															|&nbsp;
															</c:if>
														</logic:iterate>
												   </td>
												 </tr>

												</table>
											</td>
										</tr>
										</logic:notEmpty>
										<!-- end of pagination -->
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
													<digi:img src="module/aim/images/arrow-014E86.gif" 	styleClass="list-item-image" width="15" height="10"/>
														<digi:link href="/editOrgGroup.do?action=create" >
															<digi:trn key="aim:addNewOrgGroup">Add Group</digi:trn></digi:link>
												</td>
											</tr>
											<tr>
												<td class="inside">
													<digi:img src="module/aim/images/arrow-014E86.gif" 	styleClass="list-item-image" width="15" height="10"/>
														<digi:link href="/organisationManager.do" >
															<digi:trn key="aim:organizationManager">Organization Manager</digi:trn></digi:link>
												</td>
											</tr>
											<tr>
												<td class="inside">
													<digi:img src="module/aim/images/arrow-014E86.gif" styleClass="list-item-image" 	width="15" height="10"/>
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
</digi:form>
</div>