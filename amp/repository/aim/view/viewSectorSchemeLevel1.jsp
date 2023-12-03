<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule" %>

<div class="admin-content">
<script langauage="JavaScript">
	function onDelete() {
	<c:set var="translation">
		<digi:trn key="aim:ConfirmDelete">Delete this Sector ?</digi:trn>
	</c:set>
		var flag = confirm("${translation}");
		return flag;
	}
	function updateScheme(id) {
		
			<digi:context name="addScheme" property="context/ampModule/moduleinstance/updateSectorSchemes.do?event=updateScheme" />
			
			document.aimAddSectorForm.action = "<%= addScheme%>&editSchemeId="+id;
			document.aimAddSectorForm.target = "_self";
			document.aimAddSectorForm.submit();
	
	}
</script>


<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<digi:instance property="aimAddSectorForm" />
<digi:context name="digiContext" property="context" />
<digi:form action="/viewSectorDetails.do" method="post">

	<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000
		align=center>
		<tr>
			<td align=left class=r-dotted-lg valign="top" width=750>
				<table cellPadding=5 cellspacing="0" width="100%" border="0">
					<tr>
						<!-- Start Navigation -->
						<td height=33><span class=crumb> <c:set
									var="clickToViewAdmin">
									<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
								</c:set> <digi:link href="/admin.do" styleClass="comment"
									title="${clickToViewAdmin}">
									<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
								</digi:link>&nbsp;&gt;&nbsp; <c:set var="schemes">
									<digi:trn key="aim:schemes">Click here to Sector Schemes</digi:trn>
								</c:set> <digi:link href="/getSectorSchemes.do" styleClass="comment"
									title="${schemes}">
									<digi:trn key="aim:schemes">
						Schemes
						</digi:trn>
								</digi:link>
						</td>
						<!-- End navigation -->
					</tr>
					<!--<tr>
					<td height=16 valign="center" width=571><span class=subtitle-blue>
					<digi:trn key="aim:sectorManager">Sector Manager</digi:trn></span>
					</td>
				</tr>-->

					<tr>
						<td height=16 valign="center" width=571><digi:errors /></td>
					</tr>
					<!-- End of Logo -->
					<html:hidden property="idGot" />
					<tr>
						<td noWrap width="100%" vAlign="top">
							<table width="100%" cellpadding="1" cellspacing="1" border="0">
								<tr>
									<td noWrap width=750 vAlign="top">
										<table cellpadding="1" cellspacing="1" width="100%"
											valign="top">
											<tr bgColor=#ffffff>
												<td vAlign="top" width="100%">

													<table width="100%" cellspacing="1" cellpadding="1"
														valign="top" align=left>

														<tr>
															<td bgcolor=#F2F2F2>
																<table width="100%"
																	style="font-size: 12px; margin: 10px;">
																	<tr>
																		<td style="padding-right: 10px;"><digi:trn
																				key="aim:SchemeName">Scheme Name</digi:trn><font
																			color="red">*</font>:</td>
																		<td><html:textarea name="aimAddSectorForm"
																				property="secSchemeName" rows="1" cols="35" />
																		</td>
																	</tr>
																	<tr>
																		<td style="padding-right: 10px;"><digi:trn
																				key="aim:SchemeCode">Scheme Code</digi:trn><font
																			color="red">*</font>:</td>
																		<td><html:text name="aimAddSectorForm"
																				property="secSchemeCode" size="20" />
																		</td>
																	</tr>



																	<tr>
																		<td>&nbsp;</td>
																		<td valign=top><input type="button"
																			style="margin-top: 10px;" class="buttonx"
																			name="addBtn"
																			value='<digi:trn jsFriendly="true" key="btn:save">Save</digi:trn>'
																			onclick="updateScheme('<bean:write name="aimAddSectorForm" property="secSchemeId" />')" />
																		<td>
																	</tr>
																</table></td>

														</tr>

														<field:display name="Level 1 Sectors List"
															feature="Sectors">
															<tr>
																<td class="report">
																	<table width="100%" cellspacing="1" cellpadding=4
																		valign="top" align=left bgcolor="#cccccc"
																		style="font-size: 12px;">
																		<thead>
																			<tr>
																				<td colspan="3" bgColor=#c7d4db class=box-title
																					height="25" align="center">
																					<!-- Table title -->
																					<b style="font-size: 12px;">
																						<digi:trn key="aim:LeveloneSectors">Level One Sectors</digi:trn>
																					</b>
																					<!-- end table title -->
																				</td>
																			</tr>
																		</thead>


																		<tbody class="yui-dt-data">
																			<logic:empty name="aimAddSectorForm"
																				property="formFirstLevelSectors">
																				<tr bgcolor="#ffffff">
																					<td colspan="5" align="center"><b> <digi:trn
																								key="aim:noSectorPresent">
															No Sector present
															</digi:trn> </b>
																					</td>
																				</tr>
																			</logic:empty>

																			<logic:notEmpty name="aimAddSectorForm"
																				property="formFirstLevelSectors">
																				<logic:iterate name="aimAddSectorForm"
																					property="formFirstLevelSectors"
																					id="sectorSchemeLevelOne"
																					type="org.digijava.ampModule.aim.dbentity.AmpSector	">
																					<tr>
																						<td bgcolor="#ffffff" width="73%"><jsp:useBean
																								id="urlParams2" type="java.util.Map"
																								class="java.util.HashMap" /> <c:set
																								target="${urlParams2}" property="ampSectorId">
																								<bean:write name="sectorSchemeLevelOne"
																									property="ampSectorId" />
																							</c:set> <c:set target="${urlParams2}" property="event"
																								value="edit" /> <c:set target="${urlParams2}"
																								property="level" value="two" /> <c:set
																								var="clickToViewSector">
																								<digi:trn key="aim:clickToViewSector">Click here to view Sector</digi:trn>
																							</c:set> <digi:link href="/viewSectorDetails.do"
																								name="urlParams2" title="${clickToViewSector}">
																								<bean:write name="sectorSchemeLevelOne"
																									property="name" />
																							</digi:link>
																						</td>

																						<td bgcolor="#ffffff" width="15%" align="center"
																							class="ignore"><c:set
																								var="clickToEditSector">
																								<digi:trn key="aim:clickToEditSector">Click here to Edit Sector</digi:trn>
																							</c:set> <c:set var="edittext">
																								<digi:trn key="aim:edit">Edit</digi:trn>
																							</c:set> [ <digi:link href="/viewSectorDetails.do"
																								name="urlParams2" title="${clickToEditSector}">${edittext}</digi:link>
																							]</td>
																						<td bgcolor="#ffffff" width="12%" align="center"
																							class="ignore"><jsp:useBean id="urlParams4"
																								type="java.util.Map" class="java.util.HashMap" />
																							<c:set target="${urlParams4}"
																								property="ampSectorId">
																								<bean:write name="sectorSchemeLevelOne"
																									property="ampSectorId" />

																							</c:set> <c:set target="${urlParams4}"
																								property="schemeId">

																								<bean:write name="aimAddSectorForm"
																									property="parentId" />
																							</c:set> <c:set target="${urlParams4}" property="event"
																								value="delete" /> <c:set
																								var="clickToDeleteSector">
																								<digi:trn key="aim:clickToDeleteSector">Click here to Delete Sector</digi:trn>
																							</c:set> <c:set var="deletetext">
																								<digi:trn key="aim:delete">Delete</digi:trn>
																							</c:set> [ <digi:link href="/deleteSector.do"
																								name="urlParams4" title="${clickToDeleteSector}"
																								onclick="return onDelete()">${deletetext}</digi:link>
																							]</td>
																					</tr>
																				</logic:iterate>



																			</logic:notEmpty>
																		</tbody>
																		<!-- end page logic -->
																	</table></td>
															</tr>
														</field:display>
													</table></td>
											</tr>
										</table></td>

									<td noWrap width="100%" vAlign="top">
										<table align="center" cellpadding="0" cellspacing="0"
											width="90%" border="0">
											<tr>
												<td>
													<!-- Other Links -->
													<table cellpadding="0" cellspacing="0" width="120">
														<tr>
															<td bgColor=#c9c9c7 class=box-title>
																	<b style="font-size: 12px; padding-left: 5px;"><digi:trn
																	key="aim:otherLinks">Other
																		links</digi:trn></b>
															</td>
															<td class="header-corner"
																height="17" width="17"></td>
														</tr>
													</table></td>
											</tr>
											<tr>
												<td bgColor=#ffffff class=box-border>
													<table cellPadding=5 cellspacing="1" width="100%"
														class="inside">
														<tr>
															<td class="inside"><digi:img
																	src="ampModule/aim/images/arrow-014E86.gif" styleClass="list-item-image" width="15"
																	height="10" /> <c:set var="clickToViewAdmin">
																	<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
																</c:set> <digi:link href="/admin.do" title="${clickToViewAdmin}">
																	<digi:trn key="aim:AmpAdminHome">
												Admin Home
												</digi:trn>
																</digi:link>
															</td>
														</tr>
														<field:display name="Add Sector Level 1 Link"
															feature="Sectors">
															<tr>
																<td class="inside"><jsp:useBean id="urlParams5"
																		type="java.util.Map" class="java.util.HashMap" /> <c:set
																		target="${urlParams5}" property="ampSecSchemeId">
																		<bean:write name="aimAddSectorForm"
																			property="secSchemeId" />
																	</c:set> <c:set target="${urlParams5}" property="parent"
																		value="scheme" /> <digi:img
																		src="ampModule/aim/images/arrow-014E86.gif" styleClass="list-item-image" width="15"
																		height="10" /> <c:set var="clickToAddSector">
																		<digi:trn key="aim:clickToAddSector">Click here to Add a Sector</digi:trn>
																	</c:set> <digi:link href="/addSector.do" name="urlParams5"
																		title="${clickToAddSector}">
																		<digi:trn key="aim:addSector">
												Add Sector
												</digi:trn>
																	</digi:link>
																</td>
															</tr>
														</field:display>
														<field:display name="View Schemes Link" feature="Sectors">
															<tr>
																<td class="inside"><digi:img
																		src="ampModule/aim/images/arrow-014E86.gif" styleClass="list-item-image" width="15"
																		height="10" /> <c:set var="clickToViewSchemes">
																		<digi:trn key="aim:clickToViewSchemes">Click here to the Schemes</digi:trn>
																	</c:set> <digi:link href="/getSectorSchemes.do"
																		title="${clickToViewSchemes}">
																		<digi:trn key="aim:viewSchemes">
												View Schemes
												</digi:trn>
																	</digi:link>
																</td>
															</tr>
														</field:display>
														
														<!-- end of other links -->
													</table></td>
											</tr>
										</table></td>
								</tr>
							</table></td>
					</tr>
				</table></td>
		</tr>
	</table>
</digi:form>
</div>
