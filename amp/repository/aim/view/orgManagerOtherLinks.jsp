<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://digijava.org/fields" prefix="field"%>
<%@ taglib uri="http://digijava.org/features" prefix="feature"%>
<%@ taglib uri="http://digijava.org/modules" prefix="module"%>
<table align="center" cellpadding="0" cellspacing="0" width="90%" border="0">
								<tr>
									<td>
										<!-- Other Links -->
										<table cellpadding="0" cellspacing="0" width="120">
											<tr>
												<td bgColor=#c9c9c7>
												<b style="font-size:12px; padding-left:5px;">
													<digi:trn key="aim:otherLinks">Other links</digi:trn>
													</b>
												</td>
												<td class="header-corner"	height="17" width=17></td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td bgColor=#ffffff>
										<table cellPadding=5 cellspacing="1" width="100%" class="inside">
											<tr>
												<!--<td>
													<digi:img src="module/aim/images/arrow-014E86.gif" styleClass="list-item-image" width="15" height="10"/></td>-->
												<td class="inside">
													<digi:link href="/admin.do">
													<digi:trn key="aim:AmpAdminHome">
													Admin Home
													</digi:trn>
													</digi:link>
												</td>
											</tr>
											<tr>
												<!--<td class="inside">
													<digi:img src="module/aim/images/arrow-014E86.gif" styleClass="list-item-image" width="15" height="10"/></td>-->
												<td class="inside">
														<digi:link href="/editOrganisation.do?actionFlag=create&mode=resetMode" >
															<digi:trn key="aim:addNewOrganization">Add an Organization</digi:trn></digi:link>
												</td>
											</tr>
											<tr>
												<!--<td>
													<digi:img src="module/aim/images/arrow-014E86.gif" styleClass="list-item-image" width="15" height="10"/></td>-->
												<td class="inside">
														<digi:link href="/orgTypeManager.do" >
															<digi:trn key="aim:orgTypeManager">Organization Type Manager</digi:trn></digi:link>
												</td>
											</tr>
											<tr>
												<!--<td>
													<digi:img src="module/aim/images/arrow-014E86.gif" styleClass="list-item-image" width="15" height="10"/></td>-->
												<td class="inside">
														<digi:link href="/orgGroupManager.do?resetAlpha=true" >
															<digi:trn key="aim:orgGroupManager">Organization Group Manager</digi:trn></digi:link>
												</td>
											</tr>
											<field:display name="Budget Department" feature="Budget">
											<tr>
												<!--<td>
													<digi:img src="module/aim/images/arrow-014E86.gif" styleClass="list-item-image"	width="15" height="10"/></td>-->
												<td class="inside">
													<digi:link href="/departmentsmanager.do" >
														<digi:trn>Departments Manager</digi:trn>
													</digi:link>
												</td>
											</tr>
											</field:display>
											<!-- end of other links -->
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>