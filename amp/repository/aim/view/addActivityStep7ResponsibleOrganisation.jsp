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

<digi:instance property="aimEditActivityForm" />
								<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<a title="<digi:trn key="aim:OrganisationResponsible">The responsible Organization</digi:trn>">
										<b><digi:trn key="aim:responsibleOrganisation">Responsible Organization</digi:trn></b></a>
									</td></tr>
									<tr><td bgColor=#f4f4f2>
										&nbsp;
									</td></tr>	
																
									<field:display name="Responsible Organization" feature="Responsible Organization">
									<logic:notEmpty name="aimEditActivityForm" property="agencies.respOrganisations">
										<tr>
											<td>
											<table width="100%" cellspacing="1" cellPadding=5 class="box-border-nopadding">
												<logic:iterate name="aimEditActivityForm" property="agencies.respOrganisations" 
												id="repOrganisation" type="org.digijava.module.aim.dbentity.AmpOrganisation">
												<tr><td>
													<table width="80%" cellSpacing="1" cellPadding="1" vAlign="top" align="left" bgcolor="#ffffff">
														<tr>
															<td width="2%">
																<html:multibox  property="agencies.selRespOrganisations">
																	<bean:write name="repOrganisation" property="ampOrgId" />
																</html:multibox>
															</td>
															<td align="left" width="49%">
																<bean:write name="repOrganisation" property="name" />
															</td>
														  
																<field:display name="Responsible Organization Department/Division"  feature="Responsible Organization">
																	<td width="39%" valign="top">
																			<digi:trn>Department/Division: </digi:trn><html:text size="30" property="agencies.respOrgToInfo(${repOrganisation.ampOrgId})"></html:text>
																	</td>
																</field:display>
																
																<td width="10%" valign="top">
																	<field:display name="Responsible Organization Percentage"  feature="Responsible Organization">
																		<digi:trn>Percentage</digi:trn> : <html:text property="agencies.respOrgPercentage(${repOrganisation.ampOrgId})"></html:text>
																	</field:display>
																	&nbsp;
																</td>
														</tr>
													</table>
												</td>
												</tr>
												</logic:iterate>											
												<tr><td>
												    <table>
												    <tr>
													<td>
														<field:display name="Responsible Organization Add Button" feature="Responsible Organization">
														<aim:addOrganizationButton callBackFunction="submitAfterSelectingOrg();"   form="${aimEditActivityForm.agencies}" collection="respOrganisations" refreshParentDocument="false" styleClass="dr-menu"><digi:trn key="btn:addOrganizations">Add Organizations</digi:trn></aim:addOrganizationButton>
														</field:display>
													</td>
													<td>
														<field:display name="Responsible Organization Remove Button" feature="Responsible Organization">
														<html:button  styleClass="dr-menu" property="submitButton" onclick="removeSelOrgs(9)">
															<digi:trn key="btn:removeSelectedOrganizations">Remove Selected Organizations</digi:trn>
														</html:button>
														</field:display>

													</td>
													</tr>
													</table>
													</td>
												</tr>
											</table>
											</td>
											</tr>
										</logic:notEmpty>
										<field:display name="Responsible Organization Add Button" feature="Responsible Organization">
										<logic:empty name="aimEditActivityForm" property="agencies.respOrganisations">
											<tr>
												<td>
													<table width="100%" bgcolor="#cccccc" cellspacing="1" cellPadding=5>
														<tr>
															<td bgcolor="#ffffff">
																<aim:addOrganizationButton callBackFunction="submitAfterSelectingOrg();"  form="${aimEditActivityForm.agencies}" collection="respOrganisations" refreshParentDocument="false" styleClass="dr-menu"><digi:trn key="btn:addOrganizations">Add Organizations</digi:trn></aim:addOrganizationButton>
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</logic:empty>
										</field:display>
									</field:display>
										