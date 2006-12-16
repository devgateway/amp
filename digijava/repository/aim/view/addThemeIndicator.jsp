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
	function validate() 
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
		return true;
	}

	function saveProgram(id)
	{
			var temp = validate();
			if (temp == true) 
			{
				<digi:context name="addThmInd" property="context/module/moduleinstance/addThemeIndicator.do?event=save"/>
				document.aimThemeForm.action = "<%=addThmInd%>&themeId=" +id;
				document.aimThemeForm.target = "_self";
				document.aimThemeForm.submit();
			}
			return true;	
	}
	
	function addIndVal(id)
	{
			<digi:context name="addIndVal" property="context/module/moduleinstance/addThemeIndicator.do?event=indValue"/>
			document.aimThemeForm.action = "<%=addIndVal%>&themeId=" +id;
			document.aimThemeForm.target = "_self";
			document.aimThemeForm.submit();
			return true;
	}
	
	function load(){}

	function unload(){}

	function closeWindow() 
	{
			<digi:context name="closeInd" property="context/module/moduleinstance/closeThemeIndicator.do"/>
			document.aimThemeForm.action = "<%=closeInd%>";
			document.aimThemeForm.submit();
			window.close();
			return true;
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
				<tr bgcolor=#ffffff>
				<td height="20" align="right">
					<b>Category</b>&nbsp;
				</td>
				<td align="left" valign="botto">
					<html:select name="aimThemeForm" property="category" styleClass="inp-text">
						<html:option value="0">Input</html:option>	
						<html:option value="1">Output</html:option>
						<html:option value="2">Process</html:option>
						<html:option value="3">Outcomes</html:option>
					</html:select>&nbsp;&nbsp;
					<html:checkbox name="aimThemeForm" property="npIndicator" title="Tick to mark this indicator as an National Planning Indicator"/>
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
				<% int calIndex = 0; %>
				<% String calIdIndex = ""; %>
				<c:if test="${ !empty aimThemeForm.prgIndValues}">
				<tr bgcolor=#dddddb>
				<td bgColor=#dddddb height="25" align="center" colspan="4">
						<table width="100%" border="0" bgcolor="#f4f4f2" cellspacing="1" cellpadding="0" class=box-border-nopadding>
								<tr bgcolor="#003366" class="textalb">
									<td align="center" valign="middle" width="75">
										<b><font color="white">Actual/<br>Planned</font></b>
									</td>
									<td align="center" valign="middle" width="120">
										<b><font color="white">Amount</font></b>
									</td>
									<td align="center" valign="middle" width="120">
										<b><font color="white">Creation Date</font></b>
									</td>
								</tr>
								<c:set var="index" value="-1"/>
							 	<c:forEach var="prgIndValues" items="${aimThemeForm.prgIndValues}">
										<tr>
											<td valign="bottom">
												<c:set var="index" value="${index+1}"/>
												<html:select name="prgIndValues" indexed="true" property="valueType" styleClass="inp-text">
													<html:option value="1">Actual</html:option>	
													<html:option value="0">Target</html:option>
												</html:select>
											</td>
											<td valign="bottom">
												<html:text name="prgIndValues" indexed="true" property="valAmount" size="17" styleClass="amt"/>
											</td>
											<td vAlign="bottom">
												<table cellPadding=0 cellSpacing=0>
													<tr>
														<td>
															<% calIdIndex = "" + calIndex; calIndex++;%>
															<html:text name="prgIndValues" indexed="true" property="creationDate" 
															styleId="<%=calIdIndex%>" readonly="true" size="10"/>
														</td>
														<td align="left" vAlign="center">&nbsp;
						             					<a href='javascript:calendar("<%=calIdIndex%>")'>	
		   						          			<img src="../ampTemplate/images/show-calendar.gif" border="0"></a>
														</td>
													</tr>
												</table>
											</td>																
											<%--	
											<td>
												<a href="javascript:removeFundingDetail(<bean:write name="fundingDetail" property="indexId"/>,0)">
												 	<digi:img src="module/cms/images/deleteIcon.gif" border="0" alt="Delete this transaction"/>
												</a>
											</td>
											--%>
										</tr>	
								</c:forEach>
						</table>
				</td>
				</tr>
				</c:if>
				<tr bgColor=#dddddb>
				<td bgColor=#dddddb height="25" align="center" colspan="4">
						<input styleClass="dr-menu" type="button" name="addValBtn" value="Add Indicator Values" onclick="return addIndVal('<bean:write name="aimThemeForm" property="themeId"/>')">&nbsp;&nbsp;
				</td>
				</tr>
				<tr bgcolor=#ffffff><td height="15" colspan="4"></td></tr>
				<tr bgColor=#dddddb>
				<td bgColor=#dddddb height="25" align="center" colspan="4">
						<input styleClass="dr-menu" type="button" name="addBtn" value="Save" onclick="return saveProgram('<bean:write name="aimThemeForm" property="themeId"/>')">&nbsp;&nbsp;
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
											<td width="9" height="15" bgcolor="#ffffff">
												&nbsp;
											</td>
											<td width="50" bgcolor="#dddddb">
												Code
											</td>
											<td align="left" bgcolor="#dddddb">
												Name
											</td>
											<td align="left" width="60" bgcolor="#dddddb">
												Type
											</td>
											<td align="center" bgcolor="#dddddb">
												Values
											</td>
										</tr>
										<logic:iterate name="aimThemeForm" property="prgIndicators" id="prgIndicators" type="org.digijava.module.aim.helper.AmpPrgIndicator">
												<tr bgcolor="#ffffff">
														<td width="9" height="15" bgcolor="#ffffff">
																<img src= "../ampTemplate/images/arrow_right.gif" border=0>
														</td>
														<td bgcolor="#f4f4f2" width="50">
																<bean:write name="prgIndicators" property="code"/>
														</td>
														<td align="left" bgcolor="#f4f4f2"><b>
																<bean:write name="prgIndicators" property="name"/></b>
														</td>
														<td align="left" width="60" bgcolor="#f4f4f2">
																<bean:write name="prgIndicators" property="type"/>
														</td>
														<td align="center" bgcolor="#f4f4f2">
																<logic:notEmpty name="prgIndicators" property="prgIndicatorValues">
																		<table width="100%" bgColor="#d7eafd" cellPadding=3 cellSpacing=1 align="center">
																				<tr bgcolor="#66FFFF">
																						<td width="50" bgcolor="#66FFFF">
																								Actual/Target
																						</td>
																						<td align="left" bgcolor="#66FFFF">
																								Amount
																						</td>
																						<td align="left" width="60" bgcolor="#66FFFF">
																								Date
																						</td>
																				</tr>
																				<logic:iterate name="prgIndicators" property="prgIndicatorValues" id="prgIndicatorValues" type="org.digijava.module.aim.helper.AmpPrgIndicatorValue">
																						<tr bgcolor="#ffffff">
																								<td bgcolor="#f4f4f2">
																										<logic:equal name="prgIndicatorValues" property="valueType" value="1">
																											Actual
																												
																										</logic:equal>
																										<logic:equal name="prgIndicatorValues" property="valueType" value="0">
																											Target
																												
																										</logic:equal>
																								</td>
																								<td align="left" bgcolor="#f4f4f2"><b>
																										<bean:write name="prgIndicatorValues" property="valAmount"/></b>
																								</td>
																								<td align="left" bgcolor="#f4f4f2">
																										<bean:write name="prgIndicatorValues" property="creationDate"/>
																								</td>
																						</tr>
																				</logic:iterate>
																		</table>
																</logic:notEmpty>	
														</td>
												</tr>
										</logic:iterate>
								</table>
						</td></tr>
						<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
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
