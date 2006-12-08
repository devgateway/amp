<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript">
	
	function openLocWindow(wndWidth, wndHeight){
	window.name = "opener" + new Date().getTime();
	if (wndWidth == null || wndWidth == 0 || wndHeight == null || wndHeight == 0) {
		wndWidth = window.screen.availWidth/2;
		wndHeight = window.screen.availHeight/2;
	}
	popupPointer = window.open("about:blank", "forumPopup", "height=" + wndHeight + ",width=" + wndWidth + ",menubar=no,scrollbars=no");
	}
	
	function newWin(val1, val2) {
		<digi:context name="selectLoc" property="context/module/moduleinstance/addLocation.do" />
		url = "<%= selectLoc %>?edLevel=" + val1 + "&edAction=" + val2;
		document.aimAddLocationForm.action = url;
		document.aimAddLocationForm.currUrl.value = "<%= selectLoc %>";
		document.aimAddLocationForm.submit();
	}
	
	function delet(val1, val2) {
		<digi:context name="selectLoc" property="context/module/moduleinstance/addLocation.do" />
		url = "<%= selectLoc %>?edLevel=" + val1 + "&edAction=" + val2;
			if (confirm("Are you sure about deleting this " + val1 + " ?")) {
				document.aimAddLocationForm.action = url;
				document.aimAddLocationForm.target = "_self";
				document.aimAddLocationForm.submit();
			}
	}
	
	function countryChanged() {
		  document.aimAddLocationForm.level.value = "region";
		  <digi:context name="selectLoc" property="context/module/moduleinstance/locationManager.do" />
		  document.aimAddLocationForm.action = "<%= selectLoc %>";
		  document.aimAddLocationForm.target = "_self";
		  document.aimAddLocationForm.submit();
	}

	function regionChanged() {
		  document.aimAddLocationForm.level.value = "zone";			  
		  <digi:context name="selectLoc" property="context/module/moduleinstance/locationManager.do" />
		  document.aimAddLocationForm.action = "<%= selectLoc %>";
		  document.aimAddLocationForm.target = "_self";
		  document.aimAddLocationForm.submit();
	}

	function zoneChanged() {
		  document.aimAddLocationForm.level.value = "woreda";			  
		  <digi:context name="selectLoc" property="context/module/moduleinstance/locationManager.do" />
		  document.aimAddLocationForm.action = "<%= selectLoc %>";
		  document.aimAddLocationForm.target = "_self";
		  document.aimAddLocationForm.submit();
	}
	
	function woredaChanged() {
		  document.aimAddLocationForm.level.value = "nextworeda";			  
		  <digi:context name="selectLoc" property="context/module/moduleinstance/locationManager.do" />
		  document.aimAddLocationForm.action = "<%= selectLoc %>";
		  document.aimAddLocationForm.target = "_self";
		  document.aimAddLocationForm.submit();
	}

</script>

<digi:errors/>
<digi:context name="digiContext" property="context"/>
<digi:instance property="aimAddLocationForm" />

<digi:form action="/locationManager.do"  method="post">

<html:hidden property="level" />
<html:hidden property="start" value="false" />
<input type="hidden" name="currUrl" value="">

<!--  AMP Admin Logo -->
	<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<!--<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=636>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>-->
			<table cellPadding=5 cellSpacing=0 width="912">
				<tr>
					<!-- Start Navigation -->
					<td height=33 width="900"><span class=crumb>
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</bean:define>
						<digi:link href="/admin.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:regionManager"> Region Manager
						</digi:trn>
                      </span>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=1157 ><span class=subtitle-blue> 
						<digi:trn key="aim:regionManager">Region Manager</digi:trn>
                      </span>
					</td>
				</tr>
				<tr>
					<td noWrap width=900 vAlign="top">
					<table width="965" cellspacing=1 cellSpacing=1>
					<tr>
						<td noWrap width=663 vAlign="top">
							<!--<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="773">
								<!--<tr bgColor=#f4f4f2>
									<td vAlign="top" width="771">
										&nbsp;
									</td>
								</tr>
								<tr bgColor=#f4f4f2>
									<td valign="top" width="771">-->
										<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="626" border=0>	
											<tr>
												<td bgColor=#ffffff class=box-border width="624">
													<table border=0 cellPadding=1 cellSpacing=1 class=box-border width="784">
														<tr bgColor=#dddddb>
															<!-- header -->
															<td bgColor=#ffffff height="20" align="center" colspan="2" width="776"><B>
																
															</td>
															<!-- end header -->
														</tr>
													<!-- Page Logic -->
													
													<logic:greaterEqual name="aimAddLocationForm" property="impLevelValue" value="1">
														<tr>
															<td width="193" align="right" valign="top" height="19">
																<digi:trn key="aim:AmpCountry">Country</digi:trn>
															</td>
															<td  align="left" width="539" valign="top" height="19">
																<html:select property="countryId" onchange="countryChanged()"	>
																	<html:option value="">-- Select Country --</html:option>
																	<html:optionsCollection name="aimAddLocationForm" property="country" 
																		   value="iso" label="countryName" />
																</html:select>
																<br>
																<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10" />
																	<a href="javascript:newWin('country','create')">
																	<digi:trn key="aim:AmpAddCountry">Add a country</digi:trn></a>
																<%--
																<logic:notEqual name="aimAddLocationForm" property="countryId" value="">
																	<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10" />
																		<a href="javascript:newWin('country','edit')">
																		<digi:trn key="aim:AmpEditCountry">Edit this country</digi:trn></a>
																</logic:notEqual>
																--%>
															</td>
														</tr>
													</logic:greaterEqual>	
														
													<logic:greaterEqual name="aimAddLocationForm" property="impLevelValue" value="1">
														<tr>
															<td  width="193" align="right" valign="top" height="19">
																<digi:trn key="aim:AmpRegion">Region</digi:trn>
															</td>
															<td  align="left" width="539" valign="top" height="19">
																<html:select property="regionId"  onchange="regionChanged()">
																	<html:option value="-1">-- Select Region --</html:option>
																		<logic:notEmpty name="aimAddLocationForm" property="region">
																			<html:optionsCollection name="aimAddLocationForm" property="region" 
																							value="ampRegionId" label="name" />
																		</logic:notEmpty>
																</html:select>
																<br>
																<logic:notEqual name="aimAddLocationForm" property ="countryId" value ="">
																	<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
																	<a href="javascript:newWin('region','create')">
																		<digi:trn key="aim:AmpAddRegion">Add a region</digi:trn></a>
																</logic:notEqual>
																	<logic:notEqual name="aimAddLocationForm" property="regionId" value="-1">
																		<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
																			<a href="javascript:newWin('region','edit')">
																				<digi:trn key="aim:AmpEditRegion">Edit this region</digi:trn></a>
																		<logic:equal name="aimAddLocationForm" property="regionFlag" value="yes">
																			<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
																			<a href="javascript:delet('region','delete')">
																				<digi:trn key="aim:AmpDeleteRegion">Delete this region</digi:trn></a>
																		</logic:equal>
												                    </logic:notEqual>
															</td>
														</tr>
													</logic:greaterEqual>														
																											
													<logic:greaterEqual name="aimAddLocationForm" property="impLevelValue" value="1">
														<tr>
															<td  width="193" align="right" valign="top" height="19">
																<digi:trn key="aim:AmpZone">Zone</digi:trn>
															</td>
															<td width="539" height="19">
																<html:select property="zoneId" onchange="zoneChanged()">
																	<html:option value="-1">-- Select Zone --</html:option>
																		<logic:notEmpty name="aimAddLocationForm" property="zone">
																			<html:optionsCollection name="aimAddLocationForm" property="zone" 
																					value="ampZoneId" label="name" />
																		</logic:notEmpty>
																</html:select>
																<br>
																<!--here -->
															<logic:notEqual name="aimAddLocationForm" 			property="regionId"  value="-1">
																	<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
																	<a href="javascript:newWin('zone','create')">
																		<digi:trn key="aim:AmpAddZone">Add a zone </digi:trn>
																	</a>
																</logic:notEqual>
																	<logic:notEqual name="aimAddLocationForm" property="zoneId"  value="-1">
																		<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
																			<a href="javascript:newWin('zone','edit')">
																				<digi:trn key="aim:AmpEditZone">Edit this zone</digi:trn>
																			</a>
																		<logic:equal name="aimAddLocationForm" property="zoneFlag" value="yes">
																			<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
																				<a href="javascript:delet('zone','delete')">
																					<digi:trn key="aim:AmpDeleteZone">Delete this zone</digi:trn>
																				</a>
																		</logic:equal>
												                	</logic:notEqual>
															</td>
														</tr>
													</logic:greaterEqual>
	
													<logic:greaterEqual name="aimAddLocationForm" property="impLevelValue" value="1">
														<tr>
															<td  width="193" align="right" valign="top" height="19">
																<digi:trn key="aim:AmpWoreda">Woreda</digi:trn>
															</td>
															<td width="539" height="19">
																<html:select property="woredaId" onchange="woredaChanged()">
																	<html:option value="-1">-- Select Woreda --</html:option>
																		<logic:notEmpty name="aimAddLocationForm" property="woreda">
																			<html:optionsCollection name="aimAddLocationForm" property="woreda" 
																				value="ampWoredaId" label="name" />
																		</logic:notEmpty>
																</html:select>
																<br>
																<logic:notEqual name ="aimAddLocationForm"
																property="zoneId" value="-1">

																	<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
																	<a href="javascript:newWin('woreda','create')">
																		<digi:trn key="aim:AmpAddWoreda">Add a woreda</digi:trn>
																	</a>
                                                                 </logic:notEqual>
																	<logic:notEqual name="aimAddLocationForm" property="woredaId"  value="-1">
																		<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
																			<a href="javascript:newWin('woreda','edit')">
																				<digi:trn key="aim:AmpEditWoreda">Edit this woreda</digi:trn>
																			</a>
																		<logic:equal name="aimAddLocationForm" property="woredaFlag"  value="yes">
																			<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
																				<a href="javascript:delet('woreda','delete')">
																					<digi:trn key="aim:AmpDeleteWoreda">Delete this woreda</digi:trn>
																				</a>
																		</logic:equal>
																	</logic:notEqual>
															</td>
														</tr>
													</logic:greaterEqual>						

												<!-- end page logic -->
													</table>
												</td>
											</tr>
											
											<tr>
												<td colspan="4" width="674">
													
												</td>
											</tr>
											
										</table>
									</td>
								</tr>
								<tr>
									<!--<td bgColor=#f4f4f2 width="771">
										&nbsp;
									</td>-->
								</tr>
						<!--	</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>-->
	</table>
	</td>
	</tr>
</table>
</digi:form>
