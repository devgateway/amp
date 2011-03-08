<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript" type="text/javascript">
	var addLocationButton	= new ButtonWrapper('add_location_button');
	function updateAddLocationButton() {
		var locationLevelSelect = document.getElementsByName("location.implemLocationLevel")[0];
		if ( locationLevelSelect.selectedIndex > 0 ) {
			addLocationButton.enable();
		}
		else
			addLocationButton.disable();
	}
	YAHOOAmp.util.Event.on(window, "load", updateAddLocationButton);
</script>

<digi:instance property="aimEditActivityForm" />
<bean:define id="location" name="aimEditActivityForm" property="location"></bean:define>
                              <table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#f4f4f2">
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
                                        <td>&nbsp;
                                        
                                        </td>
                                      </tr>
                                      <tr>
                                        <td vAlign="center">
                                          <table width="100%" cellspacing="1" cellpadding="5" bgcolor="#ffffff">
                                          <field:display name="Implementation Level" feature="Location">
                                            <tr>
                                              <td>
                                                <a title="<digi:trn key="aim:impleLevel">Federal and regional programs are the scope of a project. They are a classification of the sponsorship of the project or program. This works in conjunction with location</digi:trn>">
                                                 <digi:trn key="aim:implementLevel">
                                                 Implementation Level
                                                 </digi:trn>
                                                </a>&nbsp;
                                              </td>
												<td>
													<c:set var="translation">
														<digi:trn key="aim:addActivityImplLevelFirstLine">Please select from below</digi:trn>
													</c:set>
													<category:showoptions multiselect="false" firstLine="${translation}" name="aimEditActivityForm" property="location.levelId" keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.IMPLEMENTATION_LEVEL_KEY %>" styleClass="inp-text" />
	                                              	<script language="Javascript">                                             	
														var locationLevelSelect = document.getElementsByName("location.levelId")[0];
														locationLevelSelect.onchange=function() {
															locationLevelChanged();
	                                              		}     	
                                              		</script>
												</td>
                                            </tr>
                                            </field:display>
                                            
                                            <field:display name="Implementation Location" feature="Location">
                                            <tr>
                                              <td vAlign="center" colspan=5>
                                              <digi:trn key="aim:regionZoneWored"> Select the appropriate Region, Zone or Woreda as needed.</digi:trn>
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
                                              <td vAlign="center">
	                                              		<c:set var="translation">
															<digi:trn key="aim:addActivityImplLevelFirstLine">Please select from below</digi:trn>
														</c:set>					
														<category:showoptions multiselect="false" firstLine="${translation}" name="aimEditActivityForm" property="location.implemLocationLevel" tag="${aimEditActivityForm.location.levelId}" keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.IMPLEMENTATION_LOCATION_KEY %>" styleClass="inp-text" />
													                                             													                                             	                                              	
		                                              	<script language="Javascript">                                             	
															var implemLocationLevelSelect = document.getElementsByName("location.implemLocationLevel")[0];
															if(implemLocationLevelSelect!=null){
			                                              		implemLocationLevelSelect.onchange=function() {
			                                              			//removeAllLocations();
			                                              			updateAddLocationButton();
			                                              		}
															}
	                                              		</script>
											</td>
                                            </tr>
                                            </field:display>
                                          </table>
                                        </td>
                                      </tr>
                                      <tr>
                                        <td>&nbsp;
                                        
                                        </td>
                                      </tr>
                                      
                                      <tr>
                                        <td>
                                          <table cellPadding=5 cellspacing="1" border="0" width="100%"	bgcolor="#d7eafd">
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
                                                <table cellpadding="1" cellspacing="1" border="0"	bgcolor="#ffffff" width="100%">
                                                  <logic:empty name="aimEditActivityForm" property="location.selectedLocs">
                                                  <field:display name="Add Location" feature="Location">
                                                    <tr>
                                                    <td bgcolor="#ffffff"><html:button
														styleClass="dr-menu" property="submitButton" disabled="true" style="color: lightgray" styleId="add_location_button"
														onclick="selectLocation()">
														<digi:trn key="btn:addLocation">Add Location</digi:trn>
													</html:button></td>
                                                    </tr>
                                                    </field:display>
                                                  </logic:empty>
                                                  <logic:notEmpty name="aimEditActivityForm" property="location.selectedLocs">
                                                    <tr>
                                                      <td>
                                                        <table cellSpacing="0" cellPadding="0" border="0"	bgcolor="#ffffff" width="100%">
                                                        <c:forEach var="selectedLocs" items="${aimEditActivityForm.location.selectedLocs}">
                                                          <tr>
                                                              <td width="100%">
                                                                <table width="100%" cellSpacing="1" cellPadding="1"	vAlign="top" align="left">
                                                                  <tr><field:display name="Region" feature="Location">
                                                                    <td width="3" vAlign="center">
                                                                    <html:multibox property="location.selLocs" styleId="selLocs">
                                                                      <bean:write name="selectedLocs" property="locId" />
                                                                    </html:multibox>
                                                                    </td>
                                                                    <td>
                                                                    <c:forEach var="ancestorLoc" items="${selectedLocs.ancestorLocationNames}">
                                                                    	[${ancestorLoc}] 
                                                                    </c:forEach>
                                                                    </td>
                                                                    </field:display>
                                                                    <%--<td vAlign="center" align="left" width="100%">
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
                                                                    </td> --%>
                                                                    <td align="right" nowrap="nowrap">
                                                                    <c:set var="percentageDisplay" value="block" />
																		<c:if test="${selectedLocs.percentageBlocked}">
																			<c:set var="percentageDisplay" value="none"/>
																		 </c:if>
																	<div style="display: ${percentageDisplay}">
                                                                     <field:display name="Regional Percentage" feature="Location">
                                                                    		<field:display name="Validate Mandatory Regional Percentage" feature="Location">
                                                                    			<FONT color="red">*</FONT>
                                                                    		</field:display>
                                                                    		<digi:trn key="aim:editActivity:location_percentage">Percentage</digi:trn>:&nbsp;
                                                                    		<html:text name="selectedLocs" disabled="${selectedLocs.percentageBlocked}" indexed="true" 
                                                                    				property="percent" size="2"  maxlength="5" onkeyup="fnChk(this, 'region')"/>
                                                                    </field:display>
                                                                   </div>
                                                                    </td>
                                                                  </tr>
                                                                </table>
                                                              </td>
                                                            </tr>
                                                          </c:forEach>
                                                          <tr>
                                                            <td>
                                                              <table cellSpacing=2 cellPadding=2>
                                                                <tr>
                                                                <field:display name="Add Location" feature="Location">
                                                                  <td>
                                                                    <html:button styleClass="dr-menu"
																		property="submitButton" disabled="true" style="color: lightgray" styleId="add_location_button"
																		onclick="selectLocation()">
																		<digi:trn key="btn:addLocation">Add Location</digi:trn>
																	</html:button>
                                                                  </td>
                                                                  </field:display>
																<field:display  name="Remove Location" feature="Location">
                                                                  <td>
                                                                    <html:button styleClass="dr-menu"
																		property="submitButton"
																		onclick="return removeSelLocations()">
																		<digi:trn key="btn:removeLocation">Remove Location</digi:trn>
																	</html:button>
                                                                  </td>
                                                                 </field:display>
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
