<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<digi:instance property="aimAdvancedReportForm" />

			<!-- by terms rows-->			
		<logic:equal name="aimAdvancedReportForm" property="reportType" value="donor">
		<logic:equal name="typeAssist" value="true">
		<logic:iterate name="records" property="assistanceCopy" id="termsType">
		<tr bgcolor="#F4F4F2">
		<!-- rowspanned -->
		<td align="left" height="21" width="89" >
		<bean:write name="termsType"/>
		</td>
		
					
		
		<logic:iterate name="records"  property="ampFund" id="ampFund" 	type="org.digijava.module.aim.helper.AmpFund">

				<logic:equal name="aimAdvancedReportForm" property="acCommFlag" value="true">
				<td align="right" height="21" width="69">
				<logic:notEqual name="ampFund" property="commAmount" value="0">
								<logic:iterate name="ampFund" property="byTypeComm" id="terms">
								<c:if test="${terms.fundingTerms == termsType}">
										<bean:write name="terms"/>
								</c:if>
								</logic:iterate>
				</logic:notEqual>
				</td>
				</logic:equal>

			
				<logic:equal name="aimAdvancedReportForm" property="acDisbFlag" value="true">
				<td align="right" height="21" width="69">
				<logic:notEqual name="ampFund" property="disbAmount" value="0">
								<logic:iterate name="ampFund" property="byTypeDisb" id="terms">
								<c:if test="${terms.fundingTerms == termsType}">
										<bean:write name="terms"/>
								</c:if>
								</logic:iterate>
				</logic:notEqual>
				</td>
				</logic:equal>
			

				<logic:equal name="aimAdvancedReportForm" property="acExpFlag" value="true">
				<td align="right" height="21" width="69">
				<logic:notEqual name="ampFund" property="expAmount" value="0">
								<logic:iterate name="ampFund" property="byTypeExp" id="terms">
								<c:if test="${terms.fundingTerms == termsType}">
										<bean:write name="terms"/>
								</c:if>
								</logic:iterate>
				</logic:notEqual>
				</td>
				</logic:equal>
			
				<logic:equal name="aimAdvancedReportForm" property="plCommFlag" value="true">
				<td align="right" height="21" width="69">
				<logic:notEqual name="ampFund" property="plCommAmount" value="0">
								<logic:iterate name="ampFund" property="byTypePlComm" id="terms">
								<c:if test="${terms.fundingTerms == termsType}">
										<bean:write name="terms"/>
								</c:if>
								</logic:iterate>
				</logic:notEqual>
				</td>
				</logic:equal>

				<logic:equal name="aimAdvancedReportForm" property="plDisbFlag" value="true">
				<td align="right" height="21" width="69">
				<logic:notEqual name="ampFund" property="plDisbAmount" value="0">
								<logic:iterate name="ampFund" property="byTypePlDisb" id="terms">
								<c:if test="${terms.fundingTerms == termsType}">
										<bean:write name="terms"/>
								</c:if>
								</logic:iterate>
				</logic:notEqual>
				</td>
				</logic:equal>

				<logic:equal name="aimAdvancedReportForm" property="plExpFlag" value="true">
				<td align="right" height="21" width="69">
				<logic:notEqual name="ampFund" property="plExpAmount" value="0">
								<logic:iterate name="ampFund" property="byTypePlExp" id="terms">
								<c:if test="${terms.fundingTerms == termsType}">
										<bean:write name="terms"/>
								</c:if>
								</logic:iterate>
				</logic:notEqual>
				</td>
				</logic:equal>


				<logic:present name="ampFund" property="byTypeUnDisb">
				<logic:equal name="aimAdvancedReportForm" property="acBalFlag" value="true">
				<td align="right" height="21" width="69">
				<logic:notEqual name="ampFund" property="unDisbAmount" value="0">

								<logic:iterate name="ampFund" property="byTypeUnDisb" id="terms">
								<c:if test="${terms.fundingTerms == termsType}">
										<bean:write name="terms"/>
								</c:if>
								</logic:iterate>
				</logic:notEqual>
				</td>
				</logic:equal>
				</logic:present>
				

			</logic:iterate>	
		</logic:iterate>		
		</logic:equal>
		</logic:equal>
		<!-- end of by terms -->
