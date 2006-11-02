<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript">

	function addLoc() {
	  level = document.aimAddLocationForm.edLevel.value;
	  if ((document.aimAddLocationForm.name.value.length==0) ||
   				(document.aimAddLocationForm.name.value==null)) {
   			alert('Please enter name for this location.');
      		document.aimAddLocationForm.name.focus();
      		return false;
      }
      if (level == 'country') {
      	if ((document.aimAddLocationForm.iso.value.length==0) ||
   				(document.aimAddLocationForm.iso.value==null)) {
   			alert('Please enter iso code for this country');
      		document.aimAddLocationForm.iso.focus();
      		return false;
      	}
      	if ((document.aimAddLocationForm.iso3.value.length==0) ||
   				(document.aimAddLocationForm.iso3.value==null)) {
   			alert('Please enter iso3 code for this country');
      		document.aimAddLocationForm.iso3.focus();
      		return false;
      	}
      }
      else {
      	if (isNaN(document.aimAddLocationForm.gsLat.value)) {
      		alert('Please enter only numerical values into Latitude Field.');
      		document.aimAddLocationForm.gsLat.focus();
      		return false;
      	}
      	if (isNaN(document.aimAddLocationForm.gsLong.value)) {
      		alert('Please enter only numerical values into Longitude Field.');
      		document.aimAddLocationForm.gsLong.focus();
      		return false;
      	}
      }
	  document.aimAddLocationForm.edFlag.value = "off";
	  document.aimAddLocationForm.submit();
	}

	function textCounter(field, countfield, maxlimit) {
		if (field.value.length > maxlimit) // if too long...trim it!
			field.value = field.value.substring(0, maxlimit);
			// otherwise, update 'characters left' counter
		else
			countfield.value = maxlimit - field.value.length;
	}
	
	function getIso(code) {
		if (code == 2)
			window.open("http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2");
		else if (code == 3)
			window.open("http://en.wikipedia.org/wiki/ISO_3166-1_alpha-3");
		else
			return false;
	}
	
	function move() {
		<digi:context name="selectLoc" property="context/module/moduleinstance/locationManager.do" />
		url = "<%= selectLoc %>";
		document.aimAddLocationForm.edFlag.value = "cancl";
		document.aimAddLocationForm.action = "<%= selectLoc %>";
		document.aimAddLocationForm.submit();
	}
	
	function load() {
		document.aimAddLocationForm.name.focus();			  
	}

	function unload() {
	}
	
</script>

<html:javascript formName="aimAddLocationForm"/>

<!--  AMP Admin Logo -->
	<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<digi:context name="digiContext" property="context"/>
<digi:instance property="aimAddLocationForm" />

<digi:form action="/addLocation.do" method="post">
<html:hidden property="edFlag" />
<html:hidden property="edLevel" />
<html:hidden property="start" value="false" />

	<table cellPadding=5 cellSpacing=0 width="600">
		<tr>
			<!-- Start Navigation -->
			<td height=33 width="600"><span class=crumb>
				<bean:define id="translation">
					<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
				</bean:define>
				<digi:link href="/admin.do" styleClass="comment" title="<%=translation%>" >
					<digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
				</digi:link>&nbsp;&gt;&nbsp;
				<bean:define id="translation">
					<digi:trn key="aim:clickToViewRegionManager">Click here to goto Region Manager</digi:trn>
				</bean:define>
				<digi:link href="/locationManager.do" styleClass="comment" title="<%=translation%>" >
					<digi:trn key="aim:regionManager">Region Manager</digi:trn>
				</digi:link>&nbsp;&gt;&nbsp;
				<c:if test="${aimAddLocationForm.edAction == 'create'}">
					<digi:trn key="aim:AmpAddRegion">Add Region</digi:trn>
				</c:if>
				<c:if test="${aimAddLocationForm.edAction == 'edit'}">
					<digi:trn key="aim:AmpEditRegion">Edit Region</digi:trn>
				</c:if>
			</td>
			<!-- End navigation -->
		</tr>
		<tr>
			<td height=16 vAlign=center width=600 ><span class=subtitle-blue> 
				<digi:trn key="aim:regionManager">Region Manager</digi:trn>
            </td>
		</tr>
		<tr>
			<td noWrap width=600 vAlign="top">
				<table width=600 cellspacing=1 cellSpacing=1>
					<tr><td noWrap width=600 vAlign="top">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
							<tr bgColor=#f4f4f2>
								<td vAlign="top" width="100%">
									&nbsp;
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
									<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="90%" border=0>	
										<tr>
											<td bgColor=#ffffff class=box-border>
												<table border=0 cellPadding=1 cellSpacing=1 class=box-border width="100%">
													<!-- Page Logic -->
													<tr>
														<td><digi:errors /></td>
													</tr>
													<tr>
														<td>
															<table width="100%" border="0" cellspacing="4">
																<tr>
																	<td width="30%" align="right">Name</td>
																	<td width="70%" align="left">
																		<html:text property="name" size="25"
																			onkeydown="textCounter(this.form.name,this.form.cname,200);" 
																			onkeyup="textCounter(this.form.name,this.form.cname,200);" />
																		<input readonly type="text" name="cname" size="3" maxlength="4" value="">
																		<digi:trn key="aim:AmpAddLocCharleft">characters left</digi:trn>
                                   									</td>
                                								</tr>
                                							<c:choose>
                                								<c:when test="${aimAddLocationForm.edLevel == 'country'}">
                                									<tr>
																		<td width="30%" align="right">ISO</td>
																		<td width="70%" align="left">
																			<html:text property="iso" size="5" maxlength="2" />
																			<br>
																			<a href="javascript:getIso(2)">
																			<digi:trn key="aim:isoCodeList">ISO code list</digi:trn></a>
																		</td>
                                									</tr>
                                									<tr>
																		<td width="30%" align="right">ISO3</td>
																		<td width="70%" align="left">
																			<html:text property="iso3" size="5" maxlength="3" />
																			<br>
																			<a href="javascript:getIso(3)">
																			<digi:trn key="aim:iso3CodeList">ISO3 code list</digi:trn></a>
																		</td>
                                									</tr>
                                								</c:when>
                                								<c:otherwise>
                                									<tr>
																		<td width="30%" align="right">Code</td>
																		<td width="70%" align="left">
																			<html:text property="code" size="10" />
																		</td>
                                									</tr>
																	<tr>
																		<td width="30%" align="right">Latitude</td>
																		<td width="70%" align="left">
																			<html:text property="gsLat" size="10" />
																		</td>
	                                								</tr>
																	<tr>
																		<td width="30%" align="right">Longitude</td>
																		<td width="70%" align="left">
																			<html:text property="gsLong" size="10" />
																		</td>
	                               									</tr>
																	<tr>
																		<td width="30%" align="right">Geo Code</td>
																		<td width="70%" align="left">
																			<html:text property="geoCode" size="10" />
																		</td>
	                                								</tr>
	                                								<tr>
																		<td width="30%" align="right">Description</td>
																		<td width="70%" align="left">
																			<html:textarea property="description" cols="40" rows="3" />
																		</td>
										 							</tr>
                                								</c:otherwise>
                                							</c:choose>
																
																<tr>
																	<td colspan="2" width="60%">	
																		<table width="100%" cellspacing="5">
																			<tr>
																				<td width="45%" align="right">
																					<input type="button" value="Save" class="dr-menu" onclick="addLoc()"></td>
																				<td width="8%" align="left">
																					<input type="reset" value="Reset" class="dr-menu"></td>
																				<td width="45%" align="left">
																					<input type="button" value="Cancel" class="dr-menu" onclick="move()"></td>
																			</tr>
																		</table>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<!-- end page logic -->
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td bgColor=#f4f4f2>
								&nbsp;
							</td></tr>
						</table>
					</td>
                    </tr>
		</table>
			</td>
		</tr>
	</table>
</digi:form>