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
										<field:display name="Add Web Resource Button" feature="Web Resources">
											<table width="100%" bgcolor="#cccccc" cellspacing="1" cellPadding=5>
												<tr>
													<td bgcolor="#ffffff">

														<html:button  styleClass="dr-menu" property="submitButton" onclick="addLinks()">
															<digi:trn key="btn:addWebResources">Add Web Resources</digi:trn>
														</html:button>

													</td>
												</tr>
											</table>
										</field:display>
									</td></tr>
