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

<c:set var="act">${param.extraAction}</c:set>
<c:set var="rll_ajax">render_locations_list</c:set>
<%
	if ("render_locations_list".equals(request.getParameter("extraAction"))) // ajax?
	{
		%><jsp:include page="/repository/aim/view/pledgeform/pledgelocationslist.jsp"></jsp:include><%  
	} else if ("render_locations_add".equals(request.getParameter("extraAction")))
	{
		%><jsp:include page="/repository/aim/view/pledgeform/pledgelocationsAddPledge.jsp"></jsp:include><%
    } else 
    { // not ajax: render the full bootstrap iframe 
%>
<!DOCTYPE html>
<html lang="en">
  <head>
	<!-- IFRAME SEAMLESS !!! -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Pledges Location Bootstrap 3</title>

    <!-- Bootstrap -->
    <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootswatch/3.1.1/united/bootstrap.min.css">
    <!-- Bootstrap-select http://silviomoreto.github.io/bootstrap-select/ -->
    <link href="/repository/bootstrap/bootstrap-select.min.css" rel="stylesheet" type="text/css">
    <link href="/repository/bootstrap/jquery.pnotify.default.css" media="all" rel="stylesheet" type="text/css" />
    <link href="/repository/bootstrap/jquery.pnotify.default.icons.css" media="all" rel="stylesheet" type="text/css" />
    <link href="/repository/bootstrap/bootstrap-dialog-min.css" rel="stylesheet" type="text/css" />
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  <style>
	.label-near-input input {max-width: 55% !important; display: inline-block;}
	.label-near-input label {margin-right: 3em; max-width: 25%;}
	.initially-hidden {display: none;}
  </style>
  </head>
  <body style="min-height: 350px">
	<jsp:include page="/repository/aim/view/pledgeform/pledgelocationslist.jsp"></jsp:include>
	<div class="text-center"><button type="button"class="btn btn-success btn-sm" id='add_location_button'>Add Location</button></div>
	<jsp:include page="/repository/aim/view/pledgeform/pledgelocationsAddPledge.jsp"></jsp:include>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
    <script src="/repository/bootstrap/bootstrap-select.min.js" type="text/javascript"></script>
    <script src="/repository/bootstrap/jquery.pnotify.min.js" type="text/javascript"></script>
    <script src="/repository/bootstrap/bootstrap-dialog.min.js" type="text/javascript"></script>
    <script src="/repository/bootstrap/hacks.js" type="text/javascript"></script>
    <script src="/repository/bootstrap/bootstrap-utils.js" type="text/javascript"></script>
    <script src="/repository/aim/view/pledgeform/pfscripts.js" type="text/javascript"></script>

  </body>
</html>
<%
    } // the big "otherwise"
%>

<%--
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
		document.pledgeForm.target = window.parentWindow;
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
													<c:set var="sizeString">5</c:set>
													<c:set var="multipleString">multiple="multiple"</c:set>
													<c:set var="changeString"> </c:set>
													<c:set var="nameString">name="userSelectedLocs"</c:set>
												</c:when>
												<c:otherwise>
													<c:set var="sizeString">1</c:set>
													<c:set var="multipleString"></c:set>
													<c:set var="changeString">locationChanged('loc_${entry.key}')</c:set>
                                                    <!-- Not sure why we do not set name (and than do not save) if this is a regular select box, not multiselect--!>
                                                    <c:set var="nameString">name="userSelectedLocs"</c:set>
												</c:otherwise>
											</c:choose>
											<tr>
											<td>
                                                <!-- this is really sensitive to the caching in CategoryManagerUtil
												<category:getoptionvalue categoryKey="<%= org.digijava.module.categorymanager.util.CategoryConstants.IMPLEMENTATION_LOCATION_KEY%>" categoryIndex="${entry.key}"/>
                                                --!>
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
--%>