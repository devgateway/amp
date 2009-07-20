<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.digijava.module.aim.form.OrgManagerForm"%>
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

	function resetSearch(){
		<digi:context name="searchOrg" property="context/module/moduleinstance/organisationManager.do"/>
		url = "<%= searchOrg %>?orgSelReset=true";
	    document.aimOrgManagerForm.action = url;
	    document.aimOrgManagerForm.submit();
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

	
	function setHoveredRow(rowId) {

		var rowElement = document.getElementById(rowId);
		if(rowElement){
	    	var className = 'Hovered',
	        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
	        cells      = rowElement.getElementsByTagName('td');

			for(var i = 0, n = cells.length; i < n; ++i) {
				cells[i].onmouseover = function() {
					this.className += ' ' + className;
				};
				cells[i].onmouseout = function() {
					this.className = this.className.replace(pattern, ' ');

				};
			}
			cells = null;
		}
	}
	-->

</script>

<style type="text/css">
		.jcol{												
		padding-left:10px;												 
		}
		.jlien{
			text-decoration:none;
		}
		.tableEven {
			background-color:#dbe5f1;
			font-size:8pt;
			padding:2px;
		}

		.tableOdd {
			background-color:#FFFFFF;
			font-size:8pt;
			padding:2px;
		}
		 
		.Hovered {
			background-color:#a5bcf2;
		}
		
		.notHovered {
			background-color:#FFFFFF;
		}
		
</style>

<digi:errors/>
<digi:instance property="aimOrgManagerForm" />
<digi:context name="digiContext" property="context" />

<digi:form action="/organisationManager.do" method="post">

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=980px>
	<tr>
		<td align=left vAlign=top>
			<table cellPadding=5 cellSpacing=0 width="100%">
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
					<td height=16 vAlign=center width=867 colspan="7">
						<span class=subtitle-blue>
							<digi:trn key="aim:organizationManager">Organization Manager</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%">
							<tr>
								<td width="30%">
									<digi:trn key="aim:orgManagerType">Type</digi:trn>:
									<html:select property="ampOrgTypeId" styleClass="inp-text" onchange="javascript:searchOrganization()">
										<html:option value="-1">-<digi:trn key="aim:all">All</digi:trn>-</html:option>
										<logic:notEmpty name="aimOrgManagerForm" property="orgTypes">
											<html:optionsCollection name="aimOrgManagerForm" property="orgTypes"
												value="ampOrgTypeId" label="orgType" />
										</logic:notEmpty>
									</html:select>
								</td>
								<td width="28%" >
					            	<digi:trn>Go to</digi:trn>:
									<html:select property="alpha" style="font-family:verdana;font-size:11px; margin-right:100px;" onchange="document.aimOrgManagerForm.submit()">
										<html:option value="viewAll">-<digi:trn key="aim:all">All</digi:trn>-</html:option>
										<c:if test="${not empty aimOrgManagerForm.alphaPages}">
										<logic:iterate name="aimOrgManagerForm" property="alphaPages" id="alphaPages" type="java.lang.String">
											<c:if test="${alphaPages != null}">
												<html:option value="<%=alphaPages %>" ><%=alphaPages %></html:option>
											</c:if>
										</logic:iterate>
										</c:if>
									</html:select>
								</td>
								<td width="28%">
									<digi:trn key="aim:keyword">Keyword</digi:trn>:
									<html:text property="keyword" styleClass="inp-text" />
								</td>
								<td width="7%">					
				                    <c:set var="trnGoBtn">
				                      <digi:trn key="aim:btnGo"> GO </digi:trn>
				                    </c:set>
				                    <input type="button" value="${trnGoBtn}" class="dr-menu" onclick="return searchOrganization()">
								</td>
								<td width="7%">
				                    <c:set var="trnResetBtn">
				                      <digi:trn key="aim:btnReset"> Reset </digi:trn>
				                    </c:set>
			                    	<input type="button" value="${trnResetBtn}" class="dr-menu" onclick="return resetSearch()">
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
				<td vAlign="top" width="75%">
					<table cellSpacing="0" cellPadding="0" vAlign="top" align="left" width="100%">
								<!-- Page Logic -->
									<logic:empty name="aimOrgManagerForm" property="pagedCol">
									<tr>
										<td colspan="5">
                                                		<b><digi:trn key="aim:noOrganization">No organization present</digi:trn>
                                                     </b>
										</td>
									</tr>
									</logic:empty>
									<logic:notEmpty name="aimOrgManagerForm" property="pagedCol">
									<tr>
										<td width="100%">
											<table cellpadding="2" width="100%">
												<tr>
													<td>
														<table width="100%" height="30" cellpadding="2" cellspacing="0">
															<tr style="background-color: #999999; color: #000000;" align="center">
																<td align="left" width="30%">
																	<c:if test="${not empty aimOrgManagerForm.sortBy && aimOrgManagerForm.sortBy!='nameAscending'}">
																		<digi:link href="/organisationManager.do?sortBy=nameAscending&reset=false&orgSelReset=false" style="color:#000000;">
																			<b><digi:trn key="aim:organizationName">Organization Name</digi:trn></b>
																		</digi:link>																					
																	</c:if>
																	<c:if test="${empty aimOrgManagerForm.sortBy || aimOrgManagerForm.sortBy=='nameAscending'}">
																		<digi:link href="/organisationManager.do?sortBy=nameDescending&reset=false&orgSelReset=false" style="color:#000000;">
																			<b><digi:trn key="aim:organizationName">Organization Name</digi:trn></b>
																		</digi:link>																					
																	</c:if>
																	<c:if test="${empty aimOrgManagerForm.sortBy || aimOrgManagerForm.sortBy=='nameAscending'}"><img  src="/repository/aim/images/up.gif"/></c:if>
																	<c:if test="${not empty aimOrgManagerForm.sortBy && aimOrgManagerForm.sortBy=='nameDescending'}"><img src="/repository/aim/images/down.gif"/></c:if>
																</td>
																<td align="left" width="30%">																		
																	<c:if test="${empty aimOrgManagerForm.sortBy || aimOrgManagerForm.sortBy!='acronymAscending'}">
																		<digi:link href="/organisationManager.do?sortBy=acronymAscending&reset=false&orgSelReset=false" style="color:#000000;">
																			<b><digi:trn key="aim:organizationAcronym">Organization Acronym</digi:trn></b>
																		</digi:link>																					
																	</c:if>
																	<c:if test="${not empty aimOrgManagerForm.sortBy && aimOrgManagerForm.sortBy=='acronymAscending'}">
																		<digi:link href="/organisationManager.do?sortBy=acronymDescending&reset=false&orgSelReset=false" style="color:#000000;">
																			<b><digi:trn key="aim:organizationAcronym">Organization Acronym</digi:trn></b>
																		</digi:link>																					
																	</c:if>
																	<c:if test="${not empty aimOrgManagerForm.sortBy && aimOrgManagerForm.sortBy=='acronymAscending'}"><img  src="/repository/aim/images/up.gif"/></c:if>
																	<c:if test="${not empty aimOrgManagerForm.sortBy && aimOrgManagerForm.sortBy=='acronymDescending'}"><img src="/repository/aim/images/down.gif"/></c:if>
																</td>																	
																<td align="left" width="20%">
																	<c:if test="${empty aimOrgManagerForm.sortBy || aimOrgManagerForm.sortBy!='typeAscending'}">
																		<digi:link href="/organisationManager.do?sortBy=typeAscending&reset=false&orgSelReset=false" style="color:#000000;">
																			<b><digi:trn key="aim:organizationType">Type</digi:trn></b>
																		</digi:link>																					
																	</c:if>
																	<c:if test="${not empty aimOrgManagerForm.sortBy && aimOrgManagerForm.sortBy=='typeAscending'}">
																		<digi:link href="/organisationManager.do?sortBy=typeDescending&reset=false&orgSelReset=false" style="color:#000000;">
																			<b><digi:trn key="aim:organizationType">Type</digi:trn></b>
																		</digi:link>																					
																	</c:if>
																	<c:if test="${not empty aimOrgManagerForm.sortBy && aimOrgManagerForm.sortBy=='typeAscending'}"><img  src="/repository/aim/images/up.gif"/></c:if>
																	<c:if test="${not empty aimOrgManagerForm.sortBy && aimOrgManagerForm.sortBy=='typeDescending'}"><img src="/repository/aim/images/down.gif"/></c:if>																																			
																</td>
																<td align="left" width="20%">
																<c:if test="${empty aimOrgManagerForm.sortBy || aimOrgManagerForm.sortBy!='groupAscending'}">
																		<digi:link href="/organisationManager.do?sortBy=groupAscending&reset=false&orgSelReset=false" style="color:#000000;">
																			<b><digi:trn key="aim:organizationGroup">Organization Group</digi:trn></b>
																		</digi:link>																					
																	</c:if>
																	<c:if test="${not empty aimOrgManagerForm.sortBy && aimOrgManagerForm.sortBy=='groupAscending'}">
																		<digi:link href="/organisationManager.do?sortBy=groupDescending&reset=false&orgSelReset=false" style="color:#000000;">
																			<b><digi:trn key="aim:organizationGroup">Organization Group</digi:trn></b>
																		</digi:link>																					
																	</c:if>
																	<c:if test="${not empty aimOrgManagerForm.sortBy && aimOrgManagerForm.sortBy=='groupAscending'}"><img  src="/repository/aim/images/up.gif"/></c:if>
																	<c:if test="${not empty aimOrgManagerForm.sortBy && aimOrgManagerForm.sortBy=='groupDescending'}"><img src="/repository/aim/images/down.gif"/></c:if>
																</td>
															</tr>
														</table>
													</td>
												</tr>
												<tr>
													<td>
														<div style="overflow: auto; width: 100%; height: 180px; max-height: 180px;">
															<table width="100%" cellspacing="0" cellpadding="2" id="dataTable">
																<logic:iterate name="aimOrgManagerForm" property="pagedCol" id="organisation" indexId="index">
                                                           			<tr height="25">
																		<td align="left" width="30%">
																		  <jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																		  <c:set target="${urlParams}" property="mode" value="resetMode" />
																		  <c:set target="${urlParams}" property="actionFlag" value="edit" />
																		  <c:set target="${urlParams}" property="ampOrgId">
																		  	<bean:write name="organisation" property="ampOrgId" />
																		  </c:set>
																		  <digi:link href="/editOrganisation.do" name="urlParams">																		  	
																		  	<bean:write name="organisation" property="name" /> 
																		  </digi:link>
																		</td>
																		<td align="left" width="30%">
																		  	<bean:write name="organisation" property="acronym" />																		  
																		</td>
																		<td align="left" width="20%">
																			<logic:notEmpty name="organisation" property="orgTypeId">
                                                              					<c:out value="${organisation.orgTypeId.orgType}" />
                                                              					<%--<bean:write name="organisation" property="${organisation.orgTypeId.orgType}" />--%>
                                                              				</logic:notEmpty>
																		</td>
																		<td align="left" width="20%">
																			<logic:notEmpty name="organisation" property="orgGrpId">
                                                              					<c:out value="${organisation.orgGrpId.orgGrpName}" />
                                                              				</logic:notEmpty>
																		</td>
                                                            		</tr>
																</logic:iterate>
															</table>
														</div>
													</td>
												</tr>
											</table>
										</td>
									</tr>
									</logic:notEmpty>
									<!-- end page logic -->
						<!-- page logic for pagination -->
						<tr height="20">
							<td>&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<table>
									<tr>
										<logic:notEmpty name="aimOrgManagerForm" property="pages">
										<td colspan="4">
											<div  style="   float:left;">
												<table style="padding:5px;" >
													<tr id="rowHighlight">
														<c:if test="${aimOrgManagerForm.currentPage > 1}">
															<jsp:useBean id="urlParamsFirst" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParamsFirst}" property="page" value="1"/>
															<c:set target="${urlParamsFirst}" property="orgSelReset" value="false"/>
															<c:set var="translation">
																<digi:trn key="aim:firstpage">First Page</digi:trn>
															</c:set>
															<td style="padding:3px;border:1px solid #999999;" nowrap="nowrap">
																<digi:link href="/organisationSearch.do"  style="text-decoration=none" name="urlParamsFirst" title="${translation}"  >
																	&lt;&lt;
																</digi:link>
															</td>
														</c:if>
														<c:set var="length" value="${aimOrgManagerForm.pagesToShow}"></c:set>
														<c:set var="start" value="${aimOrgManagerForm.offset}"/>
														<logic:iterate name="aimOrgManagerForm" property="pages" id="pages" type="java.lang.Integer" offset="${start}" length="${length}">	
															<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams1}" property="page"><%=pages%>
															</c:set>
															<c:set target="${urlParams1}" property="orgSelReset" value="false"/>
															<c:if test="${aimOrgManagerForm.currentPage == pages}">
																<td style="padding:3px;border:2px solid #000000; " nowrap="nowrap" >
																	<font color="#FF0000"><%=pages%></font>
																</td>
															</c:if>
															<c:if test="${aimOrgManagerForm.currentPage != pages}">
																<c:set var="translation">
																<digi:trn key="aim:clickToViewPage">Click here to go to Page</digi:trn> <%=pages%>
																</c:set>
																<td style="padding:3px;border:1px solid #999999;" nowrap="nowrap">
																	<digi:link href="/organisationSearch.do" name="urlParams1" title="${translation}" >
																		<%=pages%>
																	</digi:link>
																</td>
															</c:if>
														</logic:iterate>
														<c:if test="${aimOrgManagerForm.currentPage != aimOrgManagerForm.pagesSize}">
															<jsp:useBean id="urlParamsLast" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParamsLast}" property="page" value="${aimOrgManagerForm.pagesSize}"/>
															<c:set target="${urlParamsLast}" property="orgSelReset" value="false"/>
															<c:set var="translation">
															<digi:trn key="aim:lastpage">Last Page</digi:trn>
															</c:set>
															<td style="padding:3px;border:1px solid #999999;" nowrap="nowrap">
																<digi:link href="/organisationSearch.do"  style="text-decoration=none" name="urlParamsLast" title="${translation}"  >
																	&gt;&gt;  
																</digi:link>
															</td>
														</c:if>
														<td style="padding:3px;border:1px solid #999999;" nowrap="nowrap">
															<digi:trn key="aim:of">of</digi:trn>&nbsp;<c:out value="${aimOrgManagerForm.pagesSize}"></c:out>
														</td>
														<td>&nbsp;&nbsp;&nbsp;</td>
														<% OrgManagerForm aimOrgManagerForm = (OrgManagerForm) pageContext.getAttribute("aimOrgManagerForm");%>
														<td style="padding:3px;border:1px solid #999999">
												            <c:out value="<%=aimOrgManagerForm.getCols().size()%>"></c:out>&nbsp;<digi:trn key="aim:records">records</digi:trn>
									         			</td>
													</tr>
												</table>
											</div>
										</td>
										</logic:notEmpty>
										<td>
											<digi:trn key="aim:results">Results</digi:trn>:
											<html:select property="tempNumResults" styleClass="inp-text" onchange="javascript:searchOrganization()">
												<html:option value="10">10</html:option>
												<html:option value="20">20</html:option>
												<html:option value="50">50</html:option>
												<html:option value="-1">-<digi:trn key="aim:all">All</digi:trn>-</html:option>
											</html:select>
										</td>	
									</tr>
								</table>
							</td>
						</tr>
						<!-- end page logic for pagination -->
					</table>
				</td>
				<td vAlign="top" width="25%">
					<jsp:include page="orgManagerOtherLinks.jsp" />
				</td>
		</tr>
	</table>
</td>
</tr>
</table>
<script language="javascript">
	setStripsTable("dataTable", "tableEven", "tableOdd");
	setHoveredTable("dataTable", false);
	setHoveredRow("rowHighlight");
</script>
</digi:form>
