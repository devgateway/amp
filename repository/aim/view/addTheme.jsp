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
<script language="JavaScript">
<!--
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
		if (trim(document.aimThemeForm.programTypeCategValId.value) == 0) 
		{
			alert("${translation_progtype}");
			document.aimThemeForm.programTypeCategValId.focus();
			return false;
		}
		return true;
	}
	function saveProgram(rutId,id,level,name)
	{
			var temp = validate();
			if (temp == true) 
			{
				document.aimThemeForm.addBtn.disabled = true;	  	
				<digi:context name="addThm" property="context/module/moduleinstance/addSubPrgInd.do?event=save"/>
				document.aimThemeForm.action = "<%=addThm%>&themeId=" + id + "&indlevel=" + level + "&indname=" + name + "&rootId=" + rutId;
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
<feature:display name="Admin NPD" module="National Planning Dashboard"></feature:display>
<digi:form action="/themeManager.do" method="post">

		<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" align="center" border="0">
				<tr bgColor="blue"><td height="1" colspan="2"></td></tr>
				<tr bgColor=#dddddb>
				<td bgColor=#dddddb height="15" align="center" colspan="2"><h4>
						<digi:trn key="aim:programManager">Program Manager</digi:trn> </h4>
				</td>
				</tr>
				<tr bgColor="blue"><td height="1" colspan="2"></td></tr>
				<tr bgColor=#ffffff><td height="10" colspan="2"></td></tr>
				<tr bgColor=#ffffff>
					<td bgColor=#ffffff height="15" align="center" colspan="2"><h5>
							<digi:trn key="aim:CreatingNewProgram">
									Create a New Program
							</digi:trn></h5>
					</td>
				</tr>
				<field:display name="Program Name" feature="Admin NPD"></field:display>
				<tr bgColor=#ffffff>
						<td height="10" align="left">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<digi:trn key="aim:programName">
										Program Name
								</digi:trn>
								<font color="red">*</font>
						</td>
						<td height="10" align="left">
								<html:text property="programName" size="20"/>
						</td>
				</tr>
				<tr bgcolor=#ffffff><td height="5"></td></tr>
				<field:display name="Program Description" feature="Admin NPD">
				<tr bgColor=#ffffff>
					<td height="20" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<digi:trn key="aim:programDescription">
								Description
							</digi:trn>
					</td>
					<td align="left">
							<html:textarea property="programDescription" cols="35" rows="2" styleClass="inp-text"/>
					</td>
				</tr>
				</field:display>
				<field:display name="Program Lead Agency" feature="Admin NPD">
				<tr bgColor=#ffffff>
					<td height="20" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<digi:trn key="aim:programLeadAgency">
							Lead Agency
						</digi:trn>
			    </td>
					<td align="left">
							<html:textarea property="programLeadAgency" cols="35" rows="2" styleClass="inp-text"/>
					</td>
				</tr>
				</field:display>
				<field:display name="Program Code" feature="Admin NPD"></field:display>
				<tr bgColor=#ffffff>
					<td height="20" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<digi:trn key="aim:programCode">
							Program Code
						</digi:trn>
						<font color="red">*</font>
					</td>
					<td align="left">
							<html:text property="programCode" size="20" styleClass="inp-text"/>
					</td>
				</tr>
				<field:display name="Program Type" feature="Admin NPD"></field:display>
				<tr bgColor=#ffffff>
					<td height="20" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<digi:trn key="aim:programType">
							Program Type
						</digi:trn>
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
					<td height="20" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
					<td height="20" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
					<td height="20" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
					<td height="20" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<digi:trn key="aim:programOutputs">
							Outputs
						</digi:trn>
					</td>
					<td align="left">
						<html:textarea property="programOutputs" cols="35" rows="2" styleClass="inp-text"/>
					</td>
				</tr>
				</field:display>
				<field:display name="Program Beneficiaries" feature="Admin NPD">
				<tr bgColor=#ffffff>
					<td height="20" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<digi:trn key="aim:programBeneficiaries">
							Beneficiaries
						</digi:trn>
					</td>
					<td align="left">
						<html:textarea property="programBeneficiaries" cols="35" rows="2" styleClass="inp-text"/>
					</td>
				</tr>
				</field:display>
				<field:display name="Program Environment Considerations" feature="Admin NPD">
				<tr bgColor=#ffffff>
					<td height="20" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<digi:trn key="aim:programEnvironmentConsiderations">
							Environment Considerations
						</digi:trn>
					</td>
					<td align="left">
							<html:textarea property="programEnvironmentConsiderations" cols="35" rows="2" styleClass="inp-text"/>
					</td>
				</tr>
				</field:display>
				<tr bgcolor=#ffffff><td height="5"></td></tr>	
				<tr bgColor=#ffffff><td height="30" colspan="2"></td></tr>
				<tr bgColor=#dddddb>
				<td bgColor=#dddddb height="25" align="center" colspan="2">
						<input styleClass="dr-menu" type="button" name="addBtn" value="<digi:trn key="aim:btnsave">Save</digi:trn>" onclick="return saveProgram('<bean:write name="aimThemeForm" property="rootId" />','<bean:write name="aimThemeForm" property="prgParentThemeId" />','<bean:write name="aimThemeForm" property="prgLevel"/>','${aimThemeForm.encodeName}')">&nbsp;&nbsp;
						<input styleClass="dr-menu" type="reset" value="<digi:trn key="aim:btncancelCancel">Cancel</digi:trn>" onclick="closeWindow()">
						<input styleClass="dr-menu" type="button" name="close" value="<digi:trn key="aim:btnclose">Close</digi:trn>" onclick="closeWindow()">
				</td>
			</tr>
	</table>
</digi:form>
