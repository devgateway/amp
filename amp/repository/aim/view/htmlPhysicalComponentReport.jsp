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

<table width="600" cellspacing=1 cellpadding=1 valign=top align=left>

  <!-- Report Name -->
  <tr>
   <td class="head1-name" align="center">
	 <digi:trn key="aim:physicalComponentReport">
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
	<table width="100%" cellspacing=1 cellpadding=1 valign=top align=left style="border-collapse: collapse">
	  <tr><td align="left"><b>
		<digi:trn key="aim:MultilateralbyDonor">
			Multilateral Cooperation Department Commitment Disbursement
		</digi:trn></b>    
	  </td></tr> 
	  <tr><td>
	    <table cellspacing=0 cellpadding=0 valign=top align=left border=1
		style="border-collapse: collapse">
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
		    <td width=100% class="head2-name">
			  <bean:write name="multiReport" property="teamName"/>
			</td>
		  </tr>
		  <logic:iterate name="multiReport"  property="donors" id="donors" type="org.digijava.module.aim.helper.AmpTeamDonors">
		   <tr bgcolor="white">
		     <td>
				<table>	
				<tr>
				   <td>	<b><bean:write name="donors" property="donorCount"/>	</b>   </td>
				   <td width=600 class="head2-name">
					<strong><bean:write name="donors" property="donorAgency"/></strong>
				   </td>
				 </tr>
 		   	     <table cellspacing=2 cellpadding=0 valign=top align=left border=1 style="border-collapse: collapse" width=600>
		 		<logic:iterate name="donors"  property="project" id="project" type="org.digijava.module.aim.helper.Project">
				   <tr>
				     <td>
					   <table >
					     <tr>
						   <td width=40 > <b>
							   <bean:write name="donors" property="donorCount"/>.<bean:write name="project" property="count"/>	</b>
						   </td>
						   <td width=120 valign=top>	<b> Program/Project Component    </b> </td>
		 					 <td> <b> :  </b> </td>						   
						   <td width=560>
							  <bean:write name="project" property="name"/></strong>
						   </td>
				         </tr>
						<tr>
 						   <td width=40 >	    &nbsp;		   </td>
						  <td width=120 valign=top> <b>Description  </b> </td>
	 					 <td> <b> : </b> </td>						  
						  	<td width=560>
								<logic:empty name="project" property="description">&nbsp;</logic:empty>	
								<logic:notEmpty name="project" property="description">
									<bean:define id="describeKey">
										<bean:write name="project" property="description"/>
									</bean:define>
									<digi:edit key="<%=describeKey%>"/>
								</logic:notEmpty>  
							</td>
						</tr>

					<tr>
 						   <td width=40 >	    &nbsp;		   </td>
					   <td width=120  valign=top> <b> Commitment Date  </b>  </td>
 					 <td> <b> : </b> </td>					   
					   <td align="left" width=560>
 							<bean:write name="project" property="signatureDate" />
					   </td>
					</tr>
					<tr>
 						   <td width=40 >	    &nbsp;		   </td>
					   <td width=120 valign=top> <b>Completion Date  </b> </td>
	 					 <td> <b> : </b> </td>					   
					   <td align="left"width=560> 
				        <bean:write name="project" property="plannedCompletionDate" />
					   </td>
					</tr>
					<tr>
 						   <td width=40 >	    &nbsp;		   </td>
  					  <td width=120 valign=top> <b> Total Commitments  </b> </td>
 					 <td> <b> : </b> </td>  					  
					  <td align="left" width=560>
					    <bean:write name="project" property="acCommitment" />
					  </td>
					</tr>
					<tr>
 						   <td width=40 >	    &nbsp;		   </td>
					  <td width=120 valign=top> <b> Cumulative Disbursements  </b></td>
	 					 <td> <b> : </b> </td>					  
					  <td align="left" width=560>
					  <bean:write name="project" property="acDisbursement" />
					</td>
					</tr>
					<tr>
 						   <td width=40 >	    &nbsp;		   </td>
					  <td width=120 valign=top> <b> Remaining Balance   </b></td>
 					 <td> <b> : </b> </td>					  
					  <td align="left" width=560>
					  <bean:write name="project" property="acUnDisbursement" />
					</td>
					</tr>
						<tr>
						   <td width=40 >	    &nbsp;		   </td>
						 <td width=120 valign=top>  <b>Status </b> </td>
	 					 <td> <b> : </b> </td>						 
						 <td align="left" width=560 ><b>
						 	<bean:write name="project" property="status"/> - </b>
							<logic:empty name="project" property="progress"> 
							</logic:empty>
							<logic:notEmpty name="project" property="progress"> 
							<logic:iterate name="project" id="progress" property="progress">* <%=progress%>	
							</logic:iterate>
							</logic:notEmpty>
						 </td>
						</tr>
						<tr>
						   <td width=40 >	    &nbsp;		   </td>
						 <td width=120 valign=top>  <b>	Measures to Be Taken </b> </td>
	  					 <td> <b> : </b> </td>
						 <td align="left" width=560 >

							<logic:empty name="project" property="measures"> 
							</logic:empty>
							<logic:notEmpty name="project" property="measures">
							<logic:iterate name="project" id="measures" property="measures">* <%=measures%>	
							</logic:iterate>
							</logic:notEmpty>
						 </td>
						</tr>
					<tr>
 						   <td width=40 >	    &nbsp;		   </td>
 					 <td width=120 valign=top>	<b>	Issues </b> </td>
 					 <td> <b> : </b> </td>
					 <td align="left" width=560 >
						<logic:empty name="project" property="issues"> 
						</logic:empty>
						<logic:notEmpty name="project" property="issues">
						<logic:iterate name="project" id="issues" property="issues"> <%=issues%>	

						</logic:iterate>
						</logic:notEmpty>
					 </td>
					</tr>

					<tr>
					   <td width=40 >	    &nbsp;		   </td>
					 <td width=120 valign=top>	<b>	Responsible Actor </b>	</td>
 					 <td> <b> : </b> </td>
					 <td align="left" width=560 >
						
						<logic:empty name="project" property="responsibleActor"> 
						</logic:empty>
						<logic:notEmpty name="project" property="responsibleActor">
						<logic:iterate name="project" id="responsibleActor" property="responsibleActor"> <%=responsibleActor%>	
						<br>
						</logic:iterate>
						</logic:notEmpty>
						</td>
					</tr>

					</table>
					 </td>
					</tr>
				</logic:iterate>
			  </table>
			   </tr>
			 </td>
		   </tr>

		</logic:iterate>
		
		</logic:iterate>
		</logic:notEmpty>
		</table> 
	  </td></tr> 
	</table> 
   </td>
  </tr> 
</table>
