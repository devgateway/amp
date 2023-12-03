<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ page import="java.util.Map"%>
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"  src="<digi:file src="ampModule/aim/scripts/dscript120_ar_style.js"/>"></script>

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
-->
</style>

<script type="text/javascript">
	function showOrHideKeywordsDiv(show) {
		if (show) {
			document.getElementById('textDiv').style.display = 'block';
			document.getElementById('keywordsDiv').style.display = 'block';
		} else {
			document.getElementById('textDiv').style.display = 'none';
			document.getElementById('keywordsDiv').style.display = 'none';
		}
	}
	function checkSelectedLanguages() {
		var submit = true;
		if ($("select[name='exportFormat'] option:selected").val() == 2) {
			var selected = $("input[name='selectedLanguages']:checked");
			var num = selected.length;
			var englishSelected = false;
			selected.each(function() {
				if ($(this).val() == 'en') {
					englishSelected = true;
				}

			});
			if (englishSelected) {
				if (num > 2 || num == 1) {
					submit = false;
				}
			} else {
				submit = false;
			}
		}
		if (!submit) {
			alert("<digi:trn>Please select only one language in addition to english language</digi:trn>")
		}
		return submit;
	}
</script>

<digi:instance property="importExportForm" />
<digi:context name="digiContext" property="context" />
<!--  AMP Admin Logo -->
<!-- End of Logo -->

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="1000" align="center">
	<tr>
		<td align=left valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%" border="0">
				<tr>
					<!-- Start Navigation -->
					<td height=33 bgcolor=#F2F2F2><span class=crumb style="color:#376091;">
						<c:set var="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" title="${translation}" ampModule="aim">
						<digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<span style="color:#000000"><digi:trn key="aim:translationManager">Translation Manager</digi:trn></span>
					</td>
					<td style="vertical-align: top;" width=300 rowspan="7">
				<table align="center" cellpadding="0" cellspacing="0" width="90%" border="0">
								<tr>
									<td>
										<!-- Other Links -->
										<table cellpadding="0" cellspacing="0" width="100">
											<tr>
												<td bgColor=#c9c9c7>
												<b style="font-size:12px; padding-left:5px;">
													<digi:trn key="aim:otherLinks">
													Other links
													</digi:trn>
												</b>
												</td>
												<td background="ampModule/aim/images/corner-r.gif"	height="17" width=17></td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td bgColor=#ffffff>
										<table cellPadding=5 cellspacing="1" width="100%" class="inside">
											<tr>
												<!--<td>
													<digi:img src="ampModule/aim/images/arrow-014E86.gif" 	width="15" height="10"/></td>-->
												<td class="inside">
													<digi:link href="/admin.do" ampModule="aim">
													<digi:trn key="aim:AmpAdminHome">
													Admin Home
													</digi:trn>
													</digi:link>
												</td>
											</tr>
											<tr>
												<td class="inside">
														<digi:link href="/msgCleanupManager.do?reset=true" >
															<digi:trn>Translation Cleanup Manager</digi:trn></digi:link>
												</td>
											</tr>
											<tr>
												<td class="inside">
														<digi:link href="/contentTrnImportExport.do" >
															<digi:trn>Content Translation Manager</digi:trn></digi:link>
												</td>
											</tr>
											
											<!-- end of other links -->
										</table>
									</td>
								</tr>
					  </table>
	  </td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height="16" vAlign="middle"  align="center">
                      <span style="font-size:12px; color:#000000;">
                        <b><digi:trn>Translation Manager</digi:trn></b><hr />
                      </span>
					</td>
				</tr>
				<tr>
					<td height="16" vAlign="middle">
						<digi:errors />
					</td>
				</tr>
				<tr>
					<td noWrap  width="700" vAlign="top">
					<table cellspacing="1" cellspacing="1" border="0" align="center">
					<logic:empty name="importExportForm" property="importedLanguages">
					<logic:notEmpty name="importExportForm" property="languages">
					<digi:form action="/importexport.do" method="post" >
							<logic:notEmpty name="importExportForm" property="errors">
								<tr><td><span style="color: red;"><digi:trn>Errors:</digi:trn></span></td></tr>
								<c:forEach items="${importExportForm.errors}" var="error">
									<tr>
										<td><span style="color: red;">${error}</span></td>
									</tr>
								</c:forEach>
								<tr><td>&nbsp;</td></tr>
							</logic:notEmpty>
							<tr>
								<td align="center">
									<b>
										<digi:trn key="aim:translationManagerLangFoundMsg">
											The following languages were found on this site:
										</digi:trn>
									</b><br />
								</td>
							</tr>
						<c:forEach items="${importExportForm.languages}" var="lang">
							<tr>
								<td align="center">
								<table border="0" cellspacing="2" cellpadding="2" width=100>
  <tr>
    <td align=right width=50><html:checkbox property="selectedLanguages" value="${lang}"/></td>
    <td width=50><digi:trn key="aim:TranslationManagerLangiage${lang}">
                                    ${lang}
                                    </digi:trn></td>
  </tr>
</table>

 								</td>
	 						</tr>
						 </c:forEach>
						<tr>
							<td align="center">
								<div class="checkbox">
									<label for="exportAmpOfflineTranslationsOnly">
										<input type="checkbox" name="exportAmpOfflineTranslationsOnly" id="exportAmpOfflineTranslationsOnly">
										<digi:trn>Export only AMP Offline translations</digi:trn>
									</label>
								</div>
							</td>
						</tr>
						 <tr>
								<td align="center">
									<digi:trn>Select export format</digi:trn>:
										<html:select property="exportFormat">
											<html:option value="1">XML</html:option>
											<html:option value="2"><digi:trn>Excel</digi:trn></html:option>
										</html:select>
 								</td>
	 						</tr>
							 <tr>
							 	<td align="center">
                                  <c:set var="translation">
                                    <digi:trn>Export</digi:trn>
                                  </c:set>
                                  <html:submit style="dr-menu" value="${translation}" property="export" onclick="return checkSelectedLanguages()"/>
                                </td>
							 </tr>
							 <tr>
							 <td align="center">
									<br/>
									<hr />
							</td>
							</tr>
					 </digi:form>
					</logic:notEmpty>

					<tr>
						<td><br/><br/><br/></td>
					</tr>

					<digi:form action="/importexport.do" method="post" enctype="multipart/form-data">
						<tr>
							<td align="left">
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
                                  <html:submit style="dr-menu" value="${translation}" property="import"/></td>
							 </tr>
					</digi:form>

					</logic:empty>
					<logic:notEmpty name="importExportForm" property="importedLanguages">
						<digi:form action="/importexport.do" method="post" >							
							<tr>
								<td colspan="2">
									<digi:trn key="aim:translationManagerLangFoundImportMsg">
									The following languages were found in the file you imported
									</digi:trn>
									<br/>
								</td>
							</tr>
							<logic:iterate name="importExportForm" property="importedLanguages" id="lang" type="java.lang.String">
								<tr>
									<td width="30%">
										<html:hidden property="selectedImportedLanguages" value="<%=lang %>" />
										<bean:write name="lang" />
										</td>										
										<td>
										<select name='<%="LANG:"+lang%>' class="inp-text" id="firstSelect">
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
							 <tr height="5px"><td colspan="2">&nbsp;</td></tr>
							 
							 <tr height="5px"><td colspan="2">&nbsp;</td></tr>
							 <tr>
							 <td align="center">
								<c:set var="translation">
									<digi:trn key="btn:translationManagerImport">
								 		Import
								 	</digi:trn>
								</c:set>
								</td>
								<td colspan="2"><br/><html:submit style="dr-menu" value="${translation}" property="importLang" onclick="return markAllKeywords()"/></td>
							 </tr>
							 <tr>
								<td colspan="2">
									<br/>
									<digi:trn key="aim:translationManagerLangSelectImportMsg">
									Please select the languages you want to update or to insert
									</digi:trn>
								</td>
								</tr>
								<tr>
								<td colspan="2">
								<table>
								<tr><th><digi:trn>Import Options</digi:trn></th></tr>
								<tr><td><digi:trn>Update local translations: updates translations in database if their creation date(not update) is older than one in file</digi:trn></td></tr>
								<tr><td><digi:trn>Overwrite local translations: Overwrites translations in database using values from file</digi:trn></td></tr>
								<tr><td><digi:trn>Insert the non existing translations: Inserts translations which doesn't exist in database</digi:trn></td></tr>
								</table>
								</td>
							</tr>
						 </digi:form>
					</logic:notEmpty>
					</table>
					</td>
					
  </tr>
				</table>
					</td>
				</tr>
			</table>

<script  type="text/javascript" src="<digi:file src="ampModule/aim/scripts/fileUpload.js"/>"></script>
<script type="text/javascript">
	initFileUploads('<digi:trn jsFriendly="true" key="aim:browse">Browse...</digi:trn>');
</script>




