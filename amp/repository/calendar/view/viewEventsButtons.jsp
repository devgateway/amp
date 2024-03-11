<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>
<%@ taglib uri="/src/main/resources/tld/fieldVisibility.tld" prefix="field" %>
<%@ taglib uri="/src/main/resources/tld/featureVisibility.tld" prefix="feature" %>

<link rel="stylesheet" type="text/css" href="<digi:file src="module/aim/css/amptabs.css"/>"/>



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