<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<c:set var="translation_progname">
	<digi:trn key="aim:addTheme:PlsEnterProgramName">Please enter Program Name</digi:trn>
</c:set>
<c:set var="translation_progcode">
	<digi:trn key="aim:addTheme:PlsEnterProgramCode">Please enter Program Code</digi:trn>
</c:set>
<c:set var="translation_progtype">
	<digi:trn key="aim:addTheme:PlsSelectProgramType">Please select Program Type</digi:trn>
</c:set>

<feature:display name="Admin NPD" module="National Planning Dashboard"></feature:display>
<script language="JavaScript">
<!--

	function sum()
	{
		var intFin = document.aimThemeForm.programInernalFinancing.value;
		var extFin = document.aimThemeForm.programExternalFinancing.value;
		var totFin = 0;
		
		//alert("intFin" +intFin);
		//alert("extFin" + extFin);
		totFin = parseFloat(intFin) + parseFloat(extFin);
		
		//alert("totFin" + totFin);
		document.aimThemeForm.programTotalFinancing.value = totFin;
	}
	
	function validate() 
	{
		if (trim(document.aimThemeForm.programName.value).length == 0) 
		{
			alert("${translation_progname}");
			document.aimThemeForm.programName.focus();
			return false;
		}	
		if (trim(document.aimThemeForm.programCode.value).length == 0) 
		{
			alert("${translation_progcode}");
			document.aimThemeForm.programCode.focus();
			return false;
		}			
		//if (trim(document.aimThemeForm.programType.value).length == 0) 
		//{
		//	alert("Please enter Program type");
		//	document.aimThemeForm.programType.focus();
		//	return false;
		//}
		if(document.aimThemeForm.programTypeCategValId.value == 0)
		{
		  	alert("${translation_progtype}");
			document.aimThemeForm.programTypeCategValId.focus();
			return false;
		}
		return true;
	}
	function saveProgram(id,rutId,name)
	{
			var temp = validate();
			if(temp == true)
			{
				<digi:context name="editThm" property="context/module/moduleinstance/editTheme.do?event=update"/>
				document.aimThemeForm.action = "<%=editThm%>&themeId=" + id + "&rootId=" + rutId + "&indname=" + name;
				document.aimThemeForm.target = window.opener.name;
				document.aimThemeForm.submit();
				window.close();
			}
			return true;
	}
	function load(){}

	function unload(){}

	function closeWindow() 
	{
		window.close();
	}

-->
</script>

<digi:instance property="aimThemeForm" />
<digi:context name="digiContext" property="context" />

<digi:form action="/themeManager.do" method="post">
<input type="hidden" name="event">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" align="center" border="0">
	<tr>
		<td align=left class=r-dotted-lg vAlign=top width=300>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr bgColor=#dddddb>
				<td bgColor=#dddddb height="10" align="center" colspan="2"><h4>
						<digi:trn key="aim:editProgram">
									Edit Program
						</digi:trn> </h4>
				</td>
				</tr>
				<tr bgColor=#ffffff><td height="10" colspan="2"></td></tr>
				<field:display name="Program Name" feature="Admin NPD"></field:display>
				<tr bgColor=#ffffff>
						<td height="10" align="center">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<digi:trn key="aim:programName">
										Program Name</digi:trn>
								<font color="red">*</font>
						</td>
						<td height="10" align="left">
								<html:text property="programName" size="20"/>
						</td>
				</tr>
				<field:display name="Program Description" feature="Admin NPD">
				<tr bgcolor=#ffffff><td height="5"></td></tr>
				<tr bgColor=#ffffff>
				<td height="20" align="center">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<digi:trn key="aim:programDescription">
						Description</digi:trn>
				</td>
				<td align="left">
						<html:textarea property="programDescription" cols="35" rows="2" styleClass="inp-text"/>
				</td>
				</tr>
				</field:display>
				<field:display name="Internal Financing" feature="Admin NPD">
				<tr bgColor=#ffffff>
					<td height="20" align="center">
						<digi:trn key="aim:Internal">
							Internal Financing
						</digi:trn>
			    </td>
					<td align="left">
						<html:text property="programInernalFinancing" size="20" onblur="javascript:sum();"/>
					</td>
				</tr>
				</field:display>
				<field:display name="External Financing" feature="Admin NPD">
				<tr bgColor=#ffffff>
					<td height="20" align="center">
						<digi:trn key="aim:External">
							External Financing
						</digi:trn>
			    </td>
					<td align="left">
								<html:text property="programExternalFinancing" size="20" onblur="javascript:sum();"/>
					</td>
				</tr>
				</field:display>
				<field:display name="Total Financing Required" feature="Admin NPD">
				<tr bgColor=#ffffff>
					<td height="20" align="center">
						<digi:trn key="aim:TotasFinance">
							Total Financing Required
						</digi:trn>
			    </td>
					<td align="left">
							<html:text property="programTotalFinancing" size="20" readonly="true" />
					</td>
				</tr>				
				</field:display>
				<field:display name="Program Lead Agency" feature="Admin NPD">
				<tr bgColor=#ffffff>
					<td height="20" align="center">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
						<digi:trn key="aim:programLeadAgency">
								Lead Agency
						</digi:trn>
					</td>
					<td align="left"><html:textarea property="programLeadAgency"
						cols="35" rows="2" styleClass="inp-text" /></td>
				</tr>
				</field:display>
				<field:display name="Program Code" feature="Admin NPD"></field:display>
				<tr bgColor=#ffffff>
				<td height="20" align="center">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<digi:trn key="aim:programCode">
						Program Code</digi:trn>
						<font color="red">*</font>
				</td>
				<td align="left">
						<html:text property="programCode" size="20" styleClass="inp-text"/>
					
				</td>
				</tr>
				<field:display name="Program Type" feature="Admin NPD"></field:display>
				<tr bgColor=#ffffff>
				<td height="20" align="center">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<digi:trn key="aim:programType">
						Program Type</digi:trn>
						<font color="red">*</font>
				</td>
				<td align="left">
					<c:set var="translation">
									<digi:trn key="aim:program:programTypeFirstLine">Please select from below</digi:trn>
					</c:set>
					<category:showoptions firstLine="${translation}" name="aimThemeForm" property="programTypeCategValId" 
						keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.PROGRAM_TYPE_KEY %>" styleClass="inp-text" />
				</td>
				</tr>
				<field:display name="Program Target Groups" feature="Admin NPD">
				<tr bgColor=#ffffff>
					<td height="20" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<digi:trn key="aim:programTargetGroups">
							Target Groups
						</digi:trn>
					</td>
					<td align="left">
						<html:textarea property="programTargetGroups" cols="35" rows="2" styleClass="inp-text"/>
					</td>
				</tr>
				</field:display>
				<field:display name="Program Background" feature="Admin NPD">
				<tr bgColor=#ffffff>
					<td height="20" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<digi:trn key="aim:programBackground">
							Background
						</digi:trn>
					</td>
					<td align="left">
						<html:textarea property="programBackground" cols="35" rows="2" styleClass="inp-text"/>
					</td>
				</tr>
				</field:display>
				<field:display name="Program Objectives" feature="Admin NPD">
				<tr bgColor=#ffffff>
					<td height="20" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<digi:trn key="aim:programObjectives">
							Objectives
						</digi:trn>
					</td>
					<td align="left">
						<html:textarea property="programObjectives" cols="35" rows="2" styleClass="inp-text"/>
					</td>
				</tr>
				</field:display>
				<field:display name="Program Outputs" feature="Admin NPD">
				<tr bgColor=#ffffff>
					<td height="20" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <digi:trn
						key="aim:programOutputs">
																					Outputs
																			</digi:trn></td>
					<td align="left"><html:textarea property="programOutputs" cols="35"
						rows="2" styleClass="inp-text" /></td>
				</tr>
				</field:display>
				<field:display name="Program Beneficiaries" feature="Admin NPD">
				<tr bgColor=#ffffff>
					<td height="20" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <digi:trn
						key="aim:programBeneficiaries">
																					Beneficiaries
																			</digi:trn></td>
					<td align="left"><html:textarea property="programBeneficiaries"
						cols="35" rows="2" styleClass="inp-text" /></td>
				</tr>
				</field:display>
				<field:display name="Program Environment Considerations" feature="Admin NPD">
				<tr bgColor=#ffffff>
					<td height="20" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <digi:trn
						key="aim:programEnvironmentConsiderations">
																					Environment Considerations
																			</digi:trn></td>
					<td align="left"><html:textarea
						property="programEnvironmentConsiderations" cols="35" rows="2"
						styleClass="inp-text" /></td>
				</tr>
				</field:display>
				<tr bgcolor=#ffffff><td height="5"></td></tr>	
				<tr bgColor=#ffffff><td height="30" colspan="2"></td></tr>
				<tr bgColor=#dddddb>
				<td bgColor=#dddddb height="25" align="center" colspan="2">
						<input styleClass="dr-menu" type="button" name="addBtn" value='<digi:trn key="aim:btn:save">Save</digi:trn>' onclick="return saveProgram('<bean:write name="aimThemeForm" property="themeId"/>','<bean:write name="aimThemeForm" property="rootId"/>','<bean:write name="aimThemeForm" property="name"/>')">&nbsp;&nbsp;
						<input styleClass="dr-menu" type="reset" value='<digi:trn key="aim:btn:cancel">Cancel</digi:trn>' onclick="closeWindow()">
				</td>
				</tr>	
		</table>
</digi:form>
