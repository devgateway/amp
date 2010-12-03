<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %> 

<script langauage="JavaScript">
	function checkEmpty(){
		var title=document.getElementById('title').value;
		if(title==0){
			var msg='<digi:trn jsFriendly="true">Please enter name </digi:trn>';
			alert(msg);
			return false;
		}
		return true;
	}
</script>

<digi:form action="/saveFile.do" method="post" enctype="multipart/form-data">
	<p align="center"><b><font size="4" color="#00008B">
			<digi:errors/>
	</font></b></p>	
	<div align="center">
		<table width="60%">
			<tr>
				<td align="center">
					<strong>
						<font size="4" color="#00008B">
							<c:out value="${sdmForm.documentTitle}"/>
						</font>
					</strong>
				</td>
			</tr>
		</table>
	</div>
		
	
	<b><font size="4" color="#00008B"><p><digi:trn>Submit a File to a Document!</digi:trn></p></font></b>
	
	<table border="1" cellspacing="1" width="100%">
		<tr>
			<td noWrap width="50%"><font size="2">
				<digi:trn key="sdm:alignment">Alignment:</digi:trn></font><BR>
				<html:radio name="sdmForm" property="alignment" value="1"/><font size="2">
				<digi:trn key="sdm:left">Left</digi:trn></font>
				<html:radio name="sdmForm" property="alignment" value="2"/><font size="2">
				<digi:trn key="sdm:center">Center</digi:trn></font>
				<html:radio name="sdmForm" property="alignment" value="3"/><font size="2">
				<digi:trn key="sdm:right">Right</digi:trn></font>
			</td>
			<td noWrap width="25%"><font size="2">
				<digi:trn key="sdm:font">Font:</digi:trn></font>
				<html:select property="selectedFontFace">
				<html:option value="Arial">Arial</html:option>
				<html:option value="Times New Roman">Times New Roman</html:option>
				<html:option value="Tahoma">Tahoma</html:option>
				<html:option value="Verdana">Verdana</html:option>
				<html:option value="Modern">Modern</html:option>
				<html:option value="Comic Sans MS">Comic Sans MS</html:option>
				<html:option value="Courier New">Courier New</html:option>
				<html:option value="Wingdings">Wingdings</html:option></html:select>
			</td>
			<td noWrap width="25%"><font size="2">
				<digi:trn key="sdm:fontSize">Font size:</digi:trn></font>
				<html:select property="selectedFontSize">
				<html:option value="1">Tiny</html:option>
				<html:option value="2">Small</html:option>
				<html:option value="4">Normal</html:option>
				<html:option value="5">Large</html:option>
				<html:option value="7">Huge</html:option></html:select>
			</td>
		</tr>
		<tr>
			<td noWrap width="50%"><font size="2">
				<digi:trn key="sdm:fontColor">Font Color:</digi:trn></font>
				<html:select property="selectedFontColor">
				<html:option value="#444444">Font Color</html:option>
				<html:option value="darkred">Dark Red</html:option>
				<html:option value="red">Red</html:option>
				<html:option value="orange">Orange</html:option>
				<html:option value="brown">Brown</html:option>
				<html:option value="yellow">Yellow</html:option>
				<html:option value="green">Green</html:option>
				<html:option value="olive">Olive</html:option>
				<html:option value="cyan">Cyan</html:option>
				<html:option value="blue">Blue</html:option>
				<html:option value="darkblue">Dark Blue</html:option>
				<html:option value="indigo">Indigo</html:option>
				<html:option value="violet">Violet</html:option>
				<html:option value="black">Black</html:option></html:select>
			</td>
			<td noWrap width="50%" colspan="2"><font size="2">
				<digi:trn key="sdm:format">Format:</digi:trn></font><BR><font size="2"/>
				<digi:trn key="sdm:bold">Bold:</digi:trn></font>
				<html:checkbox name="sdmForm" property="bold"/><font size="2"/>
				<digi:trn key="sdm:italic">Italic:</digi:trn></font>
				<html:checkbox name="sdmForm" property="italic"/><font size="2"/>
				<digi:trn key="sdm:underline">Underline:</digi:trn></font>
				<html:checkbox name="sdmForm" property="underline"/>
			</td>
		</tr>
	</table>
	<table width="70%">
		<tr>
			<td noWrap><font size="2">
				<digi:trn key="sdm:fileTitle">Title of the File:</digi:trn></font><BR>
				<html:text name="sdmForm" property="contentTitle" size="50"/>
			</td>
		</tr>
		<tr>
			<td noWrap><font size="2">
				<digi:trn key="sdm:chooseFile">Choose the file:</digi:trn></font><BR>
				<html:file name="sdmForm" property="formFile" size="30"/>
			</td>
		</tr>
		<tr>
			<td>
				<html:submit value="Submit" onclick="return checkEmpty()"/>
			</td>
		</tr>
	</table></digi:form>
