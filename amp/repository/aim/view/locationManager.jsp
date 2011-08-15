<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<c:set var="translationDeletePopup">
	<digi:trn key="aim:deleteLocationPopup">Are you sure about deleting this</digi:trn>
</c:set>

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
		url = "<%= selectLoc %>?categoryLevel=" + val1 + "&edAction=" + val2;
		document.aimAddLocationForm.action = url;
		document.aimAddLocationForm.currUrl.value = "<%= selectLoc %>";
		document.aimAddLocationForm.submit();
	}

	function delet(val1, val2, val3) {
		<digi:context name="selectLoc" property="context/module/moduleinstance/addLocation.do" />
		url = "<%= selectLoc %>?categoryLevel=" + val1 + "&edAction=" + val2;
			if (confirm("${translationDeletePopup} " + val3 + " ?")) {
				document.aimAddLocationForm.action = url;
				document.aimAddLocationForm.target = "_self";
				document.aimAddLocationForm.submit();
			}
	}

function delet1(val1, val2) {
		<digi:context name="selectLoc" property="context/module/moduleinstance/addLocation.do" />
		url = "<%= selectLoc %>?edLevel=" + val1 + "&edAction=" + val2;
			if (confirm("Are you sure about deleting this district ?")) {
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
    <c:set var="size">
        ${aimAddLocationForm.categoryValuesSize}
    </c:set>

<!--  AMP Admin Logo -->
	<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->

<!--<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=636>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg valign="top" width=750>-->
			<table cellPadding=5 cellspacing="0" width="912">
				<tr>
					<!-- Start Navigation -->
					<td height=33 width="900"><span class=crumb>
						<c:set var="clickToViewAdmin">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${clickToViewAdmin}">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:regionManager"> Region Manager
						</digi:trn>
                      </span>
                      <div class="adminicon"><img src="/TEMPLATE/ampTemplate/img_2/adminicons/regionmanager.jpg"/></div>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 valign="center" width=1157 ><span class=subtitle-blue>
						<digi:trn key="aim:regionManager">Region Manager</digi:trn>
                      </span>
					</td>
				</tr>
				<tr>
					<td noWrap width=900 vAlign="top">
					<table width="965" cellspacing="1" cellspacing="1">
					<tr>
						<td noWrap width=663 vAlign="top">
							<!--<table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="773">
								<!--<tr bgColor=#f4f4f2>
									<td vAlign="top" width="771">
										&nbsp;
									</td>
								</tr>
								<tr bgColor=#f4f4f2>
									<td valign="top" width="771">-->
										<table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="626" border="0">
											<tr>
												<td bgColor=#ffffff class=box-border width="624">
													<table border="0" cellpadding="1" cellspacing="1" class=box-border width="784">
														<tr bgColor=#dddddb>
															<!-- header -->
															<td bgColor=#ffffff height="20" align="center" colspan="2" width="776"><B>

															</td>
															<!-- end header -->
														</tr>
													<!-- Page Logic -->
                                                                                                    <c:if test = "${size>0}">
													<logic:greaterEqual name="aimAddLocationForm" property="impLevelValue" value="1">
														<tr>
															<td width="193" align="right" valign="top" height="19">
																<c:set var="countryNormal">
																	<category:getoptionvalue categoryKey="<%= org.digijava.module.categorymanager.util.CategoryConstants.IMPLEMENTATION_LOCATION_KEY%>" categoryIndex="0"/>
																</c:set>
																${countryNormal}
															</td>
															<td  align="left" width="539" valign="top" height="19">
																<html:select property="countryId" onchange="countryChanged()"	>
																	<html:option value="Select">
																		--
																		<digi:trn key="aim:AmpSelectLocation">
																			Select
																		</digi:trn>
																		${countryNormal}
																		 --
																	</html:option>
                                                                    <c:forEach var="cn" items="${aimAddLocationForm.country}">
                                                                      <html:option value="${cn.iso}">${cn.name}</html:option>
                                                                    </c:forEach>
																</html:select>
																<br>
                                                                                                    
																<c:set var="country">
																	<category:getoptionvalue lowerCase="true" categoryKey="<%= org.digijava.module.categorymanager.util.CategoryConstants.IMPLEMENTATION_LOCATION_KEY%>" categoryIndex="0"/>
																</c:set>
																<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10" />
																	<a href="javascript:newWin(0,'create')">
																		<digi:trn key="aim:AmpAddALocation">Add a</digi:trn>
																		 ${country}
																	</a>
                                                                                                                                         
																<%--
																<logic:notEqual name="aimAddLocationForm" property="countryId" value="">
																	<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10" />
																		<a href="javascript:newWin('country','edit')">
																		<digi:trn key="aim:AmpEditCountry">Edit this country</digi:trn></a>
																</logic:notEqual>
																--%>
                                                                                                                                 <c:if test="${aimAddLocationForm.countryFlag}">
																			<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
																			<a href="javascript:delet(0,'delete', '${countryId}' )">
																				<digi:trn key="aim:AmpDeleteThisLocation">Delete this</digi:trn>
																				${country}
																			</a>
                                                                                                                                   </c:if>
															</td>
														</tr>
													</logic:greaterEqual>
                                                                                                         </c:if>
                                                                                                    <c:if test = "${size>1}">
													<logic:greaterEqual name="aimAddLocationForm" property="impLevelValue" value="1">
														<tr>
															<td  width="193" align="right" valign="top" height="19">
																<c:set var="regionNormal">
																	<category:getoptionvalue categoryKey="<%= org.digijava.module.categorymanager.util.CategoryConstants.IMPLEMENTATION_LOCATION_KEY%>" categoryIndex="1"/>
																</c:set>
																${regionNormal}
															</td>
															<td  align="left" width="539" valign="top" height="19">
																<html:select property="regionId"  onchange="regionChanged()">
																	<html:option value="-1">
																		--
																		<digi:trn key="aim:AmpSelectLocation">
																			Select
																		</digi:trn>
																		${regionNormal}
																		 --
																	</html:option>
																		<logic:notEmpty name="aimAddLocationForm" property="region">
																			<html:optionsCollection name="aimAddLocationForm" property="region"
																							value="ampRegionId" label="name" />
																		</logic:notEmpty>
																</html:select>
																<br>
																<logic:notEqual name="aimAddLocationForm" property ="countryId" value ="">
																	<c:set var="region">
																		<category:getoptionvalue lowerCase="true" categoryKey="<%= org.digijava.module.categorymanager.util.CategoryConstants.IMPLEMENTATION_LOCATION_KEY%>" categoryIndex="1"/>
																	</c:set>
																	<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
																	<a href="javascript:newWin(1,'create')">
																		<digi:trn key="aim:AmpAddALocation">Add a</digi:trn>
																		 ${region}
																	</a>
																</logic:notEqual>
																	<logic:notEqual name="aimAddLocationForm" property="regionId" value="-1">
																		<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
																			<a href="javascript:newWin(1,'edit')">
																				<digi:trn key="aim:AmpEditThisLocation">Edit this</digi:trn>
																				 ${region}
																			</a>
																		<logic:equal name="aimAddLocationForm" property="regionFlag" value="yes">
																			<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
																			<a href="javascript:delet(1,'delete', '${region}' )">
																				<digi:trn key="aim:AmpDeleteThisLocation">Delete this</digi:trn>
																				 ${region }
																			</a>

																		</logic:equal>
												                    </logic:notEqual>
															</td>
														</tr>
													</logic:greaterEqual>
                                                                                                         </c:if>
                                                                                                         <c:if test = "${size>2}">
													<logic:greaterEqual name="aimAddLocationForm" property="impLevelValue" value="1">
														<tr>
															<td  width="193" align="right" valign="top" height="19">
																<c:set var="zoneNormal">
																	<category:getoptionvalue categoryKey="<%= org.digijava.module.categorymanager.util.CategoryConstants.IMPLEMENTATION_LOCATION_KEY%>" categoryIndex="2"/>
																</c:set>
																${zoneNormal}
															</td>
															<td width="539" height="19">
																<html:select property="zoneId" onchange="zoneChanged()">
																	<html:option value="-1">
																		--
																		<digi:trn key="aim:AmpSelectLocation">
																			Select
																		</digi:trn>
																		${zoneNormal}
																		 --
																	</html:option>
																		<logic:notEmpty name="aimAddLocationForm" property="zone">
																			<html:optionsCollection name="aimAddLocationForm" property="zone"
																					value="ampZoneId" label="name" />
																		</logic:notEmpty>
																</html:select>
																<br>
																<!--here -->
															<logic:notEqual name="aimAddLocationForm" property="regionId"  value="-1">
																	<c:set var="zone">
																		<category:getoptionvalue lowerCase="true" categoryKey="<%= org.digijava.module.categorymanager.util.CategoryConstants.IMPLEMENTATION_LOCATION_KEY%>" categoryIndex="2"/>
																	</c:set>
																	<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
																	<a href="javascript:newWin(2,'create')">
																		<digi:trn key="aim:AmpAddALocation">Add a</digi:trn>
																		 ${zone}
																	</a>
																</logic:notEqual>
																	<logic:notEqual name="aimAddLocationForm" property="zoneId"  value="-1">
																		<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
																			<a href="javascript:newWin(2,'edit')">
																				<digi:trn key="aim:AmpEditThisLocation">Edit this</digi:trn>
																				 ${zone}
																			</a>
																		<logic:equal name="aimAddLocationForm" property="zoneFlag" value="yes">
																			<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
																				<a href="javascript:delet(2,'delete', '${zone}')">
																					<digi:trn key="aim:AmpDeleteThisLocation">Delete this</digi:trn>
																					 ${zone }
																				</a>
																		</logic:equal>
												                	</logic:notEqual>
															</td>
														</tr>
													</logic:greaterEqual>
                                                                                                         </c:if>
                                                                                           
                                                                                                         <c:if test = "${size>3}">
													<logic:greaterEqual name="aimAddLocationForm" property="impLevelValue" value="1">
														<tr>
															<td  width="193" align="right" valign="top" height="19">
																<c:set var="districtNormal">
																	<category:getoptionvalue categoryKey="<%= org.digijava.module.categorymanager.util.CategoryConstants.IMPLEMENTATION_LOCATION_KEY%>" categoryIndex="3"/>
																</c:set>
																${districtNormal}
															</td>
															<td width="539" height="19">
																<html:select property="woredaId" onchange="woredaChanged()">
																	<html:option value="-1">
																		--
																		<digi:trn key="aim:AmpSelectLocation">
																			Select
																		</digi:trn>
																		${districtNormal}
																		 --
																	</html:option>
																		<logic:notEmpty name="aimAddLocationForm" property="woreda">
																			<html:optionsCollection name="aimAddLocationForm" property="woreda"
																				value="ampWoredaId" label="name" />
																		</logic:notEmpty>
																</html:select>
																<br>
																<logic:notEqual name ="aimAddLocationForm"
																property="zoneId" value="-1">
																	<c:set var="district">
																		<category:getoptionvalue lowerCase="true" categoryKey="<%= org.digijava.module.categorymanager.util.CategoryConstants.IMPLEMENTATION_LOCATION_KEY%>" categoryIndex="3"/>
																	</c:set>
																	<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
																	<a href="javascript:newWin(3,'create')">
																		<digi:trn key="aim:AmpAddALocation">Add a</digi:trn>
																		 ${district}
																	</a>
                                                                 </logic:notEqual>
																	<logic:notEqual name="aimAddLocationForm" property="woredaId"  value="-1">
																		<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
																			<a href="javascript:newWin(3,'edit')">
																				<digi:trn key="aim:AmpEditThisLocation">Edit this</digi:trn>
																				 ${district }
																			</a>
																		<logic:equal name="aimAddLocationForm" property="woredaFlag"  value="yes">
																			<digi:img src="module/aim/images/arrow-014E86.gif" 	width="15" height="10"/>
																				<a href="javascript:delet( 3, 'delete', '${district}' )">
																					<digi:trn key="aim:AmpDeleteThisLocation">Delete this</digi:trn>
																					 ${district}
																				</a>
																		</logic:equal>
																	</logic:notEqual>
															</td>
														</tr>
													</logic:greaterEqual>
                                                                                                         </c:if>

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
