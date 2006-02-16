<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

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
			if (document.aimEditActivityForm.impMultiZone.value != -1)
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
			window.opener.document.aimEditActivityForm.currUrl.value="";
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
			document.aimEditActivityForm.impMultiRegion.focus();
		} else if (document.aimEditActivityForm.impLevelValue.value > 2){
			document.aimEditActivityForm.impRegion.focus();
		}
	}

	function unload() {
		window.opener.document.aimEditActivityForm.currUrl.value="";
	}
	function closeWindow() {
		window.opener.document.aimEditActivityForm.currUrl.value="";
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

	function searchLoc() {
		var flg=checkEmpty();
			if(flg)
		{
		 <digi:context name="searchLoc" property="context/module/moduleinstance/searchLocation.do"/>
		 document.aimEditActivityForm.action = "<%= searchLoc %>";
		 document.aimEditActivityForm.submit();
		 return true;
		}
	}

	function selectLoc2() {
		openNewWindow(600, 450);
		<digi:context name="selectLoc" property="context/module/moduleinstance/searchLocation.do" />
		document.aimEditActivityForm.action = "<%= selectLoc %>";
		document.aimEditActivityForm.currUrl.value = "<%= selectLoc %>";
		document.aimEditActivityForm.target = popupPointer.name;
		document.aimEditActivityForm.submit();
	} 


</script>

<digi:instance property="aimEditActivityForm" />
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
												<digi:trn key="aim:location">
												Country</digi:trn>
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
												<digi:trn key="aim:region">
												Region</digi:trn>
											</td>
											<td>
												<c:if test="${aimEditActivityForm.impLevelValue != 2}">
													<html:select property="impRegion" onchange="regionChanged()" styleClass="inp-text">
														<html:option value="-1">Select Region</html:option>
														<logic:notEmpty name="aimEditActivityForm" property="regions">
															<html:optionsCollection name="aimEditActivityForm" property="regions"
															value="ampRegionId" label="name" />
														</logic:notEmpty>
													</html:select>
												</c:if>
												<c:if test="${aimEditActivityForm.impLevelValue == 2}">
														<html:select property="impMultiRegion" styleClass="inp-text" size="5" multiple="true">
														<html:option value="-1">&nbsp;&nbsp;Select Region&nbsp;&nbsp;</html:option>
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
												<digi:trn key="aim:zone">
												Zone</digi:trn>
											</td>
											<td>
												<c:if test="${aimEditActivityForm.impLevelValue != 3}">
												<html:select property="impZone" onchange="zoneChanged()" styleClass="inp-text" >
													<html:option value="-1">Select Zone</html:option>
													<logic:notEmpty name="aimEditActivityForm" property="zones">
														<html:optionsCollection name="aimEditActivityForm" property="zones"
														value="ampZoneId" label="name" />
													</logic:notEmpty>
												</html:select>
												</c:if>
												<c:if test="${aimEditActivityForm.impLevelValue == 3}">
												<html:select property="impMultiZone" styleClass="inp-text" size="5" multiple="true">
													<html:option value="-1">&nbsp;&nbsp;Select Zone&nbsp;&nbsp;</html:option>
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
												<digi:trn key="aim:woreda">
												Woreda</digi:trn>
											</td>
											<td>
												<html:select property="impMultiWoreda"  styleClass="inp-text" size="5" multiple="true">
													<html:option value="-1">&nbsp;&nbsp;Select Woreda&nbsp;&nbsp;</html:option>
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
											<input type="button" value="Add" class="dr-menu"
											onclick="selectLocation()">
										</td>
										<td>
											<input type="reset" value="Clear" class="dr-menu">
										</td>
										<td>
											<input type="button" value="Close" class="dr-menu" onclick="closeWindow()">
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
							(<bean:write name="aimEditActivityForm" property="implementationLevel" />)
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
											<input type="button" value="Search" class="dr-menu" onclick="return searchLoc()">
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
</table>
</digi:form>
