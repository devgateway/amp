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
	<digi:trn key="aim:QuarterlyReportbySectorTitle">
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
		<digi:trn key="aim:QuarterlyReportbySector">
		Commitment / Disbursement / Actual Exp by Sector / Donor / Project
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
		   <td bgcolor="#ffffff" align="left" colspan="3" class="head2-name">
			<digi:trn key="aim:sector">SECTOR : </digi:trn> <b><u><bean:write name="multiReport" property="sector" />
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
			  <td width=90 align=center>
			   Year
			  </td>
 			  <td width=140 align=center>
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
			<td width=50> <b>
				<bean:write name="donors" property="donorCount"/>	</b>			
			</td>
			<td width=500 colspan=2>
			  <strong><bean:write name="donors" property="donorAgency" /></strong>
			</td>
		 <tr>
		<logic:iterate name="donors"  property="project" id="project" type="org.digijava.module.aim.helper.Project">
		 <tr>
			<td width=50><b>
 			 <bean:write name="donors" property="donorCount"/>
			 .<bean:write name="project" property="count"/>		 </b>
		    </td>
			   <td width=500>
				<strong><bean:write name="project" property="name"/></strong>
			   </td>
			   <% temp = stYr; %>

			   <td width=570 >
		        <table cellpadding=2 cellspacing=1 border=1 style="border-collapse: collapse">
					<logic:iterate name="project"  property="ampFund" id="ampFund" type="org.digijava.module.aim.helper.AmpFund">
			
				   <tr>
<!--					<% count++; %>
					 <%  if(count == 1)	 {	   %>
-->
					   <td width=30 rowspan=4 valign=middle ><b>
						  <%=temp%>
						  <%temp++;%></b>
					   </td>
<!--					 <%	 }	%>
-->

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
		</logic:iterate>

<!-- Start totalDonorTermAssistFund-->
			<% i = 1;%>
			<logic:notEmpty name="multiReport"  property="totalSectorTermAssistFund">
			<logic:iterate name="multiReport"  property="totalSectorTermAssistFund" id="totalSectorTermAssistFund" >
   		     <tr>
			   <td width=50>	&nbsp;	   </td>
			   <td width=200> <b>
				Total <bean:write name="totalSectorTermAssistFund" property="termAssistName" /> </b>
			   </td>
			   <td width=570>
				   <% temp = stYr; %>
			     <table cellpadding=2 cellspacing=1 border=1 style="border-collapse: collapse">
				<logic:iterate name="totalSectorTermAssistFund"  property="termFundTotal" id="termFundTotal" type="org.digijava.module.aim.helper.TermFundTotal">
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
						<logic:notEqual name="totalSectorTermAssistFund" property="totDonorPlannedDisbAmount" value="0">
							<bean:write name="totalSectorTermAssistFund" property="totDonorPlannedDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=100><b>
						<logic:notEqual name="totalSectorTermAssistFund" property="totDonorDisbAmount" value="0">
							<bean:write name="totalSectorTermAssistFund" property="totDonorDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=100><b>
						<logic:notEqual name="totalSectorTermAssistFund" property="totDonorExpAmount" value="0">
							<bean:write name="totalSectorTermAssistFund" property="totDonorExpAmount"/>
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
				<bean:write name="multiReport" property="sector" /></b>
			   </td>
			   <td width=570>
			   <% temp = stYr; %>
			     <table cellpadding=2 cellspacing=1 border=1 style="border-collapse: collapse">
				<logic:iterate name="multiReport"  property="totalSectorFund" id="totalSectorFund" type="org.digijava.module.aim.helper.FundTotal">
				<% count++; %>
				   <tr>

					 <%  if(count == 1)	 {	   %>
					   <td width=30 rowspan=4 valign=middle><b>
						  <%=temp%>
						  <%temp++;%>	</b>
					   </td>
					 <%	 }	%>

				     <td width=55>
					  <%="Q"+i%>
					  <% i = i + 1; %>
					 </td>
				     <td align=right width=100>
						<logic:notEqual name="totalSectorFund" property="totPlannedDisbAmount" value="0">
							<bean:write name="totalSectorFund" property="totPlannedDisbAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=100>
						<logic:notEqual name="totalSectorFund" property="totDisbAmount" value="0">
							<bean:write name="totalSectorFund" property="totDisbAmount" />
						</logic:notEqual>
					 </td>
				     <td align=right width=100>
						<logic:notEqual name="totalSectorFund" property="totExpAmount" value="0">
							<bean:write name="totalSectorFund" property="totExpAmount" />
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
						<logic:notEqual name="multiReport" property="sectorPlannedDisbAmount" value="0">
							<bean:write name="multiReport" property="sectorPlannedDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=100><b>
						<logic:notEqual name="multiReport" property="sectorDisbAmount" value="0">
							<bean:write name="multiReport" property="sectorDisbAmount"/>
						</logic:notEqual></b>
					 </td>
				     <td align=right width=100><b>
						<logic:notEqual name="multiReport" property="sectorExpAmount" value="0">
							<bean:write name="multiReport" property="sectorExpAmount"/>
						</logic:notEqual></b>
					 </td>
				 </table>
			   </td>
			 </tr>

<!-- End of Total Donor Fund  -->


<!-- Start totalTeamTermAssistFund-->


			<% i = 1;%>
			<logic:notEmpty name="multiReport"  property="totalTeamTermAssistFund">
			<logic:iterate name="multiReport"  property="totalTeamTermAssistFund" id="totalTeamTermAssistFund" type="org.digijava.module.aim.helper.TermFund">	     <tr>
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

				     <td width=60>
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
						<logic:notEqual name="totalTeamTermAssistFund" property="totDonorPlannedDisbAmount" value="0">
							<bean:write name="totalTeamTermAssistFund" property="totDonorPlannedDisbAmount"/>
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
		</logic:notEmpty>

<!-- End totalDonorTermAssistFund-->

<!-- Start of Grand Total -->

			<% i = 1;%>

			<logic:notEmpty name="multiReport"  property="totalTeamFund">
   		     <tr>
			   <td width=50>	&nbsp;	   </td>
			   <td width=200><b>
				<digi:trn key="aim:grandTotal">Grand Total</digi:trn></b>
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

				     <td width=60>
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
		</logic:notEmpty>

<!-- End of Grand Total -->


<!-- Closing Tags-->

		 </logic:iterate>
		</logic:notEmpty>
		</table> 
	  </td></tr> 
	</table> 
   </td>
  </tr> 
</table>	
