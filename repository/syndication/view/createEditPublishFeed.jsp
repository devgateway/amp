<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ page import="org.digijava.module.syndication.form.PublishedFeedForm"%>

<digi:instance property="publishedFeedForm" />

<link rel="stylesheet" href="<digi:file src="module/syndication/css/cms.css"/>">

<script language="JavaScript">
  function fnOnPreview() {
      <digi:context name="previewItem" property="context/module/moduleinstance/confirmNewPublishFeed.do" />
      document.publishedFeedForm.action = "<%= previewItem%>";
	  document.publishedFeedForm.target = "_self";
      document.publishedFeedForm.submit();
  }
</script>

<digi:errors/>

<digi:form action="/confirmNewPublishFeed.do">
<html:hidden name="publishedFeedForm" property="processingMode"/>
<html:hidden name="publishedFeedForm" property="itemId"/>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td colspan="3" height="1">
		<%-- Table header --%>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td width="1"><digi:img src="module/syndication/images/headerLeftEnd.gif" border="0"/></td>
				<td width="99%" class="mainHeader" align="center" valign="middle">
					<logic:equal name="publishedFeedForm" property="processingMode" value="1">
					   	<digi:trn key="syndication:createNewFeedToPublish">Create New Feed to Publish</digi:trn>
					</logic:equal>   
					<logic:equal name="publishedFeedForm" property="processingMode" value="2">
					   	<digi:trn key="syndication:editFeedToPublish">Edit Publish Feed</digi:trn>
					</logic:equal>
				</td>
				<td width="1"><digi:img src="module/syndication/images/headerRightEnd.gif" border="0"/></td>				
			</tr>
		</table>
		<%-- end of Table header --%>
	</td>
</tr>
<tr>
	<td class="leftBorder" width="1">
		<digi:img src="module/syndication/images/tree/spacer.gif" width="9" border="0"/>
	</td>
	<td width="100%" align="center" bgcolor="#F2F4FC">
		<%-- Inner --%>
		
		

		<table width="100%" border="0" cellspacing="0" cellpadding="3">
			<tr>
				<td nowrap>
					<digi:trn key="syndication:feedTitle">Feed Title</digi:trn>
				</td>
				<td>
					<html:text name="publishedFeedForm" property="feedTitle" size="70" styleClass="rowItemField"/>
				</td>		
			</tr>
			<tr>
				<td nowrap align="top">
					<digi:trn key="syndication:feedDescription">Feed Description</digi:trn>
				</td>
				<td>
					<html:textarea name="publishedFeedForm" property="feedDescription" cols="30" rows="5" />
				</td>
			</tr>		
			<tr>
				<td nowrap align="top">
					<digi:trn key="syndication:selectLanguage">Select Language</digi:trn>
				</td>
				<td>
					<html:select property="selectedLanguage">
					<bean:define id="languages" name="publishedFeedForm" property="language" type="java.util.List"/>
					<html:options collection="languages" property="code" labelProperty="name"/></html:select>
				</td>
			</tr>				
			<tr>
				<td nowrap align="top">
					<digi:trn key="syndication:selectCountry">Select Country</digi:trn>
				</td>
				<td>
					<html:select property="selectedCountry">
					<bean:define id="countries" name="publishedFeedForm" property="country" type="java.util.List"/>
					<html:options collection="countries" property="iso" labelProperty="name"/></html:select>
				</td>
			</tr>				
			<tr>
				<td colspan="2" align="center">
				
				
					<table width="100%" height="1" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td><digi:img src="module/syndication/images/rowItemLeft.gif" height="26" border="0"/></td>
							<td class="rowItem" width="99%" valign="middle" nowrap align="center">
							<input type="Button" value="Preview" onclick="javascript:fnOnPreview()" class="rowItemButton">					</td>
							<td><digi:img src="module/syndication/images/rowItemRight.gif" height="26" border="0"/></td>
						</tr>
					</table>
				
				</td>		
			</tr>	
		</table>

<%-- Inner --%>
	</td>
	<td class="rightBorder" width="1">
		<digi:img src="module/syndication/images/tree/spacer.gif" width="9" border="0"/>
	</td>	
</tr>
<tr>
	<td height="1"><digi:img src="module/syndication/images/leftBottom.gif" width="9" height="9" border="0"/></td>
	<td width="100%" class="bottomBorder" height="1"><digi:img src="module/syndication/images/tree/spacer.gif" height="9" border="0"/></td>
	<td height="1"><digi:img src="module/syndication/images/rightBottom.gif" width="9" height="9" border="0"/></td>	
</tr>
</table>
</digi:form>