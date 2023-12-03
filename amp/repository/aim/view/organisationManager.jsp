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
			
			 <digi:context name="searchOrg" property="context/ampModule/moduleinstance/organisationManager.do"/>
		     url = "<%= searchOrg %>?orgSelReset=false";
		     document.aimOrgManagerForm.action = url;
		     document.aimOrgManagerForm.target="_self";
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
			 <digi:context name="searchOrg" property="context/ampModule/moduleinstance/organisationManager.do"/>
			 url = "<%= searchOrg %>?alpha=" + val + "&orgSelReset=false";
		     document.aimOrgManagerForm.action = url;
		     document.aimOrgManagerForm.target="_self";
		     document.aimOrgManagerForm.submit();
			 return true;
		}
	}
	
	function resetSearch(){
		<digi:context name="searchOrg" property="context/ampModule/moduleinstance/organisationManager.do"/>
		url = "<%= searchOrg %>?orgSelReset=true";
	    document.aimOrgManagerForm.action = url;
	    document.aimOrgManagerForm.target="_self";
	    document.aimOrgManagerForm.submit();
		return true;
	}
    function exportXSL(){
        <digi:context name="exportUrl" property="context/ampModule/moduleinstance/exportOrgManager.do"/>;
        document.aimOrgManagerForm.action="${exportUrl}";
        document.aimOrgManagerForm.target="_blank";
        document.aimOrgManagerForm.submit();
    }
   



	var enterBinder	= new EnterHitBinder('searchBtn');
	
	-->

</script>
<div class="admin-content">
<digi:secure actions="ADMIN">
<h1 class="admintitle"><digi:trn>Organization manager</digi:trn></h1>
</digi:secure>
<digi:instance property="aimOrgManagerForm" />

<digi:context name="digiContext" property="context" />
<digi:form action="/organisationManager.do" method="post">
<c:set var="selectedTab" value="9" scope="request"/>
	<digi:errors />

	<!--  AMP Admin Logo -->
	<jsp:include page="teamPagesHeader.jsp" />
	<!-- End of Logo -->

	<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000
		align=center>
		<tr>
			<td align=left valign="top" width=1000>
				<table cellPadding=5 cellspacing="0" width="1000">
				<digi:secure actions="ADMIN">
					<tr>
						<td align="left" colspan=7>
							<div class="toolbar" align="center" style="background: #f2f2f2;">
									<jsp:include
									page="/repository/aim/view/adminXSLExportToolbar.jsp" />
							</div></td>
					</tr>
					</digi:secure>
					<c:if test="${!aimOrgManagerForm.adminSide}">
					<tr>
					<td valign="top">
						<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
							<jsp:include page="teamSetupMenu.jsp" flush="true"/>
					
					<table style="width:97%;">
					</c:if>
					<tr>
						<td width="250"><digi:trn key="aim:orgManagerType">Type</digi:trn>&nbsp;
							<html:select property="ampOrgTypeId" styleClass="inp-text-orgType">
								<html:option value="-1">
									<digi:trn key="aim:all">All</digi:trn>
								</html:option>
								<logic:notEmpty name="aimOrgManagerForm" property="orgTypes">
									<html:optionsCollection name="aimOrgManagerForm"
										property="orgTypes" value="ampOrgTypeId" label="orgType" />
								</logic:notEmpty>
							</html:select>
						</td>
						<td width="170"><digi:trn key="aim:keyword">Keyword</digi:trn>&nbsp;
						<html:text property="keyword" styleClass="inp-text" styleId="keyWordTextField"/>
                        </td>
                        <td width="100">
						<digi:trn key="aim:results">Results</digi:trn>&nbsp;
							<!--<digi:trn key="aim:resultsPerPage">Results per page</digi:trn>&nbsp;-->
							<!--<html:text property="tempNumResults" size="2" styleClass="inp-text" />-->
							<html:select property="tempNumResults" styleClass="inp-text">
								<html:option value="10">
							10
						</html:option>
								<html:option value="20">
							20
						</html:option>
								<html:option value="50">
							50
						</html:option>
								<html:option value="-1">
									<digi:trn key="aim:all">All</digi:trn>
								</html:option>
							</html:select>
						</td>
						<td width="50"><c:set var="trnResetBtn">
								<digi:trn key="aim:btnReset"> Reset </digi:trn>
							</c:set> <input type="button" value="${trnResetBtn}" class="buttonx_sm"
							onclick="return resetSearch()">
						</td>
						<td width="100"><c:set var="trnGoBtn">
								<digi:trn key="aim:btnGo"> GO </digi:trn>
							</c:set> <input type="button" value="${trnGoBtn}" class="buttonx_sm"
							onclick="return searchOrganization()"  id="searchBtn">
						</td>
					</tr>
					<tr>
						<td height=16 valign="center" width=867 colspan="7"
							style="border-bottom: 1px solid #CCCCCC; padding-bottom: 15px;"><digi:trn
								key="aim:organMan:topFilterNote">
						Select the value "ALL" in Results per page to view all results of your selection on one page.
						</digi:trn>
						</td>
					</tr>

					<tr>
						<td noWrap vAlign="top" colspan="7">
							<table width="100%" cellspacing="0" cellpadding="0">
								<tr>
									<td noWrap vAlign="top"  width="70%" >
										<table bgColor=#ffffff cellpadding="0" cellspacing="0"
											width="100%">

											<tr>
												<td valign="top">
													<table align="center" cellpadding="0" cellspacing="0"
														width="100%" border="0">
														<tr>
															<td bgColor=#ffffff>
																<table border="0" cellpadding="0" cellspacing="0"
																	width="100%" style="margin-top: 10px;">
																	<tr bgColor=#dddddb>
																		<!-- header -->
																		<td bgColor=#c7d4db height="25" align="center"
																			colspan="6"><B> <digi:trn
																					key="aim:organizationList">List of Organizations</digi:trn>
																		</b>
																		</td>
																		<!-- end header -->
																	</tr>
																	<!-- Page Logic -->

																	<logic:empty name="aimOrgManagerForm"
																		property="pagedCol">
																		<tr>
																			<td width="100%" colspan="5"><b><digi:trn
																						key="aim:noOrganization">No
		                                                        organization present</digi:trn>
																			</b>
																			</td>
																		</tr>
																	</logic:empty>
																	<logic:notEmpty name="aimOrgManagerForm"
																		property="pagedCol">
																		<tr>
																			<td width="100%" class="report">
																				<!--  to export table we are adding class "report" to its container -->
																				<table cellpadding="0" cellspacing="0" width="100%"
																					class="inside">
																					<thead>
																						<tr>
																							<td class="inside" bgcolor=#F2F2F2><c:if
																									test="${not empty aimOrgManagerForm.sortBy && aimOrgManagerForm.sortBy!='nameAscending'}">
																									<digi:link
																										href="/organisationManager.do?sortBy=nameAscending&reset=false&orgSelReset=false">
																										<b><digi:trn key="aim:organizationName">Organization Name</digi:trn>
																										</b>
																									</digi:link>
																								</c:if> <c:if
																									test="${empty aimOrgManagerForm.sortBy || aimOrgManagerForm.sortBy=='nameAscending'}">
																									<digi:link
																										href="/organisationManager.do?sortBy=nameDescending&reset=false&orgSelReset=false">
																										<b><digi:trn key="aim:organizationName">Organization Name</digi:trn>
																										</b>
																									</digi:link>
																								</c:if> <c:if
																									test="${empty aimOrgManagerForm.sortBy || aimOrgManagerForm.sortBy=='nameAscending'}">
																									<img src="/repository/aim/images/up.gif" />
																								</c:if> <c:if
																									test="${not empty aimOrgManagerForm.sortBy && aimOrgManagerForm.sortBy=='nameDescending'}">
																									<img src="/repository/aim/images/down.gif" />
																								</c:if>
																							</td>
																							<td class="inside" bgcolor=#F2F2F2><c:if
																									test="${empty aimOrgManagerForm.sortBy || aimOrgManagerForm.sortBy!='acronymAscending'}">
																									<digi:link
																										href="/organisationManager.do?sortBy=acronymAscending&reset=false&orgSelReset=false">
																										<b><digi:trn key="aim:organizationAcronym">Organization Acronym</digi:trn>
																										</b>
																									</digi:link>
																								</c:if> <c:if
																									test="${not empty aimOrgManagerForm.sortBy && aimOrgManagerForm.sortBy=='acronymAscending'}">
																									<digi:link
																										href="/organisationManager.do?sortBy=acronymDescending&reset=false&orgSelReset=false">
																										<b><digi:trn key="aim:organizationAcronym">Organization Acronym</digi:trn>
																										</b>
																									</digi:link>
																								</c:if> <c:if
																									test="${not empty aimOrgManagerForm.sortBy && aimOrgManagerForm.sortBy=='acronymAscending'}">
																									<img src="/repository/aim/images/up.gif" />
																								</c:if> <c:if
																									test="${not empty aimOrgManagerForm.sortBy && aimOrgManagerForm.sortBy=='acronymDescending'}">
																									<img src="/repository/aim/images/down.gif" />
																								</c:if>
																							</td>
																							<%--<td height="60" width="171"><b>
																			<digi:trn key="aim:organizationCountry">Country</digi:trn></b>
																		</td>--%>
																							<td class="inside" bgcolor=#F2F2F2><c:if
																									test="${empty aimOrgManagerForm.sortBy || aimOrgManagerForm.sortBy!='typeAscending'}">
																									<digi:link
																										href="/organisationManager.do?sortBy=typeAscending&reset=false&orgSelReset=false">
																										<b><digi:trn key="aim:organizationType">Type</digi:trn>
																										</b>
																									</digi:link>
																								</c:if> <c:if
																									test="${not empty aimOrgManagerForm.sortBy && aimOrgManagerForm.sortBy=='typeAscending'}">
																									<digi:link
																										href="/organisationManager.do?sortBy=typeDescending&reset=false&orgSelReset=false">
																										<b><digi:trn key="aim:organizationType">Type</digi:trn>
																										</b>
																									</digi:link>
																								</c:if> <c:if
																									test="${not empty aimOrgManagerForm.sortBy && aimOrgManagerForm.sortBy=='typeAscending'}">
																									<img src="/repository/aim/images/up.gif" />
																								</c:if> <c:if
																									test="${not empty aimOrgManagerForm.sortBy && aimOrgManagerForm.sortBy=='typeDescending'}">
																									<img src="/repository/aim/images/down.gif" />
																								</c:if>
																							</td>
																							<td class="inside" bgcolor=#F2F2F2><c:if
																									test="${empty aimOrgManagerForm.sortBy || aimOrgManagerForm.sortBy!='groupAscending'}">
																									<digi:link
																										href="/organisationManager.do?sortBy=groupAscending&reset=false&orgSelReset=false">
																										<b><digi:trn key="aim:organizationGroup">Organization Group</digi:trn>
																										</b>
																									</digi:link>
																								</c:if> <c:if
																									test="${not empty aimOrgManagerForm.sortBy && aimOrgManagerForm.sortBy=='groupAscending'}">
																									<digi:link
																										href="/organisationManager.do?sortBy=groupDescending&reset=false&orgSelReset=false">
																										<b><digi:trn key="aim:organizationGroup">Organization Group</digi:trn>
																										</b>
																									</digi:link>
																								</c:if> <c:if
																									test="${not empty aimOrgManagerForm.sortBy && aimOrgManagerForm.sortBy=='groupAscending'}">
																									<img src="/repository/aim/images/up.gif" />
																								</c:if> <c:if
																									test="${not empty aimOrgManagerForm.sortBy && aimOrgManagerForm.sortBy=='groupDescending'}">
																									<img src="/repository/aim/images/down.gif" />
																								</c:if>
																							</td>
																							<c:if test="${!aimOrgManagerForm.adminSide}">
																							<td class="inside" bgcolor=#F2F2F2>&nbsp; 
																							</td>
																							</c:if>
																						</tr>
																					</thead>
																					<!--  to export table we are adding class "yui-dt-data" to its tbody-->
																					<tbody class="yui-dt-data">
																						<logic:iterate name="aimOrgManagerForm"
																							property="pagedCol" id="organisation"
																							indexId="index">
																							<tr>
																								<td class="inside">
																								<jsp:useBean
																										id="urlParams" type="java.util.Map"
																										class="java.util.HashMap" /> <c:set
																										target="${urlParams}" property="mode"
																										value="resetMode" /> <c:set
																										target="${urlParams}" property="actionFlag"
																										value="edit" /> <c:set target="${urlParams}"
																										property="ampOrgId">
																										<bean:write name="organisation"
																											property="ampOrgId" />
																											</c:set>
																								<c:choose>
																								<c:when test="${aimOrgManagerForm.adminSide}">
																									 <digi:link href="/editOrganisation.do"
																										name="urlParams">
																										<bean:write name="organisation"
																											property="name" />
																									</digi:link>
																								</c:when>
																								<c:otherwise><c:out value="${organisation.name}"/></c:otherwise>
																								</c:choose>
																								
																								</td>
																								<td class="inside"><bean:write
																										name="organisation" property="acronym" />
																								</td>
																								<%--<td height="30" width="171">
                                                                            <logic:notEmpty name="organisation" property="countryId">
                                                              					<c:out value="${organisation.countryId.countryName}" />
                                                              				</logic:notEmpty>
																		</td>--%>
																								<td class="inside"><logic:notEmpty
																										name="organisation" property="orgGrpId">
																										<c:out
																											value="${organisation.orgGrpId.orgType.orgType}" />

																										<%--<bean:write name="organisation" property="${organisation.orgTypeId.orgType}" />--%>
																									</logic:notEmpty>
																								</td>
																								<td class="inside"><logic:notEmpty
																										name="organisation" property="orgGrpId">
																										<c:out
																											value="${organisation.orgGrpId.orgGrpName}" />
																									</logic:notEmpty>
																								</td>
																								<c:if test="${!aimOrgManagerForm.adminSide}">
																							<td class="inside">
																							 <digi:link href="/editOrganisation.do"
																										name="urlParams">
																										<digi:trn>Edit</digi:trn>
																									</digi:link>
																							</td>
																							</c:if>
																							</tr>
																						</logic:iterate>
																					</tbody>
																				</table></td>
																		</tr>
																	</logic:notEmpty>
																	<!-- end page logic -->
																</table></td>
														</tr>
														<!-- page logic for pagination -->
														<logic:notEmpty name="aimOrgManagerForm" property="pages">
															<tr>
																<td colspan="4" align=center>
																	<hr /> <digi:trn key="aim:organizationPages">
													Pages :</digi:trn> <c:if test="${aimOrgManagerForm.currentPage > 1}">
																		<jsp:useBean id="urlParamsFirst" type="java.util.Map"
																			class="java.util.HashMap" />
																		<c:set target="${urlParamsFirst}" property="page"
																			value="1" />
																		<c:set target="${urlParamsFirst}"
																			property="orgSelReset" value="false" />
																		<c:set var="translation">
																			<digi:trn key="aim:firstpage">First Page</digi:trn>
																		</c:set>

																		<digi:link href="/organisationSearch.do"
																			style="text-decoration=none" name="urlParamsFirst"
																			title="${translation}">
															&lt;&lt;
														</digi:link>

																		<jsp:useBean id="urlParamsPrevious"
																			type="java.util.Map" class="java.util.HashMap" />
																		<c:set target="${urlParamsPrevious}" property="page"
																			value="${aimOrgManagerForm.currentPage -1}" />
																		<c:set target="${urlParamsPrevious}"
																			property="orgSelReset" value="false" />
																		<c:set var="translation">
																			<digi:trn key="aim:previouspage">Previous Page</digi:trn>
																		</c:set>
																		<digi:link href="/organisationSearch.do"
																			name="urlParamsPrevious" style="text-decoration=none"
																			title="${translation}">
															&lt;
														</digi:link>
																	</c:if> <c:set var="length"
																		value="${aimOrgManagerForm.pagesToShow}"></c:set> <c:set
																		var="start" value="${aimOrgManagerForm.offset}" /> <logic:iterate
																		name="aimOrgManagerForm" property="pages" id="pages"
																		type="java.lang.Integer" offset="${start}"
																		length="${length}">
																		<jsp:useBean id="urlParams1" type="java.util.Map"
																			class="java.util.HashMap" />
																		<c:set target="${urlParams1}" property="page"><%=pages%>
																		</c:set>
																		<c:set target="${urlParams1}" property="orgSelReset"
																			value="false" />
																		<c:if test="${aimOrgManagerForm.currentPage == pages}">
																			<font color="#FF0000"><%=pages%></font>
																		</c:if>
																		<c:if test="${aimOrgManagerForm.currentPage != pages}">
																			<c:set var="translation">
																				<digi:trn key="aim:clickToViewNextPage">Click here to go to Next Page</digi:trn>
																			</c:set>
																			<digi:link href="/organisationSearch.do"
																				name="urlParams1" title="${translation}">
																				<%=pages%>
																			</digi:link>
																		</c:if>
														|&nbsp;
														</logic:iterate> <c:if
																		test="${aimOrgManagerForm.currentPage != aimOrgManagerForm.pagesSize}">
																		<jsp:useBean id="urlParamsNext" type="java.util.Map"
																			class="java.util.HashMap" />
																		<c:set target="${urlParamsNext}" property="page"
																			value="${aimOrgManagerForm.currentPage+1}" />
																		<c:set target="${urlParamsNext}"
																			property="orgSelReset" value="false" />
																		<c:set var="translation">
																			<digi:trn key="aim:nextpage">Next Page</digi:trn>
																		</c:set>
																		<digi:link href="/organisationSearch.do"
																			style="text-decoration=none" name="urlParamsNext"
																			title="${translation}">
																&gt;
															</digi:link>
																		<jsp:useBean id="urlParamsLast" type="java.util.Map"
																			class="java.util.HashMap" />
																			<c:set target="${urlParamsLast}" property="page"
																				value="${aimOrgManagerForm.pagesSize}" />
																		<c:set target="${urlParamsLast}"
																			property="orgSelReset" value="false" />
																		<c:set var="translation">
																			<digi:trn key="aim:lastpage">Last Page</digi:trn>
																		</c:set>
																		<digi:link href="/organisationSearch.do"
																			style="text-decoration=none" name="urlParamsLast"
																			title="${translation}">
																&gt;&gt;  
															</digi:link>
															&nbsp;&nbsp;
														</c:if> <c:out value="${aimOrgManagerForm.currentPage}"></c:out>&nbsp;<digi:trn
																		key="aim:of">of</digi:trn>&nbsp;<c:out
																		value="${aimOrgManagerForm.pagesSize}"></c:out>
																</td>
															</tr>
														</logic:notEmpty>
														<logic:notEmpty name="aimOrgManagerForm"
															property="alphaPages">
															<tr>
																<td align="center" colspan="4">
																	<table width="90%">
																		<tr>
																			<td align=center><c:if
																					test="${not empty aimOrgManagerForm.currentAlpha}">
																					<c:if
																						test="${aimOrgManagerForm.currentAlpha!='viewAll'}">
																						<c:if test="${aimOrgManagerForm.currentAlpha!=''}">
																							<c:set var="trnViewAllLink">
																								<digi:trn key="aim:clickToViewAllSearchPages">Click here to view all search pages</digi:trn>
																							</c:set>
																							<a href="javascript:searchAlpha('viewAll')"
																								title="${trnViewAllLink}"> <digi:trn
																									key="aim:viewAllLink">viewAll</digi:trn>&nbsp;|&nbsp;
																							</a>
																						</c:if>
																					</c:if>
																				</c:if> <logic:iterate name="aimOrgManagerForm"
																					property="alphaPages" id="alphaPages"
																					type="java.lang.String">

																					<c:if test="${alphaPages != null}">
																						<c:if
																							test="${aimOrgManagerForm.currentAlpha == alphaPages}">
																							<font color="#FF0000"><%=alphaPages %></font>
																						</c:if>
																						<c:if
																							test="${aimOrgManagerForm.currentAlpha != alphaPages}">
																							<c:set var="translation">
																								<digi:trn key="aim:clickToGoToNext">Click here to go to next page</digi:trn>
																							</c:set>
																							<a
																								href="javascript:searchAlpha('<%=alphaPages%>')"
																								title="${translation}"> <%=alphaPages %></a>
																						</c:if>
															|&nbsp;
															</c:if>
																				</logic:iterate>
                                                                                <br />
                                                                                <logic:iterate name="aimOrgManagerForm"
                                                                                               property="digitPages" id="digitPages"
                                                                                               type="java.lang.String">

                                                                                    <c:if test="${digitPages != null}">
                                                                                        <c:if
                                                                                                test="${aimOrgManagerForm.currentAlpha == digitPages}">
                                                                                            <font color="#FF0000"><%=digitPages %></font>
                                                                                        </c:if>
                                                                                        <c:if
                                                                                                test="${aimOrgManagerForm.currentAlpha != digitPages}">
                                                                                            <c:set var="translation">
                                                                                                <digi:trn key="aim:clickToGoToNext">Click here to go to next page</digi:trn>
                                                                                            </c:set>
                                                                                            <a
                                                                                                    href="javascript:searchAlpha('<%=digitPages%>')"
                                                                                                    title="${translation}"> <%=digitPages %></a>
                                                                                        </c:if>
                                                                                        |&nbsp;
                                                                                    </c:if>
                                                                                </logic:iterate>
																			</td>
																		</tr>

																	</table></td>
															</tr>
														</logic:notEmpty>
														<!-- end page logic for pagination -->
													</table></td>
											</tr>
														
											<logic:notEmpty name="aimOrgManagerForm"
												property="alphaPages">
												<tr>
													<td align=center><c:if
															test="${not empty aimOrgManagerForm.currentAlpha}">
															<c:if test="${aimOrgManagerForm.currentAlpha!='viewAll'}">
																<c:if test="${aimOrgManagerForm.currentAlpha!=''}">
																	<digi:trn key="aim:organMan:alphaFilterNote">
															Click on view All to see all existing organizations.
														</digi:trn>
																</c:if>
															</c:if>
														</c:if>
													</td>
												</tr>
											</logic:notEmpty>
										</table></td>
											<digi:secure actions="ADMIN">
									<td noWrap width="20%" vAlign="top" style="padding-top: 10px;"><jsp:include
											page="orgManagerOtherLinks.jsp" /></td>
										</digi:secure>
								</tr>
							</table></td>
					</tr>
				<c:if test="${!aimOrgManagerForm.adminSide}"></table></div></td></tr></c:if></table>
					</td>
					</tr></table> </digi:form>
</div>