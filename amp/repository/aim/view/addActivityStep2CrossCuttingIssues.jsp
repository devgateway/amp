<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript" type="text/javascript">
function edit(key) {
	document.aimEditActivityForm.step.value = "2.2";
        document.aimEditActivityForm.submit();
	document.aimEditActivityForm.action = "/editor/showEditText.do?id=" + key + "&referrer=/aim/addActivity.do?edit=true";
	document.aimEditActivityForm.editKey.value = key;
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();

}

</script>

<digi:instance property="aimEditActivityForm" />
<html:hidden property="editKey"/>
							<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#f4f4f2">
                                <tr>
                                  <td bgColor=#f4f4f2 align="center" vAlign="top"><!-- contents -->
                                    <table width="95%" bgcolor="#f4f4f2">
                                    
                                      <tr>
                                        <td>
                                          <IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15 />
                                          <a title="<digi:trn key="aim:crossCuttingIssuesTitle">Cross Cutting Issues</digi:trn>">
                                          <b>
                                            <digi:trn key="aim:crossCuttingIssues">
                                              Cross Cutting Issues
                                            </digi:trn>
                                          </b>
										  </a>
                                        </td>
                                      </tr>
                                      
                                      <tr>
                                        <td>
                                          <table cellPadding=5 cellSpacing=1 border=0 width="100%" bgcolor="#d7eafd">
                                            <tr>
                                              <td align="left">
                                                <b>
            		                                <digi:trn key="aim:crossCuttingIssues">
	    	                                          Cross Cutting Issues
		                                            </digi:trn>
                                                </b>
                                              </td>
                                            </tr>
                                            <tr>
                                              <td bgcolor="#ffffff" width="100%">
                                                <table cellPadding=1 cellSpacing=5 border=0	bgcolor="#ffffff" width="100%">
                                                  <tr>
                                                    <td bgcolor="#ffffff">
                                                    </td>
                                                  </tr>

                                                  <tr>
                                                    <td>
                                                      <table cellSpacing=1 cellPadding=1 border=0 bgcolor="#ffffff" width="40%">
                                                      <field:display name="Equal Opportunity" feature="Cross Cutting Issues">
                                                        <tr>
															<td>
															  <digi:trn key="aim:equalOportunity">Equal Opportunity:</digi:trn>
															</td>
															<td>
																<bean:define id="eqOppKey">
																	   <c:out value="${aimEditActivityForm.equalOpportunity}"/>
																</bean:define>
																<digi:edit key="<%=eqOppKey%>"/>
															</td>
															<td>
																
																<a href="javascript:edit('<%=eqOppKey%>')">
																	<digi:trn key="aim:edit">Edit</digi:trn>
																</a>
															</td>
														</tr>
														</field:display>
														<field:display name="Environment" feature="Cross Cutting Issues">
                                                        <tr>
															<td>
															 <digi:trn key="aim:environment"> Environment:</digi:trn>&nbsp;&nbsp;
															</td>
															<td>
																<bean:define id="envKey">
																	<c:out value="${aimEditActivityForm.environment}"/>
																</bean:define>
																<digi:edit key="<%=envKey%>"/>
															
															</td>
															<td>
																<a href="javascript:edit('<%=envKey%>')">
																	<digi:trn key="aim:edit">Edit</digi:trn>
																</a>
															</td>
														</tr>
														</field:display>
														<field:display name="Minorities" feature="Cross Cutting Issues">
                                                        <tr>
															<td>
															  <digi:trn key="aim:monitories"> Minorities:</digi:trn>
															</td>
															<td>
															<bean:define id="minKey">
																	<c:out value="${aimEditActivityForm.minorities}"/>
																</bean:define>
																<digi:edit key="<%=minKey%>"/>
															</td>
															<td>
																<a href="javascript:edit('<%=minKey%>')">
																	<digi:trn key="aim:edit">Edit</digi:trn>
																</a>
															</td>
														</tr>
														</field:display>
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
						</tr>
					</table>