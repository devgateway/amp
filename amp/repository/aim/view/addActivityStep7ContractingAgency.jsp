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
										<a title="<digi:trn key="aim:ContractingAgency">The third party outside of the implementing agency</digi:trn>">
										<b><digi:trn key="aim:contracting2Agency">Contracting Agency</digi:trn></b></a>
									</td></tr>
									<tr><td >
										&nbsp;
									</td></tr>
									<tr><td><field:display name="Contracting Agency" feature="Contracting Agency">
										<logic:notEmpty name="aimEditActivityForm" property="agencies.conAgencies">
											<table width="100%" cellspacing="1" cellPadding=5 class="box-border-nopadding">
												<logic:iterate name="aimEditActivityForm" property="agencies.conAgencies" id="conAgency" type="org.digijava.module.aim.dbentity.AmpOrganisation">
												<tr><td>
													<table width="80%" cellSpacing="1" cellPadding="1" vAlign="top" align="left">
														<tr>
															<td width="2%">
																<html:multibox property="agencies.selConAgencies">
																	<bean:write name="conAgency" property="ampOrgId" />
																</html:multibox>
															</td>
															<td align="left" width="49%">
																<bean:write name="conAgency" property="name" />
															</td>
															<td width="39%" valign="top">
																<field:display name="Contracting Agency Department/Division"  feature="Contracting Agency">
																	<digi:trn>Department/Division: </digi:trn><html:text property="agencies.conOrgToInfo(${conAgency.ampOrgId})"></html:text>
																</field:display>
																&nbsp;
															</td>
															<td width="10%" valign="top">
																<field:display name="Contracting Agency Percentage"  feature="Contracting Agency">
																	<digi:trn>Percentage</digi:trn> : <html:text property="agencies.conOrgPercentage(${conAgency.ampOrgId})"></html:text>
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
															<field:display name="Contracting Agency Add Button" feature="Contracting Agency">
															<aim:addOrganizationButton callBackFunction="submitAfterSelectingOrg();"  refreshParentDocument="false" collection="conAgencies" form="${aimEditActivityForm.agencies}" styleClass="dr-menu"><digi:trn key="btn:addOrganizations">Add Organizations</digi:trn></aim:addOrganizationButton>			
															</field:display>
															</td>
															<td>
															<field:display name="Contracting Agency Remove Button" feature="Contracting Agency">
																<html:button  styleClass="dr-menu" property="submitButton" onclick="return removeSelOrgs(6)">
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
										<field:display name="Contracting Agency Add Button" feature="Contracting Agency">
										<logic:empty name="aimEditActivityForm" property="agencies.conAgencies">
											<table width="100%" bgcolor="#cccccc" cellspacing="1" cellPadding=5>
												<tr>
													<td bgcolor="#ffffff">
															<aim:addOrganizationButton callBackFunction="submitAfterSelectingOrg();"  refreshParentDocument="false" collection="conAgencies" form="${aimEditActivityForm.agencies}" styleClass="dr-menu"><digi:trn key="btn:addOrganizations">Add Organizations</digi:trn></aim:addOrganizationButton>			
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
													</td>
												</tr>
											</table>
										</logic:empty>
										</field:display>
									</td></tr>
									
