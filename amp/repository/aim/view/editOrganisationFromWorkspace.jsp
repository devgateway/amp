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
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/aim" prefix="aim"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>


<%@page
	import="org.digijava.module.aim.dbentity.AmpOrganisationDocument"%><script
	language="JavaScript" type="text/javascript"
	src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript"
	src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>
<style type="text/css">
.selectStyle {
	Font-size: 11px;
	font-family: Arial;
	width: 210px;
}

.tableEven {
	background-color: #dbe5f1;
	border-left: none;
	border-right: none;
	font-size: 10px;
	font-family: Arial;
}

.tableOdd {
	background-color: #FFFFFF;
	border-left: none;
	border-right: none;
	font-size: 11px;
	font-family: Arial;
	!
	important
}

.tableHeader {
	background-color: #c7d4db;
	color: white;
	padding: 2px;
}

.Hovered {
	background-color: #a5bcf2;
}

input,textArea {
	font-family: Arial;
	font-size: 11px;
}

.tdClass {
	font-family: Arial;
	font-size: 11px;
}

.tdBoldClass {
	font-family: Arial;
	font-size: 11px;
	font-weight: bold;
}

.legendClass {
	font-family: Arial;
	font-size: 13px;
	font-weight: bold;
	color: #ffffff;
}

div.charcounter-progress-container {
	width: 50%;
	height: 3px;
	max-height: 3px;
	border: 1px solid gray;
	filter: alpha(opacity =         20);
	opacity: 0.2;
}

div.charcounter-progress-bar {
	height: 3px;
	max-height: 3px;
	font-size: 3px;
	background-color: #5E8AD1;
}
</style>

<jsp:include page="/repository/aim/view/addEditOrganizationsPopin.jsp" />
<jsp:include page="/repository/aim/view/addOrganizationPopin.jsp" />
<jsp:include page="/repository/aim/view/components/contactScripts.jsp" />

<script language="JavaScript" type="text/javascript">
	function addLoadEvent(func) {
	  var oldonload = window.onload;
	  if (typeof window.onload != 'function') {
	    window.onload = func;
	  } else {
	    window.onload = function () {
	      if (oldonload) {
	        oldonload();
	      }
	      func();
		  }
		}
	}
	


function initScripts() {
//initSectorScript();
//initOrganizationScript();
initPopInScript();
initContactScript();
}
    addLoadEvent(initScripts);

    function refreshPage(){
        <digi:context name="reload" property="context/module/moduleinstance/editOrganisation.do" />
        document.aimAddOrgForm.action = "${reload}";
        document.aimAddOrgForm.actionFlag.value='reload'
        document.aimAddOrgForm.submit();
    }

    function addOrganizations2Contact(){
        var params=getContactParams();
    <digi:context name="addCont" property="context/addAmpContactInfo.do?action=addOrganizations"/>;
            checkAndClose=true;
            var url="${addCont}"+"&"+params;
            YAHOO.util.Connect.asyncRequest("POST", url, callback1);
    }
  
   
        function addStaff(){
            var year=document.aimAddOrgForm.selectedYear;
            var type=document.aimAddOrgForm.typeOfStaff;
            var number=document.aimAddOrgForm.numberOfStaff;
            var errorMsg= '<digi:trn jsFriendly="true">Please enter numeric value only</digi:trn>';
            var reg=/^\d+$/;
           if(!reg.test(number.value)){
        	   alert(errorMsg);
               number.value = "";
               return false;
           }
            if(year.value=='-1'){
                errorMsg='<digi:trn jsFriendly="true">Please select year!</digi:trn>';
                alert(errorMsg);
                return false;
            }
            if(type.value=='0'){
                errorMsg='<digi:trn jsFriendly="true">Please select type!</digi:trn>';
                alert(errorMsg);
                return false;
            }

    <digi:context name="addStaff" property="context/module/moduleinstance/editOrganisation.do" />
            document.aimAddOrgForm.actionFlag.value="addStaffInfo";
            document.aimAddOrgForm.action = "${addStaff}";
            document.aimAddOrgForm.target = "_self";
            document.aimAddOrgForm.submit();
        }
        function deleteStaff(staffId){
            if(staffId!=null&&staffId!=""&&typeof(staffId)!='undefined'){
                document.aimAddOrgForm.selectedStaffId.value=staffId;
            }
            else{
                var elems = document.getElementsByName("selectedStaff");
                var selected = false;
                for (var i=0; i<elems.length; i++){
					if (elems[i].checked == true)
						selected = true;
                }
                if (!selected){
					alert ('<digi:trn jsFriendly="true">Please, select one option first.</digi:trn>');
					return false;
                } else {
                	document.aimAddOrgForm.selectedStaffId.value=null;
                }
            }
    		<digi:context name="deleteStaff" property="context/module/moduleinstance/editOrganisation.do" />
            document.aimAddOrgForm.actionFlag.value="deleteStaffInfo";
            document.aimAddOrgForm.action = "${deleteStaff}";
            document.aimAddOrgForm.target = "_self";
            document.aimAddOrgForm.submit();
        }
       
        function setStyle(table,hasTitle){
        	
//        	alert (table)
        	
            if(table!=null){
                table.className += " tableElement";
                setStripsTable(table.id, "tableEven", "tableOdd");
                setHoveredTable(table.id, hasTitle);
            }

        }
        function selectAll(e,className){
            if (e.checked==true)
	            $("."+className).each(function () {
	                this.checked=true;
	            });
            else
            	$("."+className).each(function () {
	                this.checked=false;
	            });
        }
        function addSectors() {
            var sectorSchemeId=document.aimAddOrgForm.ampSecSchemeId;
            if(sectorSchemeId.value=="-1"){
                alert('<digi:trn jsFriendly="true">Please select sector scheme</digi:trn>');
            }
            else{
                myAddSectors("sectorScheme="+sectorSchemeId.value+"&configId=1");
            }
        }
        function removeSectors() {
        	var sectorsToBeRemoved=$("#selectedSectors").find("input.sectorsMultibox:checked");
        	if(sectorsToBeRemoved==null || sectorsToBeRemoved.length == 0){
            	alert('<digi:trn jsFriendly="true">Please choose at least one sector to remove</digi:trn>');
            	return false;
        	}else{
        		<digi:context name="removeSectors" property="context/module/moduleinstance/editOrganisation.do" />
                document.aimAddOrgForm.actionFlag.value="removeSector";
                document.aimAddOrgForm.action = "${removeSectors}";
                document.aimAddOrgForm.target = "_self";
                document.aimAddOrgForm.submit();
        	}
        }
        
        function addSector() {
    		<digi:context name="addSectors" property="context/module/moduleinstance/editOrganisation.do" />
            document.aimAddOrgForm.actionFlag.value="addSector";
            document.aimAddOrgForm.action = "${addSectors}";
            document.aimAddOrgForm.target = "_self";
            document.aimAddOrgForm.submit();
        }
       
            function editStaffInfo(index){
                <digi:context name="editStaffInfo" property="context/module/moduleinstance/editOrganisation.do" />
                    document.aimAddOrgForm.action = "${editStaffInfo}?staffInfoIndex="+index;
                    document.aimAddOrgForm.target = "_self"
                    document.aimAddOrgForm.actionFlag.value="editStaffInfo";
                    document.aimAddOrgForm.submit();
                }


        
        
    
        

        function cancel() {
    		<digi:context name="selectLoc" property="context/module/moduleinstance/organisationManager.do" />
            url = "<%=selectLoc%>?orgSelReset=true";
            document.location.href = url;
        }
        function check() {

            var type=document.aimAddOrgForm.type;
           
            // We have different mandatory fields for NGOs and others....
            if(type.value=='NGO'){
                var orgPrimaryPurpose= document.aimAddOrgForm.orgPrimaryPurpose.value;
                var mandatoryPrimPurp = document.getElementById('mandatoryPrimPurp');
                if (mandatoryPrimPurp!= null && (orgPrimaryPurpose == null||orgPrimaryPurpose.length == 0 )) {
                    alert('<digi:trn  jsFriendly="true">Please enter primary purpose for this Organization.</digi:trn>');
                    document.aimAddOrgForm.orgPrimaryPurpose.focus();
                    return false;
                }
                var address= document.aimAddOrgForm.address.value;
                var mandatoryHeadquartersAddr = document.getElementById('mandatoryHeadquartersAddr');
                if (mandatoryHeadquartersAddr!=null && (address == null||address.length == 0 )) {
                    alert('<digi:trn  jsFriendly="true">Please Enter Address for this Organization.</digi:trn>');
                    document.aimAddOrgForm.address.focus();
                    return false;
                }
                var selSectors= document.getElementsByName("selSectors");
                var mandatorySectPref = document.getElementById('mandatorySectPref');
                if (mandatorySectPref!=null && (selSectors == null||selSectors.length == 0 )) {
                    alert('<digi:trn  jsFriendly="true">Please Select Sectors for this Organization.</digi:trn>');
                    return false;
                }
                                    
            }
           
              
            return true;

        }     

          	   
        function addDocumentsDM(documentsType, showTheFollowingDocuments) {
        	//submit organization parameters first
           	<digi:context name="getInf" property="context/module/moduleinstance/editOrganisation.do?skipReset=true" />
           	var url="${getInf}";
           	var params=getResourceParams();

           	var callback = {
       					success:function(o) {
								if (showTheFollowingDocuments==null){
									showTheFollowingDocuments="ALL";
								}
								var url		= "/contentrepository/selectDocumentDM.do?documentsType="+documentsType+"&showTheFollowingDocuments="+showTheFollowingDocuments;
								var popupName	= 'my_popup';
								window.open(url, popupName, 'width=900, height=300,scrollbars=yes,resizable=yes');
								document.forms[0].action=url;
								document.forms[0].target=popupName;
								document.forms[0].submit();
						}
       		};
       	
       		YAHOO.util.Connect.asyncRequest("POST",url, callback, params );         
            
        }


        function getResourceParams(){
        	var params="";
        	if(document.getElementById('orgName')!=null){
        		params+="name="+document.getElementById('orgName').value;
        	}
        	if(document.getElementById('acronym')!=null){
        		 params+="&acronym="+document.getElementById('acronym').value;
        	}
        	if(document.getElementById('orgGroup')!=null){
        		params+="&ampOrgGrpId="+document.getElementById('orgGroup').value;
        	}
        	if(document.getElementById('dacOrgCode')!=null){
        		params+="&dacOrgCode="+document.getElementById('dacOrgCode').value;
        	}
        	if(document.getElementById('orgIsoCode')!=null){
        		params+="&orgIsoCode="+document.getElementById('orgIsoCode').value;
        	}
        	if(document.getElementById('orgCode')!=null){
        		params+="&orgCode="+document.getElementById('orgCode').value;
        	}
        	if(document.getElementById('budgetOrgCode')!=null){
        		params+="&budgetOrgCode="+document.getElementById('budgetOrgCode').value;
        	}
        	if(document.getElementById('fiscalCalId')!=null){
        		params+="&fiscalCalId="+document.getElementById('fiscalCalId').value;
        	}
        	if(document.getElementById('ampSecSchemeId')!=null){
        		params+="&ampSecSchemeId="+document.getElementById('ampSecSchemeId').value;
        	}
        	if(document.getElementById('orgUrl')!=null){
        		params+="&orgUrl="+document.getElementById('orgUrl').value;
        	}
        	if(document.getElementById('address')!=null){
        		params+="&address="+document.getElementById('address').value;
        	}
        	if(document.getElementById('description')!=null){
        		params+="&description="+document.getElementById('description').value;
        	}                            
        	if(document.getElementById('fundingorgid')!=null){
        		params+="&fundingorgid="+document.getElementById('fundingorgid').value;
        	}
            return params;
            
        }

        
        function validateSaveOrg() {
            if(check()){
    			<digi:context name="save" property="context/module/moduleinstance/editOrganisation.do" />
                document.aimAddOrgForm.action = "${save}";
                document.aimAddOrgForm.actionFlag.value = "save";
                document.aimAddOrgForm.submit();
            }
        }

        function setStripsTable(tableId, classOdd, classEven) {
            var tableElement = document.getElementById(tableId);
            if(tableElement)
            {
                tableElement.setAttribute("border","0");
                tableElement.setAttribute("cellPadding","0");
                tableElement.setAttribute("cellSpacing","0");
                rows = tableElement.getElementsByTagName('tr');
                for(var i = 0, n = rows.length; i < n; ++i) {
                    if(i%2 == 0)
                        rows[i].className = classEven;
                    else
                        rows[i].className = classOdd;
                }
                rows = null;
            }
        }
        function setHoveredTable(tableId, hasHeaders) {
            var tableElement = document.getElementById(tableId);
            if(tableElement){
                var className = 'Hovered',
                pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
                rows      = tableElement.getElementsByTagName('tr');

                var i = 0;
                if(hasHeaders){
                    rows[0].className += " tableHeader";
                    i = 1;
                }

                for(i, n = rows.length; i < n; ++i) {
                    rows[i].onmouseover = function() {
                        this.className += ' ' + className;
                    };
                    rows[i].onmouseout = function() {
                        this.className = this.className.replace(pattern, ' ');

                    };
                }
                rows = null;
            }
        }
        
    function expand(suffix){
		var imgId='#img_'+suffix;
		var imghId='#imgh_'+suffix;
		var divId='#div_container_'+suffix;
		$(imghId).show();
		$(imgId).hide();
		$(divId).show('fast');
	}

	function collapse(suffix){
		var imgId='#img_'+suffix;
		var imghId='#imgh_'+suffix;
		var divId='#div_container_'+suffix;
		$(imghId).hide();
		$(imgId).show();
		$(divId).hide('fast');
	}
  
       

        function removeContact(selContactId){
    		<digi:context name="remLocs" property="context/module/moduleinstance/editOrganisation.do" />
            document.aimAddOrgForm.action = "${remLocs}";
            document.aimAddOrgForm.target = "_self"
            document.aimAddOrgForm.actionFlag.value="deleteContact";
            document.aimAddOrgForm.selContactId.value=selContactId;
            document.aimAddOrgForm.submit();

        }

        function removeSelectedContacts(){
        	var atLeastOneIsChecked = false;
        	var selected= $("input[class='selectedContactInfoIds']:checked").length;
        		if (selected>0) { 
                	atLeastOneIsChecked = true;
                } else { 
                	atLeastOneIsChecked = false;
                	
                }
            
        	if (atLeastOneIsChecked) {
            	<digi:context name="remConts" property="context/module/moduleinstance/editOrganisation.do" />
                document.aimAddOrgForm.action = "${remConts}";
                document.aimAddOrgForm.target = "_self"
                document.aimAddOrgForm.actionFlag.value="deleteContact";
                document.aimAddOrgForm.submit();
        	} else {
    			var errorMesage='<digi:trn jsFriendly="true">Please select at least one contact to delete</digi:trn>';
            	alert(errorMesage);
        		return false;
        	}
        }

        function changePrimaryState(){
        	var orgConts= $("input[id^='primary_']");
        	var resetConts=resetPrimary(orgConts);
        	if(resetConts==true){
        		document.getElementById('primaryOrgCont').value=true;
        	}else{
        		document.getElementById('primaryOrgCont').value=false;
        	}
        }

        function resetPrimary(contList){
            var retValue=true;
        	for(var i=0;i<contList.length;i++){
        		if(contList[i].checked){
        			retValue=false;
        			break;
        		}
        	}
        	return retValue;
        }
       
       
</script>
<style>
.yui-skin-sam a.yui-pg-page {
	margin-left: 2px;
	padding-right: 7px;
	font-size: 11px;
	border-right: 1px solid rgb(208, 208, 208);
}

.yui-skin-sam .yui-pg-pages {
	border: 0px;
	padding-left: 0px;
}

.yui-pg-current-page {
	background-color: #FFFFFF;
	color: rgb(208, 208, 208);
	padding: 0px;
}

.current-page {
	background-color: #FF6000;
	color: #FFFFFF;
	padding: 2px;
	font-weight: bold;
}

.yui-skin-sam span.yui-pg-first,.yui-skin-sam span.yui-pg-previous,.yui-skin-sam span.yui-pg-next,.yui-skin-sam span.yui-pg-last
	{
	display: none;
}

.yui-skin-sam a.yui-pg-first {
	margin-left: 2px;
	padding-right: 7px;
	border-right: 1px solid rgb(208, 208, 208);
}

.resource_box {
	background-color: #FFFFFF;
	border: 1px solid #CCCCCC;
	font-size: 12px;
	padding: 10px;
}
</style>
<c:set var="selectedTab" value="9" scope="request"/>
<td valign="top">
	<div id="tabs"
		class="ui-tabs ui-widget ui-widget-content ui-corner-all">
		<jsp:include page="teamSetupMenu.jsp" flush="true" />

		<digi:instance property="aimAddOrgForm" />
		<digi:context name="digiContext" property="context" />
		<digi:form action="/editOrganisation.do" method="post">
			<html:hidden name="aimAddOrgForm" property="actionFlag" />
			<html:hidden name="aimAddOrgForm" property="selectedStaffId" />
			<html:hidden name="aimAddOrgForm" styleId="parentLocId"
				property="parentLocId" />
			<html:hidden name="aimAddOrgForm" property="type" />
			<html:hidden name="aimAddOrgForm" property="ampOrgId" />
			<html:hidden name="aimAddOrgForm" property="selectedOrgInfoId" />
			<html:hidden name="aimAddOrgForm" property="selContactId" />
			<html:hidden styleId="primaryOrgCont"
				value="${aimAddOrgForm.resetPrimaryOrgContIds}" name="aimAddOrgForm"
				property="resetPrimaryOrgContIds" />
			<html:hidden styleId="departments"
				value="${aimAddOrgForm.resetDepartments}" name="aimAddOrgForm"
				property="resetDepartments" />
			<html:hidden styleId="budgSects"
				value="${aimAddOrgForm.resetBudgetSectors}" name="aimAddOrgForm"
				property="resetBudgetSectors" />

			<feature:display name="NGO Form" module="Organization Manager"></feature:display>

			<table bgColor="#ffffff" cellPadding="0" cellSpacing="0" width=1000
				align=center>
				<tr><td align="left" style="padding-left:5px; font-weight:bold;"><digi:link href="/organisationManager.do" styleClass="l_sm">&lt;&lt;<digi:trn>Back to Organization List</digi:trn></digi:link></td></tr>
				<tr>
					<td align=left valign="top" width="100%">
						<table bgcolor="#ffffff" cellPadding=5 cellspacing="0"
							width="100%">
							<tr>
								<td><digi:trn>All fields marked with <font
											size="2" color="#FF0000">*</font> are required.</digi:trn></td>
							</tr>
							<tr>
								<td><digi:errors /></td>
							</tr>
							<tr>
								<td>
									<table border="0" bgColor=#f4f4f2>


										<tr>
											<td bgColor=#c7d4db height="25" align="center" colspan="2"
												class="tdBoldClass"
												style="font-size: 12px; font-weight: bold;"><digi:trn>Edit Organization</digi:trn>
											</td>
										</tr>
										<tr>
											<td colspan="2">
											<div style="margin-top:15px; margin-left:15px; line-height:18px;">
												<digi:trn>Organization Name</digi:trn>: <b><c:out value="${aimAddOrgForm.name}" /></b><br>
															<digi:trn>Organization Type</digi:trn>: <b><c:out value="${aimAddOrgForm.orgTypeName}" /></b><br>
															<digi:trn>Organization Group</digi:trn>: <b><c:out value="${aimAddOrgForm.orgGroupName}" /></b>
															</div>
											</td>

										</tr>

										<tr>
											<td width="100%" colspan="2">

												<fieldset style="margin-left: 10px; margin-right: 10px;">
													<legend class="legendClass">
														<digi:trn>General Information</digi:trn>
													</legend>
													<img id="img_general" alt=""
														src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
														style="display: none;" onclick="expand('general')" /> <img
														id="imgh_general" alt=""
														src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
														onclick="collapse('general')" />
													<div id="div_container_general" >
														<table width="100%" cellpadding="5" cellspacing="5">
															<tr>
																<td valign="top" width="50%">
																	<table cellpadding="5" cellspacing="5" width=100%>
																		<tr>
																			<td class="tdBoldClass">
																				<digi:trn>Sectors Scheme</digi:trn><c:if test="${aimAddOrgForm.type=='NGO'}"> <field:display
																					name="Mandatory Indicator For Sector Preferences"
																					feature="NGO Form">
																					<span id="mandatorySectScheme"><font
																						color="red">*</font></span>
																				</field:display></c:if>
																			</td>
																			<td><html:select property="ampSecSchemeId"
																					styleClass="selectStyle" styleId="ampSecSchemeId">
																					<c:set var="translation">
																						<digi:trn>Sectors Scheme</digi:trn>
																					</c:set>
																					<html:option value="-1">-- ${translation} --</html:option>
																					<logic:notEmpty name="aimAddOrgForm"
																						property="sectorScheme">
																						<html:optionsCollection name="aimAddOrgForm"
																							property="sectorScheme" value="ampSecSchemeId"
																							label="secSchemeName" />
																					</logic:notEmpty>
																				</html:select></td>
																			<td class="tdBoldClass"><digi:trn>Organization website</digi:trn></td>
																			<td><html:text property="orgUrl"
																					styleId="orgUrl" /></td>
																		</tr>
																		<tr>
																			<td class="tdBoldClass">
																				<digi:trn>Sector Preferences</digi:trn><c:if test="${aimAddOrgForm.type=='NGO'}"> <field:display
																					name="Mandatory Indicator For Sector Preferences"
																					feature="NGO Form">
																					<span id="mandatorySectPref"><font
																						color="red">*</font></span>
																				</field:display></c:if>
																			</td>
																			<td>
																				<table cellSpacing="1" cellPadding="5"
																					id="selectedSectors">
																					<c:if test="${aimAddOrgForm.sectors != null}">
																						<c:forEach var="sector"
																							items="${aimAddOrgForm.sectors}">
																							<tr>
																								<td width="5px" align="right"><html:multibox
																										property="selSectors"
																										styleClass="sectorsMultibox">
																										<c:if test="${sector.subsectorLevel1Id == -1}">
                                                                                                    ${sector.sectorId}
                                                                                                </c:if>

																										<c:if
																											test="${sector.subsectorLevel1Id != -1 && sector.subsectorLevel2Id == -1}">
                                                                                                    ${sector.subsectorLevel1Id}
                                                                                                </c:if>
																										<c:if
																											test="${sector.subsectorLevel1Id != -1 && sector.subsectorLevel2Id != -1}">
                                                                                                    ${sector.subsectorLevel2Id}
                                                                                                </c:if>
																									</html:multibox></td>
																								<td>[${sector.sectorScheme}] <c:if
																										test="${!empty sector.sectorName}">
                                                                                                [${sector.sectorName}]
                                                                                            </c:if>

																									<c:if
																										test="${!empty sector.subsectorLevel1Name}">
	                                                                            [${sector.subsectorLevel1Name}]
                                                                                            </c:if>

																									<c:if
																										test="${!empty sector.subsectorLevel2Name}">
	                                                                            [${sector.subsectorLevel2Name}]
                                                                                            </c:if>

																								</td>
																							</tr>
																						</c:forEach>
																					</c:if>

																					<tr>
																						<td colspan="2"><input type="button"
																							class="buttonx_sm"
																							onclick="javascript:addSectors();"
																							value='<digi:trn jsFriendly="true" key="btn:addSectors">Add Sectors</digi:trn>' />
																							<c:if test="${not empty aimAddOrgForm.sectors}">
																								<input type="button" class="buttonx_sm"
																									onclick="return removeSectors()"
																									value='<digi:trn jsFriendly="true" key="btn:removeSector">Remove Sector</digi:trn>' />
																							</c:if></td>
																					</tr>

																				</table>
																			</td>
																			<c:choose>
																				<c:when test="${aimAddOrgForm.type=='NGO'}">
																					<td class="tdBoldClass">
																						<digi:trn>Organization Headquarters Address</digi:trn>
																						<field:display
																							name="Mandatory Indicator For Organization Headquarters Address"
																							feature="NGO Form">
																							<span id="mandatoryHeadquartersAddr"><font
																								color="red">*</font></span>
																						</field:display>
																					</td>
																				</c:when>
																				<c:otherwise>
																					<td class="tdBoldClass"><digi:trn>Organization Headquarters Address</digi:trn></td>
																				</c:otherwise>
																			</c:choose>
																			<td><html:textarea property="address" cols="40"
																					styleId="address" /></td>
																		</tr>
																		<tr>
																			<c:choose>
																				<c:when test="${aimAddOrgForm.type=='NGO'}">
																					<td><b><digi:trn>Organization Primary Purpose</digi:trn></b>
																						<field:display
																							name="Mandatory Indicator For Organization Primary Purpose"
																							feature="NGO Form">
																							<span id="mandatoryPrimPurp"><font
																								size="2" color="#FF0000">*</font></span>
																						</field:display></td>
																					<td><html:textarea name="aimAddOrgForm"
																							property="orgPrimaryPurpose" cols="30"
																							styleId="orgPrimaryPurpose" /></td>
																					<td class="tdBoldClass"><digi:trn>Organization Address Abroad(Internation NGO)</digi:trn></td>
																					<td><html:textarea property="addressAbroad"
																							cols="30" styleId="addressAbroad" /></td>
																				</c:when>
																				<c:otherwise>
																				<td><b><digi:trn>Description</digi:trn></b>
																					</td>
																					<td colspan=5><html:textarea
																							property="description" styleId="description"
																							style="width:100%;	height:100px;margin-bottom:10px;" />
																					</td>
																				</c:otherwise>
																			</c:choose>
																		</tr>
																	</table>


																</td>
															</tr>

														</table>
													</div>
												</fieldset>
											</td>
										</tr>
										
										<c:if test="${aimAddOrgForm.type=='NGO'}">
										<tr>
											<td colspan="2">

												<fieldset style="margin-left: 10px; margin-right: 10px;">
													<legend align="left" class="tdBoldClass"
														style="font-size: 13px; color: #FFFFFF;">
														<digi:trn>Staff Information</digi:trn>
													</legend>
													<img id="img_staff" alt=""
														src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
														style="display: none;" onclick="expand('staff')" /> <img
														id="imgh_staff" alt=""
														src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
														onclick="collapse('staff')" />
													<div id="div_container_staff">
														<table cellpadding="2" cellspacing="0" border="0" width=100%>
															<c:if test="${not empty aimAddOrgForm.staff}">
																<tr>
																	<td colspan="5"><c:if
																			test="${fn:length(aimAddOrgForm.staff)>1}">
																			<div
																				style="overflow-y: scroll; overflow-x: hidden; width: 100%; height: 100px;">
																		</c:if>
																		<table cellspacing="0" cellpadding="0" id="staffTable">
																			<c:forEach var="info" items="${aimAddOrgForm.staff}"
																				varStatus="staffInfoIndex">
																				<tr>
																					<td style="width: 40px; text-align: left;"><html:multibox
																							property="selectedStaff" styleClass="staffInfo">
                                                                    ${info.id}
                                                                </html:multibox></td>
																					<td class="tdClass"
																						style="width: 125px; text-align: center;">${info.year}</td>
																					<td class="tdClass"
																						style="width: 205px; text-align: center;"><digi:trn>${info.type.value}</digi:trn></td>
																					<td class="tdClass"
																						style="width: 200px; text-align: center;">${info.staffNumber}</td>
																					<td class="tdClass"
																						style="width: 35px; text-align: center;"><a
																						href="javascript:editStaffInfo('${staffInfoIndex.index}')"><img
																							alt="edit"
																							src="/TEMPLATE/ampTemplate/imagesSource/common/application_edit.png"
																							border="0" /></a></td>
																					<td class="tdClass"
																						style="width: 35px; text-align: center;"><a
																						href="javascript:deleteStaff('${info.id}')"> <img
																							alt="delete"
																							src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif"
																							border="0"></a></td>
																				</tr>
																			</c:forEach>
																		</table> <c:if test="${fn:length(aimAddOrgForm.staff)>1}">
																			</div>
																		</c:if></td>
																</tr>
																<tr>
																	<td colspan="5" style="text-align: left;"
																		class="tdBoldClass"><input type="checkbox"
																		onclick="selectAll(this,'staffInfo')"> <digi:trn>Select All</digi:trn>&nbsp;&nbsp;<input
																		type="button" class="buttonx_sm"
																		onclick="deleteStaff()"
																		value="<digi:trn>Delete</digi:trn>"></td>
																</tr>
															</c:if>
															<tr>
																<td
																	style="width: 40px; text-align: center; font-weight: bold">&nbsp;

																</td>
																<td style="width: 130px; text-align: center;"
																	class="tdBoldClass"><digi:trn>Year</digi:trn></td>
																<td style="width: 210px; text-align: center;"
																	class="tdBoldClass"><digi:trn>Type of staff</digi:trn>
																</td>
																<td style="width: 150px; text-align: center;"
																	class="tdBoldClass"><digi:trn>Number of Staff</digi:trn>
																</td>
																<td
																	style="width: 90px; text-align: center; font-weight: bold">&nbsp;

																</td>
															</tr>
															<tr>
																<td>&nbsp;</td>
																<td align="center"><c:set var="translation">
																		<digi:trn> Select Year</digi:trn>
																	</c:set> <html:select name="aimAddOrgForm"
																		property="selectedYear" styleClass="selectStyle"
																		style="width:120px;">
																		<html:option value="-1">-- ${translation} --</html:option>
																		<html:optionsCollection name="aimAddOrgForm"
																			property="years" label="label" value="value" />
																	</html:select></td>
																<td style="text-align: center"><c:set
																		var="translation">
																		<digi:trn>Please select a status from below</digi:trn>
																	</c:set> <category:showoptions firstLine="${translation}"
																		name="aimAddOrgForm" property="typeOfStaff"
																		keyName="<%= org.digijava.module.categorymanager.util.CategoryConstants.ORGANIZATION_STAFF_INFO_KEY%>"
																		styleClass="selectStyle" /></td>
																<td style="text-align: center"><html:text
																		name="aimAddOrgForm" property="numberOfStaff"
																		onkeyup="fnChk(this,true)" styleClass="inp-text" /></td>
																<c:set var="staffButtonTxt">
																	<c:choose>
																		<c:when test="${aimAddOrgForm.staffInfoIndex!=-1}">
																			<digi:trn>Update</digi:trn>
																		</c:when>
																		<c:otherwise>
																			<digi:trn>Add</digi:trn>
																		</c:otherwise>
																	</c:choose>
																</c:set>
																<td style="text-align: center"><input type="button"
																	class="buttonx_sm" style="width: 80px"
																	onclick="addStaff()" value="${staffButtonTxt}" /></td>
															</tr>
														</table>
													</div>
												</fieldset>
											</td>
										</tr>
										</c:if>
										<!-- Contact -->
										<tr>
											<td colspan="2">

												<fieldset
													style="margin-left: 10px; margin-right: 10px; margin-bottom: 15px;">
													<legend align="left" class="legendClass">
														<digi:trn>Contact Information</digi:trn>
													</legend>
													<img id="img_contact" alt=""
														src="/TEMPLATE/ampTemplate/images/arrow_right.gif"
														style="display: none;" onclick="expand('contact')" /> <img
														id="imgh_contact" alt=""
														src="/TEMPLATE/ampTemplate/images/arrow_down.gif"
														onclick="collapse('contact')" />
													<div id="div_container_contact">
														<table cellpadding="2" cellspacing="0" border="0"
															width="100%">
															<c:if test="${not empty aimAddOrgForm.orgContacts}">
																<tr>
																	<td colspan="2"><c:if
																			test="${fn:length(aimAddOrgForm.orgContacts)>1}">
																			<div
																				style="overflow-y: scroll; overflow-x: hidden; width: 95%; height: 100px;">
																		</c:if>
																		<table width="100%" cellSpacing="1" cellPadding="1"
																			align="left" id="table_contact_content"
																			style="margin-top: 20px;">
																			<tr>
																				<td>&nbsp;</td>
																				<td class="tdBoldClass" style="color: #000000"><digi:trn>LAST NAME</digi:trn>
																				</td>
																				<td class="tdBoldClass" style="color: #000000"><digi:trn>FIRST NAME</digi:trn>
																				</td>
																				<td class="tdBoldClass" style="color: #000000"><digi:trn>EMAIL </digi:trn>
																				</td>
																				<td class="tdBoldClass" style="color: #000000"><digi:trn> TELEPHONE </digi:trn>
																				</td>
																				<td class="tdBoldClass" style="color: #000000"><digi:trn> FAX </digi:trn>
																				</td>
																				<td class="tdBoldClass" style="color: #000000"><digi:trn>TITLE </digi:trn>
																				</td>
																				<td class="tdBoldClass" style="color: #000000"><digi:trn>PRIMARY </digi:trn>
																				</td>
																				<td colspan="2">&nbsp;</td>
																			</tr>
																			<c:forEach var="orgCont"
																				items="${aimAddOrgForm.orgContacts}"
																				varStatus="stat">
																				<c:set var="ampContactId">
																					<c:choose>
																						<c:when
																							test="${empty orgCont.contact.id||orgCont.contact.id==0}">
	                                                    ${orgCont.contact.temporaryId}
	                                                </c:when>
																						<c:otherwise>
	                                                    ${orgCont.contact.id}
	                                                </c:otherwise>
																					</c:choose>
																				</c:set>
																				<tr>
																					<td style="width: 40px"><html:multibox
																							property="selectedContactInfoIds"
																							styleClass="selectedContactInfoIds">
	                                                    ${ampContactId}
	                                                </html:multibox></td>
																					<td class="tdClass" nowrap><c:out
																							value="${orgCont.contact.lastname}" /></td>
																					<td class="tdClass" nowrap><c:out
																							value="${orgCont.contact.name}" /></td>
																					<td class="tdClass" nowrap><c:forEach
																							var="email" items="${orgCont.contact.properties}">
																							<c:if test="${email.name=='contact email'}">
																								<div>
																									<c:out value="${email.value}" />
																								</div>
																							</c:if>
																						</c:forEach></td>
																					<td class="tdClass"><c:forEach var="phone"
																							items="${orgCont.contact.properties}">
																							<c:if test="${phone.name=='contact phone'}">
																								<div>
																									<c:if test="${not empty phone.phoneCategory}">
																										<digi:trn>
																											<c:out value="${phone.phoneCategory}" />
																										</digi:trn>
																									</c:if>
																									<c:out value="${phone.value}"></c:out>
																								</div>
																							</c:if>
																						</c:forEach></td>
																					<td class="tdClass"><c:forEach var="phone"
																							items="${orgCont.contact.properties}">
																							<c:if test="${phone.name=='contact fax'}">
																								<div>
																									<c:out value="${phone.value}" />
																								</div>
																							</c:if>
																						</c:forEach></td>
																					<td class="tdClass"><c:if
																							test="${not empty orgCont.contact.title}">
																							<digi:trn>
																								<c:out value="${orgCont.contact.title.value}" />
																							</digi:trn>
																						</c:if></td>
																					<td><html:multibox name="aimAddOrgForm"
																							property="primaryOrgContIds"
																							styleId="primary_${stat.index}"
																							value="${ampContactId}"
																							onchange="changePrimaryState()" /></td>
																					<td><aim:editContactLink
																							collection="orgContacts" form="${aimAddOrgForm}"
																							contactId="${ampContactId}" addOrgBtn="hidden">
																							<img alt="edit"
																								src="/TEMPLATE/ampTemplate/imagesSource/common/application_edit.png"
																								border="0" />
																						</aim:editContactLink></td>
																					<td><a
																						href="javascript:removeContact('${ampContactId}')">
																							<img alt="delete"
																							src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif"
																							border="0" />
																					</a></td>
																				</tr>
																			</c:forEach>
																		</table> <c:if
																			test="${fn:length(aimAddOrgForm.orgContacts)>1}">
																			</div>
																		</c:if></td>
																</tr>
																<tr>
																	<td colspan="2" class="tdBoldClass"
																		style="text-align: left;"><input type="checkbox"
																		onclick="selectAll(this,'selectedContactInfoIds')">
																		<digi:trn>Select All</digi:trn>&nbsp;&nbsp;<input
																		type="button" class="buttonx_sm"
																		onclick="removeSelectedContacts()"
																		value="<digi:trn>Delete</digi:trn>"></td>
																</tr>
															</c:if>
															<tr>
																<td colspan="2"><aim:addContactButton styleClass="buttonx_sm"  collection="orgContacts" form="${aimAddOrgForm}" addOrgBtn="hidden"><digi:trn>Add contact</digi:trn></aim:addContactButton></td>
															</tr>

														</table>
													</div>
												</fieldset>
											</td>
										</tr>
										<c:if test="${aimAddOrgForm.type!='NGO'}">
											<module:display name="Document"
												parentModule="PROJECT MANAGEMENT">
												<tr>
													<td colspan="2" align=center class="yui-skin-sam">
														<table width="95%" cellspacing="0" cellpadding="0"
															border="0">
															<tr>
																<td bgcolor=#f4f4f2 align=left><bean:define
																		toScope="request" id="showRemoveButton" value="true" />
																	<bean:define toScope="request" id="documentsType"
																		value="<%=org.digijava.module.aim.dbentity.AmpOrganisationDocument.SESSION_NAME%>" />
																	<bean:define toScope="request" id="versioningRights"
																		value="false" /> <bean:define toScope="request"
																		id="viewAllRights" value="true" /> <bean:define
																		toScope="request" id="makePublicRights" value="false" />
																	<bean:define toScope="request" id="showVersionsRights"
																		value="false" /> <bean:define toScope="request"
																		id="deleteRights" value="false" /> <bean:define
																		toScope="request" id="crRights" value="true" /> <bean:define
																		toScope="request" id="checkBoxToHide" value="false" />
																		<bean:define toScope="request"
																		id="showLineBreaks" value="false" />
																	<jsp:include
																		page="/repository/contentrepository/view/showSelectedDocumentsDM.jsp" />
																		<c:set var="showTheFollowingDocuments" value="PUBLIC" />
																	<c:set var="documentsType"><%=org.digijava.module.aim.dbentity.AmpOrganisationDocument.SESSION_NAME%></c:set>
																	<html:button styleClass="buttonx_sm"
																		property="submitButton"
																		onclick="addDocumentsDM('${documentsType}','${showTheFollowingDocuments}')">
																		<digi:trn>Add Documents From Repository</digi:trn>
																	</html:button></td>
															</tr>
														</table>
													</td>
													<td></td>
												</tr>
											</module:display>
											
										</c:if>


										<tr>
											<td colspan="2" align="center" height="30">
												<table width="100%" width="555">
													<tr>
														<td align="center">
															<hr /> <html:button styleClass="buttonx_sm"
																property="submitButton"
																onclick="return validateSaveOrg()" styleId="addOrgBtn">
																<digi:trn>Save</digi:trn>
															</html:button> <input type="reset" value="<digi:trn>Reset</digi:trn>"
															class="buttonx_sm"> <input type="button"
															value="<digi:trn>Cancel</digi:trn>" class="buttonx_sm"
															onclick="cancel()">
														</td>
													</tr>
												</table>
											</td>
										</tr>

									</table>
								</td>

							</tr>
						</table>
					</td>
				</tr>

			</table>

		</digi:form>


		<script language="javascript" type="text/javascript">
    setStyle(document.getElementById("staffTable"),false);
    setStyle(document.getElementById("table_contact_content"),true);
    //var enterBinder	= new EnterHitBinder('addOrgBtn');
</script>
	</div>