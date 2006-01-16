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

<table width="650" cellspacing=1 cellpadding=1 valign=top align=left>

	<% int i=1;	%>
	<% int stYr = 0, temp = 0; %> 
	<%! int count = 0; %> 

  <!-- Report Name -->
  <tr>
   <td class="head1-name" align="center">
	<digi:trn key="aim:QuarterlyReportbyProjectTitle">
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
	<table width="100%" cellspacing=1 cellpadding=1 valign=top align=left>
	  <tr><td align="left"><b>
		<digi:trn key="aim:QuarterlyReportbyProject">
		Commitment Disbursement by Project by Donor
		</digi:trn>
	  </b>    
	  </td></tr> 
	  <tr>
	   <td>
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

			<logic:iterate name="aimMulitlateralbyDonorForm"  property="fiscalYearRange" id="fiscalYearRange">
			<% 
			   if (stYr == 0) {
				Integer fy = (Integer) fiscalYearRange; 
				stYr = fy.intValue();
			} %>
			</logic:iterate>

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
			  <td width=100 align=center>
			   Year
			  </td>
 			  <td width=100 align=center>
			   &nbsp;
			  </td>
			  <td width=100 align=center>
				<digi:trn key="aim:plannedDisbursements">Planned Disbursements</digi:trn>
			  </td>
			  <td width=100 align=center>
			    <digi:trn key="aim:disbursements">Disbursements</digi:trn>
			  </td>
			  <td width=100 align=center>
			    <digi:trn key="aim:expenditures">Expenditures</digi:trn>
			  </td>
			 </tr>
			 </table>			
			</td>
		 </tr>
		
		<logic:iterate name="multiReport"  property="donors" id="donors" type="org.digijava.module.aim.helper.AmpTeamDonors">
		 <tr>
			<td width=50>
				<bean:write name="donors" property="donorCount"/>				
			</td>
			<td width=500 colspan=2>
			  <strong><bean:write name="donors" property="donorAgency" /></strong>
			</td>
		 <tr>
		<logic:iterate name="donors"  property="project" id="project" type="org.digijava.module.aim.helper.Project">
		 <tr>
			<td width=50>
 			 <bean:write name="donors" property="donorCount"/>
			 .<bean:write name="project" property="count"/>		 
		    </td>
			   <td width=500>
				<strong><bean:write name="project" property="name"/></strong>
			   </td>
			   <% temp = stYr; %>

			   <td width=570 >
		        <table cellpadding=2 cellspacing=1 border=1 style="border-collapse: collapse">
					<logic:iterate name="project"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
			
				   <tr>
					<% count++; %>
					 <%  if(count == 1)	 {	   %>
					   <td width=30 rowspan=4 valign=middle ><b>
						  <%=temp%>
						  <%temp++;%></b>
					   </td>
					 <%	 }	%>

				     <td width=50>
					  <%="Q"+i%>
					  <% i = i + 1; %>
					 </td>
				     <td align=right width=100>
						<logic:notEqual name="ampFund" property="plannedDisbAmount" value="0">
							<bean:write name="ampFund" property="plannedDisbAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=100>
						<logic:notEqual name="ampFund" property="disbAmount" value="0">
							<bean:write name="ampFund" property="disbAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=100>
						<logic:notEqual name="ampFund" property="expAmount" value="0">
							<bean:write name="ampFund" property="expAmount" />
						</logic:notEqual>
					 </td>
				   </tr>
				   <% 
					if(count == 4)
					   count = 0;
					if(i == 5)
					   i = 1;
				   %>
				   </logic:iterate>

				   <tr>
   					 <td width=50>
						&nbsp;					
					 </td>

				     <td width=50>
						<b>		Total		</b>
					 </td>
				     <td align=right width=100><b>
						<logic:notEqual name="project" property="projPlannedDisbAmount" value="0">
							<bean:write name="project" property="projPlannedDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=100><b>
						<logic:notEqual name="project" property="projDisbAmount" value="0">
							<bean:write name="project" property="projDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=100><b>
						<logic:notEqual name="project" property="projExpAmount" value="0">
							<bean:write name="project" property="projExpAmount"/>
						</logic:notEqual></b>
					 </td>
				 </table>
			   </td>
			 </tr>

			<% i = 1;%>
			<logic:notEmpty name="project"  property="termAssist">
			<logic:iterate name="project"  property="termAssist" id="termAssist" type="org.digijava.module.aim.helper.ProjectTermAssist">
   		     <tr>
			   <td width=50>	&nbsp;	   </td>
			   <td width=200> <b>
				<bean:write name="termAssist" property="termAssistName"/></b>
			   </td>
			   <td width=570>
				   <% temp = stYr; %>
			     <table cellpadding=2 cellspacing=1 border=1 style="border-collapse: collapse">
				<logic:iterate name="termAssist"  property="termAssistFund" id="termAssistFund" type="org.digijava.module.aim.helper.AmpFund">
				  <% count++; %>
				   <tr>
					 <%  if(count == 1)	 {	   %>
					   <td width=30 rowspan=4 valign=middle><b>
						  <%=temp%>
						  <%temp++;%></b>
					   </td>
					 <%	 }	%>

				     <td width=50>
					  <%="Q"+i%>
					  <% i = i + 1; %>
					 </td>
				     <td align=right width=100>
						<logic:notEqual name="termAssistFund" property="plannedDisbAmount" value="0">
							<bean:write name="termAssistFund" property="plannedDisbAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=100>
						<logic:notEqual name="termAssistFund" property="disbAmount" value="0">
							<bean:write name="termAssistFund" property="disbAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=100>
						<logic:notEqual name="termAssistFund" property="expAmount" value="0">
							<bean:write name="termAssistFund" property="expAmount" />
						</logic:notEqual>
					 </td>
				   </tr>
   				   <% 
					if(count == 4)
					   count = 0;
  					if(i == 5)
					   i = 1;
				   %>

				   </logic:iterate>
				   <tr>
   					 <td width=50>
						&nbsp;
					 </td>
				     <td width=50><b>						
						Total	</b>
					 </td>
				     <td align=right width=100><b>
						<logic:notEqual name="termAssist" property="termPlannedDisbAmount" value="0">
							<bean:write name="termAssist" property="termPlannedDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=100><b>
						<logic:notEqual name="termAssist" property="termDisbAmount" value="0">
							<bean:write name="termAssist" property="termDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=100><b>
						<logic:notEqual name="termAssist" property="termExpAmount" value="0">
							<bean:write name="termAssist" property="termExpAmount"/>
						</logic:notEqual></b>
					 </td>
				 </table>
			   </td>
			 </tr>

		</logic:iterate>
		</logic:notEmpty>
		</logic:iterate>

<!-- Start totalDonorTermAssistFund-->
			<% i = 1;%>
			<logic:notEmpty name="donors"  property="totalDonorTermAssistFund">
			<logic:iterate name="donors"  property="totalDonorTermAssistFund" id="totalDonorTermAssistFund" type="org.digijava.module.aim.helper.TermFund">
   		     <tr>
			   <td width=50>	&nbsp;	   </td>
			   <td width=200> <b>
				Total <bean:write name="totalDonorTermAssistFund" property="termAssistName" /></b>
			   </td>
			   <td width=570>
				   <% temp = stYr; %>
			     <table cellpadding=2 cellspacing=1 border=1 style="border-collapse: collapse">
				<logic:iterate name="totalDonorTermAssistFund"  property="termFundTotal" id="termFundTotal" type="org.digijava.module.aim.helper.TermFundTotal">
				  <% count++; %>
				   <tr>
					 <%  if(count == 1)	 {	   %>
					   <td width=30 rowspan=4 valign=middle><b>
						  <%=temp%>
						  <%temp++;%></b>
					   </td>
					 <%	 }	%>

				     <td width=50>
					  <%="Q"+i%>
					  <% i = i + 1; %>
					 </td>
				     <td align=right width=100>
						<logic:notEqual name="termFundTotal" property="totPlannedDisbAmount" value="0">
							<bean:write name="termFundTotal" property="totPlannedDisbAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=100>
						<logic:notEqual name="termFundTotal" property="totDisbAmount" value="0">
							<bean:write name="termFundTotal" property="totDisbAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=100>
						<logic:notEqual name="termFundTotal" property="totExpAmount" value="0">
							<bean:write name="termFundTotal" property="totExpAmount" />
						</logic:notEqual>
					 </td>
				   </tr>
   				   <% 
					if(count == 4)
					   count = 0;
  					if(i == 5)
					   i = 1;
				   %>

				   </logic:iterate>
				   <tr>
   					 <td width=50>
						&nbsp;
					 </td>
				     <td width=50><b>						
						Total	</b>
					 </td>
				     <td align=right width=100><b>
						<logic:notEqual name="totalDonorTermAssistFund" property="totDonorPlannedDisbAmount" value="0">
							<bean:write name="totalDonorTermAssistFund" property="totDonorPlannedDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=100><b>
						<logic:notEqual name="totalDonorTermAssistFund" property="totDonorDisbAmount" value="0">
							<bean:write name="totalDonorTermAssistFund" property="totDonorDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=100><b>
						<logic:notEqual name="totalDonorTermAssistFund" property="totDonorExpAmount" value="0">
							<bean:write name="totalDonorTermAssistFund" property="totDonorExpAmount"/>
						</logic:notEqual></b>
					 </td>
				 </table>
			   </td>
			 </tr>
		</logic:iterate>
		</logic:notEmpty>

<!-- End totalDonorTermAssistFund-->


<!-- End of Total Donor Fund  -->
			<% i = 1;%>
   		     <tr>
			   <td width=50>	&nbsp;	   </td>
			   <td width=200><b>
				<digi:trn key="aim:totalFor">Total for </digi:trn>
				<bean:write name="donors" property="donorAgency" /></b>
			   </td>
			   <td width=570>
			   <% temp = stYr; %>
			     <table cellpadding=2 cellspacing=1 border=1 style="border-collapse: collapse">
				<logic:iterate name="donors"  property="totalDonorFund" id="totalDonorFund" type="org.digijava.module.aim.helper.FundTotal">
				<% count++; %>
				   <tr>

					 <%  if(count == 1)	 {	   %>
					   <td width=30 rowspan=4 valign=middle><b>
						  <%=temp%>
						  <%temp++;%>	</b>
					   </td>
					 <%	 }	%>

				     <td width=50>
					  <%="Q"+i%>
					  <% i = i + 1; %>
					 </td>
				     <td align=right width=100>
						<logic:notEqual name="totalDonorFund" property="totPlannedDisbAmount" value="0">
							<bean:write name="totalDonorFund" property="totPlannedDisbAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=100>
						<logic:notEqual name="totalDonorFund" property="totDisbAmount" value="0">
							<bean:write name="totalDonorFund" property="totDisbAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=100>
						<logic:notEqual name="totalDonorFund" property="totExpAmount" value="0">
							<bean:write name="totalDonorFund" property="totExpAmount" />
						</logic:notEqual>
					 </td>
				   </tr>
   				   <% 
					if(count == 4)
					   count = 0;
					if(i == 5)
					   i = 1;
				   %>

				   </logic:iterate>
				   <tr>
   					 <td width=50>
						&nbsp;
					 </td>
				     <td width=50>
						Total
					 </td>
				     <td align=right width=100><b>
						<logic:notEqual name="donors" property="donorPlannedDisbAmount" value="0">
							<bean:write name="donors" property="donorPlannedDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=100><b>
						<logic:notEqual name="donors" property="donorDisbAmount" value="0">
							<bean:write name="donors" property="donorDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=100><b>
						<logic:notEqual name="donors" property="donorExpAmount" value="0">
							<bean:write name="donors" property="donorExpAmount"/>
						</logic:notEqual></b>
					 </td>
				 </table>
			   </td>
			 </tr>
		</logic:iterate>

<!-- End of Total Donor Fund  -->

			<% i = 1;%>
			<logic:iterate name="multiReport"  property="totalTeamTermAssistFund" id="totalTeamTermAssistFund" type="org.digijava.module.aim.helper.TermFund">
   		     <tr>
			   <td width=50>	&nbsp;	   </td>
			   <td width=200> <b>
				Total <bean:write name="totalTeamTermAssistFund" property="termAssistName" /></b>
			   </td>
			   <td width=570>
				   <% temp = stYr; %>
			     <table cellpadding=2 cellspacing=1 border=1 style="border-collapse: collapse">
				<logic:iterate name="totalTeamTermAssistFund"  property="termFundTotal" id="termFundTotal" type="org.digijava.module.aim.helper.TermFundTotal">
				  <% count++; %>
				   <tr>
					 <%  if(count == 1)	 {	   %>
					   <td width=30 rowspan=4 valign=middle><b>
						  <%=temp%>
						  <%temp++;%></b>
					   </td>
					 <%	 }	%>

				     <td width=50>
					  <%="Q"+i%>
					  <% i = i + 1; %>
					 </td>
				     <td align=right width=100>
						<logic:notEqual name="termFundTotal" property="totCommAmount" value="0">
							<bean:write name="termFundTotal" property="totCommAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=100>
						<logic:notEqual name="termFundTotal" property="totDisbAmount" value="0">
							<bean:write name="termFundTotal" property="totDisbAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=100>
						<logic:notEqual name="termFundTotal" property="totExpAmount" value="0">
							<bean:write name="termFundTotal" property="totExpAmount" />
						</logic:notEqual>
					 </td>
				   </tr>
   				   <% 
					if(count == 4)
					   count = 0;
  					if(i == 5)
					   i = 1;

				   %>

				   </logic:iterate>
				   <tr>
   					 <td width=50>
						&nbsp;
					 </td>
				     <td width=50><b>						
						Total	</b>
					 </td>
				     <td align=right width=100><b>
						<logic:notEqual name="totalTeamTermAssistFund" property="totDonorCommAmount" value="0">
							<bean:write name="totalTeamTermAssistFund" property="totDonorCommAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=100><b>
						<logic:notEqual name="totalTeamTermAssistFund" property="totDonorDisbAmount" value="0">
							<bean:write name="totalTeamTermAssistFund" property="totDonorDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=100><b>
						<logic:notEqual name="totalTeamTermAssistFund" property="totDonorExpAmount" value="0">
							<bean:write name="totalTeamTermAssistFund" property="totDonorExpAmount"/>
						</logic:notEqual></b>
					 </td>
				 </table>
			   </td>
			 </tr>
			</logic:iterate>	
<!-- End totalTeamTermAssistFund	-->

			<% i = 1;%>
   		     <tr>
			   <td width=50>	&nbsp;	   </td>
			   <td width=200><b>
				<digi:trn key="aim:totalFor">Total for </digi:trn><bean:write name="multiReport" property="teamName" /> </b>
			   </td>
			   <td width=570>
			   <% temp = stYr; %>

			     <table cellpadding=2 cellspacing=1 border=1 style="border-collapse: collapse">
				<logic:iterate name="multiReport"  property="totalTeamFund" id="totalTeamFund" type="org.digijava.module.aim.helper.FundTotal">
				<% count++; %>
				   <tr>

					 <%  if(count == 1)	 {	   %>
					   <td width=30 rowspan=4 valign=middle><b>
						  <%=temp%>
						  <%temp++;%>	</b>
					   </td>
					 <%	 }	%>

				     <td width=50>
					  <%="Q"+i%>
					  <% i = i + 1; %>
					 </td>
				     <td align=right width=100>
						<logic:notEqual name="totalTeamFund" property="totPlannedDisbAmount" value="0">
							<bean:write name="totalTeamFund" property="totPlannedDisbAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=100>
						<logic:notEqual name="totalTeamFund" property="totDisbAmount" value="0">
							<bean:write name="totalTeamFund" property="totDisbAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=100>
						<logic:notEqual name="totalTeamFund" property="totExpAmount" value="0">
							<bean:write name="totalTeamFund" property="totExpAmount" />
						</logic:notEqual>
					 </td>
				   </tr>
   				   <% 
					if(count == 4)
					   count = 0;
					if(i == 5)
					   i = 1;
				   %>

				   </logic:iterate>
				   <tr>
   					 <td width=50>
						&nbsp;
					 </td>
				     <td width=50>
						Total
					 </td>
				     <td align=right width=100><b>
						<logic:notEqual name="multiReport" property="teamPlannedDisbAmount" value="0">
							<bean:write name="multiReport" property="teamPlannedDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=100><b>
						<logic:notEqual name="multiReport" property="teamDisbAmount" value="0">
							<bean:write name="multiReport" property="teamDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=100><b>
						<logic:notEqual name="multiReport" property="teamExpAmount" value="0">
							<bean:write name="multiReport" property="teamExpAmount"/>
						</logic:notEqual></b>
					 </td>
				 </table>
			   </td>
			 </tr>

<!-- End of Total Team Fund >
<!-- Closing Tags-->
		 </logic:iterate>
		</logic:notEmpty>
		</table> 
	  </td></tr> 
	</table> 
   </td>
  </tr> 
</table>
