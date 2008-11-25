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
									<tr>
									<td>
									<logic:notEmpty name="aimEditActivityForm" property="agencies.respOrganisations">
											<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
												<logic:iterate name="aimEditActivityForm" property="agencies.respOrganisations"
												id="repOrganisation" type="org.digijava.module.aim.dbentity.AmpOrganisation">
												<tr><td>
													<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top" align="left" bgcolor="#ffffff">
														<tr>
															<td width="3">
																<html:multibox property="agencies.selRespOrganisations">
																	<bean:write name="repOrganisation" property="ampOrgId" />
																</html:multibox>
															</td>
															<td align="left">
																<bean:write name="repOrganisation" property="name" />
															</td>
														</tr>
													</table>
												</td>
												</tr>
												</logic:iterate>
											<tr><td>
										<table cellSpacing=1 cellPadding=1>
														<tr>
															<td>
																<field:display name="Responsible Organization Add Button" feature="Responsible Organization">
																<aim:addOrganizationButton form="${aimEditActivityForm.agencies}" collection="respOrganisations" refreshParentDocument="true" styleClass="dr-menu"><digi:trn key="btn:addOrganizations">Add Organizations</digi:trn></aim:addOrganizationButton>
																</field:display>
															</td>
															<td>
																<field:display name="Responsible Organization Organizations Button" feature="Responsible Organization">
																<html:button  styleClass="dr-menu" property="submitButton" onclick="removeSelOrgs(9)">
																	<digi:trn key="btn:removeSelectedOrganizations">Remove Selected Organizations</digi:trn>
																</html:button>
																</field:display>

															</td>
														</tr>
													</table>
												</td></tr>
											</table>
										</logic:notEmpty>

										<logic:empty name="aimEditActivityForm" property="agencies.respOrganisations">
											<table width="100%" bgcolor="#cccccc" cellSpacing=1 cellPadding=5>
												<tr>
													<td bgcolor="#ffffff">
														<field:display name="Responsible Organization Add Button" feature="Responsible Organization">
														<aim:addOrganizationButton form="${aimEditActivityForm.agencies}" collection="respOrganisations" refreshParentDocument="true" styleClass="dr-menu"><digi:trn key="btn:addOrganizations">Add Organizations</digi:trn></aim:addOrganizationButton>
														</field:display>
													</td>
												</tr>
											</table>
										</logic:empty>
