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
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<script language="JavaScript" type="text/javascript">	
	var npoDividePercentsEqually	= new ButtonWrapper('divide_npo_percents_equally');
	var prProgDividePercentsEqually	= new ButtonWrapper('divide_prProg_percents_equally');
	var secProgDividePercentsEqually	= new ButtonWrapper('divide_secProg_percents_equally');
	
	function updateDividePercentsEquallyButton() {
		// if 100 can't divide on progs amount without remain, button should be disabled 		
		var npoProgs=$("#npoProg").find("input.npoProgMultibox");
		if(npoProgs.length!=0 && 100%npoProgs.length==0){
			npoDividePercentsEqually.enable();
		}else{
			npoDividePercentsEqually.disable();
		}
		var primaryProgs=$("#primaryProg").find("input.primaryProgMultibox");
		if(primaryProgs.length!=0){
			if(100%primaryProgs.length==0){
				prProgDividePercentsEqually.enable();
			}else{
				prProgDividePercentsEqually.disable();
			}	
		}
		
		var secProgs=$("#secProg").find("input.secProgMultibox");
		if(secProgs.length!=0){
			if(100%secProgs.length==0){
				secProgDividePercentsEqually.enable();
			}else{
				secProgDividePercentsEqually.disable();
			}	
		}	
	}
	
	YAHOO.util.Event.on(window, "load", updateDividePercentsEquallyButton);
</script>
		<field:display name="National Planning Objectives" feature="NPD Programs">		
		<digi:instance property="aimEditActivityForm" />		
		<tr>
			<td class="separator1" title="<digi:trn>Set of policies, projects and strategies grouped by area</digi:trn>">
			    <digi:trn>Program</digi:trn>
			</td>
		</tr>
			<tr>
				<td>
				  <digi:trn>Select the program from the list.</digi:trn>
				</td>
			</tr>         
            <tr>
              	<td>
                	<table cellPadding=5 cellSpacing=1 border=0 width="100%" bgcolor="#d7eafd">
                  		<tr>
                    		<td align="left">
			                    <b>
			                        <digi:trn>National Planning Objectives</digi:trn>
                      			</b>
                    		</td>
                  		</tr>
                  		<tr>
                    		<td bgcolor="#ffffff" width="100%">
                      			<table cellPadding=1 cellSpacing=1 border=0	bgcolor="#ffffff" width="100%">
                        			<tr>
                          				<td bgcolor="#ffffff"/>
                        			</tr>
                        			<tr>
                         	 			<td>
                            				<table cellSpacing="0" cellPadding="0" border="0" bgcolor="#ffffff" width="100%">
                           						<digi:trn>Default Program</digi:trn>:
			        							<c:if test="${!empty aimEditActivityForm.programs.nationalSetting}">
			        								${aimEditActivityForm.programs.nationalSetting.defaultHierarchy.name}
        										</c:if>
												<br>
												<tr>
                                           			<td>
                                          				<c:if test="${!empty aimEditActivityForm.programs.nationalPlanObjectivePrograms}">
															<div id="nationalProgConfig">
                                            					<table cellSpacing="0" cellPadding="0" border="0" bgcolor="#ffffff" width="100%" id="npoProg">
                                         							<tbody>
                                         								<c:forEach var="nationalPlanObjectivePrograms" items="${aimEditActivityForm.programs.nationalPlanObjectivePrograms}">
																			<c:set var="program" value="${nationalPlanObjectivePrograms.program}" />
																			<tr>
																				<td>
																					<html:multibox property="programs.selectedNPOPrograms" value="${program.ampThemeId}" styleClass="npoProgMultibox"/>
																					${nationalPlanObjectivePrograms.hierarchyNames}
																				</td>
																				<td width="5%" align="right" vAlign="center" nowrap="nowrap">
																					<FONT color="red">*</FONT> 
																					<digi:trn>Percentage</digi:trn>
																				</td>
																				<td width="5%" vAlign="center" align="left">
																					<html:text name="nationalPlanObjectivePrograms" indexed="true"
																					property="programPercentage" size="4" maxlength="5"
																					onkeyup="fnChk(this, 'program')" style="background-color: white;"/>
																				</td>
																			</tr>
																		</c:forEach>
																	</tbody>
																</table>
															</div>
                                          				</c:if>
													</td>
												</tr>
                                          		<tr>
													<td>
														<table cellSpacing="2" cellPadding="2">
															<tr>
                                              					<td>&nbsp;
                                                					<c:if test="${!empty aimEditActivityForm.programs.nationalSetting.defaultHierarchy.name && (aimEditActivityForm.programs.nationalSetting.allowMultiple||empty aimEditActivityForm.programs.nationalPlanObjectivePrograms)}">
                                   										<field:display name="Add Programs Button - National Plan Objective" feature="Program">
                                      										<html:button styleClass="dr-menu" property="submitButton" onclick="addProgram(1,${aimEditActivityForm.programs.nationalSetting.ampProgramSettingsId});">
																			<digi:trn key="btn:addPrograms">Add Programs</digi:trn>
																			</html:button>
                                    									</field:display>
                                      								</c:if>
																</td>
																<c:if test="${!empty aimEditActivityForm.programs.nationalPlanObjectivePrograms}">
																	<td>&nbsp;
                                              							<field:display name="Remove Program Button - National Plan Objective" feature="Program">
                                         									<html:button styleClass="dr-menu" property="submitButton" onclick="remProgram(1);">
																			<digi:trn key="btn:removeProgram">Remove program</digi:trn>
																			</html:button>
                                         								</field:display>
                                    								</td>
							                                 		<td> &nbsp;
																		<html:button styleClass="dr-menu" property="submitButton" onclick="dividePercentages('nationalProg');" styleId="divide_npo_percents_equally">
																			<digi:trn key="dividePercentagesEqually">Divide percentages equally</digi:trn>
																		</html:button>                                                          
                                  									</td>
                                								</c:if>
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
	  	</field:display>

	 	<field:display name="Primary Program" feature="NPD Programs">
            <tr>
                <td>
                    <table cellPadding="5" cellSpacing="1" border="0" width="100%" bgcolor="#d7eafd">
                        <tr>
                            <td align="left">
                                <b><digi:trn>Primary Program</digi:trn>
                                </b>
                            </td>
                        </tr>
                        <tr>
                            <td bgcolor="#ffffff" width="100%">
                                <table cellPadding="1" cellSpacing="1" border="0" bgcolor="#ffffff" width="100%">
                                    <tr>
                                        <td bgcolor="#ffffff"/>
                                    </tr>

                                    <tr>
                                        <td>
                                            <table cellSpacing="0" cellPadding="0" border="0" bgcolor="#ffffff" width="100%">
                                             	<digi:trn>Default Program</digi:trn>:
                                              	<c:if test="${!empty aimEditActivityForm.programs.primarySetting}">
                                              		${aimEditActivityForm.programs.primarySetting.defaultHierarchy.name}
                                              	</c:if>
                                              	<br>
                                                <tr>
                                                  	<td>
                                                		<c:if test="${!empty aimEditActivityForm.programs.primaryPrograms}">
                                                  			<div id="primaryProgConfig">
                                                  				<table cellSpacing="0" cellPadding="0" border="0" bgcolor="#ffffff" width="100%" id="primaryProg">
                                               						<tbody>
					                                               		<c:forEach var="primaryPrograms" items="${aimEditActivityForm.programs.primaryPrograms}">
					                                                        <c:set var="program" value="${primaryPrograms.program}"/>
					                                                   		<tr>
					                                                            <td>
					                                                              <html:multibox property="programs.selectedPPrograms" value="${program.ampThemeId}" styleClass="primaryProgMultibox"/> ${primaryPrograms.hierarchyNames}
					                                                            </td>
					                                                              <td width="5%" align="right" valign="center" nowrap="nowrap"><font color="red">*</font>
					                                                                  <digi:trn>Percentage</digi:trn></td>
					                                                             <td width="5%" vAlign="center" align="left">
					                                                              <html:text name="primaryPrograms" indexed="true" property="programPercentage"
					                                                              	size="4" maxlength="5" onkeyup="fnChk(this, 'program')" style="background-color: white;"/>
					                                                            </td>
					                                                    	</tr>
					                                                  	</c:forEach>
																	</tbody>
																</table>
															</div>
                                                		</c:if>
													</td>
												</tr>
                                                <tr>
                                                  	<td>
														<table cellSpacing="2" cellPadding="2">
															<tr>
																<td>&nbsp;
                                                      				<c:if test="${!empty aimEditActivityForm.programs.primarySetting.defaultHierarchy&&(aimEditActivityForm.programs.primarySetting.allowMultiple||empty aimEditActivityForm.programs.primaryPrograms)}">
				                                                       <field:display name="Add Programs Button - Primary Programs" feature="Program">
					                                                       <html:button styleClass="dr-menu" property="submitButton" onclick="addProgram(2,${aimEditActivityForm.programs.primarySetting.ampProgramSettingsId});">
																				<digi:trn>Add Programs</digi:trn>
																			</html:button>
                                                       					</field:display>
                                                      				</c:if>
																</td>
                                                      			<c:if test="${!empty aimEditActivityForm.programs.primaryPrograms}">
																	<td>&nbsp;
																		<field:display name="Remove Program Button - Primary Programs" feature="Program">
																			<html:button styleClass="dr-menu" property="submitButton" onclick="remProgram(2);">
																				<digi:trn>Remove program</digi:trn>
																			</html:button>
                                                        				</field:display>
																	</td>
                                                  		 			<td> &nbsp;
																		<html:button styleClass="dr-menu" property="submitButton" onclick="dividePercentages('primaryProg');" styleId="divide_prProg_percents_equally">
																			<digi:trn>Divide percentages equally</digi:trn>
																		</html:button>                                                            
                                                   					</td>
                                                      			</c:if>
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
	  	</field:display>

	  	<field:display name="Secondary Program" feature="NPD Programs">
            <tr>
                <td>
                    <table cellPadding="5" cellSpacing="1" border="0" width="100%" bgcolor="#d7eafd">
                        <tr>
                            <td align="left">
                                <b>
                                    <digi:trn>Secondary Program</digi:trn>
                                </b>
                            </td>
                        </tr>
                        <tr>
                            <td bgcolor="#ffffff" width="100%">
                                <table cellPadding="1" cellSpacing="1" border="0" bgcolor="#ffffff" width="100%">
                                    <tr>
                                        <td bgcolor="#ffffff" />
                                    </tr>

                                    <tr>
                                        <td>
                                            <table cellSpacing="0" cellPadding="0" border="0" bgcolor="#ffffff" width="100%">
                                               	<digi:trn>Default Program</digi:trn>:
                                              	<c:if test="${!empty aimEditActivityForm.programs.secondarySetting}">
                                              	 	${aimEditActivityForm.programs.secondarySetting.defaultHierarchy.name}
                                               	</c:if>
                                               	<br>
                                                <tr>
                                                  	<td>
                                                		<c:if test="${!empty aimEditActivityForm.programs.secondaryPrograms}">
                                                  			<div id="secondaryProgConfig">
                                                  				<table cellSpacing="0" cellPadding="0" border="0" bgcolor="#ffffff" width="100%" id="secProg">
                                               						<tbody>
                                               							<c:forEach var="secondaryPrograms" items="${aimEditActivityForm.programs.secondaryPrograms}" >
                                                   							<c:set var="program" value="${secondaryPrograms.program}"/>
                                                    						<tr>
                                                            					<td>
                                                              						<html:multibox property="programs.selectedSPrograms" value="${program.ampThemeId}" styleClass="secProgMultibox"/> ${secondaryPrograms.hierarchyNames}
                                                            					</td>
                                                              					<td width="5%" align="right" valign="center" nowrap="nowrap">
                                                              
                                                              						<font color="red">*</font>
                                                                  					<digi:trn>Percentage</digi:trn>
																				</td>
				                                                             	<td width="5%" vAlign="center" align="left">
				                                                              		<html:text name="secondaryPrograms" indexed="true" property="programPercentage"
				                                                              		size="4" maxlength="5" onkeyup="fnChk(this, 'program')" style="background-color: white;"/>
                                                            					</td>
                                                          					</tr>
                                                   						</c:forEach>
																	</tbody>	
																</table>
															</div>
                                                		</c:if>
													</td>
												</tr>
                                            	<tr>
                                                	<td>
														<table>
															<tr>
																<td>&nbsp;
			                                                       <c:if test="${!empty aimEditActivityForm.programs.secondarySetting.defaultHierarchy&&(aimEditActivityForm.programs.secondarySetting.allowMultiple||empty aimEditActivityForm.programs.secondaryPrograms)}">
				                                                       <field:display name="Add Programs Button - Secondary Programs" feature="Program">
				                                                       <html:button styleClass="dr-menu" property="submitButton" onclick="addProgram(3,${aimEditActivityForm.programs.secondarySetting.ampProgramSettingsId});">
																			<digi:trn>Add Programs</digi:trn>
																		</html:button>
				                                                      	</field:display>
			                                                       	</c:if>
																</td>
	                                                      		<c:if test="${!empty aimEditActivityForm.programs.secondaryPrograms}">
																	<td>&nbsp;
				                                                       <field:display name="Remove Program Button - Secondary Programs" feature="Program">
				                                                         	<html:button styleClass="dr-menu" property="submitButton" onclick="remProgram(3);">
																			<digi:trn>Remove program</digi:trn>
																			</html:button>
	                                                         			</field:display>
																	</td>
																	<td> &nbsp;
																		<html:button styleClass="dr-menu" property="submitButton" onclick="dividePercentages('secondaryProg');" styleId="divide_secProg_percents_equally">
																			<digi:trn>Divide percentages equally</digi:trn>
																		</html:button>                                                            
	                                                   				</td>
	                                                      		</c:if>
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

            <field:display name="NPD Program Description" feature="Program">
                <tr>
                    <td>
                         <a title="<digi:trn key="aim:ProgramDesc">Description of program, objectives, or associated projects</digi:trn>">
                         Description
		  				</a>
                    </td>
                </tr>
                <tr>
                    <td>
                        <a title="<digi:trn key="aim:ProgramDesc">Description of program, objectives, or associated projects</digi:trn>">
                        <html:textarea property="programs.programDescription" rows="3" cols="75" styleClass="inp-text"/>
		  				</a>
                    </td>
                </tr>
			</field:display>
		</field:display>
		