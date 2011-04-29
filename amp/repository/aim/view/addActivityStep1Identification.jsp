<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>


<script language="JavaScript">
	function  submitAfterSelectingChapterYear()
	{
		<digi:context name="nextTarget" property="context/module/moduleinstance/addActivity.do" />
		
    	document.aimEditActivityForm.action = "<%= nextTarget %>";
    	document.aimEditActivityForm.target = "_self";
    	document.aimEditActivityForm.editKey.value = null;
  		document.aimEditActivityForm.step.value = "1";
    	document.aimEditActivityForm.submit();
	}
</script>
<digi:instance property="aimEditActivityForm" />
<%@page import="org.digijava.module.aim.helper.Constants"%><script type="text/javascript" language="JavaScript" src="<digi:file src="module/message/script/yahoo-dom-event.js"/>"></script>
<script type="text/javascript" language="JavaScript" src="<digi:file src="module/message/script/animation-min.js"/>"></script>
<script type="text/javascript" language="JavaScript" src="<digi:file src="module/message/script/autocomplete-min.js"/>"></script>

<!--begin custom header content for this example-->
<style type="text/css">
<!--

.yui-skin-sam .yui-ac{position:relative;font-family:arial;font-size: 100%}
.yui-skin-sam .yui-ac-input{position:absolute;width:100%;font-size: 100%}
.yui-skin-sam .yui-ac-container{position:absolute;top:1.6em;width:100%;}
.yui-skin-sam .yui-ac-content{position:absolute;width:100%;border:1px solid #808080;background:#fff;overflow:hidden;z-index:9050;}
.yui-skin-sam .yui-ac-shadow{position:absolute;margin:.3em;width:100%;background:#000;-moz-opacity:0.10;opacity:.10;filter:alpha(opacity=10);z-index:9049;}
.yui-skin-sam .yui-ac-content ul{margin:0;padding:0;width:100%;}
.yui-skin-sam .yui-ac-content li{margin:0;padding:2px 5px;cursor:default;white-space:nowrap;FONT-SIZE: 100%;}
.yui-skin-sam .yui-ac-content li.yui-ac-prehighlight{background:#B3D4FF;}
.yui-skin-sam .yui-ac-content li.yui-ac-highlight{background:#426FD9;color:#FFF;}

#myAutoComplete .yui-ac-content { 
    max-height:16em;overflow:auto;overflow-x:hidden; /* set scrolling */ 
    _height:16em; /* ie6 */ 
} 


#myAutoComplete ul {
	list-style: square;
	padding-right: 0px;
	padding-bottom: 2px;
}

#myAutoComplete div {
	padding: 0px;
	margin: 0px; 
}

#myAutoComplete,
#myAutoComplete2 {
    width:15em; /* set width here */
    padding-bottom:2em;
}
-->
</style>

<script language="JavaScript">
var responseSuccessRetrive = function(o){
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
	 		
	var text=o.responseText;

	if (text=='null'){
		removeOptionSelected('budgetorg');
		removeOptionSelected('budgetdepart');
		document.getElementById("budgetdepart").disabled=false;
		document.getElementById("budgetorg").disabled=false;
		document.getElementById("budgetorg").options[0] = new Option("Select", "0","true","true");
		document.getElementById("budgetdepart").options[0] = new Option("Select", "0","true","true");
	}
	
	//Split the document
	var returnelements=text.split("||")
	//Process each of the elements
	if (returnelements[0]=='1'){
		removeOptionSelected('budgetorg');
		removeOptionSelected('budgetdepart');

		for ( var i=1; i<returnelements.length; i++ ){
			valueLabelPair = returnelements[i].split("|")
			document.getElementById("budgetorg").options[i-1] = new Option(valueLabelPair[1], valueLabelPair[0]);
			document.getElementById("budgetorg").disabled=false;
		}
		document.getElementById("budgetorg").options[0].selected='selected';
		document.getElementById("budgetdepart").options[0] = new Option('Select', '0');
		document.getElementById("budgetdepart").options[0].selected='selected';
		
	}else if (returnelements[0]=='2'){
		removeOptionSelected('budgetdepart');
		for ( var i=1; i<returnelements.length; i++ ){
			valueLabelPair = returnelements[i].split("|")
			document.getElementById("budgetdepart").options[i-1] = new Option(valueLabelPair[1], valueLabelPair[0]);
		}
		document.getElementById("budgetdepart").options[0].selected='selected';
		document.getElementById("budgetdepart").disabled=false;
		document.getElementById("budgetorg").disabled=false;
	}
}	

function removeOptionSelected(name)
{
  var elSel = document.getElementById(name);
  var i;
  for (i = elSel.length; i>=0; i--) {
    	elSel.remove(i);
  	}
}


var responseFailureRetrive = function(o){ 
// Access the response object's properties in the 
// same manner as listed in responseSuccess( ). 
// Please see the Failure Case section and 
// Communication Error sub-section for more details on the 
// response object's properties.
	alert("Connection Failure!"); 
}  

var RetriveCallback = { 
	success:responseSuccessRetrive, 
	failure:responseFailureRetrive 
};

function getBudgetOptions(id,type)
{	
	<digi:context name="retrivevalues" property="/aim/RetrieveOptionsAction.do"/>
	var params = "id="+ id + "&optionstype=" + type
	var url= "<%=retrivevalues%>";
	YAHOOAmp.util.Connect.asyncRequest('POST',url,RetriveCallback,params);

	if (type=='orgselect'){
		document.getElementById("budgetdepart").disabled=true;
		document.getElementById("budgetorg").disabled=true;
	}
}

</script>
<script language="JavaScript">
function OnBudgetRules ( textboxId,  messageElId, numOfCharsNeeded) {
	this.textboxEl		= null;
	this.messageEl		= null;
	this.textboxId					= textboxId;
	this.messageElId				= messageElId;
	this.numOfCharsNeeded	= numOfCharsNeeded;
}
OnBudgetRules.prototype.init		= function () {
	this.textboxEl		= document.getElementById( this.textboxId );
	this.messageEl		= document.getElementById( this.messageElId );
}
OnBudgetRules.prototype.check		= function () {
	if ( this.textboxEl == null ) {
		this.init();
	}
	var numOfChars		= this.textboxEl.value.length;
	if ( numOfChars < this.numOfCharsNeeded ){
		this.messageEl.innerHTML	= "<font color='red'> <digi:trn>Characters to add</digi:trn>: " + (this.numOfCharsNeeded-numOfChars) + "</font>" ;
	}
	else{
		this.textboxEl.value				= this.textboxEl.value.substr(0, this.numOfCharsNeeded);
		this.messageEl.innerHTML	= "<font color='green'><digi:trn>Ok</digi:trn></font>";
	}
}



//YAHOOAmp.util.Event.addListener(window, "load", doBudgetRulesCheck ) ;


function toggleElement ( elementId, show ) {
	var displayValue;
	if ( show )
		displayValue	= '';
	else
		displayValue	= 'none';

 	var el			= document.getElementById( elementId );
 	if ( el != null )
 		el.style.display=displayValue;
}

function toggleBudgetFields(show) {
	<field:display name="Code Chapitre Dropdown" feature="Budget">	
		toggleElement("CodeChapitreDrop", show);
	</field:display> 
	<field:display name="Budget Sector" feature="Budget">
		if (document.getElementById("budgetCV").value!=${aimEditActivityForm.identification.budgetCVOff}){
			toggleElement("budgsector", show);
			toggleElement("budgsector1", show);
			toggleElement("bctd", show);
			toggleElement("bctd1", show);
		}else{
			toggleElement("budgsector", true);
			toggleElement("budgsector1", true);
			toggleElement("bctd", true);
			toggleElement("bctd1", true);
		}
	</field:display>
	<field:display name="Budget Organization" feature="Budget">
		if (document.getElementById("budgetCV").value!=${aimEditActivityForm.identification.budgetCVOff}){
			toggleElement("budgetorg", show);
			toggleElement("budgetorg1", show);
		}else{
			toggleElement("budgetorg", true);
			toggleElement("budgetorg1", true);
		}
	</field:display>
	<field:display name="Budget Department" feature="Budget">
		toggleElement("budgetdepart", show);
		toggleElement("budgetdepart1", show);
	</field:display>
	<field:display name="Budget Program" feature="Budget">
		toggleElement("budgetprog", show);
		toggleElement("budgetprog1", show);
	</field:display>

	toggleElement("budgetTable", show);
	toggleElement("Imputation", show);
	toggleElement("FY", show);
	toggleElement("Vote", show);
	toggleElement("Sub-Vote", show);
	toggleElement("Sub-Program", show);
	toggleElement("ProjectCode", show);
	toggleElement("financial", show);
	toggleElement("CodeChapitre", show);
	toggleElement("Imputation1", show);
	toggleElement("CodeChapitre1", show);
	toggleElement("FY1", show);
	toggleElement("Vote1", show);
	toggleElement("Sub-Vote1", show);
	toggleElement("Sub-Program1", show);
	toggleElement("ProjectCode1", show);

	toggleElement("Imputation2", show);
	toggleElement("CodeChapitre2", show);
	toggleElement("FY2", show);
	toggleElement("Vote2", show);
	toggleElement("Sub-Vote2", show);
	toggleElement("Sub-Program2", show);
	toggleElement("ProjectCode2", show);
}

document.getElementsByTagName('body')[0].className='yui-skin-sam';
	function budgetCheckboxClick()
	{
		
		if (document.getElementById("budgetCV") != null) {
			var l = document.getElementById("budgetCV").options.length;
			var bud=false;
			for(i=0; i<l; i++){
				if(document.getElementById("budgetCV").options[i].selected && document.getElementById("budgetCV").options[i].value=='${aimEditActivityForm.identification.budgetCVOn}'){
					toggleBudgetFields ( true );
				 	bud=true;
				}
			}
			if(!bud){
			 	toggleBudgetFields( false );
			 }
		}
	}

function InitBud(){
	if(document.getElementById("budgetCV").value=="${aimEditActivityForm.identification.budgetCVOn}"){
		 toggleBudgetFields ( true );
	}
	else{
		 toggleBudgetFields ( false );
	}
}

function disableSelection(element) {

//alert("asd");
    element.onselectstart = function() {
      return false;
    };
    element.unselectable = "on";
    element.style.MozUserSelect = "none";
    element.style.cursor = "default";
}


function disableSelection1(target){

	for (i=0;i<target.childNodes.length;i++){
			//alert("ad "+i);
			nownode=target.childNodes[i];
			alert("ad "+i+" ::"+nownode);
			if (typeof nownode.onselectstart!="undefined") //IE route
				nownode.onselectstart=function(){return false;};
			else if (typeof nownode.style.MozUserSelect!="undefined") //Firefox route
					nownode.style.MozUserSelect="none"
				else //All other route (ie: Opera)
			nownode.onmousedown=function(){return false;};
			
	}

if (typeof target.onselectstart!="undefined") //IE route
	target.onselectstart=function(){return false;};
else if (typeof target.style.MozUserSelect!="undefined") //Firefox route
	target.style.MozUserSelect="none"
else //All other route (ie: Opera)
	target.onmousedown=function(){return false;};
	//alert("Ad");
target.style.cursor = "default"
}

</script>

<html:hidden styleId="FYs" value="${aimEditActivityForm.identification.resetselectedFYs}" name="aimEditActivityForm" property="identification.resetselectedFYs"/>
											<bean:define id="contentDisabled">false</bean:define>
											<c:set var="contentDisabled"><field:display name="Project Title" feature="Identification">false</field:display>
											</c:set>
											<c:if test="${contentDisabled==''}">
												<c:set var="contentDisabled">true</c:set>
											</c:if>
											<field:display name="Project Title" feature="Identification"></field:display>
											<tr bgcolor="#ffffff">											
												<td valign="top" align="left">
													<FONT color=red>*</FONT>
													<a title="<digi:trn key="aim:TitleInDonorsOrMoFEDInternalSystems">Title used in donors or MoFED internal systems</digi:trn>">
													<digi:trn key="aim:projectTitle">Project Title</digi:trn>
													</a>
												
												</td>
												<td valign="top" align="left">
													<a title="<digi:trn key="aim:TitleInDonorsOrMoFEDInternalSystems">
													Title used in donors or MoFED internal systems
													</digi:trn>">
													<html:textarea name="aimEditActivityForm" property="identification.title" cols="60" rows="2" styleClass="inp-text"  disabled="${contentDisabled}"/>
													</a>
												</td>											
											</tr>
											
											<field:display name="Status" feature="Identification"></field:display>
											<bean:define id="contentDisabled">false</bean:define>
											<c:set var="contentDisabled">
											<field:display name="Status" feature="Identification">false</field:display>
											</c:set>
											<c:if test="${contentDisabled==''}">
												<c:set var="contentDisabled">true</c:set>
											</c:if>											
											<tr>
												<td bgcolor="#ffffff">
												<FONT color=red>*</FONT>&nbsp;
												<digi:trn key="aim:status">Status</digi:trn>												  
												<a href="javascript:popupwin('activity_status')">
												<img src="../ampTemplate/images/help.gif" alt="<digi:trn>Click to get help on Status</digi:trn>" width=10 height=10 border=0></a>
												</td>
												<td bgcolor="#ffffff">
													<c:set var="translation">
														<digi:trn key="aim:addActivityStatusFirstLine">Please select a status from below</digi:trn>
													</c:set>
													<c:if test="${contentDisabled=='true'}">
	                                                	<category:showoptions   firstLine="${translation}" name="aimEditActivityForm" property="identification.statusId"  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.ACTIVITY_STATUS_KEY %>" styleClass="inp-text" outerdisabled="disabled" />
	                                                </c:if>
	                                                <c:if test="${contentDisabled=='false'}">
	                                                	<category:showoptions   firstLine="${translation}" name="aimEditActivityForm" property="identification.statusId"  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.ACTIVITY_STATUS_KEY %>" styleClass="inp-text" />
	                                                </c:if>
												</td>
											</tr>
											<tr>
												<td bgcolor="#ffffff">
												</td>
												<td bgcolor="#ffffff">
													<digi:trn key="aim:reasonsToChangeStatus">If there have been some changes in the status, explain below the reasons</digi:trn> :
													<a title="<digi:trn key="aim:ReasonforStatusofProject">Use this space to provide explanations as to why that status was selected. Used primarily in the case of cancelled and suspended projects</digi:trn>">
                                                    <br/>
													<html:textarea name="aimEditActivityForm" property="identification.statusReason" cols="60" rows="2" styleClass="inp-text"   disabled="${contentDisabled}"/>
													</a>												
												</td>
											</tr>
											
											<field:display name="Project Comments" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:CommentsOfProject">Comments realted to the whole project</digi:trn>">
												<digi:trn key="aim:projectComments">Project Comments</digi:trn>
												</a>
											</td>
											<td valign="top" align="left">
												<table cellPadding="0" cellSpacing="0">
													<tr>
														<td>
															<bean:define id="projcomKey">
																<c:out value="${aimEditActivityForm.identification.projectComments}"/>
															</bean:define>
															<digi:edit key="<%=projcomKey%>"/>
														</td>
													</tr>
													<tr>
														<td>
															<a href="javascript:edit('${projcomKey}','Project Comments')">
															<digi:trn key="aim:edit">Edit</digi:trn></a>															
														</td>
													</tr>
												</table>
											</td></tr>
											</field:display>
																						
											<field:display name="Objective" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:ObjectivesAndComponentsofProject">The key objectives and main components of the project</digi:trn>">
												<digi:trn key="aim:objective">Objective</digi:trn>
												</a>
											</td>
											<td valign="top" align="left">
												<table cellPadding=0 cellSpacing=0>
													<tr>
														<td>
															<bean:define id="objKey">
																<c:out value="${aimEditActivityForm.identification.objectives}"/>
															</bean:define>
															<digi:edit key="<%=objKey%>"/>
												
														</td>
													</tr>
													<tr>
														<td>
															<%--
															<a href="<c:out value="${aimEditActivityForm.context}"/>/editor/showEditText.do?id=<%=objKey%>&referrer=<c:out value="${aimEditActivityForm.context}"/>/aim/addActivity.do?edit=true">Edit</a>
															--%>
															
																<a href="javascript:edit('<%=objKey%>','objective')">
																<digi:trn key="aim:edit">Edit</digi:trn></a>	
																&nbsp;
															
														<field:display name="Objectively Verifiable Indicators" feature="Identification">
															<a href="javascript:commentWin('<digi:trn jsFriendly="true">Add/Edit Objectively Verifiable Indicators</digi:trn>','objObjVerIndicators')" id="CommentObjObjVerIndicators"><digi:trn>Add/Edit Objectively Verifiable Indicators</digi:trn></a>
															&nbsp;
														</field:display>
														<field:display name="Assumptions" feature="Identification">
															<a href="javascript:commentWin('<digi:trn jsFriendly="true">Add/Edit Assumption</digi:trn>','objAssumption')" id="CommentObjAssumption"><digi:trn>Add/Edit Assumption</digi:trn></a>
															&nbsp;
														</field:display>
														<field:display name="Verifications" feature="Identification">
															<a href="javascript:commentWin('<digi:trn jsFriendly="true">Add/Edit Verification</digi:trn>','objVerification')" id="CommentObjVerification"><digi:trn>Add/Edit Verification</digi:trn></a>
														</field:display>		
														</td>
													</tr>
												</table>												
											</td></tr>
											</field:display>
											
											<field:display name="Project Description" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:DescriptionofProject">Information describing the project</digi:trn>">
												<digi:trn key="aim:description">Project Description</digi:trn>
												</a>
											</td>
											<td valign="top" align="left">
												<table cellPadding=0 cellSpacing=0>
													<tr>
														<td>
															<bean:define id="descKey">
																<c:out value="${aimEditActivityForm.identification.description}"/>
															</bean:define>
			
															<digi:edit key="<%=descKey%>"/>
														</td>
													</tr>
													<tr>
														<td>
															<%--
															<a href="<c:out value="${aimEditActivityForm.context}"/>/editor/showEditText.do?id=<%=descKey%>&referrer=<c:out value="${aimEditActivityForm.context}"/>/aim/addActivity.do?edit=true">Edit</a>
															--%>
																<a href="javascript:edit('<%=descKey%>','Project Description')">
																<digi:trn key="aim:edit">Edit</digi:trn></a>															
														</td>
													</tr>
												</table>
											</td></tr>
											</field:display>
											
											<!-- Purpose -->
											<field:display name="Purpose" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:PurposeofProject">Purpose of the project</digi:trn>">
												<digi:trn key="aim:purpose">
												Purpose
												</digi:trn>
												</a>
											</td>
											
											<td valign="top" align="left">
												<table cellPadding=0 cellSpacing=0>
												
													<tr>
														<td>
															<bean:define id="purpKey">
																<c:out value="${aimEditActivityForm.identification.purpose}"/>
															</bean:define>
			
															<digi:edit key="<%=purpKey%>"/>
														</td>
													</tr>
													<tr>
														<td>
															<%--
															<a href="<c:out value="${aimEditActivityForm.context}"/>/editor/showEditText.do?id=<%=descKey%>&referrer=<c:out value="${aimEditActivityForm.context}"/>/aim/addActivity.do?edit=true">Edit</a>--%>
																<a href="javascript:edit('<%=purpKey%>','Purpose')">
																<digi:trn key="aim:edit">Edit</digi:trn></a>
																&nbsp;
															<field:display name="Purpose Verifiable Indicators" feature="Identification">
																<a href="javascript:commentWin('<digi:trn jsFriendly="true">Add/Edit Objectively Verifiable Indicators</digi:trn>','purpObjVerIndicators')" id="CommentPurpObjVerInd"><digi:trn>Add/Edit Objectively Verifiable Indicators</digi:trn></a>
																&nbsp;
															</field:display>
															<field:display name="Purpose Assumptions" feature="Identification">
																<a href="javascript:commentWin('<digi:trn jsFriendly="true">Add/Edit Assumption</digi:trn>','purpAssumption')" id="CommentPurpAssumption"><digi:trn>Add/Edit Assumption</digi:trn></a>
																&nbsp;
															</field:display>
															<field:display name="Purpose Verifications" feature="Identification">
																<a href="javascript:commentWin('<digi:trn jsFriendly="true">Add/Edit Verification</digi:trn>','purpVerification')" id="CommentPurpVerification"><digi:trn>Add/Edit Verification</digi:trn></a>
															</field:display>
														</td>
													</tr>
												</table>
											</td></tr>
											</field:display>

											<field:display name="Results" feature="Identification">
											<!-- Results -->
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:ResultsofProject">Results of the project</digi:trn>">
												<digi:trn key="aim:results">
												Results
												</digi:trn>
												</a>
											</td>

											<td valign="top" align="left">
												<table cellPadding=0 cellSpacing=0>
													<tr>
														<td>
															<bean:define id="resKey">
																<c:out value="${aimEditActivityForm.identification.results}"/>
															</bean:define>
			
															<digi:edit key="<%=resKey%>"/>
														</td>
													</tr>
													<tr>
														<td>
															<%--
															<a href="<c:out value="${aimEditActivityForm.context}"/>/editor/showEditText.do?id=<%=descKey%>&referrer=<c:out value="${aimEditActivityForm.context}"/>/aim/addActivity.do?edit=true">Edit</a>--%>
			
															<a href="javascript:edit('<%=resKey%>','Results')">
															<digi:trn key="aim:edit">Edit</digi:trn></a>
																&nbsp;
															<field:display name="Results Verifiable Indicators" feature="Identification">
																<a href="javascript:commentWin('<digi:trn jsFriendly="true">Add/Edit Objectively Verifiable Indicators</digi:trn>','resObjVerIndicators')" id="CommentResObjVerInd"><digi:trn>Add/Edit Objectively Verifiable Indicators</digi:trn></a>
															&nbsp;
															</field:display>
															<field:display name="Results Assumptions" feature="Identification">
																<a href="javascript:commentWin('<digi:trn key="aim:addEditAssumption" jsFriendly="true">Add/Edit Assumption</digi:trn>','resAssumption')" id="CommentResAssumption"><digi:trn key="aim:addEditAssumption">Add/Edit Assumption</digi:trn></a>
																&nbsp;
															</field:display>
															<field:display name="Results Verifications" feature="Identification">
																<a href="javascript:commentWin('<digi:trn jsFriendly="true">Add/Edit Verification</digi:trn>','resVerification')" id="CommentResVerification"><digi:trn>Add/Edit Verification</digi:trn></a>
															</field:display>
														</td>
													</tr>
												</table>
											</td></tr>
											</field:display>
											
											<bean:define id="largeTextFeature" value="Identification" toScope="request"/>
										
											<bean:define id="largeTextLabel" value="Lessons Learned" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.identification.lessonsLearned}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyEdit.jsp"/>
			
											<bean:define id="largeTextLabel" value="Project Impact" toScope="request"/>
											
												<bean:define id="largeTextKey" toScope="request">
													<c:out value="${aimEditActivityForm.identification.projectImpact}"/>
												</bean:define>
											<field:display name="Project Impact" feature="Identification">
											<tr bgcolor="#ffffff">
												<td valign="top" align="left">
												
										            <a title="<digi:trn key="aim:EditProjectImpact">Edit Project Impact</digi:trn>">
													     <digi:trn key="aim:${largeTextLabel}">${largeTextLabel}</digi:trn> 
													</a>
									            
												</td>
												<td valign="top" align="left">
												<table cellPadding=0 cellSpacing=0>
													<tr>
														<td><digi:edit key="${largeTextKey}" /></td>
													</tr>
													<tr>
														<td><a href="javascript:edit('${largeTextKey}','Project Impact')"><digi:trn key="aim:edit">Edit</digi:trn></a></td>
													</tr>
												</table>
												</td>
											</tr>
											</field:display>											





											<bean:define id="largeTextLabel" value="Activity Summary" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.identification.activitySummary}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyEdit.jsp"/>
			
											<bean:define id="largeTextLabel" value="Contracting Arrangements" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.identification.contractingArrangements}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyEdit.jsp"/>
			
											<bean:define id="largeTextLabel" value="Conditionality and Sequencing" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.identification.condSeq}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyEdit.jsp"/>
			
											<bean:define id="largeTextLabel" value="Linked Activities" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.identification.linkedActivities}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyEdit.jsp"/>
			
											<bean:define id="largeTextLabel" value="Conditionalities" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.identification.conditionality}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyEdit.jsp"/>
			
											<bean:define id="largeTextLabel" value="Project Management" toScope="request"/>
											<bean:define id="largeTextKey" toScope="request">
												<c:out value="${aimEditActivityForm.identification.projectManagement}"/>
											</bean:define>
											<jsp:include page="largeTextPropertyEdit.jsp"/>
											
											
											
																		
											<field:display name="Project Implementing Unit" feature="Identification">
												<tr bgcolor="#ffffff">
													<td valign="top" align="left">
														<a title="<digi:trn>Information about project implementation unit</digi:trn>">
															<digi:trn>Project Implementing Unit</digi:trn>
														</a>
													</td>
													<td valign="top" align="left">
															<c:set var="translation">
																<digi:trn>Please select from below</digi:trn>
															</c:set>															
															<category:showoptions firstLine="${translation}" name="aimEditActivityForm" property="identification.projectImplUnitId" keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.PROJECT_IMPLEMENTING_UNIT_KEY %>" styleClass="inp-text" />
													</td>
												</tr>
											</field:display>
											
											
											<field:display name="Accession Instrument" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:DescriptionOfAccessionInstrument">Accession Instrument of the project</digi:trn>">
												<digi:trn key="aim:AccessionInstrument">
												Accession Instrument
												</digi:trn>
												</a>
											</td>
											<td valign="top" align="left">
													<c:set var="translation">
														<digi:trn key="aim:addActivityAccInstrFirstLine">Please select from below</digi:trn>
													</c:set>
													
													<category:showoptions firstLine="${translation}" name="aimEditActivityForm" property="identification.accessionInstrument" categoryName="<%= org.digijava.module.categorymanager.util.CategoryConstants.ACCESSION_INSTRUMENT_NAME %>" styleClass="inp-text" />
											</td></tr>	
											</field:display>

											<field:display name="Project Category" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:DescriptionOfProjectCategory">Project Category</digi:trn>">
												<digi:trn key="aim:ProjectCategory">
												Project Category
												</digi:trn>
												</a>
											</td>
											
											<td valign="top" align="left">
													<c:set var="translation">
														<digi:trn key="aim:addActivityAccInstrFirstLine">Please select from below</digi:trn>
													</c:set>
													<category:showoptions firstLine="${translation}" name="aimEditActivityForm" property="identification.projectCategory" categoryName="<%= org.digijava.module.categorymanager.util.CategoryConstants.PROJECT_CATEGORY_NAME %>" styleClass="inp-text" />													
											</td></tr>	
											</field:display>


											<field:display name="Government Agreement Number" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:step1:GovernmentAgreementNumTooltip">Government Agreement Number</digi:trn>">
												<digi:trn key="aim:step1:GovernmentAgreementNumTitle">
												Government Agreement Number
												</digi:trn>
												</a>
											</td>
											<td valign="top" align="left">
												<html:text name="aimEditActivityForm" property="identification.govAgreementNumber" styleClass="inp-text"/>
											</td></tr>	
											</field:display>
											
											
											<field:display name="Budget Code Project ID" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:step1:BudgetCodeProjectID">Budget Code Project ID</digi:trn>">
												<digi:trn key="aim:step1:BudgetCodeProjectID">
												Budget Code Project ID
												</digi:trn>
												</a>
											</td>
											<td valign="top" align="left">
											<div id="myAutoComplete">
												<html:text name="aimEditActivityForm" styleId="myInput" property="identification.budgetCodeProjectID" styleClass="inp-text" style="Font-size:11px;width:96%;"/>
												<div id="myContainer"></div>
											</div>	
											</td></tr>	
											</field:display>
											
											<field:display name="A.C. Chapter" feature="Identification">
											<tr bgcolor="#ffffff"><td valign="top" align="left">
												<a title="<digi:trn key="aim:DescriptionofACChapter">A.C. Chapter of the project</digi:trn>">
												<digi:trn key="aim:acChapter">
												A.C. Chapter
												</digi:trn>
												</a>
											</td>
											<td valign="top" align="left">
													<c:set var="translation">
														<digi:trn key="aim:addActivityAcChapterFirstLine">Please select from below</digi:trn>
													</c:set>
													<category:showoptions firstLine="${translation}" name="aimEditActivityForm" property="identification.acChapter" categoryName="<%= org.digijava.module.categorymanager.util.CategoryConstants.ACCHAPTER_NAME %>" styleClass="inp-text" />
											</td></tr>											
											</field:display>
											
											<feature:display name="Budget" module="Project ID and Planning">
											
											
											<tr bgcolor="#ffffff">
												<td valign="top" align="left">

													<a title="<digi:trn key="aim:DescriptionofProject">Summary information describing the project</digi:trn>">
														<digi:trn key="aim:actBudget">Activity Budget</digi:trn>
													</a>
												</td>
												<td>
												<field:display name="On/Off/Treasure Budget" feature="Budget">
													<c:set var="noanswer">
														<digi:trn>No Answer</digi:trn>
													</c:set>
											 		<category:showoptions firstLine="${noanswer}" name="aimEditActivityForm" property="identification.budgetCV" outeronchange="budgetCheckboxClick();"
											 		  keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.ACTIVITY_BUDGET_KEY %>" styleClass="inp-text" outerid="budgetCV" />	
												</field:display>
												</td>
											</tr>
											<tr bgcolor="#ffffff"/>
											<tr bgcolor="#ffffff">
											<td>
											</td>
											
											<td>
											<table id="budgetTable" class="box-border" cellSpacing=1 cellPadding=1 border=0 width="350">
											<%--
												<field:display name="Imputation" feature="Budget">
												<tr valign="top" id="Imputation" align="left"  >
													<td>
														<a title="<digi:trn>Imputation</digi:trn>">
														<digi:trn>
															Imputation
														</digi:trn>
														</a>
													</td>
													<td align="right">
														<html:text property="identification.FY" size="18" styleId="ImputationField" styleClass="inp-text" onkeyup="imputationRules.check();"/>
													</td>
												</tr>
												</field:display>
											 --%>
											
											
											<field:display name="Code Chapitre" feature="Budget">
											<tr valign="top" id="CodeChapitre" align="left"  >
												<td>
													<a title="<digi:trn>Code Chapitre</digi:trn>">
													<digi:trn>
														Code Chapitre
													</digi:trn>
													</a>
												</td>
												<td align="right">
													<html:text property="identification.projectCode" size="18" styleId="CodeChapitreField" styleClass="inp-text" onkeyup="codeChapitreRules.check();"/>
												</td>
											</tr>
											</field:display>
											
											<field:display name="FY" feature="Budget">
											<tr valign="top" id="FY" align="left" >
												<td>
													<a title="<digi:trn key="aim:FY">FY</digi:trn>">
													<digi:trn key="aim:actFY">
													FY
													</digi:trn>
													</a>
												</td>
												<td align="right">
													<html:select name="aimEditActivityForm" property="identification.selectedFYs" multiple="true" styleId="FYsSel" size="5" style="width:120px">
															<logic:empty name="aimEditActivityForm" property="identification.yearsRange">
																<html:option value="0"><digi:trn>Not applicable</digi:trn></html:option>
															</logic:empty>					                        			
						                        			<logic:notEmpty name="aimEditActivityForm" property="identification.yearsRange">
						                        				<html:optionsCollection name="aimEditActivityForm" property="identification.yearsRange" value="value" label="label"/>
						                        			</logic:notEmpty>						                        			
						                			</html:select>
												
													
													<%-- <html:text property="identification.FY" size="18" styleClass="inp-text"/> --%>
													
												</td>
											</tr>
											</field:display>
											
										<field:display name="Vote" feature="Budget" >
											<tr valign="top"  id="Vote" align="left" >
												<td>
												<field:display name="Validate Mandatory Vote" feature="Budget">
	 	 		                                	<FONT color="red">*</FONT>
	                                            </field:display>
													<a title="<digi:trn key="aim:Vote">Vote</digi:trn>">
													<digi:trn key="aim:actVote">
													Vote
													</digi:trn>
													</a>
												</td>
												<td align="right">
													<html:text property="identification.vote" size="18" styleClass="inp-text"/>
												</td>
											</tr>	
											</field:display>
											
											<field:display name="Sub-Vote" feature="Budget">
											<tr valign="top" id="Sub-Vote" align="left" >
												<td>
												<field:display name="Validate Mandatory Sub-Vote" feature="Budget">
	 	 		                                	<FONT color="red">*</FONT>
	                                            </field:display>
													<a title="<digi:trn key="aim:Sub-Vote">Sub-Vote</digi:trn>">
													<digi:trn key="aim:actSub-Vote">
													Sub-Vote
													</digi:trn>
													</a>
												</td>
												<td align="right">
													<html:text property="identification.subVote" size="18" styleClass="inp-text"/>
												</td>
											</tr>
										</field:display>
											
									<field:display name="Sub-Program" feature="Budget">
										<tr valign="top" id="Sub-Program" align="left">
											<td>
											<field:display name="Validate Mandatory Sub-Program" feature="Budget">
	 	 		                            	<FONT color="red">*</FONT>
	                                         </field:display>
												<a title="<digi:trn key="aim:Sub_Program">Sub-Program</digi:trn>">
												<digi:trn key="aim:actSubProgram">
												Sub-Program
												</digi:trn>
												</a>
											</td>
											<td align="right">
												<html:text property="identification.subProgram" size="18" styleClass="inp-text"/>
											</td>
										</tr>
									</field:display>
											
									<field:display name="Project Code" feature="Budget">
										<tr valign="top" id="ProjectCode" align="left" >
											<td>
											<field:display name="Validate Mandatory Project Code" feature="Budget">
	 	 		                                	<FONT color="red">*</FONT>
	                                        </field:display>
												<a title="<digi:trn key="aim:ProjectCode">Project Code</digi:trn>">
												<digi:trn key="aim:actProjectCode">
												Project Code
												</digi:trn>
												</a>
											</td>
											<td align="right">
												<html:text property="identification.projectCode" size="18" styleClass="inp-text"/>
											</td>
										</tr>
									</field:display>
								 <tr id="CodeChapitreDrop">
									<field:display name="Code Chapitre Dropdown" feature="Budget">
										<td>										
											 <html:select property="identification.chapterYear" styleClass="inp-text" onchange="submitAfterSelectingChapterYear();">
											 	<html:option value="0"><digi:trn>Select Code Year</digi:trn></html:option>
											 	<html:optionsCollection property="identification.chapterYears" value="wrappedInstance" label="wrappedInstance"/>
											 </html:select>
										</td>
										<td>
											<logic:present name="aimEditActivityForm" property="identification.chapterCodes"> 
											<html:select property="identification.chapterCode" styleClass="inp-text">
											 	<html:option value="0">Select Code Chapitre</html:option>
											 	<html:optionsCollection property="identification.chapterCodes" value="wrappedInstance" label="wrappedInstance"/>
											 </html:select>
											 </logic:present>
										</td>
									</field:display>
								</tr>
							</table>
											<!-- Budget classification -->
											<field:display name="Budget Classification" feature="Budget">		
											<tr bgcolor="#ffffff">
												<td valign="top" align="left" id="bctd" >
													<a title="<digi:trn key="aim:DescriptionofProject">Summary information describing the project</digi:trn>">
														<digi:trn>Budget Classification</digi:trn>
													</a>
												</td>
												<td id="bctd1">
													<table class="box-border" cellSpacing=1 cellPadding=1 border=0 width="350">
                                                  		<field:display name="Budget Sector" feature="Budget">
                                                  		<tr>
                                                  			<td width="75" id="budgsector1">
                                                  				<digi:trn>Sector</digi:trn>
                                                  			</td>
		                                                    <td>
		                                                    	<html:select styleId="budgsector" name="aimEditActivityForm" styleClass="inp-text" property="identification.selectedbudgedsector" onchange="getBudgetOptions(this.value,'orgselect');" style="max-width:250; min-width:250;">
		                                                    		<html:option value="0"><digi:trn>Select</digi:trn></html:option>
		                                                    		<html:optionsCollection name="aimEditActivityForm" property="identification.budgetsectors" value="idsector" label="sectorname"/>
		                                                    	</html:select>
		                                                    </td>
		                                                 </tr>
		                                                 </field:display>
		                                                 <field:display name="Budget Organization" feature="Budget">
		                                                 <tr>
		                                                 	<td width="75" id="budgetorg1">
                                                  				<digi:trn>Organization</digi:trn>
                                                  			</td>
		                                                    <td>
		                                                    	<html:select  name="aimEditActivityForm" styleClass="inp-text" property="identification.selectedorg" styleId="budgetorg" onchange="getBudgetOptions(this.value,'depselect')" style="max-width:250; min-width:250;">
      																<html:option value="0"><digi:trn>Select</digi:trn></html:option>
      																<html:optionsCollection name="aimEditActivityForm" property="identification.budgetorgs" value="ampOrgId" label="name"/>
    															</html:select>
		                                                    </td>
		                                                 </tr>
		                                                 </field:display>
		                                                 <field:display name="Budget Department" feature="Budget">
		                                                <tr>
		                                                    <td width="75" id="budgetdepart1">
                                                  				<digi:trn>Department</digi:trn>
                                                  			</td>
		                                                    <td>
		                                                    	<html:select  name="aimEditActivityForm" styleClass="inp-text" property="identification.selecteddepartment" styleId="budgetdepart" style="max-width:250; min-width:250;">
      																<html:option value="0"><digi:trn>Select</digi:trn></html:option>
      																<html:optionsCollection name="aimEditActivityForm" property="identification.budgetdepartments" value="id" label="name"/>
    															</html:select>
    														</td>
    													</tr>
    													</field:display>
    													<field:display name="Budget Program" feature="Budget">
    													<tr>
    														<td width="75" id="budgetprog1">
                                                  				<digi:trn>Program</digi:trn>
                                                  			</td>
		                                                    <td>
		                                                    	<html:select  name="aimEditActivityForm" styleClass="inp-text" property="identification.selectedprogram" styleId="budgetprog" style="max-width:250; min-width:250;">
      																<html:option value="0"><digi:trn>Select</digi:trn></html:option>
      																<html:optionsCollection name="aimEditActivityForm" property="identification.budgetprograms" value="ampThemeId" label="name"/>
    															</html:select>
		                                                    </td>
                                                  		</tr>
                                                  		</field:display>
                                                	</table>
												</td>
											</tr>
											</field:display>
											<!--End  Budget classification -->
										</td>
									</tr>
								</feature:display>
								
								<field:display name="Financial Instrument" feature="Budget">
										<tr bgcolor="#ffffff"><td valign="top" align="left" >
											<a title="<digi:trn key="aim:GBS">Financial Instrument</digi:trn>">
											<digi:trn key="aim:actGBS">
												Financial Instrument
												</digi:trn>
												</a>
											</td>
										<td valign="top" align="left" >
											<category:showoptions listView="false" name="aimEditActivityForm" property="identification.gbsSbs" categoryName="<%=org.digijava.module.categorymanager.util.CategoryConstants.FINANCIAL_INSTRUMENT_NAME %>" styleClass="inp-text" />
										</td>
									</tr>
								</field:display>	
								<field:display name="Government Approval Procedures" feature="Budget">
										<tr bgcolor="#ffffff"><td valign="top" align="left">
											<a title="<digi:trn key="aim:governmentApprovalProcedures">Government Approval Procedures </digi:trn>">
											<digi:trn key="aim:actGovernmentApprovalProcedures">
												Government Approval Procedures 
												</digi:trn>
												</a>
											</td>
										<td valign="top" align="left">
												<digi:trn key="aim:yes">Yes</digi:trn><html:radio name="aimEditActivityForm" property="identification.governmentApprovalProcedures" value="true"/> &nbsp;&nbsp;<digi:trn key="aim:no">No</digi:trn><html:radio name="aimEditActivityForm" property="identification.governmentApprovalProcedures" value="false"/>
									</td></tr>
								</field:display>	
								
								<field:display name="Joint Criteria" feature="Budget">
										<tr bgcolor="#ffffff"><td valign="top" align="left">
											<a title="<digi:trn key="aim:jointCriteria">Joint Criteria</digi:trn>">
											<digi:trn key="aim:actJointCriteria">
												Joint Criteria 
												</digi:trn>
												</a>
											</td>
										<td valign="top" align="left">
												<digi:trn key="aim:yes">Yes</digi:trn><html:radio property="identification.jointCriteria" value="true"/> &nbsp;&nbsp;<digi:trn key="aim:no">No</digi:trn><html:radio property="identification.jointCriteria" value="false"/>
									</td></tr>
								</field:display>
								
								<field:display name="Humanitarian Aid" feature="Identification">
									<tr bgcolor="#ffffff">
										<td valign="top" align="left">
											<a title="<digi:trn key="aim:humanitarianAid">Humanitarian Aid</digi:trn>">
												<digi:trn key="aim:humanitarianAid">
													Humanitarian Aid 
												</digi:trn>
											</a>
										</td>
										<td valign="top" align="left">
												<digi:trn key="aim:yes">Yes</digi:trn><html:radio property="identification.humanitarianAid" value="true"/> &nbsp;&nbsp;<digi:trn key="aim:no">No</digi:trn><html:radio property="identification.humanitarianAid" value="false"/>
										</td>
									</tr>
								</field:display>	

								<field:display name="Cris Number" feature="Identification">
									<tr bgcolor="#ffffff">
										<td valign="top" align="left">
											<a title="<digi:trn key="aim:crisNumber">Cris Number</digi:trn>">
												<digi:trn key="aim:crisNumber">
													Cris Number 
												</digi:trn>
											</a>
										</td>
										<td valign="top" align="left">
										    <html:text name="aimEditActivityForm" property="identification.crisNumber" size="12" styleClass="inp-text" />
										</td>
									</tr>
								</field:display>	

								<field:display name="Procurement System" feature="Identification">
									<tr bgcolor="#ffffff"><td valign="top" align="left">
										<a title="<digi:trn key="aim:ProcurementSystem">Procurement System</digi:trn>">
										<digi:trn key="aim:ProcurementSystem">
										Procurement System
										</digi:trn>
										</a>
									</td>
									<td valign="top" align="left">
											<c:set var="translation">
												<digi:trn key="aim:addActivityProcSystemFirstLine">Please select from below</digi:trn>
											</c:set>
											
											<category:showoptions firstLine="${translation}" name="aimEditActivityForm" property="identification.procurementSystem" categoryName="<%= org.digijava.module.categorymanager.util.CategoryConstants.PROCUREMENT_SYSTEM_NAME %>" styleClass="inp-text" />
									</td></tr>	
								</field:display>

								<field:display name="Reporting System" feature="Identification">
									<tr bgcolor="#ffffff"><td valign="top" align="left">
										<a title="<digi:trn key="aim:ReportingSystem">Reporting System</digi:trn>">
										<digi:trn key="aim:ReportingSystem">
										Reporting System
										</digi:trn>
										</a>
									</td>
									<td valign="top" align="left">
											<c:set var="translation">
												<digi:trn key="aim:addActivityRepSystemFirstLine">Please select from below</digi:trn>
											</c:set>
											
											<category:showoptions firstLine="${translation}" name="aimEditActivityForm" property="identification.reportingSystem" categoryName="<%= org.digijava.module.categorymanager.util.CategoryConstants.REPORTING_SYSTEM_NAME %>" styleClass="inp-text" />
									</td></tr>	
								</field:display>

								<field:display name="Audit System" feature="Identification">
									<tr bgcolor="#ffffff"><td valign="top" align="left">
										<a title="<digi:trn key="aim:AuditSystem">Audit System</digi:trn>">
										<digi:trn key="aim:AuditSystem">
										Audit System
										</digi:trn>
										</a>
									</td>
									<td valign="top" align="left">
											<c:set var="translation">
												<digi:trn key="aim:addActivityAuditSystemFirstLine">Please select from below</digi:trn>
											</c:set>
											
											<category:showoptions firstLine="${translation}" name="aimEditActivityForm" property="identification.auditSystem" categoryName="<%= org.digijava.module.categorymanager.util.CategoryConstants.AUDIT_SYSTEM_NAME %>" styleClass="inp-text" />
									</td></tr>	
								</field:display>

								<field:display name="Institutions" feature="Identification">
									<tr bgcolor="#ffffff"><td valign="top" align="left">
										<a title="<digi:trn key="aim:Institutions">Institutions</digi:trn>">
										<digi:trn key="aim:Institutions">
										Institutions
										</digi:trn>
										</a>
									</td>
									<td valign="top" align="left">
											<c:set var="translation">
												<digi:trn key="aim:addActivityProcSystemFirstLine">Please select from below</digi:trn>
											</c:set>
											
											<category:showoptions firstLine="${translation}" name="aimEditActivityForm" property="identification.institutions" categoryName="<%= org.digijava.module.categorymanager.util.CategoryConstants.INSTITUTIONS_NAME %>" styleClass="inp-text" />
									</td></tr>	
								</field:display>
								
						<script>
							InitBud();
						</script>

<script type="text/javascript">
	var myArray = [
	   	<c:forEach var="relAct" items="${aimEditActivityForm.identification.budgetCodes}">
			"<bean:write name="relAct" filter="true"/>",
		</c:forEach>     
	];

	YAHOO.example.ACJSArray = new function() {
		// Instantiate JS Array DataSource
	    this.oACDS = new YAHOO.widget.DS_JSArray(myArray);
	    // Instantiate AutoComplete
	    this.oAutoComp = new YAHOO.widget.AutoComplete("myInput", "myContainer", this.oACDS);
	    //this.oAutoComp.prehighlightClassName = "yui-ac-prehighlight";    
	    this.oAutoComp.useShadow = true;
	    //this.oAutoComp.forceSelection = true;
        this.oAutoComp.maxResultsDisplayed = myArray.length; 
	    this.oAutoComp.formatResult = function(oResultItem, sQuery) {
	        var sMarkup = oResultItem[0];
	        return (sMarkup);
	    };
	}; 

</script>
									