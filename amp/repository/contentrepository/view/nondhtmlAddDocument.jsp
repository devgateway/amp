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
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule" %>

<%-- Looks like a dead file, not sure whether actually used anywhere --%>


<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/common.js"/>"></script>

<c:set var="translation_validation_title_chars">
			<digi:trn key="contentrepository:plsTitleChars">Please only use letters, digits, '_' and space !</digi:trn>
</c:set>
<c:set var="translation_validation_url">
			<digi:trn key="contentrepository:plsSpecifyUrl;">Please specify a Url !</digi:trn>
</c:set>
<c:set var="translation_validation_title">
			<digi:trn key="contentrepository:plsSpecifyTitle">Please specify a title !</digi:trn>
</c:set>
<c:set var="translation_validation_filedata">
			<digi:trn key="contentrepository:plsSpecifyPath">Please select a file path !</digi:trn>
</c:set>
<c:set var="translation_validation_url_invalid">
			<digi:trn key="contentrepository:urlInvalid">URL format invalid !</digi:trn>
</c:set>

<script language="JavaScript">
<!-- 
	
	function checkDot(str, cnt)
	{
		var count = 0, index=0, flag;
		for (i = 0;  i < str.length;  i++)
		{
			if(str.charAt(i) == ".")
				count = count + 1;
			if(count == 2)
			{
				index = i + 1;
				break;
			}
				
		}
		var diff = str.length - index;
		if(count >= cnt && diff > 1)
			flag =  true;
		else
		{
			alert(" ${translation_validation_url_invalid} ");
			document.crDocumentManagerForm.webLink.focus();
			flag =  false;
		}
		return flag;
	}
	
	function validateUrl(str)
	{
		str = trim(str);
		var flag;
		var temp="";
		if(str.substr(0,3) == "www")
			flag = checkDot(str, 2);
		else if(str.substr(0,7) == "http://")
		{
			temp = str.substring(7,10);
			if(temp == "www")
				flag = checkDot(str, 2);
			else
				flag = checkDot(str, 1);	
		}
		else
		{
			flag = false;
			alert(" ${translation_validation_url_invalid} ");
			document.crDocumentManagerForm.webLink.focus();
		}
		return flag;
	}
	function usesAllowedCharacters(str) {
		//var regexp	= new RegExp("[a-zA-Z0-9_/-/ ]+");
		var regexp	= new RegExp("[A-Za-zÀÁÃÄÇÈÉËÌÍÏÑÒÓÕÖÙÚÜàáãäçèéëìíïñòóõöùúü0-9_/-/ ]+");
		var found	= regexp.exec(str);
		if (found != str)
			return false;
		return true;
	}
	function validateResource()
	{
		var titleFlag = isEmpty(document.crDocumentManagerForm.docTitle.value);
		var urlFlag = isEmpty(document.crDocumentManagerForm.webLink.value);
		if(titleFlag == true && urlFlag == true)
		{
			alert(" ${translation_validation_title} ${translation_validation_url}");
			document.crDocumentManagerForm.docTitle.focus();
			return false;
		}
		else
		{
			if(titleFlag == true)
			{
				alert(" ${translation_validation_title} ");
				document.crDocumentManagerForm.docTitle.focus();
				return false;
			}
			if(urlFlag == true)
			{
				alert(" ${translation_validation_url} ");
				document.crDocumentManagerForm.webLink.focus();
				return false;
			}
			
	/* AMP-2883		
	
	       if ( !usesAllowedCharacters(document.crDocumentManagerForm.docTitle.value) ) {
				alert(" ${translation_validation_title_chars} ");
				document.crDocumentManagerForm.docTitle.focus();
				return false;
			}
			else
			{
				flag = validateUrl(document.crDocumentManagerForm.webLink.value);
				return flag;
			}
	*/			
		}
		return true;
	}
	

	function validateDocument()
	{
		var titleFlag = isEmpty(document.crDocumentManagerForm.docTitle.value);
		var fileFlag = isEmpty(document.crDocumentManagerForm.fileData.value);
		if(titleFlag == true && fileFlag == true)
		{
			alert(" ${translation_validation_title} ${translation_validation_filedata} ");
			document.crDocumentManagerForm.docTitle.focus();
			return false;
			
		}
		else
		{
			if(titleFlag == true)
			{
				alert(" ${translation_validation_title} ");
				document.crDocumentManagerForm.docTitle.focus();
				return false;
			}

			/*
			if ( !usesAllowedCharacters(document.crDocumentManagerForm.docTitle.value) ) {
				alert(" ${translation_validation_title_chars} ");
				document.crDocumentManagerForm.docTitle.focus();
				return false;
			}
			*/
			if(fileFlag == true)
			{
				alert(" ${translation_validation_filedata} ");
				document.crDocumentManagerForm.fileData.focus();
				return false;
			}
		}
		return true;
	}
	
	function addDocument() 
	{
		var resourceFlag, docFlag;
		if(document.crDocumentManagerForm.webResource.value == "false")
		{
			docFlag = validateDocument();
			
		}
		else
			resourceFlag = validateResource();		
		if(docFlag == true || resourceFlag == true)
		{

			document.crDocumentManagerForm.action = "/contentrepository/addTemporaryDocument.do";	
		    document.crDocumentManagerForm.submit();
		}
	}

	function load() {
		document.crDocumentManagerForm.docTitle.focus();
	}

	function unload() {
	}
	function closeWindow() {
		window.close();			  
	}

-->
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
<script  type="text/javascript" src="<digi:file src="ampModule/aim/scripts/fileUpload.js"/>"></script>

<digi:instance property="crDocumentManagerForm" />
<digi:form action="/addTemporaryDocument.do" method="post" enctype="multipart/form-data" onsubmit="return false;">

<html:hidden property="webResource" />
<html:hidden property="pageCloseFlag" />
<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border="0">
	<tr><td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align="center" vAlign="top">
									
					<span class="error">
						<digi:errors />
						<logic:iterate id="element" name="crDocumentManagerForm" property="errors">
						   <digi:trn key="${element.key}">
						       <bean:write name="element" property="value"/>
						   </digi:trn>
						</logic:iterate>
					</span>
					<span class="message">
						<logic:iterate id="element" name="crDocumentManagerForm" property="messages">
						    <digi:trn key="${element.key}">
						        <bean:write name="element" property="value"/>
						    </digi:trn>
						</logic:iterate>
					</span>
				
				</td>
			</tr>
			<tr>
				<td align=left valign="top">
					<table bgcolor=#f4f4f2 cellpadding="0" cellspacing="0" width="100%" class=box-border-nopadding>
						<tr bgcolor="#006699">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<logic:equal name="crDocumentManagerForm" property="webResource" value="false">
									<digi:trn key="aim:selectDocument">
									Select document</digi:trn>
								</logic:equal>
								<logic:equal name="crDocumentManagerForm" property="webResource" value="true">
									<digi:trn key="aim:selectWebResource">
									Select web resource</digi:trn>
								</logic:equal>								
								
							</td></tr>
						<tr>
							<td align="center" bgcolor=#ECF3FD>
								<table cellSpacing=2 cellPadding=2>
									<logic:equal name="crDocumentManagerForm" property="webResource" value="false">
									<field:display name="Document Title" feature="Related Documents">
									<tr>
										<td>
											<FONT color=red>*</FONT>
											<a title="<digi:trn key="aim:TitlefortheDocument">Title of the document to be attached</digi:trn>">
												<digi:trn key="aim:title">Title</digi:trn>
											</a>
										</td>
										<td>
											<a title="<digi:trn key="aim:TitlefortheDocument">Title of the document to be attached</digi:trn>">
												<html:text property="docTitle"  styleClass="inp-text" size="50"/>
											</a>
										</td>
									</tr>
									</field:display>
								</logic:equal>
								<logic:equal name="crDocumentManagerForm" property="webResource" value="true">
									<field:display name="Web Resources Title" feature="Web Resources">
									<tr>
										<td>
											<FONT color=red>*</FONT>
											<a title="<digi:trn key="aim:TitlefortheDocument">Title of the document to be attached</digi:trn>">
												<digi:trn key="aim:title">Title</digi:trn>
											</a>
										</td>
										<td>
											<a title="<digi:trn key="aim:TitlefortheDocument">Title of the document to be attached</digi:trn>">
												<html:text property="docTitle"  styleClass="inp-text" size="50"/>
											</a>
										</td>
									</tr>
									</field:display>
								</logic:equal>		
								<!--  -->
								<logic:equal name="crDocumentManagerForm" property="webResource" value="false">
									<field:display name="Document Description" feature="Related Documents">
									<tr>
										<td>
											<a title="<digi:trn key="aim:DescoftheDocument">Description of the contents and intent of the document</digi:trn>">
												<digi:trn key="aim:description">Description</digi:trn>
											</a>
										</td>
										<td>
											<a title="<digi:trn key="aim:DescoftheDocument">Description of the contents and intent of the document</digi:trn>">
												<html:textarea property="docDescription" rows="4" cols="50" styleClass="inp-text"/>
											</a>
										</td>
									</tr>
									</field:display>
								</logic:equal>
								<logic:equal name="crDocumentManagerForm" property="webResource" value="true">
									<field:display name="Web Resource Description" feature="Web Resources">
									<tr>
										<td>
											<a title="<digi:trn key="aim:DescoftheDocument">Description of the contents and intent of the document</digi:trn>">
												<digi:trn key="aim:description">Description</digi:trn>
											</a>
										</td>
										<td>
											<a title="<digi:trn key="aim:DescoftheDocument">Description of the contents and intent of the document</digi:trn>">
												<html:textarea property="docDescription" rows="4" cols="50" styleClass="inp-text"/>
											</a>
										</td>
									</tr>
									</field:display>
								</logic:equal>								
									
								<logic:equal name="crDocumentManagerForm" property="webResource" value="false">
									<field:display name="Document Notes" feature="Related Documents">
									<tr>
										<td>
											<a title="<digi:trn key="cr:NotesForDocument">Notes regarding the document</digi:trn>">
											<digi:trn key="cr:comments">Notes</digi:trn>
											</a>
										</td>
										<td>
											<a title="<digi:trn key="cr:NotesForDocument">Notes regarding the document</digi:trn>">
											<html:textarea property="docNotes" rows="3" cols="50" styleClass="inp-text"/>
											</a>
										</td>
									</tr>
									</field:display>
								</logic:equal>
								<logic:equal name="crDocumentManagerForm" property="webResource" value="true">
									<field:display name="Web Resources Notes" feature="Web Resources">
									<tr>
										<td>
											<a title="<digi:trn key="cr:NotesForDocument">Notes regarding the document</digi:trn>">
											<digi:trn key="cr:comments">Notes</digi:trn>
											</a>
										</td>
										<td>
											<a title="<digi:trn key="cr:NotesForDocument">Notes regarding the document</digi:trn>">
											<html:textarea property="docNotes" rows="3" cols="50" styleClass="inp-text"/>
											</a>
										</td>
									</tr>
									</field:display>
								</logic:equal>
									
								<logic:equal name="crDocumentManagerForm" property="webResource" value="false">
									<field:display name="Document Type" feature="Related Documents">
										<tr>
											<td>
												<a title="<digi:trn key="aim:typeOfTheDocumentDescription">Select type of document</digi:trn>">
													<digi:trn key="aim:typeOfTheDocument">Document type</digi:trn>
												</a>
											</td>
											<td>
												<c:set var="translation">
													<digi:trn key="aim:addActivityDocTypeFirstLine">Please select from below</digi:trn>
												</c:set>
												<a title="<digi:trn key="aim:typeOfTheDocumentDescription">Select type of document</digi:trn>">
													<category:showoptions firstLine="${translation}" name="crDocumentManagerForm" property="docType" categoryName="<%=org.digijava.ampModule.categorymanager.util.CategoryConstants.DOCUMENT_TYPE_NAME %>" styleClass="inp-text"/>
												</a>
											</td>
										</tr>
									</field:display>
								</logic:equal>
								<logic:equal name="crDocumentManagerForm" property="webResource" value="true">
									<field:display name="Web Resources Document Type" feature="Web Resources">
										<tr>
											<td>
												<a title="<digi:trn key="aim:typeOfTheDocumentDescription">Select type of document</digi:trn>">
													<digi:trn key="aim:typeOfTheDocument">Document type</digi:trn>
												</a>
											</td>
											<td>
												<c:set var="translation">
													<digi:trn key="aim:addActivityDocTypeFirstLine">Please select from below</digi:trn>
												</c:set>
												<a title="<digi:trn key="aim:typeOfTheDocumentDescription">Select type of document</digi:trn>">
													<category:showoptions firstLine="${translation}" name="crDocumentManagerForm" property="docType" categoryName="<%=org.digijava.ampModule.categorymanager.util.CategoryConstants.DOCUMENT_TYPE_NAME %>" styleClass="inp-text"/>
												</a>
											</td>
										</tr>
									</field:display>
								</logic:equal>
									
									<%-- 
									
									<tr>
										<td>
											<a title="<digi:trn key="aim:languageOfTheDocumentDescription">Select document language</digi:trn>">
											<digi:trn key="aim:langOfTheDocument">Document Language</digi:trn>
											</a>
										</td>
										<td>
											<c:set var="translation">
													<digi:trn key="aim:addActivityDocLanguageFirstLine">Please select from below</digi:trn>
											</c:set>
											<a title="<digi:trn key="aim:languageOfTheDocumentDescription">Select document language</digi:trn>">
											<category:showoptions firstLine="${translation}" name="crDocumentManagerForm" property="docLang" keyName="<%=org.digijava.ampModule.categorymanager.util.CategoryConstants.DOCUMENT_LANGUAGE_KEY %>" styleClass="inp-text"/>
											</a>
										</td>
									</tr>
									
									--%>
									<logic:equal name="crDocumentManagerForm" property="webResource" value="false">
									<field:display name="Document FileName" feature="Related Documents">
									<tr>
										<td>
										<FONT color=red>*</FONT>
											<a title="<digi:trn key="aim:FileLocation">Location of the document to be attached</digi:trn>">										  <digi:trn key="aim:file">File</digi:trn>
											</a>
										</td>
										<td>
											<a title="<digi:trn key="aim:FileLocation">Location of the document to be attached</digi:trn>">
												<div class="fileinputs">  <!-- We must use this trick so we can translate the Browse button. AMP-1786 -->
													<input id="fileData" name="fileData" type="file" class="file"/>
												</div>
											</a>
										</td>
									</tr>
									</field:display>
									</logic:equal>
									<logic:equal name="crDocumentManagerForm" property="webResource" value="true">
									<field:display name="Web Resources Url" feature="Web Resources">
									<tr>
										<td>
										<FONT color=red>*</FONT>
										<a title="<digi:trn key="aim:WebSource">Web links related to the project</digi:trn>">
										<digi:trn key="aim:webResource">Web resource</digi:trn></a>
										</td>
										<td>
											<a title="<digi:trn key="aim:WebSource">Web links related to the project</digi:trn>">
											<html:text property="webLink" />
											</a>
										</td>
									</tr>
									</field:display>
									</logic:equal>
									
									
																	
									<tr>
										<td align="center" colspan=2>
											<table cellPadding=5>
												<tr>
													<td>
														<input type="button" value="<digi:trn>Add</digi:trn>" class="dr-menu" onclick="addDocument()" id="addDocBtn">
													</td>
													<td>
														<input type="reset" value="<digi:trn>Clear</digi:trn>" class="dr-menu">													
													</td>
													<td>
														<input type="button" value="<digi:trn>Close</digi:trn>" class="dr-menu"
														onclick="closeWindow()">
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
	</td></tr>
</table>
</digi:form>

<script type="text/javascript">
	initFileUploads('<digi:trn jsFriendly="true" key="aim:browse">Browse...</digi:trn>');
	if ( document.crDocumentManagerForm.pageCloseFlag.value == "true" ) {
			window.opener.location.replace(window.opener.location.href); 
			window.close();
		}
	
	var enterBinder	= new EnterHitBinder('addDocBtn');
</script>
