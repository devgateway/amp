<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<div class="admin-content">
<digi:errors/>
<digi:instance property="aimSectorsForm" />
<digi:context name="digiContext" property="context" />

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
						<digi:trn key="aim:sectorManager">
						Sector Manager
						</digi:trn>
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
									<td vAlign="top" width="100%">&nbsp;
										
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
															<td bgColor=#dddddb height="20" align="center" colspan="5">
																<logic:empty name="aimSectorsForm" property="parentSector">
																	<b>
																		<digi:trn key="aim:mainSectors">Main Sectors</digi:trn>
																	</b>
																</logic:empty>
																<logic:notEmpty name="aimSectorsForm" property="parentSector">			
																	<b>
																		<digi:trn key="aim:subSectorsForTheSector">
																			Sub sectors for the sector 
																		</digi:trn>
																		<bean:write name="aimSectorsForm" property="parentSector" />
																	</b>
																</logic:notEmpty>
															</td>
															<!-- end header -->
														</tr>

														<!-- Page Logic -->
														<logic:empty name="aimSectorsForm" property="sectors">
														<tr>
															<td colspan="5">
																<b>No sectors present</b>
															</td>
														</tr>
														</logic:empty>
														<logic:notEmpty name="aimSectorsForm" property="sectors">
														<tr>
															<td>	
																<table width="100%" border="0">
																	<tr>
																		<td width="40%">
																			<b><digi:trn key="aim:sectorName">Sector Name</digi:trn></b>
																		</td>
																		<td width="20%" colspan="4">
																			<b><digi:trn key="aim:organisation">Organisation</digi:trn></b>
																		</td>
																	</tr>
																	<logic:iterate name="aimSectorsForm" property="sectors" id="sectors" 
																	type="org.digijava.ampModule.aim.helper.Sector">
																	<tr>
																		<td width="40%">
																			<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
																			<c:set target="${urlParams1}" property="sectorId">
																				<bean:write name="sectors" property="sectorId" />
																			</c:set>
																			<c:set var="translation">
																				<digi:trn key="aim:clickToViewSector">Click here to view Sector</digi:trn>
																			</c:set>
																			<digi:link href="/sectorManager.do" name="urlParams1" title="${translation}" >
																				<bean:write name="sectors" property="sectorName"/>
																			</digi:link>						
																		</td>
																		<td width="20%">
																			<bean:write name="sectors" property="orgName"/>
																		</td>
																		<td>
																			<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
																			<c:set target="${urlParams2}" property="id">
																				<bean:write name="sectors" property="sectorId" />
																			</c:set>
																			<c:set var="translation">
																				<digi:trn key="aim:clickToViewSectorDetails">Click here to view Sector Details</digi:trn>
																			</c:set>
																			[ <digi:link href="/viewSectorDetails.do" name="urlParams2" title="${translation}" >
																			<digi:trn key="aim:sectorManagerViewDetails">View Details</digi:trn>
																			</digi:link> ]
																		</td>
																		<td>
																			<c:set var="translation">
																				<digi:trn key="aim:clickToEditSector">Click here to Edit Sector</digi:trn>
																			</c:set>
																			[ <digi:link href="/editSector.do" name="urlParams2" title="${translation}" >
																			<digi:trn key="aim:sectorManagerEdit">Edit</digi:trn>
																			</digi:link> ]						
																		</td>
																		<td>
																			<c:set var="translation">
																				<digi:trn key="aim:clickToDeleteSector">Click here to Delete Sector</digi:trn>
																			</c:set>
																			[ <digi:link href="/deleteSector.do" name="urlParams2" title="${translation}" >
																			<digi:trn key="aim:sectorManagerDelete">Delete</digi:trn>
																			</digi:link> ]												
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
											<!-- end page logic for pagination -->
										</table>
									</td>
								</tr>
								<tr>
									<td bgColor=#f4f4f2>&nbsp;
										
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
												<td background="ampModule/aim/images/corner-r.gif" 	height="17" width="17">&nbsp;
												
												</td>
											</tr>
										</table>
									</td>
								</tr>
									<tr>
									<td bgColor=#ffffff class=box-border>
										<table cellPadding=5 cellspacing="1" width="100%">
											<tr>
												<td class="inside">
													<digi:img src="ampModule/aim/images/arrow-014E86.gif"	width="15" height="10"/>
													<c:set var="translation">
														<digi:trn key="aim:clickToSearchSector">Click here to Search Sector</digi:trn>
													</c:set>
													<digi:link href="/searchSector.do" title="${translation}" >
														<digi:trn key="aim:searchSector">Search a sector</digi:trn>
													</digi:link>
												</td>
											</tr>
											<tr>
												<td class="inside">
													<digi:img src="ampModule/aim/images/arrow-014E86.gif"	width="15" height="10"/>
													<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
													<logic:empty name="aimSectorsForm" property="parentSector">
														<c:set target="${urlParams}" property="parSecId" value="0" />
														<c:set var="translation">
															<digi:trn key="aim:clickToAddMainSector">Click here to Add Main Sector</digi:trn>
														</c:set>
														<digi:link href="/addSector.do" name="urlParams" title="${translation}" >
															<digi:trn key="aim:addMainSector">Add a main sector</digi:trn>
														</digi:link>						
													</logic:empty>
													<logic:notEmpty name="aimSectorsForm" property="parentSector">			
														<c:set target="${urlParams}" property="parSecId">
															<bean:write name="aimSectorsForm" property="parentSectorId" />
														</c:set>
														<c:set var="translation">
															<digi:trn key="aim:clickToAddSubsector">Click here to Add SubSector</digi:trn>
														</c:set>
														<digi:link href="/addSector.do" name="urlParams" title="${translation}" >
															<digi:trn key="aim:addSubSectorFor">Add a sub sector for </digi:trn>
															<bean:write name="aimSectorsForm" property="parentSector" />
														</digi:link>						
													</logic:notEmpty>													
												</td>
											</tr>
											<tr>
												<td class="inside">
													<digi:img src="ampModule/aim/images/arrow-014E86.gif"	width="15" height="10"/>
													<jsp:useBean id="urlParams3" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${urlParams3}" property="sectorId">
														<bean:write name="aimSectorsForm" property="prevViewedSectorId" />
													</c:set>
													<c:set var="translation">
														<digi:trn key="aim:clickToGoBackToParentSector">Click here to go back to Parent Sector</digi:trn>
													</c:set>
													<digi:link href="/sectorManager.do" name="urlParams3" title="${translation}" >
														Back to parent sector
													</digi:link>													
												</td>
											</tr>											
											<tr>
												<td class="inside">
													<digi:img src="ampModule/aim/images/arrow-014E86.gif" width="15" height="10"/>
													<c:set var="translation">
														<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
													</c:set>
													<digi:link href="/admin.do" title="${translation}" >
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
</div>


