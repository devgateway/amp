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
		<digi:context name="selSector" property="context/module/moduleinstance/sectorSelected.do"/>
	    document.aimEditActivityForm.action = "<%= selSector %>";
		 document.aimEditActivityForm.target = window.opener.name;
	    document.aimEditActivityForm.submit();
		window.close();
	}	

	function reloadSector(value) {
		document.aimEditActivityForm.subsectorLevel1.disabled=false;
		document.aimEditActivityForm.subsectorLevel2.disabled=false;
		if (value == 1) {
			document.aimEditActivityForm.sector.value = -1;
		} else if (value == 2) {
			document.aimEditActivityForm.subsectorLevel1.value = -1;
		} else if (value == 3) {
			document.aimEditActivityForm.subsectorLevel2.value = -1;
		}
		<digi:context name="selSector" property="context/module/moduleinstance/selectSectors.do"/>
	    document.aimEditActivityForm.action = "<%= selSector %>";
  		document.aimEditActivityForm.submit();									
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

	function searchSector() {
		var flg=checkEmpty();
			if(flg)
		{
		 <digi:context name="searchSctr" property="context/module/moduleinstance/searchSectors.do"/>
		 document.aimEditActivityForm.action = "<%= searchSctr %>";
		 document.aimEditActivityForm.submit();
		 return true;
		}
	}

	function load() {
		if (window.opener.document.aimEditActivityForm.currUrl.value == "") {
			window.opener.document.aimEditActivityForm.currUrl.value = "/selectSector";
		}
		document.aimEditActivityForm.sectorScheme.focus();			  
	}

	function unload() {
		window.opener.document.aimEditActivityForm.currUrl.value="";
	}

	-->

</script>

<digi:instance property="aimEditActivityForm" />
<digi:form action="/sectorSelected.do" method="post">

<html:hidden property="sectorReset" value="false"/>

<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border=0>
	<tr><td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left vAlign=top>
					<table bgcolor=#f4f4f2 cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<digi:trn key="aim:selectSectors">
								Select Sectors</digi:trn>
							</td></tr>
						<tr>
							<td align="center" bgcolor=#ECF3FD>
								<table cellSpacing=2 cellPadding=2>
									<tr>
										<td>
											<digi:trn key="aim:sectorScheme">
											Sector scheme</digi:trn>
										</td>
										<td>
											<html:select property="sectorScheme" onchange="reloadSector(1)" styleClass="inp-text">
												<html:option value="-1">Select sector scheme</html:option>
												<logic:notEmpty name="aimEditActivityForm" property="sectorSchemes">
													<html:optionsCollection name="aimEditActivityForm" property="sectorSchemes" 
													value="ampSecSchemeId" label="secSchemeName" />												
												</logic:notEmpty>
											</html:select>
										</td>
									</tr>								
									<tr>
										<td>
											<digi:trn key="aim:sector">
											Sector</digi:trn>
										</td>
										<td>
											<html:select property="sector" onchange="reloadSector(2)" styleClass="inp-text">
												<html:option value="-1">Select sector</html:option>
												<logic:notEmpty name="aimEditActivityForm" property="parentSectors">
													<html:optionsCollection name="aimEditActivityForm" property="parentSectors" 
													value="ampSectorId" label="name" />												
												</logic:notEmpty>												
											</html:select>
										</td>
									</tr>								
									<tr>
										<td>
											<digi:trn key="aim:subSectorLevel1">
											Sub-Sector Level 1</digi:trn>
										</td>
										<td>
											<html:select property="subsectorLevel1" onchange="reloadSector(3)" styleClass="inp-text">
												<html:option value="-1">Select sub-sector</html:option>
												<logic:notEmpty name="aimEditActivityForm" property="childSectorsLevel1">
													<html:optionsCollection name="aimEditActivityForm" property="childSectorsLevel1" 
													value="ampSectorId" label="name" />												
												</logic:notEmpty>													
											</html:select>
										</td>
									</tr>
									<tr>
										<td>
											<digi:trn key="aim:subSectorLevel2">
											Sub-Sector Level 2</digi:trn>
										</td>
										<td>
											<html:select property="subsectorLevel2" styleClass="inp-text">
												<html:option value="-1">Select sub-sector</html:option>
												<logic:notEmpty name="aimEditActivityForm" property="childSectorsLevel2">
													<html:optionsCollection name="aimEditActivityForm" property="childSectorsLevel2" 
													value="ampSectorId" label="name" />												
												</logic:notEmpty>													
											</html:select>
										</td>
									</tr>									
									<tr>
										<td align="center" colspan=2>
											<table cellPadding=5>
												<tr>
													<td>
														<input type="button" value="Add" class="dr-menu" onclick="selectSector()">
													</td>
													<td>
														<input type="reset" value="Clear" class="dr-menu">
													</td>
													<td>
														<input type="button" value="Close" class="dr-menu" 
																	 onclick="javascript:window.close()">
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
								<digi:trn key="aim:searchSectors">
								Search Sectors</digi:trn>
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
											<input type="button" value="Search" class="dr-menu" onclick="return searchSector()">
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

