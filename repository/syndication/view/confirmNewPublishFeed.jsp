<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ page import="org.digijava.module.syndication.form.PublishedFeedForm"%>

<digi:instance property="publishedFeedForm" />

<link rel="stylesheet" href="<digi:file src="module/syndication/css/cms.css"/>">

<script language="JavaScript">

  function fnOnCreate() {
      <digi:context name="createFeed" property="context/module/moduleinstance/createNewPublishFeed.do" />
      document.publishedFeedForm.action = "<%= createFeed%>";
	  document.publishedFeedForm.target = "_self";	  
      document.publishedFeedForm.submit();
  }
  
  function fnOnBack() {
      <digi:context name="backEdit" property="context/module/moduleinstance/showCreateNewPublishFeed.do" />
      document.publishedFeedForm.action = "<%= backEdit%>";
	  document.publishedFeedForm.target = "_self";	  
      document.publishedFeedForm.submit();
  }
  
  function fnOnEdit() {
      <digi:context name="editFeed" property="context/module/moduleinstance/editNewPublishFeed.do" />
      document.publishedFeedForm.action = "<%= editFeed%>";
	  document.publishedFeedForm.target = "_self";	  
      document.publishedFeedForm.submit();
  }
  
</script>
<digi:errors/>

<digi:form action="/createNewPublishFeed.do" >
<html:hidden name="publishedFeedForm" property="feedUrl"/>
<html:hidden name="publishedFeedForm" property="feedTitle"/>
<html:hidden name="publishedFeedForm" property="feedDescription"/>
<html:hidden name="publishedFeedForm" property="selectedLanguage"/>
<html:hidden name="publishedFeedForm" property="selectedCountry"/>
<html:hidden name="publishedFeedForm" property="itemId"/>


<!-- Preview -->

<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td colspan="3" height="1">
		<%-- Table header --%>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td width="1"><digi:img src="module/syndication/images/headerLeftEnd.gif" border="0"/></td>
				<td width="99%" class="mainHeader" align="center" valign="middle">
					
				   	<digi:trn key="syndication:feedDetails">Feed Details</digi:trn>
					
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
	
	
		<table border="0" width="100%">
			<tr>
				<td class="bold" colspan="5" align="left">
					<h1><digi:trn key="syndication:reviewFeedPublish">Please review the feed before publishing it to the Global Feeds Agregator</digi:trn>:</h>
				</td>
			</tr>
			<tr>
				<td valign="top" class="bold" width="15%">
					<digi:trn key="syndication:feedTitle">Feed Title</digi:trn>:
				</td>
				<td>
					<bean:write name="publishedFeedForm" property="feedTitle" />
				</td>
			</tr>
			<tr>
				<td valign="top" class="bold" width="15%">
					<digi:trn key="syndication:feedDescription">Feed Description</digi:trn>:
				</td>
				<td>
					<bean:write name="publishedFeedForm" property="feedDescription" />
				</td>
			</tr>
			<tr>
				<td valign="top" class="bold" width="15%">
					<digi:trn key="syndication:feedUrl">Feed URL</digi:trn>:
				</td>
				<td>
					<bean:write name="publishedFeedForm" property="feedUrl" />
				</td>
			</tr>
			<tr>
				<td valign="top" class="bold" width="15%">
					<digi:trn key="syndication:contentType">Content Type</digi:trn>:
				</td>
				<td>
					<bean:write name="publishedFeedForm" property="contentType" />
				</td>
			</tr>
			<tr>
				<td valign="top" class="bold" width="15%">
					<digi:trn key="syndication:language">Language</digi:trn>:
				</td>
				<td>
					<bean:write name="publishedFeedForm" property="selectedLanguageName" />
				</td>
			</tr>						
			<tr>
				<td valign="top" class="bold" width="15%">
					<digi:trn key="syndication:country">Country</digi:trn>:
				</td>
				<td>
					<bean:write name="publishedFeedForm" property="selectedCountryName" />
				</td>
			</tr>						
			<tr>
				<td valign="top" class="bold" width="15%">
					<digi:trn key="syndication:submissionDate">Submission Date</digi:trn>:
				</td>
				<td>
					<digi:date name="publishedFeedForm" property="dateAdded" format="dd MMM yyyy"/>
				</td>
			</tr>			
			<tr>
				<td colspan="2">
				 <logic:equal name="publishedFeedForm" property="processingMode" value="1">
					 <table width="100%" height="1" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td><digi:img src="module/syndication/images/rowItemLeft.gif" height="26" border="0"/></td>
								<td class="rowItem" width="99%" valign="middle" nowrap align="center">
								<input type="button" value="Confirm" onclick="javascript:fnOnCreate()" class="rowItemButton">
							    <input type="button" value="Edit" onclick="javascript:fnOnBack()" class="rowItemButton">										
								</td>
								<td><digi:img src="module/syndication/images/rowItemRight.gif" height="26" border="0"/></td>
							</tr>
						</table>
				 </logic:equal>   
				 <logic:equal name="publishedFeedForm" property="processingMode" value="2">
					 <table width="100%" height="1" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td><digi:img src="module/syndication/images/rowItemLeft.gif" height="26" border="0"/></td>
								<td class="rowItem" width="99%" valign="middle" nowrap align="center">
								<input type="button" value="Update" onclick="javascript:fnOnEdit()" class="rowItemButton">
							    <input type="button" value="Edit" onclick="javascript:fnOnBack()" class="rowItemButton">										
								</td>
								<td><digi:img src="module/syndication/images/rowItemRight.gif" height="26" border="0"/></td>
							</tr>
						</table>				 
				 </logic:equal>   
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