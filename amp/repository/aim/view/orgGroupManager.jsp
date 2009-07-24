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

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
<digi:form action="/orgGroupManager.do">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=980px>
	<tr>
		<td align=left vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height=33 colspan="7" width="867"><span class=crumb>
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
					<td height=16 vAlign=center width=571 colspan="7">
						<span class=subtitle-blue>
							<digi:trn key="aim:orgGroupManager">Organization Group Manager</digi:trn>
                      	</span>
					</td>
				</tr>
				<tr>
					<td>
						<table  width="100%">
							<tr>
								<td width="30%">
									<digi:trn key="aim:orgManagerType">Type</digi:trn>:
									<html:select property="ampOrgTypeId" styleClass="inp-text" onchange="javascript:searchOrganization()">
										<html:option value="-1">-<digi:trn key="aim:all">All</digi:trn>-</html:option>
										<logic:notEmpty name="aimOrgGroupManagerForm" property="orgTypes">
											<html:optionsCollection name="aimOrgGroupManagerForm" property="orgTypes"
												value="ampOrgTypeId" label="orgType" />
										</logic:notEmpty>
									</html:select>
								</td>
								<td width="28%" >
					            	<digi:trn>Go to</digi:trn>:
									<html:select property="alpha" style="font-family:verdana;font-size:11px; margin-right:100px;" onchange="javascript:searchOrganization()">
										<html:option value="viewAll">-<digi:trn key="aim:all">All</digi:trn>-</html:option>
										<c:if test="${not empty aimOrgGroupManagerForm.alphaPages}">
										<logic:iterate name="aimOrgGroupManagerForm" property="alphaPages" id="alphaPages" type="java.lang.String">
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
					<logic:empty name="aimOrgGroupManagerForm" property="organisation">
						<tr>
							<td colspan="5">
                               <b><digi:trn key="aim:noOrganizationGroup">No organization group present</digi:trn></b>	
							</td>
						</tr>
					</logic:empty>
					<logic:notEmpty name="aimOrgGroupManagerForm" 	property="organisation">
						<tr width="100%">
							<td width="100%">
								<table cellpadding="2" width="100%">
									<tr>
										<td>
											<table width="100%" height="30" cellpadding="2" cellspacing="0">
												<tr style="background-color: #999999; color: #000000;" align="center">
													<td align="left" width="50%">
														<jsp:useBean id="urlParams4" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParams4}" property="alpha"><bean:write name="aimOrgGroupManagerForm" property="currentAlpha"/></c:set>
														<c:if test="${not empty aimOrgGroupManagerForm.sortBy && aimOrgGroupManagerForm.sortBy!='nameAscending'}">
															<digi:link href="/orgGroupManager.do?sortBy=nameAscending&reset=false&orgSelReset=false" name="urlParams4"  style="color:#000000;">
																<b><digi:trn key="aim:orgGroupName">Group Name</digi:trn></b>
															</digi:link>																															
														</c:if>
														<c:if test="${empty aimOrgGroupManagerForm.sortBy || aimOrgGroupManagerForm.sortBy=='nameAscending'}">
															<digi:link href="/orgGroupManager.do?sortBy=nameDescending&reset=false&orgSelReset=false" name="urlParams4" style="color:#000000;">
																<b><digi:trn key="aim:orgGroupName">Group Name</digi:trn></b>
															</digi:link>																															
														</c:if>
														<c:if test="${empty aimOrgGroupManagerForm.sortBy || aimOrgGroupManagerForm.sortBy=='nameAscending'}"><img  src="/repository/aim/images/up.gif"/></c:if>
														<c:if test="${not empty aimOrgGroupManagerForm.sortBy && aimOrgGroupManagerForm.sortBy=='nameDescending'}"><img src="/repository/aim/images/down.gif"/></c:if>
													</td>	
													<td align="left" width="25%">
														<jsp:useBean id="urlParams5" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParams5}" property="alpha"><bean:write name="aimOrgGroupManagerForm" property="currentAlpha"/></c:set>
														<c:if test="${empty aimOrgGroupManagerForm.sortBy || aimOrgGroupManagerForm.sortBy!='codeAscending'}">
															<digi:link href="/orgGroupManager.do?sortBy=codeAscending&reset=false&orgSelReset=false" name="urlParams5" style="color:#000000;">
																<b><digi:trn key="aim:orgGroupCode">Code</digi:trn></b>
															</digi:link>																															
														</c:if>
														<c:if test="${not empty aimOrgGroupManagerForm.sortBy && aimOrgGroupManagerForm.sortBy=='codeAscending'}">
															<digi:link href="/orgGroupManager.do?sortBy=codeDescending&reset=false&orgSelReset=false"  name="urlParams5" style="color:#000000;">
																<b><digi:trn key="aim:orgGroupCode">Code</digi:trn></b>
															</digi:link>																															
														</c:if>
														<c:if test="${not empty aimOrgGroupManagerForm.sortBy && aimOrgGroupManagerForm.sortBy=='codeAscending'}"><img  src="/repository/aim/images/up.gif"/></c:if>
														<c:if test="${not empty aimOrgGroupManagerForm.sortBy && aimOrgGroupManagerForm.sortBy=='codeDescending'}"><img src="/repository/aim/images/down.gif"/></c:if>
													</td>
													<td align="left" width="25%">
														<jsp:useBean id="urlParams6" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParams6}" property="alpha"><bean:write name="aimOrgGroupManagerForm" property="currentAlpha"/></c:set>
														<c:if test="${empty aimOrgGroupManagerForm.sortBy || aimOrgGroupManagerForm.sortBy!='typeAscending'}">
															<digi:link href="/orgGroupManager.do?sortBy=typeAscending&reset=false&orgSelReset=false" name="urlParams6" style="color:#000000;">
																<b><digi:trn key="aim:orgGroupType">Type</digi:trn></b>
															</digi:link>																														
														</c:if>
														<c:if test="${not empty aimOrgGroupManagerForm.sortBy && aimOrgGroupManagerForm.sortBy=='typeAscending'}">
															<digi:link href="/orgGroupManager.do?sortBy=typeDescending&reset=false&orgSelReset=false" name="urlParams6" style="color:#000000;">
																<b><digi:trn key="aim:orgGroupType">Type</digi:trn></b>
															</digi:link>																														
														</c:if>
														<c:if test="${not empty aimOrgGroupManagerForm.sortBy && aimOrgGroupManagerForm.sortBy=='typeAscending'}"><img  src="/repository/aim/images/up.gif"/></c:if>
														<c:if test="${not empty aimOrgGroupManagerForm.sortBy && aimOrgGroupManagerForm.sortBy=='typeDescending'}"><img src="/repository/aim/images/down.gif"/></c:if>
																													
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td>
											<div style="overflow: auto; width: 100%; height: 180px; max-height: 180px;">
												<table width="100%" cellspacing="0" cellpadding="2" id="dataTable">
													<logic:iterate name="aimOrgGroupManagerForm" property="organisation" id="organisation">
		                                                <tr height="25">
															<td width="50%">
															  <jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
															  <c:set target="${urlParams}" property="action" value="edit" />
															  <c:set target="${urlParams}" property="ampOrgGrpId">
															  	<bean:write name="organisation" property="ampOrgGrpId" />
															  </c:set>
															  <digi:link href="/editOrgGroup.do" name="urlParams">
															  	<bean:write name="organisation" property="orgGrpName" />
															  </digi:link>
															</td>
															<td width="25%">
																<logic:empty name="organisation" property="orgGrpCode">
																	<c:out value="-" />
																</logic:empty>
																<logic:notEmpty name="organisation" property="orgGrpCode">
																	<bean:write name="organisation" property="orgGrpCode" />
																</logic:notEmpty>
		                                                    </td>
															<td width="25%">
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
											</div>
										</td>
									</tr>
								</table>	
							</td>
						</tr>
					</logic:notEmpty>
					<tr height="20">
						<td>&nbsp;
						</td>
					</tr>
					<tr>
						<td>
							<table>
								<tr>
									<logic:notEmpty name="aimOrgGroupManagerForm" property="pages">
									<td colspan="4">
										<div  style="   float:left;">
											<table style="padding:5px;" >
												<tr id="rowHighlight">
													<logic:iterate name="aimOrgGroupManagerForm" 	property="pages" id="pages" type="java.lang.Integer">
														<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParams1}" property="page"><%=pages%></c:set>
														<c:set target="${urlParams1}" property="sortBy">${aimOrgGroupManagerForm.sortBy}</c:set>
														<c:if test="${aimOrgGroupManagerForm.currentPage == pages}">
														<td style="padding:3px;border:2px solid #000000; " nowrap="nowrap" >
															<font color="#FF0000"><%=pages%></font>
														</td>
														</c:if>
														<c:if test="${aimOrgGroupManagerForm.currentPage != pages}">
															<td style="padding:3px;border:1px solid #999999;" nowrap="nowrap">
																<digi:link href="/orgGroupManager.do" name="urlParams1"><%=pages%></digi:link>
															</td>
														</c:if>
													</logic:iterate>
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
					
				</table>
			</td>
			<td noWrap width=25% vAlign="top">
				<table align=center cellPadding=0 cellSpacing=0 width="90%" border=0>	
					<tr>
						<td>
							<!-- Other Links -->
							<table cellPadding=0 cellSpacing=0 width=100% height="20">
								<tr>
									<td bgColor=#c9c9c7 class=box-title>
										<digi:trn key="aim:otherLinks">
										Other links
										</digi:trn>
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
<script language="javascript">
	setStripsTable("dataTable", "tableEven", "tableOdd");
	setHoveredTable("dataTable", false);
	setHoveredRow("rowHighlight");
</script>
</digi:form>