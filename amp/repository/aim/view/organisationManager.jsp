<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript">

	<!--

	function searchOrganization() {
		if (document.aimOrgManagerForm.tempNumResults.value == 0) {
			  alert ("Invalid value at 'Number of results per page'");
			  document.aimOrgManagerForm.tempNumResults.focus();
			  return false;
		} else {
			
			 <digi:context name="searchOrg" property="context/module/moduleinstance/organisationManager.do"/>
		     url = "<%= searchOrg %>?orgSelReset=false";
		     document.aimOrgManagerForm.action = url;
		     document.aimOrgManagerForm.submit();
			 return true;
		}
	}

	function searchAlpha(val) {
		if (document.aimOrgManagerForm.tempNumResults.value == 0) {
			  alert ("Invalid value at 'Number of results per page'");
			  document.aimOrgManagerForm.tempNumResults.focus();
			  return false;
		} else {
			 <digi:context name="searchOrg" property="context/module/moduleinstance/organisationManager.do"/>
			 url = "<%= searchOrg %>?alpha=" + val + "&orgSelReset=false";
		     document.aimOrgManagerForm.action = url;
		     document.aimOrgManagerForm.submit();
			 return true;
		}
	}
	
	function resetSearch(){
		<digi:context name="searchOrg" property="context/module/moduleinstance/organisationManager.do"/>
		url = "<%= searchOrg %>?orgSelReset=true";
	    document.aimOrgManagerForm.action = url;
	    document.aimOrgManagerForm.submit();
		return true;
	}

	-->

</script>

<digi:errors/>
<digi:instance property="aimOrgManagerForm" />
<digi:context name="digiContext" property="context" />

<digi:form action="/organisationManager.do" method="post">

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="879">
				<tr>
					<!-- Start Navigation -->
					<td height=33 colspan="7" width="867"><span class=crumb>
						<digi:link href="/admin.do" styleClass="comment">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:organizationManager"> Organization Manager
						</digi:trn>
                      </span>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=867 colspan="7"><span class=subtitle-blue>
						<digi:trn key="aim:organizationManager"></span><span class=crumb>Organization
                      Manager
						</digi:trn>
                      </span>
					</td>
				</tr>
				<tr>
					<td width="201">
						<digi:trn key="aim:orgManagerType">Type</digi:trn>&nbsp;
						<html:select property="ampOrgTypeId" styleClass="inp-text">
							<html:option value="-1">All</html:option>
							<logic:notEmpty name="aimOrgManagerForm" property="orgTypes">
								<html:optionsCollection name="aimOrgManagerForm" property="orgTypes"
									value="ampOrgTypeId" label="orgType" />
							</logic:notEmpty>
						</html:select>
					</td>
					<td width="195">
						<digi:trn key="aim:keyword">Keyword</digi:trn>&nbsp;
						<html:text property="keyword" styleClass="inp-text" />
					</td>
					<td width="120">
						<digi:trn key="aim:results">Results</digi:trn>&nbsp;
						<!--<digi:trn key="aim:resultsPerPage">Results per page</digi:trn>&nbsp;-->
						<!--<html:text property="tempNumResults" size="2" styleClass="inp-text" />-->
						<html:select property="tempNumResults" styleClass="inp-text">
							<html:option value="10">10</html:option>
							<html:option value="20">20</html:option>
							<html:option value="50">50</html:option>
							<html:option value="-1">ALL</html:option>
						</html:select>
					</td>
					<td width="50">
                    <c:set var="trnResetBtn">
                      <digi:trn key="aim:btnReset"> Reset </digi:trn>
                    </c:set>
                    <input type="button" value="${trnResetBtn}" class="buton" onclick="return resetSearch()">
					</td>
					<td width="260">					
                    <c:set var="trnGoBtn">
                      <digi:trn key="aim:btnGo"> GO </digi:trn>
                    </c:set>
                    <input type="button" value="${trnGoBtn}" class="buton" onclick="return searchOrganization()">
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=867 colspan="7">
						<digi:trn key="aim:organMan:topFilterNote">
						Select the value "ALL" in Results per page to view all results of your selection on one page.
						</digi:trn>
                     </td>
				</tr>

					<tr>
					<td noWrap width=867 vAlign="top" colspan="7">
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
																<digi:trn key="aim:organizationList">List of Organizations</digi:trn>
                                                              </b>
															</td>
															<!-- end header -->
														</tr>
													<!-- Page Logic -->

														<logic:empty name="aimOrgManagerForm" property="pagedCol">
														<tr>
															<td colspan="5">
                                                   		<b><digi:trn key="aim:noOrganization">No
                                                        organization present</digi:trn>
                                                       </b>
															</td>
														</tr>
														</logic:empty>
														<logic:notEmpty name="aimOrgManagerForm" 	property="pagedCol">
														<tr>
															<td width="100%">
																<table width="634" border=0	 bgColor=#f4f4f2>
																	<tr>
																		<td height="30" width="220"><b>
																			<digi:trn key="aim:organizationAcronym">Organization
                                                                            Acrony</digi:trn></b>
																		</td>																	
																		<td height="30" width="220"><b>
																			<digi:trn key="aim:organizationName">Organization
                                                                            Name</digi:trn></b>
																		</td>
																	<%--<td height="30" width="171"><b>
																			<digi:trn key="aim:organizationCountry">Country</digi:trn></b>
																		</td>--%>
																		<td height="30"width="147"><b>
																			<digi:trn key="aim:organizationType">Type</digi:trn></b>
																		</td>
																		<td height="30"width="147"><b>
																			<digi:trn key="aim:organizationGroup">Organization Group</digi:trn></b>
																		</td>
																	</tr>
																<logic:iterate name="aimOrgManagerForm" property="pagedCol" id="organisation">
                                                           			<tr>
	                                                           			<td height="30">
																		  <jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																		  <c:set target="${urlParams}" property="mode" value="resetMode" />
																		  <c:set target="${urlParams}" property="actionFlag" value="edit" />
																		  <c:set target="${urlParams}" property="ampOrgId">
																		  	<bean:write name="organisation" property="ampOrgId" />
																		  </c:set>
																		  <digi:link href="/editOrganisation.do" name="urlParams">																		  	
																		  	<bean:write name="organisation" property="acronym" />
																		  </digi:link>
																		</td>
																		<td height="30">
																		  	<bean:write name="organisation" property="name" />																		  
																		</td>
																	<%--<td height="30" width="171">
                                                                            <logic:notEmpty name="organisation" property="countryId">
                                                              					<c:out value="${organisation.countryId.countryName}" />
                                                              				</logic:notEmpty>
																		</td>--%>
																		<td height="30">
																			<logic:notEmpty name="organisation" property="orgTypeId">
                                                              					<c:out value="${organisation.orgTypeId.orgType}" />
                                                              					<%--<bean:write name="organisation" property="${organisation.orgTypeId.orgType}" />--%>
                                                              				</logic:notEmpty>
																		</td>
																		<td height="30">
																			<logic:notEmpty name="organisation" property="orgGrpId">
                                                              					<c:out value="${organisation.orgGrpId.orgGrpName}" />
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
											<logic:notEmpty name="aimOrgManagerForm" property="pages">
											<tr>
												<td colspan="4">
													<digi:trn key="aim:organizationPages">
													Pages :</digi:trn>
													<logic:iterate name="aimOrgManagerForm" 	property="pages" id="pages" type="java.lang.Integer">
													<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${urlParams1}" property="page"><%=pages%>
													</c:set>

													<c:set target="${urlParams1}" property="orgSelReset" value="false"/>
													<c:if test="${aimOrgManagerForm.currentPage == pages}">
														<font color="#FF0000"><%=pages%></font>
													</c:if>
													<c:if test="${aimOrgManagerForm.currentPage != pages}">
														<c:set var="translation">
															<digi:trn key="aim:clickToViewNextPage">Click here to go to Next Page</digi:trn>
														</c:set>
														<digi:link href="/organisationSearch.do" name="urlParams1" title="${translation}" >
															<%=pages%>
														</digi:link>
													</c:if>
													|&nbsp; </logic:iterate>
												</td>
											</tr>
											</logic:notEmpty>
											<logic:notEmpty name="aimOrgManagerForm" property="alphaPages">
											<tr>
												<td align="center" colspan="4">
													<table width="90%">
														<tr>
														    <td>
														    <c:if test="${not empty aimOrgManagerForm.currentAlpha}">
														    	<c:if test="${aimOrgManagerForm.currentAlpha!='viewAll'}">
															    	<c:if test="${aimOrgManagerForm.currentAlpha!=''}">														    	
																    	<c:set var="trnViewAllLink">
																			<digi:trn key="aim:clickToViewAllSearchPages">Click here to view all search pages</digi:trn>
																		</c:set>
																		<a href="javascript:searchAlpha('viewAll')" title="${trnViewAllLink}">
																				<digi:trn key="aim:viewAllLink">viewAll</digi:trn></a>
																	</c:if>
																</c:if>
														    </c:if>
															
															<logic:iterate name="aimOrgManagerForm" property="alphaPages" id="alphaPages" type="java.lang.String">
															<c:if test="${alphaPages != null}">
																<c:if test="${aimOrgManagerForm.currentAlpha == alphaPages}">
																	<font color="#FF0000"><%=alphaPages %></font>
																</c:if>
																<c:if test="${aimOrgManagerForm.currentAlpha != alphaPages}">
																	<c:set var="translation">
																		<digi:trn key="aim:clickToGoToNext">Click here to go to next page</digi:trn>
																	</c:set>
																	<a href="javascript:searchAlpha('<%=alphaPages%>')" title="${translation}" >
																		<%=alphaPages %></a>
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
											<!-- end page logic for pagination -->
										</table>
									</td>
								</tr>
								<tr>
									<td bgColor=#f4f4f2>
										<c:if test="${not empty aimOrgManagerForm.currentAlpha}">
											<c:if test="${aimOrgManagerForm.currentAlpha!='viewAll'}">
											   	<c:if test="${aimOrgManagerForm.currentAlpha!=''}">														    	
											    	<digi:trn key="aim:organMan:alphaFilterNote">
														Click on viewAll to see all existing organizations.
													</digi:trn>
												</c:if>
											</c:if>
										</c:if>										
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
													<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>
												<td>
														<digi:link href="/editOrganisation.do?actionFlag=create&mode=resetMode" >
															<digi:trn key="aim:addNewOrganization">Add an Organization</digi:trn></digi:link>
												</td>
											</tr>
											<tr>
												<td>
													<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>
												<td>
														<digi:link href="/orgTypeManager.do" >
															<digi:trn key="aim:orgTypeManager">Organization Type Manager</digi:trn></digi:link>
												</td>
											</tr>
											<tr>
												<td>
													<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>
												<td>
														<digi:link href="/orgGroupManager.do" >
															<digi:trn key="aim:orgGroupManager">Organization Group Manager</digi:trn></digi:link>
												</td>
											</tr>
											<tr>
												<td>
													<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>
												<td>
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
