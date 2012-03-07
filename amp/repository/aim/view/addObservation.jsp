<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<jsp:include page="scripts/newCalendar.jsp"  />
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
									
<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border=0>
	<digi:instance property="aimEditActivityForm" />
	<digi:form name="addObservationForm" type="aimEditActivityForm"  styleId="addObservationForm"  action="/addObservation.do" method="post" >
		<script language="JavaScript">
			
			
		</script>
		
		<html:hidden property="observations.issueId"/>
		<html:hidden property="funding.event"/>
		<html:hidden property="edit" value="true"/>
		<html:hidden property="editAct"/>
		<tr>
			<td vAlign="top">
				<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
					<tr>
						<td align=left vAlign=top>
							<table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
								<tr>
									<td align="center" bgcolor=#ECF3FD>
										<table cellSpacing=2 cellPadding=2>
											<tr>
												<td align="right" valign="top">
											  		<digi:trn key="aim:observation">Observation</digi:trn>
											  		&nbsp; 
											  	</td>
									     		<td valign="top">
													<a title="<digi:trn key="aim:observationsForTheActivity">The observations for the activity</digi:trn>">
														<html:textarea styleId="observation" property="observations.issue" styleClass="inp-text" rows="3" cols="60"/>
													</a>
												</td>
											</tr>								
											<field:display name="Regional Observations Date" feature="Regional Observations" >
											<tr>
												<td align="right"><digi:trn key="aim:dateOfobservation">Date of Observation</digi:trn>&nbsp;</td>
										      	<td>
										      		<html:text property="observations.issueDate" size="10" readonly="true" styleId="observationDate" styleClass="inp-text" /> 
													<a id="clear1" href="javascript:clearDate(document.getElementById('observationDate'), 'clear1')">
			 	    									<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0"/>
													</a>
													<a id="date1" href='javascript:pickDateWithClear("date1", document.getElementById("observationDate"),"clear1")'>
														<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
													</a>
												</td>
										  	</tr>
										  	</field:display>
											<tr>
												<td align="center" colspan=2>
													<table cellPadding=5>
														<tr>
															<td>
																<c:if test="${aimEditActivityForm.observations.issueId == -1}">
																	<input type="submit" onclick="return validateObservation()" value="<digi:trn key='btn:addObservation'>Add</digi:trn>" class="dr-menu">												
																</c:if>
																<c:if test="${aimEditActivityForm.observations.issueId != -1}">
																	<input type="submit" onclick="return validateObservation()" value="<digi:trn key='btn:updateObservation'>Update</digi:trn>" class="dr-menu">													
																</c:if>
															</td>
															<td>
																<input type="button" value="<digi:trn key='btn:clear'>Clear</digi:trn>" class="dr-menu" onclick="javascript:return clearFieldObservation()">													
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
	</digi:form>
</table>