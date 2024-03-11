<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://digijava.org/fields" prefix="field" %>
<%@ taglib uri="http://digijava.org/features" prefix="feature" %>

<link rel="stylesheet" type="text/css" href="<digi:file src="/WEB-INF/repository/aim/css/amptabs.css"/>"/>



<digi:instance property="calendarViewForm"/>
    <DIV id="tabs">
    	<UL>
    		<feature:display name="Yearly View" module="Calendar">
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
				</feature:display>
				<feature:display name="Monthly View" module="Calendar">							
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
				</feature:display>
				<feature:display name="Weekly View" module="Calendar">
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
				</feature:display>
				<feature:display name="Daily View" module="Calendar">
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
				</feature:display>	
		</UL>						
	</DIV>