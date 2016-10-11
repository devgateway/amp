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


 <digi:instance property="aimNewIndicatorForm" />
                                   <table cellPadding=5 cellspacing="1" border="0" width="100%"	bgcolor="#F8F8F8">
											<tr>
                                              <td bgcolor="#F8F8F8" width="100%">
                                                <table cellpadding="1" cellspacing="1" border="0"	bgcolor="#ffffff" width="100%">
                                                  <c:if test="${empty aimNewIndicatorForm.activitySectors}">
                                                    <tr>
                                                      <td bgcolor="#F8F8F8">
                                                        <input type="button" class="buttonx" onclick="addSectors();" value='<digi:trn jsFriendly="true" key="btn:addSectors">Add Sectors</digi:trn>' />
                                                      </td>
                                                    </tr>
                                                  </c:if>
                                                  <c:if test="${!empty aimNewIndicatorForm.activitySectors}">
                                                    <tr>
                                                      <td>
                                                        <table cellspacing="0" cellpadding="0" border="0" bgcolor="#ffffff" width="100%">
                                                          <c:forEach var="activitySectors" items="${aimNewIndicatorForm.activitySectors}">
                                                            <tr>
                                                              <td>
                                                                  <table width="100%" cellspacing="1" border="0" cellpadding="1" vAlign="top" align="left" class="inside">
                                                                  <tr bgcolor="#F8F8F8">
                                                                    <td width="3%" vAlign="center">
                                                                      <html:multibox property="selActivitySector">
	                                                                     <c:if test="${activitySectors.subsectorLevel1Id == -1}">
	                                                                      ${activitySectors.sectorId}
	                                                                      </c:if>
	                                                                      <c:if test="${activitySectors.subsectorLevel1Id != -1 && activitySectors.subsectorLevel2Id == -1}">
	                                                                      ${activitySectors.subsectorLevel1Id}
	                                                                      </c:if>
	                                                                      <c:if test="${activitySectors.subsectorLevel1Id != -1 && activitySectors.subsectorLevel2Id != -1}">
	                                                                      ${activitySectors.subsectorLevel2Id}
	                                                                      </c:if>
	                                                                  </html:multibox>
                                                                    </td>
                                                                    <td  width="87%" vAlign="center" align="left">
                                                                      <c:if test="${!empty activitySectors.sectorName}">
                                                                        [${activitySectors.sectorName}]
                                                                      </c:if>
                                                                      <c:if test="${!empty activitySectors.subsectorLevel1Name}">
                                                                        [${activitySectors.subsectorLevel1Name}]
                                                                      </c:if>
                                                                      <c:if test="${!empty activitySectors.subsectorLevel2Name}">
                                                                        [${activitySectors.subsectorLevel2Name}]
                                                                      </c:if>
                                                                    </td>
                                                                   </tr>
                                                                </table>
                                                              </td>
                                                            </tr>
                                                          </c:forEach>
                                                          <tr bgcolor="#F8F8F8">
                                                            <td>
                                                              <table cellSpacing=2 cellPadding=2>
                                                                <tr>
                                                                  <logic:notEmpty name="MS" scope="application">
                                                                    <td>
                                                                      <input type="button" value="<digi:trn key="admin:addsector">Add Sectors</digi:trn>" class="dr-menu"  onclick="addSectors();">
                                                                    </td>
                                                                  </logic:notEmpty>
                                                                  <td >
																	<input type="button" class="dr-menu" onclick="return removeSelSectors()" value='<digi:trn jsFriendly="true" key="btn:removeSector">Remove Sector</digi:trn>' />
                                                                  </td>
                                                                </tr>
                                                              </table>
                                                            </td>
                                                          </tr>
                                                        </table>
                                                      </td>
                                                    </tr>
                                                  </c:if>
                                                </table>
                                              </td>
                                            </tr>
                                          </table>
                                          <!-- Add Sectors -->
                                        </td>
                                      </tr>
