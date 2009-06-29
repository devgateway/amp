<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ page import="java.util.Map"%>
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"  src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>

<!-- this is for the nice tooltip widgets -->
<DIV id="TipLayer"  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>

<script langauage="JavaScript">
	var importHelp="<digi:trn>Translation Manager</digi:trn>"
	var separateKeywords="<digi:trn>Please separate keywords by semicolons</digi:trn>"

	function enableChkBox(chkBox) {
		alert(chkBox);
	}

	function addKeyword(keyword) {
	    var list = document.getElementById('keywords');
	    if (list == null || keyword == null || keyword.value == null || keyword.value == "") {
	      return;
	    }

	    var flag=false;
	    for(var i=0; i<list.length;i++){
	      if(list.options[i].value==keyword.value &&list.options[i].text==keyword.value){
	        flag=true;
	        break;
	      }
	    }
	    if(flag){
	      return false;
	    }

		var keywordVal=keyword.value;
		while(keywordVal.indexOf(";")!=-1){		
			var optionValue=keywordVal.substring(0,keywordVal.indexOf(";"));		
			addOption(list,optionValue,optionValue);				
			keywordVal=keywordVal.substring(keywordVal.indexOf(";")+1);		
		}
		if(keywordVal.length>0){
			addOption(list,keywordVal,keywordVal);
		}	

		keyword.value = "";
	  }

	  function addOption(list, text, value){
		if (list == null) {
		   return;
		}
		var option = document.createElement("OPTION");
		option.value = value;
		option.text = text;
		list.options.add(option);
		return false;
	  }

	  function removeKeyword() {
		var list = document.getElementById('keywords');
		if (list == null) {
		    return;
		}
		var index = list.selectedIndex;
		if (index != -1) {
		   for(var i = list.length - 1; i >= 0; i--) {
			   if (list.options[i].selected) {
		          list.options[i] = null;
		        }
		   }
		   if (list.length > 0) {
		      list.selectedIndex = index == 0 ? 0 : index - 1;
		   }
		}
	  }

	  function markAllKeywords(){
		  var list = document.getElementById('keywords');  
		  if(list!=null){
			for(var i = 0; i < list.length; i++) {
				list.options[i].selected = true;
			}
		  }
		  return true;
	  }
</script>


<style type="text/css">
<!--
div.fileinputs {
	position: relative;
	height: 30px;
	width: 300px;
}

input.file {
	width: 300px;
	margin: 0;
}

input.file.hidden {
	position: relative;
	text-align: right;
	-moz-opacity:0 ;
	filter:alpha(opacity: 0);
	width: 300px;
	opacity: 0;
	z-index: 2;
}

div.fakefile {
	position: absolute;
	top: 0px;
	left: 0px;
	width: 300px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}

div.fakefile input {
	margin-bottom: 5px;
	margin-left: 0;
	width: 217px;
}
div.fakefile2 {
	position: absolute;
	top: 0px;
	left: 217px;
	width: 300px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}
div.fakefile2 input{
	width: 83px;
}

	.tableEven {
		background-color:#dbe5f1;
		font-size:8pt;
		padding:2px;
	}

	.tableOdd {
		background-color:#FFFFFF;
		font-size:8pt;
		padding:2px;
	}
	 
	.Hovered {
		background-color:#a5bcf2;
	}
		
-->
</style>


<script type="text/javascript">
	var W3CDOM = (document.createElement && document.getElementsByTagName);

	function initFileUploads() {
		if (!W3CDOM) return;
		var fakeFileUpload = document.createElement('div');
		fakeFileUpload.className = 'fakefile';
		fakeFileUpload.appendChild(document.createElement('input'));

		var fakeFileUpload2 = document.createElement('div');
		fakeFileUpload2.className = 'fakefile2';


		var button = document.createElement('input');
		button.type = 'button';

		button.value = '<digi:trn key="aim:browse">Browse...</digi:trn>';
		fakeFileUpload2.appendChild(button);

		fakeFileUpload.appendChild(fakeFileUpload2);
		var x = document.getElementsByTagName('input');
		for (var i=0;i<x.length;i++) {
			if (x[i].type != 'file') continue;
			if (x[i].parentNode.className != 'fileinputs') continue;
			x[i].className = 'file hidden';
			var clone = fakeFileUpload.cloneNode(true);
			x[i].parentNode.appendChild(clone);
			x[i].relatedElement = clone.getElementsByTagName('input')[0];

 			x[i].onchange = x[i].onmouseout = function () {
				this.relatedElement.value = this.value;
			}
		}
	}

	function initFileUploads3() {
		if (!W3CDOM) return;
		var fakeFileUpload = document.createElement('div');
		fakeFileUpload.className = 'fakefile';
		fakeFileUpload.appendChild(document.createElement('input'));
		var image = document.createElement('img');
		image.src='pix/button_select.gif';
		fakeFileUpload.appendChild(image);
		var x = document.getElementsByTagName('input');
		for (var i=0;i<x.length;i++) {
			if (x[i].type != 'file') continue;
			if (x[i].parentNode.className != 'fileinputs') continue;
			x[i].className = 'file hidden';
			var clone = fakeFileUpload.cloneNode(true);
			x[i].parentNode.appendChild(clone);
			x[i].relatedElement = clone.getElementsByTagName('input')[0];
			x[i].onchange = x[i].onmouseout = function () {
				this.relatedElement.value = this.value;
			}
		}
	}
	
	function showOrHideKeywordsDiv(show){
		if(show){
			document.getElementById('textDiv').style.display='block';
			document.getElementById('keywordsDiv').style.display='block';
		}else{
			document.getElementById('textDiv').style.display='none';
			document.getElementById('keywordsDiv').style.display='none';
		}
	}

	function changeDivsVisibility(){
		var whatToDoWithTrns=document.getElementById('firstSelect').value;
		var whatToDoWithKeywords=document.getElementById('mySelect').value;
		
		if(whatToDoWithTrns=='nonexisting'){
			document.getElementById('mySelectsTextDiv').style.display='none';
			document.getElementById('mySelect').style.display='none';
			showOrHideKeywordsDiv(false);
		}else {
			document.getElementById('mySelectsTextDiv').style.display='block';
			document.getElementById('mySelect').style.display='block';
			if(whatToDoWithKeywords=='updateEverything'){
				showOrHideKeywordsDiv(false);
			}else if(whatToDoWithKeywords=='skip' || whatToDoWithKeywords=='update'){
				showOrHideKeywordsDiv(true);
			}
		}
	}

	function setStripsTable(tableId, classOdd, classEven) {
		var tableElement = document.getElementById(tableId);
		rows = tableElement.getElementsByTagName('tr');
		for(var i = 0, n = rows.length; i < n; ++i) {
			if(i%2 == 0)
				rows[i].className = classEven;
			else
				rows[i].className = classOdd;
		}
		rows = null;
	}
	function setHoveredTable(tableId, hasHeaders) {

		var tableElement = document.getElementById(tableId);
		if(tableElement){
	    	var className = 'Hovered',
	        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
	        rows      = tableElement.getElementsByTagName('tr');

			for(var i = 0, n = rows.length; i < n; ++i) {
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
	
</script>

<digi:instance property="aimTranslatorManagerForm" />
<digi:context name="digiContext" property="context" />
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td align=left vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:translationManager">Translation Manager</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height="16" vAlign="middle" width="571">
                      <span class=subtitle-blue>
                        <digi:trn>Translation Manager</digi:trn>
                      </span>
					</td>
				</tr>
				<tr>
					<td height="16" vAlign="middle" width="571">
						<digi:errors />
					</td>
				</tr>
				
							<tr>
								<td noWrap width=100% vAlign="top">
								<table width="100%" cellspacing=1 cellSpacing=1 border=0>
									<logic:empty name="aimTranslatorManagerForm" property="importedLanguages">
									<tr>
										<td>
											<fieldset>
												<legend><digi:trn key="aim:translationManagerAvailableLanguage">Available translations</digi:trn></legend>
												<table>
													<logic:notEmpty name="aimTranslatorManagerForm" property="languages">
														<digi:form action="/translationManager.do" method="post" >
															<tr>
																<td>
																	<digi:trn key="aim:translationManagerLangFoundMsg">
																	<b>The following languages are currently available:</b>
																	</digi:trn>
																</td>
															</tr>
															<tr>
																<td>
																	<div  style="overflow:auto;width:100%;height:auto;max-height:220px;"  >
																		<table width="100%" BORDER=0 cellpadding="0" cellspacing="0" id="dataTable"    >
																			<c:forEach items="${aimTranslatorManagerForm.languages}" var="lang">
																				<tr>
																					<td>
																						<html:checkbox property="selectedLanguages" value="${lang}"/>
													                                    <digi:trn key="aim:TranslationManagerLangiage${lang}">
													                                    ${lang}
													                                    </digi:trn>
																						<br/>
													 								</td>
														 						</tr>
																			</c:forEach>
																		</table>
																	</div>
																</td>
															</tr>
															<tr>
																 <td>
																	<br/>
																	<digi:trn>To export a language or languages, please select them above.</digi:trn>
																</td>
															</tr>
															<tr>
															 	<td>
								                                  <c:set var="translation">
								                                    <digi:trn>Export</digi:trn>
								                                  </c:set>
								                                  <html:submit style="dr-menu" value="${translation}" property="export"/>
								                            	</td>
															</tr>
														</digi:form>
													</logic:notEmpty>
												</table>
											</fieldset>
										</td>
										<td valign="top">
											<fieldset>
												<legend><digi:trn key="aim:translationManagerImportLanguage">Import translations</digi:trn></legend>
												<table>
													<digi:form action="/translationManager.do" method="post" enctype="multipart/form-data">
														<tr>
														 	<td>
							                                	<digi:trn>To import a language please select it below and then import it into the Admin Section.</digi:trn>
															</td>
														</tr>
														<tr>
															<td>
																<!-- <html:file property="fileUploaded"></html:file> -->
																<div class="fileinputs">  <!-- We must use this trick so we can translate the Browse button. AMP-1786 -->
																	<!-- CSS content must be put in a separated file and a class must be generated -->
																	<input id="fileUploaded" name="fileUploaded" type="file" class="file">
																</div>
															</td>
														</tr>
														<tr>
														 	<td>
							                                  <c:set var="translation">
							                                  	<digi:trn >Import</digi:trn>
							                                  </c:set>
							                                  <html:submit style="dr-menu" value="${translation}" property="import"/>
															</td>
														</tr>
													</digi:form>
												</table>
											</fieldset>
										</td>
									</tr>
								</logic:empty>
								<logic:notEmpty name="aimTranslatorManagerForm" property="importedLanguages">
									<digi:form action="/translationManager.do" method="post" >							
										<tr>
											<td colspan="2">
												<digi:trn key="aim:translationManagerLangFoundImportMsg">
													<b>The following languages where found in the file you imported:</b>
												</digi:trn>
												<br/>
											</td>
										</tr>
										<tr>
											<td>
												<div  style="overflow:auto;width:400;height:auto;max-height:220px;"  >
													<table width="100%" BORDER=0 cellpadding="0" cellspacing="0" id="dataTable"    >
														<logic:iterate name="aimTranslatorManagerForm" property="importedLanguages" id="lang" type="java.lang.String">
															<tr>
																<td width="30%">
																	<html:hidden property="selectedImportedLanguages" value="<%=lang %>" />
																	<bean:write name="lang" />
																</td>										
																<td align="center">
																	<select name='<%="LANG:"+lang%>' class="inp-text" id="firstSelect" onchange="changeDivsVisibility()">
																		<option value="-1" selected>
																			<digi:trn key="aim:translationManagerImportPleaseSelect">
																				-- Please select --
																			</digi:trn>
																		</option>
																		<option value="update">
																			<digi:trn key="aim:translationManagerImportUpdateLocal">
																				Update local translations
																			</digi:trn>
																		</option>
																		<option value="overwrite">
																			<digi:trn key="aim:translationManagerImportOverwriteLocal">
																				Overwrite local translations
																			</digi:trn>
																		</option>
																		<option value="nonexisting">
																			<digi:trn key="aim:translationManagerImportNonExistingLocal">
																				Insert the non existing translations
																			</digi:trn>
																		</option>
																	</select>
								 								</td>
									 						</tr>
														 </logic:iterate>
													</table>
												</div>
											</td>
										</tr>
					
										<tr height="5px"><td colspan="2">&nbsp;</td></tr>
										<tr>
										 	<td colspan="2">
										 		<div style="visibility: visible" id="mySelectsTextDiv">
										 			<digi:trn>
														<b>Please Select what to do with translations</b>
													</digi:trn>
										 		</div>							 		 
										 	</td>
										</tr>
										<tr>
										 	<td>
										 		<html:select name="aimTranslatorManagerForm" property="skipOrUpdateTrnsWithKeywords" onchange="changeDivsVisibility()" styleId="mySelect" styleClass="inp-text">
										 			<html:option value="skip"><digi:trn>skip all marked with this keyword </digi:trn></html:option>
										 			<html:option value="update"><digi:trn>skip all except those marked with this keyword </digi:trn></html:option>
										 			<html:option value="updateEverything"><digi:trn>do not skip any translations </digi:trn></html:option>
										 		</html:select>
										 	</td>
										</tr>
										
										<tr height="5px"><td colspan="2">&nbsp;</td></tr>							 
										<tr>
										 	<td colspan="2">
										 		<div style="visibility: visible" id="textDiv">
										 			<digi:trn>
														<b>Please enter here keywords of the translations you want to be skipped/updated during import</b>
													</digi:trn>
										 			&nbsp;
										 			<img src="../ampTemplate/images/help.gif" onmouseover="stm([importHelp,separateKeywords],Style[15])" onmouseout="htm()"/>
										 		</div>									 		 
										 	</td>
										</tr>
										<tr>
										 	<td>
										 		<div style="visibility: visible;" id="keywordsDiv">
										 			<input id="keyword" type="text" style="width:250px;height: 20px" align="top">
											 		<input type="button" style="width:100px;vertical-align: top;" onclick="addKeyword(document.getElementById('keyword'))" value="<digi:trn key="message:addUsBtn">Add >></digi:trn>">
													<br>
													<html:select multiple="multiple" styleId="keywords" name="aimTranslatorManagerForm" property="keywords" size="11" styleClass="inp-text" style="width: 250px;height: 50px;">
													</html:select>
													<input type="button" style="width:100px;vertical-align: top;" onclick="removeKeyword()" value="<<<digi:trn key="message:rmbtn">Remove</digi:trn>" >
										 		</div>
										 	</td>
										</tr>												 
										 
										<tr height="5px"><td colspan="2">&nbsp;</td></tr>
										<tr>
											<c:set var="translation">
												<digi:trn key="btn:translationManagerImport">
											 		Import
											 	</digi:trn>
											</c:set>
											<td colspan="2"><br/><html:submit style="dr-menu" value="${translation}" property="importLang" onclick="return markAllKeywords()"/></td>
										</tr>
										<tr>
											<td colspan="2">
												<br/>
												<digi:trn key="aim:translationManagerLangSelectImportMsg">
													<b>Please select the languages you want to update or to insert</b>
												</digi:trn>
											</td>
										</tr>
									 </digi:form>
								</logic:notEmpty>
								</table>
								</td>
								<td>

								</td>
							</tr>
						</table>
					</td>
				</tr>
			
</table>
<script type="text/javascript">
	initFileUploads();
	setStripsTable("dataTable", "tableEven", "tableOdd");
	setHoveredTable("dataTable", false);
</script>




