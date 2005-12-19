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

<table width="760" cellspacing=1 cellpadding=1 valign=top align=left>

  <!-- Report Name -->
  <tr>
   <td class="head1-name" align="center">
	 <digi:trn key="aim:MultilateralbyDonorTitle">
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
	<table width="85%" cellspacing=1 cellpadding=1 valign=top align=left>
	  <tr><td align="left"><b>
		<digi:trn key="aim:MultilateralbyDonor">
			Multilateral Cooperation Department Commitment Disbursement
		</digi:trn></b>    
	  </td></tr> 
	  <tr><td>
	    <table cellspacing=0 cellpadding=0 valign=top align=left border=1
		style="border-collapse: collapse">
		 <tr>
		   <td width=50>
			<a title="<digi:trn key="aim:SerialNumber">Serial Number</digi:trn>">
				<b><digi:trn key="aim:SerialNo">S.No  </digi:trn></b>
			</a>
		   </td>
		   <td width=200>
			<a title="<digi:trn key="aim:DonorName">The country or agency that financed the project</digi:trn>">
			<b>
			<digi:trn key="aim:donor">Donor</digi:trn></b>
			</a>
		   </td>
		   <td width=570>
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
			<tr>
				<td bgcolor="#ffffff" align="center" colspan="3" class="head2-name">
					<bean:write name="multiReport" property="teamName"/>
				</td>
			</tr>
			<tr>
			   <td width=50>
					&nbsp;
			   </td>
			   <td width=200>
				&nbsp;
			   </td>
			   <td width=570>
			     <table cellpadding=2 cellspacing=1 border=1 style="border-collapse: collapse">
				   <tr>
				    <td width=51 align=center>
					    Year
					 </td>
				     <td width=78 align=center>
						<digi:trn key="aim:commitments">Commitment</digi:trn>
					 </td>
				     <td width=78 align=center>
						<digi:trn key="aim:plannedDisbursements">Planned Disbursements</digi:trn>
					 </td>
				     <td width=75 align=center>
						<digi:trn key="aim:disbursements">Disbursements</digi:trn>
					 </td>
				     <td width=76 align=center>
						<digi:trn key="aim:expenditures">Expenditures</digi:trn>
					 </td>
				     <td width=77 align=center>
						<digi:trn key="aim:unDisbursedhere">UnDisbursed</digi:trn>
					 </td>
				   </tr>
				 </table>			
			   </td>
			</tr>

			<%! int stYr = 0; %> 
			<%! int temp = 0; %> 
			<logic:iterate name="aimMulitlateralbyDonorForm"  property="fiscalYearRange" id="fiscalYearRange">
			<% 
			   if (stYr == 0) {
				Integer fy = (Integer) fiscalYearRange; 
				stYr = fy.intValue();
			} %>
			</logic:iterate>

			<logic:iterate name="multiReport"  property="donors" id="donors" type="org.digijava.module.aim.helper.AmpTeamDonors">

			 <tr>
			   <td width=50>
				<bean:write name="multiReport" property="count"/>.
				<bean:write name="donors" property="donorCount"/>				
			   </td>
			   <td width=200>
				<strong><bean:write name="donors" property="donorAgency" /></strong>
			   </td>
			   <td width=570>
				 <% temp = stYr; %>
			     <table cellpadding=2 cellspacing=1 border=1 style="border-collapse: collapse">
 				   <logic:iterate name="donors"  property="totalDonorFund" id="totalDonorFund" type="org.digijava.module.aim.helper.FundTotal">
				   <tr>
				     <td width=50>
						<%=temp%>
						<%temp++;%>
					 </td>
				     <td align=right width=80>
						<logic:notEqual name="totalDonorFund" property="totCommAmount" value="0">
							<bean:write name="totalDonorFund" property="totCommAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=80>
						<logic:notEqual name="totalDonorFund" property="totPlannedDisbAmount" value="0">
							<bean:write name="totalDonorFund" property="totPlannedDisbAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=80>
						<logic:notEqual name="totalDonorFund" property="totDisbAmount" value="0">
							<bean:write name="totalDonorFund" property="totDisbAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=80>
						<logic:notEqual name="totalDonorFund" property="totExpAmount" value="0">
							<bean:write name="totalDonorFund" property="totExpAmount" />
						</logic:notEqual>
					 </td>
				     <td width=80>
						&nbsp;
					 </td>
				   </tr>
				   </logic:iterate>
				   <tr>
				     <td width=50>
						Total
					 </td>
				     <td align=right width=80><b>
						<bean:write name="donors" property="donorCommAmount"/></b>
					 </td>
				     <td align=right width=80><b>
						<logic:notEqual name="donors" property="donorPlannedDisbAmount" value="0">
							<bean:write name="donors" property="donorPlannedDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=80><b>
						<logic:notEqual name="donors" property="donorDisbAmount" value="0">
							<bean:write name="donors" property="donorDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=80><b>
						<logic:notEqual name="donors" property="donorExpAmount" value="0">
							<bean:write name="donors" property="donorExpAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=80><b>
						<logic:notEqual name="donors" property="donorUnDisbAmount" value="0">
							<bean:write name="donors" property="donorUnDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				 </table>
			   </td>
			 </tr>
			
			 <!-- Terms of Assistance -->
			 <logic:iterate name="donors"  property="totalDonorTermAssistFund" id="totalDonorTermAssistFund" type="org.digijava.module.aim.helper.TermFund">
			 <tr>
			   <td width=50>
				&nbsp;
			   </td>
			   <td width=200 align="left">
				<bean:write name="totalDonorTermAssistFund" property="termAssistName"/>
			   </td>
			   <td width=570>
			   <%  temp = stYr; %>
			     <table cellpadding=2 cellspacing=1 border=1 style="border-collapse: collapse">
				   <logic:iterate name="totalDonorTermAssistFund"  property="termFundTotal" id="termFundTotal" type="org.digijava.module.aim.helper.TermFundTotal">
				   <tr>
				     <td width=50>
						<%=temp%>
						<%temp++;%>
					 </td>
				     <td align=right width=80>
						<logic:notEqual name="termFundTotal" property="totCommAmount" value="0">
							<bean:write name="termFundTotal" property="totCommAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=80>
						<logic:notEqual name="termFundTotal" property="totPlannedDisbAmount" value="0">
							<bean:write name="termFundTotal" property="totPlannedDisbAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=80>
						<logic:notEqual name="termFundTotal" property="totDisbAmount" value="0">
							<bean:write name="termFundTotal" property="totDisbAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=80>
						<logic:notEqual name="termFundTotal" property="totExpAmount" value="0">
							<bean:write name="termFundTotal" property="totExpAmount" />
						</logic:notEqual>
					 </td>
				     <td width=80>
						&nbsp;
					 </td>
				   </tr>
				   </logic:iterate>
				   <tr>
				     <td width=50>
					    Total
					 </td>
				     <td align=right width=80><b>
						<logic:notEqual name="totalDonorTermAssistFund" property="totDonorCommAmount" value="0">
							<bean:write name="totalDonorTermAssistFund" property="totDonorCommAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=80><b>
						<logic:notEqual name="totalDonorTermAssistFund" property="totDonorPlannedDisbAmount" 	value="0">
							<bean:write name="totalDonorTermAssistFund" property="totDonorPlannedDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=80><b>
						<logic:notEqual name="totalDonorTermAssistFund" property="totDonorDisbAmount" value="0">
							<bean:write name="totalDonorTermAssistFund" property="totDonorDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=80><b>
						<logic:notEqual name="totalDonorTermAssistFund" property="totDonorExpAmount" value="0">
							<bean:write name="totalDonorTermAssistFund" property="totDonorExpAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=80><b>
						<logic:notEqual name="totalDonorTermAssistFund" property="totDonorUnDisbAmount" value="0">
							<bean:write name="totalDonorTermAssistFund" property="totDonorUnDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				 </table>
			   </td>
			 </tr>
			 </logic:iterate>	
			</logic:iterate>
			</logic:iterate>

			 <!-- Total of Terms of Assistance -->
			<logic:iterate name="multiReport"  property="totalTeamTermAssistFund" id="totalTeamTermAssistFund" type="org.digijava.module.aim.helper.TermFund">
			 <tr>
			   <td width=50>
				&nbsp;
			   </td>
			   <td width=200><b>
				Total <bean:write name="totalTeamTermAssistFund" property="termAssistName" /></b>
			   </td>
			   <td width=570>
				   <%  temp = stYr; %>	
			     <table cellpadding=2 cellspacing=1 border=1 style="border-collapse: collapse">
				   <logic:iterate name="totalTeamTermAssistFund"  property="termFundTotal" id="termFundTotal" type="org.digijava.module.aim.helper.TermFundTotal">
				   <tr>
				     <td width=50>
						<%=temp%>
						<%temp++;%>
					 </td>
				     <td align=right width=80>
						<logic:notEqual name="termFundTotal" property="totCommAmount" value="0">
							<bean:write name="termFundTotal" property="totCommAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=80>
						<logic:notEqual name="termFundTotal" property="totPlannedDisbAmount" value="0">
							<bean:write name="termFundTotal" property="totPlannedDisbAmount" />
						</logic:notEqual></b>
					 </td>
				     <td align=right width=80>
						<logic:notEqual name="termFundTotal" property="totDisbAmount" value="0">
							<bean:write name="termFundTotal" property="totDisbAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=80>
						<logic:notEqual name="termFundTotal" property="totExpAmount" value="0">
							<bean:write name="termFundTotal" property="totExpAmount" />
						</logic:notEqual>
					 </td>
				     <td width=80>
						&nbsp;
					 </td>
				   </tr>
				   </logic:iterate>
				   <tr>
				     <td width=50>
						Total
					 </td>
				     <td align=right width=80><b>
						<logic:notEqual name="totalTeamTermAssistFund" property="totDonorCommAmount" value="0">
							<bean:write name="totalTeamTermAssistFund" property="totDonorCommAmount"/>
						</logic:notEqual></b>	
					 </td>
				     <td align=right width=80><b>
						<logic:notEqual name="totalTeamTermAssistFund" property="totDonorPlannedDisbAmount" value="0">
							<bean:write name="totalTeamTermAssistFund" property="totDonorPlannedDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=80><b>
						<logic:notEqual name="totalTeamTermAssistFund" property="totDonorDisbAmount" value="0">
							<bean:write name="totalTeamTermAssistFund" property="totDonorDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=80><b>
						<logic:notEqual name="totalTeamTermAssistFund" property="totDonorExpAmount" value="0">
							<bean:write name="totalTeamTermAssistFund" property="totDonorExpAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=80><b>
						<logic:notEqual name="totalTeamTermAssistFund" property="totDonorUnDisbAmount" value="0">
							<bean:write name="totalTeamTermAssistFund" property="totDonorUnDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				 </table>
			   </td>
			 </tr>
			 </logic:iterate>
			<!-- end of total terms assist -->

			<!-- Total donor funds -->
			 <logic:iterate name="donors"  property="totalDonorTermAssistFund" id="totalDonorTermAssistFund" type="org.digijava.module.aim.helper.TermFund">
			 <tr>
			   <td width=50>
				&nbsp;
			   </td>
			   <td width=200><b>
				Total for <bean:write name="multiReport" property="teamName" /></b>
			   </td>
			   <td width=570>
			   <%  temp = stYr; %>
			     <table cellpadding=2 cellspacing=1 border=1 style="border-collapse: collapse">
		    	   <logic:iterate name="multiReport"  property="totalTeamFund" id="totalTeamFund" type="org.digijava.module.aim.helper.FundTotal">
				   <tr>
				     <td width=50>
						<%=temp%>
						<%temp++;%>
					 </td>
				     <td align=right width=80>
						<logic:notEqual name="totalTeamFund" property="totCommAmount" value="0">
							<bean:write name="totalTeamFund" property="totCommAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=80>
						<logic:notEqual name="totalTeamFund" property="totPlannedDisbAmount" value="0">
							<bean:write name="totalTeamFund" property="totPlannedDisbAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=80>
						<logic:notEqual name="totalTeamFund" property="totDisbAmount" value="0">
							<bean:write name="totalTeamFund" property="totDisbAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=80>
						<logic:notEqual name="totalTeamFund" property="totExpAmount" value="0">
							<bean:write name="totalTeamFund" property="totExpAmount" />
						</logic:notEqual>
					 </td>
				     <td width=80>
						&nbsp;
					 </td>
				   </tr>
				   </logic:iterate>
				   <tr>
				     <td width=50>
					    Total
					 </td>
				     <td align=right width=80><b>
						<logic:notEqual name="multiReport" property="teamCommAmount" value="0">
							<bean:write name="multiReport" property="teamCommAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=80><b>
						<logic:notEqual name="multiReport" property="teamPlannedDisbAmount" value="0">
							<bean:write name="multiReport" property="teamPlannedDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=80><b>
						<logic:notEqual name="multiReport" property="teamDisbAmount" value="0">
							<bean:write name="multiReport" property="teamDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=80><b>
						<logic:notEqual name="multiReport" property="teamExpAmount" value="0">
							<bean:write name="multiReport" property="teamExpAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=80><b>
						<logic:notEqual name="multiReport" property="teamUnDisbAmount" value="0">
							<bean:write name="multiReport" property="teamUnDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				 </table>
			   </td>
			 </tr>
			 </logic:iterate>
			<!-- end of total donor funds -->

		
		</logic:notEmpty>
		</table> 
	  </td></tr> 
	</table> 
   </td>
  </tr> 
</table>
