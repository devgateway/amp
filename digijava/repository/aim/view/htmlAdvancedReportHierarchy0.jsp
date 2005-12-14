<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">
	function load() 
	{
		window.print();
	}

	function unload() {}
</script>

<digi:instance property="aimAdvancedReportForm" />

<table border="1" bordercolor="#808080" width="85%" cellspacing=1 cellpadding=1 valign=top align=left style="border-collapse: collapse">
	<logic:notEmpty name="aimAdvancedReportForm" property="allReports"> 
		<% int x = 1; %>
		<logic:iterate name="aimAdvancedReportForm" property="allReports" id="report" type="org.digijava.module.aim.helper.Report">
			<tr>
				<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
					<strong>
						S.No. <%= x %>
					</strong>
					<% x++ ;%>
				</td></tr>
				<logic:iterate name="report"  property="records" id="records" type="org.digijava.module.aim.helper.AdvancedReport">
					<logic:notEmpty name="records" property="title">
						<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
							<strong>Title			:</strong>
							<bean:write name="records" property="title" />
						</td></tr>
					</logic:notEmpty>
				</logic:iterate>	
				<logic:iterate name="report"  property="records" id="records" type="org.digijava.module.aim.helper.AdvancedReport">
					<logic:notEmpty name="records" property="objective">
						<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
							<strong>Objective			:</strong>
							<bean:write name="records" property="objective"/>
						</td></tr>
					</logic:notEmpty>
				</logic:iterate>
				<logic:iterate name="report"  property="records" id="records" type="org.digijava.module.aim.helper.AdvancedReport">
					<logic:notEmpty name="records" property="description">
						<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
							<strong>Description			:</strong>
							<bean:write name="records" property="description"/>
						</td></tr>
					</logic:notEmpty>
				</logic:iterate>
				<tr>	
					<logic:iterate name="aimAdvancedReportForm"  property="titles" id="titles" type="org.digijava.module.aim.helper.Column">
						<logic:notEqual name="titles" property="columnName" value="Project Title">
							<logic:notEqual name="titles" property="columnName" value="Objective">	
								<logic:notEqual name="titles" property="columnName" value="Description">
									<td width="9%">
										<div align="center"><strong>
											<bean:write name="titles" property="columnName" />
										</strong></div>
									</td>
								</logic:notEqual>
							</logic:notEqual>
						</logic:notEqual>
					</logic:iterate>
				</tr>
				<logic:iterate name="report"  property="records" id="records" type="org.digijava.module.aim.helper.AdvancedReport">
					<logic:notEmpty name="records" property="ampId">
						<td align="center" width="9%">
							<bean:write name="records" property="ampId" />
						</td>
					</logic:notEmpty>
					<logic:notEmpty name="records" property="actualStartDate">
						<td align="center" width="9%">
							<bean:write name="records" property="actualStartDate" />
						</td>
					</logic:notEmpty>
					<logic:notEmpty name="records" property="actualCompletionDate">
						<td align="center" width="9%">
							<bean:write name="records" property="actualCompletionDate" />
						</td>
					</logic:notEmpty>
					<logic:notEmpty name="records" property="status">
						<td align="center" width="9%">
							<bean:write name="records" property="status" />
						</td>
					</logic:notEmpty>
					<logic:notEmpty name="records" property="level">
						<td align="center" width="9%">
							<bean:write name="records" property="level" />
						</td>
					</logic:notEmpty>
					<logic:notEmpty name="records" property="assistance">
						<td align="center" width="9%">
							<logic:iterate name="records" id="assistance" property="assistance"> 
								<%=assistance%>	<br>
							</logic:iterate>
						</td>
					</logic:notEmpty>
					<logic:notEmpty name="records" property="sectors">
						<td align="center" width="9%">
							<logic:iterate name="records" id="sectors" property="sectors"> 
								<%=sectors%>	<br>
							</logic:iterate>
						</td>
					</logic:notEmpty>
					<logic:notEmpty name="records" property="regions">
						<td align="center" width="9%">
							<logic:iterate name="records" id="regions" property="regions">
								<%=regions%>	<br>
							</logic:iterate>
						</td>
					</logic:notEmpty>
					<logic:notEmpty name="records" property="contacts">
						<td align="center" width="9%">
							<logic:iterate name="records" id="contacts" property="contacts"> 
								<%=contacts%>	<br>
							</logic:iterate>
						</td>
					</logic:notEmpty>
					<logic:notEmpty name="records" property="modality">
						<td align="center" width="9%">
							<logic:iterate name="records" id="modality" property="modality"> 
								<%=modality%>	<br>
							</logic:iterate>
						</td>
					</logic:notEmpty>
					<logic:notEmpty name="records" property="donors">
						<td align="center" width="9%">
							<logic:iterate name="records" id="donors" property="donors"> 
								<%=donors%>	<br>
							</logic:iterate>
						</td>
					</logic:notEmpty>
				</logic:iterate>
				<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
					<table border="1" bordercolor="#C0C0C0" cellspacing=1 cellpadding=1 valign=top align=left style="border-collapse:collapse">
						<tr>
							<logic:equal name="aimAdvancedReportForm" property="option" value="A">
								<td width="12%"><strong>Year</strong></td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="option" value="Q">
								<td width="12%" colspan="2"><strong>Year</strong></td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
								<td width="12%"><strong>Actual Commitment</strong></td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
								<td width="12%"><strong>Actual Disbursement</strong></td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
								<td width="12%"><strong>Actual Expenditure</strong></td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
								<td width="13%"><strong>Planned Commitment</strong></td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
								<td width="13%"><strong>Planned Disbursement</strong></td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
								<td width="13%"><strong>Planned Expenditure</strong></td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
								<td width="13%"><strong>UnDisbursed</strong></td>
							</logic:equal>
						</tr>
						<%
							int a = 1;
							int b = 1;
						%>	
						<logic:equal name="aimAdvancedReportForm" property="option" value="A">
						<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
							<logic:notEmpty name="records" property="ampFund">
								<logic:iterate name="records"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
									<%
										if((a==1 && b==1) || (a==2 && b==6) || (a==3 && b==11))
								  		{		
									%>
									<tr>
										<td width="12%"><strong>
											<%=fiscalYearRange%></strong>
										</td>
										<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
											<td width="12%">
												<logic:notEqual name="ampFund" property="commAmount" value="0">
													<bean:write name="ampFund" property="commAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
											<td width="12%">
												<logic:notEqual name="ampFund" property="disbAmount" value="0">
													<bean:write name="ampFund" property="disbAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
											<td width="12%">
												<logic:notEqual name="ampFund" property="expAmount" value="0">
													<bean:write name="ampFund" property="expAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
											<td width="13%">
												<logic:notEqual name="ampFund" property="plCommAmount" value="0">
													<bean:write name="ampFund" property="plCommAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
											<td width="13%">
												<logic:notEqual name="ampFund" property="plDisbAmount" value="0">
													<bean:write name="ampFund" property="plDisbAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
											<td width="13%">
												<logic:notEqual name="ampFund" property="plExpAmount" value="0">
													<bean:write name="ampFund" property="plExpAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
											<td width="13%">
												<logic:notEqual name="ampFund" property="unDisbAmount" value="0">
													<bean:write name="ampFund" property="unDisbAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
									</tr>
									<%
										}
										if(b==12)
								  		{
									%>
									<tr>
										<td width="12%"><strong>
											Total</strong>
										</td>
										<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
											<td width="12%">
												<logic:notEqual name="ampFund" property="commAmount" value="0">
													<bean:write name="ampFund" property="commAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
											<td width="12%">
												<logic:notEqual name="ampFund" property="disbAmount" value="0">
													<bean:write name="ampFund" property="disbAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
											<td width="12%">
												<logic:notEqual name="ampFund" property="expAmount" value="0">
													<bean:write name="ampFund" property="expAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
											<td width="13%">
												<logic:notEqual name="ampFund" property="plCommAmount" value="0">
													<bean:write name="ampFund" property="plCommAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
											<td width="13%">
												<logic:notEqual name="ampFund" property="plDisbAmount" value="0">
													<bean:write name="ampFund" property="plDisbAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
											<td width="13%">
												<logic:notEqual name="ampFund" property="plExpAmount" value="0">
													<bean:write name="ampFund" property="plExpAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
											<td width="13%">
												<logic:notEqual name="ampFund" property="unDisbAmount" value="0">
													<bean:write name="ampFund" property="unDisbAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
									</tr>
									<%
										}
										b++;
									%>	
									</logic:iterate>
								</logic:notEmpty>
								<%
									a++;
								%>
							</tr>
						</logic:iterate>
						</logic:equal>


						<%
							int p=1;
							int q=1;
						%>

						<logic:equal name="aimAdvancedReportForm" property="option" value="Q">
						<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
							<logic:notEmpty name="records" property="ampFund">
								<% int mark=0; %>
									<logic:iterate name="records"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
									
		  							<%
										mark++;
										if((p==1 && q==1) || (p==1 && q==2) || (p==1 && q==3) || (p==1 && q==4) || (p==2 && q==18) || (p==2 && q==19) || (p==2 && q==20) || (p==2 && q==21) || (p==3 && q==35) || (p==3 && q==36) || (p==3 && q==37) || (p==3 && q==38))
								  		{		
									%>
							
									<tr>
										<td width="8%" rowspan="4"><strong>
											<%=fiscalYearRange%></strong>
										</td>
										<td width="4%"><strong>
											<% 
												int temp = 0;
												temp = mark%4;
												if(temp == 0)
													temp = 4;
											%>
											Q <%= temp%></strong>
										</td>
										<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
											<td width="12%">
												<logic:notEqual name="ampFund" property="commAmount" value="0">
													<bean:write name="ampFund" property="commAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
											<td width="12%">
												<logic:notEqual name="ampFund" property="disbAmount" value="0">
													<bean:write name="ampFund" property="disbAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
											<td width="12%">
												<logic:notEqual name="ampFund" property="expAmount" value="0">
													<bean:write name="ampFund" property="expAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
											<td width="13%">
												<logic:notEqual name="ampFund" property="plCommAmount" value="0">
													<bean:write name="ampFund" property="plCommAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
											<td width="13%">
												<logic:notEqual name="ampFund" property="plDisbAmount" value="0">
													<bean:write name="ampFund" property="plDisbAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
											<td width="13%">
												<logic:notEqual name="ampFund" property="plExpAmount" value="0">
													<bean:write name="ampFund" property="plExpAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
											<td width="13%">
												<logic:notEqual name="ampFund" property="unDisbAmount" value="0">
													<bean:write name="ampFund" property="unDisbAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
									</tr>
									<%
										}
										if(q == 39)
								  		{
									%>
									<tr>
										<td width="12%" colspan="2"><strong>
											Total</strong>
										</td>
										<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
											<td width="12%">
												<logic:notEqual name="ampFund" property="commAmount" value="0">
													<bean:write name="ampFund" property="commAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
											<td width="12%">
												<logic:notEqual name="ampFund" property="disbAmount" value="0">
													<bean:write name="ampFund" property="disbAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
											<td width="12%">
												<logic:notEqual name="ampFund" property="expAmount" value="0">
													<bean:write name="ampFund" property="expAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
											<td width="13%">
												<logic:notEqual name="ampFund" property="plCommAmount" value="0">
													<bean:write name="ampFund" property="plCommAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
											<td width="13%">
												<logic:notEqual name="ampFund" property="plDisbAmount" value="0">
													<bean:write name="ampFund" property="plDisbAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
											<td width="13%">
												<logic:notEqual name="ampFund" property="plExpAmount" value="0">
													<bean:write name="ampFund" property="plExpAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
											<td width="13%">
												<logic:notEqual name="ampFund" property="unDisbAmount" value="0">
													<bean:write name="ampFund" property="unDisbAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
									</tr>
							
									<%
										}
										q++;
									%>	

									</logic:iterate>
								</logic:notEmpty>
								
								<%
									p++;
								%>
							</tr>
						</logic:iterate>
						</logic:equal>


					</table>
				</td></tr>
			</tr>
		</logic:iterate>
		
		<logic:equal name="aimAdvancedReportForm" property="option" value="A">
		<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/> class="head2-name" align="left">
			<strong>
				Grand Total
			</strong>
		</td></tr>
		<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
			<table border="1" cellspacing=1 cellpadding=1 valign=top align=left style="border-collapse: collapse">
				<tr>
					<td width="12%"><strong>	Total  </strong></td>
					<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
						<td width="12%"><strong>Actual Commitment</strong></td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
						<td width="12%"><strong>Actual Disbursement</strong></td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
						<td width="12%"><strong>Actual Expenditure</strong></td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
						<td width="13%"><strong>Planned Commitment</strong></td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
						<td width="13%"><strong>Planned Disbursement</strong></td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
						<td width="13%"><strong>Planned Expenditure</strong></td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
						<td width="13%"><strong>UnDisbursed</strong></td>
					</logic:equal>
				</tr>
				<%
					int i=1;
					int j=1;
				%>	
				<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
					<logic:iterate name="aimAdvancedReportForm"  property="totFund" id="totFund" type="org.digijava.module.aim.helper.AmpFund">
					<%
						if((i==1 && j==1) || (i==2 && j==6) || (i==3 && j==11))
		  				{		
					%>
						<tr>
							<td width="12%"><strong>
								<%=fiscalYearRange%></strong>
							</td>
							<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
								<td width="12%">
									<logic:notEqual name="totFund" property="commAmount" value="0">
										<bean:write name="totFund" property="commAmount" />
									</logic:notEqual>
								</td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
								<td width="12%">
									<logic:notEqual name="totFund" property="disbAmount" value="0">
										<bean:write name="totFund" property="disbAmount" />
									</logic:notEqual>
								</td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
								<td width="12%">		
									<logic:notEqual name="totFund" property="expAmount" value="0">
										<bean:write name="totFund" property="expAmount" />
									</logic:notEqual>
								</td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
								<td width="13%">
									<logic:notEqual name="totFund" property="plCommAmount" value="0">
										<bean:write name="totFund" property="plCommAmount" />
									</logic:notEqual>
								</td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
								<td width="13%">
									<logic:notEqual name="totFund" property="plDisbAmount" value="0">
										<bean:write name="totFund" property="plDisbAmount" />
									</logic:notEqual>
								</td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
								<td width="13%">
									<logic:notEqual name="totFund" property="plExpAmount" value="0">
										<bean:write name="totFund" property="plExpAmount" />
									</logic:notEqual>
								</td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
								<td width="13%">
									<logic:notEmpty name="totFund" property="unDisbAmount">
										<logic:notEqual name="totFund" property="unDisbAmount" value="0">
											<bean:write name="totFund" property="unDisbAmount" />
										</logic:notEqual>
									</logic:notEmpty>
								</td>
							</logic:equal>
						</tr>
					<%
						}
						if(j==12)
		  				{
					%>
						<tr>
							<td width="12%"><strong>
								Grand Total</strong>
							</td>
							<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
								<td width="12%">
									<logic:notEqual name="totFund" property="commAmount" value="0">
										<bean:write name="totFund" property="commAmount" />
									</logic:notEqual>
								</td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
								<td width="12%">
									<logic:notEqual name="totFund" property="disbAmount" value="0">
										<bean:write name="totFund" property="disbAmount" />
									</logic:notEqual>
								</td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
								<td width="12%">
									<logic:notEqual name="totFund" property="expAmount" value="0">
										<bean:write name="totFund" property="expAmount" />
									</logic:notEqual>
								</td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
								<td width="13%">
									<logic:notEqual name="totFund" property="plCommAmount" value="0">
										<bean:write name="totFund" property="plCommAmount" />
									</logic:notEqual>
								</td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
								<td width="13%">
									<logic:notEqual name="totFund" property="plDisbAmount" value="0">
										<bean:write name="totFund" property="plDisbAmount" />
									</logic:notEqual>
								</td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
								<td width="13%">
									<logic:notEqual name="totFund" property="plExpAmount" value="0">
										<bean:write name="totFund" property="plExpAmount" />
									</logic:notEqual>
								</td>
							</logic:equal>
							<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
								<td width="13%">
									<logic:notEmpty name="totFund" property="unDisbAmount">
										<logic:notEqual name="totFund" property="unDisbAmount" value="0">
											<bean:write name="totFund" property="unDisbAmount" />
										</logic:notEqual>
									</logic:notEmpty>		
								</td>
							</logic:equal>
						</tr>
					<%
						}
						j++;
					%>	
					</logic:iterate>
					<%
						i++;
					%>
				</logic:iterate>
			</table>
		</td></tr>
		</logic:equal>



		<logic:equal name="aimAdvancedReportForm" property="option" value="Q">
		<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/> class="head2-name" align="left">
			<strong>
				Grand Total
			</strong>
		</td></tr>
		<tr><td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/>>
			<table border="1" cellspacing=1 cellpadding=1 valign=top align=left style="border-collapse: collapse">
				<tr>
					<td width="12%" colspan="2"><strong>	Total  </strong></td>
					<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
						<td width="12%"><strong>Actual Commitment</strong></td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
						<td width="12%"><strong>Actual Disbursement</strong></td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
						<td width="12%"><strong>Actual Expenditure</strong></td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
						<td width="13%"><strong>Planned Commitment</strong></td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
						<td width="13%"><strong>Planned Disbursement</strong></td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
						<td width="13%"><strong>Planned Expenditure</strong></td>
					</logic:equal>
					<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
						<td width="13%"><strong>UnDisbursed</strong></td>
					</logic:equal>
				</tr>
				<%
					int pg=1;
					int qg=1;
				%>	
				<logic:iterate name="aimAdvancedReportForm"  property="fiscalYearRange" id="fiscalYearRange">
						<% int markg=0; %>
							<logic:iterate name="aimAdvancedReportForm"  property="totFund" id="totFund" type="org.digijava.module.aim.helper.AmpFund">
					
								<%
										markg++;
										if((pg==1 && qg==1) || (pg==1 && qg==2) || (pg==1 && qg==3) || (pg==1 && qg==4) || (pg==2 && qg==18) || (pg==2 && qg==19) || (pg==2 && qg==20) || (pg==2 && qg==21) || (pg==3 && qg==35) || (pg==3 && qg==36) || (pg==3 && qg==37) || (pg==3 && qg==38))
								  		{		
								%>
									<tr>
										<td width="8%"><strong>
											<%=fiscalYearRange%></strong>
										</td>
										<td width="4%"><strong>
											<% 
												int tempg = 0;
												tempg = markg%4;
												if(tempg == 0)
													tempg = 4;
											%>
											Q <%= tempg%></strong>
										</td>
										<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
											<td width="12%">
												<logic:notEqual name="totFund" property="commAmount" value="0">
													<bean:write name="totFund" property="commAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
											<td width="12%">
												<logic:notEqual name="totFund" property="disbAmount" value="0">
													<bean:write name="totFund" property="disbAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
											<td width="12%">
												<logic:notEqual name="totFund" property="expAmount" value="0">
													<bean:write name="totFund" property="expAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
											<td width="13%">
												<logic:notEqual name="totFund" property="plCommAmount" value="0">
													<bean:write name="totFund" property="plCommAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
											<td width="13%">
												<logic:notEqual name="totFund" property="plDisbAmount" value="0">
													<bean:write name="totFund" property="plDisbAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
											<td width="13%">
												<logic:notEqual name="totFund" property="plExpAmount" value="0">
													<bean:write name="totFund" property="plExpAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
											<td width="13%">
												<logic:notEqual name="totFund" property="unDisbAmount" value="0">
													<bean:write name="totFund" property="unDisbAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
									</tr>
									
								<%
									}
									if(qg == 39)
									{
								%>
									<tr>
										<td width="12%" colspan="2"><strong>
											Grand Total</strong>
										</td>
										<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
											<td width="12%">
												<logic:notEqual name="totFund" property="commAmount" value="0">
													<bean:write name="totFund" property="commAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
											<td width="12%">
												<logic:notEqual name="totFund" property="disbAmount" value="0">
													<bean:write name="totFund" property="disbAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
											<td width="12%">
												<logic:notEqual name="totFund" property="expAmount" value="0">
													<bean:write name="totFund" property="expAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
											<td width="13%">
												<logic:notEqual name="totFund" property="plCommAmount" value="0">
													<bean:write name="totFund" property="plCommAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
											<td width="13%">
												<logic:notEqual name="totFund" property="plDisbAmount" value="0">
													<bean:write name="totFund" property="plDisbAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
											<td width="13%">
												<logic:notEqual name="totFund" property="plExpAmount" value="0">
													<bean:write name="totFund" property="plExpAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
										<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
											<td width="13%">
												<logic:notEqual name="totFund" property="unDisbAmount" value="0">
													<bean:write name="totFund" property="unDisbAmount" />
												</logic:notEqual>
											</td>
										</logic:equal>
									</tr>
							
								<%
									}
									qg++;
								%>	

							</logic:iterate>
						
						<%
							pg++;
						%>

					</tr>
				</logic:iterate>
			</table>
		</td></tr>
		</logic:equal>	
	</logic:notEmpty>

	<logic:empty name="aimAdvancedReportForm" property="allReports"> 
		<tr>
			<td colspan=<bean:write name="aimAdvancedReportForm" property="totalColumns"/> align="center">
				<b>
					<digi:trn key="aim:noRecords">No Records</digi:trn>
				</b>
			</td>
		</tr>
	</logic:empty>

</table>
