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
										<a title="<digi:trn key="aim:AgencyImplementing">The organisation that directly implements the activity</digi:trn>">
										<b><digi:trn key="aim:implementingAgency">Implementing Agency</digi:trn></b></a>
									</td></tr>
									<tr><td>
										&nbsp;
									</td></tr>
									<tr><td><field:display name="Implementing Agency" feature="Implementing Agency">
										<logic:notEmpty name="aimEditActivityForm" property="agencies.impAgencies">
											<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
												<logic:iterate name="aimEditActivityForm" property="agencies.impAgencies"
												id="impAgency" type="org.digijava.module.aim.dbentity.AmpOrganisation">
												<tr><td>
													<table width="80%" cellSpacing="1" cellPadding="1" vAlign="top" align="left">
														<tr>
															<td width="2%">
																<html:multibox property="agencies.selImpAgencies">
																	<bean:write name="impAgency" property="ampOrgId" />
																</html:multibox>
															</td>
															<td align="left" width="49%" >
																<bean:write name="impAgency" property="name" />
															</td>
															<td width="39%" valign="top">
																<field:display name="Implementing Agency Department/Division"  feature="Implementing Agency">
																	<digi:trn>Department/Division: </digi:trn><html:text property="agencies.impOrgToInfo(${impAgency.ampOrgId})"></html:text>
																</field:display>
																&nbsp;
															</td>
															<td width="10%" valign="top">
																<field:display name="Implementing Agency Percentage"  feature="Implementing Agency">
																	<digi:trn>Percentage</digi:trn> : <html:text property="agencies.impOrgPercentage(${impAgency.ampOrgId})"></html:text>
																</field:display>
																&nbsp;
															</td>
														</tr>
													</table>
												</td></tr>
												</logic:iterate>
												<tr><td>
													<table cellSpacing=1 cellPadding=1>
														<tr>
															<td>
																<field:display name="Implementing Agency Add Button" feature="Implementing Agency">
																	<aim:addOrganizationButton callBackFunction="submitAfterSelectingOrg();"  form="${aimEditActivityForm.agencies}" collection="impAgencies" refreshParentDocument="false" styleClass="dr-menu"><digi:trn key="btn:addOrganizations">Add Organizations</digi:trn></aim:addOrganizationButton>
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
																<field:display name="Implementing Agency Remove Button" feature="Implementing Agency">															
																<html:button  styleClass="dr-menu" property="submitButton" onclick="return removeSelOrgs(2)">
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
										<field:display name="Implementing Agency Add Button" feature="Implementing Agency">
										<logic:empty name="aimEditActivityForm" property="agencies.impAgencies">
											<table width="100%" bgcolor="#cccccc" cellSpacing=1 cellPadding=5>
												<tr>
													<td bgcolor="#ffffff">
															<aim:addOrganizationButton callBackFunction="submitAfterSelectingOrg();"  form="${aimEditActivityForm.agencies}" collection="impAgencies" refreshParentDocument="false" styleClass="dr-menu"><digi:trn key="btn:addOrganizations">Add Organizations</digi:trn></aim:addOrganizationButton>
													</td>
												</tr>
											</table>
										</logic:empty>
										</field:display>
									</td></tr>
