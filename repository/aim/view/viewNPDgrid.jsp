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
										<td align="left">
											<jsp:useBean id="urlParamsSort" type="java.util.Map" class="java.util.HashMap"/>
											<c:set target="${urlParamsSort}" property="selYears" value="${aimNPDForm.selYears}"/>			
											<digi:link styleId="export2xsl" href="/exportIndicators2xsl.do~programId=${aimNPDForm.programId}" name="urlParamsSort">
												<digi:trn >Export to Excel</digi:trn>&nbsp;<digi:img src="images/xls_icon.jpg" border="0"/>
											</digi:link>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										 	<digi:link styleId="printWin" href="#" onclick="window.print(); return false;">
											<digi:trn key="aim:print">Print</digi:trn>
											&nbsp;<digi:img src="images/print_icon.gif" border="0"/></digi:link>
										   </td>
									            <tr>
												<td align="left" vAlign="top">
													<table bgcolor="#f4f4f2" cellPadding="0" cellSpacing="0" width="100%" class="box-border-nopadding">
														<tr bgcolor="#006699">
															<td valign="center" width="100%" align ="center" class="textalb" height="20">
																<digi:trn key="aim:indGrid:indicGraphHeader">Graph</digi:trn>
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
						<jsp:useBean id="urlParamsSort1" type="java.util.Map" class="java.util.HashMap"/>
						<c:set target="${urlParamsSort1}" property="selYears" value="${aimNPDForm.selYears}"/>								
													
						<digi:link styleId="export2xsl" href="/exportIndicators2xsl.do~programId=${aimNPDForm.programId}" name="urlParamsSort1">
							<digi:trn >Export to Excel</digi:trn>&nbsp;<digi:img src="images/xls_icon.jpg" border="0"/>
						</digi:link>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<digi:link styleId="printWin" href="#" onclick="window.print(); return false;"><digi:trn key="aim:print">Print</digi:trn> &nbsp;<digi:img src="images/print_icon.gif" border="0"/></digi:link>
					</td>
					</c:if>
						<table bgcolor="#f4f4f2" cellPadding="0" cellSpacing="0" width="100%" class="box-border-nopadding">
							<tr bgcolor="#006699">
								<td valign="center" width="100%" align ="center" class="textalb" height="20">
									<digi:trn key="aim:indGrid:indicatorsHader">indicators</digi:trn>
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
												<strong><digi:trn key="aim:indGrid:indicname">Indicator Name</digi:trn></strong>
											</td>
											<c:forEach  var="year" items="${aimNPDForm.selYears}">
                                               <td align="center">
                                               		<strong><digi:trn key="aim:indGrid:baseVal">Base</digi:trn></strong>
                                               </td>
												<td align="center">
													<strong><digi:trn key="aim:indGrid:actualVal">Actual</digi:trn></strong>
												</td>
												<td align="center">
													<strong><digi:trn key="aim:indGrid:targetVal">Target</digi:trn></strong>
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