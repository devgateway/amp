<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>


<script langauage="JavaScript">
	function onDelete() {
	<c:set var="translation">
		<digi:trn key="aim:ConfirmDelete">Delete this Scheme ?</digi:trn>
	</c:set>
		var flag = confirm("${translation}");
		return flag;
	}
	function updateScheme(id) {
		
			<digi:context name="addScheme" property="context/module/moduleinstance/updateSectorSchemes.do?event=updateScheme" />
			
			document.aimAddSectorForm.action = "<%= addScheme%>&editSchemeId="+id;
			document.aimAddSectorForm.target = "_self";
			document.aimAddSectorForm.submit();
	
	}
</script>

<digi:instance property="aimAddSectorForm" />
<digi:context name="digiContext" property="context" />
<digi:form action="/viewSectorDetails.do" method="post">
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
<html:hidden property="idGot"/>

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
						<c:set var="clickToViewAdmin">
						<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${clickToViewAdmin}" >
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<c:set var="schemes">
							<digi:trn key="aim:schemes">Click here to Sector Schemes</digi:trn>
						</c:set>
						<digi:link href="/getSectorSchemes.do" styleClass="comment" title="${schemes}" >
						<digi:trn key="aim:schemes">
						Schemes
						</digi:trn>
						</digi:link>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>
					<digi:trn key="aim:sectorManager">Sector Manager</digi:trn></span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<digi:errors />
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
										
										<tr>
											<td>	
												<table width="100%">
													<tr>
														<td>
															<digi:trn key="aim:SchemeName">Scheme Name</digi:trn><font color="red">*</font>:
														</td>
														<td>
															<html:textarea  name ="aimAddSectorForm" property="secSchemeName" rows="1" cols= "35"/> 
														</td>
													</tr>
													<tr>
														<td>
															<digi:trn key="aim:SchemeCode">Scheme Code</digi:trn><font color="red">*</font>:
														</td>
														<td>
															<html:text name ="aimAddSectorForm" property="secSchemeCode" size="5"/> 
														</td>
													</tr>


													
													<tr>
														<td>
															&nbsp;
														</td>
														<td >&nbsp;&nbsp;
								<input  type="button" class="dr-menu" name="addBtn" value='<digi:trn key="btn:save">Save</digi:trn>' onclick="updateScheme('<bean:write name="aimAddSectorForm" property="secSchemeId" />')"/>
														<td>
													</tr>
											</table>
											</td>
											
										</tr>	
										
										<field:display name="Level 1 Sectors List" feature="Sectors">
										<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
											<!-- Table title -->
											<digi:trn key="aim:LeveloneSectors">
												Level One Sectors
											</digi:trn>
											<!-- end table title -->										
										</td></tr>
										<tr><td>
											<table width="100%" cellspacing=1 cellpadding=4 valign=top align=left bgcolor="#d7eafd">
													<logic:empty name="aimAddSectorForm" property="formFirstLevelSectors">
													<tr bgcolor="#ffffff">
														<td colspan="5" align="center"><b>
															<digi:trn key="aim:noSectorPresent">
															No Sector present
															</digi:trn>
														</b></td>
													</tr>
													</logic:empty>

													<logic:notEmpty name="aimAddSectorForm" property="formFirstLevelSectors">
													<logic:iterate name="aimAddSectorForm" property="formFirstLevelSectors" id="sectorSchemeLevelOne"
																	type="org.digijava.module.aim.dbentity.AmpSector	">
													<tr> 
														<td bgcolor="#ffffff">
															<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams2}" property="ampSectorId">
															<bean:write name="sectorSchemeLevelOne" property="ampSectorId" />
															</c:set>
															<c:set target="${urlParams2}" property="event" value="edit" />
															<c:set target="${urlParams2}" property="level" value="two" />
															<c:set var="clickToViewSector">
															<digi:trn key="aim:clickToViewSector">Click here to view Sector</digi:trn>
															</c:set>
															<digi:link href="/viewSectorDetails.do" name="urlParams2" title="${clickToViewSector}" >
															<bean:write name="sectorSchemeLevelOne" property="name"/></digi:link>
														</td>
														
														<td bgcolor="#ffffff" width="75" align="center">
														<c:set var="clickToEditSector">
															<digi:trn key="aim:clickToEditSector">Click here to Edit Sector</digi:trn>
														</c:set>
														<c:set var="edittext">
															<digi:trn key="aim:edit">Edit</digi:trn>
														</c:set>
														
															[ <digi:link href="/viewSectorDetails.do" name="urlParams2" title="${clickToEditSector}">${edittext}</digi:link> ]
														</td>
														<td bgcolor="#ffffff" width="75" align="center">
															<jsp:useBean id="urlParams4" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams4}" property="ampSectorId">
																<bean:write name="sectorSchemeLevelOne" property="ampSectorId" />
																
															</c:set>
															<c:set target="${urlParams4}" property="schemeId">
					
																<bean:write name="aimAddSectorForm" property="parentId" />
															</c:set>

															<c:set target="${urlParams4}" property="event" value="delete"/>
															<c:set var="clickToDeleteSector">
															<digi:trn key="aim:clickToDeleteSector">Click here to Delete Sector</digi:trn>
															</c:set>
															<c:set var="deletetext">
															<digi:trn key="aim:delete">Delete</digi:trn>
															</c:set>
															
															[ <digi:link href="/deleteSector.do" name="urlParams4" 
																title="${clickToDeleteSector}" onclick="return onDelete()">${deletetext}</digi:link> ]
																
														</td>
													</tr>
													</logic:iterate>

																									
													
													</logic:notEmpty>
													<!-- end page logic -->													
											</table>
										</td></tr>
									</field:display>									
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
										<field:display name="Add Sector Level 1 Link" feature="Sectors">
										<tr>
											<td>
											<jsp:useBean id="urlParams5" type="java.util.Map" class="java.util.HashMap"/>
												<c:set target="${urlParams5}" property="ampSecSchemeId">
													<bean:write name="aimAddSectorForm" property="secSchemeId" />
												</c:set>
												<c:set target="${urlParams5}" property="parent" value="scheme"/>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="clickToAddSector">
												<digi:trn key="aim:clickToAddSector">Click here to Add a Sector</digi:trn>
												</c:set>
												<digi:link href="/addSector.do" name="urlParams5" title="${clickToAddSector}" >
												<digi:trn key="aim:addSector">
												Add Sector
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										</field:display>
										<field:display name="View Schemes Link" feature="Sectors">
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="clickToViewSchemes">
												<digi:trn key="aim:clickToViewSchemes">Click here to the Schemes</digi:trn>
												</c:set>
												<digi:link href="/getSectorSchemes.do" title="${clickToViewSchemes}" >
												<digi:trn key="aim:viewSchemes">
												View Schemes
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										</field:display>
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="clickToViewAdmin">
												<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
												</c:set>
												<digi:link href="/admin.do" title="${clickToViewAdmin}" >
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


