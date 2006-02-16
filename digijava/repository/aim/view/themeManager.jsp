<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:errors/>
<digi:instance property="aimThemeForm" />
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
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</bean:define>
						<digi:link href="/admin.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:programManager">
						Program Manager
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
										<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="97%" border=0>	
											<tr>
												<td bgColor=#ffffff class=box-border>
													<table border=0 cellPadding=1 cellSpacing=1 class=box-border-nopadding width="100%">
														<tr bgColor=#dddddb>
															<!-- header -->
															<td bgColor=#dddddb height="20" align="center" colspan="5"><B>
																<digi:trn key="aim:programs">
																Programs
																</digi:trn>						
															</td>
															<!-- end header -->
														</tr>
														<logic:empty name="aimThemeForm" property="themes">
														<tr>
															<td align="center">
																<b>
																No programs present
																</b>
															</td>
														</tr>
														</logic:empty>
														<logic:notEmpty name="aimThemeForm" property="themes">
														<tr>
															<td>
																<table width="100%" border="0" bgColor=#dddddd cellPadding=3 cellSpacing=1>
																	<%--
																	<tr>
																		<td colspan="3" bgcolor="#f4f4f2">
																			<b>
																				<digi:trn key="aim:programName">
																					Program Name
																				</digi:trn>
																			</b>
																		</td>
																	</tr>
																	--%>
																	<logic:iterate name="aimThemeForm" property="themes" id="themes" 
																	type="org.digijava.module.aim.dbentity.AmpTheme">
																	<tr>
																		<td bgcolor="#f4f4f2">
																			<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
																			<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																			<c:set target="${urlParams2}" property="id">
																				<bean:write name="themes" property="ampThemeId" />
																			</c:set>
																			<c:set target="${urlParams}" property="id">
																				<bean:write name="themes" property="ampThemeId" />
																			</c:set>
																			<%--
																			<digi:link href="/viewTheme.do" name="urlParams2" 
																			<bean:define id="translation">
																				<digi:trn key="aim:clickToViewTheProgram">Click here to view the program</digi:trn>
																			</bean:define>
																			title="<%=translation%>" >
																			--%>
																				<bean:write name="themes" property="name"/>
																			<%--	
																			</digi:link>
																			--%>
																		</td>
																		<td align="center" width="40" bgcolor="#f4f4f2">
																			<c:set target="${urlParams}" property="action" value="edit" />
																			[ 
																			<bean:define id="translation">
																				<digi:trn key="aim:clickToEditTheProgram">Click here to edit the program</digi:trn>
																			</bean:define>
																			<digi:link href="/getTheme.do" name="urlParams" 
																			title="<%=translation%>" >
																			<digi:trn key="aim:themeManagerEdit">
																				Edit
																			</digi:trn>
																			</digi:link> ]
																		</td>
																		<td align="center" width="58" bgcolor="#f4f4f2">
																			<c:set target="${urlParams}" property="action" value="delete" />
																			[ 
																			<bean:define id="translation">
																				<digi:trn key="aim:clickToDeleteProgram">Click here to delete program</digi:trn>
																			</bean:define>
																			<digi:link href="/getTheme.do" name="urlParams" 
																			title="<%=translation%>" >
																			<digi:trn key="aim:themeManagerDelete">
																				Delete
																			</digi:trn>
																			</digi:link> ]
																		</td>
																	</tr>
																	</logic:iterate>
																</table>
															</td>
														</tr>
														</logic:notEmpty>
														<logic:notEmpty name="aimThemeForm" property="pages">
														<tr>
															<td>
																<digi:trn key="aim:themeManagerPages">
																Pages :
																</digi:trn>
																<logic:iterate name="aimThemeForm" property="pages" id="pages" type="java.lang.Integer">
																	<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
																	<c:set target="${urlParams1}" property="page">
																		<%=pages%>
																	</c:set>
																	<bean:define id="translation">
																		<digi:trn key="aim:clickToGoToNext">Click here to go to next page</digi:trn>
																	</bean:define>
																	<digi:link href="/themeManager.do" name="urlParams1" 
																	title="<%=translation%>" >
																		<%=pages%>
																	</digi:link>
																	|&nbsp; 
																</logic:iterate>
															</td>
														</tr>
														</logic:notEmpty>
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
														<bean:define id="translation">
															<digi:trn key="aim:clickToAddProgram">Click here to add program</digi:trn>
														</bean:define>
														<digi:link href="/addTheme.do" title="<%=translation%>" >
						<digi:trn key="aim:addProgram">
						Add program
						</digi:trn>
						</digi:link>
												</td>
											</tr>
											<tr>
												<td>
													<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
													<bean:define id="translation">
														<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
													</bean:define>
													<digi:link href="/admin.do" title="<%=translation%>" >
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
