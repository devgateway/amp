<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/c.tld" prefix="c" %>


<digi:instance property="aimAdvancedReportForm" />

				<logic:notEmpty name="records" property="ampFund">
				
				<!-- this is for type Of assist -->
				<logic:equal name="typeAssist" value="true">
				<logic:equal name="aimAdvancedReportForm" property="reportType" value="donor">
				<td align="left" valign="center" height="21">Total</td>
				</logic:equal>
				</logic:equal>
		
				<logic:iterate name="records"  property="ampFund" id="ampFund" 	type="org.digijava.module.aim.helper.AmpFund">
					<logic:iterate name="aimAdvancedReportForm"  property="addedMeasures" id="addedMeasures">
						<c:if test="${addedMeasures.measureName == 'Actual Commitments'}">
						<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
						<td align="right" height="21" >
						<logic:notEqual name="ampFund" property="commAmount" value="0">
						<bean:write name="ampFund" property="commAmount" />
						</logic:notEqual>
						</td>
						</logic:equal>
						</c:if>
						
						<c:if test="${addedMeasures.measureName == 'Actual Disbursements'}">
						<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
						<td align="right" height="21" >
						<logic:notEqual name="ampFund" property="disbAmount" value="0">
						<bean:write name="ampFund" property="disbAmount" />
						</logic:notEqual>
						</td>
						</logic:equal>
						</c:if>
						
						<c:if test="${addedMeasures.measureName == 'Actual Expenditures'}">
						<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
						<td align="right" height="21" >
						<logic:notEqual name="ampFund" property="expAmount" value="0">
						<bean:write name="ampFund" property="expAmount" />
						</logic:notEqual>
						</td>
						</logic:equal>
						</c:if>
						
						<c:if test="${addedMeasures.measureName == 'Planned Commitments'}">
						<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
						<td align="right" height="21" >
						<logic:notEqual name="ampFund" property="plCommAmount" value="0">
						<bean:write name="ampFund" property="plCommAmount" />
						</logic:notEqual>
						</td>
						</logic:equal>
						</c:if>
						
						<c:if test="${addedMeasures.measureName == 'Planned Disbursements'}">
						<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
						<td align="right" height="21" >
						<logic:notEqual name="ampFund" property="plDisbAmount" value="0">
						<bean:write name="ampFund" property="plDisbAmount" />
						</logic:notEqual>
						</td>
						</logic:equal>
						</c:if>
						
						<c:if test="${addedMeasures.measureName == 'Planned Expenditures'}">
						<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
						<td align="right" height="21" >
						<logic:notEqual name="ampFund" property="plExpAmount" value="0">
						<bean:write name="ampFund" property="plExpAmount" />
						</logic:notEqual>
						</td>
						</logic:equal>
						</c:if>

													
						<c:if test="${addedMeasures.measureName == 'Undisbursed Balance'}">							
							<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
							<logic:notEmpty name="ampFund" property="unDisbAmount">
							<td align="right" height="21" >
							<logic:notEqual name="ampFund" property="unDisbAmount" value="0">
							<bean:write name="ampFund" property="unDisbAmount" />
							</logic:notEqual>
							</td>
							</logic:notEmpty>
							</logic:equal>
						</c:if>

					</logic:iterate>
				</logic:iterate>
				</logic:notEmpty>								
