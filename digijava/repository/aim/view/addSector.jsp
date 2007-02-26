
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<script langauage="JavaScript">
	function onDelete() {
		var flag = confirm("Delete this Scheme?");
		return flag;
	}
	function updateScheme(id) {
			  
			  
			 if(isEmpty(document.aimAddSectorForm.sectorName.value)==true)
			 {
						alert("please enter a sector name:");
			 }	
			 else if(isEmpty(document.aimAddSectorForm.sectorCode.value)==true)
			 {
						alert("please enter a sector code:");
			 }	
			 else
			 {
			<digi:context name="addSector" property="context/module/moduleinstance/addSector.do?event=addSector" />
			document.aimAddSectorForm.action = "<%= addSector%>&ampSecSchemeIdpoi="+id+"&parent=scheme";
			document.aimAddSectorForm.target = "_self";
			document.aimAddSectorForm.submit();
			 }
	
	}
</script>
<digi:errors/>
<digi:instance property="aimAddSectorForm" />
<digi:form action="/addSector.do" method="post">

<html:hidden property="sectorId" />
<html:hidden property="levelType" />
<html:hidden property="parentId" />

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
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewSectorManager">Click here to view Sector Manager</digi:trn>
						</bean:define>
						<digi:link href="/getSectorSchemes.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:sectorManager">
						Sector Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:addSector">Add Sector</digi:trn>	
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>
						<digi:trn key="aim:sectorManager">
						Sector Manager
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
										<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="90%" border=0>	
											<tr>
												<td bgColor=#ffffff class=box-border>
													<table border=0 cellPadding=1 cellSpacing=1 class=box-border width="100%">
														<tr bgColor=#dddddb>
															<!-- header -->
															<td bgColor=#dddddb height="20" 			align="center" colspan="5"><B>
																<digi:trn key="aim:addSector">Add Sector</digi:trn>	
															<!-- end header -->
														</tr>
														<!-- Page Logic -->
														<tr>
															<td width="100%">	
																<table width="100%" border=0	 bgColor=#f4f4f2>
																	<tr>
																		<td width="30%" align="right">
																		<digi:trn key="aim:sectorName">Sector Name</digi:trn>	
																		</td>
																	    <td width="30%" >
																	          <html:text property="sectorName" size="20" />
																	    </td>
																	</tr>
																	<tr>
																		<td width="30%" align="right">
																	        <digi:trn key="aim:sectorCode">Sector Code</digi:trn>
																		</td>
																	    <td width="30%">
																           <html:text property="sectorCode" size="10" />
																		</td>
																	</tr>
																	<tr>
																		<td width="30%" align="right">
																	        <digi:trn key="aim:sectorDescription">Description</digi:trn>	
																		</td>
																		<td width="30%">
																           <html:text property="description" size="40" />
																		</td>
																	</tr>
																	<tr>
		<%--<td width="30%" align="right">
			<digi:trn key="aim:sectorOrganisation">Organization</digi:trn>		
		</td>
		<td width="30%" align="left">
			<logic:empty name="aimAddSectorForm" property="ampOrganisation">
				<html:select property="ampOrganisationId">
					<html:option value="">---- Select Organization ----</html:option>
					<logic:notEmpty name="aimAddSectorForm" property="organisationList">	
						<html:optionsCollection name="aimAddSectorForm" property="organisationList" value="key" label="value" />
					</logic:notEmpty>
				</html:select>
			</logic:empty>
			<logic:notEmpty name="aimAddSectorForm" property="ampOrganisation">
				<html:hidden property="ampOrganisationId" />
				<b><bean:write name="aimAddSectorForm" property="ampOrganisation" /></b>
			</logic:notEmpty>
		</td>--%>
	</tr>	
	<tr>
		<td colspan="2" width="60%">
			<table width="100%" cellspacing="5">
				<tr>
					<td width="50%" align="right">
					<%--
						<html:submit value="Save" styleClass="dr-menu"/>--%>
							<input  type="button" name="addBtn" value="Save" onclick="updateScheme('<bean:write name="aimAddSectorForm" property="parentId" />')"
					</td>
					<td width="50%" align="left">
						<html:reset value="Cancel" styleClass="dr-menu"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
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
</digi:form>








