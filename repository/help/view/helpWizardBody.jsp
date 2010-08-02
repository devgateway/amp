<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<%@page import="org.digijava.module.help.util.HelpUtil"%>
<digi:instance property="helpForm" />
<html:hidden name="helpForm" property="glossaryMode"/>
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>

<script langauage="JavaScript">
	function next(){
             <digi:context name="url" property="context/module/moduleinstance/helpActions.do?actionType=createHelpTopic" />
			helpForm.action="${url}";
  			helpForm.target = "_self";
  			helpForm.submit();

	}

	function cancel(){
                <digi:context name="url" property="context/module/moduleinstance/helpActions.do?actionType=cancelHelpTopic" />
		helpForm.action="${url}";
  		helpForm.target = "_self";
  		helpForm.submit();
	}

	function finish (edit){
		if(edit=='true'){
			document.getElementsByName('wizardStep')[0].value=2;
                        <digi:context name="url" property="context/module/moduleinstance/helpActions.do?actionType=editHelpTopic" />
			helpForm.action="${url}";
  			helpForm.target = "_self";
  			helpForm.submit();
		}
		if(edit=='false'){
			document.getElementsByName('wizardStep')[0].value=3;
                        <digi:context name="url" property="context/module/moduleinstance/helpActions.do?actionType=createHelpTopic" />
			helpForm.action="${url}";
  			helpForm.target = "_self";
  			helpForm.submit();
		}
	}

		function back(){
			document.getElementsByName('actionBack')[0].value=true;
			document.getElementsByName('wizardStep')[0].value=0;
			<digi:context name="url" property="context/module/moduleinstance/helpActions.do?actionType=createHelpTopic" />
			helpForm.action="${url}";
  			helpForm.target = "_self";
  			helpForm.submit();
	}

</script>
<digi:form action="/helpActions.do">
	<html:hidden property="wizardStep" />
	<html:hidden property="actionBack" value="false" />
	<c:if test="${!helpForm.edit}">
		<c:if test="${helpForm.wizardStep==1}">
			<table width="100%" cellspacing="0" cellpadding="0" border="0" bgcolor="#ffffff" align="center" valign="top">
				<tr>
					<td class="r-dotted-lg" width="10"/>
					<td class="r-dotted-lg" valign="top" align="left">
						<table width="98%" cellspacing="3" cellpadding="1" align="left" valign="top">
						<td/>
						<td>
							<table width="100%" cellspacing="5" cellpadding="3" border="0" valign="top">
								<tr>
									<td width="75%" valign="top">
										<table width="100%" cellspacing="0" cellpadding="0" border="0">
											<tr>
												<td width="100%">
													<table width="100%" cellspacing="0" cellpadding="0" border="0">
														<tr>
															<td width="13" height="20" background="module/aim/images/left-side.gif"/>
															<td class="textalb" valign="center" height="20" bgcolor="#006699" align="center">
																Help Topic Wizard: Step 1 of 2
															</td>
															<td width="13" height="20" background="module/aim/images/right-side.gif"/>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td width="100%" bgcolor="#f4f4f2">
													<table width="100%" cellspacing="1" cellpadding="3" bgcolor="#006699" align="left" valign="top">
														<tr>
															<td valign="top" bgcolor="#f4f4f2" align="center">
																<table width="95%" border="0" bgcolor="#f4f4f2">
																	<tr>
																		<td/>
																	</tr>
																	<tr>
																		<td>
																			<table width="100%" cellspacing="1" cellpadding="5" bgcolor="#ffffff">
																				<c:if test="${not empty helpForm.helpErrors}">
																					<c:forEach var="error" items="${helpForm.helpErrors}">
																						<tr>
																							<td>
																								<font color="red">${error}</font>
																							</td>
																						</tr>
																					</c:forEach>
																				</c:if>
																				<tr>
																					<td align="right"><digi:trn key="help:topic:key">Help Topic key</digi:trn></td>
																					<td align="left"><html:text property="topicKey" /></td>
																				</tr>
																				
																				<c:if test="${not empty helpForm.topicTree}">
																				 
																				<tr>
																					<td align="right"><digi:trn key="help:selectGroup">Select group</digi:trn></td>
																					<td align="left">

																						<bean:define id="firstLevel" name="helpForm" property="topicTree" type="java.util.Collection"/>
																						<html:select property="parentId" name="helpForm" styleClass="inp-text">
																						<html:option value=""><digi:trn key="help:noGroup">No group</digi:trn></html:option>
																						<%= HelpUtil.renderLevelGroup(firstLevel) %>
																						</html:select>
																					</td>
																				</tr>
																				
																				</c:if>
																			
																				<tr>
																					<td colspan="2">
																						<table width="100%">
																							<tr>
																								<td align="right">
																									<c:set var="trnNextBtn">
																										<digi:trn key="help:btn:next">next</digi:trn>
																									</c:set>
																									<input type="button" value="${trnNextBtn }" onclick="next();" />
																								</td>
																								<td align="left">
																									<c:set var="trnCancelBtn">
																										<digi:trn key="help:btn:cancel">Cancel</digi:trn>
																									</c:set>
																									<input type="button" value="${trnCancelBtn}" onclick="cancel();" />
																								</td>
																							</tr>
																						</table>
																					</td>
																				</tr>
																			</table>
																		</td>
																	</tr>
																	<tr>
																		<td/>
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
						</td>
						<td/>
						</table>
					</td>
					<td width="10"/>
				</tr>
			</table>
		</c:if>

		<c:if test="${helpForm.wizardStep==2}">
			<table width="100%" cellspacing="0" cellpadding="0" border="0" bgcolor="#ffffff" align="center" valign="top">
				<tr>
					<td class="r-dotted-lg" width="10"/>
					<td class="r-dotted-lg" valign="top" align="left">
						<table width="98%" cellspacing="3" cellpadding="1" align="left" valign="top">
						<td/>
						<td>
							<table width="100%" cellspacing="5" cellpadding="3" border="0" valign="top">
								<tr>
									<td width="75%" valign="top">
										<table width="100%" cellspacing="0" cellpadding="0" border="0">
											<tr>
												<td width="100%">
													<table width="100%" cellspacing="0" cellpadding="0" border="0">
														<tr>
															<td width="13" height="20" background="module/aim/images/left-side.gif"/>
															<td class="textalb" valign="center" height="20" bgcolor="#006699" align="center">
																Help Topic Wizard: Step 2 of 2
															</td>
															<td width="13" height="20" background="module/aim/images/right-side.gif"/>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td width="100%" bgcolor="#f4f4f2">
													<table width="100%" cellspacing="1" cellpadding="3" bgcolor="#006699" align="left" valign="top">
														<tr>
															<td valign="top" bgcolor="#f4f4f2" align="center">
																<table width="95%" border="0" bgcolor="#f4f4f2">
																	<tr>
																		<td/>
																	</tr>
																	<tr>
																		<td>
																			<table width="100%" cellspacing="1" cellpadding="5" bgcolor="#ffffff">
																				<tr>
																					<td align="right"><STRONG><digi:trn>Title</digi:trn></STRONG></td>
																					<td align="left"><digi:trn linkAlwaysVisible="true">${helpForm.topicKey}</digi:trn></td>
																				</tr>
																			<!--
                                                                                <tr>
																					<td align="right"><STRONG><digi:trn key="help:keywords">Keywords</digi:trn></STRONG></td>
																					<td align="left"><digi:trn key="${helpForm.keywordsTrnKey}" linkAlwaysVisible="true">Keywords</digi:trn></td>
																				</tr>
																			-->
                                                                                
                                                                                <tr>
																					<td align="right"><STRONG><digi:trn>body</digi:trn></STRONG></td>
																					<td align="left"><digi:edit key="${helpForm.bodyEditKey}">no text preview</digi:edit></td>
																				</tr>
																				<tr>
																					<td colspan="2">
																						<table width="100%">
																							<tr>
																								<td align="right">
																									<c:set var="trnSaveBtn">
																										<digi:trn key="help:btn:save">Finish</digi:trn>
																									</c:set>
																									<input type="button" value="${trnSaveBtn }" onclick="finish('${helpForm.edit}');" />
																								</td>
																								<td align="center" width="6%">
																									<c:set var="trnBacklBtn">
																										<digi:trn key="help:btn:back">Back</digi:trn>
																									</c:set>
																									<input type="button" value="${trnBacklBtn}" onclick="back();" />
																								</td>
																								<td align="left">
																									<c:set var="trnCancelBtn">
																										<digi:trn key="help:btn:cancel">Cancel</digi:trn>
																									</c:set>
																									<input type="button" value="${trnCancelBtn}" onclick="cancel();" />
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
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</table>
				</td>
			</tr>
		</table>
	</c:if>
</c:if>
<c:if test="${helpForm.edit}">
			<table width="100%" cellspacing="0" cellpadding="0" border="0" bgcolor="#ffffff" align="center" valign="top">
				<tr>
					<td class="r-dotted-lg" width="10"/>
					<td class="r-dotted-lg" valign="top" align="left">
						<table width="98%" cellspacing="3" cellpadding="1" align="left" valign="top">
						<td/>
						<td>
							<table width="100%" cellspacing="5" cellpadding="3" border="0" valign="top">
								<tr>
									<td width="75%" valign="top">
										<table width="100%" cellspacing="0" cellpadding="0" border="0">
											<tr>
												<td width="100%">
													<table width="100%" cellspacing="0" cellpadding="0" border="0">
														<tr>
															<td width="13" height="20" background="module/aim/images/left-side.gif"/>
															<td class="textalb" valign="center" height="20" bgcolor="#006699" align="center">
																Help Topic Wizard: Step 1 of 2
															</td>
															<td width="13" height="20" background="module/aim/images/right-side.gif"/>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td width="100%" bgcolor="#f4f4f2">
													<table width="100%" cellspacing="1" cellpadding="3" bgcolor="#006699" align="left" valign="top">
														<tr>
															<td valign="top" bgcolor="#f4f4f2" align="center">
																<table width="95%" border="0" bgcolor="#f4f4f2">
																	<tr>
																		<td/>
																	</tr>
																	<tr>
																		<td>
																			<table width="100%" cellspacing="1" cellpadding="5" bgcolor="#ffffff">
																				<c:if test="${not empty helpForm.helpErrors}">
																					<c:forEach var="error" items="${helpForm.helpErrors}">
																						<tr>
																							<td>
																								<font color="red">${error}</font>
																							</td>
																						</tr>
																					</c:forEach>
																				</c:if>
																				<!-- 
																				<tr>
																					<td align="right"><digi:trn key="help:selectGroup">Select group</digi:trn></td>
																					<td align="left">
																						<c:if test="${not empty helpForm.firstLevelTopics}">
																							<html:select property="parentId" name="helpForm" styleClass="inp-text">
																								<html:option value=""><digi:trn key="help:noGroup">No group</digi:trn></html:option>
																									<logic:iterate id="firstLevTopic" name="helpForm" property="firstLevelTopics">
																										<c:set var="trn">
																											<digi:trn key="${firstLevTopic.titleTrnKey}">${firstLevTopic.titleTrnKey}</digi:trn>
																										</c:set>
																										<html:option value="${firstLevTopic.helpTopicId}">${trn}</html:option>
																									</logic:iterate>
																							</html:select>
																						</c:if>
																					</td>
																				</tr>
																				-->
																				<tr>
																					<td align="right"><STRONG><digi:trn>Title</digi:trn></STRONG></td>
																					<td align="left"><digi:trn linkAlwaysVisible="true">${helpForm.topicKey}</digi:trn></td>
																				</tr>
																			<!--	
                                                                                <tr>
																					<td align="right"><STRONG><digi:trn key="help:keywords">Keywords</digi:trn></STRONG></td>
																					<td align="left"><digi:trn key="${helpForm.keywordsTrnKey}" linkAlwaysVisible="true">Keywords</digi:trn></td>
																				</tr>
																			-->
                                                                               <tr>
																					<td align="right"><STRONG><digi:trn>body</digi:trn></STRONG></td>
																					<td align="left"><digi:edit key="${helpForm.bodyEditKey}">no text preview</digi:edit></td>
																				</tr>
																				<tr>
																					<td colspan="2">
																						<table width="100%">
																							<tr>
																								<td align="right">
																									<c:set var="trnSaveBtn">
																										<digi:trn key="help:btn:save">Finish</digi:trn>
																									</c:set>
																									<input type="button" value="${trnSaveBtn }" onclick="finish('${helpForm.edit}');" />
																								</td>
																								<td align="left">
																									<c:set var="trnCancelBtn">
																										<digi:trn key="help:btn:cancel">Cancel</digi:trn>
																									</c:set>
																									<input type="button" value="${trnCancelBtn}" onclick="cancel();" />
																								</td>
																							</tr>
																						</table>
																					</td>
																				</tr>
																			</table>

																		</td>
																	</tr>
																	<tr>
																		<td/>
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
						</td>
						<td/>
						</table>
					</td>
					<td width="10"/>
				</tr>
			</table>
</c:if>
</digi:form>

