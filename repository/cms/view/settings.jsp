
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<link rel="stylesheet" href="<digi:file src="module/cms/css/cms.css"/>">

<digi:form action="/saveCMSSettings.do">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td colspan="3" height="1">
		<%-- Table header --%>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td width="1"><digi:img src="module/cms/images/headerLeftEnd.gif" border="0"/></td>
				<td width="99%" class="mainHeader" align="center" valign="middle">
					<digi:trn key="cms:options">Options:</digi:trn>
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

   
   
   
   
   
	<table border="0" cellpadding="3" cellspacing="0" width="100%">
		<tr bgcolor="#F0F3F7">
			<td valign="top">
				<digi:trn key="cms:moderated">Moderated?</digi:trn>
			</td>
			<td align="left" valign="top">
				<table border="0" cellpadding="2" cellspacing="1">
					<tr>
						<td width="51">
							<html:radio name="cmsAdminForm" property="moderated" value="true"/>
							<digi:trn key="cms:yes">Yes</digi:trn>
						</td>
						<td width="44">
							<html:radio name="cmsAdminForm" property="moderated" value="false"/>
							<digi:trn key="cms:no">No</digi:trn>
						</td>
					</tr>
				</table>
			</td>
			<td valign="top">	&nbsp;
			</td>
		</tr>
		<tr bgcolor="#F0F3F7">
			<td valign="top">
				<digi:trn key="cms:private">Private?</digi:trn>
			</td>
			<td align="left" valign="top">
				<table border="0" cellpadding="2" cellspacing="1">
					<tr>
						<td width="51">
							<html:radio name="cmsAdminForm" property="privateMode" value="true"/>
							<digi:trn key="cms:yes">Yes</digi:trn>
						</td>
						<td width="44">
							<html:radio name="cmsAdminForm" property="privateMode" value="false"/>
							<digi:trn key="cms:no">No</digi:trn>
						</td>
					</tr>
				</table>
			</td>
			<td valign="top">	&nbsp;
			</td>
		</tr>
			<bean:define id="moduleName" name="cmsAdminForm" property="moduleName" type="java.lang.String" />
			<bean:define id="instanceName" name="cmsAdminForm" property="instanceName" type="java.lang.String" />
			<bean:define id="approve" name="cmsAdminForm" property="approve" type="java.lang.String" />
			<bean:define id="reject" name="cmsAdminForm" property="reject" type="java.lang.String" />
			<bean:define id="revoke" name="cmsAdminForm" property="revoke" type="java.lang.String" />
		<tr bgcolor="#F0F3F7">
			<td valign="top">
				<digi:trn key="cms:approveMessage">Approve Message:</digi:trn>
			</td>
			<td align="left" valign="top">
			<fieldset title="Alert Emails">
			<legend><digi:trn key="cms:alertsMailer">User Alerts E-mail</digi:trn></legend>
			 <table bgcolor="#ffffff">
			    <tr><td width="5%" align="right" valign="top"><b>From:</b></td>
			        <td><digi:msg default="noreply@digijava.org" linkAlwaysVisible="true">
			             alerts:cmsnewcontent:emailUser</digi:msg></td><tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Subject:</b></td>
			      <td>
			       <digi:msg default="Your {siteName} content" linkAlwaysVisible="true">
			            <%=moduleName%>:<%=instanceName%>:alert:<%=approve%>:subject
			       </digi:msg>
			      </td>
			   <tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Message:</b></td>
			      <td>
			       <digi:msg default="Your cms item was approved  {siteName} Title: {title} URL:{url} File Name: {fileName}" linkAlwaysVisible="true">
			           <%=moduleName%>:<%=instanceName%>:alert:<%=approve%>:body
		           </digi:msg>
			      </td>
			   <tr>
			 </table>
			</fieldset> 
			</td>
			<td align="left" valign="top"><b>
				<span class="tlt">
				<digi:trn key="cms:sendApproveMessage">Send approve message?</digi:trn></span></b>
				<table border="0" cellpadding="2" cellspacing="1">
					<tr>
						<td width="51">
							<html:radio name="cmsAdminForm" property="sendApproveMsg" value="true"/>
							<digi:trn key="cms:yes">Yes</digi:trn>
						</td>
						<td width="44">
							<html:radio name="cmsAdminForm" property="sendApproveMsg" value="false"/>
							<digi:trn key="cms:no">No</digi:trn>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr bgcolor="#F0F3F7">
			<td valign="top" class="tlt">
				<digi:trn key="cms:rejectMessage">Reject message:</digi:trn>
			</td>
			<td align="left" valign="top">
			<fieldset title="Alert Emails">
			<legend><digi:trn key="cms:alertsMailer">User Alerts E-mail</digi:trn></legend>
			 <table bgcolor="#ffffff">
			    <tr><td width="5%" align="right" valign="top"><b>From:</b></td>
			        <td><digi:msg default="noreply@digijava.org" linkAlwaysVisible="true">
			            alerts:cmsnewcontent:emailUser</digi:msg></td><tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Subject:</b></td>
			      <td>
			       <digi:msg default="Your {siteName} content" linkAlwaysVisible="true">
			           <%=moduleName%>:<%=instanceName%>:alert:<%=reject%>:subject</digi:msg>
			      </td>
			   <tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Message:</b></td>
			      <td>
			       <digi:msg default="Your cms item was rejected  {siteName}  Title: {title}  URL: {url}  File Name: {fileName}" linkAlwaysVisible="true">
			           <%=moduleName%>:<%=instanceName%>:alert:<%=reject%>:body
		           </digi:msg>
			      </td>
			   <tr>
			 </table>
			 </fieldset>
			</td>
			<td align="left" valign="top"><b>
				<span class="tlt">
				<digi:trn key="cms:sendRejectMessage">Send reject message?</digi:trn></span></b>
				<table border="0" cellpadding="2" cellspacing="1">
					<tr>
						<td width="51">
							<html:radio name="cmsAdminForm" property="sendRejectMsg" value="true"/>
							<digi:trn key="cms:yes">Yes</digi:trn>
						</td>
						<td width="44">
							<html:radio name="cmsAdminForm" property="sendRejectMsg" value="false"/>
							<digi:trn key="cms:no">No</digi:trn>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr bgcolor="#F0F3F7">
			<td valign="top" bgcolor="#F0F3F7" class="tlt">
				<digi:trn key="cms:revokeMessage">Revoke message:</digi:trn>
			</td>
			<td align="left" valign="top">
			<fieldset title="Alert Emails">
			<legend><digi:trn key="cms:alertsMailer">User Alerts E-mail</digi:trn></legend>
			 <table bgcolor="#ffffff">
			    <tr><td width="5%" align="right" valign="top"><b>From:</b></td>
			      <td><digi:msg default="noreply@digijava.org" linkAlwaysVisible="true">
			          alerts:cmsnewcontent:emailUser</digi:msg></td><tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Subject:</b></td>
			      <td>
			       <digi:msg default="Your {siteName} content" linkAlwaysVisible="true">
			           <%=moduleName%>:<%=instanceName%>:alert:<%=revoke%>:subject</digi:msg>
			      </td>
			   <tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Message:</b></td>
			      <td>
			       <digi:msg default="Your cms item was revoked {siteName} Title: {title} \n URL: {url} File Name: {fileName}" linkAlwaysVisible="true">
			           <%=moduleName%>:<%=instanceName%>:alert:<%=revoke%>:body
		           </digi:msg>
			      </td>
			   <tr>
			 </table>
			 </fieldset>
			</td>
			<td align="left" valign="top"><b>
				<span class="tlt">
				<digi:trn key="cms:sendRevokeMessage">Send revoke message?</digi:trn></span></b>
				<table border="0" cellpadding="2" cellspacing="1">
					<tr>
						<td width="51">
							<html:radio name="cmsAdminForm" property="sendRevokeMsg" value="true"/>
							<digi:trn key="cms:yes">Yes</digi:trn>
						</td>
						<td width="44">
							<html:radio name="cmsAdminForm" property="sendRevokeMsg" value="false"/>
							<digi:trn key="cms:no">No</digi:trn>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		
		
		
		<tr>
			<td colspan="3" valign="top" align="center">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td><digi:img src="module/cms/images/rowItemLeft.gif" height="26" border="0"/></td>
						<td class="rowItem" valign="middle" align="center" nowrap width="99%">
							<html:submit value="Save Options" styleClass="rowItemButton"/>
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

<table border="0" cellpadding="3" cellspacing="0">
	<tr>
		<td nowrap>
			<digi:link href="/showCreateContentItem.do"><digi:trn key="cms:viewMyItems">View my items</digi:trn></digi:link>
		</td>
		<td nowrap>
			<digi:link href="/showTaxonomyManager.do"><digi:trn key="cms:taxonomyManager">Taxonomy manager</digi:trn></digi:link>
		</td>
	</tr>
	<tr>
		<td nowrap>
			<digi:link href="/showCreateContentItem.do"><digi:trn key="cms:viewMyPendingItems">View my pending items</digi:trn></digi:link>
		</td>
		<td nowrap>
			<digi:link href="/viewContentItems.do"><digi:trn key="cms:administer">Administer</digi:trn></digi:link>
		</td>
	</tr>	
	<tr>
		<td nowrap>
			<digi:link href="/showCreateContentItem.do"><digi:trn key="cms:addUrlOrFile">Add URL or File</digi:trn></digi:link>
		</td>
		<td>
			<digi:link href="/index.do"><digi:trn key="cms:showIndex">Show index</digi:trn></digi:link>
		</td>
	</tr>		
</table>