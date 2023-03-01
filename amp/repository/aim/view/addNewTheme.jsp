<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"/>"></script>

<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/container/assets/container.css">

<jsp:include page="scripts/newCalendar.jsp" flush="true" />


<style>
body {font-family:Arial, Helvetica, sans-serif; font-size:12px;}
.buttonx {background-color:#5E8AD1; border-top: 1px solid #99BAF1; border-left:1px solid #99BAF1; border-right:1px solid #225099; border-bottom:1px solid #225099; font-size:11px; color:#FFFFFF; font-weight:bold; padding-left:5px; padding-right:5px; padding-top:3px; padding-bottom:3px;}
hr {border: 0; color: #E5E5E5; background-color: #E5E5E5; height: 1px; width: 100%; text-align: left;}
</style>
<c:set var="msgValidNumbers">
    <digi:trn key="aim:msgEnterNumericValues">Please enter numeric values for field </digi:trn>
</c:set>

<c:set var="programInernalFinancing">
	<digi:trn key="aim:Internal"> Internal financing </digi:trn>
</c:set>

<c:set var="programExternalFinancing">
	<digi:trn key="aim:External"> External financing </digi:trn> 
</c:set>

<c:set var="programTotalFinancing">
	<digi:trn key="aim:TotasFinance"> Total financing  </digi:trn> 
</c:set>

<c:set var="msgEnterPgrName">
<digi:trn key="aim:sgEnterPgrName">Please enter the program name </digi:trn>
</c:set>

<c:set var="msgEnterPgrType">
    <digi:trn key="aim:msgEnterPgrType">Please Select a  program type</digi:trn>
</c:set>

<c:set var="msgEnterCode">
    <digi:trn key="aim:msgEnterPgrCode">Please enter the program code</digi:trn>
</c:set>


<digi:instance property="aimThemeForm" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
	<!--

	
	
	function validate() 
	{
		var validNumbers='0123456789,.'
		if (trim(document.aimThemeForm.programName.value).length == 0) 
		{
			alert("${msgEnterPgrName}" );
			document.aimThemeForm.programName.focus();
			return false;
		}	
		if (trim(document.aimThemeForm.programCode.value).length == 0) 
		{
			alert("${msgEnterCode}");
			document.aimThemeForm.programCode.focus();
			return false;
		}		
		if (document.aimThemeForm.programTypeCategValId.value == 0) 
		{
			alert("${msgEnterPgrType}");
			document.aimThemeForm.programTypeCategValId.focus();
			return false;
		}			

	<logic:empty name="aimThemeForm" property="parentId"> 
		document.aimThemeForm.programInernalFinancing.value=trim(document.aimThemeForm.programInernalFinancing.value);
		if (document.aimThemeForm.programInernalFinancing.value.length > 0){
				var text=document.aimThemeForm.programInernalFinancing.value;
				for (i=0; i < text.length; i++){
					if (validNumbers.indexOf(text.charAt(i)) ==-1){
					alert("${msgValidNumbers} ${programInernalFinancing}");
					document.aimThemeForm.programInernalFinancing.focus();
					return false;
					}
				}			
			}else{
			document.aimThemeForm.programInernalFinancing.value=0;
		} 

		document.aimThemeForm.programExternalFinancing.value=trim(document.aimThemeForm.programExternalFinancing.value);
		if (document.aimThemeForm.programExternalFinancing.value.length > 0){
			var text=document.aimThemeForm.programExternalFinancing.value;
			for (i=0; i < text.length; i++){
				if (validNumbers.indexOf(text.charAt(i)) ==-1){
				alert("${msgValidNumbers} ${programExternalFinancing}");
				document.aimThemeForm.programExternalFinancing.focus();
				return false;
				}
			}			
		} else{
			document.aimThemeForm.programExternalFinancing.value=0;
			}

		document.aimThemeForm.programTotalFinancing.value=trim(document.aimThemeForm.programTotalFinancing.value);
			if (document.aimThemeForm.programTotalFinancing.value.length > 0){
			var text=document.aimThemeForm.programTotalFinancing.value;
			for (i=0; i < text.length; i++){
				if (validNumbers.indexOf(text.charAt(i)) ==-1){
				alert("${msgValidNumbers} ${programTotalFinancing}");
				document.aimThemeForm.programTotalFinancing.focus();
				return false;
				}
			}
		} else{
			document.aimThemeForm.programTotalFinancing.value=0;
		}
	</logic:empty>

			
			
		return true;
	}

		function submitProgram()
		{
			var temp = validate();
			if (temp == true) 
			{
			<digi:context name="addThm" property="context/module/moduleinstance/addTheme.do"/>
			document.aimThemeForm.action = "<%=addThm%>";
			document.aimThemeForm.submit();
			window.opener.location.reload();
			//window.close();
			}
		}
	
		function closeProgram()
		{
			<digi:context name="addThm" property="context/module/moduleinstance/addTheme.do"/>
			document.aimThemeForm.action = "<%=addThm%>";
			document.aimThemeForm.target = window.opener.name;
			window.opener.location.reload();
			window.close();
			return true;
		}	
	
		function load()
	{
		document.aimThemeForm.programName.value = "";
		document.aimThemeForm.programCode.value = "";
		document.aimThemeForm.programType.value = "";
		document.aimThemeForm.programDescription.value = "";		
	}
	
	function closeWindow() 
	{
		window.close();
	}
-->

</script>
<digi:errors/>

<digi:form action="/addTheme.do" method="post">
<digi:context name="digiContext" property="context" />




<html:hidden property="event"/>
<html:hidden property="themeId"/>
<html:hidden property="parentId"/>
<html:hidden property="prgLevel"/>


 				<table width="100%" align="center" cellPadding=3 cellspacing="0" style="font-size:12px;">
			  <tr bgColor=#dddddb>
				<td height="30" colspan="3" align="center" valign="middle" bgColor=#c7d4db>
						            <strong>
<logic:empty name="aimThemeForm" property="parentId"> 						  
          <digi:trn key="aim:addEditProgram">
										Add  Program                                    </digi:trn>
</logic:empty>
<logic:notEmpty name="aimThemeForm" property="parentId"> 
								 <digi:trn key="aim:addEditSubProgram">
										Add/Edit Sub Program                                    </digi:trn>
								</logic:notEmpty>
						            </strong>				  </td>
			  </tr>
				<tr bgColor=#ffffff><td height="10" colspan="3"></td></tr>
				<logic:notEmpty name="aimThemeForm" property="parentId"> 
										<tr bgColor=#ffffff>
								  <td height="10" align="right" valign="middle">
									<strong>
									<digi:trn key="aim:parentProgramName">
									Parent Program Name									</digi:trn>
									</strong></td>
									<td align="left">&nbsp;</td>
								    <td height="10" align="left"><strong>
							        <bean:write name="aimThemeForm" property="parentProgram"/>
								      </strong></td>
				  </tr>
				               </logic:notEmpty> 
                  <tr bgColor=#ffffff>
						<td width="30%" height="10" align="right" valign="middle"><digi:trn key="aim:programName">
										Program Name</digi:trn><font color="red">*</font>						</td>
                        <td width="2" align="left">&nbsp;</td>
                  <td width="71%" height="10" align="left">
<html:text property="programName" size="20" maxlength="200"/>						</td>
				<tr bgColor=#ffffff>
				<td width="30%" height="20" align="right" valign="middle">
				  <digi:trn key="aim:programDescription">Description</digi:trn>				</td>
				
                <td width="2" align="left">&nbsp;</td>
                <td align="left">
						<html:textarea property="programDescription" cols="35" rows="2" styleClass="inp-text"/>				</td>
				</tr>
					
<logic:empty name="aimThemeForm" property="parentId"> 
<tr bgColor=#ffffff>
					<td width="30%" height="20" align="right" valign="middle">
<digi:trn key="aim:Internal">
							Internal Financing						</digi:trn>			    </td>
                    <td width="2" align="left">&nbsp;</td>
                    <td align="left">
						<html:text property="programInernalFinancing" size="20"/>					</td>
				</tr>
				<tr bgColor=#ffffff>
					<td width="30%" height="20" align="right" valign="middle">
<digi:trn key="aim:External">
							External Financing						</digi:trn>			    </td>
                    <td width="2" align="left">&nbsp;</td>
                  <td align="left">
								<html:text property="programExternalFinancing" size="20"/>					</td>
				</tr>
				<tr bgColor=#ffffff>
					<td width="30%" height="20" align="right" valign="middle">
<digi:trn key="aim:TotasFinance">
							Total Financing Required						</digi:trn>			    </td>
                    <td width="2" align="left">&nbsp;</td>
                  <td align="left">
							<html:text property="programTotalFinancing" size="20"/>					</td>
				</tr>
</logic:empty>
				<tr bgColor=#ffffff>
					<td width="30%" height="20" align="right" valign="middle"><digi:trn key="aim:programLeadAgency">
								Lead Agency
						</digi:trn>					</td>
				    <td width="2" align="left">&nbsp;</td>
			      <td align="left"><html:textarea property="programLeadAgency"
						cols="35" rows="2" styleClass="inp-text" /></td>
				</tr>
				<tr bgColor=#ffffff>
				<td width="30%" height="20" align="right" valign="middle"><digi:trn key="aim:programCode">
						Program Code</digi:trn>		<font color="red"> *</font>	</td>
                <td width="2" align="left">&nbsp;</td>
                <td align="left">
						<html:text property="programCode" size="20" styleClass="inp-text" maxlength="50"/>				</td>
				</tr>
				<tr bgColor=#ffffff>
				<td width="30%" height="20" align="right" valign="middle"><digi:trn key="aim:programType">
						Program Type</digi:trn>
						<font color="red">*</font>				</td>
                <td width="2" align="left">&nbsp;</td>
                <td align="left">
					<c:set var="translation">
						<digi:trn key="aim:program:programTypeFirstLine">Please select from below</digi:trn>
					</c:set>
					<category:showoptions firstLine="${translation}" name="aimThemeForm" property="programTypeCategValId" 
						keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.PROGRAM_TYPE_KEY %>" styleClass="inp-text" />						</td>
				</tr>
				<tr bgColor="#ffffff">
					<td width="30%" height="20" align="right" valign="middle"><digi:trn key="aim:programTargetGroups">Target Groups
						</digi:trn>					</td>
                    <td width="2" align="left">&nbsp;</td>
                  <td align="left">
						<html:textarea property="programTargetGroups" cols="35" rows="2" styleClass="inp-text"/>					</td>
				</tr>
				<tr bgColor=#ffffff>
					<td width="30%" height="20" align="right" valign="middle"><digi:trn key="aim:programBackground">Background
						</digi:trn>					</td>
                    <td width="2" align="left">&nbsp;</td>
                  <td align="left">
						<html:textarea property="programBackground" cols="35" rows="2" styleClass="inp-text"/>					</td>
				</tr>
				<tr bgColor=#ffffff>
					<td width="30%" height="20" align="right" valign="middle"><digi:trn key="aim:programObjectives">Objectives
						</digi:trn>					</td>
                    <td width="2" align="left">&nbsp;</td>
                  <td align="left">
						<html:textarea property="programObjectives" cols="35" rows="2" styleClass="inp-text"/>					</td>
				</tr>
				<tr bgColor=#ffffff>
					<td width="30%" height="20" align="right" valign="middle"><digi:trn
						key="aim:programOutputs">Outputs
				  </digi:trn></td>
				    <td width="2" align="left">&nbsp;</td>
			      <td align="left"><html:textarea property="programOutputs" cols="35"
						rows="2" styleClass="inp-text" /></td>
				</tr>
				<tr bgColor=#ffffff>
					<td width="30%" height="20" align="right" valign="middle"><digi:trn
						key="aim:programBeneficiaries">Beneficiaries
				  </digi:trn></td>
				    <td width="2" align="left">&nbsp;</td>
			      <td align="left"><html:textarea property="programBeneficiaries"
						cols="35" rows="2" styleClass="inp-text" /></td>
				</tr>
				<tr bgColor=#ffffff>
					<td width="30%" height="20" align="right" valign="middle"><digi:trn
						key="aim:programEnvironmentConsiderations">Environment Considerations
				  </digi:trn></td>
				    <td width="2" align="left">&nbsp;</td>
			      <td align="left"><html:textarea
						property="programEnvironmentConsiderations" cols="35" rows="2"
						styleClass="inp-text" /></td>
				</tr>
				<tr bgColor=#ffffff>
					<td width="30%" height="20" align="right" valign="middle">
						<digi:trn>Show in RM Filter list</digi:trn>
					</td>
				  <td width="2" align="left">&nbsp;</td>
			      <td align="left"><html:checkbox property="showInRMFilters"/></td>
				</tr>
				<tr>
					<td width="30%" height="20" align="right" valign="middle">
						<digi:trn key="aim:startDate">Start Date</digi:trn>
					</td>
					<td width="2" align="left">&nbsp;</td>
					<td align="left">
						<html:text property="startDate"  styleId="startDate" name="aimThemeForm" readonly="true"  />
						<a id="date1" href='javascript:pickDateById("date1", "startDate")'>
							<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
						</a>
					</td>
				</tr>
					<tr>
						<td width="30%" height="20" align="right" valign="middle">
							<digi:trn key="aim:endDate">End Date</digi:trn>
						</td>
						<td width="2" align="left">&nbsp;</td>
						<td align="left">
							<html:text property="endDate"  styleId="endDate" name="aimThemeForm" readonly="true"  />
							<a id="date1" href='javascript:pickDateById("date1", "endDate")'>
								<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
							</a>
						</td>
					</tr>
				<tr>
					<td height="25" align="center" colspan="3" bgcolor=#ffffff>
					<hr />
						<input class="buttonx" type="button" id="addBtn" name="addBtn" value="<digi:trn key="aim:btnsave">Save</digi:trn>" onclick="return submitProgram('<bean:write name="aimThemeForm" property="themeId"/>','<bean:write name="aimThemeForm" property="rootId"/>','<bean:write name="aimThemeForm" property="name"/>')">&nbsp;&nbsp;
						<input class="buttonx" type="reset" value="<digi:trn>Reset</digi:trn>">&nbsp;&nbsp;
						<input class="buttonx" type="button" name="close" value="<digi:trn key="aim:btnclose">Close</digi:trn>" onclick="closeWindow()">
					</td>
				</tr>	
  </table>

</digi:form>


<script language="JavaScript">
	<!--
	if(document.aimThemeForm.event.value=='close') closeProgram();
	
	var enterBinder	= new EnterHitBinder('addBtn');
	-->
</script>