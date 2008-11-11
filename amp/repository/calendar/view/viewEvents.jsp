<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance  property="calendarViewForm"/>

<table border="0"  width="100%" height="100%">
       <tr>
          <td colspan="2" valign="top"><jsp:include page="../../aim/view/teamPagesHeader.jsp" flush="true"/><td>
       </tr> 
       <tr>			
			<td height=33>
				<span class=crumb>
					&nbsp;
					<c:set var="translation">
						<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
					</c:set>
					<digi:link href="/../aim/showDesktop.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:portfolio">Portfolio</digi:trn>
					</digi:link>&nbsp;&gt;&nbsp;
					<digi:trn key="calendar:Calendar">Calendar</digi:trn>
					&nbsp;&gt;&nbsp;
					<c:if test="${calendarViewForm.view=='yearly'}"><digi:trn key="calendar:yearlyView">Yearly View</digi:trn></c:if>
					<c:if test="${calendarViewForm.view=='monthly'}"><digi:trn key="calendar:monthlyView">Monthly View</digi:trn></c:if>
					<c:if test="${calendarViewForm.view=='weekly'}"><digi:trn key="calendar:weeklyView">Weekly View</digi:trn></c:if>
					<c:if test="${calendarViewForm.view=='daily'}"><digi:trn key="calendar:dailyView">Daily View</digi:trn></c:if>
				</span>
			</td>
		</tr>      
       	<tr>
	        <td valign="top" width="230" > 
	          <digi:form action="/showCalendarView.do" styleId="filterForm">   
	            <table border="0" width="100%">	            	
	            	<tr>
	                    <td valign="top" style="padding:10px;"><jsp:include page="viewEventsNavigator.jsp" flush="true"/><td>
	                </tr>
	                <tr style="width: 100%">
			          	<td valign="top" style="padding:5px;">          	
			          		<table>
				                <tr>
				                  <td style="font-weight:Bold;font-family:Tahoma;padding:6px;font-size: 12px">
				                   		<digi:trn key="calendar:navToday"> Today&nbsp;is:</digi:trn>
				       	            	${calendarViewForm.currentDateBreakDown.dayOfMonth}/${calendarViewForm.currentDateBreakDown.month}/${calendarViewForm.currentDateBreakDown.year}
				                  </td>
				                <tr>
				            </table>          	          
			          	<td>
			        </tr>	                             
	                <tr valign="top">
	                    <td valign="top" style="padding:10px;"><jsp:include page="viewEventsFilter.jsp" flush="true"/></td>
	                </tr>	               		                
	            </table>
	            </digi:form>
	        </td>
	        <td valign="top" width="100%">
	            <table width="100%">                           
	                <tr>
	                    <td valign="top" style="padding:10px;height: 100%;"><jsp:include page="viewEventsBody.jsp" flush="true"/></td>
	                </tr>
	            </table>
	        </td>
    	</tr>
</table>
