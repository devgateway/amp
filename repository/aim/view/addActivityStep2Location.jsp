<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript" type="text/javascript">

</script>

<digi:instance property="aimEditActivityForm" />
                              <table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699">
                                <tr>
                                  <td bgColor=#f4f4f2 align="center" vAlign="top"><!-- contents -->
                                    <table width="95%" bgcolor="#f4f4f2">
                                      <tr>
                                        <td>
                                          <IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15 />
                                          <b>
                                            <digi:trn key="aim:location">
                                            Location
                                            </digi:trn>
                                          </b>
                                        </td>
                                      </tr>
                                      <tr>
                                        <td>
                                          <digi:trn key="aim:chooseLocation">
                                          Choose the area covered by the project.
                                          </digi:trn>
                                        </td>
                                      </tr>
                                      <tr>
                                        <td>
                                        &nbsp;
                                        </td>
                                      </tr>
                                      <tr>
                                        <td vAlign="center">
                                          <table border=0>
                                            <tr>
                                              <td>
                                                <a title="<digi:trn key="aim:impleLevel">Federal and regional programs are the scope of a project. They are a classification of the sponsorship of the project or program. This works in conjunction with location</digi:trn>">
                                                 <digi:trn key="aim:implementLevel">
                                                 Implementation Level
                                                 </digi:trn>
                                                </a>&nbsp;
                                              </td>
											<td><html:select property="level" styleClass="inp-text">
													<html:option value="-1">Select Level</html:option>
														<logic:iterate name="aimEditActivityForm"
														property="levelCollection" id="ampLevelId">
														<option value="<%=ampLevelId%>"><bean:define
															id="ampLevelName" name="ampLevelId" property="name" />
														<digi:trn key="<%= "aim:" + ampLevelName%>">
														<bean:write name="ampLevelId" property="name" />
														</digi:trn></option>
													</logic:iterate>

												</html:select></td>
                                            </tr>
                                            <tr>
                                              <td vAlign="center" colspan=5>
                                              <digi:trn key="aim:regionZoneWored">Select the appropriate Region, Zone or Woreda as needed.</digi:trn>
                                              </td>
                                            </tr>
                                            <tr>
                                              <td vAlign="center">
                                                <a title="<digi:trn key="aim:impLocation">The regions, zones and woredas in which the project is implemented</digi:trn>">
                                                  <digi:trn key="aim:implementationLoc">
                                                  Implementation Location
                                                  </digi:trn>
                                                </a>&nbsp;
                                              </td>
                                              <td vAlign="center"><br>
												<html:select property="implementationLevel"
													styleClass="inp-text">
													<html:option value="country">
														<digi:trn key="aim:CountryStep2">Country</digi:trn>
													</html:option>
													<html:option value="region">
														<digi:trn key="aim:RegionStep2">Region</digi:trn>
													</html:option>
													<html:option value="zone">
														<digi:trn key="aim:ZoneStep2">Zone</digi:trn>
													</html:option>
													<html:option value="woreda">
														<digi:trn key="aim:DistrictStep2">District </digi:trn>
													</html:option>
												</html:select>
											</td>
                                            </tr>
                                          </table>
                                        </td>
                                      </tr>
                                      <tr>
                                        <td>
                                        &nbsp;
                                        </td>
                                      </tr>
                                      <tr>
                                        <td>
                                          <table cellPadding=5 cellSpacing=1 border=0 width="100%"	bgcolor="#d7eafd">
                                            <tr>
                                              <td align="left">
                                                <b>
                                                  <digi:trn key="aim:location">
                                                  Location
                                                  </digi:trn>
                                                </b>
                                              </td>
                                            </tr>
                                            <tr>
                                              <td bgcolor="#ffffff" width="100%">
                                                <table cellPadding=1 cellSpacing=1 border=0	bgcolor="#ffffff" width="100%">
                                                  <logic:empty name="aimEditActivityForm" property="selectedLocs">
                                                    <tr>
                                                    <td bgcolor="#ffffff"><html:button
														styleClass="buton" property="submitButton"
														onclick="selectLocation()">
														<digi:trn key="btn:addLocation">Add Location</digi:trn>
													</html:button></td>
                                                    </tr>
                                                  </logic:empty>
                                                  <logic:notEmpty name="aimEditActivityForm" property="selectedLocs">
                                                    <tr>
                                                      <td>
                                                        <table cellSpacing=0 cellPadding=0 border=0	bgcolor="#ffffff" width="100%">
                                                          <logic:iterate name="aimEditActivityForm"	property="selectedLocs" id="selectedLocs" type="org.digijava.module.aim.helper.Location">
                                                            <tr>
                                                              <td width="100%">
                                                                <table width="100%" cellSpacing=1 cellPadding=1	vAlign="top" align="left">
                                                                  <tr>
                                                                    <td width="3" vAlign="center">
                                                                    <html:multibox property="selLocs">
                                                                      <bean:write name="selectedLocs" property="locId" />
                                                                    </html:multibox>
                                                                    </td>
                                                                    <td vAlign="center" align="left">
                                                                      <c:if test="${!empty selectedLocs.country}">
                                                                        [<bean:write name="selectedLocs" property="country" />]
                                                                      </c:if>
                                                                      <c:if test="${!empty selectedLocs.region}">
                                                                        [<bean:write name="selectedLocs" property="region"/>]
                                                                      </c:if>
                                                                      <c:if test="${!empty selectedLocs.zone}">
                                                                        [<bean:write name="selectedLocs" property="zone"/>]
                                                                      </c:if>
                                                                      <c:if test="${!empty selectedLocs.woreda}">
                                                                        [<bean:write name="selectedLocs" property="woreda"/>]
                                                                      </c:if>
                                                                    </td>
                                                                  </tr>
                                                                </table>
                                                              </td>
                                                            </tr>
                                                          </logic:iterate>
                                                          <tr>
                                                            <td>
                                                              <table cellSpacing=2 cellPadding=2>
                                                                <tr>
                                                                  <td>
                                                                    <html:button styleClass="buton"
																		property="submitButton"
																		onclick="selectLocation()">
																		<digi:trn key="btn:addLocation">Add Location</digi:trn>
																	</html:button>
                                                                  </td>
                                                                  <td>
                                                                    <html:button styleClass="buton"
																		property="submitButton"
																		onclick="return removeSelLocations()">
																		<digi:trn key="btn:removeLocation">Remove Location</digi:trn>
																	</html:button>
                                                                  </td>
                                                                </tr>
                                                              </table>
                                                            </td>
                                                          </tr>
                                                        </table>
                                                      </td>
                                                    </tr>
                                                  </logic:notEmpty>
                                                </table>
                                              </td>
                                            </tr>
                                          </table>
