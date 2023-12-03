<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>

<link rel="stylesheet" type="text/css" href="<digi:file src="ampModule/aim/css/amptabs.css"/>"/>



<digi:instance property="calendarViewForm"/>
    <DIV id="tabs">
    	<UL>
    		<feature:display name="Yearly View" ampModule="Calendar">
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
				<feature:display name="Monthly View" ampModule="Calendar">
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
				<feature:display name="Weekly View" ampModule="Calendar">
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
				<feature:display name="Daily View" ampModule="Calendar">
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