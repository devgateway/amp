<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>


<digi:errors />

<digi:instance property="calendarForm"/>
<logic:present name="calendarForm" property="eventsList"> 

<table border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td width="10" height="25"><digi:img src="images/ui/teaserTitleLeft.gif"/></td>
		<td width="100%" height="25" class="teaserTitleBody">&nbsp;Calendar</td>
		<td width="5" height="25"><digi:img src="images/ui/teaserTitleRight.gif"/></td>
	</tr>
	<tr>
		<td width="10" height="25"><digi:img src="images/ui/teaserExtrainfoLeft.gif"/></td>
		<td width="100%" height="25" class="teaserExtraBody">
		&nbsp;Calendar events
		</td>
		<td width="5" height="25"><digi:img src="images/ui/teaserExtrainfoRight.gif"/></td>
	</tr>
	<tr>
	<td class="bodyLeftTile" width="10"><digi:img src="images/ui/spacer.gif"/></td>
	<td class="bodyField" width="100%">
	<!-- Body -->
	

		<table width="180" border="0" align="center" cellpadding="5" cellspacing="1">
		<tr><td>
		<logic:iterate id="eventsList" name="calendarForm" property="eventsList">
		  <p> 
		    <font color="#1A8CFF">&raquo;</font>
		   <logic:present name="eventsList" property="title">
		    <logic:present name="eventsList" property="description">
				<digi:link href="/showCalendarItemDetails.do" paramName="eventsList" paramId="activeCalendarItem" paramProperty="id" >
		          <bean:write name="eventsList" property="title" filter="false" />
				</digi:link>   
		    </logic:present>
		    <logic:notPresent name="eventsList" property="description">
		      <a href='<bean:write name="eventsList" property="sourceUrl"/>' class="text">
		          <bean:write name="eventsList" property="title" filter="false" />
		      </a>
		    </logic:notPresent>
		    </logic:present>
		    <br>
		    <span class="dta"><i><bean:write name="eventsList" property="startDate" />-<bean:write name="eventsList" property="endDate" /></i></span>
		  </p>
		</logic:iterate> 
		</tr></td>
		  <tr> 
		    <td><div align="right">
		      <digi:link href="/viewEvents.do"><font color="#1A8CFF" size="-2">
		      <digi:trn key="calendar:viewAll">View All</digi:trn></font></digi:link>
		         <br>
		        <digi:secure authenticated="true">
		          <digi:link href="/showCreateCalendarItem.do"><font color="#1A8CFF" size="-2"><digi:trn key="calendar:addEvent">Add event</digi:trn></font></digi:link>
		        </digi:secure>
		    </div></td>
		  </tr>
		</table>

		
<!-- Body -->
	</td>
	<td class="bodyRightTile" width="10"><digi:img src="images/ui/spacer.gif"/></td>
	</tr>
	
	<tr>
	<td width="10" height="11"><digi:img src="images/ui/teaserBottomLeft.gif"/></td>
	<td width="100%" height="11" class="teaserBottomBody"><digi:img src="images/ui/spacer.gif"/></td>
	<td width="5" height="11"><digi:img src="images/ui/teaserBottomRight.gif"/></td>
	</tr>	
</table>
</logic:present>