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

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
    function updateImplLevel() {
        var implLevelSelect = document.getElementsByName("levelId")[0];
        //if ( implLevelSelect.selectedIndex > 0 ) {
            document.pledgeForm.action	= "/aim/selectPledgeLocation.do?edit=true";
            document.pledgeForm.submit();
        //}
    }

	function updateLocLevel(){
		var locationLevelSelect = document.getElementsByName("implemLocationLevel")[0];
		//if ( locationLevelSelect.selectedIndex > 0 ) {
			document.pledgeForm.action	= "/aim/selectPledgeLocation.do?edit=true";		
			document.pledgeForm.submit();
		//}
	}
	
	function locationChanged( selectId ) {
		var selectEl		= document.getElementById(selectId);
		document.pledgeForm.parentLocId.value = selectEl.options[selectEl.selectedIndex].value;
		//if ( document.pledgeForm.parentLocId.value != "-1" ) {
			document.pledgeForm.action	= "/aim/selectPledgeLocation.do?edit=true";		
			document.pledgeForm.submit();
		//}
	}

	function submitForm() {
		document.pledgeForm.target = window.opener.name;
    	document.pledgeForm.submit();
	 	window.close();
	}
	function closeWindow() {
		window.close();
	}

    function goBack() {
        document.location.href	= "/aim/selectPledgeLocation.do?edit=true";
    }
</script>

<digi:instance property="pledgeForm" />

<digi:form action="/pledgeLocationSelected.do" method="post">

<html:hidden styleId="parentLocId" property="parentLocId" />

<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border="0">
	<tr><td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left valign="top">
					<table bgcolor=#f4f4f2 cellpadding="0" cellspacing="0" width="100%" class=box-border-nopadding>
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<digi:trn key="aim:selectLocation">
								Select Location</digi:trn>
							</td>
						</tr>
						<tr>
							<td align="center" bgcolor=#ECF3FD>
								<table cellPadding=2 cellSpacing=2>


                                    <tr>
                                        <td>
                                            <digi:trn key="aim:implementationLevel">
                                                Implementation Level
                                            </digi:trn>
                                        </td>
                                        <td>
                                            <c:set var="translation">
                                                <digi:trn key="aim:addActivityImplLevelFirstLine">Please select from below</digi:trn>
                                            </c:set>

                                            <category:showoptions multiselect="false" firstLine="${translation}" name="pledgeForm" property="levelId" keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.IMPLEMENTATION_LEVEL_KEY %>" styleClass="inp-text" />
                                            <script language="Javascript">
                                                var locationImplSelect = document.getElementsByName("levelId")[0];
                                                locationImplSelect.onchange=function() {
                                                    updateImplLevel();
                                                }
                                            </script>
                                        </td>
                                    </tr>


									<tr>
										<td>
											<digi:trn key="aim:implementationLoc">
                                                  Implementation Location
                                        	</digi:trn>
                                        </td>
										<td vAlign="center" >
				                      		<c:set var="translation">
                                                <digi:trn key="aim:addActivityImplLevelFirstLine">Please select from below</digi:trn>
                                             </c:set>

                                             <c:choose>
                                                 <c:when test="${pledgeForm.levelId <= 0}">
                                                    <select class="inp-text" name="implemLocationLevel">
                                                        <option selected="selected" value="0">${translation}</option>
                                                    </select>
                                                </c:when>
                                                <c:otherwise>
                                                    <category:showoptions multiselect="false" firstLine="${translation}" name="pledgeForm" property="implemLocationLevel" tag="${pledgeForm.levelId}" keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.IMPLEMENTATION_LOCATION_KEY %>" styleClass="inp-text" />

                                                    <script language="Javascript">
                                                        var implemLocationLevelSelect = document.getElementsByName("implemLocationLevel")[0];
                                                        if(implemLocationLevelSelect!=null){
                                                            implemLocationLevelSelect.onchange = function() {
                                                                //removeAllLocations();
                                                                updateLocLevel();
                                                            }
                                                        }
                                                    </script>
                                                </c:otherwise>
                                            </c:choose>
										</td>
									</tr>
									<logic:notEmpty name="pledgeForm" property="locationByLayers">
										<logic:iterate name="pledgeForm" property="locationByLayers" id="entry">
											<bean:define id="myCollection" type="java.util.Collection" name="entry" property="value" />
											<% pageContext.setAttribute("colSize", myCollection.size() ) ;%>
											<c:choose>
                                                <c:when test="${colSize > 1}">
												<%--c:when test="${entry.key==(pledgeForm.impLevelValue -1) }"--%>
													<c:set var="sizeString">5</c:set>
													<c:set var="multipleString">multiple="multiple"</c:set>
													<c:set var="changeString"> </c:set>
													<c:set var="nameString">name="userSelectedLocs"</c:set>
												</c:when>
												<c:otherwise>
													<c:set var="sizeString">1</c:set>
													<c:set var="multipleString"></c:set>
													<c:set var="changeString">locationChanged('loc_${entry.key}')</c:set>
                                                    <%-- Not sure why we do not set name (and than do not save) if this is a regular select box, not multiselect--%>
                                                    <c:set var="nameString">name="userSelectedLocs"</c:set>
													<%--c:set var="nameString"></c:set--%>
												</c:otherwise>
											</c:choose>
											<tr>
											<td>
                                                <%-- this is really sensitive to the caching in CategoryManagerUtil
												<category:getoptionvalue categoryKey="<%= org.digijava.module.categorymanager.util.CategoryConstants.IMPLEMENTATION_LOCATION_KEY%>" categoryIndex="${entry.key}"/>
                                                --%>
                                                <digi:trn>${pledgeForm.implLocationValue.value}</digi:trn>
											</td>
											<td>
												<select id="loc_${entry.key}" class="inp-text" size="${sizeString}" onchange="${changeString}" ${multipleString} ${nameString} >
													<c:if test="${colSize!=1 && sizeString=='1'}">
														<option value="-1">&nbsp;&nbsp;<digi:trn>Please select from below</digi:trn>&nbsp;&nbsp;</option>
													</c:if>
													<logic:notEmpty name="entry" property="value">
														<logic:iterate name="entry" property="value" id="locationEntry">
															<c:choose>
															<c:when test="${locationEntry.key == pledgeForm.selectedLayers[entry.key]}">
																<option selected="selected" value="${locationEntry.key}">${locationEntry.value}</option>
															</c:when>
															<c:otherwise>
																<option value="${locationEntry.key}">${locationEntry.value}</option>
															</c:otherwise>
															</c:choose>
														</logic:iterate>
													</logic:notEmpty>
												</select>
											</td>
										</tr>
										</logic:iterate>
									
									</logic:notEmpty>
									
								</table>
							</td>
						</tr>
						<c:if test="${pledgeForm.noMoreRecords}">
								<tr bgcolor="#ECF3FD">
									<td colspan="2" align="center" height="20"> 
									<digi:trn key="location:norcords:found">No records found</digi:trn>
									</td>	
								</tr>
						</c:if>
						<tr bgcolor="#ECF3FD">
							<td align="center">
								<table cellPadding=3 cellSpacing=3>
									<tr>
										<td>	
											
											<input 
											<c:if test="${pledgeForm.noMoreRecords}">
												disabled="true"
											</c:if> 
											
											type="button" value="<digi:trn key='btn:add'>Add</digi:trn>" class="dr-menu"
											onclick="submitForm()">
										</td>
										<td>
											<input type="button" value="<digi:trn key='btn:close'>Close</digi:trn>" class="dr-menu" onclick="closeWindow()">
										</td>
                                        <%--td>
                                            <input type="button" value="<digi:trn key='btn:back'>Back</digi:trn>" class="dr-menu" onclick="goBack()">
                                        </td--%>
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
