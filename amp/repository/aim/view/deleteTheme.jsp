<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">
<!--

function cancel() {
	<digi:context name="cancel" property="context/module/moduleinstance/themeManager.do" />
	document.aimAddThemeForm.action = "<%= cancel %>";
	document.aimAddThemeForm.target = "_self"
	document.aimAddThemeForm.submit();	
}

-->
</script>

<digi:instance property="aimAddThemeForm" />
<digi:form action="/deleteTheme.do" method="post">

<html:hidden property="themeId" />

<table width="100%" cellSpacing=0 cellPadding=0 vAlign="top" align="left">
<tr><td>
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
</td></tr>
<tr><td>
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
	
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<c:set var="translation">
							<digi:trn key="aim:clickToViewThemeManager">Click here to view Theme Manager</digi:trn>
						</c:set>
						<digi:link href="/themeManager.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:programManager">	
						Program Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:deleteProgram">
						Delete Program
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>
						<digi:trn key="aim:programManager">	
						Program Manager
						</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=0 cellSpacing=0>
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
										<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="97%" border=0>	
											<tr>
												<td>
													<digi:errors/>
												</td>
											</tr>
											<tr>
												<td bgColor=#ffffff>
													<table border=0 cellPadding=0 cellSpacing=0 width="100%" bgcolor="#dddddd">
														<tr bgColor=#dddddb>
															<!-- header -->
															<td bgColor=#dddddb height="20"	align="center" colspan="5"><B>
																<digi:trn key="aim:deleteProgram">
																Delete Program
															</digi:trn>
															<!-- end header -->
														</tr>
														<!-- Page Logic -->
														<tr bgcolor="#dddddd">
															<td width="100%">	
																<table width="100%" border=0 vAlign="top" align="left"
																cellPadding=4 cellSpacing=1 bgcolor="#dddddd">
																	<tr>
																		<td align="right" bgcolor="#f4f4f2" width="100">
																		<digi:trn key="aim:programName">Program Name</digi:trn>	</td>
																	    <td bgcolor="#f4f4f2">
																		 	<c:out value="${aimAddThemeForm.themeName}"/>
																	    </td>
																	</tr>
																	<tr>
																		<td align="right" bgcolor="#f4f4f2">
																	        <digi:trn key="aim:programCode">Program Code</digi:trn>	
																		</td>
																	    <td bgcolor="#f4f4f2">
																			 <c:out value="${aimAddThemeForm.themeCode}"/>	
																		</td>
																	</tr>
																	<tr>
																		<td align="right" bgcolor="#f4f4f2">
																	        <digi:trn key="aim:description">Description</digi:trn>	
																		</td>
																		<td bgcolor="#f4f4f2">
																			<c:out value="${aimAddThemeForm.description}"/>
																		</td>
																	</tr>
																	<tr>
																		<td align="right" bgcolor="#f4f4f2">
																	        <digi:trn key="aim:themeType">Type
																			</digi:trn>		
																		</td>
																		<td bgcolor="#f4f4f2">
																			<c:out value="${aimAddThemeForm.type}"/>
																		</td>
																	</tr>
																	<c:if test="${aimAddThemeForm.flag == 'delete'}">
																	<tr>
																		<td align="center" colspan="2" bgcolor="#ffffff">
																			<b><digi:trn key="aim:confirmDeleteTheme">
																			Are you sure about deleting this program ?
																			</digi:trn></b>
																		</td>
																	</tr>																	
																	<tr>
																		<td colspan="2" align="center" bgcolor="#ffffff">
																			<table width="100%" cellspacing="5">
																				<tr>
																					<td width="50%" align="right">
																					<html:submit styleClass="dr-menu" value="Delete" />
																				</td>
																					<td width="50%" align="left">
																						<html:reset value="Cancel" styleClass="dr-menu" onclick="cancel()"/>
																					</td>
																				</tr>
																			</table>
																		</td>
																	</tr>																	
																	</c:if>
																	<c:if test="${aimAddThemeForm.flag == 'activityReferences'}">
																	<tr>
																		<td colspan="2" align="center">
																			<b><digi:trn key="aim:cannotDeleteProgram">
																			Cannot delete this program as one or more activities reference it.
																			</digi:trn></b>
																		</td>
																	</tr>																	
																	</c:if>																	
																</table>	
															</td>
														</tr>
													<!-- end page logic -->
													</table>
												</td>
											</tr>
											
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
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>



