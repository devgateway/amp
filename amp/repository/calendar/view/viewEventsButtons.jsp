<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<link rel="stylesheet" type="text/css" href="<digi:file src="module/aim/css/amptabs.css"/>"/>

<!-- Yahoo Panel --> 
<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/container.css'/>"/>

<digi:instance property="calendarViewForm"/>

<table id="tbl"width="100%" cellpadding="0" cellspacing="0" height="100%">
    <tr height="100%">
    	<td>
    		<DIV id="tabs">
               <UL>
           			<c:if test="${calendarViewForm.view == 'yearly'}">
                      <LI>
                      	<a name="node">
                        	<div>
								<digi:trn key="calendar:Yearly">Yearly</digi:trn>							
                            </div>
                        </a>
                      </LI>
					</c:if> 
					<c:if test="${calendarViewForm.view != 'yearly'}">
                       <LI>
                       		<span>
                            	<a href="#" onclick="submitFilterForm('yearly', '${calendarViewForm.timestamp}'); return(false);">
                            		<div>
                            			<digi:trn key="calendar:Yearly">Yearly</digi:trn>	
                            		</div>                            	
                            	</a>
                            </span>
                         </LI>
					</c:if>							
                    <c:if test="${calendarViewForm.view == 'monthly'}">
                    	<LI>
                        	<a name="node">
                            	<div>
									<digi:trn key="calendar:Monthly">Monthly</digi:trn>						
                                </div>
                            </a>
                        </LI>
					</c:if>
					<c:if test="${calendarViewForm.view != 'monthly'}">
						<LI>
                        	<span>
								<a href="#" onclick="submitFilterForm('monthly', '${calendarViewForm.timestamp}'); return(false);">
									<div>
									<digi:trn key="calendar:Monthly">Monthly</digi:trn>
									</div>
								</a>			
                            </span>
                        </LI>
					</c:if>
					<c:if test="${calendarViewForm.view == 'weekly'}">
                    	<LI>
                        	<a name="node">
                            	<div>
									<digi:trn key="calendar:Weekly">Weekly</digi:trn>
                                </div>
                            </a>
                        </LI>
					</c:if>
					<c:if test="${calendarViewForm.view != 'weekly'}">
                    	<LI>
                        	<span>
								<a href="#" onclick="submitFilterForm('weekly', '${calendarViewForm.timestamp}'); return(false);">
									<div>
										<digi:trn key="calendar:Weekly">Weekly</digi:trn>
									</div>									
								</a>
                            </span>
                       </LI>
					</c:if>
                   <c:if test="${calendarViewForm.view == 'daily'}">
                    	<LI>
                        	<a name="node">
                            	<div>
									<digi:trn key="calendar:1Day">1 Day</digi:trn>
                                </div>
                            </a>
                        </LI>
					</c:if>
					<c:if test="${calendarViewForm.view != 'daily'}">
                    	<LI>
                        	<span>
								<a href="#" onclick="submitFilterForm('daily', '${calendarViewForm.timestamp}'); return(false);">
									<div>
										<digi:trn key="calendar:1Day">1 Day</digi:trn>
									</div>
								</a>
                            </span>
                        </LI>
					</c:if>	
				</UL>						
			</DIV>
    	</td>    	
    
    <%--
        <td <c:if test="${calendarViewForm.view == 'yearly'}">id="clicked"</c:if> align="center"><a href="#" onclick="submitFilterForm('yearly', '${calendarViewForm.timestamp}'); return(false);"><digi:trn key="calendar:Yearly">Yearly</digi:trn></a></td>
        <td style="border:none;width:5px">&nbsp;</td>
        <td <c:if test="${calendarViewForm.view == 'monthly'}">id="clicked"</c:if> align="center"><a href="#" onclick="submitFilterForm('monthly', '${calendarViewForm.timestamp}'); return(false);"><digi:trn key="calendar:Monthly">Monthly</digi:trn></a></td>
        <td style="border:none;width:5px">&nbsp;</td>
        <td <c:if test="${calendarViewForm.view == 'weekly'}">id="clicked"</c:if> align="center"><a href="#" onclick="submitFilterForm('weekly', '${calendarViewForm.timestamp}'); return(false);"><digi:trn key="calendar:Weekly">Weekly</digi:trn></a></td>
        <td style="border:none;width:5px">&nbsp;</td>
        <td <c:if test="${calendarViewForm.view == 'daily'}">id="clicked"</c:if> align="center"><a href="#" onclick="submitFilterForm('daily', '${calendarViewForm.timestamp}'); return(false);"><digi:trn key="calendar:1Day">1 Day</digi:trn></a></td>
        <td style="border:none;width:5px">&nbsp;</td>
        <td <c:if test="${calendarViewForm.view == 'custom'}">id="clicked"</c:if> align="center"><a href="#" onclick="submitFilterForm('custom', '${calendarViewForm.timestamp}'); return(false);"><digi:trn key="calendar:CustomView">Custom View</digi:trn></a></td>--%>
    <tr>
</table>
