<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<link href="/TEMPLATE/ampTemplate/css/tabview.css" rel="stylesheet" type="text/css"></link>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript">

function login()
{
	<digi:context name="addUrl" property="context/module/moduleinstance/login.do" />
    document.aimRegionalObservationsForm.action = "<%=addUrl%>";
    document.aimRegionalObservationsForm.submit();
}

function fnEditProject(id)
{
	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />
   	document.aimRegionalObservationsForm.action = "<%=addUrl%>~pageId=1~step=14~action=edit~surveyFlag=true~activityId=" + id;
	document.aimRegionalObservationsForm.target = "_self";
   	document.aimRegionalObservationsForm.submit();
}


YAHOOAmp.namespace("YAHOOAmp.amp");

var myPanel = new YAHOOAmp.widget.Panel("myPreview", {
	width:"940px",
	fixedcenter: true,
    constraintoviewport: false,
    underlay:"none",
    close:true,
    visible:false,
    modal:true,
    draggable:true,
    context: ["showbtn", "tl", "bl"]
    });
var panelStart=0;

var responseSuccess = function(o){
	/* Please see the Success Case section for more
	* details on the response object's properties.
	* o.tId
	* o.status
	* o.statusText
	* o.getResponseHeader[ ]
	* o.getAllResponseHeaders
	* o.responseText
	* o.responseXML
	* o.argument
	*/
	var response = o.responseText; 
	var content = document.getElementById("myContentContent");
	//response = response.split("<!")[0];
	content.innerHTML = response;
	//content.style.visibility = "visible";
	
	showContent();
}

function showPanelLoading(msg){
	   var content = document.getElementById("myContentContent");
	   content.innerHTML = "<div style='text-align: center'>" + "Loading..." +
	   "... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>";   
	   showContent();
	 }
	 
var responseFailure = function(o){ 
	// Access the response object's properties in the 
	// same manner as listed in responseSuccess( ). 
	// Please see the Failure Case section and 
	// Communication Error sub-section for more details on the 
	// response object's properties.
	//alert("Connection Failure!"); 
}  
var callback = 
{ 
	success:responseSuccess, 
	failure:responseFailure 
};

function showContent(){
	var element = document.getElementById("myContent");
	element.style.display = "inline";
	if (panelStart < 1){
		myPanel.setBody(element);
	}
	if (panelStart < 2){
		document.getElementById("myContent").scrollTop=0;
		myPanel.show();
		panelStart = 2;
	}
}

function preview(id)
{
	showPanelLoading();
	var postString="&pageId=2&activityId=" + id+"&isPreview=2&previewPopin=true";
	//alert(postString);
	<digi:context name="addUrl" property="context/module/moduleinstance/viewActivityPreviewPopin.do" />
	var url = "<%=addUrl %>?"+postString;
	YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	
}

function initPopin() {
	var msg="\n<digi:trn>Activity Preview</digi:trn>";
	myPanel.setHeader(msg);
	myPanel.setBody("");
	myPanel.beforeHideEvent.subscribe(function() {
		panelStart=1;
	}); 
	
	myPanel.render(document.body);
}

window.onload=initPopin();

function viewChanges(id){
	openNewWindow(650,200);
	<digi:context name="showLog" property="context/module/moduleinstance/showActivityLog.do" />
	popupPointer.document.location.href = "<%= showLog %>?activityId=" + id;
}

function expandAll() {
   
	$("img[id$='_minus']").show();
	$("img[id$='_plus']").hide();	
	$("div[id$='_dots']").hide();
	$("div[id^='act_']").show('fast');
}

function collapseAll() {

	$("img[id$='_minus']").hide();
	$("img[id$='_plus']").show();	
	$("div[id$='_dots']").show();
	$("div[id^='act_']").hide();
}

function toggleGroup(group_id){
	var strId='#'+group_id;
	$(strId+'_minus').toggle();
	$(strId+'_plus').toggle();
	$(strId+'_dots').toggle();
	$('#act_'+group_id).toggle('fast');
}


function projectFiche(id)
{
	<digi:context name="ficheUrl" property="context/module/moduleinstance/projectFicheExport.do" />
	window.open ( "<%=ficheUrl%>~ampActivityId=" + id,"<digi:trn key="aim:projectFiche">Project Fiche</digi:trn>");
}
</script>

<digi:errors/>
<div id="myContent" style="display: none;">
	<div id="myContentContent" class="content" style="overflow: scroll; height: 500px;">
	</div>
</div>
<digi:instance property="aimRegionalObservationsForm" />
<digi:context name="digiContext" property="context"/>
<logic:equal name="aimRegionalObservationsForm" property="validLogin" value="false">
	<digi:form action="/viewRegionalObservations.do" name="aimRegionalObservationsForm" type="org.digijava.module.aim.form.RegionalObservationsForm" method="post">
		<h3 align="center"> Invalid Login. Please Login Again. </h3><p align="center"><html:submit styleClass="dr-menu" value="Log In" onclick="login()" /></p>
	</digi:form>
</logic:equal>

<digi:form action="/viewRegionalObservations.do" name="aimRegionalObservationsForm" type="org.digijava.module.aim.form.RegionalObservationsForm" method="post">
	<logic:equal name="aimRegionalObservationsForm" property="validLogin" value="true">
		<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top" border=0 width="100%">
			<TR>
				<TD vAlign="top" align="center">
					<!-- contents -->
					<TABLE width="99%" cellSpacing=0 cellPadding=0 vAlign="top" align="center" bgcolor="#f4f4f4" class="box-border-nopadding">
						<TR>
							<TD bgcolor="#f4f4f4">
								<TABLE width="100%" cellSpacing=3 cellPadding=3 vAlign="top" align="center" bgcolor="#f4f4f4" border=0>
									<TR bgColor=#f4f4f2><TD align=left >
										<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left" vAlign="top">
											<TR>
												<TD align="left">
													<SPAN class=crumb>
														<jsp:useBean id="urlPhysicalProgress" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlPhysicalProgress}" property="ampActivityId">
															<bean:write name="aimRegionalObservationsForm" property="ampActivityId"/>
														</c:set>
														<c:set target="${urlPhysicalProgress}" property="tabIndex" value="3"/>
														<c:set var="translation">
															<digi:trn>Click here to view Regional Observations</digi:trn>
														</c:set>
														<digi:link href="/viewRegionalObservations.do" name="urlPhysicalProgress" styleClass="comment" title="${translation}" >
															<digi:trn>Regional Observations</digi:trn>
														</digi:link>
														&nbsp;&gt;&nbsp;<digi:trn key="aim:ppOverview">Overview</digi:trn>
													</SPAN>
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
								<feature:display name="Regional Observations" module="Regional Observations">
									<TR>
										<TD bgcolor="#F4F4F2" vAlign="bottom" width="100%">
											<!-- issues -->
											<div id="content" class="yui-skin-sam" style="width:100%;"> 
												<div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;">
                        							<ul class="yui-nav">
                          								<li class="selected">
                          									<a>
                          										<div>
                                									<digi:trn>Regional Observations</digi:trn>
                          										</div>
                          									</a>
                          								</li>
                        							</ul>
                        							<div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">
														<TABLE width="100%" cellPadding="0" cellSpacing="1" vAlign="top" align="center" bgColor=#dddddd>
	   	            										<logic:empty name="aimRegionalObservationsForm" property="issues">
																<TR bgcolor="#f4f4f2">
																	<TD align="center">
																		<font color="red"><digi:trn>No Regional Observations</digi:trn></font>
																	</TD>
																</TR>
															</logic:empty>
															<logic:notEmpty name="aimRegionalObservationsForm" property="issues">
																<logic:iterate name="aimRegionalObservationsForm" property="issues" id="obs" type="org.digijava.module.aim.dbentity.AmpRegionalObservation">
																	<TR bgcolor="#f4f4f2"><TD width="100%" bgcolor="#F4F4F2" align="center">
																		<TABLE width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="center" bgColor=#f4f4f2>
																			<TR>
																				<TD width="100%" vAlign="top" align="left">
																					<TABLE width="100%" cellPadding="2" cellSpacing="1" vAlign="top" align="left" bgcolor="#ffffff">
																						<TR bgcolor="#dfdfdf">
																							<TD>
																								<font color="#0000ff"><digi:trn>Observation:</digi:trn> </font>
																								<bean:write name="obs" property="name"/>
																								&nbsp;
																								<bean:write name="obs" property="observationDate"/>														
																							</TD>
																						</TR>
																						<logic:empty name="obs" property="regionalObservationMeasures">
																							<TR>
																								<TD align="center">
																									<font color="red">
																										<digi:trn key="aim:noMeasures">No measures</digi:trn>
																									</font>
																								</TD>
																							</TR>
																						</logic:empty>
																						<logic:notEmpty name="obs" property="regionalObservationMeasures">
																							<logic:iterate name="obs" property="regionalObservationMeasures" id="measure" type="org.digijava.module.aim.dbentity.AmpRegionalObservationMeasure">
																								<TR>
																									<TD>
																										<TABLE width="95%" cellPadding="2" cellSpacing="1" vAlign="top" align="center" bgcolor="#dddddd">
																											<TR bgcolor="#f6f6f6">
																												<TD>
																													<font color="#0000ff"><digi:trn key="aim:measure">Measure</digi:trn>: </font>
																													<bean:write name="measure" property="name"/>
																												</TD>
																											</TR>
																											<logic:empty name="measure" property="actors">
																												<TR bgcolor="#ffffff">
																													<TD align="center">
																														<font color="red"><digi:trn key="aim:noActors">No actors</digi:trn></font>
																													</TD>
																												</TR>
																											</logic:empty>
																											<logic:notEmpty name="measure" property="actors">
																												<TR bgcolor="#ffffff">
																													<TD>
																														<TABLE width="100%" cellPadding="2" cellSpacing="1" vAlign="top" align="center" bgcolor="#ffffff">
																															<logic:iterate name="measure" property="actors" id="actor" type="org.digijava.module.aim.dbentity.AmpRegionalObservationActor">
																																<TR bgcolor="#ffffff">
																																	<TD>
		                                                                                        										<font color="#0000ff"><digi:trn key="aim:actor">Actor</digi:trn>: </font>
																																		<bean:write name="actor" property="name"/>
																																	</TD>
																																</TR>
																															</logic:iterate>
																														</TABLE>
																													</TD>
																												</TR>
																											</logic:notEmpty>
																										</TABLE>
																									</TD>
																								</TR>
																							</logic:iterate>
																						</logic:notEmpty>
																					</TABLE>
																				</TD>
																			</TR>
																		</TABLE>
																	</TD>
																</TR>
															</logic:iterate>
														</logic:notEmpty>
													</TABLE>
												</div>
											</div>						
										</div>						
									</TD>
								</TR>
                			</feature:display>
						</TABLE>
					</TD>
				</TR>
			<TR>
		<TD>&nbsp;</TD>
	</TR>
</TABLE>
</TD>
</TR>
</TABLE>
</logic:equal>
</digi:form>

<script>
if(document.getElementById('showBottomBorder').value=='1'){
	document.write('</table><tr><td class="td_bottom1">&nbsp;</td></tr></table>&nbsp');
}
</script>