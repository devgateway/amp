<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

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
	width: 100px;
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


<!-- Yahoo Panel --> 
<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/container.css'/>"/>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-dom-event.js'/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/container-min.js'/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dragdrop-min.js'/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-min.js'/>"></script>


<script langauage="JavaScript">	
	
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

		button.value = '<digi:trn>Browse...</digi:trn>';
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

</script>

<digi:instance property="addressbookForm" />
<table width=750 cellspacing="2" cellpadding="2" valign="top" align="left" border="0">
	<tr>
	<!-- Start Navigation -->
		<td height="33"><span class="crumb">
			<c:set var="translation">
				<digi:trn>Click here to goto Admin Home</digi:trn>
			</c:set>
		    <digi:link href="/admin.do" styleClass="comment" title="${translation}" >
				<digi:trn>Admin Home</digi:trn>
			</digi:link>&nbsp;&gt;&nbsp;
			<digi:trn>Export/Import Contacts</digi:trn>
		</td>
	<!-- End navigation -->
	</tr>
	<tr>
		<td colspan="2" >
			 <span class=subtitle-blue>
                       <digi:trn key="aim:ContactManagerHeader">
                     		 Contact Manager
                       </digi:trn>
               </span>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	
	<tr>
		<td align="left" width="50%" valign="top">
			<digi:form action="/exportImportContacts.do?actionType=importContacts" method="post" enctype="multipart/form-data">
				<fieldset>
					<legend><digi:trn key="aim:contactmanagerimportbutton">Import</digi:trn></legend>
					<a title="<digi:trn key="aim:FileLocation">Location of the document to be attached</digi:trn>">
						<div class="fileinputs">  <!-- We must use this trick so we can translate the Browse button. AMP-1786 -->
							<input id="fileUploaded" name="fileUploaded" type="file" class="file"/>
						</div>
						<input type="button" onclick="submit()" value='<digi:trn key="aim:contactmanagerimportbutton">Import</digi:trn>'/>
					</a>
				</fieldset>
			</digi:form>
		</td>
		<td align="left" width="50%" valign="top">
			<digi:form action="/exportImportContacts.do?actionType=exportContacts">
				<fieldset>
					<legend><digi:trn key="aim:contactmanagerexportbutton">Export</digi:trn></legend>
					<input type="button" onclick="submit()" value='<digi:trn key="aim:contactmanagerexportbutton">Export</digi:trn>'/>
				</fieldset>	
			</digi:form>
		</td>
	</tr>
</table>

<script type="text/javascript">
	initFileUploads();
</script>
