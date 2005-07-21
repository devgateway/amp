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
						   <td width=120 valign=top>	<b> Project  </b> </td>
						   <td width=560>
							  <bean:write name="project" property="name"/></strong>
						   </td>
				         </tr>
			   		     <logic:iterate name="project"  property="component" id="component" type="org.digijava.module.aim.helper.AmpComponent">
  					     <tr>
 						   <td width=40 >
						   <b><bean:write name="donors" property="donorCount"/>.<bean:write name="project" property="count"/>.<bean:write name="component" property="count"/> </b>
						   </td>

						  <td width=120 valign=top><b> Component Name  </b> </td>
					       <td width=560>
						      <bean:write name="component" property="name"/>
					       </td>
					     </tr>
						<tr>
 						   <td width=40 >	    &nbsp;		   </td>
						  <td width=120 valign=top> <b>Objective</b> </td>
						  <td width=560>
							  <logic:notEmpty name="component" property="objective">
							   <bean:write name="component" property="objective" />
							  </logic:notEmpty>
						  </td>
						</tr>
						<tr>
						   <td width=40 >	    &nbsp;		   </td>
						 <td width=120 valign=top>  <b>	Status </b> </td>
						 <td align="left" width=560 >

							<logic:empty name="component" property="status"> 
							</logic:empty>
							<logic:notEmpty name="component" property="status">
							<logic:iterate name="component" id="status" property="status">* <%=status%>	
							</logic:iterate>
							</logic:notEmpty>
						 </td>
						</tr>
					<tr>
 						   <td width=40 >	    &nbsp;		   </td>
 					 <td width=120 valign=top>	<b>	Issues	</b> </td>
					 <td align="left" width=560 >
						<logic:empty name="component" property="issues"> 
						</logic:empty>
						<logic:notEmpty name="component" property="issues">
						<logic:iterate name="component" id="issues" property="issues"> <%=issues%>	

						</logic:iterate>
						</logic:notEmpty>
					 </td>
					</tr>

					<tr>
					   <td width=40 >	    &nbsp;		   </td>
					 <td width=120 valign=top>	<b>	Measures </b>	</td>
					 <td align="left" width=560 >
						
						<logic:empty name="component" property="measures"> 
						</logic:empty>
						<logic:notEmpty name="component" property="measures">
						<logic:iterate name="component" id="measures" property="measures"> <%=measures%>	
						<br>
						</logic:iterate>
						</logic:notEmpty>
						</td>
					</tr>

					<tr>
 					 <td width=40 >	    &nbsp;		   </td>
					 <td width=120 valign=top>	<b>	Resonsible Actors </b> </td>
					 <td align="left" width=560>
					   
						<logic:empty name="component" property="responsibleActor">&nbsp;
						</logic:empty>
						<logic:notEmpty name="component" property="responsibleActor">
						<logic:iterate name="component" id="responsibleActor" property="responsibleActor"> 	<%=responsibleActor%>	
						</logic:iterate>
						</logic:notEmpty>
					 </td>
					 </tr>


					<tr>
 						   <td width=40 >	    &nbsp;		   </td>
					   <td width=120  valign=top> <b> Signature Date </b>  </td>
					   <td align="left" width=560>
 							<bean:write name="component" property="signatureDate" />
					   </td>
					</tr>
					<tr>
 						   <td width=40 >	    &nbsp;		   </td>
					   <td width=120 valign=top> <b>Ending Date  </b> </td>
					   <td align="left"width=560> 
				        <bean:write name="component" property="plannedCompletionDate" />
					   </td>
					</tr>
					<tr>
 						   <td width=40 >	    &nbsp;		   </td>
  					  <td width=120 valign=top> <b> Total Allocation  </b> </td>
					  <td align="left" width=560>
					    <bean:write name="component" property="acCommitment" />
					  </td>
					</tr>
					<tr>
 						   <td width=40 >	    &nbsp;		   </td>
					  <td width=120 valign=top> <b> Cumulative Expenditure   </b></td>
					  <td align="left" width=560>
					  <bean:write name="component" property="acExpenditure" />
					</td>
					</tr>

					     </logic:iterate>
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