<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
	<!--
	function validatePrg() 
	{
		if (trim(document.aimThemeForm.name.value).length == 0) 
		{
			alert("Please enter Indicator name");
			document.aimThemeForm.name.focus();
			return false;
		}	
		if (trim(document.aimThemeForm.code.value).length == 0) 
		{
			alert("Please enter Indicator code");
			document.aimThemeForm.code.focus();
			return false;
		}			
		if (trim(document.aimThemeForm.type.value).length == 0) 
		{
			alert("Please enter Indicator type");
			document.aimThemeForm.type.focus();
			return false;
		}
		if (trim(document.aimThemeForm.creationDate.value).length == 0) 
		{
			alert("Please enter the date of creation of the Indicator");
			document.aimThemeForm.creationDate.focus();
			return false;
		}
		return true;
	}
	function validateProj() 
	{
		if (trim(document.aimThemeForm.name.value).length == 0) 
		{
			alert("Please enter Indicator name");
			document.aimThemeForm.name.focus();
			return false;
		}	
		if (trim(document.aimThemeForm.code.value).length == 0) 
		{
			alert("Please enter Indicator code");
			document.aimThemeForm.code.focus();
			return false;
		}
		return true;
	}
	function savePrgInd(id)
	{
		var temp = validatePrg();
		if (temp == true) 
		{
			<digi:context name="editThmInd" property="context/module/moduleinstance/editAllIndicator.do?indicator=savePrg"/>
			document.aimThemeForm.action = "<%=editThmInd%>&indicatorId=" +id;
			document.aimThemeForm.target = window.opener.name;
			document.aimThemeForm.submit();
			window.close();
		}
		return true;	
	}	
	function saveProjInd(id)
	{
		var temp = validateProj();
		if (temp == true) 
		{
			<digi:context name="editThmInd" property="context/module/moduleinstance/editAllIndicator.do?indicator=saveProj"/>
			document.aimThemeForm.action = "<%=editThmInd%>&indicatorId=" +id;
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
<digi:form action="/editAllIndicator.do" method="post">
<digi:context name="digiContext" property="context"/>
<input type="hidden" name="indicator">
		<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" align="center" border="0">
				<tr bgColor="blue"><td height="1" colspan="2"></td></tr>
				<tr bgColor=#dddddb>
				<td bgColor=#dddddb height="15" align="center" colspan="2"><h4>
						Edit Indicator </h4>
				</td>
				</tr>
				<tr bgColor="blue"><td height="1" colspan="2"></td></tr>
				<tr bgColor=#ffffff><td height="10" colspan="2"></td></tr>
				<tr bgColor=#ffffff><td height="10" colspan="4"></td></tr>

				<logic:equal name="aimThemeForm" property="saveType" value="program">
 				<tr bgColor=#ffffff>
						<td height="10" align="right">
								<b>Actual/Target</b>&nbsp;
						</td>
						<td height="10" align="left">
								<html:select name="aimThemeForm" property="valueType" styleClass="inp-text">
										<html:option value="0">Actual</html:option>	
										<html:option value="1">Target</html:option>
								</html:select>
								&nbsp;&nbsp;&nbsp;
								<b>Category</b>&nbsp;
								<html:select name="aimThemeForm" property="category" styleClass="inp-text">
										<html:option value="0">Input</html:option>	
										<html:option value="1">Output</html:option>
										<html:option value="2">Process</html:option>
										<html:option value="3">Outcomes</html:option>
								</html:select>
						</td>
				</tr>
				<tr bgcolor=#ffffff><td height="5" colspan="4"></td></tr>
				<tr bgcolor=#ffffff><td height="5" colspan="4"></td></tr>	
				<tr bgColor=#ffffff>
				<td height="20" align="right">
						<b>Indicator Name</b><font color="red">*</font>&nbsp;
				</td>
				<td align="left" colspan="3">
						<html:text name="aimThemeForm" property="name" size="30"/>
				</td>
				</tr>
				<tr bgcolor=#ffffff><td height="5" colspan="4"></td></tr>
				<tr bgColor=#ffffff>
				<td height="20" align="right">
						<b>Indicator Code</b><font color="red">*</font>&nbsp;
				</td>
				<td align="left">
						<html:text name="aimThemeForm" property="code" size="20" styleClass="inp-text"/>
				</td>
				</tr>
				<tr bgcolor=#ffffff><td height="5" colspan="4"></td></tr>
				<tr bgColor=#ffffff>
				<td height="20" align="right">
						<b>Indicator Type</b><font color="red">*</font>&nbsp;
				</td>
				<td align="left">
						<html:text name="aimThemeForm" property="type" size="20" styleClass="inp-text"/>
				</td>
				</tr>
				<tr bgcolor=#ffffff><td height="5" colspan="4"></td></tr>
				<tr bgColor=#ffffff>
				<td height="20" align="right">&nbsp;
						<b>Creation Date</b><font color="red">*</font>&nbsp;
				</td>
				<td align="left">
						<table cellPadding=0 cellSpacing=0>
								<tr>
								<td>
										<html:text name="aimThemeForm" property="creationDate" styleId="creationDate" readonly="true" size="10"/>
								</td>
								<td align="center" vAlign="center">&nbsp;
           					<a href="javascript:calendar('creationDate')">	
				          			<img src="../ampTemplate/images/show-calendar.gif" border="0">
								</a>
								</td>
								</tr>
						</table>
				</td>
				</tr>
				<tr bgcolor=#ffffff><td height="5" colspan="4"></td></tr>
				<tr bgColor=#ffffff>
				<td height="20" align="right">
						<b>National Planning Indicator</b>
				</td>
				<td align="left">
						<html:checkbox name="aimThemeForm" property="npIndicator"/>
				</td>	
				<tr bgcolor=#ffffff><td height="5" colspan="4"></td></tr>
				
				<tr bgColor=#dddddb>
				<td bgColor=#dddddb height="25" align="center" colspan="2">
						<input styleClass="dr-menu" type="button" name="addBtn1" value="Save" onclick="return savePrgInd('<bean:write name="aimThemeForm" property="indicatorId"/>')">
						&nbsp;&nbsp;
						<input styleClass="dr-menu" type="reset" value="Cancel">&nbsp;&nbsp;
						<input styleClass="dr-menu" type="button" name="close" value="Close" onclick="closeWindow()">			
				</td>
				</tr>
				</logic:equal>	
				
				<logic:equal name="aimThemeForm" property="saveType" value="project">	
					<tr bgColor=#ffffff>
						<td height="20" align="right">
							<b>Indicator Name</b><font color="red">*</font>&nbsp;
						</td>
						<td align="left" colspan="3">
							<html:text name="aimThemeForm" property="name" size="30"/>
						</td>
					</tr>
					<tr bgcolor=#ffffff><td height="5"></td></tr>
					<tr bgColor=#ffffff>
						<td height="20" align="right">
							<b>Indicator Code</b><font color="red">*</font>&nbsp;
						</td>
						<td align="left">
							<html:text name="aimThemeForm" property="code" size="20" styleClass="inp-text"/>
						</td>
					</tr>
					<tr bgColor=#ffffff><td height="30" colspan="2"></td></tr>
					<tr bgColor=#dddddb>
						<td bgColor=#dddddb height="25" align="center" colspan="2">
							<input styleClass="dr-menu" type="button" name="addBtn2" value="Save" onclick="return saveProjInd('<bean:write name="aimThemeForm" property="indicatorId"/>')">&nbsp;&nbsp;
							<input styleClass="dr-menu" type="reset" value="Cancel">&nbsp;&nbsp;
							<input styleClass="dr-menu" type="button" name="close" value="Close" onclick="closeWindow()">			
						</td>
					</tr>	
				</logic:equal>
		</table>
</digi:form>
