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
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<script langauage="JavaScript">
	<c:set var="translation">
		<digi:trn key="aim:ConfirmDelete">Delete this Scheme ?</digi:trn>
	</c:set>
	function onDelete() {
		var flag = confirm("${translation}");
		return flag;
	}
</script>
<digi:instance property="aimAddSectorForm" />
<digi:context name="digiContext" property="context" />
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
<html:hidden property="event" value="view"/>
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
						<digi:trn key="aim:sectorManager">
						Sector Manager
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue><digi:trn key="aim:sectorManager">Sector Manager</digi:trn></span>
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
										<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
											<!-- Table title -->
											<digi:trn key="aim:schemes">Schemes</digi:trn>
											<!-- end table title -->
										</td></tr>
										<tr><td>
											<table width="100%" cellspacing=1 cellpadding=4 valign=top align=left bgcolor="#d7eafd">
													<logic:empty name="aimAddSectorForm" property="formSectorSchemes">
													<tr bgcolor="#ffffff">
														<td colspan="5" align="center"><b>
															<digi:trn key="aim:noSchemes">
															No Schemes present
															</digi:trn>
														</b></td>
													</tr>
													</logic:empty>
													<logic:notEmpty name="aimAddSectorForm" property="formSectorSchemes">
													<logic:iterate name="aimAddSectorForm" property="formSectorSchemes" id="sectorScheme"
																	type="org.digijava.module.aim.dbentity.AmpSectorScheme	">
													<tr>
														<td width="256" bgcolor="#ffffff">
															<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams2}" property="ampSecSchemeId">
															<bean:write name="sectorScheme" property="ampSecSchemeId" />
															</c:set>
															<c:set target="${urlParams2}" property="event" value="edit" />
															<c:set target="${urlParams2}" property="dest" value="admin" />
															<c:set var="clickToViewSchemes">
															<digi:trn key="aim:clickToViewSchemes">Click here to view Schemes</digi:trn>
															</c:set>
															<c:set var="edittext">
															<digi:trn key="aim:edit">Edit</digi:trn>
															</c:set>
															<digi:link href="/updateSectorSchemes.do" name="urlParams2" title="${clickToViewSchemes}" >
															<bean:write name="sectorScheme" property="secSchemeName"/></digi:link>
													  </td>

														<td bgcolor="#ffffff" width="97" align="right">
															<c:set var="trnEditScheme">
																<digi:trn key="aim:clickToEditScheme">Click here to Edit Scheme</digi:trn>
															</c:set>



															[ <digi:link href="/updateSectorSchemes.do" name="urlParams2" title="${trnEditScheme}" >
															 ${edittext} 
															</digi:link>
															]													  </td>

														<%--<logic:equal name="aimAddSectorForm" property="deleteSchemeFlag" value="true">--%>
                                                                                                               
														<td bgcolor="#ffffff" width="75" align="left">
                                                           <c:if test="${!sectorScheme.used}">
															<jsp:useBean id="urlParams4" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams4}" property="ampSecSchemeId">
																<bean:write name="sectorScheme" property="ampSecSchemeId" />
															</c:set>
															<c:set target="${urlParams4}" property="event" value="deleteScheme"/>
															<c:set var="trnDeleteScheme">
																<digi:trn key="aim:clickToDeleteScheme">Click here to Delete Scheme</digi:trn>
															</c:set>
															[ <digi:link href="/updateSectorSchemes.do" name="urlParams4"
																title="${trnDeleteScheme}" onclick="return onDelete()">
																 <digi:trn key="aim:delete">Delete</digi:trn>
															</digi:link>
                                                             ]</c:if>&nbsp;</td>
                                                                                                                        


														<%--<logic:equal name="aimAddSectorForm" property="deleteSchemeFlag" value="false">
															<td colspan="2" align="center">
																<b><digi:trn key="aim:cannotDeleteSectorMsg2">
																	Cannot Delete the sector since activity exist
																</digi:trn></b>
															</td>

														</logic:equal>--%>
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
									<table cellPadding=0 cellSpacing=0 width=200>
										<tr>
											<td bgColor=#c9c9c7 class=box-title>
												<digi:trn key="aim:otherLinks">
												Other links
												</digi:trn>
											</td>
											<td background="module/aim/images/corner-r.gif" height="17" width=17>&nbsp;
												
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellSpacing=1 width="100%">
										<field:display name="Add Scheme Link" feature="Sectors">
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="trnAddScheme">
													<digi:trn key="aim:clickToAddScheme">Click here to Add a Scheme</digi:trn>
												</c:set>
												<digi:link href="/updateSectorSchemes.do?dest=admin&event=addscheme" title="${trnAddScheme}" >
                                                  <digi:trn key="aim:addScheme">
                                                  Add Scheme
                                                  </digi:trn>
												</digi:link>
											</td>
										</tr>
										</field:display>
                                                                             	<field:display name="Multi Sector Configuration" feature="Sectors">
                                                                                     <tr>
                                                                                         <td>
                                                                                             <digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
                                                                                             <c:set var="trnViewConfigurations">
                                                                                                 <digi:trn key="aim:ClickToConfigureClassifications">Click here to see the Configuration of Classifications</digi:trn>
                                                                                             </c:set>
                                                                                             <digi:link href="/getSectorClassConfig.do" title="${trnViewConfigurations}" >
                                                                                                 <digi:trn key="aim:MultiSectorConfiguration">
                                                                                                     Multi Sector Configuration
                                                                                                 </digi:trn>
                                                                                             </digi:link>
                                                                                         </td>
                                                                                     </tr>
																				</field:display>
                                                                         
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="trnViewAdmin">
													<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
												</c:set>
												<digi:link href="/admin.do" title="${trnViewAdmin}" >
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


