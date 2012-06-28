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
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ page import="org.digijava.module.aim.uicomponents.form.selectOrganizationComponentForm" %>

<digi:instance property="aimEditActivityForm" />
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<a title="<digi:trn key="aim:SectorGroupTitle">The Sector Group</digi:trn>">
										<b><digi:trn key="aim:sectorGroup">Sector Group</digi:trn></b></a>
									</td></tr>
									<tr><td bgColor=#f4f4f2>
										&nbsp;
									</td></tr>
									<tr><td><field:display name="Sector Group" feature="Sector Group">
										<logic:notEmpty name="aimEditActivityForm" property="agencies.sectGroups">
											<table width="100%" cellspacing="1" cellPadding=5 class="box-border-nopadding">
												<logic:iterate name="aimEditActivityForm" property="agencies.sectGroups"
												id="impAgency" type="org.digijava.module.aim.dbentity.AmpOrganisation">
												<tr><td>
													<table width="80%" cellSpacing="1" cellPadding="1" vAlign="top" align="left">
														<tr>
															<td width="2%">
																<html:multibox property="agencies.selSectGroups">
																	<bean:write name="impAgency" property="ampOrgId" />
																</html:multibox>
															</td>
															<td align="left" width="49%">
																<bean:write name="impAgency" property="name" />
															</td>
															<td width="49%">
																<field:display name="Sector Group Department/Division"  feature="Sector Group">
																	<digi:trn>Department/Division: </digi:trn><html:text property="agencies.sectOrgToInfo(${impAgency.ampOrgId})"></html:text>
																</field:display>
																&nbsp;
															</td>
														</tr>
													</table>
												</td></tr>
												</logic:iterate>
												<tr><td>
													<table cellspacing="1" cellpadding="1">
														<tr>
															<td>
																<field:display name="Sector Group Add Button" feature="Sector Group">
																	<aim:addOrganizationButton refreshParentDocument="false" callBackFunction="submitAfterSelectingOrg();"  collection="sectGroups" form="${aimEditActivityForm.agencies}" styleClass="dr-menu"><digi:trn key="btn:addOrganizations">Add Organizations</digi:trn></aim:addOrganizationButton>			
																	<%
																	selectOrganizationComponentForm compForm1 = (selectOrganizationComponentForm) session.getAttribute("aimSelectOrganizationForm");
																	selectOrganizationComponentForm compForm2 = (selectOrganizationComponentForm) session.getAttribute("siteampdefaultaimSelectOrganizationForm");
																	if(compForm1 != null){
																		compForm1.setDelegateClass("");
																	}
																	if(compForm2 != null){
																		compForm2.setDelegateClass("");
																	}
																	%>			
																</field:display>
															</td>
															<td>
																<field:display name="Sector Group Remove Button" feature="Sector Group">															
																<html:button  styleClass="dr-menu" property="submitButton" onclick="return removeSelOrgs(8)">
																	<digi:trn key="btn:removeSelectedOrganizations">Remove Selected Organizations</digi:trn>
																</html:button>
																</field:display>
															</td>
														</tr>
													</table>
												</td></tr>
											</table>
										</logic:notEmpty>
										</field:display>
										<field:display name="Sector Group Add Button" feature="Sector Group">
										<logic:empty name="aimEditActivityForm" property="agencies.sectGroups">
											<table width="100%" bgcolor="#cccccc" cellspacing="1" cellPadding=5>
												<tr>
													<td bgcolor="#ffffff">
														<aim:addOrganizationButton refreshParentDocument="false" callBackFunction="submitAfterSelectingOrg();"  collection="sectGroups" form="${aimEditActivityForm.agencies}" styleClass="dr-menu"><digi:trn key="btn:addOrganizations">Add Organizations</digi:trn></aim:addOrganizationButton>			
													</td>
												</tr>
											</table>
										</logic:empty>
										</field:display>
									</td></tr>
