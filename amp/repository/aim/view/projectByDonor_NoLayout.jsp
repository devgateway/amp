<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script language="JavaScript">
	function load() {
		window.print();
	}

	function unload() {}

</script>



<digi:instance property="aimMulitlateralbyDonorForm" />

<table width="650" cellspacing="1" cellpadding="1" valign="top" align=left>

  <!-- Report Name -->
  <tr>
   <td class="head1-name" align="center">
	<digi:trn key="aim:ProjectbyDonorTitle">
		<bean:write name="aimMulitlateralbyDonorForm" property="reportName" />
	</digi:trn>
   </td>
  </tr> 


  <!-- Table name-->
  <tr>
   <td class="head2-name" align="center">
	 <bean:write name="aimMulitlateralbyDonorForm" property="workspaceType" />&nbsp; <bean:write name="aimMulitlateralbyDonorForm" property="workspaceName" />
   </td>
  </tr> 

  <!-- Report data -->
  <tr>
   <td>
	<table width="100%" cellspacing="1" cellpadding="1" valign="top" align=left >
	  <tr><td align="left"><b>
		<digi:trn key="aim:ProjectbyDonor">Commitment Disbursement by Project by Donor</digi:trn>
		</b>    
	  </td></tr> 
	  <tr><td>

	  <!--  Displays the Who -->

	    <table cellspacing="0" cellpadding="0" valign="top" align=left border="1"
		style="border-collapse: collapse">
		 <tr>
		   <td width=30>
			<a title="<digi:trn key="aim:SerialNumber">Serial Number</digi:trn>">
				<b><digi:trn key="aim:SerialNo">S.No  </digi:trn></b>
			</a>
		   </td>
		   <td width=150>
			<a title="<digi:trn key="aim:DonorName">The country or agency that financed the project</digi:trn>">
			<b>
			<digi:trn key="aim:donor">Donor</digi:trn></b>
			</a>
		   </td>
		   <td width=540>
			&nbsp;
		   </td>
		 </tr>
		<logic:empty name="aimMulitlateralbyDonorForm" property="multiReport"> 
		<tr>
			<td bgcolor="#ffffff" align="center" colspan="3">
			<b>
			<digi:trn key="aim:noRecords">No Records</digi:trn>
			</b>
			</td>
		</tr>
		</logic:empty>

		<logic:notEmpty name="aimMulitlateralbyDonorForm"  property="multiReport">
		<logic:iterate name="aimMulitlateralbyDonorForm"  property="multiReport" id="multiReport" type="org.digijava.module.aim.helper.multiReport">
		  <td bgcolor="#ffffff" align="center" colspan="3" class="head2-name">
			<bean:write name="multiReport" property="teamName"/>
		  </tr>
 			<tr>
			   <td width=30>
					&nbsp;
			   </td>
			   <td width=150>
				&nbsp;
			   </td>
			   <td width=540>
			     <table cellpadding=2 cellspacing="1" border="1" style="border-collapse: collapse">
				   <tr>
				     <td width=51 align="center">
					    Year
					 </td>
					 <td width=78 align="center">
						 <digi:trn key="aim:commitments">Commitment</digi:trn>
					  </td>

				     <td width=78 align="center">

						<digi:trn key="aim:plannedDisbursements">Planned Disbursements</digi:trn>
					 </td>
				     <td width=75 align="center">
						<digi:trn key="aim:disbursements">Disbursements</digi:trn>
					 </td>
				     <td width=76 align="center">
						<digi:trn key="aim:expenditures">Expenditures</digi:trn>
					 </td>
					 <td width=77 align="center">
					 		<digi:trn key="aim:undisbursed">Undisbursed</digi:trn>
					</td>
				   </tr>
				 </table>			
			   </td>
			</tr>

			<% int stYr = 0; %> 
			<% int temp = 0; %> 
			<logic:iterate name="aimMulitlateralbyDonorForm"  property="fiscalYearRange" id="fiscalYearRange">
			<% 
			   if (stYr == 0) {
				Integer fy = (Integer) fiscalYearRange; 
				stYr = fy.intValue();
			} %>
			</logic:iterate>

			<logic:iterate name="multiReport"  property="donors" id="donors" type="org.digijava.module.aim.helper.AmpTeamDonors">
			 <tr>
			   <td width=30>
				<bean:write name="donors" property="donorCount"/>				
			   </td>
			   <td width=300 height=20 colspan=2>
				<strong><bean:write name="donors" property="donorAgency" /></strong>
			   </td>
			 </tr>

			 <logic:iterate name="donors"  property="project" id="project" type="org.digijava.module.aim.helper.Project">
			 <tr>
				<td width=30>
					<bean:write name="donors" property="donorCount"/>.<bean:write name="project" property="count"/>
				</td>
				<td width=540>
					<strong> <bean:write name="project" property="name"/>	</strong>
				</td>
				<td>
				 <% temp = stYr; %>

				  <table cellpadding=2 cellspacing="1" border="1" style="border-collapse: collapse">
	  				<logic:iterate name="project"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
					  <tr>
  						<td width=30>
						  <%=temp%>
						  <%temp++;%>
					    </td>

						 <td align=right width=80>
 						  <logic:notEqual name="ampFund" property="commAmount"  value="0" >
						    <bean:write name="ampFund" property="commAmount" />
						  </logic:notEqual>
   						  <logic:equal name="ampFund" property="commAmount"  value="0" >
							&nbsp;
						  </logic:equal>

					    </td>
						 <td align=right width=80>
 						  <logic:notEqual name="ampFund" property="plannedDisbAmount"  value="0" >
						    <bean:write name="ampFund" property="plannedDisbAmount" />
						  </logic:notEqual>
   						  <logic:equal name="ampFund" property="plannedDisbAmount"  value="0" >
							&nbsp;
						  </logic:equal>

					    </td>

						 <td align=right width=80>
							<logic:notEqual name="ampFund" property="disbAmount"  value="0" >
							<bean:write name="ampFund" property="disbAmount" />
							</logic:notEqual>
							<logic:equal name="ampFund" property="disbAmount"  value="0" >
							&nbsp;
							</logic:equal>

						</td>
						 <td align=right width=80>
							<logic:notEqual name="ampFund" property="expAmount"  value="0" >
							<bean:write name="ampFund" property="expAmount" />
							</logic:notEqual>
							<logic:equal name="ampFund" property="expAmount"  value="0" >
							&nbsp;
							</logic:equal>
						</td>
						<td align=right width=80>
							&nbsp;
						</td>
					  </tr> 
					</logic:iterate>
				     <td width=30>
					  <b>
						Total	</b>
					 </td>
					 <td align=right width=80>
					  <logic:notEqual name="project" property="projCommAmount"  value="0" >
					  <bean:write name="project" property="projCommAmount"/>
					  </logic:notEqual>

					  <logic:equal name="project" property="projCommAmount"  value="0" >
					  &nbsp;
					  </logic:equal>
					</td>
					 <td align=right width=80>
					  <logic:notEqual name="project" property="projPlannedDisbAmount"  value="0" >
					  <bean:write name="project" property="projPlannedDisbAmount"/>
					  </logic:notEqual>

					  <logic:equal name="project" property="projPlannedDisbAmount"  value="0" >
					  &nbsp;
					  </logic:equal>
					</td>

					 <td align=right width=80>
						<logic:notEqual name="project" property="projDisbAmount"  value="0" >
						<bean:write name="project" property="projDisbAmount"/>
						</logic:notEqual>

						<logic:equal name="project" property="projDisbAmount"  value="0" >
						&nbsp;
						</logic:equal>
					</td>
					 <td align=right width=80>
						<logic:notEqual name="project" property="projExpAmount"  value="0" >
						<bean:write name="project" property="projExpAmount"/>
						</logic:notEqual>

						<logic:equal name="project" property="projExpAmount"  value="0" >
						&nbsp;
						</logic:equal>
					</td>
					 <td align=right width=80>
						<logic:notEqual name="project" property="projUnDisbAmount"  value="0" >
						<bean:write name="project" property="projUnDisbAmount"/>
						</logic:notEqual>

						<logic:equal name="project" property="projUnDisbAmount"  value="0" >
						&nbsp;
						</logic:equal>
					</td>
			  </table>
				</td>
			 </tr>


<!--  Propject Term Assist -->
			<logic:notEmpty name="project"  property="termAssist">
			<logic:iterate name="project"  property="termAssist" id="termAssist" type="org.digijava.module.aim.helper.ProjectTermAssist">
			<tr bgcolor="white">
				<td align=right width=30>
					&nbsp;
				</td>
				<td align=left width=540>
					<bean:write name="termAssist" property="termAssistName"/>
				</td>
				<td>
				   <%  temp = stYr; %>

				  <table cellpadding=2 cellspacing="1" border="1" style="border-collapse: collapse">
					<logic:iterate name="termAssist"  property="termAssistFund" id="termAssistFund" type="org.digijava.module.aim.helper.AmpFund">
					<tr>
				     <td width=30>
					   <%=temp%>
					   <%temp++;%>
					 </td>
						<td align=right width=80>
							<logic:notEqual name="termAssistFund" property="commAmount"  value="0" >
							<bean:write name="termAssistFund" property="commAmount" />
							</logic:notEqual>
							<logic:equal name="termAssistFund" property="commAmount"  value="0" >
							&nbsp;
							</logic:equal>
						</td>
						<td align=right width=80>
							<logic:notEqual name="termAssistFund" property="plannedDisbAmount"  value="0" >
							<bean:write name="termAssistFund" property="plannedDisbAmount" />
							</logic:notEqual>
							<logic:equal name="termAssistFund" property="plannedDisbAmount"  value="0" >
							&nbsp;
							</logic:equal>
						</td>

						<td align=right width=80>
							<logic:notEqual name="termAssistFund" property="disbAmount"  value="0" >
							<bean:write name="termAssistFund" property="disbAmount" />
							</logic:notEqual>
							
							<logic:equal name="termAssistFund" property="disbAmount"  value="0" >
							&nbsp;
							</logic:equal>
						</td>
						<td align=right width=80>
							<logic:notEqual name="termAssistFund" property="expAmount"  value="0" >
							<bean:write name="termAssistFund" property="expAmount" />
							</logic:notEqual>

							<logic:equal name="termAssistFund" property="expAmount"  value="0" >
							&nbsp;
							</logic:equal>
						</td>
						<td align=right width=80>
							&nbsp;
						</td>
					</tr>
					</logic:iterate>
				     <td width=30>	<b>
						Total	</b>
					 </td>

					<td align=right width=80> 
						<logic:notEqual name="termAssist" property="termCommAmount"  value="0" >
						<bean:write name="termAssist" property="termCommAmount"/>
						</logic:notEqual>

						<logic:equal name="termAssist" property="termCommAmount"  value="0" >
						&nbsp;
						</logic:equal>
					</td>
					<td align=right width=80> 
						<logic:notEqual name="termAssist" property="termPlannedDisbAmount"  value="0" >
						<bean:write name="termAssist" property="termPlannedDisbAmount"/>
						</logic:notEqual>

						<logic:equal name="termAssist" property="termPlannedDisbAmount"  value="0" >
						&nbsp;
						</logic:equal>
					</td>
					<td align=right width=80>
						<logic:notEqual name="termAssist" property="termDisbAmount"  value="0" >
						<bean:write name="termAssist" property="termDisbAmount"/>
						</logic:notEqual>
						
						<logic:equal name="termAssist" property="termDisbAmount"  value="0" >
						&nbsp;
						</logic:equal>
					</td>
					<td align=right width=80>
						<logic:notEqual name="termAssist" property="termExpAmount"  value="0" >
						<bean:write name="termAssist" property="termExpAmount"/>
						</logic:notEqual>

						<logic:equal name="termAssist" property="termExpAmount"  value="0" >
						&nbsp;
						</logic:equal>
					</td>
					<td align=right width=80>
						<logic:notEqual name="termAssist" property="termUnDisbAmount"  value="0" >
						<bean:write name="termAssist" property="termUnDisbAmount"/>
						</logic:notEqual>

						<logic:equal name="termAssist" property="termUnDisbAmount"  value="0" >
						&nbsp;
						</logic:equal>
					</td>
				</table>
				</td>
			</tr>
			</logic:iterate>
			</logic:notEmpty>
			</logic:iterate>

<!--  End Propject Term Assist -->

		<logic:notEmpty name="donors"  property="totalDonorTermAssistFund">
		<logic:iterate name="donors"  property="totalDonorTermAssistFund" id="totalDonorTermAssistFund" type="org.digijava.module.aim.helper.TermFund">
			<tr bgcolor="white">
				<td align=right width=30>
					&nbsp;
				</td>
				<td align=left width=540> <b>
					Total <bean:write name="totalDonorTermAssistFund" property="termAssistName" /> </b>
				</td>
				<td>
				   <%  temp = stYr; %>

				  <table cellpadding=2 cellspacing="1" border="1" style="border-collapse: collapse">
					<logic:iterate name="totalDonorTermAssistFund"  property="termFundTotal" id="termFundTotal" type="org.digijava.module.aim.helper.TermFundTotal">
					<tr>
				     <td width=30>
					   <%=temp%>
					   <%temp++;%>
					 </td>
						<td align=right width=80><b>
						  <logic:notEqual name="termFundTotal" property="totCommAmount"  value="0" >
						   <bean:write name="termFundTotal" property="totCommAmount" />
						  </logic:notEqual>

						  <logic:equal name="termFundTotal" property="totCommAmount"  value="0" >
						   	&nbsp;
						  </logic:equal></b>
						</td>
						<td align=right width=80><b>
						  <logic:notEqual name="termFundTotal" property="totPlannedDisbAmount"  value="0" >
						   <bean:write name="termFundTotal" property="totPlannedDisbAmount" />
						  </logic:notEqual>

						  <logic:equal name="termFundTotal" property="totPlannedDisbAmount"  value="0" >
						   	&nbsp;
						  </logic:equal></b>
						</td>

						<td align=right width=80><b>
							 <logic:notEqual name="termFundTotal" property="totDisbAmount"  value="0" >
							 <bean:write name="termFundTotal" property="totDisbAmount" />
							 </logic:notEqual>
							
							 <logic:equal name="termFundTotal" property="totDisbAmount"  value="0" >
							  	&nbsp;
							 </logic:equal></b>
						</td>
						<td align=right width=80><b>
						  <logic:notEqual name="termFundTotal" property="totExpAmount"  value="0" >
						  <bean:write name="termFundTotal" property="totExpAmount" />
						  </logic:notEqual>

						  <logic:equal name="termFundTotal" property="totExpAmount"  value="0" >
								&nbsp;
						  </logic:equal></b>
						</td>
						<td align=right width=80>
							&nbsp;
						</td>
					</tr>
					</logic:iterate>
				     <td width=30><strong>
						Total			</strong>
					 </td>

					<td align=right width=80> <b>
					 <logic:notEqual name="totalDonorTermAssistFund" property="totDonorCommAmount"  value="0" >
					  <bean:write name="totalDonorTermAssistFund" property="totDonorCommAmount"/>
					 </logic:notEqual>
					 <logic:equal name="totalDonorTermAssistFund" property="totDonorCommAmount"  value="0" >
						&nbsp;
					 </logic:equal></b>
					</td>
					<td align=right width=80> <b>
					 <logic:notEqual name="totalDonorTermAssistFund" property="totDonorPlannedDisbAmount"  value="0" >
					  <bean:write name="totalDonorTermAssistFund" property="totDonorPlannedDisbAmount"/>
					 </logic:notEqual>
					 <logic:equal name="totalDonorTermAssistFund" property="totDonorPlannedDisbAmount"  value="0" >
						&nbsp;
					 </logic:equal></b>
					</td>

					<td align=right width=80><b>
					 <logic:notEqual name="totalDonorTermAssistFund" property="totDonorDisbAmount"  value="0" >
					  <bean:write name="totalDonorTermAssistFund" property="totDonorDisbAmount"/>
					 </logic:notEqual>
					 <logic:equal name="totalDonorTermAssistFund" property="totDonorDisbAmount"  value="0" >
						&nbsp;
					 </logic:equal></b>
					</td>
					<td align=right width=80><b>
					 <logic:notEqual name="totalDonorTermAssistFund" property="totDonorExpAmount"  value="0" >	
					  <bean:write name="totalDonorTermAssistFund" property="totDonorExpAmount"/>
					 </logic:notEqual>
					 <logic:equal name="totalDonorTermAssistFund" property="totDonorExpAmount"  value="0" >	
						&nbsp;
					 </logic:equal></b>
					</td>
					<td align=right width=80><b>
					 <logic:notEqual name="totalDonorTermAssistFund" property="totDonorUnDisbAmount"  value="0" >	
					  <bean:write name="totalDonorTermAssistFund" property="totDonorUnDisbAmount"/>
					 </logic:notEqual>
					 <logic:equal name="totalDonorTermAssistFund" property="totDonorUnDisbAmount"  value="0" >	
						&nbsp;
					 </logic:equal></b>
					</td>

				</table>
				</td>
			</tr>
			</logic:iterate>
			</logic:notEmpty>

<!--   end Total Donor assist fund-->

			<tr bgcolor="white">
				<td align=right width=30>
					&nbsp;
				</td>
				<td align=left width=540> <b>
					Total for <bean:write name="donors" property="donorAgency" />	</b>
				</td>
				<td>
				   <%  temp = stYr; %>

				  <table cellpadding=2 cellspacing="1" border="1" style="border-collapse: collapse">
					<logic:iterate name="donors"  property="totalDonorFund" id="totalDonorFund" type="org.digijava.module.aim.helper.FundTotal">
					<tr>
				     <td width=30>
					   <%=temp%>
					   <%temp++;%>
					 </td>
						<td align=right width=80><b>
							<logic:notEqual name="totalDonorFund" property="totCommAmount"  value="0" >	
							<br><bean:write name="totalDonorFund" property="totCommAmount" />
							</logic:notEqual>
							<logic:equal name="totalDonorFund" property="totCommAmount"  value="0" >	
							&nbsp;
							</logic:equal></b>
						</td>
						<td align=right width=80><b>
						 <logic:notEqual name="totalDonorFund" property="totPlannedDisbAmount"  value="0" >	
						<bean:write name="totalDonorFund" property="totPlannedDisbAmount" />
							</logic:notEqual>
							<logic:equal name="totalDonorFund" property="totPlannedDisbAmount"  value="0" >	
							&nbsp;
							</logic:equal></b>
						</td>

						<td align=right width=80><b>
								<logic:notEqual name="totalDonorFund" property="totDisbAmount"  value="0" >	
								<br><bean:write name="totalDonorFund" property="totDisbAmount" />
								</logic:notEqual>
								<logic:equal name="totalDonorFund" property="totDisbAmount"  value="0" >	
								&nbsp;
								</logic:equal></b>
						</td>
						<td align=right width=80><b>
							<logic:notEqual name="totalDonorFund" property="totExpAmount"  value="0" >	
							<br><bean:write name="totalDonorFund" property="totExpAmount" />
							</logic:notEqual>
							<logic:equal name="totalDonorFund" property="totExpAmount"  value="0" >	
							&nbsp;
							</logic:equal></b>
						</td>
						<td width=80>
						&nbsp;
						</td>
					</tr>
					</logic:iterate>
				     <td width=30><strong>
						Total			</strong>
					 </td>

					<td align=right width=80> <b>
						<logic:notEqual name="donors" property="donorCommAmount"  value="0" >	
						<bean:write name="donors" property="donorCommAmount"/></td>
						</logic:notEqual>
						<logic:equal name="donors" property="donorCommAmount"  value="0" >	
						&nbsp;
						</logic:equal>	</b>
					</td>
					<td align=right width=80><b>
						<logic:notEqual name="donors" property="donorPlannedDisbAmount"  value="0" >	
						<bean:write name="donors" property="donorPlannedDisbAmount"/>
						</logic:notEqual>
						<logic:equal name="donors" property="donorPlannedDisbAmount"  value="0" >	
						&nbsp;
						</logic:equal></b>
					</td>
					<td align=right width=80><b>
						<logic:notEqual name="donors" property="donorDisbAmount"  value="0" >	
						<bean:write name="donors" property="donorDisbAmount"/>
						</logic:notEqual>
						<logic:equal name="donors" property="donorDisbAmount"  value="0" >	
						&nbsp;
						</logic:equal></b>
					</td>
					<td align=right width=80><b>
						<logic:notEqual name="donors" property="donorExpAmount"  value="0" >	
						<bean:write name="donors" property="donorExpAmount"/>
						</logic:notEqual>
						<logic:equal name="donors" property="donorExpAmount"  value="0" >	
						&nbsp;
						</logic:equal></b>
					</td>
					<td align=right width=80><b>
						<logic:notEqual name="donors" property="donorUnDisbAmount"  value="0" >	
						<bean:write name="donors" property="donorUnDisbAmount"/>
						</logic:notEqual>
						<logic:equal name="donors" property="donorUnDisbAmount"  value="0" >	
						&nbsp;
						</logic:equal></b>
					</td>
				</table>
				</td>
			</tr>
		</logic:iterate>

<!-- End of Total donor fund -->
			 
		<logic:notEmpty name="multiReport"  property="totalTeamTermAssistFund">
		<logic:iterate name="multiReport"  property="totalTeamTermAssistFund" id="totalTeamTermAssistFund" type="org.digijava.module.aim.helper.TermFund">
				<tr bgcolor="white">
				<td align=right width=30>
					&nbsp;
				</td>
				<td align=left width=540> <b>
					Total <bean:write name="totalTeamTermAssistFund" property="termAssistName" />	</b>
				</td>
				<td>
				   <%  temp = stYr; %>

				  <table cellpadding=2 cellspacing="1" border="1" style="border-collapse: collapse">
					<logic:iterate name="totalTeamTermAssistFund"  property="termFundTotal" id="termFundTotal" type="org.digijava.module.aim.helper.TermFundTotal">
					<tr>
				     <td width=30>
					   <%=temp%>
					   <%temp++;%>
					 </td>
						<td align=right width=80><b>
							<logic:notEqual name="termFundTotal" property="totCommAmount"  value="0" >	
							<bean:write name="termFundTotal" property="totCommAmount" />
							</logic:notEqual>
							<logic:notEqual name="termFundTotal" property="totCommAmount"  value="0" >	
							&nbsp;
							</logic:notEqual></b>
						</td>
						<td align=right width=80><b>
							<logic:notEqual name="termFundTotal" property="totPlannedDisbAmount"  value="0" >
							<bean:write name="termFundTotal" property="totPlannedDisbAmount" />
							</logic:notEqual>
							<logic:notEqual name="termFundTotal" property="totPlannedDisbAmount"  value="0" >
							&nbsp;
							</logic:notEqual></b>
						</td>

						<td align=right width=80><b>
							<logic:notEqual name="termFundTotal" property="totDisbAmount"  value="0" >
							<bean:write name="termFundTotal" property="totDisbAmount" />
							</logic:notEqual>
							<logic:notEqual name="termFundTotal" property="totDisbAmount"  value="0" >
							&nbsp;
							</logic:notEqual></b>
						</td>
						<td align=right width=80>
							<b>
							<logic:notEqual name="termFundTotal" property="totExpAmount"  value="0" >			
							<bean:write name="termFundTotal" property="totExpAmount" />
							</logic:notEqual>

							<logic:notEqual name="termFundTotal" property="totExpAmount"  value="0" >			&nbsp;	
							</logic:notEqual></b>
						</td>
						<td width=80>
							&nbsp;
						</td>
					</tr>
					</logic:iterate>
				     <td width=30><strong>
						Total			</strong>
					 </td>

					<td align=right width=80> <b>
 					 <logic:notEqual name="totalTeamTermAssistFund" property="totDonorCommAmount"  value="0" >
					 <bean:write name="totalTeamTermAssistFund" property="totDonorCommAmount"/>
					 </logic:notEqual>
					 <logic:notEqual name="totalTeamTermAssistFund" property="totDonorCommAmount"  value="0" >
					  &nbsp;
					 </logic:notEqual></b>
					</td>
					<td align=right width=80><b>
					 <logic:notEqual name="totalTeamTermAssistFund" property="totDonorPlannedDisbAmount"  value="0" >
					  <bean:write name="totalTeamTermAssistFund" property="totDonorPlannedDisbAmount"/>
					 </logic:notEqual>
					 <logic:equal name="totalDonorTermAssistFund" property="totDonorPlannedDisbAmount"  value="0" >
						&nbsp;
					 </logic:equal></b>
					</td>

					<td align=right width=80><b>
					 <logic:notEqual name="totalTeamTermAssistFund" property="totDonorDisbAmount"  value="0" >
					  <bean:write name="totalTeamTermAssistFund" property="totDonorDisbAmount"/>
					 </logic:notEqual>
					 <logic:equal name="totalDonorTermAssistFund" property="totDonorDisbAmount"  value="0" >
						&nbsp;
					 </logic:equal></b>
					</td>
					<td align=right width=80><b>
					 <logic:notEqual name="totalTeamTermAssistFund" property="totDonorExpAmount"  value="0" >	
					  <bean:write name="totalTeamTermAssistFund" property="totDonorExpAmount"/>
					 </logic:notEqual>
					 <logic:equal name="totalTeamTermAssistFund" property="totDonorExpAmount"  value="0" >	
						&nbsp;
					 </logic:equal></b>
					</td>
					<td align=right width=80><b>
					 <logic:notEqual name="totalTeamTermAssistFund" property="totDonorUnDisbAmount"  value="0" >	
					  <bean:write name="totalTeamTermAssistFund" property="totDonorUnDisbAmount"/>
					 </logic:notEqual>
					 <logic:equal name="totalTeamTermAssistFund" property="totDonorUnDisbAmount"  value="0" >	
						&nbsp;
					 </logic:equal></b>
					</td>
				</table>
				</td>
			</tr>
			</logic:iterate>
			</logic:notEmpty>

<!--  End of Total Team term Assist fund-->

			<tr bgcolor="white">
				<td align=right width=30>
					&nbsp;
				</td>
				<td align=left width=540> <b>
				  <digi:trn key="aim:totalFor">Total for </digi:trn>
				  <bean:write name="multiReport" property="teamName" />	</b>
				</td>
				<td>
				   <%  temp = stYr; %>

				  <table cellpadding=2 cellspacing="1" border="1" style="border-collapse: collapse">
					<logic:iterate name="multiReport"  property="totalTeamFund" id="totalTeamFund" type="org.digijava.module.aim.helper.FundTotal">
					<tr>
				     <td width=30>
					   <%=temp%>
					   <%temp++;%>
					 </td>
						<td align=right width=80> <b>
							<logic:notEqual name="totalTeamFund" property="totCommAmount"  value="0" >	
							<bean:write name="totalTeamFund" property="totCommAmount" />
							</logic:notEqual></b><b>
							<logic:equal name="totalTeamFund" property="totCommAmount"  value="0" >	
							&nbsp;
							</logic:equal></b>
						</td>
						<td align=right width=80><b>
								<logic:notEqual name="totalTeamFund" property="totDisbAmount"  value="0" >	
								<bean:write name="totalTeamFund" property="totDisbAmount" />
								</logic:notEqual>
								<logic:equal name="totalTeamFund" property="totDisbAmount"  value="0" >	
								&nbsp;
								</logic:equal></b>
						</td>

						<td align=right width=80><b>
								<logic:notEqual name="totalTeamFund" property="totPlannedDisbAmount"  value="0" >	
								<bean:write name="totalTeamFund" property="totPlannedDisbAmount" />
								</logic:notEqual>
								<logic:equal name="totalTeamFund" property="totPlannedDisbAmount"  value="0" >	
								&nbsp;
								</logic:equal></b>
						</td>
						<td align=right width=80><b>
							<logic:notEqual name="totalTeamFund" property="totExpAmount"  value="0" >	
							<bean:write name="totalTeamFund" property="totExpAmount" />
							</logic:notEqual>
							<logic:equal name="totalTeamFund" property="totExpAmount"  value="0" >	
							&nbsp;
							</logic:equal></b>
						</td>
						<td width=80>
							&nbsp;
						</td>
					</tr>
					</logic:iterate>
				     <td width=30><strong>
						Total			</strong>
					 </td>

					<td align=right width=80><b>
						<logic:notEqual name="multiReport" property="teamCommAmount"  value="0" >	
						<bean:write name="multiReport" property="teamCommAmount"/></td>
						</logic:notEqual>
						<logic:equal name="multiReport" property="teamCommAmount"  value="0" >	
						&nbsp;
						</logic:equal></b>
					</td>
					<td align=right width=80><b>
						<logic:notEqual name="multiReport" property="teamPlannedDisbAmount"  value="0" >	
						<bean:write name="multiReport" property="teamPlannedDisbAmount"/>
						</logic:notEqual>
						<logic:equal name="multiReport" property="teamPlannedDisbAmount"  value="0" >	
						&nbsp;
						</logic:equal></b>
					</td>

					<td align=right width=80><b>
						<logic:notEqual name="multiReport" property="teamDisbAmount"  value="0" >	
						<bean:write name="multiReport" property="teamDisbAmount"/>
						</logic:notEqual>
						<logic:equal name="multiReport" property="teamDisbAmount"  value="0" >	
						&nbsp;
						</logic:equal></b>
					</td>
					<td align=right width=80><b>
						<logic:notEqual name="multiReport" property="teamExpAmount"  value="0" >	
						<bean:write name="multiReport" property="teamExpAmount"/>
						</logic:notEqual>
						<logic:equal name="multiReport" property="teamExpAmount"  value="0" >	
						&nbsp;
						</logic:equal></b>
					</td>
					<td align=right width=80><b>
						<logic:notEqual name="multiReport" property="teamUnDisbAmount"  value="0" >	
						<bean:write name="multiReport" property="teamUnDisbAmount"/>
						</logic:notEqual>
						<logic:equal name="multiReport" property="teamUnDisbAmount"  value="0" >	
						&nbsp;
						</logic:equal></b>
					</td>
				</table>
				</td>
			</tr>

<!--End of Total Team fund -->
		</logic:iterate>
		</logic:notEmpty>
		</table> 
	  </td></tr> 
	</table> 
   </td>
  </tr> 
</table>
