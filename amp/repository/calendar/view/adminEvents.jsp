
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:errors/>
<digi:form action="/confirmCalendarItems.do" method="post">
	<p>
	<c:forEach var="statusItem" items="${calendarItemForm.statusList}">
	<c:if test="${!statusItem.selected}">
	<digi:link href="/showCalendarItems.do" paramName="statusItem" paramId="status" paramProperty="status" styleClass="tlt">
	<c:out value="${statusItem.title}"/></digi:link></c:if>
	<c:if test="${statusItem.selected}" ><strong>
	<c:out value="${statusItem.title}"/></strong></c:if>&nbsp;|</c:forEach></p>
	
	<c:if test="${!empty calendarItemForm.eventsList}">
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
				<digi:trn key="calendar:select">Select</digi:trn></strong>
			</td>
			<td><strong>
				<digi:trn key="calendar:title1">Title</digi:trn></strong>
			</td>
			<td><strong>
				<digi:trn key="calendar:author1">Author</digi:trn></strong>
			</td>
			<td nowrap><strong>
				<digi:trn key="calendar:releaseDate1">Release Date</digi:trn></strong>
			</td>
			<td><strong>
				<digi:trn key="calendar:status">Status</digi:trn></strong>
			</td>
		</tr>
		<c:forEach var="calendarItem" items="${calendarItemForm.eventsList}" >
		<tr valign="top" bgcolor="#F0F3F7">
			<td align="center">
				<html:checkbox name="calendarItem" property="selected" indexed="true"/>
			</td>
			<td nowrap>
				<c:if test="${!empty calendarItem.title}">
				<c:out value="${calendarItem.title}" escapeXml="false"/>
				</c:if>
				<html:hidden property="returnUrl"/>
				<digi:link href="/showEditCalendarItem.do" paramName="calendarItem" paramId="activeCalendarItem" paramProperty="id" styleClass="tlt">[
				<digi:trn key="calendar:edit">Edit</digi:trn>]</digi:link>
			</td>
			<td nowrap>
				<c:out value="${calendarItem.authorFirstNames}" />
				<c:out value="${calendarItem.authorLastName}" />
			</td>
			<td nowrap>
				<c:out value="${calendarItem.startDate}" />
			</td>
			<td nowrap>
				<c:out value="${calendarItem.status}" />
			</td>
		</tr>
		</c:forEach></table></c:if>
		<!-- empty -->
		<c:if test="${empty calendarItemForm.eventsList}">
		<table>
  		  <tr><td>
  	       <digi:trn key="calendar:thereAreNoEventsToView">
  	          There are no items to be displayed on this page. Please use prev/next links for navigation</digi:trn>
  	      </td></tr>
  	     </table> 
    	</c:if>
		<table>
		<tr>
			<td align="right" valign="top">
				<c:if test="${!empty calendarItemForm.prev}">
				<digi:link href="/showCalendarItems.do" paramName="calendarItemForm" paramId="nav" paramProperty="prev"><small><font size="1">&lt;&lt;</font></small><small>
				<digi:trn key="news:prev">Prev</digi:trn></small></digi:link></c:if>
			</td>
			<td align="left" valign="top">
				<c:if test="${!empty calendarItemForm.next}">
				<digi:link href="/showCalendarItems.do" paramName="calendarItemForm" paramId="nav" paramProperty="next">
				<small><digi:trn key="news:next">Next</digi:trn></small><small><font size="1">&gt;&gt;</font></small></digi:link></c:if>
			</td>
		</tr>
	</table>
	<digi:trn key="calendar:soTheFollowingToTheSelItems">Do the following to the selected items</digi:trn>
	<html:select property="selectedStatus">
	<bean:define id="sid" name="calendarItemForm" property="calendarStatus" type="java.util.Collection"/>
	<html:options collection="sid" property="code" labelProperty="statusName"/></html:select><BR>
	<html:submit value="submit" />
	
<c:if test="${!calendarItemForm.selected}">
 <script language="JavaScript">
    alert("Please select at least one item");
 </script> 	
</c:if>	
</digi:form>
