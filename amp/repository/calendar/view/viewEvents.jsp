<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule"%>

<digi:instance  property="calendarViewForm"/>
<c:if test="${!calendarViewForm.print}">			
	<table border="0" width="1000" height="800px" align="center"> 
       	<tr>			
			<td height="33" colspan="2">				
				<span class="crumb">					
					&nbsp;
					<c:set var="translation">
						<digi:trn>Click here to view MyDesktop</digi:trn>
					</c:set>
					<digi:link href="/../aim/showDesktop.do" styleClass="comment" title="${translation}" >
						<digi:trn>Portfolio</digi:trn>
					</digi:link>&nbsp;&gt;&nbsp;
					<digi:trn>Calendar</digi:trn>
					&nbsp;&gt;&nbsp;
					
					<span class="crumb" id="viewSpan" style="color: #FF6000">
						<c:if test="${not empty calendarViewForm.view && calendarViewForm.view!='none'}">
							<digi:trn>${calendarViewForm.view} View</digi:trn>
						</c:if>
						<c:if test="${empty calendarViewForm.view || calendarViewForm.view=='none'}"><digi:trn>Monthly View</digi:trn></c:if>
						
					</span>					
				</span>
			</td>
		</tr>      
       	<tr>       		
			<td valign="top" width="223px;" > 
	        	<digi:form action="/showCalendarView.do" styleId="filterForm">   
	            	<table border="0" cellpadding="0" cellspacing="0" width="100%">
	                	<tr>
			          		<td valign="top" colspan="2" align="left">
			          			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				                	<tr>
				                  		<td style="font-weight:Bold;font-family:Tahoma;padding-left:10px;font-size: 12px;color: #000000">
				                   			<digi:trn> Today&nbsp;is:</digi:trn>
				       	            		${calendarViewForm.currentDateBreakDown.dayOfMonth}/${calendarViewForm.currentDateBreakDown.month}/${calendarViewForm.currentDateBreakDown.year}
				                  		</td>
				                	</tr>
				            	</table>          	          
			          		</td>
			        	</tr>

	                	<tr valign="top" >
	                    	<td valign="top" colspan="2" style="padding:10px;">
	                    		<jsp:include page="viewEventsFilter.jsp" flush="true"/>
	                    	</td>
	                	</tr>
	                	<feature:display name="Filter" ampModule="Calendar">
	                	<tr valign="top" align="center">
	                    	<td valign="top" width="50%" align="right" style="padding:10px;">
								<field:display name="Run Filter Button" feature="Filter">
									<input class="buttonx" type="submit" value="<digi:trn>Run Filter</digi:trn>" onClick="changeDonorsAndEventTypesState();"/>
						    	</field:display>
							</td>
							<td valign="top" width="50%" align="left" style="padding:10px;">
						    	<field:display name="Reset Filter Button" feature="Filter">
						    		<input class="buttonx" type="button" onclick="javascript:document.location.href = '/calendar/showCalendarView.do?filterInUse=false'" value="<digi:trn>Reset</digi:trn>" />
								</field:display>
	                    	</td>
	                	</tr>
	                	</feature:display>
	            	</table>
	        	</digi:form>
			</td>
			<td valign="top" width="100%">
				<!-- CELLS SECTION -->
	               <jsp:include page="viewEventsBody.jsp" />
	        </td>
    	</tr>
	</table>
</c:if>	    
<c:if test="${calendarViewForm.print}">
	<table border="0" align="center" width="100%" height="500px">
		<tr align="center">
			<td align="center" width="800" height="500px" > 
		<jsp:include page="viewEventsBody.jsp" />
			</td>
		</tr>
	</table>
</c:if>