<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<digi:context name="digiContext" property="context" />

<digi:errors/>
<digi:instance property="aimPhysicalProgressForm" />
<table width="100%">
	<tr>
 <td>
  <table border="0" width="30%" cellspacing="5" cellpadding="0">
    <tr bgcolor="aqua">
        <td width="25%" align="center"><b><font size="1">Edit</font></b></td>
        <td width="25%" align="center"><b><font size="1">Save</font></b></td>
        <td width="25%" align="center"><b><font size="1">Print</font></b></td>
        <td width="25%" align="center"><b><font size="1">Cancel</font></b></td>
        <td width="25%" align="center"><b><font size="1">Cancel</font></b></td>
    </tr>
  </table>
 </td>
</tr>


	<tr>
		<td><h3><bean:write name="aimPhysicalProgressForm" property="name" /></h3>	</td>
	</tr>
	<tr>
		<td> <bean:write name="aimPhysicalProgressForm" property="description" /><logic:equal name="aimPhysicalProgressForm" property="flag" value="1"> <a href="<%= digiContext %>/aim/viewChannelOverviewDescription.do?ampActivityId=<bean:write name="aimPhysicalProgressForm" 
		property="ampActivityId"/>">more...</a></logic:equal></td>
	</tr>
	<tr> <td> &nbsp; </td> </tr>
	<tr>
		<td width="100%">
			<table width="100%">
				<tr bgcolor="aqua" border="1">
					<td align="center" >
						 <b>
						<a href="<%= digiContext %>/aim/viewChannelOverview.do?ampActivityId=<bean:write name="aimPhysicalProgressForm" 
		property="ampActivityId"/>"><digi:trn key="aim:channelOverview">
						Channel Overview</digi:trn>	
						</a>
						</b>
					</td>
					<td align="center"> 
						<b>
						<a href="<%= digiContext %>/aim/viewFinancingBreakdown.do?ampActivityId=<bean:write name="aimPhysicalProgressForm" 
		property="ampActivityId"/>"><digi:trn key="aim:financialProgress"> 
						Financial Progress</digi:trn> 
						</a>
						</b>
					</td>
					<td align="center" bgcolor="white">
						<b>
						<a href="<%= digiContext %>/aim/viewPhysicalProgress.do?ampActivityId=<bean:write name="aimPhysicalProgressForm" 
		property="ampActivityId"/>"> <digi:trn key="aim:physicalProgress">
						 Physical Progress </digi:trn>
						</a>
						</b> 
					</td>
					<td align="center">
						<b> 
						<a href="<%= digiContext %>/aim/viewKnowledge.do?ampActivityId=<bean:write name="aimPhysicalProgressForm" 
		property="ampActivityId"/>"><digi:trn key="aim:knowledge"> 
						Knowledge</digi:trn>
						</a>	
						</b>
					</td>
	
				</tr>
				<tr>
					<td colspan="4"> &nbsp; </td>
				</tr>
				
				
				<tr> 
					<td colspan="4">
						<table border="0" width=100%>
							<tr bgcolor="aqua">
								<td align="center" ><digi:trn key="aim:physicalProgressDetails"> Physical Progress Details </digi:trn></td>
							</tr>
							
							
							<tr> <td><br> <table border="0" width=80% align=center>
							<tr><td width=50%><b><digi:trn key="aim:title">Title</digi:trn></b></td><td width=40%><b><digi:trn key="aim:date">Date</digi:trn></b></td><td width=10%></td></tr>
							<logic:iterate name="aimPhysicalProgressForm"  property="physicalProgress" id="physicalProgress" type="org.digijava.module.aim.helper.PhysicalProgress">
			 				<tr>
			 				<td>
			 					<a href="<%= digiContext %>/aim/viewPhysicalProgressDescription.do?pid=<bean:write name="physicalProgress" property="pid"/>"><bean:write name="physicalProgress" property="title"/></a>
			 				</td>
			  					<td>
			 <bean:write name="physicalProgress" property="reportingDate"/></td>
			 <td><a href="<%= digiContext %>/aim/getPhysicalProgress.do?pid=<bean:write name="physicalProgress" property="pid"/>">Edit</a></td>
			 </tr></logic:iterate></table>

							
							
							 </td> </tr>	  
							 
						</table>
					</td>	
				</tr>		
			</table>
		</td>
	</tr>
</table>


































