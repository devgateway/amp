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
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<script langauage="JavaScript">
	<c:set var="translation">
		<digi:trn>Delete this Sector ?</digi:trn>
	</c:set>
	function onDelete() {
		var flag = confirm("${translation}");
		return flag;
	}
	 function exportXSL(){
     <digi:context name="exportUrl" property="context/ampModule/moduleinstance/exportSectorManager2XSL.do"/>;
     document.aimAddSectorForm.action="${exportUrl}";
     document.aimAddSectorForm.target="_blank";
     document.aimAddSectorForm.submit();
 }
</script>
<div class="admin-content">
<h1 class="admintitle"><digi:trn>Sector manager</digi:trn></h1>
<digi:instance property="aimAddSectorForm" />
<digi:form action="/getSectorSchemes.do" method="post">
<digi:context name="digiContext" property="context" />
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->
<html:hidden property="event" value="view"/>
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="1000"  align=center>
	<tr>
		<td align=left class=r-dotted-lg valign="top" width=700>
			<table cellPadding=5 cellspacing="0" width="100%" border="0">
				<tr>
					<td align="left">
						<jsp:include page="/repository/aim/view/adminXSLExportToolbar.jsp" />
					</td>
				</tr>
				<tr>
					<td height=16 valign="center" width=571>
						<digi:errors />
					</td>
				</tr>
				<tr>
					<td noWrap width="100%" vAlign="top">
					<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr><td noWrap width=750 vAlign="top">
						<table cellpadding="1" cellspacing="1" width="100%" valign="top">
							<tr bgColor=#ffffff >
								<td vAlign="top" width="100%" class="report"style="border-top:1px solid #ccc;">

									<table width="100%" cellspacing="1" cellpadding="1" valign="top" align=left class="inside">
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
														<!--  to export table we are adding class "yui-dt-data" to its tbody-->
											<tbody class="yui-dt-data">
													<logic:iterate name="aimAddSectorForm" property="formSectorSchemes" id="sectorScheme"
																	type="org.digijava.ampModule.aim.dbentity.AmpSectorScheme	">
													<tr>
														<td width="72%" bgcolor="#ffffff" class="inside">
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

														<td bgcolor="#ffffff" width="15%" align="center" class="inside ignore">
															<c:set var="trnEditScheme">
																<digi:trn key="aim:clickToEditScheme">Click here to Edit Scheme</digi:trn>
															</c:set>



															[ <digi:link href="/updateSectorSchemes.do" name="urlParams2" title="${trnEditScheme}" >
															 ${edittext} 
															</digi:link>
															]													  </td>

														<%--<logic:equal name="aimAddSectorForm" property="deleteSchemeFlag" value="true">--%>
                                                                                                               
														<td bgcolor="#ffffff" width="13%" align="center" class="inside ignore">
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
													</tbody>



													</logic:notEmpty>
													<!-- end page logic -->
									
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
	    <td align=left class=r-dotted-lg valign="top" width=300><table align="center" cellpadding="0" cellspacing="0" width="90%" border="0">
							<tr>
								<td>
									<!-- Other Links -->
									<table cellpadding="0" cellspacing="0" width="120">
										<tr>
											<td bgColor=#c9c9c7 class=box-title>
												<b style="font-size:12px; padding-left:5px;">
													<digi:trn key="aim:otherLinks">
														Other links
													</digi:trn>
												</b>
											</td>
											<td class="header-corner"  height="17" width="17"></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellspacing="1" width="100%" class="inside">
								<tr>
									<td class="inside"><digi:img
											src="ampModule/aim/images/arrow-014E86.gif" styleClass="list-item-image" width="15"
											height="10" /> <c:set var="trnViewAdmin">
											<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
										</c:set> <digi:link href="/admin.do" title="${trnViewAdmin}">
											<digi:trn key="aim:AmpAdminHome">
													Admin Home
													</digi:trn>
										</digi:link></td>
								</tr>
								<field:display name="Add Scheme Link" feature="Sectors">
										<tr>
											<td class="inside">
												<digi:img src="ampModule/aim/images/arrow-014E86.gif" styleClass="list-item-image" width="15" height="10"/>
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
                                                                                         <td class="inside">
                                                                                             <digi:img src="ampModule/aim/images/arrow-014E86.gif" styleClass="list-item-image" width="15" height="10"/>
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
                                                                         
										
										<!-- end of other links -->
									</table>
								</td>
							</tr>
						</table></td>
	</tr>
</table>
</digi:form>
</div>

