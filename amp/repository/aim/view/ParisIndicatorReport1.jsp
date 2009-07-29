<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>



<digi:instance property="aimParisIndicatorForm" />

			  <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0" >
					<tr bgcolor="#FFFFFF">						
					  <td colspan="10">
						<table border="0" cellspacing="0" cellpadding="0">
							  <!--<tr bgcolor="#C9C9C7">
								<td bgcolor="#C9C9C7" class="box-title">-->
								<tr>
								 <td>
									<table border="0"  cellspacing="0" cellpadding="0" rowspan="2">
										<tr bgcolor="#C9C9C7">
											<td width="10%" >
												<digi:trn key="aim:ParisIndicator 1">
													Paris Indicator 1
												</digi:trn>
											</td>
											<td bgcolor="#FFFFFF"><img src="../ampTemplate/images/corner-r.gif" ></td>
										</tr>
										<tr>
										&nbsp;
										</tr>
									</table>
								</td>
								
								
							  
							  </tr>
							  <tr>
								<td>	
									<table border="1" >
										<tr>
											<td valign ="center" align="center" rowspan = "2">
											Donor
												
											</td>
											<logic:iterate name="aimParisIndicatorForm" property="reportQuestions" id="reportQuest" type="org.digijava.module.aim.helper.ParisIndicatorHelper">
											<td valign ="center" align="center" colspan="2" >
												<bean:write name="reportQuest" property="helperQuestion"/>
											</td>
											</logic:iterate>
											<td rowspan="2" valign ="center" align="center">
												percentage
											</td>

										</tr>
										
										<!--<tr>
										<logic:iterate name="aimParisIndicatorForm" property="reportQuestions" id="reportQuest" type="org.digijava.module.aim.helper.ParisIndicatorHelper">
											<td valign="center" align="center">
												Year1
											</td>
											
											<td valign ="center" align="center">
												Year2
											</td>
											<td valign ="center" align="center">
												Year3
											</td>
											</logic:iterate>
											
										</tr>-->	
										<logic:iterate name="aimParisIndicatorForm" property="donorList" id="donorList" type="org.digijava.module.aim.helper.ParisIndicatorReportHelper">
										<tr>
											<td valign ="center" align="center">
										
											<bean:write name="donorList" property="piHelperDonorName"/>

											</td>
											<td valign ="center" align="center"  colspan="2">
													100
											</td>

											<td valign ="center" align="center" colspan="2">
													200
											</td>
											<!--<td valign ="center" align="center" >
													300
											</td>

											<td valign ="center" align="center" >
													400
											</td>
											<td valign ="center" align="center" >
													500
											</td>

											<td valign ="center" align="center" >
													600
											</td>-->
											<td valign ="center" align="center" >
													100%
											</td>

										</tr>
										</logic:iterate>
										
									</table>
								</td>							
							  </tr>

						  </table>

					  </td>
					</tr>
					
		
</table>



