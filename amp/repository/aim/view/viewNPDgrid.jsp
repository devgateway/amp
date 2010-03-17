<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@taglib uri="/taglib/struts-html" prefix="html"%>
<%@taglib uri="/taglib/digijava" prefix="digi"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<digi:instance property="aimNPDForm"/>

<table width="100%" cellSpacing="5" cellPadding="5" vAlign="top" border="0">
	<tr>
		<td valign="top">
			<table cellPadding="3" cellSpacing="3" width="100%" class="box-border-nopadding">
				<tr>
					<td align="left" vAlign="top">     
     					<c:if test="${aimNPDForm.mode == 1}">
							<table width="100%" cellSpacing="5" cellPadding="5" vAlign="top" border="0">
								<tr>
									<td valign="top">
										<table cellPadding="5" cellSpacing="5" width="100%">
											<tr class="otherLinks">
												<td align="left">
													<jsp:useBean id="urlParamsSort" type="java.util.Map" class="java.util.HashMap"/>
													<c:set target="${urlParamsSort}" property="selYears" value="${aimNPDForm.selYears}"/>
													<c:set target="${urlParamsSort}" property="donorIds" value="${aimNPDForm.donorIds}"/>
													<c:set target="${urlParamsSort}" property="statusId" value="${aimNPDForm.statusId}"/>
													<c:set target="${urlParamsSort}" property="startYear" value="${aimNPDForm.startYear}"/>
													<c:set target="${urlParamsSort}" property="endYear" value="${aimNPDForm.endYear}"/>			
													<digi:link styleId="export2xsl" href="/exportIndicators2xsl.do~programId=${aimNPDForm.programId}" name="urlParamsSort">
														<digi:trn>Export to Excel</digi:trn>&nbsp;<digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/xls_icon.jpg" border="0"/>
													</digi:link>
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												 	<digi:link styleId="printWin" href="#" onclick="window.print(); return false;">
													<digi:trn>Print</digi:trn>
													&nbsp;<digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/print_icon.gif" border="0"/></digi:link>
												</td>
											</tr>
									        <tr>
												<td align="left" vAlign="top">
													<table bgcolor="#f4f4f2" cellPadding="0" cellSpacing="0" width="100%" class="box-border-nopadding">
														<tr bgcolor="#006699">
															<td valign="center" width="100%" align ="center" class="textalb" height="20">
																<digi:trn>Graph</digi:trn>
															</td>
														</tr>
														<tr>
															<td bgcolor="#ecf3fd">
																<img id="graph">
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</c:if>
					
						<c:if test="${aimNPDForm.mode != 1}">
							<td align="left">
								<table class="otherLinks">
									<tr>
										<td align="left">
											<jsp:useBean id="urlParamsSort1" type="java.util.Map" class="java.util.HashMap"/>
											<c:set target="${urlParamsSort1}" property="selYears" value="${aimNPDForm.selYears}"/>
											<c:set target="${urlParamsSort1}" property="donorIds" value="${aimNPDForm.donorIds}"/>
											<c:set target="${urlParamsSort1}" property="statusId" value="${aimNPDForm.statusId}"/>
											<c:set target="${urlParamsSort1}" property="startYear" value="${aimNPDForm.startYear}"/>
											<c:set target="${urlParamsSort1}" property="endYear" value="${aimNPDForm.endYear}"/>								
																		
											<digi:link styleId="export2xsl" href="/exportIndicators2xsl.do~programId=${aimNPDForm.programId}" name="urlParamsSort1">
												<digi:trn >Export to Excel</digi:trn>&nbsp;<digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/xls_icon.jpg" border="0"/>
											</digi:link>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<digi:link styleId="printWin" href="#" onclick="window.print(); return false;"><digi:trn key="aim:print">Print</digi:trn> &nbsp;<digi:img src="/TEMPLATE/ampTemplate/imagesSource/common/print_icon.gif" border="0"/></digi:link>
										</td>
									</tr>
								</table>								
							</td>
						</c:if>
					</td>
				</tr>
				<tr>
					<td>
						<table bgcolor="#f4f4f2" cellPadding="0" cellSpacing="0" width="100%" class="box-border-nopadding">
							<tr bgcolor="#006699">
								<td valign="center" width="100%" align ="center" class="textalb" height="20">
									<digi:trn>indicators</digi:trn>
								</td>
							</tr>
							<tr>
								<td bgcolor="#ecf3fd" align="center">
									<table width="80%" id="indres" cellspacing="0">
										<tr>
											<td>
												&nbsp;
											</td>
											<c:forEach  var="year" items="${aimNPDForm.selYears}">
												<td colspan="3" align="center">
													<strong>${year}</strong>
												</td>
											</c:forEach>
										</tr>
										<tr>
											<td>
												<strong><digi:trn>Indicator Name</digi:trn></strong>
											</td>
											<c:forEach  var="year" items="${aimNPDForm.selYears}">
                                               <td align="center">
                                               		<strong><digi:trn>Base</digi:trn></strong>
                                               </td>
												<td align="center">
													<strong><digi:trn>Actual</digi:trn></strong>
												</td>
												<td align="center">
													<strong><digi:trn>Target</digi:trn></strong>
												</td>
				
											</c:forEach>
											
										</tr>
										<c:if test="${!empty aimNPDForm.indicators}">
											<c:forEach var="indRow" items="${aimNPDForm.indicators}">
												<tr>
													<td> 
														<span title="${indRow.description}">${indRow.name}</span>
													</td>
													<c:forEach  var="val" items="${indRow.values}">
                                                        <td align="right">
															${val.baseValue}
														</td>
														<td align="right">
															${val.actualValue}
														</td>
														<td align="right">
															${val.targetValue}
														</td>
															
												</c:forEach>
												</tr>
                                                <tr>
                                                	<td>
                                                    	<digi:trn>Source</digi:trn>
                                                    </td>
                                                    <c:forEach  var="valSource" items="${indRow.values}">
                                                    	<td align="right">
                                                        	${valSource.baseValueSource}
                                                        </td>
                                                        <td align="right">
                                                        	${valSource.actualValueSource}
                                                        </td>
                                                        <td align="right">
                                                        	${valSource.targetValueSource}
                                                        </td>
                                                	</c:forEach>
                                            	</tr>
											</c:forEach>
										</c:if>
									</table>
								</td>
							</tr>
							<tr>
								<td bgcolor="#ecf3fd">
									&nbsp;
								</td>
							</tr>
						</table>						
					</td>
				</tr>
				<!-- activities start -->
				<tr>
					<td>						
						<table bgcolor="#f4f4f2" cellPadding="2" cellSpacing="2" width="100%" class="box-border-nopadding">
							<tr bgcolor="#006699">
								<td valign="middle" width="100%" align ="center" class="textalb" height="20">
									<digi:trn>Activities</digi:trn>
								</td>
							</tr>							
							<tr>							
								<td>
									<c:if test="${not empty aimNPDForm.activities}">
										<table cellPadding="2" cellSpacing="2" width="100%">
											<tr bgcolor="#CCCCCC">
												<td nowrap="nowrap"><b><digi:trn>Title</digi:trn></b></td>
												<td nowrap="nowrap"><b><digi:trn>Status</digi:trn></b></td>
												<td nowrap="nowrap"><b><digi:trn>donor</digi:trn></b></td>
												<td colspan="2" nowrap="nowrap"><b><digi:trn>Start date</digi:trn></b></td>
												<td nowrap="nowrap"><b><digi:trn>Planned Commitments</digi:trn></b></td>
												<td nowrap="nowrap"><b><digi:trn>Actual Commitments</digi:trn></b></td>
												<td nowrap="nowrap"><b><digi:trn>Actual Disbursements</digi:trn></b></td>
											</tr>
											<c:forEach var="act" items="${aimNPDForm.activities}" varStatus="stat">
												<c:set var="clr">
													<c:if test="${stat.index%2==0}">#FFFFFF</c:if>
													<c:if test="${stat.index%2==1}">#CCCCCC</c:if>													
												</c:set>
												<tr bgcolor="${clr}">
													<td>${act.name}</td>
													<td>${act.status}</td>
													<td>
														<c:if test="${not empty act.donors}">
															<c:forEach var="donor" items="${act.donors}">
																<div>${donor.label}</div>
															</c:forEach>
														</c:if>
													</td>
													<td>${act.startDate}</td>
													<td>&nbsp;</td>
													<td>
														<c:if test="${act.plannedAmount!='N/A'}">${act.plannedAmount}</c:if>
														<c:if test="${act.plannedAmount=='N/A'}">--</c:if>
													</td>
													<td>
														<c:if test="${act.actualAmount!='N/A'}">${act.actualAmount}</c:if>
														<c:if test="${act.actualAmount=='N/A'}">--</c:if>
													</td>
													<td>
														<c:if test="${act.actualDisbAmount!='N/A'}">${act.actualDisbAmount}</c:if>
														<c:if test="${act.actualDisbAmount=='N/A'}">--</c:if>
													</td>
												</tr>
											</c:forEach>
											<tr height="2px"><td>&nbsp;</td></tr>
											<tr>
												<td colspan="5" align="right"><b><digi:trn>Totals</digi:trn>:</b></td>
												<td>${aimNPDForm.plannedCommSum}</td>
												<td>${aimNPDForm.actualSum}</td>
												<td>${aimNPDForm.actualDisbSum}</td>
											</tr>
										</table>
									</c:if>
									<c:if test="${empty aimNPDForm.activities}">
										<div><digi:trn>No Activities Present</digi:trn></div>
									</c:if>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<table width="100%">
	<tr>
		<td align="center">
		<html:button styleClass="dr-dialogmenu" property="submitButton" onclick="closeWindow();">
				<digi:trn key="aim:close">Close</digi:trn>
			</html:button>
		</td>
	</tr>

</table>