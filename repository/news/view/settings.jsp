
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>



<digi:form action="/updateNewsItemSettings.do" method="post"><h6>
	<digi:trn key="news:moduleAdminSettings">Module Admin Settings</digi:trn></h6>
	<table width="100%" >
  	 <digi:secure authenticated="true">
		<tr>
	  	  <td width="50%" align>
			<digi:link href="/viewAllNews.do?status=mall">
			<digi:trn key="news:viewMyNews">View my news</digi:trn></digi:link>
		   </td>
  		   <td width="50%">
			 <digi:link href="/showNewsItems.do?status=pe">
			 <digi:trn key="news:administer">Administer</digi:trn></digi:link>
		   </td>
		</tr>
		<tr>
			<td width="50%">
		  	 <digi:link href="/viewAllNews.do?status=mpe">
 		         <digi:trn key="news:viewMyPeNews">View my pending news</digi:trn></digi:link>
			</td>
			<td width="50%">
			  <logic:equal name="newsAdminForm" property="setting.syndication" value="true" >
				 <digi:link href="/collectorFeedList.do">
				 <digi:trn key="news:syndicationCollector">Syndication Collector</digi:trn></digi:link>
			  </logic:equal>
			  &nbsp;
			</td>
		</tr>
		<tr>
			<td width="50%">
			 <digi:link href="/showCreateNewsItem.do">
			 <digi:trn key="news:addNewItem">Add New Item</digi:trn></digi:link>
			</td>
			<td width="50%">
			  <logic:equal name="newsAdminForm" property="setting.syndication" value="true" >
				 <digi:link href="/publishedFeedList.do">
				 <digi:trn key="news:syndicationPublisher">Syndication Publisher</digi:trn></digi:link>
			  </logic:equal>
			  &nbsp;
			</td>
		</tr>
</digi:secure>
   </table>
   <BR>
	<table border="0" cellpadding="5" cellspacing="1">
		<tr bgcolor="#F0F3F7">
			<td valign="top" class="tlt"><strong>
				<digi:trn key="news:opetions">Options:</digi:trn></strong>
			</td>
			<td align="left" valign="top">	&nbsp;
			</td>
			<td valign="top">	&nbsp;
			</td>
		</tr>
		<tr bgcolor="#F0F3F7">
			<td valign="top" class="tlt">
				<digi:trn key="news:moderated">Moderated?</digi:trn>
			</td>
			<td align="left" valign="top">
				<table border="0" cellpadding="2" cellspacing="1">
					<tr>
						<td width="51">
							<html:radio name="newsAdminForm" property="setting.moderated" value="true"/>
							<digi:trn key="news:yes">Yes</digi:trn>
						</td>
						<td width="44">
							<html:radio name="newsAdminForm" property="setting.moderated" value="false"/>
							<digi:trn key="news:no">No</digi:trn>
						</td>
					</tr>
				</table>
			</td>
			<td valign="top">	&nbsp;
			</td>
		</tr>
		<tr bgcolor="#F0F3F7">
			<td valign="top" class="tlt">
				<digi:trn key="news:private">Private?</digi:trn>
			</td>
			<td align="left" valign="top">
				<table border="0" cellpadding="2" cellspacing="1">
					<tr>
						<td width="51">
							<html:radio name="newsAdminForm" property="setting.privateItem" value="true"/>
							<digi:trn key="news:yes">Yes</digi:trn>
						</td>
						<td width="44">
							<html:radio name="newsAdminForm" property="setting.privateItem" value="false"/>
							<digi:trn key="news:no">No</digi:trn>
						</td>
					</tr>
				</table>
			</td>
			<td valign="top">	&nbsp;
			</td>
		</tr>
		<tr bgcolor="#F0F3F7">
			<td valign="top" class="tlt">
				<digi:trn key="news:syndication">Syndication?</digi:trn>
			</td>
			<td align="left" valign="top">
				<table border="0" cellpadding="2" cellspacing="1">
					<tr>
						<td width="51">
							<html:radio name="newsAdminForm" property="setting.syndication" value="true"/>
							<digi:trn key="news:yes">Yes</digi:trn>
						</td>
						<td width="44">
							<html:radio name="newsAdminForm" property="setting.syndication" value="false"/>
							<digi:trn key="news:no">No</digi:trn>
						</td>
					</tr>
				</table>
			</td>
			<td valign="top">	&nbsp;
			</td>
		</tr>
			<bean:define id="moduleName" name="newsAdminForm" property="moduleName" type="java.lang.String" />
			<bean:define id="instanceName" name="newsAdminForm" property="instanceName" type="java.lang.String" />
			<bean:define id="approve" name="newsAdminForm" property="approve" type="java.lang.String" />
			<bean:define id="reject" name="newsAdminForm" property="reject" type="java.lang.String" />
			<bean:define id="revoke" name="newsAdminForm" property="revoke" type="java.lang.String" />
			<bean:define id="archive" name="newsAdminForm" property="archive" type="java.lang.String" />
		<tr bgcolor="#F0F3F7">
			<td valign="top" class="tlt">
				<digi:trn key="news:approveMessage">Approve Message:</digi:trn>
			</td>
			<td align="left" valign="top">
			<fieldset title="Alert Emails">
			<legend><digi:trn key="news:alertsMailer">User Alerts E-mail</digi:trn></legend>
			 <table bgcolor="#ffffff">
			    <tr><td width="5%" align="right" valign="top"><b>From:</b></td>
			        <td><digi:msg default="noreply@digijava.org" linkAlwaysVisible="true">alerts:newcontent:emailUser</digi:msg></td><tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Subject:</b></td>
			      <td>
			       <digi:msg default="Your {siteName} content" linkAlwaysVisible="true"><%=moduleName%>:<%=instanceName%>:alert:<%=approve%>:subject</digi:msg>
			      </td>
			   <tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Message:</b></td>
			      <td>
			       <digi:msg default="Your news item was approved  {siteName} Title: {title} URL: {sourceUrl}" linkAlwaysVisible="true"><%=moduleName%>:<%=instanceName%>:alert:<%=approve%>:body</digi:msg>
			      </td>
			   <tr>
			 </table>
			</fieldset> 
			</td>
			<td align="left" valign="top"><b>
				<span class="tlt">
				<digi:trn key="news:sendApproveMessage">Send approve message?</digi:trn></span></b>
				<table border="0" cellpadding="2" cellspacing="1">
					<tr>
						<td width="51">
							<html:radio name="newsAdminForm" property="setting.sendApproveMessage" value="true"/>
							<digi:trn key="news:yes">Yes</digi:trn>
						</td>
						<td width="44">
							<html:radio name="newsAdminForm" property="setting.sendApproveMessage" value="false"/>
							<digi:trn key="news:no">No</digi:trn>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr bgcolor="#F0F3F7">
			<td valign="top" class="tlt">
				<digi:trn key="news:rejectMessage">Reject message:</digi:trn>
			</td>
			<td align="left" valign="top">
			<fieldset title="Alert Emails">
			<legend><digi:trn key="news:alertsMailer">User Alerts E-mail</digi:trn></legend>
			 <table bgcolor="#ffffff">
			    <tr><td width="5%" align="right" valign="top"><b>From:</b></td>
			        <td><digi:msg default="noreply@digijava.org" linkAlwaysVisible="true">
			            alerts:newcontent:emailUser</digi:msg></td><tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Subject:</b></td>
			      <td>
			       <digi:msg default="Your {siteName} content" linkAlwaysVisible="true"><%=moduleName%>:<%=instanceName%>:alert:<%=reject%>:subject</digi:msg>
			      </td>
			   <tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Message:</b></td>
			      <td>
			       <digi:msg default="Your news item was rejected  {siteName}  Title: {title}  URL: {sourceUrl}" linkAlwaysVisible="true"><%=moduleName%>:<%=instanceName%>:alert:<%=reject%>:body</digi:msg>
			      </td>
			   <tr>
			 </table>
			 </fieldset>
			</td>
			<td align="left" valign="top"><b>
				<span class="tlt">
				<digi:trn key="news:sendRejectMessage">Send reject message?</digi:trn></span></b>
				<table border="0" cellpadding="2" cellspacing="1">
					<tr>
						<td width="51">
							<html:radio name="newsAdminForm" property="setting.sendRejectMessage" value="true"/>
							<digi:trn key="news:yes">Yes</digi:trn>
						</td>
						<td width="44">
							<html:radio name="newsAdminForm" property="setting.sendRejectMessage" value="false"/>
							<digi:trn key="news:no">No</digi:trn>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr bgcolor="#F0F3F7">
			<td valign="top" bgcolor="#F0F3F7" class="tlt">
				<digi:trn key="news:revokeMessage">Revoke message:</digi:trn>
			</td>
			<td align="left" valign="top">
			<fieldset title="Alert Emails">
			<legend><digi:trn key="news:alertsMailer">User Alerts E-mail</digi:trn></legend>
			 <table bgcolor="#ffffff">
			    <tr><td width="5%" align="right" valign="top"><b>From:</b></td>
			      <td><digi:msg default="noreply@digijava.org" linkAlwaysVisible="true">
			          alerts:newcontent:emailUser</digi:msg></td><tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Subject:</b></td>
			      <td>
			       <digi:msg default="Your {siteName} content" linkAlwaysVisible="true"><%=moduleName%>:<%=instanceName%>:alert:<%=revoke%>:subject</digi:msg>
			      </td>
			   <tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Message:</b></td>
			      <td>
			       <digi:msg default="Your news item was revoked {siteName} Title: {title} \n URL: {sourceUrl}" linkAlwaysVisible="true"><%=moduleName%>:<%=instanceName%>:alert:<%=revoke%>:body</digi:msg>
			      </td>
			   <tr>
			 </table>
			 </fieldset>
			</td>
			<td align="left" valign="top"><b>
				<span class="tlt">
				<digi:trn key="news:sendRevokeMessage">Send revoke message?</digi:trn></span></b>
				<table border="0" cellpadding="2" cellspacing="1">
					<tr>
						<td width="51">
							<html:radio name="newsAdminForm" property="setting.sendRevokeMessage" value="true"/>
							<digi:trn key="news:yes">Yes</digi:trn>
						</td>
						<td width="44">
							<html:radio name="newsAdminForm" property="setting.sendRevokeMessage" value="false"/>
							<digi:trn key="news:no">No</digi:trn>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr bgcolor="#F0F3F7">
			<td valign="top" class="tlt">
				<digi:trn key="news:archiveMessage">Archive message:</digi:trn>
			</td>
			<td align="left" valign="top">
			<fieldset title="Alert Emails">
			<legend><digi:trn key="news:alertsMailer">User Alerts E-mail</digi:trn></legend>
			 <table bgcolor="#ffffff">
			    <tr><td width="5%" align="right" valign="top"><b>From:</b></td>
			      <td><digi:msg default="noreply@digijava.org" linkAlwaysVisible="true">alerts:newcontent:emailUser</digi:msg></td><tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Subject:</b></td>
			      <td>
			       <digi:msg default="Your {siteName} content" linkAlwaysVisible="true"><%=moduleName%>:<%=instanceName%>:alert:<%=archive%>:subject</digi:msg>
			      </td>
			   <tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Message:</b></td>
			      <td>
			       <digi:msg default="Your news item was archived {siteName} Title: {title} \n URL: {sourceUrl}" linkAlwaysVisible="true"><%=moduleName%>:<%=instanceName%>:alert:<%=archive%>:body</digi:msg>
			      </td>
			   <tr>
			 </table>
			</fieldset> 
			</td>
			<td align="left" valign="top"><b>
				<span class="tlt">
				<digi:trn key="news:sendArchiveMessage">Send archive message?</digi:trn></span></b>
				<table border="0" cellpadding="2" cellspacing="1">
					<tr>
						<td width="51">
							<html:radio name="newsAdminForm" property="setting.sendArchiveMessage" value="true"/>
							<digi:trn key="news:yes">Yes</digi:trn>
						</td>
						<td width="44">
							<html:radio name="newsAdminForm" property="setting.sendArchiveMessage" value="false"/>
							<digi:trn key="news:no">No</digi:trn>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr bgcolor="#F0F3F7">
			<td colspan="3">
				<table height="63">
					<tr bgcolor="#F0F3F7">
						<td align="left" valign="top" height="19"><b>
							<span class="tlt">
							<digi:trn key="news:numOfNewItemsInTeaser">Number of new items in teaser:</digi:trn></span></b>
						</td>
						<td height="19">
							<html:select name="newsAdminForm" property="selectedNumOfItemsInTeaser">
							<bean:define id="numberItems" name="newsAdminForm" property="numberOfTeasers" type="java.util.Collection"/>
							<html:options collection="numberItems" property="value" labelProperty="value"/></html:select>
						</td>
					</tr>
					<tr bgcolor="#F0F3F7">
						<td align="left" valign="top" height="19"><b>
							<span class="tlt">
							<digi:trn key="news:numOfCharsInItemTitle">Number of characters in item title:</digi:trn></span></b>
						</td>
						<td height="19">
							<html:text name="newsAdminForm" property="numOfCharsInTitle" size="7"/>
						</td>
					</tr>
					<tr bgcolor="#F0F3F7">
						<td align="left" valign="top" height="13"><b>
							<span class="tlt">
							<digi:trn key="news:numOfItemsPerPage">Number of items per page:</digi:trn></span></b>
						</td>
						<td height="13">
							<html:text name="newsAdminForm" property="numOfItemsPerPage" size="7"/>
						</td>
					</tr>
					<tr><td colspan=2>&nbsp;</td></tr>
					<tr bgcolor="#F0F3F7">
						<td align="left" valign="top" height="13"><b>
							<span class="tlt">
							<digi:trn key="news:shortVerDelimiter">Short version delimiter:</digi:trn></span></b>
							<br>
							<small><digi:trn key="news:leaveEmpty">leave the box blank if short versioning should not be possible</digi:trn></small>
						</td>
						<td height="13">
							<html:text name="newsAdminForm" property="shortVersionDelimiter" size="7"/>
						</td>
					</tr>
					
				</table>
			</td>
		</tr>
		<tr bgcolor="#F0F3F7">
			<td colspan="3" valign="top" align="center">
				<html:submit value="Save Options"/>
			</td>
		</tr>
	</table>
</digi:form>