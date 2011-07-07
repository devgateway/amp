<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>



<digi:instance  property="calendarViewForm"/>
<c:if test="${!calendarViewForm.print}">			
<table border="0"  width="100%" height="100%">
       <tr>
          <td colspan="2" valign="top"><jsp:include page="../../aim/view/teamPagesHeader.jsp" /><td>
       </tr> 
       <tr>			
			<td height=33 colspan="2">				
				<span class=crumb>					
					&nbsp;
					<c:set var="translation">
						<digi:trn>Click here to view MyDesktop</digi:trn>
					</c:set>
					<digi:link href="/../aim/showDesktop.do" styleClass="comment" title="${translation}" >
						<digi:trn>Portfolio</digi:trn>
					</digi:link>&nbsp;&gt;&nbsp;
					<digi:trn>Calendar</digi:trn>
					<c:if test="${calendarViewForm.view!='none'}">&nbsp;&gt;&nbsp;</c:if>
					<span class="crumb" id="viewSpan">
						<digi:trn>Monthly View</digi:trn>
					</span>					
				</span>
			</td>
		</tr>      
       	<tr>
	        <td valign="top" width="300" > 
	          <digi:form action="/showCalendarView.do" styleId="filterForm">   
	            <table border="0" cellpadding="0" cellspacing="0" width="100%">
	                <tr>
			          	<td valign="top"  align="left">
			          		<table border="0" cellpadding="0" cellspacing="0" width="100%">
				                <tr>
				                  <td style="font-weight:Bold;font-family:Tahoma;padding:10px;font-size: 12px;color: #800000">
				                   		<digi:trn> Today&nbsp;is:</digi:trn>
				       	            	${calendarViewForm.currentDateBreakDown.dayOfMonth}/${calendarViewForm.currentDateBreakDown.month}/${calendarViewForm.currentDateBreakDown.year}
				                  </td>
				                <tr>
				            </table>          	          
			          	<td>
			        </tr>
			        <tr>
	                    <td valign="top" style="padding:10px;"><jsp:include page="viewEventsNavigator.jsp" /><td>
	                </tr>
	                <tr valign="top">
	                    <td valign="top" style="padding:10px;"><jsp:include page="viewEventsFilter.jsp" /></td>
	                </tr>
	            </table>
	            </digi:form>
	        </td>
    
	        <td valign="top" width="100%">
	               <jsp:include page="viewEventsBody.jsp" />
	        </td>
    	</tr>
</table>
</c:if>	    
<c:if test="${calendarViewForm.print}">
<table border="0" align="center" width="100%" height="500px">
<tr  align="center">
	<td  align="center" width="800" height="500px" > 
		<jsp:include page="viewEventsBody.jsp" />
	</td>
</tr>

</table>
</c:if>
