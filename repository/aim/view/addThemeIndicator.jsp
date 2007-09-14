<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addFunding.js"/>"></script>

<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp" flush="true" />
</script>

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

	function getDateValues(){
		var dates = [];
		var c=0;
		var elem=document.getElementsByName('prgIndValues['+c+'].creationDate');
		while(elem != null && elem.length > 0){
			dates[dates.length]=elem[0].value;
			c++;
			elem=document.getElementsByName('prgIndValues['+c+'].creationDate');
		}
		return dates;
	
	}

	function getTypeValues(){
		var types = [];
		var c=0;
		var elem=document.getElementsByName('prgIndValues['+c+'].valueType');
		while(elem != null && elem.length > 0){
			types[types.length]=elem[0].value;
			c++;
			elem=document.getElementsByName('prgIndValues['+c+'].valueType');
		}
		return types;
	}

	function checkValues(){
		var values=getTypeValues();
		if (values.length !=null){
			var targets=0;
			for (var i=0; i< values.length; i++){
				if (values[i] == '0'){
					targets++;
				}
			}
			if (targets > 1 || targets < 1 ) {
				alert('Please specify only one target value');
				return false;
			}
		}
		
		var dates=getDateValues();
		if (dates.length !=null){
			for (var i=0; i< dates.length; i++){
				if (dates[i] == '' || dates[i] ==  null ){
					alert('Please specify dates for all indicators');
					return false;
				}
			}
		}
		
		return true;
}

	function checkBaseValues(){
		var values=getTypeValues();
		if (values.length !=null){
			var targets=0;
			for (var i=0; i< values.length; i++){
				if (values[i] == '2'){
					targets++;
				}
			}
			if (targets > 1 || targets < 1 ) {
				alert('Please specify only one Base value');
				return false;
			}
		}
		
	
		return true;
}


	function saveProgram(id,indId)
	{
			if(checkBaseValues() == false) return false;
			if (checkValues() == false) return false;
			
			var temp = validate();
			if (temp == true) 
			{
			
				<digi:context name="addThmInd" property="context/module/moduleinstance/addThemeIndicator.do?event=save"/>
				document.aimThemeForm.action = "<%=addThmInd%>&themeId=" + id + "&indicatorId=" + indId;
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

	function closeWindow(indiType) 
	{
			<digi:context name="closeInd" property="context/module/moduleinstance/closeThemeIndicator.do"/>
			document.aimThemeForm.action = "<%=closeInd%>?type="+indiType;
			document.aimThemeForm.target = window.opener.name;
			document.aimThemeForm.submit();
			window.close();
	}

	function trim(s) {
		return s.replace( /^\s*/, "" ).replace( /\s*$/, "" );
  	}
	-->
</script>

<jsp:include page="scripts/newCalendar.jsp" flush="true" />
<digi:form action="/addThemeIndicator.do" method="post">
<digi:context name="digiContext" property="context"/>
<input type="hidden" name="event">
		<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" align="center" border="0">
				<tr bgColor=#dddddb>
				<td bgColor=#dddddb height="15" align="center" colspan="4"><h4>
						<digi:trn key="aim:programind">Program M&E Indicators </digi:trn></h4>
				</td>
				</tr>
				<tr bgColor=#ffffff><td height="10" colspan="4"></td></tr>
				<tr bgcolor=#ffffff><td height="5" colspan="4"></td></tr>
				<tr bgColor=#ffffff>
				<td height="20" align="right">
						<b><digi:trn key="aim:indname">Indicator Name</digi:trn></b><font color="red">*</font>&nbsp;
				</td>
				<td align="left" colspan="3">
						<html:text name="aimThemeForm" property="name" size="30"/>
				</td>
				</tr>
				<tr bgcolor=#ffffff><td height="5" colspan="4"></td></tr>
				<tr bgColor=#ffffff>
				<td height="20" align="right">
						<b><digi:trn key="aim:indcode">Indicator Code</digi:trn></b><font color="red">*</font>&nbsp;
				</td>
				<td align="left">
						<html:text name="aimThemeForm" property="code" size="20" styleClass="inp-text"/>
				</td>
				</tr>
				<tr bgcolor=#ffffff><td height="5" colspan="4"></td></tr>
				<tr bgColor=#ffffff>
				<td height="20" align="right">
						<b><digi:trn key="aim:indtype">Indicator Type</digi:trn></b><font color="red">*</font>&nbsp;
				</td>
				<td align="left">
						<html:select property="type" styleClass="inp-text">
								<html:option value="A"><digi:trn key="aim:ascending">Ascending</digi:trn></html:option>
								<html:option value="D"><digi:trn key="aim:dscending">descending</digi:trn></html:option>
						</html:select>
				</td>
				</tr>
				<tr bgcolor=#ffffff><td height="5" colspan="4"></td></tr>
				<tr bgColor=#ffffff>
				<td height="20" align="right">
						<b><digi:trn key="aim:cratedate">Creation Date</digi:trn></b><font color="red">*</font>&nbsp;
				</td>
				<td vAlign="bottom">
						<table cellPadding=0 cellSpacing=0>
								<tr>
								<td>
										<html:text name="aimThemeForm" property="creationDate" styleId="themeDate" readonly="true" size="10"/>
								</td>
								<td align="right" vAlign="center">&nbsp;
									<a id="date1" href='javascript:pickDate("date1",document.aimThemeForm.creationDate)'>
										<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
									</a>
								</td>
								</tr>
						</table>
				</td>
				</tr>
				<tr bgcolor=#ffffff><td height="5" colspan="4"></td></tr>
				<tr bgcolor=#ffffff>
				<td height="20" align="right">
					<b><digi:trn key="aim:crategory">Category</digi:trn></b>&nbsp;
				</td>
				<td align="left" valign="bottom">
					<html:select name="aimThemeForm" property="category" styleClass="inp-text">
						<html:option value="0"><digi:trn key="aim:input">Input</digi:trn></html:option>	
						<html:option value="1"><digi:trn key="aim:output">Output</digi:trn></html:option>
						<html:option value="2"><digi:trn key="aim:process">Process</digi:trn></html:option>
						<html:option value="3"><digi:trn key="aim:outcomes">Outcomes</digi:trn></html:option>
					</html:select>&nbsp;&nbsp;
				</td>
				</tr>
				<tr bgcolor=#ffffff><td height="5" colspan="4"></td></tr>
				<tr bgcolor=#ffffff>
				<td height="20" align="right"><b>
					<digi:trn key="aim:nationalplan">National Planning Indicator</digi:trn></b>&nbsp;
				</td>
				<td align="left" valign="botto">
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
										<b><font color="white"><digi:trn key="aim:actl">Actual</digi:trn>/<br><digi:trn key="aim:Plan">Planned</digi:trn></font></b>
									</td>
									<td align="center" valign="middle" width="120">
										<b><font color="white"><digi:trn key="aim:value">Value</digi:trn></font></b>
									</td>
									<td align="center" valign="middle" width="120">
										<b><font color="white"><digi:trn key="aim:creationdate">Creation Date</digi:trn></font></b>
									</td>
								</tr>
								<c:set var="index" value="-1"/>
							 	<c:forEach var="prgIndValues" items="${aimThemeForm.prgIndValues}">
										<tr>
											<td valign="bottom">
												<c:set var="index" value="${index+1}"/>
												<html:select name="prgIndValues" indexed="true" property="valueType" styleClass="inp-text">
													<html:option value="1"><digi:trn key="aim:mulprgmana:actual">Actual</digi:trn></html:option>	
													<html:option value="2"><digi:trn key="aim:mulprgmana:base">Base</digi:trn></html:option>	
													<html:option value="0"><digi:trn key="aim:mulprgmana:target">Target</digi:trn></html:option>
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
															<a id="date_<%=calIdIndex%>" href='javascript:pickDate("date_<%=calIdIndex%>",document.getElementsByName("prgIndValues[<%=calIdIndex%>].creationDate")[0])'>
																<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
															</a>
														</td>
													</tr>
												</table>
											</td>																
										</tr>	
								</c:forEach>
						</table>
				</td>
				</tr>
				</c:if>
				<tr bgColor=#dddddb>
				<td bgColor=#dddddb height="25" align="center" colspan="4">
						<input styleClass="dr-menu" type="button" name="addValBtn" value="<digi:trn key="aim:eddidicatoes">Add Indicator Values</digi:trn>" onclick="return addIndVal('<bean:write name="aimThemeForm" property="themeId"/>')">&nbsp;&nbsp;
				</td>
				</tr>
				<tr bgcolor=#ffffff><td height="15" colspan="4"></td></tr>
				<tr bgColor=#dddddb>
				<td bgColor=#dddddb height="25" align="center" colspan="4">
						<input styleClass="dr-menu" type="button" name="addBtn" value="<digi:trn key="aim:save">Save</digi:trn>" onclick="return saveProgram('<bean:write name="aimThemeForm" property="themeId"/>','<bean:write name="aimThemeForm" property="indicatorId"/>')">&nbsp;&nbsp;
						<input styleClass="dr-menu" type="reset" value="<digi:trn key="aim:cancel">Cancel</digi:trn>" onclick="closeWindow('<bean:write name="aimThemeForm" property="indType"/>')">	&nbsp;&nbsp;
						<input styleClass="dr-menu" type="button" name="close" value="<digi:trn key="aim:close">Close</digi:trn>" onclick="closeWindow('<bean:write name="aimThemeForm" property="indType"/>')">			
				</td>
				</tr>	
		</table>
		<table width="100%" cellPadding=4 cellSpacing=1 valign=top align=left bgcolor="#ffffff">
				<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
						<digi:trn key="aim:listofprogram">List of Program Indicators</digi:trn>
				</td></tr>
				<logic:notEmpty name="aimThemeForm" property="prgIndicators">
						<tr><td>
								<table width="100%" bgColor="#d7eafd" cellPadding=3 cellSpacing=1>
										<tr bgcolor="#ffffff">
											<td width="9" height="15" bgcolor="#ffffff">
												&nbsp;
											</td>
											<td width="50" bgcolor="#dddddb">
												<digi:trn key="aim:code">Code</digi:trn>
											</td>
											<td align="left" bgcolor="#dddddb">
												<digi:trn key="aim:name">Name</digi:trn>
											</td>
											<td align="left" width="60" bgcolor="#dddddb">
												<digi:trn key="aim:creation">Creation</digi:trn>
											</td>
											<td align="center" bgcolor="#dddddb">
												<digi:trn key="aim:values">Values</digi:trn>
											</td><%--
											<td width="25" height="15" bgcolor="#ffffff">
												&nbsp;
											</td>--%>
										</tr>
										<logic:iterate name="aimThemeForm" property="prgIndicators" id="prgIndicators" type="org.digijava.module.aim.helper.AmpPrgIndicator">
												<tr bgcolor="#ffffff">
														<td width="9" height="15" bgcolor="#ffffff">
																<img src= "../ampTemplate/images/bullet_grey.gif" border=0>
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
														<td align="center" bgcolor="#f4f4f2">
																<logic:notEmpty name="prgIndicators" property="prgIndicatorValues">
																		<table width="100%" bgColor="#d7eafd" cellPadding=3 cellSpacing=1 align="center">
																				<tr bgcolor="#66FFFF">
																						<td width="50" bgcolor="#66FFFF">
																								<digi:trn key="aim:act">Actual</digi:trn>/<digi:trn key="aim:target">Target</digi:trn>
																						</td>
																						<td align="left" bgcolor="#66FFFF">
																								<digi:trn key="aim:value">Value</digi:trn>
																						</td>
																						<td align="left" width="70" bgcolor="#66FFFF">
																								<digi:trn key="aim:date">Date</digi:trn>
																						</td>
																				</tr>
																				<logic:iterate name="prgIndicators" property="prgIndicatorValues" id="prgIndicatorValues" type="org.digijava.module.aim.helper.AmpPrgIndicatorValue">
																						<tr bgcolor="#ffffff">
																								<td width="50" bgcolor="#f4f4f2">
																									<c:if test="${prgIndicatorValues.valueType=='0'}">
																											<digi:trn key="aim:target">Target</digi:trn>
																									</c:if>
																									<c:if test="${prgIndicatorValues.valueType=='1'}">
																											<digi:trn key="aim:atual">Actual</digi:trn>
																									</c:if>
																									<c:if test="${prgIndicatorValues.valueType=='2'}">
																										    <digi:trn key="aim:base">Base</digi:trn>
																									</c:if>
																								</td>
																								<td align="left" bgcolor="#f4f4f2"><b>
																										<bean:write name="prgIndicatorValues" property="valAmount"/></b>
																								</td>
																								<td align="left" width="70" bgcolor="#f4f4f2">
																										<bean:write name="prgIndicatorValues" property="creationDate"/>
																								</td>
																						</tr>
																				</logic:iterate>
																		</table>
																</logic:notEmpty>	
														</td>
														<td width="25" height="15" bgcolor="#ffffff">
																<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																<c:set target="${urlParams}" property="prgIndicatorId">
																		<bean:write name="prgIndicators" property="indicatorId"/>
																</c:set>
																<c:set target="${urlParams}" property="themeId">
																		<bean:write name="aimThemeForm" property="themeId"/>
																</c:set>
																<c:set var="translation">
																		<digi:trn key="aim:clickToEditPrgIndicator">Click here to Edit Program Indicator</digi:trn>
																</c:set>
																[<digi:link href="/addThemeIndicator.do?event=edit" name="urlParams" title="${translation}">
																		<digi:trn key="aim:subProgramManagerEdit">Edit</digi:trn>
																</digi:link>]
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



