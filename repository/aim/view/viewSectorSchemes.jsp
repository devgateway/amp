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
	function setStripsTable(tableId, classOdd, classEven) {
		var tableElement = document.getElementById(tableId);
		rows = tableElement.getElementsByTagName('tr');
		for(var i = 0, n = rows.length; i < n; ++i) {
			if(i%2 == 0)
				rows[i].className = classEven;
			else
				rows[i].className = classOdd;
		}
		rows = null;
	}

	function setHoveredTable(tableId, hasHeaders) {

		var tableElement = document.getElementById(tableId);
		if(tableElement){
	    	var className = 'Hovered',
	        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
	        rows      = tableElement.getElementsByTagName('tr');

			for(var i = 0, n = rows.length; i < n; ++i) {
				rows[i].onmouseover = function() {
					this.className += ' ' + className;
				};
				rows[i].onmouseout = function() {
					this.className = this.className.replace(pattern, ' ');

				};
			}
			rows = null;
		}
	}
</script>

<style type="text/css">
.jcol {
	padding-left: 10px;
}

.jlien {
	text-decoration: none;
}

.tableEven {
	background-color: #dbe5f1;
	font-size: 8pt;
	padding: 2px;
}

.tableOdd {
	background-color: #FFFFFF;
	font-size: 8pt;
	padding: 2px;
}

.Hovered {
	background-color: #a5bcf2;
}

.notHovered {
	background-color: #FFFFFF;
}
</style>


<digi:instance property="aimAddSectorForm" />
<digi:context name="digiContext" property="context" />
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
<html:hidden property="event" value="view"/>
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td align=left vAlign=top width=750>
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

									<table cellSpacing="0" cellPadding="0" vAlign="top" align="left" width="100%">
										<tr>
											<td>
												<table width="100%" height="30" cellpadding="2" cellspacing="0">
													<tr style="background-color: #999999; color: #000000;" align="center">
														<td width="80%" align="left">
															<b><digi:trn key="aim:schemes">Schemes </digi:trn></b>
														</td>
														<td width="20%" rowspan="2">
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td>
												<div style="overflow: auto; width: 100%; height: 180px; max-height: 180px;">
													<table width="100%" cellspacing="0" cellpadding="2" id="dataTable">
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
															<logic:iterate name="aimAddSectorForm" property="formSectorSchemes" id="sectorScheme" type="org.digijava.module.aim.dbentity.AmpSectorScheme	">
																<tr height="25">
																	<td align="left" width="80%">
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
																	<td align="center" width="10%">
																		<c:set var="trnEditScheme">
																			<digi:trn key="aim:clickToEditScheme">Click here to Edit Scheme</digi:trn>
																		</c:set>
																		<digi:link href="/updateSectorSchemes.do" name="urlParams2" title="${trnEditScheme}" >
																		 	<digi:img src="../ampTemplate/images/application_edit.png" border="0"/>
																		</digi:link>
																	</td>
																	<td align="center" width="10%">
			                                                           	<c:if test="${!sectorScheme.used}">
																			<jsp:useBean id="urlParams4" type="java.util.Map" class="java.util.HashMap"/>
																			<c:set target="${urlParams4}" property="ampSecSchemeId">
																				<bean:write name="sectorScheme" property="ampSecSchemeId" />
																			</c:set>
																			<c:set target="${urlParams4}" property="event" value="deleteScheme"/>
																			<c:set var="trnDeleteScheme">
																				<digi:trn key="aim:clickToDeleteScheme">Click here to Delete Scheme</digi:trn>
																			</c:set>
																			<digi:link href="/updateSectorSchemes.do" name="urlParams4"
																				title="${trnDeleteScheme}" onclick="return onDelete()">
																				 <digi:img src="../ampTemplate/images/trash_16.gif" border="0"/>
																			</digi:link>
			                                                            </c:if>
																	</td>
			                                                    </tr>
															</logic:iterate>
														</logic:notEmpty>
													</table>
												</div>
											</td>
										</tr>
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
									<table cellPadding=0 cellSpacing=0 width=200 height="20">
										<tr>
											<td bgColor=#c9c9c7 class=box-title>
												<digi:trn key="aim:otherLinks">
												Other links
												</digi:trn>
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
					<tr>
				        <td>
							<table>
				             	<tr>
				                 	<td colspan="2">
				                 		<strong><digi:trn key="aim:IconReference">Icons Reference</digi:trn></strong>
				       				</td>
				       			</tr>
				     			<tr>
				           			<td width="15" height="20" align="center">
										<img src= "../ampTemplate/images/application_edit.png" vspace="2" border="0" align="absmiddle" />
									</td>
									<td nowrap="nowrap">
				               			<digi:trn key="aim:clickToInactiveCurrency">Click on this icon to edit Scheme</digi:trn>
				               			<br />
				       				</td>
				       			</tr>
				        		<tr>
				           			<td width="15" height="20" align="center">
										<img src= "../ampTemplate/images/trash_16.gif" vspace="2" border="0" align="absmiddle" />
				               		</td>
									<td nowrap="nowrap">
									<digi:trn key="aim:clickToDeleteCurrency">Click on this icon to delete Scheme</digi:trn>
				               			<br />
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
<script language="javascript">
	setStripsTable("dataTable", "tableEven", "tableOdd");
	setHoveredTable("dataTable", false);
</script> 

