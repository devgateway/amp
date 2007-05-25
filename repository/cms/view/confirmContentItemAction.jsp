<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ page import="org.digijava.module.cms.form.CMSContentItemForm" %>
<%@ page import="org.digijava.module.common.dbentity.ItemStatus" %>
<digi:instance property="cmsContentItemForm" />
<link rel="stylesheet" href="<digi:file src="module/cms/css/cms.css"/>">

<script language="JavaScript">
	function confirmChanges(){
	  <digi:context name="confirmChanges" property="context/module/moduleinstance/changeItemStatus.do" />
      document.cmsContentItemForm.action = "<%= confirmChanges %>";
      document.cmsContentItemForm.submit();	
	}
	function cancel(){
	  <digi:context name="cancel" property="context/module/moduleinstance/viewContentItems.do" />
      document.cmsContentItemForm.action = "<%= cancel %>";
      document.cmsContentItemForm.submit();		
	}	
</script>

<digi:errors/>
<digi:form action="/showChangeItemStatus.do">
<html:hidden name="cmsContentItemForm" property="itemStatus"/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td colspan="3" height="1">
		<%-- Table header --%>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td width="1"><digi:img src="module/cms/images/headerLeftEnd.gif" border="0"/></td>
				<td width="99%" class="mainHeader" align="center" valign="middle">
					<digi:trn key="cms:confirm">Confirm</digi:trn>
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
	<td width="100%" align="center" bgcolor="#F2F4FC">
		<%-- Inner --%>
			<br>
				<digi:trn key="cms:theFollowingItemsWillBe">The following item(s) will be</digi:trn>
				<logic:equal name="cmsContentItemForm" property="itemStatus" value="<%= ItemStatus.PUBLISHED %>">
					&nbsp;<digi:trn key="cms:published">published</digi:trn>
				</logic:equal>
				<logic:equal name="cmsContentItemForm" property="itemStatus" value="<%= ItemStatus.REJECTED %>">
					<digi:trn key="cms:rejected">rejected</digi:trn>
				</logic:equal>
				<logic:equal name="cmsContentItemForm" property="itemStatus" value="<%= ItemStatus.PENDING %>">
					<digi:trn key="cms:pended">pended</digi:trn>
				</logic:equal>								
			<br>
		
				<table border="0" width="100%" cellpadding="0" cellspacing="0">
				 <tr>
				    <th class="itemHeader" width="40%" align="left" nowrap>
					   <digi:trn key="cms:title">Title</digi:trn>
				    </td>
				    <th class="itemHeader" width="40%" align="left" nowrap>
					   <digi:trn key="cms:author">Author</digi:trn>
				    </td>
					 <th class="itemHeader" width="10%" align="left" nowrap>
					   <digi:trn key="cms:releaseDate">Release date</digi:trn>
				    </td>					
				 </tr>
				<tr>
					<td class="itemRowSeparator" colspan="4"><digi:img src="module/cms/images/tree/spacer.gif" height="2" border="0"/></td>
				</tr>
					 <logic:present name="cmsContentItemForm" property="itemsList">
					  <logic:iterate indexId="index" id="itemsList" name="cmsContentItemForm" property="itemsList" type="org.digijava.module.cms.dbentity.CMSContentItem">
						<bean:define id="oddOrEven" value="<%= String.valueOf(index.intValue()%2) %>"/>				  
						<logic:equal name="oddOrEven" value="0">
							<tr class="itemRowOdd">
						</logic:equal>
						<logic:notEqual name="oddOrEven" value="0">
							<tr class="itemRowEven">
						</logic:notEqual>
					  <td align="left" style="border-bottom: 1px solid #C8CEEA;">
						<html:hidden name="cmsContentItemForm" property="<%= "propertyArray[" + String.valueOf(index) + "]" %>" value="<%= String.valueOf(itemsList.getId()) %>"/>					  
					     &nbsp;<bean:write name="itemsList" property="title" />
					  </td>
					  <td align="left" style="border-bottom: 1px solid #C8CEEA;" nowrap>
					  <logic:present name="itemsList" property="authorUser">
					     <bean:write name="itemsList" property="authorUser.firstNames"/>
						 &nbsp;
						 <bean:write name="itemsList" property="authorUser.lastName"/>
					  </logic:present>	 
					  <logic:notPresent name="itemsList" property="authorUser">
					     N/A
					  </logic:notPresent>
					  </td>
					  <td style="border-bottom: 1px solid #C8CEEA;">
						<digi:date name="itemsList" property="submissionDate" format="dd MMM yyyy"/>					  
					  </td>
				  </tr>
				  </logic:iterate>
				 </logic:present>
				</table>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr valign="top">
				<td nowrap width="30%">
					<logic:equal name="cmsContentItemForm" property="itemStatus" value="<%= ItemStatus.PUBLISHED %>">
						&nbsp;<digi:trn key="cms:rePublish">Re-Publish</digi:trn>
					</logic:equal>
					<logic:equal name="cmsContentItemForm" property="itemStatus" value="<%= ItemStatus.REJECTED %>">
						<digi:trn key="cms:reject">Reject</digi:trn>
					</logic:equal>
					<logic:equal name="cmsContentItemForm" property="itemStatus" value="<%= ItemStatus.PENDING %>">
						<digi:trn key="cms:pending">Pending</digi:trn>
					</logic:equal>								
					<digi:trn key="cms:message">message:</digi:trn>
				</td>
				<td colspan="2" nowrap width="384">
				<bean:define id="moduleName" name="cmsContentItemForm" property="moduleName" type="java.lang.String" />
				<bean:define id="instanceName" name="cmsContentItemForm" property="instanceName" type="java.lang.String" />
				<bean:define id="itemStatus" name="cmsContentItemForm" property="itemStatus" type="java.lang.String" />
				<fieldset title="Alert Emails">
				<legend><digi:trn key="cms:alertsMailer">User Alerts E-mail</digi:trn></legend>
				 <table bgcolor="#ffffff">
				    <tr><td width="5%" align="right" valign="top"><b>From:</b></td>
				       <td><digi:msg default="noreply@digijava.org" linkAlwaysVisible="true">
				           alerts:cmsnewcontent:emailUser
				          </digi:msg></td><tr>
				    <tr>
			    	  <td width="5%" align="right" valign="top"><b>Subject:</b></td>
				      <td>
				       <digi:msg default="Your {siteName} content" linkAlwaysVisible="true">
				           <%=moduleName%>:<%=instanceName%>:alert:<%=itemStatus%>:subject
			           </digi:msg>
			    	  </td>
				   <tr>
				    <tr>
					      <td width="5%" align="right" valign="top"><b>Message:</b></td>
						      <td>
						       <digi:msg default="{siteName} Title: {title}  URL: {url} File Name: {fileName}" linkAlwaysVisible="true">
			           				<%=moduleName%>:<%=instanceName%>:alert:<%=itemStatus%>:body
					           </digi:msg>
						      </td>
						   <tr>
						 </table>
					 </fieldset>
					</td>
				</tr>
				<tr valign="top">
					<td nowrap>
						<digi:trn key="cms:sendMessage">Send message?</digi:trn></span>
					</td>
					<td nowrap>
					<table border="0" cellpadding="2" cellspacing="1" width="112">
						<tr>
							<td>
								<html:radio name="cmsContentItemForm" property="sendMessage" value="true"><strong style="font-weight: 400">
								<digi:trn key="cms:yes">Yes</digi:trn></strong></html:radio>
							</td>
							<td width="51">
								<html:radio name="cmsContentItemForm" property="sendMessage" value="false"><strong style="font-weight: 400">
								<digi:trn key="cms:no">No</digi:trn></strong></html:radio>
						</tr>
					</table>
					</td>
				</tr>
				</table>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="rowItem" align="right" valign="middle" nowrap width="50%">
						 	<input type="Button" value="Confirm" class="rowItemButton" onclick="confirmChanges()">
						</td>
						<td class="rowItem" valign="middle" nowrap>
						 	<digi:img src="module/cms/images/tree/spacer.gif" height="26" width="2" border="0"/>
						</td>
						<td class="rowItem" align="left" valign="middle" nowrap width="50%">
						 	<input type="Button" value="Cancel" class="rowItemButton" onclick="cancel()">
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