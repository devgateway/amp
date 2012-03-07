<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.digijava.module.aim.helper.*" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<digi:instance property="aimEditActivityForm" />
	<logic:notEmpty name="aimEditActivityForm" property="customFieldsSteps">
		<logic:iterate name="aimEditActivityForm" property="customFieldsSteps" id="step" indexId="stepIdx">
								<logic:equal name="step" property="step" value="${stepNumber}">
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<b>
											<digi:trn key="aim:customfields:step_name:${stepNumber}"><c:out value="${step.name}"/></digi:trn>
										</b>
									</td></tr>
									<tr><td>
										&nbsp;
									</td></tr>
									<tr><td>
										<table width="100%" bgcolor="#ffffff" cellSpacing=1 cellPadding=5>
										    <logic:notEmpty name="step" property="customFields">
											<logic:iterate name="step" property="customFields" id="customField" indexId="index">
											<field:display name="${customField.FM_field}" feature="Step${aimEditActivityForm.step}">												
												<tr>
													<td width=200 bgcolor="#ffffff">
														<a title="<digi:trn key="aim:customfield:${customField.name}:description">${customField.description}</digi:trn>">&nbsp;
														<digi:trn key="aim:customfield:${customField.name}">${customField.name}</digi:trn>
														</a>
													</td>
													<td bgcolor="#ffffff">
														<a title="<digi:trn key="aim:customfield:${customField.name}:description">${customField.description}</digi:trn>">
														<c:choose>
															<c:when test="<%=customField instanceof ComboBoxCustomField%>">
																<html:select name="aimEditActivityForm" property="customFieldsSteps[${stepIdx}].customFields[${index}].value" styleClass="inp-text">
																	<html:optionsCollection  name="aimEditActivityForm" property="customFieldsSteps[${stepIdx}].customFields[${index}].options" value="key" label="value"/>
																</html:select>											
															</c:when>
															<c:when test="<%=customField instanceof CategoryCustomField%>">
																<category:showoptions name="aimEditActivityForm" property="customFieldsSteps[${stepIdx}].customFields[${index}].longValue" categoryName="${customField.categoryName}" styleClass="inp-text" />								
															</c:when>
															<c:when test="<%=customField instanceof DateCustomField%>">																
																<html:text name="aimEditActivityForm" readonly="true" property="customFieldsSteps[${stepIdx}].customFields[${index}].strDate" size="10"
																styleId="customField${index}_input_text" styleClass="inp-text"  />																
																<a id="customField_clear${index}" href="javascript:clearDate(document.aimEditActivityForm.customField${index}_input_text, 'customField_clear${index}')">
																 	<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
																</a>
																<a id="customField_date${index}" href='javascript:pickDateWithClear("customField_date${index}",document.aimEditActivityForm.customField${index}_input_text,"customField_clear${index}")'>
																	<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
																</a>
															</c:when>
															<c:when test="<%=customField instanceof RadioOptionCustomField%>">
																<logic:iterate name="aimEditActivityForm" property="customFieldsSteps[${stepIdx}].customFields[${index}].options" id="option">
																	<digi:trn key="aim:customfield:${customField.name}:option:${option.key}"><c:out value="${option.value}"/></digi:trn>
																	<html:radio name="aimEditActivityForm" property="customFieldsSteps[${stepIdx}].customFields[${index}].value" value="${option.key}"/> &nbsp;&nbsp;
																</logic:iterate>
															</c:when>
															<c:when test="<%=customField instanceof CheckCustomField%>">
																<html:checkbox name="aimEditActivityForm" property="customFieldsSteps[${stepIdx}].customFields[${index}].booleanValue"/>
																<c:out value="${customField.labelTrue}"/>
															</c:when>
															<c:otherwise>
																<html:text name="aimEditActivityForm" property="customFieldsSteps[${stepIdx}].customFields[${index}].value" size="40"
																styleId="originalAppDate" styleClass="inp-text" />																														
															</c:otherwise>
														</c:choose>
														</a>
													</td>
												</tr>
											</field:display>
											</logic:iterate>
											</logic:notEmpty>
										</table>
									</td></tr>
								</logic:equal>
							</logic:iterate>
	</logic:notEmpty>
							
									
