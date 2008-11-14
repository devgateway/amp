<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">

	function selectLocation()
	{
		var check = false;
		var implevel=document.aimEditActivityForm.impLevelValue.value;
		if(implevel == 1)
		{
				check=true;
		}

		if(implevel == 2)
		{
			if (document.aimEditActivityForm.impMultiRegion.value != -1)
			{
				check=true;
			}
		}
		else if(implevel == 3)
		{
			if ((document.aimEditActivityForm.impMultiZone.value != "") && (document.aimEditActivityForm.impMultiZone.value != -1))
			{
				check=true;
			}
		}
		else if(implevel == 4)
		{
			if (document.aimEditActivityForm.impMultiWoreda.value != -1)
			{
				check=true;
			}
		}

		if(check)
		{
			<digi:context name="selLoc" property="context/module/moduleinstance/locationSelected.do?edit=true"/>
	    	document.aimEditActivityForm.action = "<%= selLoc %>";
		 	document.aimEditActivityForm.target = window.opener.name;
	    	document.aimEditActivityForm.submit();
		 	window.close();
		 }
		 else
		 {
		 	alert("Please select a valid Location.");
		 }
	}

	function countryChanged() {
		  document.aimEditActivityForm.fill.value = "region";
		  <digi:context name="selectLoc" property="context/module/moduleinstance/selectLocation.do?edit=true" />
		  document.aimEditActivityForm.action = "<%= selectLoc %>";
		  document.aimEditActivityForm.target = "_self";
		  document.aimEditActivityForm.submit();
	}

	function regionChanged() {
		  document.aimEditActivityForm.fill.value = "zone";
		  <digi:context name="selectLoc" property="context/module/moduleinstance/selectLocation.do?edit=true" />
		  document.aimEditActivityForm.action = "<%= selectLoc %>";
		  document.aimEditActivityForm.target = "_self";
		  document.aimEditActivityForm.submit();
	}

	function zoneChanged() {
		  document.aimEditActivityForm.fill.value = "woreda";
		  <digi:context name="selectLoc" property="context/module/moduleinstance/selectLocation.do?edit=true" />
		  document.aimEditActivityForm.action = "<%= selectLoc %>";
		  document.aimEditActivityForm.target = "_self";
		  document.aimEditActivityForm.submit();
	}

	function load() {
		if (document.aimEditActivityForm.impLevelValue.value == 2) {
			document.aimEditActivityForm.impMultiRegion.value=-1;
			document.aimEditActivityForm.impMultiRegion.focus();
		} else if (document.aimEditActivityForm.impLevelValue.value > 2){
			//document.aimEditActivityForm.impRegion.value=-1;
			document.aimEditActivityForm.impRegion.focus();
		}
	}

	function unload() {
	}
	function closeWindow() {
		window.close();
	}

	function checkEmpty() {
		var flag=true;
		if(trim(document.aimEditActivityForm.keyword.value) == "")
		{
			alert("Please Enter a Keyword....");
			flag=false;
		}
		if(trim(document.aimEditActivityForm.tempNumResults.value) == 0)
		{
			alert("Invalid value at 'Number of results per page'");
			flag=false;
		}

		return flag;
	}

function checkNumeric(objName,comma,period,hyphen)
	{
		var numberfield = objName;
		if (chkNumeric(objName,comma,period,hyphen) == false)
		{
			numberfield.select();
			numberfield.focus();
			return false;
		}
		else
		{
			return true;
		}
	}

	function chkNumeric(objName,comma,period,hyphen)
	{
// only allow 0-9 be entered, plus any values passed
// (can be in any order, and don't have to be comma, period, or hyphen)
// if all numbers allow commas, periods, hyphens or whatever,
// just hard code it here and take out the passed parameters
		var checkOK = "0123456789" + comma + period + hyphen;
		var checkStr = objName;
		var allValid = true;
		var decPoints = 0;
		var allNum = "";

		for (i = 0;  i < checkStr.value.length;  i++)
		{
			ch = checkStr.value.charAt(i);
			for (j = 0;  j < checkOK.length;  j++)
			if (ch == checkOK.charAt(j))
			break;
			if (j == checkOK.length)
			{
				allValid = false;
				break;
			}
			if (ch != ",")
			allNum += ch;
		}
		if (!allValid)
		{
			alertsay = "Please enter only numbers in the \"Number of results per page\"."
			alert(alertsay);
			return (false);
		}
	}


	function searchLoc() {
		if(checkNumeric(document.aimEditActivityForm.tempNumResults	,'','','')==true)
		{
			if(checkEmpty())
			{
			 <digi:context name="searchLoc" property="context/module/moduleinstance/searchLocation.do"/>
			 document.aimEditActivityForm.action = "<%= searchLoc %>";
			 document.aimEditActivityForm.submit();
			 return true;
			}
		}
		else return false;
	}

	function selectLoc2() {
		openNewWindow(600, 450);
		<digi:context name="selectLoc" property="context/module/moduleinstance/searchLocation.do" />
		document.aimEditActivityForm.action = "<%= selectLoc %>";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
	}


</script>

<digi:instance property="aimEditActivityForm" />
<bean:define id="location" name="aimEditActivityForm" property="location"></bean:define>

<digi:form action="/locationSelected.do" method="post">
<html:hidden property="locationReset" value="false" />
<html:hidden property="fill" />
<html:hidden property="impLevelValue" />
<html:hidden property="country" />
<html:hidden property="impCountry" />


<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border=0>
	<tr><td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left vAlign=top>
					<table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<digi:trn key="aim:selectLocation">
								Select Location</digi:trn>
							</td>
						</tr>
						<tr>
							<td align="center" bgcolor=#ECF3FD>
								<table cellPadding=2 cellSpacing=2>
									<logic:greaterEqual name="aimEditActivityForm" property="impLevelValue" value="1">
										<tr>
											<td>
												<category:getoptionvalue categoryKey="<%= org.digijava.module.categorymanager.util.CategoryConstants.IMPLEMENTATION_LOCATION_KEY%>" categoryIndex="0"/>
											</td>
											<td><b>
												<c:out value="${aimEditActivityForm.country}"/></b>
												<%--
												<html:select property="impCountry" onchange="countryChanged()" styleClass="inp-text">
													<html:option value=" ">Select Country</html:option>
													<html:optionsCollection name="aimEditActivityForm" property="countries"
													value="iso" label="countryName" />
												</html:select>--%>
											</td>
										</tr>
									</logic:greaterEqual>
									<logic:greaterEqual name="aimEditActivityForm" property="impLevelValue" value="2">
										<tr>
											<td>
												<category:getoptionvalue categoryKey="<%= org.digijava.module.categorymanager.util.CategoryConstants.IMPLEMENTATION_LOCATION_KEY%>" categoryIndex="1"/>
											</td>
											<td>
												<c:if test="${aimEditActivityForm.impLevelValue != 2}">
													<html:select property="impRegion" onchange="regionChanged()" styleClass="inp-text">
                                                                                                             <html:option value="-1"><digi:trn key="aim:addActivityLocations:selectRegion">Select Region</digi:trn></html:option>
														<logic:notEmpty name="aimEditActivityForm" property="regions">
															<html:optionsCollection name="aimEditActivityForm" property="regions"
															value="ampRegionId" label="name" />
														</logic:notEmpty>
													</html:select>
												</c:if>
												<c:if test="${aimEditActivityForm.impLevelValue == 2}">
														<html:select property="impMultiRegion" styleClass="inp-text" size="5" multiple="true">
														<html:option value="-1">&nbsp;&nbsp;<digi:trn key="aim:addActivityLocations:selectRegion">Select Region</digi:trn>&nbsp;&nbsp;</html:option>
														<logic:notEmpty name="aimEditActivityForm" property="regions">
															<html:optionsCollection name="aimEditActivityForm" property="regions"
															value="ampRegionId" label="name" />
														</logic:notEmpty>
													</html:select>
												</c:if>
											</td>
										</tr>
									</logic:greaterEqual>
									<logic:greaterEqual name="aimEditActivityForm" property="impLevelValue" value="3">
										<tr>
											<td>
												<category:getoptionvalue categoryKey="<%= org.digijava.module.categorymanager.util.CategoryConstants.IMPLEMENTATION_LOCATION_KEY%>" categoryIndex="2"/>
											</td>
											<td>
												<c:if test="${aimEditActivityForm.impLevelValue != 3}">
												<html:select property="impZone" onchange="zoneChanged()" styleClass="inp-text" >
													<html:option value="-1"><digi:trn key="aim:addActivityLocations:selectZone">Select Zone</digi:trn></html:option>
													<logic:notEmpty name="aimEditActivityForm" property="zones">
														<html:optionsCollection name="aimEditActivityForm" property="zones"
														value="ampZoneId" label="name" />
													</logic:notEmpty>
												</html:select>
												</c:if>
												<c:if test="${aimEditActivityForm.impLevelValue == 3}">
												<html:select property="impMultiZone" styleClass="inp-text" size="5" multiple="true">
                                                                                                     <html:option value="-1">&nbsp;&nbsp;<digi:trn key="aim:addActivityLocations:selectZone">Select Zone</digi:trn>&nbsp;&nbsp;</html:option>
													<logic:notEmpty name="aimEditActivityForm" property="zones">
														<html:optionsCollection name="aimEditActivityForm" property="zones"
														value="ampZoneId" label="name" />
													</logic:notEmpty>
												</html:select>
												</c:if>
											</td>
										</tr>
									</logic:greaterEqual>
									<logic:greaterEqual name="aimEditActivityForm" property="impLevelValue" value="4">
										<tr>
											<td>
												<category:getoptionvalue categoryKey="<%= org.digijava.module.categorymanager.util.CategoryConstants.IMPLEMENTATION_LOCATION_KEY%>" categoryIndex="3"/>
											</td>
											<td>
												<html:select property="impMultiWoreda"  styleClass="inp-text" size="5" multiple="true">
													<html:option value="-1">&nbsp;&nbsp;<digi:trn key="aim:addActivityLocations:selectDistrict">Select District </digi:trn>&nbsp;&nbsp;</html:option>
													<logic:notEmpty name="aimEditActivityForm" property="woredas">
														<html:optionsCollection name="aimEditActivityForm" property="woredas"
														value="ampWoredaId" label="name" />
													</logic:notEmpty>
												</html:select>
											</td>
										</tr>
									</logic:greaterEqual>
								</table>
							</td>
						</tr>

						<tr bgcolor="#ECF3FD">
							<td align="center">
								<table cellPadding=3 cellSpacing=3>
									<tr>
										<td>
											<input type="button" value="<digi:trn key='btn:add'>Add</digi:trn>" class="dr-menu"
											onclick="selectLocation()">
										</td>
										<td>
											<input type="reset" value="<digi:trn key='btn:clear'>Clear</digi:trn>" class="dr-menu">
										</td>
										<td>
											<input type="button" value="<digi:trn key='btn:close'>Close</digi:trn>" class="dr-menu" onclick="closeWindow()">
										</td>
									</tr>
								</table>
							</td>
						</tr>

					</table>
				</td>
			</tr>
		</table>
	</td></tr>
</table>

<br>
<br>
 <c:if test="${!aimEditActivityForm.defaultCountryIsSet}">
<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border=0>
	<tr><td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left vAlign=top>


					<table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<digi:trn key="aim:searchLocations">
								Search Locations</digi:trn>
							(<script language="JavaScript">
								{
										document.write('<category:getoptionvalue categoryValueId="${location.implemLocationLevel}"/>');
								}
							</script>)
							</td></tr>
						<tr>
							<td align="center" bgcolor=#ECF3FD>
								<table cellSpacing=2 cellPadding=2>
									<tr>
										<td>
											<digi:trn key="aim:enterKeyword">
											Enter a keyword</digi:trn>
										</td>
										<td>
											<html:text property="keyword" styleClass="inp-text" />
										</td>
									</tr>
									<tr>
										<td>
											<digi:trn key="aim:numResultsPerPage">
											Number of results per page</digi:trn>
										</td>
										<td>
											<html:text property="tempNumResults" size="2" value="10" styleClass="inp-text" />
										</td>
									</tr>
									<tr>
										<td align="center" colspan=2>
											<input type="button" value="<digi:trn key='btn:search'>Search</digi:trn>" class="dr-menu" onclick="return searchLoc()">
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>

				</td>
			</tr>
</table>
</c:if>
</digi:form>
