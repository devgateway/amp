
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<link href="images/style.css" rel="stylesheet" type="text/css">
<digi:instance property="newsItemForm"/>
	<table border="0" cellspacong="1" cellpadding="2" width="100%">
		<tr>
			<td align="center">
				<table width="100%">
					<digi:secure authenticated="true">
					<tr>
						<td width="50%" align>
							<digi:link href="/viewAllNews.do?status=mall">
							<digi:trn key="news:viewMyNews">View my news</digi:trn></digi:link>
						</td>
						<digi:secure actions="ADMIN">
						<td width="50%">
							<digi:link href="/showNewsItems.do?status=pe">
							<digi:trn key="news:administer">Administer</digi:trn></digi:link>
						</td></digi:secure>
					</tr>
					<tr>
						<td width="50%">
							<digi:link href="/viewAllNews.do?status=mpe">
							<digi:trn key="news:viewMyPeNews">View my pending news</digi:trn></digi:link>
						</td>
						<digi:secure actions="ADMIN">
						<td width="50%">
							<digi:link href="/showNewsItemSettings.do">
							<digi:trn key="news:settings">Settings</digi:trn></digi:link>
						</td></digi:secure>
					</tr>
					<tr>
						<td width="50%">
							<digi:link href="/showCreateNewsItem.do">
							<digi:trn key="news:addNewItem">Add New Item</digi:trn></digi:link>
						</td>
						<td width="50%">	&nbsp;
						</td>
					</tr></digi:secure>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<c:if test="${!empty newsItemForm.newsList}">
				<table border="0" cellspacing="1" cellpadding="5">
					<c:forEach var="newsList" items="${newsItemForm.newsList}" >
					<tr class="bggray2">
						<td class="dgPageTitle" colspan="2">
							<c:if test="${!empty newsList.title}">
							<span class="dgTitle"><strong>
							<digi:link href="/showNewsItemDetails.do" paramName="newsList" paramId="activeNewsItem" paramProperty="id">
							<bean:define id="title" name="newsList" property="title" type="java.lang.String"/>
							<%=title%></digi:link></strong></span></c:if>
						</td>
					</tr>
					<tr class="bggray2">
						<td colspan="2">
							<c:out value="${newsList.releaseDate}"/>
						</td>
					</tr>
					<tr class="bggray2">
						<td nowrap class="bold">
							<digi:trn key="news:source">Source:</digi:trn>
						</td>
						<td><a href='<bean:write name="newsList" property="sourceUrl"/>'>
							<c:out value="${newsList.sourceName}"/>
						</td>
					</tr>
					<tr class="bggray2">
						<td nowrap class="bold">
							<digi:trn key="news:country">Country:</digi:trn>
						</td>
						<td>
							<c:if test="${!empty newsList.country}">
							<bean:define id="countryKey" name="newsList" property="countryKey" type="java.lang.String"/>
							<digi:trn key='<%=countryKey%>'><c:out value="${newsList.countryName}"/></digi:trn></c:if>
						</td>
					</tr>
					<tr class="bggray2">
						<td nowrap class="bold">
							<digi:trn key="news:author">Author:</digi:trn>
						</td>
						<td>
							<digi:context name="digiContext" property="context"/><a href='<%= digiContext %>/um/user/showUserProfile.do?activeUserId=<bean:write name="newsList" property="authorUserId" />' onClick="window.open(this.href, 'users', 'HEIGHT=500,resizable=yes,scrollbars=yes,WIDTH=500');return false;" target="user" paramId="id" paramName="index">
							<c:out value="${newsList.authorFirstNames}"/>
							<c:out value="${newsList.authorLastName}"/>
							</a>
						</td>
					</tr>
					<tr class="bggray2">
						<td colspan="2">	&nbsp;
						</td>
					</tr></c:forEach>
					<tr>
						<td align="right" valign="top">
							<c:if test="${!empty newsItemForm.prev}">
							<digi:link href="/viewAllNews.do" paramName="newsItemForm" paramId="nav" paramProperty="prev"><small><<</small>
							<digi:trn key="news:prev">Prev</digi:trn></digi:link></c:if>
						</td>
						<td align="left" valign="top">
							<c:if test="${!empty newsItemForm.next}">
							<digi:link href="/viewAllNews.do" paramName="newsItemForm" paramId="nav" paramProperty="next">
							<digi:trn key="news:next">Next</digi:trn><small>>></small></digi:link></c:if>
						</td>
					</tr>
				</table></c:if>
				<c:if test="${empty newsItemForm.newsList}">
					<digi:trn key="news:noItems">There are no items to view</digi:trn>
				</c:if>
			</td>
		</tr>
	</table>