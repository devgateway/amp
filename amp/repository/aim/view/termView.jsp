<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<digi:instance property="aimAdvancedReportForm" />

			<!-- by terms rows-->			
		<logic:equal name="aimAdvancedReportForm" property="reportType" value="donor">
		<logic:equal name="typeAssist" value="true">
		<logic:iterate name="records" property="assistanceCopy" id="termsType">
		<tr><!-- rowspanned -->
		<td align="left" height="21" width="89" >
		<bean:write name="termsType"/>
		</td>
		<logic:iterate name="records"  property="ampFund" id="ampFund" 	type="org.digijava.module.aim.helper.AmpFund">
		
				<logic:iterate name="aimAdvancedReportForm"  property="addedMeasures" id="addedMeasures">
								
					<c:if test="${addedMeasures.measureName == 'Actual Commitments'}">
					<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
					<td align="right" height="21" width="89" >
							<logic:notEqual name="ampFund" property="commAmount" value="0">
								<logic:iterate name="ampFund" property="byTypeComm" id="terms">
								<c:if test="${terms.fundingTerms == termsType}">
										<bean:write name="terms"/>
								</c:if>
								</logic:iterate>
							</logic:notEqual>
					</td>
					</logic:equal>
					</c:if>

					<c:if test="${addedMeasures.measureName == 'Actual Disbursements'}">
					<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
					<td align="right" height="21" width="89">
					<logic:notEqual name="ampFund" property="disbAmount" value="0">
								<logic:iterate name="ampFund" property="byTypeDisb" id="terms">
								<c:if test="${terms.fundingTerms == termsType}">
										<bean:write name="terms"/>
								</c:if>
								</logic:iterate>
					</logic:notEqual>
					</td>
					</logic:equal>
					</c:if>
						
					<c:if test="${addedMeasures.measureName == 'Actual Expenditures'}">
					<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
					<td align="right" height="21" width="89">
					<logic:notEqual name="ampFund" property="expAmount" value="0">
								<logic:iterate name="ampFund" property="byTypeExp" id="terms">
								<c:if test="${terms.fundingTerms == termsType}">
										<bean:write name="terms"/>
								</c:if>
								</logic:iterate>
					</logic:notEqual>
					</td>
					</logic:equal>
					</c:if>
						
					<c:if test="${addedMeasures.measureName == 'Planned Commitments'}">
					<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
					<td align="right" height="21" width="89">
					<logic:notEqual name="ampFund" property="plCommAmount" value="0">
								<logic:present name="ampFund" property="byTypePlComm">
								<logic:iterate name="ampFund" property="byTypePlComm" id="terms">
								<c:if test="${terms.fundingTerms == termsType}">
										<bean:write name="terms"/>
								</c:if>
								</logic:iterate>
								</logic:present>
								<logic:notPresent name="ampFund" property="byTypePlComm">
								ERROR - byType not added!
								</logic:notPresent>
					</logic:notEqual>
					</td>
					</logic:equal>
					</c:if>
						
					<c:if test="${addedMeasures.measureName == 'Planned Disbursements'}">
					<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
					<td align="right" height="21" width="89">
					<logic:notEqual name="ampFund" property="plDisbAmount" value="0">
								<logic:present name="ampFund" property="byTypePlDisb">
								<logic:iterate name="ampFund" property="byTypePlDisb" id="terms">
								<c:if test="${terms.fundingTerms == termsType}">
										<bean:write name="terms"/>
								</c:if>
								</logic:iterate>
								</logic:present>
								<logic:notPresent name="ampFund" property="byTypePlDisb">
								ERROR - byType not added!
								</logic:notPresent>
					</logic:notEqual>
					</td>
					</logic:equal>
					</c:if>
						
					<c:if test="${addedMeasures.measureName == 'Planned Expenditures'}">
					<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
					<td align="right" height="21" width="89">
					<logic:notEqual name="ampFund" property="plExpAmount" value="0">
								<logic:iterate name="ampFund" property="byTypePlExp" id="terms">
								<c:if test="${terms.fundingTerms == termsType}">
										<bean:write name="terms"/>
								</c:if>
								</logic:iterate>
					</logic:notEqual>
					</td>
					</logic:equal>
					</c:if>

					<c:if test="${addedMeasures.measureName == 'Undisbursed Balance'}">
					<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
					<logic:notEmpty name="ampFund" property="unDisbAmount">
					<td align="right" height="21" width="89">
					<logic:notEqual name="ampFund" property="unDisbAmount" value="0">
								<logic:iterate name="ampFund" property="byTypeUnDisb" id="terms">
								<c:if test="${terms.fundingTerms == termsType}">
										<bean:write name="terms"/>
								</c:if>
								</logic:iterate>
					</logic:notEqual>
					</td>
					</logic:notEmpty>
					</logic:equal>
					</c:if>
						
			</logic:iterate>
			</logic:iterate>	
		</logic:iterate>		
		</logic:equal>
		</logic:equal>
		<!-- end of by terms -->
