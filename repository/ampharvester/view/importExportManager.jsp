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
</script>

<digi:instance property="aHarManagerForm" />

<table width="100%" cellspacing="2" cellpadding="2" valign="top" align="center" border="0">
	<tr>
		<th width="50%"><digi:trn key="ie:import">Import</digi:trn></th>
		<th width="50%"><digi:trn key="ie:export">Export</digi:trn></th>
	</tr>
<c:if test="${not empty aHarManagerForm.errorLog}">
	<tr align="center" bgcolor="#FFBBBB">
		<td colspan="2" >
			<digi:form action="/ieManager.do?actionType=error" method="post" enctype="multipart/form-data">
				<digi:trn key="amp:export:import:page:error">while operation there produced some error</digi:trn>
				<html:submit style="dr-menu" value="view errors"/>
				&nbsp;
			</digi:form>	
		</td>
	</tr>
</c:if>	
	<tr>
		<td align="center">
			<digi:form action="/ieManager.do?actionType=upload" method="post" enctype="multipart/form-data">
				<table>
					<tr>
						<td><digi:trn key="ie:selectteam">Select Team</digi:trn></td>
						<td>
							<html:select name="aHarManagerForm" property="selectedAmpTeamId" >
								<c:forEach var="vTeam" items="${aHarManagerForm.teamList}" varStatus="lStatus">
									<option value="${vTeam.ampTeamId}">${vTeam.name}</option>
								</c:forEach>
							</html:select>
						</td>
					</tr>
					<tr>
						<td><digi:trn key="ie:selectfile">Select File</digi:trn>  </td>
						<td>
							<a title="<digi:trn key="aim:FileLocation">Location of the document to be attached</digi:trn>">
								<div class="fileinputs">  <!-- We must use this trick so we can translate the Browse button. AMP-1786 -->
									<input id="uploadFile" name="uploadFile" type="file" class="file"/>
								</div>
							</a>
							<c:set var="translation">
								<digi:trn key="aim:translationmanagerimportbutton">Import</digi:trn>
							</c:set>
							<html:submit style="dr-menu" value="${translation}" property="importTreeVisibility" />
						</td>
					</tr>
				</table>
			</digi:form>	
		</td>
		<td align="center">
			<digi:form action="/ieManager.do?actionType=export" method="post" enctype="multipart/form-data">
				<html:submit style="dr-menu" value="Export"/>
			</digi:form>	
		</td>
	</tr>
</table>

<script type="text/javascript">
	initFileUploads();
 	if ( document.crDocumentManagerForm.pageCloseFlag.value == "true" ) {
 		window.opener.location.replace(window.opener.location.href);
 	 	window.close();
 	}
 </script>