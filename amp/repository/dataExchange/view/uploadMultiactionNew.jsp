<%@ page pageEncoding="UTF-8" %>
<%@page import="org.digijava.module.dataExchange.action.ImportActionNew"%>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>






<digi:instance property="importFormNew"/>
<style type="text/css">
<!--

	#uploadBtn{
	   position: relative;
	   font-family: calibri;
	   width: 90px;
	   height:20px;
	   padding: 10px;
	   -webkit-border-radius: 5px;
	   -moz-border-radius: 5px;
	   border: 1px dashed #BBB; 
	   text-align: center;
	   background-color: #DDD;
	   cursor:pointer;
	   border: 1px dashed #BBB;
	  }
	td.defaultTab {
		background-color:#f2f2f2;
		border: 1px solid #b8b7b7;
		color: #47608e;
		width: 100px;
		height: 30px;
		text-align:center;
    font-size: 11px;
    font-weight: bold;
	}
	
	td.inactiveTab {
		background-color:#4c6b7f;
		border: 1px solid #b8b7b7;
		border-bottom: none;
		color: white;
		width: 100px;
		height: 30px;
		text-align:center;
		cursor: pointer;
    font-size: 11px;
    font-weight: bold;
	}
	
	td.disabledTab {
		background-color:#8b8b8b;
		border: 1px solid #b8b7b7;
		border-bottom: none;
		color: white;
		width: 100px;
		height: 30px;
		text-align:center;
    font-size: 11px;
    font-weight: bold;
	}
	
	a.tabLinks {
		color: white;
	}
	
	div#busyCover {
		display:none;	
	}
	
	div#mappingTableBusy {
		background-color:black;
		position:absolute;
		opacity:0.2;
		filter:alpha(opacity=20); /* For IE8 and earlier */
	}
	
	img#busyImg {
		position:absolute;
	}
	
	span.breadcrambCurrent {
		color:red;
	}
	
	span.breadcrambOther {
		color:black;
	}
	
	div.wizardNav {
		height:20px;
		width:50px;
		border: 1px solid #B8B7B7;
		cursor:pointer;
		vertical-align:middle;
	};
	
	div.wizardBtn {
		color:white;
		background-color: #4b687a;
		align:center;
	}
	
	div.wizardBtn:hover {
		color:white;
		background-color: #6b8b9e;
	}
	
	div.wizardBtn:active {
		color:white;
		background-color: #3d5768;
	}
	
	div.wizardBtnDisabled {
		color:#d4d4d4;
		background-color: #a6a8a9;
	}
	
	a.wizardLink {
		color: white;
    font-size: 12px;
    font-weight:normal;
    text-decoration:none;
	}
	
	table.wizardTable {
		border: 1px solid #b8b7b7;
		background-color: #e0e0e0;
    font-weight:normal;
    text-decoration:none;

	}

-->
</style>
	
	<script language="javascript">
	function getFile(){
	        $("#upfile").click();
	  }
	
	 function setLabel(obj){
		    var file = obj.value;
		    var fileName = file.split("\\");
		    var text = fileName[fileName.length-1];
		    if (text.length > 15) {
		    	text = text.substring (0,12) + "....";
		    }
		    $("#uploadLabel").text(text);
	}
	 
	var contentBusy = function (show) {
		if (show) {
			var containerSize = $("#mainTableContainer");
			var width = containerSize.width();
			var height = containerSize.height();
			var pos = containerSize.position();
			$("#mappingTableBusy").css("left", pos.left + "px").css("top", pos.top + "px").css("width", width + "px").css("height", height + "px")
			$("#busyImg").css("left", pos.left + width/2 - 16 + "px").css("top", pos.top + height/2 - 16 + "px");
			$("#busyCover").css("display", "block");
			$("#optionsDiv").css("display", "none");
			
		} else {
			$("#busyCover").css("display", "none");
		}
	};
</script>
	
<bean:define name="importFormNew" property="page" id="pageId"/>

<c:set var="IATI_IMPORT_PAGE_UPLOAD"><%=ImportActionNew.IATI_IMPORT_PAGE_UPLOAD%></c:set>
<c:set var="IATI_IMPORT_PAGE_LOGS"><%=ImportActionNew.IATI_IMPORT_PAGE_LOGS%></c:set>
<c:set var="IATI_IMPORT_PAGE_MAPPING"><%=ImportActionNew.IATI_IMPORT_PAGE_MAPPING%></c:set>
<c:set var="IATI_IMPORT_PAGE_FILTERS"><%=ImportActionNew.IATI_IMPORT_PAGE_FILTERS%></c:set>
<c:set var="IATI_IMPORT_PAGE_SESSIONS"><%=ImportActionNew.IATI_IMPORT_PAGE_SESSIONS%></c:set>
<logic:present name="importFormNew" property="upSess">
	<c:set var="curSessId"><bean:write name="importFormNew" property="upSess.id"/></c:set>
</logic:present>

<div id="busyCover">
	<div id="mappingTableBusy">&nbsp;</div>
	<img id="busyImg" src="/TEMPLATE/ampTemplate/images/amploading.gif">
</div>
	
<table cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<table cellpadding="0" cellspacing="0" width="1008">
				<tr>
					<td width="20" style="border-bottom: 1px solid #b8b7b7">
						&nbsp;
					</td>
					<logic:equal name="importFormNew" property="page" value="<%= String.valueOf(ImportActionNew.IATI_IMPORT_PAGE_SESSIONS) %>">
					<td class="defaultTab" style="border-bottom: none;">
						<digi:trn>Sessions</digi:trn>
					</logic:equal>
					<logic:notEqual name="importFormNew" property="page" value="<%= String.valueOf(ImportActionNew.IATI_IMPORT_PAGE_SESSIONS) %>">
					<td class="inactiveTab" style="border-bottom: none;">
						<a class="tabLinks" href="javascript:navigateTo('/dataExchange/importActionNew.do')"><digi:trn>Sessions</digi:trn></a>
					</logic:notEqual>
						
					</td>
					<td width="3" style="border-bottom: 1px solid #b8b7b7">
						&nbsp;
					</td>
					<logic:equal name="importFormNew" property="page" value="<%= String.valueOf(ImportActionNew.IATI_IMPORT_PAGE_UPLOAD) %>">
					<td class="defaultTab" style="border-bottom: none;">
						<digi:trn>New session</digi:trn>
					</logic:equal>
					<logic:notEqual name="importFormNew" property="page" value="<%= String.valueOf(ImportActionNew.IATI_IMPORT_PAGE_UPLOAD) %>">
					<td class="inactiveTab" style="border-bottom: none;">
						<a class="tabLinks" href="javascript:navigateTo('/dataExchange/importActionNew.do?action=showUploadScreen')"><digi:trn>New session</digi:trn></a>
					</logic:notEqual>
						
					</td>
					</td>
					<td width="548" style="border-bottom: 1px solid #b8b7b7">
						&nbsp;
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class="defaultTab" style="border-top:none;height:570px" height="550" valign="top" id="mainTableContainer">
		<table border="0" width="100%"> <!-- Wizard part -->
			<tr>
				<td>
					<logic:notEqual name="importFormNew" property="page" value="<%= String.valueOf(ImportActionNew.IATI_IMPORT_PAGE_SESSIONS) %>">
					<table  class="wizardTable">
						<tr>
							<!-- Breadcramb -->
							<td width="500" align="left">
							<logic:equal name="importFormNew" property="page" value="<%= String.valueOf(ImportActionNew.IATI_IMPORT_PAGE_UPLOAD) %>">
									<span class="breadcrambCurrent"><digi:trn>Upload</digi:trn></span>
							</logic:equal>
							<logic:notEqual name="importFormNew" property="page" value="<%= String.valueOf(ImportActionNew.IATI_IMPORT_PAGE_UPLOAD) %>">
								<span class="breadcrambOther"><digi:trn>Upload</digi:trn></span>
							</logic:notEqual>
							&raquo;
							<logic:equal name="importFormNew" property="page" value="<%= String.valueOf(ImportActionNew.IATI_IMPORT_PAGE_FILTERS) %>">
								<span class="breadcrambCurrent"><digi:trn>Country Filter</digi:trn></span>
							</logic:equal>
							<logic:notEqual name="importFormNew" property="page" value="<%= String.valueOf(ImportActionNew.IATI_IMPORT_PAGE_FILTERS) %>">
								<span class="breadcrambOther"><digi:trn>Country Filter</digi:trn></span>
							</logic:notEqual>
							&raquo;
							<logic:equal name="importFormNew" property="page" value="<%= String.valueOf(ImportActionNew.IATI_IMPORT_PAGE_MAPPING) %>">
								<span class="breadcrambCurrent"><digi:trn>Mapping</digi:trn></span>
							</logic:equal>
							<logic:notEqual name="importFormNew" property="page" value="<%= String.valueOf(ImportActionNew.IATI_IMPORT_PAGE_MAPPING) %>">
								<span class="breadcrambOther"><digi:trn>Mapping</digi:trn></span>
							</logic:notEqual>
							&raquo;
							<logic:equal name="importFormNew" property="page" value="<%= String.valueOf(ImportActionNew.IATI_IMPORT_PAGE_LOGS) %>">
								<span class="breadcrambCurrent"><digi:trn>Logs</digi:trn></span>
							</logic:equal>
							<logic:notEqual name="importFormNew" property="page" value="<%= String.valueOf(ImportActionNew.IATI_IMPORT_PAGE_LOGS) %>">
								<span class="breadcrambOther"><digi:trn>Logs</digi:trn></span>
							</logic:notEqual>
							</td>
							<!-- end of Breadcramb -->
							
							<td width="500" align="right" valign="middle">
							<logic:equal name="importFormNew" property="page" value="<%= String.valueOf(ImportActionNew.IATI_IMPORT_PAGE_UPLOAD) %>">
									<td width="50" nowrap><div class="wizardNav wizardBtnDisabled">&lt;<digi:trn>back</digi:trn></td>
									<td width="50" nowrap>
										<div id="next" class="wizardNav wizardBtnDisabled">
											<input type="hidden" value="submit">
											<digi:trn>next</digi:trn>&nbsp;&raquo;
										</div>
									</td>
								</logic:equal>

								<logic:equal name="importFormNew" property="page" value="<%= String.valueOf(ImportActionNew.IATI_IMPORT_PAGE_FILTERS) %>">
									<td width="50" nowrap>
										<div class="wizardNav wizardBtn">
											<input type="hidden" value="/dataExchange/importActionNew.do?action=showUploadScreen">
											&laquo;&nbsp;<digi:trn>back</digi:trn>
										</div>
									</td>
									<td width="50" nowrap>
										<div id="next" class="wizardNav wizardBtn">
											<input type="hidden" value="submit">
											<digi:trn>next</digi:trn>&nbsp;&raquo;
										</div>
									</td>
								</logic:equal>
								
							<logic:equal name="importFormNew" property="page" value="<%= String.valueOf(ImportActionNew.IATI_IMPORT_PAGE_MAPPING) %>">
								<td width="50" nowrap>
									<img id='mappingSaveDis' style="display:block;" src='/TEMPLATE/ampTemplate/images/save_dis.png'>
									<img id='mappingSave' style="display:none; cursor:pointer;" src='/TEMPLATE/ampTemplate/images/save.png'>
								</td>
								<c:if test="${empty curSessId}">
									<td width="50" nowrap>
										<div class="wizardNav wizardBtn">
											<input type="hidden" value="/dataExchange/importActionNew.do?action=showFilters">
											&laquo;&nbsp;<digi:trn>back</digi:trn>
										</div>
									</td>
								</c:if>
								<c:if test="${not empty curSessId}">
									<td width="50" nowrap>
										<div class="wizardNav wizardBtnDisabled">
											&laquo;&nbsp;<digi:trn>back</digi:trn>
										</div>
									</td>
								</c:if>
								<td width="50" nowrap>
									<div class="wizardNav wizardBtn">
										<input type="hidden" value="submit">
											<digi:trn>next</digi:trn>&nbsp;&raquo;
									</div>
								</td>
							</logic:equal>
							
							<logic:equal name="importFormNew" property="page" value="<%= String.valueOf(ImportActionNew.IATI_IMPORT_PAGE_LOGS) %>">
								
								<c:set var="showImportAll" scope="page" value="false"/>
								<logic:iterate id="item" name="importFormNew" property="logItems" indexId="idx">
								<c:if test="${item.logType=='OK'}">
									<c:set var="showImportAll" scope="page" value="true"/>
								</c:if>
								</logic:iterate>
								<c:if test="${showImportAll}">
									<td width="50" nowrap>
										<div class="wizardNav wizardBtn">
											<input type="button" class="buttonx_sm"  value="<digi:trn>Import All</digi:trn>" onclick="executeImportAll();"/>
										</div>
									</td>
								</c:if>
								<td width="50" nowrap>
									<div class="wizardNav wizardBtn">
										<input type="hidden" value="/dataExchange/importActionNew.do?action=loadUploadSession&objId=${curSessId}">
										&laquo;&nbsp;<digi:trn>back</digi:trn>
									</div>
								</td>
								
								<td width="50" nowrap>
									<div class="wizardNav wizardBtnDisabled">
										<digi:trn>next</digi:trn>&nbsp;&raquo;
									</div>
								</td>
								
							</logic:equal>

							
							
							
						</tr>
					</table> <!-- end of Wizard part -->
					</logic:notEqual>
				</td>
			</tr>
			<tr>
				<td>
					<logic:equal name="importFormNew" property="page" value="<%= String.valueOf(ImportActionNew.IATI_IMPORT_PAGE_UPLOAD) %>">
						<digi:form action="/importActionNew.do?action=upload" method="post" enctype="multipart/form-data">
		
							<table width="100%" cellspacing="0" cellpadding="0" border="0">
								<tr>
							    <td width="49%" valign="top" style="border: none;" class="inside">
										<fieldset>
											<legend><span class="legend_label"><digi:trn>Choose file</digi:trn></span></legend>
<%-- 											<html:file property="file"   name="importFormNew" /> --%>
											
											 <div id="uploadBtn" onclick="getFile()"><digi:trn>Click to upload!</digi:trn></div>
   											 	<div id="uploadLabel" style="display:inline;position:relative;top:-25px;left:60px;text-overflow:ellipsis;overflow:hidden; -o-text-overflow: ellipsis; white-space: nowrap;width:60px;"><digi:trn>No file chosen</digi:trn></div>
									
<!--    											 this is file input tag, so i hide it! -->
    										<div style='height: 0px;width:0px; overflow:hidden;'><html:file property="file"  styleId="upfile" name="importFormNew" onchange="setLabel(this)" /></div>
										
											</fieldset>	 
									</td>
									<td>
										<input type="hidden" class="inputx" style="width:95%;" value="" name="name">							
										<fieldset>
											<legend><span class="legend_label"><digi:trn>Select Configuration</digi:trn></span></legend>
											<b><digi:trn>Please choose the configuration that will be used</digi:trn>:</b><br>
											<html:select name="importFormNew" property="configurationId" styleClass="inputx" style="margin-top:5px;width:500px">
												<html:optionsCollection name="importFormNew" property="configurations" label="name" value="id"/>
											</html:select>
			        							<br><br>
										</fieldset>
									</td>
									<td width="2%">&nbsp;</td>
							    	<td width="49%" valign="top">
							    	
									</td>
								</tr>
							</table>
		
						
						</digi:form>
						
						
					<script language="javascript">
						$('input[type=file]').change(function(e){
							checkSelFile();
						});
						
						function checkSelFile() {
							var originatorObj = $('input[type=file]');
							var nextBtn = $("#next");
							if (originatorObj.val() == null || originatorObj.val().trim().length == 0) {
								if (nextBtn.hasClass("wizardBtn")) {
									nextBtn.removeClass("wizardBtn").addClass("wizardBtnDisabled");
								}
							} else {
								
								nextBtn.removeClass("wizardBtnDisabled").addClass("wizardBtn");
							}
						}
						
						checkSelFile();
						
					</script>
					</logic:equal>
					
					<logic:equal name="importFormNew" property="page" value="<%= String.valueOf(ImportActionNew.IATI_IMPORT_PAGE_LOGS) %>">
						
						<script type="text/javascript">
								function toggleGroup(group_id){
									var strId='#'+group_id;
									$(strId+'_minus').toggle();
									$(strId+'_plus').toggle();
									$('#log_'+group_id).toggle('fast');
								}
								
								function executeImport(id) {
									$("input[type='hidden'][name='objId']").val(id);
									$("form[name='importFormNew']").submit();
								}
								
								function executeImportAll() {
									$("form[name='importFormNew']").attr("action", "/dataExchange/admin/importActionNew.do?action=executeImportAll");
									$("form[name='importFormNew']").submit();
								}
						</script>
						
						
						
						<digi:form action="/importActionNew.do?action=executeImport" method="post" enctype="multipart/form-data">
							<html:hidden name="importFormNew" property="objId"/>
							<table class="inside" width=977 border=0 cellpadding="0" cellspacing="0" style="margin:10px;">
											<tr>
												<td colspan="6" align=center background="/TEMPLATE/ampTemplate/img_2/ins_header.gif" class=inside>
												<b><digi:trn>Execution Logs</digi:trn></b>
											</td>
								<logic:empty name="importFormNew" property="logItems">
										<tr>
											<td bgcolor="#FFFFFF" class="inside" colspan="6">
												<div class="t_sm"><digi:trn>No Records Found</digi:trn> </div>
											</td>
										</tr>				
									</logic:empty>
									<logic:notEmpty name="importFormNew" property="logItems">
										<logic:iterate id="item" name="importFormNew" property="logItems" indexId="idx">
											<tr>
												<%--
													<td bgcolor=#FFFFFF class=inside>
													    	<c:if test="${item.logType=='OK'}">
													    		<html:checkbox name="showLogsForm"  property="selectedActivities"  value="${item.id}" />
													    	</c:if>
													</td>
													--%>
											    <td bgcolor=#FFFFFF class=inside>
											    	<div class="t_sm">${item.name}</div>
											    </td>
											    <td bgcolor=#FFFFFF class=inside align=center>
											    	<c:if test="${item.logType=='ERROR' }">
											    		<img src="/TEMPLATE/ampTemplate/img_2/not_ok_ico.gif" />
											    	</c:if>
											    	<c:if test="${item.logType=='INFO' }">
											    		<img src="/TEMPLATE/ampTemplate/img_2/ico_info.gif" />
											    	</c:if>
											    	<c:if test="${item.logType=='OK'}">
											    		<img src="/TEMPLATE/ampTemplate/img_2/ok_ico.gif" />
											    	</c:if>
											    	<c:if test="${item.logType=='IGNORE'}">
											    		<img src="/TEMPLATE/ampTemplate/img_2/alert.ico" />
											    	</c:if>
											    </td>
											    <td bgcolor=#FFFFFF class=inside>
											    	<div class="t_sm">
											    		<div class="t_sm">
											    		   <digi:trn>Name</digi:trn>:	 ${item.name } |
											    		   <digi:trn>Database ID</digi:trn>:	 ${item.id }	  |
											    		   <digi:trn>Status</digi:trn>:	 ${item.logType }	  |
											    		   <digi:trn>Date</digi:trn>:	 ${item.dateAsString}	  |
											    		   <digi:trn>Time</digi:trn>:	 ${item.timeAsString }	  |							    		   
											    		   <digi:trn>Amp Object Id</digi:trn>:	${item.itemType }
											    		</div>
											    	</div>
											    </td>
											    
											    <td bgcolor="#FFFFFF" class="inside" align="center" nowrap="nowrap">
											    	<img src="/TEMPLATE/ampTemplate/img_2/ico_plus.gif" id="${item.id}_plus" onclick="toggleGroup('${item.id}')" style="cursor: pointer;"/>
											    	<div id="log_${item.id}" style="display: none;width:500px; overflow: auto;">				    		
											    		<table border="0" width="100%">
												    		<tr>
												    			<td style="border: none;vertical-align: text-top;" colspan="2" rowspan="8" class="inside">				    				
												    				<img src="/TEMPLATE/ampTemplate/img_2/ico_blue_minus.gif"  id="${item.id}_minus" style="display: none;cursor: pointer; " onclick="toggleGroup('${item.id}')"/>	
												    			</td>
												    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">				    				
												    				<strong><digi:trn>Description</digi:trn>:</strong>
												    			</td>
												    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
												    				${item.description }
												    			</td>
												    		</tr>							    		
											    		</table>
											    	</div>
											    </td>							    
											    <td width="20" align="center" bgcolor=#FFFFFF class=inside>
										    		<c:if test="${item.logType=='OK'}">
											    		<input type="button" class="buttonx_sm" <c:if test="${item.importDoneOn!=null}">style="background-color:red;"</c:if>  value="<digi:trn>Import</digi:trn>" onclick="executeImport(${item.id});"/>
											    	</c:if>
											    </td>
											</tr>
										</logic:iterate>
									</logic:notEmpty>
							</table>
						</digi:form>
					</logic:equal>
					
					<logic:equal name="importFormNew" property="page" value="<%= String.valueOf(ImportActionNew.IATI_IMPORT_PAGE_FILTERS) %>">
						<digi:form action="/importActionNew.do?action=showMapping" method="post">
									<table class="inside" width=980 border=0 cellpadding="0" cellspacing="0" style="margin:10px;">
										<tr>
											<td colspan="6" align=center background="/TEMPLATE/ampTemplate/img_2/ins_header.gif" class=inside>
											<b><digi:trn>Select Countries</digi:trn></b>
										</td>
										<logic:empty name="importFormNew" property="countryList">
											<tr>
												<td bgcolor="#FFFFFF" class="inside" colspan="6">
													<div class="t_sm"><digi:trn>No Records Found</digi:trn> </div>
												</td>
											</tr>
										</logic:empty>
										<logic:notEmpty name="importFormNew" property="countryList">
											<logic:iterate name="importFormNew" property="countryList" id="item" >
												<tr>
													<td bgcolor=#FFFFFF class=inside width="10">
											  		<html:multibox name="importFormNew" property="selCountries">
											   			<bean:write name="item" property="key"/> 
											  		</html:multibox> 
													</td><td bgcolor=#FFFFFF class=inside>
											   		<bean:write name="item" property="value"/> 
											  	</td>
											  </tr>
											</logic:iterate>
										</logic:notEmpty>
									</table>
						</digi:form>
						
						<script language="javascript">
						$("input[type=checkbox][name='selCountries']").change(function(e){
							checkSelCountry();
						});
						
						function checkSelCountry() {
							var checkedCountries = $("input[type=checkbox][name='selCountries']:checked");
							var nextBtn = $("#next");
							if (checkedCountries.length > 0) {
								nextBtn.removeClass("wizardBtnDisabled").addClass("wizardBtn");
							} else {
								nextBtn.removeClass("wizardBtn").addClass("wizardBtnDisabled");
							}
						}
						
						checkSelCountry();
						
					</script>
					</logic:equal>
					
					<logic:equal name="importFormNew" property="page" value="<%= String.valueOf(ImportActionNew.IATI_IMPORT_PAGE_MAPPING) %>">
						
						<style type="text/css">
					<!--
					.autocompleteDropdownText {
						border: 1px solid silver;
						font-size: 11px !important;
						font-family: Arial,Helvetica,sans-serif;
						cursor:pointer;
						
					}
					
					.autocompleteDropdownText:hover {
						background-color:#feff90;
					}
					
					.autocompleteDropdownSameAsText {
						border: 1px solid silver;
						font-size: 11px !important;
						font-family: Arial,Helvetica,sans-serif;
						cursor:pointer;
						
					}
					
					.autocompleteDropdownSameAsText:hover {
						background-color:#feff90;
					}
					
					div.optionsContainer {
						width:500px;
						height:200px;
						position:absolute;
						background-color:white;
						border:1px solid black;
						display:none;
					}
					
					table.optionsContainerTable {
						width:498px;
						height:180px;
						position:absolute;
					}
					
					div.flowContainer {
						overflow-y:auto;
						overflow-x:hidden;
						width:495px;
						height:180px;
						
					}
					
					td.infoWnd {
						border: 1px solid black;
						background-color:#B8B7B7;
						color:black;
						font-size: 11px !important;
						font-weight:normal;
					}
					
					td.optionItem {
						padding:2px;
						border: 1px solid transparent;
						cursor:pointer;
					}
					
					td.optionItem:hover {
						border: 1px solid #bfc8d6;
						background-color: #dfe5ee;
					}
					
					td.optionItem:active {
						border: 1px solid #0e2342;
						background-color: #2b4b7a;
						color: white;
					}
					
					div#mappingTableContainer {
						width:987px;
						height:500px;
						border: 1px solid black;
						overflow-x:hidden;
						overflow-y:scroll;
					}
					
					
					
					-->
					</style>
						<div id="optionsDiv" class="optionsContainer">
							<table class="optionsContainerTable">
								<tr>
									<td align="left">
										<div id="content" class="flowContainer">
										</div>
									</td>
								</tr><tr>
									<td align="left" id="info" height="12" class="infoWnd">Total
									</td>
								</tr>
							</table>
						</div>
						
						<div id="sameAsOptionsDiv" class="optionsContainer">
										<div id="sameAsContent" class="flowContainer"></div>
						</div>
						
						<digi:form action="/importActionNew.do?action=saveMapping" method="post">
						<table>
							<tr>
								<td align="left">
									<html:select name="importFormNew" property="selAmpClass">
										<logic:iterate name="importFormNew" property="ampClasses" id="ampClass" type="String">
											<html:option value="<%= ampClass %>"><digi:trn ><bean:write name="ampClass"/></digi:trn></html:option>
										</logic:iterate>
									</html:select>
								</td>
							</tr>
							<tr>
								<td>
									<div id="mappingTableContainer"></div>
								</td>
							</tr>
						</table>
		
						</digi:form>

					
					<script language="javascript">
						
						
						var unmappedString = "<digi:trn>Unmapped</digi:trn>";
						var addNewString = "<digi:trn>Add new</digi:trn>";
						var sameAsString = "<digi:trn>Same As</digi:trn>";
						var dontImport = "<digi:trn>Don't import</digi:trn>";
						
						
						
						
						var contentBusyShow = function() {
							contentBusy(true);
						}
						
						var contentBusyHide = function() {
							contentBusy(false);
						}
					
						
						var objIdStrPrefix = "amp_value_autocomplete_container_";
						
						
						var getMappingObjects = function (selectedClass) {
							var url = "../../dataExchange/importActionNew.do?action=getMappingObjects";
							$.ajax({
							  type: 'POST',
							  url: url,
							  data:{selectedClass:selectedClass},
							  success: getMappingObjectsSuccess,
							  dataType: "json",
							  beforeSend:contentBusyShow,
					    	complete:contentBusyHide
							});
						};
						
						var getMappingObjectsSuccess = function (data, textStatus, jqXHR) {
							var objects = data.objects;
							
							var optionsMarkup = [];
							optionsMarkup.push("<table width='100%' style='border-collapse:collapse' border=1>");
							$(objects).each(function(index, element) {
								optionsMarkup.push("<tr>");
								optionsMarkup.push("<td width='200'>");
								optionsMarkup.push(element.iatiItems);
								optionsMarkup.push("</td>");
								optionsMarkup.push("<td width='300'>");
								optionsMarkup.push(element.iatiValues);
								optionsMarkup.push("</td>");
								optionsMarkup.push("<td width='500' align='left'>");
								optionsMarkup.push("<div style='width: 500px;' id='");
								optionsMarkup.push(objIdStrPrefix);
								optionsMarkup.push(element.tmpId);
								optionsMarkup.push("'>");
								optionsMarkup.push("<input name='selAmpValue' type='hidden' value='");
								optionsMarkup.push(element.ampId);
								optionsMarkup.push("'>");
								optionsMarkup.push("<input name='sameAsTmpId' type='hidden' value='");
								optionsMarkup.push(element.sameAsTmpId);
								optionsMarkup.push("'>");
								if (element.sameAsTmpId == null || element.sameAsTmpId == -1) {
									optionsMarkup.push("<input class='autocompleteDropdownText' type='text' style='width: 495px;' value='");
									optionsMarkup.push(element.ampValues);
									optionsMarkup.push("'>");
								} else {
									optionsMarkup.push("<input class='autocompleteDropdownText' type='text' style='width: 50px;' value='");
									optionsMarkup.push(sameAsString);
									optionsMarkup.push("'>");
									
									optionsMarkup.push("<input class='autocompleteDropdownSameAsText' type='text' style='width: 440px;' value='");
									optionsMarkup.push(element.sameAsText);
									optionsMarkup.push("'>");
									
								}
								optionsMarkup.push("</div>");
								optionsMarkup.push("</td>");
							});
							optionsMarkup.push("</table>");
							
							$("#mappingTableContainer").html(optionsMarkup.join(''));
							setAutocompleteEvtListeners ();
							checkForDefaultValues();
						}
						
						$("select[name=selAmpClass]").change(function (evt) {
							var originatorObj = $(evt.target);
							getMappingObjects (originatorObj.find("option:selected" ).attr("value"));
						});
						
						getMappingObjects ($("select[name=selAmpClass] option:selected" ).attr("value"));
						
						
						
						
						//Autocomplete
						
						var timeoutObj = null;
						var curEvtObj = null;
							
						function setAutocompleteEvtListeners () {
							$(".optionsContainer").css("display","none");
							$("input.autocompleteDropdownText").click(function(e) {
								$(".optionsContainer").css("display","none");
								var originatorObj = $(e.target);
								curEvtObj = originatorObj;
								var originatorObjAbsPos = originatorObj.position();
								var optionsContainerDiv = $("#optionsDiv");
								optionsContainerDiv.css("display", "block");
								optionsContainerDiv.css("left", originatorObjAbsPos.left + "px");
								optionsContainerDiv.css("top", originatorObjAbsPos.top+20 + "px");
								var hiddenInput = originatorObj.parent().find("input[type='hidden'][name='selAmpValue']");
								
								if (hiddenInput.val()=="" || hiddenInput.val()=="0" || hiddenInput.val()==-1) {
									originatorObj.val("").css("color", "black");
									searchStr = "";//Get all options if "add new" or "unmapped" is selected
								} else {
									searchStr = originatorObj.val(); 
								}
								getAutosuggestOptionValues(searchStr, 100);
							});
							
							
							$("input.autocompleteDropdownText").keyup(function(e) {
								if (timeoutObj != null) {
									window.clearTimeout (timeoutObj);
								}
								timeoutObj = window.setTimeout("getAutosuggestOptionValues(curEvtObj.val(), 100)", 300);
								
							});
							
							$("input.autocompleteDropdownSameAsText").click(sameAsEvtListener);
							$("input.autocompleteDropdownSameAsText").keyup(sameAsEvtListener);
						}
						
						var getAutosuggestOptionValues = function (queryStr, maxNumber) {
							var url = "../../dataExchange/importActionNew.do?action=getOptionsAjaxAction";
							$.ajax({
							  type: 'GET',
							  url: url,
							  data:{maxResultCount:maxNumber},
							  headers:{searchStr:queryStr.substring(0, 32)},
							  success: autocompleteRequestSuccess,
							  dataType: "json"
							});
						}
						
						var autocompleteRequestSuccess = function (data, textStatus, jqXHR) {
							var optionsContainer = $("#optionsDiv #content");
							var infoWnd = $("#optionsDiv .infoWnd");
							
							var optionsMarkup = [];
							optionsMarkup.push("<table width='100%'>");
							$(data.objects).each(function(index, element) {
					    	optionsMarkup.push("<tr>");
					    	optionsMarkup.push("<td nowrap class='optionItem'>");

								var caption = null;					    	
					    	if (element.val == "Unmapped") {
					    		caption =  unmappedString;
					    	} else if (element.val == "Add new") {
					    		caption =  addNewString;
					    	} else if (element.val == "Same as") {
					    		caption =  sameAsString;
					    	} else if (element.val == "Don't import") {
					    		caption =  dontImport;
					    	} else {
					    		caption =  element.val;
					    	}
					    	
					    	if (element.val != null) {
					    		optionsMarkup.push(caption);
					    	} else {
					    		optionsMarkup.push("EMPTY VALUE");
					    	}
					    	optionsMarkup.push("<input class='objId' type='hidden' value='");
					    	optionsMarkup.push(element.id);
					    	optionsMarkup.push("'>");
					    	optionsMarkup.push("<input class='objCaption' type='hidden' value='");
					    	optionsMarkup.push(caption);
					    	optionsMarkup.push("'>");
					    	optionsMarkup.push("</td>");
					    	optionsMarkup.push("</tr>");
					    	
							});
							optionsMarkup.push("</table>");
							optionsContainer.html(optionsMarkup.join(''));
							var totalLabel = "<digi:trn jsFriendly='true'>Total object count/Showing: </digi:trn>";
							infoWnd.html(totalLabel +  data.totalCount + "/" +  data.objects.length);
							
							$("td.optionItem").click(function (e) {
								var originatorObj = $(e.target);
								$("#optionsDiv").css("display", "none");
								var newVal = originatorObj.find(".objCaption").val();
								if (newVal.trim() == "") newVal = "EMPTY VALUE";
								curEvtObj.val(newVal);
								
								var newId = originatorObj.find(".objId").val();
								curEvtObj.parent().find("input[type='hidden'][name='selAmpValue']").val(newId);
								if (newId != -2) { //Not "Same As"
									curEvtObj.parent().find("input[type='hidden'][name='sameAsTmpId']").val("-1");	
								}
								
								var objTmpIdStr = curEvtObj.parent().attr("id");
								var objTmpId = objTmpIdStr.substring (objIdStrPrefix.length);
								checkForDefaultValues(objTmpId, newId);
								
								updateMapping(objTmpId, newId);
								
							});
						}
						
						var updateMapping = function (objIdPar, newValPar) {
							var url = "../../dataExchange/importActionNew.do?action=updateMapping";
							contentBusy(true);
							$.ajax({
							  type: 'POST',
							  url: url,
							  data:{objId: objIdPar, newVal: newValPar},
							  success: updateMappingSuccess,
							  error: updateMappingError,
							  dataType: "json"
							});
							setModified();
						}
						
						var curSameAsEvtObj = null;
						
						var updateMappingSuccess = function (data, textStatus, jqXHR) {
							if (data != null) { //"Same As"
								curEvtObj.parent().find(".autocompleteDropdownSameAsText").remove();
								curEvtObj.css("width","50px");
								var sameAsMappingInput = $("<input class='autocompleteDropdownSameAsText' type='text' style='width: 440px;'>");
								curEvtObj.parent().append(sameAsMappingInput);
							} else {
								curEvtObj.css("width","495px");
								curEvtObj.parent().find(".autocompleteDropdownSameAsText").remove();
							}
							
							$("input.autocompleteDropdownSameAsText").unbind("click");
							$("input.autocompleteDropdownSameAsText").unbind("keyup");
							$("input.autocompleteDropdownSameAsText").click(sameAsEvtListener);
							$("input.autocompleteDropdownSameAsText").keyup(sameAsEvtListener);
							
							contentBusy(false);
						}
						
						var sameAsEvtListener = function(e) {
								$(".optionsContainer").css("display","none");
								var originatorObj = $(e.target);
								curSameAsEvtObj = originatorObj;
								var originatorObjAbsPos = originatorObj.position();
								var optionsContainerDiv = $("#sameAsOptionsDiv");
								optionsContainerDiv.css("display", "block");
								optionsContainerDiv.css("left", originatorObjAbsPos.left + "px");
								optionsContainerDiv.css("top", originatorObjAbsPos.top+20 + "px");
								optionsContainerDiv.css("width", originatorObj.width() + "px");
								optionsContainerDiv.find(".flowContainer").css("width", originatorObj.width() + "px");
								optionsContainerDiv.find(".flowContainer").css("height", optionsContainerDiv.height() + "px");
								var hiddenInput = originatorObj.parent().find("input[type='hidden'][name='sameAsTmpId']");

								if (timeoutObj != null) {
									window.clearTimeout (timeoutObj);
								}
								timeoutObj = window.setTimeout("getSameAsAutosuggestOptionValues(curSameAsEvtObj.val())", 300);

							}
						
						var getSameAsAutosuggestOptionValues = function (queryStr) {
							var url = "../../dataExchange/importActionNew.do?action=getSameAsOptionsAjaxAction";
							$.ajax({
							  type: 'GET',
							  url: url,
							  headers:{searchStr:queryStr.substring(0, 32)},
							  success: sameAsAutocompleteRequestSuccess,
							  dataType: "json"
							});
						}
						
						var sameAsAutocompleteRequestSuccess = function (data, textStatus, jqXHR) {
							var optionsContainer = $("#sameAsOptionsDiv #sameAsContent");
							
							
							var optionsMarkup = [];
							optionsMarkup.push("<table width='100%'>");
							$(data.items).each(function(index, element) {
					    	optionsMarkup.push("<tr>");
					    	optionsMarkup.push("<td align='left' nowrap class='optionItem'>");
				    		optionsMarkup.push(element.text);
					    	optionsMarkup.push("<input class='objId' type='hidden' value='");
					    	optionsMarkup.push(element.tmpId);
					    	optionsMarkup.push("'>");
					    	optionsMarkup.push("<input class='objCaption' type='hidden' value='");
					    	optionsMarkup.push(element.text);
					    	optionsMarkup.push("'>");
					    	optionsMarkup.push("</td>");
					    	optionsMarkup.push("</tr>");
					    	
							});
							optionsMarkup.push("</table>");
							optionsContainer.html(optionsMarkup.join(''));
							
							$("td.optionItem").click(function (e) {
								var originatorObj = $(e.target);
								$("#optionsDiv").css("display", "none");
								var newVal = originatorObj.find(".objCaption").val();
								if (newVal.trim() == "") newVal = "EMPTY VALUE";
								curSameAsEvtObj.val(newVal);
								
								var newId = originatorObj.find(".objId").val();
								curSameAsEvtObj.parent().find("input[type='hidden'][name='sameAsTmpId']").val(newId);

								$(".optionsContainer").css("display","none");
								
								var objTmpIdStr = curSameAsEvtObj.parent().attr("id");
								var objTmpId = objTmpIdStr.substring (objIdStrPrefix.length);
								var newId = originatorObj.find(".objId").val();
								updateSameAsMapping(objTmpId, newId);
								
							});
						}
						
						var updateSameAsMapping = function (objIdPar, newValPar) {
							var url = "../../dataExchange/importActionNew.do?action=updateSameAsMapping";
							contentBusy(true);
							$.ajax({
							  type: 'POST',
							  url: url,
							  data:{objId: objIdPar, newVal: newValPar},
							  success: updateSameAsMappingSuccess,
							  error: updateSameAsMappingError,
							  dataType: "json"
							});
							setModified();
						}

						var updateSameAsMappingError = function (data, textStatus, jqXHR) {
							contentBusy(false);
							alert ("Error updating mapping item !");
						}
						
						var updateSameAsMappingSuccess = function (data, textStatus, jqXHR) {
							contentBusy(false);
						}
						
						var updateMappingError = function (data, textStatus, jqXHR) {
							contentBusy(false);
							alert ("Error updating mapping item !");
						}
						
						var checkForDefaultValues = function() {
							$("input[type='hidden'][name='selAmpValue']").each(function(index, element) {
								var elementObj = $(element);
								var sameAsVal = elementObj.parent().find("input[type='hidden'][name='sameAsTmpId']").val();
								if (sameAsVal == null || sameAsVal < 0) {
									if (elementObj.val() == null || elementObj.val()=="0" || elementObj.val()=="") {
										elementObj.parent().find(".autocompleteDropdownText").css("color", "#707070").val(unmappedString);
									} else if (elementObj.val()==-1) {
										elementObj.parent().find(".autocompleteDropdownText").css("color", "#707070").val(addNewString);
									} else if (elementObj.val()==-3) {
										elementObj.parent().find(".autocompleteDropdownText").css("color", "#707070").val(dontImport);
									}
								} else {
									elementObj.parent().find(".autocompleteDropdownText").css("color", "#707070").val(sameAsString);
								}
							});
						}
						
						checkForDefaultValues();
						
						
						var modified = false;
						setModified = function() {
							modified = true;
							$("#mappingSaveDis").css("display","none");
							$("#mappingSave").css("display","block");
							
						}
						
						$("#mappingSave").click(function(e){
							var curForm = $("form[name='importFormNew']");
							curForm.attr("action", curForm.attr("action") + "&stayOnPage=true")
							submitForm();
						});
						
					</script>
					</logic:equal>
					
					<logic:equal name="importFormNew" property="page" value="<%= String.valueOf(ImportActionNew.IATI_IMPORT_PAGE_SESSIONS) %>">
					<table class="inside" width=977 border=0 cellpadding="0" cellspacing="0" style="margin:10px;">
						<tr>
								<td colspan="6" align=center background="/TEMPLATE/ampTemplate/img_2/ins_header.gif" class=inside>
								<b><digi:trn>Upload Sessions</digi:trn></b>
						</td>
						<logic:empty name="importFormNew" property="uploadSessions">
							<tr>
								<td bgcolor="#FFFFFF" class="inside" colspan="6">
									<div class="t_sm"><digi:trn>No Records Found</digi:trn> </div>
								</td>
							</tr>				
						</logic:empty>
						<tr>
							<td class="inside" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" align="center">
								<digi:trn>File Name</digi:trn>
							</td>
							<td class="inside" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" align="center">
								<digi:trn>Upload Date</digi:trn>
							</td>
							<td class="inside" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" align="center">
								<digi:trn>Last Edit Date</digi:trn>
							</td>
							<td class="inside" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" align="center">
								&nbsp;
							</td>
							
							
						</tr>
						<logic:notEmpty name="importFormNew" property="uploadSessions">
							<logic:iterate name="importFormNew" property="uploadSessions" id="us">
								<tr>
									<td bgcolor=#FFFFFF class=inside>
										<bean:write name="us" property="fileName"/>
									</td>
									<td bgcolor=#FFFFFF class=inside>
										<bean:write name="us" property="formatedUploadDate"/>
									</td>
									<td bgcolor=#FFFFFF class=inside>
										<bean:write name="us" property="formatedLastEditDate"/>
									</td>
									<td bgcolor=#FFFFFF class=inside>
										<a href="javascript:navigateTo('/dataExchange/importActionNew.do?action=loadUploadSession&objId=<bean:write name="us" property="id"/>')">
											Mapping
										</a>
									</td>
									
								</tr>
							</logic:iterate>
						</logic:notEmpty>
					</table>
					</logic:equal>
				</tr>
				</td>
			</table>
		</td>
	</tr>
</table>


<script language="javascript">
	$("form[name='importFormNew']").submit(function(e) {
		contentBusy(true);
	}); 
	
	function navigateTo (url) {
		contentBusy(true);
		window.location.href=url;
	}


	function submitForm() {
		$("form[name='importFormNew']").submit();
	}
	
	$("div.wizardNav").click (function (e) {
		var originatorObj = $(e.target);
		var navHolder = originatorObj.find("input");
		if (navHolder.val() != null && originatorObj.hasClass("wizardBtn")) {
			if (navHolder.val()=="submit") {
				submitForm();
			} else {
				navigateTo(navHolder.val());
			}
		}
		
	});	
</script>