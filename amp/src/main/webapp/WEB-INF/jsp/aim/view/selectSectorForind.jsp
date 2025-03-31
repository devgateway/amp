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

	<!--

	function selectSector() {
		var check = checkSectorEmpty();
		if(check)
		{
		<digi:context name="selSector" property="context/module/moduleinstance/sectorSelectedForind.do?edit=true"/>
	    document.aimNewIndicatorForm.action = "<%= selSector %>";
		 document.aimNewIndicatorForm.target = window.opener.name;
	    document.aimNewIndicatorForm.submit();
		window.close();
		}
	}	
  
	function reloadSector(value) {
		//document.aimNewIndicatorForm.subsectorLevel1.disabled=false;
		//document.aimNewIndicatorForm.subsectorLevel2.disabled=false;
		if (value == 1) {
			document.aimNewIndicatorForm.sector.value = -1;
		}
	/*
		} else if (value == 2) {
			document.aimNewIndicatorForm.subsectorLevel1.value = -1;
		} else if (value == 3) {
			document.aimNewIndicatorForm.subsectorLevel2.value = -1;
		}
	*/
		<digi:context name="selSector" property="context/module/moduleinstance/selectSectorForind.do?edit=true"/>
	    document.aimNewIndicatorForm.action = "<%= selSector %>";
  		document.aimNewIndicatorForm.submit();									
	}	
	
	function checkSectorEmpty()
	{
		var sectorFlag = true;
		if(document.aimNewIndicatorForm.sector.value == -1)
		{
			alert("Please Select a sector First")
			sectorFlag = false;
		}
		return sectorFlag;
	}
	function checkEmpty() {
		var flag=true;
		if(trim(document.aimNewIndicatorForm.keyword.value) == "")
		{
			alert("Please Enter a Keyword....");
			flag=false;
		}
		if(trim(document.aimNewIndicatorForm.tempNumResults.value) == 0)
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

	function searchSector() {
	if(checkNumeric(document.aimNewIndicatorForm.tempNumResults	,'','','')==true) 
		{	
			var flg=checkEmpty();
			if(flg)
			{
			 <digi:context name="searchSctr" property="context/module/moduleinstance/searchSectorsforind.do?edit=true"/>
			 document.aimNewIndicatorForm.action = "<%= searchSctr %>";
			 document.aimNewIndicatorForm.submit();
			 return true;
			}
		}
		else return false;
	}

	function load() {
		document.aimNewIndicatorForm.sectorScheme.focus();			  
	}

	function unload() {
	}

	function closeWindow() {
		window.close();
	}

	-->

</script>

<digi:instance property="aimNewIndicatorForm" />
<digi:form action="/sectorSelectedForind.do" method="post">
<html:hidden property="sectorReset" value="false"/>

<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border="0">
	<tr><td vAlign="top">
		<table bgcolor="#F8F8F8" cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left valign="top">
					<table bgcolor="#F8F8F8" cellpadding="0" cellspacing="0" width="100%" class=box-border-nopadding>
						<tr bgcolor="#4A687A">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<digi:trn key="aim:selectSectors">
								Select Sectors</digi:trn>
							</td></tr>
						<tr>
							<td align="center" >
								<table cellSpacing=2 cellPadding=2>
									<tr>
										<td>
											<digi:trn key="aim:sectorScheme">
											Sector scheme</digi:trn>
										</td>
										<td>
                                                                                    <c:if test="${not empty aimNewIndicatorForm.sectorSchemes}">
                                                                                        <html:select property="sectorScheme" onchange="reloadSector(1)" styleClass="inp-text">
												<logic:notEmpty name="aimNewIndicatorForm" property="sectorSchemes">
													<html:optionsCollection name="aimNewIndicatorForm" property="sectorSchemes" 
													value="ampSecSchemeId" label="secSchemeName" />												
												</logic:notEmpty>
											</html:select>
                                                                                        
                                                                                    </c:if>
											
										</td>
									</tr>								
									<tr>
										<td>
											<digi:trn key="aim:sector">
											Sector</digi:trn>
										</td>
										<td>
                                                                                    <c:if test="${not empty aimNewIndicatorForm.parentSectors}">
											<html:select property="sector" onchange="reloadSector(2)" styleClass="inp-text">
												<html:option value="-1">Select sector</html:option>
												<logic:notEmpty name="aimNewIndicatorForm" property="parentSectors">
													<html:optionsCollection name="aimNewIndicatorForm" property="parentSectors" 
													value="ampSectorId" label="name" />												
												</logic:notEmpty>												
											</html:select>
                                                                                        </c:if>
										</td>
									</tr>								
									<!--<tr>
										<td>
											<digi:trn key="aim:subSectorLevel1">
											Sub-Sector Level 1</digi:trn>
										</td>
										<td>
											<html:select property="subsectorLevel1" onchange="reloadSector(3)" styleClass="inp-text">
												<html:option value="-1">Select sub-sector</html:option>
												<logic:notEmpty name="aimNewIndicatorForm" property="childSectorsLevel1">
													<html:optionsCollection name="aimNewIndicatorForm" property="childSectorsLevel1" 
													value="ampSectorId" label="name" />												
												</logic:notEmpty>													
											</html:select>
										</td>
												</tr>
									<tr <logic:empty name="SA" scope="application">style="position:relative;display:none;"</logic:empty>>
										<td>
											<digi:trn key="aim:subSectorLevel2">
											Sub-Sector Level 2</digi:trn>
										</td>
										<td>
											<html:select property="subsectorLevel2" styleClass="inp-text">
												<html:option value="-1">Select sub-sector</html:option>
												<logic:notEmpty name="aimNewIndicatorForm" property="childSectorsLevel2">
													<html:optionsCollection name="aimNewIndicatorForm" property="childSectorsLevel2" 
													value="ampSectorId" label="name" />												
												</logic:notEmpty>													
											</html:select>
										</td>
									</tr>-->									
									<tr>
										<td align="center" colspan=2>
											<table cellPadding=5>
												<tr>
													<td>
														<html:button  styleClass="dr-menu" property="submitButton" onclick="selectSector()">
															<digi:trn key="btn:add">Add</digi:trn> 
														</html:button>
													</td>
													<td>
														<html:reset  styleClass="dr-menu" property="submitButton">
															<digi:trn key="btn:clear">Clear</digi:trn> 
														</html:reset>
													</td>
													<td>
														 <html:button  styleClass="dr-menu" property="submitButton"  onclick="closeWindow()">
																<digi:trn key="btn:close">Close</digi:trn> 
														 </html:button>
													</td>
												</tr>
											</table>
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

<!--
<br>
<br>
<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border="0">
	<tr><td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left valign="top">
					<table bgcolor=#f4f4f2 cellpadding="0" cellspacing="0" width="100%" class=box-border-nopadding>
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<digi:trn key="aim:searchSectors">
								Search Sectors</digi:trn>
							</td></tr>
						<tr>
							<td align="center" bgcolor=#ECF3FD>
								<table cellSpacing=2 cellPadding=2>
									<tr>
										<td>
											<digi:trn key="aim:enterKeyword">
											Enter a keyword </digi:trn>
										</td>
										<td>
											<html:text property="keyword"  styleClass="inp-text" />
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
											<html:button  styleClass="dr-menu" property="submitButton" onclick="return searchSector()">
												<digi:trn key="btn:search">Search</digi:trn> 
											</html:button>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			</table>
 -->
</digi:form>

