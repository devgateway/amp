<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script langauage="JavaScript">
	function onDelete() {
		var flag = confirm("Delete this Program Type?");
		return flag;
	}
</script>

<digi:instance property="aimProgramTypeForm" />
<digi:form action="/programTypeManager.do" method="post">
<digi:context name="digiContext" property="context" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->


<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%" border="0">
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
					<c:set var="ToViewAdmin">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
					</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${ToViewAdmin}">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:ProgramTypeManager">
						 Program Type Manager
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 valign="center" width=571><span class=subtitle-blue>Program Type Manager</span>
					</td>
				</tr>
				<tr>
					<td height=16 valign="center" width=571>
						<digi:errors />
					</td>
				</tr>				
				<tr>
					<td noWrap width="100%" vAlign="top">
					<table width="100%" cellspacing="1" cellspacing="1" border="0">
					<tr><td noWrap width=600 vAlign="top">
						<table bgColor=#d7eafd cellpadding="1" cellspacing="1" width="100%" valign="top">
							<tr bgColor=#ffffff>
								<td vAlign="top" width="100%">
									
									<table width="100%" cellspacing="1" cellpadding="1" valign="top" align=left>	
										<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
											<!-- Table title -->
											<digi:trn key="aim:programTypes">
												Program Types
											</digi:trn>
											<!-- end table title -->										
										</td></tr>
										<tr><td>
											<table width="100%" cellspacing="1" cellpadding=4 valign="top" align=left bgcolor="#d7eafd">
												<logic:empty name="aimProgramTypeForm" property="programNames">
													<tr bgcolor="#ffffff">
														<td colspan="5" align="center"><b>
															<digi:trn key="aim:noProgramTypesPresent">
																	No Program Types persent
															</digi:trn>
														</b></td>
													</tr>
													</logic:empty>
													<logic:notEmpty name="aimProgramTypeForm" property="programNames">
													<logic:iterate name="aimProgramTypeForm" property="programNames" id="programNames"
																type="org.digijava.module.aim.dbentity.AmpProgramType">	
													
													<tr>
														<td bgcolor="#ffffff">
														  <jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams2}" property="prgTypeId">
															<bean:write name="programNames" property="ampProgramId"/>	
															</c:set>
															<c:set target="${urlParams2}" property="event" value="editPrgType"/>
															<c:set var="ToEditProgramType">
																<digi:trn key="aim:clickToEditProgramType">Click here to Edit Program Type</digi:trn>
															</c:set>		
															<digi:link href="/programTypeManager.do"  name ="urlParams2" title="${ToEditProgramType}" >
																<bean:write name="programNames" property="title"/>
															</digi:link>
					
														</td>
														<td bgcolor="#ffffff" width="40" align="center">
															<c:set var="ToEditProgramType1">
																<digi:trn key="aim:clickToEditProgramType">Click here to Edit Program Type</digi:trn>
															</c:set>
															[ <digi:link href="/programTypeManager.do"  name ="urlParams2" title="${ToEditProgramType1}" >Edit</digi:link> ]
														</td>
														<td bgcolor="#ffffff" width="55" align="center">
															<jsp:useBean id="urlParams4" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams4}" property="prgTypeId">
															<bean:write name="programNames" property="ampProgramId"/>	
															</c:set>
															<c:set target="${urlParams4}" property="event" value="delete"/>
															<c:set var="ToDeleteComponent">
																	<digi:trn key="aim:clickToDeleteComponent">Click here to Delete Component</digi:trn>
															</c:set>
															[ <digi:link href="/programTypeManager.do" name="urlParams4" 
																title="${ToDeleteComponent}" onclick="return onDelete()">Delete</digi:link> ]
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
					
					<td noWrap width="100%" vAlign="top">
						<table align="center" cellpadding="0" cellspacing="0" width="90%" border="0">	
							<tr>
								<td>
									<!-- Other Links -->
									<table cellpadding="0" cellspacing="0" width="120">
										<tr>
											<td bgColor=#c9c9c7 class=box-title>
												<digi:trn key="aim:otherLinks">
												Other links
												</digi:trn>
											</td>
											<td background="module/aim/images/corner-r.gif" height="17" width="17">&nbsp;
												
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellspacing="1" width="100%">
										<tr>
											<td nowrap  class="inside">
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="ToAddProgramType">
													<digi:trn key="aim:clickToAddProgramType">Click here to Add a Program Type</digi:trn>
												</c:set>
												
												<digi:link href="/programTypeManager.do?event=add"  title="${translation}" >
												<digi:trn key="aim:addProgramType">
												Add Program type
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										<tr>
											<td class="inside">
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="ToViewAdmin">
														<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
												</c:set>
												<digi:link href="/admin.do" title="${ToViewAdmin}" >
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
</digi:form>

