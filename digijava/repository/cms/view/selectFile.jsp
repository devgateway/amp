<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:instance property="cmsContentItemForm" />

<link rel="stylesheet" href="<digi:file src="module/cms/css/cms.css"/>">

<digi:errors/>

<script language="JavaScript">
	function selectFile() {
		 <digi:context name="selectFile" property="context/module/moduleinstance/selectFile.do" />
	      document.cmsContentItemForm.action = "<%= selectFile %>";
		  document.cmsContentItemForm.target = window.opener.name;
	      document.cmsContentItemForm.submit();
		  window.close();
	}
</script>

<digi:form action="/selectFile.do" enctype="multipart/form-data">
<html:hidden name="cmsContentItemForm" property="processingMode"/>
<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
<tr>
	<td colspan="3" height="1">
		<%-- Table header --%>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td width="1"><digi:img src="module/cms/images/headerLeftEnd.gif" border="0"/></td>
				<td width="99%" class="mainHeader" align="center" valign="middle">
					<digi:trn key="cms:selectFile">Select file</digi:trn>
				</td>
				<td width="1"><digi:img src="module/cms/images/headerRightEnd.gif" border="0"/></td>				
			</tr>
		</table>
		<%-- end of Table header --%>
	</td>
</tr>
<tr>
	<td class="leftBorder" width="1">
		<digi:img src="module/cms/images/tree/spacer.gif" width="9" border="0"/>
	</td>
	<td width="100%" height="100%" align="center" bgcolor="#F2F4FC">
		<%-- Inner --%>
		
		<table border="0" cellpadding="5" cellspacing="0" width="100%" height="100%">
			<tr>
				<td height="99%" align="left" valign="middle">
					<html:file name="cmsContentItemForm" property="formFile" size="30"/>					
				</td>
			</tr>
			<tr>
				<td>
					<table width="100%" height="1" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td><digi:img src="module/cms/images/rowItemLeft.gif" height="26" border="0"/></td>
							<td class="rowItem" width="50%" valign="middle" nowrap align="right">
								<input type="button" value="Select" onclick="selectFile()" class="rowItemButton">
							</td>
							<td class="rowItem"><digi:img src="module/cms/images/tree/spacer.gif" width="5" border="0"/></td>
							<td class="rowItem" width="50%" valign="middle" nowrap align="left">
								<input type="button" value="Close" onclick="window.close()" class="rowItemButton">
							</td>
							<td><digi:img src="module/cms/images/rowItemRight.gif" height="26" border="0"/></td>
						</tr>
					</table>
				</td>
			</tr>
		
		</table>
		<%-- Inner --%>		
	</td>
	<td class="rightBorder" width="1">
		<digi:img src="module/cms/images/tree/spacer.gif" width="9" border="0"/>
	</td>	
</tr>
<tr>
	<td height="1"><digi:img src="module/cms/images/leftBottom.gif" width="9" height="9" border="0"/></td>
	<td width="100%" class="bottomBorder" height="1"><digi:img src="module/cms/images/tree/spacer.gif" height="9" border="0"/></td>
	<td height="1"><digi:img src="module/cms/images/rightBottom.gif" width="9" height="9" border="0"/></td>	
</tr>
</table>
</digi:form>