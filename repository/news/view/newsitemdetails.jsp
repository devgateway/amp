
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<script>function fnOnReject() {
      <digi:context name="confirmRejectNewsItem" property="context/module/moduleinstance/confirmNewsItems.do" />
      document.newsItemForm.action = "<%= confirmRejectNewsItem %>";
      document.newsItemForm.submit();
  }
 function fnOnAdmin() {
      <digi:context name="administerNewsItems" property="context/module/moduleinstance/showNewsItems.do?status=pe" />
      document.newsItemForm.action = "<%= administerNewsItems%>";
      document.newsItemForm.submit();
  }
 function fnOnSettings() {
      <digi:context name="showNewsItemSettings" property="context/module/moduleinstance/showNewsItemSettings.do" />
      document.newsItemForm.action = "<%= showNewsItemSettings%>";
      document.newsItemForm.submit();
  }
</script>
<digi:errors/>
<digi:form action="/showEditNewsItem.do" method="post"><link href="images/style.css" rel="stylesheet" type="text/css">
	<table>
		<tr>
			<td class="dgTitle">
				<digi:trn key="news:newsItemDetails">News Item Details</digi:trn>
			</td>
		</tr>
		<tr>
			<td>
			</td>
		</tr>
		<tr>
			<td>	&nbsp;
				<digi:link href="/viewAllNews.do">
				<digi:trn key="news:viewAll">View All</digi:trn></digi:link>
			</td>
		</tr>
		<tr>
			<td>
			</td>
		</tr>
	</table>
	<table border="0" cellspacing="1" cellpadding="5" width="70%">
		<tr>
			<c:if test="${!empty newsItemForm.title}">
			<td colspan="2" noWrap class="dgPageTitle">
				<c:if test="${!empty newsItemForm.title}">
				<c:out value="${newsItemForm.title}" escapeXml="false" />
				</c:if>
			</td>
			</c:if>
		</tr>
		<tr>
			<td noWrap valign="top" Class="bggray2" width="23%">
				<span class="bold">
				<digi:trn key="news:description">Description:</digi:trn></span>
			</td>
			<td Class="bggray2">
				<c:if test="${!empty newsItemForm.description}">
				<c:out value="${newsItemForm.description}" escapeXml="false" />
				</c:if>
			</td>
		</tr>
		<tr>
			<td noWrap valign="top" Class="bggray2">
				<span class="bold">
				<digi:trn key="news:source">Source:</digi:trn></span>
			</td>
			<td Class="bggray2">
				<c:out value="${newsItemForm.sourceName}" /><br><a href='<bean:write name="newsItemForm" property="sourceUrl" />'>
				<c:out value="${newsItemForm.sourceUrl}" /></a>
			</td>
		</tr>
		<tr>
			<td noWrap valign="top" Class="bggray2">
				<span class="bold">
				<digi:trn key="news:language">Language:</digi:trn></span>
			</td>
			<td Class="bggray2">
				<c:if test="${!empty newsItemForm.selectedLanguage}">
				<bean:define id="languageKey" name="newsItemForm" property="languageKey" type="java.lang.String"/>
				<digi:trn key="<%=languageKey%>"><%=languageKey%></digi:trn>
				</c:if>
			</td>
		</tr>
		<tr>
			<td noWrap valign="top" Class="bggray2">
				<span class="bold">
				<digi:trn key="news:country">Country:</digi:trn></span>
			</td>
			<td Class="bggray2">
				<c:if test="${!empty newsItemForm.country}">
				<bean:define id="countryKey" name="newsItemForm" property="countryKey" type="java.lang.String"/>
				<digi:trn key="<%=countryKey%>"><bean:write name="newsItemForm" property="countryName" /></digi:trn>
				</c:if>
			</td>
		</tr>
		<tr>
			<td noWrap valign="top" Class="bggray2">
				<span class="bold">
				<digi:trn key="news:releaseDate">Release date:</digi:trn></span>
			</td>
			<td Class="bggray2">
				<c:out value="${newsItemForm.releaseDate}" />
			</td>
		</tr>
		<tr>
			<td noWrap valign="top" Class="bggray2">
				<span class="bold">
				<digi:trn key="news:archiveDate">Archive date:</digi:trn></span>
			</td>
			<td Class="bggray2">
				<c:out value="${newsItemForm.archiveDate}" />
			</td>
		</tr>
		<tr>
			<td noWrap valign="top" Class="bggray2">
				<span class="bold">
				<digi:trn key="news:author">Author:</digi:trn></span>
			</td>
			<td Class="bggray2">
				<digi:context name="digiContext" property="context"/><a href='<%= digiContext %>/um/user/showUserProfile.do?activeUserId=<bean:write name="newsItemForm" property="authorUserId" />' onclick="window.open(this.href, 'users', 'HEIGHT=500,resizable=yes,scrollbars=yes,WIDTH=500');return false;" target="user" paramId="id" paramName="index">
				<c:out value="${newsItemForm.authorFirstNames}" />
				<c:out value="${newsItemForm.authorLastName}" />
				</a>
			</td>
		</tr>
		<tr>
			<td valign="top">	&nbsp;
			</td>
			<td>
				<c:if test="${newsItemForm.editable}">
					<html:hidden property="activeNewsItem"/>
					<html:hidden property="returnUrl"/>
					<html:submit value="Edit"/>
				</c:if>
				<digi:secure actions="ADMIN">
				<html:hidden property="activeNewsItem"/>
				<html:submit value="Reject" onclick="javascript:fnOnReject()"/>
				<html:submit value="Admin" onclick="javascript:fnOnAdmin()"/>
				<html:submit value="Settings" onclick="javascript:fnOnSettings()"/></digi:secure>
			</td>
		</tr>
	</table>
</digi:form>