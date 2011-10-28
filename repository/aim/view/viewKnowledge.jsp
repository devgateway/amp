<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>

<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>

<%@ taglib uri="/taglib/struts-html" prefix="html"%>

<%@ taglib uri="/taglib/digijava" prefix="digi"%>

<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript">


function login()

{

	<digi:context name="addUrl" property="context/module/moduleinstance/login.do" />

    document.aimKnowledgeForm.action = "<%=addUrl%>";

    document.aimKnowledgeForm.submit();

}





function projectFiche(id)

{

	<digi:context name="ficheUrl" property="context/module/moduleinstance/projectFicheExport.do" />

	window.open ( "<%=ficheUrl%>~ampActivityId=" + id,"<digi:trn key="aim:projectFiche">Project Fiche</digi:trn>");

}



function fnEditProject(id)

{

	<digi:context name="addUrl" property="context/module/moduleinstance/editActivity.do" />

   document.aimKnowledgeForm.action = "<%=addUrl%>~pageId=1~step=6~action=edit~surveyFlag=true~activityId=" + id;

	document.aimKnowledgeForm.target = "_self";

   document.aimKnowledgeForm.submit();

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
	var postString="&pageId=2&activityId=" + id+"&isPreview=1&previewPopin=true";
	//alert(postString);
	<digi:context name="addUrl" property="context/module/moduleinstance/viewActivityPreviewPopin.do" />
	var url = "<%=addUrl %>?"+postString;
	YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	
}

function initPopin() {
	var msg='\n<digi:trn>Activity Preview</digi:trn>';
	myPanel.setHeader(msg);
	myPanel.setBody("");
	myPanel.beforeHideEvent.subscribe(function() {
		panelStart=1;
	}); 
	
	myPanel.render(document.body);
}

window.onload=initPopin();

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


</script>

<div id="myContent" style="display: none;">
	<div id="myContentContent" class="content" style="overflow: scroll; height: 500px;">
	</div>
</div>
<digi:context name="digiContext" property="context" />

<digi:instance property="aimKnowledgeForm" />

<logic:equal name="aimKnowledgeForm" property="validLogin" value="false">

	<digi:form action="/viewKnowledge.do" name="aimKnowledgeForm"
		type="org.digijava.module.aim.form.KnowledgeForm" method="post">

		<h3 align="center">Invalid Login. Please Login Again.</h3>

		<p align="center"><input type="button" class="dr-menu"
			value="Log In" onclick="login()" /></p>

	</digi:form>

</logic:equal>

<html:hidden property="id" />

<logic:equal name="aimKnowledgeForm" property="validLogin" value="true">

	<digi:form action="/viewKnowledge.do" name="aimKnowledgeForm"
		type="org.digijava.module.aim.form.KnowledgeForm" method="post">

		<TABLE cellSpacing=0 cellPadding=0 align="center" vAlign="top"
			border=0 width="100%">

			<TR>

				<TD vAlign="top" align="center"><!-- contents -->



				<TABLE width="99%" cellSpacing=0 cellPadding=0 vAlign="top"
					align="center" bgcolor="#f4f4f4" class="box-border-nopadding">

					<TR>
						<TD bgcolor="#f4f4f4">



						<TABLE width="100%" cellSpacing=3 cellPadding=3 vAlign="top"
							align="center" bgcolor="#f4f4f4">

							<TR bgColor=#f4f4f2>

								<TD align="left">

								<TABLE width="100%" cellPadding="3" cellSpacing="2" align="left"
									vAlign="top">

									<TR>

										<TD align="left"><SPAN class=crumb> <jsp:useBean
											id="urlKnowledge" type="java.util.Map"
											class="java.util.HashMap" /> <c:set target="${urlKnowledge}"
											property="ampActivityId">

											<bean:write name="aimKnowledgeForm" property="id" />

										</c:set> <c:set target="${urlKnowledge}" property="tabIndex" value="4" />

										<c:set var="translation">

											<digi:trn key="aim:clickToViewKnowledge">Click here to view Knowledge</digi:trn>

										</c:set> <digi:link href="/viewKnowledge.do" name="urlKnowledge"
											styleClass="comment" title="${translation}">

											<digi:trn key="aim:documents">Documents</digi:trn>

										</digi:link>&nbsp;&gt;&nbsp;<digi:trn key="aim:docOverview">Overview</digi:trn>
										</SPAN></TD>
									</TR>

								</table>

								</TD>

							</TR>
							<module:display name="Document" parentModule="PROJECT MANAGEMENT">
								<tr>
									<TD>
									
											<bean:define id="dmWindowTitle" toScope="request">Related Documents</bean:define>
											<bean:define toScope="request" id="documentsType"
												value="<%=org.digijava.module.aim.helper.ActivityDocumentsConstants.RELATED_DOCUMENTS %>" />
											<bean:define toScope="request" id="versioningRights" value="false" />
											<bean:define toScope="request" id="viewAllRights" value="true" /> 
											<bean:define toScope="request" id="deleteRights" value="false" /> 
											<bean:define toScope="request" id="showVersionsRights" value="false" /> 
											<bean:define toScope="request" id="makePublicRights" value="false" /> 
											<bean:define toScope="request" id="crRights" value="true" /> 
											<jsp:include
												page="/repository/contentrepository/view/showSelectedDocumentsDM.jsp" />
											
									</TD>
								</tr>
							</module:display>
							
							<c:if
								test="${aimKnowledgeForm.managedDocuments != null && !empty aimKnowledgeForm.managedDocuments}">

								<TR bgColor=#f4f4f2>

									<TD vAlign="top" align="center" width="100%">

									<TABLE width="98%" cellPadding=0 cellSpacing=0 vAlign="top"
										align="center" bgColor=#f4f4f2>

										<TR>

											<TD width="100%" bgcolor="#F4F4F2" height="17">

											<TABLE border="0" cellpadding="0" cellspacing="0"
												bgcolor="#F4F4F2" height="17">

												<TR bgcolor="#F4F4F2" height="17">

													<TD bgcolor="#C9C9C7" class="box-title">&nbsp;&nbsp; <digi:trn
														key="aim:managedDocuments">DM-Managed Documents</digi:trn></TD>

													<TD><IMG src="../ampTemplate/images/corner-r.gif"
														width="17" height="17"></TD>

												</TR>

											</TABLE>

											</TD>

										</TR>

										<TR>

											<TD width="100%" bgcolor="#F4F4F2" align="center">

											<TABLE width="100%" cellPadding="2" cellSpacing="2"
												vAlign="top" align="center" bgColor=#f4f4f2
												class="box-border-nopadding">

												<TR>

													<TD width="100%" vAlign="top" align="left">

													<TABLE width="100%" cellPadding="4" cellSpacing="1"
														vAlign="top" align="left" bgcolor="#ffffff">

														<TR bgcolor="#dddddd">

															<TD width="100%" colspan="2" align="center"><b>

															<digi:trn key="aim:item">Item</digi:trn></b></TD>

														</TR>

														<logic:iterate name="aimKnowledgeForm"
															property="managedDocuments" id="document">



															<TR bgcolor="#f4f4f2">

																<TD align="right"><IMG alt=Link height=10
																	src="../ampTemplate/images/arrow-gr.gif"></TD>

																<TD width="98%" align="left"><b> <bean:write
																	name="document" property="name" /></b> - <i>File : <c:out
																	value="${document.fileName}" /></i></TD>

															</TR>

															<c:if test="${!empty document.description}">

																<TR bgcolor="#f4f4f2">

																	<TD width="98%" align="left" colspan="2">

																	&nbsp;&nbsp;&nbsp;&nbsp;<c:out
																		value="${document.description}" /></TD>

																</TR>

															</c:if>

														</logic:iterate>

													</TABLE>

													</TD>

												</TR>

											</TABLE>

											</TD>

										</TR>

									</TABLE>

									</TD>

								</TR>

							</c:if>

						</TABLE>



						</TD>
					</TR>



				</TABLE>

				<!-- end --></TD>

			</TR>

			<TR>

				<TD>&nbsp;</TD>

			</TR>

		</TABLE>

	</digi:form>

</logic:equal>
<script>
if(document.getElementById('showBottomBorder').value=='1'){
	document.write('</table><tr><td class="td_bottom1">&nbsp;</td></tr></table>&nbsp');
}
</script>