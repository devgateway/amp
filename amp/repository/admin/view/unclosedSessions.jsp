<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="unclosedSessionsForm" />
<digi:errors/>

<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
	<tr class="yellow">
		<td><digi:img src="ampModule/admin/images/yellowLeftTile.gif" border="0" width="20"/></td>
		<td width="100%">
			<font class="sectionTitle">
				<digi:trn key="admin:unclosedSessions">Unclosed Sessions</digi:trn>
			</font>
		</td>
	</tr>
	<tr>
	<td colspan="2" class="yellow" valign="top" align="left">
		<table>
		 <tr>	  
		   <td align="left" width="60%"><b><digi:trn key="admin:numberOfSuspectedSessions">Total number of suspected sessions:</digi:trn></b></td>
		   <td align="left"><font color="blue"><c:out value="${unclosedSessionsForm.totalCount}" /></font></td>
		 </tr>
	    </table>   
	</td>	
	</tr>  
	<c:if test="${!empty unclosedSessionsForm.unclosedSeesions}">
	<tr><td colspan="2" class="yellow" valign="top" align="left">
		<table>
		 <tr>	  
		   <td align="left" width="60%"><b><digi:trn key="admin:numberOfOpenedSessions">Total number of opened sessions:</digi:trn></b></td>
		   <td align="left"><font color="blue"><c:out value="${unclosedSessionsForm.totalOpened}" /></font></td>
		 </tr>
	    </table>   
	</td></tr> 
	<tr><td colspan="2" class="yellow" valign="top" align="left">
	   <c:if test="${unclosedSessionsForm.showAll}">
	       <digi:trn key="admin:nowShowingAll">Now showing All sessions.</digi:trn><digi:link href="/showUnclosedSessions.do?show=opened"><digi:trn key="admin:showOpened">Show Opened</digi:trn></digi:link>
	   </c:if>
	   <c:if test="${! unclosedSessionsForm.showAll}">
	       <digi:trn key="admin:nowShowingOpened">Now showing Opened sessions.</digi:trn><digi:link href="/showUnclosedSessions.do?show=all"><digi:trn key="admin:showAll">Show All</digi:trn></digi:link>
	   </c:if>
	</td></tr> 
	<tr>
	<td colspan="2" class="yellow" valign="top" align="left">
	<table>
	<tr bgcolor="silver">
	   <td align="left" width="10%"><b><digi:trn key="admin:sessionKey">Session Key</digi:trn></b></td>
	   <td align="center" width="80%"><b><digi:trn key="admin:sessionTrace">Session Stack Trace</digi:trn></b></td>
	   <td align="center" width="10%">&nbsp;</td>   
	 </tr> 
	  <logic:iterate indexId="index" id="unclosedSessions" name="unclosedSessionsForm" property="unclosedSeesions" type="org.digijava.ampModule.admin.form.UnclosedSessionsForm.UnclosedSessionInfo">
	    <c:set var="isEven" value="${index % 2}"/>
	    <tr 
	    <c:if test="${isEven == 0}">bgcolor="#EBEBEB"</c:if>
	    <c:if test="${isEven == 1}">bgcolor="#D2D2DB"</c:if>
	    >
	  	<td align="left" valign="top" width="10%">
	  	    <c:out value="${unclosedSessions.key}" />
	  	</td>
	  	<td align="center" valign="top" width="80%">
			<html:textarea name="unclosedSessions" property="value" rows="5" cols="50"/>  	
	  	</td>
	  	<td align="center" valign="top" width="10%">
	  	 <c:if test="${! unclosedSessions.closed}">
	  		    <digi:link href="/showUnclosedSessions.do" paramName="index" paramId="index"><digi:trn key="admin:release">Release</digi:trn></digi:link>
		 </c:if>	
	  	 <c:if test="${unclosedSessions.closed}">
	  	    <digi:trn key="admin:explicitlyClosed">Explicitly closed</digi:trn>
		 </c:if>	
	  	</td>
	    </tr>
	  </logic:iterate> 
	</table>
	</td>
	</tr>
	</c:if>  
</table>