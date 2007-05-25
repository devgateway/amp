
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:errors/>
<digi:form action="/updateConfirmNewsItems.do" method="post"><link href="images/style.css" rel="stylesheet" type="text/css">
	<p>
	<digi:trn key="news:theFollowingItemsWillBe">The following item(s) will be</digi:trn>&quot;<strong>
	<c:out value="${newsItemForm.statusTitle}"/>ed</strong>&quot;</p>
	<c:if test="${!empty newsItemForm.newsList}">
	<table width="278" border="0" cellpadding="5" cellspacing="1">
		<tr bgcolor="#F0F3F7" styleClass="tlt">
			<td width="140"><strong>
				<digi:trn key="news:title1">Title</digi:trn></strong>
			</td>
			<td width="219"><strong>
				<digi:trn key="news:author1">Author</digi:trn></strong>
			</td>
			<td width="154" nowrap><strong>
				<digi:trn key="news:releaseDate1">Release Date</digi:trn></strong>
			</td>
		</tr>
		<c:forEach var="newsList" items="${newsItemForm.newsList}" >
		<c:if test="${newsList.selected}" >
		<tr valign="top" bgcolor="#F0F3F7">
			<td nowrap width="140">
				<c:out value="${newsList.title}"/>
				<html:hidden property="returnUrl"/>&nbsp;
				<digi:link href="/showEditNewsItem.do" paramName="newsList" paramId="activeNewsItem" paramProperty="id" styleClass="tlt">[
				<digi:trn key="news:edit1">Edit</digi:trn>]</digi:link>
			</td>
			<td nowrap width="219">
				<c:out value="${newsList.authorFirstNames}"/>
				<c:out value="${newsList.authorLastName}"/>
			</td>
			<td nowrap width="154">
				<c:out value="${newsList.releaseDate}"/>
			</td>
		</tr>
		<tr>
			<td colspan="3" width="535">
				<html:hidden name="newsItem" property="selected" value="true" indexed="true"/>
			</td>
		</tr></c:if></c:forEach>
		<tr valign="top" bgcolor="#F0F3F7">
			<td nowrap styleClass="tlt" width="140"><b>
				<span class="tlt">
				<c:out value="${newsList.statusTitle}"/>
				<digi:trn key="news:message">message:</digi:trn></span></b>
			</td>
			<td colspan="2" nowrap width="384">
			<bean:define id="moduleName" name="newsItemForm" property="moduleName" type="java.lang.String" />
			<bean:define id="instanceName" name="newsItemForm" property="instanceName" type="java.lang.String" />
			<bean:define id="selectedStatus" name="newsItemForm" property="selectedStatus" type="java.lang.String" />
			<fieldset title="Alert Emails">
			<legend><digi:trn key="news:alertsMailer">User Alerts E-mail</digi:trn></legend>
			 <table bgcolor="#ffffff">
			    <tr><td width="5%" align="right" valign="top"><b>From:</b></td>
			       <td><digi:msg default="noreply@digijava.org" linkAlwaysVisible="true">
			           alerts:newcontent:emailUser
			          </digi:msg></td><tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Subject:</b></td>
			      <td>
			       <digi:msg default="Your {siteName} content" linkAlwaysVisible="true">
			           <%=moduleName%>:<%=instanceName%>:alert:<%=selectedStatus%>:subject
		           </digi:msg>
			      </td>
			   <tr>
			    <tr>
			      <td width="5%" align="right" valign="top"><b>Message:</b></td>
			      <td>
			       <digi:msg default="{siteName} Title: {title}  URL: {sourceUrl}" linkAlwaysVisible="true">
			           <%=moduleName%>:<%=instanceName%>:alert:<%=selectedStatus%>:body
		           </digi:msg>
			      </td>
			   <tr>
			 </table>
			 </fieldset>
			</td>
		</tr>
		<tr valign="top" bgcolor="#F0F3F7">
			<td nowrap styleClass="tlt" width="140"><b>
				<span class="tlt">
				<digi:trn key="news:sendMessage">Send message?</digi:trn></span></b>
			</td>
			<td colspan="2" nowrap width="384">
				<table border="0" cellpadding="2" cellspacing="1" width="112">
					<tr>
						<td width="51">
							<html:radio name="newsItemForm" property="sendMessage" value="true"><strong style="font-weight: 400">
							<digi:trn key="news:yes">Yes</digi:trn></strong></html:radio>
						</td>
						<td width="51">
							<html:radio name="newsItemForm" property="sendMessage" value="false"><strong style="font-weight: 400">
							<digi:trn key="news:no">No</digi:trn></strong></html:radio>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td width="140">
				<html:submit value="Confirm"  onclick="this.disabled = true; form.submit()" />
			</td>
		</tr>
	</table></c:if>
</digi:form>