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


				<digi:instance property="aimEditActivityForm" />
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<a title="<digi:trn key="aim:WebSource">Web links related to the project</digi:trn>">										  			  <b><digi:trn key="aim:webResource">Web resources</digi:trn></b></a>
									</td></tr>
									<tr><td bgColor=#f4f4f2>
										&nbsp;
									</td></tr>									
									<tr><td>
										<logic:notEmpty name="aimEditActivityForm" property="linksList">
											<table width="100%" cellSpacing=1 cellPadding=5 class="box-border-nopadding">
												<logic:iterate name="aimEditActivityForm" property="linksList"
												id="relLinks" type="org.digijava.module.aim.helper.RelatedLinks">
												<bean:define name="relLinks" id="selWebLinks" property="relLink" />
												<tr>
													<td>
														<html:multibox property="selLinks">
															<bean:write name="selWebLinks" property="id" />
														</html:multibox>
														<bean:write name="selWebLinks" property="title" /> - 
														<a href="<bean:write name="selWebLinks" property="url" />" target="_blank">
														<bean:write name="selWebLinks" property="url" /></a><br>
														<field:display name="Web Resource Description" feature="Web Resources">
													<b>Desc:</b><bean:write name="selWebLinks" property="description" />
													</field:display>
													</td>
												</tr>
												</logic:iterate>
												<tr><td>
													<table cellSpacing=2 cellPadding=2>
														<tr>
														<field:display name="Add Web Resource Button" feature="Web Resources">
															<td>
																<html:button  styleClass="buton" property="submitButton" onclick="addLinks()">
																	<digi:trn key="btn:addWebResources">Add Web Resources</digi:trn>
																</html:button>
															</td>
															</field:display>
															<field:display name="Remove Web Resource Button" feature="Web Resources">
															<td>
																<html:button  styleClass="buton" property="submitButton" onclick="return removeSelLinks()">
																	<digi:trn key="btn:removeWebResources">Remove Web Resources</digi:trn>
																</html:button>

															</td>
															</field:display>
														</tr>
													</table>
												</td></tr>
											</table>											
										</logic:notEmpty>
										<logic:empty name="aimEditActivityForm" property="linksList">
										<field:display name="Add Web Resource Button" feature="Web Resources">
											<table width="100%" bgcolor="#cccccc" cellSpacing=1 cellPadding=5>
												<tr>
													<td bgcolor="#ffffff">

														<html:button  styleClass="buton" property="submitButton" onclick="addLinks()">
															<digi:trn key="btn:addWebResources">Add Web Resources</digi:trn>
														</html:button>

													</td>
												</tr>
											</table>
										</field:display>
										</logic:empty>
									</td></tr>
