<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript" type="text/javascript">
	function addSector(param)
	{
		<digi:context name="addSec" property="context/addActivity.do?addSector=true&edit=param" />
	    document.aimEditActivityForm.action = "<%= addSec %>";
		document.aimEditActivityForm.target = "_self";
		document.aimEditActivityForm.submit();
	}

	function removeSelSectors() {
		var flag = validate(2);
		if (flag == false) return false;

	    <digi:context name="remSec" property="context/addActivity.do?remSectors=true" />
	    document.aimEditActivityForm.action = "<%= remSec %>";
	    document.aimEditActivityForm.target = "_self";
	    document.aimEditActivityForm.submit();
	    return true;
	}
</script>

<digi:instance property="aimEditActivityForm" />
	<tr>
	<td>
				<!-- contents -->
                                    <table width="100%" bgcolor="#f4f4f2">
                                    
                                    
                                      <tr>
                                        <td>
                                          <IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15 />
                                          <b>
                                            <FONT color="red">
                                            *
                                            </FONT>
                                            <digi:trn key="aim:sector">
                                            Sector
                                            </digi:trn>
                                          </b>
                                          <a href="javascript:popupwin()">
                                            <img src="../ampTemplate/images/help.gif" alt="Click to get help on Status" width=10 height=10 border=0/>
                                          </a>
                                        </td>
                                      </tr>
                                      <tr>
                                        <td>
                                          <FONT color=red>
                                          *
                                          </FONT>
                                          <digi:trn	key="aim:chooseSector">
                                          Choose the sector.
                                          </digi:trn>
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
                                                  <digi:trn key="aim:sector">
                                                    Sector
                                                  </digi:trn>
                                                </b>
                                              </td>
											</tr>
                                            <tr>
                                              <td bgcolor="#ffffff" width="100%">
                                                <table cellPadding=1 cellSpacing=1 border=0	bgcolor="#ffffff" width="100%">
                                                  <c:if test="${empty aimEditActivityForm.activitySectors}">
                                                    <tr>
                                                      <td bgcolor="#ffffff">
                                                      	<field:display name="Add Sectors Button" feature="Sectors">
                                                      
                                                      	</field:display>
                                                      	<html:button styleClass="buton"
															property="submitButton" onclick="addSectors();">
															<digi:trn key="btn:addSectors">Add Sectors</digi:trn>
														</html:button>
                                                      </td>
                                                    </tr>
                                                  </c:if>
                                                  <c:if test="${!empty aimEditActivityForm.activitySectors}">
                                                    <tr>
                                                      <td>
                                                        <table cellSpacing=0 cellPadding=0 border=0 bgcolor="#ffffff" width="100%">
                                                          <c:forEach var="activitySectors" items="${aimEditActivityForm.activitySectors}">
                                                            <tr>
                                                              <td>
                                                                <table width="100%" cellSpacing=1 cellPadding=1 vAlign="top" align="left">
                                                                  <tr>
                                                                    <td width="3%" vAlign="center">
                                                                      <html:multibox property="selActivitySectors">
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
       
                                                                        [${activitySectors.sectorScheme}]
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
                                                                    <td width="5%" vAlign="center" align="right">
                                                                    <field:display name="Sectors Percentage" feature="Sectors"></field:display>
                                                                    	<FONT color="red">*</FONT><digi:trn key="aim:percentage">Percentage</digi:trn>:&nbsp;</td>
                                                                    <td width="5%" vAlign="center" align="left">
                                                                      <html:text name="activitySectors" indexed="true" property="sectorPercentage"
                                                                      			 size="2" maxlength="3" onkeyup="fnChk(this)"/>
                                                                   </td>
                                                                  </tr>
                                                                </table>
                                                              </td>
                                                            </tr>
                                                            <c:set var="sectror" value="${activitySectors.count}"/>
                                                          </c:forEach>
                                                          <tr>
                                                            <td>
                                                              <table cellSpacing=2 cellPadding=2>
                                                                <tr>
																<logic:equal name="aimEditActivityForm" property="multiSectorSelecting" value="On">
																	<field:display name="Add Sectors Button" feature="Sectors">&nbsp;
    		                                                            <td>
    		                                                            <html:hidden name="aimEditActivityForm" property="editAct"/>
    		                                                            <html:button styleClass="buton"
															property="submitButton" onclick="addSectors(${aimEditActivityForm.editAct});">
															<digi:trn key="btn:addSectors">Add Sectors</digi:trn>
														</html:button></td>
                                                                    </field:display>                                                          
																</logic:equal>      
																    <field:display name="Remove Sectors Button" feature="Sectors">&nbsp;
                                                                  <td>
                                                                  <html:button styleClass="buton"
															property="submitButton" onclick="return removeSelSectors()">
															<digi:trn key="btn:removeSector">Remove Sector</digi:trn>
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
                                                  </c:if>
                                                </table>
                                              </td>
                                            </tr>
                                          </table>
                                          <!-- Add Sectors -->
                                        </td>
                                      </tr>
                                     
                                     </table>
                                     </td>
                                     </tr>
                                     
