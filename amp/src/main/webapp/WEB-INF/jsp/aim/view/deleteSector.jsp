<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:errors/>
<digi:instance property="aimAddSectorForm" />
<digi:form action="/deleteSector.do" method="post">

<html:hidden property="sectorId" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%">
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
							<digi:trn key="aim:clickToViewSectorManager">Click here to view Sector Manager</digi:trn>
						</c:set>
						<digi:link href="/sectorManager.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:sectorManager">
						Sector Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:deleteSector">Delete Sector</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 valign="center" width=571><span class=subtitle-blue>
						<digi:trn key="aim:sectorManager">
						Sector Manager
						</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td noWrap width="100%" vAlign="top">
					<table width="100%" cellspacing="1" cellspacing="1">
					<tr>
						<td noWrap width=600 vAlign="top">
							<table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="100%">
								<tr bgColor=#f4f4f2>
									<td vAlign="top" width="100%">
										&nbsp;
									</td>
								</tr>
								<tr bgColor=#f4f4f2>
									<td valign="top">
										<table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="90%" border="0">	
											<tr>
												<td bgColor=#ffffff class=box-border>
													<table border="0" cellpadding="1" cellspacing="1" class=box-border width="100%">
														<tr bgColor=#dddddb>
															<!-- header -->
															<td bgColor=#dddddb height="20" 			align="center" colspan="5"><B>
																<digi:trn key="aim:deleteSector">Delete Sector</digi:trn>
															<!-- end header -->
														</tr>
														<!-- Page Logic -->
														<tr>
															<td width="100%">	
																<table width="100%" border="0"	 bgColor=#f4f4f2>
																	<tr>
																		<td width="30%" align="right">
																		<digi:trn key="aim:sectorName">Sector Name</digi:trn>
																		</td>
																	    <td width="30%" >
																	          <bean:write name="aimAddSectorForm" property="sectorName" />
																	    </td>
																	</tr>
																	<tr>
																		<td width="30%" align="right">
																	        <digi:trn key="aim:sectorCode">Sector Code</digi:trn>
																		</td>
																	    <td width="30%">
																           <bean:write name="aimAddSectorForm" property="sectorCode" />
																		</td>
																	</tr>
																	<tr>
																		<td width="30%" align="right">
																	        <digi:trn key="aim:sectorDescription">Description</digi:trn>
																		</td>
																		<td width="30%">
																           <bean:write name="aimAddSectorForm" property="description" />
																		</td>
																	</tr>
																	<tr>
																		<td width="30%" align="right">
																	        <digi:trn key="aim:sectorOrganisation">Organisation</digi:trn>
																		</td>
																		<td width="30%">
																	          <bean:write name="aimAddSectorForm" property="ampOrganisation" />
																		</td>
																	</tr>
																	
																	<logic:equal name="aimAddSectorForm" property="flag" value="delete">
		<tr>
			<td align="center" colspan="2">
				<b><digi:trn key="aim:confirmDeleteSector">
				Are you sure you want to delete this sector?
				</digi:trn></b>
			</td>
		</tr>
		<tr>
			<td colspan="2" width="60%">
				<table width="100%" cellspacing="5">
					<tr>
						<td width="50%" align="right">
							<html:submit value="Delete" />
						</td>
						<td width="50%" align="left">
							<html:button property="button" value="Cancel" onclick="history.go(-1)"/>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</logic:equal>
	<logic:equal name="aimAddSectorForm" property="flag" value="subSectorExist">
		<tr>
			<td colspan="2" align="center">
				<b><digi:trn key="aim:cannotDeleteSectorMsg1">
				Cannot Delete the sector since sub sectors exist
				</digi:trn></b>
			</td>
		</tr>
	</logic:equal>
	<logic:equal name="aimAddSectorForm" property="flag" value="activityExist">
		<tr>
			<td colspan="2" align="center">
				<b><digi:trn key="aim:cannotDeleteSectorMsg2">			
				Cannot Delete the sector since activity exist
				</digi:trn></b>
			</td>
		</tr>
	</logic:equal>
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
						<td noWrap width="100%" vAlign="top">
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










