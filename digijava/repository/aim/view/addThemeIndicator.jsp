<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/calendar.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addFunding.js"/>"></script>

<script language="JavaScript">
	<!--
	function saveProgram(id)
	{
			<digi:context name="addThmInd" property="context/module/moduleinstance/addThemeIndicator.do?event=save"/>
			document.aimThemeForm.action = "<%=addThmInd%>&themeId=" +id;
			document.aimThemeForm.target = "_self";
			document.aimThemeForm.submit();
			return true;	
	}
	
	function load(){}

	function unload(){}

	function closeWindow() 
	{
		window.close();
	}

	function trim(s) {
		return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
  }
	-->
</script>

<digi:instance property="aimThemeForm" />
<digi:form action="/addThemeIndicator.do" method="post">
<digi:context name="digiContext" property="context"/>
<input type="hidden" name="event">
		<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" align="center" border="0">
				<tr bgColor=#dddddb>
				<td bgColor=#dddddb height="15" align="center" colspan="4"><h4>
						Program M&E Indicators </h4>
				</td>
				</tr>
				<tr bgColor=#ffffff><td height="10" colspan="4"></td></tr>
				<tr bgColor=#ffffff>
						<td height="10" align="right">
								<b>Actual/Target</b>&nbsp;
						</td>
						<td height="10" align="left">
								<html:select name="aimThemeForm" property="valueType" styleClass="inp-text">
										<html:option value="1">Actual</html:option>	
										<html:option value="0">Target</html:option>
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
						<b>Indicator Name</b>&nbsp;
				</td>
				<td align="left" colspan="3">
						<html:text name="aimThemeForm" property="name" size="30"/>
				</td>
				</tr>
				<tr bgcolor=#ffffff><td height="5" colspan="4"></td></tr>
				<tr bgColor=#ffffff>
				<td height="20" align="right">
						<b>Indicator Code</b>&nbsp;
				</td>
				<td align="left">
						<html:text name="aimThemeForm" property="code" size="20" styleClass="inp-text"/>
						&nbsp;&nbsp;&nbsp;
						<b>Indicator Type</b>&nbsp;
						<html:text name="aimThemeForm" property="type" size="17" styleClass="inp-text"/>
				</td>
				</tr>
				<tr bgcolor=#ffffff><td height="5" colspan="4"></td></tr>
				<tr bgColor=#ffffff>
				<td height="20" align="right">&nbsp;
						<b>Creation Date</b>&nbsp;
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
								<td>&nbsp;&nbsp;&nbsp;&nbsp;
										<b>National Planning Indicator</b>&nbsp;
										<html:checkbox name="aimThemeForm" property="npIndicator" />
								</td>
								</tr>
						</table>
				</td>
				</tr>	
				<tr bgcolor=#ffffff><td height="5" colspan="4"></td></tr>
				<tr bgColor=#ffffff>
				<td height="20" align="right"><b>
						<digi:trn key="aim:IndDescription">
								Description
						</digi:trn></b>&nbsp;
				</td>
				<td align="left" colspan="3">
						<html:textarea property="indicatorDescription" cols="35" rows="2" styleClass="inp-text"/>
				</td>
				</tr>
				<tr bgcolor=#ffffff><td height="15" colspan="4"></td></tr>	
				<tr bgColor=#dddddb>
				<td bgColor=#dddddb height="25" align="center" colspan="4">
						<input styleClass="dr-menu" type="button" name="addBtn" value="Save" onclick="return saveProgram('<bean:write name="aimThemeForm" property="themeId" />')">&nbsp;&nbsp;
						<input styleClass="dr-menu" type="reset" value="Cancel">&nbsp;&nbsp;
						<input styleClass="dr-menu" type="button" name="close" value="Close" onclick="closeWindow()">			
				</td>
				</tr>	
		</table>
		<table width="100%" cellPadding=4 cellSpacing=1 valign=top align=left bgcolor="#ffffff">
				<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
						List of Program Indicators
				</td></tr>
				<logic:notEmpty name="aimThemeForm" property="prgIndicators">
						<tr><td>
								<table width="100%" bgColor="#d7eafd" cellPadding=3 cellSpacing=1>
										<tr bgcolor="#ffffff">
											<td width="9" height="15" bgcolor="#f4f4f2">
												&nbsp;
											</td>
											<td bgcolor="#f4f4f2" width="50">
												Code
											</td>
											<td align="left" bgcolor="#f4f4f2">
												Name
											</td>
											<td align="left" width="60" bgcolor="#f4f4f2">
												Date
											</td>
											<td align="left" width="30" bgcolor="#f4f4f2">
												Type
											</td>
										</tr>
										<logic:iterate name="aimThemeForm" property="prgIndicators" id="prgIndicators" type="org.digijava.module.aim.helper.AmpPrgIndicator">
												<tr bgcolor="#ffffff">
														<td width="9" height="15" bgcolor="#f4f4f2">
																<img src= "../ampTemplate/images/arrow_right.gif" border=0>
														</td>
														<td bgcolor="#f4f4f2" width="50">
																<bean:write name="prgIndicators" property="code"/>
														</td>
														<td align="left" bgcolor="#f4f4f2"><b>
																<bean:write name="prgIndicators" property="name"/></b>
														</td>
														<td align="left" width="60" bgcolor="#f4f4f2">
																<bean:write name="prgIndicators" property="creationDate"/>
														</td>
														<td align="left" width="30" bgcolor="#f4f4f2">
																<bean:write name="prgIndicators" property="type"/>
														</td>
												</tr>
										</logic:iterate>
								</table>
						</td></tr>
				</logic:notEmpty>
				<logic:empty name="aimThemeForm" property="prgIndicators">
						<tr align="center" bgcolor="#ffffff"><td><b>
								<digi:trn key="aim:noIndicatorsPresent">No Indicators present</digi:trn></b></td>
						</tr>
						<tr bgColor="#d7eafd"><td></td></tr>
				</logic:empty>
		</table>
</digi:form>
