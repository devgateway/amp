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
		<digi:context name="selSector" property="context/module/moduleinstance/sectorActions.do?actionType=addsectorToindicator"/>
	    document.aimIndicatorForm.action = "<%= selSector %>";
		 //document.aimIndicatorForm.target = window.opener.name;
	    document.aimIndicatorForm.submit();
            window.opener.location.reload(true);
            window.opener.focus();
		window.close();
		
	}	
	
	function reloadSector(value) {		
		if (value == 1) {
			document.aimIndicatorForm.sector.value = -1;
		}	
		<digi:context name="selSector" property="context/module/moduleinstance/sectorActions.do?actionType=loadSectors"/>
	    document.aimIndicatorForm.action = "<%= selSector %>";
  		document.aimIndicatorForm.submit();									
	}	
	
	
	function closeWindow() {
		window.close();
	}

	-->

</script>

<digi:instance property="aimIndicatorForm" />
<digi:form action="/sectorActions.do" method="post">
<html:hidden property="sectorReset" value="false"/>

<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border="0">
	<tr><td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left valign="top">
					<table bgcolor=#f4f4f2 cellpadding="0" cellspacing="0" width="100%" class=box-border-nopadding>
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
												<logic:notEmpty name="aimIndicatorForm" property="sectorSchemes">
													<html:optionsCollection name="aimIndicatorForm" property="sectorSchemes" value="ampSecSchemeId" label="secSchemeName" />												
												</logic:notEmpty>
											</html:select>
										</td>
									</tr>								
									<tr>
										<td>
											<digi:trn key="aim:sector">	Sector</digi:trn>
										</td>
										<td>
											<html:select property="sector" onchange="reloadSector(2)" styleClass="inp-text">
												<html:option value="-1">Select sector</html:option>
												<logic:notEmpty name="aimIndicatorForm" property="allSectors">
													<html:optionsCollection name="aimIndicatorForm" property="allSectors"value="ampSectorId" label="name" />												
												</logic:notEmpty>												
											</html:select>
										</td>
									</tr>
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
</digi:form>

