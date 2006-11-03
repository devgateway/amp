<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script langauage="JavaScript">
	function onDelete() {
		var flag = confirm("Delete this Component?");
		return flag;
	}
</script>

<digi:instance property="aimComponentsForm" />
<digi:context name="digiContext" property="context" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->


<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
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
						<digi:trn key="aim:componentManager">
						Component Manager
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>Components Manager</span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<html:errors />
					</td>
				</tr>				
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1 border=0>
					<tr><td noWrap width=600 vAlign="top">
						<table bgColor=#d7eafd cellPadding=1 cellSpacing=1 width="100%" valign="top">
							<tr bgColor=#ffffff>
								<td vAlign="top" width="100%">
									
									<table width="100%" cellspacing=1 cellpadding=1 valign=top align=left>	
										<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
											<!-- Table title -->
											<digi:trn key="aim:components">
												Components
											</digi:trn>
											<!-- end table title -->										
										</td></tr>
										<tr><td>
											<table width="100%" cellspacing=1 cellpadding=4 valign=top align=left bgcolor="#d7eafd">
													<logic:empty name="aimComponentsForm" property="components">
													<tr bgcolor="#ffffff">
														<td colspan="5" align="center"><b>
															<digi:trn key="aim:noComponents">
															No Components present
															</digi:trn>
														</b></td>
													</tr>
													</logic:empty>
													<logic:notEmpty name="aimComponentsForm" property="components">
													<logic:iterate name="aimComponentsForm" property="components" id="componentlist"
																	type="org.digijava.module.aim.dbentity.AmpComponent">
													<tr>
														<td bgcolor="#ffffff">
														<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams2}" property="componentId">
															<bean:write name="componentlist" property="ampComponentId" />
															</c:set>
															<c:set target="${urlParams2}" property="event" value="edit"/>
															<bean:define id="translation">

																<digi:trn key="aim:clickToEditComponents">Click here to Edit</digi:trn>
															</bean:define>		
															<digi:link href="/updateComponents.do" name="urlParams2" title="<%=translation%>" >
																<bean:write name="componentlist" property="title"/>
															</digi:link>
														</td>
														<td bgcolor="#ffffff" width="40" align="center">
															<bean:define id="translation">
																<digi:trn key="aim:clickToEditComponent">Click here to Edit Component</digi:trn>
															</bean:define>
															[ <digi:link href="/updateComponents.do" name="urlParams2" title="<%=translation%>" >Edit</digi:link> ]
														</td>
											 
														<%--<logic:equal name="aimAddSectorForm" property="deleteSchemeFlag" value="true">--%>
														<td bgcolor="#ffffff" width="55" align="center">
															<jsp:useBean id="urlParams4" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams4}" property="componentId">
																<bean:write name="componentlist" property="ampComponentId" />
															</c:set>
															<c:set target="${urlParams4}" property="event" value="delete"/>
															<bean:define id="translation">
																<digi:trn key="aim:clickToDeleteComponent">Click here to Delete Component</digi:trn>
															</bean:define>
															[ <digi:link href="/updateComponents.do" name="urlParams4" 
																title="<%=translation%>" onclick="return onDelete()">Delete</digi:link> ]
														</td>
													</tr>
													</logic:iterate>
													</logic:notEmpty>
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
											<td nowrap>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<bean:define id="translation">
													<digi:trn key="aim:clickToAddComponent">Click here to Add a Component</digi:trn>
												</bean:define>
												<digi:link href="/updateComponents.do?event=add"  title="<%=translation%>" >
												<digi:trn key="aim:addComponent">
												Add Component
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
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
					</td></tr>
					</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>


