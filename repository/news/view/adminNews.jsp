
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:errors/>
<digi:form action="/confirmNewsItems.do" method="post">
	<table width="100%">
  	 <digi:secure authenticated="true">
		<tr>
	  	  <td width="50%" align>
			<digi:link href="/viewAllNews.do?status=mall">
			<digi:trn key="news:viewMyNews">View my news</digi:trn></digi:link>
		   </td>
			<digi:secure actions="ADMIN">
			<td width="50%">
			 <digi:link href="/showNewsItemSettings.do">
			 <digi:trn key="news:settings">Settings</digi:trn></digi:link>
			</td></digi:secure>
		</tr>
		<tr>
			<td width="50%">
		  	 <digi:link href="/viewAllNews.do?status=mpe">
		     <digi:trn key="news:viewMyPeNews">View my pending news</digi:trn></digi:link>
			</td>
			<td width="50%">
			</td>
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
	<p>

	<c:forEach items="${newsItemForm.statusList}" var="statusItem" >
	<c:if test="${!statusItem.selected}">
	<digi:link href="/showNewsItems.do" paramName="statusItem" paramId="status" paramProperty="status" styleClass="tlt">
	<c:out value="${statusItem.title}"/></digi:link>
	</c:if>
	<c:if test="${statusItem.selected}">
	<strong><c:out value="${statusItem.title}"/></strong></c:if>&nbsp;|</c:forEach></p>
	<!-- -->
	<c:if test="${!empty newsItemForm.newsList}">
	<table width="100%" border="0" cellpadding="7" cellspacing="1">
		<tr valign="bottom" bgcolor="#F0F3F7">
			<td align="center" nowrap bgcolor="#FFFFFF">	&nbsp;
			</td>
			<td bgcolor="#FFFFFF">	&nbsp;
			</td>
			<td bgcolor="#FFFFFF">	&nbsp;
			</td>
			<td bgcolor="#FFFFFF">	&nbsp;
			</td>
			<td bgcolor="#FFFFFF">	&nbsp;
			</td>
		</tr>
		<tr bgcolor="#F0F3F7" class="tlt">
			<td align="center" nowrap><strong>
				<digi:trn key="news:select1">Select</digi:trn></strong>
			</td>
			<td><strong>
				<digi:trn key="news:title1">Title</digi:trn></strong>
			</td>
			<td><strong>
				<digi:trn key="news:author1">Author</digi:trn></strong>
			</td>
			<td nowrap><strong>
				<digi:trn key="news:releaseDate1">Release date</digi:trn></strong>
			</td>
			<td><strong>
				<digi:trn key="news:status1">Status</digi:trn></strong>
			</td>
		</tr>
		<c:forEach var="newsItem" items="${newsItemForm.newsList}" >
		<tr valign="top" bgcolor="#F0F3F7">
			<td align="center">
				<html:checkbox name="newsItem" property="selected" indexed="true"/>
			</td>
			<td nowrap>
				<c:if test="${!empty newsItem.title}">
				<c:out value="${newsItem.title}" escapeXml="false" />
				</c:if>
				<html:hidden property="returnUrl"/>
				<digi:link href="/showEditNewsItem.do" paramName="newsItem" paramId="activeNewsItem" paramProperty="id" styleClass="tlt">[
				<digi:trn key="news:edit1">Edit</digi:trn>]</digi:link>
			</td>
			<td nowrap>
				<c:out value="${newsItem.authorFirstNames}" />
				<c:out value="${newsItem.authorLastName}" />
			</td>
			<td nowrap>
				<c:out value="${newsItem.releaseDate}" />
			</td>
			<td nowrap>
			<%--
			<c:choose>
			  <c:when test="${newsItem.autoArchived}">
				   <digi:trn key="news:byDate">By Date</digi:trn>
			  </c:when>
			  <c:otherwise>
					<c:out value="${newsItem.status}" />
			  </c:otherwise>
			</c:choose>
			--%>
			<c:out value="${newsItem.status}" />
			</td>
		</tr></c:forEach></table></c:if>
		<!-- empty -->
		<c:if test="${empty newsItemForm.newsList}">
		<table>
  		  <tr><td>
  	       <digi:trn key="news:thereAreNoEventsToView">
  	          There are no items to be displayed on this page. Please use prev/next links for navigation</digi:trn>
  	      </td></tr>
  	     </table> 
    	</c:if>
    	<!-- -->
		<table>
		<tr>
			<td align="right" valign="top">
				<c:if test="${!empty newsItemForm.prev}">
				<digi:link href="/showNewsItems.do" paramName="newsItemForm" paramId="nav" paramProperty="prev"><small><font size="1">&lt;&lt;</font></small><small>
				<digi:trn key="news:prev">Prev</digi:trn></small></digi:link></c:if>
			</td>
			<td align="left" valign="top">
				<c:if test="${!empty newsItemForm.next}">
				<digi:link href="/showNewsItems.do" paramName="newsItemForm" paramId="nav" paramProperty="next">
                <small><digi:trn key="news:next">Next</digi:trn></small><small><font size="1">&gt;&gt;</font></small></digi:link></c:if>
			</td>
		</tr>
	</table>
	<digi:trn key="news:soTheFollowingToTheSelItems">Do the following to the selected items</digi:trn>
	<html:select property="selectedStatus">
	<bean:define id="sid" name="newsItemForm" property="itemStatus" type="java.util.Collection"/>
	<html:options collection="sid" property="code" labelProperty="statusName"/></html:select><BR>
	<html:submit value="submit" />
	
<c:if test="${!newsItemForm.selected}">
 <script language="JavaScript">
    alert("Please select at least one item");
 </script> 	
</c:if>	
</digi:form>