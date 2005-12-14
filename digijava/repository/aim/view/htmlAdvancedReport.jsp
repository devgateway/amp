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

<table width="772" border="0" cellpadding="10" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
         <td width="750" align="left" valign="top" >
		
		  	<table width="85%"  border="0" cellpadding="5" cellspacing="0">

			<tr><td colspan=3 class="head1-name" align=center>
					<bean:write name="aimAdvancedReportForm" property="reportName" />
			</td></tr>
			<tr>
				<td colspan=3 class="head2-name" align=center>
				<bean:write name="aimAdvancedReportForm" property="workspaceType" />&nbsp; 
				<bean:write name="aimAdvancedReportForm" property="workspaceName" />&nbsp; 
			</td></tr>
	<tr>
      <td>
			<table width="85%" cellspacing=1 cellpadding=1 valign=top align=left style="border-collapse: collapse">
            <tr bgcolor="#FFFFFF">
              	<td colspan="10">
				  		<table width="100%" cellspacing=1 cellpadding=1 valign=top align=left style="border-collapse: collapse">
                  	<tr>
                    		<td align="left"><strong>
									<digi:trn key="aim:ViewProjects">
										Commitments, Disbursements & Pipeline Projects
									</digi:trn></strong>
								</td>
                  	</tr>
              		</table>
					</td>
            </tr>
				<tr bgcolor="#FFFFFF">
            	<td valign="top" colspan="10">
						<!-- begin no hierarchy -->
						<logic:equal name="aimAdvancedReportForm"  property="hierarchyFlag" value="false"> 
							<jsp:include page="htmlAdvancedReportHierarchy0.jsp"/>
						</logic:equal>
						<!-- end no hierarchy -->
						
						<!-- begin with hierarchy -->
						<logic:equal name="aimAdvancedReportForm"  property="hierarchyFlag" value="true"> 
							<jsp:include page="htmlAdvancedReportHierarchy1.jsp"/>	
						</logic:equal>
						<!-- end with hierarchy -->
					</td>
				</tr>
				<tr bgcolor="#F4F4F2">
         </table>
      </td>
   </tr>
	</td>
	</tr>
</table>
