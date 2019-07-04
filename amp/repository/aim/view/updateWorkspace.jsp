<%@ page pageEncoding="UTF-8" %>
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>
<%@ page import="org.digijava.kernel.request.TLSUtils" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>


<%	
	ReportContextData.createWithId(request.getSession(), ReportContextData.REPORT_ID_WORKSPACE_EDITOR, false);
	request.setAttribute(ReportContextData.BACKUP_REPORT_ID_KEY, ReportContextData.REPORT_ID_WORKSPACE_EDITOR);
	
%>

<html:javascript formName="aimUpdateWorkspaceForm"/>


<digi:instance property="aimUpdateWorkspaceForm" />
<digi:context name="digiContext" property="context" />
<div id="popin" class="invisible-item">
    <div id="popinContent" class="content">
    </div>
</div>

<style type="text/css">
   #popin .content {
        overflow:auto;
        height:500px;
        background-color:#ffffff;
        padding:10px;
    }
    
    fieldset.main_side_cont.disabled{
    	background-color: #f1f1f1;
    }

    fieldset.main_side_cont.disabled div, fieldset.main_side_cont.disabled table{
    	background-color: #f1f1f1;
    	color: #D1D1D1;
    }
    
	fieldset.main_side_cont.disabled a, fieldset.main_side_cont.disabled button, fieldset.main_side_cont.disabled input{
		cursor: default;
    }
        
    fieldset.main_side_cont.disabled legend{
    	background-color: #D1D1D1;
    }
 </style>

<link rel="stylesheet" type="text/css" href="<digi:file src= '/repository/aim/view/scripts/jquery-ui-1.11.0/jquery-ui.min.css'/>">
<link rel="stylesheet" type="text/css" href="/repository/aim/view/css/filters/filters2.css">
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/prefilters.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/filters/filters.js'/>?version=fantastic_15" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/filters/searchManager.js'/>" ></script>

<c:set var="filterPanelName">
	<digi:trn key="rep:filter:filters">Filters</digi:trn>
</c:set>
<c:set var="failureMessage">
	<digi:trn key="aim:reportwizard:connectionProblems">Apparently there are some connection problems. Please try again in a few moments.</digi:trn>
</c:set>
<c:set var="filterProblemsMessage">
	<digi:trn key="aim:reportwizard:filterProblems">Apparently there are some problems displaying filters pop-up. Please try again.</digi:trn>
</c:set>
<c:set var="loadingDataMessage">
	<digi:trn key="aim:reportwizard:loadingData">Loading...</digi:trn>
</c:set>
<c:set var="cannotSaveFiltersMessage">
	<digi:trn key="aim:reportwizard:cannotSaveFilters">There was a problem saving the filters. Please try again.</digi:trn>
</c:set>
<c:set var="settingsPanelName">
	<digi:trn key="rep:filter:filters">Settings</digi:trn>
</c:set>
<c:set var="savingDataMessage">
	<digi:trn key="aim:reportwizard:savingData">Saving data. Please wait.</digi:trn>
</c:set>

<script type="text/javascript">

    var isRtl = <%=TLSUtils.getCurrentLocale().getLeftToRight() == false%>;
    var language = '<%=TLSUtils.getCurrentLocale().getCode()%>';
    var region = '<%=TLSUtils.getCurrentLocale().getRegion()%>';
	
	YAHOO.util.Event.onDOMReady(initComputationsFields);
    
	YAHOO.namespace("YAHOO.amp");

    var myPanel = new YAHOO.widget.Panel("newpopins", {
        width:"600px",
        fixedcenter: true,
        constraintoviewport: false,
        underlay:"none",
        close:true,
        visible:false,
        modal:true,
        draggable:true,
        context: ["showbtn", "tl", "bl"],
        effect:{effect:YAHOO.widget.ContainerEffect.FADE, duration: 0.5}
    });
    var panelStart=0;
    var checkAndClose=false;
    function initOrganizationScript() {
        var msg='\n<digi:trn jsFriendly="true">Add Organizations</digi:trn>';
        myPanel.setHeader(msg);
        myPanel.setBody("");
        myPanel.beforeHideEvent.subscribe(function() {
            panelStart=1;
        });

        myPanel.render(document.body);
    }
    function showPanelLoading(msg){
        myPanel.setHeader(msg);
        var content = document.getElementById("popinContent");
        content.innerHTML = '<div style="text-align: center">' +
            '<img src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader-darkblue.gif" border="0" height="17px"/>&nbsp;&nbsp;' +
            '<digi:trn jsFriendly="true">Loading...</digi:trn><br/><br/></div>';
            showContent();
    }
    function mapCallBack(status, statusText, responseText, responseXML){
        window.location.reload();
    }


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
        var content = document.getElementById("popinContent");
        //response = response.split("<!")[0];
        content.innerHTML = response;
        //content.style.visibility = "visible";

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
        var element = document.getElementById("popin");
        element.style.display = "inline";
        if (panelStart < 1){
            myPanel.setBody(element);
        }
        if (document.getElementById('tempNumResults')) {
            document.getElementById('tempNumResults').value =
				TranslationManager.convertNumbersToEasternArabicIfNeeded(isRtl, language, region, document.getElementById('tempNumResults').value);
		}
       // alert(element)
        if (panelStart < 2){
            //document.getElementById("popin").scrollTop=0;
            myPanel.show();
            panelStart = 2;
        }
        checkErrorAndClose();
    }

     function checkErrorAndClose(){
     	if(checkAndClose==true){
        	if(document.getElementsByName("someError")[0]==null || document.getElementsByName("someError")[0].value=="false"){
            	myPanel.hide();
                panelStart=1;
            }
            checkAndClose=false;
            <digi:context name="addOrgs" property="context/childWorkspacesAdded.do?childorgs=true&dest=admin"/>
		    document.aimUpdateWorkspaceForm.action = "${addOrgs}";
            document.aimUpdateWorkspaceForm.submit();
            
        }            
     }
    
     
     function initComputationsFields(){
    	 $("input[name='useFilter']:radio").each(
    			 function(){
    				 if ($(this).attr("checked")){
    					 toggleFilterVsOrgs(this);				 
    				 }
    			 });
     }
     
     function toggleFilterVsOrgs(radio){
    	 var useFilter = radio.value == "true";

    	 enableFilter(useFilter);
    	 enableOrgs(!useFilter);
     }

     function enableFilter(enabled){
 
    	 var legend = $("input[name='useFilter'][value='true']:radio").parent();
    	 var fieldset =  legend.parent();
    	 
    	 if(enabled){
        	 $("#add_filters_button").removeAttr("disabled");
        	 $(fieldset).removeClass("disabled");
    	 }else{
        	 $("#add_filters_button").attr("disabled", "disabled");
        	 $(fieldset).addClass("disabled");
    	 };
     }

     function disabler(event) {
		    event.preventDefault();
		    return false;
	 }

     function enableOrgs(enabled){
    	 var legend = $("input[name='useFilter'][value='false']:radio").parent();
    	 var fieldset =  legend.parent();
    	 
    	 if(enabled){
    		 $(fieldset).find($("a")).each(function(){
        		 $(this).unbind("click", disabler);
        	 });
        	 
        	 $(fieldset).find($("input:button")).removeAttr("disabled");
        	 $(fieldset).removeClass("disabled");
    	 }else{
    		 $(fieldset).find($("a")).each(function(){
        		 $(this).bind("click", disabler);
        	 });
        	 $(fieldset).find($("input:button")).attr("disabled", "disabled");
        	 $(fieldset).addClass("disabled");
    	 };
     }
    
</script>
<jsp:include page="/repository/aim/view/addOrganizationPopin.jsp"  />

<digi:form action="/updateWorkspace.do" method="post" name="aimUpdateWorkspaceForm"
type="org.digijava.module.aim.form.UpdateWorkspaceForm"
onsubmit="return validateAimUpdateWorkspaceForm(this);">

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript">
<!--

YAHOO.util.Event.addListener(window, "load", initWorkspacePage) ;

function initWorkspacePage() {
	workspaceChangeType();
	computationChange();
	privateChange();
}

function toggleGroup(group_id){
	var strId='#'+group_id;
	$(strId+'_minus').toggle();
	$(strId+'_plus').toggle();
	$(strId+'_dots').toggle();
	$('#act_'+group_id).toggle('fast');
}

function addChildWorkspaces() {
		if (document.aimUpdateWorkspaceForm.workspaceType.value != "Team") {
			openNewWindow(650, 380);
			<digi:context name="addChild" property="context/module/moduleinstance/addChildWorkspaces.do" />
			document.aimUpdateWorkspaceForm.action = "<%=addChild%>?dest=admin";
			document.aimUpdateWorkspaceForm.target = popupPointer.name;
			document.aimUpdateWorkspaceForm.addFlag.value = false;
			document.aimUpdateWorkspaceForm.submit();
		} else {
			alert("<digi:trn key="aim:teamcaddmanagement">Workspace type must be MANAGEMENT to add teams</digi:trn>");
			document.aimUpdateWorkspaceForm.workspaceType.focus();
			return false;
		}
}

function addChildOrgs() {
			
			openNewWindow(650, 380);
			<digi:context name="addChild" property="context/module/moduleinstance/addChildWorkspaces.do" />
			document.aimUpdateWorkspaceForm.action = "<%=addChild%>?dest=admin&childorgs=true";
			document.aimUpdateWorkspaceForm.target = popupPointer.name;
			document.aimUpdateWorkspaceForm.addFlag.value = false;
			document.aimUpdateWorkspaceForm.submit();
}


function removeChildOrg(id) {
	var temp = confirm("<digi:trn key="aim:deletelinkedorganization">Do you want to delete this linked organization ?</digi:trn>");
	if(temp == false)
	{
			document.aimUpdateWorkspaceForm.submit();
			return false;
	}
	else
	{
	<digi:context name="update" property="context/module/moduleinstance/removeChildWorkspace.do" />
	document.aimUpdateWorkspaceForm.action = "<%=update%>?dest=admin&childorgs=true&tId="+id;
	document.aimUpdateWorkspaceForm.target = "_self";
	document.aimUpdateWorkspaceForm.addFlag.value = false;
	document.aimUpdateWorkspaceForm.submit();
	}
}



function removeChildWorkspace(id) {
	var temp = confirm("<digi:trn key="aim:deletechildworkspace">Do you want to delete this child workspace ?</digi:trn>");
	if(temp == true)
	{
		<digi:context name="update" property="context/module/moduleinstance/removeChildWorkspace.do" />
		document.aimUpdateWorkspaceForm.action = "<%=update%>?dest=admin&tId="+id;
		document.aimUpdateWorkspaceForm.target = "_self";
		document.aimUpdateWorkspaceForm.addFlag.value = false;
		document.aimUpdateWorkspaceForm.submit();
	}
}

function chekingWorkspaceTypeAndCatValue(action){
	if(action!="reset") {
		var index2  = document.aimUpdateWorkspaceForm.workspaceType.selectedIndex;
		var val2    = document.aimUpdateWorkspaceForm.workspaceType.options[index2].value;
		var msg='';
		/*
		if(val1 == "DONOR" && (val2 == "Team"|| val2=="Management")){
			msg+="<digi:trn key="aim:workspaceManager:selectDonorType">if you choose Donor Team Category, you must choose Donor workspace type and vice versa</digi:trn>";
			alert(msg);
			return false;
		}else if(val1 == "GOVERNMENT" && val2 == "Donor"){
			msg+="<digi:trn key="aim:workspaceManager:selectGivernmentType">if you choose Government Team Category, you must choose Team or Management workspace type and vice versa</digi:trn>";
			alert(msg);
			return false;
		} else return true;
		*/
	}
	return true;
}

function update1(action, tid){

	if (action == "editreset"){
		<digi:context name="update" property="context/module/moduleinstance/getWorkspace.do" />
		//document.aimUpdateWorkspaceForm.action = "<%=update%>~dest=admin&event="+action;
		//document.aimUpdateWorkspaceForm.target = "_self";
		//document.aimUpdateWorkspaceForm.submit();
		window.location="<%=update%>~dest=admin~event=edit~tId="+tid;
	}
	return true;
}

function update(action) {
	if (action == "reset"){
		<digi:context name="update" property="context/module/moduleinstance/createWorkspace.do" />
		document.aimUpdateWorkspaceForm.action = "<%=update%>~dest=admin&event="+action;
		document.aimUpdateWorkspaceForm.target = "_self";
		//document.aimUpdateWorkspaceForm.submit();
		window.location="<%=update%>?dest=admin&event="+action;
	}
	
	//alert("comput: "+document.aimUpdateWorkspaceForm.computation.value);
	//alert("addact: "+document.aimUpdateWorkspaceForm.addActivity.checked);
	//alert("orgs: "+document.aimUpdateWorkspaceForm.organizations.value);
	if(chekingWorkspaceTypeAndCatValue(action)==true){	
		var event	= document.aimUpdateWorkspaceForm.actionEvent.value;
		var relFlag = document.aimUpdateWorkspaceForm.relatedTeamFlag.value;
		if (event == "edit" && relFlag == "noedit") {

			var name = trim(document.aimUpdateWorkspaceForm.teamName.value);
			if (name == "" || name.length == 0) {
				alert("<digi:trn key="aim:teamnamerequired">Team name required</digi:trn>");
				document.aimUpdateWorkspaceForm.teamName.focus();
				return false;
			}
			else {
			
				document.aimUpdateWorkspaceForm.relatedTeamFlag.value = "no";
				document.aimUpdateWorkspaceForm.addFlag.value = false;
				<digi:context name="update" property="context/module/moduleinstance/updateWorkspace.do" />
				document.aimUpdateWorkspaceForm.action = "<%=update%>?dest=admin&event="+action;
				document.aimUpdateWorkspaceForm.target = "_self";
				document.aimUpdateWorkspaceForm.submit();
			}
		}
		else
		if (action != "reset"){
			document.aimUpdateWorkspaceForm.workspaceType.disabled = false;
			if (!validateAimUpdateWorkspaceForm(document.aimUpdateWorkspaceForm)){
				return false;
			}
			if (document.aimUpdateWorkspaceForm.computation.checked == true) {
				var selectedOrganizations = $('[name="selectedOrgId"]');
				var hasFilters = $.parseJSON(document.aimUpdateWorkspaceForm.hasFilters.value);
				if (!hasFilters && selectedOrganizations.length<=0){
					alert("<digi:trn key="aim:computation">Please add a filter or a children organization</digi:trn>");
					document.aimUpdateWorkspaceForm.computation.focus();
					return false;
				}
			}
			
		    if (action != "reset"){
			if (event == "add" || event == "edit") {
				if (relFlag == "set") {
					var index2  = document.aimUpdateWorkspaceForm.workspaceType.selectedIndex;
					var index3  = document.aimUpdateWorkspaceForm.relatedTeam.selectedIndex;
					var index4  = document.aimUpdateWorkspaceForm.typeId.selectedIndex;
					var val2    = document.aimUpdateWorkspaceForm.workspaceType.options[index2].value;
					var val3    = document.aimUpdateWorkspaceForm.relatedTeam.options[index3].value;
					var val4	= document.aimUpdateWorkspaceForm.relatedTeamFlag.value;
					var val5	= document.aimUpdateWorkspaceForm.typeId.options[index4].value;
					var lab5	= document.aimUpdateWorkspaceForm.typeId.options[index4].text;
					var bsize	= parseInt(document.aimUpdateWorkspaceForm.relatedTeamBilatCollSize.value, 10);
					var val6	= document.aimUpdateWorkspaceForm.relatedTeamFlag.value;
	
					if (val1 == "DONOR" && val2 == "Donor") {
						if (val5 == "0") {
								alert("<digi:trn key="aim:selectteamtype">Please select team type</digi:trn>");
								document.aimUpdateWorkspaceForm.typeId.focus();
								return false;
							}
							else if (val3 == "-1") {
								alert("<digi:trn key="aim:selectrelatedteam">Please select a related team</digi:trn>");
								document.aimUpdateWorkspaceForm.relatedTeam.focus();
								return false;
							}

						}
					}
				}
			}
		   
			document.aimUpdateWorkspaceForm.relatedTeamFlag.value = "no";
			document.aimUpdateWorkspaceForm.addFlag.value = false;
			<digi:context name="update" property="context/module/moduleinstance/updateWorkspace.do" />
			document.aimUpdateWorkspaceForm.action = "<%=update%>?dest=admin&event="+action;
			document.aimUpdateWorkspaceForm.target = "_self";
			document.aimUpdateWorkspaceForm.submit();
		}
		
	}	
}
  
function workspaceChangeType(){
	if(document.aimUpdateWorkspaceForm.workspaceType.value == "Team"){
		$("tr[id^='private_workspace']").show(); //show  private ws
		$("tr[id^='management_']").hide();
		$("tr[id^='team_']").show('fast');
		//document.aimUpdateWorkspaceForm.addActivity.checked = true;
		if(document.aimUpdateWorkspaceForm.computation.checked == true)
			$("tr[id^='computation_']").show('fast');
		else $("tr[id^='computation_']").hide();
	}

	if(document.aimUpdateWorkspaceForm.workspaceType.value == "Management"){
		//document.aimUpdateWorkspaceForm.addActivity.checked = false;
		document.aimUpdateWorkspaceForm.computation.checked = false;
			$("tr[id^='private_workspace']").hide(); //hide private ws
			//unchecking private workspace checkbox in case its selected
			$( "input[name='isolated']" ).prop('checked',false)
			$("tr[id^='team_']").hide();
			$("tr[id^='management_']").show('fast');
			$("tr[id^='computation_']").hide()
	}

}

function computationChange(){
	if(document.aimUpdateWorkspaceForm.computation.checked == true){
		$("tr[id^='computation_']").show('fast');
		$("[name='isolated']").attr("disabled", true); //Show private ws when its computation ws
		$("[name='isolated").attr('checked', false);
	} else {
		$("tr[id^='computation_']").hide();
		$("[name='isolated']").removeAttr("disabled");
	}
}

function privateChange() {
	if(document.aimUpdateWorkspaceForm.isolated.checked == true) {
		$("[name='isolated']").removeAttr("disabled");
		$("[name='computation']").attr('disabled', true);
		$("[name='computation").attr('checked', false);
		$("tr[id^='computation_']").hide();
	} else {
		$("[name='computation']").removeAttr("disabled");
	}
}

function relTeam() { 
	
	var index2  = document.aimUpdateWorkspaceForm.workspaceType.selectedIndex;
	var index3  = document.aimUpdateWorkspaceForm.typeId.selectedIndex;
	var val2    = document.aimUpdateWorkspaceForm.workspaceType.options[index2].value;
	var val3    = document.aimUpdateWorkspaceForm.typeId.options[index3].value;
	var val4	= document.aimUpdateWorkspaceForm.relatedTeamFlag.value;

	
	if (val1 == "DONOR" && val2 == "Donor") {
		if (val4 == "no") {
			if (val3 != "0") {
				document.aimUpdateWorkspaceForm.relatedTeamFlag.value = "yes";
				document.aimUpdateWorkspaceForm.addFlag.value = false;
				<digi:context name="update" property="context/module/moduleinstance/updateWorkspace.do" />
				document.aimUpdateWorkspaceForm.action = "<%=update%>";
				document.aimUpdateWorkspaceForm.target = "_self";
				document.aimUpdateWorkspaceForm.submit();
			}
			else
				return false;
		}
		if (document.getElementById("relTeamRow"))
			document.getElementById("relTeamRow").style.display = '';
		return false;
	}
	else {
		if (document.getElementById("relTeamRow"))
			document.getElementById("relTeamRow").style.display = 'none';
		return false;
	}
}
function cancel()
 {
//	document.aimUpdateWorkspaceForm.action = "/aim/workspaceManager.do";
//	document.aimUpdateWorkspaceForm = "_self";
//	document.aimUpdateWorkspaceForm.submit();
	window.location="/aim/workspaceManager.do";
	return true;
 }
-->
</script>

  <script>
  
  function showSiloDialog() {
	  if((document.aimUpdateWorkspaceForm.parentTeamName.value != "") && 
			  (document.aimUpdateWorkspaceForm.isolated.checked )) {
  		$( "#dialog-confirm" ).dialog( "open" );
	  } else {
      	update('edit');
	  }
  	
  	
  }
  
  
  $(function() {
    $( "#dialog-confirm" ).dialog({
    	   //resizable: false,
    	      //height:640,
    	      modal: true,
    	      autoOpen: false,
    	      buttons: {
    	        "Confirm": function() {
    	        	update('edit');
    	          //$( this ).dialog( "close" );
    	        },
    	        Cancel: function() {
    	          $( this ).dialog( "close" );
    	        }
    	      }

    	
    	
    });
  });
  </script>


<html:hidden property="teamId" />
<html:hidden property="actionEvent" />
<html:hidden property="id" />
<html:hidden property="mainAction" />
<html:hidden property="parentTeamName" value="${aimUpdateWorkspaceForm.parentTeamName}"/>
<html:hidden property="stepInWizard" value="1" />

<html:hidden property="relatedTeamFlag" />
<html:hidden property="addFlag" />
<html:hidden property="relatedTeamBilatCollSize" />

<input type="hidden" name="event">
<input type="hidden" name="dest">

<input type="hidden" name="currUrl">




<div id="dialog-confirm" title="Workspace marked as child">
  <p>This workspace is marked as the child of ${aimUpdateWorkspaceForm.parentTeamName}.</p>
  <p>Marking it as private will remove this workspace from ${aimUpdateWorkspaceForm.parentTeamName}.</p>
  <p>Are you sure you want to set this workspace as private?</p>
</div>

<table width="1000" cellPadding="0" cellSpacing="0" vAlign="top" align="center">
<tr><td vAlign="top" align="left">
<jsp:include page="teamPagesHeader.jsp"  />
</td></tr>
<tr><td vAlign="top" align="left">
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000 vAlign="top" class="left-align">
	<tr>
		<td class="r-dotted-lg left-align" valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<td height=33><span class=crumb>
						<digi:link href="/admin.do" styleClass="comment">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:link href="/workspaceManager.do" styleClass="comment">
						<digi:trn key="aim:workspaceManager">
						Workspace Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="add">
						<digi:trn key="aim:addTeam">Add Team</digi:trn>
						</logic:equal>
						<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="edit">
						<digi:trn key="aim:editTeam">Edit Team</digi:trn>
						</logic:equal>
						<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="delete">
						<digi:trn key="aim:deleteTeam">Delete Team</digi:trn>
						</logic:equal>
						<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="view">
						<digi:trn key="aim:viewTeam">View Team</digi:trn>
						</logic:equal></span>
					</td>
				</tr>
				<!--<tr>
					<td height=16 valign="center" width=571><span class=subtitle-blue><digi:trn key="aim:workspaceManager">Workspace Manager</digi:trn></span>
					</td>
				</tr>-->
				<tr>
					<td>
						<digi:errors />
					</td>
				</tr>
				<tr>
					<td noWrap width="100%" vAlign="top">
					<table width="100%" cellspacing="1" cellspacing="1">
					<tr><td noWrap width=750 vAlign="top">
						<table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="100%">
							<tr bgColor=#f4f4f2>
								<td valign="top">
									<table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="100%" border="0">
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<table border="0" cellpadding="0" cellspacing="0" width="100%">
													<tr bgColor=#f4f4f2>
														<td bgColor=#C7D4DB class=box-title height="25" align="center" style="font-size:12px; font-weight:bold;">
															<!-- Table title -->
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="add">
																<digi:trn key="aim:addTeam">Add Team</digi:trn>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="edit">
																<digi:trn key="aim:editTeam">Edit Team</digi:trn>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="delete">
																<digi:trn key="aim:deleteTeam">Delete Team</digi:trn>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="view">
																<digi:trn key="aim:viewTeam">View Team</digi:trn>
															</logic:equal>
															<!-- end table title -->
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td bgColor=#ffffff class=box-border>
												<table border="0" cellPadding=3 cellspacing="1" width="100%" bgcolor="#dddddd" class="workspace-table">
													<tr>
														<td width="150" class="right-align" bgcolor="#FFFFFF" style="font-size:12px; font-weight:bold;">
															<font color="red"><b>*</b></font>
															<digi:trn key="aim:teamName">Team Name</digi:trn>
														</td>
														<td class="left-align" bgcolor="#FFFFFF" style="font-size:12px; font-weight:bold;">
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="delete">
																<b><bean:write name="aimUpdateWorkspaceForm" property="teamName" /></b>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="view">
																<b><bean:write name="aimUpdateWorkspaceForm" property="teamName" /></b>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="edit">
																<html:text property="teamName" size="50" styleClass="inp-text" />
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="add">
																<html:text property="teamName" size="50" styleClass="inp-text" />
															</logic:equal>
														</td>
													</tr>
													<tr>
														<td width="150" class="right-align" bgcolor="#f4f4f2">
															<font color="red"><b>*</b></font>
															<digi:trn key="aim:workspaceGroup">Workspace Group</digi:trn>
														</td>
														<td class="left-align" bgcolor="#f4f4f2">
															<c:set var="translation">
																<digi:trn>Please select from below</digi:trn>
															</c:set>															
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="delete">
																<b><bean:write name="aimUpdateWorkspaceForm" property="workspaceGroup" /></b>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="view">
																<b><bean:write name="aimUpdateWorkspaceForm" property="workspaceGroup" /></b>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="edit">
																<category:showoptions firstLine="${translation}" name="aimUpdateWorkspaceForm" property="workspaceGroup" keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.WORKSPACE_GROUP_KEY %>" styleClass="inp-text" />
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="add">
																<category:showoptions firstLine="${translation}" name="aimUpdateWorkspaceForm" property="workspaceGroup" keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.WORKSPACE_GROUP_KEY %>" styleClass="inp-text" />
															</logic:equal>
														</td>
													</tr>
													
													<tr>
														<td class="right-align" bgcolor="#FFFFFF" style="font-size:12px; font-weight:bold;">
															<font color="red"><b>*</b></font>
															<digi:trn key="aim:workspaceType">Workspace Type</digi:trn>
														</td>
														<c:if test="${aimUpdateWorkspaceForm.actionEvent == 'add'}">
														<td class="left-align" bgcolor="#FFFFFF">
															<html:select property="workspaceType" styleClass="inp-text" onchange="workspaceChangeType()">
																<html:option value="-1"><digi:trn key="aim:selectWorkspace">-- Select Workspace --</digi:trn></html:option>
																<html:option value="Management"><digi:trn key="aim:management">Management</digi:trn></html:option>
																<html:option value="Team"><digi:trn key="aim:team">Team</digi:trn></html:option>
															</html:select>
														</td>
														</c:if>
														<c:if test="${aimUpdateWorkspaceForm.actionEvent != 'add'}">
														<td class="left-align" bgcolor="#FFFFFF" style="font-size:12px; font-weight:bold;">
															<c:choose>
																<c:when test="${aimUpdateWorkspaceForm.actionEvent == 'edit'}">
																	<c:choose>
																		<c:when test="${aimUpdateWorkspaceForm.relatedTeamFlag == 'noedit'}">
																			<b><bean:write name="aimUpdateWorkspaceForm" property="workspaceType" /></b>
																		</c:when>
																		<c:otherwise>
																			<html:select property="workspaceType" styleClass="inp-text" disabled="true" >
																				<html:option value="-1"><digi:trn key="aim:selectWorkspace">-- Select Workspace --</digi:trn></html:option>
																				<html:option value="Management" ><digi:trn key="aim:management">Management</digi:trn></html:option>
																				<html:option value="Team" ><digi:trn key="aim:team">Team</digi:trn></html:option>
																			</html:select>
																		</c:otherwise>
																	</c:choose>
																</c:when>
																<c:otherwise>
																	<b><bean:write name="aimUpdateWorkspaceForm" property="workspaceType" /></b>
																</c:otherwise>
															</c:choose>
														</td>
														</c:if>
													</tr>
													<feature:display name="FM Template" module="Workspace Manager" >   
                                                    <tr>
                                                        <td width="150" class="right-align" bgcolor="#f4f4f2">
                                                            <digi:trn>FM Template</digi:trn>
                                                        </td>
                                                        <td class="left-align" bgcolor="#f4f4f2">
                                                            <c:set var="translation">
                                                                <digi:trn>Select, if workspace will use a different FM template</digi:trn>
                                                            </c:set>
                                                            <logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="delete">
                                                                <b><bean:write name="aimUpdateWorkspaceForm" property="fmTemplate" /></b>
                                                            </logic:equal>
                                                            <logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="view">
                                                                <b><bean:write name="aimUpdateWorkspaceForm" property="fmTemplate" /></b>
                                                            </logic:equal>
                                                            <logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="edit">
                                                                <html:select property="fmTemplate" styleClass="inp-text">
                                                                    <html:option value="-1">${translation}</html:option>
                                                                    <logic:notEmpty name="aimUpdateWorkspaceForm" property="fmTemplateList" >
                                                                        <html:optionsCollection name="aimUpdateWorkspaceForm" property="fmTemplateList"
                                                                                                value="id" label="name" styleClass="COLOR: #cc0000;" />
                                                                    </logic:notEmpty>
                                                                </html:select>
                                                            </logic:equal>
                                                            <logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="add">
                                                                <html:select property="fmTemplate" styleClass="inp-text">
                                                                    <html:option value="-1">${translation}</html:option>
                                                                    <logic:notEmpty name="aimUpdateWorkspaceForm" property="fmTemplateList" >
                                                                        <html:optionsCollection name="aimUpdateWorkspaceForm" property="fmTemplateList"
                                                                                                value="id" label="name" styleClass="COLOR: #cc0000;" />
                                                                    </logic:notEmpty>
                                                                </html:select>
                                                            </logic:equal>
                                                        </td>
                                                    </tr>
                                                    </feature:display>
												<feature:display name="Workspace Prefix" module="Workspace Manager" >   
                                                    <tr>
                                                        <td width="150" class="right-align" bgcolor="#FFFFFF">
                                                            <digi:trn key="aim:workspacePrefix">Workspace Prefix</digi:trn>
                                                        </td>
                                                        <td class="left-align" bgcolor="#FFFFFF">
                                                            <c:set var="translation">
                                                                <digi:trn>Select, if workspace will use a different translation set or category set</digi:trn>
                                                            </c:set>
                                                            <logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="delete">
                                                                <b><bean:write name="aimUpdateWorkspaceForm" property="workspacePrefix" /></b>
                                                            </logic:equal>
                                                            <logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="view">
                                                                <b><bean:write name="aimUpdateWorkspaceForm" property="workspacePrefix" /></b>
                                                            </logic:equal>
                                                            <logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="edit">
                                                                <category:showoptions firstLine="${translation}" name="aimUpdateWorkspaceForm" property="workspacePrefix" keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.WORKSPACE_PREFIX_KEY %>" styleClass="inp-text" />
                                                            </logic:equal>
                                                            <logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="add">
                                                                <category:showoptions firstLine="${translation}" name="aimUpdateWorkspaceForm" property="workspacePrefix" keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.WORKSPACE_PREFIX_KEY %>" styleClass="inp-text" />
                                                            </logic:equal>
                                                        </td>
                                                    </tr>
                                                    </feature:display>

                                                    <tr>
														<td class="right-align" bgcolor="#FFFFFF" style="font-size:12px; font-weight:bold;">
															<digi:trn key="aim:description">Description</digi:trn>
														</td>
														<td class="left-align" bgcolor="#FFFFFF" style="font-size:12px; font-weight:bold;">
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="delete">
																<b><bean:write name="aimUpdateWorkspaceForm" property="description" /></b>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="view">
																<b><bean:write name="aimUpdateWorkspaceForm" property="description" /></b>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="edit">
																<html:textarea property="description" rows="3" cols="50" styleClass="inp-text"/>
															</logic:equal>
															<logic:equal name="aimUpdateWorkspaceForm" property="actionEvent" value="add">
																<html:textarea property="description" rows="3" cols="50" styleClass="inp-text"/>
															</logic:equal>
														</td>
													</tr>
													<tr>
														<td colspan="4" style="background-color: #dddddd;">
															<table>
																<tr>
																	<td style="font-size:12px; font-weight:bold;border:0px;">
																		<html:checkbox property="crossteamvalidation" value="true">
																			<digi:trn>Cross Team Validation</digi:trn>
																		</html:checkbox>
																	</td>
																</tr>
															</table>
															
														</td>
													</tr>
													<tr id="private_workspace">
														<td colspan="4">
															<table>
																<tr>
																	<td style="font-size:12px; font-weight:bold;">
																		<html:checkbox property="isolated" value="true" onclick="privateChange()">
																			<digi:trn>Private workspace</digi:trn>
																		</html:checkbox>
																	</td>
																</tr>
															</table>
															
														</td>
													</tr>
													<tr id="send_summary_notification_">
														<td colspan="4">
															<table>
																<tr>
																	<td style="font-size:12px; font-weight:bold;">
																		<html:checkbox
																				property="sendSummaryChangesManager"
																				value="true" >
																			<digi:trn>Send summary changes
																				to managers</digi:trn>
																		</html:checkbox>
																	</td>
																</tr>
															</table>

														</td>
													</tr>

													<tr id="send_summary_notification_">
														<td colspan="4">
															<table>
																<tr>
																	<td style="font-size:12px; font-weight:bold;">
																		<html:checkbox
																				property="sendSummaryChangesApprover"
																				value="true" >
																			<digi:trn>Send summary changes
																				to approvers</digi:trn>
																		</html:checkbox>
																	</td>
																</tr>
															</table>

														</td>
													</tr>
													<c:if test="${aimUpdateWorkspaceForm.actionEvent == 'add'}">
														<c:if test="${aimUpdateWorkspaceForm.relatedTeamFlag != 'no'}">
															<tr  id="relTeamRow">
																<td class="right-align" bgcolor="#FFFFFF" style="font-size:12px; font-weight:bold;">
																	<digi:trn key="aim:relatedTeam">Related Team</digi:trn>
																</td>
																<td class="left-align" bgcolor="#FFFFFF" style="font-size:12px; font-weight:bold;">
																	<html:select property="relatedTeam" styleClass="inp-text">
																		<c:if test="${aimUpdateWorkspaceForm.relatedTeamFlag == 'nil'}">
																			<html:option value="-1">No related team available</html:option>
																		</c:if>
																		<c:if test="${aimUpdateWorkspaceForm.relatedTeamFlag == 'set'}">
																			<html:option value="-1">-- Select Team --</html:option>
																			<logic:notEmpty name="aimUpdateWorkspaceForm" property="relatedTeamBilatColl" >
																				<optgroup label="Bilateral" style="FONT-WEIGHT: bold;COLOR: #cc0000;">
																				<%--<html:option value="-1" style="FONT-WEIGHT: bold;COLOR: #cc0000;">-- Bilateral --</html:option>--%>
																				<html:optionsCollection name="aimUpdateWorkspaceForm" property="relatedTeamBilatColl"
																										value="ampTeamId" label="name" />
																				</optgroup>
																			</logic:notEmpty>
																			<logic:notEmpty name="aimUpdateWorkspaceForm" property="relatedTeamMutilatColl" >
																				<optgroup label="Multilateral" style="FONT-WEIGHT: bold;COLOR: #006600">
																				<%--<html:option value="-1">-- Multilateral --</html:option>--%>
																				<html:optionsCollection name="aimUpdateWorkspaceForm" property="relatedTeamMutilatColl"
																										value="ampTeamId" label="name" />
																				</optgroup>
																			</logic:notEmpty>
																		</c:if>
																	</html:select>
																</td>
															</tr>
														</c:if>
													</c:if>
															
											
													
													<tr id="management_workspace" style="display: none;background: #FFFFFF">
														<td colspan="4">
																	<c:if test="${aimUpdateWorkspaceForm.actionEvent != 'delete'}">
																		<c:if test="${aimUpdateWorkspaceForm.relatedTeamFlag != 'noedit'}">
																			<table>
																			<tr>
																				<td class="right-align" width="150" bgcolor="#FFFFFF" style="font-size:12px; font-weight:bold;">
																					<digi:trn key="aim:childWorkspacesOrTeams">Child Workspaces/Teams</digi:trn>
																				</td>
																				<td class="left-align" bgcolor="#FFFFFF" style="font-size:12px; font-weight:bold;">
																					<c:set var="translation">
																						<digi:trn key="btn:createWorkspaceAdd">
																							Add
																						</digi:trn>
																					</c:set>
																					<input type="button" value="${translation}" class="dr-menu" onclick="addChildWorkspaces()">
																				</td>
																			</tr>
																			</table>
																		</c:if>
																	</c:if>
																	<c:if test="${!empty aimUpdateWorkspaceForm.childWorkspaces}">
																	
																			<table width="98%" cellPadding=2 cellspacing="0" valign="top" align="center"
																			class="box-border-nopadding">
																			<c:forEach var="workspaces" items="${aimUpdateWorkspaceForm.childWorkspaces}">
																				<tr>
																					<td class="left-align">&nbsp;
																						<c:out value="${workspaces.name}"/>
																					</td>
																					<td class="right-align" width="10">
																						<c:if test="${aimUpdateWorkspaceForm.actionEvent != 'delete'}">
																						<a href="javascript:removeChildWorkspace(<c:out value="${workspaces.ampTeamId}"/>)">
																					 	<digi:img src="../ampTemplate/images/deleteIcon.gif"
																						border="0" alt="Remove this child workspace"/></a>&nbsp;
																						</c:if>
																					</td>
																				</tr>
																			</c:forEach>
																			</table>
																		
																	</c:if>
														</td>
													</tr>
													
													<tr id="team_workspace" style="display: none;background: #FFFFFF">
														<td colspan="4">
															
															<table>
																<tr>
																	<td style="font-size:12px; font-weight:bold;">
																		<html:checkbox property="computation"  value="true" onclick="computationChange()"><digi:trn key="chk:computation">Computation</digi:trn></html:checkbox>
																	</td>
																</tr>
															</table>
															
														</td>
													</tr>
													
													<tr  id="computation_addon" style="display: none;background: #FFFFFF">
														<td colspan="4">
																<table>
																	<tr>
																		<td>
																			<html:checkbox property="addActivity" value="true"><digi:trn key="chk:addActivity">Add Activity</digi:trn></html:checkbox>
																		</td>
																	</tr>
																	
																	<tr>
																		<td>
																			<html:checkbox property="hideDraftActivities" value="true"><digi:trn>Hide draft activities</digi:trn></html:checkbox>
																		</td>
																	</tr>
																	
																</table>
																<fieldset class="main_side_cont">
																	<legend>
																		<html:radio property="useFilter" value="${true}" onclick="toggleFilterVsOrgs(this);"/>
																		<span class="legend_label"><digi:trn key="rep:wizard:subtitle:selectedFilters">Selected Filters</digi:trn></span></legend>
																		<input id="hasFilters" type="hidden" value="<%=ReportContextData.hasFilters()%>" name="hasFilters">
																	<div id="listFiltersDiv" style="height:85px; width: 713px; overflow-y:auto; overflow-x:auto; margin-bottom: 5px; white-space: normal;" class="inputx">
																		<jsp:include page="reportWizard/showSelectedFilters.jsp" />				
																	</div>
																	<button type="button" value="Filters" class="buttonx_sm btn_save" id="add_filters_button" style="margin-right:2px;" onclick="repFilters.showFilters('<%=ReportContextData.getCurrentReportContextId(request, true)%>')"/>
																		<digi:trn key="btn:repFilters">Filters</digi:trn>
																	</button>
																</fieldset>
																<fieldset class="main_side_cont">
																	<legend>
																		<html:radio property="useFilter" value="${false}" onclick="toggleFilterVsOrgs(this);"/>
																		<span class="legend_label"><digi:trn key="aim:childrenOrganizations">Children (Organizations)</digi:trn></span></legend>
																	<c:if test="${!empty aimUpdateWorkspaceForm.organizations}">
																		<table width="98%" cellPadding=2 cellspacing="0" valign="top" align="center"
																			class="box-border-nopadding">
																			<c:forEach var="org" items="${aimUpdateWorkspaceForm.organizations}">
																				<tr>
																					<td class="left-align">&nbsp;
                                                                                        <input type="hidden" value="${org.ampOrgId}" name="selectedOrgId">
																						<c:out value="${org.name}"/>
																					</td>
																					<td class="right-align" width="10">
																						<c:if test="${aimUpdateWorkspaceForm.actionEvent != 'delete'}">
																						<a href="javascript:removeChildOrg(<c:out value="${org.ampOrgId}"/>)">
																					 	<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="Remove this linked org"/></a>&nbsp;
																						</c:if>
																					</td>
																				</tr>
																			</c:forEach>
																		</table>
																	</c:if>
																	<c:if test="${aimUpdateWorkspaceForm.actionEvent != 'delete'}">
																		<c:if test="${aimUpdateWorkspaceForm.relatedTeamFlag != 'noedit'}">
																			<aim:addOrganizationButton showAs="popin" refreshParentDocument="false" collection="organizations" form="${aimUpdateWorkspaceForm}" styleClass="buttonx_sm btn_save">
																					<digi:trn>Add</digi:trn>
																			</aim:addOrganizationButton>
                                                                            <script>
                                                                                /**
                                                                                  * this is actually JS function override
                                                                                  * the default implementation will be called first
                                                                                  */
                                                                                var selectOrgSelectedAware = selectOrg;
                                                                                selectOrg = function(params) {
                                                                                    var selectedItems = getSelectedItems();

                                                                                    selectOrgSelectedAware(params + "&" + "<%= org.digijava.module.aim.uicomponents.form.selectOrganizationComponentForm.EXCLUDED_ORG_IDS_SEPARATED %>"
                                                                                        + "=" + selectedItems);
                                                                                }

                                                                                /* List of Ids separated by "_" */
                                                                                function getSelectedItems() {
                                                                                    var ids = [];
                                                                                    var selectedOrganizations = $('[name="selectedOrgId"]');
                                                                                    if (selectedOrganizations && selectedOrganizations.length > 0) {
                                                                                        selectedOrganizations.each(function(){
                                                                                            ids.push($(this).val());
                                                                                        });
                                                                                    }

                                                                                    return ids.length > 0 ? ids.join('_') : "";
                                                                                }
                                                                            </script>
																		</c:if>
																	</c:if>
																</fieldset>																			
														</td>
													</tr>
													
													<c:if test="${aimUpdateWorkspaceForm.actionEvent != 'delete'}">
													<tr>
														<td colspan="2" align="center" bgcolor="#FFFFFF">
															<table cellPadding=5>
																<tr>
																	<td>
																		<c:set var="translation">
																			<digi:trn key="btn:createWorkspaceSave">
																				Save
																			</digi:trn>
																		</c:set>
																		<c:if test="${aimUpdateWorkspaceForm.actionEvent == 'add'}">
																			<input type="button" value="${translation}" class="buttonx"
																			onclick="update('add')"/>
																		</c:if>
																		<c:if test="${aimUpdateWorkspaceForm.actionEvent == 'edit'}">
																			<input type="button" value="${translation}" class="buttonx" onclick="showSiloDialog()"/>
																		</c:if>
																	</td>
																	<td>
																		<!-- <html:reset value="Clear" styleClass="dr-menu"/> -->
																		<c:set var="translation">
																			<digi:trn key="btn:createWorkspaceReset">
																				Reset
																			</digi:trn>
																		</c:set>
																		<c:if test="${aimUpdateWorkspaceForm.actionEvent == 'edit'}">
																			<input type="button" value="${translation}" class="buttonx" onclick="update1('editreset',${aimUpdateWorkspaceForm.teamId})"/>
																		</c:if>
																		<c:if test="${aimUpdateWorkspaceForm.actionEvent == 'add'}">
																			<input type="button" value="${translation}" class="buttonx" onclick="update('reset')"/>
																		</c:if>
																	</td>
																	<td>
																		<c:set var="translation">
																			<digi:trn key="btn:createWorkspaceCancel">
																				Cancel
																			</digi:trn>
																		</c:set>
																		<input name="" value="${translation}" onclick="cancel()" class="buttonx" type="button">
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
								</td>
							</tr>
							<tr><td bgColor=#FFFFFF>&nbsp;
								
							</td></tr>
						</table>
					</td>
					<td noWrap width="100%" vAlign="top">
						<table align="center" cellpadding="0" cellspacing="0" width="90%" border="0">
							<tr>
								<td>
									<table cellpadding="0" cellspacing="0" width="100" class="inside">
										<tr>
											<td bgColor=#c9c9c7>
												<b style="padding-left:5px;">
												<digi:trn key="aim:otherLinks">Other links</digi:trn></b>
											</td>
											<td class="corner-right">&nbsp;</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellspacing="1" width="100%" class="inside">
										<tr>
											<td class="inside">
												<span class="list-item-arrow"></span>
												<digi:link href="/workspaceManager.do">
												<digi:trn key="aim:teams">
												Teams
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										
										
										<tr>
											<td class="inside">
												<span class="list-item-arrow"></span>
												<digi:link href="/admin.do">
												<digi:trn key="aim:AmpAdminHome">
												Admin Home
												</digi:trn>
												</digi:link>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td></tr>
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

<script type="text/javascript">
	repFilters = new Filters("${filterPanelName}", "${failureMessage}", "${filterProblemsMessage}", 
					 		 "${loadingDataMessage}", "${savingDataMessage}", "${cannotSaveFiltersMessage}", 
					 		 false, "${settingsPanelName}");
</script>		
<%@ include file="/repository/aim/view/scripts/newCalendar.jsp"  %>

<script language="JavaScript" type="text/javascript">
    addLoadEvent(initOrganizationScript);
  </script>
