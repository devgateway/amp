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


<digi:instance property="aimCommitmentbyDonorForm" />

<table width="700" cellspacing=1 cellpadding=1 valign=top align=left >

	<%! int stYr = 0, ind = 0; %> 
	<%! int temp = 0, count = 0; %> 
  <!-- Report Name -->
  <tr>
   <td class="head1-name" align=center>
	<digi:trn key="aim:ViewProjectsTitle">
	<bean:write name="aimCommitmentbyDonorForm" property="reportName" />
	</digi:trn>
   </td>
  </tr> 


  <!-- Table name-->
  <tr>
   <td class="head2-name" align=center>
	 <bean:write name="aimCommitmentbyDonorForm" property="workspaceType" />&nbsp; <bean:write name="aimCommitmentbyDonorForm" property="workspaceName" />
   </td>
  </tr> 

  <!-- Report data -->
  <tr>
	<td>
		<table width="100%" cellspacing=1 cellpadding=1 valign=top align=left style="border-collapse: collapse">
			<tr><td align="left"><b>
				<digi:trn key="aim:ViewProjects">
				Commitments, Disbursements, & Pipeline Projects
				</digi:trn>
				</b>    
			</td></tr> 

			<logic:iterate name="aimCommitmentbyDonorForm"  property="forecastYear" id="forecastYear">
			<% count++;%>
			<% 
			   if (stYr == 0) {
				Integer fy = (Integer) forecastYear; 
				stYr = fy.intValue();
			} %>
			</logic:iterate>
			<logic:notEmpty name="aimCommitmentbyDonorForm" property="allReports"> 
			<logic:iterate name="aimCommitmentbyDonorForm"  property="allReports" id="allReports" type="org.digijava.module.aim.helper.Report">
			 <tr>
 			  <td width=650>
			  <table width="100%" cellspacing=1 cellpadding=1 valign=top align=left style="border-collapse: collapse" border=1 >

				<tr valign=top>
				 <td width=650 colspan=2>
				  <table>
				   <tr>
				    <td align=left colspan=2>  <b> Donor(s) :</b> 
						<logic:empty name="allReports" property="donors">&nbsp;
						</logic:empty>
						<logic:notEmpty name="allReports" property="donors">
						<logic:iterate name="allReports" id="donors" property="donors"> <%=donors%>	
						</logic:iterate>
						</logic:notEmpty>
					</td>
				   </tr>
	   				<tr >
				     <td align=left colspan=2>  <b> Project Name  : </b>
					  <bean:write name="allReports" property="title" />
					 </td>
					</tr>
				  </table>
				 </td>
				
				</tr>
				<tr>
				 <td width=100% valign=top  >
				  <table style="border-collapse: collapse" border=1 width="100%" cellspacing=1 cellpadding=1 border=1 valign=top >
					<tr>
					  <td width=150><b>
						Sector :</b>
							<logic:empty name="allReports" property="sectors">&nbsp;</logic:empty>
							<logic:notEmpty name="allReports" property="sectors">
							<logic:iterate name="allReports" id="sectors" property="sectors"> <%=sectors%>	
							</logic:iterate>
							</logic:notEmpty>
					  </td>
					  <td width=130 valign=top > <b>
						Status : </b><bean:write name="allReports" property="status" />
					  </td>
					</tr>
					<tr>
					   <td width=150><b>
							Type of Assistance : </b>
							<logic:empty name="allReports" property="assistance">&nbsp;	</logic:empty>
							<logic:notEmpty name="allReports" property="assistance">
							<logic:iterate name="allReports" id="assistance" property="assistance"> <%=assistance%>	
							</logic:iterate>
							</logic:notEmpty>
					   </td>
					   <td width=130 valign=top >
					     <b>	Level : </b>
						 <bean:write name="allReports" property="level" />
					   </td>
					</tr>
					<tr>
					  <td width=150> <b>
						Start Date : </b> <bean:write name="allReports" property="startDate" />  </td>
					  <td width=130 valign=top ><b>
						Close Date : </b> <bean:write name="allReports" property="closeDate" />					  </td>
					</tr>

					<tr>
					  <td width=150><b>
						Region :</b>
						<logic:empty name="allReports" property="regions">&nbsp;</logic:empty>
						<logic:notEmpty name="allReports" property="regions">
						<logic:iterate name="allReports" id="regions" property="regions"> <%=regions%>	
						</logic:iterate>
						</logic:notEmpty>
					  </td>
					  <td width=130 valign=top > <b>
						Total Commitment : </b>
						<logic:notEqual name="allReports" property="acCommitment" value="0">
						<bean:write name="allReports" property="acCommitment" />
						</logic:notEqual>
					  </td>
					</tr>


					<tr>
					  <td width=150> <b>
						Total Disb. : </b> 
						<logic:notEqual name="allReports" property="acDisbursement" value="0">
						<bean:write name="allReports" property="acDisbursement" />
						</logic:notEqual>
					  </td>
					  <td width=130 valign=top ><b>
						UnDisb : </b> 						
						<logic:notEqual name="allReports" property="acUnDisbursement" value="0">
						<bean:write name="allReports" property="acUnDisbursement" />
						</logic:notEqual>
					  </td>
					</tr>
				  </table>
				 </td>

				 <td width=300 valign=top >
				  <table>
				   <tr>
					<td align=left align=top >
					 <table cellpadding=2 cellspacing=1 border=1 style="border-collapse: collapse">
					   <tr>
						 <td width=55 align=center>
							&nbsp;Year&nbsp;&nbsp;
						 </td>
						 <td width=100 align=center>
						<digi:trn key="aim:plannedDisbursements">Planned Disbursed </digi:trn>
						 </td>
					   </tr>
					 </table>			
				   </td>
				  </tr>	

				  <tr>
				  <td valign=top  width=200>
				  <% temp = stYr; %>
			     <table cellpadding=2 cellspacing=1 border=1 style="border-collapse: collapse" valign=top >
			 		<logic:iterate name="allReports"  property="ampFund" id="ampFund" 	type="org.digijava.module.aim.helper.AmpFund">
					<tr>
	 					 <td width=40>
						<%=temp%>
						<%temp++;%>
					 </td>
					  <td align=right width=130>
						<logic:notEqual name="ampFund" property="disbAmount" value="0">
						<bean:write name="ampFund" property="disbAmount" />
						</logic:notEqual>
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
			 </td>
			</tr>
			</logic:iterate>

			<tr>
			 <td >
				<table width="650" cellspacing=1 cellpadding=1 valign=top align=left style="border-collapse: collapse" border=1 >
				 <tr>
				 	<td width=80>
					 <b>	Total		</b>
				    </td>
					<td align=right width=710>
					 <table>
					  <tr>
						<td width=150 align=left>
							<b>	Total Commitment	</b>
						</td>

						<td align="right" width=100>
							<logic:notEqual name="aimCommitmentbyDonorForm" property="totComm" value="0">
							<bean:write name="aimCommitmentbyDonorForm" property="totComm" />
							</logic:notEqual>
						</td>
					  </tr>
					  <tr>
						<td width=150 align=left>
							<b>Total Disbursment	</b>
						</td>
						<td align="right" width=100>
							<logic:notEqual name="aimCommitmentbyDonorForm" property="totDisb" value="0">
							<bean:write name="aimCommitmentbyDonorForm" property="totDisb" />
							</logic:notEqual>
						</td>
					  </tr>
					  <tr>
						<td width=150 align=left>
						<b>	Total UnDisbursed	</b>
						</td>
						<td align="right" width=100>
							<logic:notEqual name="aimCommitmentbyDonorForm" property="totUnDisb" value="0">
							<bean:write name="aimCommitmentbyDonorForm" property="totUnDisb" />
							</logic:notEqual>
						</td>
				      </tr>
					 </table>
					</td>
					<td width=130>
					  <% temp = stYr; %>
					 <table cellpadding=2 cellspacing=1 border=1 style="border-collapse: collapse" valign=top >
						<logic:iterate name="aimCommitmentbyDonorForm"  property="totDisbFund" id="totDisbFund" type="org.digijava.module.aim.helper.AmpFund">
						<tr>
						<% ind++; %>
						<% if(ind > count ) {  ind=0;%>
						 <td width=50>
							Total 
						</td>
						<% } else { %>
							 <td width=40>
							<%=temp%>
							<%temp++;%>
						 </td>

						<%} %>
						  <td align=right width=100>
						<logic:notEqual name="totDisbFund" property="disbAmount" value="0">
						<bean:write name="totDisbFund" property="disbAmount" />
						</logic:notEqual>
						 </td>
						</tr>
						</logic:iterate>
					  </table>
					</td>
				   </tr>
				 </table>
				</td>
			  </tr>

		   </logic:notEmpty>
		</table> 
	</td>
  </tr> 
  </table>
